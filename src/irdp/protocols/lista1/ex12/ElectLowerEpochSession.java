package irdp.protocols.lista1.ex12;

import irdp.protocols.tutorialDA.events.ProcessInitEvent;
import irdp.protocols.tutorialDA.utils.SampleProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Direction;
import net.sf.appia.core.Event;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;

public class ElectLowerEpochSession extends Session {

	private int processNumber;
	private int epoch;
	private List<EpochProcess> candidates;
	private EpochProcess leader;
	private List<EpochProcess> processes;
	private int delay;
	private int delta;
	private Timer timer;
	
	public ElectLowerEpochSession(Layer layer) {
		super(layer);
	}

	public void handle(Event event) {
		if (event instanceof ChannelInit)
			handleChannelInit((ChannelInit) event);
		else if (event instanceof ProcessInitEvent)
			handleProcessInitEvent((ProcessInitEvent) event);
		else if (event instanceof HeartbeatEvent)
			handleHeartbeatEvent((HeartbeatEvent) event);
		else if (event instanceof TimeTickEvent)
			handleTimeTickEvent((TimeTickEvent) event);
	}

	private void handleTimeTickEvent(TimeTickEvent event) {
		EpochProcess newLeader = maxRank(candidates);
		if (newLeader != null && !newLeader.getSocketAddress().equals(this.leader.getSocketAddress())){
			delay = delay + delta;
			leader = newLeader;
			LeaderTrustEvent lt = createLeaderTrustEvent(String.valueOf(leader.getProcessNumber()));
			lt.setChannel(event.getChannel());
			try { 
				lt.init();
				lt.go();
			} catch (AppiaEventException e){
				e.printStackTrace();
			}
		}
		candidates.clear();
		for (EpochProcess ep : processes){
			HeartbeatEvent hb = createHeartbeatEvent(ep);
			hb.setChannel(event.getChannel());
			try {
				hb.init();
				hb.go();
			} catch (AppiaEventException e){
				e.printStackTrace();
			}
		}
		timer.setInterval(delay);

	}

	private void handleHeartbeatEvent(HeartbeatEvent event) {
		int arrivedEpoch;
		arrivedEpoch = event.getMessage().popInt();
		for (EpochProcess candidate : candidates){
			if (event.source.equals(candidate.getSocketAddress()) && arrivedEpoch < candidate.getEpoch())
				candidates.remove(candidate);
		}
		for (EpochProcess ep : processes){
			if (ep.getSocketAddress().equals(event.source)){
				ep.setEpoch(arrivedEpoch);
				candidates.add(ep);
			}
		}
		try {
			event.go();
		} catch (AppiaEventException e){
			e.printStackTrace();
		}

	}

	private LeaderTrustEvent createLeaderTrustEvent(String leaderName){
		LeaderTrustEvent lt = new LeaderTrustEvent();
		lt.setSourceSession(this);
		lt.setDir(Direction.UP);
		lt.setLeaderName(leaderName);
		return lt;
	}

	private HeartbeatEvent createHeartbeatEvent(EpochProcess ep){
		HeartbeatEvent hb = new HeartbeatEvent();
		hb.setSourceSession(this);
		hb.dest = ep.getSocketAddress();
		hb.setDir(Direction.DOWN);
		hb.getMessage().pushInt(this.epoch);
		return hb;
	}

	private void handleProcessInitEvent(ProcessInitEvent event) {
		EpochProcess newProcess = null;
		for (SampleProcess p : event.getProcessSet().getAllProcesses()){
			if (p.isSelf())
				processNumber = p.getProcessNumber();
			newProcess = new EpochProcess(p.getSocketAddress(),p.getProcessNumber(),p.isSelf());
			newProcess.setEpoch(0);
			processes.add(newProcess);
		}
		try {
			event.go();
		} catch (AppiaEventException e){
			e.printStackTrace();
		}

		epoch = retrieveEpoch();
		epoch++;
		save(epoch);
		leader = maxRank(processes);
		LeaderTrustEvent lt = createLeaderTrustEvent(String.valueOf(leader.getProcessNumber()));
		lt.setChannel(event.getChannel());
		try { 
			lt.init();
			lt.go();
		} catch (AppiaEventException e){
			e.printStackTrace();
		}
		for (EpochProcess ep : processes){
			HeartbeatEvent hb = createHeartbeatEvent(ep);
			hb.setChannel(event.getChannel());
			try {
				hb.init();
				hb.go();
			} catch (AppiaEventException e){
				e.printStackTrace();
			}
		}
		timer = new Timer(delay,event.getChannel());
		timer.start();

	}

	private void handleChannelInit(ChannelInit init) {
		epoch = 0;
		delta = 1000;
		candidates = new ArrayList<EpochProcess>();
		processes = new ArrayList<EpochProcess>();
		delay = delta;

		try {
			init.go();
		} catch (AppiaEventException e) {
			e.printStackTrace();
		}
	}

	private EpochProcess maxRank(List<EpochProcess> list){
		EpochProcess newLeader = null;
		for (EpochProcess p : list){
			if (newLeader == null || 
					(newLeader != null && 
					(p.getEpoch() < newLeader.getEpoch() || 
							(p.getEpoch() == newLeader.getEpoch() && p.getProcessNumber() < newLeader.getProcessNumber()))))
				newLeader = p;
			
		}
		return newLeader;
	}

	private void save(int epoch){
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("epoch-p" + processNumber + ".txt"));
			out.write(String.valueOf(epoch));
			out.flush();
			out.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private int retrieveEpoch(){
		int epoch = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader("epoch-p" + processNumber + ".txt"));
			String value = in.readLine();
			if (value != null && value.length() > 0) 
				epoch = Integer.valueOf(value);
			in.close();
		} catch (IOException e){
			e.printStackTrace();
		}
		return epoch;
	}

}
