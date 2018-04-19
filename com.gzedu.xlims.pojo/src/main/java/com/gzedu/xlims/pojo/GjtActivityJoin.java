package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the GJT_ACTIVITY_JOIN database table.
 * 
 */
@Entity
@Table(name = "GJT_ACTIVITY_JOIN")
@NamedQuery(name = "GjtActivityJoin.findAll", query = "SELECT g FROM GjtActivityJoin g")
public class GjtActivityJoin implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private GjtActivityJoinPK id;

	@Column(name = "AUDIT_STATUS")
	private BigDecimal auditStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "JOIN_DT")
	private Date joinDt;

	public GjtActivityJoin() {
	}

	public GjtActivityJoinPK getId() {
		return this.id;
	}

	public void setId(GjtActivityJoinPK id) {
		this.id = id;
	}

	public BigDecimal getAuditStatus() {
		return this.auditStatus;
	}

	public void setAuditStatus(BigDecimal auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Date getJoinDt() {
		return this.joinDt;
	}

	public void setJoinDt(Date joinDt) {
		this.joinDt = joinDt;
	}

}