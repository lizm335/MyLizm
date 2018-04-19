/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.systemManage;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.systemManage.ToDoService;

/**
 * 
 * 功能说明：代办事项
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年7月1日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Controller
@RequestMapping("/system/todo")
public class ToDoController {

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	ToDoService toDoService;

	// 查询所有课程班级
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String schoolId = user.getGjtOrg().getId();

		Integer queryCourseClass = toDoService.queryCourseClass(schoolId);
		Integer queryGrade = toDoService.queryGrade(schoolId);
		// Integer queryStudyYear = toDoService.queryStudyYear(schoolId);
		Integer queryTeachClass = toDoService.queryTeachClass(schoolId);
		// Integer queryStudyYearRenWu =
		// toDoService.queryStudyYearRenWu(schoolId);
		Integer queryHeadTeach = toDoService.queryTeach(schoolId, EmployeeTypeEnum.班主任.getNum());
		Integer queryCourseTeach = toDoService.queryTeach(schoolId, EmployeeTypeEnum.辅导教师.getNum());
		Integer queryDuDaoTeach = toDoService.queryTeach(schoolId, EmployeeTypeEnum.督导教师.getNum());

		model.addAttribute("queryHeadTeach", queryHeadTeach);
		model.addAttribute("queryCourseTeach", queryCourseTeach);
		model.addAttribute("queryDuDaoTeach", queryDuDaoTeach);
		// model.addAttribute("queryStudyYearRenWu", queryStudyYearRenWu);
		model.addAttribute("queryCourseClass", queryCourseClass);
		model.addAttribute("queryGrade", queryGrade);
		// model.addAttribute("queryStudyYear", queryStudyYear);
		model.addAttribute("queryTeachClass", queryTeachClass);
		return "systemManage/todo/list";
	}
}
