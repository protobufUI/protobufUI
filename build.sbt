import sbt.Keys._
import sbtprotobuf.{ProtobufPlugin => PB}

name := "protobufUI"

version := "1.0"

organization := "pl.protobufui"

scalaVersion := "2.11.7"

val akkaVersion = "2.3.11"

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion
  , "org.scalatest" %% "scalatest" % "2.2.1" % "test"
  , "org.mockito" % "mockito-core" % "2.0.3-beta" % "test"
  , "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
  , "com.google.protobuf" % "protobuf-java" % "3.0.0-alpha-3.1"
  , "org.scala-lang" % "scala-reflect" % "2.11.7"
  , "pl.codekratisti" %% "ipe-toolkit" % "0.1.5-SNAPSHOT"
)

PB.protobufSettings

// Run in separate VM, so there are no issues with double initialization of JavaFX
// Dodatkowo bez tej linijki nie chce załadować arkuszy styli JFX
fork := true
fork in Test := true

mainClass in(Compile, run) := Some("protobufui.gui.Main")



