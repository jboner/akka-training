package akka.training.key

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
    var registeredRecipients = Map[Uuid, ActorRef]()

    def findRecipientFor(recipientUuid: Uuid): Option[ActorRef] = registeredRecipients get recipientUuid

    def handleRegistrations: Receive = {
      case RegisterRecipient(recipient) => registeredRecipients += recipient.uuid -> recipient
      case DeregisterRecipient(recipient) => registeredRecipients -= recipient.uuid
    }
  }

  trait ParcelManagement { actor: Actor =>
    def findRecipientFor(recipientUuid: Uuid): Option[ActorRef]

    def handleParcel: Receive = {
      case p@Parcel(recipientUuid, _) =>
        findRecipientFor(recipientUuid) match {
          case Some(r) => r forward p
          case None => actor.self reply_? ReturnToSender(p)
        }
    }
  }
}

class Mailman extends Actor with RegistrationBehavior with ParcelManagement {
  def receive = handleRegistrations orElse handleParcel
}