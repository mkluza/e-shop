package services

import models.Bought
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.annotation.meta.TypeQualifierNickname
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BoughtRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository, val orderRepository: OrderRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class BoughtTable(tag: Tag) extends Table[Bought](tag, "bought_t") {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def providerKey = column[String]("providerKey")
    def providerKeyFk = foreignKey("userFk", providerKey, user_t)(_.providerKey)

    def orderId = column[Long]("orderId")
    def orderIdFk = foreignKey("orderFk", orderId, order_t)(_.id)

    def * = (id, providerKey, orderId) <> ((Bought.apply _).tupled, Bought.unapply)
  }

  import userRepository.UserTable
  import orderRepository.OrderTable

  val bought = TableQuery[BoughtTable]
  val user_t = TableQuery[UserTable]
  val order_t = TableQuery[OrderTable]

  def create(providerKey: String, orderId: Long): Future[Bought] = db.run {
    (bought.map(b => (b.providerKey, b.orderId))
      returning bought.map(_.id)
      into {case ((providerKey, orderId),id) => Bought(id, providerKey, orderId)}
      ) += (providerKey, orderId)
  }

  def read(id: Long): Future[Option[Bought]] = db.run {
    bought.filter(_.id === id).result.headOption
  }

  def readByUserId(providerKey: String): Future[Seq[Bought]] = db.run {
    bought.filter(_.providerKey === providerKey).result
  }

  def readAll(): Future[Seq[Bought]] = db.run {
    bought.result
  }

  def update(id: Long, updatedBought: Bought): Future[Int] = {
    val boughtTOupdate: Bought = updatedBought.copy(id)
    db.run(bought.filter(_.id === id).update(boughtTOupdate))
  }

  def delete(id: Long): Future[Int] = db.run(bought.filter(_.id === id).delete)
}