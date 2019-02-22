package io.epiphanous.flink.examples
import io.epiphanous.flink.examples.jobs.{WikiEditsJob, WordCountJob}
import io.epiphanous.flink.examples.models.WordCountEvent
import io.epiphanous.flink.examples.serdes.{
  WordCountDeserializationSchema,
  WordCountEncoder,
  WordCountSerializationSchema
}
import io.epiphanous.flinkrunner.FlinkRunnerFactory

@SerialVersionUID(123456789L)
class WordCountRunnerFactory()
    extends FlinkRunnerFactory[WordCountEvent]
    with Serializable {
  override def getJobInstance(name: String) = name match {
    case "WordCountJob" ⇒ new WordCountJob()
    case "WikiEditsJob" ⇒ new WikiEditsJob()
    case _              ⇒ throw new UnsupportedOperationException(s"unknown job '$name'")
  }

  override def getDeserializationSchema      = new WordCountDeserializationSchema()
  override def getSerializationSchema        = new WordCountSerializationSchema()
  override def getEncoder                    = new WordCountEncoder()
  override def getKeyedDeserializationSchema = ???
  override def getKeyedSerializationSchema   = ???
  override def getAddToJdbcBatchFunction     = ???

}
