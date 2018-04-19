package com.gzedu.xlims.pojo.exam;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.gzedu.xlims.pojo.GjtCourse;

/**
 * The persistent class for the GJT_EXAM_SUBJECT_NEW database table.
 * 
 */
@Entity
@Table(name = "GJT_EXAM_SUBJECT_NEW")
@NamedQuery(name = "GjtExamSubjectNew.findAll", query = "SELECT g FROM GjtExamSubjectNew g")
@NamedNativeQueries({ @NamedNativeQuery(name = "GjtExamSubjectNew.noSubjectCourseList", query = "select "
		+ "		gc.* " + "	 from "
		+ "		GJT_COURSE gc left join gjt_exam_subject_new gesn on gc.course_id=gesn.course_id " + "	 where "
		+ "		gc.xx_id=:XXID and gc.kch is not null and gc.is_deleted='N' and gesn.subject_id is null", resultClass = GjtCourse.class) })
public class GjtExamSubjectNew implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SUBJECT_ID")
	private String subjectId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ID", insertable = false, updatable = false)
	private GjtCourse gjtCourse;

	@Column(name = "COURSE_ID")
	private String courseId;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "EXAM_NO")
	private String examNo;

	@Column(name = "IS_DELETED")
	@Where(clause = "is_deleted=0")
	private int isDeleted;

	private String memo;

	private String name;

	private int status;

	@Column(name = "SUBJECT_CODE")
	private String subjectCode;

	@Column(name = "TYPE")
	private int type;// 考试方式:1-网考;2-笔试;3-机考;4-形考;5-论文;6-报告;7-其他

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "XX_ID")
	private String xxId;

	/***
	 * 2016-10-25 数据结构改变... 本来刚开始做的时候说好是考试科目与课程1对1 到考试预约前一天又说有些课程需要多个考试科目,
	 * 因为教务员创建不同专业的教学计划时选择了同一个课程, 例如: gjt_course 有一个课程叫英语, 课程号是'english',
	 * 1.教务员创建本科英语教学计划时选择了课程英语, 该教学计划填写了课程号english. 2.然后教务员继续创建专科的英语教学计划,
	 * 依然选择同一个课程英语, 但是教学计划的课程号填写了english1. 合理的流程应该是先创建课程英语(专科),
	 * 然后第二步创建专科英语教学计划的时候应该填写英语(专科)的对应信息, 但现在由于教务员并没如此规范操作, 导致课程数据里没有英语(专科),
	 * 因此按考试科目与课程1对1 的结构做就会出现科目不够的情况, 因此改成考试科目与课程号1对1...
	 */
	@Column(name = "kch")
	private String kch;

	public GjtExamSubjectNew() {
	}

	public String getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getCourseId() {
		return this.courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
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

	public String getExamNo() {
		return this.examNo;
	}

	public void setExamNo(String examNo) {
		this.examNo = examNo;
	}

	public int getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSubjectCode() {
		return this.subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
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

	public GjtCourse getGjtCourse() {
		return gjtCourse;
	}

	public void setGjtCourse(GjtCourse gjtCourse) {
		this.gjtCourse = gjtCourse;
	}

	public String getKch() {
		return kch;
	}

	public void setKch(String kch) {
		this.kch = kch;
	}

}