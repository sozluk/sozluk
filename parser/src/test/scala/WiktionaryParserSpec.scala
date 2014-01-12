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

import org.scalatest._

import java.io.{ Reader, FileReader }

class WiktionaryParserSpec extends FlatSpec with Matchers with WiktionaryParser {

  def xmlSrc: Reader = new FileReader(getClass.getResource("/trwiktionary.xml").getPath)

  "A WiktionaryParser" should "parse Turkish wiktionary" in {
    val list = parse.toList
    list should have size 4
    list(0)._1 shouldBe "empati"
    list(1)._1 shouldBe "etimoloji"
    list(2)._1 shouldBe "imece"
    list(3)._1 shouldBe "mayıs"
  }

}
