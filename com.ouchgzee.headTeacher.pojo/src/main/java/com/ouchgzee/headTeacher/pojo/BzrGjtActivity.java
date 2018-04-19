package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * 班级活动实体类 <br>
 * The persistent class for the GJT_ACTIVITY database table. 活动表
 * 
 */
@Entity
@Table(name = "GJT_ACTIVITY")
// @NamedQuery(name = "GjtActivity.findAll", query = "SELECT g FROM GjtActivity g")
@Deprecated public class BzrGjtActivity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "ACTIVITY_ADDRESS")
	private String activityAddress;

	@Lob
	@Column(name = "ACTIVITY_INTRODUCE")
	private String activityIntroduce;

	@Lob
	@Column(name = "ACTIVITY_PICTURE")
	private String activityPicture;

	@Column(name = "ACTIVITY_TITLE")
	private String activityTitle;

	@Column(name = "AUDIT_STATUS")
	private BigDecimal auditStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BEGIN_TIME")
	private Date beginTime;

	@Column(name = "CEILING_NUM")
	private BigDecimal ceilingNum;

	@Column(name = "CHARGE_MONEY")
	private BigDecimal chargeMoney;

	@Column(name = "COMMENT_NUM", insertable = false)
	private BigDecimal commentNum;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME")
	private Date endTime;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	@Column(name = "IS_FREE")
	private BigDecimal isFree;

	@Column(name = "JOIN_NUM", insertable = false)
	private BigDecimal joinNum;

	@Column(name = "PUBLICITY_PICTURE")
	private String publicityPicture;

	@Column(name = "RECEIVE_ID")
	private String receiveId;

	@Column(name = "RECEIVE_TYPE")
	private BigDecimal receiveType;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;
	@Transient
	private long colWaitActivityNum;

	/**
	 * @return the colWaitActivityNum
	 */
	public long getColWaitActivityNum() {
		return colWaitActivityNum;
	}

	/**
	 * @param colWaitActivityNum
	 *            the colWaitActivityNum to set
	 */
	public void setColWaitActivityNum(long colWaitActivityNum) {
		this.colWaitActivityNum = colWaitActivityNum;
	}

	public BzrGjtActivity() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActivityAddress() {
		return this.activityAddress;
	}

	public void setActivityAddress(String activityAddress) {
		this.activityAddress = activityAddress;
	}

	public String getActivityIntroduce() {
		return this.activityIntroduce;
	}

	public void setActivityIntroduce(String activityIntroduce) {
		this.activityIntroduce = activityIntroduce;
	}

	public String getActivityPicture() {
		return this.activityPicture;
	}

	public void setActivityPicture(String activityPicture) {
		this.activityPicture = activityPicture;
	}

	public String getActivityTitle() {
		return this.activityTitle;
	}

	public void setActivityTitle(String activityTitle) {
		this.activityTitle = activityTitle;
	}

	public BigDecimal getAuditStatus() {
		return this.auditStatus;
	}

	public void setAuditStatus(BigDecimal auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Date getBeginTime() {
		return this.beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public BigDecimal getCeilingNum() {
		return this.ceilingNum;
	}

	public void setCeilingNum(BigDecimal ceilingNum) {
		this.ceilingNum = ceilingNum;
	}

	public BigDecimal getChargeMoney() {
		return this.chargeMoney;
	}

	public void setChargeMoney(BigDecimal chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	public BigDecimal getCommentNum() {
		return this.commentNum;
	}

	public void setCommentNum(BigDecimal commentNum) {
		this.commentNum = commentNum;
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

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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

	public BigDecimal getIsFree() {
		return this.isFree;
	}

	public void setIsFree(BigDecimal isFree) {
		this.isFree = isFree;
	}

	public BigDecimal getJoinNum() {
		return this.joinNum;
	}

	public void setJoinNum(BigDecimal joinNum) {
		this.joinNum = joinNum;
	}

	public String getPublicityPicture() {
		return this.publicityPicture;
	}

	public void setPublicityPicture(String publicityPicture) {
		this.publicityPicture = publicityPicture;
	}

	public String getReceiveId() {
		return this.receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	public BigDecimal getReceiveType() {
		return this.receiveType;
	}

	public void setReceiveType(BigDecimal receiveType) {
		this.receiveType = receiveType;
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

}