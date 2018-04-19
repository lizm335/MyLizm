/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.dto.YearCourseDto;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtStudyYearCourseService;
import com.gzedu.xlims.service.edumanage.GjtStudyYearService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/edumanage/yearcourse")
public class GjtYearCourseController {

	@Autowired
	GjtStudyYearService gjtStudyYearService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtStudyYearCourseService gjtStudyYearCourseService;

	@Autowired
	CommonMapService commonMapService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String schoolId = user.getGjtOrg().getId();

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<YearCourseDto> page = gjtStudyYearService.queryYearCourse(schoolId, searchParams, pageRequst);
		for (YearCourseDto item : page) {
			Integer count = gjtStudyYearService.queryCount(schoolId, item.getCOURSEID(), item.getYEARID().intValue());
			item.setCLASSNUM(count);
		}
		Map<Integer, String> studyYearMap = commonMapService.getStudyYearMap();

		model.addAttribute("pageInfo", page);
		model.addAttribute("studyYearMap", studyYearMap);
		
		return "edumanage/yearcourse/list";
	}

	// @RequestMapping(value = "list", method = RequestMethod.GET)
	// public String list(@RequestParam(value = "page", defaultValue = "1") int
	// pageNumber,
	// @RequestParam(value = "page.size", defaultValue = "10") int pageSize,
	// Model model,
	// HttpServletRequest request) {
	// GjtUserAccount user = (GjtUserAccount)
	// request.getSession().getAttribute(WebConstants.CURRENT_USER);
	// String schoolId = user.getGjtOrg().getId();
	//
	// PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
	// Map<String, Object> searchParams =
	// Servlets.getParametersStartingWith(request, "search_");
	//
	// Page<GjtStudyYearCourse> page =
	// gjtStudyYearCourseService.queryAll(schoolId, searchParams, pageRequst);
	// // for (YearCourseDto item : page) {
	// // Integer count = gjtStudyYearService.queryCount(schoolId,
	// // item.getCOURSEID(),
	// // Integer.valueOf(item.getYEARID()));
	// // item.setCLASSNUM(count);
	// // }
	// Map<Integer, String> studyYearMap = commonMapService.getStudyYearMap();
	//
	// model.addAttribute("pageInfo", page);
	// model.addAttribute("studyYearMap", studyYearMap);
	// model.addAttribute("searchParams",
	// Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
	// return "edumanage/yearcourse/list";
	// }
}
