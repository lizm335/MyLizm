package com.ouchgzee.study.web.vo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.pojo.message.GjtMessageClassify;
import com.gzedu.xlims.pojo.message.GjtMessageFeedback;
import com.gzedu.xlims.pojo.message.GjtMessageInfo;
import com.gzedu.xlims.pojo.message.GjtMessageUser;

public class MessageInfoVo {
	private String messageId = "";
	private String infoType = "";// 通知类型
	private String infoTheme = "";// 通知标题
	private String infoContent = "";// 通知内容
	private String putUserName = "";// 发布者
	private String createdDt = "";// 发布时间
	private String fileUrl = "";// 附件地址
	private String fileName = "";// 附件名称
	private String messageUserId;// 中间表主键ID
	private String isEnabled;// 是否已读
	private String isStick;// 是否置顶
	private String typeClassify;// 类型分类
	private long commentCount;// 评论数
	private long likeCount;// 点赞数
	private long readTotal;// 阅读人数
	private String isLike;// 是否开启点赞
	private String isFeedback;// 是否开启反馈
	private String isComment;// 是否开启评论
	private List<GjtMessageFeedback> feedbackList;
	private List<Map<String, Object>> commentList;
	private boolean ifFeedbackCheck;
	private String coverUrl;// 封面地址
	private boolean ifLikeCheck;
	private long feebackTotal;

	public MessageInfoVo() {

	}

	public MessageInfoVo(GjtMessageInfo gjtMessageInfo) {
		super();
		this.messageId = gjtMessageInfo.getMessageId();
		this.infoTheme = gjtMessageInfo.getInfoTheme();
		this.infoType = gjtMessageInfo.getInfoType();
		this.infoContent = gjtMessageInfo.getInfoContent();
		if (gjtMessageInfo.getGjtUserAccount() != null) {// 新的统一查询user表
			this.putUserName = gjtMessageInfo.getGjtUserAccount().getRealName();
		} else {
			this.putUserName = "系统";
		}
		if (gjtMessageInfo.getCreatedDt() != null) {
			this.createdDt = DateUtils.getStringToDate(gjtMessageInfo.getCreatedDt());
		}
		this.fileUrl = gjtMessageInfo.getFileUrl();
		this.fileName = gjtMessageInfo.getFileName();
		boolean bool = false;
		if (gjtMessageInfo.getEffectiveTime() != null) {
			bool = gjtMessageInfo.getEffectiveTime().getTime() > DateUtils.getDate().getTime();
		}
		this.isStick = bool == true ? "1" : "0";
		GjtMessageClassify gjtMessageClassify = gjtMessageInfo.getGjtMessageClassify();
		this.typeClassify = gjtMessageClassify == null ? "" : gjtMessageClassify.getName();
		this.isLike = gjtMessageInfo.getIsLike();
		this.isComment = gjtMessageInfo.getIsComment();
		this.isFeedback = gjtMessageInfo.getIsFeedback();
	}

	public MessageInfoVo(String messageId, String infoType, String infoTheme, String infoContent, String putUserName,
			String createdDt) {
		super();
		this.messageId = messageId;
		this.infoType = infoType;
		this.infoTheme = infoTheme;
		this.infoContent = infoContent;
		this.putUserName = putUserName;
		this.createdDt = createdDt;
	}

	public MessageInfoVo(GjtMessageUser gjtMessageUser, Map<String, Object> readMap, Map<String, Object> likeMap,
			Map<String, Object> commMap) {
		super();
		GjtMessageInfo info = gjtMessageUser.getGjtMessageInfo();
		this.messageUserId = gjtMessageUser.getId();
		this.messageId = gjtMessageUser.getMessageId();
		this.infoType = info.getInfoType();
		this.infoTheme = info.getInfoTheme();
		this.infoTheme = info.getInfoTheme();
		this.isEnabled = gjtMessageUser.getIsEnabled();
		this.coverUrl = info.getAttachment();
		String content = "";

		Pattern p = Pattern.compile("(<[^>]*>)", Pattern.CASE_INSENSITIVE);
		if (StringUtils.isNotBlank(info.getInfoContent())) {
			Matcher m = p.matcher(info.getInfoContent());
			String res = m.replaceAll("");
			content = res.length() > 100 ? res.substring(0, 100) + "..." : res;
		}

		this.infoContent = content;
		if (info.getGjtUserAccount() != null) {
			this.putUserName = info.getGjtUserAccount().getRealName();// 新的统一查询user表
		} else {
			this.putUserName = "系统";
		}
		if (gjtMessageUser.getCreatedDt() != null) {
			this.createdDt = DateUtils.getStringToDate(gjtMessageUser.getCreatedDt());
		}
		boolean bool = false;
		if (info.getEffectiveTime() != null) {
			bool = info.getEffectiveTime().getTime() > DateUtils.getDate().getTime();
		}
		this.isStick = bool == true ? "1" : "0";
		GjtMessageClassify gjtMessageClassify = info.getGjtMessageClassify();
		this.typeClassify = gjtMessageClassify != null ? gjtMessageClassify.getName() : "";

		BigDecimal readTotal1 = (BigDecimal) readMap.get(info.getMessageId());
		this.readTotal = readTotal1 == null ? 0 : readTotal1.longValue();

		BigDecimal commentCount1 = (BigDecimal) commMap.get(info.getMessageId());// 评论数
		BigDecimal likeCount1 = (BigDecimal) likeMap.get(info.getMessageId());// 点赞总数
		this.commentCount = commentCount1 == null ? 0 : commentCount1.longValue();
		this.likeCount = likeCount1 == null ? 0 : likeCount1.longValue();

		this.isComment = info.getIsComment();
		this.isFeedback = info.getIsFeedback();
		this.isLike = info.getIsLike();

	}

	public MessageInfoVo(GjtMessageUser gjtMessageUser) {
		super();
		GjtMessageInfo info = gjtMessageUser.getGjtMessageInfo();
		this.messageUserId = gjtMessageUser.getId();
		this.messageId = gjtMessageUser.getMessageId();
		this.infoType = info.getInfoType();
		this.infoTheme = info.getInfoTheme();
		this.infoTheme = info.getInfoTheme();
		this.isEnabled = gjtMessageUser.getIsEnabled();
		String content = "";

		Pattern p = Pattern.compile("(<[^>]*>)", Pattern.CASE_INSENSITIVE);
		if (StringUtils.isNotBlank(info.getInfoContent())) {
			Matcher m = p.matcher(info.getInfoContent());
			String res = m.replaceAll("");
			content = res.length() > 100 ? res.substring(0, 100) + "..." : res;
		}

		this.infoContent = content;
		if (info.getGjtUserAccount() != null) {
			this.putUserName = info.getGjtUserAccount().getRealName();// 新的统一查询user表
		} else {
			this.putUserName = "系统";
		}
		if (gjtMessageUser.getCreatedDt() != null) {
			this.createdDt = DateUtils.getStringToDate(gjtMessageUser.getCreatedDt());
		}
		boolean bool = false;
		if (info.getEffectiveTime() != null) {
			bool = info.getEffectiveTime().getTime() > DateUtils.getDate().getTime();
		}
		this.isStick = bool == true ? "1" : "0";
		GjtMessageClassify gjtMessageClassify = info.getGjtMessageClassify();
		this.typeClassify = gjtMessageClassify != null ? gjtMessageClassify.getName() : "";

	}

	public String getIsEnabled() {
		return isEnabled;
	}

	public String getIsStick() {
		return isStick;
	}

	public void setIsStick(String isStick) {
		this.isStick = isStick;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getMessageUserId() {
		return messageUserId;
	}

	public void setMessageUserId(String messageUserId) {
		this.messageUserId = messageUserId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getInfoTheme() {
		return infoTheme;
	}

	public void setInfoTheme(String infoTheme) {
		this.infoTheme = infoTheme;
	}

	public String getInfoContent() {
		return infoContent;
	}

	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}

	public String getPutUserName() {
		return putUserName;
	}

	public void setPutUserName(String putUserName) {
		this.putUserName = putUserName;
	}

	public String getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(String createdDt) {
		this.createdDt = createdDt;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTypeClassify() {
		return typeClassify;
	}

	public void setTypeClassify(String typeClassify) {
		this.typeClassify = typeClassify;
	}

	public long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(long commentCount) {
		this.commentCount = commentCount;
	}

	public long getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(long likeCount) {
		this.likeCount = likeCount;
	}

	public long getReadTotal() {
		return readTotal;
	}

	public void setReadTotal(long readTotal) {
		this.readTotal = readTotal;
	}

	public String getIsLike() {
		return isLike;
	}

	public void setIsLike(String isLike) {
		this.isLike = isLike;
	}

	public String getIsFeedback() {
		return isFeedback;
	}

	public void setIsFeedback(String isFeedback) {
		this.isFeedback = isFeedback;
	}

	public String getIsComment() {
		return isComment;
	}

	public void setIsComment(String isComment) {
		this.isComment = isComment;
	}

	public List<GjtMessageFeedback> getFeedbackList() {
		return feedbackList;
	}

	public void setFeedbackList(List<GjtMessageFeedback> feedbackList) {
		this.feedbackList = feedbackList;
	}

	public List<Map<String, Object>> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Map<String, Object>> commentList) {
		this.commentList = commentList;
	}

	public boolean isIfFeedbackCheck() {
		return ifFeedbackCheck;
	}

	public void setIfFeedbackCheck(boolean ifFeedbackCheck) {
		this.ifFeedbackCheck = ifFeedbackCheck;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public boolean isIfLikeCheck() {
		return ifLikeCheck;
	}

	public void setIfLikeCheck(boolean ifLikeCheck) {
		this.ifLikeCheck = ifLikeCheck;
	}

	public long getFeebackTotal() {
		return feebackTotal;
	}

	public void setFeebackTotal(long feebackTotal) {
		this.feebackTotal = feebackTotal;
	}

}
