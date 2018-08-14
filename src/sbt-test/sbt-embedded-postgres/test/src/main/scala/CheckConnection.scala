import java.sql.Connection
import java.util.Properties

import collection.JavaConverters._

object CheckConnection {

  def check(connectionString: String) = {
    val conn: Connection = new org.postgresql.Driver().connect(connectionString, new Properties())
    val query = conn.createStatement().executeQuery("SELECT count(*) FROM pg_database where datname='database'")
    while (query.next()) assert(query.getInt("count") == 1)
    conn.close()
  }

  def main(args: Array[String]): Unit = {
    println(System.getProperties.asScala)

    val connectionString = System.getProperty("DATABASE_URL")

    println("connectionString", connectionString)

    check(connectionString)
  }

}
