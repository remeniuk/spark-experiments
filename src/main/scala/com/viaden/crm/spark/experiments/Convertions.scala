package com.viaden.crm.spark.experiments

import java.util.Date
import org.joda.time.format.DateTimeFormat

/**
 * Created by remeniuk on 28.12.14.
 */
object Convertions {

  private val MillisInDay = 86400000
  val dateFormat = DateTimeFormat.forPattern("YYYY-MM-DD HH:mm:ss")

  def dateToTimestamp(date: String) = dateFormat.parseDateTime(date).getMillis / MillisInDay

  def timestampToDate(timestamp: Long) = new Date(timestamp.toLong * MillisInDay)

  abstract class NullableDatabaseValue[T](value: String, parse: String => T) {
    def fromString: T = if (value == "NULL") parse("0") else parse(value)
  }

  implicit class NullableString(value: String) {
    def toStringOrEmpty = if (value == "NULL") "Unknown" else value
  }

  implicit class NullableInt(value: String) extends NullableDatabaseValue[Int](value, _.toInt) {
    def toIntOrEmpty = fromString
  }

  implicit class NullableDouble(value: String) extends NullableDatabaseValue[Double](value, _.toDouble) {
    def toDoubleOrEmpty = fromString
  }

  implicit class NullableLong(value: String) extends NullableDatabaseValue[Long](value, _.toLong) {
    def toLongOrEmpty = fromString
  }

}
