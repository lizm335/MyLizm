package com.gzedu.xlims.web.controller.thesis;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
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

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.pojo.status.ThesisStatusEnum;
import com.gzedu.xlims.pojo.thesis.GjtThesisAdviser;
import com.gzedu.xlims.pojo.thesis.GjtThesisApply;
import com.gzedu.xlims.pojo.thesis.GjtThesisArrange;
import com.gzedu.xlims.pojo.thesis.GjtThesisPlan;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.thesis.GjtThesisAdviserService;
import com.gzedu.xlims.service.thesis.GjtThesisApplyService;
import com.gzedu.xlims.service.thesis.GjtThesisArrangeService;
import com.gzedu.xlims.service.thesis.GjtThesisPlanService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/thesisArrange")
public class GjtThesisArrangeController {

	private final static Logger log = LoggerFactory.getLogger(GjtThesisArrangeController.class);

	@Autowired
	private GjtThesisArrangeService gjtThesisArrangeService;

	@Autowired
	private GjtThesisAdviserService gjtThesisAdviserService;

	@Autowired
	private GjtThesisPlanService gjtThesisPlanService;

	@Autowired
	private GjtThesisApplyService gjtThesisApplyService;

	@Autowired
	private GjtOrgService gjtOrgService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	private CommonMapService commonMapService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		String orgId = user.getGjtOrg().getId();
		if (!"1".equals(user.getGjtOrg().getOrgType())) {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(orgId);
		}

		searchParams.put("EQ_gjtThesisPlan.orgId", orgId);

		// 默认选择当前论文计划
		if (EmptyUtils.isEmpty(searchParams) || StringUtils.isBlank((String) searchParams.get("EQ_thesisPlanId"))) {
			String gradeId = gjtGradeService.getCurrentGradeId(orgId);
			GjtThesisPlan thesisPlan = gjtThesisPlanService.findByGradeIdAndOrgIdAndStatus(gradeId, orgId, 3);
			if (thesisPlan != null) {
				searchParams.put("EQ_thesisPlanId", thesisPlan.getThesisPlanId());
				model.addAttribute("thesisPlanId", thesisPlan.getThesisPlanId());
			}
		} else if (EmptyUtils.isNotEmpty(searchParams) && EmptyUtils.isNotEmpty(searchParams.get("EQ_thesisPlanId"))) {
			model.addAttribute("thesisPlanId", ObjectUtils.toString(searchParams.get("EQ_thesisPlanId")));
		}

		// 查询“未开始”、“进行中”、“已结束”、“未设置论文指导老师”和“未设置论文答辩老师”
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.put("GT_gjtThesisPlan.applyBeginDt", today); // 未开始
		model.addAttribute("notStart", gjtThesisArrangeService.findAll(map, pageRequst).getTotalElements());
		map.remove("GT_gjtThesisPlan.applyBeginDt");
		map.put("LTE_gjtThesisPlan.applyBeginDt", today);
		map.put("GTE_gjtThesisPlan.defenceDt", today);
		model.addAttribute("starting", gjtThesisArrangeService.findAll(map, pageRequst).getTotalElements());
		map.remove("LTE_gjtThesisPlan.applyBeginDt");
		map.remove("GTE_gjtThesisPlan.defenceDt");
		map.put("LT_gjtThesisPlan.defenceDt", today); // 已结束
		model.addAttribute("end", gjtThesisArrangeService.findAll(map, pageRequst).getTotalElements());
		map.remove("LT_gjtThesisPlan.defenceDt");
		map.put("ISNULL_gjtThesisAdvisers1.adviserId", null); // 未设置论文指导老师
		model.addAttribute("notSetGuideTeacher", gjtThesisArrangeService.findAll(map, pageRequst).getTotalElements());
		map.remove("ISNULL_gjtThesisAdvisers1.adviserId");
		map.put("ISNULL_gjtThesisAdvisers2.adviserId", null); // 未设置论文答辩老师
		map.put("ISNULL_gjtThesisAdvisers3.adviserId", null);
		model.addAttribute("notSetDefenceTeacher", gjtThesisArrangeService.findAll(map, pageRequst).getTotalElements());

		// 状态判断
		if (request.getParameter("status") != null) {
			int status = Integer.parseInt(request.getParameter("status").toString());
			if (status == 1) { // 未开始
				searchParams.put("GT_gjtThesisPlan.applyBeginDt", today);
			} else if (status == 2) { // 进行中
				searchParams.put("LTE_gjtThesisPlan.applyBeginDt", today);
				searchParams.put("GTE_gjtThesisPlan.defenceDt", today);
			} else if (status == 3) { // 已结束
				searchParams.put("LT_gjtThesisPlan.defenceDt", today);
			} else if (status == 4) { // 未设置论文指导老师
				searchParams.put("ISNULL_gjtThesisAdvisers1.adviserId", null);
			} else if (status == 5) { // 未设置论文答辩老师
				searchParams.put("ISNULL_gjtThesisAdvisers2.adviserId", null);
				searchParams.put("ISNULL_gjtThesisAdvisers3.adviserId", null);
			}
		}

		log.info("searchParams:[" + searchParams + "]");
		Page<GjtThesisArrange> pageInfo = gjtThesisArrangeService.findAll(searchParams, pageRequst);
		Date now = new Date();
		for (GjtThesisArrange thesisArrange : pageInfo) {
			// 设置状态
			if (thesisArrange.getGjtThesisPlan().getApplyBeginDt().compareTo(now) > 0) {
				thesisArrange.setStatus(1);
			} else if (thesisArrange.getGjtThesisPlan().getApplyBeginDt().compareTo(now) <= 0
					&& thesisArrange.getGjtThesisPlan().getDefenceDt().compareTo(now) >= 0) {
				thesisArrange.setStatus(2);
			} else if (thesisArrange.getGjtThesisPlan().getDefenceDt().compareTo(now) < 0) {
				thesisArrange.setStatus(3);
			}

			if (thesisArrange.getGjtThesisPlan().getSubmitProposeEndDt().compareTo(now) > 0) {
				thesisArrange.setCanUpdate(true);
			} else {
				thesisArrange.setCanUpdate(false);
			}

			// 统计申请人数
			List<GjtThesisApply> thesisApplys = gjtThesisApplyService.findByThesisPlanIdAndSpecialtyBaseId(
					thesisArrange.getThesisPlanId(), thesisArrange.getSpecialtyBaseId());
			if (thesisApplys != null && thesisApplys.size() > 0) {
				thesisArrange.setApplyNum(thesisApplys.size());

				int passNum = 0;
				for (GjtThesisApply thesisApply : thesisApplys) {
					if (ThesisStatusEnum.THESIS_COMPLETED.getValue() == thesisApply.getStatus()) {
						passNum++;
					}
				}
				thesisArrange.setPassNum(passNum);
			} else {
				thesisArrange.setApplyNum(0);
				thesisArrange.setPassNum(0);
			}
		}

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("thesisPlanMap", gjtThesisPlanService.getThesisPlanMap(user.getGjtOrg().getId()));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyBaseMap(user.getGjtOrg().getId()));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnUpdate", subject.isPermitted("/thesisArrange/list$update"));
		model.addAttribute("isBtnCreate", subject.isPermitted("/thesisArrange/list$create"));
		model.addAttribute("isBtnView", subject.isPermitted("/thesisArrange/list$view"));

		return "thesis/thesisArrange_list";
	}

	/**
	 * 返回新增页面
	 * 
	 * @param model
	 * @return
	 */
	@RequiresPermissions("/thesisArrange/list$create")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request, String thesisPlanId) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		List<GjtThesisPlan> list = gjtThesisPlanService.findByOrgIdAndStatus(user.getGjtOrg().getId(), 3);
		GjtThesisPlan thesisPlan = null;
		if (StringUtils.isBlank(thesisPlanId)) {
			GjtGrade gjtGrade = gjtGradeService.findCurrentGrade(user.getGjtOrg().getId());
			thesisPlan = gjtThesisPlanService.findByGradeIdAndOrgIdAndStatus(gjtGrade.getGradeId(),
					user.getGjtOrg().getId(), 3);
			thesisPlanId = thesisPlan.getThesisPlanId();
		} else {
			thesisPlan = gjtThesisPlanService.findOne(thesisPlanId);
		}

		if (thesisPlan != null) {
			// 查询可申请毕业论文的专业
			List<Map<String, Object>> specialtyList = gjtThesisArrangeService.getCanApplySpecialty(
					user.getGjtOrg().getId(), thesisPlan.getGradeId(), thesisPlan.getThesisPlanId());
			model.addAttribute("specialtyList", specialtyList);
		}

		model.addAttribute("list", list);
		model.addAttribute("thesisPlanId", thesisPlanId);

		return "thesis/thesisArrange_setting";
	}

	@RequiresPermissions("/thesisArrange/list$create")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback create(@RequestParam("thesisPlanId") String thesisPlanId,
			@RequestParam("specialtyBaseIds") String specialtyBaseIds,
			@RequestParam("canApplyNums") String canApplyNums, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "设置成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			List<GjtThesisArrange> thesisArranges = new ArrayList<GjtThesisArrange>();
			String[] specialtyBaseIds2 = specialtyBaseIds.split(",");
			String[] canApplyNums2 = canApplyNums.split(",");
			for (int i = 0; i < specialtyBaseIds2.length; i++) {
				GjtThesisArrange thesisArrange = new GjtThesisArrange();
				thesisArrange.setThesisPlanId(thesisPlanId);
				thesisArrange.setSpecialtyBaseId(specialtyBaseIds2[i]);
				thesisArrange.setCanApplyNum(Integer.parseInt(canApplyNums2[i]));
				thesisArrange.setCreatedBy(user.getId());

				thesisArranges.add(thesisArrange);
			}

			if (thesisArranges.size() > 0) {
				gjtThesisArrangeService.insert(thesisArranges);
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "设置失败");
			log.error(e.getMessage(), e);
		}

		return feedback;
	}

	/**
	 * 返回编辑页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("/thesisArrange/list$update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		GjtThesisArrange thesisArrange = gjtThesisArrangeService.findOne(id);
		// 统计申请人数
		List<GjtThesisApply> thesisApplys = gjtThesisApplyService.findByThesisPlanIdAndSpecialtyBaseId(
				thesisArrange.getThesisPlanId(), thesisArrange.getSpecialtyBaseId());
		if (thesisApplys != null && thesisApplys.size() > 0) {
			thesisArrange.setApplyNum(thesisApplys.size());

			int defenceNum = 0;
			for (GjtThesisApply thesisApply : thesisApplys) {
				if (thesisApply.getNeedDefence() == 1) {
					defenceNum++;
				}
			}
			thesisArrange.setDefenceNum(defenceNum);
		} else {
			thesisArrange.setApplyNum(0);
			thesisArrange.setDefenceNum(0);
		}

		List<GjtThesisAdviser> advisers1 = thesisArrange.getGjtThesisAdvisers1();
		if (advisers1 != null && advisers1.size() > 0) {
			for (GjtThesisAdviser adviser : advisers1) {
				Map<String, Object> guideNum = gjtThesisArrangeService.getGuideNum(thesisArrange.getThesisPlanId(),
						adviser.getTeacherId(), thesisArrange.getSpecialtyBaseId());
				if (guideNum.get("num1") != null) {
					adviser.setGuideNum1(((BigDecimal) guideNum.get("num1")).intValue());
				} else {
					adviser.setGuideNum1(0);
				}

				if (guideNum.get("num2") != null) {
					adviser.setGuideNum2(((BigDecimal) guideNum.get("num2")).intValue());
				} else {
					adviser.setGuideNum2(0);
				}
			}
		}

		model.addAttribute("entity", thesisArrange);
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("action", "update");

		return "thesis/thesisArrange_form";
	}

	@RequiresPermissions("/thesisArrange/list$update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(@ModelAttribute("entity") GjtThesisArrange entity, String[] advisers1, Integer[] adviserNums,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "保存成功");

		try {
			// 先删除指导老师
			Set<Integer> types = new HashSet<Integer>();
			types.add(1);
			gjtThesisAdviserService.deleteByArrangeIdAndAdviserTypes(entity.getArrangeId(), types);

			GjtThesisArrange thesisArrange = gjtThesisArrangeService.findOne(entity.getArrangeId());
			thesisArrange.setExampleUrl(entity.getExampleUrl());
			thesisArrange.setExampleName(entity.getExampleName());
			thesisArrange.setUpdatedBy(user.getId());

			if (thesisArrange.getGjtThesisAdvisers() == null) {
				thesisArrange.setGjtThesisAdvisers(new ArrayList<GjtThesisAdviser>());
			}
			if (advisers1 != null && advisers1.length > 0) {
				for (int i = 0; i < advisers1.length; i++) {
					GjtThesisAdviser adviser = new GjtThesisAdviser();
					adviser.setArrangeId(thesisArrange.getArrangeId());
					adviser.setAdviserType(1);
					adviser.setTeacherId(advisers1[i]);
					adviser.setAdviserNum(adviserNums[i]);

					thesisArrange.getGjtThesisAdvisers().add(adviser);
				}
			}

			gjtThesisArrangeService.update(thesisArrange);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "保存失败");
		}

		return feedback;
	}

	/**
	 * 返回编辑页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("/thesisArrange/list$update")
	@RequestMapping(value = "updateDefenceTeacher/{id}", method = RequestMethod.GET)
	public String updateDefenceTeacherForm(@PathVariable("id") String id, Model model) {
		GjtThesisArrange thesisArrange = gjtThesisArrangeService.findOne(id);
		// 统计申请人数
		List<GjtThesisApply> thesisApplys = gjtThesisApplyService.findByThesisPlanIdAndSpecialtyBaseId(
				thesisArrange.getThesisPlanId(), thesisArrange.getSpecialtyBaseId());
		if (thesisApplys != null && thesisApplys.size() > 0) {
			thesisArrange.setApplyNum(thesisApplys.size());

			int defenceNum = 0;
			for (GjtThesisApply thesisApply : thesisApplys) {
				if (thesisApply.getNeedDefence() == 1) {
					defenceNum++;
				}
			}
			thesisArrange.setDefenceNum(defenceNum);
		} else {
			thesisArrange.setApplyNum(0);
			thesisArrange.setDefenceNum(0);
		}

		model.addAttribute("entity", thesisArrange);
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("action", "update");

		return "thesis/thesisArrange_form2";
	}

	@RequiresPermissions("/thesisArrange/list$update")
	@RequestMapping(value = "updateDefenceTeacher", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updateDefenceTeacher(@ModelAttribute("entity") GjtThesisArrange entity, String[] advisers2,
			String[] advisers3, HttpServletRequest request) {
		// GjtUserAccount user = (GjtUserAccount)
		// request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "保存成功");

		try {
			// 先删除答辩老师
			Set<Integer> types = new HashSet<Integer>();
			types.add(2);
			types.add(3);
			gjtThesisAdviserService.deleteByArrangeIdAndAdviserTypes(entity.getArrangeId(), types);

			GjtThesisArrange thesisArrange = gjtThesisArrangeService.findOne(entity.getArrangeId());

			thesisArrange.setGjtThesisAdvisers2(new ArrayList<GjtThesisAdviser>());
			thesisArrange.setGjtThesisAdvisers3(new ArrayList<GjtThesisAdviser>());
			if (advisers2 != null && advisers2.length > 0) {
				for (String teacherId : advisers2) {
					GjtThesisAdviser adviser = new GjtThesisAdviser();
					adviser.setArrangeId(thesisArrange.getArrangeId());
					adviser.setAdviserType(2);
					adviser.setTeacherId(teacherId);

					thesisArrange.getGjtThesisAdvisers2().add(adviser);
				}
			}
			if (advisers3 != null && advisers3.length > 0) {
				for (String teacherId : advisers3) {
					GjtThesisAdviser adviser = new GjtThesisAdviser();
					adviser.setArrangeId(thesisArrange.getArrangeId());
					adviser.setAdviserType(3);
					adviser.setTeacherId(teacherId);

					thesisArrange.getGjtThesisAdvisers3().add(adviser);
				}
			}

			gjtThesisArrangeService.updateDefenceTeacher(thesisArrange);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "保存失败");
		}

		return feedback;
	}

	@RequiresPermissions("/thesisArrange/list$view")
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		GjtThesisArrange thesisArrange = gjtThesisArrangeService.findOne(id);
		// 统计申请人数
		List<GjtThesisApply> thesisApplys = gjtThesisApplyService.findByThesisPlanIdAndSpecialtyBaseId(
				thesisArrange.getThesisPlanId(), thesisArrange.getSpecialtyBaseId());
		if (thesisApplys != null && thesisApplys.size() > 0) {
			thesisArrange.setApplyNum(thesisApplys.size());

			int defenceNum = 0;
			for (GjtThesisApply thesisApply : thesisApplys) {
				if (thesisApply.getNeedDefence() == 1) {
					defenceNum++;
				}
			}
			thesisArrange.setDefenceNum(defenceNum);
		} else {
			thesisArrange.setApplyNum(0);
			thesisArrange.setDefenceNum(0);
		}

		List<GjtThesisAdviser> advisers1 = thesisArrange.getGjtThesisAdvisers1();
		if (advisers1 != null && advisers1.size() > 0) {
			for (GjtThesisAdviser adviser : advisers1) {
				Map<String, Object> guideNum = gjtThesisArrangeService.getGuideNum(thesisArrange.getThesisPlanId(),
						adviser.getTeacherId(), thesisArrange.getSpecialtyBaseId());
				if (guideNum.get("num1") != null) {
					adviser.setGuideNum1(((BigDecimal) guideNum.get("num1")).intValue());
				} else {
					adviser.setGuideNum1(0);
				}

				if (guideNum.get("num2") != null) {
					adviser.setGuideNum2(((BigDecimal) guideNum.get("num2")).intValue());
				} else {
					adviser.setGuideNum2(0);
				}
			}
		}

		model.addAttribute("entity", thesisArrange);
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("action", "view");

		return "thesis/thesisArrange_view";
	}

	@RequestMapping(value = "choiceGuideTeacher", method = RequestMethod.GET)
	public String choiceGuideTeacher(@RequestParam("arrangeId") String arrangeId,
			@RequestParam("teacherIds") String teacherIds, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<GjtEmployeeInfo> guideTeachers = gjtEmployeeInfoService.findListByType(EmployeeTypeEnum.论文教师.getNum(),
				EmployeeTypeEnum.论文指导教师.getNum(), user.getGjtOrg().getId());

		if (guideTeachers != null && teacherIds != null && !"".equals(teacherIds)) {
			String[] ids = teacherIds.split(",");
			for (String id : ids) {
				for (GjtEmployeeInfo guideTeacher : guideTeachers) {
					if (id.equals(guideTeacher.getEmployeeId())) {
						guideTeachers.remove(guideTeacher);
						break;
					}
				}
			}
		}

		model.addAttribute("guideTeachers", guideTeachers);

		GjtThesisArrange thesisArrange = gjtThesisArrangeService.findOne(arrangeId);
		GjtGrade grade = gjtGradeService.findPrevGradeBefore(thesisArrange.getGjtThesisPlan().getOrgId(),
				thesisArrange.getGjtThesisPlan().getGradeId());
		List<Map<String, Object>> list = gjtThesisArrangeService.getPreNoPass(
				thesisArrange.getGjtThesisPlan().getOrgId(), grade.getGradeId(), thesisArrange.getSpecialtyBaseId());
		model.addAttribute("list", list);

		return "thesis/choiceGuideTeacher";
	}

	@RequestMapping(value = "choiceDefenceTeacher", method = RequestMethod.GET)
	public String choiceDefenceTeacher(@RequestParam("teacherIds") String teacherIds, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<GjtEmployeeInfo> defenceTeachers = gjtEmployeeInfoService.findListByType(EmployeeTypeEnum.论文教师.getNum(),
				EmployeeTypeEnum.论文答辩教师.getNum(), user.getGjtOrg().getId());

		if (defenceTeachers != null && teacherIds != null && !"".equals(teacherIds)) {
			String[] ids = teacherIds.split(",");
			for (String id : ids) {
				for (GjtEmployeeInfo guideTeacher : defenceTeachers) {
					if (id.equals(guideTeacher.getEmployeeId())) {
						defenceTeachers.remove(guideTeacher);
						break;
					}
				}
			}
		}

		model.addAttribute("defenceTeachers", defenceTeachers);
		return "thesis/choiceDefenceTeacher";
	}

	@RequestMapping(value = "findGuideNum")
	@ResponseBody
	public Map<String, Object> findGuideNum(@RequestParam("arrangeId") String arrangeId,
			@RequestParam("teacherId") String teacherId, HttpServletRequest request) {
		GjtThesisArrange thesisArrange = gjtThesisArrangeService.findOne(arrangeId);
		Map<String, Object> guideNum = gjtThesisArrangeService.getGuideNum(thesisArrange.getThesisPlanId(), teacherId,
				thesisArrange.getSpecialtyBaseId());
		if (guideNum.get("num1") == null) {
			guideNum.put("num1", 0);
		}

		if (guideNum.get("num2") == null) {
			guideNum.put("num2", 0);
		}

		return guideNum;
	}

	@RequestMapping(value = "listStudent", method = RequestMethod.GET)
	public String listStudent(@RequestParam("thesisPlanId") String thesisPlanId,
			@RequestParam("specialtyBaseId") String specialtyBaseId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		GjtThesisPlan thesisPlan = gjtThesisPlanService.findOne(thesisPlanId);
		Page<Map<String, Object>> pageInfo = gjtThesisArrangeService.getCanApplyStudent(thesisPlan.getOrgId(),
				thesisPlan.getGradeId(), thesisPlanId, specialtyBaseId, searchParams, pageRequst);

		model.addAttribute("thesisPlan", thesisPlan);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMapBySpecialtyBaseId(specialtyBaseId));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(user.getGjtOrg().getId()));
		model.addAttribute("thesisStatusMap", EnumUtil.getThesisApplyStatusStringMap());

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnExport", subject.isPermitted("/thesisArrange/list$exportCanApplyStudent"));

		return "thesis/thesisArrange_studentList";
	}

	@RequestMapping(value = "exportStudentList", method = RequestMethod.GET)
	public void exportStudentList(@RequestParam("thesisPlanId") String thesisPlanId,
			@RequestParam("specialtyBaseId") String specialtyBaseId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Map<String, Object> searchParams = new HashMap<String, Object>();

		GjtThesisPlan thesisPlan = gjtThesisPlanService.findOne(thesisPlanId);
		Page<Map<String, Object>> pageInfo = gjtThesisArrangeService.getCanApplyStudent(thesisPlan.getOrgId(),
				thesisPlan.getGradeId(), thesisPlanId, specialtyBaseId, searchParams, pageRequst);
		HSSFWorkbook workbook = this.getStudentListWorkbook(thesisPlan, pageInfo.getContent());
		ExcelUtil.downloadExcelFile(response, workbook, "可申请学生明细表.xls");
	}

	private HSSFWorkbook getStudentListWorkbook(GjtThesisPlan thesisPlan, List<Map<String, Object>> list) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("可申请学生明细表");
			String[] heads = { "论文计划名称", "论文计划编号", "姓名", "学号", "手机", "层次", "学期", "专业名称", "专业代码", "专业规则号", "所属学习中心",
					"状态" };

			HSSFRow row = sheet.createRow(0);
			for (int i = 0; i < heads.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(heads[i]);
			}

			int rowIdx = 1;
			int colIdx = 0;
			HSSFCell cell;

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行

			// 为了能够使用换行，需要设置单元格的样式 wrap=true
			HSSFCellStyle s = wb.createCellStyle();
			s.setWrapText(true);

			Map<String, String> thesisStatusMap = EnumUtil.getThesisApplyStatusStringMap();

			for (Map<String, Object> obj : list) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(thesisPlan.getThesisPlanName());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(thesisPlan.getThesisPlanCode());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("studentName") != null ? obj.get("studentName").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("studentCode") != null ? obj.get("studentCode").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("mobile") != null ? obj.get("mobile").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("trainingLevel") != null ? obj.get("trainingLevel").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("gradeName") != null ? obj.get("gradeName").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("specialtyName") != null ? obj.get("specialtyName").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("specialtyCode") != null ? obj.get("specialtyCode").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("ruleCode") != null ? obj.get("ruleCode").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("scName") != null ? obj.get("scName").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("status") != null ? thesisStatusMap.get(obj.get("status").toString()) : "--");
			}

			return wb;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
