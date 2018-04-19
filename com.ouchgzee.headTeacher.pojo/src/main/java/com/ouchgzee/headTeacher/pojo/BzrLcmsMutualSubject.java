package com.ouchgzee.headTeacher.pojo;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the LCMS_MUTUAL_SUBJECT database table.
 * 
 */
@Entity
@Table(name = "LCMS_MUTUAL_SUBJECT")
// @NamedQuery(name = "LcmsMutualSubject.findAll", query = "SELECT l FROM LcmsMutualSubject l")
@Deprecated public class BzrLcmsMutualSubject implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SUBJECT_ID")
	private String subjectId;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "SUBJECT_ID")
	@OrderBy("createdDt asc")
	private List<BzrLcmsMutualReply> gjtLcmsMutualReply;

	@Column(name = "ACT_ID")
	private String actId;

	@Column(name = "CLASS_ID")
	private String classId;// 有可能是教学班，有可能是课程班，取决于问题转发给班主任还是辅导老师

	@Column(name = "CREATE_ACCOUNT_ID", insertable = false, updatable = false)
	private String createAccountId;// 创建者

	@OneToOne(fetch = FetchType.LAZY) // 用户表
	@JoinColumn(name = "CREATE_ACCOUNT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtUserAccount createAccount;

	@Column(name = "CREATED_BY") // 旧表的使用了student和emplyee；废弃;但是会兼容跟以前旧的
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", updatable = false)
	private Date createdDt;

	@Column(name = "FEEDBACK_CONTACTS")
	private String feedbackContacts;

	@Column(name = "FEEDBACK_TYPE")
	private String feedbackType;

	@Column(name = "FORWARD_ACCOUNT_ID")
	private String forwardAccountId;// 转发的用户

	@Column(name = "HAS_OTHER_QUESTION")
	private String hasOtherQuestion;

	@Column(name = "INITIAL_ACCOUNT_ID")
	private String initialAccountId;// 问题初始发送的用户

	@Column(name = "IS_COMMEND")
	private String isCommend;// 常见问题

	private String isdeleted;// 是否删除

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PUBLISH_DT")
	private Date publishDt;

	private String remark;

	@Column(name = "REPLY_COUNT") // 回复总数
	private BigDecimal replyCount;

	private String replyer;// 回复者 1-辅导老师；2-督导 3-运维人员

	@Column(name = "RES_PATH") // 图片地址英文逗号","隔开
	private String resPath;

	@Column(name = "SUBJECT_CONTENT") // 答疑内容
	private String subjectContent;

	@Column(name = "SUBJECT_STATUS") // 是否解决
	private String subjectStatus;

	@Column(name = "SUBJECT_TITLE") // 答疑标题
	private String subjectTitle;

	@Column(name = "SUBJECT_TYPE") // 废弃没用到
	private String subjectType;

	@Column(name = "TERMCOURSE_ID") // 期课程ID，也就是教学计划ID
	private String termcourseId;

	@Column(name = "UPDATED_BY") // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "USER_ID") // 旧表的使用了student和emplyee；废弃;但是会兼容跟以前旧的
	private String userId;

	@Column(name = "USER_TYPE") // 旧的分 stud和tchr;现在多了后台角色
	private String userType;

	@Column(name = "VERSION")
	private BigDecimal version;

	@Column(name = "ORG_ID") // 学生所属机构
	private String orgId;

	@Column(name = "OFTEN_TYPE") // 常见问题
	private String oftenType;

	public BzrLcmsMutualSubject() {
	}

	public String getOftenType() {
		return oftenType;
	}

	public void setOftenType(String oftenType) {
		this.oftenType = oftenType;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public List<BzrLcmsMutualReply> getGjtLcmsMutualReply() {
		return gjtLcmsMutualReply;
	}

	public void setGjtLcmsMutualReply(List<BzrLcmsMutualReply> gjtLcmsMutualReply) {
		this.gjtLcmsMutualReply = gjtLcmsMutualReply;
	}

	public BzrGjtUserAccount getCreateAccount() {
		return createAccount;
	}

	public void setCreateAccount(BzrGjtUserAccount createAccount) {
		this.createAccount = createAccount;
	}

	public String getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getActId() {
		return this.actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public String getClassId() {
		return this.classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getCreateAccountId() {
		return this.createAccountId;
	}

	public void setCreateAccountId(String createAccountId) {
		this.createAccountId = createAccountId;
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

	public String getFeedbackContacts() {
		return this.feedbackContacts;
	}

	public void setFeedbackContacts(String feedbackContacts) {
		this.feedbackContacts = feedbackContacts;
	}

	public String getFeedbackType() {
		return this.feedbackType;
	}

	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}

	public String getForwardAccountId() {
		return this.forwardAccountId;
	}

	public void setForwardAccountId(String forwardAccountId) {
		this.forwardAccountId = forwardAccountId;
	}

	public String getHasOtherQuestion() {
		return this.hasOtherQuestion;
	}

	public void setHasOtherQuestion(String hasOtherQuestion) {
		this.hasOtherQuestion = hasOtherQuestion;
	}

	public String getInitialAccountId() {
		return this.initialAccountId;
	}

	public void setInitialAccountId(String initialAccountId) {
		this.initialAccountId = initialAccountId;
	}

	public String getIsCommend() {
		return this.isCommend;
	}

	public void setIsCommend(String isCommend) {
		this.isCommend = isCommend;
	}

	public String getIsdeleted() {
		return this.isdeleted;
	}

	public void setIsdeleted(String isdeleted) {
		this.isdeleted = isdeleted;
	}

	public Date getPublishDt() {
		return this.publishDt;
	}

	public void setPublishDt(Date publishDt) {
		this.publishDt = publishDt;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getReplyCount() {
		return this.replyCount;
	}

	public void setReplyCount(BigDecimal replyCount) {
		this.replyCount = replyCount;
	}

	public String getReplyer() {
		return this.replyer;
	}

	public void setReplyer(String replyer) {
		this.replyer = replyer;
	}

	public String getResPath() {
		return this.resPath;
	}

	public void setResPath(String resPath) {
		this.resPath = resPath;
	}

	public String getSubjectContent() {
		return this.subjectContent;
	}

	public void setSubjectContent(String subjectContent) {
		this.subjectContent = subjectContent;
	}

	public String getSubjectStatus() {
		return this.subjectStatus;
	}

	public void setSubjectStatus(String subjectStatus) {
		this.subjectStatus = subjectStatus;
	}

	public String getSubjectTitle() {
		return this.subjectTitle;
	}

	public void setSubjectTitle(String subjectTitle) {
		this.subjectTitle = subjectTitle;
	}

	public String getSubjectType() {
		return this.subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getTermcourseId() {
		return this.termcourseId;
	}

	public void setTermcourseId(String termcourseId) {
		this.termcourseId = termcourseId;
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