/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service.exam;

import com.ouchgzee.headTeacher.dto.StudentLearnDto;
import com.ouchgzee.headTeacher.dto.StudentRecResultDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtExamPoint;
import com.ouchgzee.headTeacher.pojo.BzrGjtRecResult;
import com.ouchgzee.headTeacher.service.base.BaseService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 预约选课考试信息操作接口<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月13日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated public interface BzrGjtRecResultService extends BaseService<BzrGjtRecResult> {

	/**
	 * 分页根据条件查询学员预约选课考试信息
	 * 
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<StudentRecResultDto> queryStudentRecResultByPage(String classId, Map<String, Object> searchParams,
			PageRequest pageRequest);

	/**
	 * 获取学员每学期的预约考试详情
	 * 
	 * @param studentId
	 * @return
	 */
	List<Map> queryStudentRecResultDetail(String studentId);

	/**
	 * 导出学员的课程预约情况<br>
	 * 1.根据条件查询出数据<br>
	 * 2.生成设置Excel流<br>
	 * 3.写入数据<br>
	 *
	 * @param studentId
	 * @return Excel文件流
	 */
	HSSFWorkbook exportStudentRecResultDetailToExcel(String studentId);

	/**
	 * 分页根据条件查询班级学员的学习情况
	 * 
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<StudentLearnDto> queryLearningSituationByClassIdPage(String classId, Map<String, Object> searchParams,
			PageRequest pageRequest);

	/**
	 * 获取课程班学员的学习情况[总学员数、进度正常数、进度落后数、成绩合格数、成绩不合格数、通过数、未通过数]
	 * 
	 * @param teachClassId
	 *            教学班
	 * @param courseId
	 *            课程
	 * @return studentNum-总学员数 normalNum-进度正常数 backwardNum-进度落后数
	 *         qualifiedNum-成绩合格数 unQualifiedNum-成绩不合格数 passNum-通过数
	 *         unPassNum-未通过数
	 */
	Map countClassStudentLearningSituation(String teachClassId, String courseId);

	/**
	 * 导出班级学员的学习情况<br>
	 * 1.根据条件查询出数据<br>
	 * 2.生成设置Excel流<br>
	 * 3.写入数据<br>
	 *
	 * @param searchParams
	 * @param sort
	 * @return Excel文件流
	 */
	HSSFWorkbook exportLearningSituationToExcel(Map<String, Object> searchParams, Sort sort);

	/**
	 * 获取学员学情详情
	 * 
	 * @param studentId
	 * @return
	 */
	List<Map> queryStudentRecResultLearningDetail(String studentId);

	/**
	 * 导出学员的学情详情<br>
	 * 1.根据条件查询出数据<br>
	 * 2.生成设置Excel流<br>
	 * 3.写入数据<br>
	 *
	 * @param studentId
	 * @return Excel文件流
	 */
	HSSFWorkbook exportStudentRecResultLearningDetailToExcel(String studentId);

	/**
	 * 获取学员的预约情况[可预约、已预约]
	 * 
	 * @param studentId
	 * @return
	 */
	Object[] countStudentReserveSituation(String studentId);

	/**
	 * 获取班级待预约考试的学员数
	 * 
	 * @param classId
	 * @return
	 */
	long countClassStudentWaitExam(String classId);

	/**
	 * 获取班级待预约考点的学员数
	 * 
	 * @param classId
	 * @return
	 */
	long countClassStudentWaitExamPoint(String classId);

	/**
	 * 预约考试
	 *
	 * @param studentId
	 *            学员ID
	 * @param recId
	 *            预约选课ID
	 * @param updatedBy
	 *            修改人
	 * @return
	 */
	boolean updateRecExamCourse(String studentId, String recId, String updatedBy);

	/**
	 * 批量预约考试
	 *
	 * @param studentId
	 *            学员ID
	 * @param recIds
	 *            多个预约选课ID
	 * @param updatedBy
	 *            修改人
	 * @return
	 */
	boolean updateRecExamCourse(String studentId, String[] recIds, String updatedBy);

	/**
	 * 取消预约考试
	 *
	 * @param studentId
	 *            学员ID
	 * @param recId
	 *            预约选课ID
	 * @param updatedBy
	 *            修改人
	 * @return
	 */
	boolean updateCancelRecExamCourse(String studentId, String recId, String updatedBy);

	// -------------------------------- 考点信息 -------------------------------- //

	/**
	 * 分页根据条件查询考点信息
	 * 
	 * @param searchParams
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<BzrGjtExamPoint> queryExamPointByPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 根据条件查询考点信息
	 * 
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	List<BzrGjtExamPoint> queryExamPointBy(Map<String, Object> searchParams, Sort sort);

	/**
	 * 预约考点
	 *
	 * @param studentId
	 *            学员ID
	 * @param termId
	 *            学期ID
	 * @param examPointId
	 *            考点ID
	 * @param user
	 *            修改人
	 * @param pointName
	 *            考点名称
	 * @return
	 */
	boolean updateRecExamPoint(String studentId, String termId, String examPointId, String user);

	/**
	 * 根据学员信息查询详细的学员信息
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> queryStudent(Map<String, Object> map);

	/**
	 * 根据学员id查询每个学期信息
	 * 
	 * @param id
	 * @return
	 */
	List<Map<String, Object>> queryTerm(String id);

	/**
	 * 每个学期的明细
	 * 
	 * @param id
	 * @param s
	 * @return
	 */
	List<Map<String, Object>> queryStudentSourceDetail(String id, String term);

	/**
	 * 查询学分
	 * 
	 * @param id
	 * @param specialtyId
	 * @return
	 */
	List getCreditInfoAnd(String id, String specialtyId);

	/**
	 * 学员学情详情页-- 学分详情
	 * @param params
	 * @return
	 */
	List getCreditInfoAnd(Map<String,Object> params);

	List getPassCreditInfoAnd(String id, String specialtyId);

	List getMinAndSum(String id, String specialtyId);

	/**
	 * 获取班级学员的学情
	 * 
	 * @param searchParams
	 * @return
	 */
	Page<Map<String, Object>> queryLearningSituations(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 根据条件查询全部学员学情
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryAllLearningSituations(Map<String, Object> searchParams);

	/**
	 * 根据条件获取此班级所有学员所有科目的考试预约情况列表
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<Map<String, Object>> queryStudentRecResultPage(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 根据条件获取此班级学员的全部预约信息
	 * 
	 * @param searchParams
	 * @return
	 */
	List<Map<String, Object>> queryStudentRecResults(Map<String, Object> searchParams);

	/**
	 * 根据班级的条件信息导出班级的考试预约信息
	 * 
	 * @param searchParams
	 * @return
	 */
	HSSFWorkbook exportStudentRecResultsToExcel(Map<String, Object> searchParams);

	/**
	 * 根据教学计划获取此教学计划里面拥有的考试方式
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryExamTypeByTeachplan();

	/**
	 * 根据条件获取教学班级学员的课程学情
	 * 
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<Map<String, Object>> getCourseListPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 获取班级数据用于导出
	 * 
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> getCourseList(Map<String, Object> params);

	/**
	 * 查询学员的登录，时长，学习进度，学习成绩等状态数量
	 * 
	 * @param studentId
	 * @return
	 */
	Map<String, Object> queryStuStudyRecord(String studentId);

	/**
	 * 导出课程学情
	 * 
	 * @param classId
	 * @return
	 */
	HSSFWorkbook exportStuCourse(String classId);

	/**
	 * 查询学员的学情总体情况统计，以及个人的一些基本信息
	 * 
	 * @param params
	 * @return
	 */
	Map<String, Object> queryStuStudyCondition(Map<String, Object> params);

	/**
	 * 课程学情总览
	 * 
	 * @param params
	 * @return
	 */
	Map<String, Object> courseConditionDetials(Map<String, Object> params);

	/**
	 * 查询出全部选课
	 * @return
	 */
	List<Map<String,Object>> queryAllRecResults();

	/**
	 * 更新用户选课的主要应用设备信息
	 * @param data
	 */
	boolean updateRecResultDeviceMsg(Map<String, Object> data);

	/**
	 * 课程考情详情页
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
    Page<Map<String,Object>> courseLearnConditionDetails(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 学支平台--首页批量导出学生学情。
	 * @param searchParams
	 * @return
	 */
	List<File> getClassConditons(Map searchParams) throws ExecutionException, InterruptedException;

	/**
	 * 学支平台--首页批量导出班级的学员信息
	 * @param searchParams
	 * @return
	 */
    List<File> getClassStudInfo(Map searchParams) throws ExecutionException, InterruptedException;
}
