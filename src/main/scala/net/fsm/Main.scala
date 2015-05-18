package net.fsm

import akka.actor.{Actor, ActorRef, ActorLogging, Props}
import akka.pattern.ask
import akka.util.Timeout
import Common.{Binary, State, TerminateStream}
import scala.concurrent.{Future, TimeoutException, Await}
import scala.concurrent.duration._
import java.util.concurrent.TimeUnit

/**
 * Accepts request to add a 0 or 1 to the existing "stream" of binary. 
 * 
 * 
 */ 
 object Main {

 	def addToStream(b: Binary, counterActor: ActorRef): Unit = {
 		counterActor ! b
 	}

 	def terminateStream(counterActor: ActorRef): Option[State] = {
 		implicit val timeout: akka.util.Timeout = Timeout(3, TimeUnit.SECONDS)
 		val state: Future[State] = (counterActor ? TerminateStream).mapTo[State]
 		try {
 			val result: State = Await.result(state, 3 seconds)
 			Some(result)
 		}
 		catch {
 			case e: TimeoutException => println(e); None
 		}
 	}
 }