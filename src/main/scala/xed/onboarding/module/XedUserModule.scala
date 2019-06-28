package xed.onboarding.module

import com.google.inject.Provides
import com.twitter.inject.TwitterModule
import javax.inject.Singleton
import xed.onboarding.domain.{UserID, UserInfo}
import xed.onboarding.repository.{CacheRepository, LocalUserRepository, OnMemoryCacheRepository, UserRepository}
import xed.onboarding.service.{UserCacheService, UserCacheServiceImpl, UserService, UserServiceImpl}

object XedUserModule extends TwitterModule{

  protected override def configure(): Unit = {
    bind[UserCacheService].to[UserCacheServiceImpl]
    bind[UserService].to[UserServiceImpl].asEagerSingleton()
    bind[UserRepository].to[LocalUserRepository].asEagerSingleton()
  }

  @Singleton
  @Provides
  def providesUserCacheRepository(): CacheRepository[UserID, UserInfo] = {
    new OnMemoryCacheRepository[UserID, UserInfo]()
  }

  //module and provide
}
