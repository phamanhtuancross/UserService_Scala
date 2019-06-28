package xed.onboarding.service

import java.util.concurrent.TimeUnit

import com.google.common.cache.{CacheBuilder, CacheLoader}
import com.twitter.util.Future
import javax.inject.Inject
import xed.onboarding.domain.UserInfo
import xed.onboarding.domain.request.SearchRequest
import xed.onboarding.repository.UserRepository
import xed.onboarding.util.ZConfig


trait UserService {
  def createUSer(user: UserInfo): Future[BaseResponse]
  def updateUser(user: UserInfo): Future[BaseResponse]
  def getUser(userId: String): Future[BaseResponse]
  def deleteUser(userId: String): Future[BaseResponse]
  def listUsers(searchRequest: SearchRequest): Future[BaseResponse]
}

class UserServiceImpl @Inject()(userRepository: UserRepository) extends UserService {

  private val cacheMaxSize = ZConfig.getInt("cache.max_size")
  private val cacheIntervalInMins = ZConfig.getInt("cache.interval_minutes")

  private val cache = CacheBuilder.newBuilder()
    .maximumSize(cacheMaxSize)
    .expireAfterWrite(cacheIntervalInMins, TimeUnit.MINUTES)
    .build(new CacheLoader[String, UserInfo] {
      override def load(k: String): UserInfo = {
        null
      }
    }).asMap()


  override def createUSer(user: UserInfo): Future[BaseResponse] = {
    val userID = user.userID.id
    for {
      r <- userRepository.create(user)
    } yield r match {
      case None => BaseResponse(false, None)
      case Some(x)=>
        cache.put(s"$userID", x)
        BaseResponse(r.isDefined, Some(x))
    }
  }


  override def updateUser(user : UserInfo): Future[BaseResponse] = {
    for{
     r <- userRepository.update(user)
    } yield BaseResponse(r,None)
  }

  override def getUser(userId: String): Future[BaseResponse] = {
    for{
      r <- userRepository.get(userId)
    } yield  r match {
      case None => BaseResponse(false,None)
      case  x => BaseResponse(r.isDefined, x)
    }
  }

  override def deleteUser(userId: String): Future[BaseResponse] = {
    for{
      r <- userRepository.delete(userId)
    } yield BaseResponse(r, None)
  }

  override def listUsers(searchRequest: SearchRequest): Future[BaseResponse] = {
    for {
      r <- userRepository.getAll(searchRequest.from, searchRequest.size)
    } yield BaseResponse(true, Some(r))
  }
}

case class BaseResponse(success: Boolean, data: Option[Any])
