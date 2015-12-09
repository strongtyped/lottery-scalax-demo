package lottery.domain.service

import com.typesafe.scalalogging.LazyLogging
import io.funcqrs.Projection
import io.funcqrs.HandleEvent
import lottery.domain.model.LotteryProtocol._
import lottery.domain.model.LotteryView

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LotteryViewProjection(repo: LotteryViewRepo) extends Projection with LazyLogging {

  def handleEvent: HandleEvent = ???
}
