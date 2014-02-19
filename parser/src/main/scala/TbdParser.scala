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
import org.jsoup.select.Elements
import scala.collection.JavaConversions._

object TbdParser {

  private val harfs = ('A' to 'Z') :+ '1'

  private def request(harf: Char) =
    url("http://www.tbd.org.tr/index.php").GET <<? Map(
      "sayfa" -> "sozluk",
      "tipi" -> "entr",
      "harf" -> s"$harf")

  private def processResponse(response: Elements): List[(String, String)] = {
    response.map { tr =>
      val tds = tr.select("td")
      (tds(0).text.trim, tds(1).text.trim)
    }.filter {
      case (word, meaning) =>
        word.nonEmpty && meaning.nonEmpty
    }.toList
  }

  def parse(harf: Char): Future[Either[Throwable, List[(String, String)]]] =
    Http(request(harf) OK as.jsoup.Query("tr.sozlukSatir")).either.right.map(processResponse)

  def main(args: Array[String]) {
    val f = Future sequence {
      harfs map { harf =>
        parse(harf) map {
          case Left(t) =>
          case Right(results) => results.foreach {
            case (word, meaning) =>
              println(s"$word => $meaning")
          }
        }
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
