/**
 * Exercise 5 - "Magical and balanced"
 *
 * In this exercise we will use mixin composition
 * and learn to create actors within actors as well
 * as route messages to achieve a load-balancing effect
 * for a set of actors.
 *
 * Let's go!
 */

package akka.training.key

import akka.actor._
import akka.actor.Actor._
import akka.event.EventHandler
import akka.training.AkkaTrainingTest
import akka.routing._
import java.io.Serializable
import JobRelated._

object JobRelated {
  //Defines a Task to be performed, that returns Any
  trait Work extends Serializable { def perform(): Any }

  //A Work definition that returns the sum of the supplied numbers
  case class SumSequence(numbers: Seq[BigInt]) extends Work {
    def perform() = numbers.sum
  }

  //This message tells a Worker that he should stop
  case object StopWorking

  //A Worker performs Tasks and sends back the result
  class Worker extends Actor {
    def receive = {
      case w: Work =>
        EventHandler.info(this, "%s starts to perform work of type %s" format (self, w.getClass.getSimpleName))
        self reply_? w.perform()
        EventHandler.info(this, "%s stops to perform work of type %s" format (self, w.getClass.getSimpleName))
      case StopWorking => self.stop
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

  //It must be wizardry to be able to ocnjure new actors out of thin air
  trait Wizardry { self: Actor with ForemanBehavior =>
    def conjureNewWorker(): ActorRef = //TODO: Implement this to create and start a Worker
      actorOf[Worker].start

    def forwardWorkToAWorker(w: Work) {
      //Use conjureNewWorker() and send the Work to that worker, preserving the origin of the work, if any
      val worker = conjureNewWorker
      worker forward w
      //TODO: Then tell the worker to stop working
      worker ! StopWorking
    }
  }

  //Loadbalancing certainly has its uses!
  trait Loadbalancing { self: Actor with ForemanBehavior =>
    //This shouldn't be implemented here, it should be supplied by the class mixing this trait in
    def numberOfWorkers: Int

    //TODO: create something that will hold workers to be forwarded to in a load-balanced fashion
    lazy val workers = new CyclicIterator[ActorRef]( (1 to numberOfWorkers).map(_ => actorOf[Worker].start).toList )

    def forwardWorkToAWorker(w: Work) {
      //TODO: forward the incoming work to one of your workers, preserving the origin of the work, if any
      workers.next forward w
    }
  }
}

//This is our wizard foreman, he can conjure new workers out of thin air and send work to them
class WizardForeman extends Actor with ForemanBehavior with Wizardry

//This is our loadbalancing foreman, he will distribute work in a balanced way to a pool of workers
//TODO: Make it possible to specify number of workers when creating a LoadbalancingForeman and make
//it a regular class instead of an abstract class
class LoadbalancingForeman(val numberOfWorkers: Int) extends Actor with ForemanBehavior with Loadbalancing


/**
* The following test verifies that the functionality
* of the code you've written works as expected,
* you can execute the test by typing this in sbt:
*
* test-only akka.training.key.ForemanSpec
*/

class ForemanSpec extends AkkaTrainingTest {
  //This method creates a new _anonymous_ actor that can be used to collect responses
  //We will poll this for the correct results
  def createVerifier() = Some(actorOf(
    new Actor {
      var responses: List[(ActorRef, Any)] = Nil
      def receive = {
        case 'Summary => self reply responses
        case other => responses ::= (self.sender.getOrElse(null), other)
      }
    }
  ).start)

  //A utility method that retries a specified thunk until the timeout while sleeping X millis
  //between each retry.
  //You may use this if you want or need to
  def within(timeoutMs: Long, sleepIntervalMs: Long = 200)(condition: => Boolean) {
    val deadline = System.currentTimeMillis + timeoutMs
    while(System.currentTimeMillis < deadline) {
      if(condition) return
      else Thread.sleep(sleepIntervalMs)
    }

    sys.error("Test failed")
  }

  "Exercise5" should {
    "teach you how to create new actors inside other actors" in {
      //This is an implicit Option[ActorRef], and will be used as sender for any ! sent in this context
      implicit val verifier = createVerifier()
      //TODO: Create and start a WizardForeman
      val foreman = actorOf[WizardForeman].start

      //TODO: Send N number of SumSequence work to the foreman (using !),
      //      write a test that verifies that all work was repsonded to,
      //      responses were from unique worker actors,
      //      meaning that the wizard created new ones for each workload.
      //      Also verify that all the results came back as expected.
      val numbers = BigInt(10000) to BigInt(1500000)
      val work = SumSequence(numbers)
      val expectedResult = work.perform()

      for(i <- 1 to 10) foreman ! work

      within(10000) {
        (verifier.get !! 'Summary) match {
          case Some(result: List[(ActorRef,Any)]) =>
            result.map(_._1).distinct.size == 10 && result.map(_._2).forall(expectedResult ==)
          case _ => false
        }
      }
    }

    "teach you to how to load-balance work between a finite amount of actors" in {
      //This is an implicit Option[ActorRef], and will be used as sender for any ! sent in this context
      implicit val verifier = createVerifier()
      //TODO: Create and start a LoadbalancingForeman with 10 workers
      val foreman = actorOf(new LoadbalancingForeman(10)).start

      //TODO: Send N number of SumSequence work to the foreman (using !),
      //      write a test that verifies that all work was repsonded to,
      //      responses were from 10 worker actors,
      //      meaning that the loadbalancer reused the workers.
      //      Also verify that all the results came back as expected.
      val numbers = BigInt(10000) to BigInt(15000)
      val work = SumSequence(numbers)
      val expectedResult = work.perform()

      for(i <- 1 to 1000) foreman ! work

      within(10000) {
        (verifier.get !! 'Summary) match {
          case Some(result: List[(ActorRef,Any)]) =>
            result.size == 1000 && result.map(_._1).distinct.size == 10 && result.map(_._2).forall(expectedResult ==)
          case _ => false
        }
      }
    }
  }
}
