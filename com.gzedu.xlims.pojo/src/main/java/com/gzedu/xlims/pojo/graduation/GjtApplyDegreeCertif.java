package com.gzedu.xlims.pojo.graduation;

import com.gzedu.xlims.pojo.GjtStudentInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 申请毕业证实体类
 */
@Entity
@Table(name = "GJT_APPLY_DEGREE_CERTIF")
@NamedQuery(name = "GjtGraduationApplyCertifDegree.findAll", query = "SELECT g FROM GjtApplyDegreeCertif g")
public class GjtApplyDegreeCertif implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 申请ID
	 */
	@Id
	@Column(name = "APPLY_ID")
	private String applyId;

	@ManyToOne
	@JoinColumn(name = "GRADUATION_PLAN_ID")
	private GjtGraduationPlan gjtGraduationPlan;

	@ManyToOne
	@JoinColumn(name = "STUDENT_ID")
	private GjtStudentInfo gjtStudentInfo;

	@Column(name = "STUDENT_ID", insertable = false, updatable = false)
	private String studentId;

	@Column(name = "COLLEGE_ID")
	private String collegeId;

	//院校
	@ManyToOne
	@JoinColumn(name = "COLLEGE_ID", insertable = false, updatable = false)
	private GjtDegreeCollege gjtDegreeCollege;

	//身份证反面
	@Column(name = "IDCARD_BACK_URL")
	private String idcardBackUrl;

	//身份证正面
	@Column(name = "IDCARD_FRONT_URL")
	private String idcardFrontUrl;

	//学位论文查重初检报告
	@Column(name = "PRELIMINARY_REPORT_URL")
	private String preliminaryReportUrl;

	//毕业设计（论文）审批
	@Column(name = "THESIS_URL")
	private String thesisUrl;

	//资料审核意见
	@Column(name = "INFORMATION_AUDIT_OPINION")
	private String informationAuditOpinion;

	//资料审核结果 默认0(0-未提交 1-已提交 11-通过 12-未通过 )
	@Column(name = "INFORMATION_AUDIT")
	private Integer informationAudit;
	/**
	 * 毕业证书编号
	 */
//	@Column(name = "GRADUATION_CERTIFICATE_NO")
//	private String graduationCertificateNo;

	/**
	 * 毕业证书路径
	 */
//	@Column(name = "GRADUATION_CERTIFICATE_URL")
//	private String graduationCertificateUrl;

	/**
	 * 申请学位满足条件，默认0 1-已达标，0-未达标
	 */
	@Column(name = "DEGREE_CONDITION")
	private Integer degreeCondition;

	/**
	 * 是否申请学位，默认0 1-申请，0-不申请
	 */
	@Column(name = "APPLY_DEGREE")
	private Integer applyDegree;

	/**
	 * 电子注册号
	 */
	@Column(name = "ELE_REGISTRATION_NUMBER")
	private String eleRegistrationNumber;

	/**
	 * 审核状态，默认0(0-待审核 1-分部通过 2-分部未通过 11总部通过 12-总部不通过 )
	 */
	@Column(name = "AUDIT_STATE")
	private Integer auditState;

	/**
	 * 领取时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECEIVE_DT")
	private Date receiveDt;

	/**
	 * 是否领取，默认0 (0-待发放 1-已领取 2-已发放，待领取)
	 */
	@Column(name = "IS_RECEIVE", insertable = false)
	private Integer isReceive;

	/**
	 * 学生申请备注
	 */
	@Column(name = "REMARK")
	private String remark;

	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "DELETED_BY", insertable = false)
	private String deletedBy;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Transient
	private GjtGraduationRegister gjtGraduationRegister;

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public GjtGraduationPlan getGjtGraduationPlan() {
		return gjtGraduationPlan;
	}

	public void setGjtGraduationPlan(GjtGraduationPlan gjtGraduationPlan) {
		this.gjtGraduationPlan = gjtGraduationPlan;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public Integer getApplyDegree() {
		return applyDegree;
	}

	public void setApplyDegree(Integer applyDegree) {
		this.applyDegree = applyDegree;
	}

	public String getEleRegistrationNumber() {
		return eleRegistrationNumber;
	}

	public void setEleRegistrationNumber(String eleRegistrationNumber) {
		this.eleRegistrationNumber = eleRegistrationNumber;
	}

	public Integer getAuditState() {
		return auditState;
	}

	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getDeletedBy() {
		return deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(String collegeId) {
		this.collegeId = collegeId;
	}

	public GjtDegreeCollege getGjtDegreeCollege() {
		return gjtDegreeCollege;
	}

	public void setGjtDegreeCollege(GjtDegreeCollege gjtDegreeCollege) {
		this.gjtDegreeCollege = gjtDegreeCollege;
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

	public String getPreliminaryReportUrl() {
		return preliminaryReportUrl;
	}

	public void setPreliminaryReportUrl(String preliminaryReportUrl) {
		this.preliminaryReportUrl = preliminaryReportUrl;
	}

	public String getThesisUrl() {
		return thesisUrl;
	}

	public void setThesisUrl(String thesisUrl) {
		this.thesisUrl = thesisUrl;
	}

	public String getInformationAuditOpinion() {
		return informationAuditOpinion;
	}

	public void setInformationAuditOpinion(String informationAuditOpinion) {
		this.informationAuditOpinion = informationAuditOpinion;
	}

	public Integer getInformationAudit() {
		return informationAudit;
	}

	public void setInformationAudit(Integer informationAudit) {
		this.informationAudit = informationAudit;
	}

	public Integer getDegreeCondition() {
		return degreeCondition;
	}

	public void setDegreeCondition(Integer degreeCondition) {
		this.degreeCondition = degreeCondition;
	}

	public Date getReceiveDt() {
		return receiveDt;
	}

	public void setReceiveDt(Date receiveDt) {
		this.receiveDt = receiveDt;
	}

	public Integer getIsReceive() {
		return isReceive;
	}

	public void setIsReceive(Integer isReceive) {
		this.isReceive = isReceive;
	}

	public GjtGraduationRegister getGjtGraduationRegister() {
		return gjtGraduationRegister;
	}

	public void setGjtGraduationRegister(GjtGraduationRegister gjtGraduationRegister) {
		this.gjtGraduationRegister = gjtGraduationRegister;
	}

}