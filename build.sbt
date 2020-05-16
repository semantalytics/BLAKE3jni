name := "BLAKE3jni"

version := "0.2.0"

publishMavenStyle := true

developers +=
  Developer(
    "sken",
    "sken",
    "sken77@pm.me",
    url("https://www.github.com/sken77")
  )

organization := "io.lktk"

licenses += ("Apache-2.0", url(
  "https://www.apache.org/licenses/LICENSE-2.0.txt"
))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/sken77/BLAKE3jni"),
    "git@github.com:sken77/BLAKE3jni.git"
  )
)

libraryDependencies ++= List(
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "org.scijava" % "native-lib-loader" % "2.3.4",
  "org.slf4j" % "slf4j-nop" % "1.7.30"
)

Compile / unmanagedResourceDirectories += baseDirectory.value / "natives"

//ignore everything related to scala
Compile / unmanagedSourceDirectories := (Compile / javaSource).value :: Nil
Test / unmanagedSourceDirectories := (Test / javaSource).value :: Nil
autoScalaLibrary := false // exclude scala-library from dependencies
crossPaths := false // drop off Scala suffix from artifact names.
