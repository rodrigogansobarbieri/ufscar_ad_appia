package irdp.protocols.lista1.ex3;

import net.sf.appia.core.Event;

public class JobSubmitEvent extends Event {

	 int rqid;
	  String str;

	  void setId(int rid) {
	    rqid = rid;
	  }

	  void setString(String s) {
	    str = s;
	  }

	  int getId() {
	    return rqid;
	  }

	  String getString() {
	    return str;
	  }
}
