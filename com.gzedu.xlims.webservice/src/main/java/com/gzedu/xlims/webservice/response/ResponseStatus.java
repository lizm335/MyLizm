package com.gzedu.xlims.webservice.response;

/**
 * 响应状态枚举
 */
public enum ResponseStatus {
	
	SUCCESS(200, "请求成功"),
	FAIL(501, "处理失败"),
	UNKNOW_ERROR(500, "未知异常"),
	PARAM_ERROR(400, "参数错误"),
	ILLEGAL_ERROR(10004, "非法请求"),
	NUKNOW_ACCOUNT(1005,"帐号不存在");

	/**
	 * 返回码
	 */
	private Integer code;
	/**
	 * 返回状态描述
	 */
	private String message;
	
	private ResponseStatus(Integer code,String message) {
		this.code = code;
		this.message = message;
	}
	
	public static ResponseStatus valueOf(Integer code){
		if(null==code){
			return UNKNOW_ERROR;
		}else{
			for(ResponseStatus status : ResponseStatus.values()){
				if(status.getCode().intValue()==code.intValue()){
					return status;
				}
			}
		}
		return UNKNOW_ERROR;
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
	
}
