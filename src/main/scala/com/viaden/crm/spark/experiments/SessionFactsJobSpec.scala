package com.viaden.crm.spark.experiments

import org.specs2.mutable.Specification
import org.apache.spark.{SparkContext, SparkConf}
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._

/**
 * Created by remeniuk on 28.12.14.
 */
class SessionFactsJobSpec extends Specification {

  "Session facts job" should {
    "overwrite aggregates, if they already exist" in {
      val sparkConf = new SparkConf().setAppName("SessionFacts").setMaster("local[*]")
      val sc = new SparkContext(sparkConf)

      val config = ConfigFactory.parseMap(Map(
        SessionFactsJob.FromDate -> "0",
        SessionFactsJob.ToDate -> Integer.MAX_VALUE.toString,
        SessionFactsJob.SessionsPath -> "sessions.tsv"
      ).asJava)

      while(true){
        SessionFactsJob.runJob(sc, config)
        Thread.sleep(2000)
      }

      SessionFactsJob.runJob(sc, config)
        .asInstanceOf[Array[DailySessionStats]] ===
        Array(DailySessionStats(Convertions.dateFormat.parseDateTime("2014-10-30 03:00:00").toDate, 1, 3, 12941))
    }
  }

}
