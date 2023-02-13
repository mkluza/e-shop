package services

import models.Product
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.annotation.meta.TypeQualifierNickname
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class ProductTable(tag: Tag) extends Table[Product](tag, "product_t") {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def categoryId = column[Long]("categoryId")
    def categoryIdFk = foreignKey("categoryFk", categoryId, category_t)(_.id)

    def name: Rep[String] = column[String]("name")
    def description: Rep[String] = column[String]("description")
    def price: Rep[String] = column[String]("price")
    def image: Rep[String] = column[String]("image")

    def * = (id, categoryId, name, description, price, image) <> ((Product.apply _).tupled, Product.unapply)
  }

  import categoryRepository.CategoryTable

  val product = TableQuery[ProductTable]
  val category_t = TableQuery[CategoryTable]

  def create(categoryId: Long, name: String, description: String, price: String, image: String): Future[Product] = db.run {
    (product.map(p => (p.categoryId, p.name, p.description, p.price, p.image))
      returning product.map(_.id)
      into {case ((categoryId, name, description, price, image),id) => Product(id, categoryId, name, description, price, image)}
      ) += (categoryId, name, description, price, image)
  }

  def read(id: Long): Future[Option[Product]] = db.run {
    product.filter(_.id === id).result.headOption
  }

  def readByCategory(categoryId: Long): Future[Seq[Product]] = db.run {
    product.filter(_.categoryId === categoryId).result
  }

  def readAll(): Future[Seq[Product]] = db.run {
    product.result
  }

  def update(id: Long, updatedProduct: Product): Future[Int] = {
    val productTOupdate: Product = updatedProduct.copy(id)
    db.run(product.filter(_.id === id).update(productTOupdate))
  }

  def delete(id: Long): Future[Int] = db.run(product.filter(_.id === id).delete)
}