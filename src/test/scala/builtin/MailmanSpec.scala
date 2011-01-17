package akka.training.test

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, WordSpec}
import org.scalatest.matchers.MustMatchers

import akka.actor._
import akka.actor.Actor._
import akka.training.Mailman
import akka.training.Mailman._
import MailmanSpec._
import java.util.concurrent. {TimeUnit, CountDownLatch}

object MailmanSpec {
  class Recipient(verifyParcel: (Parcel) => Unit) extends Actor {
    def receive = {
      case p: Parcel => verifyParcel(p)
    }
  }
}

class MailmanSpec extends WordSpec
                  with BeforeAndAfterAll
                  with BeforeAndAfterEach
                  with MustMatchers
{
  var actor: ActorRef = _

  override def beforeEach = {
    actor = actorOf[Mailman].start
  }

  "A Mailman" should {
     "be instantiable" in {
        actor must not be null
     }

     "reply with ReturnToSender when recipient isn't registered" in {
       val recipient = actorOf(new Recipient( p => error("Shouldn't get here!") )).start
       val parcel = Parcel(recipient.uuid, "Wooot!")
       (actor !! parcel).get must be === ReturnToSender(parcel)
     }

     "forward Parcels to the correct recipient when recipient is registered" in {
        val delivery = new CountDownLatch(1)
        val expectedMessage = "Woot!"
        val recipient = actorOf(new Recipient( p => if (p.contents == expectedMessage) delivery.countDown )).start
        actor ! RegisterRecipient(recipient)
        actor ! Parcel(recipient.uuid, expectedMessage)
        delivery.await(3, TimeUnit.SECONDS) must be === true
     }

     "reply with ReturnToSender when Parcel is delivered to deregistered recipient" in {
        val recipient = actorOf(new Recipient( p => error("Shouldn't get here!") )).start
        actor ! RegisterRecipient(recipient)
        actor ! DeregisterRecipient(recipient)
        val parcel = Parcel(recipient.uuid, "Wooot!")
        (actor !! parcel).get must be === ReturnToSender(parcel)
     }
  }

  override def afterEach = {
    registry.shutdownAll
  }
}