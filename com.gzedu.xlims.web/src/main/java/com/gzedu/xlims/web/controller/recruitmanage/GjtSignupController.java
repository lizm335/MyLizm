/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.recruitmanage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.ExcelUtil;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.flow.GjtFlowRecord;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.flow.GjtFlowService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.signup.GjtSignUpInfoDataService;
import com.gzedu.xlims.service.signup.GjtSignupService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;
import com.gzedu.xlims.web.controller.recruitmanage.vo.SignupInfoVo;

/**
 * 功能说明：
 * 
 * @author 卢林林
 * @Date 2016年11月29日
 * @version 2.0
 *
 */
@Controller
@RequestMapping("/recruitmanage/signup")
public class GjtSignupController extends BaseController {

	@Autowired
	GjtSignupService gjtSignupService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	private GjtFlowService gjtFlowService;

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	GjtSignUpInfoDataService gjtSignUpInfoDataService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;
	
	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtOrgService gjtOrgService;

	/*@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_isDeleted", "N");

		Page<GjtSignup> lists = gjtSignupService.queryPageList(user.getGjtOrg().getId(), searchParams, pageRequst);
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		long totalNum = gjtSignupService.queryAuditStateTotalNum(user.getGjtOrg().getId(), "", searchParams);// 总数量
		long StaySubmitNum = gjtSignupService.queryAuditStateTotalNum(user.getGjtOrg().getId(), "4", searchParams);// 状态为【待提交】的数量
		long StayCheckNum = gjtSignupService.queryAuditStateTotalNum(user.getGjtOrg().getId(), "3", searchParams);// 状态为【待审核】的数量
		long AuditStatePassNum = gjtSignupService.queryAuditStateTotalNum(user.getGjtOrg().getId(), "1", searchParams);// 状态为【审核通过】的数量
		long AuditStateNoPassNum = gjtSignupService.queryAuditStateTotalNum(user.getGjtOrg().getId(), "0",
				searchParams);// 状态为【审核不通过】的数量
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId());// 院校下的学习中心
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("infos", lists);
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("StaySubmitNum", StaySubmitNum);
		model.addAttribute("StayCheckNum", StayCheckNum);
		model.addAttribute("AuditStatePassNum", AuditStatePassNum);
		model.addAttribute("AuditStateNoPassNum", AuditStateNoPassNum);

		return "recruitmanage/signup/signup_list";
	}*/

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "list",method = RequestMethod.GET)
	public String signUpList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
							 @RequestParam(value = "page.size", defaultValue = "5") int pageSize, Model model,
							 HttpServletRequest request, HttpSession session) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		//院校下学习中心
//		Map schoolInfoMap = commonMapService.getOrgMap(user.getId());
		Map schoolInfoMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());
		//专业
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		//年级-->修改为学期
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		//层次
		Map pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());

		String xxId = searchParams.get("EQ_xxId") != null ? searchParams.get("EQ_xxId").toString() : null;
		if(StringUtils.isBlank(xxId)) {
			xxId = user.getGjtOrg().getId();
			searchParams.put("XX_ID", xxId);
			model.addAttribute("defaultXxId", xxId);
		}

		Object gradeId = searchParams.get("GRADE_ID");
		if(gradeId == null){
			gradeId = gjtGradeService.getCurrentGradeId(xxId);
			searchParams.put("GRADE_ID", gradeId);
			model.addAttribute("defaultGradeId", gradeId);
		}


		//searchParams.put("XX_ID", ObjectUtils.toString(user.getGjtOrg().getId()));
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = gjtSignUpInfoDataService.getSignUpList(searchParams, pageRequst);

		Map countMap = gjtSignUpInfoDataService.countSignupInfo(searchParams);

		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("pageInfo",pageInfo);
		model.addAttribute("COUNT_MAP", countMap);
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, ""));

		return "recruitmanage/signup/signup_listInfo";
	}

	/**
	 * 查看详情
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String viewForm(Model model, HttpServletRequest request) {
		String signupId = request.getParameter("signupId");
		String studentId = request.getParameter("studentId");
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(studentId);

		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		Map<String, String> rollTypeMap = commonMapService.getRollTypeMap();// 学籍状态

		// 证件资料
		Map signupCopyData = gjtSignupService.getSignupCopyData(studentId);
		model.addAttribute("signupCopyData", signupCopyData);

		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_studentId", studentId);
		List<GjtFlowRecord> flowRecordList = gjtFlowService.queryBy(searchParams, new Sort("createdDt"));

		// isUndergraduateCourse 培养层次是否为本科
		boolean isUndergraduateCourse = isUndergraduateCourse(gjtStudentInfo.getPycc());

		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("rollTypeMap", rollTypeMap);
		model.addAttribute("item", gjtStudentInfo);
		model.addAttribute("flowRecordList", flowRecordList);
		model.addAttribute("isUndergraduateCourse", isUndergraduateCourse);
		model.addAttribute("action", request.getParameter("action") != null ? request.getParameter("action") : "view");

		return "recruitmanage/signup/from";
	}

	/**
	 * 批量下载学生报读资料
	 * 
	 * @throws IOException
	 */
	@RequestMapping(value = "exportInfo", method = RequestMethod.GET)
	public void exportInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_isDeleted", "N");
		String choiceAuditState = request.getParameter("search_EQ_auditState");
		if (StringUtils.isBlank(choiceAuditState)) {
			searchParams.put("EQ_auditState", "");
		}
		String outputUrl = "学生报读资料_" + Calendar.getInstance().getTimeInMillis() + ".xls";
		HSSFWorkbook workbook = gjtSignupService.exportInfo(user.getGjtOrg().getId(), searchParams, null);

		super.downloadExcelFile(request, response, workbook, outputUrl);
	}

	//============================================= 报读统计 =============================================//

	/**
	 * 报读统计
     * @return
     */
	@RequestMapping(value = "statistics", method = RequestMethod.GET)
	public String querySignupStatistics(HttpServletRequest request, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		Object gradeId = searchParams.get("gradeId");
		if(gradeId == null) {
			String xxId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(user.getGjtOrg().getId());
			gradeId = gjtGradeService.getCurrentGradeId(xxId);
			searchParams.put("gradeId", gradeId);
			model.addAttribute("defaultGradeId", gradeId);
		}

		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		Map<String, String> pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());// 培养层次
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		return "recruitmanage/signup/signup_statistical_list";
	}

	/**
	 * 根据条件统计学员报读资料统计
	 * 
	 * @return
	 */
	@RequestMapping(value = "searchSignupNum", method = RequestMethod.GET)
	@ResponseBody
	public Feedback searchSignupNum(Model model, HttpServletRequest request, HttpSession session) {
		Feedback feedback = new Feedback(true, "成功");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, null);
		searchParams.put("gradeId", request.getParameter("gradeId"));
		searchParams.put("pycc", request.getParameter("pycc"));
		searchParams.put("signupSpecialtyId", request.getParameter("signupSpecialtyId"));
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<Map> infos = gjtSignupService.querySignupNums(user.getGjtOrg().getId(), searchParams);
		List<SignupInfoVo> vos = new ArrayList<SignupInfoVo>();
		List<SignupInfoVo> sigunpInfo = new ArrayList<SignupInfoVo>();
		vos.add(new SignupInfoVo("0", "审核不通过", 0));
		vos.add(new SignupInfoVo("1", "审核通过", 0));
		vos.add(new SignupInfoVo("2", "重新提交", 0));
		vos.add(new SignupInfoVo("3", "待审核", 0));
		vos.add(new SignupInfoVo("4", "未提交", 0));
		for (SignupInfoVo vo : vos) {
			for (int i = 0; i < infos.size(); i++) {
				Map map = infos.get(i);
				if (vo.getId().toString().equals(map.get("CODE").toString())) {
					vo.setNum(Integer.parseInt(map.get("VALUE").toString()));
				}
			}
			sigunpInfo.add(vo);
		}
		feedback.setObj(sigunpInfo);
		return feedback;
	}

	/**
	 * 根据条件统计学员报读缴费统计
	 * 
	 * @return
	 */
	@RequestMapping(value = "searchSignupPayCostNum", method = RequestMethod.GET)
	@ResponseBody
	public Feedback searchSignupPayCostNum(Model model, HttpServletRequest request, HttpSession session) {
		Feedback feedback = new Feedback(true, "成功");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, null);
		searchParams.put("gradeId", request.getParameter("gradeId"));
		searchParams.put("pycc", request.getParameter("pycc"));
		searchParams.put("signupSpecialtyId", request.getParameter("signupSpecialtyId"));
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<Map> infos = gjtSignupService.querySignupPayCostNum(user.getGjtOrg().getId(), searchParams);
		List<SignupInfoVo> vos = new ArrayList<SignupInfoVo>();
		List<SignupInfoVo> sigunpPayCost = new ArrayList<SignupInfoVo>();
		vos.add(new SignupInfoVo("0", "未缴费", 0));
		vos.add(new SignupInfoVo("1", "已缴费", 0));
		for (SignupInfoVo vo : vos) {
			for (int i = 0; i < infos.size(); i++) {
				Map map = infos.get(i);
				if (vo.getId().toString().equals(map.get("CODE").toString())) {
					vo.setNum(Integer.parseInt(map.get("VALUE").toString()));
				}
			}
			sigunpPayCost.add(vo);
		}
		feedback.setObj(sigunpPayCost);
		return feedback;
	}

	/**
	 * 资料审核
	 * 
	 * @param studentId
	 * @param auditState
	 * @param auditContent
	 * @return
	 */
	@RequestMapping(value = "audit", method = RequestMethod.POST)
	public String update(@RequestParam("studentId") String studentId, @RequestParam("auditState") String auditState,
			String auditContent, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "审核成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			boolean flag = gjtFlowService.auditSignupData(studentId, auditState, auditContent, user.getId(),
					user.getRealName());
			if (!flag) {
				feedback = new Feedback(false, "审核失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			feedback = new Feedback(false, "审核失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/schoolRollInfo/view/" + studentId + "?action=audit";
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	@RequestMapping(value = "querySignUpDetail",method = RequestMethod.GET)
	public String querySignUpDetail(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		Map resultMap = gjtSignUpInfoDataService.querySignUpDetail(searchParams);
		
		model.addAttribute("info", resultMap);
		
		return "recruitmanage/signup/signup_listInfoDetail";
	}
	
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "exportView",method = RequestMethod.GET)
	public String exportData(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		//院校下学习中心
//		Map schoolInfoMap = commonMapService.getOrgMap(user.getId());
		Map schoolInfoMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());
		//专业
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		//年级
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		//层次
		Map pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());

		//学籍状态
		Map xjztMap = commonMapService.getXjzt();

		//缴费状态
		Map chargeMap = commonMapService.getChargeMap();

		//审核状态
		Map auditMap = commonMapService.getAuditMap();
		
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("xjztMap", xjztMap);
		model.addAttribute("chargeMap", chargeMap);
		model.addAttribute("auditMap", auditMap);
		return "recruitmanage/signup/signup_downLoad";
	}

	@SysLog("招生管理-报读信息-导出报名统计表")
	@SuppressWarnings("unused")
	@RequestMapping(value="/exportSignUpList",method = RequestMethod.GET)
	public void exportSignUpList(HttpServletRequest request,HttpServletResponse response,HttpSession session)throws IOException{
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		String excelFileName = gjtSignUpInfoDataService.exportSignUpData(searchParams);
		ExcelUtil.exportExcel(excelFileName, request, response);
		
	}

	/**
	 * 用户属性统计
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/searchSignUserAge",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> searchSignUserAge(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		Map resultMap = gjtSignUpInfoDataService.userAttributeCount(searchParams);
		
		return resultMap;
	}

	/**
	 * 学历层次统计
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/userPyccCount",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> userPyccCount(Model model,HttpServletRequest request,HttpSession session) throws Exception{
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		Map resultMap = gjtSignUpInfoDataService.userPyccCount(searchParams);
		
		return resultMap;
	}

	/**
	 * 报读专业统计
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/userCountBySpecial",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> userCountBySpecial(Model model,HttpServletRequest request,HttpSession session) throws Exception{
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		Map resultMap = gjtSignUpInfoDataService.userCountBySpecial(searchParams);
		
		return resultMap;
	}

	/**
	 * 报读资料统计
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/queryEnrolmentCount",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryEnrolmentCount(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		Map resultMap = gjtSignUpInfoDataService.queryEnrolmentCount(searchParams);
		
		return resultMap;
	}

	/**
	 * 报读缴费统计
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/queryPaymentCount",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryPaymentCount(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		Map resultMap = gjtSignUpInfoDataService.queryPaymentCount(searchParams);
		
		return resultMap;
	}

	/**
	 * 学习中心统计
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value="/queryStudyCenter",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryStudyCenter(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		Map resultMap = gjtSignUpInfoDataService.queryStudyCenter(searchParams);
		
		return resultMap;
		
	}

	/**
	 * 区域统计
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryAreaCount")
	@ResponseBody
	public Map<String,Object> queryAreaCount(Model model,HttpServletRequest request,HttpSession session){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,"");
		searchParams.put("XX_ID",user.getGjtOrg().getId());
		Map resultMap = gjtSignUpInfoDataService.queryAreaCount(searchParams);
		return resultMap;
	}


	@RequestMapping(value = "studentRollSituation", method = RequestMethod.GET)
	@ResponseBody
	public Feedback clockSituation(Model model, HttpServletRequest request, ServletResponse response, HttpSession session) {
		Feedback feedback = new Feedback(true, "成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
			if("1".equals(user.getGjtOrg().getOrgType())) {
				searchParams.put("XX_ID", user.getGjtOrg().getId());
			} else {
				searchParams.put("EQ_studyId", user.getGjtOrg().getId());
			}
			// 学籍状态统计
			Map studentRollSituation = gjtSignUpInfoDataService.countStudentRollSituationBy(searchParams);

			Map result = new HashMap();
			result.put("studentRollSituation", studentRollSituation);
			feedback.setObj(result);
		} catch (Exception e) {
			feedback = new Feedback(false, "服务器异常");
		}
		return feedback;
	}
	
}
