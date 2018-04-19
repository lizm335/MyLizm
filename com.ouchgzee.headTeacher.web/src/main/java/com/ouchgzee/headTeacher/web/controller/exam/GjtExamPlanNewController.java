package com.ouchgzee.headTeacher.web.controller.exam;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.MapKit;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.exam.GjtExamPlanNewService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.web.common.Servlets;

@Controller
@RequestMapping("/home/class/exam/plan")
public class GjtExamPlanNewController {
	
	private final static Logger log = LoggerFactory.getLogger(GjtExamPlanNewController.class);

	@Autowired
	private GjtExamPlanNewService gjtExamPlanNewService;

	@Autowired
	private CommonMapService commonMapService;
	
	@Autowired
	private  GjtCourseService gjtCourseService;

	@Autowired
	private GjtOrgService gjtOrgService;

	/**
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		BzrGjtEmployeeInfo employeeInfo = (BzrGjtEmployeeInfo) request.getSession().getAttribute(Servlets.SESSION_EMPLOYEE_NAME);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, "createdDt", "DESC");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		searchParams.put("EQ_xxId", employeeInfo.getGjtOrg().getId());
		
		String schoolId= employeeInfo.getGjtOrg().getId();
		
		// 默认选择当前期(批次)
		if(EmptyUtils.isEmpty(searchParams)|| StringUtils.isBlank((String) searchParams.get("EQ_examBatchCode"))){
			String code = commonMapService.getCurrentGjtExamBatchNew(schoolId);
			searchParams.put("EQ_examBatchCode", code);
			model.addAttribute("examBatchCode",code);
		}else if(EmptyUtils.isNotEmpty(searchParams) && EmptyUtils.isNotEmpty(searchParams.get("EQ_examBatchCode")) ){
			model.addAttribute("examBatchCode", ObjectUtils.toString(searchParams.get("EQ_examBatchCode")));
		}
		
		// 查询“未开始”、“预约中”、“待考试”、“考试中”和“已结束”
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.remove("EQ_status");
		model.addAttribute("all", gjtExamPlanNewService.queryAll(map, pageRequst).getTotalElements());
		
		map.put("GT_examBatchNew.bookSt", today);
		model.addAttribute("notStart", gjtExamPlanNewService.queryAll(map, pageRequst).getTotalElements());
		
		map.remove("GT_examBatchNew.bookSt");
		map.put("LTE_examBatchNew.bookSt", today);
		map.put("GTE_examBatchNew.bookEnd", today);
		model.addAttribute("booking", gjtExamPlanNewService.queryAll(map, pageRequst).getTotalElements());
		
		map.remove("LTE_examBatchNew.bookSt");
		map.remove("GTE_examBatchNew.bookEnd");
		map.put("LT_examBatchNew.bookEnd", today);
		map.put("GT_examSt", today);
		model.addAttribute("preExam", gjtExamPlanNewService.queryAll(map, pageRequst).getTotalElements());
		
		map.remove("LT_examBatchNew.bookEnd");
		map.remove("GT_examSt");
		map.put("LTE_examSt", today);
		map.put("GT_examEnd", today);
		model.addAttribute("examing", gjtExamPlanNewService.queryAll(map, pageRequst).getTotalElements());
		
		map.remove("LTE_examSt");
		map.remove("GT_examEnd");
		map.put("LT_examEnd", today);
		model.addAttribute("end", gjtExamPlanNewService.queryAll(map, pageRequst).getTotalElements());
		
		//状态转换
		if (searchParams.get("EQ_status") != null && !"".equals(searchParams.get("EQ_status"))) {
			int status = Integer.parseInt(searchParams.get("EQ_status").toString());
			if (status == 1) {
				searchParams.put("GT_examBatchNew.bookSt", today);
			} else if (status == 2) {
				searchParams.put("LTE_examBatchNew.bookSt", today);
				searchParams.put("GTE_examBatchNew.bookEnd", today);
			} else if (status == 3) {
				searchParams.put("LT_examBatchNew.bookEnd", today);
				searchParams.put("GT_examSt", today);
			} else if (status == 4) {
				searchParams.put("LTE_examSt", today);
				searchParams.put("GT_examEnd", today);
			} else if (status == 5) {
				searchParams.put("LT_examEnd", today);
			}
		}
		searchParams.remove("EQ_status");
		
		Page<GjtExamPlanNew> pageInfo = gjtExamPlanNewService.queryAll(searchParams, pageRequst);
		Date now = new Date();
		for (GjtExamPlanNew examPlan : pageInfo) {
			if (examPlan.getExamBatchNew().getBookSt().compareTo(now) > 0) {
				examPlan.setStatus(1);  //未开始
			} else if (examPlan.getExamBatchNew().getBookEnd().compareTo(now) >= 0
					&& examPlan.getExamBatchNew().getBookSt().compareTo(now) <= 0) {
				examPlan.setStatus(2);  //预约中
			} else if (examPlan.getExamBatchNew().getBookEnd().compareTo(now) < 0
					&& examPlan.getExamSt() != null && examPlan.getExamSt().compareTo(now) > 0) {
				examPlan.setStatus(3);  //待考试
			} else if (examPlan.getExamSt() != null && examPlan.getExamSt().compareTo(now) <= 0
					&& examPlan.getExamEnd() != null && examPlan.getExamEnd().compareTo(now) > 0) {
				examPlan.setStatus(4);  //考试中
			} else if (examPlan.getExamEnd() != null && examPlan.getExamEnd().compareTo(now) < 0) {
				examPlan.setStatus(5);  //已结束
			}
		}
		//searchParams.put("schoolId",schoolId);
		//Page<Map> pageInfo =gjtExamPlanNewService.queryExamPlan(searchParams,pageRequst);
		
		model.addAttribute("courseList", gjtCourseService.findByXxidAndIsDeleted(schoolId, "N"));
		model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(schoolId));
		model.addAttribute("examTypeMap", MapKit.toIntAscMap(commonMapService.getExamTypeIntMap()));
		model.addAttribute("examStyleMap", commonMapService.getDates("EXAM_STYLE"));
		model.addAttribute("pageInfo", pageInfo);
		
		return "/new/class/exam/exam_plan_list";
	}

	/**
	 * 根据搜索条件导出开考科目
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "export")
	@ResponseBody
	public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BzrGjtEmployeeInfo employeeInfo = (BzrGjtEmployeeInfo) request.getSession().getAttribute(Servlets.SESSION_EMPLOYEE_NAME);
		PageRequest pageRequst = Servlets.buildPageRequest(1, 10000, "createdDt", "DESC");// 单次最多导出10000条
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_xxId", employeeInfo.getGjtOrg().getId());
		
		//状态转换
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		if (searchParams.get("EQ_status") != null && !"".equals(searchParams.get("EQ_status"))) {
			int status = Integer.parseInt(searchParams.get("EQ_status").toString());
			if (status == 1) {
				searchParams.put("GT_examBatchNew.bookSt", today);
			} else if (status == 2) {
				searchParams.put("LTE_examBatchNew.bookSt", today);
				searchParams.put("GTE_examBatchNew.bookEnd", today);
			} else if (status == 3) {
				searchParams.put("LT_examBatchNew.bookEnd", today);
				searchParams.put("GT_examSt", today);
			} else if (status == 4) {
				searchParams.put("LTE_examSt", today);
				searchParams.put("GT_examEnd", today);
			} else if (status == 5) {
				searchParams.put("LT_examEnd", today);
			}
		}
		searchParams.remove("EQ_status");
		
		Page<GjtExamPlanNew> pageInfo = gjtExamPlanNewService.queryAll(searchParams, pageRequst);
		Date now = new Date();
		for (GjtExamPlanNew examPlan : pageInfo) {
			if (examPlan.getExamBatchNew().getBookSt().compareTo(now) > 0) {
				examPlan.setStatus(1);  //未开始
			} else if (examPlan.getExamBatchNew().getBookEnd().compareTo(now) >= 0
					&& examPlan.getExamBatchNew().getBookSt().compareTo(now) <= 0) {
				examPlan.setStatus(2);  //预约中
			} else if (examPlan.getExamBatchNew().getBookEnd().compareTo(now) < 0
					&& examPlan.getExamSt() != null && examPlan.getExamSt().compareTo(now) > 0) {
				examPlan.setStatus(3);  //待考试
			} else if (examPlan.getExamSt() != null && examPlan.getExamSt().compareTo(now) <= 0
					&& examPlan.getExamEnd() != null && examPlan.getExamEnd().compareTo(now) > 0) {
				examPlan.setStatus(4);  //考试中
			} else if (examPlan.getExamEnd() != null && examPlan.getExamEnd().compareTo(now) < 0) {
				examPlan.setStatus(5);  //已结束
			}
		}
		
		HSSFWorkbook workbook = gjtExamPlanNewService.exportByList(pageInfo.getContent());
		String fileName = "开考科目表.xls";
		response.setContentType("application/x-msdownload;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
		workbook.write(response.getOutputStream());
	}

	/**
	 * 前后端日期格式转换
	 * @param binder
	 */
	@InitBinder  
	public void initBinder(WebDataBinder binder) {  
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
		dateFormat.setLenient(false);  
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));   //true:允许输入空值，false:不能为空值  
	}
}
