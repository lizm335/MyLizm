package com.gzedu.xlims.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the GJT_PAGE_DEF database table.
 * 
 */
@Entity
@Table(name = "GJT_PAGE_DEF")
@NamedQuery(name = "GjtPageDef.findAll", query = "SELECT g FROM GjtPageDef g")
public class GjtPageDef implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "APP_KEY")
	private String appKey;

	@Column(name = "APP_NAME")
	private String appName;

	@Lob
	private String content;

	@Column(name = "POS_KEY")
	private String posKey;

	@Column(name = "POS_NAME")
	private String posName;

	@Column(name = "XX_ID")
	private String xxId;
	@Transient
	private String orgName;

	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param orgName
	 *            the orgName to set
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public GjtPageDef() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppKey() {
		return this.appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppName() {
		return this.appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPosKey() {
		return this.posKey;
	}

	public void setPosKey(String posKey) {
		this.posKey = posKey;
	}

	public String getPosName() {
		return this.posName;
	}

	public void setPosName(String posName) {
		this.posName = posName;
	}

	public String getXxId() {
		return this.xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

}