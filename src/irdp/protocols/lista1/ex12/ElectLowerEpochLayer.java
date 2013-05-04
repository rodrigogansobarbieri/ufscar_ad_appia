package irdp.protocols.lista1.ex12;

import irdp.protocols.tutorialDA.events.ProcessInitEvent;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;

public class ElectLowerEpochLayer extends Layer {

	public ElectLowerEpochLayer(){
		evAccept = new Class[4];
		evAccept[0] = ChannelInit.class;
		evAccept[1] = ProcessInitEvent.class;
		evAccept[2] = HeartbeatEvent.class;
		evAccept[3] = TimeTickEvent.class;
		
		evRequire = new Class[3];
		evRequire[0] = ChannelInit.class;
		evRequire[1] = ProcessInitEvent.class;
		evRequire[2] = HeartbeatEvent.class;
		
		evProvide = new Class[2];
		evProvide[0] = HeartbeatEvent.class;
		evProvide[1] = LeaderTrustEvent.class;
	}
	
	@Override
	public Session createSession() {
		return new ElectLowerEpochSession(this);
	}

}
