package irdp.protocols.lista1.ex7;

import irdp.protocols.tutorialDA.events.ProcessInitEvent;
import irdp.protocols.tutorialDA.utils.MessageID;
import irdp.protocols.tutorialDA.utils.ProcessSet;
import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Direction;
import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.SendableEvent;
import net.sf.appia.core.events.channel.ChannelInit;

public class EncapsulationSession extends Session {

	public EncapsulationSession(Layer layer) {
		super(layer);
		seqNumber = 0;
	}

	private int seqNumber;
	private ProcessSet processes;

	public void handle(Event event) {
		if (event instanceof ChannelInit)
			handleChannelInit((ChannelInit) event);
		else if (event instanceof ProcessInitEvent)
			handleProcessInitEvent((ProcessInitEvent) event);
		else if (event instanceof SendableEvent) {
			if (event.getDir() == Direction.DOWN)
				handleSendMessage((SendableEvent) event);
			else
				handleDeliverMessage((SendableEvent) event);
		}
		
	}		


		private void handleProcessInitEvent(ProcessInitEvent event) {
			processes = event.getProcessSet();
			try {
				event.go();
			} catch (AppiaEventException e) {
				e.printStackTrace();
			}
		}

		private void handleChannelInit(ChannelInit init) {
			try {
				init.go();
			} catch (AppiaEventException e) {
				e.printStackTrace();
			}
		}


		private void handleSendMessage(SendableEvent event) {
			MessageID msgID = new MessageID(processes.getSelfRank(), seqNumber);
			int dest = ((AddressedSendableEvent)event).getDest();
			seqNumber++;
			SendableEvent clone = null;
		      try {
		        clone = (SendableEvent) event.cloneEvent();
		        clone.getMessage().pushObject(msgID);
		      } catch (CloneNotSupportedException e) {
		        e.printStackTrace();
		      }
			try {
				clone.setDir(Direction.DOWN);
				clone.setSourceSession(this);
				clone.dest = processes.getProcess(dest).getSocketAddress();
				clone.init();
				clone.go();
			} catch (AppiaEventException e) {
				e.printStackTrace();
			}
		}

		private void handleDeliverMessage(SendableEvent event) {
			MessageID msgID = (MessageID) event.getMessage().popObject();
			SendableEvent clone = null;
			try {
				clone = (SendableEvent) event.cloneEvent();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			try {
				clone.setDir(Direction.UP);
				clone.setSourceSession(this);
				clone.source = processes.getProcess(msgID.process).getSocketAddress();
				clone.init();
				clone.go();
			} catch (AppiaEventException e) {
				e.printStackTrace();
			}
		}

	}
