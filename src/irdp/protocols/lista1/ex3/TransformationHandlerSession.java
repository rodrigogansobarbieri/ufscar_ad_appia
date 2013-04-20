package irdp.protocols.lista1.ex3;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;

public class TransformationHandlerSession extends Session {

	public TransformationHandlerSession(Layer layer) {
		super(layer);
	}

	public void handle(Event event) {
		if (event instanceof ChannelInit)
			handleChannelInit((ChannelInit) event);
		else if (event instanceof JobSubmitEvent)
			handleJobSubmitEvent((JobSubmitEvent) event);
		else if (event instanceof JobConfirmEvent)
			handleJobConfirmEvent((JobConfirmEvent) event);

	}

	private void handleChannelInit(ChannelInit init){
		try {
			init.go();
		} catch (AppiaEventException e) {
			e.printStackTrace();
		}
	}

	private void handleJobSubmitEvent(JobSubmitEvent event){
		try	{
			System.out.println("\n[TransformationHandler] Transforming \n");
			event.str = event.str.toUpperCase();
			event.go();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private void handleJobConfirmEvent(JobConfirmEvent event){
		try {
			System.out.println("[TransformationHandler] Passing through Job Confirmation");
			event.go(); 
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
