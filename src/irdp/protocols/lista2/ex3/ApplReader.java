package irdp.protocols.lista2.ex3;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Direction;
import net.sf.appia.core.events.SendableEvent;
import net.sf.appia.core.message.Message;

import java.util.StringTokenizer;

public class ApplReader extends Thread {

  private ApplSession parentSession;
  private java.io.BufferedReader keyb;
  private String local = null;

  public ApplReader(ApplSession parentSession) {
    super();
    this.parentSession = parentSession;
    keyb = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
  }

  public void run() {
    while (true) {
      try {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        System.out.print("> ");
        local = keyb.readLine();
        if (local.equals(""))
          continue;
        StringTokenizer st = new StringTokenizer(local);
        /*
         * creates the event, push the message and sends this to the appia
         * channel.
         */
        SendableEvent asyn = new SendableEvent();
        Message message = asyn.getMessage();
        String msg = "";
        while (st.hasMoreTokens())
          msg += (st.nextToken() + " ");
        message.pushString(msg);
        asyn.asyncGo(parentSession.channel, Direction.DOWN);
      } catch (java.io.IOException e) {
        e.printStackTrace();
      } catch (AppiaEventException e) {
        e.printStackTrace();
      }
    }
  }
}
