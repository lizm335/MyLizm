package com.gzedu.xlims.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="GJT_GRANT_COURSE_SCORE")
public class GjtGrantCourseScore {
	
	@Id
	private String id;
	// 姓名
	@Column(name="STUDENT_NAME")
	private String studentName;
	// 学号
	@Column(name="STUDENT_NO")
	private String studentNo;
	// 平时成绩
	@Column(name="USUAL_SCORE")
	private Integer usualScore;
	// 期末成绩
	@Column(name="TERMINAL_SCORE")
	private Integer terminalScore;
	
	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;
	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;
	@Column(name = "UPDATED_BY", insertable = false) // 修改人
	private String updatedBy;
	@Temporal(TemporalType.TIMESTAMP) // 修改时间
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;
	@Column(name = "IS_DELETED", insertable = false) // 是否删除
	private String isDeleted;
	
	public GjtGrantCourseScore() {
		super();
	}
	public GjtGrantCourseScore(String studentName, String studentNo, Integer usualScore, Integer terminalScore) {
		super();
		this.studentName = studentName;
		this.studentNo = studentNo;
		this.usualScore = usualScore;
		this.terminalScore = terminalScore;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentNo() {
		return studentNo;
	}
	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}
	public Integer getUsualScore() {
		return usualScore;
	}
	public void setUsualScore(Integer usualScore) {
		this.usualScore = usualScore;
	}
	public Integer getTerminalScore() {
		return terminalScore;
	}
	public void setTerminalScore(Integer terminalScore) {
		this.terminalScore = terminalScore;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDt() {
		return createdDt;
	}
	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedDt() {
		return updatedDt;
	}
	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}
	public String getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
}
