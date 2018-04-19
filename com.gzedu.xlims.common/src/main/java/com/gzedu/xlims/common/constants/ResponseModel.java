package com.gzedu.xlims.common.constants;

/**
 * 
 * @author lzj
 *
 */
public class ResponseModel{
    private int msgCode;
    private int busCode;
    private String message;
    private String data;

    public ResponseModel(int msgCode, int busCode, String message, String data) {
        this.msgCode = msgCode;
        this.busCode = busCode;
        this.message = message;
        this.data = data;
    }

    public ResponseModel(int msgCode, int busCode, String message) {
        this.msgCode = msgCode;
        this.busCode = busCode;
        this.message = message;
    }

    public ResponseModel(int busCode, String message) {
        this(MessageCode.BIZ_ERROR.getMsgCode(), busCode, message);
    }

    public ResponseModel(MessageCode messageCode) {
        this(messageCode.getMsgCode(), 0, messageCode.getMessage());
        this.message = messageCode.getMessage();
    }

    public ResponseModel(MessageCode messageCode, String errorMessage) {
        this(messageCode.getMsgCode(), 0, errorMessage);
        if (errorMessage == null){
            this.message = messageCode.getMessage();
        }
    }

    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public int getBusCode() {
        return busCode;
    }

    public void setBusCode(int busCode) {
        this.busCode = busCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}

