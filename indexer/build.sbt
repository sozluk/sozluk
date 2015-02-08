name := "indexer"

resolvers ++= Seq(
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s" % Common.elastic4sVersion,
  "org.scalatest" %% "scalatest" % "2.2.1" % "test")

seq(Revolver.settings: _*)
