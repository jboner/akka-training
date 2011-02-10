/**
 * Exercise 4 - "The Mailman is a trait-or"
 *
 * In this exercise we will learn how to use Scala traits
 * to create modular behaviors that will allow you to
 * mix in behaviors into your actors.
 * We will also learn how to pass messages around and preserve
 * the sender of a message.
 *
 * Let's go!
 */

package akka.training.key

import akka.training.AkkaTrainingTest
import akka.actor._
import akka.actor.Actor._

/**
 * Below is the blueprint of our soon to be MailmanActor and RecipientActor,
 * this is where you will implement the behavior of the actor.
 * We will also learn to use 'forward' to preserve the original
 * sender of messages.
 *
 * Underneath the RecipientActor-class is the unit-test you will create
 * to verify your implementation.
 *
 * To verify that your code works as expected run:
 * sbt > test-only akka.training.key.MailmanActorSpec
 */

case class Parcel(recipient: Uuid, contents: AnyRef)

object MailmanActor {
  sealed trait MailmanMessage
  case class RegisterRecipient(recipient: ActorRef) extends MailmanMessage
  case class DeregisterRecipient(recipient: ActorRef) extends MailmanMessage
  case class ReturnToSender(parcel: Parcel) extends MailmanMessage

  trait RegistrationBehavior { actor: Actor =>
    var registeredRecipients = Map[Uuid, ActorRef]()

    //TODO: Implement this method
    def handleRegistrations: Receive = {
      case RegisterRecipient(recipient) => registeredRecipients += recipient.uuid -> recipient
      case DeregisterRecipient(recipient) => registeredRecipients -= recipient.uuid
    }
    //RegisterRecipient messages should register recipients
    //DeregisterRecipient messages should deregister recipients

    //TODO: Implement this method
    def findRecipientFor(recipientUuid: Uuid): Option[ActorRef] = registeredRecipients get recipientUuid
  }

  trait ParcelManagement { actor: Actor =>

    def findRecipientFor(recipientUuid: Uuid): Option[ActorRef] //You will not need to implement this

    //TODO: Implement the following method
    def handleParcel: Receive = {
      case p@Parcel(recipientUuid, _) =>
        findRecipientFor(recipientUuid) match {
          case Some(r) => r forward p
          case None => actor.self reply_? ReturnToSender(p)
        }
    }
    //Parcel should be forwarded to the correct recipient (if found) else reply with a ReturnToSender
  }
}

import MailmanActor._
class MailmanActor extends Actor with RegistrationBehavior with ParcelManagement  { //TODO: Make this into an Actor that utilizes your traits
  //TODO: Implement the receive method
  def receive = handleRegistrations orElse handleParcel
}

object RecipientActor {
  sealed trait RecipientMessage
  case class ThanksForThePresent(contents: AnyRef) extends RecipientMessage
}

class RecipientActor extends Actor { //TODO: Make this an Actor
  import RecipientActor._

  //TODO: Implement receive to only receive parcels meant for this actor,
  //      and respond with a ThanksForThePresent message with the contents of the parcel
  def receive = {
    case p@Parcel(uuid,contents) if uuid == self.uuid =>
      self reply_? ThanksForThePresent(contents)
  }
}

/**
* The following test verifies that the functionality
* of the code you've written works as expected,
* you can execute the test by typing this in sbt:
*
* test-only akka.training.key.MailmanActorSpec
*/

class MailmanActorSpec extends AkkaTrainingTest {
  import MailmanActor._
  import RecipientActor._


  "Exercise4" should {
    "teach you how to route messages to other actors and preserve the sender" in {
      //TODO: Create and start a MailmanActor
      val mailman = actorOf[MailmanActor].start

      //TODO: Create and start a RecipientActor
      val aSomebody = actorOf[RecipientActor].start

      //TODO: Create a parcel, addressed to the recipient, to the mailman and make sure you get a ReturnToSender with the same parcel
      val someParcel = Parcel(aSomebody.uuid, "Something stupid")
      (mailman !! someParcel) must be === Some(ReturnToSender(someParcel))

      //TODO: Register the recipient to the mailman
      mailman ! RegisterRecipient(aSomebody)

      //TODO: Send a parcel, addressed to the recipient, to the mailman and
      // make sure you get a ThanksForThePresent back with the expected contents
      (mailman !! Parcel(aSomebody.uuid, "Something awesome")) must be === Some(ThanksForThePresent("Something awesome"))

      //TODO: Now deregister the recipient from the mailman
      mailman ! DeregisterRecipient(aSomebody)

      //TODO: Then verify that sending a parcel, addressed to the recipient, to the mailman yields the expected ReturnToSender
      (mailman !! someParcel) must be === Some(ReturnToSender(someParcel))

      //TODO: Stop both the mailman and the recipient
      mailman.stop
      aSomebody.stop
    }
  }
}