package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the GJT_MESSAGE_USER database table.
 * 
 */
@Entity
@Table(name = "GJT_MESSAGE_USER")
// @NamedQuery(name = "GjtMessageUser.findAll", query = "SELECT g FROM GjtMessageUser g")
@Deprecated public class BzrGjtMessageUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "IS_ENABLED") // 是否阅读0否1已阅
	private String isEnabled;

	@OneToOne
	@JoinColumn(name = "MESSAGE_ID", insertable = false, updatable = false)
	private BzrGjtMessageInfo gjtMessageInfo;

	@Column(name = "MESSAGE_ID")
	private String messageId;// 插入的时候，不关联查询

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@OneToOne
	@JoinColumn(name = "USER_ID", insertable = false, updatable = false)
	private BzrGjtUserAccount gjtUserAccount;

	@Column(name = "USER_ID")
	private String userId;// 插入的时候，不关联查询

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	private BigDecimal version;

	public BzrGjtMessageUser() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
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

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public BzrGjtMessageInfo getGjtMessageInfo() {
		return gjtMessageInfo;
	}

	public void setGjtMessageInfo(BzrGjtMessageInfo gjtMessageInfo) {
		this.gjtMessageInfo = gjtMessageInfo;
	}

	public BzrGjtUserAccount getGjtUserAccount() {
		return gjtUserAccount;
	}

	public void setGjtUserAccount(BzrGjtUserAccount gjtUserAccount) {
		this.gjtUserAccount = gjtUserAccount;
	}

}