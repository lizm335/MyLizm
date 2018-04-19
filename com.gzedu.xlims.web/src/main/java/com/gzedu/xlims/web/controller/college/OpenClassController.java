package com.gzedu.xlims.web.controller.college;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtTermCourseinfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.dto.CanCourseDto;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.edumanage.OpenClassService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller("collegeOpenClassController")
@RequestMapping("/edumanage/openclassCollege")
public class OpenClassController {
	private static final Logger log = LoggerFactory.getLogger(OpenClassController.class);

	@Autowired
	private OpenClassService openClassService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtTeachPlanService gjtTeachPlanService;

	@Autowired
	CommonMapService commonMapService;

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
		}

		Page<Map<String, Object>> page = openClassService.queryGraduationSpecialtyList(searchParams, pageRequst);
		model.addAttribute("pageInfo", page);

		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);

		Map<String, String> courseMap = commonMapService.getCourseMap(user.getGjtOrg().getId());
		model.addAttribute("courseMap", courseMap);

		return "edumanage/college/openclass/openClassList";
	}

	@RequestMapping(value = "/process", method = RequestMethod.GET)
	public String process(HttpServletRequest request, String gradeId, String courseId) {
		String url = "";
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtTermCourseinfo termCourseinfo = gjtTeachPlanService
				.queryTermCourseinfoByOrgIdAndTermIdAndCourseId(user.getGjtOrg().getId(), gradeId, courseId);
		if (termCourseinfo != null) {
			url = AppConfig.getProperty("courseSyncServer") + "/processControl/index.do?formMap.TERMCOURSE_ID="
					+ termCourseinfo.getTermcourseId();
		}

		return "redirect:" + url; // 修改完重定向
	}

	/**
	 * 开课流程
	 * 
	 * @return
	 */
	@RequiresPermissions("/edumanage/openclassCollege/list$create")
	@RequestMapping(value = "/toOpenCourse", method = RequestMethod.GET)
	public String toOpenCourse(HttpServletRequest request, ModelMap model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		String gradeId = gjtGradeService.getCurrentGradeId(user.getGjtOrg().getId());
		model.addAttribute("defaultGradeId", gradeId);
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 学期

		model.addAttribute("termMap", termMap);
		model.addAttribute("action", "create");
		return "edumanage/college/openclass/open_course_form";
	}

	/**
	 * 开设课程
	 * 
	 * @return
	 */
	@SysLog("开设管理-开设课程")
	@RequiresPermissions("/edumanage/openclassCollege/list$create")
	@RequestMapping(value = "/doOpenCourse", method = RequestMethod.POST)
	public String doOpenCourse(@RequestParam("termId") String termId, @RequestParam("courseIds") String courseIds,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "开设成功");

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			openClassService.updateOpenCourse(termId, courseIds, user.getGjtOrg().getId(), user.getId());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "操作失败：异常：" + e.getMessage());
		}

		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/openclassCollege/list";
	}

	/**
	 * 获取可开设的课程
	 * 
	 * @param termId
	 */
	@RequestMapping(value = "/getCourses", method = RequestMethod.GET)
	@ResponseBody
	public Feedback getCourses(@RequestParam("termId") String termId, HttpServletRequest request) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<CanCourseDto> list = openClassService.queryCanCourseBy(termId, user.getGjtOrg().getId());
		return new Feedback(true, "success", list);
	}

}
