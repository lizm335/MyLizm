package com.gzedu.xlims.service.edumanage;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtCustomCourse;

public interface GjtCustomCourseService {

	public Page<Map<String, Object>> queryAll(Map<String, Object> params,PageRequest pageRequst);

	public Page<Map<String, Object>> queryAuditAll(Map<String, Object> params, PageRequest pageRequst);

	public GjtCustomCourse findByTeachPlanIdAndOrgId(String teachPlanId, String orgId);

	public void save(GjtCustomCourse customCourse);

	public GjtCustomCourse findOne(String id);

}
