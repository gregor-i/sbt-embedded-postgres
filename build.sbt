import scala.sys.process._

organization := "com.github.gregor-i"
name := "sbt-embedded-postgres"
ThisBuild / version := {
  Option("git tag -l --points-at HEAD".!!.trim)
    .filter(_.nonEmpty)
    .getOrElse("SNAPSHOT")
}

ThisBuild / scalaVersion := "2.12.13"

sbtPlugin := true

scalacOptions ++= List("-unchecked")

libraryDependencies += "com.opentable.components" % "otj-pg-embedded" % "0.13.3"

enablePlugins(SbtPlugin)

scriptedLaunchOpts := { scriptedLaunchOpts.value ++
  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
}

scriptedBufferLog := false
