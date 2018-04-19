package com.gzedu.xlims.pojo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 全国区域信息实体类<br>
 * The persistent class for the GjtDistrict database table.
 *
 */
@Entity
@Table(name="GJT_DISTRICT")
@NamedQuery(name="GjtDistrict.findAll", query="SELECT g FROM GjtDistrict g")
public class GjtDistrict implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String code;

	private String name;

	private String pid;

	private String py;

	private String zip;

	public GjtDistrict() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
}