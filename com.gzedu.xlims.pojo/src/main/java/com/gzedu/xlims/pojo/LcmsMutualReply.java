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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the LCMS_MUTUAL_REPLY database table.
 * 
 */
@Entity
@Table(name = "LCMS_MUTUAL_REPLY")
@NamedQuery(name = "LcmsMutualReply.findAll", query = "SELECT l FROM LcmsMutualReply l")
public class LcmsMutualReply implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "REPLY_ID")
	private String replyId;

	@Column(name = "SUBJECT_ID", insertable = false, updatable = false) // 答疑主表
	private String subjectId;

	@ManyToOne
	@JoinColumn(name = "SUBJECT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private LcmsMutualSubject lcmsMutualSubject;

	@Column(name = "CREATED_BY", insertable = false, updatable = false) // 旧程序没用到；现在用userAccoutId，也就是回复者ID
	private String createdBy;

	@OneToOne(fetch = FetchType.LAZY) // 用户表
	@JoinColumn(name = "CREATED_BY")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtUserAccount CreatedUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", updatable = false)
	private Date createdDt;

	private String isdeleted;

	@Column(name = "PARENT_ID")
	private String parentId;

	private String remark;

	@Column(name = "REPLY_CONTENT")
	private String replyContent;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REPLY_DT")
	private Date replyDt;

	@Column(name = "REPLY_TITLE")
	private String replyTitle;

	@Column(name = "RES_PATH")
	private String resPath;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "USER_TYPE")
	private String userType;

	@Column(name = "VERSION")
	private BigDecimal version;

	public LcmsMutualReply() {
	}

	public LcmsMutualSubject getLcmsMutualSubject() {
		return lcmsMutualSubject;
	}

	public void setLcmsMutualSubject(LcmsMutualSubject lcmsMutualSubject) {
		this.lcmsMutualSubject = lcmsMutualSubject;
	}

	public GjtUserAccount getCreatedUser() {
		return CreatedUser;
	}

	public void setCreatedUser(GjtUserAccount createdUser) {
		CreatedUser = createdUser;
	}

	public String getReplyId() {
		return this.replyId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
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

	public String getIsdeleted() {
		return this.isdeleted;
	}

	public void setIsdeleted(String isdeleted) {
		this.isdeleted = isdeleted;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReplyContent() {
		return this.replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public Date getReplyDt() {
		return this.replyDt;
	}

	public void setReplyDt(Date replyDt) {
		this.replyDt = replyDt;
	}

	public String getReplyTitle() {
		return this.replyTitle;
	}

	public void setReplyTitle(String replyTitle) {
		this.replyTitle = replyTitle;
	}

	public String getResPath() {
		return this.resPath;
	}

	public void setResPath(String resPath) {
		this.resPath = resPath;
	}

	public String getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
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

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

}