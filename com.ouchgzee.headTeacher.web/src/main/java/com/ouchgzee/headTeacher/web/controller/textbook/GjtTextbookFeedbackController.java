package com.ouchgzee.headTeacher.web.controller.textbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookDistribute;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookFeedback;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.service.textbook.BzrGjtTextbookDistributeService;
import com.ouchgzee.headTeacher.service.textbook.BzrGjtTextbookFeedbackService;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

@Controller
@RequestMapping("/home/class/textbookFeedback")
public class GjtTextbookFeedbackController extends BaseController {

	private static final Log log = LogFactory.getLog(GjtTextbookFeedbackController.class);

	@Autowired
	private BzrGjtTextbookFeedbackService gjtTextbookFeedbackService;

	@Autowired
	private BzrGjtTextbookDistributeService gjtTextbookDistributeService;

	@Autowired
	private BzrCommonMapService commonMapService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			Model model, HttpServletRequest request, HttpSession session) {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_gjtStudentInfo.xxId", employeeInfo.getXxId());
		searchParams.put("EQ_gjtStudentInfo.gjtClassStudentList.isDeleted", "N");
		searchParams.put("EQ_gjtStudentInfo.gjtClassStudentList.gjtClassInfo.classId", super.getCurrentClassId(session));
		
		Page<BzrGjtTextbookFeedback> pageInfo = gjtTextbookFeedbackService.findAll(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		
		//查询“待处理”状态和“已处理”状态的反馈总数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("EQ_gjtStudentInfo.xxId", employeeInfo.getXxId());
		map.put("EQ_gjtStudentInfo.gjtClassStudentList.isDeleted", "N");
		map.put("EQ_gjtStudentInfo.gjtClassStudentList.gjtClassInfo.classId", super.getCurrentClassId(session));
		map.put("EQ_status", 1);
		model.addAttribute("pending", gjtTextbookFeedbackService.findAll(map, pageRequst).getTotalElements());
		map.put("EQ_status", 2);
		model.addAttribute("processed", gjtTextbookFeedbackService.findAll(map, pageRequst).getTotalElements());
		
		return "new/class/textbook/textbookFeedback_list";
	}

	@RequestMapping(value = "view/{id}")
	public String view(@PathVariable("id") String id, Model model) {
		log.info("id:" + id);
		BzrGjtTextbookFeedback textbookFeedback = gjtTextbookFeedbackService.findOne(id);
		
		List<Integer> statuses = new ArrayList<Integer>();
		statuses.add(1);
		statuses.add(2);
		statuses.add(3);
		List<BzrGjtTextbookDistribute> textbookDistributes = gjtTextbookDistributeService.findByStudentIdAndIsDeletedAndStatusIn(textbookFeedback.getGjtStudentInfo().getStudentId(), "N", statuses);
		
		model.addAttribute("entity", textbookFeedback);
		model.addAttribute("textbookDistributes", textbookDistributes);
		model.addAttribute("trainingLevelMap", commonMapService.getPyccMap());
		
		return "new/class/textbook/textbookFeedback_detail";
	}

}
