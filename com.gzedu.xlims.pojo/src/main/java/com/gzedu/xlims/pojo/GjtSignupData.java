package com.gzedu.xlims.pojo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the GJT_SIGNUP_DATA database table.
 * 
 */
@Entity
@Table(name="GJT_SIGNUP_DATA")
@NamedQuery(name="GjtSignupData.findAll", query="SELECT g FROM GjtSignupData g")
public class GjtSignupData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="AUDIT_DATE")
	private Date auditDate;

	@Column(name="AUDIT_OPINION")
	private String auditOpinion;

	@Column(name="AUDIT_STATE")
	private String auditState;

	private String auditor;

	@Column(name="CONTENT_TYPE")
	private String contentType;

	@Column(name="FILE_TYPE")
	private String fileType;

	@Column(name="ID_NO")
	private String idNo;

	@Column(name="SIGNUP_ID")
	private String signupId;

	private String source;

	@Column(name="STUDENT_ID")
	private String studentId;

	private String url;

	@Column(name="URL_NEW")
	private String urlNew;

	public GjtSignupData() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getAuditDate() {
		return this.auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public String getAuditOpinion() {
		return this.auditOpinion;
	}

	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}

	public String getAuditState() {
		return this.auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public String getAuditor() {
		return this.auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getIdNo() {
		return this.idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getSignupId() {
		return this.signupId;
	}

	public void setSignupId(String signupId) {
		this.signupId = signupId;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlNew() {
		return urlNew;
	}

	public void setUrlNew(String urlNew) {
		this.urlNew = urlNew;
	}
}