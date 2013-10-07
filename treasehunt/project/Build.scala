import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "treasehunt"
  val appVersion      = "0.1-SNAPSHOT"

  val appDependencies = Seq(
//    "be.objectify"  %  "deadbolt-java_2.10" % "2.0-SNAPSHOT",
//    "be.objectify"  %  "deadbolt-java"      % "2.1-SNAPSHOT",
    "be.objectify"  %%  "deadbolt-java"      % "2.1-RC2",
//    "be.objectify"  %  "deadbolt-java"      % "2.1-07022012",
//    "be.objectify" %% "deadbolt-core" % "2.1-RC2",
    "com.feth"      %%  "play-authenticate"  % "0.2.5-SNAPSHOT",
    "postgresql"    %   "postgresql"        % "9.1-901-1.jdbc4",
    javaCore,
    javaJdbc,
    javaEbean
  )
  
  
  val common = play.Project(
    appName + "-common", appVersion, appDependencies, path = file("modules/common")
  ).settings(
      
  )

  
  val website = play.Project(
    appName + "-website", appVersion, appDependencies, path = file("modules/website")
  ).settings(
    
    
      
  ).dependsOn(
      common
  ).aggregate(
      common 
  )

  val admin = play.Project(
    appName + "-admin", appVersion, appDependencies, path = file("modules/admin")
  ).settings(
    
    
      
  ).dependsOn(
      common
  ).aggregate(
      common 
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
      
    resolvers += "Local Play Repository" at "file:///D:/DEVEL/ide/play-2.1.0/repository",
      
    // must be build locally (clean, publish-local)
//    resolvers += Resolver.url("Objectify Play Repository (release)", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns),
//    resolvers += Resolver.url("Objectify Play Repository (snapshot)", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns),
      
//    resolvers += Resolver.url("play-authenticate (release)", url("http://joscha.github.com/play-authenticate/repo/releases/"))(Resolver.ivyStylePatterns),
//    resolvers += Resolver.url("play-authenticate (snapshot)", url("http://joscha.github.com/play-authenticate/repo/snapshots/"))(Resolver.ivyStylePatterns),
    
//    resolvers += Resolver.url("play-easymail (release)", url("http://joscha.github.com/play-easymail/repo/releases/"))(Resolver.ivyStylePatterns),
//    resolvers += Resolver.url("play-easymail (snapshot)", url("http://joscha.github.com/play-easymail/repo/snapshots/"))(Resolver.ivyStylePatterns)
      
    // prevent from forking tests (Debug Tests via IDE)
    sbt.Keys.fork in Test := false
    
  ).dependsOn(
      website, admin
  ).aggregate(
      website, admin 
  )
  
}
