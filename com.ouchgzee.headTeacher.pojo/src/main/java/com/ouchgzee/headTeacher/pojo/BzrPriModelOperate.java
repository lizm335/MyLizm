package com.ouchgzee.headTeacher.pojo;

import com.gzedu.xlims.common.UUIDUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the PRI_MODEL_OPERATE database table.
 * 
 */
@Entity
@Table(name = "PRI_MODEL_OPERATE")
// @NamedQuery(name = "PriModelOperate.findAll", query = "SELECT p FROM PriModelOperate p")
@Deprecated public class BzrPriModelOperate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "xlimsSeq")
	@SequenceGenerator(initialValue = 1, name = "xlimsSeq", sequenceName = "XLIMS_SEQUENCE")
	// @GenericGenerator(name = "system-uuid", strategy = "uuid2")
	@Column(name = "MODEL_OPERATE_ID")
	private String modelOperateId;

	@ManyToOne
	@JoinColumn(name = "MODEL_ID")
	private BzrPriModelInfo priModelInfo;

	@ManyToOne
	@JoinColumn(name = "OPERATE_ID")
	private BzrPriOperateInfo priOperateInfo;

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

	public BzrPriModelOperate() {
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

	public BzrPriModelInfo getPriModelInfo() {
		return priModelInfo;
	}

	public void setPriModelInfo(BzrPriModelInfo priModelInfo) {
		this.priModelInfo = priModelInfo;
	}

	public BzrPriOperateInfo getPriOperateInfo() {
		return priOperateInfo;
	}

	public void setPriOperateInfo(BzrPriOperateInfo priOperateInfo) {
		this.priOperateInfo = priOperateInfo;
	}

}