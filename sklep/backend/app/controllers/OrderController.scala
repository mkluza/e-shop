package controllers

import models.Order
import services.OrderRepository

import javax.inject._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import play.api.libs.json.{JsValue, Json}

@Singleton
class OrderController @Inject()(orderRepo: OrderRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def createOrder(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Order].map {
      order =>
        orderRepo.create(order.cartId, order.addressId).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("ERROR --> incorrect data")))
  }

  def readOrder(id: Long): Action[AnyContent] = Action.async {
    val order = orderRepo.read(id)
    order.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("ERROR --> can't be found")
    }
  }

  def readAllOrder(): Action[AnyContent] = Action.async {
    val order = orderRepo.readAll()
    order.map { order =>
      Ok(Json.toJson(order))
    }
  }

  def updateOrder(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Order].map {
      order =>
        orderRepo.update(order.id, order).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("ERROR --> invalid json")))
  }

  def deleteOrder(id: Long): Action[AnyContent] = Action.async {
    orderRepo.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }
}
