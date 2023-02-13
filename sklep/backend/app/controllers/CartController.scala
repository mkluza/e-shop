package controllers

import models.Cart
import services.CartRepository

import javax.inject._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import play.api.libs.json.{JsValue, Json}

@Singleton
class CartController @Inject()(cartRepo: CartRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def createCart(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Cart].map {
      cart =>
        cartRepo.create(cart.providerKey, cart.productId, cart.amount).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("ERROR --> incorrect data")))
  }

  def readCart(id: Long): Action[AnyContent] = Action.async { implicit request =>
    val cart = cartRepo.read(id)
    cart.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("ERROR --> can't be found")
    }
  }

  def readCartByUser(providerKey: String): Action[AnyContent] = Action.async {
    val carts = cartRepo.readByUser(providerKey)
    carts.map { carts =>
      Ok(Json.toJson(carts))
    }
  }

  def readAllCarts(): Action[AnyContent] = Action.async {
    val carts = cartRepo.readAll()
    carts.map { carts =>
      Ok(Json.toJson(carts))
    }
  }

  def updateCart(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Cart].map {
      cart =>
        cartRepo.update(cart.id, cart).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("ERROR --> invalid json")))
  }

  def deleteCart(id: Long): Action[AnyContent] = Action.async {
    cartRepo.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }
}