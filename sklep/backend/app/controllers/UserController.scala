package controllers

import com.mohiva.play.silhouette.api.LoginInfo
import play.api.libs.json.Json
import play.api.mvc._
import javax.inject._
import services.UserRepository

import scala.concurrent.ExecutionContext

class UserController @Inject()(userRepo: UserRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  def getUsersOauth(loginInfo: LoginInfo) = Action.async {
    val user = userRepo.retrieve(loginInfo)
    user.map { seq =>
      Ok(Json.toJson(seq))
    }
  }

  def getUserByKey(key: String): Action[AnyContent] = Action.async {
    val user = userRepo.getByKey(key)
    user.map {
      user => Ok(Json.toJson(user))
    }
  }
}
