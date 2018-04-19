package com.gzedu.xlims.pojo.message;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;

/**
 * The persistent class for the GJT_MESSAGE_INFO database table.
 * 
 */
@Entity
@Table(name = "GJT_MESSAGE_INFO")
@NamedQuery(name = "GjtMessageInfo.findAll", query = "SELECT g FROM GjtMessageInfo g")
public class GjtMessageInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MESSAGE_ID")
	private String messageId;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "MESSAGE_ID")
	@OrderBy("orderSort asc")
	private List<GjtMessagePutObject> gjtMessagePutObjects;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "MESSAGE_ID")
	@OrderBy("createdDt desc")
	private List<GjtMessageComment> GjtMessageComments;

	private String attachment;// 封面地址

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	private String degree;// 重要程度：0-一般，1-重要

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "FILE_URL")
	private String fileUrl;

	@Column(name = "GET_USER")
	private String getUser;// 接收者

	@Column(name = "GET_USER_METHOD")
	private String getUserMethod;// 接收者类型

	@Column(name = "GET_USER_ROLE")
	private String getUserRole;// 接收者角色

	@Lob
	@Column(name = "INFO_CONTENT")
	private String infoContent;// 内容

	@Column(name = "INFO_THEME")
	private String infoTheme;// 标题

	@Column(name = "INFO_TOOL")
	private String infoTool;// 1 站内 2 邮件 3 短信

	@Column(name = "INFO_TYPE")
	private String infoType;// 1-系统消息 2-教务通知 11-班级公告 12-考试通知 13-学习提醒 具体查看数据字典

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "IS_ENABLED") // 是否已读
	private String isEnabled;

	private String memo;

	@Column(name = "PUT_USER")
	private String putUser;

	@OneToOne
	@JoinColumn(name = "PUT_USER", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtUserAccount gjtUserAccount;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION")
	private BigDecimal version;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "XX_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtOrg gjtOrg;

	@Column(name = "CLASS_ID") // 班级 ID
	private String classId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_TIME") // 置顶有效时间
	private Date effectiveTime;

	@Column(name = "CREATE_ROLE_NAME") // 冗余角色名
	private String createRoleName;

	@Column(name = "IS_APPOINT") // 0-按条件查询，1-指定收件人
	private String isAppoint;

	@Column(name = "APP_SEND") // 是否APP推送：0-否，1-是
	private String appSend;

	@Column(name = "PUBLIC_SEND") // 是否公众号推送：0-否，1-是
	private String publicSend;

	@Column(name = "IS_COMMENT") // 是否开启评论功能：0-否，1-是
	private String isComment;

	@Column(name = "IS_LIKE") // 是否开启点赞功能：0-否，1-是
	private String isLike;

	@Column(name = "IS_FEEDBACK") // 是否开启反馈功能：0-否，1-是
	private String isFeedback;

	@Column(name = "TYPE_CLASSIFY") // 类型分类
	private String typeClassify;

	@OneToOne
	@JoinColumn(name = "TYPE_CLASSIFY", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtMessageClassify gjtMessageClassify;

	@Column(name = "FEEDBACK_CONTENT") // 反馈内容
	private String feedbackContent;

	public GjtUserAccount getGjtUserAccount() {
		return gjtUserAccount;
	}

	public void setGjtUserAccount(GjtUserAccount gjtUserAccount) {
		this.gjtUserAccount = gjtUserAccount;
	}

	public GjtMessageInfo() {
	}

	public GjtOrg getGjtOrg() {
		return gjtOrg;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public Date getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public void setGjtOrg(GjtOrg gjtOrg) {
		this.gjtOrg = gjtOrg;
	}

	public GjtMessageInfo(String id) {
		this.messageId = id;
	}

	public String getMessageId() {
		return this.messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getAttachment() {
		return this.attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getDegree() {
		return this.degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return this.fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getGetUser() {
		return this.getUser;
	}

	public void setGetUser(String getUser) {
		this.getUser = getUser;
	}

	public String getGetUserMethod() {
		return this.getUserMethod;
	}

	public void setGetUserMethod(String getUserMethod) {
		this.getUserMethod = getUserMethod;
	}

	public String getGetUserRole() {
		return this.getUserRole;
	}

	public void setGetUserRole(String getUserRole) {
		this.getUserRole = getUserRole;
	}

	public String getInfoContent() {
		return this.infoContent;
	}

	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}

	public String getInfoTheme() {
		return this.infoTheme;
	}

	public void setInfoTheme(String infoTheme) {
		this.infoTheme = infoTheme;
	}

	public String getInfoTool() {
		return this.infoTool;
	}

	public void setInfoTool(String infoTool) {
		this.infoTool = infoTool;
	}

	public String getInfoType() {
		return this.infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPutUser() {
		return this.putUser;
	}

	public void setPutUser(String putUser) {
		this.putUser = putUser;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public String getCreateRoleName() {
		return createRoleName;
	}

	public void setCreateRoleName(String createRoleName) {
		this.createRoleName = createRoleName;
	}

	public String getIsAppoint() {
		return isAppoint;
	}

	public void setIsAppoint(String isAppoint) {
		this.isAppoint = isAppoint;
	}

	public String getAppSend() {
		return appSend;
	}

	public void setAppSend(String appSend) {
		this.appSend = appSend;
	}

	public String getPublicSend() {
		return publicSend;
	}

	public void setPublicSend(String publicSend) {
		this.publicSend = publicSend;
	}

	public String getIsComment() {
		return isComment;
	}

	public void setIsComment(String isComment) {
		this.isComment = isComment;
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

	public String getTypeClassify() {
		return typeClassify;
	}

	public void setTypeClassify(String typeClassify) {
		this.typeClassify = typeClassify;
	}

	public String getFeedbackContent() {
		return feedbackContent;
	}

	public void setFeedbackContent(String feedbackContent) {
		this.feedbackContent = feedbackContent;
	}

	public GjtMessageClassify getGjtMessageClassify() {
		return gjtMessageClassify;
	}

	public void setGjtMessageClassify(GjtMessageClassify gjtMessageClassify) {
		this.gjtMessageClassify = gjtMessageClassify;
	}

	public List<GjtMessagePutObject> getGjtMessagePutObjects() {
		return gjtMessagePutObjects;
	}

	public void setGjtMessagePutObjects(List<GjtMessagePutObject> gjtMessagePutObjects) {
		this.gjtMessagePutObjects = gjtMessagePutObjects;
	}

	public List<GjtMessageComment> getGjtMessageComments() {
		return GjtMessageComments;
	}

	public void setGjtMessageComments(List<GjtMessageComment> gjtMessageComments) {
		GjtMessageComments = gjtMessageComments;
	}

}