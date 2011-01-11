package akka.training.test

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, WordSpec}
import org.scalatest.matchers.MustMatchers

import akka.actor._
import akka.actor.Actor._
import akka.training.EchoActor

class Exercise1Spec extends WordSpec
                  with BeforeAndAfterAll
                  with BeforeAndAfterEach
                  with MustMatchers
{
  var actor: ActorRef = _

  override def beforeEach = {
    actor = actorOf[EchoActor].start
  }

  "An EchoActor" should {
     "be instantiable" in {
        actor must not be null
     }

    "echo the first message" in {
       (actor !! "ping") must equal (Some("ping"))
    }

    "echo back messages consistently" in {
       val r = new java.util.Random
       for(i <- 1 to 100) {
         val msg = r.nextInt.toString
         (actor !! msg) must equal (Some(msg))
       }
    }

    "handle the absense of a sender" in {
      actor ! "ping"
      (actor !! "pong") must equal (Some("pong"))
    }

    "echo back non-string messages" in {
      (actor !! 5) must equal (Some(5))
      (actor !! null) must equal (Some(null))
      (actor !! true) must equal (Some(true))
      val obj = new java.lang.Object
      (actor !! obj) must equal (Some(obj))
    }
  }

  override def afterEach = {
    registry.shutdownAll
  }
}