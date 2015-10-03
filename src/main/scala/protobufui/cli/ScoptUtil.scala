package protobufui.cli

import java.io.File

import packageinfo.BuildInfo
import scopt.OptionParser

case class Config(workspace: File=null,
                   load: File=null,
                   gui:Boolean=false,
                   suites:List[String]=List(),
                   cases:List[String]=List())

object Parser{
  val parser = new OptionParser[Config](s"${BuildInfo.jarName}.jar") {
    override def showUsageOnError = true
    head(BuildInfo.name, BuildInfo.version)
    note("Testing web services based on Google Protocol Buffers")
    note(s"Build at: ${BuildInfo.builtAtString}")

    opt[File]('w', "workspace") valueName "<file>" action { (x, c) =>
      c.copy(workspace = x) } text "workspace.xml"

    opt[File]('l', "load") valueName "<file>" action { (x, c) =>
      c.copy(load = x) } text "directory, proto or jar to be loaded"

    opt[List[String]]('s', "suite") valueName "<test_suite1,test_suite2>" action { (x, c) =>
      c.copy(suites = x) } text "test suites list"

    opt[List[String]]('c', "cases") valueName "<test_case1,test_case2>" action { (x, c) =>
      c.copy(cases = x) } text "test cases list"

    opt[Unit]("gui") action { (_, c) =>
      c.copy(gui = true) } text "enable gui"

    help("help") text "prints usage"
    version("version") text "prints version"
  }
  def parse(args:Seq[String]):Config = {
    parser.parse(args, Config()) match {
      case Some(config) =>
        config
      case None =>
        throw new IllegalArgumentException
    }
  }

}
