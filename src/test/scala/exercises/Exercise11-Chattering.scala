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
import akka.training.ChatActor._

object ChatActor {
  //Convenience method to obtain a chatclient id given an alias
  def clientIdFromAlias(alias: String) = "chatclient:" + alias

  //This method gives us a reference to a ChatActor running on another machine
  def connect(alias: String, host: String, port: Int) = remote.actorFor(clientIdFromAlias(alias), host, port)

  //Starts up a local instance of our ChatActor and registers it as a server-managed remote actor
  def start(localAlias: String, host: String, port: Int) {
    remote.start(host, port)
    actorOf(new ChatActor(clientIdFromAlias(localAlias))).start
  }

  //Stops the ChatActor that is bound to the specified localAlias and shuts down the remoting
  def stop(localAlias: String) {
    registry.actorsFor(clientIdFromAlias(localAlias)).foreach(_.stop)
    remote.shutdown
  }
}

class ChatActor(id: String) extends Actor {

  self.id = id

  override def preStart = remote register self

  override def postStop = remote unregister self

  def receive = {
    case msg: String => log.slf4j.info(">>> {}", msg)
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

    }

    "teach you how to obtain references to remote actors" in {

    }

    "teach you " in {

    }
  }
}