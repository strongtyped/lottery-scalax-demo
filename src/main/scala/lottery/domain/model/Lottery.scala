package lottery.domain.model

import java.time.OffsetDateTime
import java.util.UUID
import scala.language.reflectiveCalls
import io.funcqrs._
import io.funcqrs.dsl.BindingDsl

import scala.util.{ Failure, Random, Success, Try }

case class Lottery(name: String, participants: List[String] = List(),
                   winner: Option[String] = None,
                   id: LotteryId) extends AggregateLike {

  type Id = LotteryId
  type Protocol = LotteryProtocol.type

  def addParticipant(name: String): Lottery = {
    copy(participants = participants :+ name)
  }

  def removeParticipant(name: String): Lottery =
    copy(participants = participants.filter(_ != name))

  def selectParticipant(): Try[String] = {

    if (hasWinner) {
      Failure(new RuntimeException("Lottery has already a winner!"))
    } else if (hasNoParticipants) {
      Failure(new RuntimeException("Lottery has no participants"))
    } else {
      val index = Random.nextInt(participants.size)
      Try(participants(index))
    }
  }

  def hasWinner = winner.isDefined

  def hasNoParticipants = participants.isEmpty

  def hasParticipant(name: String) = participants.contains(name)
}

case class LotteryId(value: String) extends AggregateID

object LotteryId {

  def fromString(aggregateId: String): LotteryId = {
    LotteryId(aggregateId)
  }

  def generate() = LotteryId(UUID.randomUUID.toString)
}

object LotteryProtocol extends ProtocolLike {

  case class LotteryMetadata(aggregateId: LotteryId,
                             commandId: CommandId,
                             eventId: EventId = EventId(),
                             date: OffsetDateTime = OffsetDateTime.now(),
                             tags: Set[Tag] = Set()) extends Metadata with JavaTime {

    type Id = LotteryId
  }

  sealed trait LotteryCommand extends ProtocolCommand

  // =================================================================
  // Commands
  case class CreateLottery(name: String) extends LotteryCommand

  case class AddParticipant(name: String) extends LotteryCommand

  case class RemoveParticipant(name: String) extends LotteryCommand

  case object Reset extends LotteryCommand

  case object Run extends LotteryCommand
  // =================================================================

  // =================================================================
  // Events
  sealed trait LotteryEvent extends ProtocolEvent with MetadataFacet[LotteryMetadata]

  case class LotteryCreated(name: String, metadata: LotteryMetadata) extends LotteryEvent

  sealed trait LotteryUpdateEvent extends LotteryEvent

  // Update Events
  case class ParticipantAdded(name: String, metadata: LotteryMetadata) extends LotteryUpdateEvent

  case class ParticipantRemoved(name: String, metadata: LotteryMetadata) extends LotteryUpdateEvent

  case class WinnerSelected(winner: String, metadata: LotteryMetadata) extends LotteryUpdateEvent
  // =================================================================

}

object Lottery {

  import LotteryProtocol._

  val tag = Tags.aggregateTag("Lottery")

  def metadata(id: LotteryId, cmd: LotteryCommand) = {
    LotteryMetadata(id, cmd.id, tags = Set(tag))
  }

  def behavior(id: LotteryId): Behavior[Lottery] = {

    val dsl = new BindingDsl[Lottery]
    import dsl.api._

    import io.funcqrs.dsl.BindingDsl.api._

    Behavior.empty
  }
}