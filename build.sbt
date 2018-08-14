organization := "com.github.gregor-i"
name := "sbt-embedded-postgres"
version := "2.0.0-RC1"

scalaVersion in ThisBuild := "2.12.8"

sbtPlugin := true

scalacOptions ++= List("-unchecked")

publishMavenStyle := true
bintrayRepository := "maven"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

libraryDependencies += "com.opentable.components" % "otj-pg-embedded" % "0.12.6"

enablePlugins(SbtPlugin)

scriptedLaunchOpts := { scriptedLaunchOpts.value ++
  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
}

scriptedBufferLog := false
