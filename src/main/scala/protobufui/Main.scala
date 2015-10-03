package protobufui

import javafx.application.Application

import akka.actor.ActorSystem
import ipetoolkit.bus.{IPEEventBus, ClassBasedEventBus}
import protobufui.cli.{CliMain, Config, Parser}
import protobufui.gui.GuiMain


object Main {

  val actorSystem: ActorSystem = ActorSystem("protobufUI")
  implicit val eventBus: ClassBasedEventBus = IPEEventBus
  var config:Config = null

  def main(args: Array[String]): Unit = {
    config = Parser.parse(args)
    if(config.gui) {
      Application.launch(classOf[GuiMain], args: _*)
    }else{
      CliMain.start(config)
    }
  }
}
