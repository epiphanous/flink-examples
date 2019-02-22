package io.epiphanous.flink.examples
import io.epiphanous.flink.examples.models.WordCountEvent
import io.epiphanous.flinkrunner.FlinkRunner

object Runner {

  def main(args: Array[String]): Unit =
    run(args)

  def run(
      args: Array[String],
      optConfig: Option[String] = None,
      sources: Map[String, Seq[Array[Byte]]] = Map.empty,
      callback: PartialFunction[Stream[WordCountEvent], Unit] = {
        case _ =>
          ()
      }
    ): Unit = {
    val runner =
      new FlinkRunner[WordCountEvent](args,
                                      new WordCountRunnerFactory(),
                                      sources,
                                      optConfig)
    runner.process(callback)
  }

}
