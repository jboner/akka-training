package akka.training

import akka.actor._
import akka.actor.Actor._
import java.io._
import java.nio.channels._
import java.nio._
import akka.training.FileAppenderActor._

object FileAppenderActor {
  //Opens the supplied file, you cannot open a new file before closing the old,
  //it should only be received when a file isn't Open
  case class Open(file: File)

  //Append should only be received when a file is Open
  case class Append(text: String)

  //Close should only be received when a file is Open
  case object Close

  //WhichFile can be sent at any time, and will be responded to with CurrentFile
  case object WhichFile
  case class CurrentFile(file: Option[File])

  //This is the response to all Open, Append and Close messages that is successful
  case object OK
}

class FileAppenderActor extends Actor {
  var currentFile: Option[File] = None
  var currentChannel: Option[FileChannel] = None

  def receive = {
    case _ =>
  }
}