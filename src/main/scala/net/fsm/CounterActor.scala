package net.fsm

import akka.actor.{Actor, ActorLogging, Props}

object CounterActor {

	def makeErrorMsg(x: Any): String =	
		s"Received ${x.toString}, but only 0 or 1 are accepted."
}

class CounterActor extends Actor with ActorLogging {

	import CounterActor._
	import Common._

	override def receive =
		s0 

	def s0: PartialFunction[Any, Unit] = {
		case ONE             => context.become(s1)
		case ZERO            => ()
		case TerminateStream => sender ! State0
		case badInput        => log.error(makeErrorMsg(badInput))
	}

	def s1: PartialFunction[Any, Unit] = {
		case ONE             => context.become(s2)
		case ZERO            => context.become(s0)
		case TerminateStream => sender ! State1; context.become(s0)
		case badInput        => log.error(makeErrorMsg(badInput))
	}

	def s2: PartialFunction[Any, Unit] = {
		case ONE             => context.become(s3)
		case ZERO            => context.become(s0)
		case TerminateStream => sender ! State2; context.become(s0) 
		case badInput        => log.error(makeErrorMsg(badInput))
	}	

	def s3: PartialFunction[Any, Unit] = {
		case ONE             => ()
		case ZERO            => context.become(s0)
		case TerminateStream => sender ! State3; context.become(s0)
		case badInput        => log.error(makeErrorMsg(badInput))
	}	
	
}