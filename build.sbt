val V = new Object {
  val flinkrunner     = "1.1.4-SNAPSHOT"
  val flink           = "1.7.1"
  val logback         = "1.2.3"
  val scalaLogging    = "3.9.0"
  val log4jOverSlf4j  = "1.7.25"
  val typesafeConfig  = "1.3.3"
  val scalaTest       = "3.0.5"
  val rocksdb         = "5.17.2"
  val circe           = "0.11.1"
  val bloom           = "0.11.0-rfl"
  val enumeratum      = "1.5.13"
  val enumeratumCirce = "1.5.18"
  val guava           = "27.0.1-jre"
  val scalaCheck      = "1.14.0"
  val squants         = "1.3.0"
  val hadoopAws          = "2.7.3"
  val awsSdk          = "1.11.183"
  val jackson         = "2.6.7"
  val joda            = "2.8.1"
  val httpcore        = "4.4.4"
  val httpclient      = "4.5.3"
//  hadoop-aws-2.7.3.jar
//  aws-java-sdk-s3-1.11.183.jar and its dependencies:
//    aws-java-sdk-core-1.11.183.jar
//  aws-java-sdk-kms-1.11.183.jar
//  jackson-annotations-2.6.7.jar
//  jackson-core-2.6.7.jar
//  jackson-databind-2.6.7.jar
//  joda-time-2.8.1.jar
//  httpcore-4.4.4.jar
//  httpclient-4.5.3.jar

}

resolvers += Resolver.sonatypeRepo("releases")
//addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

addCompilerPlugin("io.tryp" % "splain" % "0.3.3" cross CrossVersion.patch)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-language:_",
  "-unchecked",
  //  "-Xfatal-warnings",
  "-Xfuture",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Ypartial-unification",
  //  "-Xplugin-require:macroparadise",
  "-P:splain:implicits:true"
  //  "-Ytyper-debug"
)

scalacOptions in Test ++= Seq("-Yrangepos")

def excludeLog4j(m: ModuleID) = m.excludeAll(
  ExclusionRule(organization = "log4j"),
  ExclusionRule(organization = "org.slf4j", name = "slf4j-log4j12")
)

val flinkProvidedDeps = Seq(
  "flink-scala_2.11",
  "flink-streaming-scala_2.11",
  "flink-s3-fs-hadoop"
).map("org.apache.flink" % _ % V.flink % "provided").map(excludeLog4j)

val flinkExtraDeps = Seq(
  "flink-connector-kafka",
  "flink-connector-kinesis",
  "flink-connector-wikiedits"
).map("org.apache.flink" %% _ % V.flink).map(excludeLog4j)

val circeDeps = Seq(
  "circe-core",
  "circe-generic",
  "circe-generic-extras",
  "circe-java8",
  "circe-parser"
  //  "circe-optics"
).map("io.circe" %% _ % V.circe)

val loggingDeps = Seq(
  "ch.qos.logback"             % "logback-core"     % V.logback % "provided",
  "ch.qos.logback"             % "logback-classic"  % V.logback % "provided",
  "org.slf4j"                  % "log4j-over-slf4j" % V.log4jOverSlf4j % "provided",
  "com.typesafe.scala-logging" %% "scala-logging"   % V.scalaLogging
)

val otherDeps = Seq(
  "io.epiphanous"     %% "flinkrunner"      % V.flinkrunner,
  "org.rocksdb"       % "rocksdbjni"        % V.rocksdb,
  "com.typesafe"      % "config"            % V.typesafeConfig,
  "com.github.ponkin" % "bloom-core"        % V.bloom,
  "com.beachape"      %% "enumeratum"       % V.enumeratum,
  "com.beachape"      %% "enumeratum-circe" % V.enumeratum,
  "com.google.guava"  % "guava"             % V.guava,
  "org.typelevel"     %% "squants"          % V.squants
)

val s3Deps = Seq(
  "org.apache.hadoop"          % "hadoop-aws"          % V.hadoopAws,
  "com.amazonaws"              % "aws-java-sdk-core"   % V.awsSdk,
  "com.amazonaws"              % "aws-java-sdk-s3"     % V.awsSdk,
  "com.amazonaws"              % "aws-java-sdk-kms"    % V.awsSdk,
  "com.fasterxml.jackson.core" % "jackson-core"        % V.jackson,
  "com.fasterxml.jackson.core" % "jackson-annotations" % V.jackson,
  "com.fasterxml.jackson.core" % "jackson-databind"    % V.jackson,
  "org.apache.httpcomponents"  % "httpcore"            % V.httpcore,
   "org.apache.httpcomponents" % "httpclient"          % V.httpclient,
  "joda-time"                  % "joda-time"           % V.joda
).map(excludeLog4j)

val testDeps = Seq(
  "org.scalactic"       %% "scalactic"             % V.scalaTest % Test,
  "org.scalatest"       %% "scalatest"             % V.scalaTest % Test,
  "org.scalacheck"      %% "scalacheck"            % V.scalaCheck,
  "com.propensive"      %% "magnolia"              % "0.6.1" % Test,
  "org.apache.flink"    %% "flink-runtime-web"     % V.flink % Test

//  "com.github.alexarchambault"   %% "scalacheck-shapeless_1.14" % "1.2.0-1" % "test",
)

lazy val WordCount = (project in file("."))
  .settings(
    organization := "io.epiphanous",
    name := "wordcount",
    version := "0.0.1",
    scalaVersion := "2.11.12",
    libraryDependencies ++=
      flinkProvidedDeps ++
        flinkExtraDeps ++
        circeDeps ++
        otherDeps ++
        loggingDeps ++
        s3Deps ++
        testDeps,
    assembly / assemblyJarName := s"wordcount-${version.value}.jar",
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "io.epiphanous.flink.examples"
  )
  .enablePlugins(BuildInfoPlugin)

Compile / run / fork := true
Global / cancelable := true

test in assembly := {}

run in Compile := Defaults
  .runTask(fullClasspath in Compile,
    mainClass in (Compile, run),
    runner in (Compile, run))
  .evaluated
