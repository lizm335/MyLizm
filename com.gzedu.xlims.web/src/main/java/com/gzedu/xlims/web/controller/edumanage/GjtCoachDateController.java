/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.openClass.GjtCoachDate;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.OpenClassService;
import com.gzedu.xlims.service.organization.GjtCoachDateService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/edumanage/coachdata")
public class GjtCoachDateController {
	private static final Logger log = LoggerFactory.getLogger(GjtCoachDateController.class);

	@Autowired
	private GjtCoachDateService gjtCoachDateService;
	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;
	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private OpenClassService openClassService;

	@RequestMapping(value = "/coachDateList", method = RequestMethod.GET)
	public String findCoachDateList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		searchParams.put("EQ_createdBy", user.getId());
		Page<Map<String, Object>> page = gjtCoachDateService.findCoachDateList(searchParams, pageRequst);
		model.addAttribute("pageInfo", page);
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);

		return "/edumanage/caochdate/caochdate_list";
	}

	@RequestMapping(value = "/toCreate", method = RequestMethod.GET)
	public String toCreate(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);
		model.addAttribute("action", "create");
		return "/edumanage/caochdate/caochdate_form";
	}

	/**
	 * 新增共享资料
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月28日 上午10:05:37
	 * @param dataName
	 * @param dataPath
	 * @param termCourseIds
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Feedback create(GjtCoachDate gjtCoachDate, @RequestParam List<String> termCourseIds,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "保存成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			gjtCoachDate.setCreatedBy(user.getId());
			gjtCoachDate.setIsDeleted("N");
			gjtCoachDate.setOrgId(user.getGjtOrg().getId());
			gjtCoachDateService.createCoachDate(gjtCoachDate, termCourseIds);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback.setMessage("系统异常");
			feedback.setSuccessful(false);
		}
		return feedback;
	}

	@RequestMapping(value = "/chooseCourse", method = RequestMethod.GET)
	public String chooseCourse(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request, String gradeIds,String termCourseIds) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.findOneByAccountId(user.getId());
		if (employeeInfo != null)
			searchParams.put("EQ_classTeacher", employeeInfo.getEmployeeId());
		List<String> gradeIdList = null;
		if (StringUtils.isNoneBlank(gradeIds)) {
			gradeIdList = Arrays.asList(gradeIds.split(","));
		}
		searchParams.put("IN_gradeIds", gradeIdList);
		List<String> termCourseIdList = null;
		if (StringUtils.isNoneBlank(termCourseIds)) {
			termCourseIdList = Arrays.asList(termCourseIds.split(","));
		}
		searchParams.put("NOTIN_termCourseIds", termCourseIdList);
		Page<Map<String, Object>> page = openClassService.queryTermCoureListByTeacher(searchParams, pageRequst);
		model.addAttribute("pageInfo", page);
		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);
		return "/edumanage/caochdate/choose_course";
	}

	/**
	 * 删除共享资料
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月28日 下午4:15:05
	 * @param dataId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteData", method = RequestMethod.GET)
	public Feedback deleteData(String dataId) {
		try {
			GjtCoachDate gjtCoachDate = gjtCoachDateService.queryById(dataId);
			gjtCoachDate.setIsDeleted("Y");
			gjtCoachDateService.save(gjtCoachDate);
			return new Feedback(true, "删除成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Feedback(false, "删除失败");
		}
	}

	@RequestMapping(value = "downloadData", method = RequestMethod.GET)
	public String downloadData(String dataId, String redUrl) {
		try {
			GjtCoachDate gjtCoachDate = gjtCoachDateService.queryById(dataId);
			gjtCoachDate.setDownloadNum(gjtCoachDate.getDownloadNum() + 1);
			gjtCoachDateService.save(gjtCoachDate);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "redirect:" + redUrl; // 修改完重定向
	}

}
