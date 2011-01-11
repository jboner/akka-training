import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val akka_repo  = "Akka Maven Repository" at "http://akka.io/repository"
  val akkaPlugin = "se.scalablesolutions.akka" % "akka-sbt-plugin" % "1.0-RC3"
}