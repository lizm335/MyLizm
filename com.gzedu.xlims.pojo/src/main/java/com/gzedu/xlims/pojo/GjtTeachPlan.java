package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gzedu.xlims.pojo.exam.GjtExamSubjectNew;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;

/**
 * The persistent class for the GJT_TEACH_PLAN database table.
 * 
 */
@Entity
@Table(name = "GJT_TEACH_PLAN")
@NamedQuery(name = "GjtTeachPlan.findAll", query = "SELECT g FROM GjtTeachPlan g")
public class GjtTeachPlan implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "TEACH_PLAN_ID")
	private String teachPlanId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtCourse gjtCourse;// 课程

	@Column(name = "COURSE_ID")
	private String courseId;

	@Column(name = "GRADE_ID")
	private String gradeId;// 年级

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE_ID", insertable = false, updatable = false)
	private GjtGrade gjtGrade;// 年级

	@Column(name = "KKZY")
	private String kkzy;// 专业

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "KKZY", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSpecialty gjtSpecialty;// 专业

	private Integer kkxq;// 学期num

	@Column(name = "replacecourse")
	private String replaceCourseId;// 替换课程ID

	@ManyToOne
	@JoinColumn(name = "replacecourse", referencedColumnName = "COURSE_ID", insertable = false, updatable = false)
	private GjtCourse gjtReplaceCourse;

	private String ksdw; // 考试单位:1-省, 2-中央 3-分校
	// private String kclx;
	// private String kclb;// 课程类别 0普通课程 1毕业设计课程 2社会实践课程 3学位课程

	@Column(name = "ACTUAL_GRADE_ID")
	private String actualGradeId;// 实际对应的gradeId

	@Column(name = "GRADE_SPECIALTY_ID")
	private String gradeSpecialtyId;


	@ManyToMany() // cascade = CascadeType.ALL
	@JoinTable(name = "GJT_TEACH_PLAN_TEXTBOOK", joinColumns = {
			@JoinColumn(name = "TEACH_PLAN_ID") }, inverseJoinColumns = { @JoinColumn(name = "TEXTBOOK_ID") })
	private List<GjtTextbook> gjtTextbookList;// 主教材


	@ManyToOne
	@JoinColumn(name = "SUBJECT_ID", insertable = false, updatable = false)
	private GjtExamSubjectNew gjtExamSubjectNew;// 考试科目

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

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
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

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "COURSE_TYPE")
	private String courseType;// 课程类型,0统设，1非统设

	@Column(name = "COURSE_CODE")
	private String courseCode;// 课程编码

	@Column(name = "SUBJECT_ID")
	private String subjectId;// 考试科目ID

	@Column(name = "IS_DOUBLE_CERTIFICATE")
	private String isDoubleCertificate;// 是否双证

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;// 是否开设 0为开设 1已开设

	@Column(insertable = false)
	private String isvalid;

	private String jc;

	@Temporal(TemporalType.TIMESTAMP)
	private Date jsnd;

	private BigDecimal jsxs;

	private String kclbm;

	private String kcsx; // 课程性质 0必修 1选修 2补修

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

	@Column(name = "SYNC_STATUS", insertable = false)
	private String syncStatus;

	private BigDecimal syxs;

	@Column(name = "TERM_ID")
	private String termId;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@Lob
	@Column(name = "XCX_TIP")
	private String xcxTip;

	private Double xf;

	private Integer xs;

	@ManyToOne(fetch = FetchType.LAZY) // 学院信息
	@JoinColumn(name = "XX_ID", insertable = false, updatable = false)
	private GjtSchoolInfo gjtSchoolInfo;

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

	private int grantData;// 是否发放复习资料 0：是 1：否

	private int courseCategory;// 课程类型：0-普通课程；1-社会实践；2-毕业论文

	private int learningStyle;// 学习方式 0国开在线 1国开学习网

	@Column(name = "EXAM_NO")
	private String examNo; // 试卷号

	@Column(name = "REPLACE_EXAM_NO")
	private String replaceExamNo; // 替换课程试卷号

	public GjtTeachPlan() {
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
		} else if (ksfs.equals("17")) {
			return 6;
		} else if (ksfs.equals("18")) {
			return 7;
		} else if (ksfs.equals("14")) {
			return 8;
		} else if (ksfs.equals("15")) {
			return 9;
		} else if (ksfs.equals("20")) {
			return 10;
		} else if (ksfs.equals("21")) {
			return 11;
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

	public Integer getKkxq() {
		return this.kkxq;
	}

	public void setKkxq(Integer kkxq) {
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

	public Integer getKcksbz() {
		return kcksbz;
	}

	public Integer getKcxxbz() {
		return kcxxbz;
	}

	public Integer getPassScore() {
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

	public Double getXf() {
		return xf;
	}

	public void setXf(Double xf) {
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

	public GjtCourse getGjtCourse() {
		return gjtCourse;
	}

	public void setGjtCourse(GjtCourse gjtCourse) {
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

	public GjtGrade getGjtGrade() {
		return gjtGrade;
	}

	public void setGjtGrade(GjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

	public GjtSpecialty getGjtSpecialty() {
		return gjtSpecialty;
	}

	public void setGjtSpecialty(GjtSpecialty gjtSpecialty) {
		this.gjtSpecialty = gjtSpecialty;
	}


	public List<GjtTextbook> getGjtTextbookList() {
		return gjtTextbookList;
	}

	public void setGjtTextbookList(List<GjtTextbook> gjtTextbookList) {
		this.gjtTextbookList = gjtTextbookList;
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

	public GjtExamSubjectNew getGjtExamSubjectNew() {
		return gjtExamSubjectNew;
	}

	public void setGjtExamSubjectNew(GjtExamSubjectNew gjtExamSubjectNew) {
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

	public String getReplaceCourseId() {
		return replaceCourseId;
	}

	public void setReplaceCourseId(String replaceCourseId) {
		this.replaceCourseId = replaceCourseId;
	}

	public String getKsdw() {
		return ksdw;
	}

	public void setKsdw(String ksdw) {
		this.ksdw = ksdw;
	}

	public GjtCourse getGjtReplaceCourse() {
		return gjtReplaceCourse;
	}

	public void setGjtReplaceCourse(GjtCourse gjtReplaceCourse) {
		this.gjtReplaceCourse = gjtReplaceCourse;
	}

	public String getActualGradeId() {
		return actualGradeId;
	}

	public void setActualGradeId(String actualGradeId) {
		this.actualGradeId = actualGradeId;
	}

	public String getGradeSpecialtyId() {
		return gradeSpecialtyId;
	}

	public void setGradeSpecialtyId(String gradeSpecialtyId) {
		this.gradeSpecialtyId = gradeSpecialtyId;
	}

	public int getGrantData() {
		return grantData;
	}

	public void setGrantData(int grantData) {
		this.grantData = grantData;
	}

	public int getCourseCategory() {
		return courseCategory;
	}

	public void setCourseCategory(int courseCategory) {
		this.courseCategory = courseCategory;
	}

	public int getLearningStyle() {
		return learningStyle;
	}

	public void setLearningStyle(int learningStyle) {
		this.learningStyle = learningStyle;
	}

	public String getExamNo() {
		return examNo;
	}

	public void setExamNo(String examNo) {
		this.examNo = examNo;
	}

	public String getReplaceExamNo() {
		return replaceExamNo;
	}

	public void setReplaceExamNo(String replaceExamNo) {
		this.replaceExamNo = replaceExamNo;
	}
}