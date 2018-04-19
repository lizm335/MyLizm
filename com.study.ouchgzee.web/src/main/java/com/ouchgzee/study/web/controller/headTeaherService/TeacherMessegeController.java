package com.ouchgzee.study.web.controller.headTeaherService;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.pojo.GjtSpecialtyBase;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.service.organization.GjtSpecialtyBaseService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.ouchgzee.study.web.common.BaseController;

/**
 * 功能说明：班主任信息
 * 
 * @author 卢林林 lulinlin@eenet.com
 * @Date 2017年3月2日
 * @version 2.5
 */
@Controller
@RequestMapping("/pcenter/headTeacherService/teacherMessege")
public class TeacherMessegeController extends BaseController {
	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;
	
	@Autowired
	private GjtSpecialtyBaseService gjtSpecialtyBaseService;

	@Value("#{configProperties['eeChatInterface']}")
	String eeServer;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(HttpServletRequest request, HttpSession session) {
		GjtStudentInfo student = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> employeeMap = new HashMap<String, Object>();
		employeeMap = gjtEmployeeInfoService.queryHeadTeacherInfo(student.getStudentId());
		if (employeeMap != null) {
			String employeeId = (String) employeeMap.get("employeeId");
			String eeUrl = "班主任ID不对";
			if (StringUtils.isNotEmpty(employeeId)) {
				eeUrl = eeServer + "/openChat.do?data=" + EncryptUtils
						.encrypt("{\"USER_ID\":\"" + student.getStudentId() + "\",\"TO_ID\":\"" + employeeId + "\"}");// 旧的接口是传这样的参数
			}
			employeeMap.put("eeUrl", eeUrl);
		}
		resultMap.put("info", employeeMap);
		return resultMap;
	}

	@RequestMapping(value = "dutyTeacherInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> dutyTeacherInfo( HttpSession session) {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		gjtEmployeeInfoService.queryHeadTeacherInfo(student.getStudentId());
		GjtSpecialtyBase gjtSpecialtyBase = gjtSpecialtyBaseService.findByXxIdAndSpecialtyId(student.getXxId(), student.getMajor());
		Map<String, Object> result = Maps.newHashMap();
		if (gjtSpecialtyBase != null) {
			result.put("dutyTeacherName", gjtSpecialtyBase.getTeacher());
			result.put("dutyTeacherUrl", gjtSpecialtyBase.getTeacherImgUrl());
			result.put("dutyTeacherDetails", gjtSpecialtyBase.getTeacherDetails());
		}
		return result;
	}
	
}
