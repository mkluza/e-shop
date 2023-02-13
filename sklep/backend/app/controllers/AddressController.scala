package controllers

import models.Address
import services.AddressRepository

import javax.inject._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import play.api.libs.json.{JsValue, Json}

@Singleton
class AddressController @Inject()(addressRepo: AddressRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def createAddress(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Address].map {
      address =>
        addressRepo.create(address.providerKey, address.firstname, address.lastname, address.city, address.zipcode, address.street, address.phoneNumber).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("ERROR --> incorrect data")))
  }


  def readAddress(providerKey: String): Action[AnyContent] = Action.async {
    val address = addressRepo.read(providerKey)
    address.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("ERROR --> can't be found")
    }
  }

  def readAllAddresses(): Action[AnyContent] = Action.async {
    val address = addressRepo.readAll()
    address.map { address =>
      Ok(Json.toJson(address))
    }
  }

  def updateAddress(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Address].map {
      address =>
        addressRepo.update(address.id, address).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("ERROR --> invalid json")))
  }

  def deleteAddress(id: Long): Action[AnyContent] = Action.async {
    addressRepo.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }
}
