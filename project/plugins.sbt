scalaVersion := "2.10.4"

resolvers ++= Seq(
  "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "sbt-github-repo" at "http://sbt-github-repo.fever.ch"
)

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.4")