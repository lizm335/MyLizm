package com.ouchgzee.headTeacher.web.controller.thesis;

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

import com.gzedu.xlims.pojo.thesis.GjtThesisPlan;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.thesis.GjtThesisPlanService;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

@Controller
@RequestMapping("/home/class/thesisPlan")
public class GjtThesisPlanController extends BaseController {

	@Autowired
	private GjtThesisPlanService gjtThesisPlanService;
	
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
		
		Page<GjtThesisPlan> pageInfo = gjtThesisPlanService.findAll(searchParams, pageRequst);
		Date now = new Date();
		for (GjtThesisPlan thesisPlan : pageInfo) {
			if (thesisPlan.getStatus() == 1) {
				thesisPlan.setStatus2(4);
			} else if (thesisPlan.getStatus() == 2) {
				thesisPlan.setStatus2(5);
			} else if (thesisPlan.getStatus() == 3) {
				if (thesisPlan.getApplyBeginDt().compareTo(now) > 0) {
					thesisPlan.setStatus2(1);
				} else if (thesisPlan.getApplyBeginDt().compareTo(now) <= 0
						&& thesisPlan.getDefenceDt().compareTo(now) >= 0) {
					thesisPlan.setStatus2(2);
				} else if (thesisPlan.getDefenceDt().compareTo(now) < 0) {
					thesisPlan.setStatus2(3);
				}
			}
		}
		model.addAttribute("pageInfo", pageInfo);
		
		Map<String, String> termMap = commonMapService.getGradeMap(employeeInfo.getXxId());
		model.addAttribute("termMap", termMap);

		return "new/class/thesis/thesisPlan_list";
	}

}
