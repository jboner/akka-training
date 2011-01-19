package builtin

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, WordSpec}
import org.scalatest.matchers.MustMatchers

import akka.actor._
import akka.actor.Actor._
import akka.training.Crime

class TheCrimeSpec extends WordSpec
                  with BeforeAndAfterAll
                  with BeforeAndAfterEach
                  with MustMatchers
{
  var crime: Crime = _

  override def beforeEach = {
    crime = new Crime
  }

  "A Crime" should {

    "be able to have suspects" in {
      crime.suspects must not be null
    }
  }

  override def afterEach = {
    crime.suspects.close
    registry.shutdownAll
  }
}