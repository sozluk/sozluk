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

package org.sozluk.api

import scala.concurrent.{ Future, ExecutionContext }
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.index.IndexResponse
import com.sksamuel.elastic4s.{ ElasticClient, CustomAnalyzer }
import com.sksamuel.elastic4s.ElasticDsl._
import org.sozluk.common.SozlukSettings._

trait ElasticComponent {
  val elastic: Elastic

  trait Elastic {
    val client: ElasticClient

    def queryWords(query: String): Future[SearchResponse] =
      client execute {
        import scala.language.reflectiveCalls
        search in indexNameWords types indexTypeWord query matchPhrase(fieldNameKey, query) suggestions (
          suggest using completion as "ac" on query field fieldNameAutoComplete,
          suggest using term as "term" on query field fieldNameAutoComplete maxEdits 2 minWordLength 3 analyzer CustomAnalyzer("word_analyzer")
        )
      }

    def queryQuotes(query: String): Future[SearchResponse] =
      client execute {
        search in indexNameQuotes types indexTypeQuote query {
          must {
            matches(fieldNameValue, query)
          } should {
            matches(fieldNameRaw, query) boost 0.5
          }
        }
      }

    /** Did we make this request to TDK before? */
    def existsTdk(query: String)(implicit ec: ExecutionContext): Future[Boolean] =
      client execute {
        get id query from indexNameTdks -> indexTypeTdk
      } map (_.isExists)

    /** Log the request to TDK */
    def indexTdk(query: String): Future[IndexResponse] = {
      client execute {
        index into indexNameTdks -> indexTypeTdk id query update false ttl 1209600000 //2 weeks
      }
    }

    def indexWord(key: String, meanings: Array[String], source: String) {
      client execute {
        index into s"${indexNameWords}/${indexTypeWord}" id key fields (
          fieldNameKey -> key,
          fieldNameValue -> meanings,
          fieldNameAutoComplete -> key,
          fieldNameSource -> source
        )
      }
    }

  }

  object Elastic extends Elastic {
    val client: ElasticClient = Context.client
  }

}
