/**
 * Exercise 11 - "The Chattering"
 *
 * In this exercise we will collaborate with others to create a simple, distributed, chat.
 * We will learn how to start the remoting, how to shut it down, how to register server-managed
 * services and how to send messages to actors running on other machines.
 *
 *
 * Let's go!
 */

package akka.training.key

import akka.training.AkkaTrainingTest
import akka.actor._
import akka.actor.Actor._
import akka.event.EventHandler

sealed trait ChatMessage
case class Message(msg: String) extends ChatMessage
case object GetChatLog extends ChatMessage

class ChatActor(id: String) extends Actor {
  self.id = id
  var chatLog = Vector[String]()

  def receive = {
    case Message(msg) =>
      chatLog = chatLog :+ msg
      chatLog foreach (msg => EventHandler.info(this, "===> " + msg))
    case GetChatLog =>
      self reply chatLog
  }
}

class ChatSpec extends AkkaTrainingTest {
  "Exercise11" should {
    "teach you how to start the remote server" in {
      //TODO: start the remote server on an address of your choice
      Actor.remote.start("localhost", 2552)

      //TODO: verify that the remote server is running
      Actor.remote.isRunning must be (true)

      //TODO: verify that the remote server is running on the address you specified
      Actor.remote.address.getHostName must be ("localhost")
      Actor.remote.address.getPort must be (2552)
    }

    "teach you how to stop the remote server" in {
      //TODO: shut down the remote server
      Actor.remote.shutdown

      //TODO: verify that the remote server is not running any more
      Actor.remote.isRunning must be (false)
    }

    "teach you how register and use a remote actor" in {
      //TODO: start the remote server on an address of your choice
      Actor.remote.start("localhost", 2552)

      //TODO: register the ChatActor as a remote actor on the server
      Actor.remote.register("chat:service", actorOf(new ChatActor("chat:service")))

      //TODO: get a reference to the remote ChatActor on the client side
      val chat = Actor.remote.actorFor("chat:service", "localhost", 2552)

      //TDOO: send chat messages to chat actor
      chat ! Message("Hello guys")
      chat ! Message("How are you?")

      //TODO: get the chat log of messages
      val chatLog: Vector[String] = (chat !! GetChatLog)
        .as[Vector[String]]
        .getOrElse(throw new Exception("Couldn't get the chat log from ChatServer"))

      chatLog(0) must equal ("Hello guys")
      chatLog(1) must equal ("How are you?")

      Actor.remote.shutdown
    }
 }
}
