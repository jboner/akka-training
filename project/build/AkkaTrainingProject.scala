import sbt._

class AkkaTrainingProject(info: ProjectInfo) extends DefaultProject(info) with AkkaProject {
  object Repositories {
    lazy val scalaToolsReleases = MavenRepository("Scala Tools Releases Repo","http://scala-tools.org/repo-releases")
    lazy val akkaReleases       = MavenRepository("Akka Repository","http://akka.io/repository/")
  }

  val akkaStm = akkaModule("stm")
  val akkaTypedActor = akkaModule("typed-actor")
  val akkaRemote = akkaModule("remote")
  val akkaHttp = akkaModule("http")
//val akkaAmqp = akkaModule("amqp")
//val akkaCamel = akkaModule("camel")
//val akkaJta = akkaModule("jta")
//val akkaKernel = akkaModule("kernel")
//val akkaCassandra = akkaModule("persistence-cassandra")
//val akkaMongo = akkaModule("persistence-mongo")
//val akkaRedis = akkaModule("persistence-redis")
//val akkaSpring = akkaModule("spring")
  import Repositories._
  val scalaTestModuleConfig   = ModuleConfiguration("org.scalatest",scalaToolsReleases)
  val akkaModuleConfig        = ModuleConfiguration("se.scalablesolutions.akka",akkaReleases)

  //Testing
   lazy val junit          = "junit"                  % "junit"               % "4.5"             % "test" //Common Public License 1.0
   lazy val scalatest      = "org.scalatest"          % "scalatest"           % "1.2"             % "test" //ApacheV2
   lazy val specs          = "org.scala-tools.testing" %% "specs"             % "1.6.6"           % "test" //MIT
}