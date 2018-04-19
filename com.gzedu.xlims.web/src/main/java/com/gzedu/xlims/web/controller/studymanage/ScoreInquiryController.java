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
 * 功能说明：学习管理-成绩查询
 * 
 * @author
 * @Date
 * @version
 */
@Controller
@RequestMapping("/studymanage")
public class ScoreInquiryController extends BaseController {
	private final static Logger log = LoggerFactory.getLogger(ScoreInquiryController.class);

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


	/** ---------------------------------------------------------------- 成绩查询 ---------------------------------------------------------------- **/


	/**
	 * 学习管理=》成绩查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "getScoreList")
	public String getScoreList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业名称
		Map pyccMap = commonMapService.getPyccMap();// 层次
		Map examTypeMap = commonMapService.getExamTypeMap();// 考试方式
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 入学学期
		Map yearMap = commonMapService.getYearMap(user.getGjtOrg().getId());// 入学年级
		Map courseMap = commonMapService.getCourseMap(user.getGjtOrg().getId());// 课程列表
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 学习中心

		Map searchParams = Servlets.getParametersStartingWith(request, "");
		
		if (EmptyUtils.isNotEmpty(user.getGjtOrg().getGjtStudyCenter())) {
			searchParams.put("EQ_studyId", user.getGjtOrg().getGjtStudyCenter().getId());
		} else {
			searchParams.put("XX_ID", user.getGjtOrg().getId());
		}
		
		Map<String, String> currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(currentGradeMap)) {
			String currentGradeId = currentGradeMap.keySet().iterator().next();
			model.addAttribute("currentGradeId", currentGradeId);
			if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("NJ")))) {// 如果没有选择，则默认查当前学期
				searchParams.put("NJ", currentGradeId);
			} else if ("all".equals(ObjectUtils.toString(searchParams.get("NJ")))) {
				searchParams.remove("NJ");
			}
		}
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Page pageInfo = studyManageService.getScoreList(searchParams, pageRequst);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("examTypeMap", examTypeMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("yearMap", yearMap);
		model.addAttribute("courseMap", courseMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		StringBuffer prefixName = new StringBuffer();// 导出文件名字根据搜索条件命名前缀
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			prefixName.append(
					ObjectUtils.toString(studyCenterMap.get(ObjectUtils.toString(searchParams.get("XXZX_ID")))) + "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("NJ")))) {
			prefixName.append(ObjectUtils.toString(gradeMap.get(ObjectUtils.toString(searchParams.get("NJ")))) + "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			prefixName.append(
					ObjectUtils.toString(specialtyMap.get(ObjectUtils.toString(searchParams.get("SPECIALTY_ID"))))
							+ "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))) {
			prefixName.append(
					ObjectUtils.toString(courseMap.get(ObjectUtils.toString(searchParams.get("COURSE_ID")))) + "-");
		}
		searchParams.put("prefixName", ObjectUtils.toString(prefixName.toString(), ""));
		request.getSession().setAttribute("downLoadScoreListExportXls", searchParams);// 导出数据的查询条件
		return "studymanage/score/get_score_list";
	}

	/**
	 * 成绩查询导出页面
	 * 
	 * @param totalNum
	 * @return
	 */
	@RequestMapping(value = "/scoreListExport/{totalNum}", method = { RequestMethod.GET, RequestMethod.POST })
	public String scoreListExport(@PathVariable("totalNum") String totalNum, HttpServletRequest request, Model model) {
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
		return "studymanage/score/score_list_export";
	}

	/**
	 * 成绩查询--》成绩列表导出
	 */
	@SysLog("成绩列表-导出")
	@RequestMapping(value = "downLoadScoreListExportXls", method = { RequestMethod.POST })
	public void downLoadScoreListExportXls(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			try {
				Map searchParams = (Map) request.getSession().getAttribute("downLoadScoreListExportXls");
				Workbook wb = studyManageService.downLoadScoreListExportXls(searchParams);
				String fileName = StringUtils.getBrowserStr(request,
						ObjectUtils.toString(searchParams.get("prefixName"), "") + "学员成绩列表.xls");
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
	 * 学习管理=》成绩查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "getScoreListCount")
	@ResponseBody
	public Map getScoreListCount(HttpServletRequest request) {
		Map resultMap = new HashMap();
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map searchParams = Servlets.getParametersStartingWith(request, "");
			
			if (EmptyUtils.isNotEmpty(user.getGjtOrg().getGjtStudyCenter())) {
				searchParams.put("EQ_studyId", user.getGjtOrg().getGjtStudyCenter().getId());
			} else {
				searchParams.put("XX_ID", user.getGjtOrg().getId());
			}
	
			// 查询条件统计项
			searchParams.put("EXAM_STATE", searchParams.get("EXAM_STATE_TEMP"));
			long score_state_count = studyManageService.getScoreCount(searchParams);
			resultMap.put("EXAM_STATE_COUNT", score_state_count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 学习管理=》学分查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "getCreditsList", method = RequestMethod.GET)
	public String getCreditsList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业名称
		Map pyccMap = commonMapService.getPyccMap();// 层次
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 入学学期
		Map yearMap = commonMapService.getYearMap(user.getGjtOrg().getId());// 入学年级

		Map searchParams = Servlets.getParametersStartingWith(request, "");
		
		if (EmptyUtils.isNotEmpty(user.getGjtOrg().getGjtStudyCenter())) {
			searchParams.put("EQ_studyId", user.getGjtOrg().getGjtStudyCenter().getId());
		} else {
			searchParams.put("XX_ID", user.getGjtOrg().getId());
		}

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = studyManageService.getCreditsList(searchParams, pageRequst);

		// 查询条件统计项
		searchParams.put("SCORE_TYPE", "");
		long score_count = studyManageService.getCreditsCount(searchParams);
		searchParams.put("SCORE_TYPE", "2");
		long score_count2 = studyManageService.getCreditsCount(searchParams);

		model.addAttribute("SCORE_COUNT", score_count);
		model.addAttribute("SCORE_COUNT1", (score_count - score_count2));
		model.addAttribute("SCORE_COUNT2", score_count2);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("yearMap", yearMap);

		return "studymanage/credits/get_credits_list";
	}

}
