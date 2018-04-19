package com.gzedu.xlims.pojo.exam;

import com.gzedu.xlims.pojo.GjtStudentInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 报告批改实体类
 */
@Entity
@Table(name="GJT_EXAM_CORRECT_PAPERS")
@NamedQuery(name="GjtExamCorrectPapers.findAll", query="SELECT g FROM GjtExamCorrectPapers g")
public class GjtExamCorrectPapers implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@Id
	@Column(name="ID")
	private String id;

	/**
	 * 考试计划CODE
	 */
	@Column(name="EXAM_BATCH_CODE")
	private String examBatchCode;

	@ManyToOne
	@JoinColumn(name="STUDENT_ID")
	private GjtStudentInfo gjtStudentInfo;

	@ManyToOne
	@JoinColumn(name="EXAM_PLAN_ID")
	private GjtExamPlanNew gjtExamPlanNew;

	/**
	 * 选课ID
	 */
	@Column(name="REC_ID")
	private String recId;

	/**
	 * 报告文件名
	 */
	@Column(name = "PAPERS_FILE_NAME")
	private String papersFileName;

	/**
	 * 报告文件地址，BASE64图片上传到eefile保存图片路径
	 */
	@Column(name = "PAPERS_FILE")
	private String papersFile;

	/**
	 * 获得成绩
	 */
	@Column(name="SCORE")
	private Integer score;

	/**
	 * 报告状态，默认0 (-1-待上传 0-待评分 1-已通过 2-未通过)
	 */
	@Column(name="CORRECT_STATE", insertable = false)
	private Integer correctState;

	/**
	 * 批改时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CORRECT_DT")
	private Date correctDt;

	/**
	 * 批改人
	 */
	@Column(name = "CORRECT_BY")
	private String correctBy;

	/**
	 * 备注
	 */
	private String memo;

	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getExamBatchCode() {
		return examBatchCode;
	}

	public void setExamBatchCode(String examBatchCode) {
		this.examBatchCode = examBatchCode;
	}

	public GjtStudentInfo getGjtStudentInfo() {
		return gjtStudentInfo;
	}

	public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
		this.gjtStudentInfo = gjtStudentInfo;
	}

	public GjtExamPlanNew getGjtExamPlanNew() {
		return gjtExamPlanNew;
	}

	public void setGjtExamPlanNew(GjtExamPlanNew gjtExamPlanNew) {
		this.gjtExamPlanNew = gjtExamPlanNew;
	}

	public String getRecId() {
		return recId;
	}

	public void setRecId(String recId) {
		this.recId = recId;
	}

	public String getPapersFileName() {
		return papersFileName;
	}

	public void setPapersFileName(String papersFileName) {
		this.papersFileName = papersFileName;
	}

	public String getPapersFile() {
		return papersFile;
	}

	public void setPapersFile(String papersFile) {
		this.papersFile = papersFile;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getCorrectState() {
		return correctState;
	}

	public void setCorrectState(Integer correctState) {
		this.correctState = correctState;
	}

	public Date getCorrectDt() {
		return correctDt;
	}

	public void setCorrectDt(Date correctDt) {
		this.correctDt = correctDt;
	}

	public String getCorrectBy() {
		return correctBy;
	}

	public void setCorrectBy(String correctBy) {
		this.correctBy = correctBy;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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