name := "parser"

libraryDependencies ++= Seq(
  "org.scalesxml" %% "scales-xml" % "0.6.0-M2",
  "org.xwiki.rendering" % "xwiki-rendering-syntax-mediawiki" % "5.3",
  "org.xwiki.rendering" % "xwiki-rendering-syntax-xhtml" % "5.3",
  "org.xwiki.commons" % "xwiki-commons-component-default" % "5.3",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "net.databinder.dispatch" %% "dispatch-jsoup" % "0.11.0",
  "org.jsoup" % "jsoup" % "1.7.3",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test")
