package io.epiphanous.flink.examples.models

import java.util.UUID

import io.epiphanous.flinkrunner.model.FlinkEvent

sealed case class WordCountEvent(
    line: String,
    $timestamp: Long = System.currentTimeMillis())
    extends FlinkEvent {
  def $key: String              = line
  def $id: String               = UUID.randomUUID().toString
  override def toString: String = line
}
