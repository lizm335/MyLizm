package com.gzedu.xlims.webservice.controller.gjt.message;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.pojo.message.GjtMessageInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageInfoVo {
	// 通知ID
	private String messageInfoId;
	// 通知标题
	private String infoTheme;
	// 通知类型
	private String infoType;
	// 通知发布时间
	private String createdDt;
	// 通知发布者
	private String putUser;
	private String infoContent = "";// 通知内容
	
	public MessageInfoVo() {
		super();
	}
	public MessageInfoVo(GjtMessageInfo message ) {
		super();
		this.messageInfoId = message.getMessageId();;
		this.infoTheme = message.getInfoTheme();
		this.infoType = message.getInfoType();
		this.createdDt = DateUtils.getStringToDate(message.getCreatedDt());
		if (message.getGjtUserAccount() != null) {// 新的统一查询user表
			this.putUser = message.getGjtUserAccount().getRealName();
		} else {
			this.putUser = "--";
		}
		String content = "";

		Pattern p = Pattern.compile("(<[^>]*>)", Pattern.CASE_INSENSITIVE);
		if (StringUtils.isNotBlank(message.getInfoContent())) {
			Matcher m = p.matcher(message.getInfoContent());
			String res = m.replaceAll("");
			content = res.length() > 100 ? res.substring(0, 100) + "..." : res;
		}

		this.infoContent = content;
	}
	public String getMessageInfoId() {
		return messageInfoId;
	}
	public void setMessageInfoId(String messageInfoId) {
		this.messageInfoId = messageInfoId;
	}
	public String getInfoTheme() {
		return infoTheme;
	}
	public void setInfoTheme(String infoTheme) {
		this.infoTheme = infoTheme;
	}
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	public String getCreatedDt() {
		return createdDt;
	}
	public void setCreatedDt(String createdDt) {
		this.createdDt = createdDt;
	}
	public String getPutUser() {
		return putUser;
	}
	public void setPutUser(String putUser) {
		this.putUser = putUser;
	}

	public String getInfoContent() {
		return infoContent;
	}

	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}
}
