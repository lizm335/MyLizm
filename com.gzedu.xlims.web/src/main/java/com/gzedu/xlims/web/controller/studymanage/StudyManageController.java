package com.gzedu.xlims.web.controller.studymanage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.gzedu.SMSUtil;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.studymanage.StudyManageService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

import net.spy.memcached.MemcachedClient;

/**
 * 功能说明：学习管理-学情分析
 * 
 * @author
 * @Date
 * @version
 */
@Controller
@RequestMapping("/studymanage")
public class StudyManageController extends BaseController {
	private final static Logger log = LoggerFactory.getLogger(StudyManageController.class);

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

	@Autowired
	private MemcachedClient memcachedClient;

	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;


	/** ---------------------------------------------------------------- 学情分析 ---------------------------------------------------------------- **/


	/**
	 * 学习管理=》课程学情
	 * 
	 * @return
	 */
	@RequestMapping(value = "getCourseStudyList", method = RequestMethod.GET)
	public String getCourseStudyList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 获取学期列表
		Map courseMap = commonMapService.getCourseMap(user.getGjtOrg().getId());// 课程列表

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
		String onlyTeacher = request.getParameter("onlyTeacher");
		if (StringUtils.isNotBlank(onlyTeacher)) {
			GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.findOneByAccountId(user.getId());
			if (employeeInfo != null) {
				searchParams.put("CLASS_TEACHER", employeeInfo.getEmployeeId());
			}
		}
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = studyManageService.getCourseStudyList(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("courseMap", courseMap);
		model.addAttribute("pageInfoTotalElements", pageInfo.getTotalElements());
		request.getSession().setAttribute("getCourseStudyList", searchParams);// 用于导出取此条件的参数
		return "studymanage/coursestudy/get_coursestudy_list";
	}

	/**
	 * 导出课程活动数据加载页
	 * @param request
	 * @param model
     * @return
     */
	@RequestMapping(value = "courseActivityExport", method = RequestMethod.GET)
	public String courseActivityExport(HttpServletRequest request, Model model) {
		SimpleDateFormat sb = new SimpleDateFormat("yyyy-MM-dd");
		String endDate = sb.format(DateUtils.getDate());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		String startDate = sb.format(calendar.getTime());

		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		return "studymanage/coursestudy/courseActivityExport";
	}

	/**
	 * 导出课程活动数据
	 * @param request
	 * @param response
	 * @param startDate
	 * @param endDate
     * @param activityType
     */
	@SysLog("导出课程活动数据")
	@RequestMapping(value = "downCourseActivityExport", method = RequestMethod.POST)
	public void downCourseActivityExport(HttpServletRequest request, HttpServletResponse response, String startDate,
			String endDate, String activityType) {
		try {
			SimpleDateFormat sb = new SimpleDateFormat("yyyy-MM-dd");

			if (!StringUtils.isNotBlank(startDate)) {
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, -7);
				startDate = sb.format(calendar.getTime());
			}

			if (!StringUtils.isNotBlank(endDate)) {
				endDate = sb.format(DateUtils.getDate());
			}

			Date da = new Date(sb.parse(endDate).getTime() + 24 * 3600 * 1000);
			endDate = sb.format(da);

			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("startDate", startDate);
			searchParams.put("endDate", endDate);
			searchParams.put("activityType", activityType);
			Workbook wb = null;
			String fileName = "错误.xls";
			log.info("导出课程活动数据参数:{}", searchParams);
			if ("2".equals(activityType)) {
				wb = studyManageService.downCourseActivityExport(searchParams);
				fileName = StringUtils.getBrowserStr(request, DateUtils.getDate().getTime() + "主题讨论数据.xls");
			} else {
				wb = studyManageService.downCourseSubjectExport(searchParams);
				fileName = StringUtils.getBrowserStr(request, DateUtils.getDate().getTime() + "答疑数据.xls");
			}

			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			wb.write(response.getOutputStream());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 导出课程学情统计表页面
	 * @param totalNum
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "courseStudyListExport/{totalNum}", method = { RequestMethod.POST, RequestMethod.GET })
	public String courseStudyListExport(@PathVariable("totalNum") String totalNum, HttpServletRequest request,
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
		return "studymanage/coursestudy/courseStudyListExport";
	}

	/**
	 * 获取验证码
	 * 
	 * @param request
	 * @param feedback
	 * @return
	 */
	@RequestMapping(value = "getMessageCode", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Feedback getMessageCode(HttpServletRequest request, Feedback feedback) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String phone = ObjectUtils.toString(user.getSjh());
		Random rd = new Random();
		String code = ObjectUtils.toString(rd.nextInt(900000) + 100000);
		memcachedClient.delete("CODE_" + user.getSjh());
		memcachedClient.set("CODE_" + user.getSjh(), 60 * 60 * 24, code); // 防止发送超时已经发送了而认为发送失败了，所以提前存储一份
		int smsResult = SMSUtil.sendTemplateMessageCode(phone, code, "gk");
		if (smsResult == 1) {
			request.getSession().setAttribute(user.getSjh(), code);
			feedback.setSuccessful(true);
			feedback.setMessage("获取验证码成功！");
			feedback.setObj(code);
		} else {
			feedback.setSuccessful(false);
			feedback.setMessage("获取验证码失败！");
			feedback.setObj("");
		}

		return feedback;
	}

	/**
	 * 校验验证码
	 * 
	 * @param request
	 * @param userCode
	 * @param feedback
	 * @return
	 */
	@RequestMapping(value = "getCheckCode", method = { RequestMethod.POST })
	@ResponseBody
	public Feedback getCheckCode(HttpServletRequest request, String userCode, Feedback feedback) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String sjh = ObjectUtils.toString(user.getSjh());
		String code = ObjectUtils.toString(request.getSession().getAttribute(user.getSjh()), "");
		String memCode = ObjectUtils.toString(memcachedClient.get("CODE_" + sjh));
		if (code.equals(userCode) || memCode.equals(userCode)) {
			request.getSession().setAttribute("hasPermission", true);
			feedback.setSuccessful(true);
		} else {
			request.getSession().setAttribute("hasPermission", false);
			feedback.setSuccessful(false);
		}
		return feedback;
	}

	/**
	 * 下载课程学情
	 */
	@SysLog("课程学情列表-导出")
	@RequestMapping(value = "downLoadcourseStudyListExportXls", method = { RequestMethod.POST })
	public void downLoadcourseStudyListExportXls(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			try {
				Map searchParams = (Map) request.getSession().getAttribute("getCourseStudyList");
				Workbook wb = studyManageService.downLoadcourseStudyListExportXls(searchParams);
				String fileName = StringUtils.getBrowserStr(request, "课程学情.xls");
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
	 * 课程学情-课程学情明细
	 * 
	 * @param courseId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/{courseId}/{gradeId}/getCourseStudyDetails")
	public String getCourseStudyDetails(@PathVariable("courseId") String courseId,
			@PathVariable("gradeId") String gradeId, HttpServletRequest request,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 获取学期列表
		Map courseClass = commonMapService.getCourseClassInfoMap(user.getGjtOrg().getId(), courseId);
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		Map pyccMap = commonMapService.getPyccMap();// 层次
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 学习中心

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("courseId", courseId);
		if("1".equals(user.getGjtOrg().getOrgType())) {
			searchParams.put("XX_ID", user.getGjtOrg().getId());
		} else {
			searchParams.put("EQ_studyId", user.getGjtOrg().getId());
		}
		searchParams.put("orgType", user.getGjtOrg().getOrgType());
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

		Page pageInfo = studyManageService.getCourseStudyDetails(searchParams, pageRequst);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("courseclass", courseClass);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		request.getSession().setAttribute("commonCourseConditionDetailExport", searchParams);
		return "studymanage/coursestudy/get_coursestudy_details";
	}

	/**
	 * 管理后台--课程学情学情明细，班级学情学情明细，学员学情学情明细，导出页面
	 * 
	 * @param totalNum
	 * @return
	 */
	@RequestMapping(value = "/commonCourseConditionDetailExport/{totalNum}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String commonCourseConditionDetailExport(@PathVariable("totalNum") String totalNum,
			HttpServletRequest request, Model model) {
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
		return "studymanage/common_course_detail_export";
	}

	/**
	 * 管理后台--学员学情选项卡导出学情明细，导出页面
	 * 
	 * @param totalNum
	 * @return
	 */
	@RequestMapping(value = "/studCourseConditionDetailExport/{totalNum}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String studCourseConditionDetailExport(@PathVariable("totalNum") String totalNum, HttpServletRequest request,
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
		return "/studymanage/studentcourse/stud_course_detail_export";
	}

	/**
	 * 管理后台--课程学情学情明细，班级学情学情明细，学员学情学情明细，导出方法
	 */
	@SysLog("学情详情-导出")
	@RequestMapping(value = "downLoadCommonCourseDetailExportXls", method = { RequestMethod.POST })
	public void downLoadCommonCourseDetailExportXls(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			try {
				Map searchParams = (Map) request.getSession().getAttribute("commonCourseConditionDetailExport");
				Workbook wb = studyManageService.downLoadCommonCourseDetailExportXls(searchParams);
				String fileName = StringUtils.getBrowserStr(request, "学情详情.xls");
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
	 * 学习管理=》课程班学情
	 * 
	 * @return
	 */
	@RequestMapping(value = "getCourseClassList", method = RequestMethod.GET)
	public String getCourseClassList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 获取学期列表
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
		Page pageInfo = studyManageService.getCourseClassList(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("totalNum", pageInfo.getTotalElements());
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("courseMap", courseMap);
		model.addAttribute("classMap", classMap);
		request.getSession().setAttribute("getCourseClassList", searchParams);// 用于导出
		return "studymanage/courseclass/get_courseclass_list";
	}

	/**
	 * 课程班学情列表导出
	 * 
	 * @return
	 */
	@RequestMapping(value = "/courseClassConditionListExport/{totalNum}")
	public String courseClassConditionListExport(@PathVariable("totalNum") String totalNum, HttpServletRequest request,
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
		return "studymanage/courseclass/course_class_condition_export";
	}

	/**
	 * 学情分析--》课程班学情列表下载
	 */
	@SysLog("课程班学情列表-导出")
	@RequestMapping(value = "downLoadCourseClassConditionListExportXls", method = { RequestMethod.POST })
	public void downLoadCourseClassConditionListExportXls(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			try {
				Map searchParams = (Map) request.getSession().getAttribute("getCourseClassList");
				Workbook wb = studyManageService.downLoadCourseClassConditionListExportXls(searchParams);
				String fileName = StringUtils.getBrowserStr(request, "课程班学情列表.xls");
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
	 * 课程班学情详情
	 * 
	 * @param classId
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getCourseClassDetail/{classId}/{courseId}/{gradeId}")
	public String getCourseClassDetail(@PathVariable("classId") String classId,
			@PathVariable("courseId") String courseId, @PathVariable("gradeId") String gradeId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 获取学期列表
		Map courseClass = commonMapService.getCourseClassInfoMap(user.getGjtOrg().getId(), courseId);
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		Map pyccMap = commonMapService.getPyccMap();// 层次
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 学习中心

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("courseId", courseId);
		if("1".equals(user.getGjtOrg().getOrgType())) {
			searchParams.put("XX_ID", user.getGjtOrg().getId());
		} else {
			searchParams.put("EQ_studyId", user.getGjtOrg().getId());
		}
		// seachParams.put("orgType",user.getGjtOrg().getOrgType());
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

		searchParams.put("classId", classId);
		Page pageInfo = studyManageService.getCourseClassDetail(searchParams, pageRequst);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("courseclass", courseClass);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		request.getSession().setAttribute("commonCourseConditionDetailExport", searchParams);
		return "studymanage/courseclass/get_courseclass_detail";
	}

	/**
	 * 学习管理=》教学班学情，20171220如需改动请参考课程学情、课程班学情
	 * 
	 * @return
	 */
	@RequestMapping(value = "getTeachClassList", method = RequestMethod.GET)
	public String getTeachClassList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		// 学年度
		Map termMap = commonMapService.getTermMap(user.getGjtOrg().getId());

		Map searchParams = Servlets.getParametersStartingWith(request, "");
		String term_id = ObjectUtils.toString(searchParams.get("TERM_ID"));
		// 默认最新的学年度
		if (EmptyUtils.isEmpty(term_id)) {
			Iterator<Map.Entry<String, String>> it = termMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				term_id = ObjectUtils.toString(entry.getKey());
				searchParams.put("TERM_ID", term_id);
				break;
			}
		}
		if ("all".equals(ObjectUtils.toString(searchParams.get("TERM_ID")))) {
			term_id = "";
			searchParams.put("TERM_ID", term_id);
		}
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = studyManageService.getTeachClassList(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("termMap", termMap);
		model.addAttribute("TERM_ID", term_id);
		return "studymanage/teachclass/get_teachclass_list";
	}

	@RequestMapping(value = "getTeachClassDetails/{classId}", method = { RequestMethod.GET, RequestMethod.POST })
	public String getTeachClassDetails(@PathVariable("classId") String classId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		// 层次
		Map pyccMap = commonMapService.getPyccMap();
		// 专业名称
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());

		// Page<StudentLearnDto> infos =
		// gjtRecResultService.queryLearningSituationByClassIdPage(classId,
		// searchParams, pageRequst);

		searchParams.put("classId", classId);
		searchParams.put("xxId", user.getGjtOrg().getId());
		Page<Map<String, Object>> result = gjtRecResultService.queryLearningSituations(searchParams, pageRequst);

		Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(searchParams);
		param.put("passState", "0");
		Page<Map<String, Object>> result0 = gjtRecResultService.queryLearningSituations(param, pageRequst);// 不满足
		param.put("passState", "1");
		Page<Map<String, Object>> result1 = gjtRecResultService.queryLearningSituations(param, pageRequst);// 满足

		param.put("unpass", result0.getTotalElements());// 不满足
		param.put("pass", result1.getTotalElements());// 满足

		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("infoNum", param);
		model.addAttribute("result", result);
		// model.addAttribute("infos", infos);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "studymanage/teachclass/get_teachclass_details";
	}

	/**
	 * 学习管理=》学员课程学情
	 * 
	 * @return
	 */
	@RequestMapping(value = "getStudentCourseList", method = RequestMethod.GET)
	public String getStudentCourseList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 获取学期列表
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 学习中心
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map pyccMap = commonMapService.getPyccMap();// 层次
		Map<String, String> classMap = commonMapService.getClassInfoMap(user.getGjtOrg().getId(), "teach");// 教务班级

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
		Page pageInfo = studyManageService.getStudentCourseList(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("classMap", classMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("totalNum", pageInfo.getTotalElements());
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
		request.getSession().setAttribute("student_course_export", searchParams);// studentCourseListExport
		request.getSession().setAttribute("commonCourseConditionDetailExport", searchParams);
		return "studymanage/studentcourse/get_studentcourse_list";
	}

	/**
	 * 学员学情列表导出页面
	 * 
	 * @param totalNum
	 * @return
	 */
	@RequestMapping(value = "/studentCourseListExport/{totalNum}", method = { RequestMethod.GET, RequestMethod.POST })
	public String studentCourseListExport(@PathVariable("totalNum") String totalNum, HttpServletRequest request,
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
		return "studymanage/studentcourse/student_course_export";
	}

	/**
	 * 学情分析--》学员学情分析列表下载
	 */
	@SysLog("学员学情列表-导出")
	@RequestMapping(value = "downLoadStudentCourseListExportXls", method = { RequestMethod.POST })
	public void downLoadStudentCourseListExportXls(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			try {
				Map searchParams = (Map) request.getSession().getAttribute("student_course_export");
				Workbook wb = studyManageService.downLoadStudentCourseListExportXls(searchParams);
				String fileName = StringUtils.getBrowserStr(request,
						ObjectUtils.toString(searchParams.get("prefixName"), "") + "学员学情列表.xls");
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
	 * 学员学情 -- 详情页
	 * 
	 * @return
	 */
	@RequestMapping(value = "getStudentCourseDetails/{studentId}", method = { RequestMethod.GET, RequestMethod.POST })
	public String getStudentCourseDetails(@PathVariable("studentId") String studentId, HttpServletRequest request,
			Model model) {
		Map<String, Object> params = new HashMap<String, Object>();

		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);
		GjtClassInfo classInfo = gjtClassStudentService.queryTeachClassInfoByStudetnId(studentInfo.getStudentId());
		params.put("classId", classInfo.getClassId());
		params.put("xxId", studentInfo.getXxId());
		params.put("studentId", studentInfo.getStudentId());
		params.put("student_id", studentInfo.getStudentId());
		params.put("specialty_id", studentInfo.getMajor());

		Map<String, Object> result = gjtRecResultService.queryStuStudyCondition(params);// 学情信息总览
		// 课程学情详情
		List<Map<String, String>> infos = gjtRecResultService.queryStudentRecResultLearningDetail(params);

		// 模块学分详情
		List studentand = gjtRecResultService.getCreditInfoAnd(params);// 学分详情

		model.addAttribute("infos", infos);
		model.addAttribute("action", "view");
		model.addAttribute("result", result);
		model.addAttribute("studentand", studentand);
		return "/studymanage/studentcourse/student_course_details";
	}

	@RequestMapping(value = "courseCondition/{termcourseId}/{studentId}/{courseId}", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String courseConditionDetials(@PathVariable("termcourseId") String termcourseId,
			@PathVariable("studentId") String studentId, @PathVariable("courseId") String courseId,
			HttpServletRequest request, Model model) {
		Map<String, Object> param = new HashMap<String, Object>();
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);
		GjtClassInfo classInfo = gjtClassStudentService.queryTeachClassInfoByStudetnId(studentInfo.getStudentId());
		param.put("classId", classInfo.getClassId());
		param.put("xxId", studentInfo.getXxId());
		param.put("studentId", studentInfo.getStudentId());

		Map<String, Object> userInfo = gjtRecResultService.queryStuStudyCondition(param);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("termcourseId", termcourseId);
		params.put("studentId", studentId);
		params.put("courseId", courseId);
		Map<String, Object> result = gjtRecResultService.courseConditionDetials(params);

		userInfo.putAll(result);
		userInfo.put("URL",
				"formMap.USER_INFO="
						+ EncryptUtils.encrypt(ObjectUtils.toString(result.get("CHOOSE_ID")) + ","
								+ ObjectUtils.toString(result.get("STUDENT_ID")))
						+ "&formMap.IS_MANAGER=Y&&formMap.IS_TEST=Y");

		model.addAttribute("result", userInfo);
		return "studymanage/studentcourse/courseConditionDetials";
	}

	/**
	 * 学习管理=》学员专业学情，20171220如需要改动参考课程学情、课程班学情、学员学情！
	 * 
	 * @return
	 */
	@RequestMapping(value = "getStudentMajorList", method = RequestMethod.GET)
	public String getStudentMajorList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map classMap = commonMapService.getClassInfoMap(user.getGjtOrg().getId(), "teach");

		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = studyManageService.getStudentMajorList(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("classMap", classMap);
		return "studymanage/studentmajor/get_studentmajor_list";
	}

	/**
	 * 专业学情明细
	 * 
	 * @param specialtyId
	 * @return
	 */
	@RequestMapping(value = "getStudentMajorDetails/{specialtyId}")
	public String getStudentMajorDetails(@PathVariable("specialtyId") String specialtyId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		// 专业
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		// 层次
		Map pyccMap = commonMapService.getPyccMap();
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		if("1".equals(user.getGjtOrg().getOrgType())) {
			searchParams.put("XX_ID", user.getGjtOrg().getId());
		} else {
			searchParams.put("EQ_studyId", user.getGjtOrg().getId());
		}
		searchParams.put("SPECIALTY_ID", specialtyId);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = studyManageService.getStudentCourseList(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);

		return "studymanage/studentmajor/get_studentmajor_details";
	}

}
