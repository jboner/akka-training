/**
 * Exercise 1 - "Echoes in the night"
 *
 * In this exercise we will learn how to create our first Akka Actor,
 * the "EchoActor", the purpose of this Actor is to echo back incoming messages
 * to whomever sent the message. If there is way to reply, it should do nothing.
 *
 * We will learn how to create Actors, start them, receive messages and reply with a response.
 * On top of that we will learn how to write unit tests using ScalaTest!
 *
 * Let's go!
 */

package akka.training.key

import akka.training.AkkaTrainingTest
import akka.actor._
import akka.actor.Actor._

/**
 * Below is the blueprint of our soon to be EchoActor,
 * this is where you will implement the behavior of the actor.
 *
 * Underneath the EchoActor-class is the unit-test you will create
 * to verify your implementation.
 *
 * To verify that your code works as expected run:
 * sbt > test-only akka.training.key.EchoActorSpec
 */

class EchoActor extends Actor { //TODO: First, we need to make sure this is extends Actor
  //TODO: Then we need to implement the receive-method
  def receive = {
    case anything => self reply_? anything
  }
}

/**
* The following test verifies that the functionality
* of the code you've written works as expected,
* you can execute the test by typing this in sbt:
*
* test-only akka.training.key.EchoActorSpec
*/

class EchoActorSpec extends AkkaTrainingTest {
  "Exercise 1" should {
    "teach you how to create, start and stop an actor" in {
      //TODO: Create an ActorRef pointing to an instance of your EchoActor
      val actor = actorOf[EchoActor]

      //TODO: Start the ActorRef
      actor.start

      //TODO: Make sure that the ActorRef is non-null and is running
      actor must not be null
      actor.isRunning must be === true

      //TODO: Stop the ActorRef and make sure that it is stopped
      actor.stop
      actor.isShutdown must be === true
    }

    "teach you how to reply to the first message" in {
      //TODO: Create an ActorRef pointing to an instance of your EchoActor and start it
      val actor = actorOf[EchoActor].start

      //TODO: Send the ActorRef a String message using !! and make sure you get the expected response
      (actor !! "ping") must be === Some("ping")

      //TODO: Stop the ActorRef
      actor.stop


    }

    "teach you what happens when you send a message to a stopped actor" in {
      //TODO: Create an ActorRef pointing to an instance of your EchoActor
      val actor = actorOf[EchoActor]

      //TODO: Send the ActorRef a String message using !!! and make sure you get the expected response
      intercept[ActorInitializationException] {
        (actor !!! "ping").await.result
      }
    }

    "teach you how to echo back messages consistently" in {
      //TODO: Create an ActorRef pointing to an instance of your EchoActor and start it
      val actor = actorOf[EchoActor].start

      //TODO: Send a lot of different kinds of String messages to the ActorRef and make sure you consistently get the expected response
      val r = new java.util.Random
      for(i <- 1 to 100) {
         val msg = r.nextInt.toString
         (actor !! msg) must be === Some(msg)
      }

      //TODO: Stop the ActorRef
      actor.stop
    }

    "teach you how to handle the absense of a sender" in {
      //TODO: Create an ActorRef pointing to an instance of your EchoActor and start it
      val actor = actorOf[EchoActor].start

      //TODO: Send a String message to the ActorRef that doesn't have any sender
      actor ! "ping"

      //TODO: Send another String message to the ActorRef and make sure you get the expected response
      (actor !! "ping") must be === Some("ping")

      //TODO: Stop the ActorRef
      actor.stop
    }

    "teach you how to respond with different kinds of responses" in {
      //TODO: Create an ActorRef pointing to an instance of your EchoActor and start it
      val actor = actorOf[EchoActor].start

      //TODO: Send a lot of different kinds of messages (numbers, null, objects etc)
      //      to the ActorRef and make sure you consistently get the expected response
      (actor !! 5) must be === Some(5)
      (actor !! null) must be === Some(null)
      (actor !! true) must be === Some(true)
      val obj = new java.lang.Object
      (actor !! obj) must be === Some(obj)

      //TODO: Stop the ActorRef
      actor.stop
    }
  }
}