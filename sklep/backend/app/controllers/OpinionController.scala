package controllers

import models.Opinion
import services.OpinionRepository

import javax.inject._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import play.api.libs.json.{JsValue, Json}

@Singleton
class OpinionController @Inject()(opinionRepo: OpinionRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def createOpinion(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Opinion].map {
      opinion =>
        opinionRepo.create(opinion.providerKey, opinion.productId, opinion.message).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("ERROR --> incorrect data")))
  }

  def readOpinion(id: Long): Action[AnyContent] = Action.async {
    val opinion = opinionRepo.read(id)
    opinion.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("ERROR --> can't be found")
    }
  }

  def readOpinionByUser(providerKey: String): Action[AnyContent] = Action.async {
    val opinion = opinionRepo.readByUser(providerKey)
    opinion.map { opinion =>
      Ok(Json.toJson(opinion))
    }
  }

  def readOpinionByProduct(productId: Long): Action[AnyContent] = Action.async {
    val opinion = opinionRepo.readByProduct(productId)
    opinion.map { opinion =>
      Ok(Json.toJson(opinion))
    }
  }

  def readAllOpinion(): Action[AnyContent] = Action.async {
    val opinion = opinionRepo.readAll()
    opinion.map { opinion =>
      Ok(Json.toJson(opinion))
    }
  }

  def updateOpinion(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Opinion].map {
      opinion =>
        opinionRepo.update(opinion.id, opinion).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("ERROR --> invalid json")))
  }

  def deleteOpinion(id: Long): Action[AnyContent] = Action.async {
    opinionRepo.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }
}
