import play.Project._

import net.litola.SassPlugin

name := "playconf"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "com.typesafe.slick" %% "slick" % "1.0.0",
  "mysql" % "mysql-connector-java" % "5.1.30",
  "org.webjars" % "bootstrap" % "3.1.1-1"
)     

play.Project.playJavaSettings ++ SassPlugin.sassSettings

