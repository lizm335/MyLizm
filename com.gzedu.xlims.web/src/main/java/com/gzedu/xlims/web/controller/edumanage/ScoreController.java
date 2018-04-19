/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtRecResult;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 
 * 功能说明：成绩
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月12日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/edumanage/score")
public class ScoreController {

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtRecResultService gjtRecResultService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<GjtRecResult> pageInfo = gjtRecResultService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);

		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次

		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("pageInfo", pageInfo);
		
		
		return "edumanage/score/list";
	}

	// @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	// public String viewForm(@PathVariable("id") String id, Model model) {
	// model.addAttribute("entity", GjtRecResultService.queryBy(id));
	// model.addAttribute("action", "view");
	// return "edumanage/course/form";
	// }
	//
	// @RequestMapping(value = "create", method = RequestMethod.GET)
	// public String createForm(Model model, HttpServletRequest request) {
	// GjtUserAccount user = (GjtUserAccount)
	// request.getSession().getAttribute(WebConstants.CURRENT_USER);
	// Map<String, String> orgMap = commonMapService.getOrgMapBy(user.getId(),
	// false);
	// Map<String, String> employeeMap =
	// commonMapService.getEmployeeMap(user.getGjtOrg().getId(),
	// EmployeeTypeEnum.辅导教师);
	//
	// model.addAttribute("orgMap", orgMap);
	// model.addAttribute("employeeMap", employeeMap);
	// model.addAttribute("entity", new GjtRecResult());
	// model.addAttribute("action", "create");
	// return "edumanage/course/form";
	// }
	//
	// @RequestMapping(value = "create", method = RequestMethod.POST)
	// public String create(@Valid GjtRecResult entity, RedirectAttributes
	// redirectAttributes) {
	// Feedback feedback = new Feedback(true, "创建成功");
	// try {
	// GjtRecResultService.insert(entity);
	// } catch (Exception e) {
	// feedback = new Feedback(false, "创建失败");
	// }
	// redirectAttributes.addFlashAttribute("feedback", feedback);
	// return "redirect:/edumanage/course/list";
	// }
	//
	// @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	// public String updateForm(@PathVariable("id") String id, Model model,
	// HttpServletRequest request) {
	// GjtUserAccount user = (GjtUserAccount)
	// request.getSession().getAttribute(WebConstants.CURRENT_USER);
	// Map<String, String> orgMap = commonMapService.getOrgMapBy(user.getId(),
	// false);
	// Map<String, String> employeeMap =
	// commonMapService.getEmployeeMap(user.getGjtOrg().getId(),
	// EmployeeTypeEnum.辅导教师);
	//
	// model.addAttribute("orgMap", orgMap);
	// model.addAttribute("employeeMap", employeeMap);
	// model.addAttribute("entity", GjtRecResultService.queryBy(id));
	// model.addAttribute("action", "update");
	// return "edumanage/course/form";
	// }
	//
	// @RequestMapping(value = "update", method = RequestMethod.POST)
	// public String update(@Valid @ModelAttribute("info") GjtRecResult entity,
	// RedirectAttributes redirectAttributes) {
	// Feedback feedback = new Feedback(true, "更新成功");
	//
	// GjtRecResult modifyInfo =
	// GjtRecResultService.queryBy(entity.getCourseId());
	//
	// modifyInfo.setGjtOrg(entity.getGjtOrg());
	// modifyInfo.setKch(entity.getKch());
	// modifyInfo.setKcmc(entity.getKcmc());
	// modifyInfo.setSpeakerTeacher(entity.getSpeakerTeacher());
	// modifyInfo.setGjtEmployeeInfo(entity.getGjtEmployeeInfo());
	//
	// try {
	// GjtRecResultService.update(modifyInfo);
	// } catch (Exception e) {
	// feedback = new Feedback(false, "更新失败");
	// }
	// redirectAttributes.addFlashAttribute("feedback", feedback);
	// return "redirect:/edumanage/course/list";
	// }
	//
	// @RequestMapping(value = "delete")
	// @ResponseBody
	// public Feedback delete(String ids, ServletResponse response) throws
	// IOException {
	// if (StringUtils.isNotBlank(ids)) {
	// try {
	// GjtRecResultService.delete(Arrays.asList(ids.split(",")));
	// return new Feedback(true, "删除成功");
	// } catch (Exception e) {
	// return new Feedback(false, "删除失败");
	// }
	// }
	// return new Feedback(false, "删除失败");
	// }
	//
	// @RequestMapping(value = "share/{id}", method = RequestMethod.GET)
	// public String shareForm(@PathVariable("id") String id, Model model,
	// HttpServletRequest request) {
	// GjtUserAccount user = (GjtUserAccount)
	// request.getSession().getAttribute(WebConstants.CURRENT_USER);
	// Map<String, String> orgMap = commonMapService.getOrgMapBy(user.getId(),
	// false);
	// orgMap.remove(user.getGjtOrg().getId());
	//
	// model.addAttribute("orgMap", orgMap);
	// model.addAttribute("entity", GjtRecResultService.queryBy(id));
	// model.addAttribute("action", "share");
	// return "edumanage/course/share";
	// }
	//
	// @RequestMapping(value = "share", method = RequestMethod.POST)
	// public String share(@Valid @ModelAttribute("entity") GjtRecResult entity,
	// String[] orgId,
	// RedirectAttributes redirectAttributes) {
	// Feedback feedback = new Feedback(true, "共享成功");
	//
	// GjtRecResult modifyInfo =
	// GjtRecResultService.queryBy(entity.getCourseId());
	//
	// List<GjtOrg> schoolinfos = gjtOrgService.queryAll(Arrays.asList(orgId));
	// modifyInfo.setShareGjtSchoolInfos(schoolinfos);
	//
	// try {
	// GjtRecResultService.update(modifyInfo);
	// } catch (Exception e) {
	// feedback = new Feedback(false, "共享失败");
	// }
	// redirectAttributes.addFlashAttribute("feedback", feedback);
	// return "redirect:/edumanage/course/list";
	// }

}
