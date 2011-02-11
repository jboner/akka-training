

package akka.training

import akka.agent.Agent
case class Suspect(name : String)

class Crime {
  val suspects: Agent[Set[Suspect]] = null /*Add the agent here*/

  def addSuspect(suspect: Suspect): Unit = {

  }

  def removeSuspect(suspect: Suspect): Unit = {

  }

  def currentSuspects(): Set[Suspect] = {
    null
  }

  def removeSuspects(predicate: Suspect => Boolean): Unit = {

  }

  def addSuspects(newSuspects: Set[Suspect]): Unit = {

  }

  def removeAllSuspects(): Unit = {

  }
}


class Exercise8Spec extends AkkaTrainingTest {
   "Exercise5" should {
      "teach you how to create Agents and interact with them" in {
        //TODO: Create an instance of Crime

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

/**
 * object TheCrime {
  case class Suspect(name : String)

  val suspects: Agent[Set[Suspect]] = Agent(Set[Suspect]())

  def addSuspect(suspect: Suspect): Unit = suspects send (_ + suspect)

  def removeSuspect(suspect: Suspect): Unit = suspects send (_ - suspect)

  def currentSuspects(): Set[Suspect] = suspects()

  def removeSuspects(predicate: Suspect => Boolean): Unit = suspects send (_ filterNot predicate)

  def addSuspects(newSuspects: Set[Suspect]): Unit = suspects send (_ ++ newSuspects)

  def removeAllSuspects(): Unit = suspects() = Set()
}
 */
