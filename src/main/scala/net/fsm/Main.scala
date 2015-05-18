package net.fsm

import akka.actor.{Actor, ActorRef, ActorLogging, Props}
import akka.pattern.ask
import akka.util.Timeout
import Common.{Binary, State, TerminateStream}
import scala.concurrent.{Future, TimeoutException, Await}
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit
import scala.util.{Try, Success, Failure}
 
 object Main {

 	sealed trait FailedStreamTermination
 	case object TimeoutFailure extends FailedStreamTermination
 	case object ClassCastFailure extends FailedStreamTermination

 	def addToStream(b: Binary, counterActor: ActorRef): Unit = 
 		counterActor ! b

 	def terminateStream(counterActor: ActorRef): Either[FailedStreamTermination, State] = {
 		implicit val timeout: akka.util.Timeout = Timeout(3, TimeUnit.SECONDS)
 		try { 
	 		val state: Future[State] = (counterActor ? TerminateStream).mapTo[State]
			Right(Await.result(state, 3.seconds))
		}
		catch {
			case _: TimeoutException   => Left(TimeoutFailure)
			case _: ClassCastException => Left(ClassCastFailure)  
		}
 	}
 }