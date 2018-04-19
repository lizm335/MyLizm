package com.ouchgzee.study.web.common;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 通用反馈信息, 记录是否成功, 以及相应的信息提示.
 */
public class Feedback implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("RESULT")
	private String result;

	@JsonProperty("MSG")
	private String msg;

	@JsonProperty("DATA")
	private Object data;

	public Feedback() {
	}

	public Feedback(String result, String msg, Object data) {
		this.result = result;
		this.msg = msg;
		this.data = data;
	}

	public Feedback(String result, String msg) {
		this.result = result;
		this.msg = msg;
	}

	@JsonIgnore
	public String getResult() {
		return result;
	}

	@JsonIgnore
	public void setResult(String result) {
		this.result = result;
	}

	@JsonIgnore
	public String getMsg() {
		return msg;
	}

	@JsonIgnore
	public void setMsg(String msg) {
		this.msg = msg;
	}

	@JsonIgnore
	public Object getData() {
		return data;
	}

	@JsonIgnore
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * 枚举结果类型
	 */
	public enum Type {
		成功("0"),

		失败("1"),

		异常("2"),

		登录超时("3");

		String code;

		private Type(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
	}
}
