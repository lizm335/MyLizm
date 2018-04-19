/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.organization;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtStudyCenterAudit;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtStudyCenterAuditService;
import com.gzedu.xlims.service.organization.GjtStudyCenterService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 
 * 功能说明：学习中心
 * 
 * @author 卢林林 lulinlin@eenet.com
 * @Date 2016年5月23日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/organization/studyCenter")
public class StudyCenterController {

	private final static Logger log = LoggerFactory.getLogger(StudyCenterController.class);

	@Autowired
	GjtStudyCenterService gjtStudyCenterService;

	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtStudyCenterAuditService gjtStudyCenterAuditService;

	@Autowired
	GjtOrgService gjtOrgService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, String all) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		// String userId =
		// request.getSession().getAttribute("userId").toString();
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("orgId", user.getGjtOrg().getId());

		Page<Map> pageInfo = gjtStudyCenterService.queryAllOrg(searchParams, pageRequst);

		// 未启用
		searchParams.put("EQ_auditStatus", "");// 清空
		searchParams.put("EQ_isEnabled", "0");
		long noIsEnabled = gjtStudyCenterService.queryOrgCount(searchParams);

		// 已启用
		searchParams.put("EQ_isEnabled", "1");
		long yesIsEnabled = gjtStudyCenterService.queryOrgCount(searchParams);

		// 待审核
		searchParams.put("EQ_isEnabled", "");// 清空
		searchParams.put("EQ_auditStatus", "0");
		long waitAuditStatus = gjtStudyCenterService.queryOrgCount(searchParams);
		// 审核不通过
		searchParams.put("EQ_auditStatus", "2");
		long noAuditStatus = gjtStudyCenterService.queryOrgCount(searchParams);

		// 全部
		searchParams.put("EQ_auditStatus", "");// 清空
		long allcount = gjtStudyCenterService.queryOrgCount(searchParams);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("noIsEnabled", noIsEnabled);
		model.addAttribute("yesIsEnabled", yesIsEnabled);
		model.addAttribute("waitAuditStatus", waitAuditStatus);
		model.addAttribute("noAuditStatus", noAuditStatus);
		model.addAttribute("allcount", allcount);

		return "organization/studyCenter/list";
	}

	/**
	 * 跳转至新增页面
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "toCreate", method = RequestMethod.GET)
	public String toCreate(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		if ("0".equals(user.getGrantType())) {
			model.addAttribute("gjtSchoolInfo", new GjtOrg());
			model.addAttribute("studyList", new ArrayList<GjtOrg>());
			model.addAttribute("flag", "超级管理员不支持创建！");
		} else {
			// 该学习中心或者招生点所属的院校
			GjtOrg gjtSchoolInfo = gjtOrgService.queryParentBySonId(user.getGjtOrg().getId());
			model.addAttribute("gjtSchoolInfo", gjtSchoolInfo);
			// 该招生点所属的学习中心
			List<GjtOrg> studyList = gjtOrgService.queryStudyBySchoolId(gjtSchoolInfo.getId());// 查询所有的学习中心
			model.addAttribute("studyList", studyList);
		}
		model.addAttribute("action", "create");
		return "organization/studyCenter/form";
	}

	/**
	 * 新增保存
	 * 
	 * @param entity
	 * @param model
	 * @param redirectAttributes
	 * @param request
	 * @return
	 */
	@SysLog("学习中心新增")
	@RequestMapping(value = "create")
	public String create(@ModelAttribute GjtStudyCenter entity, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request, String orgType, String suoShuXxId, String suoShuXxzxId) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "新增成功");
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
			entity.setServiceArea(serviceArea.toString());
			entity.setCenterType(orgType);
			boolean insertStudyCenter = gjtStudyCenterService.insertStudyCenter(user, entity, suoShuXxId, suoShuXxzxId);
			if (!insertStudyCenter) {
				feedback = new Feedback(false, "新增失败");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "新增失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/organization/studyCenter/list";
	}

	/**
	 * 查看详情
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request, String schoolName) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		// GjtSchoolInfo
		// gjtSchoolInfo=gjtStudyCenterService.queryByOrgId(user.getGjtOrg().getId());
		GjtStudyCenter gjtStudyCenter = gjtStudyCenterService.queryById(id);
		String serviceArea = gjtStudyCenter.getServiceArea();
		if (StringUtils.isNotBlank(serviceArea)) {
			Map<String, Object> map = new HashMap<String, Object>();
			String[] strings = serviceArea.split(",");
			for (String str : strings) {
				map.put(str, true);
			}
			model.addAttribute("serviceAreas", map);
		}

		List<GjtStudyCenterAudit> list = gjtStudyCenterAuditService.queryByIncidentId(id);

		boolean bool1 = user.getPriRoleInfo().getRoleId().equals("d4b27a66c0a87b010120da231915c223");// 院长
		boolean bool2 = user.getPriRoleInfo().getRoleId().equals("9a6f05b3e24d456fb84435dd75e934c2");// 学支管理员

		model.addAttribute("isXueZhi", bool2);
		model.addAttribute("isYuanZhang", bool1);
		model.addAttribute("list", list);
		model.addAttribute("entity", gjtStudyCenter);
		model.addAttribute("schoolName", schoolName);
		return "organization/studyCenter/view";
	}

	/**
	 * 跳转至编辑页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		GjtStudyCenter gjtStudyCenter = gjtStudyCenterService.queryById(id);

		String serviceArea = gjtStudyCenter.getServiceArea();
		if (StringUtils.isNotBlank(serviceArea)) {
			Map<String, Object> map = new HashMap<String, Object>();
			String[] strings = serviceArea.split(",");
			for (String str : strings) {
				map.put(str, true);
			}
			model.addAttribute("serviceAreas", map);
		}

		// 该学习中心或者招生点所属的院校
		GjtOrg gjtSchoolInfo = gjtOrgService.queryParentBySonId(id);
		model.addAttribute("gjtSchoolInfo", gjtSchoolInfo);

		// 该招生点所属的学习中心
		List<GjtOrg> studyList = gjtOrgService.queryStudyBySchoolId(gjtSchoolInfo.getId());// 查询所有的学习中心
		model.addAttribute("studyList", studyList);

		model.addAttribute("entity", gjtStudyCenter);
		model.addAttribute("action", "update");
		return "organization/studyCenter/form";
	}

	/**
	 * 编辑后保存
	 * 
	 * @param entity
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@SysLog("学习中心修改")
	@RequestMapping(value = "update")
	public String update(@ModelAttribute GjtStudyCenter entity, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request, String orgType, String suoShuXxId, String suoShuXxzxId) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String[] values = request.getParameterValues("serviceAreas");
		StringBuffer serviceArea = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			serviceArea.append(values[i]);
			if (values.length - 1 > i) {
				serviceArea.append(",");
			}
		}
		try {
			entity.setCenterType(orgType);
			entity.setServiceArea(serviceArea.toString());
			boolean update = gjtStudyCenterService.update(user, entity, suoShuXxId, suoShuXxzxId);
			if (!update) {
				feedback = new Feedback(true, "更新失败");
			}
		} catch (Exception e) {
			feedback = new Feedback(true, "更新失败");
			log.error(e.getMessage(), e);
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/organization/studyCenter/list";
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@SysLog("学习中心删除")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, ServletResponse response) throws IOException {
		Feedback fb = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(ids)) {
			try {
				// gjtStudyCenterService.delete(Arrays.asList(ids.split(",")));
				gjtStudyCenterService.delete(ids);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				fb = new Feedback(false, "删除失败，原因:" + e.getMessage());
			}
		}
		return fb;
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@SysLog("学习中心删除")
	@ResponseBody
	@RequestMapping(value = "deleteCenter/{id}", method = RequestMethod.POST)
	public Feedback deleteCenter(@PathVariable("id") String id) throws IOException {
		Feedback fb = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(id)) {
			try {
				gjtStudyCenterService.delete(id);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				fb = new Feedback(false, "删除失败" + e.getMessage());
			}
		}
		return fb;
	}

	/**
	 * 检查学习中心编码是否存在
	 * 
	 * @param scCode
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "checkScCode")
	@ResponseBody
	public Feedback checkScCode(String scCode) throws IOException {
		PageRequest pageRequst = new PageRequest(0, 2);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_code", scCode);
		Page<GjtOrg> gjtOrg = gjtOrgService.queryAll(searchParams, pageRequst);
		Boolean boolean1 = true;
		if (gjtOrg.getTotalElements() == 0) {
			boolean1 = false;
		}
		Feedback fe = new Feedback(boolean1, "");
		fe.setSuccessful(boolean1);
		return fe;
	}

	/**
	 * 启用停用
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@SysLog("学习中心启用停用")
	@ResponseBody
	@RequestMapping(value = "changeStatus/{id}/{status}", method = RequestMethod.POST)
	public Feedback changeStatus(@PathVariable("id") String id, @PathVariable("status") String status) {

		boolean result = gjtOrgService.updateStatus(id, status);
		Feedback feedback = new Feedback();
		if (result) {
			feedback.setSuccessful(true);
		} else {
			feedback.setSuccessful(false);
		}
		return feedback;
	}

	@SysLog("学习中心审核")
	@ResponseBody
	@RequestMapping(value = "audit", method = RequestMethod.POST)
	public Feedback audit(String id, String audit, String content, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "审核成功！");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			boolean bool = user.getPriRoleInfo().getRoleName().equals("院长");
			String step = bool == true ? "3" : "2";// 审核进度
			if ("2".equals(audit)) {// 审核不通过，打回给招生平台，调用招生平台接口，目前没有
				step = "0";
			}
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
				gjtStudyCenterService.updateAudit(id, audit);// 其实这个没用了
				gjtOrgService.updateStatus(id, "0");// 启用学习中心
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "发生异常！");
			log.error(e.getMessage(), e);
		}
		return feedback;
	}

}
