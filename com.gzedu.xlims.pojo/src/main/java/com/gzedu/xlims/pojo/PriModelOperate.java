package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.gzedu.xlims.common.UUIDUtils;

/**
 * The persistent class for the PRI_MODEL_OPERATE database table.
 * 
 */
@Entity
@Table(name = "PRI_MODEL_OPERATE")
@NamedQuery(name = "PriModelOperate.findAll", query = "SELECT p FROM PriModelOperate p")
public class PriModelOperate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "xlimsSeq")
	//@SequenceGenerator(initialValue = 1, name = "xlimsSeq", sequenceName = "XLIMS_SEQUENCE")
	// @GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name = "MODEL_OPERATE_ID")
	private String modelOperateId;

	@ManyToOne
	@JoinColumn(name = "MODEL_ID")
	private PriModelInfo priModelInfo;

	@Column(name = "MODEL_ID", insertable = false, updatable = false)
	private String modelId;

	@ManyToOne
	@JoinColumn(name = "OPERATE_ID")
	private PriOperateInfo priOperateInfo;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	private String isdeleted;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	public PriModelOperate() {
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

	public String getIsdeleted() {
		return this.isdeleted;
	}

	public void setIsdeleted(String isdeleted) {
		this.isdeleted = isdeleted;
	}

	public String getModelOperateId() {
		return this.modelOperateId;
	}

	public void setModelOperateId(String modelOperateId) {
		this.modelOperateId = UUIDUtils.random();
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

	public PriModelInfo getPriModelInfo() {
		return priModelInfo;
	}

	public void setPriModelInfo(PriModelInfo priModelInfo) {
		this.priModelInfo = priModelInfo;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public PriOperateInfo getPriOperateInfo() {
		return priOperateInfo;
	}

	public void setPriOperateInfo(PriOperateInfo priOperateInfo) {
		this.priOperateInfo = priOperateInfo;
	}

}