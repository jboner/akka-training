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
