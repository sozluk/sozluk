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

import scala.collection.JavaConverters._

import scales.xml._
import scales.xml.ScalesXml._
import org.xwiki.component.embed.EmbeddableComponentManager
import org.xwiki.rendering.converter.Converter
import org.xwiki.rendering.renderer.printer.DefaultWikiPrinter
import org.xwiki.rendering.syntax.Syntax
import org.jsoup.Jsoup
import org.jsoup.nodes.{ Element, Document }

import java.io.{ Reader, StringReader, FileReader }

trait WikiquoteParser extends WikiParser {

  val ignoredPages = Set(
    "Main Page",
    "Konular listesi",
    "Filmler listesi",
    "Kitaplar listesi"
  )

  val forbiddenWords = Set(
    "YÖNLENDİRME",
    "REDIRECT"
  )

  def parse: Iterator[(String, Array[String])] = {
    words map {
      case (word, content) =>
        val doc = Jsoup.parse(content)
        val lis = doc.select("li").asScala
        (word, lis.map(_.text().trim)
          .filter(!_.equalsIgnoreCase(word))
          .filter(meaning => forbiddenWords.forall(!meaning.contains(_)))
          .distinct.toArray)
    } filter { case (word, meanings) => meanings.nonEmpty }
  }
}

