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

package akka.training

import akka.actor._
import akka.actor.Actor._

sealed trait ChatMessage
case class Message(msg: String) extends ChatMessage
case object GetChatLog extends ChatMessage

class ChatActor(id: String) extends Actor {
  self.id = id
  var chatLog = Vector[String]()

  def receive = {
    case Message(msg) =>
      //TODO: append the message to the chatLog

      //TODO: (optional) print out all the messages so far

    case GetChatLog =>
      //TODO: reply with the chatLog
  }
}

class ChatSpec extends AkkaTrainingTest {
  "Exercise11" should {
    "teach you how to start the remote server" in {
      //TODO: start the remote server on an address of your choice

      //TODO: verify that the remote server is running

      //TODO: verify that the remote server is running on the address you specified
    }

    "teach you how to stop the remote server" in {
      //TODO: shut down the remote server

      //TODO: verify that the remote server is not running any more
    }

    "teach you how register and use a remote actor" in {
      //TODO: start the remote server on an address of your choice

      //TODO: register the ChatActor as a remote actor on the server

      //TODO: get a reference to the remote ChatActor on the client side
      //val chat: ActorRef = ..

      // Uncomment when you have the chat actor
      //chat ! Message("Hello guys")
      //chat ! Message("How are you?")

      //TODO: get the chat log of messages
      //val chatLog: Vector[String] = ...

      // Uncomment when you have the chatLog
      //chatLog(0) must equal ("Hello guys")
      //chatLog(1) must equal ("How are you?")

      Actor.remote.shutdown
    }
 }
}
