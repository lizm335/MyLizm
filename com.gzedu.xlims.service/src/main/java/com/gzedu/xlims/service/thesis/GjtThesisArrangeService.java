package com.gzedu.xlims.service.thesis;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.thesis.GjtThesisArrange;

public interface GjtThesisArrangeService {
	
	public Page<GjtThesisArrange> findAll(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public GjtThesisArrange insert(GjtThesisArrange entity);
	
	public void insert(List<GjtThesisArrange> entities);

	public void update(GjtThesisArrange entity);
	
	public void updateDefenceTeacher(GjtThesisArrange entity);
	
	public GjtThesisArrange findOne(String id);
	
	public GjtThesisArrange findByThesisPlanIdAndSpecialtyBaseId(String thesisPlanId, String specialtyBaseId);
	
	/**
	 * 查询可申请毕业论文的专业
	 * @param orgId
	 * @param gradeId
	 * @param thesisPlanId
	 * @return
	 */
	public List<Map<String, Object>> getCanApplySpecialty(String orgId, String gradeId, String thesisPlanId);
	
	/**
	 * 查询可申请毕业论文的学生
	 * @param orgId
	 * @param gradeId
	 * @param thesisPlanId
	 * @param specialtyBaseId
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> getCanApplyStudent(String orgId, String gradeId, String thesisPlanId, String specialtyBaseId,
			Map<String, Object> searchParams, PageRequest pageRequst);
	
	/**
	 * 查询指导老师的指导人数
	 * @param thesisPlanId
	 * @param teacherId
	 * @param specialtyBaseId
	 * @return
	 */
	public Map<String, Object> getGuideNum(String thesisPlanId, String teacherId, String specialtyBaseId);
	
	/**
	 * 查询上期有未通过论文学员的指导老师
	 * @param orgId
	 * @param gradeId
	 * @param specialtyBaseId
	 * @return
	 */
	public List<Map<String, Object>> getPreNoPass(String orgId, String gradeId, String specialtyBaseId);

}
