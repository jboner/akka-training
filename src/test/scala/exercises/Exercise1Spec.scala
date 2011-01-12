package akka.training.test

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, WordSpec}
import org.scalatest.matchers.MustMatchers

import akka.actor._
import akka.actor.Actor._

class Exercise1Spec extends WordSpec with MustMatchers {
   "Exercise1" should {
      "teach you how to create an instance of an actor" in {
        //Create an instance of your EchoActor and verify that it's not null
      }

      "teach you that an actor needs to be started before it can accept messages" in {
        //Create an instance of your EchoActor and start it
      }

      "teach you how to send a fire-forget message to an actor" in {
        //Create an instance of your EchoActor, start it and send it a ! message
      }

      "teach you how to send a send-receive-reply message to an actor" in {
        //Create an instance of your EchoActor, start it and send it a !! message and verify the response
      }

      "teach you that an actor needs to be stopped to be garbage collected" in {
        //Create an instance of your EchoActor, start it, then stop it, then go back to your previous tests
        //and make sure your actors are stopped at the end of the test
      }
   }
}