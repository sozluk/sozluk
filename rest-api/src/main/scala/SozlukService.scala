// Copyright (C) 2014 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
