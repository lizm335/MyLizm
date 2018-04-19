package com.gzedu.xlims.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtCourseCheck;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;
import com.gzedu.xlims.service.base.BaseService;

public interface GjtCourseService extends BaseService<GjtCourse> {

	Page<GjtCourse> queryAll(String schoolId, Map<String, Object> searchParams, PageRequest pageRequst);

	Page<GjtCourse> queryAllAndShare(String schoolIds, Map<String, Object> searchParams, PageRequest pageRequst,
			List<String> coursIds);

	GjtCourse queryBy(String id);

	GjtCourse queryByKch(String kch, String isDeleted);

	void update(GjtCourse entity);

	void delete(Iterable<String> ids);

	void syncCourse(String appId, Iterable<String> ids);

	void syncCourse(String appId, GjtCourse entity);

	/**
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<GjtCourse> queryPage(Map<String, Object> searchParams, PageRequest pageRequst);

	List<GjtCourse> findByXxid(String xxid);

	long queryIsEnabledNum(String isEnabled);

	List<GjtCourse> findByXxidAndIsDeleted(String xxid, String isDeleted);

	List<GjtCourse> findAll(Iterable<String> ids);

	List<GjtCourse> findByKchAndXxId(String kch, String xxid);

	/**
	 * 查询教学计划未选择的课程
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月12日 下午4:32:31
	 * @param gradeId
	 * @param specialtyId
	 */
	List<GjtCourse> queryByGradeIdAndSpecialtyId(String gradeId, String specialtyId);

	/**
	 * 查询学习平台的课程列表数据
	 * 
	 */
	Page getPcourseList(Map formMap, PageRequest pageRequst);

	/**
	 * 查询学习平台课程 阶段
	 * 
	 * @param formMap
	 * @return
	 */
	List<Map<String, Object>> queryCourseStage(Map<String, Object> formMap);

	/**
	 * 查询学习平台课程区块
	 * 
	 * @param formMap
	 * @return
	 */
	List<Map<String, Object>> queryCourseStageArea(Map<String, Object> formMap);

	/**
	 * 查询学习平台课程 章节
	 * 
	 * @param formMap
	 * @return
	 */
	List<Map<String, Object>> queryCourseSection(Map<String, Object> formMap);

	/**
	 * 提交验收记录
	 * 
	 * @return
	 */
	boolean sumbitCheck(GjtCourseCheck item);

	/**
	 * 修改验收状态
	 * 
	 * @param taskId
	 * @param status
	 * @return
	 */
	void updateCheck(String[] taskId, String status);

	long checkCourseCount(String courseId, String doFish);

	List<Map<String, Object>> queryCourseIsEnabled();

	void updateIsEnabled(String courseId, String status);

	void updateIsEnabled(String courseId, String status, String userName);

	/**
	 * 根据课程号查询补考费用
	 * 
	 * @param kch
	 * @return
	 */
	String queryExamFeeByKch(String kch);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年2月1日 上午10:49:41
	 * @param courseId
	 * @return
	 */
	List<GjtTextbook> queryCourseTextbook(String courseId);
	
	/**
	 * 查询当前院校下课程性质为正式课程中课程类型为统考课程的信息
	 * @param id
	 * @param string
	 * @param string2
	 * @param booleanNo
	 * @return
	 */
	List<GjtCourse> findByXxIdAndCourseNatureAndCourseCategoryAndIsDeleted(String xxId, String courseNature, int courseCategory,
			String booleanNo);

}
