package irdp.protocols.lista1.ex7;

import java.io.Serializable;

public class PerfectLinkID implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7846460454764717688L;

	private int hashcode;
	
	private Object message;

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public int getHashcode() {
		return hashcode;
	}

	public void setHashcode(int hashcode) {
		this.hashcode = hashcode;
	}
	
}
