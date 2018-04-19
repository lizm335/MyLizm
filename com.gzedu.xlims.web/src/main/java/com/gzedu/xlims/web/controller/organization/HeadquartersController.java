/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.organization;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.validation.Valid;

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

import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.status.OrgTypeEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSetOrgCopyrightService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 
 * 功能说明：机构管理-总部管理
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月30日
 * @version 3.0
 *
 */
@Controller
@RequestMapping("/organization/headquarters")
public class HeadquartersController {

	private static final Logger log = LoggerFactory.getLogger(HeadquartersController.class);

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtSetOrgCopyrightService gjtSetOrgCopyrightService;

	@Autowired
	CommonMapService commonMapService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "6") int pageSize, Model model, ServletRequest request) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, "createdDt", "DESC");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_isDeleted", "N");
		searchParams.put("EQ_orgType", OrgTypeEnum.HEADQUARTERS.getNum());
		Page<GjtOrg> pageInfo = gjtOrgService.queryAll(searchParams, pageRequst);
		for (GjtOrg gjtOrg : pageInfo) {
			Map<String, Object> searchMap = new HashMap<String, Object>();
			searchMap.put("orgType", OrgTypeEnum.BRANCH.getNum());
			searchMap.put("id", gjtOrg.getId());
			Long branchCount = gjtOrgService.queryOrgCount(searchMap);// 分部总数
			gjtOrg.setBranchCount(branchCount);

			searchMap.put("orgType", OrgTypeEnum.BRANCHSCHOOL.getNum());
			Long branchSchoolCount = gjtOrgService.queryOrgCount(searchMap);// 分校总数
			gjtOrg.setBranchSchoolCount(branchSchoolCount);

			searchMap.put("orgType", OrgTypeEnum.STUDYCENTER.getNum());
			long studyCentenCount = gjtOrgService.queryOrgCount(searchMap);// 学习中心总数
			gjtOrg.setStudyCentenCount(studyCentenCount);

			searchMap.put("orgType", OrgTypeEnum.STUDYCENTER.getNum());
			searchMap.put("belongsTo", OrgTypeEnum.BRANCH.getNum());// 直属与分部的学习中心
			long branchStudyCentenCount = gjtOrgService.queryOrgCount(searchMap);
			gjtOrg.setBranchStudyCentenCount(branchStudyCentenCount);

		}

		model.addAttribute("pageInfo", pageInfo);

		return "organization/headquarters/headquarters_list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("entity", gjtOrgService.queryById(id));
		model.addAttribute("action", "view");
		return "/organization/headquarters/headquarters_form";
	}

	@RequestMapping(value = "toCreate", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("entity", new GjtOrg());
		model.addAttribute("action", "create");
		return "/organization/headquarters/headquarters_form";
	}

	@SysLog("总部管理-新增")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtOrg entity, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			entity.setOrgType(OrgTypeEnum.HEADQUARTERS.getNum());
			gjtOrgService.saveSchool(entity);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/organization/headquarters/list";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("entity", gjtOrgService.queryById(id));
		model.addAttribute("action", "update");
		return "/organization/headquarters/headquarters_form";
	}

	@SysLog("总部管理-修改")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity") GjtOrg entity, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");

		GjtOrg modifyInfo = gjtOrgService.queryById(entity.getId()); // 院校编码
		GjtSchoolInfo schoolInfo = modifyInfo.getGjtSchoolInfo();
		if (schoolInfo == null) {
			modifyInfo.setGjtSchoolInfo(new GjtSchoolInfo(entity.getId()));
		}
		modifyInfo.setCode(entity.getCode()); // 院校名称
		modifyInfo.setOrgName(entity.getOrgName());
		modifyInfo.getGjtSchoolInfo().setXxmc(entity.getOrgName()); // 联系电话
		modifyInfo.getGjtSchoolInfo().setLinkTel(entity.getGjtSchoolInfo().getLinkTel()); // 联系人
		modifyInfo.getGjtSchoolInfo().setLinkMan(entity.getGjtSchoolInfo().getLinkMan()); // 电子邮箱
		modifyInfo.getGjtSchoolInfo().setLinkMail(entity.getGjtSchoolInfo().getLinkMail()); // 院校地址
		modifyInfo.getGjtSchoolInfo().setXxdz(entity.getGjtSchoolInfo().getXxdz()); // 描述
		modifyInfo.getGjtSchoolInfo().setMemo(entity.getGjtSchoolInfo().getMemo()); // APPId
		modifyInfo.getGjtSchoolInfo().setAppid(entity.getCode());// 确认过用院校代码作appid
		modifyInfo.getGjtSchoolInfo().setXxqhm(entity.getGjtSchoolInfo().getXxqhm());

		try {
			gjtOrgService.update(modifyInfo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/organization/headquarters/list";
	}

	@SysLog("总部管理-删除")
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

	/**
	 * 删除总校
	 * 
	 * @param id
	 * @return
	 */
	@SysLog("总部管理-删除")
	@ResponseBody
	@RequestMapping(value = "deleteOrg/{id}", method = RequestMethod.POST)
	public Feedback deleteOrg(@PathVariable("id") String id) {
		Feedback fb = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(id)) {
			try {
				gjtOrgService.delete(Arrays.asList(id.split(",")));
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

}
