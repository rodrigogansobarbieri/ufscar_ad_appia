package irdp.protocols.lista1.ex12;

import java.net.SocketAddress;

import irdp.protocols.tutorialDA.utils.SampleProcess;

public class EpochProcess extends SampleProcess{

	private static final long serialVersionUID = -1535938977741492585L;

	public EpochProcess(SocketAddress addr, int proc, boolean self) {
		super(addr, proc, self);
	}

	private int epoch;
	
	public int getEpoch(){
		return epoch;
	}
	
	public void setEpoch(int value){
		epoch = value;
	}
	
	
	
	
}
