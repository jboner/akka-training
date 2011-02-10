package akka.training

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, WordSpec}
import org.scalatest.matchers.MustMatchers
import akka.actor._

abstract class AkkaTrainingTest extends  WordSpec
                                    with BeforeAndAfterAll
                                    with BeforeAndAfterEach
                                    with MustMatchers {
  override def afterEach = {
    Actor.registry.shutdownAll
  }
}