package protobufui.service.source

import java.io.{BufferedReader, InputStreamReader}

object ProtoCompiler {
  val temporaryPath = ???
}
class ProtoCompiler{
  def compile(proto: String) = {
    // saving proto string to file might be needed before invoking compile
    val protoDefinitionFile = ??? // write proto on disc

    val process = Runtime.getRuntime().exec("protoc -I=$temporaryPath --java_out=$temporaryPath $protoDefinition ");
        val input = new BufferedReader(new InputStreamReader(process.getInputStream()))
        val string = Stream.continually(input.readLine()).takeWhile(_ != null).mkString("\n")
    // if string != \n there were some problems and the error message is there

    val protoClassFile = ??? // $temporaryPath/fileFromDefinition.java
  }
}
