import sbt._
import sbt.ExclusionRule
import sbt.Keys._

object BuildSettings {

  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "com.viaden.crm",
    scalaVersion := "2.10.4",
    version := "0.1-SNAPSHOT"
  )

}

object Resolvers {
  val sonatypeSnaps = "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  val sonatypeRels = "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases"
  val sonatypeSTArch = "scalaTools Archive" at "https://oss.sonatype.org/content/groups/scala-tools"
  val mavenOrgRepo = "Maven.Org Repository" at "http://repo1.maven.org/maven2/org"
  val clouderaArtifactory = "Cloudera" at "https://repository.cloudera.com/artifactory/cdh-releases-rcs"
  val clouderaPublic = "Cloudera Pub" at "https://repository.cloudera.com/artifactory/public"
  val clouderaRepos = "Cloudera Repos" at "https://repository.cloudera.com/artifactory/cloudera-repos"
  val ooyalaResolvers = "Job Server Bintray" at "http://dl.bintray.com/spark-jobserver/maven"
}

object Dependencies {

  val config = "com.typesafe" % "config" % "1.2.0"
  val jodaTime = "joda-time" % "joda-time" % "2.3"
  val specs2 = "org.specs2" % "specs2_2.10" % "2.4"
  val sparkCore = "org.apache.spark" % "spark-core_2.10" % "1.1.0-cdh5.2.1" excludeAll(
    ExclusionRule(organization = "io.netty", name = "netty-all"),
    ExclusionRule(organization = "org.jboss.netty", name = "netty")
    )
  val sparkSql = "org.apache.spark" % "spark-sql_2.10" % "1.1.0-cdh5.2.1"
  val newRelic = "com.newrelic.agent.java" % "newrelic-api" % "3.12.1"
  val jobServer = "spark.jobserver" % "job-server-api" % "0.4.0" % "provided"
}

object SparkExperimentsBuild extends Build {

  import BuildSettings._
  import Resolvers._
  import Dependencies._

  lazy val root = Project(
    "spark-experiments",
    file("."),
    settings = buildSettings ++ Seq(
      resolvers ++= Seq(sonatypeSnaps, sonatypeRels, sonatypeSTArch, ooyalaResolvers,
        mavenOrgRepo, clouderaArtifactory, clouderaPublic, clouderaRepos),
      libraryDependencies ++= Seq(
        config, specs2, sparkCore, sparkSql, newRelic, jobServer, jodaTime
      )
    )
  )

}

