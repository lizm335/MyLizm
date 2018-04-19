package com.gzedu.xlims.pojo.graduation;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.gzedu.xlims.pojo.status.DegreeReqOperatorEnum;


/**
 * 学位条件表
 * 
 */
@Entity
@Table(name="GJT_DEGREE_REQUIREMENT")
@NamedQuery(name="GjtDegreeRequirement.findAll", query="SELECT g FROM GjtDegreeRequirement g")
public class GjtDegreeRequirement implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="REQUIREMENT_ID")
	private String requirementId;

	@JoinColumn(name="COLLEGE_ID")
	private String collegeId;

	@Column(name="SPECIALTY_ID")
	private String specialtyId;

	@Column(name="CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_DT", updatable = false)
	private Date createdDt;

	@Column(name="REQUIREMENT_DESC")
	private String requirementDesc;

	@Column(name="REQUIREMENT_NAME")
	private String requirementName;

	@Column(name="REQUIREMENT_PARAM")
	private Float requirementParam;

	@Column(name="REQUIREMENT_TYPE")
	private Integer requirementType;
	
	private int operator;
	
	private String collegeSpecialtyId;

	@Transient
	private float actualValue;

	public GjtDegreeRequirement() {
	}

	public String getRequirementId() {
		return this.requirementId;
	}

	public void setRequirementId(String requirementId) {
		this.requirementId = requirementId;
	}

	public String getCollegeId() {
		return collegeId;
	}

	public void setCollegeId(String collegeId) {
		this.collegeId = collegeId;
	}

	public String getSpecialtyId() {
		return specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
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

	public String getRequirementDesc() {
		return this.requirementDesc;
	}

	public void setRequirementDesc(String requirementDesc) {
		this.requirementDesc = requirementDesc;
	}

	public String getRequirementName() {
		return this.requirementName;
	}

	public void setRequirementName(String requirementName) {
		this.requirementName = requirementName;
	}

	public Float getRequirementParam() {
		return this.requirementParam;
	}

	public void setRequirementParam(Float requirementParam) {
		this.requirementParam = requirementParam;
	}

	public Integer getRequirementType() {
		return this.requirementType;
	}

	public void setRequirementType(Integer requirementType) {
		this.requirementType = requirementType;
	}

	public int getOperator() {
		return operator;
	}

	public void setOperator(int operator) {
		this.operator = operator;
	}
	
	

	public String getCollegeSpecialtyId() {
		return collegeSpecialtyId;
	}

	public void setCollegeSpecialtyId(String collegeSpecialtyId) {
		this.collegeSpecialtyId = collegeSpecialtyId;
	}

	public float getActualValue() {
		return actualValue;
	}

	public void setActualValue(float actualValue) {
		this.actualValue = actualValue;
	}

	@Transient
	public String getOperatorTag() {
		return DegreeReqOperatorEnum.getOpertorByValue(operator);
	}

	@Transient
	public boolean getIsPass() {
		if (actualValue == 0)
			return false;
		if(operator==0||requirementParam==null)
			return true;
		if(operator==DegreeReqOperatorEnum.EQ.getValue()){
			return actualValue == requirementParam;
		} else if (operator == DegreeReqOperatorEnum.LG.getValue()) {
			return actualValue > requirementParam;
		} else if (operator == DegreeReqOperatorEnum.LGE.getValue()) {
			return actualValue >= requirementParam;
		} else if (operator == DegreeReqOperatorEnum.LT.getValue()) {
			return actualValue < requirementParam;
		} else if (operator == DegreeReqOperatorEnum.LTE.getValue()) {
			return actualValue <= requirementParam;
		}
		return false;
	}

	@Override
	public String toString() {
		return "GjtDegreeRequirement [requirementId=" + requirementId + ", createdBy=" + createdBy + ", createdDt="
				+ createdDt + ", requirementDesc=" + requirementDesc + ", requirementName=" + requirementName
				+ ", requirementParam=" + requirementParam + ", requirementType=" + requirementType
				+ "]";
	}

}