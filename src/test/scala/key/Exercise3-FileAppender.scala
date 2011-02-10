/**
 * Exercise 3 - "File it under asynchronous"
 *
 * In this exercise we will learn how to use 'become' and 'unbecome' -
 * one of the cornerstones in the Actor Model. We will also learn how
 * to create an Actor that can asynchronously write a simple log.
 *
 * Let's go!
 */

package akka.training.key

import akka.training.AkkaTrainingTest
import akka.actor._
import akka.actor.Actor._

//Gadgets from the following packages will be needed
import java.io._
import java.nio.channels._
import java.nio._

/**
 * Below is the blueprint of our soon to be FileAppenderActor,
 * this is where you will implement the behavior of the actor.
 *
 * Underneath the FileAppenderActor-class is the unit-test you will create
 * to verify
 * ...
 * To verify that your code works as expected run:
 * sbt > test-only akka.training.key.FileAppenderActorActorSpec
 */

object FileAppenderActor {
  sealed trait FileAppenderMessage
  //Opens the supplied file, you cannot open a new file before closing the old,
  //it should only be received when a file isn't Open
  case class Open(filePath: String) extends FileAppenderMessage

  //Append should only be received when a file is Open
  case class Append(text: String) extends FileAppenderMessage

  //Close should only be received when a file is Open
  case object Close extends FileAppenderMessage

  //WhichFile can be sent at any time, and will be responded to with CurrentFile
  case object WhichFile extends FileAppenderMessage
  case class CurrentFile(filePath: Option[String]) extends FileAppenderMessage

  //This is the response to all Open, Append and Close messages that is successful
  case object OK extends FileAppenderMessage
}

class FileAppenderActor extends Actor { //TODO: First, we need to make sure this is extends Actor
  import FileAppenderActor._

  //This field should have Some(File) if there is a current file, or None if there is none
  var currentFile: Option[File] = None

  //This field should have Some(FileChannel) if there is a current file, or None if there is none
  var currentChannel: Option[FileChannel] = None

  //TODO: Write a method named 'opened' that returns a Receive (PartialFunction[Any,Unit] that receives the following
  //      message types: Append, WhichFile and Close

  def opened(): Receive = {
    case Append(txt) =>
      currentChannel.get.write(ByteBuffer.wrap(txt.getBytes("UTF-8")))
      self reply_? OK

    case WhichFile =>
      self reply_? CurrentFile(currentFile.map(_.getPath))

    case Close =>
      val oldChannel = currentChannel.get
      currentChannel = None
      currentFile = None
      oldChannel.close
      unbecome
      self reply_? OK
  }

  //TODO: Write a method named 'closed' that returns a Receive (PartialFunction[Any, Unit] that receives the following
  //      message types: Open and WhichFile

  def closed(): Receive = {
    case Open(filePath) =>
      val file = new File(filePath)
      if (file.exists && file.isFile) {
        currentChannel = Some((new FileOutputStream(file,true)).getChannel)
        currentFile = Some(file)
        become(opened())
        self reply_? OK
      }

    case WhichFile =>
      self reply_? CurrentFile(None)
  }

  //TODO: Then we need to implement the receive-method so that the actor initially has the 'closed' behavior
  def receive = closed

  override def postStop {
    currentChannel.foreach( _.close )
  }
}

/**
* The following test verifies that the functionality
* of the code you've written works as expected,
* you can execute the test by typing this in sbt:
*
* test-only akka.training.key.FileAppenderActorSpec
*/

class FileAppenderActorSpec extends AkkaTrainingTest {
  import FileAppenderActor._

  //Creates a temporary file that is deleted after the test, can be useful!
  def withTempFile(fun: File => Unit) {
    val file = File.createTempFile("foo","bar")
    try {
      fun(file)
    } finally {
      file.delete
    }
  }

  "Exercise3" should {

      "teach you how to use become and unbecome to change behavior" in {
        //TODO: Create and start a FileAppenderActor ActorRef
        val actor = actorOf[FileAppenderActor].start

        //TODO: Make sure that the ActorRef does not currently support the Append or Close messages
        actor.isDefinedAt(Append("")) must be (false)
        actor.isDefinedAt(Close) must be (false)

        //TODO: Verify that the Actor supports the Open and WhichFile messages
        actor.isDefinedAt(Open("foo")) must be === true
        actor.isDefinedAt(WhichFile) must be === true

        //TODO: Test that the ActorRef initially has no open file
        (actor !! WhichFile) must be === Some(CurrentFile(None))

        //TODO: Test that the ActorRef has no open file after sending it an "Open"-message with a path that doesn't point to a file
        actor ! Open("")
        (actor !! WhichFile) must be === Some(CurrentFile(None))

        withTempFile { f =>
          //TODO: Test that the ActorRef points to the correct file after sending it an "Open"-message with a path that points to a file
          actor ! Open(f.getPath)
          (actor !! WhichFile) must equal (Some(CurrentFile(Some(f.getPath))))

          //TODO: Now verify that the Actor doesn't support the Open message
          actor.isDefinedAt(Open("foo")) must be === false

          //TODO: Then verify that the Actor now supports the Append, WhichFile and Close messages
          actor.isDefinedAt(Append("")) must be === true
          actor.isDefinedAt(Close) must be === true
          actor.isDefinedAt(WhichFile) must be === true
        }

        //TODO: Stop the ActorRef
        actor.stop
      }

      "teach you how you can make IO asynchronous with Actors" in {
        //TODO: Create and start a FileAppenderActor ActorRef
        val actor = actorOf[FileAppenderActor].start

        withTempFile { f =>
          //TODO: Tell the FileAppender to open with a correct file
          actor ! Open(f.getPath)

          //TODO: Create and start 2 EchoActor ActorRefs
          val echo1, echo2 = actorOf[EchoActor].start

          //TODO: Send 10 Append-messages with unique texts in each, and set the sender of the messages to be the FileAppender
          val messages = Set("Bonér", "Vlugter", "Ghosh", "Krasser", "Manie", "Veentjer", "Klang", "Kuhn", "Clasen", "Chirino")
          for(m <- messages) actor ! Append(m + "\n")

          //TODO: Send a Close message to the FileAppender and await it to be processed
          (actor !! Close) must be === Some(OK)

          //TODO: Stop the EchoActors and the FileAppender
          echo1.stop
          echo2.stop
          actor.stop

          //TODO: Verify that each of the 10 messages are inside the file that the FileAppender was appending to
          io.Source.fromFile(f, "UTF-8").getLines.toSet diff messages must be === Set()
        }
      }

      "BONUS: make the actor automatically clean itself up and becoming 'closed' when stopped" in {
         //See postStop callback in FileAppenderActor
      }

      "BONUS: use akka.actor.Actor.spawn to send lots of text in concurrently to the same appender" in {
        withTempFile { f =>
          val actor = actorOf[FileAppenderActor].start
          actor ! Open(f.getPath)

          for(i <- 10000 to 20000) {
            spawn {
              actor ! Append(i.toString + "\n")
            }
          }

          (actor !! Close) must be === Some(OK)
        }

      }
  }
}