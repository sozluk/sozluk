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

package org.sozluk.common

import com.typesafe.config.{ Config, ConfigFactory }

class Settings(config: Config = ConfigFactory.load()) {

  config.checkValid(ConfigFactory.defaultReference(), "sozluk")

  // note that these fields are NOT lazy, because if we're going to
  // get any exceptions, we want to get them on startup.
  val sozlukConfig = config.getConfig("sozluk")

  val indexNameWords = sozlukConfig.getString("indexNameWords")
  val indexNameQuotes = sozlukConfig.getString("indexNameQuotes")
  val indexNameTdks = sozlukConfig.getString("indexNameTdks")

  val indexTypeWord = sozlukConfig.getString("indexTypeWord")
  val indexTypeQuote = sozlukConfig.getString("indexTypeQuote")
  val indexTypeTdk = sozlukConfig.getString("indexTypeTdk")

  val fieldNameKey = sozlukConfig.getString("fieldName.key")
  val fieldNameValue = sozlukConfig.getString("fieldName.value")
  val fieldNameRaw = sozlukConfig.getString("fieldName.valueRaw")
  val fieldNameAutoComplete = sozlukConfig.getString("fieldName.autoComplete")
  val fieldNameSource = sozlukConfig.getString("fieldName.source")

  val httpPort = sozlukConfig.getInt("http.port")
}

object SozlukSettings extends Settings
