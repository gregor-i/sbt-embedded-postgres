organization := "com.github.gregor-i"
name := "sbt-embedded-postgres"
version := "2.0.0-RC2"

scalaVersion / ThisBuild := "2.12.13"

sbtPlugin := true

scalacOptions ++= List("-unchecked")

libraryDependencies += "com.opentable.components" % "otj-pg-embedded" % "0.13.3"

enablePlugins(SbtPlugin)

scriptedLaunchOpts := { scriptedLaunchOpts.value ++
  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
}

scriptedBufferLog := false
