package sbt.postgres

import ru.yandex.qatools.embed.postgresql.distribution.Version
import ru.yandex.qatools.embed.postgresql.distribution.Version.Main.PRODUCTION
import sbt.Keys._
import sbt._

object EmbeddedPostgresPlugin extends AutoPlugin {

  object autoImport {
    val postgresPort = settingKey[Int]("Postgres server port")
    val postgresDatabase = settingKey[String]("Postgres database name")
    val postgresUsername = settingKey[String]("Postgres username.")
    val postgresPassword = settingKey[String]("Postgres password.")
    val postgresVersion = settingKey[Version.Main]("Postgres version")

    val postgresConnectionString = taskKey[String]("Postgres connection string. This will also start the server.")
    val startPostgres = taskKey[String]("start-postgres")
    val stopPostgres = taskKey[Unit]("stop-postgres")
    val postgresServer = settingKey[EmbeddedPostgresServer]("Postgres server")
    val postgresIsRunning = taskKey[Boolean]("postgres-is-running")
  }

  import autoImport._

  def isReachable(host: String, port: Int, timeout: Int = 2000): Boolean = {
    import java.io.IOException
    import java.net.{InetSocketAddress, Socket}

    val socket = new Socket
    try {
      socket.connect(new InetSocketAddress(host, port), timeout)
      true
    } catch {
      case _: IOException => false
    } finally {
      socket.close()
    }
  }

  def getFreePort(range: IndexedSeq[Int]): Int =
    scala.util.Random.shuffle(range).find(x => !isReachable("localhost", x)).getOrElse {
      throw new RuntimeException(s"No free port available in given port range.")
    }

  def defaultSettings = Seq(
    postgresPort := 25432,
    postgresDatabase := "database",
    postgresConnectionString := startPostgres.value,
    postgresUsername := "admin",
    postgresPassword := "admin",
    postgresVersion := PRODUCTION,
    postgresServer := new EmbeddedPostgresServer(
      "localhost",
      postgresPort.value,
      postgresDatabase.value,
      postgresUsername.value,
      postgresPassword.value,
      postgresVersion.value
    )
  )

  def tasks = Seq(
    startPostgres := {
      streams.value.log.info(s"Starting Postgres ...")
      val server = postgresServer.value
      val connectionString = server.start().get
      streams.value.log.info(s"Postgres started on $connectionString")
      connectionString
    },
    stopPostgres := {
      streams.value.log.info("Stopping Postgres...")
      postgresServer.value.stop()
      streams.value.log.info("Postgres stopped")
    },
    postgresIsRunning := postgresServer.value.isRunning
  )

  override val projectSettings = defaultSettings ++ tasks
}
