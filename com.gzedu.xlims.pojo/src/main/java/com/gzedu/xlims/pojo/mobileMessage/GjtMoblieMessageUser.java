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

import com.gzedu.xlims.pojo.GjtUserAccount;

/**
 * The persistent class for the GJT_MOBLIE_MESAAGE_USER database table.
 * 
 */
@Entity
@Table(name = "GJT_MOBLIE_MESSAGE_USER")
@NamedQuery(name = "GjtMoblieMessageUser.findAll", query = "SELECT g FROM GjtMoblieMessageUser g")
public class GjtMoblieMessageUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SEND_TIME")
	private Date sendTime;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "MOBILE_MESSAGE_ID")
	private String mobileMessageId;

	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "SEND_STAUTS")
	private String sendStauts;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MOBILE_MESSAGE_ID", insertable = false, updatable = false)
	private GjtMobileMessage gjtMobileMessage;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", insertable = false, updatable = false)
	private GjtUserAccount gjtUserAccount;

	public GjtMoblieMessageUser() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getMobileMessageId() {
		return this.mobileMessageId;
	}

	public void setMobileMessageId(String mobileMessageId) {
		this.mobileMessageId = mobileMessageId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public GjtMobileMessage getGjtMobileMessage() {
		return gjtMobileMessage;
	}

	public void setGjtMobileMessage(GjtMobileMessage gjtMobileMessage) {
		this.gjtMobileMessage = gjtMobileMessage;
	}

	public GjtUserAccount getGjtUserAccount() {
		return gjtUserAccount;
	}

	public void setGjtUserAccount(GjtUserAccount gjtUserAccount) {
		this.gjtUserAccount = gjtUserAccount;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendStauts() {
		return sendStauts;
	}

	public void setSendStauts(String sendStauts) {
		this.sendStauts = sendStauts;
	}

}