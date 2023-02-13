package controllers

import models.Category
import services.CategoryRepository

import javax.inject._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import play.api.libs.json.{JsValue, Json}

@Singleton
class CategoryController @Inject()(categoryRepo: CategoryRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def createCategory(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Category].map {
      category =>
        categoryRepo.create(category.name).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("ERROR --> incorrect data")))
  }

  def readCategory(id: Long): Action[AnyContent] = Action.async {
    val category = categoryRepo.read(id)
    category.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("ERROR --> can't be found")
    }
  }

  def readAllCategories(): Action[AnyContent] = Action.async {
    val categories = categoryRepo.readAll()
    categories.map { categories =>
      Ok(Json.toJson(categories))
    }
  }

  def updateCategory(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Category].map {
      category =>
        categoryRepo.update(category.id, category).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("ERROR --> invalid json")))
  }

  def deleteCategory(id: Long): Action[AnyContent] = Action.async {
    categoryRepo.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }
}

