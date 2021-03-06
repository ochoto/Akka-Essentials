package org.akka.essentials.zeromq.example4
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.zeromq.Bind
import akka.zeromq.Frame
import akka.zeromq.Listener
import akka.zeromq.SocketType
import akka.zeromq.ZMQMessage
import akka.zeromq.ZeroMQExtension
import akka.util.duration.intToDurationInt

case class Tick

class PushActor extends Actor with ActorLogging {
  val pushSocket = ZeroMQExtension(context.system).newSocket(SocketType.Push, Bind("tcp://127.0.0.1:1234"), Listener(self))
  var count = 0;
  override def preStart() {
    context.system.scheduler.schedule(1 second, 1 second, self, Tick)
  }

  def receive: Receive = {
    case Tick =>
      count += 1
      var payload = "Hi there! (" + count + ")"
      pushSocket ! ZMQMessage(Seq(Frame(payload)))
  }
}