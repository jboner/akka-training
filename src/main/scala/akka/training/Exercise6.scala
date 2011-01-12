package akka.training

import akka.actor._
import akka.actor.Actor._
import akka.routing._
import JobRelated._


class BalancedForeman(numWorkers: Int) extends Actor with LoadBalancer {
  val seq = new CyclicIterator[ActorRef](
    for(i <- (0 to numWorkers).toList) yield actorOf[Worker].start
  )
}