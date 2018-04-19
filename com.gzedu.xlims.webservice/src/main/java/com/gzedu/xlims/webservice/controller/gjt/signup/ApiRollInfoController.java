/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.webservice.controller.gjt.signup;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.GsonUtils;
import com.gzedu.xlims.common.ResultFeedback;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.constants.OrgUtil;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.flow.GjtFlowRecord;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.flow.GjtFlowService;
import com.gzedu.xlims.service.signup.GjtSignupService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.webservice.common.Feedback;

/**
 * 学籍资料控制器<br/>
 * 功能说明：招生调用，实现班主任、招生办审核<br/>
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年08月16日
 * @version 2.5
 */
@Controller
@RequestMapping("/api/roll")
public class ApiRollInfoController {

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtSignupService gjtSignupService;

	@Autowired
	private GjtFlowService gjtFlowService;

	/**
	 * 查看学籍资料详情
	 * @param studentId
	 * @param auditOperatorRole
	 * @param auditOperatorRealName 两次encodeURI,第一次编码得到的是UTF-8形式的URL，第二次编码得到的依然是UTF-8形式的URL，但是在效果上相当于首先进行了一 次UTF-8编码(此时已经全部转换为ASCII字符)，再进行了一次iso-8859-1编码，因为对英文字符来说UTF-8编码和ISO- 8859-1编码的效果相同。
	 * @param openMode 打开方式，默认0 0-查看 1-审核
	 * @return
     */
	@RequestMapping(value = "view/{studentId}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("studentId") String studentId,
						   @RequestParam("auditOperatorRole") int auditOperatorRole,
						   @RequestParam("auditOperatorRealName") String auditOperatorRealName,
						   @RequestParam(value = "openMode", defaultValue = "0") String openMode,
						   HttpServletRequest request, Model model) throws CommonException {
		String realName = null;
		try {
			realName = URLDecoder.decode(auditOperatorRealName, Constants.CHARSET);
		} catch (UnsupportedEncodingException e) {

		}
		if(auditOperatorRealName.equals(realName)) { // 防止名字没有按照要求decode两次传过来
			throw new CommonException(MessageCode.BAD_REQUEST, "无效的姓名：" + realName);
		}

		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);
		if(studentInfo != null) {
			// 证件资料
			Map<String, String> signupCopyData = gjtSignupService.getSignupCopyData(studentId);
			model.addAttribute("signupCopyData", signupCopyData);

			List<GjtFlowRecord> flowRecordList = gjtFlowService.queryFlowRecordByStudentId(studentId);

			// isUndergraduateCourse 培养层次是否为本科
			boolean isUndergraduateCourse = gjtStudentInfoService.isUndergraduateCourse(studentInfo.getPycc());

			model.addAttribute("item", studentInfo);
			model.addAttribute("flowRecordList", flowRecordList);
			model.addAttribute("isUndergraduateCourse", isUndergraduateCourse);
			boolean isOffsite = OrgUtil.isOffsite(studentInfo.getGjtSchoolInfo().getGjtOrg().getCode(), studentInfo.getSfzh());
			model.addAttribute("isOffsite", isOffsite ? 1 : 0); // 是否是异地学员(非广东) 1-是 0-否
		} else {
			model.addAttribute("feedback", new Feedback(false, "学员不存在"));
		}
		model.addAttribute("auditOperatorRole", auditOperatorRole);
		model.addAttribute("auditOperatorRealName", realName);
		model.addAttribute("openMode", openMode);
		model.addAttribute("action", Constants.BOOLEAN_1.equals(openMode) ? "audit" : "view");
		return "edumanage/schoolRollInfo/school_roll_info_form_api";
	}

	/**
	 * 资料审核，审核可以更改证件资料
	 * @param studentId
	 * @param auditState
	 * @param auditContent
     * @return
     */
	@SysLog("招生调接口-学籍资料审核")
	@RequestMapping(value = "audit", method = RequestMethod.POST)
	public String audit(@RequestParam("studentId") String studentId,
						 @RequestParam("auditState") String auditState,
						 @RequestParam("auditOperatorRole") int auditOperatorRole,
						 @RequestParam("auditOperatorRealName") String auditOperatorRealName,
						 String auditContent,
						 HttpServletRequest request, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
		Feedback feedback = new Feedback(true, "审核成功");
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

			ResultFeedback result = gjtFlowService.auditSingupInfo(studentId, auditState, auditContent, auditOperatorRole, auditOperatorRealName);
			if(!result.isSuccessful()) {
				feedback = new Feedback(false, result.getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			feedback = new Feedback(false, "审核失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/api/roll/view/"+studentId+"?auditOperatorRole="+auditOperatorRole+"&auditOperatorRealName="+ URLEncoder.encode(URLEncoder.encode(auditOperatorRealName, Constants.CHARSET), Constants.CHARSET);
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

			model.addAttribute("item", studentInfo);
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
			model.addAttribute("feedback", new Feedback(false, "学员不存在"));
		}
		model.addAttribute("action", "update");
		return "edumanage/schoolRollInfo/school_roll_info_form_update_api";
	}

	/**
	 * 修改学籍资料-更新学员学籍资料及证件照资料
	 * @param studentId
	 * @return
	 */
	@SysLog("招生调接口-修改学籍资料")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String updateForm(@RequestParam("studentId") String studentId, GjtStudentInfo entity,
							 HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "修改成功");
		// 1.修改学员学籍资料
		GjtStudentInfo modifyInfo = gjtStudentInfoService.queryById(studentId);
		if(!Constants.BOOLEAN_1.equals(modifyInfo.getGjtSignup().getAuditState()) || "2".equals(modifyInfo.getGjtSignup().getCharge())) {
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
				modifyInfo.setUpdatedBy("招生调接口-修改学籍资料");
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
				String sign = request.getParameter("sign"); // 签名
				signupCopyData.put("sign", sign); // 签名
				gjtSignupService.updateSignupCopyData(studentId, signupCopyData);
				
				// 更新完善资料状态
				gjtStudentInfoService.perfectSignupAndCertificateInfo(studentId);

				// 如果是已缴费则提交资料，收费状态 0-已全额缴费，1-已部分缴费，2-待缴费，3-已欠费
		        if("0".equals(modifyInfo.getGjtSignup().getCharge()) || "1".equals(modifyInfo.getGjtSignup().getCharge())) {
					// 确认提交资料
					gjtFlowService.initAuditSignupInfo(studentId);
				}
			} catch (Exception e) {
				e.printStackTrace();
				feedback = new Feedback(false, "修改失败");
			}
		} else {
			feedback = new Feedback(false, "学员的学籍资料已审核通过，无法再修改!");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/api/roll/update/"+studentId;
	}

}
