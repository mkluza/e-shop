package models

import play.api.libs.json._


case class Bought(id: Long = 0, providerKey: String, orderId: Long)

object Bought {
  implicit val boughtFormatBought: OFormat[Bought] = Json.format[Bought]
}