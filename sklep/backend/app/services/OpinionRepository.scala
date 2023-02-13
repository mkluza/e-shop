package services

import models.Opinion
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.annotation.meta.TypeQualifierNickname
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OpinionRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository, val productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class OpinionTable(tag: Tag) extends Table[Opinion](tag, "opinion_t") {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def providerKey = column[String]("providerKey")
    def providerKeyFk = foreignKey("userFk", providerKey, user_t)(_.providerKey)

    def productId = column[Long]("productId")
    def productIdFk = foreignKey("productFk", productId, product_t)(_.id)

    def message: Rep[String] = column[String]("message")

    def * = (id, providerKey, productId, message) <> ((Opinion.apply _).tupled, Opinion.unapply)
  }

  import userRepository.UserTable
  import productRepository.ProductTable

  val opinion = TableQuery[OpinionTable]
  val user_t = TableQuery[UserTable]
  val product_t = TableQuery[ProductTable]

  def create(providerKey: String, productId: Long, message: String): Future[Opinion] = db.run {
    (opinion.map(op => (op.providerKey, op.productId, op.message))
      returning opinion.map(_.id)
      into {case ((providerKey, productId, message),id) => Opinion(id, providerKey, productId, message)}
      ) += (providerKey, productId, message)
  }

  def read(id: Long): Future[Option[Opinion]] = db.run {
    opinion.filter(_.id === id).result.headOption
  }

  def readByUser(providerKey: String): Future[Seq[Opinion]] = db.run {
    opinion.filter(_.providerKey === providerKey).result
  }

  def readByProduct(productId: Long): Future[Seq[Opinion]] = db.run {
    opinion.filter(_.productId === productId).result
  }

  def readAll(): Future[Seq[Opinion]] = db.run {
    opinion.result
  }

  def update(id: Long, updatedOpinion: Opinion): Future[Int] = {
    val opinionTOupdate: Opinion = updatedOpinion.copy(id)
    db.run(opinion.filter(_.id === id).update(opinionTOupdate))
  }

  def delete(id: Long): Future[Int] = db.run(opinion.filter(_.id === id).delete)
}