package xed.onboarding.domain.request

import java.util.UUID

import xed.onboarding.domain.{UserID, UserInfo}

case class UserRequest(id: String, username: String, age: Int, sex: String) {
  def buildUser(): UserInfo = {
    UserInfo(UserID(id), username, age, sex)
  }
//
//  def generateUserId(): String = {
//    UUID.randomUUID().toString
//  }
}
