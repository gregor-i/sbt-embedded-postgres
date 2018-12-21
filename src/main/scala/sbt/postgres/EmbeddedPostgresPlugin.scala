package sbt.postgres

import java.util.concurrent.atomic.AtomicReference

import com.opentable.db.postgres.embedded.EmbeddedPostgres
import sbt.Keys._
import sbt._

object EmbeddedPostgresPlugin extends AutoPlugin {

  object autoImport {
    val postgresPort = settingKey[Int]("Postgres server port")
    val postgresInitStatement = settingKey[String]("Postgres initiation sql statement. "+
      "Usable to create users and databases.")

    val postgresStatus = settingKey[AtomicReference[Status]]("Postgres status")

    val postgresConnectionString = taskKey[String]("Postgres connection string. This will also start the server.")
    val startPostgres = taskKey[Running]("start postgres")
    val stopPostgres = taskKey[Closed.type]("stop postgres")
  }

  import autoImport._

  def defaultSettings = Seq(
    postgresPort := 25432,
    postgresInitStatement := "",
    postgresStatus := new AtomicReference(NotStarted)
  )

  def tasks = Seq(
    startPostgres := {
      postgresStatus.value.get() match {
        case NotStarted | Closed =>
          streams.value.log.info(s"Starting Postgres...")

          val pg = EmbeddedPostgres.builder()
            .setPort(postgresPort.value)
            .start()

          streams.value.log.info{
            if(postgresInitStatement.value != "")
              s"Initializing DB with: \n${postgresInitStatement.value}"
            else
              ""
          }
          if(postgresInitStatement.value != ""){
            val con = pg.getPostgresDatabase.getConnection
            con.createStatement().execute(postgresInitStatement.value)
            con.close()
          }

          val connectionString = pg.getJdbcUrl("postgres", "postgres")
          val running = Running(pg, connectionString)
          postgresStatus.value.set(running)
          streams.value.log.info(s"Postgres started on $connectionString")
          running

        case running: Running => running
      }
    },
    stopPostgres := {
      postgresStatus.value.get() match {
        case Closed         => Closed
        case NotStarted     => Closed
        case Running(pg, _) =>
          streams.value.log.info("Stopping Postgres...")
          pg.close()
          postgresStatus.value.set(Closed)
          Closed
      }
    },
    postgresConnectionString := startPostgres.value.connectionString
  )

  override val projectSettings = defaultSettings ++ tasks
}
