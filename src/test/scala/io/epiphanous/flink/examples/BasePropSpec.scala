package io.epiphanous.flink.examples

import org.scalatest._
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class BasePropSpec
    extends PropSpec
    with BaseSpec
    with GeneratorDrivenPropertyChecks
    with PropGenerators {}
