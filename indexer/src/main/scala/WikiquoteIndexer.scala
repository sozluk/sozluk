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

package org.sozluk.indexer

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.mappings.FieldType.{ StringType, CompletionType }
import org.sozluk.common.SozlukSettings._

trait WikiquoteIndexer extends Indexer {

  type Key = String

  type Value = String

  def createMappings(): Unit =
    client.execute {
      create index indexNameQuotes mappings (
        indexTypeQuote as (
          fieldNameKey typed StringType index "no",
          fieldNameValue multi (
            fieldNameValue typed StringType index "analyzed" analyzer "turkish",
            fieldNameRaw typed StringType index "analyzed" analyzer "simple"
          )
        )
      ) shards 2 replicas 1
    }

  protected[this] def _indexOne(key: Key, value: Value): IndexDefinition =
    index into s"${indexNameQuotes}/${indexTypeQuote}" fields (
      fieldNameKey -> key,
      fieldNameValue -> value
    )
}
