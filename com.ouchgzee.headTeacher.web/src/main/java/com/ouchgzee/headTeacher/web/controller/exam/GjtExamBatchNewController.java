package com.ouchgzee.headTeacher.web.controller.exam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.gzedu.xlims.common.constants.CacheConstants;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.service.cache.CodeGeneratorService;
import com.gzedu.xlims.service.exam.GjtExamBatchNewService;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.web.common.Servlets;

@Controller
@RequestMapping("/home/class/exam/batch")
public class GjtExamBatchNewController {

	@Autowired
	private GjtExamBatchNewService gjtExamBatchNewService;
	
	@Autowired
	private CodeGeneratorService codeGeneratorService;
	
	@Autowired
	private BzrCommonMapService commonMapService;

	@RequestMapping(value = "{op}/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable String op, @PathVariable String id, Model model, HttpServletRequest request) {
		BzrGjtEmployeeInfo employeeInfo = (BzrGjtEmployeeInfo) request.getSession().getAttribute(Servlets.SESSION_EMPLOYEE_NAME);
		GjtExamBatchNew examBatch = new GjtExamBatchNew();
		if ("create".equals(op)) {
			examBatch.setExamBatchCode(codeGeneratorService.codeGenerator(CacheConstants.EXAM_BATCH_CODE));
		} else {
			examBatch = gjtExamBatchNewService.queryBy(id);
			model.addAttribute("entity", examBatch);
		}

		Map<String, String> gradeMap = commonMapService.getGradeMap(employeeInfo.getGjtOrg().getId());
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("entity", examBatch);
		model.addAttribute("action", op);
		return "new/class/exam/exam_batch_form";
	}
	
	/**
	 * 列表 
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		BzrGjtEmployeeInfo employeeInfo = (BzrGjtEmployeeInfo) request.getSession().getAttribute(Servlets.SESSION_EMPLOYEE_NAME);
		
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		searchParams.put("EQ_xxId", employeeInfo.getGjtOrg().getId());
		
		// 查询“待审核”、“审核不通过”、“已发布”和“已过期”
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.put("GTE_recordEnd", today);
		map.put("EQ_planStatus", "1");
		model.addAttribute("preAudit", gjtExamBatchNewService.queryAll(map, pageRequst).getTotalElements());
		map.put("EQ_planStatus", "2");
		model.addAttribute("auditNotPass", gjtExamBatchNewService.queryAll(map, pageRequst).getTotalElements());
		map.put("EQ_planStatus", "3");
		model.addAttribute("auditPass", gjtExamBatchNewService.queryAll(map, pageRequst).getTotalElements());
		map.remove("EQ_planStatus");
		map.remove("GTE_recordEnd");
		map.put("LT_recordEnd", today);
		model.addAttribute("overdue", gjtExamBatchNewService.queryAll(map, pageRequst).getTotalElements());
		
		if (searchParams.get("EQ_planStatus") != null && !"".equals(searchParams.get("EQ_planStatus").toString())) {
			if ("4".equals(searchParams.get("EQ_planStatus").toString())) {
				searchParams.remove("EQ_planStatus");
				searchParams.put("LT_recordEnd", today);
			} else {
				searchParams.put("GTE_recordEnd", today);
			}
		}
		Page<GjtExamBatchNew> pageInfo = gjtExamBatchNewService.queryAll(searchParams, pageRequst);
		for (GjtExamBatchNew examBatch : pageInfo) {  //设置“已过期”
			if (examBatch.getRecordEnd() != null && examBatch.getRecordEnd().compareTo(new Date()) < 0) {
				examBatch.setPlanStatus("4");
			}
		}
		model.addAttribute("pageInfo", pageInfo);

		Map<String, String> gradeMap = commonMapService.getGradeMap(employeeInfo.getGjtOrg().getId());
		model.addAttribute("gradeMap", gradeMap);
		return "new/class/exam/exam_batch_list";
	}
	
	/**
	 * 前后端日期格式转换
	 * @param binder
	 */
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		dateFormat.setLenient(false);  
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   //true:允许输入空值，false:不能为空值  
	}

}
