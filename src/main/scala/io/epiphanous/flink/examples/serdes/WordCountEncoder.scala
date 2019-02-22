package io.epiphanous.flink.examples.serdes

import java.io.OutputStream
import java.nio.charset.StandardCharsets

import com.typesafe.scalalogging.LazyLogging
import io.epiphanous.flink.examples.models.WordCountEvent
import org.apache.flink.api.common.serialization.Encoder

class WordCountEncoder extends Encoder[WordCountEvent] with LazyLogging {
  override def encode(element: WordCountEvent, stream: OutputStream) = {
    stream.write(element.toString.getBytes(StandardCharsets.UTF_8))
    stream.write('\n')
  }
}
