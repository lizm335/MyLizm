package com.gzedu.xlims.service.studymanage;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.TblPriLoginLog;

public interface StudyManageService {

	/**
	 * 学习管理=》成绩查询
	 * 
	 * @return
	 */
	Page getScoreList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学习管理=》成绩查询（查询条件统计项）
	 * 
	 * @return
	 */
	long getScoreCount(Map searchParams);

	/**
	 * 学习管理=》学分查询
	 * 
	 * @return
	 */
	Page getCreditsList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学习管理=》学分查询（查询条件统计项）
	 * 
	 * @return
	 */
	long getCreditsCount(Map searchParams);

	/**
	 * 学习管理=》课程学情
	 * 
	 * @return
	 */
	Page getCourseStudyList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学习管理=》课程班学情
	 * 
	 * @return
	 */
	Page getCourseClassList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学习管理=》教学班学情
	 * 
	 * @return
	 */
	Page getTeachClassList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学习管理=》学员课程学情
	 * 
	 * @return
	 */
	Page getStudentCourseList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学习管理=》学员课程学情统计
	 *
	 * @return
	 */
	long getStudentCourseCount(Map searchParams);

	/**
	 * 学习管理=》学员课程学情
	 * 
	 * @return
	 */
	List<Map<String, String>> getStudentCourseList(Map searchParams);

	/**
	 * 学习管理=》学员专业学情
	 * 
	 * @return
	 */
	Page getStudentMajorList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学习管理=》教务班考勤
	 * 
	 * @return
	 */
	Map getClassLoginList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学习管理=》学员考勤
	 * 
	 * @return
	 */
	Page getStudentLoginList(Map searchParams, PageRequest pageRequst);

	/**
	 * 考勤分析=》学员考勤(考勤详情)
	 * 
	 * @return
	 */
	Map getStudentLoginDetail(Map searchParams);

	/**
	 * 考勤分析=》课程班考勤
	 * 
	 * @return
	 */
	Page getCourseClassLoginList(Map searchParams, PageRequest pageRequst);

	/**
	 * 课程学情=>课程学情详情
	 * 
	 * @param seachParams
	 * @return
	 */
	Page getCourseStudyDetails(Map<String, Object> seachParams, PageRequest pageRequst);

	/**
	 * 课程班学情=》课程班学情明细
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page getCourseClassDetail(Map searchParams, PageRequest pageRequst);

	/**
	 * 处理课程学情下载
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadcourseStudyListExportXls(Map searchParams);

	/**
	 * 期课程答疑统计 只有APP014
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook downCourseSubjectExport(Map<String, Object> searchParams);

	/**
	 * 期课程主题讨论数据 只有APP014
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook downCourseActivityExport(Map<String, Object> searchParams);

	/**
	 * 考勤分析--》课程考勤
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page getCourseLoginList(Map searchParams, PageRequest pageRequst);

	/**
	 * 考勤分析--》课程考勤下载
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadcourseLoginListExportXls(Map searchParams);

	/**
	 * 考勤分析--》课程考勤--》课程考情详情
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page getCourseClockingDetail(Map searchParams, PageRequest pageRequst);
	
	
	/**
	 * 考勤分析--》课程考勤--》课程考情详情
	 *   
	 * 指定设备的在线数量
	 * 
	 * @param searchParams
	 * @return
	 */
	long getCourseClockingDetailCount(Map searchParams);
	

	/**
	 * 考勤分析--》课程考勤--》课程考情详情下载
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadClockingDetailExportXls(Map searchParams);

	/**
	 * 考勤分析--》学员课程考勤--》学员课程考勤详情下载
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadStudentDetailExportXls(Map searchParams);

	/**
	 * 考勤分析--》学员课程考勤--》学员课程考勤列表下载
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadStudentLoginListExportXls(Map searchParams);

	/**
	 * 考勤分析--》课程班考勤列表下载
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadCourseClassListExportXls(Map searchParams);

	Page<Map<String, Object>> getStudentStudyList(Map<String, Object> searchParams, PageRequest pageRequst);

	Page<Map<String, Object>> getStudentStudyList(Map<String, Object> searchParams, Map<String, String> orderMap,
			PageRequest pageRequst);

	/**
	 * 根据ATID查询学员专业信息
	 * 
	 * @param searchParams
	 * @return
	 */
	Map countStudyInfo(Map<String, Object> searchParams);

	/**
	 * 兼容专本连读情况,返回多条数据
	 * 
	 * @param searchParams
	 * @return
	 */
	Map countStudentLearnInfo(Map<String, Object> searchParams);

	/**
	 * 根据手机号查询学员身份证、姓名、学号
	 * 
	 * @param searchParams
	 * @return
	 */
	Map getStudentBaseInfo(Map<String, Object> searchParams);

	/**
	 * 根据机构编码获取该机构各专业的学员数量（专业名称、学员数量）
	 * 
	 * @param orgIdList
	 * @param specialtyName
	 */
	List<Map<String, Object>> countByOrgIdsAndSpecialtyName(List<String> orgIdList, String specialtyName);

	/**
	 * 学情分析--》课程班学情列表导出
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadCourseClassConditionListExportXls(Map searchParams);

	/**
	 * 学情分析--》学员学情分析列表导出
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadStudentCourseListExportXls(Map searchParams);

	/**
	 * 成绩查询-成绩列表导出
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadScoreListExportXls(Map searchParams);

	/**
	 * 管理后台--课程学情学情明细，班级学情学情明细，学员学情学情明细
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadCommonCourseDetailExportXls(Map searchParams);

	/**
	 * 查询学籍信息
	 * 
	 * @param searchParams
	 * @return
	 */
	Page getRollDataInfo(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 统计学籍管理信息
	 * 
	 * @param searchParams
	 * @return
	 */
	Map getRollCountInfo(Map<String, Object> searchParams);

	/**
	 * 企业大学公共信息
	 * 
	 * @param searchParams
	 * @return
	 */
	Map getRollBaseData(Map<String, Object> searchParams);

	/**
	 * 查询专业和报读人数 TOP5
	 * 
	 * @param searchParams
	 * @return
	 */
	Map getSpecialAndSingUp(Map<String, Object> searchParams);

	/**
	 * 获得学员的成绩(统计)
	 * 
	 * @param searchParams
	 * @return
	 */
	Map getExamScoreCount(Map<String, Object> searchParams);

	/**
	 * 获得学员的成绩
	 * 
	 * @param searchParams
	 * @return
	 */
	Page getExamScoreList(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 根据开课状态返回考勤统计数
	 * 
	 * @param searchParams
	 * @return
	 */
	long getCourseLoginCount(Map searchParams);

	/**
	 * 报读资料统计
	 * 
	 * @param searchParams
	 * @return
	 */
	Map getEnrolmentCount(Map<String, Object> searchParams);

	/**
	 * 统计企业整体报读情况
	 * 
	 * @param searchParams
	 * @return
	 */
	Map getEnterpriseSignUp(Map<String, Object> searchParams);

	/**
	 * 统计当期优秀学员学习排行TOP3
	 * 
	 * @param searchParams
	 * @return
	 */
	Map getLearningRank(Map<String, Object> searchParams);

	/**
	 * 学员考勤列表统计项
	 * 
	 * @param searchParams
	 * @return
	 */
	int getStudentLoginCount(Map searchParams);
	
	/**
	 * 学生综合信息查询=》链接
	 * @return
	 */
	Page getStudentLinkList(Map searchParams, PageRequest pageRequst);

	/**
	 * 学生综合信息查询=》链接
	 * @return
	 */
	List<Map<String, Object>> getStudentLinkList(Map searchParams);

	/**
	 * 学生综合信息查询=》链接
	 * @return
	 */
	long getStudentLinkCount(Map searchParams);

	/**
	 * 学生综合信息查询=》链接导出
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadStudentLinkListExportXls(Map searchParams);

	/**
	 * 学员学习情况统计
	 * @param searchParams
	 * @return
	 */
	Map<String, Object> countStudentStudySituation(Map searchParams);
}
