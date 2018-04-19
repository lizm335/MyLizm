package com.gzedu.xlims.service.thesis;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.thesis.GjtThesisPlan;

public interface GjtThesisPlanService {

	public Page<GjtThesisPlan> findAll(Map<String, Object> searchParams, PageRequest pageRequst);

	public GjtThesisPlan insert(GjtThesisPlan entity);

	public void update(GjtThesisPlan entity);

	public GjtThesisPlan findOne(String id);

	public GjtThesisPlan findByGradeIdAndOrgId(String gradeId, String orgId);

	public GjtThesisPlan findByGradeIdAndOrgIdAndStatus(String gradeId, String orgId, int status);

	public List<GjtThesisPlan> findByOrgIdAndStatus(String orgId, int status);

	public Map<String, String> getThesisPlanMap(String orgId);

}
