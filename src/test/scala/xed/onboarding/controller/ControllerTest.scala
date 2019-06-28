package xed.onboarding.controller

import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import xed.onboarding.TestServer

abstract class ControllerTest extends FeatureTest{
  val server = new EmbeddedHttpServer(new TestServer)
}
