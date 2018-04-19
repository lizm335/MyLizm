package com.gzedu.xlims.pojo.graduation;

import java.io.Serializable;
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

import com.google.common.base.Strings;
import com.gzedu.xlims.pojo.GjtStudentInfo;

/**
 * 毕业申请实体类
 */
@Entity
@Table(name = "GJT_GRADUATION_APPLY_CERTIF")
@NamedQuery(name = "GjtGraduationApplyCertif.findAll", query = "SELECT g FROM GjtGraduationApplyCertif g")
public class GjtGraduationApplyCertif implements Serializable {

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
	/**
	 * 毕业证书编号(已废弃，不维护)
	 */
	@Column(name = "GRADUATION_CERTIFICATE_NO")
	private String graduationCertificateNo;

	/**
	 * 因为现在是老师上传，以前是要学生自己上传，所以放在毕业生等级表里面,两处我都赋值了
	 */
	@Column(name = "PHOTO_URL")
	private String photoUrl;

	/**
	 * 毕业证书路径(已废弃，不维护)
	 */
	@Column(name = "GRADUATION_CERTIFICATE_URL")
	private String graduationCertificateUrl;

	/**
	 * 是否申请学位，默认0 1-申请，0-不申请
	 */
	@Column(name = "APPLY_DEGREE")
	private Integer applyDegree;

	/**
	 * 签名，BASE64图片上传到eefile保存图片路径
	 */
	@Column(name = "SIGNATURE")
	private String signature;

	/**
	 * 电子注册号
	 */
	@Column(name = "ELE_REGISTRATION_NUMBER")
	private String eleRegistrationNumber;

	/**
	 * 毕业满足条件，默认0 1-已达标，0-未达标
	 */
	@Column(name = "GRADUATION_CONDITION")
	private Integer graduationCondition;

	/**
	 * 审核状态，默认0(0-待审核 1-分部通过 2-分部未通过 6-学员提交申请 11总部通过 12-总部不通过 )
	 */
	@Column(name = "AUDIT_STATE")
	private Integer auditState;

	/**
	 * 是否领取，默认0 (0-待发放 1-已领取 2-已发放，待领取)(已废弃，不维护)
	 */
	@Column(name = "IS_RECEIVE", insertable = false)
	private Integer isReceive;

	/**
	 * 毕业状态 0 - 未毕业 1 - 延迟毕业 2 -已毕业
	 */
	@Column(name = "GRADUATION_STATE")
	private Integer graduationState;

	/**
	 * 领取时间(已废弃，不维护)
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "RECEIVE_DT")
	private Date receiveDt;

	/**
	 * 备注
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

	/**
	 * 延迟毕业确认时间
	 */
	@Column(name = "DELAY_GRADUATION_DT")
	private Date delayGraduationDT;

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

	public String getGraduationCertificateNo() {
		return graduationCertificateNo;
	}

	public void setGraduationCertificateNo(String graduationCertificateNo) {
		this.graduationCertificateNo = graduationCertificateNo;
	}

	public String getGraduationCertificateUrl() {
		return graduationCertificateUrl;
	}

	public void setGraduationCertificateUrl(String graduationCertificateUrl) {
		this.graduationCertificateUrl = graduationCertificateUrl;
	}

	public Integer getApplyDegree() {
		return applyDegree;
	}

	public void setApplyDegree(Integer applyDegree) {
		this.applyDegree = applyDegree;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Integer getGraduationCondition() {
		return graduationCondition;
	}

	public void setGraduationCondition(Integer graduationCondition) {
		this.graduationCondition = graduationCondition;
	}

	public Integer getAuditState() {
		return auditState;
	}

	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public Integer getIsReceive() {
		return isReceive;
	}

	public void setIsReceive(Integer isReceive) {
		this.isReceive = isReceive;
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

	public GjtGraduationRegister getGjtGraduationRegister() {
		return gjtGraduationRegister;
	}

	public void setGjtGraduationRegister(GjtGraduationRegister gjtGraduationRegister) {
		this.gjtGraduationRegister = gjtGraduationRegister;
	}

	public String getEleRegistrationNumber() {
		return eleRegistrationNumber;
	}

	public void setEleRegistrationNumber(String eleRegistrationNumber) {
		this.eleRegistrationNumber = eleRegistrationNumber;
	}

	public Integer getGraduationState() {
		return graduationState;
	}

	public void setGraduationState(Integer graduationState) {
		this.graduationState = graduationState;
	}

	@Transient
	public GjtGraduationApplyCertif putRemark(String remark) {
		if (!Strings.isNullOrEmpty(remark)) {
			this.setRemark(remark);
		}
		return this;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public Date getDelayGraduationDT() {
		return delayGraduationDT;
	}

	public void setDelayGraduationDT(Date delayGraduationDT) {
		this.delayGraduationDT = delayGraduationDT;
	}
}