package models

import play.api.libs.json._


case class Order(id: Long = 0, cartId: Long, addressId: Long)

object Order {
  implicit val orderFormat: OFormat[Order] = Json.format[Order]
}