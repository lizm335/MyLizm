package com.gzedu.xlims.service.practice;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.practice.GjtPracticeApply;

public interface GjtPracticeApplyService {

	public Page<GjtPracticeApply> findAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst);

	public long count(Map<String, Object> searchParams);

	public GjtPracticeApply insert(GjtPracticeApply entity);

	public void update(GjtPracticeApply entity);

	public GjtPracticeApply findOne(String id);

	public GjtPracticeApply findByPracticePlanIdAndStudentId(String practicePlanId, String studentId);

	public List<GjtPracticeApply> findPracticePlanIdByStudentId(String studentId, String gradeId);

	public List<GjtPracticeApply> findByPracticePlanIdAndSpecialtyBaseId(String practicePlanId, String specialtyBaseId);

	public GjtPracticeApply findCompletedApply(String studentId);

	/**
	 * 查询是否可以申请
	 * 
	 * @param orgId
	 * @param gradeId
	 * @param gradeSpecialtyId
	 * @param studentId
	 * @return
	 */
	public boolean getCanApply(String orgId, String gradeId, String gradeSpecialtyId, String studentId);

	public Float getScore(String orgId, String gradeId, String gradeSpecialtyId, String studentId);

	/**
	 * 查询老师指导记录
	 * 
	 * @param orgId
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> findTeacherGuideList(String orgId, Map<String, Object> searchParams,
			PageRequest pageRequst);

	/**
	 * 更新课程分数
	 * 
	 * @param studentId
	 * @param score
	 */
	public void updateScore(String studentId, float score);

	/**
	 * 查询我的指导列表
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<GjtPracticeApply> queryMyGuideList(Map<String, Object> searchParams, PageRequest pageRequst);

}
