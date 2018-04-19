package com.gzedu.xlims.service.practice;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.practice.GjtPracticeArrange;

public interface GjtPracticeArrangeService {
	
	public Page<GjtPracticeArrange> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public GjtPracticeArrange insert(GjtPracticeArrange entity);
	
	public void insert(List<GjtPracticeArrange> entities);

	public void update(GjtPracticeArrange entity);
	
	public GjtPracticeArrange findOne(String id);
	
	/**
	 * 查询可申请社会实践的专业
	 * @param orgId
	 * @param gradeId
	 * @param practicePlanId
	 * @return
	 */
	public List<Map<String, Object>> getCanApplySpecialty(String orgId, String gradeId, String practicePlanId);
	
	/**
	 * 查询可申请社会实践的学生
	 * @param orgId
	 * @param gradeId
	 * @param practicePlanId
	 * @param specialtyBaseId
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> getCanApplyStudent(String orgId, String gradeId, String practicePlanId, String specialtyBaseId,
			Map<String, Object> searchParams, PageRequest pageRequst);
	
	/**
	 * 查询指导老师的指导人数
	 * @param practicePlanId
	 * @param teacherId
	 * @param specialtyBaseId
	 * @return
	 */
	public Map<String, Object> getGuideNum(String practicePlanId, String teacherId, String specialtyBaseId);
	
	/**
	 * 查询上期有未通过社会实践学员的指导老师
	 * @param orgId
	 * @param gradeId
	 * @param specialtyBaseId
	 * @return
	 */
	public List<Map<String, Object>> getPreNoPass(String orgId, String gradeId, String specialtyBaseId);

}
