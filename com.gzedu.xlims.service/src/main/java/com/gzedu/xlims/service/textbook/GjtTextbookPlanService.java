package com.gzedu.xlims.service.textbook;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.textbook.GjtTextbookPlan;

public interface GjtTextbookPlanService {
	
	public Page<GjtTextbookPlan> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public GjtTextbookPlan insert(GjtTextbookPlan entity);

	public void update(GjtTextbookPlan entity);
	
	public GjtTextbookPlan findOne(String id);
	
	public GjtTextbookPlan findByPlanCodeAndOrgId(String planCode, String orgId);
	
	public GjtTextbookPlan findByGradeIdAndOrgId(String gradeId, String orgId);
	
	public GjtTextbookPlan findCurrentArrangePlan(String orgId);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年7月17日 下午5:50:13
	 * @param xxId
	 * @return
	 */
	public List<GjtTextbookPlan> findByOrgIdAndSysdate(String xxId, String gradeId);

}
