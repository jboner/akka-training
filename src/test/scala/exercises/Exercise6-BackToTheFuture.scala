package akka.training

/**
 * Exercise 6 - "Back to the Future"
 *
 * In this exercise we will learn how to use Futures
 * to manage replies and to avoid blocking.
 * We will reuse what we did for exercise5 and add some
 * spice!
 *
 *
 * Let's go!
 */

import akka.actor._
import akka.actor.Actor._
import akka.dispatch.{ Future, Futures }
import akka.training.AkkaTrainingTest
import akka.training.FutureWork._
import akka.training.JobRelated.{ Work, Worker, SumSequence }

object FutureWork {
  //We will use this to introduce failures
  case object FailWork extends Work {
    def perform() = throw new IllegalStateException("expected")
  }

  //We will use this to simulate delays in work processing
  case class DelayedEchoWork(sleepytimeMs: Long, echo: Any) extends Work {
    def perform() = {
      Thread.sleep(sleepytimeMs)
      echo
    }
  }
}

/**
* The following test verifies that the functionality
* of the code you've written works as expected,
* you can execute the test by typing this in sbt:
*
* test-only akka.training.FutureSpec
*/

class FutureSpec extends AkkaTrainingTest {
  "Exercise6" should {
     "teach you how to await and get the result of a Future" in {
       //TODO: Create and start a Worker actor

       //TODO: Send, using !!!, a SumSequence message to the worker, make sure it takes a couple of seconds to run it

       //TODO: Verify that you get the correct result from the future

       //TODO: Stop the worker
     }

     "teach you how you can asynchronously transform the result of a future" in {
       //TODO: Create and start a Worker actor

       //TODO: Send, using !!!, a SumSequence message to the worker, make sure it takes a couple of seconds to run it
       //      then asynchronously transform the result to a String

       //TODO: Verify that you get the correct result from the future

       //TODO: Stop the worker
     }

     "teach you how to handle a failure in a Future" in {
       //TODO: Create and start a Worker actor

       //TODO: Send, using !!!, a FailWork to the worker

       //TODO: Write some code that examines the future and checks if it failed

       //TODO: Create code that either returns the result of the future or throws it's exception,
       //      then verify that it throws the expected exception

       //TODO: Stop the worker
     }

     "teach you how to pick the fastest service using Futures" in {
       //TODO: Create and start a wizard foreman

       //TODO: Create 2 different futures using !!! and DelayedEchoWork with
       //      a sleepytime varying by about a second or two

       //TODO: await the completion of the first completing of the futures,
       //      and then immediately verify the response
       //BONUS: Also verify that only the minimum required waiting was done

       //TODO: Stop the foreman
     }

     "teach you how to await many futures to be completed" in {
       //TODO: Create and start a WizardForeman

       //TODO: Using !!!, send 10 DelayedEchoWork with different sleepytimes, and have it echo some value,
       //      to the foreman and generate a list of the resulting futures.

       //TODO: Await all the futures, and verify that you got the result you expected in each

       //TODO: Stop foreman
     }
  }
}