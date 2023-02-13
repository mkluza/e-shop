package services

import models.Cart
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.annotation.meta.TypeQualifierNickname
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CartRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository, val productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CartTable(tag: Tag) extends Table[Cart](tag, "cart_t") {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def providerKey = column[String]("providerKey")
    def providerKeyFk = foreignKey("userFk", providerKey, user_t)(_.providerKey)

    def productId = column[Long]("productId")
    def productIdFk = foreignKey("productFk", productId, product_t)(_.id)

    def amount: Rep[Long] = column[Long]("amount")

    def * = (id, providerKey, productId, amount) <> ((Cart.apply _).tupled, Cart.unapply)
  }

  import userRepository.UserTable
  import productRepository.ProductTable

  val cart = TableQuery[CartTable]
  val user_t = TableQuery[UserTable]
  val product_t = TableQuery[ProductTable]

  def create(providerKey: String, productId: Long, amount: Long): Future[Cart] = db.run {
    (cart.map(c => (c.providerKey, c.productId, c.amount))
      returning cart.map(_.id)
      into {case ((providerKey, productId, amount),id) => Cart(id, providerKey, productId, amount)}
      ) += (providerKey, productId, amount)
  }

  def read(id: Long): Future[Option[Cart]] = db.run {
    cart.filter(_.id === id).result.headOption
  }

  def readByUser(providerKey: String): Future[Seq[Cart]] = db.run {
    cart.filter(_.providerKey === providerKey).result
  }

  def readAll(): Future[Seq[Cart]] = db.run {
    cart.result
  }

  def update(id: Long, updatedCart: Cart): Future[Int] = {
    val cartTOupdate: Cart = updatedCart.copy(id)
    db.run(cart.filter(_.id === id).update(cartTOupdate))
  }

  def delete(id: Long): Future[Int] = db.run(cart.filter(_.id === id).delete)
}