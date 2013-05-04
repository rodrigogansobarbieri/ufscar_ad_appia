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

public class StubbornLinkSession extends Session {

	private List<SendableEvent> sentMessages;
	
	public StubbornLinkSession(Layer layer) {
		super(layer);
	}
	
	public void handle(Event event) {
		if (event instanceof ChannelInit)
			handleChannelInit((ChannelInit) event);
		else if (event instanceof TimeTickEvent)
			handleTickEvent((TimeTickEvent) event);
		else if (event instanceof SendableEvent) {
			if (event.getDir() == Direction.DOWN)
				handleSendMessage((SendableEvent) event);
			else
				handleDeliverMessage((SendableEvent) event);
		}
		
	}		
	
	private void handleTickEvent(TimeTickEvent event) {
		for (SendableEvent evnt : sentMessages){
			try {
				SendableEvent resentMessage = (SendableEvent) evnt.cloneEvent();
				resentMessage.setSourceSession(this);
				resentMessage.setChannel(event.getChannel());
				resentMessage.setDir(Direction.DOWN);
				resentMessage.init();
				resentMessage.go();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
	}

	private void handleDeliverMessage(SendableEvent event) {
		try {
			event.go();
		} catch (AppiaEventException e){
			e.printStackTrace();
		}
		
	}

	private void handleSendMessage(SendableEvent event) {
		try { 
			SendableEvent storedMessage = (SendableEvent) event.cloneEvent();
			sentMessages.add(storedMessage);
		} catch (CloneNotSupportedException e){
			e.printStackTrace();
		}
		try {
			event.go();
		} catch (AppiaEventException e){
			e.printStackTrace();
		}
		
	}

	private void handleChannelInit(ChannelInit init) {
		sentMessages = new ArrayList<SendableEvent>();
		new Timer(10000,init.getChannel()).start();
		try {
			init.go();
		} catch (AppiaEventException e) {
			e.printStackTrace();
		}
	}

}
