package com.gzedu.xlims.pojo.graduation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gzedu.xlims.pojo.GjtSpecialtyBase;
import com.gzedu.xlims.pojo.GjtStudentInfo;

/**
 * The persistent class for the GJT_GRADUATION_APPLY_DEGREE database table.
 * 学位证申请表（废弃）
 */
@Entity
@Table(name = "GJT_GRADUATION_APPLY_DEGREE")
@NamedQuery(name = "GjtGraduationApplyDegree.findAll", query = "SELECT g FROM GjtGraduationApplyDegree g")
public class GjtGraduationApplyDegree implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "APPLY_ID")
	private String applyId;

	@Column(name = "APPLY_CONTENT")
	private String applyContent;

	@Column(name = "AUDIT_STATE")
	private int auditState;// 审核状态，默认0(0-待审核 1-通过 2-未通过)

	@Column(name = "COLLEGE_ID")
	private String collegeId;
	@ManyToOne
	@JoinColumn(name = "COLLEGE_ID", insertable = false, updatable = false)
	private GjtDegreeCollege gjtDegreeCollege;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "DEGREE_CERTIFICATE_NO")
	private String degreeCertificateNo;

	@Column(name = "DEGREE_CERTIFICATE_URL")
	private String degreeCertificateUrl;

	@Column(name = "DEGREE_CONDITION")
	private int degreeCondition;

	@Column(name = "DEGREE_ID")
	private String degreeId;

	@Column(name = "DELETED_BY")
	private String deletedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETED_DT")
	private Date deletedDt;

	@Column(name = "ENGLISH_CERTIFICATE_STATE")
	private BigDecimal englishCertificateState;

	@Column(name = "ENGLISH_CERTIFICATE_URL")
	private String englishCertificateUrl;

	@Column(name = "GRADUATION_PLAN_ID")
	private String graduationPlanId;

	@ManyToOne
	@JoinColumn(name = "GRADUATION_PLAN_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtGraduationPlan gjtGraduationPlan;

	@Column(name = "IDCARD_BACK_URL")
	private String idcardBackUrl;

	@Column(name = "IDCARD_FRONT_URL")
	private String idcardFrontUrl;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "IS_RECEIVE")
	private int isReceive;// 0 未发放；1已领取；2已发放，待领取

	@Column(name = "PAPER_CHECK_URL")
	private String paperCheckUrl;

	@Column(name = "PAPER_URL")
	private String paperUrl;

	@Temporal(TemporalType.DATE)
	@Column(name = "RECEIVE_DT")
	private Date receiveDt;

	private String remark;

	private String signature;

	private String orgId;

	@Column(name = "STUDENT_ID")
	private String studentId;

	/**
	 * 毕业满足条件，默认0 1-已达标，0-未达标
	 */
	@Column(name = "GRADUATION_CONDITION")
	private Integer graduationCondition;

	@ManyToOne
	@JoinColumn(name = "STUDENT_ID", insertable = false, updatable = false)
	private GjtStudentInfo gjtStudentInfo;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "SPECIALTY_ID")
	private String specialtyId;

	@ManyToOne
	@JoinColumn(name = "SPECIALTY_ID", insertable = false, updatable = false)
	private GjtSpecialtyBase gjtSpecialtyBase;

	@Transient
	private GjtGraduationRegister gjtGraduationRegister;

	public GjtGraduationApplyDegree() {
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getApplyContent() {
		return applyContent;
	}

	public void setApplyContent(String applyContent) {
		this.applyContent = applyContent;
	}

	public int getAuditState() {
		return auditState;
	}

	public void setAuditState(int auditState) {
		this.auditState = auditState;
	}

	public String getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(String collegeId) {
		this.collegeId = collegeId;
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

	public String getDegreeCertificateNo() {
		return degreeCertificateNo;
	}

	public void setDegreeCertificateNo(String degreeCertificateNo) {
		this.degreeCertificateNo = degreeCertificateNo;
	}

	public String getDegreeCertificateUrl() {
		return degreeCertificateUrl;
	}

	public void setDegreeCertificateUrl(String degreeCertificateUrl) {
		this.degreeCertificateUrl = degreeCertificateUrl;
	}

	public int getDegreeCondition() {
		return degreeCondition;
	}

	public void setDegreeCondition(int degreeCondition) {
		this.degreeCondition = degreeCondition;
	}

	public String getDegreeId() {
		return degreeId;
	}

	public void setDegreeId(String degreeId) {
		this.degreeId = degreeId;
	}

	public String getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedDt() {
		return deletedDt;
	}

	public void setDeletedDt(Date deletedDt) {
		this.deletedDt = deletedDt;
	}

	public BigDecimal getEnglishCertificateState() {
		return englishCertificateState;
	}

	public void setEnglishCertificateState(BigDecimal englishCertificateState) {
		this.englishCertificateState = englishCertificateState;
	}

	public String getEnglishCertificateUrl() {
		return englishCertificateUrl;
	}

	public void setEnglishCertificateUrl(String englishCertificateUrl) {
		this.englishCertificateUrl = englishCertificateUrl;
	}

	public String getGraduationPlanId() {
		return graduationPlanId;
	}

	public void setGraduationPlanId(String graduationPlanId) {
		this.graduationPlanId = graduationPlanId;
	}

	public GjtGraduationPlan getGjtGraduationPlan() {
		return gjtGraduationPlan;
	}

	public void setGjtGraduationPlan(GjtGraduationPlan gjtGraduationPlan) {
		this.gjtGraduationPlan = gjtGraduationPlan;
	}

	public String getIdcardBackUrl() {
		return idcardBackUrl;
	}

	public void setIdcardBackUrl(String idcardBackUrl) {
		this.idcardBackUrl = idcardBackUrl;
	}

	public String getIdcardFrontUrl() {
		return idcardFrontUrl;
	}

	public void setIdcardFrontUrl(String idcardFrontUrl) {
		this.idcardFrontUrl = idcardFrontUrl;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getIsReceive() {
		return isReceive;
	}

	public void setIsReceive(int isReceive) {
		this.isReceive = isReceive;
	}

	public String getPaperCheckUrl() {
		return paperCheckUrl;
	}

	public void setPaperCheckUrl(String paperCheckUrl) {
		this.paperCheckUrl = paperCheckUrl;
	}

	public String getPaperUrl() {
		return paperUrl;
	}

	public void setPaperUrl(String paperUrl) {
		this.paperUrl = paperUrl;
	}

	public Date getReceiveDt() {
		return receiveDt;
	}

	public void setReceiveDt(Date receiveDt) {
		this.receiveDt = receiveDt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
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

	public GjtGraduationRegister getGjtGraduationRegister() {
		return gjtGraduationRegister;
	}

	public void setGjtGraduationRegister(GjtGraduationRegister gjtGraduationRegister) {
		this.gjtGraduationRegister = gjtGraduationRegister;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Integer getGraduationCondition() {
		return graduationCondition;
	}

	public void setGraduationCondition(Integer graduationCondition) {
		this.graduationCondition = graduationCondition;
	}

	public GjtDegreeCollege getGjtDegreeCollege() {
		return gjtDegreeCollege;
	}

	public void setGjtDegreeCollege(GjtDegreeCollege gjtDegreeCollege) {
		this.gjtDegreeCollege = gjtDegreeCollege;
	}

	public String getSpecialtyId() {
		return specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	public GjtSpecialtyBase getGjtSpecialtyBase() {
		return gjtSpecialtyBase;
	}

	public void setGjtSpecialtyBase(GjtSpecialtyBase gjtSpecialtyBase) {
		this.gjtSpecialtyBase = gjtSpecialtyBase;
	}

	@Transient
	public String getAuditStateText() {
		switch (auditState) {
			case 0:
				return "待审核";
			case 1:
				return "审核通过";
			case 2:
				return "审核不通过";
			default:
				return null;
		}
	}

}