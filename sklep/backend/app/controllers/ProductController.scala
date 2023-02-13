package controllers

import models.Product
import services.ProductRepository
import services.CategoryRepository

import com.fasterxml.jackson.core.JsonGenerator.Feature
import javax.inject._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import play.api.libs.json.{JsValue, Json}


@Singleton
class ProductController @Inject()(productRepo: ProductRepository, val categoryRepo: CategoryRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  def createProduct(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Product].map {
      product =>
        productRepo.create(product.categoryId, product.name, product.description, product.price, product.image).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("ERROR --> incorrect data")))
  }

  def readProduct(id: Long): Action[AnyContent] = Action.async {
    val product = productRepo.read(id)
    product.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("ERROR --> can't be found")
    }
  }

  def readProductByCategory(categoryId: Long): Action[AnyContent] = Action.async {
    val products = productRepo.readByCategory(categoryId)
    products.map { products =>
      Ok(Json.toJson(products))
    }
  }

  def readAllProducts(): Action[AnyContent] = Action.async {
    val products = productRepo.readAll()
    products.map { products =>
      Ok(Json.toJson(products))
    }
  }

  def updateProduct(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[Product].map {
      product =>
        productRepo.update(product.id, product).map { res =>
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("ERROR --> invalid json")))
  }

  def deleteProduct(id: Long): Action[AnyContent] = Action.async {
    productRepo.delete(id).map { res =>
      Ok(Json.toJson(res))
    }
  }
}

