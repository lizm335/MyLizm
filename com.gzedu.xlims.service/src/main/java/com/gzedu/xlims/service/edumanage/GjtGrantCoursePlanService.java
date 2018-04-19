package com.gzedu.xlims.service.edumanage;

import com.gzedu.xlims.pojo.GjtGrantCoursePlan;

public interface GjtGrantCoursePlanService {
	
	public GjtGrantCoursePlan findOne(String id);

	public void save(GjtGrantCoursePlan plan);

	public void save(Iterable<GjtGrantCoursePlan> entities);

	public void delete(Iterable<GjtGrantCoursePlan> entities);

}
