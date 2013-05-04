package irdp.protocols.lista1.ex7;

import irdp.protocols.tutorialDA.events.ProcessInitEvent;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelClose;
import net.sf.appia.core.events.channel.ChannelInit;
import net.sf.appia.protocols.common.RegisterSocketEvent;

public class ApplLayer extends Layer {

  public ApplLayer() {
    evProvide = new Class[3];
    evProvide[0] = ProcessInitEvent.class;
    evProvide[1] = RegisterSocketEvent.class;
    evProvide[2] = AddressedSendableEvent.class;
    
    evRequire = new Class[1];
    evRequire[0] = ChannelInit.class;

    evAccept = new Class[4];
    evAccept[0] = ChannelInit.class;
    evAccept[1] = ChannelClose.class;
    evAccept[2] = RegisterSocketEvent.class;
    evAccept[3] = AddressedSendableEvent.class;
    
  }

  public Session createSession() {
    return new ApplSession(this);
  }

}
