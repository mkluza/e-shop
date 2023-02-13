package services

import models.Address
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import javax.annotation.meta.TypeQualifierNickname
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AddressRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class AddressTable(tag: Tag) extends Table[Address](tag, "address_t") {

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def providerKey = column[String]("providerKey")
    def providerKeyFK = foreignKey("userFk", providerKey, user_t)(_.providerKey)

    def firstname: Rep[String] = column[String]("firstname")
    def lastname: Rep[String] = column[String]("lastname")
    def city: Rep[String] = column[String]("city")
    def zipcode: Rep[String] = column[String]("zipcode")
    def street: Rep[String] = column[String]("street")
    def phoneNumber: Rep[String] = column[String]("phoneNumber")

    def * = (id, providerKey, firstname, lastname, city, zipcode, street, phoneNumber) <> ((Address.apply _).tupled, Address.unapply)
  }

  import userRepository.UserTable

  val address = TableQuery[AddressTable]
  val user_t = TableQuery[UserTable]

  def create(providerKey: String, firstname: String, lastname: String, city: String, zipcode: String, street: String, phoneNumber: String): Future[Address] = db.run {
    (address.map(ad => (ad.providerKey, ad.firstname, ad.lastname, ad.city, ad.zipcode, ad.street, ad.phoneNumber))
      returning address.map(_.id)
      into {case ((providerKey, firstname, lastname, city, zipcode, street, phoneNumber),id) => Address(id, providerKey, firstname, lastname, city, zipcode, street, phoneNumber)}
      ) += (providerKey, firstname, lastname, city, zipcode, street, phoneNumber)
  }

  def read(providerKey: String): Future[Option[Address]] = db.run {
    address.filter(_.providerKey === providerKey).result.headOption
  }

  def readAll(): Future[Seq[Address]] = db.run {
    address.result
  }

  def update(id: Long, updatedAddress: Address): Future[Int] = {
    val addressTOupdate: Address = updatedAddress.copy(id)
    db.run(address.filter(_.id === id).update(addressTOupdate))
  }

  def delete(id: Long): Future[Int] = db.run(address.filter(_.id === id).delete)
}