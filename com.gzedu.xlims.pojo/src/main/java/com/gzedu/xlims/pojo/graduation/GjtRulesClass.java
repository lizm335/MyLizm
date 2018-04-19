package com.gzedu.xlims.pojo.graduation;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.gzedu.xlims.pojo.GjtStudyCenter;


/**
 * The persistent class for the GJT_RULES_CLASS database table.
 * 
 */
@Entity
@Table(name="GJT_RULES_CLASS")
@NamedQuery(name="GjtRulesClass.findAll", query="SELECT g FROM GjtRulesClass g")
public class GjtRulesClass implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String xxid;

	private String num;
	
	@Column(name="ORG_ID")
	private String orgId;
	
	@Column(name="GRADE_ID")
	private String gradeId;
	
	private String type1;//按学习中心分班(作废)
	
	@Column(name="TYPE1_NUM")
	private Integer type1Num;//按学习中心分班 人数(作废)
	
	private String type2;//不按学习中心分班(作废)
	
	@Column(name="TYPE2_NUM")
	private Integer type2Num;//不按学习中心分班 人数(作废)
	
	@Column(name="POINT_CLASS_TYPE")
	private String pointClassType;//教学班分班类型
	
	@Column(name="POINT_CLASS_NUM")
	private Integer pointClassNum;//教学班分班限制人数
	
	@Column(name="XXZX_ID")
	private String xxzxId;//学习中心ID
	
	@OneToOne(fetch = FetchType.LAZY) // 学习中心
	@JoinColumn(name = "XXZX_ID",insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtStudyCenter gjtStudyCenter;
	
	@Column(name = "CREATED_BY", updatable = false) // 创建人
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP) // 创建时间
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;
	
	public GjtRulesClass() {
	}

	public String getXxid() {
		return this.xxid;
	}

	public void setXxid(String xxid) {
		this.xxid = xxid;
	}

	public String getNum() {
		return this.num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

	public Integer getType1Num() {
		return type1Num;
	}

	public void setType1Num(Integer type1Num) {
		this.type1Num = type1Num;
	}

	public String getType2() {
		return type2;
	}

	public void setType2(String type2) {
		this.type2 = type2;
	}

	public Integer getType2Num() {
		return type2Num;
	}

	public void setType2Num(Integer type2Num) {
		this.type2Num = type2Num;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getPointClassType() {
		return pointClassType;
	}

	public void setPointClassType(String pointClassType) {
		this.pointClassType = pointClassType;
	}

	public Integer getPointClassNum() {
		return pointClassNum;
	}

	public void setPointClassNum(Integer pointClassNum) {
		this.pointClassNum = pointClassNum;
	}
	public String getXxzxId() {
		return xxzxId;
	}
	public void setXxzxId(String xxzxId) {
		this.xxzxId = xxzxId;
	}

	public GjtStudyCenter getGjtStudyCenter() {
		return gjtStudyCenter;
	}

	public void setGjtStudyCenter(GjtStudyCenter gjtStudyCenter) {
		this.gjtStudyCenter = gjtStudyCenter;
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
}