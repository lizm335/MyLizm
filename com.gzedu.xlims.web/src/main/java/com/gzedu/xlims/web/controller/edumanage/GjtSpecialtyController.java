/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtCourseOwnership;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtSpecialtyBase;
import com.gzedu.xlims.pojo.GjtSpecialtyModuleLimit;
import com.gzedu.xlims.pojo.GjtSpecialtyOwnership;
import com.gzedu.xlims.pojo.GjtSpecialtyPlan;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.TblSysData;
import com.gzedu.xlims.pojo.exam.GjtExamSubjectNew;
import com.gzedu.xlims.pojo.status.CourseCategory;
import com.gzedu.xlims.pojo.status.CourseType;
import com.gzedu.xlims.pojo.status.ExamUnit;
import com.gzedu.xlims.pojo.status.TermType;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.TblSysDataService;
import com.gzedu.xlims.service.edumanage.GjtSpecialtyPlanService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.edumanage.GjtTextbookPlanOwnershipService;
import com.gzedu.xlims.service.exam.GjtExamSubjectNewService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtSpecialtyBaseService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.service.share.GjtShareService;
import com.gzedu.xlims.service.share.GjtSpecialtyShareService;
import com.gzedu.xlims.service.textbook.GjtTextbookService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/edumanage/specialty")
public class GjtSpecialtyController {

	private static final Logger log = LoggerFactory.getLogger(GjtSpecialtyController.class);

	@Autowired
	GjtTeachPlanService gjtTeachPlanService;

	@Autowired
	TblSysDataService tblSysDataService;

	@Autowired
	GjtSpecialtyService gjtSpecialtyService;

	@Autowired
	GjtSpecialtyPlanService gjtSpecialtyPlanService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtSpecialtyShareService gjtSpecialtyShareService;

	@Autowired
	GjtShareService gjtShareService;

	@Autowired
	GjtCourseService gjtCourseService;

	@Autowired
	GjtTextbookService gjtTextbookService;

	@Autowired
	GjtTextbookPlanOwnershipService gjtTextbookPlanOwnershipService;

	@Autowired
	GjtExamSubjectNewService gjtExamSubjectNewService;

	@Autowired
	private GjtSpecialtyBaseService gjtSpecialtyBaseService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Sort sort = new Sort(Direction.DESC, "createdDt");

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, sort);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		List<GjtSpecialtyOwnership> gjtSpecialtyOwnershipList = gjtSpecialtyShareService
				.findByOrgCode(user.getGjtOrg().getId());

		List<String> specialtyIds = new ArrayList<String>();
		if (gjtSpecialtyOwnershipList.size() != 0) {
			for (GjtSpecialtyOwnership gjtSpecialtyOwnership : gjtSpecialtyOwnershipList) {
				if (gjtSpecialtyOwnership != null) {
					specialtyIds.add(gjtSpecialtyOwnership.getSpecialtyId());
				}
			}

		}
		Page<GjtSpecialty> page = gjtSpecialtyService.queryAllAndShare(user.getGjtOrg().getId(), searchParams,
				pageRequst, specialtyIds);

		// Page<GjtSpecialty> page =
		// gjtSpecialtyService.queryAll(user.getGjtOrg().getId(), searchParams,
		// pageRequst);
		// Map<String, String> orgMap =
		// commonMapService.getOrgMapBy(user.getId(), false);
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次

		model.addAttribute("pyccMap", pyccMap);
		// model.addAttribute("orgMap", orgMap);
		// model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pageInfo", page);
		model.addAttribute("user", user);

		// Map<String, String> syhyMap =
		// commonMapService.getDates("TRADE_CODE");
		// model.addAttribute("syhyMap", syhyMap);
		model.addAttribute("subjectMap", commonMapService.getExsubjectMap()); // 学科
		if (searchParams.get("EQ_subject") != null) {
			model.addAttribute("categoryMap",
					commonMapService.getExsubjectkindMap(searchParams.get("EQ_subject").toString())); // 门类
		}
		model.addAttribute("subjectCategoryMap", commonMapService.getExsubjectAndkindMap()); // 门类
		model.addAttribute("specialtyBaseMap", commonMapService.getSpecialtyBaseMap(user.getGjtOrg().getId()));

		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.remove("EQ_type");
		map.remove("EQ_status");
		long all = gjtSpecialtyService.queryAllAndShare(user.getGjtOrg().getId(), map, pageRequst, specialtyIds)
				.getTotalElements();
		model.addAttribute("all", all);

		map.put("EQ_type", 1);
		long isEnabledNum = gjtSpecialtyService
				.queryAllAndShare(user.getGjtOrg().getId(), map, pageRequst, specialtyIds).getTotalElements();
		model.addAttribute("isEnabledNum", isEnabledNum);

		map.put("EQ_type", 2);
		long isNotEnabledNum = gjtSpecialtyService
				.queryAllAndShare(user.getGjtOrg().getId(), map, pageRequst, specialtyIds).getTotalElements();
		model.addAttribute("isNotEnabledNum", isNotEnabledNum);

		map.remove("EQ_type");
		map.put("EQ_status", 1);
		long edit = gjtSpecialtyService.queryAllAndShare(user.getGjtOrg().getId(), map, pageRequst, specialtyIds)
				.getTotalElements();
		model.addAttribute("edit", edit);

		map.put("EQ_status", 2);
		long notPublish = gjtSpecialtyService.queryAllAndShare(user.getGjtOrg().getId(), map, pageRequst, specialtyIds)
				.getTotalElements();
		model.addAttribute("notPublish", notPublish);

		map.put("EQ_status", 3);
		long publish = gjtSpecialtyService.queryAllAndShare(user.getGjtOrg().getId(), map, pageRequst, specialtyIds)
				.getTotalElements();
		model.addAttribute("publish", publish);

		map.put("EQ_status", 4);
		long stop = gjtSpecialtyService.queryAllAndShare(user.getGjtOrg().getId(), map, pageRequst, specialtyIds)
				.getTotalElements();
		model.addAttribute("stop", stop);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnCreate", subject.isPermitted("/edumanage/specialty/list$create"));
		model.addAttribute("isBtnUpdate", subject.isPermitted("/edumanage/specialty/list$update"));
		model.addAttribute("isBtnDelete", subject.isPermitted("/edumanage/specialty/list$delete"));
		model.addAttribute("isBtnStop", subject.isPermitted("/edumanage/specialty/list$stop"));
		model.addAttribute("isBtnCopy", subject.isPermitted("/edumanage/specialty/list$copy"));
		model.addAttribute("isBtnView", subject.isPermitted("/edumanage/specialty/list$view"));

		return "edumanage/specialty/list";
	}

	@RequiresPermissions("/edumanage/specialty/list$view")
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		// GjtUserAccount user = (GjtUserAccount)
		// request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtSpecialty entity = gjtSpecialtyService.queryById(id);

		// String orgId = user.getGjtOrg().getId();

		// Map<String, String> employeeMap =
		// commonMapService.getEmployeeMap(orgId, EmployeeTypeEnum.辅导教师);
		// CourseCategory[] courseCategory = CourseCategory.values();
		// TermType[] termType = TermType.values();
		// ExamType[] examType = ExamType.values();

		// Map<String, String> courseMap = commonMapService.getCourseMap(orgId);

		List<Map<String, String>> module = gjtSpecialtyService.querySpecialtyCourse(id);// 专业对应课程模块
		model.addAttribute("list", module);

		List<GjtSpecialtyPlan> gjtSpecialtyPlans = entity.getGjtSpecialtyPlans();
		Map<Integer, List<GjtSpecialtyPlan>> planMap = new HashMap<Integer, List<GjtSpecialtyPlan>>();
		for (GjtSpecialtyPlan gjtSpecialtyPlan : gjtSpecialtyPlans) {
			int termTypeCode = Integer.valueOf(gjtSpecialtyPlan.getTermTypeCode());
			boolean exists = planMap.containsKey(termTypeCode);
			if (!exists) {
				List<GjtSpecialtyPlan> list = new ArrayList<GjtSpecialtyPlan>();
				list.add(gjtSpecialtyPlan);
				planMap.put(termTypeCode, list);
			} else {
				planMap.get(termTypeCode).add(gjtSpecialtyPlan);
			}
		}
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		model.addAttribute("pyccMap", pyccMap);
		List<GjtSpecialtyPlan> gjtSpecialtyPlan = gjtSpecialtyPlanService.findBySpecialtyId(id);
		Map<Integer, String> countMap = new HashMap<Integer, String>();
		int count = 0;
		for (GjtSpecialtyPlan plan : gjtSpecialtyPlan) {
			if (countMap.containsKey(plan.getTermTypeCode())) {
				count = Integer.parseInt(countMap.get(plan.getTermTypeCode())) + 1;
				countMap.put(plan.getTermTypeCode(), count + "");
			} else {
				countMap.put(plan.getTermTypeCode(), "1");
			}
		}
		model.addAttribute("gjtSpecialtyPlan", gjtSpecialtyPlan);
		model.addAttribute("countMap", countMap);
		// 课程列表
		model.addAttribute("entity", entity);
		// model.addAttribute("planMap", planMap);
		// model.addAttribute("employeeMap", employeeMap);
		// model.addAttribute("examType", examType);
		// model.addAttribute("courseCategory", courseCategory);
		// model.addAttribute("courseMap", courseMap);
		// model.addAttribute("termType", termType);
		model.addAttribute("action", "plan");
		Map<String, String> courseTypeMap = commonMapService.getDates("CourseType");// 课程模块
		model.addAttribute("courseTypeMap", courseTypeMap);
		Map<String, String> courseTypeMap2 = commonMapService.getIdNameMap("CourseType");// 课程模块
		model.addAttribute("courseTypeMap2", courseTypeMap2);
		// Map<String, String> zyxzMap =
		// commonMapService.getDates("SPECIALTY_CATEGORY");
		// model.addAttribute("zyxzMap", zyxzMap);
		// Map<String, String> xkMap =
		// commonMapService.getDates("ProfessionalType");// 学科
		// model.addAttribute("xkMap", xkMap);
		// Map<String, String> syhyMap =
		// commonMapService.getDates("TRADE_CODE");
		// model.addAttribute("syhyMap", syhyMap);
		model.addAttribute("subjectMap", commonMapService.getExsubjectMap()); // 学科
		model.addAttribute("categoryMap", commonMapService.getExsubjectkindMap(entity.getSubject())); // 门类
		model.addAttribute("studentTypeMap", commonMapService.getDates("StudentType2")); // 学生类型

		return "edumanage/specialty/view";
	}

	@RequiresPermissions("/edumanage/specialty/list$create")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("item", new GjtSpecialty());
		// Map<String, String> schoolInfoMap =
		// commonMapService.getOrgMapBy(user.getId(), false);// 学校
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		// Map<String, String> courseTypeMap =
		// commonMapService.getIdNameMap("CourseType");
		// Map<String, String> xkMap =
		// commonMapService.getDates("ProfessionalType");// 学科
		// model.addAttribute("xkMap", xkMap);
		// model.addAttribute("courseTypeMap", courseTypeMap);
		model.addAttribute("pyccMap", pyccMap);
		// model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("action", "create");

		// Map<String, String> syhyMap =
		// commonMapService.getDates("TRADE_CODE");
		// model.addAttribute("syhyMap", syhyMap);

		// Map<String, String> zyxzMap =
		// commonMapService.getDates("SPECIALTY_CATEGORY");
		// model.addAttribute("zyxzMap", zyxzMap);

		model.addAttribute("subjectMap", commonMapService.getExsubjectMap()); // 学科
		model.addAttribute("studentTypeMap", commonMapService.getDates("StudentType2")); // 学生类型
		model.addAttribute("specialtyBaseMap", commonMapService.getSpecialtyBaseMap(user.getGjtOrg().getId()));

		return "edumanage/specialty/form";
	}

	@RequiresPermissions("/edumanage/specialty/list$create")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback create(GjtSpecialty item, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		// GjtSchoolInfo gjtSchoolInfo =
		// gjtSchoolInfoService.queryById(gjtSchoolInfoId);
		// GjtOrg gjtOrg = gjtOrgService.queryById(gjtSchoolInfoId);
		Feedback feedback = new Feedback(true, "创建成功");

		try {
			item.setSpecialtyId(UUIDUtils.random());

			GjtSpecialtyBase specialtyBase = gjtSpecialtyBaseService.queryById(item.getSpecialtyBaseId());
			item.setZyh(specialtyBase.getSpecialtyCode());
			item.setZymc(specialtyBase.getSpecialtyName());
			item.setPycc(specialtyBase.getSpecialtyLayer().toString());

			if ("1".equals(user.getGjtOrg().getOrgType())) {
				item.setXxId(user.getGjtOrg().getId());
			} else {
				if (user.getGjtOrg().getParentGjtOrg() != null) {
					item.setXxId(user.getGjtOrg().getParentGjtOrg().getId());
				} else {
					item.setXxId(user.getGjtOrg().getId());
				}
			}

			item.setYxId(user.getGjtOrg().getId());
			item.setGjtOrg(user.getGjtOrg());
			item.setOrgCode(user.getGjtOrg().getCode());
			item.setCreatedDt(DateUtils.getNowTime());
			item.setIsDeleted("N");
			item.setIsEnabled("1");
			item.setStatus(1);
			item.setVersion(BigDecimal.valueOf(3));
			gjtSpecialtyService.saveEntity(item);

			feedback = new Feedback(true, item.getSpecialtyId());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "创建失败");
		}
		// redirectAttributes.addFlashAttribute("feedback", feedback);
		// return "redirect:/edumanage/specialty/list";
		return feedback;
	}

	@RequiresPermissions("/edumanage/specialty/list$update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		// Map<String, String> schoolInfoMap =
		// commonMapService.getOrgMapBy(user.getId(), false);// 学校
		GjtSpecialty gjtSpecialty = gjtSpecialtyService.queryById(id);
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		// Map<String, String> courseTypeMap =
		// commonMapService.getIdNameMap("CourseType");
		// List<Map<String, String>> list =
		// gjtSpecialtyService.querySpecialtyCourse(id);// 专业对应课程
		// Map<String, String> zylbMap =
		// commonMapService.getDates("ProfessionalType");// 学科
		// model.addAttribute("zylbMap", zylbMap);
		// model.addAttribute("courseTypeMap", courseTypeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("item", gjtSpecialty);
		// model.addAttribute("list", list);
		// model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("action", "update");

		// Map<String, String> xkMap =
		// commonMapService.getDates("ProfessionalType");// 学科
		// model.addAttribute("xkMap", xkMap);
		// Map<String, String> syhyMap =
		// commonMapService.getDates("TRADE_CODE");
		// model.addAttribute("syhyMap", syhyMap);
		/*
		 * String[] syhys = null; if (gjtSpecialty.getSyhy() != null) syhys =
		 * gjtSpecialty.getSyhy().split(","); model.addAttribute("syhys",
		 * syhys);
		 */
		// Map<String, String> zyxzMap =
		// commonMapService.getDates("SPECIALTY_CATEGORY");
		// model.addAttribute("zyxzMap", zyxzMap);
		model.addAttribute("subjectMap", commonMapService.getExsubjectMap()); // 学科
		model.addAttribute("categoryMap", commonMapService.getExsubjectkindMap(gjtSpecialty.getSubject())); // 门类
		model.addAttribute("studentTypeMap", commonMapService.getDates("StudentType2")); // 学生类型
		model.addAttribute("specialtyBaseMap", commonMapService.getSpecialtyBaseMap(user.getGjtOrg().getId()));

		return "edumanage/specialty/form";
	}

	@RequiresPermissions("/edumanage/specialty/list$update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(GjtSpecialty item, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		// GjtSchoolInfo gjtSchoolInfo =
		// gjtSchoolInfoService.queryById(gjtSchoolInfoId);
		// GjtOrg gjtOrg = gjtOrgService.queryById(gjtSchoolInfoId);
		GjtSpecialty gjtSpecialty = gjtSpecialtyService.queryById(item.getSpecialtyId());
		// String syhy = request.getParameter("syhy");
		try {
			// gjtSpecialty.setSpecialtyBaseId(item.getSpecialtyBaseId());
			// gjtSpecialty.setPycc(item.getPycc());
			// gjtSpecialty.setSpecialtyCategory(item.getSpecialtyCategory());
			// gjtSpecialty.setGjtSchoolInfo(gjtSchoolInfo);
			// gjtSpecialty.setGjtOrg(gjtOrg);
			gjtSpecialty.setUpdatedDt(DateUtils.getNowTime());
			gjtSpecialty.setUpdatedBy(user.getId());
			// gjtSpecialty.setRuleCode(item.getRuleCode());
			// gjtSpecialty.setZyh(item.getZyh());
			// gjtSpecialty.setZymc(item.getZymc());
			// gjtSpecialty.setZxf(item.getZxf());
			// gjtSpecialty.setZdbyxf(item.getZdbyxf());
			// gjtSpecialty.setBxxf(item.getBxxf());
			// gjtSpecialty.setXxxf(item.getXxxf());
			// gjtSpecialty.setZylb(item.getZylb());
			gjtSpecialty.setType(item.getType());
			// gjtSpecialty.setSyhy(syhy);
			// gjtSpecialty.setSyhy(item.getSyhy());
			gjtSpecialty.setZyfm(item.getZyfm());
			gjtSpecialty.setXslx(item.getXslx());
			gjtSpecialty.setSubject(item.getSubject());
			gjtSpecialty.setCategory(item.getCategory());
			gjtSpecialty.setXz(item.getXz());
			gjtSpecialtyService.updateEntity(gjtSpecialty);

			feedback = new Feedback(true, item.getSpecialtyId());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "更新失败");
		}
		// redirectAttributes.addFlashAttribute("feedback", feedback);
		// return "redirect:/edumanage/specialty/list";
		return feedback;
	}

	@RequiresPermissions("/edumanage/specialty/list$delete")
	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids) throws IOException {
		Feedback fb = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				gjtSpecialtyService.deleteById(selectedIds); // 删除专业
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				fb = new Feedback(false, "删除失败");
			}
		}
		return fb;
	}

	@RequestMapping(value = "plan/delete")
	public @ResponseBody Feedback deletePlan(String ids) throws IOException {
		Feedback fb = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				gjtSpecialtyPlanService.delete(selectedIds);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				fb = new Feedback(false, "删除失败");
			}
		}
		return fb;
	}

	@RequestMapping(value = "plan/{id}", method = RequestMethod.GET)
	public String planForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtSpecialty entity = gjtSpecialtyService.queryById(id);

		// String orgId = user.getGjtOrg().getId();

		// Map<String, String> employeeMap =
		// commonMapService.getEmployeeMap(orgId, EmployeeTypeEnum.辅导教师);
		// CourseCategory[] courseCategory = CourseCategory.values();
		// TermType[] termType = TermType.values();
		// ExamType[] examType = ExamType.values();

		// Map<String, String> courseMap = commonMapService.getCourseMap(orgId);

		List<Map<String, String>> module = gjtSpecialtyService.querySpecialtyCourse(id);// 专业对应课程模块
		model.addAttribute("list", module);

		List<GjtSpecialtyPlan> gjtSpecialtyPlans = entity.getGjtSpecialtyPlans();
		Map<Integer, List<GjtSpecialtyPlan>> planMap = new HashMap<Integer, List<GjtSpecialtyPlan>>();
		for (GjtSpecialtyPlan gjtSpecialtyPlan : gjtSpecialtyPlans) {
			int termTypeCode = Integer.valueOf(gjtSpecialtyPlan.getTermTypeCode());
			boolean exists = planMap.containsKey(termTypeCode);
			if (!exists) {
				List<GjtSpecialtyPlan> list = new ArrayList<GjtSpecialtyPlan>();
				list.add(gjtSpecialtyPlan);
				planMap.put(termTypeCode, list);
			} else {
				planMap.get(termTypeCode).add(gjtSpecialtyPlan);
			}
		}
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		model.addAttribute("pyccMap", pyccMap);
		List<GjtSpecialtyPlan> gjtSpecialtyPlan = gjtSpecialtyPlanService.findBySpecialtyId(id);
		Map<Integer, String> countMap = new HashMap<Integer, String>();
		int count = 0;
		for (GjtSpecialtyPlan plan : gjtSpecialtyPlan) {
			if (countMap.containsKey(plan.getTermTypeCode())) {
				count = Integer.parseInt(countMap.get(plan.getTermTypeCode())) + 1;
				countMap.put(plan.getTermTypeCode(), count + "");
			} else {
				countMap.put(plan.getTermTypeCode(), "1");
			}
		}
		model.addAttribute("gjtSpecialtyPlan", gjtSpecialtyPlan);
		model.addAttribute("countMap", countMap);
		// 课程列表
		model.addAttribute("entity", entity);
		// model.addAttribute("planMap", planMap);
		// model.addAttribute("employeeMap", employeeMap);
		// model.addAttribute("examType", examType);
		// model.addAttribute("courseCategory", courseCategory);
		// model.addAttribute("courseMap", courseMap);
		// model.addAttribute("termType", termType);
		model.addAttribute("action", "plan");
		// Map<String, String> courseTypeMap =
		// commonMapService.getCourseTypeMap(orgId);
		// model.addAttribute("courseTypeMap", courseTypeMap);
		Map<String, String> courseTypeMap = commonMapService.getDates("CourseType");// 课程模块
		model.addAttribute("courseTypeMap", courseTypeMap);
		Map<String, String> courseTypeMap2 = commonMapService.getIdNameMap("CourseType");// 课程模块
		model.addAttribute("courseTypeMap2", courseTypeMap2);
		model.addAttribute("specialtyBaseMap", commonMapService.getSpecialtyBaseMap(user.getGjtOrg().getId()));
		// Map<String, String> zyxzMap =
		// commonMapService.getDates("SPECIALTY_CATEGORY");
		// model.addAttribute("zyxzMap", zyxzMap);
		// Map<String, String> xkMap =
		// commonMapService.getDates("ProfessionalType");// 学科
		// model.addAttribute("xkMap", xkMap);
		// Map<String, String> syhyMap =
		// commonMapService.getDates("TRADE_CODE");
		// model.addAttribute("syhyMap", syhyMap);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnSetRule", subject.isPermitted("/edumanage/specialty/list$setRule"));

		return "edumanage/specialty/plan";
	}

	@RequestMapping(value = "savePlan", method = RequestMethod.POST)
	@ResponseBody
	public Feedback savePlan(RedirectAttributes redirectAttributes, HttpServletRequest request, GjtSpecialty item,
			String courseType, String score, String totalScore, String crtvuScore, String[] courseIds,
			String[] courseTypeIds, Integer[] termTypeCodes, Integer[] courseCategorys, Integer[] coursetypes,
			Integer[] examUnits, Double[] scores, Integer[] hours) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "添加成功");

		try {
			GjtSpecialty gjtSpecialty = gjtSpecialtyService.queryById(item.getSpecialtyId());
			gjtSpecialty.setZxf(item.getZxf());
			gjtSpecialty.setZdbyxf(item.getZdbyxf());
			gjtSpecialty.setBxxf(item.getBxxf());
			gjtSpecialty.setXxxf(item.getXxxf());
			gjtSpecialty.setZyddksxf(item.getZyddksxf());
			if (gjtSpecialty.getStatus() == 1) {
				gjtSpecialty.setStatus(2);
			}
			gjtSpecialty.setUpdatedDt(DateUtils.getNowTime());
			gjtSpecialty.setUpdatedBy(user.getId());
			gjtSpecialtyService.updateEntity(gjtSpecialty);

			if (courseType != null && score != null && totalScore != null) {
				gjtSpecialtyService.deleteSpecialtyModule(item.getSpecialtyId());
				String[] split = courseType.split(",");
				String[] split2 = score.split(",");
				String[] totalScores = totalScore.split(",");
				String[] crtvuScores = crtvuScore.split(",");
				for (int i = 0; i < split.length; i++) {
					if (split[i] != null && split2.length > i && split2[i] != null && totalScores.length > i
							&& totalScores[i] != null) {
						// TblSysData tblSysData =
						// tblSysDataService.queryById(split[i]);
						GjtSpecialtyModuleLimit sm = new GjtSpecialtyModuleLimit();
						sm.setId(UUIDUtils.random());
						sm.setCreatedDt(DateUtils.getNowTime());
						sm.setCreatedBy(user.getId());
						sm.setIsDeleted("N");
						// sm.setTblSysData(tblSysData);
						sm.setModuleId(split[i]);
						sm.setGjtSpecialty(item);
						sm.setScore(split2[i]);
						sm.setTotalScore(totalScores[i]);
						sm.setCrtvuScore(Double.parseDouble(crtvuScores[i]));
						gjtSpecialtyService.updateSpecialtyModule(sm);
					}
				}
			}

			List<GjtSpecialtyPlan> planList = new ArrayList<GjtSpecialtyPlan>();
			Set<String> existsCourse = new HashSet<String>();
			if (courseIds != null && courseIds.length > 0) {
				for (int i = 0; i < courseIds.length; i++) {
					// 去重
					if (existsCourse.contains(courseIds[i])) {
						continue;
					} else {
						existsCourse.add(courseIds[i]);
					}

					if (courseTypeIds != null && courseTypeIds.length > i && termTypeCodes != null
							&& termTypeCodes.length > i && courseCategorys != null && courseCategorys.length > i
							&& coursetypes != null && coursetypes.length > i && examUnits != null
							&& examUnits.length > i && scores != null && scores.length > i && hours != null
							&& hours.length > i) {
						GjtSpecialtyPlan plan = new GjtSpecialtyPlan();
						plan.setId(UUIDUtils.random());
						plan.setXxId(user.getGjtOrg().getId());
						plan.setCreatedBy(user.getId());
						plan.setCreatedDt(new Date());
						plan.setSpecialtyId(item.getSpecialtyId());
						plan.setCourseCategory(courseCategorys[i]);
						plan.setCoursetype(coursetypes[i]);
						plan.setCourseId(courseIds[i]);
						plan.setCourseTypeId(courseTypeIds[i]);
						plan.setExamUnit(examUnits[i]);
						plan.setHours(hours[i]);
						plan.setScore(scores[i]);
						plan.setTermTypeCode(termTypeCodes[i]);

						planList.add(plan);
					}
				}
			}

			gjtSpecialtyPlanService.deleteBySpecialtyId(item.getSpecialtyId());
			gjtSpecialtyPlanService.insert(planList);
			feedback = new Feedback(true, "添加成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "添加失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return feedback;
	}

	@RequestMapping(value = "updatePlan", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updatePlan(RedirectAttributes redirectAttributes, HttpServletRequest request,
			GjtSpecialtyPlan plan) {
		// GjtUserAccount user = (GjtUserAccount)
		// request.getSession().getAttribute(WebConstants.CURRENT_USER);
		// plan.setXxId(user.getGjtOrg().getId());

		GjtSpecialtyPlan updatePlan = gjtSpecialtyPlanService.queryBy(plan.getId());
		Feedback feedback = new Feedback(true, "更新成功");
		/*
		 * String[] textbookIds = request.getParameterValues("textbookIds");
		 * List<GjtTextbookPlanOwnership> gjtTextbookPlanOwnershipList = new
		 * ArrayList<GjtTextbookPlanOwnership>(); if (textbookIds != null) {
		 * gjtTextbookPlanOwnershipService.deleteBySpecialtyPlanId(plan.getId())
		 * ; for (String textbookId : textbookIds) { GjtTextbookPlanOwnership
		 * gjtTextbookPlanOwnership = new GjtTextbookPlanOwnership();
		 * gjtTextbookPlanOwnership.setTextbookId(textbookId);
		 * gjtTextbookPlanOwnership.setTextbookType(gjtTextbookService.findOne(
		 * textbookId).getTextbookType() + "");
		 * gjtTextbookPlanOwnershipList.add(gjtTextbookPlanOwnership); } }
		 */

		try {
			updatePlan.setCourseTypeId(plan.getCourseTypeId());
			updatePlan.setCourseCategory(plan.getCourseCategory());
			updatePlan.setCourseId(plan.getCourseId());

			/*
			 * updatePlan.setHours(plan.getHours());
			 * updatePlan.setScore(plan.getScore());
			 * updatePlan.setExamType(plan.getExamType());
			 * updatePlan.setStudyRatio(plan.getStudyRatio());
			 * updatePlan.setExamRatio(plan.getExamRatio());
			 * updatePlan.setInstructions(plan.getInstructions());
			 * updatePlan.setReplaceCourseId(plan.getReplaceCourseId());
			 * updatePlan.setSubjectId(plan.getSubjectId());
			 */
			// updatePlan.setGjtTextbookPlanOwnershipList(gjtTextbookPlanOwnershipList);
			gjtSpecialtyPlanService.update(updatePlan);
			feedback = new Feedback(true, "更新成功", plan);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return feedback;
	}

	@RequiresPermissions("/edumanage/specialty/list$delete")
	@RequestMapping(value = "removePlan", method = RequestMethod.POST)
	@ResponseBody
	public Feedback removePlan(String planId, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "删除成功");
		try {
			if (StringUtils.isNotBlank(planId)) {
				gjtSpecialtyPlanService.delete(planId);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "删除失败");
		}
		return feedback;
	}

	// 校验编码是否存在
	@RequestMapping(value = "checkLogin")
	@ResponseBody
	public Feedback checkLogin(String zyh) throws IOException {
		PageRequest pageRequst = new PageRequest(0, 2);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_zyh", zyh);
		Page<GjtSpecialty> gjtSpecialty = gjtSpecialtyService.queryPage(searchParams, pageRequst);
		Boolean boolean1 = true;
		if (gjtSpecialty.getTotalElements() == 0) {
			boolean1 = false;
		}
		Feedback fe = new Feedback(boolean1, "");
		fe.setSuccessful(boolean1);
		return fe;
	}

	// 校验专业规则号是否存在
	@RequestMapping(value = "checkRuleCode")
	@ResponseBody
	public Feedback checkRuleCode(String ruleCode, HttpServletRequest request) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = new PageRequest(0, 2);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_ruleCode", ruleCode);
		searchParams.put("EQ_xxId", user.getGjtOrg().getId());
		Page<GjtSpecialty> gjtSpecialty = gjtSpecialtyService.queryPage(searchParams, pageRequst);
		Boolean boolean1 = true;
		if (gjtSpecialty.getTotalElements() == 0) {
			boolean1 = false;
		}
		Feedback fe = new Feedback(boolean1, "");
		return fe;
	}

	// 分享功能
	@RequiresPermissions("/edumanage/specialty/list$share")
	@RequestMapping(value = "share/{id}", method = RequestMethod.GET)
	public String shareForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		// GjtUserAccount user = (GjtUserAccount)
		// request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtSpecialty gjtSpecialty = gjtSpecialtyService.queryById(id);
		Map<String, String> orgAllNotBSMap = commonMapService.getXxmcDatesExceptBS(gjtSpecialty.getGjtOrg().getId());
		model.addAttribute("orgAllNotBSMap", orgAllNotBSMap);
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("entity", gjtSpecialty);
		model.addAttribute("action", "share");
		return "edumanage/specialty/share";
	}

	@RequiresPermissions("/edumanage/specialty/list$share")
	@RequestMapping(value = "share", method = RequestMethod.POST)
	public String share(@Valid @ModelAttribute("entity") GjtSpecialtyOwnership entity, String[] orgId, Model model,
			RedirectAttributes redirectAttributes) {
		boolean result = true;
		entity.setId(UUID.randomUUID().toString());
		try {
			gjtSpecialtyShareService.saveGjtShare(entity);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = false;
		}
		model.addAttribute("result", result);
		model.addAttribute("entity", gjtSpecialtyService.queryById(entity.getSpecialtyId()));
		model.addAttribute("action", "succesful");
		return "edumanage/specialty/share";
		// return "redirect:/edumanage/course/list";
	}

	@RequestMapping(value = "courseForm", method = RequestMethod.GET)
	public String courseForm(Model model, @RequestParam("termTypeCode") String termTypeCode,
			@RequestParam(value = "specialtyId", required = true) String specialtyId) {

		Map<String, String> courseTypeMap = commonMapService.getDates("CourseType");
		model.addAttribute("courseTypeMap", courseTypeMap);
		model.addAttribute("action", "savePlan");
		return "edumanage/specialty/courseForm";
	}

	@RequiresPermissions("/edumanage/specialty/list$update")
	@RequestMapping(value = "courseFormUpdate", method = RequestMethod.GET)
	public String courseFormUpdate(Model model, @RequestParam("termTypeCode") String termTypeCode,
			@RequestParam(value = "specialtyId", required = true) String specialtyId, @RequestParam("id") String id) {
		GjtSpecialtyPlan gjtSpecialtyPlan = gjtSpecialtyPlanService.queryBy(id);

		/*
		 * List<GjtTextbookPlanOwnership> gjtTextbookPlanOwnershipList =
		 * gjtTextbookPlanOwnershipService
		 * .findBySpecialtyPlanIdAndTextbookType(id, "1"); List<GjtTextbook>
		 * GjtTextbookList = new ArrayList<GjtTextbook>(); if
		 * (gjtTextbookPlanOwnershipList != null) { for
		 * (GjtTextbookPlanOwnership gjtTextbookPlanOwnership :
		 * gjtTextbookPlanOwnershipList) {
		 * GjtTextbookList.add(gjtTextbookService.findOne(
		 * gjtTextbookPlanOwnership.getTextbookId())); } }
		 * List<GjtTextbookPlanOwnership> gjtTextbookPlanOwnershipList2 =
		 * gjtTextbookPlanOwnershipService
		 * .findBySpecialtyPlanIdAndTextbookType(id, "2"); List<GjtTextbook>
		 * GjtTextbookList2 = new ArrayList<GjtTextbook>(); if
		 * (gjtTextbookPlanOwnershipList2 != null) { for
		 * (GjtTextbookPlanOwnership gjtTextbookPlanOwnership :
		 * gjtTextbookPlanOwnershipList2) {
		 * GjtTextbookList2.add(gjtTextbookService.findOne(
		 * gjtTextbookPlanOwnership.getTextbookId())); } }
		 * model.addAttribute("GjtTextbookList", GjtTextbookList);
		 * model.addAttribute("GjtTextbookList2", GjtTextbookList2);
		 */
		// model.addAttribute("gjtTextbookPlanOwnership2",
		// gjtTextbookPlanOwnership2);

		model.addAttribute("entity", gjtSpecialtyPlan);
		model.addAttribute("action", "updatePlan");
		Map<String, String> courseTypeMap = commonMapService.getDates("CourseType");// 课程模块
		model.addAttribute("courseTypeMap", courseTypeMap);
		/*
		 * Map<String, String> ksfsMap =
		 * commonMapService.getDates("ExaminationMode");// 考试方式
		 * model.addAttribute("ksfsMap", ksfsMap);
		 */
		return "edumanage/specialty/courseForm";
	}

	@RequestMapping(value = "courseFormView", method = RequestMethod.GET)
	public String courseFormView(Model model, @RequestParam("id") String id) {
		GjtSpecialtyPlan gjtSpecialtyPlan = gjtSpecialtyPlanService.queryBy(id);

		/*
		 * List<GjtTextbookPlanOwnership> gjtTextbookPlanOwnershipList =
		 * gjtTextbookPlanOwnershipService
		 * .findBySpecialtyPlanIdAndTextbookType(id, "1"); List<GjtTextbook>
		 * GjtTextbookList = new ArrayList<GjtTextbook>(); if
		 * (gjtTextbookPlanOwnershipList != null) { for
		 * (GjtTextbookPlanOwnership gjtTextbookPlanOwnership :
		 * gjtTextbookPlanOwnershipList) {
		 * GjtTextbookList.add(gjtTextbookService.findOne(
		 * gjtTextbookPlanOwnership.getTextbookId())); } }
		 * List<GjtTextbookPlanOwnership> gjtTextbookPlanOwnershipList2 =
		 * gjtTextbookPlanOwnershipService
		 * .findBySpecialtyPlanIdAndTextbookType(id, "2"); List<GjtTextbook>
		 * GjtTextbookList2 = new ArrayList<GjtTextbook>(); if
		 * (gjtTextbookPlanOwnershipList2 != null) { for
		 * (GjtTextbookPlanOwnership gjtTextbookPlanOwnership :
		 * gjtTextbookPlanOwnershipList2) {
		 * GjtTextbookList2.add(gjtTextbookService.findOne(
		 * gjtTextbookPlanOwnership.getTextbookId())); } }
		 * model.addAttribute("GjtTextbookList", GjtTextbookList);
		 * model.addAttribute("GjtTextbookList2", GjtTextbookList2);
		 */

		model.addAttribute("entity", gjtSpecialtyPlan);
		model.addAttribute("action", "viewPlan");
		Map<String, String> courseTypeMap = commonMapService.getDates("CourseType");// 课程模块
		model.addAttribute("courseTypeMap", courseTypeMap);
		/*
		 * Map<String, String> ksfsMap =
		 * commonMapService.getDates("ExaminationMode");// 考试方式
		 * model.addAttribute("ksfsMap", ksfsMap)
		 */;
		return "edumanage/specialty/courseForm";
	}

	@RequestMapping(value = "choiceCourseList", method = RequestMethod.GET)
	public String choiceCourseList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "3") int pageSize, Model model,
			@RequestParam("termTypeCode") String termTypeCode,
			@RequestParam(value = "specialtyId", required = true) String specialtyId,
			@RequestParam("courseTypeIds") String courseTypeIds, @RequestParam("courseIds") String courseIds,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Sort sort = new Sort(Direction.DESC, "createdDt");

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, sort);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		List<GjtCourseOwnership> gjtCourseOwnershipList = gjtShareService.findByOrgId(user.getGjtOrg().getId());
		List<String> coursIds = new ArrayList<String>();
		if (gjtCourseOwnershipList.size() != 0) {
			for (GjtCourseOwnership gjtCourseOwnership : gjtCourseOwnershipList) {
				if (gjtCourseOwnership != null) {
					coursIds.add(gjtCourseOwnership.getCourseId());
				}
			}

		}

		// Map<String, String> dates = commonMapService.getDates("CourseType");
		// Map<String, String> courseTypeMap = new HashMap<String, String>();
		// List<String> courseTypes = new ArrayList<String>();
		// List<GjtSpecialtyModuleLimit> specialtyModuleList =
		// gjtSpecialtyService.querySpecialtyModuleList(specialtyId);
		/*
		 * if (specialtyModuleList != null && specialtyModuleList.size() > 0) {
		 * for (GjtSpecialtyModuleLimit specialtyModule : specialtyModuleList) {
		 * if (specialtyModule.getTblSysData() != null) {
		 * courseTypes.add(specialtyModule.getTblSysData().getCode());
		 * 
		 * //只选取专业里设置的课程模块 if
		 * (dates.containsKey(specialtyModule.getTblSysData().getCode())) {
		 * courseTypeMap.put(specialtyModule.getTblSysData().getCode(),
		 * dates.get(specialtyModule.getTblSysData().getCode())); } } } } else {
		 * courseTypes.add("-1"); }
		 */
		/*
		 * String[] split = courseTypeIds.split(","); if (split != null &&
		 * split.length > 0) { for (String courseTypeId : split) { TblSysData
		 * tblSysData = tblSysDataService.queryById(courseTypeId); if
		 * (tblSysData != null) { courseTypes.add(tblSysData.getCode());
		 * 
		 * // 只选取专业里设置的课程模块 if (dates.containsKey(tblSysData.getCode())) {
		 * courseTypeMap.put(tblSysData.getCode(),
		 * dates.get(tblSysData.getCode())); } } } } else {
		 * courseTypes.add("-1"); } searchParams.put("courseTypes",
		 * courseTypes);
		 */

		List<String> notIncoursIds = new ArrayList<String>();
		/*
		 * List<GjtSpecialtyPlan> gjtSpecialtyPlanList =
		 * gjtSpecialtyPlanService.findBySpecialtyId(specialtyId); if
		 * (gjtSpecialtyPlanList != null && gjtSpecialtyPlanList.size() > 0) {
		 * for (GjtSpecialtyPlan gjtSpecialtyPlan : gjtSpecialtyPlanList) {
		 * notIncoursIds.add(gjtSpecialtyPlan.getCourseId()); } }
		 */
		if (courseIds != null && !"".equals(courseIds)) {
			String[] split2 = courseIds.split(",");
			for (String courseId : split2) {
				notIncoursIds.add(courseId);
			}
		}
		searchParams.put("notIncoursIds", notIncoursIds);

		Page<GjtCourse> pageInfo = gjtCourseService.queryAllAndShare(user.getGjtOrg().getId(), searchParams, pageRequst,
				coursIds);

		model.addAttribute("user", user);
		model.addAttribute("pageInfo", pageInfo);

		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次
		Map<String, String> courseTypeMap = commonMapService.getDates("CourseType");// 课程模块
		model.addAttribute("courseTypeMap", courseTypeMap); // 课程类型
		model.addAttribute("courseCategoryMap", commonMapService.getDatesBy("CourseCategoryInfo")); // 课程类型
		// model.addAttribute("syhyMap",
		// commonMapService.getDates("TRADE_CODE")); // 所属行业
		model.addAttribute("syzyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId())); // 所属专业
		model.addAttribute("wsjxzkMap", commonMapService.getDates("OnlineTeaching")); // 教学方式
		model.addAttribute("courseNatureMap", commonMapService.getDates("CourseNature")); // 课程性质

		return "edumanage/specialty/choiceCourseList";
	}

	@RequestMapping(value = "choiceTextbookList/{id}", method = RequestMethod.GET)
	public String choiceTextbookList(@PathVariable("id") String textbookType, String courseId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "3") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		searchParams.put("EQ_textbookType", textbookType);
		searchParams.put("EQ_courseId", courseId);
		Page<GjtTextbook> pageInfo = gjtTextbookService.findAll(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		return "edumanage/specialty/choiceTextbookList";
	}

	@RequestMapping(value = "choiceExamList", method = RequestMethod.GET)
	public String choiceExamList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "3") int pageSize, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String examType = request.getParameter("examType");
		/** 过渡版本代码 start */
		if (null == user || null != request.getParameter("userid")) {
			user = new GjtUserAccount();
			user.setId(request.getParameter("userid"));
			GjtOrg org = new GjtOrg();
			org.setId(request.getParameter("xxid"));
			user.setGjtOrg(org);
			request.getSession().setAttribute(WebConstants.CURRENT_USER, user);
		}
		/** 过渡版本代码 end */
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		// if (null == searchParams || null == searchParams.get("EQ_type")
		// || "".equals(searchParams.get("EQ_type").toString())) {
		// searchParams.put("EQ_type", "2");
		//
		// }
		searchParams.put("EQ_type", examType);
		Page<GjtExamSubjectNew> pageInfo = gjtExamSubjectNewService.queryAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		List<GjtExamSubjectNew> list = Lists.newArrayList(pageInfo.iterator());
		List<String> subjectCode = new ArrayList<String>();
		List<String> subjectId = new ArrayList<String>();
		for (GjtExamSubjectNew gjtExamSubjectNew : list) {
			subjectCode.add(gjtExamSubjectNew.getSubjectCode());
			subjectId.add(gjtExamSubjectNew.getSubjectId());
		}
		Map<String, String> countMap = gjtExamSubjectNewService.plansCountBySubject(subjectCode);
		Map<String, String> courseCodeMap = gjtExamSubjectNewService.queryTeachPlanBySubject(subjectId);
		model.addAttribute("exam_type", searchParams.get("EQ_type"));
		model.addAttribute("countMap", countMap);
		model.addAttribute("courseCodeMap", courseCodeMap);
		model.addAttribute("user", user);
		model.addAttribute("pageInfo", pageInfo);

		return "edumanage/specialty/choiceExamList";
	}

	@RequestMapping(value = "publish/{id}", method = RequestMethod.GET)
	public String publishForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		// GjtUserAccount user = (GjtUserAccount)
		// request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtSpecialty entity = gjtSpecialtyService.queryById(id);

		// String orgId = user.getGjtOrg().getId();

		// Map<String, String> employeeMap =
		// commonMapService.getEmployeeMap(orgId, EmployeeTypeEnum.辅导教师);
		// CourseCategory[] courseCategory = CourseCategory.values();
		// TermType[] termType = TermType.values();
		// ExamType[] examType = ExamType.values();

		// Map<String, String> courseMap = commonMapService.getCourseMap(orgId);

		List<Map<String, String>> module = gjtSpecialtyService.querySpecialtyCourse(id);// 专业对应课程模块
		model.addAttribute("list", module);

		List<GjtSpecialtyPlan> gjtSpecialtyPlans = entity.getGjtSpecialtyPlans();
		Map<Integer, List<GjtSpecialtyPlan>> planMap = new HashMap<Integer, List<GjtSpecialtyPlan>>();
		for (GjtSpecialtyPlan gjtSpecialtyPlan : gjtSpecialtyPlans) {
			int termTypeCode = Integer.valueOf(gjtSpecialtyPlan.getTermTypeCode());
			boolean exists = planMap.containsKey(termTypeCode);
			if (!exists) {
				List<GjtSpecialtyPlan> list = new ArrayList<GjtSpecialtyPlan>();
				list.add(gjtSpecialtyPlan);
				planMap.put(termTypeCode, list);
			} else {
				planMap.get(termTypeCode).add(gjtSpecialtyPlan);
			}
		}
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		model.addAttribute("pyccMap", pyccMap);
		List<GjtSpecialtyPlan> gjtSpecialtyPlan = gjtSpecialtyPlanService.findBySpecialtyId(id);
		Map<Integer, String> countMap = new HashMap<Integer, String>();
		int count = 0;
		for (GjtSpecialtyPlan plan : gjtSpecialtyPlan) {
			if (countMap.containsKey(plan.getTermTypeCode())) {
				count = Integer.parseInt(countMap.get(plan.getTermTypeCode())) + 1;
				countMap.put(plan.getTermTypeCode(), count + "");
			} else {
				countMap.put(plan.getTermTypeCode(), "1");
			}
		}
		model.addAttribute("gjtSpecialtyPlan", gjtSpecialtyPlan);
		model.addAttribute("countMap", countMap);
		// 课程列表
		model.addAttribute("entity", entity);
		// model.addAttribute("planMap", planMap);
		// model.addAttribute("employeeMap", employeeMap);
		// model.addAttribute("examType", examType);
		// model.addAttribute("courseCategory", courseCategory);
		// model.addAttribute("courseMap", courseMap);
		// model.addAttribute("termType", termType);
		model.addAttribute("action", "plan");
		// Map<String, String> courseTypeMap =
		// commonMapService.getCourseTypeMap(orgId);
		// model.addAttribute("courseTypeMap", courseTypeMap);
		Map<String, String> courseTypeMap = commonMapService.getDates("CourseType");// 课程模块
		model.addAttribute("courseTypeMap", courseTypeMap);
		Map<String, String> courseTypeMap2 = commonMapService.getIdNameMap("CourseType");// 课程模块
		model.addAttribute("courseTypeMap2", courseTypeMap2);
		// Map<String, String> zyxzMap =
		// commonMapService.getDates("SPECIALTY_CATEGORY");
		// model.addAttribute("zyxzMap", zyxzMap);
		// Map<String, String> xkMap =
		// commonMapService.getDates("ProfessionalType");// 学科
		// model.addAttribute("xkMap", xkMap);
		// Map<String, String> syhyMap =
		// commonMapService.getDates("TRADE_CODE");
		// model.addAttribute("syhyMap", syhyMap);

		model.addAttribute("subjectMap", commonMapService.getExsubjectMap()); // 学科
		model.addAttribute("categoryMap", commonMapService.getExsubjectkindMap(entity.getSubject())); // 门类
		model.addAttribute("studentTypeMap", commonMapService.getDates("StudentType2")); // 学生类型

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnPublish", subject.isPermitted("/edumanage/specialty/list$publish"));

		return "edumanage/specialty/publish";
	}

	@RequestMapping(value = "publish", method = RequestMethod.POST)
	public String publish(String id, HttpServletRequest request, RedirectAttributes redirectAttributes)
			throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "发布成功");
		try {
			GjtSpecialty gjtSpecialty = gjtSpecialtyService.queryById(id);
			gjtSpecialty.setStatus(3);
			gjtSpecialty.setUpdatedDt(DateUtils.getNowTime());
			gjtSpecialty.setUpdatedBy(user.getId());
			gjtSpecialtyService.updateEntity(gjtSpecialty);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "发布失败");
		}

		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/specialty/list";
	}

	@RequiresPermissions("/edumanage/specialty/list$stop")
	@RequestMapping(value = "stop")
	public String stop(String id, HttpServletRequest request, RedirectAttributes redirectAttributes)
			throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "停用成功");
		try {
			GjtSpecialty gjtSpecialty = gjtSpecialtyService.queryById(id);
			gjtSpecialty.setStatus(4);
			gjtSpecialty.setUpdatedDt(DateUtils.getNowTime());
			gjtSpecialty.setUpdatedBy(user.getId());
			gjtSpecialtyService.updateEntity(gjtSpecialty);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "停用失败");
		}

		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/specialty/list";
	}

	@RequestMapping(value = "getModuleCodeById")
	@ResponseBody
	public String getModuleCodeById(String id) {
		TblSysData tblSysData = tblSysDataService.queryById(id);
		if (tblSysData != null) {
			return tblSysData.getCode();
		} else {
			return "";
		}
	}

	@RequiresPermissions("/edumanage/specialty/list$copy")
	@RequestMapping(value = "copy")
	@ResponseBody
	public Feedback copy(String specialtyId, String ruleCode, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "复制成功");

		try {
			PageRequest pageRequst = new PageRequest(0, 2);
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("EQ_ruleCode", ruleCode);
			searchParams.put("EQ_xxId", user.getGjtOrg().getId());
			Page<GjtSpecialty> gjtSpecialty = gjtSpecialtyService.queryPage(searchParams, pageRequst);
			if (gjtSpecialty.getTotalElements() == 0) {
				GjtSpecialty copy = gjtSpecialtyService.copy(specialtyId, ruleCode, user);
				if (copy == null) {
					feedback = new Feedback(false, "复制失败");
				}
			} else {
				feedback = new Feedback(false, "专业规则号已存在！");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "复制失败");
		}

		return feedback;
	}

	/**
	 * 导入课程模块
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年6月30日 下午3:45:41
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "importCourseModel")
	public ImportFeedback importCourseModel(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String[] heads = { "课程模块", "模块最低学分", "模块毕业最低学分", "模块中央电大考试最低学分", "导入结果" };
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, heads.length - 1);
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}
			List<Map<String, Object>> feedbackData = new ArrayList<Map<String, Object>>();
			Map<String, String> courseTypeMap2 = commonMapService.getIdNameMap("CourseType");// 课程模块
			NumberFormat format = new DecimalFormat("#.##");

			for (String[] data : dataList) {
				Map<String, Object> model = new HashMap<String, Object>();
				String[] result = new String[heads.length]; // 记录导入结果
				System.arraycopy(data, 0, result, 0, Math.min(data.length, heads.length - 1)); // 先拷贝数据
				int i = 0;
				try {
					for (String key : courseTypeMap2.keySet()) {
						if (courseTypeMap2.get(key).trim().equals(data[0])) {
							model.put("id", key);
							break;
						}
					}
					if (model.get("id") == null) {
						result[result.length - 1] = "课程模块不存在";
						failedList.add(result);
						continue;
					}
					i++;
					if (StringUtils.isNotBlank(data[i]))
						model.put("totalScore", format.format(Double.valueOf(data[i])));// 模块最低学分
					i++;
					if (StringUtils.isNotBlank(data[i]))
						model.put("score", format.format(Double.valueOf(data[i])));// 模块毕业最低学分
					i++;
					if (StringUtils.isNotBlank(data[i]))
						model.put("crtvuScore", format.format(Double.valueOf(data[i])));// 模块中央电大考试最低学分
					feedbackData.add(model);
					result[result.length - 1] = "导入成功";
					successList.add(result);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					model.put("error", heads[i] + "格式有误");
					result[result.length - 1] = heads[i] + "格式有误";
					failedList.add(result);
				}
			}
			ImportFeedback feedback = new ImportFeedback(true, dataList.size(), successList.size(), failedList.size(),
					null, null);
			if (failedList.size() > 0) {
				/* 创建记录成功和失败记录的文件 */
				long currentTimeMillis = System.currentTimeMillis();
				String successFileName = "secialtyPlan_success_" + currentTimeMillis + ".xls";
				String failedFileName = "secialtyPlan_failed_" + currentTimeMillis + ".xls";

				Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "专业计划导入成功记录");
				Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "专业计划导入失败记录");

				String filePath = request.getSession().getServletContext().getRealPath("")
						+ WebConstants.EXCEL_DOWNLOAD_URL + "courseModel" + File.separator;
				File f = new File(filePath);
				if (!f.exists()) {
					f.mkdirs();
				}

				File successFile = new File(filePath, successFileName);
				successFile.createNewFile();
				ExcelUtil.writeWorkbook(workbook1, successFile);

				File failedFile = new File(filePath, failedFileName);
				failedFile.createNewFile();
				ExcelUtil.writeWorkbook(workbook2, failedFile);
				feedback.setSuccessFileName(successFileName);
				feedback.setFailedFileName(failedFileName);
			}
			feedback.setResult(feedbackData);
			return feedback;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ImportFeedback(false, "系统异常！");
		}
	}

	/**
	 * 导入专业规则
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年6月30日 下午3:46:16
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "importPlan")
	public ImportFeedback importPlan(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

			String[] heads = { "建议学期", "课程模块", "课程代码", "课程名称", "课程性质", "课程类型", "考试单位", "导入结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, heads.length - 1);
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}

			List<Map<String, Object>> feedBackData = new ArrayList<Map<String, Object>>();
			Map<String, String> courseTypeMap = commonMapService.getDates("CourseType");// 课程模块

			for (String[] data : dataList) {
				Map<String, Object> plan = new HashMap<String, Object>();
				String[] result = new String[heads.length]; // 记录导入结果
				System.arraycopy(data, 0, result, 0, Math.min(data.length, heads.length - 1)); // 先拷贝数据

				if (StringUtils.isNotBlank(data[0])) {
					plan.put("termTypeCode", TermType.getCodeByText(data[0]));// 学期;
				}

				if (StringUtils.isNotBlank(data[1])) {
					for (String key : courseTypeMap.keySet()) {
						if (courseTypeMap.get(key).trim().equals(data[1])) {
							plan.put("courseTypeId", key);
							break;
						}
					}
					if (plan.get("courseTypeId") == null) {
						result[result.length - 1] = "课程模块不存在";
						failedList.add(result);
						continue;
					}
				}

				List<GjtCourse> courses = gjtCourseService.findByKchAndXxId(data[2], user.getGjtOrg().getId());
				if (CollectionUtils.isEmpty(courses)) {
					result[result.length - 1] = "课程号不存在";
					failedList.add(result);
					continue;
				}
				GjtCourse gjtCourse = courses.get(0);
				if (!gjtCourse.getKcmc().equals(data[3])) {
					result[result.length - 1] = "课程代码与课程名称不一致";
					failedList.add(result);
					continue;
				}
				if (StringUtils.isNotBlank(data[4])) {// 课程性质
					try {
						plan.put("courseCategory", CourseCategory.valueOf(data[4]).getNum());// 课程性质
					} catch (Exception e) {
						log.error(e.getMessage(), e);
						result[result.length - 1] = "课程性质有误";
						failedList.add(result);
						continue;
					}
				}

				if (StringUtils.isNotBlank(data[5])) {// 课程类型
					if (CourseType.getCodeByName(data[5]) != null) {
						plan.put("coursetype", CourseType.getCodeByName(data[5]));
					} else {
						result[result.length - 1] = "课程类型有误";
						failedList.add(result);
						continue;
					}
				}

				if (StringUtils.isNotBlank(data[6])) {// 考试单位
					if (ExamUnit.getCodeByName(data[6]) != null) {
						plan.put("examUnit", ExamUnit.getCodeByName(data[6]));
					} else {
						result[result.length - 1] = "考试单位有误";
						failedList.add(result);
						continue;
					}
				}

				plan.put("kch", gjtCourse.getKch());
				plan.put("kcmc", gjtCourse.getKcmc());
				plan.put("isEnabled", gjtCourse.getIsEnabled());
				plan.put("courseId", gjtCourse.getCourseId());
				plan.put("hour", gjtCourse.getHour());
				plan.put("courseId", gjtCourse.getCourseId());
				plan.put("score", gjtCourse.getCredit());
				result[result.length - 1] = "导入成功";
				successList.add(result);
				feedBackData.add(plan);
			}

			ImportFeedback feedback = new ImportFeedback(true, dataList.size(), successList.size(), failedList.size(),
					null, null);
			feedback.setResult(feedBackData);
			if (failedList.size() > 0) {
				/* 创建记录成功和失败记录的文件 */
				long currentTimeMillis = System.currentTimeMillis();
				String successFileName = "secialtyPlan_success_" + currentTimeMillis + ".xls";
				String failedFileName = "secialtyPlan_failed_" + currentTimeMillis + ".xls";

				Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "专业计划导入成功记录");
				Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "专业计划导入失败记录");

				String filePath = request.getSession().getServletContext().getRealPath("")
						+ WebConstants.EXCEL_DOWNLOAD_URL + "secialtyPlan" + File.separator;
				File f = new File(filePath);
				if (!f.exists()) {
					f.mkdirs();
				}

				File successFile = new File(filePath, successFileName);
				successFile.createNewFile();
				ExcelUtil.writeWorkbook(workbook1, successFile);

				File failedFile = new File(filePath, failedFileName);
				failedFile.createNewFile();
				ExcelUtil.writeWorkbook(workbook2, failedFile);
				feedback.setSuccessFileName(successFileName);
				feedback.setFailedFileName(failedFileName);
			}
			return feedback;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ImportFeedback(false, "系统异常！");
		}
	}

	@RequestMapping(value = "getPyccMap")
	@ResponseBody
	public List<Map<String, String>> getPyccMap() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		for (Entry<String, String> entry : pyccMap.entrySet()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("key", entry.getKey());
			map.put("value", entry.getValue());
			list.add(map);
		}

		return list;
	}

}
