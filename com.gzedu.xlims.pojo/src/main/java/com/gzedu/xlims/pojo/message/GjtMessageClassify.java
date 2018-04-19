package com.gzedu.xlims.pojo.message;

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
 * The persistent class for the GJT_MESSAGE_CLASSIFY database table.
 * 
 */
@Entity
@Table(name = "GJT_MESSAGE_CLASSIFY")
@NamedQuery(name = "GjtMessageClassify.findAll", query = "SELECT g FROM GjtMessageClassify g")
public class GjtMessageClassify implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String code;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "INFO_TYPE")
	private String infoType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	private String name;

	public GjtMessageClassify() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

}