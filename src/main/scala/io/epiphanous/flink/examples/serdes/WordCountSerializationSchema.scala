package io.epiphanous.flink.examples.serdes

import java.nio.charset.StandardCharsets

import io.epiphanous.flink.examples.models.WordCountEvent
import org.apache.flink.api.common.serialization.SerializationSchema

class WordCountSerializationSchema extends SerializationSchema[WordCountEvent] {
  override def serialize(element: WordCountEvent) =
    element.line.getBytes(StandardCharsets.UTF_8)
}
