package models

import play.api.libs.json._


case class Opinion(id: Long = 0, providerKey: String, productId: Long, message: String)

object Opinion {
  implicit val opinionFormat: OFormat[Opinion] = Json.format[Opinion]
}