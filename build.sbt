import spray.revolver.RevolverPlugin._

import NativePackagerKeys._

packageArchetype.java_application

name := "natural-farming-server"

version := "1.0"

scalaVersion := "2.11.2"


resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/",
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"
)

val sprayVersion = "1.3.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.6",
  "io.spray" %% "spray-routing" % sprayVersion,
  "io.spray" %% "spray-caching" % sprayVersion,
  "io.spray" %% "spray-client" % sprayVersion,
  "io.spray" %% "spray-testkit" % sprayVersion % "test",
  "io.spray" %% "spray-json" % "1.2.6", // 1.3 is not compatible with spray routing 1.3.1
  "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "org.scalatest" %% "scalatest" % "2.2.2" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test",
  "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23"
)

Revolver.settings: Seq[sbt.Def.Setting[_]]

