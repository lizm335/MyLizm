package com.gzedu.xlims.service.exam;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface GjtExamRecordNewService {
	
	/**
	 * 考试管理=》登记成绩查询列表
	 * @return
	 */
	Page getExamRegisterList(Map searchParams, PageRequest pageRequst);
	
	/**
	 * 考试管理=》登记成绩(统计)
	 * @return
	 */
	int getRegisterCount(Map searchParams);
	
	/**
	 * 考试管理=》考试成绩查看详情
	 * @return
	 */
	Map getExamRecordDetail(Map searchParams);

	/**
	 * 导入登记成绩
	 * @return
	 */
	Map importRegisterRecord(String filePaths, Map formMap, String basePath);
	
	/**
	 * 导入登记成绩统考学位英语
	 * @return
	 */
	Map importRegisterTkXwRecord(String filePaths, Map formMap, String basePath);

	/**
	 * 导入登记成绩-特殊处理  按照学号课程代码对应得上就是干
	 * @return
	 */
	Map importRegisterRecordTeshuchuli(String filePaths, Map formMap, String basePath);
	
	/**
	 * 导出成绩
	 */
	Workbook expExamRecord(Map formMap);

	/**
	 * 导出成绩CSV格式内容
	 * @param formMap
	 * @return
	 */
	String expExamRecordToCsvContent(Map formMap);
	
	/**
	 * 导出登记成绩
	 */
	Workbook expExamRegisterRecord(Map formMap);

	/**
	 * 获取学员历史成绩
	 * @param formMap
	 * @return
	 */
    List<Map<String,Object>> getHistoryScore(Map<String, Object> formMap);
    
    /**
	 * 定时锁定登记成绩
	 */
	int registerExamState(Map formMap);
	
	/**
	 * 考试管理=》考情分析(考试预约明细)
	 * @return
	 */
	Page getRecordAppointmentList(Map searchParams, PageRequest pageRequst);
	
	/**
	 * 考试管理=》考情分析(考试预约统计)
	 * @return
	 */
	int getExamAppointmentCount(Map searchParams);
	
	/**
	 * 导出考试情况数据
	 * @return
	 */
	String exportExamAppointment(Map searchParams, String path);
	
	/**
	 * 考试情况明细查询列表
	 * @return
	 */
	Page getExamDetailList(Map searchParams, PageRequest pageRequst);
	
	/**
	 * 导出考试情况明细
	 * @return
	 */
	String exportExamDetail(Map searchParams, String path);
	
	/**
	 * 考试情况明细查询列表(统计数字)
	 * @return
	 */
	int getExamDetailCount(Map searchParams);

	/**
	 * 学生综合信息查询=》链接，带分页
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<Map<String, Object>> queryStudentExamListByPage(Map<String, Object> searchParams, PageRequest pageRequst);
	
	/**
	 * 学生综合信息查询=》链接 统计
	 * @param searchParams
	 * @return
	 */
	long getStudentExamCount(Map searchParams);
	
	/**
	 * 学生综合信息查询=》学员考情导出
	 * @param searchParams
	 * @return
	 */
	Workbook downLoadExcelExportByExamAppointment(Map searchParams);

	/**
	 * 学员考试情况
	 * @param searchParams
	 * @return
	 */
	Map<String, Object> countStudentExamSituation(Map searchParams);
}
