package controllers

import models.FavouriteProduct
import services.FavouriteProductRepository

import javax.inject._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import play.api.libs.json.{JsValue, Json}

@Singleton
class FavouriteProductController @Inject()(favouriteProductRepo: FavouriteProductRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {


  def createFavouriteProduct(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[FavouriteProduct].map {
      favouriteProduct =>
        favouriteProductRepo.create(favouriteProduct.providerKey, favouriteProduct.productId).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("ERROR --> incorrect data")))
  }

  def readFavouriteProduct(id: Long): Action[AnyContent] = Action.async {
    val favouriteProduct = favouriteProductRepo.read(id)
    favouriteProduct.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("ERROR --> favouriteProduct can't be found")
    }
  }

  def readFavouriteProductByUser(providerKey: String): Action[AnyContent] = Action.async {
    val favouriteProduct = favouriteProductRepo.readByUser(providerKey)
    favouriteProduct.map { favouriteProduct =>
      Ok(Json.toJson(favouriteProduct))
    }
  }

  def readAllFavouriteProduct(): Action[AnyContent] = Action.async {
    val favouriteProduct = favouriteProductRepo.readAll()
    favouriteProduct.map { favouriteProduct =>
      Ok(Json.toJson(favouriteProduct))
    }
  }

  def updateFavouriteProduct(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[FavouriteProduct].map {
      favouriteProduct =>
        favouriteProductRepo.update(favouriteProduct.id, favouriteProduct).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("ERROR --> invalid json")))
  }

  def deleteFavouriteProduct(id: Long): Action[AnyContent] = Action.async {
    favouriteProductRepo.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }
}
