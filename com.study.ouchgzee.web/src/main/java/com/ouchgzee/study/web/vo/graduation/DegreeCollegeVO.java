/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.study.web.vo.graduation;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年9月27日
 * @version 3.0
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DegreeCollegeVO {

	private String collegeName;
	private String collegeCover;
	private String degreeId;
	private String degreeName;
	private boolean pass;// 所有成绩是否达标
	List<DegreeRequirement> degreeRequirements;

	public String getCollegeName() {
		return collegeName;
	}

	public void setCollegeName(String collegeName) {
		this.collegeName = collegeName;
	}

	public String getCollegeCover() {
		return collegeCover;
	}

	public void setCollegeCover(String collegeCover) {
		this.collegeCover = collegeCover;
	}

	public String getDegreeId() {
		return degreeId;
	}

	public void setDegreeId(String degreeId) {
		this.degreeId = degreeId;
	}

	public String getDegreeName() {
		return degreeName;
	}

	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public List<DegreeRequirement> getDegreeRequirements() {
		return degreeRequirements;
	}

	public void setDegreeRequirements(List<DegreeRequirement> degreeRequirements) {
		this.degreeRequirements = degreeRequirements;
	}

	public DegreeRequirement addDegreeRequirement(String requirement, String requirementDetail) {
		if(this.degreeRequirements==null){
			degreeRequirements=new ArrayList<DegreeCollegeVO.DegreeRequirement>();
		}
		DegreeRequirement temp = new DegreeRequirement(requirement, requirementDetail);
		degreeRequirements.add(temp);
		return temp;
	}

	public class DegreeRequirement {
		private int type;
		private String requirement;
		private String requirementDetail;
		private Float requirementScore;
		private Float examScore;
		private Boolean isPass;
		private String attachment;
		public DegreeRequirement(String requirement, String requirementDetail) {
			this.requirement = requirement;
			this.requirementDetail = requirementDetail;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getRequirement() {
			return requirement;
		}

		public void setRequirement(String requirement) {
			this.requirement = requirement;
		}

		public String getRequirementDetail() {
			return requirementDetail;
		}

		public void setRequirementDetail(String requirementDetail) {
			this.requirementDetail = requirementDetail;
		}

		public Float getRequirementScore() {
			return requirementScore;
		}

		public void setRequirementScore(Float requirementScore) {
			this.requirementScore = requirementScore;
		}

		public Float getExamScore() {
			return examScore;
		}

		public void setExamScore(Float examScore) {
			this.examScore = examScore;
		}

		public Boolean getIsPass() {
			return isPass;
		}

		public void setIsPass(Boolean isPass) {
			this.isPass = isPass;
		}

		public String getAttachment() {
			return attachment;
		}

		public void setAttachment(String attachment) {
			this.attachment = attachment;
		}
	}
}
