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
import com.typesafe.scalalogging.LazyLogging
import org.sozluk.parser.TbdParser

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object TbdIndexerApp extends TbdIndexer with TbdParser with LazyLogging {

  val client = ElasticClient.remote("localhost", 9300)

  def shutdown() = Await.result(client.shutdown, 10 seconds)

  def main(args: Array[String]) {
    createMappings()
    logger.info("created mappings")

    val f = Future sequence {
      harfs map { harf =>
        parse(harf) flatMap {
          case Left(t) =>
            logger.error("Error from TBD", t)
            Future.successful(())
          case Right(results) =>
            Future sequence {
              results map {
                case (word, meaning) => (word, Array(meaning))
              } grouped 100 map { items =>
                logger.info(s"indexing... $harf")
                indexBulk(items)
              }
            }
        }
      }
    }

    logger.info("Waiting for finishing indexing")
    Await.result(f, 1000 seconds)
  }
}
