sys.props.get("plugin.version") match {
  case Some(x) =>
    resolvers += Resolver.bintrayRepo("gregor-i", "maven")
    addSbtPlugin("com.github.gregor-i" % "sbt-embedded-postgres" % "1.3.0-SNAPSHOT")
  case _ => sys.error("""|The system property 'plugin.version' is not defined.
                         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}

libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"
