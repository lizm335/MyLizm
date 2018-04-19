package com.ouchgzee.study.web.filter;

import org.apache.catalina.connector.CoyoteOutputStream;
import org.apache.catalina.connector.OutputBuffer;

public class FilterOutputStream extends CoyoteOutputStream {
	
	public FilterOutputStream() {
		super(null);
	}

	protected FilterOutputStream(OutputBuffer ob) {
		super(ob);
	}
	
	public void setOb(OutputBuffer ob) {
		this.ob = ob;
	}

}
