package com.gzedu.xlims.pojo;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gzedu.xlims.pojo.GjtOrg;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the GJT_STUDY_ENROLL_NUM database table.
 * 学习中心招生人数表
 */
@Entity
@Table(name="GJT_STUDY_ENROLL_NUM")
@NamedQuery(name="GjtStudyEnrollNum.findAll", query="SELECT g FROM GjtStudyEnrollNum g")
public class GjtStudyEnrollNum implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;
	
	@Column(name="ENROLL_BATCH_ID")
	private String enrollBatchId;
	
//	@OneToOne
//	@JoinColumn(name = "ENROLL_BATCH_ID")
//	@NotFound(action = NotFoundAction.IGNORE)
//	private GjtEnrollBatchNew gjtEnrollBatchNew;

	@Column(name="ENROLL_KPI_NUM")
	private String enrollKpiNum;

	@Column(name="IS_DELETED")
	private String isDeleted;

	private String memo;

	@Column(name="STUDYYEAR_ID")
	private String studyyearId;

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@Column(name="XXZX_ID")
	private String xxzxId;

	public GjtStudyEnrollNum() {
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

	public String getEnrollKpiNum() {
		return this.enrollKpiNum;
	}

	public void setEnrollKpiNum(String enrollKpiNum) {
		this.enrollKpiNum = enrollKpiNum;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getStudyyearId() {
		return this.studyyearId;
	}

	public void setStudyyearId(String studyyearId) {
		this.studyyearId = studyyearId;
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

	public String getXxzxId() {
		return this.xxzxId;
	}

	public void setXxzxId(String xxzxId) {
		this.xxzxId = xxzxId;
	}

	public String getEnrollBatchId() {
		return enrollBatchId;
	}

	public void setEnrollBatchId(String enrollBatchId) {
		this.enrollBatchId = enrollBatchId;
	}
}