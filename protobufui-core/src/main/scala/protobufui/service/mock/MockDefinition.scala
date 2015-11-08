package protobufui.service.mock

import java.net.InetSocketAddress

import com.google.protobuf.MessageLite
import protobufui.model.MessageLiteClass


case class MockDefinition(socketAddress: InetSocketAddress, requestClass: MessageLiteClass, responseGen: PartialFunction[MessageLite, MessageLite])
