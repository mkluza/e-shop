package models

import play.api.libs.json._


case class FavouriteProduct(id: Long = 0, providerKey: String, productId: Long)

object FavouriteProduct {
  implicit val favourite_productFormat: OFormat[FavouriteProduct] = Json.format[FavouriteProduct]
}
