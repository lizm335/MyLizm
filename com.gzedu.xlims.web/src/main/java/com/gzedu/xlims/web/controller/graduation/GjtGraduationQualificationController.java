package com.gzedu.xlims.web.controller.graduation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyCertif;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyCertifService;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyDegreeService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 毕业资格控制层<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年08月29日
 * @version 3.0
 */
@Controller
@RequestMapping("/graduation/qualification")
public class GjtGraduationQualificationController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(GjtGraduationQualificationController.class);

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtGraduationApplyCertifService gjtGraduationApplyCertifService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtGraduationApplyDegreeService gjtGraduationApplyDegreeService;

	/**
	 * 查询毕业资格列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_gjtStudentInfo.xxId", user.getGjtOrg().getId());
		Integer[] auditStates = { 2, 12 };
		String EQ_auditState = (String) searchParams.get("EQ_auditState");
		if ("2".equals(EQ_auditState)) {
			searchParams.remove("EQ_auditState");
			searchParams.put("IN_auditState", Arrays.asList(auditStates));
		}

		Page<GjtGraduationApplyCertif> pageInfo = gjtGraduationApplyCertifService.queryPage(searchParams, pageRequest);

		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 学期
		Map<String, String> pyccMap = commonMapService.getPyccMap();

		searchParams.remove("IN_auditState");
		searchParams.put("EQ_auditState", 11);
		long yesGraduationCount = gjtGraduationApplyCertifService.count(searchParams);

		searchParams.put("EQ_auditState", 1);
		long yesApplyGraduationCount = gjtGraduationApplyCertifService.count(searchParams);

		searchParams.put("EQ_auditState", 6);
		long noApplyGraduationCount = gjtGraduationApplyCertifService.count(searchParams);

		searchParams.remove("EQ_auditState");
		searchParams.put("IN_auditState", Arrays.asList(auditStates));
		long dissatisfyCount = gjtGraduationApplyCertifService.count(searchParams);

		if ("2".equals(EQ_auditState)) {
			searchParams.put("EQ_auditState", EQ_auditState);
		}
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("yesApplyGraduationCount", yesApplyGraduationCount);
		model.addAttribute("yesGraduationCount", yesGraduationCount);
		model.addAttribute("noApplyGraduationCount", noApplyGraduationCount);
		model.addAttribute("dissatisfyCount", dissatisfyCount);
		model.addAttribute("allCount", pageInfo.getTotalElements());

		return "graduation/qualification/graduation_qualification_list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String view(Model model, HttpServletRequest request, @PathVariable("id") String id) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtGraduationApplyCertif info = gjtGraduationApplyCertifService.queryById(id);
		Map<String, String> pyccMap = commonMapService.getPyccMap();

		GjtStudentInfo student = gjtStudentInfoService.queryById(info.getStudentId());
		model.addAttribute("specialty", student.getGjtSpecialty());
		List<Map<String, Object>> achieveList = gjtGraduationApplyDegreeService
				.queryAchievementByStudentId(info.getStudentId());
		Set<String> modelSet = new HashSet<String>();
		for (Map<String, Object> map : achieveList) {
			modelSet.add((String) map.get("modelId"));
		}
		List<Map<String, Object>> modelList = new ArrayList<Map<String, Object>>();
		BigDecimal totalCredits = new BigDecimal(0);
		BigDecimal centerCredits = new BigDecimal(0);// 中央电大考试学分
		for (String modelId : modelSet) {
			Map<String, Object> modelMap = new HashMap<String, Object>();
			modelMap.put("modelId", modelId);
			List<Map<String, Object>> temp = new ArrayList<Map<String, Object>>();
			BigDecimal getCredits = new BigDecimal(0);
			for (Map<String, Object> map : achieveList) {
				if (modelId.equals(map.get("modelId"))) {
					temp.add(map);
					getCredits = getCredits.add((BigDecimal) map.get("getCredits"));
					if ("2".equals(map.get("examUnit"))) {
						centerCredits = centerCredits.add((BigDecimal) map.get("getCredits"));
					}
				}
			}
			modelMap.put("achieveList", temp);
			modelMap.put("modelName", temp.get(0).get("modelName"));
			modelMap.put("totalscore", temp.get(0).get("totalscore"));
			modelMap.put("crtvuScore", temp.get(0).get("crtvuScore"));
			modelMap.put("getCredits", getCredits);
			totalCredits = totalCredits.add(getCredits);
			modelList.add(modelMap);
		}
		model.addAttribute("modelList", modelList);
		model.addAttribute("totalCredits", totalCredits);
		model.addAttribute("centerCredits", centerCredits);

		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("info", info);
		return "graduation/qualification/graduation_qualification_view";
	}

}
