package io.epiphanous.flink.examples.jobs
import io.epiphanous.flink.examples.{BasePropSpec, Runner}
import io.epiphanous.flink.examples.serdes.WordCountSerializationSchema

class WordCountJobSpec extends BasePropSpec {

  val ss = new WordCountSerializationSchema()

  val config = s"""
                  |jobs {
                  |  WordCountJob {
                  |    sources {
                  |      events {
                  |        connector = file
                  |        name = events
                  |        path = "resource:///hamlet.txt"
                  |      }
                  |    }
                  |  }
                  |}
    """.stripMargin

  property("transform property") {
    val args = Array("WordCountJob")
    Runner.run(args, Some(config), callback = {
      case stream =>
        val results = stream.take(500)
        results.foreach(println)
        assert(true)
    })
  }

  val config2 = s"""
                  |mock.edges = false
                  |jobs {
                  |  WordCountJob {
                  |    sources {
                  |      in {
                  |        connector = file
                  |        name = in
                  |        path = "resource:///hamlet.txt"
                  |      }
                  |    }
                  |    sinks {
                  |      out {
                  |        connector = file
                  |        name = out
                  |        path = "s3://rlyons/examples/word-count/hamlet"
                  |      }
                  |    }
                  |  }
                  |}
    """.stripMargin

  property("can write to s3") {
    val args = Array("WordCountJob")
    Runner.run(args, Some(config2))
  }

  val config3 = s"""
                   |mock.edges = false
                   |jobs {
                   |  WordCountJob {
                   |    sources {
                   |      in {
                   |        connector = kinesis
                   |        name = in
                   |        stream = mcyr-stream
                   |        config {
                   |          aws.region = us-east-1
                   |          flink.stream.initpos = TRIM_HORIZON
                   |        }
                   |      }
                   |    }
                   |    sinks {
                   |      out {
                   |        connector = file
                   |        name = out
                   |        path = "s3://rlyons/examples/word-count/mcyr"
                   |        bucket.assigner.datetime.format = "yyyy/MM/dd/HH"
                   |      }
                   |    }
                   |  }
                   |}
    """.stripMargin

  property("can read from kinesis and write to s3") {
    val args = Array("WordCountJob")
    Runner.run(args, Some(config3))
  }

}
