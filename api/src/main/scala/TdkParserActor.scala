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

package org.sozluk.api

import scala.util.{ Success, Failure }
import akka.actor.{ Actor, Props }
import akka.event.LoggingReceive
import org.sozluk.common.Logger
import org.sozluk.parser.TdkParser

object TdkParserActor {
  def props(): Props = Props[TdkParserActor]
}

class TdkParserActor extends Actor with ElasticComponent {

  val elastic: Elastic = Elastic

  import context.dispatcher

  def receive = LoggingReceive {

    case query: String =>
      // Check the query catalog for the query.
      elastic.indexTdk(query) onComplete {
        // We haven't asked for this query to TDK in the last 2 weeks.
        case Success(_) =>
          Logger.debug(s"Not exists $query")
          // Ask TDK
          TdkParser.parse(query) map {
            // Could not retreive from TDK
            case Left(t) =>

            // Suggestions
            case Right(head :: tail) if head == "Aşağıdaki sözlerden birini mi aramak istediniz?" =>
              Logger.debug(s"Bunu mu demek istediniz => ${tail.mkString(",")}")
              tail foreach (self ! _)

            // Success
            case Right(meanings) if meanings.nonEmpty =>
              elastic.indexWord(query, meanings.toArray, "tdk")

            case _ =>
          }

        // We have already asked for this query in the last 2 weeks.
        case Failure(_) =>
          Logger.debug(s"Exists $query")
      }
  }
}

