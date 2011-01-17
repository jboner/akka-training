package akka.training.key

import akka.actor._
import akka.actor.Actor._

class EchoActor extends Actor {
  def receive = {
    case anything => self reply_? anything
  }
}