package protobufui.test.step

import scala.beans.BeanProperty

/**
 * Created by humblehound on 04.10.15.
 */
case class PropertyKeyValue(@BeanProperty var key: String, @BeanProperty var value: String) {

}
