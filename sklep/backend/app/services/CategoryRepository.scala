package services

import models.Category
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CategoryTable(tag: Tag) extends Table[Category](tag, "category_t") {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name: Rep[String] = column[String]("name")

    def * = (id, name) <> ((Category.apply _).tupled, Category.unapply)
  }

  val category = TableQuery[CategoryTable]

  def create(name: String): Future[Category] = db.run {
    (category.map(c => (c.name))
      returning category.map(_.id)
      into {case ((name),id) => Category(id, name)}
      ) += (name)
  }

  def read(id: Long): Future[Option[Category]] = db.run {
    category.filter(_.id === id).result.headOption
  }

  def readAll(): Future[Seq[Category]] = db.run {
    category.result
  }

  def update(id: Long, updatedCategory: Category): Future[Int] = {
    val categoryTOupdate: Category = updatedCategory.copy(id)
    db.run(category.filter(_.id === id).update(categoryTOupdate))
  }

  def delete(id: Long): Future[Int] = db.run(category.filter(_.id === id).delete)
}