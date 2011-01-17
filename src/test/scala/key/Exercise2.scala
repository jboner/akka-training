package akka.training.key

import akka.actor._
import akka.actor.Actor._
import akka.training.AccumulatorActor._

object AccumulatorActor {
  case class IncrementBy(num: BigInt)
  case object Sum
}

class AccumulatorActor extends Actor {
  var sum = BigInt(0)
  def receive = {
    case IncrementBy(num) =>
      if (num >= 0)
        sum += num
    case Sum =>
      self reply_? sum
  }
}