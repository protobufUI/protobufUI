package protobufui.service.mock

import java.net.InetSocketAddress

import com.google.protobuf.MessageLite


case class MockDefinition[Req <: MessageLite, Res <: MessageLite](socketAddress: InetSocketAddress, reqClass: Class[Req], responseGen: Req => Res)
