package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbook;

/**
 * The persistent class for the GJT_COURSE database table.课程信息表
 * 
 */
@Entity
@Table(name = "GJT_COURSE")
// @NamedQuery(name = "GjtCourse.findAll", query = "SELECT g FROM GjtCourse g")
/*@NamedNativeQueries({
		@NamedNativeQuery(name = "GjtCourse.findByXxid", query = "select * from gjt_course where xx_id=?1 and is_deleted='N'", resultClass = GjtCourse.class) })*/
@Deprecated public class BzrGjtCourse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "COURSE_ID") // 课程ID
	private String courseId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORG_ID") // 院校
	private BzrGjtOrg gjtOrg;

	/*
	 * @ManyToOne(fetch = FetchType.LAZY) // 学院信息
	 * 
	 * @JoinColumn(name = "XX_ID")
	 * 
	 * @NotFound(action = NotFoundAction.IGNORE) private GjtSchoolInfo
	 * gjtSchoolInfo;
	 */

	@Column(name = "XX_ID")
	private String xxId;

	@ManyToMany
	@JoinTable(name = "GJT_COURSE_OWNERSHIP", joinColumns = { @JoinColumn(name = "COURSE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "ORG_ID") })
	List<BzrGjtOrg> shareGjtSchoolInfos;// 共享的院校列表

	@OneToMany
	@JoinColumn(name = "COURSE_ID", referencedColumnName = "COURSE_ID")
	List<BzrGjtSpecialtyPlan> gjtSpecialtyPlanInfos;// 专业课程中间表

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPLOYEE_ID")
	private BzrGjtEmployeeInfo gjtEmployeeInfo;// 辅导老师

	@Column(name = "speaker_teacher")
	private String speakerTeacher; // 主讲老师名称

	private Integer comments;// 评论数

	@Column(name = "COURSE_TYPE")
	private String courseType;

	@Column(name = "COURSE_NATURE")
	private String courseNature;

	@Column(name = "PYCC")
	private String pycc;

	@Column(name = "CATEGORY")
	private String category;

	@Column(name = "SUBJECT")
	private String subject;

	@Column(name = "CREDIT")
	private Integer credit;

	@Column(name = "LABEL")
	private String label;

	@Column(name = "ASSESSMENT")
	private String assessment;

	@Column(name = "QUALIFIED")
	private Integer qualified;

	@Column(name = "ACTIVITY")
	private Integer activity;

	@Column(name = "CREATED_BY") // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建日期
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "IS_DELETED") // 是否删除
	private String isDeleted;

	@Column(name = "IS_ENABLED") // 是否启用
	private String isEnabled;

	private String kcfm;// 课程封面

	private String kch;// 课程号

	private String kcjj;// 课程简介

	private String kcmc;// 课程名称

	private String kcxz;// 课程性质

	private String kcyq;// 课程要求

	private String kcywmc;// 课程英文名称

	@Column(name = "ORG_CODE")
	private String orgCode;

	private Double satisfy;// 满意度

	private String sskm;// 所属科目

	@Column(name = "SYNC_COURSE_ID") // 期课程ID
	private String syncCourseId;

	@Column(name = "SYNC_STATUS")
	private String syncStatus;// 是否同步 Y/N

	@Column(name = "UPDATED_BY") // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION") // 版本
	private BigDecimal version;

	private String wsjxzk;// 1 网上教学 0 非网上教学

	@Column(name = "XCX_NOTE")
	private String xcxNote;

	@Column(name = "YX_ID") // 院系ID
	private String yxId;

	@Column(name = "ZCJ_NOTE")
	private String zcjNote;

	@Column(name = "ZJX_NOTE")
	private String zjxNote;

	@Column(name = "HOUR")
	private Integer hour;// 学时
	@Column(name = "KHSM")
	private String khsm;// 考核说明
	@Column(name = "SYHY")
	private String syhy;// 适用行业
	@Column(name = "SYZY")
	private String syzy;// 适用专业

	@Column(name = "LECTURER")
	private String lecturer;// 主讲教师

	@ManyToMany
	@JoinTable(name = "GJT_SPECIALTY_PLAN", joinColumns = { @JoinColumn(name = "COURSE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "SPECIALTY_ID") })
	List<BzrGjtSpecialty> gjtSpecialtyInfos;// 拥有的专业列表

	@ManyToMany
	@JoinTable(name = "GJT_COURSE_TEXTBOOK", joinColumns = { @JoinColumn(name = "COURSE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "TEXTBOOK_ID") })
	private List<BzrGjtTextbook> gjtTextbookList;// 教材

	@ManyToMany
	@JoinTable(name = "GJT_COURSE_TEXTBOOK", joinColumns = { @JoinColumn(name = "COURSE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "TEXTBOOK_ID") })
	@Where(clause = "TEXTBOOK_TYPE = 1")
	private List<BzrGjtTextbook> gjtTextbookList1;// 主教材

	@ManyToMany
	@JoinTable(name = "GJT_COURSE_TEXTBOOK", joinColumns = { @JoinColumn(name = "COURSE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "TEXTBOOK_ID") })
	@Where(clause = "TEXTBOOK_TYPE = 2")
	private List<BzrGjtTextbook> gjtTextbookList2;// 复习资料

	public BzrGjtCourse() {
	}

	/**
	 * @return the lecturer
	 */
	public String getLecturer() {
		return lecturer;
	}

	/**
	 * @param lecturer
	 *            the lecturer to set
	 */
	public void setLecturer(String lecturer) {
		this.lecturer = lecturer;
	}

	public BzrGjtCourse(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseId() {
		return this.courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseType() {
		return this.courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
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

	public String getKcfm() {
		return this.kcfm;
	}

	public void setKcfm(String kcfm) {
		this.kcfm = kcfm;
	}

	public String getKch() {
		return this.kch;
	}

	public void setKch(String kch) {
		this.kch = kch;
	}

	public String getKcjj() {
		return this.kcjj;
	}

	public void setKcjj(String kcjj) {
		this.kcjj = kcjj;
	}

	public String getKcmc() {
		return this.kcmc;
	}

	public void setKcmc(String kcmc) {
		this.kcmc = kcmc;
	}

	public String getKcxz() {
		return this.kcxz;
	}

	public void setKcxz(String kcxz) {
		this.kcxz = kcxz;
	}

	public String getKcyq() {
		return this.kcyq;
	}

	public void setKcyq(String kcyq) {
		this.kcyq = kcyq;
	}

	public String getKcywmc() {
		return this.kcywmc;
	}

	public void setKcywmc(String kcywmc) {
		this.kcywmc = kcywmc;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	/**
	 * @return the comments
	 */
	public Integer getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(Integer comments) {
		this.comments = comments;
	}

	/**
	 * @return the gjtOrg
	 */
	public BzrGjtOrg getGjtOrg() {
		return gjtOrg;
	}

	/**
	 * @param gjtOrg
	 *            the gjtOrg to set
	 */
	public void setGjtOrg(BzrGjtOrg gjtOrg) {
		this.gjtOrg = gjtOrg;
	}

	/**
	 * @return the satisfy
	 */
	public Double getSatisfy() {
		return satisfy;
	}

	/**
	 * @param satisfy
	 *            the satisfy to set
	 */
	public void setSatisfy(Double satisfy) {
		this.satisfy = satisfy;
	}

	public String getSskm() {
		return this.sskm;
	}

	public void setSskm(String sskm) {
		this.sskm = sskm;
	}

	public String getSyncCourseId() {
		return this.syncCourseId;
	}

	public void setSyncCourseId(String syncCourseId) {
		this.syncCourseId = syncCourseId;
	}

	public String getSyncStatus() {
		return this.syncStatus;
	}

	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
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

	public String getWsjxzk() {
		return this.wsjxzk;
	}

	public void setWsjxzk(String wsjxzk) {
		this.wsjxzk = wsjxzk;
	}

	public String getXcxNote() {
		return this.xcxNote;
	}

	public void setXcxNote(String xcxNote) {
		this.xcxNote = xcxNote;
	}

	public String getYxId() {
		return this.yxId;
	}

	public void setYxId(String yxId) {
		this.yxId = yxId;
	}

	public String getZcjNote() {
		return this.zcjNote;
	}

	public void setZcjNote(String zcjNote) {
		this.zcjNote = zcjNote;
	}

	public String getZjxNote() {
		return this.zjxNote;
	}

	public void setZjxNote(String zjxNote) {
		this.zjxNote = zjxNote;
	}

	public BzrGjtEmployeeInfo getGjtEmployeeInfo() {
		return gjtEmployeeInfo;
	}

	public void setGjtEmployeeInfo(BzrGjtEmployeeInfo gjtEmployeeInfo) {
		this.gjtEmployeeInfo = gjtEmployeeInfo;
	}

	public String getSpeakerTeacher() {
		return speakerTeacher;
	}

	public void setSpeakerTeacher(String speakerTeacher) {
		this.speakerTeacher = speakerTeacher;
	}

	public List<BzrGjtOrg> getShareGjtSchoolInfos() {
		return shareGjtSchoolInfos;
	}

	public void setShareGjtSchoolInfos(List<BzrGjtOrg> shareGjtSchoolInfos) {
		this.shareGjtSchoolInfos = shareGjtSchoolInfos;
	}

	/*
	 * public GjtSchoolInfo getGjtSchoolInfo() { return gjtSchoolInfo; }
	 * 
	 * public void setGjtSchoolInfo(GjtSchoolInfo gjtSchoolInfo) {
	 * this.gjtSchoolInfo = gjtSchoolInfo; }
	 */

	/**
	 * @return the gjtSpecialtyPlanInfos
	 */
	public List<BzrGjtSpecialtyPlan> getGjtSpecialtyPlanInfos() {
		return gjtSpecialtyPlanInfos;
	}

	/**
	 * @param gjtSpecialtyPlanInfos
	 *            the gjtSpecialtyPlanInfos to set
	 */
	public void setGjtSpecialtyPlanInfos(List<BzrGjtSpecialtyPlan> gjtSpecialtyPlanInfos) {
		this.gjtSpecialtyPlanInfos = gjtSpecialtyPlanInfos;
	}

	/**
	 * @return the gjtSpecialtyInfos
	 */
	public List<BzrGjtSpecialty> getGjtSpecialtyInfos() {
		return gjtSpecialtyInfos;
	}

	/**
	 * @param gjtSpecialtyInfos
	 *            the gjtSpecialtyInfos to set
	 */
	public void setGjtSpecialtyInfos(List<BzrGjtSpecialty> gjtSpecialtyInfos) {
		this.gjtSpecialtyInfos = gjtSpecialtyInfos;
	}

	/**
	 * @return the hour
	 */
	public Integer getHour() {
		return hour;
	}

	/**
	 * @param hour
	 *            the hour to set
	 */
	public void setHour(Integer hour) {
		this.hour = hour;
	}

	/**
	 * @return the khsm
	 */
	public String getKhsm() {
		return khsm;
	}

	/**
	 * @param khsm
	 *            the khsm to set
	 */
	public void setKhsm(String khsm) {
		this.khsm = khsm;
	}

	/**
	 * @return the syhy
	 */
	public String getSyhy() {
		return syhy;
	}

	/**
	 * @param syhy
	 *            the syhy to set
	 */
	public void setSyhy(String syhy) {
		this.syhy = syhy;
	}

	/**
	 * @return the syzy
	 */
	public String getSyzy() {
		return syzy;
	}

	/**
	 * @param syzy
	 *            the syzy to set
	 */
	public void setSyzy(String syzy) {
		this.syzy = syzy;
	}

	public String getCourseNature() {
		return courseNature;
	}

	public void setCourseNature(String courseNature) {
		this.courseNature = courseNature;
	}

	public String getPycc() {
		return pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getAssessment() {
		return assessment;
	}

	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}

	public Integer getQualified() {
		return qualified;
	}

	public void setQualified(Integer qualified) {
		this.qualified = qualified;
	}

	public Integer getActivity() {
		return activity;
	}

	public void setActivity(Integer activity) {
		this.activity = activity;
	}

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public List<BzrGjtTextbook> getGjtTextbookList() {
		return gjtTextbookList;
	}

	public void setGjtTextbookList(List<BzrGjtTextbook> gjtTextbookList) {
		this.gjtTextbookList = gjtTextbookList;
	}

	public List<BzrGjtTextbook> getGjtTextbookList1() {
		return gjtTextbookList1;
	}

	public void setGjtTextbookList1(List<BzrGjtTextbook> gjtTextbookList1) {
		this.gjtTextbookList1 = gjtTextbookList1;
	}

	public List<BzrGjtTextbook> getGjtTextbookList2() {
		return gjtTextbookList2;
	}

	public void setGjtTextbookList2(List<BzrGjtTextbook> gjtTextbookList2) {
		this.gjtTextbookList2 = gjtTextbookList2;
	}

}