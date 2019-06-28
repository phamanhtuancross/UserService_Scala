package xed.onboarding.controller.http

import java.util.UUID

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import javax.inject.Inject
import xed.onboarding.domain.request.{SearchRequest, UserRequest}
import xed.onboarding.service.UserService

case class UserController @Inject()(service: UserService) extends Controller {
  private val apiPath = "/user"
  post(s"$apiPath"){
    req: UserRequest =>{
      service.createUSer(req.buildUser())
    }
  }

  put(s"$apiPath"){
    req: UserRequest => {
      service.updateUser(req.buildUser())
    }
  }

  get(s"$apiPath/:id"){
    req: Request =>{
      service.getUser(req.getParam("id"))
    }
  }

  delete(s"$apiPath/:id"){
    req: Request =>{
      service.deleteUser(req.getParam("id"))
    }
  }

  post(s"$apiPath/users"){
    req: SearchRequest => {
      service.listUsers(req)
    }
  }

}
