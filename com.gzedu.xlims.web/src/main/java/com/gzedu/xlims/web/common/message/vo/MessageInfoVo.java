package com.gzedu.xlims.web.common.message.vo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.pojo.message.GjtMessageClassify;
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
	private String messageUserId;
	private String isEnabled;// 是否已读
	private BigDecimal putTotal;// 发送总数
	private long readTotal;// 已读总数
	private String userRole;// 接收者角色
	private Boolean isStick;// 是否置顶
	private String stickTime;// 置顶时间
	private String degree;// 重要程度
	private String attachment;// 封面地址
	private String createRoleName;// 创建者角色
	private String typeClassify;// 类型分类
	private String pcCount;// PC查看比例
	private String appCount;// APP查看比例
	private String comCount;// 公众号查看比例
	private long commentCount;// 评论数
	private long likeCount;// 点赞总数
	private long feedbackCount;// 反馈总数
	private String noReadRatio;// 未读比例
	private String isLike;// 是否开启点赞
	private String isFeedback;// 是否开启反馈
	private String isComment;// 是否开启评论

	public MessageInfoVo() {

	}

	public MessageInfoVo(GjtMessageInfo gjtMessageInfo, Map<String, Object> putMap, Map<String, Object> readMap,
			Map<String, Object> likeMap, Map<String, Object> ticklingMap, Map<String, Object> commMap) {
		super();
		this.messageId = gjtMessageInfo.getMessageId();
		this.infoTheme = gjtMessageInfo.getInfoTheme();
		this.infoType = gjtMessageInfo.getInfoType();
		this.infoContent = gjtMessageInfo.getInfoContent();
		this.putUserName = gjtMessageInfo.getCreatedBy();
		this.createRoleName = gjtMessageInfo.getCreateRoleName();
		if (gjtMessageInfo.getCreatedDt() != null) {
			this.createdDt = DateUtils.getStringToDate(gjtMessageInfo.getCreatedDt());
		}
		this.degree = "0".equals(gjtMessageInfo.getDegree()) ? "一般" : "重要";
		this.attachment = gjtMessageInfo.getAttachment();
		this.isLike = gjtMessageInfo.getIsLike();
		this.isComment = gjtMessageInfo.getIsComment();
		this.isFeedback = gjtMessageInfo.getIsFeedback();
		GjtMessageClassify gjtMessageClassify = gjtMessageInfo.getGjtMessageClassify();
		this.typeClassify = gjtMessageClassify != null ? gjtMessageClassify.getName() : "";
		Object putObject = putMap.get(gjtMessageInfo.getMessageId());
		BigDecimal putTotal1 = new BigDecimal("1");
		if (putObject != null) {
			putTotal1 = (BigDecimal) putObject;
		}
		this.putTotal = putTotal1;

		Object readObject0 = readMap.get(gjtMessageInfo.getMessageId() + "-" + "0");
		Object readObject1 = readMap.get(gjtMessageInfo.getMessageId() + "-" + "1");
		Object readObject2 = readMap.get(gjtMessageInfo.getMessageId() + "-" + "2");

		BigDecimal pcCount1 = new BigDecimal("0");
		BigDecimal appCount1 = new BigDecimal("0");
		BigDecimal comCount1 = new BigDecimal("0");

		if (readObject0 != null) {
			pcCount1 = (BigDecimal) readObject0;
		}
		if (readObject1 != null) {
			appCount1 = (BigDecimal) readObject1;
		}
		if (readObject2 != null) {
			comCount1 = (BigDecimal) readObject2;
		}
		DecimalFormat df = new DecimalFormat("######0.00");
		Long readTotal1 = pcCount1.add(appCount1).add(comCount1).longValue();
		if (readTotal1 < 1) {
			this.pcCount = "0%";
			this.appCount = "0%";
			this.comCount = "0%";
		} else {
			String pcCount2 = df.format((pcCount1.doubleValue() / readTotal1) * 100);
			String appCount2 = df.format((appCount1.doubleValue() / readTotal1) * 100);
			String comCount2 = df.format((comCount1.doubleValue() / readTotal1) * 100);
			this.pcCount = ("0.00".equals(pcCount2) ? "0" : pcCount2) + "%";
			this.appCount = ("0.00".equals(appCount2) ? "0" : appCount2) + "%";
			this.comCount = ("0.00".equals(comCount2) ? "0" : comCount2) + "%";
		}
		this.readTotal = readTotal1;
		Double bili = (putTotal1.longValue() - readTotal1) * 1.0 / putTotal1.longValue() * 100;
		String noReadStr = df.format(bili);
		this.noReadRatio = ("100.00".equals(noReadStr) ? "100" : noReadStr) + "%";

		// String getUserRole = gjtMessageInfo.getGetUserRole();
		// if (StringUtils.isNotBlank(getUserRole)) {
		// getUserRole = getUserRole.substring(0, getUserRole.length() - 1);
		// }
		// this.userRole = getUserRole;

		this.fileUrl = gjtMessageInfo.getFileUrl();
		this.fileName = gjtMessageInfo.getFileName();
		boolean bool = false;
		if (gjtMessageInfo.getEffectiveTime() != null) {
			bool = gjtMessageInfo.getEffectiveTime().getTime() > DateUtils.getDate().getTime();
			this.stickTime = DateUtils.getTimeYMD(gjtMessageInfo.getEffectiveTime());
		}
		this.isStick = bool;
		BigDecimal commentCount1 = (BigDecimal) commMap.get(gjtMessageInfo.getMessageId());// 评论数
		BigDecimal likeCount1 = (BigDecimal) likeMap.get(gjtMessageInfo.getMessageId());// 点赞总数
		BigDecimal feedbackCount1 = (BigDecimal) ticklingMap.get(gjtMessageInfo.getMessageId());// 反馈总数;
		this.commentCount = commentCount1 == null ? 0 : commentCount1.longValue();
		this.likeCount = likeCount1 == null ? 0 : likeCount1.longValue();
		this.feedbackCount = feedbackCount1 == null ? 0 : feedbackCount1.longValue();

	}

	public MessageInfoVo(GjtMessageUser gjtMessageUser) {
		super();
		GjtMessageInfo gjtMessageInfo = gjtMessageUser.getGjtMessageInfo();
		this.messageUserId = gjtMessageUser.getId();
		this.messageId = gjtMessageUser.getMessageId();
		this.infoType = gjtMessageInfo.getInfoType();
		this.infoTheme = gjtMessageInfo.getInfoTheme();
		this.infoTheme = gjtMessageInfo.getInfoTheme();
		this.isEnabled = gjtMessageUser.getIsEnabled();
		String content = "";

		Pattern p = Pattern.compile("(<[^>]*>)", Pattern.CASE_INSENSITIVE);
		if (StringUtils.isNotBlank(gjtMessageInfo.getInfoContent())) {
			Matcher m = p.matcher(gjtMessageInfo.getInfoContent());
			String res = m.replaceAll("");
			content = res.length() > 100 ? res.substring(0, 100) + "..." : res;
		}

		this.infoContent = content;
		if (gjtMessageInfo.getGjtUserAccount() != null) {// 新的统一查询user表
			this.putUserName = gjtMessageInfo.getGjtUserAccount().getRealName();
		} else {
			this.putUserName = "系统";
		}
		if (gjtMessageUser.getCreatedDt() != null) {
			this.createdDt = DateUtils.getStringToDate(gjtMessageUser.getCreatedDt());
		}

		boolean bool = false;
		if (gjtMessageInfo.getEffectiveTime() != null) {
			bool = gjtMessageInfo.getEffectiveTime().getTime() > DateUtils.getDate().getTime();
		}
		this.isStick = bool;
	}

	public MessageInfoVo(GjtMessageInfo gjtMessageInfo, Map<String, Object> putMap, Map<String, Object> readMap) {
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

		Object putObject = putMap.get(gjtMessageInfo.getMessageId());
		if (putObject != null) {
			this.putTotal = (BigDecimal) putObject;
		} else {
			this.putTotal = new BigDecimal("0");
		}

		Object readObject = readMap.get(gjtMessageInfo.getMessageId());
		if (readObject != null) {
			this.readTotal = ((BigDecimal) readObject).longValue();
		} else {
			this.readTotal = 0L;
		}
		String getUserRole = gjtMessageInfo.getGetUserRole();
		if (StringUtils.isNotBlank(getUserRole)) {
			getUserRole = getUserRole.substring(0, getUserRole.length() - 1);
		}
		this.userRole = getUserRole;
		this.fileUrl = gjtMessageInfo.getFileUrl();
		this.fileName = gjtMessageInfo.getFileName();
		boolean bool = false;
		if (gjtMessageInfo.getEffectiveTime() != null) {
			bool = gjtMessageInfo.getEffectiveTime().getTime() > DateUtils.getDate().getTime();
			this.stickTime = DateUtils.getTimeYMD(gjtMessageInfo.getEffectiveTime());
		}
		this.isStick = bool;
	}

	public String getStickTime() {
		return stickTime;
	}

	public void setStickTime(String stickTime) {
		this.stickTime = stickTime;
	}

	public Boolean getIsStick() {
		return isStick;
	}

	public void setIsStick(Boolean isStick) {
		this.isStick = isStick;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public BigDecimal getPutTotal() {
		return putTotal;
	}

	public void setPutTotal(BigDecimal putTotal) {
		this.putTotal = putTotal;
	}

	public long getReadTotal() {
		return readTotal;
	}

	public void setReadTotal(long readTotal) {
		this.readTotal = readTotal;
	}

	public String getIsEnabled() {
		return isEnabled;
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

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getCreateRoleName() {
		return createRoleName;
	}

	public void setCreateRoleName(String createRoleName) {
		this.createRoleName = createRoleName;
	}

	public String getTypeClassify() {
		return typeClassify;
	}

	public void setTypeClassify(String typeClassify) {
		this.typeClassify = typeClassify;
	}

	public String getPcCount() {
		return pcCount;
	}

	public void setPcCount(String pcCount) {
		this.pcCount = pcCount;
	}

	public String getAppCount() {
		return appCount;
	}

	public void setAppCount(String appCount) {
		this.appCount = appCount;
	}

	public String getComCount() {
		return comCount;
	}

	public void setComCount(String comCount) {
		this.comCount = comCount;
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

	public long getFeedbackCount() {
		return feedbackCount;
	}

	public void setFeedbackCount(long feedbackCount) {
		this.feedbackCount = feedbackCount;
	}

	public String getNoReadRatio() {
		return noReadRatio;
	}

	public void setNoReadRatio(String noReadRatio) {
		this.noReadRatio = noReadRatio;
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

}
