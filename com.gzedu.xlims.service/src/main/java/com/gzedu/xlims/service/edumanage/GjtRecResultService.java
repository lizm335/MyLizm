/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.edumanage;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtRecResult;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 功能说明： 成绩记录
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月16日
 * @version 2.5
 *
 */
public interface GjtRecResultService extends BaseService<GjtRecResult> {

	List<GjtRecResult> queryAll(Iterable<String> ids);

	Page<GjtRecResult> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst);

	Page<Map<String, Object>> queryAllBySql(String orgId, Map<String, Object> searchParams, PageRequest pageRequst);

	GjtRecResult queryBy(String id);

	// void insert(GjtRecResult entity);

	// void update(GjtRecResult entity);

	// void delete(String id);

	// void delete(Iterable<String> ids);

	GjtRecResult queryByStudentIdAndCourseName(String studentId, String courseName);

	GjtRecResult queryByStudentId(String studentId, String teachPlanId);

	Map<String, Object> queryStudent(String id);

	List<Map<String, String>> queryStudentSourceDetail(String id, String term);

	List<Map<String, String>> queryTerm(String id);

	List<Map<String, String>> getMinAndSum(String id, String specialtyId);

	List<Map<String, String>> getPassCreditInfoAnd(String id, String specialtyId);

	List<Map<String, String>> getCreditInfoAnd(String id, String specialtyId);

	/**
	 * 根据学生id查询教学班学生期信息
	 * 
	 * @param student_id
	 * @return
	 */
	List<Map<String, String>> queryTeachTerm(String student_id);

	/**
	 * 根据学生id，期id信息获取教学班学生的课程学习信息详情
	 * 
	 * @param student_id
	 * @param term_id
	 * @return
	 */
	List<Map<String, String>> queryTeachStudentSourceDetail(String student_id, String term_id);

	/**
	 * 首页 成绩与学分--学分详情
	 * 
	 * @param param
	 * @return
	 */
	List<Map<String, String>> getCreditDetail(Map<String, Object> param);

	/**
	 * 查询学生学分
	 * 
	 * @param StudentId
	 * @return
	 */
	public List<Map<String, String>> queryStudentCredit(String StudentId);

	/**
	 * 首页 成绩与学分-成绩详情--查看历史成绩
	 * 
	 * @param formMap
	 * @return
	 */
	List<Map<String, Object>> getHistoryScore(Map<String, Object> formMap);

	GjtRecResult findByStudentIdAndTeachPlanIdAndCourseId(String studentId, String teachPlanId, String courseId);

	GjtRecResult findByStudentIdAndTeachPlanId(String studentId, String teachPlanId);

	GjtRecResult update(GjtRecResult entity);

	/**
	 * 查询学员学情详情
	 * 
	 * @param params
	 * @return
	 */
	Map<String, Object> queryStuStudyCondition(Map<String, Object> params);

	/**
	 * 学员课程学情详情
	 * 
	 * @param params
	 * @return
	 */
	List<Map<String, String>> queryStudentRecResultLearningDetail(Map<String, Object> params);

	/**
	 * 学员课程学情明细
	 * 
	 * @param params
	 * @return
	 */
	Map<String, Object> courseConditionDetials(Map<String, Object> params);

	/**
	 * 获取班级学员的学情
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<Map<String, Object>> queryLearningSituations(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 查询成绩与学分成绩总览
	 * 
	 * @param seachParams
	 * @return
	 */
	Map<String, Object> queryResultsOverview(Map<String, Object> seachParams);

	/**
	 * 学分详情
	 * 
	 * @param params
	 * @return
	 */
	List getCreditInfoAnd(Map<String, Object> params);

	/**
	 * 删除学员的选课信息
	 * 
	 * @param studentId
	 */
	Boolean deleteByStudentId(String studentId);

	/**
	 * 补考费-更改为已缴费
	 * 
	 * @param recId
	 * @param paySn
	 * @return
	 */
	boolean updatePayState(String recId, String paySn);

	boolean updateRebuildState(String recId);

	long countByTermCourseId(String termCourseId);

	List<Map<String, Object>> queryGradeCourseStudentNo(Map<String, Object> parm);
}
