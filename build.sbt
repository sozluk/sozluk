lazy val commonSettings = Seq(
  organization := "org.sozluk",
  version := "1.0-SNAPSHOT",
  scalaVersion  := "2.10.3",
  scalacOptions := Seq(
    "-unchecked",
    "-deprecation",
    "-encoding", "utf8",
    "-feature",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-language:existentials"),
  testOptions in Test += Tests.Argument("showtimes", "true"))

lazy val settings = (
  commonSettings
  ++ scalariformSettings
  ++ org.scalastyle.sbt.ScalastylePlugin.Settings)

lazy val restApi = project.in(file("rest-api"))
  .dependsOn(wiktionaryParser)
  .aggregate(wiktionaryParser)
  .settings(settings: _*)

lazy val wiktionaryParser = project.in(file("wiktionary-parser")).settings(settings: _*)

shellPrompt in ThisBuild := Common.prompt
