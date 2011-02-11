/**
 * Exercise 5 - "Magical and balanced"
 *
 * In this exercise we will use mixin composition
 * and learn to create actors within actors as well
 * as route messages.
 *
 * Let's go!
 */

package akka.training

import akka.actor._
import akka.actor.Actor._
import akka.routing._
import JobRelated._

object JobRelated {
  //Defines a Task to be performed, that yields a Result
  trait Task { def perform(): Result }

  //Represents the result of a Task
  trait Result

  //Represents a Work-request to the Foreman
  case class Work(task: Task)

  //A Worker performs Tasks and sends back the result
  class Worker extends Actor {
    def receive = {
      case t: Task => self reply_? t.perform()
    }
  }

  //Using a trait to implement the default behavior of a Foreman,
  trait ForemanBehavior { self: Actor =>
    def receive: Receive = {
      case w: Work => forwardWorkToAWorker(w)
    }

    //Don't implement this here, this is a part of the external contract for ForemanBehavior
    def forwardWorkToAWorker(w: Work): Unit
  }
}

trait Wizardry { self: Actor with ForemanBehavior =>
  def conjureNewWorker(): ActorRef = null

  def forwardWorkToAWorker(w: Work) {
    //Use conjureNewWorker() and send the Work to that worker, preserving the origin of the work, if any
  }
}

trait Loadbalancing { self: Actor with ForemanBehavior =>
  lazy val workers: InfiniteIterator[ActorRef] = null //TODO create a fitting InfiniteIterator with Workers

  def forwardWorkToAWorker(w: Work) {
    //TODO: forward the incoming work to one of your workers, preserving the origin of the work, if any
  }
}

//This is our wizard foreman, he can conjure new workers out of thin air and send work to them
class WizardForeman extends Actor with ForemanBehavior with Wizardry

//This is our loadbalancing foreman, he will distribute work in a balanced way to a pool of workers
class LoadbalancingForeman extends Actor with ForemanBehavior with Loadbalancing

class ForemanSpec extends AkkaTrainingTest {
  "Exercise5" should {
    "teach you how to" in {

    }
  }
}