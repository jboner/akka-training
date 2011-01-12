package akka.training.key

import akka.actor._
import akka.actor.Actor._
import java.io._
import java.nio.channels._
import java.nio._
import akka.training.FileAppenderActor._

object FileAppenderActor {
  case class Open(file: File)
  case object Close
  case class Append(text: String)
  case object WhichFile
  case class CurrentFile(file: Option[File])
  case object OK
}

class FileAppenderActor extends Actor {
  var currentFile: Option[File] = None
  var currentChannel: Option[FileChannel] = None

  def opened(): Receive = {
    case Append(txt) =>
      currentChannel.get.write(ByteBuffer.wrap(txt.getBytes("UTF-8")))
      self reply_? OK

    case WhichFile =>
      self reply_? CurrentFile(currentFile)

    case Close =>
      val oldChannel = currentChannel.get
      currentChannel = None
      currentFile = None
      oldChannel.close
      unbecome
      self reply_? OK
  }

  def closed(): Receive = {
    case Open(file) =>
      currentChannel = Some((new FileOutputStream(file,true)).getChannel)
      currentFile = Some(file)
      become(opened())
      self reply_? OK

    case WhichFile =>
      self reply_? CurrentFile(None)
  }

  def receive = closed
}