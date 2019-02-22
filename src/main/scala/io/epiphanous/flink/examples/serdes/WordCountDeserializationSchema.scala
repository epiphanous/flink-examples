package io.epiphanous.flink.examples.serdes

import java.nio.charset.StandardCharsets

import io.epiphanous.flink.examples.models.WordCountEvent
import org.apache.flink.api.common.serialization.DeserializationSchema
import org.apache.flink.api.java.typeutils.TypeExtractor

class WordCountDeserializationSchema
    extends DeserializationSchema[WordCountEvent] {
  override def deserialize(message: Array[Byte]) =
    WordCountEvent(new String(message, StandardCharsets.UTF_8))
  override def isEndOfStream(nextElement: WordCountEvent) = false
  override def getProducedType =
    TypeExtractor.createTypeInfo(classOf[WordCountEvent])
}
