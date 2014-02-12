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

package org.sozluk.parser

import dispatch._, Defaults._
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import scala.collection.JavaConversions._
import org.sozluk.common.Logger

object TdkParser {

  private def request(word: String) =
    url("http://www.tdk.gov.tr/index.php?option=com_bts").POST << Map(
      "kelime" -> word,
      "gonder" -> "ARA",
      "ayn" -> "tam",
      "kategori" -> "verilst")

  private def processResponse(response: Elements): List[String] = {
    response.map(_.text().trim()).filter(_.nonEmpty).toList
  }

  def parse(word: String): Future[Either[Throwable, List[String]]] =
    Http(request(word) OK as.jsoup.Query("p.thomicb")).either.right.map(processResponse)

  def main(args: Array[String]) {
    val word = args(0)
    val f = parse(word) map {
      _ match {
        case Left(t) =>
        case Right(head :: tail) if head == "Aşağıdaki sözlerden birini mi aramak istediniz?" =>
          Logger.info(s"Bunu mu demek istediniz => ${tail.mkString(",")}")
        case Right(meanings) =>
          meanings.foreach(Logger.info(_))
      }
    }

    import scala.concurrent.duration._
    import scala.concurrent.Await
    try {
      Await.result(f, 10 seconds)
    } finally {
      dispatch.Http.shutdown()
    }
  }
}
