package com.gzedu.xlims.pojo.mobileMessage;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gzedu.xlims.pojo.GjtUserAccount;

/**
 * The persistent class for the GJT_MOBLIE_MESAAGE_AUDIT database table.
 * 
 */
@Entity
@Table(name = "GJT_MOBLIE_MESSAGE_AUDIT")
@NamedQuery(name = "GjtMoblieMessageAudit.findAll", query = "SELECT g FROM GjtMoblieMessageAudit g")
public class GjtMoblieMessageAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "AUDIT_CONTENT")
	private String auditContent;

	@Column(name = "AUDIT_PERSON")
	private String auditPerson;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AUDIT_PERSON", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtUserAccount auditUser;

	@Column(name = "SUBMIT_PERSION")
	private String submitPersion;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBMIT_PERSION", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtUserAccount submitUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "AUDIT_ROLE_NAME")
	private String auditRoleName;

	@Column(name = "AUDIT_STATUS")
	private String auditStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "AUDIT_TIME")
	private Date auditTime;

	@Column(name = "AUDIT_USER_NAME")
	private String auditUserName;

	@Column(name = "MOBILE_MESSAGE_ID")
	private String mobileMessageId;

	@Column(name = "SUBMIT_USER_NAME")
	private String submitUserName;

	@Column(name = "SUBMIT_ROLE_NAME")
	private String submitRoleName;

	public GjtMoblieMessageAudit() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuditContent() {
		return this.auditContent;
	}

	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}

	public String getAuditPerson() {
		return this.auditPerson;
	}

	public void setAuditPerson(String auditPerson) {
		this.auditPerson = auditPerson;
	}

	public String getAuditRoleName() {
		return this.auditRoleName;
	}

	public void setAuditRoleName(String auditRoleName) {
		this.auditRoleName = auditRoleName;
	}

	public String getAuditStatus() {
		return this.auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Date getAuditTime() {
		return this.auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditUserName() {
		return this.auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	public String getMobileMessageId() {
		return this.mobileMessageId;
	}

	public void setMobileMessageId(String mobileMessageId) {
		this.mobileMessageId = mobileMessageId;
	}

	public GjtUserAccount getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(GjtUserAccount auditUser) {
		this.auditUser = auditUser;
	}

	public String getSubmitPersion() {
		return submitPersion;
	}

	public void setSubmitPersion(String submitPersion) {
		this.submitPersion = submitPersion;
	}

	public GjtUserAccount getSubmitUser() {
		return submitUser;
	}

	public void setSubmitUser(GjtUserAccount submitUser) {
		this.submitUser = submitUser;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getSubmitUserName() {
		return submitUserName;
	}

	public void setSubmitUserName(String submitUserName) {
		this.submitUserName = submitUserName;
	}

	public String getSubmitRoleName() {
		return submitRoleName;
	}

	public void setSubmitRoleName(String submitRoleName) {
		this.submitRoleName = submitRoleName;
	}

}