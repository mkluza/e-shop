package controllers

import models.Address
import models.User
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import services.{AddressRepository, UserRepository}
import javax.inject._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class AddressViewController @Inject()(addressRepo: AddressRepository, userRepo: UserRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/address/read-all";
  var userList: Seq[User] = Seq[User]()
  userRepo.getAll.onComplete {
    case Success(user) => userList = user
    case Failure(e) => print("ERROR --> userList", e)
  }

  def readAllAddress: Action[AnyContent] = Action.async { implicit request =>
    addressRepo.readAll().map(address => Ok(views.html.AddressReadAll(address)))
  }

  def createAddress(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val users = userRepo.getAll
    users.map(users => Ok(views.html.AddressCreate(addressForm, users)))
  }

  def createAddressHandle(): Action[AnyContent] = Action.async { implicit request =>
    addressForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.AddressCreate(errorForm, userList))
        )
      },
      address => {
        addressRepo.create(address.providerKey, address.firstname, address.lastname, address.city, address.zipcode, address.street, address.phoneNumber).map { _ =>
          Redirect(url)
        }
      }
    )
  }

  def updateAddress(providerKey: String): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val address = addressRepo.read(providerKey)
    address.map(address => {
      val prodForm = updateAddressForm.fill(UpdateAddressForm(address.get.id, address.get.providerKey, address.get.firstname, address.get.lastname, address.get.city, address.get.zipcode, address.get.street, address.get.phoneNumber))
      Ok(views.html.AddressUpdate(prodForm, userList))
    })
  }

  def updateAddressHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateAddressForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.AddressUpdate(errorForm, userList))
        )
      },
      address => {
        addressRepo.update(address.id, Address(address.id, address.providerKey, address.firstname, address.lastname, address.city, address.zipcode, address.street, address.phoneNumber)).map { _ =>
          Redirect(url)
        }
      }
    )
  }

  def deleteAddress(id: Long): Action[AnyContent] = Action {
    addressRepo.delete(id)
    Redirect(url)
  }

  // utilities

  val addressForm: Form[CreateAddressForm] = Form {
    mapping(
      "providerKey" -> nonEmptyText,
      "firstname" -> nonEmptyText,
      "lastname" -> nonEmptyText,
      "city" -> nonEmptyText,
      "zipcode" -> nonEmptyText,
      "street" -> nonEmptyText,
      "phoneNumber" -> nonEmptyText,
    )(CreateAddressForm.apply)(CreateAddressForm.unapply)
  }

  val updateAddressForm: Form[UpdateAddressForm] = Form {
    mapping(
      "id" -> longNumber,
      "providerKey" -> nonEmptyText,
      "firstname" -> nonEmptyText,
      "lastname" -> nonEmptyText,
      "city" -> nonEmptyText,
      "zipcode" -> nonEmptyText,
      "street" -> nonEmptyText,
      "phoneNumber" -> nonEmptyText,

    )(UpdateAddressForm.apply)(UpdateAddressForm.unapply)
  }
}

case class CreateAddressForm(providerKey: String, firstname: String, lastname: String, city: String, zipcode: String, street: String, phoneNumber: String)

case class UpdateAddressForm(id: Long, providerKey: String, firstname: String, lastname: String, city: String, zipcode: String, street: String, phoneNumber: String)
