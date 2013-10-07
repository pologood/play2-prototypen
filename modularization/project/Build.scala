import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "modularization"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean
  )
  
  val common = play.Project(
    "common", appVersion, appDependencies, path = file("modules/common")
  ).settings(
    // Add your own project settings here
  )
  
  
  val website = play.Project(
    "website", appVersion, appDependencies, path = file("modules/website")
  ).settings(
    // Add your own project settings here      
  ).dependsOn(
      common
  )

  val admin = play.Project(
    "admin", appVersion, appDependencies, path = file("modules/admin")
  ).settings(
    // Add your own project settings here      
  ).dependsOn(
      common
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  ).dependsOn(
      admin, website
  ).aggregate(
      admin, website
  )

}
