package com.viaden.crm.spark.experiments

import spark.jobserver.{SparkJobInvalid, SparkJobValid, SparkJobValidation, SparkJob}
import org.apache.spark.SparkContext
import com.typesafe.config.Config
import org.apache.spark.rdd.RDD
import Convertions._
import scala.util.Try
import com.newrelic.api.agent.{NewRelic, Trace}

/**
 * Created by remeniuk on 28.12.14.
 */
object SessionFactsJob extends SparkJob {

  val SessionsPath = "sessions_path"
  val FromDate = "from_date"
  val ToDate = "to_date"

  @Trace(dispatcher = true)
  override def runJob(sc: SparkContext, jobConfig: Config): Any = {
    NewRelic.setTransactionName("Jobs", "/HourlyJob")
    registrationStats
    sessionStats
    transactionStats
    val sessionsRDD = sc.textFile(jobConfig.getString(SessionsPath))
      .map(_.split("\t")).flatMap(parseSessionTuple)
    sessionFactsTransformation(sessionsRDD, sc, jobConfig)
      .collect()
  }

  @Trace(metricName = "TransactionStats")
  def transactionStats = {
    filterOutFraudTransactions
    revenueForecast
  }

  @Trace(metricName = "SessionStats")
  def sessionStats = {
    Thread.sleep((Math.random() * 500).toLong)
  }

  @Trace(metricName = "RegistrationStats")
  def registrationStats = {
    Thread.sleep((Math.random() * 1500).toLong)
  }

  @Trace(metricName = "FilterFrauds")
  def filterOutFraudTransactions = {
    Thread.sleep((Math.random() * 100).toLong)
  }

  @Trace(metricName = "BuildRevenueReport")
  def revenueReport = {
    Thread.sleep((Math.random() * 3000).toLong)
  }

  @Trace(metricName = "MakeRevenueForecastMonthEnd")
  def revenueForecast = {
    revenueReport
    Thread.sleep((Math.random() * 3500).toLong)
  }

  override def validate(sc: SparkContext, config: Config): SparkJobValidation = Try{
    config.getString(SessionsPath)
    config.getLong(FromDate)
    config.getLong(ToDate)
  }.map { _ =>
    SparkJobValid
  }.getOrElse{
    SparkJobInvalid(s"Make sure that following parameters are specified: $SessionsPath, " +
      s"$FromDate, $ToDate")
  }

  protected def parseSessionTuple(p: Array[String]) = try {
    Some(Session(p(0), p(1), p(2), dateToTimestamp(p(3)), p(4).toLongOrEmpty, p(5), p(6), p(7), p(8), p(9),
      p(10), p(11), p(12).toStringOrEmpty, p(13).toDoubleOrEmpty, p(14).toDoubleOrEmpty, p(15), p(16), p(17), p(18),
      p(19).toIntOrEmpty, p(20).toIntOrEmpty, p(21).toIntOrEmpty))
  } catch {
    case e =>
      e.printStackTrace()
      None
  }

  def sessionFactsTransformation(sessionsRDD: RDD[Session], sc: SparkContext, config: Config): RDD[DailySessionStats] = {
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    import sqlContext._

    sessionsRDD.registerTempTable("sessions")
    sqlContext.sql(s"""
          SELECT
             timestamp, count(distinct userId) uniqueUsersCount,
             count(*) sessionsCount, sum(duration)/count(*) avgSessionLength
          FROM
             sessions
          WHERE
             timestamp > ${config.getLong(FromDate)} AND
             timestamp < ${config.getLong(ToDate)}
          GROUP BY
             timestamp
                   """)
      .map(row => DailySessionStats(timestampToDate(row.getLong(0)), row.getLong(1),
      row.getLong(2), row.getDouble(3).toInt))
  }

}
