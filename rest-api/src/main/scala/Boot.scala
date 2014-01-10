package org.sozluk.restapi

import akka.actor.{ ActorSystem, Props }
import akka.io.IO
import spray.can.Http
import com.sksamuel.elastic4s.mapping.FieldType._
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.StopAnalyzer

object Boot extends App {

  implicit val system = ActorSystem("sozluk-system")

  val service = system.actorOf(Props[SozlukServiceActor], "sozluk-service")

  IO(Http) ! Http.Bind(service, interface = "localhost", port = 8080)

  Elasticsearch.client.execute {
    create index "places" mappings (
      "cities" as (
        id typed IntegerType,
        "name" typed StringType boost 4,
        "content" typed StringType analyzer StopAnalyzer
      )
    )
  }
}
