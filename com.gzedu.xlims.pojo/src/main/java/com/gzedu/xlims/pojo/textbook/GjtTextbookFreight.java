package com.gzedu.xlims.pojo.textbook;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the GJT_TEXTBOOK_FREIGHT database table.
 * 
 */
@Entity
@Table(name="GJT_TEXTBOOK_FREIGHT")
@NamedQuery(name="GjtTextbookFreight.findAll", query="SELECT g FROM GjtTextbookFreight g")
public class GjtTextbookFreight implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="FREIGHT_ID")
	private String freightId;

	@Column(name="DISTRICT_ID")
	private String districtId;

	@Column(name="DISTRICT_PID")
	private String districtPid;

	@Column(name="FREIGHT_RATE")
	private BigDecimal freightRate;

	private String name;

	public GjtTextbookFreight() {
	}

	public String getFreightId() {
		return this.freightId;
	}

	public void setFreightId(String freightId) {
		this.freightId = freightId;
	}

	public String getDistrictId() {
		return this.districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getDistrictPid() {
		return this.districtPid;
	}

	public void setDistrictPid(String districtPid) {
		this.districtPid = districtPid;
	}

	public BigDecimal getFreightRate() {
		return this.freightRate;
	}

	public void setFreightRate(BigDecimal freightRate) {
		this.freightRate = freightRate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}