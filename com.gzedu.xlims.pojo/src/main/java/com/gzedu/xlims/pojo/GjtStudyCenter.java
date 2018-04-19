package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

/**
 * The persistent class for the GJT_STUDY_CENTER database table. 学习中心表
 */
@Entity
@Table(name = "GJT_STUDY_CENTER")
@NamedQuery(name = "GjtStudyCenter.findAll", query = "SELECT g FROM GjtStudyCenter g")
public class GjtStudyCenter implements Serializable {
	private static final long serialVersionUID = 1L;

	public GjtStudyCenter(String id) {
		super();
		this.id = id;
	}

	@Id
	@Column(name = "ID") // 创建人
	private String id;

	@OneToMany(mappedBy = "gjtStudyCenter")
	private List<GjtStudentInfo> gjtStudentInfos;// 学习中心的学员

	@OneToOne(optional = false, mappedBy = "gjtStudyCenter", cascade = CascadeType.ALL) // 级联操作
	@JoinColumn(name = "id")
	@PrimaryKeyJoinColumn
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtOrg gjtOrg;// 院校

	@Transient
	private int gjtStudentInfosSize;// 学习中心的学院总数

	@Column(name = "AUDIT_OPINION") // 审核意见
	private String auditOpinion;

	@Column(name = "AUDIT_STATUS") // 审核状态：新建，审批通过，不通过
	private String auditStatus;

	@Column(name = "CREATED_BY") // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建日期
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "SERVICE_AREA") // 服务类型 数据字典：StudyServiceInfo
	private String serviceArea;

	@Column(name = "COMPACT_NO") // 合同编号
	private String compactNo;

	// 学习中心类型：1：学习中心、2：招生服务站（2017年3月28日18:37:19新增）
	@Column(name = "CENTER_TYPE")
	private String centerType;// 不晓得谁加的，没用的；在gjt_org有个org_type才是类型

	@Column(name = "DISTRICT")
	private String district;// 区

	@Transient
	private String districtName;// 全地址名称（不需要字段）

	@Column(name = "IS_DELETED") // 是否删除
	@Where(clause = "isDeleted='N'")
	private String isDeleted;

	@Column(name = "IS_ENABLED") // 是否启用
	private String isEnabled;

	@Column(name = "IS_VALID") // 是否有效
	private Double isValid;

	private String leader;// 负责人

	@Column(name = "LINK_TEL") // 联系电话
	private String linkTel;

	private String linkman;// 联系人

	private String memo;// 备注

	@Column(name = "OFFICE_ADDR") // 办公地址
	private String officeAddr;

	@Column(name = "OFFICE_TEL") // 办公电话
	private String officeTel;

	@Column(name = "SC_CODE") // 学习中心编号
	private String scCode;

	@Column(name = "SC_NAME") // 学习中心名称
	private String scName;

	@Column(name = "UPDATED_BY") // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION")
	private BigDecimal version;// 版本号

	public GjtStudyCenter() {
	}

	public String getCompactNo() {
		return compactNo;
	}

	public void setCompactNo(String compactNo) {
		this.compactNo = compactNo;
	}

	public GjtOrg getGjtOrg() {
		return gjtOrg;
	}

	public void setGjtOrg(GjtOrg gjtOrg) {
		this.gjtOrg = gjtOrg;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuditOpinion() {
		return this.auditOpinion;
	}

	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}

	public String getAuditStatus() {
		return this.auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
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

	public String getDistrict() {
		return this.district;
	}

	public void setDistrict(String district) {
		this.district = district;
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

	public String getLeader() {
		return this.leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String getLinkTel() {
		return this.linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public String getLinkman() {
		return this.linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOfficeAddr() {
		return this.officeAddr;
	}

	public void setOfficeAddr(String officeAddr) {
		this.officeAddr = officeAddr;
	}

	public String getOfficeTel() {
		return this.officeTel;
	}

	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}

	public String getScCode() {
		return this.scCode;
	}

	public void setScCode(String scCode) {
		this.scCode = scCode;
	}

	public String getScName() {
		return this.scName;
	}

	public void setScName(String scName) {
		this.scName = scName;
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

	public Double getIsValid() {
		return isValid;
	}

	public void setIsValid(Double isValid) {
		this.isValid = isValid;
	}

	public BigDecimal getVersion() {
		return version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public List<GjtStudentInfo> getGjtStudentInfos() {
		return gjtStudentInfos;
	}

	public void setGjtStudentInfos(List<GjtStudentInfo> gjtStudentInfos) {
		this.gjtStudentInfos = gjtStudentInfos;
	}

	public int getGjtStudentInfosSize() {
		return gjtStudentInfosSize;
	}

	public void setGjtStudentInfosSize(int gjtStudentInfosSize) {
		this.gjtStudentInfosSize = gjtStudentInfosSize;
	}

	public String getServiceArea() {
		return serviceArea;
	}

	public void setServiceArea(String serviceArea) {
		this.serviceArea = serviceArea;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getCenterType() {
		return centerType;
	}

	public void setCenterType(String centerType) {
		this.centerType = centerType;
	}

}