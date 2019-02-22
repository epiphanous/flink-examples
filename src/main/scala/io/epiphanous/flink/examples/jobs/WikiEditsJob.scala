package io.epiphanous.flink.examples.jobs

import io.epiphanous.flink.examples.models.WordCountEvent
import io.epiphanous.flinkrunner.flink.FlinkJob
import io.epiphanous.flinkrunner.model.FlinkConfig
import io.epiphanous.flinkrunner.SEE
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.DataStream
import org.apache.flink.streaming.connectors.wikiedits.WikipediaEditsSource

class WikiEditsJob() extends FlinkJob[WordCountEvent, WordCountEvent] {

  override def source()(implicit config: FlinkConfig, env: SEE) = {
    env
      .addSource(new WikipediaEditsSource())
      .map(edit ⇒ {
        println(s"$edit")
        WordCountEvent(s"${edit.getUser} ${edit.getTitle} ${edit.getSummary}",
                       edit.getTimestamp)
      })
  }

  override def transform(
      in: DataStream[WordCountEvent]
    )(implicit config: FlinkConfig,
      env: SEE
    ) = {
    in.flatMap(_.line.toLowerCase.split("\\W+"))
      .filter(_.nonEmpty)
      .map(word ⇒ (word, 1))
      .keyBy(0)
      .sum(1)
      .map(wc ⇒ WordCountEvent(s"${wc._1}\t${wc._2}"))
  }
}
