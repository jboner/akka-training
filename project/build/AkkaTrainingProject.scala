import sbt._

class AkkaTrainingProject(info: ProjectInfo) extends DefaultProject(info) with AkkaProject {
  object Repositories {
    lazy val ScalaToolsReleases = MavenRepository("Scala Tools Releases Repo","http://scala-tools.org/repo-releases")
    lazy val AkkaReleases       = MavenRepository("Akka Repository","http://akka.io/repository/")
  }

  // dependencies with sources
  override val akkaActor = akkaModule("actor") withSources
  val akkaStm = akkaModule("stm") withSources
  val akkaTypedActor = akkaModule("typed-actor") withSources
  val akkaRemote = akkaModule("remote") withSources
  val akkaHttp = akkaModule("http") withSources

  // testing
  val scalatest = "org.scalatest" % "scalatest_2.9.0" % "1.4.1" % "test"

  // module configurations
  val scalaTestModuleConfig = ModuleConfiguration("org.scalatest", Repositories.ScalaToolsReleases)
  val akkaModuleConfig2     = ModuleConfiguration("se.scalablesolutions.akka", Repositories.AkkaReleases)
}
