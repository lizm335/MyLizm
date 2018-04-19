package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the GJT_FIRST_COURSE database table.
 * 
 */
@Entity
@Table(name="GJT_FIRST_COURSE")
@NamedQuery(name="GjtFirstCourse.findAll", query="SELECT g FROM GjtFirstCourse g")
public class GjtFirstCourse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String content;

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT")
	private Date createdDt;

	@Column(name="IS_DELETED")
	private String isDeleted;

	@Column(name="ORG_ID")
	private String orgId;

	private String title;

	@Column(name="UPDATE_BY")
	private String updateBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_DT")
	private Date updateDt;

	private int type;

	@Transient
	private long viewNum;// 观看视频的学生人数

	@Transient
	private long allViewNum;// 所有可以观看的学生

	@ManyToMany
	@JoinTable(name = "GJT_FCOURSE_SPECIALTY", joinColumns = { @JoinColumn(name = "FIRST_COURSE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "SPECIALTY_BASE_ID") })
	private List<GjtSpecialtyBase> gjtSpecialtyBaseList;

	@ManyToMany
	@JoinTable(name = "GJT_FCOURSE_STUDENT", joinColumns = { @JoinColumn(name = "FIRST_COURSE_ID") }, inverseJoinColumns = {
			@JoinColumn(name = "STUDENT_ID") })
	private List<GjtStudentInfo> gjtStudentInfoList;

	@OneToMany(cascade = { CascadeType.ALL })
	@JoinColumn(name = "FIRST_COURSE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<GjtSpecialtyVideo> gjtSpecialtyVideoList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	public List<GjtSpecialtyBase> getGjtSpecialtyBaseList() {
		return gjtSpecialtyBaseList;
	}

	public void setGjtSpecialtyBaseList(List<GjtSpecialtyBase> gjtSpecialtyBaseList) {
		this.gjtSpecialtyBaseList = gjtSpecialtyBaseList;
	}

	public List<GjtSpecialtyVideo> getGjtSpecialtyVideoList() {
		return gjtSpecialtyVideoList;
	}

	public void setGjtSpecialtyVideoList(List<GjtSpecialtyVideo> gjtSpecialtyVideoList) {
		this.gjtSpecialtyVideoList = gjtSpecialtyVideoList;
	}

	public List<GjtStudentInfo> getGjtStudentInfoList() {
		return gjtStudentInfoList;
	}

	public void setGjtStudentInfoList(List<GjtStudentInfo> gjtStudentInfoList) {
		this.gjtStudentInfoList = gjtStudentInfoList;
	}

	public long getViewNum() {
		return viewNum;
	}

	public void setViewNum(long viewNum) {
		this.viewNum = viewNum;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getAllViewNum() {
		return allViewNum;
	}

	public void setAllViewNum(long allViewNum) {
		this.allViewNum = allViewNum;
	}

}