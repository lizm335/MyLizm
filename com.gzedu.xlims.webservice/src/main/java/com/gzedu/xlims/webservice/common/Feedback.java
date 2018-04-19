package com.gzedu.xlims.webservice.common;

import java.io.Serializable;

/**
 * 通用反馈信息, 记录是否成功, 以及相应的信息提示.
 * 
 */
public class Feedback implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean successful;
	private String message;
	private String type;
	private Object obj;

	public Feedback() {
	}

	public Feedback(boolean successful, String message) {
		this.successful = successful;
		this.message = message;
	}

	public Feedback(boolean successful, String message, Object obj) {
		this.successful = successful;
		this.message = message;
		this.obj = obj;
	}

	public Feedback(boolean successful, String message, String type, Object obj) {
		this.successful = successful;
		this.message = message;
		this.type = type;
		this.obj = obj;
	}

	public static Feedback success(String message) {
		return new Feedback(true, message);
	}

	public static Feedback fail(String message) {
		return new Feedback(false, message);
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public Feedback setType(String type) {
		this.type = type;
		return this;
	}

	public Object getObj() {
		return obj;
	}

	public Feedback setObj(Object obj) {
		this.obj = obj;
		return this;
	}

}
