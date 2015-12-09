package lottery.app


import akka.actor.ActorSystem
import io.funcqrs.akka.FunCQRS
import lottery.domain.model.Lottery
import lottery.domain.model.LotteryId
import lottery.domain.model.LotteryProtocol._
import lottery.domain.service.{LevelDbTaggedEventsSource, LotteryViewRepo, LotteryViewProjection}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.util.Timeout
import java.util.UUID

import scala.util.{Failure, Success}


object Main extends App { 
  implicit def timeout: Timeout = Timeout(1.second)

  val system = ActorSystem("FunCQRS")
  implicit lazy val funCQRS = new FunCQRS(system)

  import io.funcqrs.akka.FunCQRS.api._

  val id = LotteryId.generate()

  // ---------------------------------------------
  // aggregate config - write model
  val lotteryService =
    config {
      aggregate[Lottery](Lottery.behavior).withAssignedId
    }

  // ---------------------------------------------
  // create aggregate
  val result =
    for {
      _ <- lotteryService.newInstance(id, CreateLottery("ScalaX")).result()
      _ <- lotteryService.update(id)(AddParticipant("John")).result()
      _ <- lotteryService.update(id)(AddParticipant("Paul")).result()
      _ <- lotteryService.update(id)(AddParticipant("Joe")).result()
      res <- lotteryService.update(id)(Run).result()
    } yield res

  waitAndPrint(result)

  Thread.sleep(1000)
  system.terminate()

  def waitAndPrint[T](resultFut:Future[T]) = {
    Await.ready(resultFut, 3.seconds)
    resultFut.onComplete {
      case Success(res) => println(s" => result: $res")
      case Failure(ex)  => println(s"FAILED: ${ex.getMessage}")
    }
  }
}