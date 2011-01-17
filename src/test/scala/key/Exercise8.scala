package akka.training.key

import akka.agent._

object TheCrime {
  case class Suspect(name : String)

  val suspects: Agent[Set[Suspect]] = Agent(Set[Suspect]())

  def addSuspect(suspect: Suspect): Unit = suspects send (_ + suspect)

  def removeSuspect(suspect: Suspect): Unit = suspects send (_ - suspect)

  def currentSuspects(): Set[Suspect] = suspects()

  def removeSuspects(predicate: Suspect => Boolean): Unit = suspects send (_ filterNot predicate)

  def addSuspects(newSuspects: Set[Suspect]): Unit = suspects send (_ ++ newSuspects)

  def removeAllSuspects(): Unit = suspects() = Set()
}