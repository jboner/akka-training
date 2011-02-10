/**
 * Exercise 3 - "File it under asynchronous"
 *
 * In this exercise we will learn how to use 'become' and 'unbecome' -
 * one of the cornerstones in the Actor Model. We will also learn how
 * to create an Actor that can asynchronously write a simple log.
 *
 * Let's go!
 */

package akka.training

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
 * to verify your implementation.
 *
 * To verify that your code works as expected run:
 * sbt > test-only akka.training.FileAppenderActorActorSpec
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

class FileAppenderActor { //TODO: First, we need to make sure this is extends Actor
  import FileAppenderActor._

  //This field should have Some(File) if there is a current file, or None if there is none
  var currentFile: Option[File] = None

  //This field should have Some(FileChannel) if there is a current file, or None if there is none
  var currentChannel: Option[FileChannel] = None

  //TODO: Write a method named 'opened' that returns a Receive (PartialFunction[Any,Unit] that receives the following
  //      message types: Append, WhichFile and Close
  //


  //TODO: Write a method named 'closed' that returns a Receive (PartialFunction[Any, Unit] that receives the following
  //      message types: Open and WhichFile

  //TODO: Then we need to implement the receive-method so that the actor initially has the 'closed' behavior
}

/**
* The following test verifies that the functionality
* of the code you've written works as expected,
* you can execute the test by typing this in sbt:
*
* test-only akka.training.FileAppenderActorSpec
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

        //TODO: Make sure that the ActorRef does not currently support the Append or Close messages

        //TODO: Verify that the Actor supports the Open and WhichFile messages

        //TODO: Test that the ActorRef initially has no open file

        //TODO: Test that the ActorRef has no open file after sending it an "Open"-message with a path that doesn't point to a file

         withTempFile { f =>
        //TODO: Test that the ActorRef points to the correct file after sending it an "Open"-message with a path that points to a file

        //TODO: Now verify that the Actor doesn't support the Open message

        //TODO: Then verify that the Actor now supports the Append, WhichFile and Close messages
         }
        //TODO: Stop the ActorRef
      }

      "teach you how you can make IO asynchronous with Actors" in {
        //TODO: Create and start a FileAppenderActor ActorRef

        withTempFile { f =>
        //TODO: Tell the FileAppender to open with a correct file

        //TODO: Create and start 2 EchoActor ActorRefs

        //TODO: Send 10 Append-messages with unique texts in each, and set the sender of the messages to be the FileAppender

        //TODO: Send a Close message to the FileAppender and await it to be processed

        //TODO: Stop the EchoActors and the FileAppender

        //TODO: Verify that each of the 10 messages are inside the file that the FileAppender was appending to
        }
      }

      "BONUS: make the actor automatically clean itself up and becoming 'closed' when stopped" in {

      }

      "BONUS: use akka.actor.Actor.spawn to send lots of text in concurrently to the same appender" in {

      }
  }
}