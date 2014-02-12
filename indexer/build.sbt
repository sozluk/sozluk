name := "indexer"

resolvers ++= Seq(
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s" % "1.0.0-SNAPSHOT",
  "org.scalatest" %% "scalatest" % "2.0" % "test")

seq(Revolver.settings: _*)
