package akka.training.key

import akka.agent._

object TheCrime {
  case class Suspect(name : String)

  val suspects: Agent[Set[Suspect]] = agent(Set[Suspect]).start

  def addSuspect(suspect: Suspect): Unit = suspects send (_ + suspect)

  def removeSuspect(suspect: Suspect): Unit = suspects send (_ - suspect)

  def currentSuspects(): Set[Suspect] = suspects()

  def removeSuspects(predicate: Suspect => Boolean): Unit = suspects send (_ filterNot predicate)

  def addSuspects(suspects: Set[Suspect]): Unit = suspects send (_ ++ suspects)

  def removeAllSuspects(): Unit
}