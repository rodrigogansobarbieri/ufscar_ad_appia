package irdp.protocols.lista1.ex7;

import java.util.ArrayList;
import java.util.List;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Direction;
import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.SendableEvent;
import net.sf.appia.core.events.channel.ChannelInit;

public class PerfectLinkSession extends Session {

	private List<Integer> deliveredMessages;

	public PerfectLinkSession(Layer layer) {
		super(layer);
	}


	public void handle(Event event) {
		if (event instanceof ChannelInit)
			handleChannelInit((ChannelInit) event);
		else if (event instanceof SendableEvent) {
			if (event.getDir() == Direction.DOWN)
				handleSendMessage((SendableEvent) event);
			else
				handleDeliverMessage((SendableEvent) event);
		}

	}		

	private void handleDeliverMessage(SendableEvent event) {
		PerfectLinkID id = (PerfectLinkID) event.getMessage().popObject();
		try {
			if (!deliveredMessages.contains(id.getHashcode())){
				event.getMessage().pushObject(id.getMessage());
				deliveredMessages.add(id.getHashcode());
				event.go();
			}
		} catch (Exception e){
			e.printStackTrace();
		}

	}

	private void handleSendMessage(SendableEvent event) {
		PerfectLinkID id = new PerfectLinkID();
		id.setMessage(event.getMessage().popObject());
		id.setHashcode(event.hashCode());
		event.getMessage().pushObject(id);
		try {
			event.go();
		} catch (AppiaEventException e){
			e.printStackTrace();
		}

	}

	private void handleChannelInit(ChannelInit init) {
		deliveredMessages = new ArrayList<Integer>();
		try {
			init.go();
		} catch (AppiaEventException e) {
			e.printStackTrace();
		}
	}


}
