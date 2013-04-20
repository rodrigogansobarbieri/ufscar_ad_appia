package irdp.protocols.lista1.ex6;

import irdp.protocols.tutorialDA.events.*;
import irdp.protocols.tutorialDA.utils.ProcessSet;
import net.sf.appia.core.*;
import net.sf.appia.core.events.channel.ChannelClose;
import net.sf.appia.core.events.channel.ChannelInit;
import net.sf.appia.protocols.common.RegisterSocketEvent;

import java.net.InetSocketAddress;


public class ApplSession extends Session {

	Channel channel;
	private ProcessSet processes;
	private ApplReader reader;
	private boolean blocked = false;

	public ApplSession(Layer layer) {
		super(layer);
	}

	public void init(ProcessSet processes) {
		this.processes = processes;
	}

	public void handle(Event event) {
		if (event instanceof AddressedSendableEvent)
			handleAddressedSendableEvent((AddressedSendableEvent) event);
		else if (event instanceof ChannelInit)
			handleChannelInit((ChannelInit) event);
		else if (event instanceof ChannelClose)
			handleChannelClose((ChannelClose) event);
		else if (event instanceof RegisterSocketEvent)
			handleRegisterSocket((RegisterSocketEvent) event);
	}

	private void handleRegisterSocket(RegisterSocketEvent event) {
		if (event.error) {
			System.out.println("Address already in use!");
			System.exit(2);
		}
	}

	private void handleChannelInit(ChannelInit init) {
		try {
			init.go();
		} catch (AppiaEventException e) {
			e.printStackTrace();
		}
		channel = init.getChannel();

		try {
			RegisterSocketEvent rse = new RegisterSocketEvent(channel,
					Direction.DOWN, this);
			rse.port = ((InetSocketAddress) processes.getSelfProcess().getSocketAddress()).getPort();
			rse.localHost = ((InetSocketAddress)processes.getSelfProcess().getSocketAddress()).getAddress();
			rse.go();
			ProcessInitEvent processInit = new ProcessInitEvent(channel,
					Direction.DOWN, this);
			processInit.setProcessSet(processes);
			processInit.go();
		} catch (AppiaEventException e1) {
			e1.printStackTrace();
		}
		System.out.println("Channel is open.");
		reader = new ApplReader(this);
		reader.start();
	}

	private void handleChannelClose(ChannelClose close) {
		channel = null;
		System.out.println("Channel is closed.");
	}

	private void handleAddressedSendableEvent(AddressedSendableEvent event) {
		if (event.getDir() == Direction.DOWN)
			handleOutgoingEvent(event);
		else
			handleIncomingEvent(event);
	}

	private void handleIncomingEvent(AddressedSendableEvent event) {
		String message = event.getMessage().popString();
		System.out.print("Received event with message: " + message + "\n>");
	}

	private void handleOutgoingEvent(AddressedSendableEvent event) {
		if (blocked) {
			System.out
			.println("The group is blocked, therefore message can not be sent.");
			return;
		}

		try {
			event.go();
		} catch (AppiaEventException e) {
			e.printStackTrace();
		}

	}



}
