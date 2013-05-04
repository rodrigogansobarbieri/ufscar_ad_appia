package irdp.protocols.lista1.ex12;

import net.sf.appia.core.Event;

public class LeaderTrustEvent extends Event {

	private String leaderName;

	public String getLeaderName() {
		return leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}
	
}
