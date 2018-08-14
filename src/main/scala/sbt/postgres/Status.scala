package sbt.postgres

import com.opentable.db.postgres.embedded.EmbeddedPostgres

sealed trait Status

case object NotStarted extends Status
case class Running(pg: EmbeddedPostgres,
                   connectionString: String) extends Status
case object Closed extends Status
