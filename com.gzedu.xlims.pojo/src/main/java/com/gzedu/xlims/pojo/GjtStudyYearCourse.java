package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the GJT_STUDYYEAR_COURSE database table. 学年度课程关系表
 */
@Entity
@Table(name = "GJT_STUDYYEAR_COURSE")
@NamedQuery(name = "GjtStudyYearCourse.findAll", query = "SELECT g FROM GjtStudyYearCourse g")
public class GjtStudyYearCourse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@ManyToOne
	@JoinColumn(name = "COURSE_ID")
	GjtCourse course;

	@Column(name = "COURSE_ID", insertable = false, updatable = false)
	String courseId;

	@Column(name = "STUDY_YEAR_ID", insertable = false, updatable = false)
	String studyYearId;

	@ManyToOne
	@JoinColumn(name = "STUDY_YEAR_ID")
	GjtStudyYearInfo studyYearInfo;

	@OneToMany(mappedBy = "studyYearCourse")
	List<GjtClassInfo> classInfos;

	public GjtStudyYearCourse() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<GjtClassInfo> getClassInfos() {
		return classInfos;
	}

	public void setClassInfos(List<GjtClassInfo> classInfos) {
		this.classInfos = classInfos;
	}

	public GjtCourse getCourse() {
		return course;
	}

	public void setCourse(GjtCourse course) {
		this.course = course;
	}

	public GjtStudyYearInfo getStudyYearInfo() {
		return studyYearInfo;
	}

	public void setStudyYearInfo(GjtStudyYearInfo studyYearInfo) {
		this.studyYearInfo = studyYearInfo;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getStudyYearId() {
		return studyYearId;
	}

	public void setStudyYearId(String studyYearId) {
		this.studyYearId = studyYearId;
	}

}