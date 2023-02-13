package controllers

import models.FavouriteProduct
import models.User
import models.Product
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import services.{FavouriteProductRepository, ProductRepository, UserRepository}
import javax.inject._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class FavouriteProductViewController @Inject()(favouriteProductRepo: FavouriteProductRepository, userRepo: UserRepository, productRepo: ProductRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/fav-product/read-all";
  var userList: Seq[User] = Seq[User]()
  var productList: Seq[Product] = Seq[Product]()
  userRepo.getAll.onComplete {
    case Success(users) => userList = users
    case Failure(e) => print("ERROR --> userList", e)
  }
  productRepo.readAll().onComplete {
    case Success(products) => productList = products
    case Failure(e) => print("ERROR --> productList", e)
  }


  def readAllFavouriteProduct: Action[AnyContent] = Action.async { implicit request =>
    favouriteProductRepo.readAll().map(favouriteProduct => Ok(views.html.FavouriteProductReadAll(favouriteProduct)))
  }

  def createFavouriteProduct(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val users = Await.result(userRepo.getAll, 1.second)
    val products = productRepo.readAll()
    products.map(products => Ok(views.html.FavouriteProductCreate(favouriteProductForm, users, products)))
  }

  def createFavouriteProductHandle(): Action[AnyContent] = Action.async { implicit request =>
    favouriteProductForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.FavouriteProductCreate(errorForm, userList, productList))
        )
      },
      favouriteProduct => {
        favouriteProductRepo.create(favouriteProduct.providerKey, favouriteProduct.productId).map { _ =>
          Redirect(url)
        }
      }
    )
  }

  def updateFavouriteProduct(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val favouriteProduct = favouriteProductRepo.read(id)
    favouriteProduct.map(favouriteProduct => {
      val prodForm = updateFavouriteProductForm.fill(UpdateFavouriteProductForm(favouriteProduct.get.id, favouriteProduct.get.providerKey, favouriteProduct.get.productId))
      Ok(views.html.FavouriteProductUpdate(prodForm, userList, productList))
    })
  }

  def updateFavouriteProductHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateFavouriteProductForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.FavouriteProductUpdate(errorForm, userList, productList))
        )
      },
      favouriteProduct => {
        favouriteProductRepo.update(favouriteProduct.id, FavouriteProduct(favouriteProduct.id, favouriteProduct.providerKey, favouriteProduct.productId)).map { _ =>
          Redirect(url)
        }
      }
    )
  }

  def deleteFavouriteProduct(id: Long): Action[AnyContent] = Action {
    favouriteProductRepo.delete(id)
    Redirect(url)
  }

  // utilities

  val favouriteProductForm: Form[CreateFavouriteProductForm] = Form {
    mapping(
      "providerKey" -> nonEmptyText,
      "productId" -> longNumber,
    )(CreateFavouriteProductForm.apply)(CreateFavouriteProductForm.unapply)
  }

  val updateFavouriteProductForm: Form[UpdateFavouriteProductForm] = Form {
    mapping(
      "id" -> longNumber,
      "providerKey" -> nonEmptyText,
      "productId" -> longNumber,
    )(UpdateFavouriteProductForm.apply)(UpdateFavouriteProductForm.unapply)
  }
}

case class CreateFavouriteProductForm(providerKey: String, productId: Long)

case class UpdateFavouriteProductForm(id: Long, providerKey: String, productId: Long)
