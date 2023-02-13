package controllers

import models.Bought
import models.User
import models.Order
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import services.{BoughtRepository, OrderRepository, UserRepository}
import javax.inject._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class BoughtViewController @Inject()(boughtRepo: BoughtRepository, userRepo: UserRepository, orderRepo: OrderRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/bought/read-all";
  var userList: Seq[User] = Seq[User]()
  var orderList: Seq[Order] = Seq[Order]()
  userRepo.getAll.onComplete {
    case Success(users) => userList = users
    case Failure(e) => print("ERROR --> userList", e)
  }
  orderRepo.readAll().onComplete {
    case Success(orders) => orderList = orders
    case Failure(e) => print("ERROR --> orderList", e)
  }

  def readAllBought: Action[AnyContent] = Action.async { implicit request =>
    boughtRepo.readAll().map(bought => Ok(views.html.BoughtReadAll(bought)))
  }

  def createBought(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val users = Await.result(userRepo.getAll, 1.second)
    val orders = orderRepo.readAll()

    orders.map(orders => Ok(views.html.BoughtCreate(boughtForm, users, orders)))
  }

  def createBoughtHandle(): Action[AnyContent] = Action.async { implicit request =>
    boughtForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.BoughtCreate(errorForm, userList, orderList))
        )
      },
      bought => {
        boughtRepo.create(bought.providerKey, bought.orderId).map { _ =>
          Redirect(url)
        }
      }
    )
  }

  def updateBought(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val bought = boughtRepo.read(id)
    bought.map(bought => {
      val prodForm = updateBoughtForm.fill(UpdateBoughtForm(bought.get.id, bought.get.providerKey, bought.get.orderId))
      Ok(views.html.BoughtUpdate(prodForm, userList, orderList))
    })
  }

  def updateBoughtHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateBoughtForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.BoughtUpdate(errorForm, userList, orderList))
        )
      },
      bought => {
        boughtRepo.update(bought.id, Bought(bought.id, bought.providerKey, bought.orderId)).map { _ =>
          Redirect(url)
        }
      }
    )
  }

  def deleteBought(id: Long): Action[AnyContent] = Action {
    boughtRepo.delete(id)
    Redirect(url)
  }

  // utilities

  val boughtForm: Form[CreateBoughtForm] = Form {
    mapping(
      "providerKey" -> nonEmptyText,
      "orderId" -> longNumber,
    )(CreateBoughtForm.apply)(CreateBoughtForm.unapply)
  }

  val updateBoughtForm: Form[UpdateBoughtForm] = Form {
    mapping(
      "id" -> longNumber,
      "providerKey" -> nonEmptyText,
      "orderId" -> longNumber,
    )(UpdateBoughtForm.apply)(UpdateBoughtForm.unapply)
  }
}

case class CreateBoughtForm(providerKey: String, orderId: Long)

case class UpdateBoughtForm(id: Long, providerKey: String, orderId: Long)
