name := "parser"

libraryDependencies ++= Seq(
  "org.scalesxml" %% "scales-xml" % "0.5.0-M1",
  "org.xwiki.rendering" % "xwiki-rendering-syntax-mediawiki" % "5.3",
  "org.xwiki.rendering" % "xwiki-rendering-syntax-xhtml" % "5.3",
  "org.xwiki.commons" % "xwiki-commons-component-default" % "5.3",
  "org.jsoup" % "jsoup" % "1.7.3",
  "org.scalatest" %% "scalatest" % "2.0" % "test")
