package com.gzedu.xlims.web.controller.exam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.exam.GjtExamAppointmentNew;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.exam.GjtExamAppointmentNewService;
import com.gzedu.xlims.service.exam.GjtExamStudentArrangesService;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 排考安排
 */
@Controller
@RequestMapping("/exam/new/arrange")
public class GjtExamStudentArrangesController {
 
	@Autowired
	private GjtExamStudentArrangesService gjtExamStudentArrangesService;
	
	@Autowired
	private GjtExamAppointmentNewService gjtExamAppointmentNewService;
	@Autowired
	private CommonMapService commonMapService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		/** 过渡版本代码 start*/
		if(null==user || null!=request.getParameter("userid")) {
			user = new GjtUserAccount();
			user.setId(request.getParameter("userid"));
			GjtOrg org = new GjtOrg();
			org.setId(request.getParameter("xxid"));
			user.setGjtOrg(org);
			request.getSession().setAttribute(WebConstants.CURRENT_USER, user);
		}
		/** 过渡版本代码 end*/
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		if (null == searchParams || null == searchParams.get("EQ_type") || "".equals(searchParams.get("EQ_type").toString())) {
			searchParams.put("EQ_type", "2");
		}
		if(null!=searchParams.get("EQ_status") && "-1".equals(searchParams.get("EQ_status"))) {
			searchParams.remove("EQ_status");
			searchParams.put("LT_status", "2");//过期数据默认不显示
		}
		Page<GjtExamAppointmentNew> pageInfo = gjtExamAppointmentNewService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);
		if(StringUtils.isBlank((String) searchParams.get("EQ_status"))) {
			searchParams.put("EQ_status", "-1");
		}
		List<String> ids = new ArrayList<String>();
		List<GjtExamAppointmentNew> list = Lists.newArrayList(pageInfo.iterator());
		for (GjtExamAppointmentNew gjtExamAppointmentNew : list) {
			ids.add(gjtExamAppointmentNew.getStudentId());
		}
		Map<String, Map<String, String>> studentViewMap = gjtExamAppointmentNewService.studentViewList(ids);
		model.addAttribute("studentViewMap", studentViewMap);
		Map<String, String> appointPointMap = gjtExamAppointmentNewService.appointPointMap(ids);
		model.addAttribute("appointPointMap", appointPointMap);
		
		Map<String, String> subjectMap = commonMapService.getGjtExamSubjectNewIdNameMap(user.getGjtOrg().getId(), Integer.parseInt(searchParams.get("EQ_type").toString()));
		model.addAttribute("subjectMap", subjectMap);
		Map<String, String> batchMap = commonMapService.getGjtExamBatchNewIdNameMap(user.getGjtOrg().getId());
		model.addAttribute("batchMap", batchMap);
		
		model.addAttribute("exam_type", searchParams.get("EQ_type"));
		model.addAttribute("user", user);
		model.addAttribute("pageInfo", pageInfo);
		
		
		return "edumanage/exam/exam_arrange_list";
	}
	
	/**
	 * 导出已排考数据. 
	 * @param examBatchCode
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/export/arrange", method = RequestMethod.POST)
    @ResponseBody
    public void exportPlanAppoinments(@RequestParam String examBatchCode, 
    		HttpServletRequest request, HttpServletResponse response) throws Exception{
        GjtUserAccount user = (GjtUserAccount)request.getSession().getAttribute(WebConstants.CURRENT_USER);
        /** 过渡版本代码 start*/
        if(null == user) {
			user = new GjtUserAccount();
			user.setId(request.getParameter("userid"));
			GjtOrg org = new GjtOrg();
			org.setId(request.getParameter("xxid"));
			user.setGjtOrg(org);
			request.getSession().setAttribute(WebConstants.CURRENT_USER, user);
		}
		/** 过渡版本代码 end*/
        Map<String, String> params = new HashMap<String, String>();
        params.put("examBatchCode", examBatchCode);
        params.put("status", "1");
        List<Map<String, Object>> list = gjtExamAppointmentNewService.appointmentListBySearch(params);
        HSSFWorkbook workbook = gjtExamStudentArrangesService.exportArrange(list);
        
        response.setContentType("application/x-msdownload;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + new String("排考明细.xls".getBytes("UTF-8"), "ISO8859-1"));
		workbook.write(response.getOutputStream());
		
    }
}
