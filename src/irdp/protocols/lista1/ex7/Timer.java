package irdp.protocols.lista1.ex7;

import net.sf.appia.core.Channel;
import net.sf.appia.core.Direction;

public class Timer extends Thread {

	private int interval = 0;
	private Channel channel;
	
	public Timer(int interval, Channel channel){
		this.interval = interval;
		this.channel = channel;
	}
	
	public void run(){
		try {
			while (true){
				sleep(interval);
				TimeTickEvent tick = new TimeTickEvent();
				tick.asyncGo(channel, Direction.DOWN);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		
	}
	
}
