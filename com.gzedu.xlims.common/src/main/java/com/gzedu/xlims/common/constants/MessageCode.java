package com.gzedu.xlims.common.constants;

/**
 * 消息码，定义处理请求的结果，但不包含业务结果
 * 
 * @author lzj
 *
 */
public enum MessageCode {
    
	RESP_OK(200,"请求成功"),
	BAD_REQUEST(400,"请求参数有误"),
	TOKEN_INVALID(401,"登录超时"),
	NOT_LOGGED_IN(402,"未登录"), 
    PERMISSION_DENIED(403,"权限不足"),
    NOT_FOUND(404,"请求的地址无效"),
    SIGN_ERROR(405,"签名无效"),
    SECRET_ERROR(406,"密钥无效"),
    CRYPTO_ERROR(407,"请求参数无法解析"),
    SYSTEM_ERROR(500,"系统繁忙，请稍后重试"),
	BIZ_ERROR(501, "业务异常"),
	OFFLINE(502,"您的网络不给力，请稍后重试");
    
	private int msgCode;
	
	private String message;
	
	MessageCode(int msgCode, String message) {
		this.msgCode=msgCode;
		this.message=message;
	}
	
	public int getMsgCode() {
		return msgCode;
	}

    public String getMessage() {
        return message;
    }
}
