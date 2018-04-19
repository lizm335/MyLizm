package com.ouchgzee.headTeacher.web.controller.exam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ouchgzee.headTeacher.web.controller.base.BaseController;
import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.service.exam.GjtExamStudentRoomNewService;
import com.ouchgzee.headTeacher.pojo.BzrGjtClassInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.exam.BzrGjtExamBatchNew;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.service.exam.BzrGjtExamAppointmentService;
import com.ouchgzee.headTeacher.service.exam.BzrGjtExamBatchNewService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import com.ouchgzee.headTeacher.web.common.Servlets;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/home/class/exam/studentroom")
public class GjtExamStudentRoomController extends BaseController {

	@Autowired
	private BzrGjtExamBatchNewService gjtExamBatchNewService;

	@Autowired
	private BzrGjtExamAppointmentService gjtExamAppointmentService;

	@Autowired
	private BzrGjtStudentService gjtStudentService;
	
	@Autowired
	private BzrCommonMapService commonMapService;
	
	@Autowired
	private GjtExamStudentRoomNewService gjtExamStudentRoomNewService;
	

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
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		
		// 默认选择当前期(批次)
		if(EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))){
			String code = commonMapService.getCurrentGjtExamBatchNew(orgId);
			searchParams.put("EXAM_BATCH_CODE", code);
			model.addAttribute("EXAM_BATCH_CODE",code);
		}else {
			if ("all".equals(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))) {
				searchParams.put("EXAM_BATCH_CODE", "");
			}
			model.addAttribute("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}
		
		searchParams.put("CLASS_ID", ObjectUtils.toString(classInfo.getClassId()));
		searchParams.put("XX_ID", orgId);
		
		Page pageInfo = gjtExamStudentRoomNewService.getExamStudentRoomList(searchParams, pageRequst);
        model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(orgId));
		
		return "new/class/exam/exam_student_room";
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
