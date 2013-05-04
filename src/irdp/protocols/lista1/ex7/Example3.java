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

package irdp.protocols.lista1.ex7;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.StringTokenizer;

import irdp.protocols.tutorialDA.utils.ProcessSet;
import irdp.protocols.tutorialDA.utils.SampleProcess;
import net.sf.appia.core.Appia;
import net.sf.appia.core.AppiaCursorException;
import net.sf.appia.core.AppiaDuplicatedSessionsException;
import net.sf.appia.core.AppiaInvalidQoSException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.ChannelCursor;
import net.sf.appia.core.Layer;
import net.sf.appia.core.QoS;
import net.sf.appia.protocols.udpsimple.UdpSimpleLayer;


public class Example3 {

  public static void main(String[] args) {
	System.out.println("I am Process " + args[1]);
	  
    /* Create layers and put them on a array */
    Layer[] qos = {new UdpSimpleLayer(), new StubbornLinkLayer(), new PerfectLinkLayer(), new EncapsulationLayer(), new ApplLayer()};

    /* Create a QoS */
    QoS myQoS = null;
    try {
      myQoS = new QoS("Chat_QoS", qos);
    } catch (AppiaInvalidQoSException ex) {
      ex.printStackTrace();
      System.exit(1);
    }

    /* Create a channel. Uses default event scheduler. */
    Channel channel = myQoS.createUnboundChannel("Chat_Channel");
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
      System.err.println("Error in start");
      System.exit(1);
    }

    /* All set. Appia main class will handle the rest. */
    System.out.println("Starting Appia...");
    Appia.run();
  }
  
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
  
  
 
}
