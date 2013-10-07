// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

// Use the Play sbt plugin for Play projects
// AT: Update von 2.1.0 auf 2.1.1
addSbtPlugin("play" % "sbt-plugin" % "2.1.1")