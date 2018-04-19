package com.ouchgzee.study.web.vo;

import java.io.Serializable;

public class CourseDataCountVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String teachPlanId;
	private Long count;

	public String getTeachPlanId() {
		return teachPlanId;
	}

	public void setTeachPlanId(String teachPlanId) {
		this.teachPlanId = teachPlanId;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "CourseDataCountVo [teachPlanId=" + teachPlanId + ", count=" + count + "]";
	}

}
