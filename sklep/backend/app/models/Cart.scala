package models

import play.api.libs.json._


case class Cart(id: Long = 0, providerKey: String, productId: Long, amount: Long)

object Cart {
  implicit val cartFormat: OFormat[Cart] = Json.format[Cart]
}
