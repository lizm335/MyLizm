/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service.student;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.ouchgzee.headTeacher.dto.CountLoginDto;
import com.ouchgzee.headTeacher.dto.StudentClockingInDto;
import com.ouchgzee.headTeacher.dto.StudentPaymentDto;
import com.ouchgzee.headTeacher.dto.StudentSignupInfoDto;
import com.ouchgzee.headTeacher.dto.StudentStateDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtClassInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtGrade;
import com.ouchgzee.headTeacher.pojo.BzrGjtSpecialty;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentEnteringSchool;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;
import com.ouchgzee.headTeacher.pojo.BzrTblPriLoginLog;
import com.ouchgzee.headTeacher.service.base.BaseService;

/**
 * 学员业务接口<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年4月26日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated
public interface BzrGjtStudentService extends BaseService<BzrGjtStudentInfo> {

	/**
	 * 获取学员姓名
	 * 
	 * @param studentId
	 * @return
	 */
	String queryXmById(String studentId);

	/**
	 * 更新学员信息
	 * 
	 * @param studentInfo
	 * @return
	 */
	boolean update(BzrGjtStudentInfo studentInfo);

	/**
	 * 账号重置密码
	 * 
	 * @param studentId
	 *            学员ID
	 * @param updatedBy
	 *            修改人
	 * @return
	 */
	boolean updateStudentResetPwd(String studentId, String updatedBy);

	boolean updateStudentResetPwdNew(String studentId, String updatedBy);

	/**
	 * 班主任确认学员入学
	 *
	 * @param info
	 *            学员ID
	 * @param bzrId
	 *            班主任ID
	 * @return
	 */
	boolean updateStudentEnteringSchool(BzrGjtStudentEnteringSchool info, String bzrId);

	/**
	 * 分页根据条件查询班级的学员信息
	 * 
	 *
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<BzrGjtStudentInfo> queryStudentInfoByClassIdPage(String classId, Map<String, Object> searchParams,
			PageRequest pageRequest);

	/**
	 * 根据条件查询班级的学员信息
	 * 
	 * @param classId
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	List<BzrGjtStudentInfo> queryStudentInfoByClassId(String classId, Map<String, Object> searchParams, Sort sort);

	/**
	 * 分页根据条件查询班级的学员信息/报读信息
	 * 
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<BzrGjtStudentInfo> queryStudentInfoSpecialtyByClassIdPage(String classId, Map<String, Object> searchParams,
			PageRequest pageRequest);

	/**
	 * 导出学员信息/报读信息<br>
	 * 1.根据条件查询出数据<br>
	 * 2.生成设置Excel流<br>
	 * 3.写入数据<br>
	 *
	 * @param classId
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	HSSFWorkbook exportStudentInfoSpecialtyToExcel(String classId, Map<String, Object> searchParams, Sort sort);

	/**
	 * 分页根据条件查询班级的学员状态
	 * 
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<StudentStateDto> queryStudentStateByClassIdPage(String classId, Map<String, Object> searchParams,
			PageRequest pageRequest);

	/**
	 * 未确认入学学员数
	 * 
	 * @param classId
	 * @return
	 */
	long countNotLearningStudentByClassId(String classId);

	/**
	 * 未完善资料学员数
	 * 
	 * @param classId
	 * @return
	 */
	long countNotPerfectStudentByClassId(String classId);

	/**
	 * 可申请毕业学员数
	 * 
	 * @param classId
	 * @return
	 */
	long countGraduateStudentByClassId(String classId);

	/**
	 * 导出班级学员的状态信息<br>
	 * 1.根据条件查询出数据<br>
	 * 2.生成设置Excel流<br>
	 * 3.写入数据<br>
	 *
	 * @param classId
	 * @param searchParams
	 * @param sort
	 * @return Excel文件流
	 */
	HSSFWorkbook exportStudentStateToExcel(String classId, Map<String, Object> searchParams, Sort sort);

	/**
	 * 分页根据条件查询班级的学员考勤情况
	 * 
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<StudentClockingInDto> queryStudentClockingInByClassIdPage(String classId, Map<String, Object> searchParams,
			PageRequest pageRequest);

	/**
	 * 统计学员的考勤
	 * 
	 * @param username
	 * @return
	 */
	StudentClockingInDto countStudentClockingInSituation(String username);

	/**
	 * 学员考勤明细
	 * 
	 * @param username
	 * @return
	 */
	StudentClockingInDto countStudentClockingInSituation(String username, String classId);

	/**
	 * 统计班级学员登录情况 [总人数、当前在线人数、今日登录人数、从未学习人数、三天以内登录人数、三天以上未登录人数、七天以上未登录人数]
	 *
	 * @param classId
	 * @param queryParams
	 * @return studentNum-总人数 onLineLoginNum-当前在线人数 todayLoginNum-今日登录人数
	 *         notLoginNum-从未学习人数 withinThreeDayNotLoginNum-三天以内登录人数
	 *         moreThanThreeDayNotLoginNum-三天以上未登录人数
	 *         moreThanSevenDayNotLoginNum-七天以上未登录人数
	 */
	Map countStudentClockingInSituationByClass(String classId, Map<String, Object> queryParams);

	/**
	 * 统计班级学员学习情况 [学习总次数、学习总时长]
	 *
	 * @param classId
	 * @param queryParams
	 * @return studyNum-学习总次数 studyHourNum-学习总时长
	 */
	Map countStudySituationByClass(String classId, Map<String, Object> queryParams);

	/**
	 * 按照课程统计班级学员登录情况 [总人数、当前在线人数、今日登录人数、从未学习人数、三天以内登录人数、三天以上未登录人数、七天以上未登录人数]
	 * [学习总次数、学习总时长]
	 * 
	 * @param classId
	 * @param courseId
	 * @return studentNum-总人数 onLineLoginNum-当前在线人数 todayLoginNum-今日登录人数
	 *         notLoginNum-从未学习人数 withinThreeDayNotLoginNum-三天以内登录人数
	 *         moreThanThreeDayNotLoginNum-三天以上未登录人数
	 *         moreThanSevenDayNotLoginNum-七天以上未登录人数 studyNum-学习总次数
	 *         studyHourNum-学习总时长
	 */
	Map countClockInSituationByCourseClass(String classId, String courseId);

	/**
	 * 按照课程统计班级学员课程学情 [总人数、总学习进度、总学习成绩、学习总次数、学习总时长] [学习总次数、学习总时长]
	 * 
	 * @param classId
	 * @param courseId
	 * @return studentNum-总人数 totalSchedule-总学习进度 totalStudyScore-总学习成绩
	 *         studyNum-学习总次数 studyHourNum-学习总时长
	 */
	Map countLearnSituationByCourseClass(String classId, String courseId, Map<String, Object> queryParams);

	/**
	 * 统计班级学员最近两周的每日登录情况
	 * 
	 * @param classId
	 * @return dayName-日期 studentLoginNum-登录人数
	 */
	List<CountLoginDto> countTwoWeeksClockInSituation(String classId);

	/**
	 * 统计班级学员当年的每日登录情况
	 * 
	 * @param classId
	 * @return dayName-日期 studentLoginNum-登录人数
	 */
	List<CountLoginDto> countThisYearClockInSituation(String classId);

	/**
	 * 获取班级学员的考试情况[总学员数、通过数、未通过数]
	 * 
	 * @param teachClassId
	 *            教学班
	 * @param classId
	 *            课程班
	 * @return studentNum-总学员数 passNum-通过数 unPassNum-未通过数
	 */
	Map countClassStudentExamSituation(String teachClassId, String classId);

	/**
	 * 导出班级学员的考勤情况<br>
	 * 1.根据条件查询出数据<br>
	 * 2.生成设置Excel流<br>
	 * 3.写入数据<br>
	 *
	 * @param classId
	 * @param searchParams
	 * @param sort
	 * @return Excel文件流
	 */
	HSSFWorkbook exportStudentClockingInToExcel(String classId, Map<String, Object> searchParams, Sort sort);

	/**
	 * 分页根据条件查询班级的缴费信息
	 * 
	 * @param classId
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<StudentPaymentDto> queryPaymentSituationByClassIdPage(String classId, Map<String, Object> searchParams,
			PageRequest pageRequest);

	/**
	 * 导出班级学员的缴费信息<br>
	 * 1.根据条件查询出数据<br>
	 * 2.生成设置Excel流<br>
	 * 3.写入数据<br>
	 *
	 * @param classId
	 * @param searchParams
	 * @param sort
	 * @return Excel文件流
	 */
	HSSFWorkbook exportPaymentSituationToExcel(String classId, Map<String, Object> searchParams, Sort sort);

	/**
	 * 根据学员ID获取缴费信息
	 * 
	 * @param studentId
	 * @return
	 */
	Map queryPaymentSituationByStudentId(String studentId);

	/**
	 * 更改学员的学习状态
	 * 
	 * @param studentId
	 * @param payState
	 * @return
	 */
	boolean updateChangePaymentState(String studentId, int payState);

	/**
	 * 提交资料
	 * 
	 * @param studentId
	 * @param bzrId
	 * @return
	 */
	boolean signupSibmit(String studentId, String bzrId);

	/**
	 * 上传学员的证件资料
	 * 
	 * @param studentId
	 * @param fileType
	 * @param url
	 * @return
	 */
	boolean updateSignupCopyData(String studentId, String fileType, String url);

	/**
	 * 上传学员的证件资料
	 * 
	 * @param studentId
	 * @param copyData
	 * @return
	 */
	boolean updateSignupCopyData(String studentId, Map<String, String> copyData);

	/**
	 * 获取学员已修学分情况
	 * 
	 * @param studentXh
	 * @param specialtyId
	 * @return XF-学分 ZDXF-最低学分 YXXF-已修学分
	 */
	Map getMinAndSum(String studentXh, String specialtyId);

	/**
	 * 获取学校的年级信息
	 *
	 * @param xxId
	 *            学校ID
	 * @return
	 */
	List<BzrGjtGrade> queryGradeBy(String xxId);

	/**
	 * 获取年级信息
	 *
	 * @param termId
	 * @return
	 */
	BzrGjtGrade queryGradeById(String termId);

	/**
	 * 获取学校的专业信息
	 *
	 * @param xxId
	 *            学校ID
	 * @return
	 */
	List<BzrGjtSpecialty> querySpecialtyBy(String xxId);

	/**
	 * 获取学员的证件资料
	 * 
	 * @param studentId
	 * @return
	 */
	Map<String, String> getSignupCopyData(String studentId);

	/**
	 * 处理学员登录详情明细
	 * 
	 * @param infos
	 * @return
	 */
	HSSFWorkbook exportInfoDetails(List<BzrTblPriLoginLog> infos);

	/**
	 * 分页条件查询学员的学籍资料
	 *
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<StudentSignupInfoDto> queryStudentSignupInfoByPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 统计院校的学员资料审核状态人数
	 *
	 * @param searchParams
	 * @return
	 */
	Map<String, BigDecimal> countGroupbyAuditState(Map<String, Object> searchParams);

	/**
	 * 统计院校的学员资料完善状态人数
	 *
	 * @param searchParams
	 * @return
	 */
	Map<String, BigDecimal> countGroupbyPerfectStatus(Map<String, Object> searchParams);

	/**
	 * 统计院校的学员入学确认状态人数
	 *
	 * @param searchParams
	 * @return
	 */
	Map<String, BigDecimal> countGroupbyIsEnteringSchool(Map<String, Object> searchParams);

	/**
	 * 统计院校的学员学籍状态人数
	 *
	 * @param searchParams
	 * @return
	 */
	Map<String, BigDecimal> countGroupbyXjzt(Map<String, Object> searchParams);

	/**
	 * 获取学员的课程考勤统计
	 * 
	 * @param searchParams
	 * @return
	 */
	Map getStudentLoginDetail(Map searchParams);

	/**
	 * 根据学生id获取学生的教务班信息
	 * 
	 * @param studentId
	 * @return
	 */
	BzrGjtClassInfo queryTeachClassInfoByStudetnId(String studentId);

	/**
	 * 导出学员考勤统计表
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadAttendanceListXls(Map searchParams);

	/**
	 * 导出学员学习记录明细表
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadAttendanceDetailXls(Map searchParams);

	/**
	 * 获取当前学期及之前的学期
	 * 
	 * @param xxId
	 * @return
	 */
	List<BzrGjtGrade> findCurrentTermAndBeforeTerm(String xxId);

	/**
	 * 获取当前学期及之前的学期(不超过某学籍)
	 * 
	 * @param xxId
	 * @return
	 */
	List<BzrGjtGrade> findCurrentTermAndBeforeTerm(String xxId, String termId);

	/**
	 * 获取班级学员的学习排行Top10-学习进度
	 * 
	 * @param teachClassId
	 * @param courseId
	 * @return
	 */
	List<Map> queryStudyRankingTopTenScheduleByCourseClass(String teachClassId, String courseId,
			Map<String, Object> queryParams);

	/**
	 * 获取班级学员的学习排行Top10-学习次数
	 * 
	 * @param teachClassId
	 * @param courseId
	 * @return
	 */
	List<Map> queryStudyRankingTopTenStudyNumByCourseClass(String teachClassId, String courseId,
			Map<String, Object> queryParams);

	/**
	 * 获取班级学员的学习排行Top10-学习时长
	 * 
	 * @param teachClassId
	 * @param courseId
	 * @return
	 */
	List<Map> queryStudyRankingTopTenStudyHourByCourseClass(String teachClassId, String courseId,
			Map<String, Object> queryParams);

	/**
	 * 根据studentId查找atid
	 * 
	 * @param ids
	 * @return
	 */
	List<Object[]> queryAtidByIds(String... ids);

	/**
	 * 获取入学确认信息
	 * 
	 * @param studentId
	 * @return
	 */
	BzrGjtStudentEnteringSchool queryStudentEnteringSchoolByStudentId(String studentId);

	/**
	 * 学习管理--》考勤分析--》课程考勤列表
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<Map<String, Object>> courseAttendanceList(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 课程考勤详情页
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<Map<String, Object>> courseAttendanceDetails(Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * 课程考勤列表下载
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook courseAttendanceList(Map searchParams);

	/**
	 * 课程考勤详情列表下载
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook courseAttendanceDetails(Map searchParams);

	/**
	 * 课程学情列表下载
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook getCourseList(Map searchParams);

	/**
	 * 课程学情详情下载
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook courseLearnConditionDetails(Map searchParams);

	/**
	 * 学员考勤列表
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page getStudentLoginList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学员考勤统计
	 * 
	 * @param searchParams
	 * @return
	 */
	int getStudentLoginCount(Map searchParams);

	/**
	 * 学支平台--首页批量下载班级学员信息
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook getClassStudInfo(Map<String, Object> searchParams);
}
