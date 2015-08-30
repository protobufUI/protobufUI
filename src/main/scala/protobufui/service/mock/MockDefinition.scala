package protobufui.service.mock

import java.net.InetSocketAddress

import com.google.protobuf.MessageLite


case class MockDefinition(socketAddress: InetSocketAddress, requestClass: Class[_], responseGen: PartialFunction[MessageLite, MessageLite])
