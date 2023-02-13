package models

import play.api.libs.json._


case class Product(id: Long = 0, categoryId: Long, name: String, description: String, price: String, image: String)

object Product {
  implicit val productFormat: OFormat[Product] = Json.format[Product]
}
