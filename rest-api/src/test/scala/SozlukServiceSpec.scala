package org.sozluk.restapi

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._
import spray.httpx.unmarshalling._
import com.sksamuel.elastic4s.mapping.FieldType._
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.StopAnalyzer

class SozlukServiceSpec extends Specification with Specs2RouteTest with SozlukService {
  def actorRefFactory = system

  "SozlukService" should {

    "respond to ping" in {
      Get("/ping") ~> myRoute ~> check {
        responseAs[String] === "pong"
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> myRoute ~> check {
        handled must beFalse
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put("/ping") ~> sealRoute(myRoute) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }

    "return kelime" in {
      import spray.json.DefaultJsonProtocol._
      import spray.httpx.SprayJsonSupport._
      implicit val jsonUnmarshaller: Unmarshaller[Word] = jsonFormat1(Word)

      Get("/kelimeler/abuzer") ~> sealRoute(myRoute) ~> check {
        responseAs[Word].name === "abuzer"
      }
      Get("/kelimeler/abuzer%20kadayif") ~> sealRoute(myRoute) ~> check {
        responseAs[Word].name === "abuzer kadayif"
      }
    }
  }
}
