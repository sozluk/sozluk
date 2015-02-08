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

import java.io.{FileReader, Reader}

import com.sksamuel.elastic4s.ElasticClient
import com.typesafe.scalalogging.LazyLogging
import org.sozluk.parser.WikiquoteParser

import scala.concurrent.Await
import scala.concurrent.duration._

object WikiquoteIndexerApp extends WikiquoteIndexer with LazyLogging {

  val client = ElasticClient.remote("localhost", 9300)

  def main(args: Array[String]) {
    createMappings()
    logger.info("created mappings")

    new WikiquoteParser {
      def xmlSrc: Reader = new FileReader(args(0))
    }.parse grouped 100 foreach {
      _ foreach {
        case (person, quotes) =>
          logger.info("indexing...")
          Await.result(indexBulk(quotes.map((person, _))), Duration.Inf)
      }
    }
  }
}
