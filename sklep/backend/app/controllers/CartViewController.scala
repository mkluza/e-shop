package controllers

import models.Cart
import models.User
import models.Product
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import services.{CartRepository, ProductRepository, UserRepository}
import javax.inject._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class CartViewController @Inject()(cartRepo: CartRepository, userRepo: UserRepository, productRepo: ProductRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/cart/read-all";
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


  def readAllCart: Action[AnyContent] = Action.async { implicit request =>
    cartRepo.readAll().map(carts => Ok(views.html.CartReadAll(carts)))
  }

  def createCart(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val users = Await.result(userRepo.getAll, 1.second)
    val products = productRepo.readAll()

    products.map(products => Ok(views.html.CartCreate(cartForm, users, products)))
  }

  def createCartHandle(): Action[AnyContent] = Action.async { implicit request =>
    cartForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.CartCreate(errorForm, userList, productList))
        )
      },
      cart => {
        cartRepo.create(cart.providerKey, cart.productId, cart.amount).map { _ =>
          Redirect(url)
        }
      }
    )
  }

  def updateCart(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val cart = cartRepo.read(id)
    cart.map(cart => {
      val prodForm = updateCartForm.fill(UpdateCartForm(cart.get.id, cart.get.providerKey, cart.get.productId, cart.get.amount))
      Ok(views.html.CartUpdate(prodForm, userList, productList))
    })
  }

  def updateCartHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateCartForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.CartUpdate(errorForm, userList, productList))
        )
      },
      cart => {
        cartRepo.update(cart.id, Cart(cart.id, cart.providerKey, cart.productId, cart.amount)).map { _ =>
          Redirect(url)
        }
      }
    )
  }

  def deleteCart(id: Long): Action[AnyContent] = Action {
    cartRepo.delete(id)
    Redirect(url)
  }

  // utilities

  val cartForm: Form[CreateCartForm] = Form {
    mapping(
      "providerKey" -> nonEmptyText,
      "productId" -> longNumber,
      "amount" -> longNumber,
    )(CreateCartForm.apply)(CreateCartForm.unapply)
  }

  val updateCartForm: Form[UpdateCartForm] = Form {
    mapping(
      "id" -> longNumber,
      "providerKey" -> nonEmptyText,
      "productId" -> longNumber,
      "amount" -> longNumber,
    )(UpdateCartForm.apply)(UpdateCartForm.unapply)
  }
}

case class CreateCartForm(providerKey: String, productId: Long, amount: Long)

case class UpdateCartForm(id: Long, providerKey: String, productId: Long, amount: Long)
