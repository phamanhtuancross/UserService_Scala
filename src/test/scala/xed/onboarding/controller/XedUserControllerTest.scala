package xed.onboarding.controller

import com.fasterxml.jackson.databind.JsonNode
import com.twitter.finagle.http.Status
import xed.onboarding.domain.request.{SearchRequest, UserRequest}
import xed.onboarding.util.JsonUtils

class XedUserControllerTest extends ControllerTest {

  val id = "1512639"
  val userRequest = UserRequest(id,"phamanhtuancross@gmail.com", 20, "M")
  private val apiPath = "/user"

  "Create New Xed User" in {
    val r = server.httpPost(path = s"$apiPath",
      postBody = JsonUtils.toJson(userRequest),
      andExpect = Status.Ok
    )
    val response = JsonUtils.fromJson[JsonNode](r.getContentString())
    assert(response.at("/success").asBoolean(false))
  }

  "Update user by id" in {
    val updateRequest =UserRequest(id,"phamanhtuancross@gmail.com", 20, "F")
    val r = server.httpPut(s"$apiPath",
      putBody = JsonUtils.toJson(updateRequest),
      andExpect = Status.Ok)

    val response = JsonUtils.fromJson[JsonNode](r.getContentString())
    assert(response.at(("/success")).asBoolean(false))
  }

  "Get New Xed User" in {
    val r =  server.httpGet(s"$apiPath/$id", andExpect = Status.Ok)
    val response = JsonUtils.fromJson[JsonNode](r.getContentString())
    println(response)
    assert(response.at("/success").asBoolean(false))
  }


  "Delete user by id" in {
    val  r = server.httpDelete(s"$apiPath/$id", andExpect = Status.Ok)
    val response = JsonUtils.fromJson[JsonNode](r.getContentString())
    assert(response.at("/success").asBoolean(false))
  }


  "Get New Xed User then delete" in {
    val r =  server.httpGet(s"$apiPath/$id", andExpect = Status.Ok)
    val response = JsonUtils.fromJson[JsonNode](r.getContentString())
    println(response)
    assert(response.at("/success").asBoolean(false)==false)
  }

  "Create list new User" in {
    for (index <- 0 to 10){
       val requestBody = UserRequest(s"1512639 - $index", s"SunPham- $index", 10,"M")
      val r = server.httpPost(path = s"$apiPath",
        postBody = JsonUtils.toJson(requestBody),
        andExpect = Status.Ok
      )
    }
  }

  "Paging list user" in {
    val searchRequest = SearchRequest(from = 11,size = 10)
    val r = server.httpPost(s"$apiPath/users",
      postBody = JsonUtils.toJson(searchRequest),
      andExpect = Status.Ok
    )
    val response = JsonUtils.fromJson[JsonNode](r.getContentString())
    println(response)
    assert(response.at("/success").asBoolean())
  }
}
