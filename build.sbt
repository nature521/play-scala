name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  jdbc,
  "com.typesafe.play" %% "anorm" % "2.5.0"
)
libraryDependencies += cache
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test

val classPath = Seq(
  ".",
  "./lib/sqljdbc4-4.0.jar"
)

packageOptions += Package.ManifestAttributes(
  "Class-Path" -> classPath.mkString(" ")
)
