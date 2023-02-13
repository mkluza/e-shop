package models

import play.api.libs.json._

case class Address(id: Long = 0, providerKey: String, firstname: String, lastname: String,
                   city: String, zipcode: String, street: String, phoneNumber: String)

object Address {
  implicit val addressFormat: OFormat[Address] = Json.format[Address]
}
