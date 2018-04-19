/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.classManager.edu;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.constants.OrgUtil;
import com.gzedu.xlims.pojo.flow.GjtFlowRecord;
import com.gzedu.xlims.service.flow.GjtFlowService;
import com.ouchgzee.headTeacher.dto.StudentSignupInfoDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentEnteringSchool;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.service.account.BzrGjtUserAccountService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

/**
 * 学员信息控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月24日
 * @version 2.5
 */
@Controller
@RequestMapping("/home/class/studentInfo")
public class StudentInfoController extends BaseController {

	public static final Logger log = LoggerFactory.getLogger(StudentInfoController.class);

	@Autowired
	private BzrGjtStudentService gjtStudentInfoService;

	@Autowired
	private GjtFlowService gjtFlowService;

	@Autowired
	private BzrGjtUserAccountService gjtUserAccountService;

	@Autowired
	private BzrCommonMapService commonMapService;

	/**
	 * 学员信息列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request,
			HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		String classId = StringUtils.isNotEmpty(request.getParameter(Servlets.REQUEST_PARAM_CLASSID))
				? request.getParameter(Servlets.REQUEST_PARAM_CLASSID) : super.getCurrentClassId(session);
		// Page<GjtStudentInfo> infos =
		// gjtStudentInfoService.queryStudentInfoSpecialtyByClassIdPage(classId,
		// searchParams, pageRequst);
		searchParams.put("EQ_classId", classId);
		searchParams.put("EQ_specialtyId", searchParams.get("EQ_gjtSpecialty.specialtyId"));
		searchParams.put("EQ_studyId", searchParams.get("EQ_gjtStudyCenter.id"));
		searchParams.put("EQ_viewStudentInfo.gradeId",
				searchParams.get("EQ_gjtSignup.gjtEnrollBatch.gjtGrade.gradeId"));
		Page<StudentSignupInfoDto> page = gjtStudentInfoService.queryStudentSignupInfoByPage(searchParams, pageRequst);
		Map<String, BigDecimal> countAuditStateMap = gjtStudentInfoService.countGroupbyAuditState(searchParams);
		Map<String, BigDecimal> countPerfectStatusMap = gjtStudentInfoService.countGroupbyPerfectStatus(searchParams);
		Map<String, BigDecimal> countEnteringSchoolMap = gjtStudentInfoService
				.countGroupbyIsEnteringSchool(searchParams);

		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(employeeInfo.getXxId());// 中心
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(employeeInfo.getXxId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(employeeInfo.getXxId());// 年级
		Map<String, String> pyccMap = commonMapService.getPyccMap(employeeInfo.getXxId());
		Map<String, String> xjzzMap = commonMapService.getRollTypeMap();

		model.addAttribute("infos", page);
		model.addAttribute("countAuditStateMap", countAuditStateMap);
		model.addAttribute("countPerfectStatusMap", countPerfectStatusMap);
		model.addAttribute("countEnteringSchoolMap", countEnteringSchoolMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("xjzzMap", xjzzMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("specialtyMap", specialtyMap);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "new/class/edu/studentInfo/edu_student_info_list";
	}

	/**
	 * 浏览学员信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, ServletRequest request, HttpSession session) {
		BzrGjtStudentInfo studentInfo = gjtStudentInfoService.queryById(id);

		// 证件资料
		Map signupCopyData = gjtStudentInfoService.getSignupCopyData(id);
		model.addAttribute("signupCopyData", signupCopyData);

		List<GjtFlowRecord> flowRecordList = gjtFlowService.queryFlowRecordByStudentId(id);

		// isUndergraduateCourse 培养层次是否为本科
		boolean isUndergraduateCourse = isUndergraduateCourse(studentInfo.getPycc());

		model.addAttribute("item", studentInfo);
		model.addAttribute("flowRecordList", flowRecordList);
		model.addAttribute("isUndergraduateCourse", isUndergraduateCourse);
		boolean isOffsite = OrgUtil.isOffsite(studentInfo.getGjtSchoolInfo().getGjtOrg().getCode(),
				studentInfo.getSfzh());
		model.addAttribute("isOffsite", isOffsite ? 1 : 0); // 是否是异地学员(非广东) 1-是
															// 0-否
		model.addAttribute("action", "view");
		return "new/class/edu/studentInfo/edu_student_info_form";
	}

	/**
	 * 加载学员信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, HttpServletRequest request, Model model) {
		BzrGjtStudentInfo studentInfo = gjtStudentInfoService.queryById(id);

		// 证件资料
		Map signupCopyData = gjtStudentInfoService.getSignupCopyData(id);
		model.addAttribute("signupCopyData", signupCopyData);

		if (StringUtils.isNotBlank(studentInfo.getGjtSignup().getUpdatedBy())) {
			BzrGjtUserAccount auditOper = gjtUserAccountService.queryById(studentInfo.getGjtSignup().getUpdatedBy());
			model.addAttribute("auditOper", auditOper);
		}

		model.addAttribute("info", studentInfo);
		model.addAttribute("action", "update");
		return "new/class/edu/studentInfo/edu_student_info_form";
	}

	/**
	 * 更新基本信息
	 * 
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "updateSignupBaseInfo", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updateSignupBaseInfo(@Valid @ModelAttribute("info") BzrGjtStudentInfo info, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");

		BzrGjtStudentInfo modifyInfo = gjtStudentInfoService.queryById(info.getStudentId());
		// 头像未更新，则啥都不做
		if (StringUtils.isNotBlank(info.getAvatar())) {
			modifyInfo.setAvatar(info.getAvatar());
		}

		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		modifyInfo.setUpdatedBy(employeeInfo.getGjtUserAccount().getId());
		try {
			gjtStudentInfoService.update(modifyInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		return feedback;
	}

	/**
	 * 更新通讯信息
	 * 
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "updateSignupTx", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updateSignupTx(@Valid @ModelAttribute("info") BzrGjtStudentInfo info, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");

		BzrGjtStudentInfo modifyInfo = gjtStudentInfoService.queryById(info.getStudentId());
		modifyInfo.setSjh(info.getSjh());
		modifyInfo.setLxdh(info.getLxdh());
		modifyInfo.setDzxx(info.getDzxx());
		modifyInfo.setProvince(info.getProvince());
		modifyInfo.setCity(info.getCity());
		modifyInfo.setArea(info.getArea());
		modifyInfo.setTxdz(info.getTxdz());
		modifyInfo.setYzbm(info.getYzbm());
		modifyInfo.setScName(info.getScName());
		modifyInfo.setScPhone(info.getScPhone());
		modifyInfo.setScCo(info.getScCo());
		modifyInfo.setScCoAddr(info.getScCoAddr());
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		modifyInfo.setUpdatedBy(employeeInfo.getGjtUserAccount().getId());
		try {
			gjtStudentInfoService.update(modifyInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		return feedback;
	}

	/**
	 * 更新原最高学历信息
	 * 
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "updateSignupYxl", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updateSignupYxl(@Valid @ModelAttribute("info") BzrGjtStudentInfo info, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");

		BzrGjtStudentInfo modifyInfo = gjtStudentInfoService.queryById(info.getStudentId());
		modifyInfo.setExedulevel(info.getExedulevel());
		modifyInfo.setExschool(info.getExschool());
		modifyInfo.setExgraduatedtime(info.getExgraduatedtime());
		modifyInfo.setExsubject(info.getExsubject());
		modifyInfo.setExsubjectkind(info.getExsubjectkind());
		modifyInfo.setExedubaktype(info.getExedubaktype());
		modifyInfo.setExedumajor(info.getExedumajor());
		modifyInfo.setExcertificatenum(info.getExcertificatenum());
		modifyInfo.setExcertificateprove(info.getExcertificateprove());
		modifyInfo.setExcertificateprovenum(info.getExcertificateprovenum());
		modifyInfo.setExeduname(info.getExeduname());
		modifyInfo.setExedunum(info.getExedunum());
		modifyInfo.setIsgraduatebytv(info.getIsgraduatebytv());
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		modifyInfo.setUpdatedBy(employeeInfo.getGjtUserAccount().getId());
		try {
			gjtStudentInfoService.update(modifyInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		return feedback;
	}

	/**
	 * 完善证件资料<br>
	 * 
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "updateSignupCopyData", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updateSignupCopyData(@RequestParam String studentId, ServletRequest request,
			ServletResponse response, HttpSession session) {
		Feedback feedback = new Feedback(true, "操作成功");
		try {
			Map<String, String> copyData = new HashMap<String, String>();

			Map<String, String[]> paramMap = request.getParameterMap();
			for (Map.Entry<String, String[]> param : paramMap.entrySet()) {
				if (!"studentId".equals(param.getKey()) && StringUtils.isNotBlank(param.getValue()[0]))
					copyData.put(param.getKey(), param.getValue()[0]);
			}
			boolean flag = gjtStudentInfoService.updateSignupCopyData(studentId, copyData);
			Assert.isTrue(flag);
		} catch (Exception e) {
			feedback = new Feedback(false, "操作失败");
		}
		return feedback;
	}

	/**
	 * 学员入学确认
	 * 
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "toEnteringSchool", method = RequestMethod.GET)
	public String toEnteringSchool(@RequestParam String studentId, Model model) {
		BzrGjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);
		BzrGjtStudentEnteringSchool studentEnteringSchool = gjtStudentInfoService
				.queryStudentEnteringSchoolByStudentId(studentId);

		model.addAttribute("info", studentInfo);
		model.addAttribute("studentEnteringSchool", studentEnteringSchool);
		return "new/class/edu/studentInfo/edu_student_entering_school";
	}

	/**
	 * 学员入学确认
	 * 
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "enteringSchool", method = RequestMethod.POST)
	@ResponseBody
	public Feedback enteringSchool(@RequestParam String studentId, BzrGjtStudentEnteringSchool info,
			ServletRequest request, ServletResponse response, HttpSession session) {
		Feedback feedback = new Feedback(true, "操作成功");
		try {
			String enteringDtParam = request.getParameter("enteringDtParam");
			info.setEnteringDt(StringUtils.isNotBlank(enteringDtParam)
					? DateFormatUtils.ISO_DATE_FORMAT.parse(enteringDtParam) : null);

			BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
			boolean flag = gjtStudentInfoService.updateStudentEnteringSchool(info, employeeInfo.getEmployeeId());
			Assert.isTrue(flag);
		} catch (Exception e) {
			feedback = new Feedback(false, "操作失败");
		}
		return feedback;
	}

	/**
	 * 资料审批
	 * 
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "signupSubmit", method = RequestMethod.POST)
	@ResponseBody
	public Feedback signupSubmit(@RequestParam String studentId, ServletRequest request, ServletResponse response,
			HttpSession session) {
		Feedback feedback = new Feedback(true, "操作成功");
		try {
			BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
			boolean flag = gjtStudentInfoService.signupSibmit(studentId, employeeInfo.getEmployeeId());
			Assert.isTrue(flag);
		} catch (Exception e) {
			feedback = new Feedback(false, "操作失败");
		}
		return feedback;
	}

	/**
	 * 导出学员信息功能
	 */
	@RequestMapping(value = "exportInfo", method = RequestMethod.GET)
	public void exportInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		String outputUrl = "学员信息_" + Calendar.getInstance().getTimeInMillis() + ".xls";

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		String classId = StringUtils.isNotEmpty(request.getParameter(Servlets.REQUEST_PARAM_CLASSID))
				? request.getParameter(Servlets.REQUEST_PARAM_CLASSID) : super.getCurrentClassId(session);
		searchParams.put("EQ_classId", classId);
		searchParams.put("EQ_specialtyId", searchParams.get("EQ_gjtSpecialty.specialtyId"));
		searchParams.put("EQ_studyId", searchParams.get("EQ_gjtStudyCenter.id"));
		searchParams.put("EQ_viewStudentInfo.gradeId",
				searchParams.get("EQ_gjtSignup.gjtEnrollBatch.gjtGrade.gradeId"));
		HSSFWorkbook workbook = gjtStudentInfoService.exportStudentInfoSpecialtyToExcel(classId, searchParams, null);

		super.downloadExcelFile(response, workbook, outputUrl);
	}

	@RequestMapping(value = "/analogLogin")
	public String analogLogin(Model model, HttpServletRequest request, HttpSession session) {
		BzrGjtUserAccount user = (BzrGjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		String idCard = ObjectUtils.toString(searchParams.get("id_card"));
		log.info("转换前" + idCard + "---------->" + getEncoding(idCard));
		// try {
		// idCard = new String(idCard.getBytes("GB2312"), "UTF-8");
		// log.info("转换后" + idCard + "---------->" + getEncoding(idCard));
		// } catch (Exception e) {
		// log.info("报错了");
		// log.error(e.getMessage(), e);
		// }
		try {

			Map resultMap = new HashMap();
			resultMap.put("synUrl", ObjectUtils.toString(searchParams.get("synUrl"), ""));
			resultMap.put("id_card", idCard);
			resultMap.put("phone", ObjectUtils.toString(searchParams.get("phone"), ""));
			resultMap.put("account", ObjectUtils.toString(searchParams.get("account"), ""));
			resultMap.put("organ", ObjectUtils.toString(searchParams.get("organ"), ""));

			String base64 = idCard + "|" + (System.currentTimeMillis()) / 1000 + "|"
					+ Md5Util.encodeLower(idCard + "oucnet", "UTF-8");

			String base64Code = Base64.encodeBase64String(base64.getBytes("UTF-8"));

			resultMap.put("code", base64Code);
			log.info("单点到应用平台接口:base64={},所有参数：{}", base64, resultMap);
			model.addAttribute("pageMap", resultMap);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "new/class/edu/studentInfo/edu_student_info_analogLogin";

	}

	public static String getEncoding(String str) {
		String encode = "GB2312";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s = encode;
				return s;
			}
		} catch (Exception exception) {
		}
		encode = "ISO-8859-1";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s1 = encode;
				return s1;
			}
		} catch (Exception exception1) {
		}
		encode = "UTF-8";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s2 = encode;
				return s2;
			}
		} catch (Exception exception2) {
		}
		encode = "GBK";
		try {
			if (str.equals(new String(str.getBytes(encode), encode))) {
				String s3 = encode;
				return s3;
			}
		} catch (Exception exception3) {
		}
		return "";
	}
}
