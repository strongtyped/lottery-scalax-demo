//@formatter:off

import Dependencies._
import Settings._

name := "fun-cqrs"
organization in ThisBuild := "io.strongtyped"
scalaVersion in ThisBuild := "2.11.7"


ivyScala := ivyScala.value map {
  _.copy(overrideScalaVersion = true)
}

scalacOptions := Seq("-unchecked", "-deprecation", "-feature", "-Xlint:-infer-any", "-Xfatal-warnings")


// dependencies
lazy val root = Project(
  id = "lottery-sample",
  base = file("."),
  settings = mainDeps ++ commonSettings
)
addCommandAlias("format", ";scalariformFormat;test:scalariformFormat")

//@formatter:on