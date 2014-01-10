package org.sozluk.restapi

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import spray.json.DefaultJsonProtocol
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class SozlukServiceActor extends Actor with SozlukService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}

// this trait defines our service behavior independently from the service actor
trait SozlukService extends HttpService {

  case class Word(name: String)

  object MyJsonProtocol extends DefaultJsonProtocol {
    implicit val WordFormat = jsonFormat1(Word)
  }

  import MyJsonProtocol._
  import spray.httpx.SprayJsonSupport._

  val myRoute =
    pathPrefix("kelimeler" / Segment) { kelime =>
      get {
        respondWithMediaType(`application/json`) {
          complete {
            Word(kelime)
          }
        }
      }
    } ~
      path("ping") {
        get {
          complete {
            "pong"
          }
        }
      }
}
