package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the GJT_ENROLL_BATCH database table.招生批次
 * 
 */
@Entity
@Table(name = "GJT_ENROLL_BATCH")
@NamedQuery(name = "GjtEnrollBatch.findAll", query = "SELECT g FROM GjtEnrollBatch g")
public class GjtEnrollBatch implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ENROLL_BATCH_ID")
	private String enrollBatchId;

	@OneToOne
	@JoinColumn(name = "GRADE_ID") // 年级
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtGrade gjtGrade;

	@Column(name = "BATCH_CODE") // 批次编号
	private String batchCode;

	@Column(name = "BATCH_NAME") // 批次名称
	private String batchName;

	@Column(name = "DOWN_LIMIT") // 招生人数下限
	private BigDecimal downLimit;

	@Column(name = "UP_LIMIT") // 招生人数上限
	private BigDecimal upLimit;

	@Temporal(TemporalType.DATE)
	@Column(name = "BATCH_PUBLISH_DATE") // 批次发布日期
	private Date batchPublishDate;

	@Column(name = "CREATED_BY") // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Temporal(TemporalType.DATE) // 报名截至时间
	@Column(name = "ENROLL_EDATE")
	private Date enrollEdate;

	@Temporal(TemporalType.DATE) // 报名开始时间
	@Column(name = "ENROLL_SDATE")
	private Date enrollSdate;

	@Column(name = "IS_DELETED") // 是否删除
	private String isDeleted;

	@Column(name = "IS_ENABLED") // 是否启用
	private String isEnabled;

	@Column(name = "MANAGE_MODE") // 办学模式
	private String manageMode;

	private String memo;// 备注

	@Column(name = "ORG_CODE") // 机构编码
	private String orgCode;

	@Column(name = "ORG_ID") // 机构
	private String orgId;

	@Column(name = "TERM_ID") // 学期
	private String termId;

	@Column(name = "UPDATED_BY") // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;// 版本

	@OneToOne // 学院信息
	@JoinColumn(name = "XX_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSchoolInfo gjtSchoolInfo;

	@Column(name = "XXZX_ID") // 学习中间
	private String xxzxId;

	@Column(name = "YX_ID") // 院系Id
	private String yxId;

	public GjtEnrollBatch() {
	}

	public String getEnrollBatchId() {
		return this.enrollBatchId;
	}

	public void setEnrollBatchId(String enrollBatchId) {
		this.enrollBatchId = enrollBatchId;
	}

	public String getBatchCode() {
		return this.batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getBatchName() {
		return this.batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public Date getBatchPublishDate() {
		return this.batchPublishDate;
	}

	public void setBatchPublishDate(Date batchPublishDate) {
		this.batchPublishDate = batchPublishDate;
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

	public Date getEnrollEdate() {
		return this.enrollEdate;
	}

	public void setEnrollEdate(Date enrollEdate) {
		this.enrollEdate = enrollEdate;
	}

	public Date getEnrollSdate() {
		return this.enrollSdate;
	}

	public void setEnrollSdate(Date enrollSdate) {
		this.enrollSdate = enrollSdate;
	}

	/**
	 * @return the gjtGrade
	 */
	public GjtGrade getGjtGrade() {
		return gjtGrade;
	}

	/**
	 * @param gjtGrade
	 *            the gjtGrade to set
	 */
	public void setGjtGrade(GjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getManageMode() {
		return this.manageMode;
	}

	public void setManageMode(String manageMode) {
		this.manageMode = manageMode;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getTermId() {
		return this.termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
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

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	/**
	 * @return the gjtSchoolInfo
	 */
	public GjtSchoolInfo getGjtSchoolInfo() {
		return gjtSchoolInfo;
	}

	/**
	 * @param gjtSchoolInfo
	 *            the gjtSchoolInfo to set
	 */
	public void setGjtSchoolInfo(GjtSchoolInfo gjtSchoolInfo) {
		this.gjtSchoolInfo = gjtSchoolInfo;
	}

	public String getXxzxId() {
		return this.xxzxId;
	}

	public void setXxzxId(String xxzxId) {
		this.xxzxId = xxzxId;
	}

	public String getYxId() {
		return this.yxId;
	}

	public void setYxId(String yxId) {
		this.yxId = yxId;
	}

	/**
	 * @return the downLimit
	 */
	public BigDecimal getDownLimit() {
		return downLimit;
	}

	/**
	 * @param downLimit
	 *            the downLimit to set
	 */
	public void setDownLimit(BigDecimal downLimit) {
		this.downLimit = downLimit;
	}

	/**
	 * @return the upLimit
	 */
	public BigDecimal getUpLimit() {
		return upLimit;
	}

	/**
	 * @param upLimit
	 *            the upLimit to set
	 */
	public void setUpLimit(BigDecimal upLimit) {
		this.upLimit = upLimit;
	}

}