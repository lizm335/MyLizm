package com.gzedu.xlims.web.controller.edumanage;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.openclass.GjtOnlineLessonCourseDao;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.pojo.GjtTermCourseinfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.PriRoleInfo;
import com.gzedu.xlims.pojo.dto.CanCourseDto;
import com.gzedu.xlims.pojo.openClass.LcmsOnlineLesson;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.edumanage.LcmsOnlineLessonService;
import com.gzedu.xlims.service.edumanage.OpenClassService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtTermCourseinfoService;
import com.gzedu.xlims.service.teachemployee.GjtTeachEmployeeService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/edumanage/openclass")
public class OpenClassController {

	@Autowired
	private OpenClassService openClassService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtCourseService gjtCourseService;

	@Autowired
	private GjtTeachPlanService gjtTeachPlanService;

	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	CommonMapService commonMapService;
	@Autowired
	private GjtTermCourseinfoService gjtTermCourseinfoService;

	@Autowired
	private GjtOnlineLessonCourseDao gjtOnlineLessonCourseDao;

	@Autowired
	private LcmsOnlineLessonService lcmsOnlineLessonService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());

		Object gradeId = searchParams.get("EQ_gradeId");
		if (gradeId == null) { // 默认选择当前学期
			GjtGrade grade = gjtGradeService.findCurrentGrade(user.getGjtOrg().getId());
			if (grade != null) {
				searchParams.put("EQ_gradeId", grade.getGradeId());
				model.addAttribute("gradeId", grade.getGradeId());
			}
		} else if ("-1".equals(gradeId.toString())) {
			searchParams.remove("EQ_gradeId");
			model.addAttribute("gradeId", searchParams.get("EQ_gradeId"));
		}

		Page<Map<String, Object>> page = openClassService.queryTermCoureList(searchParams, pageRequst);
		model.addAttribute("pageInfo", page);

		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);

		Map<String, String> courseMap = commonMapService.getCourseMap(user.getGjtOrg().getId());
		model.addAttribute("courseMap", courseMap);

		searchParams.put("TEMP_COPY_FLG", "1");
		int copy_flg1 = openClassService.getCopyCount(searchParams);
		model.addAttribute("COPY_FLG1", copy_flg1);

		searchParams.put("TEMP_COPY_FLG", "2");
		int copy_flg2 = openClassService.getCopyCount(searchParams);
		model.addAttribute("COPY_FLG2", copy_flg2);

		int copy_flg_all = copy_flg1 + copy_flg2;
		model.addAttribute("COPY_FLG_ALL", copy_flg_all);

		return "edumanage/openclass/openClassList";
	}

	@RequestMapping(value = "myOpenClassList", method = RequestMethod.GET)
	public String myOpenClassList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.findOneByAccountId(user.getId());

		searchParams.put("EQ_classTeacher", employeeInfo.getEmployeeId());

		Object gradeId = searchParams.get("EQ_gradeId");
		if (gradeId == null) { // 默认选择当前学期
			GjtGrade grade = gjtGradeService.findCurrentGrade(user.getGjtOrg().getId());
			if (grade != null) {
				searchParams.put("EQ_gradeId", grade.getGradeId());
				model.addAttribute("gradeId", grade.getGradeId());
			}
		}

		Page<Map<String, Object>> page = openClassService.queryTermCoureListByTeacher(searchParams, pageRequst);
		model.addAttribute("pageInfo", page);

		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);

		searchParams.put("EQ_status", "1");
		long copy_flg1 = openClassService.getStatusCount(searchParams);
		model.addAttribute("COPY_FLG1", copy_flg1);

		searchParams.put("EQ_status", "2");
		long copy_flg2 = openClassService.getStatusCount(searchParams);
		model.addAttribute("COPY_FLG2", copy_flg2);
		searchParams.remove("EQ_status");
		long copy_flg_all = openClassService.getStatusCount(searchParams);
		model.addAttribute("COPY_FLG_ALL", copy_flg_all);

		return "edumanage/openclass/myOpenClassList";
	}

	@RequestMapping(value = "/process", method = RequestMethod.GET)
	public String process(String gradeId, String courseId) {
		String url = "";
		List<GjtTeachPlan> teachPlanList = gjtTeachPlanService.findByActualGradeIdAndCourseId(gradeId, courseId);
		if (teachPlanList != null && teachPlanList.size() > 0) {
			String ids = "";
			for (int i = 0; i < teachPlanList.size(); i++) {
				if (i != 0) {
					ids += ",";
				}
				ids += teachPlanList.get(i).getTeachPlanId();
			}

			url = AppConfig.getProperty("courseSyncServer")
					+ "/processControl/BatchCopyCourse.do?formMap.TERMCOURSE_IDS=" + ids;
		}

		return "redirect:" + url; // 修改完重定向
	}

	/**
	 * 开设课程
	 *
	 * @return
	 */
	@RequiresPermissions("/edumanage/openclass/list$create")
	@RequestMapping(value = "/toOpenCourse", method = RequestMethod.GET)
	public String toOpenCourse(HttpServletRequest request, ModelMap model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		String gradeId = gjtGradeService.getCurrentGradeId(user.getGjtOrg().getId());
		model.addAttribute("defaultGradeId", gradeId);
		// 学期
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());

		model.addAttribute("termMap", termMap);
		model.addAttribute("action", "create");
		return "edumanage/openclass/open_course_form";
	}

	/**
	 * 获取可开设的课程
	 *
	 * @param termId
	 */
	@RequestMapping(value = "/getCourseList", method = RequestMethod.GET)
	@ResponseBody
	public Feedback getCourseList(@RequestParam("termId") String termId, HttpServletRequest request) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<CanCourseDto> list = openClassService.getCourseList(termId, user.getGjtOrg().getId());
		return new Feedback(true, "success", list);
	}

	/**
	 * 开设课程
	 *
	 * @return
	 */
	@RequiresPermissions("/edumanage/openclass/list$create")
	@RequestMapping(value = "/saveOpenCourse", method = RequestMethod.POST)
	public String saveOpenCourse(@RequestParam("termId") String termId, @RequestParam("courseIds") String courseIds,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "开设成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		boolean updateEntity = openClassService.saveOpenCourse(termId, courseIds, user);
		if (!updateEntity) {
			feedback = new Feedback(true, "服务器异常");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/openclass/list";
	}

	@RequestMapping(value = "/copycourse", method = RequestMethod.GET)
	public String copycourse(String termcourse_id) {
		String url = AppConfig.getProperty("courseSyncServer")
				+ "/processControl/processControl/index.do?formMap.TERMCOURSE_ID=" + termcourse_id;
		return "redirect:" + url; // 修改完重定向
	}

	/**
	 * 获取开设课程的选课人数
	 */
	@RequestMapping(value = "getCourseChooseCount")
	@ResponseBody
	public List getCourseChooseCount(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return openClassService.getCourseChooseCount(formMap);
	}

	/**
	 * 初始化期课程的复制状态
	 */
	@RequestMapping(value = "updTermCopyFlg")
	@ResponseBody
	public Map updTermCopyFlg(HttpServletResponse response, HttpServletRequest request) {
		Map formMap = Servlets.getParametersStartingWith(request, "");
		return openClassService.updTermCopyFlg(formMap);
	}

	/**
	 * 导出数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "expTermCourse")
	@SysLog("开课管理-导出数据")
	@ResponseBody
	public void expTermCourse(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map formMap = Servlets.getParametersStartingWith(request, "");
		try {
			formMap.put("EQ_orgId", user.getGjtOrg().getId());
			Workbook wb = openClassService.expTermCourse(formMap, request, response);
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(("开课数据.xls").getBytes("UTF-8"), "ISO8859-1"));
			wb.write(response.getOutputStream());
			request.getSession().setAttribute(user.getSjh(), "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO 授课管理

	// 直播列表
	@RequestMapping(value = "/schoolTeach")
	public String schoolTeach(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		if (EmptyUtils.isEmpty(searchParams.get("onlineLesson"))) {
			searchParams.put("search_schoolTeach", "onlineLesson");
			GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.findOneByAccountId(user.getId());
			searchParams.put("classTeacher", employeeInfo.getEmployeeId());
			model.addAttribute("pageInfo", lcmsOnlineLessonService.findOnlineLesson(searchParams, pageRequst));
			searchParams.remove("lessonType");
			long lessonNum = openClassService.getOnlineLessonCount(searchParams);
			searchParams.put("lessonType", "1");
			long lessoningNum = openClassService.getOnlineLessonCount(searchParams);
			searchParams.put("lessonType", "0");
			long needLessonNum = openClassService.getOnlineLessonCount(searchParams);
			searchParams.put("lessonType", "2");
			long lessonedNum = openClassService.getOnlineLessonCount(searchParams);
			model.addAttribute("lessonNum", lessonNum);
			model.addAttribute("lessoningNum", lessoningNum);
			model.addAttribute("needLessonNum", needLessonNum);
			model.addAttribute("lessonedNum", lessonedNum);
			model.addAttribute("userName", user.getRealName());
		}
		model.addAttribute("grades", commonMapService.getGradeMap(user.getGjtOrg().getId()));
		model.addAttribute("courseCategoryMap", commonMapService.getDatesBy("CourseCategoryInfo")); // 课程类型

		return "edumanage/openclass/schoolTeach";
	}

	// 新增修改直播
	@RequestMapping(value = "/toLesson")
	public String toCreateLesson(String id, HttpServletRequest request,
			ModelMap model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(1, 10);
		if (EmptyUtils.isNotEmpty(id)) {
			LcmsOnlineLesson onlineLesson = lcmsOnlineLessonService.queryById(id);
			model.addAttribute("onlineLesson", onlineLesson);
			List<String> termCourseIds = lcmsOnlineLessonService.findTermCourseIdByOnlinetutorId(onlineLesson.getOnlinetutorId());
			model.addAttribute("courseInfo", openClassService.getTeachPlan(termCourseIds, pageRequst));
			model.addAttribute("termIds", gjtTermCourseinfoService.findTermIdsBytermCourseIds(termCourseIds));
		}
		model.addAttribute("grades", commonMapService.getGradeMap(user.getGjtOrg().getId()));
		return "edumanage/openclass/toLesson";
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public Feedback delete(@RequestParam("onlineId") String onlineId, HttpServletRequest request) {
		Map formMap = new HashMap();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		formMap.put("onlineId", onlineId);
		formMap.put("createdBy", user.getId());
		try {
			openClassService.updateOnlineLesson(formMap, 3);
		} catch (Exception e) {
			return new Feedback(false, "系统异常");
		}
		return new Feedback(true, "success");
	}

	// 直播详情
	@RequestMapping(value = "toLessonDetail")
	public String toLessonDetail(String id, String joinType, String stuName,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			ModelMap model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> params = Servlets.getParametersStartingWith(request, "search_");

		try {
			LcmsOnlineLesson lesson = lcmsOnlineLessonService.queryById(id);
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("onlinetutorId", lesson.getOnlinetutorId());
			model.addAttribute("onlineLesson", lcmsOnlineLessonService.findOnlineLesson(temp, Servlets.buildPageRequest(1, 1)).getContent().get(0));
			long allStu = lcmsOnlineLessonService.countJoinStudent(lesson.getOnlinetutorId(), null, temp);
			long joinStu = lcmsOnlineLessonService.countJoinStudent(lesson.getOnlinetutorId(), "1", temp);
			long allNum = lcmsOnlineLessonService.countJoinStudent(lesson.getOnlinetutorId(), null, params);
			long joinNum = lcmsOnlineLessonService.countJoinStudent(lesson.getOnlinetutorId(), "1", params);
			long viewNum = lcmsOnlineLessonService.countJoinStudent(lesson.getOnlinetutorId(), "2", params);
			model.addAttribute("allStu", allStu);
			model.addAttribute("joinStu", joinStu);
			model.addAttribute("allNum", allNum);
			model.addAttribute("unjoinNum", allNum - joinNum);
			model.addAttribute("joinNum", joinNum);
			model.addAttribute("viewNum", viewNum);
			params.put("onlinetutorId", lesson.getOnlinetutorId());
			model.addAttribute("pageInfo", lcmsOnlineLessonService.findLessonStudent(params, Servlets.buildPageRequest(pageNumber, pageSize)));
			model.put("userName", user.getRealName());
			model.addAttribute("specialtyBaseMap", commonMapService.getSpecialtyBaseMap(user.getGjtOrg().getId()));
			List<GjtCourse> gjtCourses = lcmsOnlineLessonService.findGjtCourseByOnlinetutorId(lesson.getOnlinetutorId());
			model.addAttribute("gjtCourses", gjtCourses);
			List<GjtGrade> gjtGrades = lcmsOnlineLessonService.findGjtGradeByOnlinetutorId(lesson.getOnlinetutorId());
			model.addAttribute("gjtGrades", gjtGrades);
			long endTime = lesson.getOnlinetutorFinish().getTime();
			long diff = Math.abs(System.currentTimeMillis() - endTime);
			long day = diff / (24 * 60 * 60 * 1000);
			long hour = diff % (24 * 60 * 60 * 1000) / (1000 * 60 * 60);
			long min = diff % (24 * 60 * 60 * 1000) % (1000 * 60 * 60) / (1000 * 60);
			StringBuilder sb = new StringBuilder();
			if (day > 0) {
				sb.append(day).append("天 ");
			}
			if (hour > 0 || day > 0) {
				sb.append(hour).append("时 ");
			}
			sb.append(min).append("分");
			model.addAttribute("diff", sb);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/edumanage/openclass/toLessonDetail";
	}

	@RequestMapping(value = "exportLessonStudent")
	public void exportLessonStudent(String id, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = Servlets.getParametersStartingWith(request, "search_");
		LcmsOnlineLesson lesson = lcmsOnlineLessonService.queryById(id);
		params.put("onlinetutorId", lesson.getOnlinetutorId());
		params.put("joinType", params.get("exportJoinType"));
		Page<Map<String, Object>> page = lcmsOnlineLessonService.findLessonStudent(params, Servlets.buildPageRequest(1, Integer.MAX_VALUE));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", page.getContent());
		map.put("lessonName", lesson.getOnlinetutorName());
		String path = getClass().getResource(WebConstants.EXCEL_MODEL_URL).getPath() + "参与直播学生记录导出模板.xls";
		com.gzedu.xlims.common.ExcelUtil.exportExcel(map, path, response, "参与直播学生记录.xls");
	}

	// 新增
	@RequestMapping("/saveLesson")
	@ResponseBody
	public Feedback saveLesson(HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			Map<String, Object> formMap = Servlets.getParametersStartingWith(request, "search_");
			formMap.put("createdBy", user.getId());
			if (EmptyUtils.isNotEmpty(formMap.get("id")))
				openClassService.updateOnlineLesson(formMap, 2);
			else
				openClassService.updateOnlineLesson(formMap, 1);
		} catch (Exception e) {
			e.printStackTrace();
			return new Feedback(false, "系统异常");
		}
		return new Feedback(true, "success");
	}

	@RequestMapping(value = "teacherChoiceTree", method = RequestMethod.GET)
	public String classTeacherChoiceTree(@RequestParam String termCourseId, ModelMap model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtTermCourseinfo gjtTermCourseinfo = gjtTermCourseinfoService.queryById(termCourseId);
		model.addAttribute("employeeInfo", gjtTermCourseinfo.getTeacherEmployee());
		List<GjtEmployeeInfo> gjtEmployeeInfoList = gjtEmployeeInfoService
				.findListByType(String.valueOf(EmployeeTypeEnum.任课教师.getNum()), user.getGjtOrg().getId());
		model.addAttribute("gjtEmployeeInfoList", gjtEmployeeInfoList);
		if (gjtEmployeeInfoList != null && gjtEmployeeInfoList.contains(gjtTermCourseinfo.getTeacherEmployee())) {
			gjtEmployeeInfoList.remove(gjtTermCourseinfo.getTeacherEmployee());
		}
		return "/edumanage/openclass/teacherChoiceTree";
	}

	/**
	 * 配置任课教师
	 *
	 * @param termCourseId
	 * @param empId
	 * @param request
	 * @return
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月23日 下午2:44:07
	 */
	@ResponseBody
	@RequestMapping(value = "createClassTeacher", method = RequestMethod.POST)
	public Feedback createClassTeacher(@RequestParam String termCourseId, String empId, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "配置成功");
		try {
			GjtTermCourseinfo gjtTermCourseinfo = gjtTermCourseinfoService.queryById(termCourseId);
			gjtTermCourseinfo.setTeacherEmployeeId(empId);
			gjtTermCourseinfoService.save(gjtTermCourseinfo);
		} catch (Exception e) {
			feedback.setSuccessful(false);
			feedback.setMessage("系统异常");
		}
		return feedback;
	}

	@Autowired
	GjtUserAccountService gjtUserAccountService;
	@RequestMapping(value = "import")
	@ResponseBody
	public ImportFeedback importTeacher(HttpServletRequest request, HttpServletResponse response) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "课程代码", "课程名称", "授课教师", "授课教师账号", "导入结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			Date now = new Date();
			String path = getClass().getResource("/excel/temp/").getPath() + "course-teacher.xlsx";
			File file = new File(path);
			try {
				dataList = ExcelUtil.readAsStringList(new FileInputStream(file), 1, heads.length - 1);

			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}

			List<String> updateList = new ArrayList<String>();
			if (dataList != null && dataList.size() > 0) {
				for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据
					if (datas.length < heads.length - 1) {
						result[heads.length - 1] = "数据不全";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[0])) { //
						result[heads.length - 1] = "课程号不能为空";
						failedList.add(result);
						continue;
					}
					String code = datas[0];
					if (code.length() < 5) {
						code = "00000" + code;
						code = code.substring(code.length() - 5);

					}

					List<GjtTermCourseinfo> termCourseinfos = openClassService.getByCourseCodeAndOrg(code, user.getGjtOrg().getId());
					if (CollectionUtils.isEmpty(termCourseinfos)) {
						result[heads.length - 1] = "课程号不存在";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[3])) { // 教材编号
						result[heads.length - 1] = "账号不能为空";
						failedList.add(result);
						continue;
					}

					GjtUserAccount userAccount = gjtUserAccountService.queryByLoginAccount("fk" + datas[3]);
					if (userAccount == null) {
						GjtEmployeeInfo employeeInfo = new GjtEmployeeInfo();
						employeeInfo.setZgh("fk" + datas[3]);
						employeeInfo.setXm(datas[2]);
						userAccount = this.saveTeacher(employeeInfo, user, now);
					}

					GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.findOneByAccountId(userAccount.getId());
					if (employeeInfo == null) {
						result[heads.length - 1] = "账号employeeInfo不存在";
						failedList.add(result);
						continue;
					}

					for (GjtTermCourseinfo gjtTermCourseinfo : termCourseinfos) {
						gjtTermCourseinfo.setUpdatedBy("批量修改任课教师20171027");
						gjtTermCourseinfo.setCreatedDt(now);
						gjtTermCourseinfo.setTeacherEmployeeId(employeeInfo.getEmployeeId());
						gjtTermCourseinfoService.save(gjtTermCourseinfo);
						updateList.add(gjtTermCourseinfo.getTermcourseId());
					}

					result[heads.length - 1] = "修改成功";
					successList.add(datas);
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "textbook_success_" + currentTimeMillis + ".xls";
			String failedFileName = "textbook_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "教材导入成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "教材导入失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL + "textbook"
					+ File.separator;
			File f = new File(filePath);
			if (!f.exists()) {
				f.mkdirs();
			}

			File successFile = new File(filePath, successFileName);
			successFile.createNewFile();
			ExcelUtil.writeWorkbook(workbook1, successFile);

			File failedFile = new File(filePath, failedFileName);
			failedFile.createNewFile();
			ExcelUtil.writeWorkbook(workbook2, failedFile);
			ImportFeedback feedback = new ImportFeedback(true, dataList.size(), successList.size(), failedList.size(), successFileName,
					failedFileName);
			feedback.setResult(updateList);
			return feedback;
		} catch (Exception e) {
			e.printStackTrace();
			return new ImportFeedback(false, "系统异常！" + e.getMessage());
		}
	}

	@Autowired
	PriRoleInfoService priRoleInfoService;
	@Autowired
	GjtTeachEmployeeService gjtTeachEmployeeService;

	private GjtUserAccount saveTeacher(GjtEmployeeInfo item, GjtUserAccount user, Date now) {

		String employeeId = UUIDUtils.random().toString();
		String tempType = item.getEmployeeType();
		item.setEmployeeType("14");



		GjtUserAccount userAccount = new GjtUserAccount(UUIDUtils.random());
		PriRoleInfo PriRoleInfo;
		PriRoleInfo = priRoleInfoService.queryByName("任课教师");

		userAccount.setLoginAccount(item.getZgh());
		userAccount.setIsDeleted("N");
		userAccount.setRealName(item.getXm());
		userAccount.setEmail(item.getDzxx());
		userAccount.setSjh(item.getSjh());
		userAccount.setIsEnabled(true);
		userAccount.setCreatedBy("批量新增任课教师20171027");
		userAccount.setCreatedDt(now);
		userAccount.setPriRoleInfo(PriRoleInfo);
		userAccount.setPassword2("888888");
		userAccount.setPassword(Md5Util.encode("888888"));
		userAccount.setUserType(0);
		userAccount.setTelephone(item.getLxdh());

		item.setEmployeeId(employeeId);
		item.setIsDeleted("N");
		item.setCreatedDt(DateUtils.getNowTime());
		item.setCreatedBy("批量新增任课教师20171027");
		item.setIsEnabled("1");
		item.setGjtUserAccount(userAccount);
		if (!"headTeacher".equals(tempType)) {
			item.setOrgId(user.getGjtOrg().getId());
			item.setGjtSchoolInfo(user.getGjtOrg().getGjtSchoolInfo());
			userAccount.setGjtOrg(user.getGjtOrg());
		}

		gjtTeachEmployeeService.saveEmployee(item);
		return userAccount;
	}

}
