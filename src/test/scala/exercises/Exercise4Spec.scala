package akka.training.test

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, WordSpec}
import org.scalatest.matchers.MustMatchers

import akka.actor._
import akka.actor.Actor._

class Exercise4Spec extends WordSpec with MustMatchers {
   "Exercise4" should {
      "teach you how to use mixin composition (traits) to compose actor behavior" in {
        //Write some code to experiment with your newly created mailman
      }

      "BONUS: devise a way to handle when a recipient has been stopped when attempting to dispatch Parcel to it" in {
        //Modify your code to handle the situation where the recipient has been stopped, you decide what's appropriate!
      }
   }
}