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

import java.io.{ Reader, StringReader }

trait WiktionaryParser {

  val rootPath = NoNamespaceQName("mediawiki")
  val pagePath = NoNamespaceQName("page")
  val titlePath = NoNamespaceQName("title")
  val textPath = NoNamespaceQName("text")

  object FindWord {
    val wordPattern = "[\\wıöçşğüIÖÇŞĞÜ]+"

    def unapply(page: XmlPath): Option[String] = {
      val word = text(page \* titlePath)
      if (word matches wordPattern) Some(word) else None
    }
  }

  object ExtractText {
    val ddPattern = "\\d+\\s*.+"

    def unapply(dd: Element): Option[String] = {
      val text = dd.text()
      if (text matches ddPattern) {
        val parsed = text.replaceFirst("^\\d+", "")
          .collect { case c if c.shortValue != 160 => c }
          .trim
        if (parsed.nonEmpty) Some(parsed) else None
      } else None
    }
  }

  def xmlSrc: Reader

  def xml: XmlPull = pullXml(xmlSrc)

  def words: Iterator[(String, String)] = iterate(List(rootPath, pagePath), xml) collect {
    case page @ FindWord(word) =>
      val content = text(page \\* textPath)
      (word, toXHTML(content))
  }

  def toXHTML(input: String): String = {
    val cm = new EmbeddableComponentManager()
    cm.initialize(this.getClass().getClassLoader())

    val converter: Converter = cm.getInstance(classOf[Converter])

    val printer = new DefaultWikiPrinter()
    converter.convert(new StringReader(input), Syntax.MEDIAWIKI_1_0, Syntax.XHTML_1_0, printer)
    printer.toString
  }

  def parse: Iterator[(String, Array[String])] = {
    words map {
      case (word, content) =>
        val doc = Jsoup.parse(content)
        val dds = doc.select("dd").asScala
        (word, dds.collect {
          case ExtractText(text) => text
        } toArray)
    }
  }
}
