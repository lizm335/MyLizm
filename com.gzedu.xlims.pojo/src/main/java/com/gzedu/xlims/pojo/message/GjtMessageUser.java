package com.gzedu.xlims.pojo.message;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gzedu.xlims.pojo.GjtUserAccount;

/**
 * The persistent class for the GJT_MESSAGE_USER database table.通知信息对应用户
 * 
 */
@Entity
@Table(name = "GJT_MESSAGE_USER")
@NamedQuery(name = "GjtMessageUser.findAll", query = "SELECT g FROM GjtMessageUser g")
public class GjtMessageUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "IS_ENABLED") // 是否阅读0否1已阅
	private String isEnabled;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MESSAGE_ID", insertable = false, updatable = false)
	private GjtMessageInfo gjtMessageInfo;

	@Column(name = "MESSAGE_ID")
	private String messageId;// 插入的时候，不关联查询

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FEEDBACK_DT")
	private Date feedbackDt;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", insertable = false, updatable = false)
	private GjtUserAccount gjtUserAccount;

	@Column(name = "USER_ID")
	private String userId;// 插入的时候，不关联查询

	@Column(name = "IS_LIKE") // 是否点赞：0-否,1-是
	private String isLike;

	@Column(name = "IS_CONSTRAINT") // 是否强制阅读：0-否,1-是
	private String isConstraint;

	@Column(name = "IS_TICKLING") // 是否反馈：0-否,1-是
	private String isTickling;

	@Column(name = "FEEDBACK_TYPE") // 反馈类型 GjtMessageFeedback
	private String feedbackType;

	private String platform;// 阅读平台

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	public GjtMessageUser() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public GjtMessageInfo getGjtMessageInfo() {
		return gjtMessageInfo;
	}

	public void setGjtMessageInfo(GjtMessageInfo gjtMessageInfo) {
		this.gjtMessageInfo = gjtMessageInfo;
	}

	public GjtUserAccount getGjtUserAccount() {
		return gjtUserAccount;
	}

	public void setGjtUserAccount(GjtUserAccount gjtUserAccount) {
		this.gjtUserAccount = gjtUserAccount;
	}

	public String getIsLike() {
		return isLike;
	}

	public void setIsLike(String isLike) {
		this.isLike = isLike;
	}

	public String getIsConstraint() {
		return isConstraint;
	}

	public void setIsConstraint(String isConstraint) {
		this.isConstraint = isConstraint;
	}

	public String getIsTickling() {
		return isTickling;
	}

	public void setIsTickling(String isTickling) {
		this.isTickling = isTickling;
	}

	public String getFeedbackType() {
		return feedbackType;
	}

	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public Date getFeedbackDt() {
		return feedbackDt;
	}

	public void setFeedbackDt(Date feedbackDt) {
		this.feedbackDt = feedbackDt;
	}

}