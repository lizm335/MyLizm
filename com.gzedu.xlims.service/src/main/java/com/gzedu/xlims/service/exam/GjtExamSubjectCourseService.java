package com.gzedu.xlims.service.exam;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface GjtExamSubjectCourseService {

	/**
	 * 查询课程信息列表
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Page getGjtCourseList(Map<String, Object> searchParams,PageRequest pageRequest);
	
	/**
	 * 保存考试科目与课程数据
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map saveExamSubjectCourse(Map<String, Object> searchParams);
	
	/**
	 * 查询试卷号是否存在
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Map getIsExistExamNo(Map<String, Object> searchParams);
	
	/**
	 * 导出未创建考试科目的课程
	 * @param searchParams
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	String exportExamSubjectCourse(Map searchParams,HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 导入考科目
	 * @param filePaths
	 * @param searchParams
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map importCourseSubject(String filePaths,Map searchParams,HttpServletRequest request,HttpServletResponse response);
	
	String getSubjectCode(int type);
	
}
