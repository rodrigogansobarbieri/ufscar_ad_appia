package irdp.protocols.lista1.ex3;

import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;

public class TransformationHandlerLayer extends Layer {

	public TransformationHandlerLayer(){
		evProvide = new Class[2];
		evProvide[0] = JobSubmitEvent.class;
		evProvide[1] = JobConfirmEvent.class;
		          
		evRequire = new Class[1];
		evRequire[0] = JobSubmitEvent.class;
		
		evAccept = new Class[3];
		evAccept[0] = ChannelInit.class;
		evAccept[1] = JobSubmitEvent.class;
		evAccept[2] = JobConfirmEvent.class;
		
	}
	
	
	@Override
	public Session createSession() {
		return new TransformationHandlerSession(this);
	}

}
