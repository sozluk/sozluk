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

package org.sozluk.elastic

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.StopAnalyzer
import com.sksamuel.elastic4s.mapping.FieldType.StringType

trait Indexer { self: Elastic =>

  type Key = String

  type Value = Array[String]

  type KeyValue = (Key, Value)

  def createMappings: Unit =
    client.execute {
      create index "ws" mappings (
        "w" as (
          "k" typed StringType boost 4,
          "v" typed StringType analyzer StopAnalyzer
        )
      )
    }

  private[this] def _indexOne(key: Key, value: Value) =
    index into "ws/w" id key fields (
      "k" -> key,
      "v" -> value
    )

  def indexOne(key: Key, value: Value): Unit =
    client.execute {
      _indexOne(key, value)
    }

  def indexBulk(items: Seq[KeyValue]): Unit =
    client.bulk {
      items.map(item => _indexOne(item._1, item._2)): _*
    }

}
