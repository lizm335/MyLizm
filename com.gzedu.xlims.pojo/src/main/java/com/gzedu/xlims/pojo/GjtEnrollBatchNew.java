package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * The persistent class for the GJT_ENROLL_BATCH_NEW database table. 招生批次表(新)
 */
@Entity
@Table(name = "GJT_ENROLL_BATCH_NEW")
@NamedQuery(name = "GjtEnrollBatchNew.findAll", query = "SELECT g FROM GjtEnrollBatchNew g")
public class GjtEnrollBatchNew implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ENROLL_BATCH_ID")
	private String enrollBatchId;// 招生批次ID

	@OneToMany
	@JoinColumn(name = "ENROLL_BATCH_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<GjtSignup> gjtSignup;// 用户报名、新生注册对象

	@OneToMany
	@JoinColumn(name = "ENROLL_BATCH_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<GjtStudyEnrollNum> gjtStudyEnrollNums;// 学习中心招生人数对象

	@Column(name = "CREATED_BY")
	private String createdBy;// 创建人

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;// 创建时间

	@Column(name = "ENROLL_BATCH_NAME")
	private String enrollBatchName;// 招生批次名称

	@Column(name = "ENROLL_DOWN_NUM")
	private Integer enrollDownNum;// 招生下限人数

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "ENROLL_EDATE")
	private Date enrollEdate;// 招生结束时间

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "ENROLL_SDATE")
	private Date enrollSdate;// 招生开始时间

	@Column(name = "ENROLL_TOTAL_NUM")
	private Integer enrollTotalNum;// 招生总指标

	@Column(name = "IS_DELETED")
	private String isDeleted;// 是否删除

	private String memo;// 备注

	@Column(name = "ORG_ID")
	private String orgId;

	@ManyToOne
	@JoinColumn(name = "ORG_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtOrg gjtOrg;

	private String pycc;

	// @Column(name="SPECIALTY_ID")
	// private String specialtyId;//专业ID

	@OneToOne
	@JoinColumn(name = "SPECIALTY_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSpecialty gjtSpecialty;

	private String status;

	@Column(name = "STUDY_YEAR_CODE")
	private BigDecimal studyYearCode;

	@Column(name = "STUDYYEAR_ID")
	private String studyYearId;// 学年度ID

	@ManyToOne
	@JoinColumn(name = "STUDYYEAR_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtStudyYearInfo gjtstudyYearInfo;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	private String url;// 文件地址

	@Column(name = "FILE_NAME")
	private String fileName;// 文件名称

	@Column(name = "XXZX_ID")
	private String xxzxId;

	public GjtEnrollBatchNew() {
	}

	public String getEnrollBatchId() {
		return this.enrollBatchId;
	}

	public void setEnrollBatchId(String enrollBatchId) {
		this.enrollBatchId = enrollBatchId;
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

	public String getEnrollBatchName() {
		return this.enrollBatchName;
	}

	public void setEnrollBatchName(String enrollBatchName) {
		this.enrollBatchName = enrollBatchName;
	}

	public Integer getEnrollDownNum() {
		return this.enrollDownNum;
	}

	public void setEnrollDownNum(Integer enrollDownNum) {
		this.enrollDownNum = enrollDownNum;
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

	public Integer getEnrollTotalNum() {
		return this.enrollTotalNum;
	}

	public void setEnrollTotalNum(Integer enrollTotalNum) {
		this.enrollTotalNum = enrollTotalNum;
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

	public String getOrgId() {
		return this.orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPycc() {
		return this.pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	// public String getSpecialtyId() {
	// return this.specialtyId;
	// }
	//
	// public void setSpecialtyId(String specialtyId) {
	// this.specialtyId = specialtyId;
	// }

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getStudyYearCode() {
		return this.studyYearCode;
	}

	public void setStudyYearCode(BigDecimal studyYearCode) {
		this.studyYearCode = studyYearCode;
	}

	public String getStudyYearId() {
		return studyYearId;
	}

	public void setStudyYearId(String studyYearId) {
		this.studyYearId = studyYearId;
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

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getXxzxId() {
		return this.xxzxId;
	}

	public void setXxzxId(String xxzxId) {
		this.xxzxId = xxzxId;
	}

	public GjtOrg getGjtOrg() {
		return gjtOrg;
	}

	public void setGjtOrg(GjtOrg gjtOrg) {
		this.gjtOrg = gjtOrg;
	}

	public GjtSpecialty getGjtSpecialty() {
		return gjtSpecialty;
	}

	public void setGjtSpecialty(GjtSpecialty gjtSpecialty) {
		this.gjtSpecialty = gjtSpecialty;
	}

	public GjtStudyYearInfo getGjtstudyYearInfo() {
		return gjtstudyYearInfo;
	}

	public void setGjtstudyYearInfo(GjtStudyYearInfo gjtstudyYearInfo) {
		this.gjtstudyYearInfo = gjtstudyYearInfo;
	}

	public List<GjtStudyEnrollNum> getGjtStudyEnrollNums() {
		return gjtStudyEnrollNums;
	}

	public void setGjtStudyEnrollNums(List<GjtStudyEnrollNum> gjtStudyEnrollNums) {
		this.gjtStudyEnrollNums = gjtStudyEnrollNums;
	}

	public List<GjtSignup> getGjtSignup() {
		return gjtSignup;
	}

	public void setGjtSignup(List<GjtSignup> gjtSignup) {
		this.gjtSignup = gjtSignup;
	}
}