/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.CSVUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtChooseManageService;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.common.UploadTmpFile;
import com.gzedu.xlims.web.controller.base.BaseController;

import net.spy.memcached.MemcachedClient;

/**
 * 
 * 功能说明：选课管理
 *
 */
@Controller
@RequestMapping("/edumanage/choosemanage")
public class GjtChooseManageController extends BaseController {

	public static final Logger log = LoggerFactory.getLogger(GjtChooseManageController.class);

	@Autowired
	CommonMapService commonMapService;
	
	@Autowired
	GjtChooseManageService gjtChooseManageService;
	
	@Autowired
	MemcachedClient memcachedClient;
	
	/**
	 * 选课管理列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getChooseManageList", method = RequestMethod.GET)
	public String getChooseManageList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		
		String sync_status = ObjectUtils.toString(searchParams.get("SYNC_STATUS"));
		searchParams.put("SYNC_STATUS", sync_status);
		
		// 当前学期
		String currentGradeId = "";
		Map<String, String> currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(currentGradeMap)) {
			currentGradeId = currentGradeMap.keySet().iterator().next();
			searchParams.put("CURRENT_GRADE_ID", currentGradeId);
			if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("OPEN_GRADE_ID")))) {
				searchParams.put("OPEN_GRADE_ID", currentGradeId);
			} else if ("all".equals(ObjectUtils.toString(searchParams.get("OPEN_GRADE_ID")))) {
				searchParams.remove("OPEN_GRADE_ID");
			} else {
				currentGradeId = ObjectUtils.toString(searchParams.get("OPEN_GRADE_ID"));
			}
		}
		model.addAttribute("CURRENT_GRADE_ID", currentGradeId);
		
		Page pageInfo = gjtChooseManageService.getChooseManageList(searchParams, pageRequst);
		
		model.addAttribute("pageInfo", pageInfo);
		
		Map pyccMap = commonMapService.getPyccMap();
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		Map studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("specialtyMap", specialtyMap);
		
		return "edumanage/choosemanage/getChooseManageList";
	}
	
	/**
	 * 选课管理列表（统计）
	 * @param ids
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "getChooseManageCount")
	@ResponseBody
	public Map getChooseManageCount(HttpServletRequest request) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		
		// 当前学期
		String currentGradeId = "";
		Map<String, String> currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(currentGradeMap)) {
			currentGradeId = currentGradeMap.keySet().iterator().next();
			searchParams.put("CURRENT_GRADE_ID", currentGradeId);
			if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("OPEN_GRADE_ID")))) {
				searchParams.put("OPEN_GRADE_ID", currentGradeId);
			} else if ("all".equals(ObjectUtils.toString(searchParams.get("OPEN_GRADE_ID")))) {
				searchParams.remove("OPEN_GRADE_ID");
			}
		}
		
		Map resultMap = new HashMap();
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		
		searchParams.put("XXW_SYNC_STATUS", "0");
		int choose0Count = gjtChooseManageService.getChooseManageCount(searchParams);
		
		searchParams.put("XXW_SYNC_STATUS", "1");
		int choose1Count = gjtChooseManageService.getChooseManageCount(searchParams);
		
		searchParams.put("XXW_SYNC_STATUS", "2");
		int choose2Count = gjtChooseManageService.getChooseManageCount(searchParams);
		
		int chooseCountAll = choose0Count+choose1Count;
		resultMap.put("choose0Count", choose0Count);
		resultMap.put("choose1Count", choose1Count);
		resultMap.put("choose2Count", choose2Count);
		resultMap.put("chooseCountAll", chooseCountAll);
		
		return resultMap;
	}
	
	/**
	 * 新增选课页面
	 * @return
	 */
	@RequestMapping(value = "getAddChooseInfo")
	public String getAddChooseInfo(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("resultMap", searchParams);
		return "edumanage/choosemanage/getAddChooseInfo";
	}
	
	/**
	 * 查询未选课页面
	 * @return
	 */
	@RequestMapping(value = "getNoChooseInfo")
	public String getNoChooseInfo(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		List chooseList = gjtChooseManageService.getNoChooseInfo(searchParams);
		model.addAttribute("chooseList", chooseList);
		return "edumanage/choosemanage/getNoChooseInfo";
	}
	
	/**
	 * 新增选课
	 * @param ids
	 * @return
	 * @throws IOException
	 */
	@SysLog("新增选课")
	@RequestMapping(value = "addRecResult")
	@ResponseBody
	public Map addRecResult(HttpServletRequest request) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		int num = gjtChooseManageService.addRecResult(searchParams);
		searchParams.put("num", num);
		return searchParams;
	}
	
	/**
	 * 删除选课功能
	 * @param ids
	 * @return
	 * @throws IOException
	 */
	@SysLog("删除选课")
	@RequestMapping(value = "delRecResult")
	@ResponseBody
	public Map delRecResult(HttpServletRequest request) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		int num = gjtChooseManageService.delRecResult(searchParams);
		searchParams.put("num", num);
		return searchParams;
	}
	
	/**
	 * 导出选课
	 * @return
	 */
	@RequestMapping(value = "expRecResult")
	public String expRecResult(HttpServletRequest request, HttpServletResponse response, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String phone = ObjectUtils.toString(user.getSjh());
		try {
			if(EmptyUtils.isNotEmpty(phone)){
				phone = phone.substring(phone.length()-4,phone.length());
			}
			Map formMap = Servlets.getParametersStartingWith(request, "");
			model.addAttribute("resultMap", formMap);
			model.addAttribute("sjh", user.getSjh());
			model.addAttribute("sjhs", phone); 
		} catch (Exception e){
			e.printStackTrace();
		}
		return "edumanage/choosemanage/getRecRecordExp";
	}
	
	/**
	 * 导出选课记录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "expRecordRecResult")
	@SysLog("选课管理-导出选课")
	@ResponseBody
	public void expRecordRecResult(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map formMap = Servlets.getParametersStartingWith(request, "");
		formMap.put("XX_ID", ObjectUtils.toString(user.getGjtOrg().getId()));
		
		// 当前学期
		String currentGradeId = "";
		Map<String, String> currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(currentGradeMap)) {
			currentGradeId = currentGradeMap.keySet().iterator().next();
			formMap.put("CURRENT_GRADE_ID", currentGradeId);
			if (EmptyUtils.isEmpty(ObjectUtils.toString(formMap.get("OPEN_GRADE_ID")))) {
				formMap.put("OPEN_GRADE_ID", currentGradeId);
			} else if ("all".equals(ObjectUtils.toString(formMap.get("OPEN_GRADE_ID")))) {
				formMap.remove("OPEN_GRADE_ID");
			}
		}
		
		try {
			String content = gjtChooseManageService.expRecordRecResult(formMap);
			String fileName = "学员选课表.csv";
			CSVUtils.exportCsv(fileName, content, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 同步选课
	 * @return
	 */
	@RequestMapping(value = "getSyncRecList")
	public String getSyncRecList(HttpServletRequest request, HttpServletResponse response, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map formMap = Servlets.getParametersStartingWith(request, "");
		formMap.put("XX_ID", ObjectUtils.toString(user.getGjtOrg().getId()));
		
		// 当前学期
		String currentGradeId = "";
		Map<String, String> currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(currentGradeMap)) {
			currentGradeId = currentGradeMap.keySet().iterator().next();
			formMap.put("CURRENT_GRADE_ID", currentGradeId);
		}
		try {
			Map resultMap = gjtChooseManageService.getSyncRecList(formMap);
			resultMap.putAll(formMap);
			model.addAttribute("resultMap", resultMap);
		} catch (Exception e){
			e.printStackTrace();
		}
		return "edumanage/choosemanage/getSyncRecList";
	}
	
	/**
	 * 同步选课结果
	 * 
	 * @param request
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "getSyncRecResult")
	@SysLog("同步选课到国开总部")
	public String getSyncRecResult(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<String> filePaths = UploadTmpFile.uploadSimple(request);
		Map formMap = Servlets.getParametersStartingWith(request, "");
		formMap.put("XX_ID", user.getGjtOrg().getId());
		formMap.put("USER_ID", user.getId());
		formMap.put("OrgCode", user.getGjtOrg().getCode());
		formMap.put("VirtualXxzxCode", user.getGjtOrg().getVirtualXxzxCode());
		formMap.put("operatingUserName", user.getLoginAccount());
		
		// 当前学期
		String currentGradeId = "";
		Map<String, String> currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(currentGradeMap)) {
			currentGradeId = currentGradeMap.keySet().iterator().next();
			formMap.put("CURRENT_GRADE_ID", currentGradeId);
		}
		
		Map resultMap = gjtChooseManageService.getSyncRecResult(formMap, request.getSession().getServletContext().getRealPath(""));
		model.addAttribute("resultMap", resultMap);
		return "edumanage/choosemanage/getSyncRecResult";
	}
	
	/**
	 * 同步班级数据到国开学习网
	 * @param ids
	 * @return
	 * @throws IOException
	 */
	@SysLog("同步班级数据到国开学习网")
	@RequestMapping(value = "synchClassStudentCourse")
	@ResponseBody
	public Map synchClassStudentCourse(HttpServletRequest request) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());
		searchParams.put("USER_ID", user.getId());
		searchParams.put("OrgCode", user.getGjtOrg().getCode());
		searchParams.put("VirtualXxzxCode", user.getGjtOrg().getVirtualXxzxCode());
		searchParams.put("operatingUserName", user.getLoginAccount());
		searchParams = gjtChooseManageService.synchClassStudentCourse(searchParams);
		return searchParams;
	}
	
	/**
	 * 获取同步数量
	 * @param ids
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "getSyncNum")
	@ResponseBody
	public Map getSyncNum(HttpServletRequest request) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		String xx_id = user.getGjtOrg().getId();
		String num = ObjectUtils.toString(memcachedClient.get("SYNCNUM_"+xx_id));
		searchParams.put("num", num);
		return searchParams;
	}
}
