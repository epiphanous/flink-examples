package io.epiphanous.flink.examples.jobs

import io.epiphanous.flink.examples.models.WordCountEvent
import io.epiphanous.flinkrunner.model.FlinkConfig
import io.epiphanous.flinkrunner.SEE
import org.apache.flink.api.scala._
import org.apache.flink.streaming.connectors.wikiedits.WikipediaEditsSource

class WikiEditsJob() extends WordCountJob {

  override def source()(implicit config: FlinkConfig, env: SEE) = {
    env
      .addSource(new WikipediaEditsSource())
      .map(edit ⇒ {
        println(s"$edit")
        WordCountEvent(s"${edit.getUser} ${edit.getTitle} ${edit.getSummary}",
                       edit.getTimestamp)
      })
  }
}
