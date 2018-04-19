package com.gzedu.xlims.pojo.exam;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtStudyYearInfo;

/**
 * The persistent class for the GJT_EXAM_PLAN_NEW database table.
 * 
 */
@Entity
@Table(name = "GJT_EXAM_PLAN_NEW")
@NamedQuery(name = "GjtExamPlanNew.findAll", query = "SELECT g FROM GjtExamPlanNew g")
public class GjtExamPlanNew implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EXAM_PLAN_ID")
	private String examPlanId;

	@Column(name = "BOOK_END")
	private Date bookEnd;

	@Column(name = "BOOK_ST")
	private Date bookSt;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	// @ManyToOne(optional = false, fetch = FetchType.LAZY)
	// @JoinColumn(name = "EXAM_BATCH_CODE", referencedColumnName =
	// "EXAM_BATCH_CODE", insertable = false, updatable = false)
	// private GjtExamBatchNew examBatchNew;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EXAM_BATCH_CODE", referencedColumnName = "EXAM_BATCH_CODE", insertable = false, updatable = false)
	private GjtExamBatchNew examBatchNew;

	@Column(name = "EXAM_BATCH_ID")
	private String examBatchId;

	@Column(name = "EXAM_BATCH_CODE")
	private String examBatchCode;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXAM_END")
	private Date examEnd;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXAM_ST")
	private Date examSt;

	@Column(name = "IS_DELETED")
	@Where(clause = "is_deleted=0")
	private int isDeleted;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT_CODE", referencedColumnName = "SUBJECT_CODE", insertable = false, updatable = false)
	private GjtExamSubjectNew examSubjectNew;

	@Column(name = "SUBJECT_ID")
	private String subjectId;

	@Column(name = "SUBJECT_CODE")
	private String subjectCode;
	
	/**
	 * 科目类型 1-中央课程科目 2-替换课程科目
	 */
	@Column(name = "SUBJECT_TYPE")
	private Integer subjectType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_YEAR_ID", referencedColumnName = "ID", insertable = false, updatable = false)
	private GjtStudyYearInfo studyYearInfo;

	@Column(name = "STUDY_YEAR_ID")
	private String studyYearId;

	@Column(name = "TYPE")
	private Integer type;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "XX_ID")
	private String xxId;
	
	@Column(name = "XK_PERCENT")
	private Integer xkPercent;

	@Column(name = "EXAM_NO")
	private String examNo;

	@Column(name = "COURSE_ID")
	private String courseID;
	
	@Column(name="GRADE_ID")
	private String gradeId;

	@Column(name="EXAM_PLAN_NAME")
	private String examPlanName;

	@Column(name="EXAM_PLAN_ORDER")
	private String examPlanOrder;  // 考试预约方式,1:个人预约，2：集体预约
	
	@Column(name="EXAM_PLAN_LIMIT")
	private String examPlanLimit;  // 考试预约最低分数限制，0-无分数限制

	@Column(name="EXAM_STYLE")
	private String examStyle;  // 考试方式：字典EXAM_STYLE

	/**
	 * 文档文件名
	 */
	@Column(name = "DOCUMENT_FILE_NAME")
	private String documentFileName;

	/**
	 * 文档文件地址，BASE64图片上传到eefile保存图片路径
	 */
	@Column(name = "DOCUMENT_FILE_PATH")
	private String documentFilePath;
	
	@ManyToMany
	@JoinTable(name = "GJT_EXAM_PLAN_NEW_COURSE", joinColumns = { @JoinColumn(name = "EXAM_PLAN_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "COURSE_ID") })
	private List<GjtCourse> gjtCourseList;
	
	@ManyToMany
	@JoinTable(name = "GJT_EXAM_PLAN_NEW_SPECIALTY", joinColumns = { @JoinColumn(name = "EXAM_PLAN_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "SPECIALTY_ID") })
	private List<GjtSpecialty> gjtSpecialtyList;
	
	@Transient
	private int status;  //状态：1-未开始，2-预约中，3-待考试，4-考试中，5-已结束

	public GjtExamPlanNew() {
	}

	public GjtExamPlanNew(String examPlanId) {
		super();
		this.examPlanId = examPlanId;
	}

	public String getExamPlanId() {
		return this.examPlanId;
	}

	public void setExamPlanId(String examPlanId) {
		this.examPlanId = examPlanId;
	}

	public Date getBookEnd() {
		return this.bookEnd;
	}

	public void setBookEnd(Date bookEnd) {
		this.bookEnd = bookEnd;
	}

	public Date getBookSt() {
		return this.bookSt;
	}

	public void setBookSt(Date bookSt) {
		this.bookSt = bookSt;
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

	public Date getExamEnd() {
		return this.examEnd;
	}

	public void setExamEnd(Date examEnd) {
		this.examEnd = examEnd;
	}

	public Date getExamSt() {
		return this.examSt;
	}

	public void setExamSt(Date examSt) {
		this.examSt = examSt;
	}

	public int getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getStudyYearId() {
		return this.studyYearId;
	}

	public void setStudyYearId(String studyYearId) {
		this.studyYearId = studyYearId;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getXxId() {
		return this.xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public GjtStudyYearInfo getStudyYearInfo() {
		return studyYearInfo;
	}

	public void setStudyYearInfo(GjtStudyYearInfo studyYearInfo) {
		this.studyYearInfo = studyYearInfo;
	}

	public GjtExamBatchNew getExamBatchNew() {
		return examBatchNew;
	}

	public void setExamBatchNew(GjtExamBatchNew examBatchNew) {
		this.examBatchNew = examBatchNew;
	}

	public GjtExamSubjectNew getExamSubjectNew() {
		return examSubjectNew;
	}

	public void setExamSubjectNew(GjtExamSubjectNew examSubjectNew) {
		this.examSubjectNew = examSubjectNew;
	}

	public String getExamBatchCode() {
		return examBatchCode;
	}

	public void setExamBatchCode(String examBatchCode) {
		this.examBatchCode = examBatchCode;
	}

	public String getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Integer getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(Integer subjectType) {
		this.subjectType = subjectType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getExamBatchId() {
		return examBatchId;
	}

	public void setExamBatchId(String examBatchId) {
		this.examBatchId = examBatchId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	@Override
	public GjtExamPlanNew clone() throws CloneNotSupportedException {
		return (GjtExamPlanNew) super.clone();
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public Integer getXkPercent() {
		return xkPercent;
	}

	public void setXkPercent(Integer xkPercent) {
		this.xkPercent = xkPercent;
	}

	public String getExamNo() {
		return examNo;
	}

	public void setExamNo(String examNo) {
		this.examNo = examNo;
	}

	public String getCourseID() {
		return courseID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	public String getExamPlanName() {
		return examPlanName;
	}

	public void setExamPlanName(String examPlanName) {
		this.examPlanName = examPlanName;
	}

	public String getExamPlanOrder() {
		return examPlanOrder;
	}

	public void setExamPlanOrder(String examPlanOrder) {
		this.examPlanOrder = examPlanOrder;
	}
	
	public String getExamPlanLimit() {
		return examPlanLimit;
	}

	public void setExamPlanLimit(String examPlanLimit) {
		this.examPlanLimit = examPlanLimit;
	}

	public String getExamStyle() {
		return examStyle;
	}

	public void setExamStyle(String examStyle) {
		this.examStyle = examStyle;
	}

	public List<GjtCourse> getGjtCourseList() {
		return gjtCourseList;
	}

	public void setGjtCourseList(List<GjtCourse> gjtCourseList) {
		this.gjtCourseList = gjtCourseList;
	}

	public void addGjtCourseList(GjtCourse gjtCourse) {
		if(this.gjtCourseList == null) {
			this.gjtCourseList = new ArrayList<GjtCourse>();
		}
		boolean flag = true;
		for (GjtCourse c : this.gjtCourseList) {
			if(c.getCourseId().equals(gjtCourse.getCourseId())) {
				flag = false;
				break;
			}
		}
		if(flag) {
			this.gjtCourseList.add(gjtCourse);
		}
	}

	public List<GjtSpecialty> getGjtSpecialtyList() {
		return gjtSpecialtyList;
	}

	public void setGjtSpecialtyList(List<GjtSpecialty> gjtSpecialtyList) {
		this.gjtSpecialtyList = gjtSpecialtyList;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDocumentFileName() {
		return documentFileName;
	}

	public void setDocumentFileName(String documentFileName) {
		this.documentFileName = documentFileName;
	}

	public String getDocumentFilePath() {
		return documentFilePath;
	}

	public void setDocumentFilePath(String documentFilePath) {
		this.documentFilePath = documentFilePath;
	}

	@Override
	public String toString() {
		return "GjtExamPlanNew{" +
				"examPlanId='" + examPlanId + '\'' +
				", bookEnd=" + bookEnd +
				", bookSt=" + bookSt +
				", createdBy='" + createdBy + '\'' +
				", createdDt=" + createdDt +
				", examBatchNew=" + examBatchNew +
				", examBatchId='" + examBatchId + '\'' +
				", examBatchCode='" + examBatchCode + '\'' +
				", examEnd=" + examEnd +
				", examSt=" + examSt +
				", isDeleted=" + isDeleted +
				", examSubjectNew=" + examSubjectNew +
				", subjectId='" + subjectId + '\'' +
				", subjectCode='" + subjectCode + '\'' +
				", studyYearInfo=" + studyYearInfo +
				", studyYearId='" + studyYearId + '\'' +
				", type=" + type +
				", updatedBy='" + updatedBy + '\'' +
				", updatedDt=" + updatedDt +
				", xxId='" + xxId + '\'' +
				", xkPercent=" + xkPercent +
				", examNo='" + examNo + '\'' +
				", courseID='" + courseID + '\'' +
				", gradeId='" + gradeId + '\'' +
				", examPlanName='" + examPlanName + '\'' +
				", examPlanOrder='" + examPlanOrder + '\'' +
				", examPlanLimit='" + examPlanLimit + '\'' +
				", examStyle='" + examStyle + '\'' +
				", gjtCourseList=" + gjtCourseList +
				", gjtSpecialtyList=" + gjtSpecialtyList +
				", status=" + status +
				'}';
	}
}