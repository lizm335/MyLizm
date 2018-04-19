package com.gzedu.xlims.service.graduation;

import com.gzedu.xlims.pojo.graduation.GjtGraduationDefencePlan;

/**
 * 毕业专业答辩安排
 * @author eenet09
 *
 */
public interface GjtGraduationDefencePlanService {
	
	public void insert(GjtGraduationDefencePlan entity);
	
	public void update(GjtGraduationDefencePlan entity);
	
	public GjtGraduationDefencePlan queryById(String id);
	
	public void delete(GjtGraduationDefencePlan entity);

}
