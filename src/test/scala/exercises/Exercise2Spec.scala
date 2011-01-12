package akka.training.test

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, WordSpec}
import org.scalatest.matchers.MustMatchers

import akka.actor._
import akka.actor.Actor._

class Exercise2Spec extends WordSpec with MustMatchers {
   "Exercise2" should {
      "teach you how to handle multiple types of messages in an actor" in {
        //Write some code to try out sending the different messages the AccumulatorActor expects
      }

      "teach you how to manage state inside an actor" in {
        //Write some manipulate the internal state of the actor using messages
      }
   }
}