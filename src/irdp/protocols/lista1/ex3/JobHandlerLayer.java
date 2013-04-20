package irdp.protocols.lista1.ex3;

import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;

public class JobHandlerLayer extends Layer {

	public JobHandlerLayer(){
		
		evProvide = new Class[2];
		evProvide[0] = JobConfirmEvent.class;
		evProvide[1] = PrintRequestEvent.class;
		
		evRequire = new Class[0];
		
		evAccept = new Class[3];
		evAccept[0] = PrintConfirmEvent.class;
		evAccept[1] = JobSubmitEvent.class;
		evAccept[2] = ChannelInit.class;
		
	}
	
	@Override
	public Session createSession() {
		return new JobHandlerSession(this);
	}

}
