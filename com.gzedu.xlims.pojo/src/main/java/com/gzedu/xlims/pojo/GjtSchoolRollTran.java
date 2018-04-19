package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


/**
 * The persistent class for the GJT_SCHOOL_ROLL_TRANS database table.
 * 
 */
@Entity
@Table(name="GJT_SCHOOL_ROLL_TRANS")
@NamedQuery(name="GjtSchoolRollTran.findAll", query="SELECT g FROM GjtSchoolRollTran g")
public class GjtSchoolRollTran implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="TRANSACTION_ID")
	private String transactionId;

	@Column(name="AUDIT_OPERATOR")
	private String auditOperator;//审核人

	@Column(name="AUDIT_OPERATOR_ROLE")
	private int auditOperatorRole;//审核角色

	@Column(name="CREATED_BY",updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="IS_DELETED",insertable = false)
	private String isDeleted;

	private String memo;

	@Column(name="STUDENT_ID")
	private String studentId;
	
	@Column(name="IS_APPLY_FOR")
	private String isApplyFor; //是否是重新申请 0：是；1：否

	@Lob
	@Column(name="TRANSACTION_CONTENT")
	private String transactionContent;//异动内容

	@Column(name="TRANSACTION_STATUS")
	private BigDecimal transactionStatus;//异动状态:默认0 0-待审核 1-通过 2-不通过 3-劝学中 4-劝学失败 5-劝学成功 6-待核算 7-已核算 8-待确认 9-已确认 10-待登记 11-已登记 12-退学成功 13-撤销退学
	
	@Column(name="TRANSACTION_PART_STATUS")
	private Integer transactionPartStatus;//异动子状态（为了区分信息更正的类别:0、性别民族变更 ；1、入学文化程度更变；2、姓名变更；3、身份证变更）
	
	@Column(name="TRANSACTION_TYPE")
	private int transactionType;//异动类型

	@Column(name="UPDATED_BY",insertable = false)
	private String updatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT",insertable = false)
	private Date updatedDt;

	@Column(name="VERSION",insertable = false)
	private int version;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID" ,insertable=false,updatable=false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtStudentInfo gjtStudentInfo;
	
	@OneToMany(mappedBy="gjtSchoolRollTran",fetch = FetchType.LAZY)
	private List<GjtSchoolRollTransAudit> gjtSchoolRollTransAudits;

	public GjtSchoolRollTran() {
	}

	public String getTransactionId() {
		return this.transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getAuditOperator() {
		return this.auditOperator;
	}

	public void setAuditOperator(String auditOperator) {
		this.auditOperator = auditOperator;
	}

	public int getAuditOperatorRole() {
		return this.auditOperatorRole;
	}

	public void setAuditOperatorRole(int auditOperatorRole) {
		this.auditOperatorRole = auditOperatorRole;
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

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getTransactionContent() {
		return this.transactionContent;
	}

	public void setTransactionContent(String transactionContent) {
		this.transactionContent = transactionContent;
	}

	public BigDecimal getTransactionStatus() {
		return this.transactionStatus;
	}

	public void setTransactionStatus(BigDecimal transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	
	public Integer getTransactionPartStatus() {
		return transactionPartStatus;
	}

	public void setTransactionPartStatus(Integer transactionPartStatus) {
		this.transactionPartStatus = transactionPartStatus;
	}

	public int getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
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

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<GjtSchoolRollTransAudit> getGjtSchoolRollTransAudits() {
		return this.gjtSchoolRollTransAudits;
	}

	public void setGjtSchoolRollTransAudits(List<GjtSchoolRollTransAudit> gjtSchoolRollTransAudits) {
		this.gjtSchoolRollTransAudits = gjtSchoolRollTransAudits;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public String getIsApplyFor() {
		return isApplyFor;
	}

	public void setIsApplyFor(String isApplyFor) {
		this.isApplyFor = isApplyFor;
	}
}