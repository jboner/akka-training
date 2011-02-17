import sbt._

class AkkaTrainingProject(info: ProjectInfo) extends DefaultProject(info) with AkkaProject {
  object Repositories {
    lazy val scalaToolsReleases = MavenRepository("Scala Tools Releases Repo","http://scala-tools.org/repo-releases")
    lazy val akkaReleases       = MavenRepository("Akka Repository","http://akka.io/repository/")
  }

  akkaActor withSources
  val akkaStm = akkaModule("stm") withSources
  val akkaTypedActor = akkaModule("typed-actor") withSources
  val akkaRemote = akkaModule("remote") withSources
  val akkaHttp = akkaModule("http") withSources
//val akkaAmqp = akkaModule("amqp") withSources
//val akkaCamel = akkaModule("camel") withSources
//val akkaJta = akkaModule("jta") withSources
//val akkaKernel = akkaModule("kernel") withSources
//val akkaCassandra = akkaModule("persistence-cassandra") withSources
//val akkaMongo = akkaModule("persistence-mongo") withSources
//val akkaRedis = akkaModule("persistence-redis") withSources
//val akkaSpring = akkaModule("spring") withSources
  import Repositories._
  val scalaTestModuleConfig   = ModuleConfiguration("org.scalatest",scalaToolsReleases)
  val akkaModuleConfig2        = ModuleConfiguration("se.scalablesolutions.akka",akkaReleases)

  //Testing
   lazy val junit          = "junit"                  % "junit"               % "4.5"             % "test" //Common Public License 1.0
   lazy val scalatest      = "org.scalatest"          % "scalatest"           % "1.2"             % "test" //ApacheV2
   lazy val specs          = "org.scala-tools.testing" %% "specs"             % "1.6.6"           % "test" //MIT
}