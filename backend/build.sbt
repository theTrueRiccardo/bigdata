name := "taxi-trajectory-analytics"
organization := "com.riccardo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

// This is the maximum version that Spark can handle
scalaVersion := "2.13.18"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test
val sparkVersion = "4.2.0"
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion
)
libraryDependencySchemes += "org.scala-lang.modules" %% "scala-parser-combinators" % VersionScheme.Always

// for formatting the code writing sbt format
addCommandAlias("format", "scalafmtAll; scalafmtSbt")
