import scala.sys.process._

organization := scala.sys.env.get("GROUP").getOrElse("com.github.gregor-i")
name := scala.sys.env.get("ARTIFACT").getOrElse("sbt-embedded-postgres")
ThisBuild / version := scala.sys.env.get("VERSION").getOrElse("SNAPSHOT")

ThisBuild / scalaVersion := "2.12.13"

sbtPlugin := true

scalacOptions ++= List("-unchecked")

libraryDependencies += "com.opentable.components" % "otj-pg-embedded" % "0.13.3"

enablePlugins(SbtPlugin)

scriptedLaunchOpts := { scriptedLaunchOpts.value ++
  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
}

scriptedBufferLog := false
