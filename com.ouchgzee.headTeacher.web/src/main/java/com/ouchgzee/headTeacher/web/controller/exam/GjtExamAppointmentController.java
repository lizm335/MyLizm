package com.ouchgzee.headTeacher.web.controller.exam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
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

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.ouchgzee.headTeacher.pojo.BzrGjtClassInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.exam.BzrGjtExamBatchNew;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.service.exam.BzrGjtExamAppointmentService;
import com.ouchgzee.headTeacher.service.exam.BzrGjtExamBatchNewService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

@Controller
@RequestMapping("/home/class/exam/appointment")
public class GjtExamAppointmentController extends BaseController {

	@Autowired
	private BzrGjtExamBatchNewService gjtExamBatchNewService;

	@Autowired
	private BzrGjtExamAppointmentService gjtExamAppointmentService;

	@Autowired
	private BzrGjtStudentService gjtStudentService;
	
	@Autowired
	private BzrCommonMapService commonMapService;
	

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		BzrGjtEmployeeInfo employeeInfo = (BzrGjtEmployeeInfo) request.getSession().getAttribute(Servlets.SESSION_EMPLOYEE_NAME);
		BzrGjtClassInfo classInfo = super.getCurrentClass(request.getSession());
		String orgId = "-1";
		if ("1".equals(employeeInfo.getGjtOrg().getOrgType())) {
			orgId = employeeInfo.getGjtOrg().getId();
		} else {
			orgId = commonMapService.getParentWithType(employeeInfo.getGjtOrg().getId(), "1");
		}
		
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		
		//考试计划设置默认值
        if(EmptyUtils.isEmpty(searchParams) || StringUtils.isBlank((String) searchParams.get("EQ_examBatchCode"))){
        	BzrGjtExamBatchNew examBatch = gjtExamBatchNewService.findCurrentExamBatch(orgId);
    		if (examBatch != null && "3".equals(examBatch.getPlanStatus())) {
    			//当前学期存在已发布的考试计划
    			Page<Map<String, Object>> pageInfo = gjtExamAppointmentService.findCurrentAppointmentList(classInfo.getClassId(), examBatch.getExamBatchCode(), searchParams, pageRequst);
    			model.addAttribute("pageInfo", pageInfo);
    			model.addAttribute("isCurrent", true);
    			model.addAttribute("examBatchCode", examBatch.getExamBatchCode());
    			model.addAttribute("examBatch", examBatch);
    			
    			// 查询“未预约科目”、“已预约科目”、“未预约考点”和“已预约考点”
    			Map<String, Object> map = new HashMap<String, Object>();
    			map.putAll(searchParams);
    			map.remove("EQ_pointStatus");
    			
    			map.put("EQ_appointmentStatus", "0");
    			model.addAttribute("appointmentStatus0", gjtExamAppointmentService.findCurrentAppointmentList(classInfo.getClassId(), examBatch.getExamBatchCode(), map, pageRequst).getTotalElements());

    			map.put("EQ_appointmentStatus", "1");
    			model.addAttribute("appointmentStatus1", gjtExamAppointmentService.findCurrentAppointmentList(classInfo.getClassId(), examBatch.getExamBatchCode(), map, pageRequst).getTotalElements());
    			
    			map.remove("EQ_appointmentStatus");
    			map.put("EQ_pointStatus", "0");
    			model.addAttribute("pointStatus0", gjtExamAppointmentService.findCurrentAppointmentList(classInfo.getClassId(), examBatch.getExamBatchCode(), map, pageRequst).getTotalElements());

    			map.put("EQ_pointStatus", "1");
    			model.addAttribute("pointStatus1", gjtExamAppointmentService.findCurrentAppointmentList(classInfo.getClassId(), examBatch.getExamBatchCode(), map, pageRequst).getTotalElements());
    		} else {
    			//查询往期计划
    			BzrGjtExamBatchNew examBatch2 = gjtExamBatchNewService.findLastExamBatch(orgId);
    			if (examBatch2 != null) {
    				Page<Map<String, Object>> pageInfo = gjtExamAppointmentService.findHistoryAppointmentList(classInfo.getClassId(), examBatch2.getExamBatchCode(), searchParams, pageRequst);
        			model.addAttribute("pageInfo", pageInfo);
        			model.addAttribute("isCurrent", false);
                    model.addAttribute("examBatchCode", examBatch2.getExamBatchCode());
        			model.addAttribute("examBatch", examBatch2);
        			
        			// 查询“未预约科目”和“已预约科目”
        			Map<String, Object> map = new HashMap<String, Object>();
        			map.putAll(searchParams);
        			
        			map.put("EQ_appointmentStatus", "0");
        			model.addAttribute("appointmentStatus0", gjtExamAppointmentService.findHistoryAppointmentList(classInfo.getClassId(), examBatch2.getExamBatchCode(), map, pageRequst).getTotalElements());
        			
        			map.put("EQ_appointmentStatus", "1");
        			model.addAttribute("appointmentStatus1", gjtExamAppointmentService.findHistoryAppointmentList(classInfo.getClassId(), examBatch2.getExamBatchCode(), map, pageRequst).getTotalElements());
    			} else {
    				model.addAttribute("isCurrent", false);
    				model.addAttribute("appointmentStatus0", 0);
    				model.addAttribute("appointmentStatus1", 0);
    			}
    		}
        } else if(EmptyUtils.isNotEmpty(searchParams) && EmptyUtils.isNotEmpty(searchParams.get("EQ_examBatchCode")) ){
            String examBatchCode = ObjectUtils.toString(searchParams.get("EQ_examBatchCode"));
            BzrGjtExamBatchNew examBatch = gjtExamBatchNewService.findCurrentExamBatch(orgId);
            if (examBatch != null && examBatchCode.equals(examBatch.getExamBatchCode())) {
            	//选择的是当前学期的考试计划
            	Page<Map<String, Object>> pageInfo = gjtExamAppointmentService.findCurrentAppointmentList(classInfo.getClassId(), examBatchCode, searchParams, pageRequst);
    			model.addAttribute("pageInfo", pageInfo);
    			model.addAttribute("isCurrent", true);
    			model.addAttribute("examBatchCode", examBatchCode);
    			model.addAttribute("examBatch", examBatch);
    			
    			// 查询“未预约科目”、“已预约科目”、“未预约考点”和“已预约考点”
    			Map<String, Object> map = new HashMap<String, Object>();
    			map.putAll(searchParams);
    			map.remove("EQ_pointStatus");
    			
    			map.put("EQ_appointmentStatus", "0");
    			model.addAttribute("appointmentStatus0", gjtExamAppointmentService.findCurrentAppointmentList(classInfo.getClassId(), examBatchCode, map, pageRequst).getTotalElements());

    			map.put("EQ_appointmentStatus", "1");
    			model.addAttribute("appointmentStatus1", gjtExamAppointmentService.findCurrentAppointmentList(classInfo.getClassId(), examBatchCode, map, pageRequst).getTotalElements());
    			
    			map.remove("EQ_appointmentStatus");
    			map.put("EQ_pointStatus", "0");
    			model.addAttribute("pointStatus0", gjtExamAppointmentService.findCurrentAppointmentList(classInfo.getClassId(), examBatchCode, map, pageRequst).getTotalElements());

    			map.put("EQ_pointStatus", "1");
    			model.addAttribute("pointStatus1", gjtExamAppointmentService.findCurrentAppointmentList(classInfo.getClassId(), examBatchCode, map, pageRequst).getTotalElements());
            } else {
    			//查询往期计划
    			Page<Map<String, Object>> pageInfo = gjtExamAppointmentService.findHistoryAppointmentList(classInfo.getClassId(), examBatchCode, searchParams, pageRequst);
    			model.addAttribute("pageInfo", pageInfo);
    			model.addAttribute("isCurrent", false);
                model.addAttribute("examBatchCode", examBatchCode);
    			model.addAttribute("examBatch", gjtExamBatchNewService.findByExamBatchCode(examBatchCode));
    			
    			// 查询“未预约科目”和“已预约科目”
    			Map<String, Object> map = new HashMap<String, Object>();
    			map.putAll(searchParams);
    			
    			map.put("EQ_appointmentStatus", "0");
    			model.addAttribute("appointmentStatus0", gjtExamAppointmentService.findHistoryAppointmentList(classInfo.getClassId(), examBatchCode, map, pageRequst).getTotalElements());
    			
    			map.put("EQ_appointmentStatus", "1");
    			model.addAttribute("appointmentStatus1", gjtExamAppointmentService.findHistoryAppointmentList(classInfo.getClassId(), examBatchCode, map, pageRequst).getTotalElements());
    		}
        }

		model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(orgId));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(orgId));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(orgId));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());

		return "new/class/exam/exam_appointment_list";
	}
	
	@RequestMapping(value = "view")
	public String view(String studentId, String examBatchCode, boolean isCurrent, Model model,
			HttpServletRequest request) {
		List<Map<String, Object>> appointmentList;
		if (isCurrent) {
			appointmentList = gjtExamAppointmentService.findCurrentStudentAppointment(studentId, examBatchCode);

			List<Map<String, Object>> pointAppointment = gjtExamAppointmentService.findStudentPointAppointment(studentId, examBatchCode);
			if (pointAppointment != null && pointAppointment.size() > 0) {
				model.addAttribute("point", pointAppointment.get(0));
			}
		} else {
			appointmentList = gjtExamAppointmentService.findHistoryStudentAppointment(studentId, examBatchCode);
		}
		
		int canAppointment = 0;
		int hasAppointment = 0;
		if (appointmentList != null && appointmentList.size() > 0) {
			canAppointment = appointmentList.size();
			
			Map<Integer, List<Map<String, Object>>> appointmentMap = new LinkedHashMap<Integer, List<Map<String,Object>>>();
			for (Map<String, Object> appointment : appointmentList) {
				int term = ((BigDecimal)appointment.get("term")).intValue();
				if (appointmentMap.get(term) != null) {
					List<Map<String, Object>> list = appointmentMap.get(term);
					list.add(appointment);
				} else {
					List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
					list.add(appointment);
					appointmentMap.put(term, list);
				}
				
				if (appointment.get("courseId2") != null) {
					hasAppointment ++;
				}
				
			}
			
			model.addAttribute("appointmentMap", appointmentMap);
		}

		model.addAttribute("isCurrent", isCurrent);
		model.addAttribute("canAppointment", canAppointment);
		model.addAttribute("hasAppointment", hasAppointment);
		model.addAttribute("studentInfo", gjtStudentService.queryById(studentId));
		
		return "new/class/exam/exam_appointment_view";
	}




	/**
	 * 导出预约考试数据
	 *
	 * @param examBatchCode
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/export/plan", method = RequestMethod.POST)
	@ResponseBody
	public void exportPlanAppoinments(@RequestParam String examBatchCode, HttpServletRequest request,
									  HttpServletResponse response) throws Exception {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);

		BzrGjtEmployeeInfo employeeInfo = (BzrGjtEmployeeInfo) request.getSession().getAttribute(Servlets.SESSION_EMPLOYEE_NAME);
		BzrGjtClassInfo classInfo = super.getCurrentClass(request.getSession());
		String orgId = "-1";
		if ("1".equals(employeeInfo.getGjtOrg().getOrgType())) {
			orgId = employeeInfo.getGjtOrg().getId();
		} else {
			orgId = commonMapService.getParentWithType(employeeInfo.getGjtOrg().getId(), "1");
		}
		BzrGjtExamBatchNew examBatch = gjtExamBatchNewService.findLastExamBatch(orgId);
		Page<Map<String, Object>> page = gjtExamAppointmentService.findCurrentAppointmentList(classInfo.getClassId(), examBatchCode, searchParams, pageRequst);


		HSSFWorkbook workbook = gjtExamAppointmentService.exportAppointPlan(page.getContent(),examBatch);

		response.setContentType("application/x-msdownload;charset=utf-8");
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String("考试预约数据.xls".getBytes("UTF-8"), "ISO8859-1"));
		workbook.write(response.getOutputStream());
	}
}
