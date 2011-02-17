import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val akka_repo  = "Akka Maven Repository" at "http://akka.io/repository"
  val sbtIdeaRepo = "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"
  
  val sbtIdea = "com.github.mpeltonen" % "sbt-idea-plugin" % "0.2.0"
  val akkaPlugin = "se.scalablesolutions.akka" % "akka-sbt-plugin" % "1.0"
}