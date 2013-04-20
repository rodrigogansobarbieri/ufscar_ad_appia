package irdp.protocols.lista1.ex3;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Direction;
import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;

public class JobHandlerSession extends Session{

	public JobHandlerSession(Layer layer) {
		super(layer);
	}

	public void handle(Event event) {
		if (event instanceof ChannelInit)
			handleChannelInit((ChannelInit) event);
		else if (event instanceof JobSubmitEvent)
			handleJobSubmitEvent((JobSubmitEvent) event);
		else if (event instanceof PrintConfirmEvent)
			handlePrintConfirmEvent((PrintConfirmEvent) event);

	}

	private void handleChannelInit(ChannelInit init){
		try {
			init.go();
		} catch (AppiaEventException e) {
			e.printStackTrace();
		}
	}

	private void handleJobSubmitEvent(JobSubmitEvent event){
		PrintRequestEvent request = new PrintRequestEvent();
		try	{
			System.out.println("[JobHandler] Job Received, creating Print Request");
			request.setChannel(event.getChannel());
			request.setSourceSession(this);
			request.setString(event.getString());
			request.setId(event.getId());
			request.setDir(event.getDir());
			request.init();
			request.go();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private void handlePrintConfirmEvent(PrintConfirmEvent event){
		
		System.out.println("\n[JobHandler] Received Print Confirmation, creating Job Confirmation \n");
		JobConfirmEvent confirm = new JobConfirmEvent();
		try {
			confirm.setChannel(event.getChannel());
			confirm.setDir(Direction.UP); 
			confirm.setSourceSession(this);
			confirm.setId(event.getId()); 
			confirm.init();
			confirm.go(); 
		} catch (Exception e){
			e.printStackTrace();
		}
	}


}
