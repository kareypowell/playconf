name := "playconf"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "mysql" % "mysql-connector-java" % "5.1.30",
  "org.webjars" % "bootstrap" % "3.1.1-1"
)     

play.Project.playJavaSettings
