package io.epiphanous.flink.examples
import com.typesafe.scalalogging.LazyLogging
import org.scalatest._

trait BaseSpec
    extends Matchers
    with OptionValues
    with EitherValues
    with TryValues
    with Inside
    with LazyLogging {

  val runnerFactory = new WordCountRunnerFactory()
}
