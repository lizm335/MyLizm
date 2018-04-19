package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

/**
 * 学院信息实体类<br>
 * The persistent class for the GJT_ORG database table.学院表
 *
 */
@Entity
@Table(name = "GJT_ORG")
// @NamedQuery(name = "GjtOrg.findAll", query = "SELECT g FROM GjtOrg g")
@Deprecated public class BzrGjtOrg implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public BzrGjtOrg(String id) {
		super();
		this.id = id;
	}
	
	@Id
	private String id;// id

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtSchoolInfo gjtSchoolInfo;// 院校基本信息

	@OneToMany
	@JoinColumn(name = "ORG_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<BzrGjtUserAccount> gjtUserAccounts;// 院校所属用户

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "PERENT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	BzrGjtOrg parentGjtOrg;// 父

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "PERENT_ID")
	@Where(clause = "is_Deleted='N'")
	@NotFound(action = NotFoundAction.IGNORE)
	List<BzrGjtOrg> childGjtOrgs;// 子列表

	@OneToMany
	@JoinColumn(name = "EMPLOYEE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<BzrGjtEmployeeInfo> gjtEmployeeInfos;

	@OneToMany
	@JoinColumn(name = "STUDENT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<BzrGjtStudentInfo> gjtStudentInfos;

	private String code;// 院校编码

	@Column(name = "ORG_NAME") // 名称
	private String orgName;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "EXTR_CODE") //
	private String extrCode;

	@Column(name = "ID_") // 接口平台从外部平台传过来的主键ID
	private String id_;

	@Column(name = "IS_DELETED", insertable = false) // 是否删除
	@Where(clause = "IS_DELETED='N'")
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	private String memo;// 备注

	@Column(name = "ORG_RESPONSIBILITY")
	private String orgResponsibility;

	// 1 院校 ; 2 院系所部; 3 教学点(学习中心) ; 4 企业; 5 部/科室(普通部门)
	@Column(name = "ORG_TYPE")
	private String orgType;

	@Column(name = "SOURCE_") // 数据来源
	private String source;

	@Column(name = "TREE_CODE") // ORG_TYPE为 2 院系所部; 时，显示院系的树用到，其他则为空
	private String treeCode;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	public BzrGjtOrg() {
	}

	public String getId_() {
		return id_;
	}

	public void setId_(String id_) {
		this.id_ = id_;
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

	public String getExtrCode() {
		return this.extrCode;
	}

	public void setExtrCode(String extrCode) {
		this.extrCode = extrCode;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgResponsibility() {
		return this.orgResponsibility;
	}

	public void setOrgResponsibility(String orgResponsibility) {
		this.orgResponsibility = orgResponsibility;
	}

	public String getOrgType() {
		return this.orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTreeCode() {
		return this.treeCode;
	}

	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
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

	public List<BzrGjtUserAccount> getGjtUserAccounts() {
		return gjtUserAccounts;
	}

	public void setGjtUserAccounts(List<BzrGjtUserAccount> gjtUserAccounts) {
		this.gjtUserAccounts = gjtUserAccounts;
	}

	public BzrGjtOrg getParentGjtOrg() {
		return parentGjtOrg;
	}

	public void setParentGjtOrg(BzrGjtOrg parentGjtOrg) {
		this.parentGjtOrg = parentGjtOrg;
	}

	public List<BzrGjtOrg> getChildGjtOrgs() {
		return childGjtOrgs;
	}

	public void setChildGjtOrgs(List<BzrGjtOrg> childGjtOrgs) {
		this.childGjtOrgs = childGjtOrgs;
	}

	public BzrGjtSchoolInfo getGjtSchoolInfo() {
		return gjtSchoolInfo;
	}

	public void setGjtSchoolInfo(BzrGjtSchoolInfo gjtSchoolInfo) {
		this.gjtSchoolInfo = gjtSchoolInfo;
	}

	public List<BzrGjtStudentInfo> getGjtStudentInfos() {
		return gjtStudentInfos;
	}

	public void setGjtStudentInfos(List<BzrGjtStudentInfo> gjtStudentInfos) {
		this.gjtStudentInfos = gjtStudentInfos;
	}

	public List<BzrGjtEmployeeInfo> getGjtEmployeeInfos() {
		return gjtEmployeeInfos;
	}

	public void setGjtEmployeeInfos(List<BzrGjtEmployeeInfo> gjtEmployeeInfos) {
		this.gjtEmployeeInfos = gjtEmployeeInfos;
	}
}
