/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.dto;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.gzedu.xlims.pojo.GjtGradeSpecialty;
import com.gzedu.xlims.pojo.GjtSpecialty;

/**
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年4月13日
 * @version 3.0
 *
 */
public class GjtSpecialtyDto extends GjtSpecialty {
	public GjtSpecialtyDto(GjtSpecialty specialty, GjtGradeSpecialty gradeSpecialty) {
		setSpecialtyId(specialty.getSpecialtyId());
		setRuleCode(specialty.getRuleCode());
		setZymc(specialty.getZymc());
		setZyh(specialty.getZyh());
		setSpecialtyCategory(specialty.getSpecialtyCategory());
		setPycc(specialty.getPycc());
		setZylb(specialty.getZylb());
		setSyhy(specialty.getSyhy());
		setZxf(specialty.getZxf());
		setZdbyxf(specialty.getZdbyxf());
		setSubject(specialty.getSubject());
		setCategory(specialty.getCategory());
		setType(specialty.getType());
		this.studyCenterIds = gradeSpecialty.getStudyCenterIds();
		this.gradeSpecialtyId = gradeSpecialty.getId();
		this.SpecialtyPlanCount = CollectionUtils.isEmpty(specialty.getGjtSpecialtyPlans()) ? 0
				: specialty.getGjtSpecialtyPlans().size();
	}

	private int SpecialtyPlanCount;
	private String gradeSpecialtyId;
	private List<String> studyCenterIds;

	public int getSpecialtyPlanCount() {
		return SpecialtyPlanCount;
	}

	public void setSpecialtyPlanCount(int specialtyPlanCount) {
		SpecialtyPlanCount = specialtyPlanCount;
	}

	public String getGradeSpecialtyId() {
		return gradeSpecialtyId;
	}

	public void setGradeSpecialtyId(String gradeSpecialtyId) {
		this.gradeSpecialtyId = gradeSpecialtyId;
	}

	public List<String> getStudyCenterIds() {
		return studyCenterIds;
	}

	public void setStudyCenterIds(List<String> studyCenterIds) {
		this.studyCenterIds = studyCenterIds;
	}


}
