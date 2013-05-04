package irdp.protocols.lista1.ex12;

import irdp.protocols.tutorialDA.utils.MessageID;

public class MessageIDContent extends MessageID{

	public MessageIDContent(int p, int s) {
		super(p, s);
	}

	private static final long serialVersionUID = -1318526295445002856L;
	private Object content;

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
}
