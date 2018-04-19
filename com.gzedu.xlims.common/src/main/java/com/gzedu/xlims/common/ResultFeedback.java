package com.gzedu.xlims.common;

import java.io.Serializable;

/**
 * 通用反馈信息, 记录是否成功, 以及相应的信息提示.
 * 
 */
public class ResultFeedback implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean successful;
	private String message;
	private String type;
	private Object obj;

	public ResultFeedback(boolean successful, String message) {
		this.successful = successful;
		this.message = message;
	}

	public ResultFeedback(boolean successful, String message, String type) {
		this.successful = successful;
		this.message = message;
		this.type = type;
	}

	public ResultFeedback(boolean successful, String message, String type, Object obj) {
		this.successful = successful;
		this.message = message;
		this.type = type;
		this.obj = obj;
	}

	public static ResultFeedback success(String message) {
		return new ResultFeedback(true, message);
	}

	public static ResultFeedback fail(String message) {
		return new ResultFeedback(false, message);
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

	public ResultFeedback setType(String type) {
		this.type = type;
		return this;
	}

	public Object getObj() {
		return obj;
	}

	public ResultFeedback setObj(Object obj) {
		this.obj = obj;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("{\"successful\":%1$s,\"message\":\"%2$s\",\"type\":%3$s,\"obj\":%4$s}", successful, message, type, obj);
	}

}
