package com.ouchgzee.headTeacher.service.edumanage;

import java.util.List;

import com.ouchgzee.headTeacher.pojo.BzrGjtTeachPlan;

/**
 * 
 * 功能说明：教学计划
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月17日
 * @version 2.5
 *
 */
@Deprecated public interface BzrGjtTeachPlanService {

	/**
	 * 根据年级、专业获得教学计划
	 * 
	 * @param gradeId
	 *            年级ID
	 * @param specialtyId
	 *            专业ID
	 * @return
	 */
	public List<BzrGjtTeachPlan> queryGjtTeachPlan(String gradeId, String specialtyId);

}
