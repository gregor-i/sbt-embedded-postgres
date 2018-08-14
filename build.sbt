organization := "com.github.gregor-i"
name := "sbt-embedded-postgres"
version := "1.3.0"

scalaVersion in ThisBuild := "2.12.6"

sbtPlugin := true

scalacOptions ++= List("-unchecked")

publishMavenStyle := true
bintrayRepository := "maven"
licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

libraryDependencies ++= {
  Seq(
    "ru.yandex.qatools.embed" % "postgresql-embedded" % "2.9",
    "org.postgresql" % "postgresql" % "42.2.2"  % Test,
    "org.scalatest" %% "scalatest" % "3.0.1" % Test
  )
}
