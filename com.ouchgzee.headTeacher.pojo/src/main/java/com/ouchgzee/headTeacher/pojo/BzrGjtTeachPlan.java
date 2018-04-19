package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbook;

/**
 * The persistent class for the GJT_TEACH_PLAN database table.
 * 
 */
@Entity
@Table(name = "GJT_TEACH_PLAN")
// @NamedQuery(name = "GjtTeachPlan.findAll", query = "SELECT g FROM GjtTeachPlan g")
@Deprecated public class BzrGjtTeachPlan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TEACH_PLAN_ID")
	private String teachPlanId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ID", insertable = false, updatable = false)
	private BzrGjtCourse gjtCourse;// 课程

	@Column(name = "COURSE_ID")
	private String courseId;

	@Column(name = "GRADE_ID")
	private String gradeId;// 年级

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE_ID", insertable = false, updatable = false)
	private BzrGjtGrade gjtGrade;// 年级

	@Column(name = "KKZY")
	private String kkzy;// 专业

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "KKZY", insertable = false, updatable = false)
	private BzrGjtSpecialty gjtSpecialty;// 专业

	private int kkxq;// 学期num

	@OneToMany(mappedBy = "gjtGradeSpecialtyPlanBook", cascade = CascadeType.ALL)
	private List<BzrGjtGradeSpecialtyPlanBook> gjtGradeSpecialtyPlanBooks;// 教材中间表集合

	@ManyToMany
	@JoinTable(name = "GJT_GRADE_SPECIALTY_PLAN_BOOK", joinColumns = {
			@JoinColumn(name = "GRADE_SPECIALTY_PLAN_ID") }, inverseJoinColumns = { @JoinColumn(name = "TEXTBOOK_ID") })
	@Where(clause = "TEXTBOOK_TYPE = 1")
	private List<BzrGjtTextbook> gjtTextbookList1;// 主教材

	@ManyToMany
	@JoinTable(name = "GJT_GRADE_SPECIALTY_PLAN_BOOK", joinColumns = {
			@JoinColumn(name = "GRADE_SPECIALTY_PLAN_ID") }, inverseJoinColumns = { @JoinColumn(name = "TEXTBOOK_ID") })
	@Where(clause = "TEXTBOOK_TYPE = 2")
	private List<BzrGjtTextbook> gjtTextbookList2;// 复习资料

	@ManyToOne
	@JoinColumn(name = "SUBJECT_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtExamSubjectNew gjtExamSubjectNew;// 考试科目

	private Integer kcksbz;// 考试比重
	private Integer kcxxbz;// 学习比重

	@Column(name = "BOOK_ENDTIME")
	private String bookEndtime;

	@Column(name = "BOOK_STARTTIME")
	private String bookStarttime;

	@Column(name = "STUDY_YEAR_CODE")
	private Integer studyYearCode;// 学年度code

	private String cksm;

	@Column(name = "COURSE_FEE")
	private BigDecimal courseFee;// 课程费用，可为空

	@Lob
	@Column(name = "COURSE_TIP")
	private String courseTip;// 课程说明

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Lob
	@Column(name = "DOUBLE_CERT_TIP")
	private String doubleCertTip;// 双证说明

	@Temporal(TemporalType.TIMESTAMP)
	private Date edate;

	@Column(name = "EXAM_ETIME")
	private Timestamp examEtime;

	@Lob
	@Column(name = "EXAM_REMARK")
	private String examRemark;

	@Column(name = "EXAM_STIME")
	private Timestamp examStime;

	@Column(name = "EXAM_STYLE")
	private String examStyle;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "COURSE_TYPE")
	private String courseType;// 课程类型

	@Column(name = "COURSE_CODE")
	private String courseCode;// 课程编码

	@Column(name = "SUBJECT_ID")
	private String subjectId;// 考试科目ID

	@Column(name = "IS_DOUBLE_CERTIFICATE")
	private String isDoubleCertificate;// 是否双证

	@Column(name = "IS_ENABLED")
	private String isEnabled;

	private String isvalid;

	private String jc;

	@Temporal(TemporalType.TIMESTAMP)
	private Date jsnd;

	private BigDecimal jsxs;

	private String kclbm;

	private String kcsx;

	private String kczyfx;

	private String ksfs;// 考试方式

	@Temporal(TemporalType.TIMESTAMP)
	private Date ksnd;

	@Column(name = "ORG_CODE")
	private String orgCode;

	@Column(name = "ORG_ID")
	private String orgId;

	@Column(name = "PASS_SCORE")
	private Integer passScore;// 通过分值

	private BigDecimal pscjbz;

	private String pycc;

	@Temporal(TemporalType.TIMESTAMP)
	private Date sdate;

	private String sfkskc;

	private BigDecimal sjxs;

	private String skfsm;

	private BigDecimal skjsz;

	private BigDecimal skksz;

	@Column(name = "SYNC_STATUS")
	private String syncStatus;

	private BigDecimal syxs;

	@Column(name = "TERM_ID")
	private String termId;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	private BigDecimal version;

	@Lob
	@Column(name = "XCX_TIP")
	private String xcxTip;

	private Integer xf;

	private Integer xs;

	@ManyToOne(fetch = FetchType.LAZY) // 学院信息
	@JoinColumn(name = "XX_ID", insertable = false, updatable = false)
	private BzrGjtSchoolInfo gjtSchoolInfo;

	@Column(name = "XX_ID")
	private String xxId;

	@Column(name = "YX_ID")
	private String yxId;

	private String yxkc;

	@Lob
	@Column(name = "ZCJ_TIP")
	private String zcjTip;

	@Lob
	@Column(name = "ZJX_TIP")
	private String zjxTip;

	private BigDecimal zjxs;

	private BigDecimal zxs;

	private BigDecimal zxxs;

	public BzrGjtTeachPlan() {
	}

	// 根据旧数据的考试方式 ，获得新平台TYPE 考试方式:1-网考;2-笔试;3-机考;4-形考;5-网考（省）
	public int getNewExamType() {
		if (ksfs.equals("7")) {
			return 1;
		} else if (ksfs.equals("8")) {
			return 2;
		} else if (ksfs.equals("11")) {
			return 3;
		} else if (ksfs.equals("13")) {
			return 4;
		} else if (ksfs.equals("19")) {
			return 5;
		}
		return 0;
	}

	public String getTeachPlanId() {
		return this.teachPlanId;
	}

	public void setTeachPlanId(String teachPlanId) {
		this.teachPlanId = teachPlanId;
	}

	public String getBookEndtime() {
		return this.bookEndtime;
	}

	public void setBookEndtime(String bookEndtime) {
		this.bookEndtime = bookEndtime;
	}

	public String getBookStarttime() {
		return this.bookStarttime;
	}

	public void setBookStarttime(String bookStarttime) {
		this.bookStarttime = bookStarttime;
	}

	public String getCksm() {
		return this.cksm;
	}

	public void setCksm(String cksm) {
		this.cksm = cksm;
	}

	public BigDecimal getCourseFee() {
		return this.courseFee;
	}

	public void setCourseFee(BigDecimal courseFee) {
		this.courseFee = courseFee;
	}

	public String getCourseTip() {
		return this.courseTip;
	}

	public void setCourseTip(String courseTip) {
		this.courseTip = courseTip;
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

	public String getDoubleCertTip() {
		return this.doubleCertTip;
	}

	public void setDoubleCertTip(String doubleCertTip) {
		this.doubleCertTip = doubleCertTip;
	}

	public Date getEdate() {
		return this.edate;
	}

	public void setEdate(Date edate) {
		this.edate = edate;
	}

	public Timestamp getExamEtime() {
		return this.examEtime;
	}

	public void setExamEtime(Timestamp examEtime) {
		this.examEtime = examEtime;
	}

	public String getExamRemark() {
		return this.examRemark;
	}

	public void setExamRemark(String examRemark) {
		this.examRemark = examRemark;
	}

	public Timestamp getExamStime() {
		return this.examStime;
	}

	public void setExamStime(Timestamp examStime) {
		this.examStime = examStime;
	}

	public String getExamStyle() {
		return this.examStyle;
	}

	public void setExamStyle(String examStyle) {
		this.examStyle = examStyle;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIsDoubleCertificate() {
		return this.isDoubleCertificate;
	}

	public void setIsDoubleCertificate(String isDoubleCertificate) {
		this.isDoubleCertificate = isDoubleCertificate;
	}

	public String getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getIsvalid() {
		return this.isvalid;
	}

	public void setIsvalid(String isvalid) {
		this.isvalid = isvalid;
	}

	public String getJc() {
		return this.jc;
	}

	public void setJc(String jc) {
		this.jc = jc;
	}

	public Date getJsnd() {
		return this.jsnd;
	}

	public void setJsnd(Date jsnd) {
		this.jsnd = jsnd;
	}

	public BigDecimal getJsxs() {
		return this.jsxs;
	}

	public void setJsxs(BigDecimal jsxs) {
		this.jsxs = jsxs;
	}

	public String getKclbm() {
		return this.kclbm;
	}

	public void setKclbm(String kclbm) {
		this.kclbm = kclbm;
	}

	public String getKcsx() {
		return this.kcsx;
	}

	public void setKcsx(String kcsx) {
		this.kcsx = kcsx;
	}

	public String getKczyfx() {
		return this.kczyfx;
	}

	public void setKczyfx(String kczyfx) {
		this.kczyfx = kczyfx;
	}

	public int getKkxq() {
		return this.kkxq;
	}

	public void setKkxq(int kkxq) {
		this.kkxq = kkxq;
	}

	public String getKkzy() {
		return kkzy;
	}

	public void setKkzy(String kkzy) {
		this.kkzy = kkzy;
	}

	public String getKsfs() {
		return this.ksfs;
	}

	public void setKsfs(String ksfs) {
		this.ksfs = ksfs;
	}

	public Date getKsnd() {
		return this.ksnd;
	}

	public void setKsnd(Date ksnd) {
		this.ksnd = ksnd;
	}

	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * @return the gjtSchoolInfo
	 */
	public BzrGjtSchoolInfo getGjtSchoolInfo() {
		return gjtSchoolInfo;
	}

	/**
	 * @param gjtSchoolInfo
	 *            the gjtSchoolInfo to set
	 */
	public void setGjtSchoolInfo(BzrGjtSchoolInfo gjtSchoolInfo) {
		this.gjtSchoolInfo = gjtSchoolInfo;
	}

	public Integer getKcksbz() {
		return kcksbz;
	}

	public Integer getKcxxbz() {
		return kcxxbz;
	}

	public int getPassScore() {
		return passScore;
	}

	public void setPassScore(int passScore) {
		this.passScore = passScore;
	}

	public BigDecimal getPscjbz() {
		return this.pscjbz;
	}

	public void setPscjbz(BigDecimal pscjbz) {
		this.pscjbz = pscjbz;
	}

	public String getPycc() {
		return this.pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	public Date getSdate() {
		return this.sdate;
	}

	public void setSdate(Date sdate) {
		this.sdate = sdate;
	}

	public String getSfkskc() {
		return this.sfkskc;
	}

	public void setSfkskc(String sfkskc) {
		this.sfkskc = sfkskc;
	}

	public BigDecimal getSjxs() {
		return this.sjxs;
	}

	public void setSjxs(BigDecimal sjxs) {
		this.sjxs = sjxs;
	}

	public String getSkfsm() {
		return this.skfsm;
	}

	public void setSkfsm(String skfsm) {
		this.skfsm = skfsm;
	}

	public BigDecimal getSkjsz() {
		return this.skjsz;
	}

	public void setSkjsz(BigDecimal skjsz) {
		this.skjsz = skjsz;
	}

	public BigDecimal getSkksz() {
		return this.skksz;
	}

	public void setSkksz(BigDecimal skksz) {
		this.skksz = skksz;
	}

	public String getSyncStatus() {
		return this.syncStatus;
	}

	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
	}

	public BigDecimal getSyxs() {
		return this.syxs;
	}

	public void setSyxs(BigDecimal syxs) {
		this.syxs = syxs;
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

	public String getXcxTip() {
		return this.xcxTip;
	}

	public void setXcxTip(String xcxTip) {
		this.xcxTip = xcxTip;
	}

	public Integer getXf() {
		return xf;
	}

	public void setXf(Integer xf) {
		this.xf = xf;
	}

	public Integer getXs() {
		return xs;
	}

	public void setXs(Integer xs) {
		this.xs = xs;
	}

	public String getYxId() {
		return this.yxId;
	}

	public void setYxId(String yxId) {
		this.yxId = yxId;
	}

	public String getYxkc() {
		return this.yxkc;
	}

	public void setYxkc(String yxkc) {
		this.yxkc = yxkc;
	}

	public String getZcjTip() {
		return this.zcjTip;
	}

	public void setZcjTip(String zcjTip) {
		this.zcjTip = zcjTip;
	}

	public String getZjxTip() {
		return this.zjxTip;
	}

	public void setZjxTip(String zjxTip) {
		this.zjxTip = zjxTip;
	}

	public BigDecimal getZjxs() {
		return this.zjxs;
	}

	public void setZjxs(BigDecimal zjxs) {
		this.zjxs = zjxs;
	}

	public BigDecimal getZxs() {
		return this.zxs;
	}

	public void setZxs(BigDecimal zxs) {
		this.zxs = zxs;
	}

	public BigDecimal getZxxs() {
		return this.zxxs;
	}

	public void setZxxs(BigDecimal zxxs) {
		this.zxxs = zxxs;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public BzrGjtCourse getGjtCourse() {
		return gjtCourse;
	}

	public void setGjtCourse(BzrGjtCourse gjtCourse) {
		this.gjtCourse = gjtCourse;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public BzrGjtGrade getGjtGrade() {
		return gjtGrade;
	}

	public void setGjtGrade(BzrGjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

	public BzrGjtSpecialty getGjtSpecialty() {
		return gjtSpecialty;
	}

	public void setGjtSpecialty(BzrGjtSpecialty gjtSpecialty) {
		this.gjtSpecialty = gjtSpecialty;
	}

	public List<BzrGjtGradeSpecialtyPlanBook> getGjtGradeSpecialtyPlanBooks() {
		return gjtGradeSpecialtyPlanBooks;
	}

	public void setGjtGradeSpecialtyPlanBooks(List<BzrGjtGradeSpecialtyPlanBook> gjtGradeSpecialtyPlanBooks) {
		this.gjtGradeSpecialtyPlanBooks = gjtGradeSpecialtyPlanBooks;
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

	public void setKcksbz(Integer kcksbz) {
		this.kcksbz = kcksbz;
	}

	public void setKcxxbz(Integer kcxxbz) {
		this.kcxxbz = kcxxbz;
	}

	public void setPassScore(Integer passScore) {
		this.passScore = passScore;
	}

	public BzrGjtExamSubjectNew getGjtExamSubjectNew() {
		return gjtExamSubjectNew;
	}

	public void setGjtExamSubjectNew(BzrGjtExamSubjectNew gjtExamSubjectNew) {
		this.gjtExamSubjectNew = gjtExamSubjectNew;
	}

	public String getCourseType() {
		return courseType;
	}

	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

	public Integer getStudyYearCode() {
		return studyYearCode;
	}

	public void setStudyYearCode(Integer studyYearCode) {
		this.studyYearCode = studyYearCode;
	}

}