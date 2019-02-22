package io.epiphanous.flink.examples
import io.epiphanous.flink.examples.models.WordCountEvent
import org.scalacheck.{Arbitrary, Gen}

import scala.collection.mutable.ListBuffer
import scala.util.Random

trait PropGenerators {

  def strGen(min: Int = 5, max: Int = 10) =
    for {
      size   <- Gen.choose(min, max)
      string <- Gen.identifier.suchThat(id => id.length >= min)
    } yield string.take(size)

  lazy val simpleStrGen = strGen()

  def wordCountEventGen: Gen[WordCountEvent] =
    for {
      numWords ← Gen.choose(10, 20)
      words    ← Gen.listOfN(numWords, simpleStrGen)
    } yield WordCountEvent(words.mkString(" "))

  implicit lazy val wordCountEventArb: Arbitrary[WordCountEvent] = Arbitrary(
    wordCountEventGen)

  val rng = new Random()

  def genPop[T](
      mean: Int,
      sd: Double = 0
    )(implicit arb: Arbitrary[T]
    ): List[T] = {
    val pop = ListBuffer.empty[T]
    val n   = (rng.nextGaussian() * sd + mean).round.toInt
    var k   = 0
    while (k < n) {
      arb.arbitrary.sample match {
        case Some(obj: T) =>
          pop += obj
          k += 1
        case None => // noop
      }
    }
    pop.toList
  }

  def genPopWith[T](mean: Int, sd: Double, arb: Arbitrary[T]): List[T] =
    genPop[T](mean, sd)(arb)

  def genOne[T](implicit arb: Arbitrary[T]) = genPop[T](1).head

  def genOneWith[T](arb: Arbitrary[T]) =
    genOne[T](arb)
}
