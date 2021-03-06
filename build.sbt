import AssemblyKeys._

lazy val commonSettings = Seq(
  organization := "org.sozluk",
  version := "1.0-SNAPSHOT",
  scalaVersion  := "2.11.5",
  scalacOptions := Seq(
    "-unchecked",
    "-deprecation",
    "-encoding", "utf8",
    "-feature",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-language:existentials"))

lazy val settings = (
  commonSettings
  ++ org.scalastyle.sbt.ScalastylePlugin.Settings
  ++ assemblySettings)

lazy val api = project.in(file("api"))
  .dependsOn(common, parser)
  .settings(settings: _*)
  .settings(test in assembly := {})
  .settings(testOptions in Test += Tests.Argument("showtimes", "true"))

lazy val parser = project.in(file("parser"))
  .dependsOn(common)
  .settings(settings: _*)
  .settings(test in assembly := {})
  .settings(testOptions in Test += Tests.Argument("-oDS"))

lazy val indexer = project.in(file("indexer"))
  .dependsOn(parser, common)
  .aggregate(parser, common)
  .settings(settings: _*)
  .settings(test in assembly := {})
  .settings(testOptions in Test += Tests.Argument("-oDS"))

lazy val common = project.in(file("common"))
  .settings(settings: _*)
  .settings(test in assembly := {})

shellPrompt in ThisBuild := Common.prompt
