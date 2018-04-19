package com.gzedu.xlims.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="GJT_GRANT_COURSE_CERTIFICATE")
public class GjtGrantCourseCertificate {
	
	@Id
	@Column(name="GRANT_COURSE_CERTIFICATE_ID")
	private String grantCourseCertificateId;
	// 姓名
	@Column(name="STUDENT_NAME")
	private String studentName;
	
	// 学号
	@Column(name="STUDENT_NO")
	private String studentNo;
	// 签到状态(1:已签到，0：未签到 )
	@Column(name="SING_STATUS")
	private int signStatus = 0;
	
	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false,updatable = false)
	private Date createdDt;
	
	@Column(name = "UPDATED_BY", insertable = false) // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;
	
	@Column(name = "IS_DELETED", insertable = false) // 是否删除
	private String isDeleted;
	
	
	public GjtGrantCourseCertificate() {
		super();
	}
	public GjtGrantCourseCertificate(String studentName, String studentNo, int signStatus) {
		super();
		this.studentName = studentName;
		this.studentNo = studentNo;
		this.signStatus = signStatus;
		this.createdDt = new Date();
	}
	public String getGrantCourseCertificateId() {
		return grantCourseCertificateId;
	}
	public void setGrantCourseCertificateId(String grantCourseCertificateId) {
		this.grantCourseCertificateId = grantCourseCertificateId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentNo() {
		return studentNo;
	}
	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}
	public int getSignStatus() {
		return signStatus;
	}
	public void setSignStatus(int signStatus) {
		this.signStatus = signStatus;
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
	public String getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
}
