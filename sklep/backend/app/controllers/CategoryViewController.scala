package controllers

import models.Category
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import services.CategoryRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryViewController @Inject()(categoryRepo: CategoryRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val url = "/form/category/read-all";
  def readAllCategories: Action[AnyContent] = Action.async { implicit request =>
    categoryRepo.readAll().map(categories => Ok(views.html.CategoryReadAll(categories)))
  }

  def createCategory(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val categories = categoryRepo.readAll()
    categories.map(_ => Ok(views.html.CategoryCreate(categoryForm)))
  }

  def createCategoryHandle(): Action[AnyContent] = Action.async { implicit request =>
    categoryForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.CategoryCreate(errorForm))
        )
      },
      category => {
        categoryRepo.create(category.name).map { _ =>
          Redirect(url)
        }
      }
    )
  }

  def updateCategory(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val category = categoryRepo.read(id)
    category.map(category => {
      val prodForm = updateCategoryForm.fill(UpdateCategoryForm(category.get.id, category.get.name))
      Ok(views.html.CategoryUpdate(prodForm))
    })
  }

  def updateCategoryHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateCategoryForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.CategoryUpdate(errorForm))
        )
      },
      category => {
        categoryRepo.update(category.id, Category(category.id, category.name)).map { _ =>
          Redirect(url)
        }
      }
    )
  }

  def deleteCategory(id: Long): Action[AnyContent] = Action {
    categoryRepo.delete(id)
    Redirect(url)
  }

  // utilities

  val categoryForm: Form[CreateCategoryForm] = Form {
    mapping(
      "name" -> nonEmptyText,
    )(CreateCategoryForm.apply)(CreateCategoryForm.unapply)
  }

  val updateCategoryForm: Form[UpdateCategoryForm] = Form {
    mapping(
      "id" -> longNumber,
      "name" -> nonEmptyText,
    )(UpdateCategoryForm.apply)(UpdateCategoryForm.unapply)
  }
}

case class CreateCategoryForm(name: String)

case class UpdateCategoryForm(id: Long, name: String)
