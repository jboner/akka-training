package akka.training

import akka.actor._
import akka.actor.Actor._
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

    //Only method that needs to be implemented
    def forwardWorkToAWorker(w: Work): Unit
  }
}

trait Wizardry { self: Actor with ForemanBehavior =>
  def conjureNewWorker(): ActorRef = null

  def forwardWorkToAWorker(w: Work) {
    //Use conjureNewWorker() and send the Work to that worker, preserving the origin of the work if any
  }
}

class WizardForeman extends Actor with ForemanBehavior with Wizardry