/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.api;

import com.gzedu.xlims.service.api.ApiOpenClassService;
import com.gzedu.xlims.web.common.Servlets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * 
 * 功能说明：开课接口
 * 
 * @author 李明 liming@eenet.com
 * @Date 2017年7月5日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/api/openclass/")
public class ApiOpenClassController {
	
	@Autowired
	ApiOpenClassService apiOpenClassService;
	
	/**
	 * 初始化学员选课记录
	 */
	@RequestMapping(value = "initStudentChoose")
	@ResponseBody
	public Map initStudentChoose(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return apiOpenClassService.initStudentChoose(formMap);
	}
	
	/**
	 * 恢复旧的学习记录
	 */
	@RequestMapping(value = "stuOldToNewRec")
	@ResponseBody
	public Map stuOldToNewRec(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return apiOpenClassService.stuOldToNewRec(formMap);
	}
	
	/**
	 * 初始化期课程记录
	 */
	@RequestMapping(value = "initTermCourse")
	@ResponseBody
	public Map initTermCourse(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return apiOpenClassService.initTermCourse(formMap);
	}
	
	/**
	 * 初始化调班数据
	 */
	@Deprecated
	@RequestMapping(value = "initCourseClass")
	@ResponseBody
	public Map initCourseClass(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return apiOpenClassService.initCourseClass(formMap);
	}
	
	/**
	 * 初始化课程班级的辅导老师和督导老师数据
	 */
	@RequestMapping(value = "initCourseTeacher")
	@ResponseBody
	public Map initCourseTeacher(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return apiOpenClassService.initCourseTeacher(formMap);
	}
	
	/**
	 * 检查同步期课程班级是否同步到学习平台
	 */
	@RequestMapping(value = "syncTermClass")
	@ResponseBody
	public Map syncTermClass(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return apiOpenClassService.syncTermClass(formMap);
	}
	
	/**
	 * 教学计划保存的时候初始化学员的选课信息
	 */
	@RequestMapping(value = "initPlanStuRec")
	@ResponseBody
	public Map initPlanStuRec(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return apiOpenClassService.initPlanStuRec(formMap);
	}
	
	/**
	 * 开课完成初始化学员的选课（点击开课流程的时候调用）
	 */
	@RequestMapping(value = "initTermcourseStuRec")
	@ResponseBody
	public Map initTermcourseStuRec(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return apiOpenClassService.initTermcourseStuRec(formMap);
	}
	
	/**
	 * 初始化课程数据
	 */
	@RequestMapping(value = "initCourseInfo")
	@ResponseBody
	public Map initCourseInfo(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return apiOpenClassService.initCourseInfo(formMap);
	}
	
	/***
	 * 定时调用
	 * 1、选课数据不足的补齐
	 * 2、删除多余的选课信息
	 * 3、删除开课为0、班级为0 的数据
	 * 4、学习平台的选课数据保持一致
	 */
	@RequestMapping(value = "initStudentRec")
	@ResponseBody
	public Map initStudentRec(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return apiOpenClassService.initStudentRec(formMap);
	}
}
