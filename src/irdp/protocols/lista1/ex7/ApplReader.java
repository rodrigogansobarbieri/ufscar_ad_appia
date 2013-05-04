package irdp.protocols.lista1.ex7;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Direction;
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
        AddressedSendableEvent asyn = new AddressedSendableEvent();
        Message message = asyn.getMessage();
        asyn.setDest(Integer.parseInt(st.nextToken()));
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
