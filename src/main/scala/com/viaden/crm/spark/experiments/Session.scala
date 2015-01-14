package com.viaden.crm.spark.experiments

/**
 * Created by remeniuk on 12.07.14.
 */
import java.util.Date

case class Session(sessionId: String, userId: String, appId: String, timestamp: Long,
                   duration: Long, loginMode: String, deviceIds: String, deviceBrand: String,
                   deviceModel: String, platform: String, osVersion: String, appVersion: String,
                   country: String, latitude: Double, longitude: Double, userAgent: String,
                   ip: String, segmentIds: String, campaignIds: String, year: Int, month: Int, day: Int)

case class DailySessionStats(date: Date, uniqueUsersCount: Long, sessionsCount: Long,
                             avgSessionLength: Double)

