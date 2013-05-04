package irdp.protocols.lista1.ex7;

import irdp.protocols.tutorialDA.events.ProcessInitEvent;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.SendableEvent;
import net.sf.appia.core.events.channel.ChannelInit;

public class EncapsulationLayer extends Layer{

	public EncapsulationLayer(){
		evAccept = new Class[3];
		evAccept[0] = SendableEvent.class;
		evAccept[1] = ChannelInit.class;
		evAccept[2] = ProcessInitEvent.class;
		
		evProvide = new Class[0];
		
		evRequire = new Class[3];
		evRequire[0] = SendableEvent.class;
		evRequire[1] = ChannelInit.class;
		evRequire[2] = ProcessInitEvent.class;
		
		
		
	}
	
	@Override
	public Session createSession() {
		return new EncapsulationSession(this);
	}

	
	
	
}
