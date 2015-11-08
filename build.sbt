import sbt.Keys._
import sbtbuildinfo.BuildInfoPlugin.autoImport._

lazy val root = (project in file("."))
  .aggregate(cli, gui, core)

lazy val cli = (project in file("protobufui-cli"))
  .enablePlugins(BuildInfoPlugin)
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies += "com.github.scopt" %% "scopt" % "3.3.0",
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion,
      "jarName" ->  s"${name.value}-${version.value}.jar"
    ),
    buildInfoPackage := "packageinfo",
    buildInfoOptions += BuildInfoOption.BuildTime
  )
  .dependsOn(core)


lazy val gui = (project in file("protobufui-gui"))
  .settings(commonSettings: _*)
  .dependsOn(core)


lazy val core = (project in file("protobufui-core"))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= {
      val scalaV = scalaVersion.value
      Seq(
        "org.scala-lang" % "scala-compiler" % scalaV,
        "org.scala-lang" % "scala-library" % scalaV,
        "org.scala-lang" % "scala-reflect" % scalaV,
        "jline" % "jline" % "2.13",
        "pl.codekratisti" %% "ipe-toolkit" % "0.1.12-SNAPSHOT"
      )
    }
  )

val akkaVersion = "2.3.11"
lazy val commonSettings = Seq(
  version := "1.0",
  organization := "pl.protobufui",
  scalaVersion := "2.11.7",
  libraryDependencies ++= Seq(
    "com.google.protobuf" % "protobuf-java" % "3.0.0-alpha-3.1",
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "org.scalatest" %% "scalatest" % "2.2.1" % "test",
    "org.mockito" % "mockito-core" % "2.0.3-beta" % "test"
  )
)
//TODO wyczyscic caly plik ze smieci

// Run in separate VM, so there are no issues with double initialization of JavaFX
// Dodatkowo bez tej linijki nie chce załadować arkuszy styli JFX
fork := true
fork in Test := true

mainClass in(Compile, run) := Some("protobufui.Main")

test in assembly := {}
mainClass in assembly := Some("protobufui.Main")

assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

