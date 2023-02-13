package controllers

import models.Bought
import services.BoughtRepository

import javax.inject._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import play.api.libs.json.{JsValue, Json}

@Singleton
class BoughtController @Inject()(val boughtRepo: BoughtRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def createBought(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Bought].map {
      bought =>
        boughtRepo.create(bought.providerKey, bought.orderId).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("ERROR --> incorrect data")))
  }

  def readBought(id: Long): Action[AnyContent] = Action.async {
    val bought = boughtRepo.read(id)
    bought.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("ERROR --> can't be found")
    }
  }

  def readBoughtByUser(providerKey: String): Action[AnyContent] = Action.async {
    val bought = boughtRepo.readByUserId(providerKey)
    bought.map { bought =>
      Ok(Json.toJson(bought))
    }
  }

  def readAllBought(): Action[AnyContent] = Action.async {
    val bought = boughtRepo.readAll()
    bought.map { bought =>
      Ok(Json.toJson(bought))
    }
  }

  def updateBought(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Bought].map {
      bought =>
        boughtRepo.update(bought.id, bought).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("ERROR --> invalid json")))
  }

  def deleteBought(id: Long): Action[AnyContent] = Action.async {
    boughtRepo.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }
}