package xed.onboarding.repository

import java.util.OptionalInt

import com.twitter.util.Future
import xed.onboarding.domain.response.PageResult
import xed.onboarding.domain.{UserID, UserInfo}

import scala.collection.mutable.ListBuffer

trait UserRepository{
  def create(user: UserInfo):Future[Option[UserInfo]]
  def get(userId: String): Future[Option[UserInfo]]
  def update(user: UserInfo): Future[Boolean]
  def delete(userId: String): Future[Boolean]
  def getAll(from: Int, size: Int): Future[PageResult[UserInfo]]
}

class LocalUserRepository extends UserRepository {
  var db:ListBuffer[UserInfo] = ListBuffer()

  override def create(user: UserInfo): Future[Option[UserInfo]] = {
    db += user
    Future.value(Option(user))
  }

  override def get(userId: String): Future[Option[UserInfo]] = {
    val user = db.find(_.userID.equals(UserID(userId)))
      Future.value(user)
  }

  override def update(user: UserInfo): Future[Boolean] = {
    val index = db.indexWhere(p => p.userID.equals(user.userID))
    db.update(index, user)
    Future.value(true)
  }

  override def delete(userId: String): Future[Boolean] = {
    val index  = db.indexWhere(_.userID.equals(UserID(userId)))
    db.remove(index)
    Future.value(true)
  }

  override def getAll(from: Int, size: Int): Future[PageResult[UserInfo]] = {
    val res = db.slice(from, from + size)
    Future.value(PageResult(res.size, res))
  }
}
