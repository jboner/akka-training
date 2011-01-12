package akka.training.test

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, WordSpec}
import org.scalatest.matchers.MustMatchers

import akka.actor._
import akka.actor.Actor._
import akka.training.FileAppenderActor
import akka.training.FileAppenderActor._
import java.io. {FileNotFoundException, File}

class FileAppenderActorSpec extends WordSpec
                  with BeforeAndAfterAll
                  with BeforeAndAfterEach
                  with MustMatchers
{
  var actor: ActorRef = _

  override def beforeEach = {
    actor = actorOf[FileAppenderActor].start
  }

  "An FileAppenderActor" should {
     "be instantiable" in {
        actor must not be null
     }

     "have no initial file" in {
       (actor !! WhichFile).get must be === CurrentFile(None)
     }

     "must throw FileNotFound when target is not an existing file" in {
        intercept[FileNotFoundException] {
          actor !! Open(new File(""))
        }
     }

     "must not accept Close messages when not open" in {
        actor.isDefinedAt(Close) must be === false
     }

     "must not accept Append messages when not open" in {
        actor.isDefinedAt(Append("foo")) must be === false
     }

     "must be able to Open an existing file" in {
       withTempFile {
         file => (actor !! Open(file)).get must be === OK
       }
     }

     "must be able to Close an Open appender" in {
       withTempFile {
         file => {
           (actor !! Open(file)).get must be === OK
           (actor !! Close).get must be === OK

           actor.isDefinedAt(Close) must be === false
           actor.isDefinedAt(Open(new File("foo"))) must be === true
           actor.isDefinedAt(Append("foo")) must be === true
         }
       }
     }

     "must be able to append text to the file" in {
       withTempFile {
         file => {
           val data = List("fooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo",
                           "baaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaar",
                           "baaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaz",
                           "brrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrz",
                           "buuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuz",
                           "biiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiz")

           actor ! Open(file)

           for(d <- data)
             actor ! Append(d)

           (actor !! Close).get must be === OK

           val textWritten = io.Source.fromFile(file).getLines.mkString
           val expected = data.mkString

           textWritten must be === expected
         }
       }
     }
  }

  //Creates a temporary file that is deleted after the test
  def withTempFile(fun: File => Unit) {
    val file = File.createTempFile("foo","bar")
    try {
      fun(file)
    } finally {
      file.delete
    }
  }

  override def afterEach = {
    registry.shutdownAll
  }
}