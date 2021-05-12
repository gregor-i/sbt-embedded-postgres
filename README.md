sbt-embedded-postgres
=====================

Support for running PostgreSQL for use in integration tests.

End of Service
--------------

This project is archived. 
The JFrog Platform has sunset their free maven repository, so the released Jars are also lost.
If you have need of this library, feel free to open an issue and we will find a solution.


Installation
------------
Add the following to your `project/plugins.sbt` file:
```
resolvers += Resolver.bintrayRepo("gregor-i", "maven")
addSbtPlugin("com.github.gregor-i" % "sbt-embedded-postgres" % "2.0.0-RC1")
```

Configuration
-------------
To use the embedded postgres server, just define a dependency on `startPostgres` or `postgresConnectionString`.
This will make sbt execute `startPostgres` before starting your process. For example:
```
enablePlugins(EmbeddedPostgresPlugin)
javaOptions += s"-DDATABASE_URL=${postgresConnectionString.value}"
```

Configuration options (in `build.sbt`) and their defaults
```
postgresPort := 25432
postgresInitStatement := ""
```

If you want to run your build on a CI server, it is advised to chose a port at random to enable concurrent builds. 
This is done by setting `postgresPort := 0`. Access the chosen port by reading `postgresConnectionString`.      


Migration from 1.3.x to 2.0
---------------------------

Since 2.0 sbt-embedded-postgres no longer depends on 
[yandex-qatools/postgresql-embedded](https://github.com/yandex-qatools/postgresql-embedded) 
but instead uses
[opentable/otj-pg-embedded](https://github.com/opentable/otj-pg-embedded). 
This has a lot of benefits, ie. more stability, easier code, faster bootup time.
But it also involves some changes to the API:
* `postgresDatabase`, `postgresUsername` and `postgresPassword` are gone. 
  Instead you can use the default credential `postgres:postgres` to the database `postgres`.
  If you need to create your own user with its specific credentials you may use `postgresInitStatement`.
  For an example look into the [test](src/sbt-test/sbt-embedded-postgres/test/build.sbt).
* `postgresVersion` is gone. Currently there is no alternative.
* `postgresSilencer` is gone.

Known issues:
-------------

* `java.sql.SQLException: No suitable driver` may be thrown by applications directly started from sbt.
 This is caused by a [bug in the jdk itself](https://bugs.openjdk.java.net/browse/JDK-8146872).
 As stated in the ticket, a easy workaround is to load the postgres driver before connection via jdbc by calling `java.util.ServiceLoader.load(classOf[org.postgresql.Driver])`
