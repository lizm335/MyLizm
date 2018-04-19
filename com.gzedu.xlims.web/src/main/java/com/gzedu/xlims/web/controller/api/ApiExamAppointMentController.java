package com.gzedu.xlims.web.controller.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.service.exam.GjtExamAppointmentNewService;

@Controller
@RequestMapping("/api/exam/new/appointment")
public class ApiExamAppointMentController {

	private static final Log log = LogFactory.getLog(ApiExamAppointMentController.class);
	
	@Autowired
	private GjtExamAppointmentNewService gjtExamAppointmentNewService;
	
	/**
	 * 兼容旧版个人中心预约的接口, 需要根据考试计划id去查课程号, 再通过课程号查考试科目, 因此使用旧版个人中心预约的话就必须限制一个课程只有一个考试科目.
	 * 如果是新版个人中心, 直接传考试科目id 过来, 就只可以直接找到考试计划了
	 * 
	 * @param studentid
	 * @param courseid		
	 * @param op 			0-取消预约;1-预约
	 * @return
	 */
	@RequestMapping(value = "plan", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> appointPlan(@RequestParam String studentid, @RequestParam String teachPlanid, 
			@RequestParam int op, HttpServletRequest request) {
		log.info("uri: /api/exam/new/appointment/plan, studenid: "+studentid+", teachPlanid: "+teachPlanid);
		String bukao = request.getParameter("bukao");//是否补考,0-非补考;1-补考. 该字段为预留字段, 暂未用到
		Map<String, Object> resultMap = gjtExamAppointmentNewService.appointExamPlan(studentid, teachPlanid, op);
		return resultMap;
	}
	
	/**
	 * 3.0 版本接收预约请求接口
	 * @param studentid
	 * @param subjectCode
	 * @param op 			0-取消预约;1-预约
	 * @return
	 */
	@RequestMapping(value = "planNew", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> appointPlanNew(@RequestParam String studentid, @RequestParam String subjectCode, 
			@RequestParam int op, HttpServletRequest request) {
		log.info("uri: /api/exam/new/appointment/planNew, studenid: "+studentid+", subjectCode: "+subjectCode);
		String bukao = request.getParameter("bukao");//是否补考,0-非补考;1-补考. 该字段为预留字段, 暂未用到
		Map<String, Object> resultMap = gjtExamAppointmentNewService.appointExamPlanNew(studentid, subjectCode, op);
		return resultMap;
	}
	
	
	/**
	 * 兼容旧版个人中心的考点预约. 通过考点名称做匹配
	 * @param studentid		学员id
	 * @param pointName		考点名字
	 * @param xxid			所属学院
	 * @param op			0-取消预约;others-预约;
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "point", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> appointPoint(@RequestParam String studentid, @RequestParam String pointName, 
			@RequestParam String xxid, @RequestParam int op, HttpServletRequest request) {
		log.info("uri: /api/exam/new/appointment/point, studenid: "+studentid+", pointName: "+pointName+", xxid: "+xxid+", op: "+op);
		Map<String, Object> resultMap = gjtExamAppointmentNewService.appointExamPoint(studentid, pointName, xxid, op);
		return resultMap;
	}
	
	@RequestMapping(value = "pointNew", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> appointPointNew(@RequestParam String studentid, @RequestParam String pointid, 
			@RequestParam String xxid, @RequestParam int op, @RequestParam String studyyearid, HttpServletRequest request) {
		log.info("uri: /api/exam/new/appointment/point, studenid: "+studentid+", pointid: "+pointid+", xxid: "+xxid+", op: "+op);
		Map<String, Object> resultMap = gjtExamAppointmentNewService.appointExamPointNew(studentid, pointid, xxid, op, studyyearid);
		return resultMap;
	}
}
