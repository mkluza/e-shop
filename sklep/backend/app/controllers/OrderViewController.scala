package controllers

import models.Order
import models.Cart
import models.Address

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import services.OrderRepository
import services.CartRepository
import services.AddressRepository

import javax.inject._
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class OrderViewController @Inject()(orderRepo: OrderRepository, cartRepo: CartRepository, addressRepo: AddressRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/order/read-all";
  var cartList: Seq[Cart] = Seq[Cart]()
  var addressList: Seq[Address] = Seq[Address]()

  cartRepo.readAll().onComplete {
    case Success(carts) => cartList = carts
    case Failure(e) => print("ERROR --> cartList", e)
  }
  addressRepo.readAll().onComplete {
    case Success(addresses) => addressList = addresses
    case Failure(e) => print("ERROR --> addressList", e)
  }


  def readAllOrder: Action[AnyContent] = Action.async { implicit request =>
    orderRepo.readAll().map(orders => Ok(views.html.OrderReadAll(orders)))
  }

  def createOrder(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val carts = Await.result(cartRepo.readAll(), 1.second)
    val addresses = addressRepo.readAll()
    addresses.map(addresses => Ok(views.html.OrderCreate(orderForm, carts, addresses)))
  }

  def createOrderHandle(): Action[AnyContent] = Action.async { implicit request =>
    orderForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.OrderCreate(errorForm, cartList, addressList))
        )
      },
      order => {
        orderRepo.create(order.cartId, order.addressId).map { _ =>
          Redirect(url)
        }
      }
    )
  }

  def updateOrder(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val order = orderRepo.read(id)
    order.map(order => {
      val prodForm = updateOrderForm.fill(UpdateOrderForm(order.get.id, order.get.cartId, order.get.addressId))
      Ok(views.html.OrderUpdate(prodForm, cartList, addressList))
    })
  }

  def updateOrderHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateOrderForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.OrderUpdate(errorForm, cartList, addressList))
        )
      },
      order => {
        orderRepo.update(order.id, Order(order.id, order.cartId, order.addressId)).map { _ =>
          Redirect(url)
        }
      }
    )
  }

  def deleteOrder(id: Long): Action[AnyContent] = Action {
    orderRepo.delete(id)
    Redirect(url)
  }

  // utilities

  val orderForm: Form[CreateOrderForm] = Form {
    mapping(
      "cartId" -> longNumber,
      "addressId" -> longNumber,
    )(CreateOrderForm.apply)(CreateOrderForm.unapply)
  }

  val updateOrderForm: Form[UpdateOrderForm] = Form {
    mapping(
      "id" -> longNumber,
      "cartId" -> longNumber,
      "addressId" -> longNumber,
    )(UpdateOrderForm.apply)(UpdateOrderForm.unapply)
  }
}

case class CreateOrderForm(cartId: Long, addressId: Long)

case class UpdateOrderForm(id: Long, cartId: Long, addressId: Long)
