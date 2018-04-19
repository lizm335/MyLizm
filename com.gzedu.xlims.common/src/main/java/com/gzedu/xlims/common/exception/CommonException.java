package com.gzedu.xlims.common.exception;


import com.gzedu.xlims.common.constants.BusinessCode;
import com.gzedu.xlims.common.constants.MessageCode;


/**
 * 通用异常类
 *
 * @author lzj
 * 
 */
public class CommonException extends Exception
{

    private static final long serialVersionUID = -9168410338735887786L;
    
    /**
     * 消息状态码枚举变量
     */
    private MessageCode messageCode;
    
    /**
     * 错误消息
     */
    private String errorMessage;

    /**
     * 业务码
     */
    private int busCode;


    public CommonException(MessageCode messageCode) {
        this.messageCode = messageCode;
        this.errorMessage = messageCode.getMessage();
    }


    public CommonException(BusinessCode businessCode) {
        this.busCode = businessCode.getBusCode();
        this.errorMessage = businessCode.getMessage();
    }


    public CommonException(int busCode, String errorMessage) {
        this.busCode = busCode;
        this.errorMessage = errorMessage;
    }


    public CommonException(MessageCode messageCode, String errorMessage) {
        this.messageCode = messageCode;
        this.errorMessage = errorMessage;
    }


    public MessageCode getMessageCode() {
        return messageCode;
    }


    public void setMessageCode(MessageCode messageCode) {
        this.messageCode = messageCode;
    }


    public String getErrorMessage() {
        return errorMessage;
    }


    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public int getBusCode() {
        return busCode;
    }


    public void setBusCode(int busCode) {
        this.busCode = busCode;
    }


}
