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
import com.sksamuel.elastic4s.mapping.FieldType.{ StringType, CompletionType }
import org.sozluk.common.SozlukSettings._
import com.typesafe.scalalogging.slf4j.Logging

trait TbdIndexer extends Indexer with Logging {

  type Key = String

  type Value = Array[String]

  def createMappings(): Unit =
    client.execute {
      create index indexNameWords mappings (
        indexTypeWord as (
          fieldNameKey typed StringType analyzer "word_analyzer",
          fieldNameValue typed StringType index "analyzed" analyzer "turkish",
          fieldNameAutoComplete typed CompletionType
        )
      ) shards 2 replicas 1
    }

  protected[this] def _indexOne(key: Key, value: Value): IndexDefinition = {
    logger.info(s"Indexing $key => $value")
    index into s"${indexNameWords}/${indexTypeWord}" id key fields (
      fieldNameKey -> key,
      fieldNameValue -> value,
      fieldNameAutoComplete -> key,
      fieldNameSource -> "tbd"
    ) update false
  }
}
