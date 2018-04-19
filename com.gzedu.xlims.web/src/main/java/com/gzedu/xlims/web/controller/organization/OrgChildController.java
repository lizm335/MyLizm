/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.organization;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 功能说明：分校管理
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月23日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/organization/orgChild")
public class OrgChildController {

	private static final Log log = LogFactory.getLog(OrgChildController.class);

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	CommonMapService commonMapService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession()
				.getAttribute(WebConstants.CURRENT_USER);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber,pageSize, "createdDt", "DESC");
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_isDeleted","N");
		searchParams.put("EQ_orgType","2");
		
		if (user.getIsSuperMgr()) {
			Page<GjtOrg> pageInfo = gjtOrgService.queryAll(searchParams, pageRequst);
			Map<String, String> orgMap = commonMapService.getOrgMapByType("2");
			model.addAttribute("orgMap", orgMap);
			model.addAttribute("pageInfo", pageInfo);
		} else {
			Page<GjtOrg> pageInfo = gjtOrgService.queryAllByParentId(user.getId(),true, searchParams, pageRequst);
			Map<String, String> orgMap = commonMapService.getChildOrgMap(user.getGjtOrg().getId(), "2");
			model.addAttribute("orgMap", orgMap);
			model.addAttribute("pageInfo", pageInfo);
		}
		
		return "organization/orgChild/list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		model.addAttribute("entity", gjtOrgService.queryById(id));
		model.addAttribute("action", "view");
		return "organization/orgChild/form";
	}

	@RequestMapping(value = "toCreate", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		GjtOrg gjtOrg = new GjtOrg();
		gjtOrg.setParentGjtOrg(user.getGjtOrg());

		Map<String,String> orgList;
		if(user.getIsSuperMgr()){//超级管理员
			orgList = commonMapService.getOrgMapByType("1");
			model.addAttribute("orgList",orgList);
		}

		model.addAttribute("entity", gjtOrg);
		model.addAttribute("action", "create");
		return "organization/orgChild/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtOrg entity,RedirectAttributes redirectAttributes, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "创建成功");

		try {
			entity.setCreatedBy(user.getId());
			gjtOrgService.insertChildOrg(user.getId(), entity);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, e.getMessage());
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/organization/orgChild/list";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model,HttpServletRequest request) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map<String,String> orgList;
		if("0".equals(user.getGrantType())){//超级管理员
			orgList = commonMapService.getOrgMap(user.getId());
			model.addAttribute("orgList",orgList);
		}

		model.addAttribute("entity", gjtOrgService.queryById(id));
		model.addAttribute("action", "update");
		return "organization/orgChild/form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity") GjtOrg entity,RedirectAttributes redirectAttributes,HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");

		String tempParentId = entity.getParentGjtOrg().getId();//更新parentid

		GjtOrg modifyInfo = gjtOrgService.queryById(entity.getId());
		// 院校编码
		modifyInfo.setCode(entity.getCode());
		// 院校名称
		modifyInfo.setOrgName(entity.getOrgName());
		modifyInfo.getGjtSchoolInfo().setXxmc(entity.getOrgName());
		// 联系电话
		modifyInfo.getGjtSchoolInfo().setLinkTel(entity.getGjtSchoolInfo().getLinkTel());
		// 联系人
		modifyInfo.getGjtSchoolInfo().setLinkMan(entity.getGjtSchoolInfo().getLinkMan());
		// 电子邮箱
		modifyInfo.getGjtSchoolInfo().setLinkMail(entity.getGjtSchoolInfo().getLinkMail());
		// 院校地址
		modifyInfo.getGjtSchoolInfo().setXxdz(entity.getGjtSchoolInfo().getXxdz());
		// 描述
		modifyInfo.getGjtSchoolInfo().setMemo(entity.getGjtSchoolInfo().getMemo());
		// APPId
		modifyInfo.getGjtSchoolInfo().setAppid(entity.getGjtSchoolInfo().getAppid());

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if("0".equals(user.getGrantType())) {//超级管理员
			GjtOrg gjtOrg = gjtOrgService.queryById(tempParentId);
			modifyInfo.setParentGjtOrg(gjtOrg);
		}

		try {
			gjtOrgService.update(modifyInfo);
		} catch (Exception e) {
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/organization/orgChild/list";
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, ServletResponse response)
			throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			try {
				gjtOrgService.delete(Arrays.asList(ids.split(",")));
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败，原因:" + e.getMessage());
			}
		}
		return new Feedback(false, "删除失败");
	}


	/**
	 * 删除分校
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteChildOrg/{id}",method = RequestMethod.POST)
	public Feedback deleteOrg(@PathVariable("id") String id){

		if (StringUtils.isNotBlank(id)) {
			try {
				gjtOrgService.delete(Arrays.asList(id.split(",")));
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败" + e.getMessage());
			}
		}
		return new Feedback(false, "删除失败");
	}

	// 校验编码是否存在
	@RequestMapping(value = "checkLogin")
	@ResponseBody
	public Feedback checkLogin(String code) throws IOException {
		PageRequest pageRequst = new PageRequest(0, 2);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_code", code);
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
	 * @param id
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "changeStatus/{id}/{status}",method = RequestMethod.POST)
	public Feedback changeStatus(@PathVariable("id") String id,@PathVariable("status") String status){

		boolean result = gjtOrgService.updateStatus(id,status);
		Feedback feedback = new Feedback();
		if(result){
			feedback.setSuccessful(true);
		}else {
			feedback.setSuccessful(false);
		}
		return feedback;
	}
}
