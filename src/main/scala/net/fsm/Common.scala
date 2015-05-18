package net.fsm

object Common {
	
 	sealed trait Binary
 	case object ZERO extends Binary
 	case object ONE extends Binary

	sealed trait State
	case object State0 extends State
	case object State1 extends State
	case object State2 extends State
	case object State3 extends State

	case object TerminateStream
}