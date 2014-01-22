name := "indexer"

libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s" % "0.90.10.0",
  "org.scalatest" %% "scalatest" % "2.0" % "test")

seq(Revolver.settings: _*)
