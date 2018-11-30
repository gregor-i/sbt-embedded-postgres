package sbt.postgres

import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres
import ru.yandex.qatools.embed.postgresql.distribution.Version

class EmbeddedPostgresServer(
  host:String,
  port:Int,
  dbName: String,
  username: String,
  password: String,
  pgVersion: Version.Main
) {
  val pg = new EmbeddedPostgres(pgVersion)
  pg.getConfig

  def start(): Option[String] =
    if(!pg.getProcess.isPresent)
      Some(pg.start(host,port, dbName,username, password))
    else
      Option(pg.getConnectionUrl.orElse(null))

  def stop(): Unit = pg.stop()

  def isRunning: Boolean = pg.getProcess.isPresent
}
