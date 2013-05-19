package irdp.protocols.lista2.ex3;

import java.util.LinkedList;

import irdp.protocols.tutorialDA.events.ProcessInitEvent;
import irdp.protocols.tutorialDA.utils.MessageID;
import irdp.protocols.tutorialDA.utils.ProcessSet;
import irdp.protocols.tutorialDA.utils.SampleProcess;
import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Direction;
import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.SendableEvent;
import net.sf.appia.core.events.channel.ChannelInit;

public class EagerRBSession extends Session {

	// List of MessageID objects
	private LinkedList<MessageID> delivered;

	private ProcessSet processes;
	private int seqNumber;

	public EagerRBSession(Layer layer) {
		super(layer);
		seqNumber = 0;
	}

	public void handle(Event event) {
		if (event instanceof ChannelInit)
			handleChannelInit((ChannelInit) event);
		else if (event instanceof ProcessInitEvent)
			handleProcessInitEvent((ProcessInitEvent) event);
		else if (event instanceof SendableEvent) {
			if (event.getDir() == Direction.DOWN)
				// UPON event from the above protocol (or application)
				rbBroadcast((SendableEvent) event);
			else
				// UPON event from the bottom protocol (or perfect point2point links)
				bebDeliver((SendableEvent) event);
		}
	}

	private void handleChannelInit(ChannelInit init) {
		try {
			init.go();
		} catch (AppiaEventException e) {
			e.printStackTrace();
		}
		delivered = new LinkedList<MessageID>();
	}

	private void handleProcessInitEvent(ProcessInitEvent event) {
		processes = event.getProcessSet();
		try {
			event.go();
		} catch (AppiaEventException e) {
			e.printStackTrace();
		}
	}

	private void rbBroadcast(SendableEvent event) {
		// first we take care of the header of the message
		SampleProcess self = processes.getSelfProcess();
		MessageID msgID = new MessageID(self.getProcessNumber(), seqNumber);
		seqNumber++;
		event.getMessage().pushObject(msgID);
		// broadcast the message
		bebBroadcast(event);
	}

	private void bebBroadcast(SendableEvent event) {
		try {
			event.setDir(Direction.DOWN);
			event.setSourceSession(this);
			event.init();
			event.go();
		} catch (AppiaEventException e) {
			e.printStackTrace();
		}
	}

	private void bebDeliver(SendableEvent event) {
		MessageID msgID = (MessageID) event.getMessage().peekObject();
		if (!delivered.contains(msgID)) {
			delivered.add(msgID);
			// removes the header from the message (sender and seqNumber) and delivers it
			SendableEvent cloned = null;
			try {
				cloned = (SendableEvent) event.cloneEvent();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				return;
			}
			event.getMessage().popObject();
			try {
				event.go();
			} catch (AppiaEventException e) {
				e.printStackTrace();
			}
			SendableEvent retransmission = null;

			try {
				retransmission = (SendableEvent) cloned.cloneEvent();
			} catch (CloneNotSupportedException e1) {
				e1.printStackTrace();
			}
			bebBroadcast(retransmission);
		}
		else {
			System.out.println("[EagerRBSession] Received a message already delivered");
		}
	}
}
