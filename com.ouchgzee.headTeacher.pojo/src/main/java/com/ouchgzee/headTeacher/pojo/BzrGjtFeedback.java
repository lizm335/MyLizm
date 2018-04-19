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

/**
 * 留言反馈信息实体类<br>
 * The persistent class for the GJT_FEEDBACK database table.
 * 
 */
@Entity
@Table(name = "GJT_FEEDBACK")
// @NamedQuery(name = "GjtFeedback.findAll", query = "SELECT g FROM GjtFeedback g")
@Deprecated public class BzrGjtFeedback implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	/**
	 * 转发给辅导老师的ID
	 * 
	 * @ManyToOne
	 * @JoinColumn(name = "SHARE_EMPLOYEE_ID")
	 * @NotFound(action = NotFoundAction.IGNORE) private GjtEmployeeInfo
	 *                  gjtEmployeeInfo;
	 */
	@Lob
	private String content;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;// 旧系统用的是studentId

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", updatable = false)
	private Date createdDt;

	@Column(name = "CREATOR_ROLE")
	private String creatorRole;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DEAL_DT")
	private Date dealDt;

	@Column(name = "DEAL_RESULT")
	private String dealResult;

	@Lob
	@Column(name = "EVALUATE_CONTENT")
	private String evaluateContent;

	@Column(name = "EVALUATE_SCORE", insertable = false)
	private BigDecimal evaluateScore;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "ORG_CODE")
	private String orgCode;

	@Column(name = "ORG_ID")
	private String orgId;

	private String pid;

	private String source;

	private String tags;

	private String title;

	@Column(name = "TYPE")
	private String type;

	@Column(name = "USER_AGENT")
	private String userAgent;

	@Column(name = "IMGURLS")
	private String imgUrls;

	public BzrGjtFeedback() {
	}

	public String getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(String imgUrls) {
		this.imgUrls = imgUrls;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getCreatorRole() {
		return this.creatorRole;
	}

	public void setCreatorRole(String creatorRole) {
		this.creatorRole = creatorRole;
	}

	public Date getDealDt() {
		return this.dealDt;
	}

	public void setDealDt(Date dealDt) {
		this.dealDt = dealDt;
	}

	public String getDealResult() {
		return this.dealResult;
	}

	public void setDealResult(String dealResult) {
		this.dealResult = dealResult;
	}

	public String getEvaluateContent() {
		return this.evaluateContent;
	}

	public void setEvaluateContent(String evaluateContent) {
		this.evaluateContent = evaluateContent;
	}

	public BigDecimal getEvaluateScore() {
		return this.evaluateScore;
	}

	public void setEvaluateScore(BigDecimal evaluateScore) {
		this.evaluateScore = evaluateScore;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPid() {
		return this.pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTags() {
		return this.tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserAgent() {
		return this.userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/*
	 * public GjtEmployeeInfo getGjtEmployeeInfo() { return gjtEmployeeInfo; }
	 * 
	 * public void setGjtEmployeeInfo(GjtEmployeeInfo gjtEmployeeInfo) {
	 * this.gjtEmployeeInfo = gjtEmployeeInfo; }
	 */
}