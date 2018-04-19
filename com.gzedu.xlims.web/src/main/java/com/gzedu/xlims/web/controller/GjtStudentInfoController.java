
package com.gzedu.xlims.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.web.SimulationUtils;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.TblSysDataService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.service.organization.GjtStudyCenterService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/usermanage/student")
public class GjtStudentInfoController {

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@Autowired
	GjtStudyCenterService gjtStudyCenterService;

	@Autowired
	GjtGradeService gjtGradeService;

	@Autowired
	TblSysDataService tblSysDataService;

	@Autowired
	GjtSpecialtyService gjtSpecialtyService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String querySource(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, ModelMap model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<GjtStudentInfo> page = gjtStudentInfoService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);

		Map<String, String> schoolInfoMap = commonMapService.getOrgMapByOrgId(user.getGjtOrg().getId()); // 学校
		// Map<String, String> orgMap =
		// commonMapService.getOrgMap(user.getId()); // 机构
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		Map<String, String> rollTypeMap = commonMapService.getRollTypeMap();// 学籍状态

		model.addAttribute("pageInfo", page);
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("gradeMap", gradeMap);
		// model.addAttribute("orgMap", orgMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("rollTypeMap", rollTypeMap);
		model.addAttribute("specialtyMap", specialtyMap);
		return "usermanage/student/list";
	}

	// 查找学生信息
	@RequestMapping(value = "queryById/{id}", method = RequestMethod.GET)
	public String queryById(@PathVariable("id") String id, ModelMap model, HttpServletRequest request) {
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(id);

		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		Map<String, String> rollTypeMap = commonMapService.getRollTypeMap();// 学籍状态

		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("rollTypeMap", rollTypeMap);

		model.addAttribute("item", gjtStudentInfo);
		return "usermanage/student/form";
	}

	// 跳转到修改页面
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(String id, ModelMap model) {
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(id);
		model.addAttribute("gjtStudentInfo", gjtStudentInfo);
		return "";
	}

	// 修改对象
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(RedirectAttributes redirectAttributes, GjtStudentInfo item, ModelMap model) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtStudentInfo info = gjtStudentInfoService.queryById(item.getStudentId());
		if (StringUtils.isNotBlank(item.getAvatar())) {
			info.setAvatar(item.getAvatar());
		}
		info.setXm(item.getXm());
		info.setXbm(item.getXbm());
		info.setNation(item.getNation());
		info.setPoliticsstatus(item.getPoliticsstatus());
		info.setUserType(item.getUserType());
		info.setSfzh(item.getSfzh());
		info.setXjzt(item.getXjzt());
		info.setRxny(item.getRxny());
		info.setExedulevel(item.getExedulevel());
		info.setTxdz(item.getTxdz());
		info.setYzbm(item.getYzbm());
		info.setDzxx(item.getDzxx());
		info.setSjh(item.getSjh());
		info.setScCo(item.getScCo());
		info.setScCoAddr(item.getScCoAddr());
		info.setScName(item.getScName());
		info.setScPhone(item.getScPhone());

		Boolean updateEntity = gjtStudentInfoService.updateEntityAndFlushCache(info);
		if (!updateEntity) {
			feedback = new Feedback(true, "网络异常");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/usermanage/student/list";
	}

	@RequestMapping(value = "/simulation", method = RequestMethod.GET)
	public String simulation(String id,HttpServletRequest request) {
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(id);
		String url = "";
		if (studentInfo != null) {
			url = gjtUserAccountService.studentSimulation(studentInfo.getStudentId(), studentInfo.getXh());
		}
		return "redirect:" + url; // 修改完重定向
	}

	// 重置密码
	@RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updatePwd(String id) throws Exception {
		Feedback feedback = new Feedback(false, "网络异常");
		String initialize = "888888";
		int i = gjtUserAccountService.updatePwd(id, Md5Util.encode(initialize), initialize);
		if (i > 0) {
			feedback = new Feedback(true, "密码重置成功");
		}
		return feedback;
	}

	// 帐号停用启用
	@RequestMapping(value = "/updateIsEnabled", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updateIsEnabled(String id, String isEnabled) throws Exception {
		Integer mun = StringUtils.isNotBlank(isEnabled) ? Integer.valueOf(isEnabled) : 0;
		Feedback feedback = new Feedback(false, "网络异常！");
		int i = gjtUserAccountService.updateIsEnabled(id, mun);
		if (i > 0) {
			feedback = new Feedback(true, "操作成功");
		}
		return feedback;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public Feedback delete(String ids) throws Exception {
		String[] selectedIds = null;
		if (ids != null) {
			selectedIds = ids.split(",");
		}
		Boolean bool = gjtStudentInfoService.deleteById(selectedIds);
		Feedback feed = new Feedback(bool, "");
		return feed;
	}
}
