package akka.training.key

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
import akka.training.AkkaTrainingTest
import akka.dispatch.{ Future, Futures }
import akka.training.key.FutureWork._
import akka.training.key.JobRelated.{ Work, Worker, SumSequence }

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
* test-only akka.training.key.FutureSpec
*/

class FutureSpec extends AkkaTrainingTest {
  "Exercise6" should {
     "teach you how to await and get the result of a Future" in {
       //TODO: Create and start a Worker actor
       val worker = actorOf[Worker].start

       val message = SumSequence(BigInt(10000) to BigInt(100000))
       val expect = message.numbers.sum

       //TODO: Send, using !!!, a SumSequence message to the worker, make sure it takes a couple of seconds to run it
       val future: Future[BigInt] = worker !!! message

       //TODO: Verify that you get the correct result from the future
       future.await.result.get must be === expect

       //TODO: Stop the worker
       worker.stop
     }

     "teach you how you can asynchronously transform the result of a future" in {
       //TODO: Create and start a Worker actor
       val worker = actorOf[Worker].start

       val message = SumSequence(BigInt(10000) to BigInt(100000))
       val expect = message.numbers.sum

       //TODO: Send, using !!!, a SumSequence message to the worker, make sure it takes a couple of seconds to run it
       //      then asynchronously transform the result to a String
       val future = (worker.!!![BigInt](message)).map(_.toString)

       //TODO: Verify that you get the correct result from the future
       future.await.result.get must be === expect.toString

       //TODO: Stop the worker
       worker.stop
     }

     "teach you how to handle a failure in a Future" in {
       //TODO: Create and start a Worker actor
       val worker = actorOf[Worker].start

       //TODO: Send, using !!!, a FailWork to the worker
       val future = worker !!! FailWork

       //TODO: Write some code that examines the future and checks if it failed
       val exception = future.await.exception.get
       exception.getClass must be === classOf[IllegalStateException]
       exception.getMessage must be === "expected"

       //TODO: Create code that either returns the result of the future or throws it's exception,
       //      then verify that it throws the expected exception
       intercept[IllegalStateException] {
         future.await.resultOrException
         fail("Shouldn't get here")
       }

       //TODO: Stop the worker
       worker.stop
     }

     "teach you how to pick the fastest service using Futures" in {
       //TODO: Create and start a wizard foreman
       val foreman = actorOf[WizardForeman].start

       //TODO: Create 2 different futures using !!! and DelayedEchoWork with
       //      a sleepytime varying by about a second or two
       val future1: Future[String] = foreman !!! DelayedEchoWork(1000, "future1")
       val future2: Future[String] = foreman !!! DelayedEchoWork(4000, "future2")

       //TODO: await the completion of the first completing of the futures,
       //      and then immediately verify the response
       //BONUS: Also verify that only the minimum required waiting was done
       Futures.awaitEither(future1, future2).get must be === "future1"
       future2.resultOrException must be === None


       //TODO: Stop the foreman
       foreman.stop
     }

     "teach you how to await many futures to be completed" in {
       //TODO: Create and start a WizardForeman
       val foreman = actorOf[WizardForeman].start

       //TODO: Using !!!, send 10 DelayedEchoWork with different sleepytimes, and have it echo some value,
       //      to the foreman and generate a list of the resulting futures.
       val numbers = (1 to 10).toList
       val futures = for(i <- numbers) yield foreman.!!![Int](DelayedEchoWork(i * 100, i))

       //TODO: Await all the futures, and verify that you got the result you expected in each
       Futures.awaitAll(futures)
       for((n,f) <- numbers zip futures) f.result.get must be === n

       //TODO: Stop foreman
       foreman.stop
     }

     "BONUS: teach you how to aggregate the results of several futures into one" in {
       //TODO: Create and start a WizardForeman
       val foreman = actorOf[WizardForeman].start

       //TODO: Using !!!, send 10 DelayedEchoWork with different sleepytimes, and have it echo some integer value,
       //      to the foreman and generate a list of the resulting futures.
       val numbers = (1 to 10).toList
       val futures = for(i <- numbers) yield foreman.!!![Int](DelayedEchoWork(i * 100, i))

       //TODO: Do a non-blocking fold or reduce of the futures and sum the values
       val sumFuture = Futures.fold(0)(futures)(_ + _)

       //TODO: Await the final value and verify that you got the result you expected

       sumFuture.await.result.get must be === numbers.sum

       //TODO: Stop foreman
       foreman.stop
     }
  }
}