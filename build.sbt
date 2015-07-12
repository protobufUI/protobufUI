name := "protobufUI"

version := "1.0"

organization := "pl.protobufui"

scalaVersion := "2.11.7"

val akkaVersion = "2.3.11"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion
  , "org.scalatest" %% "scalatest" % "2.2.1" % "test"
  , "org.mockito" % "mockito-core" % "2.0.3-beta" % "test"
  , "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
  , "com.google.protobuf" % "protobuf-java" % "3.0.0-alpha-3.1"
)
