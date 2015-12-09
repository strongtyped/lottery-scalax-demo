import sbt.Keys._
import sbt._

//@formatter:off
object Dependencies {  

  //------------------------------------------------------------------------------------------------------------
  // io.strongtyped.funcqrs core
  val scalaLogging          =  "com.typesafe.scala-logging" %%  "scala-logging"    % "3.1.0"
  // val scalaTest             =  "org.scalatest"              %%  "scalatest"        % "2.2.1"         % "test"
  val scalaTest             =  "org.scalatest"              %% "scalatest"          % "3.0.0-M10" % "test"
  //------------------------------------------------------------------------------------------------------------



  //------------------------------------------------------------------------------------------------------------
  // Akka Module
  val akkaVersion               =   "2.4.0"
  val akkaActor                 =   "com.typesafe.akka"           %%  "akka-actor"        % akkaVersion
  
  val akkaPersistence           =   "com.typesafe.akka"           %%  "akka-persistence"  % akkaVersion
  val akkaSlf4j                 =   "com.typesafe.akka"           %%  "akka-slf4j"        % akkaVersion
  val akkaTestKit               =   "com.typesafe.akka"           %%  "akka-testkit"      % akkaVersion     % "test"
  val akkaStreams               =   "com.typesafe.akka"           %%  "akka-stream-experimental"            % "1.0"
  val akkaPersistenceQuery      =   "com.typesafe.akka"           %%  "akka-persistence-query-experimental" % akkaVersion

  //------------------------------------------------------------------------------------------------------------

  val levelDb           =   "org.iq80.leveldb"            %   "leveldb"           % "0.7"
  val levelDbJNI        =   "org.fusesource.leveldbjni"   %   "leveldbjni-all"    % "1.8"


  val funCqrsVersion    =   "0.0.7-SNAPSHOT"
  val funCqrsAkka       =   "io.strongtyped"              %% "fun-cqrs-akka"                  % funCqrsVersion
  val funCqrsPlayJson   =   "io.strongtyped"              %% "fun-cqrs-play-json"             % funCqrsVersion
  val funCqrsTestKit    =   "io.strongtyped"              %% "fun-cqrs-test-kit"              % funCqrsVersion % "test"


  val mainDeps = Seq(
    
    libraryDependencies ++= Seq(levelDb, levelDbJNI),
    
    libraryDependencies ++= Seq(scalaLogging, scalaTest),

    libraryDependencies ++= Seq(akkaActor, akkaPersistence, akkaSlf4j),
    // experimental
    libraryDependencies ++= Seq(akkaPersistenceQuery, akkaStreams),
    // test scope
    libraryDependencies ++= Seq(akkaTestKit),

    // fun-cqrs
    libraryDependencies ++= Seq(funCqrsAkka, funCqrsPlayJson, funCqrsTestKit)
  ) 
  //------------------------------------------------------------------------------------------------------------

}
// /@formatter:on