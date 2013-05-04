package irdp.protocols.lista1.ex12;

import net.sf.appia.core.Channel;
import net.sf.appia.core.Direction;

public class Timer extends Thread {

	private int interval = 0;
	private Channel channel;
	
	public Timer(int interval, Channel channel){
		this.setInterval(interval);
		this.channel = channel;
	}
	
	public void run(){
		try {
			while (true){
				sleep(getInterval());
				TimeTickEvent tick = new TimeTickEvent();
				tick.asyncGo(channel, Direction.DOWN);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}
	
}
