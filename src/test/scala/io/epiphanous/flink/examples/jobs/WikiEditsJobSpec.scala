package io.epiphanous.flink.examples.jobs
import io.epiphanous.flink.examples.{BasePropSpec, Runner}

class WikiEditsJobSpec extends BasePropSpec {

  val config2 = s"""
                   |mock.edges = false
                   |jobs {
                   |  WikiEditsJob {
                   |    sinks {
                   |      out {
                   |        connector = file
                   |        name = out
                   |        path = "s3://rlyons/examples/word-count/wikipedia-edits"
                   |        bucket.assigner.datetime.format = "yyyy/MM/dd/HH"
                   |      }
                   |    }
                   |  }
                   |}
    """.stripMargin

  property("can write to s3") {
    val args = Array("WikiEditsJob")
    Runner.run(args, Some(config2))
  }

}
