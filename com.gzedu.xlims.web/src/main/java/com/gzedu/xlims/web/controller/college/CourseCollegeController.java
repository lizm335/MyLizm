/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.college;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.share.GjtShareService;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 功能说明：院校模式--课程管理
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年6月6日
 * @version 3.0
 *
 */
@Controller
@RequestMapping("/edumanage/courseCollege")
public class CourseCollegeController extends BaseController {
	@Autowired
	GjtShareService gjtShareService;
	@Autowired
	CommonMapService commonMapService;
	@Autowired
	GjtCourseService gjtCourseService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Sort sort = new Sort(Direction.DESC, "createdDt");

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, sort);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		List<String> coursIds = new ArrayList<String>();
		// 院校模式暂时用 广州实验学院的课程，所以写死，将来再改 user.getGjtOrg().getId();
		String orgId = WebConstants.GK_ORG_ID;

		Page<GjtCourse> pageInfo = gjtCourseService.queryAllAndShare(orgId, searchParams, pageRequst,
				coursIds);

		model.addAttribute("user", user);
		model.addAttribute("pageInfo", pageInfo);

		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.put("EQ_isEnabled", 1);
		long isEnabledNum = gjtCourseService.queryAllAndShare(orgId, map, pageRequst, coursIds)
				.getTotalElements();
		model.addAttribute("isEnabledNum", isEnabledNum);

		map.put("EQ_isEnabled", 0);
		long isNotEnabledNum = gjtCourseService.queryAllAndShare(orgId, map, pageRequst, coursIds)
				.getTotalElements();
		model.addAttribute("isNotEnabledNum", isNotEnabledNum);

		Map<String, String> syhyMap = commonMapService.getDates("TRADE_CODE");
		model.addAttribute("syhyMap", syhyMap); // 所属行业
		Map<String, String> syzyMap = commonMapService.getSpecialtyMap(orgId);
		model.addAttribute("syzyMap", syzyMap); // 所属专业
		model.addAttribute("wsjxzkMap", commonMapService.getDates("OnlineTeaching")); // 教学方式
		model.addAttribute("courseNatureMap", commonMapService.getDates("CourseNature")); // 课程性质
		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次

		return "edumanage/college/course/college_course_list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		redirectAttributes.addFlashAttribute(WebConstants.COLLEGE_MODEL_TAG, true);
		return "redirect:/edumanage/course/view/" + id;
	}
}