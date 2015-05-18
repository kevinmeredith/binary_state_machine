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

 	def addToStream(b: Binary, counterActor: ActorRef): Unit = {
 		counterActor ! b
 	}

 	def terminateStream(counterActor: ActorRef): Option[State] = {
 		implicit val timeout: akka.util.Timeout = Timeout(3, TimeUnit.SECONDS)
 		val state: Try[Future[State]] = Try { (counterActor ? TerminateStream).mapTo[State] }
 		try {
 			state match {
 				case Success(s) => Some(Await.result(s, 3.seconds))
				case Failure(_) => None

 			}
 		}
 		catch {
 			case e: TimeoutException => println(e); None
 		}
 	}
 }