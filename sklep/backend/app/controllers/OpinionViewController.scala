package controllers

import models.Opinion
import models.User
import models.Product
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import services.{OpinionRepository, ProductRepository, UserRepository}
import javax.inject._

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class OpinionViewController @Inject()(opinionRepo: OpinionRepository, userRepo: UserRepository, productRepo: ProductRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/opinion/read-all";
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

  def readAllOpinion: Action[AnyContent] = Action.async { implicit request =>
    opinionRepo.readAll().map(opinions => Ok(views.html.OpinionReadAll(opinions)))
  }

  def createOpinion(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val users = Await.result(userRepo.getAll, 1.second)
    val products = productRepo.readAll()
    products.map(products => Ok(views.html.OpinionCreate(opinionForm, users, products)))
  }

  def createOpinionHandle(): Action[AnyContent] = Action.async { implicit request =>
    opinionForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.OpinionCreate(errorForm, userList, productList))
        )
      },
      opinion => {
        opinionRepo.create(opinion.providerKey, opinion.productId, opinion.message).map { _ =>
          Redirect(url)
        }
      }
    )
  }

  def updateOpinion(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val opinion = opinionRepo.read(id)
    opinion.map(opinion => {
      val prodForm = updateOpinionForm.fill(UpdateOpinionForm(opinion.get.id, opinion.get.providerKey, opinion.get.productId, opinion.get.message))
      Ok(views.html.OpinionUpdate(prodForm, userList, productList))
    })
  }

  def updateOpinionHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateOpinionForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.OpinionUpdate(errorForm, userList, productList))
        )
      },
      opinion => {
        opinionRepo.update(opinion.id, Opinion(opinion.id, opinion.providerKey, opinion.productId, opinion.message)).map { _ =>
          Redirect(url)
        }
      }
    )
  }

  def deleteOpinion(id: Long): Action[AnyContent] = Action {
    opinionRepo.delete(id)
    Redirect(url)
  }

  // utilities

  val opinionForm: Form[CreateOpinionForm] = Form {
    mapping(
      "providerKey" -> nonEmptyText,
      "productId" -> longNumber,
      "message" -> nonEmptyText,
    )(CreateOpinionForm.apply)(CreateOpinionForm.unapply)
  }

  val updateOpinionForm: Form[UpdateOpinionForm] = Form {
    mapping(
      "id" -> longNumber,
      "providerKey" -> nonEmptyText,
      "productId" -> longNumber,
      "message" -> nonEmptyText,
    )(UpdateOpinionForm.apply)(UpdateOpinionForm.unapply)
  }
}

case class CreateOpinionForm(providerKey: String, productId: Long, message: String)

case class UpdateOpinionForm(id: Long, providerKey: String, productId: Long, message: String)
