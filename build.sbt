import com.typesafe.sbt.packager.docker.Cmd

name := "cluster-watcher"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, DockerPlugin)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

dockerRepository := Some("docker-local.artifactory.virtuefusion.corp/virtuefusion")

dockerBaseImage := "docker-local.artifactory.virtuefusion.corp/virtuefusion/vf_rhel7-java:1.7.0"

bashScriptExtraDefines ++= IO.readLines(baseDirectory.value / "scripts" / "env.sh")

javaOptions += "-Djava.rmi.server.hostname=127.0.0.1"
javaOptions += "-Dcom.sun.management.jmxremote.port=9090"
javaOptions += "-Dcom.sun.management.jmxremote.rmi.port=9090"
javaOptions += "-Dcom.sun.management.jmxremote.authenticate=false"
javaOptions += "-Dcom.sun.management.jmxremote.ssl=false"
javaOptions += " -Dcom.sun.management.jmxremote=true"
javaOptions += " -Dcom.sun.management.jmxremote"
