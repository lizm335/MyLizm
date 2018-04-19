package com.gzedu.xlims.pojo;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the GJT_FEEDBACK database table.
 * 
 */
@Entity
@Table(name="GJT_FEEDBACK")
@NamedQuery(name="GjtFeedback.findAll", query="SELECT g FROM GjtFeedback g")
public class GjtFeedback implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Lob
	private String content;

	@Column(name="CREATED_BY")
	private String createdBy;

	//@Temporal(TemporalType.TIMESTAMP)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="CREATOR_ROLE")
	private String creatorRole;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DEAL_DT")
	private Date dealDt;

	@Column(name="DEAL_RESULT")
	private String dealResult;

	@Lob
	@Column(name="EVALUATE_CONTENT")
	private String evaluateContent;

	@Column(name="EVALUATE_SCORE")
	private BigDecimal evaluateScore;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="ORG_CODE")
	private String orgCode;

	@Column(name="ORG_ID")
	private String orgId;

	private String pid;

	private String source;

	private String tags;

	private String title;

	@Column(name="TYPE")
	private String type;

	@Column(name="USER_AGENT")
	private String userAgent;
	
	@Column(name="IMGURLS")
	private String imgUrls;
	
	@OneToMany(cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "pid")
	@OrderBy("createdDt asc")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<GjtFeedback> gjtFeedbackList;	
	
	public List<GjtFeedback> getGjtFeedbackList() {
		return gjtFeedbackList;
	}

	public void setGjtFeedbackList(List<GjtFeedback> gjtFeedbackList) {
		this.gjtFeedbackList = gjtFeedbackList;
	}

	public String getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(String imgUrls) {
		this.imgUrls = imgUrls;
	}

	public GjtFeedback() {
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

}