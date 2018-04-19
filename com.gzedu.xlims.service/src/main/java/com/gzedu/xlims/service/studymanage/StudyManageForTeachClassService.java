package com.gzedu.xlims.service.studymanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.TblPriLoginLog;

/**
 * 针对教学班的学习管理服务
 * 
 * @author 欧集红 
 * @Date 2018年4月11日
 * @version 1.0
 * 
 */
public interface StudyManageForTeachClassService {
	
	/**
	 * 
	 * 班主任平台 - 教学班的课程考勤情况
	 * 
	 * 考勤分析--》课程考勤--》课程考情详情
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page getTeachClassCourseClockingDetail(Map searchParams, PageRequest pageRequst);
	
	
	/**
	 * 教学班课程考勤
	 * 
	 * 考勤分析--》课程考勤--》课程考情详情
	 *   
	 * 指定设备的在线数量
	 * 
	 * @param searchParams
	 * @return
	 */
	long getTeachClassCourseClockingDetailCount(Map searchParams);
	
	
	/**
	 * 考勤分析--》课程考勤--》课程考情详情下载
	 * 
	 * @param searchParams
	 * @return
	 */
	Workbook downloadCourseClockingDetailExportXls(Map searchParams);
	
	/**
	 * 课程考勤列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page getCourseLoginList(Map searchParams, PageRequest pageRequst);
	
	/**
	 * 根据开课状态返回考勤统计数
	 *
	 * @param searchParams
	 * @return
	 */
	long getCourseLoginCount(Map searchParams);
	
	/**
	 * 学习管理=》学员考勤
	 * 
	 * @return
	 */
	Page getStudentLoginList(Map searchParams, PageRequest pageRequst);
	
	/**
	 * 学员考勤列表统计项
	 * 
	 * @param searchParams
	 * @return
	 */
	int getStudentLoginCount(Map searchParams);

	/**
	 * 考勤分析=》学员考勤(考勤详情)
	 * 
	 * @return
	 */
	Map getStudentLoginDetail(Map searchParams);
	
	
	/**
	 * 考勤分析--》学员课程考勤--》学员课程考勤列表下载
	 *
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadStudentLoginListExportXls(Map searchParams);

	/**
	 * 考勤分析--》课程考勤下载
	 *
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadCourseLoginListExportXls(Map searchParams);
	
	/**
	 * 考勤分析--》学员课程考勤--》学员课程考勤详情下载
	 *
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadStudentDetailExportXls(Map searchParams);
	
	/**
	 * 处理学员登录详情明细
	 *
	 * @param infos
	 * @return
	 */
	Workbook exportInfoDetails(List<TblPriLoginLog> tblPriLoginLogs);
	
	/**
	 * 处理课程学情下载
	 * @param searchParams
	 * @return
	 */
	Workbook downloadCourseStudyListExportXls(Map searchParams);
	
	/**
	 * 课程学情列表
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page getCourseStudyList(Map searchParams, PageRequest pageRequst);
	
	/**
	 * 课程学情详细信息
	 * @param seachParams
	 * @param pageRequst
	 * @return
	 */
	Page getCourseStudyDetails(Map<String, Object> seachParams, PageRequest pageRequst);
	
	
	/**
	 * 课程学情明细下载 
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadCommonCourseDetailExportXls(Map searchParams);
	
	/**
	 * 学员学情列表展示
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page getStudentCourseList(Map searchParams, PageRequest pageRequst);
	
	/**
	 * 学员学情列表下载
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadStudentCourseListExportXls(Map searchParams);
	
	
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
	 * 学习管理=》学分查询  -- 下载成绩列表
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadScoreListExportXls(Map searchParams);
	
}
