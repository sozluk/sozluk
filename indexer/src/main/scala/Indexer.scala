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

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.{ElasticClient, IndexDefinition}

trait Indexer {

  type Key

  type Value

  type KeyValue = (Key, Value)

  def client: ElasticClient

  def createMappings(): Unit

  protected[this] def _indexOne(key: Key, value: Value): IndexDefinition

  def indexOne(key: Key, value: Value) =
    client.execute {
      _indexOne(key, value)
    }

  def indexBulk(items: Seq[KeyValue]) =
    client.execute {
      bulk(
        items.map(item => _indexOne(item._1, item._2)): _*
      )
    }

}
