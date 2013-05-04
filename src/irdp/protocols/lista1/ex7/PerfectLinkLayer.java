package irdp.protocols.lista1.ex7;

import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.SendableEvent;
import net.sf.appia.core.events.channel.ChannelInit;

public class PerfectLinkLayer extends Layer {

	public PerfectLinkLayer(){
		evAccept = new Class[2];
		evAccept[0] = ChannelInit.class;
		evAccept[1] = SendableEvent.class;
		
		evRequire = new Class[2];
		evRequire[0] = ChannelInit.class;
		evRequire[1] = SendableEvent.class;
		
		evProvide = new Class[0];
	}
	
	@Override
	public Session createSession() {
		return new PerfectLinkSession(this);
	}
	
}
