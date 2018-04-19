package com.gzedu.xlims.pojo;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.gzedu.xlims.common.gzedu.EmptyUtils;

/**
 * The persistent class for the GJT_ORG database table.学院表
 * 
 */
@Entity
@Table(name = "GJT_ORG")
@NamedQuery(name = "GjtOrg.findAll", query = "SELECT g FROM GjtOrg g")
public class GjtOrg implements Serializable {
	private static final long serialVersionUID = 1L;

	public GjtOrg(String id) {
		super();
		this.id = id;
	}

	@Id
	private String id;// id

	// 如果user表里面的org_id是院校，这个值才有效，否则拿出来的是学习中心的信息
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSchoolInfo gjtSchoolInfo;// 院校基本信息

	// 同上原理
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtStudyCenter gjtStudyCenter;// 学习中心

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gjtOrg")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<GjtUserAccount> gjtUserAccounts;// 院校所属用户

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "PERENT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	GjtOrg parentGjtOrg;// 父

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "PERENT_ID")
	@Where(clause = "is_Deleted='N'")
	@NotFound(action = NotFoundAction.IGNORE)
	List<GjtOrg> childGjtOrgs;// 子列表

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPLOYEE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	@Where(clause = "is_Deleted='N'")
	private List<GjtEmployeeInfo> gjtEmployeeInfos;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<GjtStudentInfo> gjtStudentInfos;

	@OneToMany
	@JoinColumn(name = "COURSE_ID")
	List<GjtCourse> gjtCourses;// 院校下的课程列表

	@ManyToMany
	@JoinTable(name = "GJT_COURSE_OWNERSHIP", joinColumns = { @JoinColumn(name = "ORG_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "COURSE_ID") })
	List<GjtCourse> shareGjtCourses;// 被共享的课程列表

	private String code;// 院校编码

	@Column(name = "ORG_NAME") // 名称
	private String orgName;

	@Column(name = "CREATED_BY") // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "EXTR_CODE") //
	private String extrCode;

	@Column(name = "ID_") // 接口平台从外部平台传过来的主键ID
	private String id_;

	@Column(name = "IS_DELETED") // 是否删除
	@Where(clause = "IS_DELETED='N'")
	private String isDeleted;

	@Column(name = "IS_ENABLED") // 是否启用
	private String isEnabled;

	private String memo;// 备注

	@Column(name = "ORG_RESPONSIBILITY")
	private String orgResponsibility;

	// 旧 1 院校 ; 2 院系所部; 3 教学点(学习中心) ; 4 企业; 5 部/科室(普通部门)
	@Column(name = "ORG_TYPE") // 新2017年8月30日
	private String orgType;// 0-总部，10-分部，1是分校，3是学习中心，6是招生点（学习体验中心）

	@Column(name = "SOURCE_") // 数据来源
	private String source;

	@Column(name = "TREE_CODE") // ORG_TYPE为 2 院系所部; 时，显示院系的树用到，其他则为空
	private String treeCode;

	@Column(name = "SCHOOL_MODEL")
	private String schoolModel; // 办学模式 1-学历办学 2-中职院校 3-院校模式（有考试）4-院校模式（无考试）
	
	@Column(name = "VIRTUAL_XXZX_CODE")
	private String virtualXxzxCode; // 虚拟学习中心ID

	@Column(name = "ATID") // ATID
	private String atid;

	@Column(name = "UPDATED_BY") // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;// 版本号

	@Transient
	private Long branchCount;// 分部数

	@Transient
	private Long branchSchoolCount;// 分校数

	@Transient
	private Long studyCentenCount;// 学习中心个数

	@Transient
	private Long branchStudyCentenCount;// 直属分部学习中心个数

	@Transient
	private Long enrollStudentCount;// 招生点数量

	@Transient
	private Long userCount;// 用户数

	@Transient
	private List<String> serviceList;

	@Column(name = "BELONGS_TO") // 学习中心所属；所属分部-10，所属分校-1
	private String belongsTo;

	public GjtOrg() {
	}

	public String getBelongsTo() {
		return belongsTo;
	}

	public void setBelongsTo(String belongsTo) {
		this.belongsTo = belongsTo;
	}

	public List<String> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<String> serviceList) {
		this.serviceList = serviceList;
	}

	public Long getUserCount() {
		return userCount;
	}

	public void setUserCount(Long userCount) {
		this.userCount = userCount;
	}

	public Long getEnrollStudentCount() {
		return enrollStudentCount;
	}

	public void setEnrollStudentCount(Long enrollStudentCount) {
		this.enrollStudentCount = enrollStudentCount;
	}

	public Long getBranchCount() {
		return branchCount;
	}

	public void setBranchCount(Long branchCount) {
		this.branchCount = branchCount;
	}

	public Long getBranchSchoolCount() {
		return branchSchoolCount;
	}

	public void setBranchSchoolCount(Long branchSchoolCount) {
		this.branchSchoolCount = branchSchoolCount;
	}

	public Long getStudyCentenCount() {
		return studyCentenCount;
	}

	public void setStudyCentenCount(Long studyCentenCount) {
		this.studyCentenCount = studyCentenCount;
	}

	public Long getBranchStudyCentenCount() {
		return branchStudyCentenCount;
	}

	public void setBranchStudyCentenCount(Long branchStudyCentenCount) {
		this.branchStudyCentenCount = branchStudyCentenCount;
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

	public String getSchoolModel() {
		return schoolModel;
	}

	public void setSchoolModel(String schoolModel) {
		this.schoolModel = schoolModel;
	}

	public String getAtid() {
		return atid;
	}

	public void setAtid(String atid) {
		this.atid = atid;
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

	public List<GjtUserAccount> getGjtUserAccounts() {
		return gjtUserAccounts;
	}

	public void setGjtUserAccounts(List<GjtUserAccount> gjtUserAccounts) {
		this.gjtUserAccounts = gjtUserAccounts;
	}

	public GjtOrg getParentGjtOrg() {
		return parentGjtOrg;
	}

	public void setParentGjtOrg(GjtOrg parentGjtOrg) {
		this.parentGjtOrg = parentGjtOrg;
	}

	public List<GjtOrg> getChildGjtOrgs() {
		return childGjtOrgs;
	}

	public void setChildGjtOrgs(List<GjtOrg> childGjtOrgs) {
		this.childGjtOrgs = childGjtOrgs;
	}

	public GjtSchoolInfo getGjtSchoolInfo() {
		return gjtSchoolInfo;
	}

	public void setGjtSchoolInfo(GjtSchoolInfo gjtSchoolInfo) {
		this.gjtSchoolInfo = gjtSchoolInfo;
	}

	public GjtStudyCenter getGjtStudyCenter() {
		return gjtStudyCenter;
	}

	public void setGjtStudyCenter(GjtStudyCenter gjtStudyCenter) {
		this.gjtStudyCenter = gjtStudyCenter;
	}

	public List<GjtStudentInfo> getGjtStudentInfos() {
		return gjtStudentInfos;
	}

	public void setGjtStudentInfos(List<GjtStudentInfo> gjtStudentInfos) {
		this.gjtStudentInfos = gjtStudentInfos;
	}

	public List<GjtEmployeeInfo> getGjtEmployeeInfos() {
		return gjtEmployeeInfos;
	}

	public void setGjtEmployeeInfos(List<GjtEmployeeInfo> gjtEmployeeInfos) {
		this.gjtEmployeeInfos = gjtEmployeeInfos;
	}

	public String getVirtualXxzxCode() {
		return virtualXxzxCode;
	}

	public void setVirtualXxzxCode(String virtualXxzxCode) {
		this.virtualXxzxCode = virtualXxzxCode;
	}
	
	/**
	 * 获取学习中心
	 * 
	 * @return
	 */
	public int getChildGjtOrgsChilds() {
		int totals = 0;
		if (EmptyUtils.isNotEmpty(this.childGjtOrgs)) {
			for (GjtOrg gjt : this.childGjtOrgs) {
				if ("3".equals(gjt.getOrgType())) {// 学习中心
					totals++;
					continue;
				}
				List<GjtOrg> gjtOrgsChild = gjt.getChildGjtOrgs();
				if (EmptyUtils.isNotEmpty(gjtOrgsChild)) {
					totals += gjtOrgsChild.size();
				}
			}
		}
		return totals;
	}

	/**
	 * 获取分校信息
	 * 
	 * @return
	 */
	public int getChildGjtOrgSchools() {
		int totals = 0;
		if (EmptyUtils.isNotEmpty(this.childGjtOrgs)) {
			for (GjtOrg gjt : this.childGjtOrgs) {
				if ("2".equals(gjt.getOrgType())) {// 获取分校
					totals++;
				}
			}
		}
		return totals;
	}

}