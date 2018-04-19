/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.signup;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.FileKit;
import com.gzedu.xlims.common.GsonUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.constants.OrgUtil;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.dto.StudentSignupInfoDto;
import com.gzedu.xlims.pojo.flow.GjtFlowRecord;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.flow.GjtFlowService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.signup.GjtSignupService;
import com.gzedu.xlims.service.signup.SignupDataAddService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 学籍资料控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年09月20日
 * @version 2.5
 */
@Controller
@RequestMapping("/edumanage/schoolRollInfo")
public class SchoolRollInfoController extends BaseController {

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtSignupService gjtSignupService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtFlowService gjtFlowService;

	@Autowired
	private SignupDataAddService signupDataAddService;

	@Autowired
	private CommonMapService commonMapService;

	/**
	 * 学籍资料列表
	 * @param pageNumber
	 * @param pageSize
     * @return
     */
	@RequestMapping(value = "list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		String xxId = searchParams.get("EQ_gjtSchoolInfo.id") != null ? searchParams.get("EQ_gjtSchoolInfo.id").toString() : null;
		if(StringUtils.isBlank(xxId)) {
			xxId = user.getGjtOrg().getId();
			searchParams.put("EQ_gjtSchoolInfo.id", xxId);
			model.addAttribute("defaultXxId", xxId);
		}
		Object gradeId = searchParams.get("EQ_viewStudentInfo.gradeId");
		if(gradeId == null) {
			gradeId = gjtGradeService.getCurrentGradeId(xxId);
			searchParams.put("EQ_viewStudentInfo.gradeId", gradeId);
			model.addAttribute("defaultGradeId", gradeId);
		}
//		Page<GjtStudentInfo> page = gjtStudentInfoService.querySource(searchParams, pageRequst);
		Page<StudentSignupInfoDto> page = gjtStudentInfoService.queryStudentSignupInfoByPage(searchParams, pageRequst);
		Object signupAuditState = searchParams.remove("EQ_signupAuditState");
		Object perfectStatus = searchParams.remove("EQ_perfectStatus");
		Map<String, BigDecimal> countAuditStateMap = gjtStudentInfoService.countGroupbyAuditState(searchParams);
		Map<String, BigDecimal> countPerfectStatusMap = gjtStudentInfoService.countGroupbyPerfectStatus(searchParams);
		searchParams.put("EQ_signupAuditState", signupAuditState);
		searchParams.put("EQ_perfectStatus", perfectStatus);

		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId()); // 学校
		// Map<String, String> orgMap = commonMapService.getOrgMap(user.getId()); // 机构
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 学期
		Map<String, String> pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());

		model.addAttribute("pageInfo", page);
		model.addAttribute("countAuditStateMap", countAuditStateMap);
		model.addAttribute("countPerfectStatusMap", countPerfectStatusMap);
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		// model.addAttribute("orgMap", orgMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("specialtyMap", specialtyMap);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "edumanage/schoolRollInfo/school_roll_info_list";
	}

	/**
	 * 查看学籍资料详情
	 * @param id
     * @return
     */
	@RequiresPermissions("/edumanage/schoolRollInfo/list$view")
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(id);

		// 证件资料
		Map<String, String> signupCopyData = gjtSignupService.getSignupCopyData(id);
		model.addAttribute("signupCopyData", signupCopyData);

		List<GjtFlowRecord> flowRecordList = gjtFlowService.queryFlowRecordByStudentId(id);

		// isUndergraduateCourse 培养层次是否为本科
		boolean isUndergraduateCourse = isUndergraduateCourse(studentInfo.getPycc());

		model.addAttribute("item", studentInfo);
		model.addAttribute("flowRecordList", flowRecordList);
		model.addAttribute("isUndergraduateCourse", isUndergraduateCourse);
		boolean isOffsite = OrgUtil.isOffsite(studentInfo.getGjtSchoolInfo().getGjtOrg().getCode(), studentInfo.getSfzh());
		model.addAttribute("isOffsite", isOffsite ? 1 : 0); // 是否是异地学员(非广东) 1-是 0-否
		model.addAttribute("action", request.getParameter("action") != null ? request.getParameter("action") : "view");
		return "edumanage/schoolRollInfo/school_roll_info_form";
	}

	/**
	 * 资料审核，审核可以更改证件资料
	 * @param studentId
	 * @param auditState
	 * @param auditContent
     * @return
     */
	@SysLog("学籍资料-资料审核")
	@RequiresPermissions("/edumanage/schoolRollInfo/list$approval")
	@RequestMapping(value = "audit", method = RequestMethod.POST)
	public String audit(@RequestParam("studentId") String studentId,
						 @RequestParam("auditState") String auditState,
						 String auditContent,
						 HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "审核成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		try {
			gjtStudentInfoService.updateEntityAndFlushCache(gjtStudentInfoService.queryById(studentId));
			
			// 更新各个选项
			String signupSfzType = request.getParameter("signupSfzType");
			String zgxlRadioType = request.getParameter("zgxlRadioType");
			String signupByzType = request.getParameter("signupByzType");
			String signupJzzType = request.getParameter("signupJzzType");
			if (StringUtils.isNotBlank(signupSfzType) || StringUtils.isNotBlank(zgxlRadioType) || StringUtils.isNotBlank(signupByzType) || StringUtils.isNotBlank(signupJzzType)) {
				gjtSignupService.updateEveryType(studentId, signupSfzType, zgxlRadioType, signupByzType, signupJzzType);
			}

			final Map<String, String> signupCopyData = new HashMap<String, String>();
			signupCopyData.put("zp", request.getParameter("zp"));
			signupCopyData.put("sfz-z", request.getParameter("sfzz"));
			signupCopyData.put("sfz-f", request.getParameter("sfzf"));
			signupCopyData.put("jzz", request.getParameter("jzz")); // 异地学员(非广东)，需提供居住证或工牌
			signupCopyData.put("jzzf", request.getParameter("jzzf")); // 居住证反
			signupCopyData.put("ygzm", request.getParameter("ygzm")); // 工牌
			signupCopyData.put("byz-z", request.getParameter("byzz"));
			signupCopyData.put("xlz", request.getParameter("xlz"));
			signupCopyData.put("dzzch", request.getParameter("dzzch")); // 毕业电子注册号证明
			signupCopyData.put("xsz", request.getParameter("xsz")); // 国家开放大学或广州电大学生证原件
			signupCopyData.put("cjd", request.getParameter("cjd")); // 成绩单
			signupCopyData.put("lqmc", request.getParameter("lqmc")); // 录取名册或入学通知书
			signupCopyData.put("yjbyszm", request.getParameter("yjbyszm")); // 应届毕业生证明
			signupCopyData.put("ykcns", request.getParameter("ykcns")); // 预科生承诺书
			signupCopyData.put("xlzm", request.getParameter("xlzm")); // 学历证明
			signupCopyData.put("cns", request.getParameter("cns")); // 专科承诺书
			gjtSignupService.updateSignupCopyData(studentId, signupCopyData);

			boolean flag = gjtFlowService.auditSignupData(studentId, auditState, auditContent, user.getId(), user.getRealName());
			if(!flag) {
				feedback = new Feedback(false, "审核失败");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "审核失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/schoolRollInfo/view/"+studentId+"?action=audit";
	}

	/**
	 * 导出学籍审核环节数据
	 */
	@SysLog("学籍资料-导出学籍审核环节数据")
	@RequiresPermissions("/edumanage/schoolRollInfo/list$exportAuditState")
	@RequestMapping(value = "exportAuditState")
	public void exportAuditState(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String xxId = searchParams.get("EQ_gjtSchoolInfo.id") != null ? searchParams.get("EQ_gjtSchoolInfo.id").toString() : null;
		if(StringUtils.isBlank(xxId)) {
			xxId = user.getGjtOrg().getId();
			searchParams.put("EQ_gjtSchoolInfo.id", xxId);
		}
		Object gradeId = searchParams.get("EQ_viewStudentInfo.gradeId");
		if(gradeId == null) {
			gradeId = gjtGradeService.getCurrentGradeId(xxId);
			searchParams.put("EQ_viewStudentInfo.gradeId", gradeId);
		}
		String path = request.getSession().getServletContext().getRealPath("")
				+ WebConstants.EXCEL_DOWNLOAD_URL + "roll" + File.separator;
		String outFile = gjtStudentInfoService.exportAuditState(searchParams, null, path);

		super.downloadFile(request, response, path + outFile);
		FileKit.delFile(path + outFile);
	}

	/**
	 * 账号同步至运营平台
	 * @param studentId
	 * @return
	 */
	@SysLog("学籍资料-账号同步至运营平台")
	@RequiresPermissions("/edumanage/schoolRollInfo/list$sync")
	@RequestMapping(value = "/syncYunYingCenter", method = RequestMethod.POST)
	@ResponseBody
	public Feedback syncYunYingCenter(@RequestParam("studentId") String studentId,
						 HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "同步成功");
		try {
			Map<String, Object> resultSync = signupDataAddService.syncYunYingCenter(studentId);
			boolean syncFlag = (Boolean) resultSync.get("successful");
			if(syncFlag) {

			} else {
				String errorMessage = (String) resultSync.get("message");
				feedback = new Feedback(false, errorMessage);
				feedback.setType(resultSync.get("type")+"");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "服务器异常");
		}
		return feedback;
	}

	/**
	 * 同步EE号，且学员加入群
	 * @param studentId
	 * @return
	 */
	@SysLog("学籍资料-同步EE号，且学员加入群")
	@RequiresPermissions("/edumanage/schoolRollInfo/list$sync")
	@RequestMapping(value = "/syncEENo", method = RequestMethod.POST)
	@ResponseBody
	public Feedback syncEENo(@RequestParam("studentId") String studentId,
									  HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "同步成功");
		try {
			// 生成学员EE号
			boolean isCreateEENo = signupDataAddService.createEENo(studentId);
			if(isCreateEENo) {
				// 生成EE群，且学员加入群
				signupDataAddService.createGroupNoAndAddSingleStudent(studentId);
			} else {
				feedback = new Feedback(false, "同步失败");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "服务器异常");
		}
		return feedback;
	}

	/**
	 * 修改学籍资料-加载学籍资料
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "update/{studentId}", method = RequestMethod.GET)
	public String update(@PathVariable("studentId") String studentId,
						 HttpServletRequest request, Model model) {
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);
		if(studentInfo != null) {
			// 证件资料
			Map<String, String> signupCopyData = gjtSignupService.getSignupCopyData(studentId);
			model.addAttribute("signupCopyData", signupCopyData);

			List<GjtFlowRecord> flowRecordList = gjtFlowService.queryFlowRecordByStudentId(studentId);

			model.addAttribute("item", studentInfo);
			model.addAttribute("flowRecordList", flowRecordList);
			model.addAttribute("dicPoliticsstatuList", CacheService.Dictionary.dicPoliticsstatuList); // 政治面貌字典
			model.addAttribute("dicExedulevelList", CacheService.Dictionary.dicExedulevelList); // 原学历层次字典
			model.addAttribute("dicExsubjectList", CacheService.Dictionary.dicExsubjectList); // 原学科字典
			model.addAttribute("dicExedubaktypeList", CacheService.Dictionary.dicExedubaktypeList); // 原学历学习类型字典
			model.addAttribute("dicExcertificateproveList", CacheService.Dictionary.dicExcertificateproveList); // 原学历证明材料字典
			model.addAttribute("dicExeduCertificateList", CacheService.Dictionary.dicExeduCertificateList); // 原学历证件类型字典
			model.addAttribute("dicIsgraduatebytvList", CacheService.Dictionary.dicIsgraduatebytvList); // 是否电大毕业字典
			model.addAttribute("dicExsubjectListJson", GsonUtils.toJson(CacheService.Dictionary.dicExsubjectList)); // 原学科字典JSON

			// isUndergraduateCourse 培养层次是否为本科
			boolean isUndergraduateCourse = gjtStudentInfoService.isUndergraduateCourse(studentInfo.getPycc());
			model.addAttribute("isUndergraduateCourse", isUndergraduateCourse);
			boolean isOffsite = OrgUtil.isOffsite(studentInfo.getGjtSchoolInfo().getGjtOrg().getCode(), studentInfo.getSfzh());
			model.addAttribute("isOffsite", isOffsite ? 1 : 0); // 是否是异地学员(非广东) 1-是 0-否
		} else {
			model.addAttribute("feedback", new com.gzedu.xlims.webservice.common.Feedback(false, "学员不存在"));
		}
		model.addAttribute("action", "update");
		return "edumanage/schoolRollInfo/school_roll_info_form_update";
	}

	/**
	 * 修改学籍资料-更新学员学籍资料及证件照资料
	 * @param studentId
	 * @return
	 */
	@SysLog("学籍资料-修改学籍资料")
	@RequiresPermissions("/personal/index$admin")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String updateForm(@RequestParam("studentId") String studentId, GjtStudentInfo entity,
							 HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "修改成功");
		// 1.修改学员学籍资料
		GjtStudentInfo modifyInfo = gjtStudentInfoService.queryById(studentId);
//		if(!Constants.BOOLEAN_1.equals(modifyInfo.getGjtSignup().getAuditState())) {
			try {
				// 强制修改的学籍信息
				if (StringUtils.isNotEmpty(entity.getXm())) modifyInfo.setXm(entity.getXm());
				if (StringUtils.isNotEmpty(entity.getXh())) modifyInfo.setXh(entity.getXh());
				if (StringUtils.isNotEmpty(entity.getXbm())) modifyInfo.setXbm(entity.getXbm());
				if (StringUtils.isNotEmpty(entity.getCertificatetype())) modifyInfo.setCertificatetype(entity.getCertificatetype());
				if (StringUtils.isNotEmpty(entity.getSfzh())) modifyInfo.setSfzh(entity.getSfzh());
				// ./end 强制修改的学籍信息
	
				if (StringUtils.isNotEmpty(entity.getNativeplace())) modifyInfo.setNativeplace(entity.getNativeplace());
				if (StringUtils.isNotEmpty(entity.getMzm())) modifyInfo.setMzm(entity.getMzm());
				if (StringUtils.isNotEmpty(entity.getPoliticsstatus()))
					modifyInfo.setPoliticsstatus(entity.getPoliticsstatus());
				if (StringUtils.isNotEmpty(entity.getHyzkm())) modifyInfo.setHyzkm(entity.getHyzkm());
				if (StringUtils.isNotEmpty(entity.getHkxzm())) modifyInfo.setHkxzm(entity.getHkxzm());
				if (StringUtils.isNotEmpty(entity.getCsrq())) modifyInfo.setCsrq(entity.getCsrq());
				if (StringUtils.isNotEmpty(entity.getIsonjob())) modifyInfo.setIsonjob(entity.getIsonjob());
				if (StringUtils.isNotEmpty(entity.getHkszd())) modifyInfo.setHkszd(entity.getHkszd());
				if (StringUtils.isNotEmpty(entity.getSjh())) modifyInfo.setSjh(entity.getSjh());
				if (StringUtils.isNotEmpty(entity.getLxdh())) modifyInfo.setLxdh(entity.getLxdh());
				if (StringUtils.isNotEmpty(entity.getDzxx())) modifyInfo.setDzxx(entity.getDzxx());
				if (StringUtils.isNotEmpty(entity.getProvince())) modifyInfo.setProvince(entity.getProvince());
				if (StringUtils.isNotEmpty(entity.getCity())) modifyInfo.setCity(entity.getCity());
				if (StringUtils.isNotEmpty(entity.getArea())) modifyInfo.setArea(entity.getArea());
				if (StringUtils.isNotEmpty(entity.getTxdz())) modifyInfo.setTxdz(entity.getTxdz().trim());
				if (StringUtils.isNotEmpty(entity.getYzbm())) modifyInfo.setYzbm(entity.getYzbm());
				if (StringUtils.isNotEmpty(entity.getScCo())) modifyInfo.setScCo(entity.getScCo());
				if (StringUtils.isNotEmpty(entity.getScCoAddr())) modifyInfo.setScCoAddr(entity.getScCoAddr());
				String jobPost = request.getParameter("jobPost");
				if (StringUtils.isNotEmpty(jobPost)) modifyInfo.getGjtSignup().setJobPost(jobPost);
				if (StringUtils.isNotEmpty(entity.getExedulevel())) modifyInfo.setExedulevel(entity.getExedulevel());
				if (StringUtils.isNotEmpty(entity.getExschool())) modifyInfo.setExschool(entity.getExschool());
				if (StringUtils.isNotEmpty(entity.getExgraduatedtime()))
					modifyInfo.setExgraduatedtime(entity.getExgraduatedtime());
				if (StringUtils.isNotEmpty(entity.getExsubject())) modifyInfo.setExsubject(entity.getExsubject());
				if (StringUtils.isNotEmpty(entity.getExsubjectkind()))
					modifyInfo.setExsubjectkind(entity.getExsubjectkind());
				if (StringUtils.isNotEmpty(entity.getExedubaktype())) modifyInfo.setExedubaktype(entity.getExedubaktype());
				if (StringUtils.isNotEmpty(entity.getExedumajor())) modifyInfo.setExedumajor(entity.getExedumajor());
				if (StringUtils.isNotEmpty(entity.getExcertificatenum()))
					modifyInfo.setExcertificatenum(entity.getExcertificatenum());
				if (StringUtils.isNotEmpty(entity.getExcertificateprove()))
					modifyInfo.setExcertificateprove(entity.getExcertificateprove());
				if (StringUtils.isNotEmpty(entity.getExcertificateprovenum()))
					modifyInfo.setExcertificateprovenum(entity.getExcertificateprovenum());
				if (StringUtils.isNotEmpty(entity.getExeduname())) modifyInfo.setExeduname(entity.getExeduname());
				if (StringUtils.isNotEmpty(entity.getExedunum())) modifyInfo.setExedunum(entity.getExedunum());
				if (StringUtils.isNotEmpty(entity.getIsgraduatebytv()))
					modifyInfo.setIsgraduatebytv(entity.getIsgraduatebytv());
				String auditState = request.getParameter("auditState");
				if (StringUtils.isNotEmpty(auditState))
					modifyInfo.getGjtSignup().setAuditState(auditState);  	// 修改审核状态
				if (StringUtils.isNotEmpty(entity.getUpdatedBy()))
					modifyInfo.setUpdatedBy(entity.getUpdatedBy());   		// 记录修改描述
				gjtStudentInfoService.updateEntityAndFlushCache(modifyInfo);
	
				// 2.修改证件照资料
				// 更新各个选项
				String signupSfzType = request.getParameter("signupSfzType");
				String zgxlRadioType = request.getParameter("zgxlRadioType");
				String signupByzType = request.getParameter("signupByzType");
				String signupJzzType = request.getParameter("signupJzzType");
				if (StringUtils.isNotBlank(signupSfzType) || StringUtils.isNotBlank(zgxlRadioType) || StringUtils.isNotBlank(signupByzType) || StringUtils.isNotBlank(signupJzzType)) {
					gjtSignupService.updateEveryType(studentId, signupSfzType, zgxlRadioType, signupByzType, signupJzzType);
				}
	
				final Map<String, String> signupCopyData = new HashMap<String, String>();
				signupCopyData.put("zp", request.getParameter("zp"));
				signupCopyData.put("sfz-z", request.getParameter("sfzz"));
				signupCopyData.put("sfz-f", request.getParameter("sfzf"));
				signupCopyData.put("jzz", request.getParameter("jzz")); // 异地学员(非广东)，需提供居住证或工牌
				signupCopyData.put("jzzf", request.getParameter("jzzf")); // 居住证反
				signupCopyData.put("ygzm", request.getParameter("ygzm")); // 工牌
				signupCopyData.put("byz-z", request.getParameter("byzz"));
				signupCopyData.put("xlz", request.getParameter("xlz"));
				signupCopyData.put("dzzch", request.getParameter("dzzch")); // 毕业电子注册号证明
				signupCopyData.put("xsz", request.getParameter("xsz")); // 国家开放大学或广州电大学生证原件
				signupCopyData.put("cjd", request.getParameter("cjd")); // 成绩单
				signupCopyData.put("lqmc", request.getParameter("lqmc")); // 录取名册或入学通知书
				signupCopyData.put("yjbyszm", request.getParameter("yjbyszm")); // 应届毕业生证明
				signupCopyData.put("ykcns", request.getParameter("ykcns")); // 预科生承诺书
	
				signupCopyData.put("xlzm", request.getParameter("xlzm")); // 学历证明
				signupCopyData.put("cns", request.getParameter("cns")); // 专科承诺书
				gjtSignupService.updateSignupCopyData(studentId, signupCopyData);
			} catch (Exception e) {
				e.printStackTrace();
				feedback = new Feedback(false, "修改失败");
			}
//		} else {
//			feedback = new Feedback(false, "学员的学籍资料已审核通过，无法再修改!");
//		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/schoolRollInfo/update/"+studentId;
	}

}
