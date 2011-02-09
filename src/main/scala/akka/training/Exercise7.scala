package akka.training

import akka.actor._
import akka.actor.Actor._
import akka.routing._

class EchoLoadBalancer(noOfBackingEchoActors: Int) extends LoadBalancer {
  require(noOfBackingEchoActors > 0)
  // Implement this load balancer to balance between 'noOfBackingEchoActors' number of EchoActors
}