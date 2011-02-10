package akka.training.test

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, WordSpec}
import org.scalatest.matchers.MustMatchers

import akka.actor.Actor

class DemoSpec extends WordSpec
                  with BeforeAndAfterAll
                  with BeforeAndAfterEach
                  with MustMatchers
{
  override def beforeAll = { }
  override def beforeEach = { }

  override def afterAll = { }
  override def afterEach = { }

  "A DemoSpec" should {
     "be able to know if 0 is 0" in {
        0 must be === 0
     }
  }
}