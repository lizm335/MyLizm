package com.ouchgzee.headTeacher.web.controller.textbook;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookPlan;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.service.textbook.BzrGjtTextbookPlanService;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

@Controller
@RequestMapping("/home/class/textbookPlan")
public class GjtTextbookPlanController extends BaseController {

	//private final static Logger log = LoggerFactory.getLogger(GjtTextbookPlanController.class);

	@Autowired
	private BzrGjtTextbookPlanService gjtTextbookPlanService;
	
	@Autowired
	private BzrCommonMapService commonMapService;


	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, HttpSession session) {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", employeeInfo.getXxId());
		searchParams.put("EQ_status", 3);

		Page<BzrGjtTextbookPlan> pageInfo = gjtTextbookPlanService.findAll(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("gradeMap", commonMapService.getGradeMap(employeeInfo.getXxId()));

		return "new/class/textbook/textbookPlan_list";
	}


	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("entity", gjtTextbookPlanService.findOne(id));
		model.addAttribute("action", "view");
		return "new/class/textbook/textbookPlan_form";
	}



	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
	}

}
