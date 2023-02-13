package controllers

import models.{Category, Product, User}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import services.ProductRepository
import services.CategoryRepository
import javax.inject._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class ProductViewController @Inject()(productRepo: ProductRepository, categoryRepo: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/product/read-all";
  var categoryList: Seq[Category] = Seq[Category]()
  categoryRepo.readAll().onComplete {
    case Success(categories) => categoryList = categories
    case Failure(e) => print("ERROR --> categoryList", e)
  }

  def readAllProduct: Action[AnyContent] = Action.async { implicit request =>
    productRepo.readAll().map(products => Ok(views.html.ProductReadAll(products)))
  }

  def createProduct(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val categories = categoryRepo.readAll()
    categories.map(categories => Ok(views.html.ProductCreate(productForm, categories)))
  }

  def createProductHandle(): Action[AnyContent] = Action.async { implicit request =>
    productForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.ProductCreate(errorForm, categoryList))
        )
      },
      product => {
        productRepo.create(product.categoryId, product.name, product.description, product.price, product.image).map { _ =>
          Redirect(url)
        }
      }
    )
  }

  def updateProduct(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val product = productRepo.read(id)
    product.map(product => {
      val prodForm = updateProductForm.fill(UpdateProductForm(product.get.id, product.get.categoryId, product.get.name, product.get.description, product.get.price, product.get.image))
      Ok(views.html.ProductUpdate(prodForm, categoryList))
    })
  }

  def updateProductHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateProductForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.ProductUpdate(errorForm, categoryList))
        )
      },
      product => {
        productRepo.update(product.id, Product(product.id, product.categoryId, product.name, product.description, product.price, product.image)).map { _ =>
          Redirect(url)
        }
      }
    )
  }

  def deleteProduct(id: Long): Action[AnyContent] = Action {
    productRepo.delete(id)
    Redirect(url)
  }

  // utilities

  val productForm: Form[CreateProductForm] = Form {
    mapping(
      "categoryId" -> longNumber,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "price" -> nonEmptyText,
      "image" -> nonEmptyText,
    )(CreateProductForm.apply)(CreateProductForm.unapply)
  }

  val updateProductForm: Form[UpdateProductForm] = Form {
    mapping(
      "id" -> longNumber,
      "categoryId" -> longNumber,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "price" -> nonEmptyText,
      "image" -> nonEmptyText,
    )(UpdateProductForm.apply)(UpdateProductForm.unapply)
  }
}

case class CreateProductForm(categoryId: Long, name: String, description: String, price: String, image: String)

case class UpdateProductForm(id: Long, categoryId: Long, name: String, description: String, price: String, image: String)
