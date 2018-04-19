package com.ouchgzee.study.service.course;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface CourseLearningService {

	@SuppressWarnings("rawtypes")
	/**
	 * 课程学习
	 * @param searchParams
	 * @return
	 */
	Map courseLearning(Map<String, Object> searchParams);

	/**
	 * 课程学习(新)
	 * @param searchParams
	 * @return
	 */
	Map getCourseLearningData(Map<String,Object> searchParams);


	/**
	 * 课程学习(院校模式)
	 * @param searchParams
	 * @return
	 */
	Map acadeMyLearnCourse(Map<String,Object> searchParams);

	/**
	 * 课程学习(院校模式--考试模式)
	 * @param searchParams
	 * @return
	 */
	Map acadeMyLearnCourseData(Map<String,Object> searchParams);

	/**
	 * 个人中心-首页-学习排名top5
	 * @param searchParams
	 * @return
	 */
	List<Map<String, Object>> getStudyRank(Map<String, Object> searchParams);

	/**
	 * 根据当前期查学习排名
	 * @param searchParams
	 * @return
	 */
	Map getStudyRankByTerm(Map<String, Object> searchParams);
	
	/**
	 * 查询出自己击败多少人。
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map getOrderPM(Map<String, Object> searchParams);
	
	/**
	 * 查询进度排名
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
//	Map getProgressOrder(Map<String, Object> searchParams);
	List getProgressOrder(Map<String, Object> searchParams);
	
	/**
	 * 课程学习
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map learningByTerm(Map<String, Object> searchParams);
	
	/**
	 * 更新选课记录状态为学习中，插入重修记录表
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map learningRepair(Map<String, Object> searchParams);
	
	/**
	 * 课程数据统计(提供给移动端)
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map getCourseAndTermData(Map<String, Object> searchParams);
	
	/**
	 * 统计在学课程人数
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map getPersonalCountByCourse(Map<String, Object> searchParams);
	
	/**
	 * 根据教学计划得到课程学习结果
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map getCourseResult(Map<String, Object> searchParams);

	/**
	 * 课程学习(无考试模式)
	 * @param searchParams
	 * @return
	 */
	Map acadeMyLearningByNotExam(Map<String,Object> searchParams);


	Map getGrantCourseCert(Map<String,Object> searchParams);

	/**
	 * 查询授课计划
	 * @param searchParams
	 * @return
	 */
	Map getGrantCousePlan(Map<String,Object> searchParams);

	/**
	 * 查询授课凭证
	 * @param searchParams
	 * @return
	 */
	Map getGrantCourseCertData(Map<String,Object> searchParams);
	
	/**
	 * 下载成绩单
	 */
	void downGradesExcel(Map formMap, HttpServletRequest request, HttpServletResponse response, String basePath);

	/**
	 * 查询直播列表
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月9日 上午10:29:48
	 * @param lessonName
	 * @param status
	 * @param pageRequest
	 * @return
	 */
	Page<Map<String, Object>> queryOnlineLessonList(String studentId, String lessonName, Integer status, Integer lessonType, PageRequest pageRequest);

}
