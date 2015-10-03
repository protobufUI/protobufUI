package protobufui.cli

import java.io.File

import packageinfo.BuildInfo
import scopt.OptionParser

case class Config(load: File=null,
                   gui:Boolean=false)

object Parser{
  val parser = new OptionParser[Config](s"${BuildInfo.jarName}.jar") {
    override def showUsageOnError = true
    head(BuildInfo.name, BuildInfo.version)
    note("Testing web services based on Google Protocol Buffers")
    note(s"Build at: ${BuildInfo.builtAtString}")

    opt[File]('l', "load") valueName "<file>" action { (x, c) =>
      c.copy(load = x) } text "directory, proto or jar to be loaded"
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
