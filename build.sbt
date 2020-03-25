name := "BLAKE3jni"

version := "0.0.1"

organization := "org.scash"

scmInfo := Some(ScmInfo(url("https://github.com/sken77/BLAKE3jni"), "git@github.com:sken77/BLAKE3jni.git"))

bintrayPackageLabels := Seq("cryptography", "BLAKE3", "p2p", "blockchain")
bintrayOrganization := Some("scala-cash")
bintrayRepository := "io"

libraryDependencies ++= List(
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "org.scijava" % "native-lib-loader" % "2.3.4"
)

Compile / unmanagedResourceDirectories += baseDirectory.value / "natives"

//ignore everything related to scala
Compile / unmanagedSourceDirectories := (Compile / javaSource).value :: Nil
Test / unmanagedSourceDirectories := (Test / javaSource).value :: Nil
autoScalaLibrary := false // exclude scala-library from dependencies
crossPaths := false // drop off Scala suffix from artifact names.
