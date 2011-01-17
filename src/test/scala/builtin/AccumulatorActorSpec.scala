package akka.training.test

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, WordSpec}
import org.scalatest.matchers.MustMatchers

import akka.actor._
import akka.actor.Actor._
import akka.training.AccumulatorActor
import akka.training.AccumulatorActor._

class AccumulatorActorSpec extends WordSpec
                  with BeforeAndAfterAll
                  with BeforeAndAfterEach
                  with MustMatchers
{
  var actor: ActorRef = _

  override def beforeEach = {
    actor = actorOf[AccumulatorActor].start
  }

  "An AccumulatorActor" should {
     "be instantiable" in {
        actor must not be null
     }

     "have an initial count of 0" in {
       (actor !! Sum) must equal (Some(0))
     }

     "increment its internal counter" in {
       actor ! IncrementBy(1)
       (actor !! Sum) must equal (Some(1))
     }

     "not increment when the number is negative" in {
       actor ! IncrementBy(-1000)
       (actor !! Sum) must equal (Some(0))
     }

     "never overflow" in {
       actor ! IncrementBy(Long.MaxValue)
       actor ! IncrementBy(1)
       (actor !! Sum) must equal (Some(BigInt(Long.MaxValue) + 1))
     }
  }

  override def afterEach = {
    registry.shutdownAll
  }
}