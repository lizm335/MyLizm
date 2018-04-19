package com.ouchgzee.headTeacher.web.controller.practice;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gzedu.xlims.pojo.practice.GjtPracticePlan;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.practice.GjtPracticePlanService;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

@Controller
@RequestMapping("/home/class/practicePlan")
public class GjtPracticePlanController extends BaseController {
	
	@Autowired
	private GjtPracticePlanService gjtPracticePlanService;
	
	@Autowired
	private CommonMapService commonMapService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, HttpSession session) {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", employeeInfo.getXxId());
		searchParams.put("EQ_status", 3);

		Page<GjtPracticePlan> pageInfo = gjtPracticePlanService.findAll(searchParams, pageRequst);
		Date now = new Date();
		for (GjtPracticePlan practicePlan : pageInfo) {
			if (practicePlan.getStatus() == 1) {
				practicePlan.setStatus2(4);
			} else if (practicePlan.getStatus() == 2) {
				practicePlan.setStatus2(5);
			} else if (practicePlan.getStatus() == 3) {
				if (practicePlan.getApplyBeginDt().compareTo(now) > 0) {
					practicePlan.setStatus2(1);
				} else if (practicePlan.getApplyBeginDt().compareTo(now) <= 0
						&& practicePlan.getReviewDt().compareTo(now) >= 0) {
					practicePlan.setStatus2(2);
				} else if (practicePlan.getReviewDt().compareTo(now) < 0) {
					practicePlan.setStatus2(3);
				}
			}
		}
		model.addAttribute("pageInfo", pageInfo);
		
		Map<String, String> termMap = commonMapService.getGradeMap(employeeInfo.getXxId());
		model.addAttribute("termMap", termMap);
		

		return "new/class/practice/practicePlan_list";
	}

}
