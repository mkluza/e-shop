package services

import models.Order
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.annotation.meta.TypeQualifierNickname
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val cartRepository: CartRepository, val addressRepository: AddressRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class OrderTable(tag: Tag) extends Table[Order](tag, "order_t") {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def cartId = column[Long]("cartId")
    def cartIdFk = foreignKey("cartFk", cartId, cart_t)(_.id)

    def addressId = column[Long]("addressId")
    def addressIdFk = foreignKey("addressFk", addressId, address_t)(_.id)


    def * = (id, cartId, addressId) <> ((Order.apply _).tupled, Order.unapply)
  }

  import cartRepository.CartTable
  import addressRepository.AddressTable

  val order = TableQuery[OrderTable]
  val cart_t = TableQuery[CartTable]
  val address_t = TableQuery[AddressTable]

  def create(cartId: Long, addressId: Long): Future[Order] = db.run {
    (order.map(or => (or.cartId, or.addressId))
      returning order.map(_.id)
      into {case ((cartId, addressId),id) => Order(id, cartId, addressId)}
      ) += (cartId, addressId)
  }

  def read(id: Long): Future[Option[Order]] = db.run {
    order.filter(_.id === id).result.headOption
  }

  def readAll(): Future[Seq[Order]] = db.run {
    order.result
  }

  def update(id: Long, updatedOrder: Order): Future[Int] = {
    val orderTOupdate: Order = updatedOrder.copy(id)
    db.run(order.filter(_.id === id).update(orderTOupdate))
  }

  def delete(id: Long): Future[Int] = db.run(order.filter(_.id === id).delete)
}