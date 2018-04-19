package com.gzedu.xlims.pojo.mobileMessage;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gzedu.xlims.pojo.GjtUserAccount;

/**
 * The persistent class for the GJT_MOBILE_MESSAGE database table.
 * 
 */
@Entity
@Table(name = "GJT_MOBILE_MESSAGE")
@NamedQuery(name = "GjtMobileMessage.findAll", query = "SELECT g FROM GjtMobileMessage g")
public class GjtMobileMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "AUDIT_STATUS")
	private String auditStatus;// 0-待审核，1 审核通过，2审核不通过

	private String content;

	@Column(name = "CREATE_USERNAME")
	private String createUsername;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@OneToOne
	@JoinColumn(name = "CREATED_BY", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtUserAccount gjtUserAccount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SEND_TIME")
	private Date sendTime;

	@Column(name = "IS_APPOINT")
	private String isAppoint;

	@Column(name = "CREATE_ROLE_NAME")
	private String createRoleName;

	private String signature;

	private String type;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "MOBILE_MESSAGE_ID")
	private List<GjtMoblieMessageSearch> searchList;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "MOBILE_MESSAGE_ID")
	@OrderBy("createdDt asc")
	private List<GjtMoblieMessageAudit> auditList;

	@Transient
	private long sendCount;

	@Transient
	private long sendSuccessCount;

	@Transient
	private long sendFailedCount;

	public GjtMobileMessage() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuditStatus() {
		return this.auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateUsername() {
		return this.createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
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

	public String getIsAppoint() {
		return this.isAppoint;
	}

	public void setIsAppoint(String isAppoint) {
		this.isAppoint = isAppoint;
	}

	public String getSignature() {
		return this.signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public GjtUserAccount getGjtUserAccount() {
		return gjtUserAccount;
	}

	public void setGjtUserAccount(GjtUserAccount gjtUserAccount) {
		this.gjtUserAccount = gjtUserAccount;
	}

	public List<GjtMoblieMessageSearch> getSearchList() {
		return searchList;
	}

	public void setSearchList(List<GjtMoblieMessageSearch> searchList) {
		this.searchList = searchList;
	}

	public List<GjtMoblieMessageAudit> getAuditList() {
		return auditList;
	}

	public void setAuditList(List<GjtMoblieMessageAudit> auditList) {
		this.auditList = auditList;
	}

	public String getCreateRoleName() {
		return createRoleName;
	}

	public void setCreateRoleName(String createRoleName) {
		this.createRoleName = createRoleName;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public long getSendCount() {
		return sendCount;
	}

	public void setSendCount(long sendCount) {
		this.sendCount = sendCount;
	}

	public long getSendSuccessCount() {
		return sendSuccessCount;
	}

	public void setSendSuccessCount(long sendSuccessCount) {
		this.sendSuccessCount = sendSuccessCount;
	}

	public long getSendFailedCount() {
		return sendFailedCount;
	}

	public void setSendFailedCount(long sendFailedCount) {
		this.sendFailedCount = sendFailedCount;
	}

}