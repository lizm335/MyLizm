package com.gzedu.xlims.web.controller.studymanage;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.studymanage.StudyManageService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 功能说明：学习管理-考勤分析
 * 
 * @author
 * @Date
 * @version
 */
@Controller
@RequestMapping("/studymanage")
public class ClockingInController extends BaseController {
	private final static Logger log = LoggerFactory.getLogger(ClockingInController.class);

	@Autowired
	StudyManageService studyManageService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	GjtRecResultService gjtRecResultService;

	@Autowired
	GjtClassStudentService gjtClassStudentService;


	/** ---------------------------------------------------------------- 考勤分析 ---------------------------------------------------------------- **/


	/**
	 * 考勤分析--》课程考勤
	 */
	@RequestMapping(value = "getCourseLoginList", method = RequestMethod.GET)
	public String getCourseLoginList(Model model, HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(user.getGjtOrg().getGjtStudyCenter())) {
			searchParams.put("EQ_studyId", user.getGjtOrg().getGjtStudyCenter().getId());
		}
		
		Map currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(currentGradeMap)) {
			String currentGradeId = (String) currentGradeMap.keySet().iterator().next();
			model.addAttribute("currentGradeId", currentGradeId);
			if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {// 如果没有选择，则默认查当前学期
				searchParams.put("GRADE_ID", currentGradeId);
			} else if ("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
				searchParams.remove("GRADE_ID");
			}
		}
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = studyManageService.getCourseLoginList(searchParams, pageRequst);

		// 学期
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		Map courseMap = commonMapService.getCourseMap(user.getGjtOrg().getId());

		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("courseMap", courseMap);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageInfo0", pageInfo.getTotalElements());
		request.getSession().setAttribute("getCourseLoginList", searchParams);// 用于导出取此条件的参数
		return "studymanage/classlogin/get_classlogin_list";
	}

	/**
	 * 课程考勤列表查询统计项
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getCourseLoginCount")
	@ResponseBody
	public Map getCourseLoginCount(HttpServletRequest request) {
		Map resultMap = new HashMap();
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map searchParams = Servlets.getParametersStartingWith(request, "");
			if ("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
				searchParams.remove("GRADE_ID");
			}
			searchParams.put("XX_ID", user.getGjtOrg().getId());
			if (EmptyUtils.isNotEmpty(user.getGjtOrg().getGjtStudyCenter())) {
				searchParams.put("EQ_studyId", user.getGjtOrg().getGjtStudyCenter().getId());
			}
			// 查询条件统计项
			searchParams.put("TIME_FLG", ObjectUtils.toString(searchParams.get("TIME_FLG_TEMP")));
			long LOGIN_STATE_COUNT = studyManageService.getCourseLoginCount(searchParams);
			resultMap.put("LOGIN_STATE_COUNT", LOGIN_STATE_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 考勤分析--》课程考勤》导出文档的页面
	 * 
	 * @param totalNum
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "courseLoginListExport/{totalNum}", method = { RequestMethod.POST, RequestMethod.GET })
	public String courseLoginListExport(@PathVariable("totalNum") String totalNum, HttpServletRequest request,
			Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			String phone = ObjectUtils.toString(user.getSjh());
			if (EmptyUtils.isNotEmpty(phone)) {
				model.addAttribute("mobileNumber", phone.substring(phone.length() - 4, phone.length()));
			}
			model.addAttribute("totalNum", totalNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "studymanage/classlogin/courseLoginListExport";
	}

	/**
	 * 考勤分析--》课程考勤》下载课程考勤
	 */
	@SysLog("课程考勤列表-导出")
	@RequestMapping(value = "downLoadcourseLoginListExportXls", method = { RequestMethod.POST })
	public void downLoadcourseLoginListExportXls(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			try {
				Map searchParams = (Map) request.getSession().getAttribute("getCourseLoginList");
				Workbook wb = studyManageService.downLoadcourseLoginListExportXls(searchParams);
				String fileName = StringUtils.getBrowserStr(request, "课程考勤.xls");
				response.setContentType("application/x-msdownload;charset=utf-8");
				response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
				wb.write(response.getOutputStream());
				request.getSession().setAttribute(user.getSjh(), "");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("您没有权限");
		}

	}

	/**
	 * 课程考勤详情
	 * 
	 * @param courseId
	 * @param model
	 * @param pageNumber
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/{courseId}/{gradeId}/getCourseClockingDetail", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String getCourseClockingDetail(@PathVariable("courseId") String courseId,
			@PathVariable("gradeId") String gradeId, Model model,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map courseClass = commonMapService.getCourseClassInfoMap(user.getGjtOrg().getId(), courseId);// 课程班
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 获取学期列表
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		Map pyccMap = commonMapService.getPyccMap();// 层次

		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		searchParams.put("COURSE_ID", courseId);
		searchParams.put("courseId", courseId);
		if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("gradeId")))) {
			searchParams.put("gradeId", gradeId);
			model.addAttribute("grade_id", gradeId);
		} else {
			model.addAttribute("grade_id", ObjectUtils.toString(searchParams.get("gradeId")));
		}
		Map currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(currentGradeMap)) {
			String currentGradeId = (String) currentGradeMap.keySet().iterator().next();
			model.addAttribute("currentGradeId", currentGradeId);
			if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("gradeId")))) {// 如果没有选择，则默认查当前学期
				searchParams.put("gradeId", currentGradeId);
			} else if ("all".equals(ObjectUtils.toString(searchParams.get("gradeId")))) {
				searchParams.remove("gradeId");
			}
		}
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = studyManageService.getCourseClockingDetail(searchParams, pageRequst);
		Map param = new HashMap();
		param.putAll(searchParams);
		// 选项卡统计
		if ("".equals(ObjectUtils.toString(searchParams.get("MAIN_DEVICE")))) {
			param.put("MAIN_DEVICE", "APP");
			Page pageInfoApp = studyManageService.getCourseClockingDetail(param, pageRequst);
			param.put("MAIN_DEVICE", "PC");
			Page pageInfoPc = studyManageService.getCourseClockingDetail(param, pageRequst);
			param.put("MAIN_DEVICE", "NO");
			Page pageInfoNo = studyManageService.getCourseClockingDetail(param, pageRequst);
			model.addAttribute("ALL_MAIN_DEVICE", pageInfo.getTotalElements());
			model.addAttribute("APP_MAIN_DEVICE", pageInfoApp.getTotalElements());
			model.addAttribute("PC_MAIN_DEVICE", pageInfoPc.getTotalElements());
			model.addAttribute("NO_MAIN_DEVICE", pageInfoNo.getTotalElements());
		} else if ("APP".equals(ObjectUtils.toString(searchParams.get("MAIN_DEVICE")))) {
			param.put("MAIN_DEVICE", "");
			Page pageInfoAll = studyManageService.getCourseClockingDetail(param, pageRequst);
			param.put("MAIN_DEVICE", "PC");
			Page pageInfoPc = studyManageService.getCourseClockingDetail(param, pageRequst);
			param.put("MAIN_DEVICE", "NO");
			Page pageInfoNo = studyManageService.getCourseClockingDetail(param, pageRequst);
			model.addAttribute("ALL_MAIN_DEVICE", pageInfoAll.getTotalElements());
			model.addAttribute("APP_MAIN_DEVICE", pageInfo.getTotalElements());
			model.addAttribute("PC_MAIN_DEVICE", pageInfoPc.getTotalElements());
			model.addAttribute("NO_MAIN_DEVICE", pageInfoNo.getTotalElements());
		} else if ("PC".equals(ObjectUtils.toString(searchParams.get("MAIN_DEVICE")))) {
			param.put("MAIN_DEVICE", "");
			Page pageInfoAll = studyManageService.getCourseClockingDetail(param, pageRequst);
			param.put("MAIN_DEVICE", "APP");
			Page pageInfoApp = studyManageService.getCourseClockingDetail(param, pageRequst);
			param.put("MAIN_DEVICE", "NO");
			Page pageInfoNo = studyManageService.getCourseClockingDetail(param, pageRequst);
			model.addAttribute("ALL_MAIN_DEVICE", pageInfoAll.getTotalElements());
			model.addAttribute("APP_MAIN_DEVICE", pageInfoApp.getTotalElements());
			model.addAttribute("PC_MAIN_DEVICE", pageInfo.getTotalElements());
			model.addAttribute("NO_MAIN_DEVICE", pageInfoNo.getTotalElements());
		} else if ("NO".equals(ObjectUtils.toString(searchParams.get("MAIN_DEVICE")))) {
			param.put("MAIN_DEVICE", "");
			Page pageInfoAll = studyManageService.getCourseClockingDetail(param, pageRequst);
			param.put("MAIN_DEVICE", "APP");
			Page pageInfoApp = studyManageService.getCourseClockingDetail(param, pageRequst);
			param.put("MAIN_DEVICE", "PC");
			Page pageInfoPc = studyManageService.getCourseClockingDetail(param, pageRequst);
			model.addAttribute("ALL_MAIN_DEVICE", pageInfoAll.getTotalElements());
			model.addAttribute("APP_MAIN_DEVICE", pageInfoApp.getTotalElements());
			model.addAttribute("PC_MAIN_DEVICE", pageInfoPc.getTotalElements());
			model.addAttribute("NO_MAIN_DEVICE", pageInfo.getTotalElements());
		}

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("courseClass", courseClass);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);

		request.getSession().setAttribute("getCourseClockingDetail", searchParams);// 用于导出取此条件的参数
		return "studymanage/classlogin/course_clocking_detail";
	}

	/**
	 * 课程考勤详情下载
	 * 
	 * @param totalNum
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "courseClockingDetailExport/{totalNum}", method = { RequestMethod.POST, RequestMethod.GET })
	public String courseClockingDetailExport(@PathVariable("totalNum") String totalNum, HttpServletRequest request,
			Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			String phone = ObjectUtils.toString(user.getSjh());
			if (EmptyUtils.isNotEmpty(phone)) {
				model.addAttribute("mobileNumber", phone.substring(phone.length() - 4, phone.length()));
			}
			model.addAttribute("totalNum", totalNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "studymanage/classlogin/course_clockingDetail_export";
	}

	/**
	 * 考勤分析--》课程考勤》下载课程考勤详情
	 */
	@SysLog("课程考勤详情-导出")
	@RequestMapping(value = "downLoadClockingDetailExportXls", method = { RequestMethod.POST })
	public void downLoadClockingDetailExportXls(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			try {
				Map searchParams = (Map) request.getSession().getAttribute("getCourseClockingDetail");
				Workbook wb = studyManageService.downLoadClockingDetailExportXls(searchParams);
				String fileName = StringUtils.getBrowserStr(request, "课程考勤详情.xls");
				response.setContentType("application/x-msdownload;charset=utf-8");
				response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
				wb.write(response.getOutputStream());
				request.getSession().setAttribute(user.getSjh(), "");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("您没有权限");
		}
	}

	/**
	 * 考勤分析=》学员考勤(考勤详情)
	 * 
	 * @return
	 */
	@RequestMapping(value = "getStudentLoginDetail/{studentId}", method = RequestMethod.GET)
	public String getStudentLoginDetail(Model model, HttpServletRequest request,
			@PathVariable("studentId") String studentId) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");

		if("1".equals(user.getGjtOrg().getOrgType())) {
			searchParams.put("XX_ID", user.getGjtOrg().getId());
		} else {
			searchParams.put("EQ_studyId", user.getGjtOrg().getId());
		}
		searchParams.put("STUDENT_ID", studentId);
		Map resultMap = studyManageService.getStudentLoginDetail(searchParams);
		model.addAttribute("resultMap", resultMap);

		request.getSession().setAttribute("getStudentLoginDetail", searchParams);
		return "studymanage/studentlogin/get_studentlogin_detail";
	}

	/**
	 * 学员考勤明细导出文档的页面
	 * 
	 * @param totalNum
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "studentLoginExport/{totalNum}", method = { RequestMethod.POST, RequestMethod.GET })
	public String studentLoginExport(@PathVariable("totalNum") String totalNum, HttpServletRequest request,
			Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			String phone = ObjectUtils.toString(user.getSjh());
			if (EmptyUtils.isNotEmpty(phone)) {
				model.addAttribute("mobileNumber", phone.substring(phone.length() - 4, phone.length()));
			}
			model.addAttribute("totalNum", totalNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "studymanage/studentlogin/student_loginDetail_export";
	}

	/**
	 * 学员考勤列表 导出文档的页面
	 * 
	 * @param totalNum
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "studentLoginListExport/{totalNum}", method = { RequestMethod.POST, RequestMethod.GET })
	public String studentLoginListExport(@PathVariable("totalNum") String totalNum, HttpServletRequest request,
			Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			String phone = ObjectUtils.toString(user.getSjh());
			if (EmptyUtils.isNotEmpty(phone)) {
				model.addAttribute("mobileNumber", phone.substring(phone.length() - 4, phone.length()));
			}
			model.addAttribute("totalNum", totalNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "studymanage/studentlogin/student_loginList_export";
	}

	/**
	 * 考勤分析--》学员考勤列表下载
	 */
	@SysLog("学员课程考勤列表-导出")
	@RequestMapping(value = "downLoadStudentLoginListExportXls", method = { RequestMethod.POST })
	public void downLoadStudentLoginListExportXls(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			try {
				Map searchParams = (Map) request.getSession().getAttribute("getStudentLoginList");
				Workbook wb = studyManageService.downLoadStudentLoginListExportXls(searchParams);
				String fileName = StringUtils.getBrowserStr(request,
						ObjectUtils.toString(searchParams.get("prefixName"), "") + "学员课程考勤列表.xls");
				response.setContentType("application/x-msdownload;charset=utf-8");
				response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
				wb.write(response.getOutputStream());
				request.getSession().setAttribute(user.getSjh(), "");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("您没有权限");
		}
	}

	/**
	 * 考勤分析--》学员考勤》下载学员课程考勤详情
	 */
	@SysLog("学员考勤详情-导出")
	@RequestMapping(value = "downLoadStudentDetailExportXls", method = { RequestMethod.POST })
	public void downLoadStudentDetailExportXls(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			try {
				Map searchParams = (Map) request.getSession().getAttribute("getStudentLoginDetail");
				Workbook wb = studyManageService.downLoadStudentDetailExportXls(searchParams);
				String fileName = StringUtils.getBrowserStr(request, "学员课程考勤详情.xls");
				response.setContentType("application/x-msdownload;charset=utf-8");
				response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
				wb.write(response.getOutputStream());
				request.getSession().setAttribute(user.getSjh(), "");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("您没有权限");
		}
	}

	/**
	 * 考勤分析=》教务班考勤
	 *
	 * @return
	 */
	@RequestMapping(value = "getClassLoginList", method = RequestMethod.GET)
	public String getClassLoginList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
									@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
									HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		String xxId = user.getGjtOrg().getId();
		searchParams.put("XX_ID", xxId);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Map resultMap = studyManageService.getClassLoginList(searchParams, pageRequst);
		Page pageInfo = (Page) resultMap.get("page");
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("resultMap", resultMap);
		return "studymanage/classlogin/get_classlogin_list";
	}

	/**
	 * 专业考勤
	 */
	public String getMajorLoginList() {
		return "studymanage/classlogin/get_major_login_list";
	}

	/**
	 * 课程班考勤
	 */
	@RequestMapping(value = "getCourseClassLoginList", method = RequestMethod.GET)
	public String getCourseClassLoginList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
										  @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
										  HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map courseMap = commonMapService.getCourseMap(user.getGjtOrg().getId());// 课程列表
		Map<String, String> classMap = commonMapService.getClassInfoMap(user.getGjtOrg().getId(), "course");// 院校下的课程班

		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(user.getGjtOrg().getGjtStudyCenter())) {
			searchParams.put("EQ_studyId", user.getGjtOrg().getGjtStudyCenter().getId());
		}
		Map currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(currentGradeMap)) {
			String currentGradeId = (String) currentGradeMap.keySet().iterator().next();
			model.addAttribute("currentGradeId", currentGradeId);
			if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {// 如果没有选择，则默认查当前学期
				searchParams.put("GRADE_ID", currentGradeId);
			} else if ("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
				searchParams.remove("GRADE_ID");
			}
		}

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = studyManageService.getCourseClassLoginList(searchParams, pageRequst);
		// 学期
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("classMap", classMap);
		model.addAttribute("courseMap", courseMap);

		request.getSession().setAttribute("getCourseClassLoginList", searchParams);// 用于导出取此条件的参数
		return "studymanage/courseclasslogin/get_course_class_login_list";
	}

	/**
	 * 课程班考勤--》课程班考勤列表导出
	 *
	 * @return
	 */
	@RequestMapping(value = "courseClassListExport/{totalNum}", method = { RequestMethod.POST, RequestMethod.GET })
	public String courseClassListExport(@PathVariable("totalNum") String totalNum, HttpServletRequest request,
										Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			String phone = ObjectUtils.toString(user.getSjh());
			if (EmptyUtils.isNotEmpty(phone)) {
				model.addAttribute("mobileNumber", phone.substring(phone.length() - 4, phone.length()));
			}
			model.addAttribute("totalNum", totalNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "studymanage/courseclasslogin/courseClassListExport";
	}

	/**
	 * 考勤分析--》课程班考勤列表下载
	 */
	@SysLog("课程班考勤列表-导出")
	@RequestMapping(value = "downLoadCourseClassListExportXls", method = { RequestMethod.POST })
	public void downLoadCourseClassListExportXls(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			try {
				Map searchParams = (Map) request.getSession().getAttribute("getCourseClassLoginList");
				Workbook wb = studyManageService.downLoadCourseClassListExportXls(searchParams);
				String fileName = StringUtils.getBrowserStr(request, "课程班考勤列表.xls");
				response.setContentType("application/x-msdownload;charset=utf-8");
				response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
				wb.write(response.getOutputStream());
				request.getSession().setAttribute(user.getSjh(), "");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("您没有权限");
		}
	}

	/**
	 * 课程班考勤--》课程班考勤明细
	 *
	 * @param termcourseId
	 * @param classId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/{termcourseId}/{classId}/{courseId}/{gradeId}/getCourseClassClockingDetail", method = {
			RequestMethod.GET, RequestMethod.POST })
	public String getCourseClassClockingDetail(@PathVariable("termcourseId") String termcourseId,
											   @PathVariable("classId") String classId, @PathVariable("courseId") String courseId,
											   @PathVariable("gradeId") String gradeId, @RequestParam(value = "page", defaultValue = "1") int pageNumber,
											   @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
											   HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		// 获取学期列表
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		// 专业
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		// 层次
		Map pyccMap = commonMapService.getPyccMap();
		// 学期
		Map courseClass = commonMapService.getClassInfoMap(user.getGjtOrg().getId(), "course");

		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		searchParams.put("termcourseId", termcourseId);
		searchParams.put("CLASS_ID", classId);
		searchParams.put("COURSE_ID", courseId);
		if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("gradeId")))) {
			searchParams.put("gradeId", gradeId);
			model.addAttribute("grade_id", gradeId);
		} else {
			model.addAttribute("grade_id", ObjectUtils.toString(searchParams.get("gradeId")));
		}
		Map currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(currentGradeMap)) {
			String currentGradeId = (String) currentGradeMap.keySet().iterator().next();
			model.addAttribute("currentGradeId", currentGradeId);
			if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("gradeId")))) {// 如果没有选择，则默认查当前学期
				searchParams.put("gradeId", currentGradeId);
			} else if ("all".equals(ObjectUtils.toString(searchParams.get("gradeId")))) {
				searchParams.remove("gradeId");
			}
		}
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = studyManageService.getCourseClockingDetail(searchParams, pageRequst);
		Map param = new HashMap();
		param.putAll(searchParams);
		// 选项卡统计
		if ("".equals(ObjectUtils.toString(searchParams.get("MAIN_DEVICE")))) {
			param.put("MAIN_DEVICE", "APP");
			Page pageInfoApp = studyManageService.getCourseClockingDetail(param, pageRequst);
			param.put("MAIN_DEVICE", "PC");
			Page pageInfoPc = studyManageService.getCourseClockingDetail(param, pageRequst);
			param.put("MAIN_DEVICE", "NO");
			Page pageInfoNo = studyManageService.getCourseClockingDetail(param, pageRequst);
			model.addAttribute("ALL_MAIN_DEVICE", pageInfo.getTotalElements());
			model.addAttribute("APP_MAIN_DEVICE", pageInfoApp.getTotalElements());
			model.addAttribute("PC_MAIN_DEVICE", pageInfoPc.getTotalElements());
			model.addAttribute("NO_MAIN_DEVICE", pageInfoNo.getTotalElements());
		} else if ("APP".equals(ObjectUtils.toString(searchParams.get("MAIN_DEVICE")))) {
			param.put("MAIN_DEVICE", "");
			Page pageInfoAll = studyManageService.getCourseClockingDetail(param, pageRequst);
			param.put("MAIN_DEVICE", "PC");
			Page pageInfoPc = studyManageService.getCourseClockingDetail(param, pageRequst);
			param.put("MAIN_DEVICE", "NO");
			Page pageInfoNo = studyManageService.getCourseClockingDetail(param, pageRequst);
			model.addAttribute("ALL_MAIN_DEVICE", pageInfoAll.getTotalElements());
			model.addAttribute("APP_MAIN_DEVICE", pageInfo.getTotalElements());
			model.addAttribute("PC_MAIN_DEVICE", pageInfoPc.getTotalElements());
			model.addAttribute("NO_MAIN_DEVICE", pageInfoNo.getTotalElements());
		} else if ("PC".equals(ObjectUtils.toString(searchParams.get("MAIN_DEVICE")))) {
			param.put("MAIN_DEVICE", "");
			Page pageInfoAll = studyManageService.getCourseClockingDetail(param, pageRequst);
			param.put("MAIN_DEVICE", "APP");
			Page pageInfoApp = studyManageService.getCourseClockingDetail(param, pageRequst);
			param.put("MAIN_DEVICE", "NO");
			Page pageInfoNo = studyManageService.getCourseClockingDetail(param, pageRequst);
			model.addAttribute("ALL_MAIN_DEVICE", pageInfoAll.getTotalElements());
			model.addAttribute("APP_MAIN_DEVICE", pageInfoApp.getTotalElements());
			model.addAttribute("PC_MAIN_DEVICE", pageInfo.getTotalElements());
			model.addAttribute("NO_MAIN_DEVICE", pageInfoNo.getTotalElements());
		} else if ("NO".equals(ObjectUtils.toString(searchParams.get("MAIN_DEVICE")))) {
			param.put("MAIN_DEVICE", "");
			Page pageInfoAll = studyManageService.getCourseClockingDetail(param, pageRequst);
			param.put("MAIN_DEVICE", "APP");
			Page pageInfoApp = studyManageService.getCourseClockingDetail(param, pageRequst);
			param.put("MAIN_DEVICE", "PC");
			Page pageInfoPc = studyManageService.getCourseClockingDetail(param, pageRequst);
			model.addAttribute("ALL_MAIN_DEVICE", pageInfoAll.getTotalElements());
			model.addAttribute("APP_MAIN_DEVICE", pageInfoApp.getTotalElements());
			model.addAttribute("PC_MAIN_DEVICE", pageInfoPc.getTotalElements());
			model.addAttribute("NO_MAIN_DEVICE", pageInfo.getTotalElements());
		}

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("courseClass", courseClass);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);

		request.getSession().setAttribute("getCourseClockingDetail", searchParams);// 用于导出取此条件的参数
		return "studymanage/courseclasslogin/get_course_class_login_detail";
	}

	/**
	 * 考勤分析=》学员考勤
	 *
	 * @return
	 */
	@RequestMapping(value = "getStudentLoginList", method = RequestMethod.GET)
	public String getStudentLoginList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
									  @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
									  HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 学习中心
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业名称
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		Map pyccMap = commonMapService.getPyccMap();// 层次

		Map<String, String> classMap = commonMapService.getClassInfoMap(user.getGjtOrg().getId(), "teach");

		Map searchParams = Servlets.getParametersStartingWith(request, "");
		if("1".equals(user.getGjtOrg().getOrgType())) {
			searchParams.put("XX_ID", user.getGjtOrg().getId());
		} else {
			searchParams.put("EQ_studyId", user.getGjtOrg().getId());
		}
		Map currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(currentGradeMap)) {
			String currentGradeId = (String) currentGradeMap.keySet().iterator().next();
			model.addAttribute("currentGradeId", currentGradeId);
			if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {// 如果没有选择，则默认查当前学期
				searchParams.put("GRADE_ID", currentGradeId);
			} else if ("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
				searchParams.remove("GRADE_ID");
			}
		}
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = studyManageService.getStudentLoginList(searchParams, pageRequst);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("classMap", classMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		StringBuffer prefixName = new StringBuffer();// 导出文件名字根据搜索条件命名前缀
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			prefixName.append(
					ObjectUtils.toString(studyCenterMap.get(ObjectUtils.toString(searchParams.get("XXZX_ID")))) + "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			prefixName.append(
					ObjectUtils.toString(gradeMap.get(ObjectUtils.toString(searchParams.get("GRADE_ID")))) + "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			prefixName.append(
					ObjectUtils.toString(specialtyMap.get(ObjectUtils.toString(searchParams.get("SPECIALTY_ID"))))
							+ "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))) {
			prefixName.append(
					ObjectUtils.toString(classMap.get(ObjectUtils.toString(searchParams.get("CLASS_ID")))) + "-");
		}
		searchParams.put("prefixName", ObjectUtils.toString(prefixName.toString(), ""));
		request.getSession().setAttribute("getStudentLoginList", searchParams);// 用于导出取此条件的参数
		return "studymanage/studentlogin/get_studentlogin_list";
	}

	/**
	 * 学员考勤列表查询统计项
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getStudentLoginCount")
	@ResponseBody
	public Map getStudentLoginCount(HttpServletRequest request) {
		Map resultMap = new HashMap();
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map searchParams = Servlets.getParametersStartingWith(request, "");
			if("1".equals(user.getGjtOrg().getOrgType())) {
				searchParams.put("XX_ID", user.getGjtOrg().getId());
			} else {
				searchParams.put("EQ_studyId", user.getGjtOrg().getId());
			}
			if ("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
				searchParams.remove("GRADE_ID");
			}
			searchParams.put("STUDY_STATUS", ObjectUtils.toString(searchParams.get("STUDY_STATUS_TEMP")));
			// 查询条件统计项
			int LOGIN_STATE_COUNT = studyManageService.getStudentLoginCount(searchParams);
			resultMap.put("STUDY_STATUS_COUNT", LOGIN_STATE_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

}
