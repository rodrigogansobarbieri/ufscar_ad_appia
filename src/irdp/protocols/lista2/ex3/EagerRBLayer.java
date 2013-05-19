package irdp.protocols.lista2.ex3;

import irdp.protocols.tutorialDA.events.ProcessInitEvent;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.SendableEvent;
import net.sf.appia.core.events.channel.ChannelInit;

public class EagerRBLayer extends Layer {


	public EagerRBLayer(){
		evAccept = new Class[3];
		evAccept[0] = ChannelInit.class;
		evAccept[1] = ProcessInitEvent.class;
		evAccept[2] = SendableEvent.class;

		evRequire = new Class[3];
		evRequire[0] = ChannelInit.class;
		evRequire[1] = ProcessInitEvent.class;
		evRequire[2] = SendableEvent.class;

		evProvide = new Class[0];
		
	}

	@Override
	public Session createSession() {
		return new EagerRBSession(this);
	}

}
