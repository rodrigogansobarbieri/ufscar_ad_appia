package irdp.protocols.lista1.ex7;

import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.SendableEvent;
import net.sf.appia.core.events.channel.ChannelInit;

public class StubbornLinkLayer extends Layer {

	@Override
	public Session createSession() {
		return new StubbornLinkSession(this);
	}
	
	public StubbornLinkLayer(){
		evAccept = new Class[3];
		evAccept[0] = ChannelInit.class;
		evAccept[1] = SendableEvent.class;
		evAccept[2] = TimeTickEvent.class;
		
		evRequire = new Class[2];
		evRequire[0] = ChannelInit.class;
		evRequire[1] = SendableEvent.class;
		
		evProvide = new Class[1];
		evProvide[0] = SendableEvent.class;
		
		
	}

}
