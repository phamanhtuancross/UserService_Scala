package xed.onboarding


import com.google.inject.Module
import com.google.inject.util.Modules
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.finatra.thrift.ThriftServer
import com.twitter.finatra.thrift.routing.ThriftRouter
import xed.onboarding.controller.http
import xed.onboarding.controller.http.{HealthController, UserController}
import xed.onboarding.controller.thrift.CacheController
import xed.onboarding.module.XedUserModule
import xed.onboarding.util.ZConfig

/**
  * Created by SangDang on 9/8/
  **/
object MainApp extends Server

class TestServer extends Server{

  override def modules: Seq[com.google.inject.Module] = Seq(overrideModule(super.modules ++ Seq(XedUserModule): _*))

  private def overrideModule(modules: Module*): Module = {
    if (modules.size == 1) return modules.head

    var module = modules.head
    modules.tail.foreach(m => {
      module = Modules.`override`(module).`with`(m)
    })
    module
  }
}
class Server extends HttpServer with ThriftServer {

  override protected def defaultFinatraHttpPort: String = ZConfig.getString("server.http.port",":8080")

  override protected def defaultFinatraThriftPort: String = ZConfig.getString("server.thrift.port",":8082")

  override protected def disableAdminHttpServer: Boolean = ZConfig.getBoolean("server.admin.disable",true)

  override def modules: Seq[Module] = Seq(XedUserModule)

  override protected def configureHttp(router: HttpRouter): Unit = {
    router.filter[CommonFilters]
      .add[http.CacheController]
      .add[HealthController]
      .add[UserController] //Added Onboarding Controller
  }

  override protected def configureThrift(router: ThriftRouter): Unit = {
    router
      .add[CacheController]
  }
}
