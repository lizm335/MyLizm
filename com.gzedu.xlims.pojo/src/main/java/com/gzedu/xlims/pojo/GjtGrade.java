package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
import org.springframework.format.annotation.DateTimeFormat;

/**
 * The persistent class for the GJT_GRADE database table.年级表
 * 
 */
@Entity
@Table(name = "GJT_GRADE")
@NamedQuery(name = "GjtGrade.findAll", query = "SELECT g FROM GjtGrade g")
public class GjtGrade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "GRADE_ID") // 年级ID
	private String gradeId;

	@Column(name = "BELONG_YEAR") // 所属学年
	private String belongYear;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = "GRADE_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private List<GjtTermInfo> gjtTermInfo;// 子菜单列表


	@OneToMany(mappedBy = "gjtGrade")
	@Where(clause = "IS_DELETED='N'")
	List<GjtGradeSpecialty> gjtGradeSpecialties;

	@Column(name = "CREATED_BY") // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Temporal(TemporalType.TIMESTAMP) // 入学时间
	@Column(name = "ENTER_DT")
	private Date enterDt;

	@Column(name = "GRADE_CODE") // 年级编码
	private String gradeCode;

	@Column(name = "GRADE_NAME") // 年级名称
	private String gradeName;

	@Column(name = "IS_DELETED") // 是否删除
	private String isDeleted;

	@Column(name = "IS_ENABLED") // 是否启用
	private String isEnabled;

	@Column(name = "SPACE_MONTH") // 间隔月数
	private BigDecimal spaceMonth;

	@Column(name = "UPDATED_BY") // 修改人
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;// 版本好

	@Temporal(TemporalType.DATE) // 学期开始时间
	@Column(name = "START_DATE")
	private Date startDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE) // 学期结束时间
	@Column(name = "END_DATE")
	private Date endDate;

	@Temporal(TemporalType.DATE) // 招生计划开始时间
	@Column(name = "ENROLL_START_DATE")
	private Date enrollStartDate;

	@Temporal(TemporalType.DATE) // 招生计划结束时间
	@Column(name = "ENROLL_END_DATE")
	private Date enrollEndDate;

	@Column(name = "ENROLL_RESPONSIBLE") // 招生计划责任人岗位
	private String enrollResponsible;

	@Temporal(TemporalType.DATE) // 学籍计划开始时间
	@Column(name = "SCHOOLROLL_START_DATE")
	private Date schoolrollStartDate;

	@Temporal(TemporalType.DATE) // 学籍计划结束时间
	@Column(name = "SCHOOLROLL_END_DATE")
	private Date schoolrollEndDate;

	@Column(name = "SCHOOLROLL_RESPONSIBLE") // 学籍计划责任人岗位
	private String schoolrollResponsible;

	@Temporal(TemporalType.DATE) // 教务计划开始时间
	@Column(name = "EDUCATIONAL_START_DATE")
	private Date educationalStartDate;

	@Temporal(TemporalType.DATE) // 教务计划结束时间
	@Column(name = "EDUCATIONAL_END_DATE")
	private Date educationalEndDate;

	@Column(name = "EDUCATIONAL_RESPONSIBLE") // 教务计划责任人岗位
	private String educationalResponsible;

	@Temporal(TemporalType.DATE) // 教学计划开始时间
	@Column(name = "TEACHING_START_DATE")
	private Date teachingStartDate;

	@Temporal(TemporalType.DATE) // 教学计划结束时间
	@Column(name = "TEACHING_END_DATE")
	private Date teachingEndDate;

	@Column(name = "TEACHING_RESPONSIBLE") // 教学计划责任人岗位
	private String teachingResponsible;

	@Temporal(TemporalType.DATE) // 考试计划开始时间
	@Column(name = "EXAM_START_DATE")
	private Date examStartDate;

	@Temporal(TemporalType.DATE) // 考试计划结束时间
	@Column(name = "EXAM_END_DATE")
	private Date examEndDate;

	@Column(name = "EXAM_RESPONSIBLE") // 考试计划责任人岗位
	private String examResponsible;

	@Temporal(TemporalType.DATE) // 毕业计划开始时间
	@Column(name = "GRADUATION_START_DATE")
	private Date graduationStartDate;

	@Temporal(TemporalType.DATE) // 毕业计划结束时间
	@Column(name = "GRADUATION_END_DATE")
	private Date graduationEndDate;

	@Column(name = "GRADUATION_RESPONSIBLE") // 毕业计划责任人岗位
	private String graduationResponsible;

	@Temporal(TemporalType.DATE) // 开课计划开始时间
	@Column(name = "COURSE_START_DATE")
	private Date courseStartDate;

	@Temporal(TemporalType.DATE) // 开课计划结束时间
	@Column(name = "COURSE_END_DATE")
	private Date courseEndDate;

	@Column(name = "COURSE_RESPONSIBLE") // 开课计划责任人岗位
	private String courseResponsible;

	@Temporal(TemporalType.DATE)
	private Date textbookStartDate;// 教材开始时间
	@Temporal(TemporalType.DATE)
	private Date textbookEndDate;// 教材结束时间
	private String textbookResponsible;// 教材负责岗位

	@Temporal(TemporalType.DATE)
	@Column(name = "upcourse_end_date")
	private Date upCourseEndDate;// 上传授课截止时间

	@Temporal(TemporalType.DATE)
	@Column(name = "UP_ACHIEVEMENT_DATE")
	private Date upAchievementDate;// 上传成绩截止时间

	@Temporal(TemporalType.DATE)
	private Date payStartDate;// 老生缴费开始时间
	@Temporal(TemporalType.DATE)
	private Date payEndDate;// 老生缴费结束时间

	@Temporal(TemporalType.DATE)
	private Date oldStudentEnterDate;// 老生入学时间
	@Temporal(TemporalType.DATE)
	private Date newStudentEnterDate;// 新生入学时间

	private String enrollResponsiblePer;// 招生计划负责人
	private String schoolrollResponsiblePer;// 学籍计划负责人
	private String educationalResponsiblePer;// 教务计划负责人
	private String teachingResponsiblePer;// 教学计划负责人
	private String examResponsiblePer;// 考试计划负责人
	private String graduationResponsiblePer;// 毕业计划负责人
	private String textbookResponsiblePer;// 教材负责人

	private int textbookStatus;// 教材发放编排状态 0：未启用 1：已启用
	@ManyToOne
	@JoinColumn(name = "YEAR_ID")
	private GjtYear gjtYear;

	@OneToOne // 学院信息
	@JoinColumn(name = "XX_ID")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtSchoolInfo gjtSchoolInfo;

	public GjtGrade() {
	}

	public GjtGrade(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeId() {
		return this.gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
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

	public Date getEnterDt() {
		return this.enterDt;
	}

	public void setEnterDt(Date enterDt) {
		this.enterDt = enterDt;
	}

	public String getGradeCode() {
		return this.gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getGradeName() {
		return this.gradeName;
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

	public String getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public BigDecimal getSpaceMonth() {
		return this.spaceMonth;
	}

	public void setSpaceMonth(BigDecimal spaceMonth) {
		this.spaceMonth = spaceMonth;
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

	public GjtSchoolInfo getGjtSchoolInfo() {
		return gjtSchoolInfo;
	}

	public void setGjtSchoolInfo(GjtSchoolInfo gjtSchoolInfo) {
		this.gjtSchoolInfo = gjtSchoolInfo;
	}

	public List<GjtTermInfo> getGjtTermInfo() {
		return gjtTermInfo;
	}

	public void setGjtTermInfo(List<GjtTermInfo> gjtTermInfo) {
		this.gjtTermInfo = gjtTermInfo;
	}



	public String getBelongYear() {
		return belongYear;
	}

	public void setBelongYear(String belongYear) {
		this.belongYear = belongYear;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEnrollStartDate() {
		return enrollStartDate;
	}

	public void setEnrollStartDate(Date enrollStartDate) {
		this.enrollStartDate = enrollStartDate;
	}

	public Date getEnrollEndDate() {
		return enrollEndDate;
	}

	public void setEnrollEndDate(Date enrollEndDate) {
		this.enrollEndDate = enrollEndDate;
	}

	public String getEnrollResponsible() {
		return enrollResponsible;
	}

	public void setEnrollResponsible(String enrollResponsible) {
		this.enrollResponsible = enrollResponsible;
	}

	public Date getSchoolrollStartDate() {
		return schoolrollStartDate;
	}

	public void setSchoolrollStartDate(Date schoolrollStartDate) {
		this.schoolrollStartDate = schoolrollStartDate;
	}

	public Date getSchoolrollEndDate() {
		return schoolrollEndDate;
	}

	public void setSchoolrollEndDate(Date schoolrollEndDate) {
		this.schoolrollEndDate = schoolrollEndDate;
	}

	public String getSchoolrollResponsible() {
		return schoolrollResponsible;
	}

	public void setSchoolrollResponsible(String schoolrollResponsible) {
		this.schoolrollResponsible = schoolrollResponsible;
	}

	public Date getEducationalStartDate() {
		return educationalStartDate;
	}

	public void setEducationalStartDate(Date educationalStartDate) {
		this.educationalStartDate = educationalStartDate;
	}

	public Date getEducationalEndDate() {
		return educationalEndDate;
	}

	public void setEducationalEndDate(Date educationalEndDate) {
		this.educationalEndDate = educationalEndDate;
	}

	public String getEducationalResponsible() {
		return educationalResponsible;
	}

	public void setEducationalResponsible(String educationalResponsible) {
		this.educationalResponsible = educationalResponsible;
	}

	public Date getTeachingStartDate() {
		return teachingStartDate;
	}

	public void setTeachingStartDate(Date teachingStartDate) {
		this.teachingStartDate = teachingStartDate;
	}

	public Date getTeachingEndDate() {
		return teachingEndDate;
	}

	public void setTeachingEndDate(Date teachingEndDate) {
		this.teachingEndDate = teachingEndDate;
	}

	public String getTeachingResponsible() {
		return teachingResponsible;
	}

	public void setTeachingResponsible(String teachingResponsible) {
		this.teachingResponsible = teachingResponsible;
	}

	public Date getExamStartDate() {
		return examStartDate;
	}

	public void setExamStartDate(Date examStartDate) {
		this.examStartDate = examStartDate;
	}

	public Date getExamEndDate() {
		return examEndDate;
	}

	public void setExamEndDate(Date examEndDate) {
		this.examEndDate = examEndDate;
	}

	public String getExamResponsible() {
		return examResponsible;
	}

	public void setExamResponsible(String examResponsible) {
		this.examResponsible = examResponsible;
	}

	public Date getGraduationStartDate() {
		return graduationStartDate;
	}

	public void setGraduationStartDate(Date graduationStartDate) {
		this.graduationStartDate = graduationStartDate;
	}

	public Date getGraduationEndDate() {
		return graduationEndDate;
	}

	public void setGraduationEndDate(Date graduationEndDate) {
		this.graduationEndDate = graduationEndDate;
	}

	public String getGraduationResponsible() {
		return graduationResponsible;
	}

	public void setGraduationResponsible(String graduationResponsible) {
		this.graduationResponsible = graduationResponsible;
	}

	public Date getCourseStartDate() {
		return courseStartDate;
	}

	public void setCourseStartDate(Date courseStartDate) {
		this.courseStartDate = courseStartDate;
	}

	public Date getCourseEndDate() {
		return courseEndDate;
	}

	public void setCourseEndDate(Date courseEndDate) {
		this.courseEndDate = courseEndDate;
	}

	public String getCourseResponsible() {
		return courseResponsible;
	}

	public void setCourseResponsible(String courseResponsible) {
		this.courseResponsible = courseResponsible;
	}

	public Date getPayStartDate() {
		return payStartDate;
	}

	public void setPayStartDate(Date payStartDate) {
		this.payStartDate = payStartDate;
	}

	public Date getPayEndDate() {
		return payEndDate;
	}

	public void setPayEndDate(Date payEndDate) {
		this.payEndDate = payEndDate;
	}

	public Date getOldStudentEnterDate() {
		return oldStudentEnterDate;
	}

	public void setOldStudentEnterDate(Date oldStudentEnterDate) {
		this.oldStudentEnterDate = oldStudentEnterDate;
	}

	public Date getNewStudentEnterDate() {
		return newStudentEnterDate;
	}

	public void setNewStudentEnterDate(Date newStudentEnterDate) {
		this.newStudentEnterDate = newStudentEnterDate;
	}

	public String getEnrollResponsiblePer() {
		return enrollResponsiblePer;
	}

	public void setEnrollResponsiblePer(String enrollResponsiblePer) {
		this.enrollResponsiblePer = enrollResponsiblePer;
	}

	public String getSchoolrollResponsiblePer() {
		return schoolrollResponsiblePer;
	}

	public void setSchoolrollResponsiblePer(String schoolrollResponsiblePer) {
		this.schoolrollResponsiblePer = schoolrollResponsiblePer;
	}

	public String getEducationalResponsiblePer() {
		return educationalResponsiblePer;
	}

	public void setEducationalResponsiblePer(String educationalResponsiblePer) {
		this.educationalResponsiblePer = educationalResponsiblePer;
	}

	public String getTeachingResponsiblePer() {
		return teachingResponsiblePer;
	}

	public void setTeachingResponsiblePer(String teachingResponsiblePer) {
		this.teachingResponsiblePer = teachingResponsiblePer;
	}

	public String getExamResponsiblePer() {
		return examResponsiblePer;
	}

	public void setExamResponsiblePer(String examResponsiblePer) {
		this.examResponsiblePer = examResponsiblePer;
	}

	public String getGraduationResponsiblePer() {
		return graduationResponsiblePer;
	}

	public void setGraduationResponsiblePer(String graduationResponsiblePer) {
		this.graduationResponsiblePer = graduationResponsiblePer;
	}

	public GjtYear getGjtYear() {
		return gjtYear;
	}

	public void setGjtYear(GjtYear gjtYear) {
		this.gjtYear = gjtYear;
	}

	public List<GjtGradeSpecialty> getGjtGradeSpecialties() {
		return gjtGradeSpecialties;
	}

	public void setGjtGradeSpecialties(List<GjtGradeSpecialty> gjtGradeSpecialties) {
		this.gjtGradeSpecialties = gjtGradeSpecialties;
	}

	/**
	 * 获取专本科数量
	 * 
	 * @param type
	 *            0专业 1本科
	 * @return
	 */
	@Transient
	public long getSpecialtyCount(int type) {
		long count = 0;
		if (gjtGradeSpecialties == null || gjtGradeSpecialties.isEmpty())
			return count;
		for (GjtGradeSpecialty g : gjtGradeSpecialties) {
			if (g.getGjtSpecialty() == null) {
				continue;
			}
			String pycc = g.getGjtSpecialty().getPycc();
			String isDeleted = g.getGjtSpecialty().getIsDeleted();
			if (type == 1 && "N".equals(isDeleted) && ("2".equals(pycc) || "8".equals(pycc))) {
				count++;
			} else if (type == 0 && "N".equals(isDeleted) && !"2".equals(pycc) && !"8".equals(pycc)) {
				count++;
			}
		}
		return count;
	}

	public Date getTextbookStartDate() {
		return textbookStartDate;
	}

	public void setTextbookStartDate(Date textbookStartDate) {
		this.textbookStartDate = textbookStartDate;
	}

	public Date getTextbookEndDate() {
		return textbookEndDate;
	}

	public void setTextbookEndDate(Date textbookEndDate) {
		this.textbookEndDate = textbookEndDate;
	}

	public String getTextbookResponsible() {
		return textbookResponsible;
	}

	public void setTextbookResponsible(String textbookResponsible) {
		this.textbookResponsible = textbookResponsible;
	}

	public String getTextbookResponsiblePer() {
		return textbookResponsiblePer;
	}

	public void setTextbookResponsiblePer(String textbookResponsiblePer) {
		this.textbookResponsiblePer = textbookResponsiblePer;
	}

	public Date getUpCourseEndDate() {
		return upCourseEndDate;
	}

	public void setUpCourseEndDate(Date upCourseEndDate) {
		this.upCourseEndDate = upCourseEndDate;
	}

	public Date getUpAchievementDate() {
		return upAchievementDate;
	}

	public void setUpAchievementDate(Date upAchievementDate) {
		this.upAchievementDate = upAchievementDate;
	}

	public int getTextbookStatus() {
		return textbookStatus;
	}

	public void setTextbookStatus(int textbookStatus) {
		this.textbookStatus = textbookStatus;
	}

	/**
	 * 统计已设置的计划项
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年4月19日 下午1:53:18
	 * @return
	 */
	@Transient
	public int getCountDate() {
		Date[] countDate = new Date[] { enrollStartDate, schoolrollStartDate, educationalStartDate, examStartDate,
				examStartDate, graduationStartDate };
		int count = 0;
		for (Date d : countDate) {
			if (d == null)
				continue;
			count++;
		}
		return count;
	}

}