/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.organization;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.SignUtil;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtStudyCenterAudit;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.OrgTypeEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.dictionary.GjtDistrictService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSetOrgCopyrightService;
import com.gzedu.xlims.service.organization.GjtStudyCenterAuditService;
import com.gzedu.xlims.service.organization.GjtStudyCenterService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

import net.sf.json.JSONObject;

/**
 * 
 * 功能说明：机构管理-学习体验中心管理
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月31日
 * @version 3.0
 *
 */
@Controller
@RequestMapping("/organization/enrollStudent")
public class EnrollStudentController {

	private static final Logger log = LoggerFactory.getLogger(EnrollStudentController.class);

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtSetOrgCopyrightService gjtSetOrgCopyrightService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtDistrictService gjtDistrictService;

	@Autowired
	GjtStudyCenterService gjtStudyCenterService;

	@Autowired
	GjtStudyCenterAuditService gjtStudyCenterAuditService;

	@Value("#{configProperties['api.enroll.domain']}")
	String enrollServer;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "8") int pageSize, Model model,
			HttpServletRequest request) {

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, "createdDt", "DESC");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		searchParams.put("orgId", user.getGjtOrg().getId());
		Map<String, String> studyServiceInfoMap = commonMapService.getDates("StudyServiceInfo");
		Page<Map<String, Object>> pageInfo = gjtOrgService.queryAllEnrollStudent(searchParams, pageRequst);
		for (Map<String, Object> gjtOrg : pageInfo) {
			String serviceArea = (String) gjtOrg.get("SERVICE_AREA");// 拥有的服务项
			List<String> serviceList = new ArrayList<String>();
			if (StringUtils.isNotBlank(serviceArea)) {
				for (String str : serviceArea.split(",")) {
					serviceList.add(studyServiceInfoMap.get(str));
				}
				gjtOrg.put("serviceList", serviceList);
			}
			String address = "";
			String queryAllNameById = gjtDistrictService.queryAllNameById(String.valueOf(gjtOrg.get("DISTRICT")));
			if (StringUtils.isNotBlank(queryAllNameById)) {
				address += queryAllNameById + gjtOrg.get("OFFICE_ADDR");
			}
			gjtOrg.put("officeAddr", address);
		}

		searchParams.put("orgType", OrgTypeEnum.ENROLLSTUDENT.getNum());
		searchParams.remove("EQ_auditStatus");
		searchParams.put("EQ_isEnabled", "1");
		long starCount = gjtOrgService.queryAllStudyCenterCount(searchParams);

		searchParams.put("EQ_isEnabled", "0");
		long stopCount = gjtOrgService.queryAllStudyCenterCount(searchParams);

		searchParams.remove("EQ_isEnabled");
		searchParams.put("EQ_auditStatus", "0");
		long waitAuditStatus = gjtOrgService.queryAllStudyCenterCount(searchParams);

		searchParams.put("EQ_auditStatus", "2");
		long noAuditStatus = gjtOrgService.queryAllStudyCenterCount(searchParams);

		// 全部
		searchParams.remove("EQ_auditStatus");
		long allcount = gjtOrgService.queryAllStudyCenterCount(searchParams);

		model.addAttribute("starCount", starCount);
		model.addAttribute("stopCount", stopCount);
		model.addAttribute("waitAuditStatus", waitAuditStatus);
		model.addAttribute("noAuditStatus", noAuditStatus);
		model.addAttribute("allcount", allcount);
		model.addAttribute("pageInfo", pageInfo);

		return "organization/enrollStudent/enrollStudent_list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtOrg gjtOrg = gjtOrgService.queryById(id);
		String serviceArea = gjtOrg.getGjtStudyCenter().getServiceArea();
		if (StringUtils.isNotBlank(serviceArea)) {
			Map<String, Object> map = new HashMap<String, Object>();
			String[] strings = serviceArea.split(",");
			for (String str : strings) {
				map.put(str, true);
			}
			model.addAttribute("serviceAreas", map);
		}

		String address = gjtDistrictService.queryAllNameById(gjtOrg.getGjtStudyCenter().getDistrict())
				+ gjtOrg.getGjtStudyCenter().getOfficeAddr();
		gjtOrg.getGjtStudyCenter().setOfficeAddr(address);

		List<GjtStudyCenterAudit> list = gjtStudyCenterAuditService.queryByIncidentId(id);

		boolean bool1 = user.getPriRoleInfo().getRoleId().equals("d4b27a66c0a87b010120da231915c223");// 院长
		boolean bool2 = user.getPriRoleInfo().getRoleId().equals("9a6f05b3e24d456fb84435dd75e934c2");// 学支管理员

		Map<String, String> studyServiceInfoMap = commonMapService.getDates("StudyServiceInfo");// 学习中心服务项初始值
		model.addAttribute("studyServiceInfoMap", studyServiceInfoMap);
		model.addAttribute("isXueZhi", bool2);// 判断是谁审核
		model.addAttribute("isYuanZhang", bool1);// 判断是谁审核
		model.addAttribute("list", list);
		model.addAttribute("entity", gjtOrg);
		return "organization/enrollStudent/enrollStudent_view";
	}

	@RequestMapping(value = "toCreate", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<Map<String, String>> listAll = gjtOrgService.queryOrgByOrgType(OrgTypeEnum.STUDYCENTER.getNum(),
				user.getGjtOrg().getId());

		Map<String, String> studyServiceInfoMap = commonMapService.getDates("StudyServiceInfo");
		model.addAttribute("studyServiceInfoMap", studyServiceInfoMap);

		model.addAttribute("listAll", listAll);
		model.addAttribute("entity", new GjtOrg());
		model.addAttribute("action", "create");
		return "/organization/enrollStudent/enrollStudent_form";
	}

	@SysLog("学习体验中心管理-新增")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtOrg entity, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String[] values = request.getParameterValues("serviceAreas");
		StringBuffer serviceArea = new StringBuffer();
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				serviceArea.append(values[i]);
				if (values.length - 1 > i) {
					serviceArea.append(",");
				}
			}
		}
		try {
			entity.setCreatedBy(user.getId());
			entity.getGjtStudyCenter().setServiceArea(serviceArea.toString());
			entity.setOrgType(OrgTypeEnum.ENROLLSTUDENT.getNum());
			gjtOrgService.saveStudyCenter(entity);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/organization/enrollStudent/list";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<Map<String, String>> listAll = gjtOrgService.queryOrgByOrgType(OrgTypeEnum.STUDYCENTER.getNum(),
				user.getGjtOrg().getId());

		GjtOrg gjtOrg = gjtOrgService.queryById(id);
		String serviceArea = gjtOrg.getGjtStudyCenter().getServiceArea();// 拥有的服务项
		if (StringUtils.isNotBlank(serviceArea)) {
			Map<String, Object> map = new HashMap<String, Object>();
			String[] strings = serviceArea.split(",");
			for (String str : strings) {
				map.put(str, true);
			}
			model.addAttribute("serviceAreas", map);
		}

		Map<String, String> studyServiceInfoMap = commonMapService.getDates("StudyServiceInfo");// 学习中心服务项初始值
		model.addAttribute("studyServiceInfoMap", studyServiceInfoMap);

		model.addAttribute("entity", gjtOrg);
		model.addAttribute("action", "update");
		model.addAttribute("listAll", listAll);
		return "/organization/enrollStudent/enrollStudent_form";
	}

	@SysLog("学习体验中心管理-修改")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity") GjtOrg entity, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtOrg modifyInfo = gjtOrgService.queryById(entity.getId()); // 院校编码

		String[] values = request.getParameterValues("serviceAreas");
		StringBuffer serviceArea = new StringBuffer();
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				serviceArea.append(values[i]);
				if (values.length - 1 > i) {
					serviceArea.append(",");
				}
			}
		}

		GjtStudyCenter studyCenter = modifyInfo.getGjtStudyCenter();
		if (studyCenter == null) {
			modifyInfo.setGjtStudyCenter(new GjtStudyCenter(entity.getId()));
		}
		GjtOrg parentGjtOrg = gjtOrgService.queryById(entity.getParentGjtOrg().getId());
		modifyInfo.setParentGjtOrg(parentGjtOrg);
		modifyInfo.setCode(entity.getCode());
		modifyInfo.setOrgName(entity.getOrgName());
		modifyInfo.setUpdatedBy(user.getId());
		modifyInfo.setUpdatedDt(DateUtils.getNowTime());

		modifyInfo.getGjtStudyCenter().setScCode(entity.getCode());
		modifyInfo.getGjtStudyCenter().setScName(entity.getOrgName());
		modifyInfo.getGjtStudyCenter().setCompactNo(entity.getGjtStudyCenter().getCompactNo());
		modifyInfo.getGjtStudyCenter().setDistrict(entity.getGjtStudyCenter().getDistrict());
		modifyInfo.getGjtStudyCenter().setOfficeAddr(entity.getGjtStudyCenter().getOfficeAddr());
		modifyInfo.getGjtStudyCenter().setOfficeTel(entity.getGjtStudyCenter().getOfficeTel());
		modifyInfo.getGjtStudyCenter().setMemo(entity.getGjtStudyCenter().getMemo());
		modifyInfo.getGjtStudyCenter().setServiceArea(serviceArea.toString());
		modifyInfo.getGjtStudyCenter().setUpdatedBy(user.getId());
		modifyInfo.getGjtStudyCenter().setUpdatedDt(DateUtils.getNowTime());

		try {
			gjtOrgService.updateStudyCenter(modifyInfo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/organization/enrollStudent/list";
	}

	@SysLog("学习体验中心管理-删除")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, ServletResponse response) throws IOException {
		Feedback fb = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(ids)) {
			try {
				gjtOrgService.delete(Arrays.asList(ids.split(",")));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				fb = new Feedback(false, "删除失败，原因:" + e.getMessage());
			}
		}
		return fb;
	}

	@SysLog("学习体验中心管理-删除")
	@ResponseBody
	@RequestMapping(value = "deleteOrg/{id}", method = RequestMethod.POST)
	public Feedback deleteOrg(@PathVariable("id") String id) {
		Feedback fb = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(id)) {
			try {
				gjtStudyCenterService.delete(id);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				fb = new Feedback(false, "删除失败 " + e.getMessage());
			}
		}
		return fb;
	}

	@RequestMapping(value = "checkCode", method = { RequestMethod.POST })
	@ResponseBody
	public Feedback checkCode(String code, String oldCode) {
		boolean flage = false;
		if (code.equals(oldCode)) {
			flage = true;
		} else {
			flage = gjtOrgService.queryByCode(code, oldCode);
		}
		Feedback fe = new Feedback(flage, "");
		return fe;
	}

	@SysLog("学习体验中心管理-启用/停用")
	@ResponseBody
	@RequestMapping(value = "changeStatus/{id}/{status}", method = RequestMethod.POST)
	public Feedback changeStatus(@PathVariable("id") String id, @PathVariable("status") String status) {
		boolean result = gjtOrgService.updateStatus(id, status);
		Feedback feedback = new Feedback(result, "");
		return feedback;
	}

	@SysLog("学习体验中心管理-审核")
	@ResponseBody
	@RequestMapping(value = "audit", method = RequestMethod.POST)
	public Feedback audit(String orgCode, String id, String audit, String content, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "审核操作成功！");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		boolean bool = "d4b27a66c0a87b010120da231915c223".equals(user.getPriRoleInfo().getRoleId());// 院长
		GjtOrg gjtOrg = user.getGjtOrg();
		String schoolCode = "";
		if ("1".equals(gjtOrg.getOrgType())) {
			schoolCode = gjtOrg.getCode();
		} else {
			GjtOrg queryParentBySonId = gjtOrgService.queryParentBySonId(gjtOrg.getId());
			schoolCode = queryParentBySonId.getCode();
		}

		String step = bool == true ? "3" : "2";// 审核进度

		try {
			String operatingState;
			if ("2".equals(audit)) {// 审核不通过，
				operatingState = "E";
			} else {
				operatingState = "Y";
			}

			if ("E".equals(operatingState) || bool) {// 审核不通过，或者二审通过才推送到招生平台
				Map<String, String> params = new HashMap<String, String>();
				String url = enrollServer + "/Learncenterapi/auditCenter.html";
				params.put("LEARNCENTER_CODE", orgCode);// 编码
				params.put("OPERATING_STATE", operatingState);// Y通过,E不通过
				params.put("AUDIT_REMARK", content);// 备注
				long time = DateUtils.getDate().getTime();

				// 额外参数;签名
				params.put("school_code", schoolCode);// 院校ID
				params.put("sign", SignUtil.formatUrlMap(params, time));// 字母要大写
				params.put("appid", SignUtil.APPID);// APPID不需要参与加密
				params.put("time", String.valueOf(time));

				log.info("学习中心审核参数：url={},{}", url, params);
				String result = HttpClientUtils.doHttpPost(url, params, 3000, "utf-8");
				log.info("审核反馈结果：{}", result);
				if (StringUtils.isNotEmpty(result)) {
					JSONObject json = JSONObject.fromObject(result);
					int bo = (Integer) json.get("status");
					if (bo == 0) {
						return new Feedback(false, "招生平台接口发生异常！错误信息：" + json.get("msg"));
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Feedback(false, "招生平台接口发生异常！");
		}

		try {
			GjtStudyCenterAudit item = new GjtStudyCenterAudit();
			item.setId(UUIDUtils.random().toString());
			item.setIncidentId(id);
			item.setAuditContent(content);
			item.setAuditDt(DateUtils.getNowTime());
			item.setAuditOperator(user.getRealName());
			item.setAuditOperatorRole(user.getPriRoleInfo().getRoleName());
			item.setAuditState(audit);
			item.setAuditStep(step);
			item.setCreatedBy(user.getId());
			item.setCreatedDt(DateUtils.getNowTime());
			item.setIsDeleted("N");
			item.setVersion(new BigDecimal("1"));
			gjtStudyCenterAuditService.save(item);

			if (bool && "1".equals(audit)) {
				// 审核通过，启用学习中心，并且可以创建帐号
				gjtStudyCenterService.updateAudit(id, audit);
				gjtOrgService.updateStatus(id, "0");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "审核操作失败，发生异常！");
			log.error(e.getMessage(), e);
		}
		return feedback;
	}

}
