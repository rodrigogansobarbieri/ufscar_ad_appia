package irdp.protocols.lista1.ex3;

import net.sf.appia.core.Event;

public class JobConfirmEvent extends Event{
	
	 int rqid;

	  void setId(int rid) {
	    rqid = rid;
	  }

	  int getId() {
	    return rqid;
	  }

}
