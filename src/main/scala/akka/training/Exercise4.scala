package akka.training

import akka.actor._
import akka.actor.Actor._
import akka.actor.Uuid
import Mailman._

object Mailman {
  case class RegisterRecipient(recipient: ActorRef)
  case class DeregisterRecipient(recipient: ActorRef)
  case class Parcel(recipient: Uuid, contents: AnyRef)
  case class ReturnToSender(parcel: Parcel)

  trait RegistrationBehavior { actor: Actor =>
    def handleRegistrations: Receive = {
      case _ =>
    }
  }

  trait ParcelManagement { actor: Actor =>
    def findRecipientFor(recipientUuid: Uuid): Option[ActorRef] = None /* Implement */
    def handleParcel: Receive = {
      case _ =>
    }
  }
}

class Mailman extends Actor with RegistrationBehavior with ParcelManagement {
  def receive = {
    case _ =>
  }
}