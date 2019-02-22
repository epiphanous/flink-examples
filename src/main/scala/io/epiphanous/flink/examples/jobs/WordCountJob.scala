package io.epiphanous.flink.examples.jobs

import io.epiphanous.flink.examples.models.WordCountEvent
import io.epiphanous.flinkrunner.SEE
import io.epiphanous.flinkrunner.flink.FlinkJob
import io.epiphanous.flinkrunner.model.FlinkConfig
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.DataStream

class WordCountJob() extends FlinkJob[WordCountEvent, WordCountEvent] {
  override def transform(
      in: DataStream[
        WordCountEvent
      ]
    )(implicit config: FlinkConfig,
      env: SEE
    ) = {
    in.flatMap(_.line.toLowerCase.split("\\W+"))
      .filter(_.nonEmpty)
      .map(word ⇒ (word, 1))
      .keyBy(0)
      .sum(1)
      .map(wc ⇒ {
        println(wc)
        WordCountEvent(s"${wc._1}\t${wc._2}")
      })
  }
}
