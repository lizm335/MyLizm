package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 答疑短信发送记录表
 */
@Entity
@Table(name = "GJT_MESSAGE_RESULT")
@NamedQuery(name = "GjtMessageResult.findAll", query = "SELECT g FROM GjtMessageResult g")
public class GjtMessageResult implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "INCIDENT_ID") // 事件Id
	private String incidentId;

	@Column(name = "CREATED_USER") // 发送人名
	private String createdUser;

	@Column(name = "RECEIVE_USER") // 接收者人名
	private String receiveUser;

	@Column(name = "RECEIVE_ID") // 接收者ID
	private String receiveId;

	@Column(name = "CREATED_BY") // 创建人ID
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT")
	private Date createdDt;

	public GjtMessageResult() {
		super();
	}

	public String getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIncidentId() {
		return incidentId;
	}

	public void setIncidentId(String incidentId) {
		this.incidentId = incidentId;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(String receiveUser) {
		this.receiveUser = receiveUser;
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