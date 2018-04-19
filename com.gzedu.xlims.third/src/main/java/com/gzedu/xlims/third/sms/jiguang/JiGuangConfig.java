package com.gzedu.xlims.third.sms.jiguang;

import java.util.HashMap;
import java.util.Map;

/**
 * 极光推送配置
 * 
 * @author lyj
 * 
 */
public class JiGuangConfig {
	
	private String appKey;
	
	private String masterSecret;

	private String contentType;
	
	// 调用地址
	private String url;
	// 必填 推送平台设置:"android"、"ios"、"winphone"、"all"
	private String platform;
	// 推送设备指定
	private String audience;
	// 可选 通知内容体。是被推送到客户端的内容。与 message 一起二者必须有其一，可以二者并存
	// private String notification;
	// 可选 消息内容体。是被推送到客户端的内容。与 notification 一起二者必须有其一，可以二者并存
	// private String message;
	// 可选 短信渠道补充送达内容体
	// private String sms_message;
	// 可选 推送参数
	// private String options;
	/*
	 * True 表示推送生产环境，False 表示要推送开发环境；如果不指定则为推送生产环境。JPush 官方 API LIbrary (SDK) 默认设置为推送 “开发环境”。
	 */
	private boolean apnsProduction = false;
	
	
	private static Map<String ,String> codeMap = new HashMap<String,String>();
	
	static {
		codeMap.put("1000","系统内部错误	服务器端内部逻辑错误，请稍后重试。	500");
		codeMap.put("1001", "只支持 HTTP Post 方法	不支持 Get 方法。	405");
		codeMap.put("1002", "缺少了必须的参数	必须改正	400");
		codeMap.put(
				"1003",
				"参数值不合法	必须改正，如Audience参数中tag，alias，registration_id有空值，错误提示Empty tag/alias/registration_id is not allowed!	400");
		codeMap.put("1004", "验证失败	必须改正。详情请看：调用验证	401");
		codeMap.put(
				"1005",
				"消息体太大	必须改正。 Android平台Notification+Message长度限制为4000字节； iOS Notification 中 “iOS”:{ } 及大括号内的总体长度不超过：2000个字节（包括自定义参数和符号），iOS 的 Message部分长度不超过 4000 字节； WinPhone平台Notification长度限制为4000字节	400");
		codeMap.put("1008", "app_key参数非法	必须改正	400");
		codeMap.put("1011", "没有满足条件的推送目标	请检查audience	400");
		codeMap.put("1020", "只支持 HTTPS 请求	必须改正	404");
		codeMap.put("1030", "内部服务超时	稍后重试");
		
		
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getAudience() {
		return audience;
	}
	public void setAudience(String audience) {
		this.audience = audience;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getMasterSecret() {
		return masterSecret;
	}
	public void setMasterSecret(String masterSecret) {
		this.masterSecret = masterSecret;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public boolean isApnsProduction() {
		return apnsProduction;
	}
	public void setApnsProduction(boolean apnsProduction) {
		this.apnsProduction = apnsProduction;
	}

}
