package com.gzedu.xlims.service.practice;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.practice.GjtPracticePlan;

public interface GjtPracticePlanService {

	public Page<GjtPracticePlan> findAll(Map<String, Object> searchParams, PageRequest pageRequst);

	public GjtPracticePlan insert(GjtPracticePlan entity);

	public void update(GjtPracticePlan entity);

	public GjtPracticePlan findOne(String id);

	public GjtPracticePlan findByGradeIdAndOrgId(String gradeId, String orgId);

	public GjtPracticePlan findByGradeIdAndOrgIdAndStatus(String gradeId, String orgId, int status);

	public List<GjtPracticePlan> findByOrgIdAndStatus(String orgId, int status);

	public Map<String, String> getPracticePlanMap(String orgId);

}
