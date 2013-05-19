/*
 *
 * Hands-On code of the book Introduction to Reliable Distributed Programming
 * by Christian Cachin, Rachid Guerraoui and Luis Rodrigues
 * Copyright (C) 2005-2011 Luis Rodrigues
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 *
 * Contact
 * 	Address:
 *		Rua Alves Redol 9, Office 605
 *		1000-029 Lisboa
 *		PORTUGAL
 * 	Email:
 * 		ler@ist.utl.pt
 * 	Web:
 *		http://homepages.gsd.inesc-id.pt/~ler/
 * 
 */

package irdp.protocols.lista2.ex3;

import irdp.protocols.tutorialDA.basicBroadcast.BasicBroadcastLayer;
import irdp.protocols.tutorialDA.utils.ProcessSet;
import irdp.protocols.tutorialDA.utils.SampleProcess;
import net.sf.appia.core.*;
import net.sf.appia.protocols.tcpcomplete.TcpCompleteLayer;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.StringTokenizer;

public class Example5 {

	private static ProcessSet buildProcessSet(String filename, int selfProc) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(
					filename)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		String line;
		StringTokenizer st;
		boolean hasMoreLines = true;
		ProcessSet set = new ProcessSet();
		// reads lines of type: <process number> <IP address> <port>
		while(hasMoreLines) {
			try {
				line = reader.readLine();
				if (line == null)
					break;
				st = new StringTokenizer(line);
				if (st.countTokens() != 3) {
					System.err.println("Wrong line in file: "+st.countTokens());
					continue;
				}
				int procNumber = Integer.parseInt(st.nextToken());
				InetAddress addr = InetAddress.getByName(st.nextToken());
				int portNumber = Integer.parseInt(st.nextToken());
				boolean self = (procNumber == selfProc);
				SampleProcess process = new SampleProcess(new InetSocketAddress(addr,
						portNumber), procNumber, self);
				set.addProcess(process, procNumber);
			} catch (IOException e) {
				hasMoreLines = false;
			} catch (NumberFormatException e) {
				System.err.println(e.getMessage());
			}
		} // end of while
		return set;
	}

	public static void main(String[] args) {
		System.out.println("I am Process " + args[1]);

		/* Create layers and put them on a array */
		Layer[] qos = {new TcpCompleteLayer(), new BasicBroadcastLayer(), new EagerRBLayer(), new ApplLayer()};

		/* Create a QoS */
		QoS myQoS = null;
		try {
			myQoS = new QoS("Reliable Broadcast QoS", qos);
		} catch (AppiaInvalidQoSException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		/* Create a channel. Uses default event scheduler. */
		Channel channel = myQoS.createUnboundChannel("Reliable Broadcast Channel");
		ChannelCursor cc = channel.getCursor();

		ApplSession sas = (ApplSession) qos[qos.length - 1]
				.createSession();
		sas.init(buildProcessSet(args[0], Integer.parseInt(args[1])));

		try {
			cc.top();
			cc.setSession(sas);
		} catch (AppiaCursorException ex) {
			System.err.println("Unexpected exception in main. Type code:" + ex.type);
			System.exit(1);
		}


		try {
			channel.start();
		} catch (AppiaDuplicatedSessionsException ex) {
			System.err.println("Sessions binding strangely resulted in "
					+ "one single sessions occurring more than " + "once in a channel");
			System.exit(1);
		}

		/* All set. Appia main class will handle the rest */
		System.out.println("Starting Appia...");
		Appia.run();
	}

}