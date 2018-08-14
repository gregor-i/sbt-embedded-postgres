import java.sql.Connection
import java.util.Properties

version := "0.1"
scalaVersion in ThisBuild := "2.12.7"

enablePlugins(EmbeddedPostgresPlugin)

val checkConnection = taskKey[Unit]("checking the postgresConnectionString")

checkConnection := {
  val conn: Connection = new org.postgresql.Driver().connect(postgresConnectionString.value, new Properties())
  val query = conn.createStatement().executeQuery("SELECT count(*) FROM pg_database where datname='database'")
  assert(query.next())
  assert(query.getInt("count") == 1)
  assert(!query.next())
  conn.close()
}

postgresInitStatement := """CREATE ROLE "admin"
                           |  WITH PASSWORD 'admin'
                           |  SUPERUSER
                           |  LOGIN;
                           |
                           |CREATE DATABASE "database"
                           |  WITH OWNER = "admin";""".stripMargin
