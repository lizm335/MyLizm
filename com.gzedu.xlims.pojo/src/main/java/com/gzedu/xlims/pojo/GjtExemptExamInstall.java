package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


/**
 * The persistent class for the GJT_EXEMPT_EXAM_INSTALL database table.
 * 免修免考设置
 */
@Entity
@Table(name="GJT_EXEMPT_EXAM_INSTALL")
@NamedQuery(name="GjtExemptExamInstall.findAll", query="SELECT g FROM GjtExemptExamInstall g")
public class GjtExemptExamInstall implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="INSTALL_ID")
	private String installId;

	@Column(name="COURSE_ID")
	private String courseId;
	
	@Column(name="XX_ID")
	private String xxId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ID", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtCourse gjtCourse;// 课程

	@Column(name="CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT",insertable = false, updatable = false)
	private Date createdDt;
	
	@Lob
	@Column(name="GRADE_ID")
	private String gradeId;
	
	@Lob
	@Column(name="GRADE_NAME")
	private String gradeName;//学期名称
	
	@ManyToOne(fetch = FetchType.LAZY) // 学期
	@JoinColumn(name = "GRADE_ID",insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtGrade gjtGrade;

	@Column(name="IS_DELETED")
	private String isDeleted;
	
	@Column(name="MATERIAL")
	private String material;

	private String memo;
	
	@Column(name="STATUS")
	private String status;//状态:0-停用 1-启用

	@Column(name="UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATED_DT")
	private Date updatedDt;

	@Column(name="VERSION",insertable = false, updatable = false)
	private BigDecimal version;

	@OneToMany(mappedBy="gjtExemptExamInstall",fetch = FetchType.LAZY)
	private List<GjtExemptExamMaterial> gjtExemptExamMaterials;

	public GjtExemptExamInstall() {
	}

	public String getInstallId() {
		return installId;
	}

	public void setInstallId(String installId) {
		this.installId = installId;
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

	public String getGradeId() {
		return this.gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	
	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getMaterial() {
		return this.material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public List<GjtExemptExamMaterial> getGjtExemptExamMaterials() {
		return gjtExemptExamMaterials;
	}

	public void setGjtExemptExamMaterials(List<GjtExemptExamMaterial> gjtExemptExamMaterials) {
		this.gjtExemptExamMaterials = gjtExemptExamMaterials;
	}

	public GjtCourse getGjtCourse() {
		return gjtCourse;
	}

	public void setGjtCourse(GjtCourse gjtCourse) {
		this.gjtCourse = gjtCourse;
	}

	public GjtGrade getGjtGrade() {
		return gjtGrade;
	}

	public void setGjtGrade(GjtGrade gjtGrade) {
		this.gjtGrade = gjtGrade;
	}

	public String getXxId() {
		return xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}
	
}