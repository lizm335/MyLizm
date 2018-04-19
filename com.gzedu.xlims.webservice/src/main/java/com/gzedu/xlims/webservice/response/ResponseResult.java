package com.gzedu.xlims.webservice.response;

import com.gzedu.xlims.common.GsonUtils;

/**
 * 响应对象model
 */
public class ResponseResult {

	/**
	 * 响应状态码
	 */
	private Integer code;
	/**
	 * 响应的描述
	 */
	private String message;
	/**
	 * 响应的正文
	 */
	private Object content;

	public ResponseResult(Integer code,String message){
		this.code = code;
		this.message = message;
		this.content = content;
	}

	public ResponseResult(Integer code,String message,Object content){
		this.code = code;
		this.message = message;
		this.content = content;
	}

	public ResponseResult(ResponseStatus status,Object content){
		this.code = status.getCode();
		this.message = status.getMessage();
		this.content = content;
	}
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ResponseResult" + GsonUtils.toJson(this);
	}
	
}
