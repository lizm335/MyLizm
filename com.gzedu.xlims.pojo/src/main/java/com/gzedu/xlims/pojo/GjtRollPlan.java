package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


/**
 * 学籍计划<br/>
 * The persistent class for the GJT_ROLL_PLAN database table.
 * 
 */
@Entity
@Table(name="GJT_ROLL_PLAN")
@NamedQuery(name="GjtRollPlan.findAll", query="SELECT g FROM GjtRollPlan g")
public class GjtRollPlan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private String id;

	@Column(name="ROLL_PLAN_NO")
	private String rollPlanNo;

	@ManyToOne(fetch = FetchType.LAZY) // 年级ID
	@JoinColumn(name = "GRADE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtGrade gjtGrade;

	@Column(name="ROLL_PLAN_TITLE")
	private String rollPlanTitle;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OFFICIAL_BEGIN_DT")
	private Date officialBeginDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OFFICIAL_END_DT")
	private Date officialEndDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OFFICIAL_BEGIN_DT_2")
	private Date officialBeginDt2;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OFFICIAL_END_DT_2")
	private Date officialEndDt2;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FOLLOW_BEGIN_DT")
	private Date followBeginDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FOLLOW_END_DT")
	private Date followEndDt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ROLL_TRANS_BEGIN_DT")
	private Date rollTransBeginDt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ROLL_TRANS_END_DT")
	private Date rollTransEndDt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ROLL_TRANS_BEGIN_DT2")
	private Date rollTransBeginDt2;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ROLL_TRANS_END_DT2")
	private Date rollTransEndDt2;

	@Column(name="AUDIT_STATE", insertable = false)
	private BigDecimal auditState;

	@Column(name="AUDIT_CONTENT")
	private String auditContent;

	@Column(name="AUDIT_OPERATOR")
	private String auditOperator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AUDIT_DT")
	private Date auditDt;

	@Column(name="PUBLISH_OPERATOR")
	private String publishOperator;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="PUBLISH_DT")
	private Date publishDt;

	@Column(name="XX_ID")
	private String xxId;

	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	public GjtRollPlan() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRollPlanNo() {
		return rollPlanNo;
	}

	public void setRollPlanNo(String rollPlanNo) {
		this.rollPlanNo = rollPlanNo;
	}

	public GjtGrade getGjtGrade() {
		return gjtGrade;
	}

	public void setGjtGrade(GjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

	public String getRollPlanTitle() {
		return rollPlanTitle;
	}

	public void setRollPlanTitle(String rollPlanTitle) {
		this.rollPlanTitle = rollPlanTitle;
	}

	public Date getOfficialBeginDt() {
		return officialBeginDt;
	}

	public void setOfficialBeginDt(Date officialBeginDt) {
		this.officialBeginDt = officialBeginDt;
	}

	public Date getOfficialEndDt() {
		return officialEndDt;
	}

	public void setOfficialEndDt(Date officialEndDt) {
		this.officialEndDt = officialEndDt;
	}

	public Date getOfficialBeginDt2() {
		return officialBeginDt2;
	}

	public void setOfficialBeginDt2(Date officialBeginDt2) {
		this.officialBeginDt2 = officialBeginDt2;
	}

	public Date getOfficialEndDt2() {
		return officialEndDt2;
	}

	public void setOfficialEndDt2(Date officialEndDt2) {
		this.officialEndDt2 = officialEndDt2;
	}

	public Date getFollowBeginDt() {
		return followBeginDt;
	}

	public void setFollowBeginDt(Date followBeginDt) {
		this.followBeginDt = followBeginDt;
	}

	public Date getFollowEndDt() {
		return followEndDt;
	}

	public void setFollowEndDt(Date followEndDt) {
		this.followEndDt = followEndDt;
	}

	public BigDecimal getAuditState() {
		return auditState;
	}

	public void setAuditState(BigDecimal auditState) {
		this.auditState = auditState;
	}

	public String getAuditContent() {
		return auditContent;
	}

	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}

	public String getAuditOperator() {
		return auditOperator;
	}

	public void setAuditOperator(String auditOperator) {
		this.auditOperator = auditOperator;
	}

	public Date getAuditDt() {
		return auditDt;
	}

	public void setAuditDt(Date auditDt) {
		this.auditDt = auditDt;
	}

	public String getPublishOperator() {
		return publishOperator;
	}

	public void setPublishOperator(String publishOperator) {
		this.publishOperator = publishOperator;
	}

	public Date getPublishDt() {
		return publishDt;
	}

	public void setPublishDt(Date publishDt) {
		this.publishDt = publishDt;
	}

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public BigDecimal getVersion() {
		return version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public String getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Date getRollTransBeginDt() {
		return rollTransBeginDt;
	}

	public void setRollTransBeginDt(Date rollTransBeginDt) {
		this.rollTransBeginDt = rollTransBeginDt;
	}

	public Date getRollTransEndDt() {
		return rollTransEndDt;
	}

	public void setRollTransEndDt(Date rollTransEndDt) {
		this.rollTransEndDt = rollTransEndDt;
	}

	public Date getRollTransBeginDt2() {
		return rollTransBeginDt2;
	}

	public void setRollTransBeginDt2(Date rollTransBeginDt2) {
		this.rollTransBeginDt2 = rollTransBeginDt2;
	}

	public Date getRollTransEndDt2() {
		return rollTransEndDt2;
	}

	public void setRollTransEndDt2(Date rollTransEndDt2) {
		this.rollTransEndDt2 = rollTransEndDt2;
	}
}