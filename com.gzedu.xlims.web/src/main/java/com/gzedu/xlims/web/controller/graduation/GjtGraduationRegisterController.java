package com.gzedu.xlims.web.controller.graduation;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtSchoolAddress;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtGraduationPlan;
import com.gzedu.xlims.pojo.graduation.GjtGraduationRegister;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.graduation.GjtGraduationPlanService;
import com.gzedu.xlims.service.graduation.GjtGraduationRegisterService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSchoolAddressService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 毕业生登记表控制层<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017-12-15
 * @version 3.0
 */
@Controller
@RequestMapping("/graduation/register")
public class GjtGraduationRegisterController extends BaseController {
	
	private static final Log log = LogFactory.getLog(GjtGraduationRegisterController.class);

	@Autowired
	private GjtGraduationPlanService gjtGraduationPlanService;

	@Autowired
	private GjtGraduationRegisterService gjtGraduationRegisterService;

	@Autowired
	private GjtSchoolAddressService gjtSchoolAddressService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtOrgService gjtOrgService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private CacheService cacheService;

	/**
	 * 查询毕业申请列表
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String orgId = user.getGjtOrg().getId();

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Map<String, String> graduationPlanMap = commonMapService.getGraduationPlanMap(user.getGjtOrg().getId());// 毕业计划
		if(StringUtils.isBlank((String) searchParams.get("EQ_graduationPlanId")) && graduationPlanMap != null && graduationPlanMap.size() > 0) {
			searchParams.put("EQ_graduationPlanId", graduationPlanMap.keySet().iterator().next());
			model.addAttribute("graduationPlanId", searchParams.get("EQ_graduationPlanId"));
		}
		Page<GjtGraduationRegister> pageInfo = gjtGraduationRegisterService.queryGraduationRegisterByPage(orgId, searchParams, pageRequst);

		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 学期
		Map<String, String> pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());

		Map<String, Long> countStateMap = new HashMap<String, Long>();
		Object correctState = searchParams.remove("EQ_expressSignState");
		long countAll = gjtGraduationRegisterService.count(orgId, searchParams);
		countStateMap.put("", countAll);
		searchParams.put("EQ_expressSignState", 0);
		long count0 = gjtGraduationRegisterService.count(orgId, searchParams);
		countStateMap.put("0", count0);
		searchParams.put("EQ_expressSignState", 1);
		long count1 = gjtGraduationRegisterService.count(orgId, searchParams);
		countStateMap.put("1", count1);
		searchParams.put("EQ_expressSignState", 2);
		long count2 = gjtGraduationRegisterService.count(orgId, searchParams);
		countStateMap.put("2", count2);
		searchParams.put("EQ_correctState", correctState);
		
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("graduationPlanMap", graduationPlanMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("countStateMap", countStateMap);
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "graduation/register/graduation_register_list";
	}

	/**
	 * 毕业生登记表-初始化本期毕业学员
	 * @throws Exception
	 */
	@SysLog("毕业生登记表-初始化本期毕业学员")
	@RequiresPermissions("/graduation/register/list$init")
	@RequestMapping(value = "init", method = RequestMethod.POST)
	@ResponseBody
	public Feedback init(HttpServletRequest request, HttpServletResponse response) {
		Feedback feedback = new Feedback(true, "success");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			//查询当前的学期
			String xxId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(user.getGjtOrg().getId());
			String currentGradeId = gjtGradeService.getCurrentGradeId(xxId);
			//根据当前学期查询是否存在毕业计划
			GjtGraduationPlan gjtGraduationPlan = gjtGraduationPlanService.queryByTermId(currentGradeId);
			if (gjtGraduationPlan != null) {
				//当前学期的学期开始时间
				Date currentGradeDate = gjtGradeService.queryById(currentGradeId).getStartDate();
				List<GjtGrade> gjtGradeList = gjtGradeService.getGradeList(currentGradeDate, xxId);
				Date afterDate = null;
				if (gjtGradeList != null && gjtGradeList.size() > 0) {
					GjtGrade gjtGrade = gjtGradeList.get(3);//当前学期往后推2年,即往后推4个学期
					afterDate = gjtGrade.getStartDate();
				}
				if (afterDate != null) {
					gjtGraduationRegisterService.initCurrentTermGraduationStudent(xxId, gjtGraduationPlan.getId(), afterDate);
				} else {
					feedback = new Feedback(false, "院校的学期数据异常！");
				}
			} else {
				feedback = new Feedback(false, "当前学期暂无毕业计划！");
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "服务器异常！");
		}
		return feedback;
	}

	/**
	 * 毕业生登记表-签收快件
	 * @param id
	 * @throws Exception
	 */
	@SysLog("毕业生登记表-签收快件")
	@RequiresPermissions("/graduation/register/list$express")
	@RequestMapping(value = "express", method = RequestMethod.POST)
	@ResponseBody
	public Feedback express(@RequestParam String id,
							HttpServletRequest request, HttpServletResponse response) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "success");
		GjtGraduationRegister info = gjtGraduationRegisterService.queryById(id);
		info.setExpressSignState(2);
		info.setExpressSignDt(new Date());
		info.setUpdatedBy(user.getId());
		boolean flag = gjtGraduationRegisterService.update(info);
		if(!flag) {
			feedback = new Feedback(false, "操作失败！");
		}
		return feedback;
	}

	/**
	 * 毕业生登记表-收件地址管理
	 * @throws Exception
	 */
	@RequiresPermissions("/graduation/register/list$addressManage")
	@RequestMapping(value = "getGraduationAddress", method = RequestMethod.GET)
	@ResponseBody
	public Feedback getGraduationAddress(HttpServletRequest request, HttpServletResponse response) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String xxId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(user.getGjtOrg().getId());

		Feedback feedback = new Feedback(true, "success");
		GjtSchoolAddress info = gjtSchoolAddressService.queryByXxIdAndType(xxId, 1);
		feedback.setObj(info);
		return feedback;
	}

	/**
	 * 毕业生登记表-收件地址管理
	 * @throws Exception
	 */
	@SysLog("毕业生登记表-收件地址管理")
	@RequiresPermissions("/graduation/register/list$addressManage")
	@RequestMapping(value = "updateGraduationAddress", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updateGraduationAddress(GjtSchoolAddress info,
							 HttpServletRequest request, HttpServletResponse response) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String xxId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(user.getGjtOrg().getId());

		Feedback feedback = new Feedback(true, "success");
		GjtSchoolAddress modifyInfo = gjtSchoolAddressService.queryByXxIdAndType(xxId, 1);

		boolean flag = false;
		if(modifyInfo == null) {
			info.setAddressId(UUIDUtils.random());
			info.setXxId(xxId);
			info.setType(1);
			info.setCreatedBy(user.getId());
			flag = gjtSchoolAddressService.insert(info);
		} else {
			modifyInfo.setReceiver(info.getReceiver());
			modifyInfo.setMobile(info.getMobile());
			modifyInfo.setProvinceCode(info.getProvinceCode());
			modifyInfo.setCityCode(info.getCityCode());
			modifyInfo.setAreaCode(info.getAreaCode());
			modifyInfo.setAddress(info.getAddress());
			modifyInfo.setPostcode(info.getPostcode());
			modifyInfo.setUpdatedBy(user.getId());
			flag = gjtSchoolAddressService.update(modifyInfo);
		}
		if(!flag) {
			feedback = new Feedback(false, "操作失败！");
		}
		return feedback;
	}

}
