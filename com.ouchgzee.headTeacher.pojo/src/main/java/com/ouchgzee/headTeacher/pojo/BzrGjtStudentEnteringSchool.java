package com.ouchgzee.headTeacher.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 学员入学确认<br/>
 * The persistent class for the GJT_STUDENT_ENTERING_SCHOOL database table.
 * 
 */
@Entity
@Table(name="GJT_STUDENT_ENTERING_SCHOOL")
// @NamedQuery(name="GjtStudentEnteringSchool.findAll", query="SELECT g FROM GjtStudentEnteringSchool g")
@Deprecated public class BzrGjtStudentEnteringSchool implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 学员ID
	 */
	@Id
	@Column(name="STUDENT_ID")
	private String studentId;

	/**
	 * 联系确认时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTERING_DT")
	private Date enteringDt;

	/**
	 * 联系方式，1-电话 2-QQ
	 */
	@Column(name="CONTACT")
	private Integer contact;

	/**
	 * 联系号码，依赖联系方式
	 */
	@Column(name="Contact_NUMBER")
	private String contactNumber;

	/**
	 * 是否发短信，默认0 0-否 1-是
	 */
	@Column(name="IS_SEND_SMS")
	private Integer isSendSms;

	/**
	 * 是否安装APP，默认0 0-否 1-是
	 */
	@Column(name="IS_INSTALL_APP")
	private Integer isInstallApp;

	/**
	 * 是否加入班级群，默认0 0-否 1-是
	 */
	@Column(name="IS_JOIN_CLASS_EE")
	private Integer isJoinClassEe;

	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	public BzrGjtStudentEnteringSchool() {
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public Date getEnteringDt() {
		return enteringDt;
	}

	public void setEnteringDt(Date enteringDt) {
		this.enteringDt = enteringDt;
	}

	public Integer getContact() {
		return contact;
	}

	public void setContact(Integer contact) {
		this.contact = contact;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Integer getIsSendSms() {
		return isSendSms;
	}

	public void setIsSendSms(Integer isSendSms) {
		this.isSendSms = isSendSms;
	}

	public Integer getIsInstallApp() {
		return isInstallApp;
	}

	public void setIsInstallApp(Integer isInstallApp) {
		this.isInstallApp = isInstallApp;
	}

	public Integer getIsJoinClassEe() {
		return isJoinClassEe;
	}

	public void setIsJoinClassEe(Integer isJoinClassEe) {
		this.isJoinClassEe = isJoinClassEe;
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
}