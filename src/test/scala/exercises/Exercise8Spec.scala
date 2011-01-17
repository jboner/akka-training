package akka.training.test

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, WordSpec}
import org.scalatest.matchers.MustMatchers

import akka.actor._
import akka.actor.Actor._

class Exercise8Spec extends WordSpec with MustMatchers {
   "Exercise5" should {
      "teach you how to create Agents and interact with them" in {
        //Create and agent and interact with it
      }

      "teach you why you shouldn't use mutable data inside the Agent" in {
        //Go creative and try to figure out what harm can be done by having mutable state inside the Agent
      }

      "teach you that Agents are STM-aware, so you can use them inside atomic blocks" in {
        //Create a couple of agents and surround them in atomic-blocks to see how they can participate in the transaction
      }

      "teach you that Agents reads do not block writes" in {
        //Create an Agent and send it a function that blocks until you want it to progress, then read the agents value
      }

      "BONUS: Discover how you can use Agent's monadic methods (map, filter, flatMap, foreach) in for-comprehensions" in {
        //Create an Agent and tinker with using it since a for-comprehension
      }
   }
}