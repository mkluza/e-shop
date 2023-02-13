package services

import models.FavouriteProduct
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.annotation.meta.TypeQualifierNickname
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FavouriteProductRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository, val productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class FavouriteProductTable(tag: Tag) extends Table[FavouriteProduct](tag, "favourite_product_t") {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def providerKey = column[String]("providerKey")
    def providerKeyFk = foreignKey("userFk", providerKey, user_t)(_.providerKey)

    def productId = column[Long]("productId")
    def productIdFk = foreignKey("productFk", productId, product_t)(_.id)


    def * = (id, providerKey, productId) <> ((FavouriteProduct.apply _).tupled, FavouriteProduct.unapply)
  }

  import userRepository.UserTable
  import productRepository.ProductTable

  val favourite_product = TableQuery[FavouriteProductTable]
  val user_t = TableQuery[UserTable]
  val product_t = TableQuery[ProductTable]

  def create(providerKey: String, productId: Long): Future[FavouriteProduct] = db.run {
    (favourite_product.map(fp => (fp.providerKey, fp.productId))
      returning favourite_product.map(_.id)
      into {case ((providerKey, productId),id) => FavouriteProduct(id, providerKey, productId)}
      ) += (providerKey, productId)
  }

  def read(id: Long): Future[Option[FavouriteProduct]] = db.run {
    favourite_product.filter(_.id === id).result.headOption
  }

  def readByUser(providerKey: String): Future[Seq[FavouriteProduct]] = db.run {
    favourite_product.filter(_.providerKey === providerKey).result
  }

  def readAll(): Future[Seq[FavouriteProduct]] = db.run {
    favourite_product.result
  }

  def update(id: Long, updatedFavouriteProduct: FavouriteProduct): Future[Int] = {
    val favouriteProductTOupdate: FavouriteProduct = updatedFavouriteProduct.copy(id)
    db.run(favourite_product.filter(_.id === id).update(favouriteProductTOupdate))
  }

  def delete(id: Long): Future[Int] = db.run(favourite_product.filter(_.id === id).delete)
}