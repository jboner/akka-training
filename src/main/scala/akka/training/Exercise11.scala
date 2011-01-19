package akka.training

import akka.actor._
import akka.actor.Actor._
import akka.training.Chat._

//The idea is to have this as a group exercise, and have the participants suggest improvements and additional features
//to be implemented together and run on each individuals machine

object Chat {
  def connect(name: String, host: String, port: Int) = remote.actorFor("chatclient:"+name, host, port)
  def join(name: String, host: String, port: Int) = {
    remote.start(host, port)
    actorOf(new ChatClient(name)).start
  }
}

class ChatClient(name: String) extends Actor {

  self.id = "chatclient:"+name

  override def preStart = remote register self

  override def postStop = remote unregister self

  def receive = {
    case msg: String => log.slf4j.info(">>> {}", msg)
  }
}