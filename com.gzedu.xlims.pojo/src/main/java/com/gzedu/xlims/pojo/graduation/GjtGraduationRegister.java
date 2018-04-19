package com.gzedu.xlims.pojo.graduation;

import com.gzedu.xlims.pojo.GjtStudentInfo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * 毕业登记表
 * 
 */
@Entity
@Table(name="GJT_GRADUATION_REGISTER")
@NamedQuery(name="GjtGraduationRegister.findAll", query="SELECT g FROM GjtGraduationRegister g")
public class GjtGraduationRegister implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="REGISTER_ID")
	private String registerId;

	@ManyToOne
	@JoinColumn(name="STUDENT_ID", insertable = false, updatable = false)
	private GjtStudentInfo gjtStudentInfo;

	@Column(name="AWARD_RECORD")
	private String awardRecord;

	private String company;

	@Column(name="COMPANY_PHONE")
	private String companyPhone;

	@Column(name="CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name="DELETED_BY", insertable = false)
	private String deletedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DELETED_DT", insertable = false)
	private Date deletedDt;

	private String email;

	private String evaluation;

	@Column(name="GRADUATION_DESIGN")
	private String graduationDesign;

	@Column(name="HOME_PHONE")
	private String homePhone;

	@Column(name="IS_DELETED", insertable = false)
	private String isDeleted;

	private String phone;
	
	private String submitType;//提交状态：1、保存；2、提交
	/**
	 * 新华社照片
	 */
	private String photo;

	@Column(name="PRACTICE_CONTENT")
	private String practiceContent;

	@Column(name="STUDENT_ID")
	private String studentId;

	@Column(name="UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT", insertable = false)
	private Date updatedDt;

	/**
	 * 快递公司
	 */
	@Column(name="EXPRESS_COMPANY")
	private String expressCompany;

	/**
	 * 快递单号
	 */
	@Column(name="EXPRESS_NUMBER")
	private String expressNumber;

	/**
	 * 快递签收状态，默认0 0-未寄送，1-寄送中，2-已签收
	 */
	@Column(name="EXPRESS_SIGN_STATE", insertable = false)
	private Integer expressSignState;

	/**
	 * 快递签收时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="EXPRESS_SIGN_DT", insertable = false)
	private Date expressSignDt;

	/**
	 * 毕业计划ID
	 */
	@Column(name="GRADUATION_PLAN_ID")
	private String graduationPlanId;

	public GjtGraduationRegister() {
	}

	public String getRegisterId() {
		return this.registerId;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public void setRegisterId(String registerId) {
		this.registerId = registerId;
	}

	public String getAwardRecord() {
		return this.awardRecord;
	}

	public void setAwardRecord(String awardRecord) {
		this.awardRecord = awardRecord;
	}

	public String getCompany() {
		return this.company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyPhone() {
		return this.companyPhone;
	}

	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
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

	public String getDeletedBy() {
		return this.deletedBy;
	}

	public void setDeletedBy(String deletedBy) {
		this.deletedBy = deletedBy;
	}

	public Date getDeletedDt() {
		return this.deletedDt;
	}

	public void setDeletedDt(Date deletedDt) {
		this.deletedDt = deletedDt;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEvaluation() {
		return this.evaluation;
	}

	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

	public String getGraduationDesign() {
		return this.graduationDesign;
	}

	public void setGraduationDesign(String graduationDesign) {
		this.graduationDesign = graduationDesign;
	}

	public String getHomePhone() {
		return this.homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPracticeContent() {
		return this.practiceContent;
	}

	public void setPracticeContent(String practiceContent) {
		this.practiceContent = practiceContent;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
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

	public String getSubmitType() {
		return submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getExpressNumber() {
		return expressNumber;
	}

	public void setExpressNumber(String expressNumber) {
		this.expressNumber = expressNumber;
	}

	public Integer getExpressSignState() {
		return expressSignState;
	}

	public void setExpressSignState(Integer expressSignState) {
		this.expressSignState = expressSignState;
	}

	public Date getExpressSignDt() {
		return expressSignDt;
	}

	public void setExpressSignDt(Date expressSignDt) {
		this.expressSignDt = expressSignDt;
	}

	public String getGraduationPlanId() {
		return graduationPlanId;
	}

	public void setGraduationPlanId(String graduationPlanId) {
		this.graduationPlanId = graduationPlanId;
	}

	@Override
	public String toString() {
		return "GjtGraduationRegister [registerId=" + registerId + ", awardRecord=" + awardRecord + ", company="
				+ company + ", companyPhone=" + companyPhone + ", createdBy=" + createdBy + ", createdDt=" + createdDt
				+ ", deletedBy=" + deletedBy + ", deletedDt=" + deletedDt + ", email=" + email + ", evaluation="
				+ evaluation + ", graduationDesign=" + graduationDesign + ", homePhone=" + homePhone + ", isDeleted="
				+ isDeleted + ", phone=" + phone + ", photo=" + photo + ", practiceContent=" + practiceContent
				+ ", studentId=" + studentId + ", updatedBy=" + updatedBy + ", updatedDt=" + updatedDt + "]";
	}

}