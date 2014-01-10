package org.sozluk.restapi

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._

object Elasticsearch {

  val client = ElasticClient.local

}
