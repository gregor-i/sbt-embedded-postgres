organization := "com.github.gregor-i"
name := "sbt-embedded-postgres"
version := "1.3.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.7"

sbtPlugin := true

scalacOptions ++= List("-unchecked")

publishMavenStyle := true
bintrayRepository := "maven"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

libraryDependencies ++= {
  Seq(
    "ru.yandex.qatools.embed" % "postgresql-embedded" % "2.10",
    "org.postgresql" % "postgresql" % "42.2.5"  % Test,
    "org.scalatest" %% "scalatest" % "3.0.5" % Test
  )
}
