/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtCourseCheck;
import com.gzedu.xlims.pojo.GjtCourseOwnership;
import com.gzedu.xlims.pojo.GjtFirstCourse;
import com.gzedu.xlims.pojo.GjtSpecialtyBase;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.graduation.GjtFirstCourseService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.share.GjtShareService;
import com.gzedu.xlims.service.textbook.GjtTextbookService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 
 * 功能说明： 课程管理
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月16日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/edumanage/course")
public class CourseController {

	private static final Logger log = LoggerFactory.getLogger(CourseController.class);

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtCourseService gjtCourseService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtShareService gjtShareService;

	@Autowired
	GjtTextbookService gjtTextbookService;

	@Autowired
	GjtFirstCourseService gjtFirstCourseService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
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

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		boolean isBtnFormalTab = subject.isPermitted("/edumanage/course/list$formalTab");
		boolean isBtnExperienceTab = subject.isPermitted("/edumanage/course/list$experienceTab");
		boolean isBtnReplaceTab = subject.isPermitted("/edumanage/course/list$replaceTab");
		if (!isBtnFormalTab && !isBtnExperienceTab && !isBtnReplaceTab) {
			isBtnFormalTab = true;
		}

		if (EmptyUtils.isEmpty(searchParams) || StringUtils.isBlank((String) searchParams.get("EQ_courseNature"))
				|| "".equals(searchParams.get("EQ_courseNature"))) {
			if (isBtnFormalTab) {
				searchParams.put("EQ_courseNature", "1");
			} else if (isBtnExperienceTab) {
				searchParams.put("EQ_courseNature", "2");
			} else if (isBtnReplaceTab) {
				searchParams.put("EQ_courseNature", "3");
			}
		}

		Page<GjtCourse> pageInfo = gjtCourseService.queryAllAndShare(user.getGjtOrg().getId(), searchParams, pageRequst,
				coursIds);

		for (GjtCourse gjtCourse : pageInfo) {
			if ("5".equals(gjtCourse.getIsEnabled())) {
				long totalCount = gjtCourseService.checkCourseCount(gjtCourse.getCourseId(), "");
				long yesCount = gjtCourseService.checkCourseCount(gjtCourse.getCourseId(), "Y");
				gjtCourse.setProportion(yesCount + "/" + totalCount);
			}
		}

		model.addAttribute("user", user);
		model.addAttribute("pageInfo", pageInfo);

		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.put("EQ_isEnabled", 1);
		long isEnabledNum = gjtCourseService.queryAllAndShare(user.getGjtOrg().getId(), map, pageRequst, coursIds)
				.getTotalElements();
		model.addAttribute("isEnabledNum", isEnabledNum);

		map.put("EQ_isEnabled", 5);// 部分
		long isPortionNum = gjtCourseService.queryAllAndShare(user.getGjtOrg().getId(), map, pageRequst, coursIds)
				.getTotalElements();
		model.addAttribute("isPortionNum", isPortionNum);

		map.put("EQ_isEnabled", 3);// 待验收
		long isWaitNum = gjtCourseService.queryAllAndShare(user.getGjtOrg().getId(), map, pageRequst, coursIds)
				.getTotalElements();
		model.addAttribute("isWaitNum", isWaitNum);

		map.put("EQ_isEnabled", 4);// 验收不通过
		long isNotNum = gjtCourseService.queryAllAndShare(user.getGjtOrg().getId(), map, pageRequst, coursIds)
				.getTotalElements();
		model.addAttribute("isNotNum", isNotNum);

		map.put("EQ_isEnabled", 0);
		long isNoResourcesNum = gjtCourseService.queryAllAndShare(user.getGjtOrg().getId(), map, pageRequst, coursIds)
				.getTotalElements();
		model.addAttribute("isNoResourcesNum", isNoResourcesNum);

		map.put("EQ_isEnabled", 2);
		long isConstructionNum = gjtCourseService.queryAllAndShare(user.getGjtOrg().getId(), map, pageRequst, coursIds)
				.getTotalElements();
		model.addAttribute("isConstructionNum", isConstructionNum);

		// Map<String, String> syhyMap =
		// commonMapService.getDates("TRADE_CODE");
		// model.addAttribute("syhyMap", syhyMap); // 所属行业
		Map<String, String> syzyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		model.addAttribute("syzyMap", syzyMap); // 所属专业
		model.addAttribute("wsjxzkMap", commonMapService.getDates("OnlineTeaching")); // 教学方式
		model.addAttribute("courseCategoryMap", commonMapService.getDatesBy("CourseCategoryInfo")); // 课程类型
		// model.addAttribute("courseNatureMap",
		// commonMapService.getDates("CourseNature")); // 课程性质
		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次

		// model.addAttribute("subjectMap", commonMapService.getExsubjectMap());
		// // 学科
		// model.addAttribute("categoryMap",
		// commonMapService.getExsubjectAndkindMap()); // 门类

		// 查询可替换课程
		PageRequest pageRequst2 = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Map<String, Object> searchParams2 = new HashMap<String, Object>();
		List<String> courseNatures = new ArrayList<String>();
		courseNatures.add("1");
		courseNatures.add("2");
		searchParams2.put("IN_courseNature", courseNatures);
		Page<GjtCourse> pageInfo2 = gjtCourseService.queryAllAndShare(user.getGjtOrg().getId(), searchParams2,
				pageRequst2, coursIds);
		model.addAttribute("replaceCourses", pageInfo2.getContent());

		model.addAttribute("oclassHost", AppConfig.getProperty("oclassHost"));

		model.addAttribute("isBtnChangeCourseNature", subject.isPermitted("/edumanage/course/list$changeCourseNature"));
		// 正式课程标签
		model.addAttribute("isBtnFormalTab", isBtnFormalTab);
		model.addAttribute("isBtnCreateFormal", subject.isPermitted("/edumanage/course/list$createFormal"));
		model.addAttribute("isBtnUpdateFormal", subject.isPermitted("/edumanage/course/list$updateFormal"));
		model.addAttribute("isBtnDeleteFormal", subject.isPermitted("/edumanage/course/list$deleteFormal"));
		model.addAttribute("isBtnViewFormal", subject.isPermitted("/edumanage/course/list$viewFormal"));
		model.addAttribute("isBtnEnableFormal", subject.isPermitted("/edumanage/course/list$enableFormal"));
		// model.addAttribute("isBtnDisableFormal",
		// subject.isPermitted("/edumanage/course/list$disableFormal"));
		model.addAttribute("isBtnConstructionFormal", subject.isPermitted("/edumanage/course/list$formalConstruction"));
		model.addAttribute("isBtnNoResourcesFormal", subject.isPermitted("/edumanage/course/list$formalNoResources"));
		model.addAttribute("isBtnPreviewFormal", subject.isPermitted("/edumanage/course/list$previewFormal"));
		model.addAttribute("isBtnOfficialChangeWait", subject.isPermitted("/edumanage/course/list$officialChangeWait"));
		// 体验课程标签
		model.addAttribute("isBtnExperienceTab", isBtnExperienceTab);
		model.addAttribute("isBtnCreateExperience", subject.isPermitted("/edumanage/course/list$createExperience"));
		model.addAttribute("isBtnUpdateExperience", subject.isPermitted("/edumanage/course/list$updateExperience"));
		model.addAttribute("isBtnDeleteExperience", subject.isPermitted("/edumanage/course/list$deleteExperience"));
		model.addAttribute("isBtnViewExperience", subject.isPermitted("/edumanage/course/list$viewExperience"));
		model.addAttribute("isBtnEnableExperience", subject.isPermitted("/edumanage/course/list$enableExperience"));
		model.addAttribute("isBtnExperienceChangeWait",
				subject.isPermitted("/edumanage/course/list$experienceChangeWait"));

		// model.addAttribute("isBtnDisableExperience",
		// subject.isPermitted("/edumanage/course/list$disableExperience"));
		model.addAttribute("isBtnConstructionExperience",
				subject.isPermitted("/edumanage/course/list$experienceConstruction"));
		model.addAttribute("isBtnNoResourcesExperience",
				subject.isPermitted("/edumanage/course/list$experienceNoResources"));
		model.addAttribute("isBtnPreviewExperience", subject.isPermitted("/edumanage/course/list$previewExperience"));
		// 替换课程标签
		model.addAttribute("isBtnReplaceTab", isBtnReplaceTab);
		model.addAttribute("isBtnCreateReplace", subject.isPermitted("/edumanage/course/list$createReplace"));
		model.addAttribute("isBtnUpdateReplace", subject.isPermitted("/edumanage/course/list$updateReplace"));
		model.addAttribute("isBtnDeleteReplace", subject.isPermitted("/edumanage/course/list$deleteReplace"));
		model.addAttribute("isBtnViewReplace", subject.isPermitted("/edumanage/course/list$viewReplace"));
		model.addAttribute("isBtnEnableReplace", subject.isPermitted("/edumanage/course/list$enableReplace"));
		// model.addAttribute("isBtnDisableReplace",
		// subject.isPermitted("/edumanage/course/list$disableReplace"));
		model.addAttribute("isBtnReplaceChangeWait", subject.isPermitted("/edumanage/course/list$replaceChangeWait"));
		model.addAttribute("isBtnConstructionReplace",
				subject.isPermitted("/edumanage/course/list$replaceConstruction"));
		model.addAttribute("isBtnNoResourcesReplace", subject.isPermitted("/edumanage/course/list$replaceNoResources"));
		model.addAttribute("isBtnPreviewReplace", subject.isPermitted("/edumanage/course/list$previewReplace"));

		model.addAttribute("isBtnCheckCourse", subject.isPermitted("/edumanage/course/list$checkCourse"));
		model.addAttribute("isBtnCheckCourseView", subject.isPermitted("/edumanage/course/list$checkCourseView"));

		return "edumanage/course/list";
	}

	/**
	 * 当前登录用户所属院校课程列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "listCourse", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listCourse(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<GjtCourse> pageInfo = gjtCourseService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);
		map.put("pageInfo", pageInfo);
		return map;
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtCourse gjtCourse = gjtCourseService.queryBy(id);
		model.addAttribute("entity", gjtCourse);
		model.addAttribute("action", "view");

		model.addAttribute("courseCategoryMap", commonMapService.getDatesBy("CourseCategoryInfo")); // 课程类型
		Map<String, String> syzyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		model.addAttribute("syzyMap", syzyMap); // 所属专业
		model.addAttribute("wsjxzkMap", commonMapService.getDates("OnlineTeaching")); // 教学方式
		// model.addAttribute("courseTypeMap",
		// commonMapService.getDates("CourseType")); // 课程类型
		// model.addAttribute("courseNatureMap",
		// commonMapService.getDates("CourseNature")); // 课程性质
		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次

		String[] syzys = null;
		if (gjtCourse.getSyzy() != null) {
			syzys = gjtCourse.getSyzy().split(",");
		}
		model.addAttribute("syzys", syzys);

		String[] replaceCourseIds = null;
		if (gjtCourse.getReplaceCourseId() != null) {
			replaceCourseIds = gjtCourse.getReplaceCourseId().split(",");
		}
		model.addAttribute("replaceCourseIds", replaceCourseIds);

		// 查询可替换课程
		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		List<String> courseNatures = new ArrayList<String>();
		courseNatures.add("1");
		courseNatures.add("2");
		searchParams.put("IN_courseNature", courseNatures);
		List<GjtCourseOwnership> gjtCourseOwnershipList = gjtShareService.findByOrgId(user.getGjtOrg().getId());
		List<String> coursIds = new ArrayList<String>();
		if (gjtCourseOwnershipList.size() != 0) {
			for (GjtCourseOwnership gjtCourseOwnership : gjtCourseOwnershipList) {
				if (gjtCourseOwnership != null) {
					coursIds.add(gjtCourseOwnership.getCourseId());
				}
			}
		}
		Page<GjtCourse> pageInfo = gjtCourseService.queryAllAndShare(user.getGjtOrg().getId(), searchParams, pageRequst,
				coursIds);
		model.addAttribute("replaceCourses", pageInfo.getContent());

		// model.addAttribute("subjectMap", commonMapService.getExsubjectMap());
		// // 学科
		// model.addAttribute("categoryMap",
		// commonMapService.getExsubjectkindMap(gjtCourse.getSubject())); // 门类
		if (model.containsAttribute(WebConstants.COLLEGE_MODEL_TAG)) {
			return "edumanage/college/course/college_course_view";
		}
		return "edumanage/course/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("entity", new GjtCourse());
		model.addAttribute("action", "create");
		// Map<String, String> syhyMap =
		// commonMapService.getDates("TRADE_CODE");
		// model.addAttribute("syhyMap", syhyMap); // 所属行业
		Map<String, String> syzyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		model.addAttribute("syzyMap", syzyMap); // 所属专业
		model.addAttribute("wsjxzkMap", commonMapService.getDates("OnlineTeaching")); // 教学方式
		model.addAttribute("courseCategoryMap", commonMapService.getDatesBy("CourseCategoryInfo")); // 课程类型
		// model.addAttribute("courseNatureMap",
		// commonMapService.getDates("CourseNature")); // 课程性质
		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次
		// model.addAttribute("subjectMap", commonMapService.getExsubjectMap());
		// // 学科

		// 查询可替换课程
		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		List<String> courseNatures = new ArrayList<String>();
		courseNatures.add("1");
		courseNatures.add("2");
		searchParams.put("IN_courseNature", courseNatures);
		List<GjtCourseOwnership> gjtCourseOwnershipList = gjtShareService.findByOrgId(user.getGjtOrg().getId());
		List<String> coursIds = new ArrayList<String>();
		if (gjtCourseOwnershipList.size() != 0) {
			for (GjtCourseOwnership gjtCourseOwnership : gjtCourseOwnershipList) {
				if (gjtCourseOwnership != null) {
					coursIds.add(gjtCourseOwnership.getCourseId());
				}
			}
		}
		Page<GjtCourse> pageInfo = gjtCourseService.queryAllAndShare(user.getGjtOrg().getId(), searchParams, pageRequst,
				coursIds);
		model.addAttribute("replaceCourses", pageInfo.getContent());

		return "edumanage/course/form";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback create(@Valid GjtCourse entity, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "创建成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

			if ("1".equals(user.getGjtOrg().getOrgType())) {
				entity.setXxId(user.getGjtOrg().getId());
			} else {
				if (user.getGjtOrg().getParentGjtOrg() != null) {
					entity.setXxId(user.getGjtOrg().getParentGjtOrg().getId());
				} else {
					entity.setXxId(user.getGjtOrg().getId());
				}
			}

			entity.setIsEnabled("0");
			entity.setYxId(user.getGjtOrg().getId());
			entity.setGjtOrg(user.getGjtOrg());
			entity.setOrgCode(user.getGjtOrg().getCode());
			entity.setCreatedBy(user.getId());

			String[] textbookIds = request.getParameterValues("textbookIds");

			if (textbookIds != null) {
				List<GjtTextbook> gjtTextbook1 = new ArrayList<GjtTextbook>();// 主教材
				List<GjtTextbook> gjtTextbook2 = new ArrayList<GjtTextbook>();// 复习资料
				List<GjtTextbook> textbooks = gjtTextbookService.findAll(Arrays.asList(textbookIds));

				for (GjtTextbook gjtTextbook : textbooks) {
					if (gjtTextbook.getTextbookType() == 1) {
						gjtTextbook1.add(gjtTextbook);
					} else {
						gjtTextbook2.add(gjtTextbook);
					}
				}
				entity.setGjtTextbookList1(gjtTextbook1);
				entity.setGjtTextbookList2(gjtTextbook2);
			}

			if (entity.getCourseNature() == null || "".equals(entity.getCourseNature())) {
				entity.setCourseNature("1");
			}

			gjtCourseService.insert(entity);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "创建失败：" + e.getMessage());
		}

		// redirectAttributes.addFlashAttribute("feedback", feedback);
		// return "redirect:/edumanage/course/list?search_EQ_courseNature=" +
		// entity.getCourseNature();
		return feedback;
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtCourse gjtCourse = gjtCourseService.queryBy(id);
		model.addAttribute("entity", gjtCourse);
		model.addAttribute("action", "update");

		Map<String, String> syzyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		model.addAttribute("syzyMap", syzyMap); // 所属专业
		model.addAttribute("wsjxzkMap", commonMapService.getDates("OnlineTeaching")); // 教学方式
		model.addAttribute("courseCategoryMap", commonMapService.getDatesBy("CourseCategoryInfo")); // 课程类型
		// model.addAttribute("courseTypeMap",
		// commonMapService.getDates("CourseType")); // 课程类型
		// model.addAttribute("courseNatureMap",
		// commonMapService.getDates("CourseNature")); // 课程性质
		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次
		// model.addAttribute("subjectMap", commonMapService.getExsubjectMap());
		// // 学科
		// model.addAttribute("categoryMap",
		// commonMapService.getExsubjectkindMap(gjtCourse.getSubject())); // 门类

		String[] syzys = null;
		if (gjtCourse.getSyzy() != null) {
			syzys = gjtCourse.getSyzy().split(",");
		}
		model.addAttribute("syzys", syzys);

		String[] replaceCourseIds = null;
		if (gjtCourse.getReplaceCourseId() != null) {
			replaceCourseIds = gjtCourse.getReplaceCourseId().split(",");
		}
		model.addAttribute("replaceCourseIds", replaceCourseIds);

		// 查询可替换课程
		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		List<String> courseNatures = new ArrayList<String>();
		courseNatures.add("1");
		courseNatures.add("2");
		searchParams.put("IN_courseNature", courseNatures);
		List<GjtCourseOwnership> gjtCourseOwnershipList = gjtShareService.findByOrgId(user.getGjtOrg().getId());
		List<String> coursIds = new ArrayList<String>();
		if (gjtCourseOwnershipList.size() != 0) {
			for (GjtCourseOwnership gjtCourseOwnership : gjtCourseOwnershipList) {
				if (gjtCourseOwnership != null) {
					coursIds.add(gjtCourseOwnership.getCourseId());
				}
			}
		}
		Page<GjtCourse> pageInfo = gjtCourseService.queryAllAndShare(user.getGjtOrg().getId(), searchParams, pageRequst,
				coursIds);
		model.addAttribute("replaceCourses", pageInfo.getContent());

		return "edumanage/course/form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(@Valid @ModelAttribute("info") GjtCourse entity, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		GjtCourse modifyInfo = gjtCourseService.queryBy(entity.getCourseId());
		if (StringUtils.isNotBlank(entity.getKch()))
			modifyInfo.setKch(entity.getKch());
		modifyInfo.setKcmc(entity.getKcmc());
		modifyInfo.setWsjxzk(entity.getWsjxzk());
		modifyInfo.setCourseType(entity.getCourseType());
		// modifyInfo.setCourseNature(entity.getCourseNature());
		modifyInfo.setPycc(entity.getPycc());
		modifyInfo.setCategory(entity.getCategory());
		modifyInfo.setSubject(entity.getSubject());
		// modifyInfo.setSyhy(entity.getSyhy());
		modifyInfo.setSyzy(entity.getSyzy());
		modifyInfo.setCredit(entity.getCredit());
		modifyInfo.setHour(entity.getHour());
		modifyInfo.setLabel(entity.getLabel());
		// modifyInfo.setIsEnabled(entity.getIsEnabled());
		modifyInfo.setAssessment(entity.getAssessment());
		modifyInfo.setQualified(entity.getQualified());
		modifyInfo.setActivity(entity.getActivity());
		modifyInfo.setKhsm(entity.getKhsm());
		modifyInfo.setKcfm(entity.getKcfm());
		modifyInfo.setKcjj(entity.getKcjj());
		modifyInfo.setReplaceCourseId(entity.getReplaceCourseId());
		modifyInfo.setCourseCategory(entity.getCourseCategory());
		modifyInfo.setUpdatedBy(user.getId());
		modifyInfo.setGuideName(entity.getGuideName());
		modifyInfo.setGuidePath(entity.getGuidePath());
		String[] textbookIds = request.getParameterValues("textbookIds");

		if (textbookIds != null) {
			List<GjtTextbook> gjtTextbook1 = new ArrayList<GjtTextbook>();// 主教材
			List<GjtTextbook> gjtTextbook2 = new ArrayList<GjtTextbook>();// 复习资料
			List<GjtTextbook> textbooks = gjtTextbookService.findAll(Arrays.asList(textbookIds));

			for (GjtTextbook gjtTextbook : textbooks) {
				if (gjtTextbook.getTextbookType() == 1) {
					gjtTextbook1.add(gjtTextbook);
				} else {
					gjtTextbook2.add(gjtTextbook);
				}
			}
			modifyInfo.setGjtTextbookList1(gjtTextbook1);
			modifyInfo.setGjtTextbookList2(gjtTextbook2);
		} else {
			modifyInfo.setGjtTextbookList1(null);
			modifyInfo.setGjtTextbookList2(null);
		}

		try {
			gjtCourseService.update(modifyInfo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "更新失败：" + e.getMessage());
		}

		// redirectAttributes.addFlashAttribute("feedback", feedback);
		// return "redirect:/edumanage/course/list?search_EQ_courseNature=" +
		// modifyInfo.getCourseNature();
		return feedback;
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, ServletResponse response) throws IOException {
		Feedback fb = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(ids)) {
			try {
				gjtCourseService.delete(Arrays.asList(ids.split(",")));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				fb = new Feedback(false, "删除失败");
			}
		}
		return fb;
	}

	@RequestMapping(value = "share/{id}", method = RequestMethod.GET)
	public String shareForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> orgMap = commonMapService.getOrgMapBy(user.getId(), false);
		orgMap.remove(user.getGjtOrg().getId());
		GjtCourse gjtCourse = gjtCourseService.queryBy(id);
		Map<String, String> orgAllNotBSMap = commonMapService.getXxmcDatesExceptBS(gjtCourse.getGjtOrg().getId());
		model.addAttribute("orgAllNotBSMap", orgAllNotBSMap);
		model.addAttribute("orgMap", orgMap);
		model.addAttribute("entity", gjtCourse);
		model.addAttribute("action", "share");
		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次
		return "edumanage/course/share";
	}

	@RequestMapping(value = "share", method = RequestMethod.POST)
	public String share(@Valid @ModelAttribute("entity") GjtCourseOwnership entity, String[] orgId, Model model,
			RedirectAttributes redirectAttributes) {
		boolean result = true;
		entity.setId(UUID.randomUUID().toString());
		// GjtCourse modifyInfo =
		// gjtCourseService.queryBy(entity.getCourseId());
		//
		// List<GjtOrg> schoolinfos =
		// gjtOrgService.queryAll(Arrays.asList(orgId));
		// modifyInfo.setShareGjtSchoolInfos(schoolinfos);

		try {
			gjtShareService.saveGjtShare(entity);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = false;
		}
		model.addAttribute("result", result);
		model.addAttribute("entity", gjtCourseService.queryBy(entity.getCourseId()));
		model.addAttribute("action", "succesful");
		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次
		return "edumanage/course/share";
		// return "redirect:/edumanage/course/list";
	}

	/*
	 * @RequestMapping(value = "share", method = RequestMethod.POST) public
	 * String share(@Valid @ModelAttribute("entity") GjtCourse entity, String[]
	 * orgId, RedirectAttributes redirectAttributes) { Feedback feedback = new
	 * Feedback(true, "共享成功"); GjtCourse modifyInfo =
	 * gjtCourseService.queryBy(entity.getCourseId());
	 * 
	 * List<GjtOrg> schoolinfos = gjtOrgService.queryAll(Arrays.asList(orgId));
	 * modifyInfo.setShareGjtSchoolInfos(schoolinfos);
	 * 
	 * try { gjtCourseService.update(modifyInfo); } catch (Exception e) {
	 * feedback = new Feedback(false, "共享失败"); }
	 * redirectAttributes.addFlashAttribute("feedback", feedback); return
	 * "redirect:/edumanage/course/list"; }
	 */
	// 课程编号是否存在
	@RequestMapping(value = "checkLogin")
	@ResponseBody
	public Feedback checkLogin(String kch, HttpServletRequest request) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = new PageRequest(0, 2);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_kch", kch);
		searchParams.put("EQ_xxId", user.getGjtOrg().getId());
		searchParams.put("EQ_isDeleted", "N");
		Page<GjtCourse> gjtCourse = gjtCourseService.queryPage(searchParams, pageRequst);
		Boolean boolean1 = true;
		if (gjtCourse.getTotalElements() == 0) {
			boolean1 = false;
		}
		Feedback fe = new Feedback(boolean1, "");
		fe.setSuccessful(boolean1);
		return fe;
	}

	@RequestMapping(value = "choiceCourseList", method = RequestMethod.GET)
	public String choiceCourseList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
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
		Page<GjtCourse> pageInfo = gjtCourseService.queryAllAndShare(user.getGjtOrg().getId(), searchParams, pageRequst,
				coursIds);
		model.addAttribute("user", user);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次
		model.addAttribute("courseNatureMap", commonMapService.getDates("CourseNature")); // 课程性质
		model.addAttribute("courseCategoryMap", commonMapService.getDatesBy("CourseCategoryInfo")); // 课程类型
		model.addAttribute("wsjxzkMap", commonMapService.getDates("OnlineTeaching")); // 教学方式
		// model.addAttribute("syhyMap",
		// commonMapService.getDates("TRADE_CODE")); // 所属行业
		model.addAttribute("syzyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId())); // 所属专业
		return "edumanage/course/choiceCourseList";
	}

	@RequestMapping(value = "choiceMultiCourseList", method = RequestMethod.GET)
	public String choiceMultiCourseList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
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
		Page<GjtCourse> pageInfo = gjtCourseService.queryAllAndShare(user.getGjtOrg().getId(), searchParams, pageRequst,
				coursIds);
		model.addAttribute("user", user);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次
		// model.addAttribute("syhyMap",
		// commonMapService.getDates("TRADE_CODE")); // 所属行业
		model.addAttribute("syzyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId())); // 所属专业
		// model.addAttribute("courseTypeMap",
		// commonMapService.getDates("course_type")); // 课程类型
		// model.addAttribute("wsjxzkMap",
		// commonMapService.getDates("OnlineTeaching")); // 教学方式
		model.addAttribute("courseCategoryMap", commonMapService.getDatesBy("CourseCategoryInfo")); // 课程类型
		return "edumanage/course/choiceMultiCourseList";
	}

	@RequestMapping(value = "changeSubject")
	@ResponseBody
	public Map<String, List<CacheService.Value>> changeSubject(String subject) {
		Map<String, List<CacheService.Value>> map = new HashMap<String, List<CacheService.Value>>();
		map.put("obj", commonMapService.getExsubjectkindList(subject));
		return map;
	}

	/**
	 * 学院模式--课程管理
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月16日 下午2:50:53
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "courseList", method = RequestMethod.GET)
	public String courseList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Sort sort = new Sort(Direction.DESC, "createdDt");

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, sort);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		List<GjtCourseOwnership> gjtCourseOwnershipList = gjtShareService.findByOrgCode(user.getGjtOrg().getId());
		List<String> coursIds = new ArrayList<String>();
		if (gjtCourseOwnershipList.size() != 0) {
			for (GjtCourseOwnership gjtCourseOwnership : gjtCourseOwnershipList) {
				if (gjtCourseOwnership != null) {
					coursIds.add(gjtCourseOwnership.getCourseId());
				}
			}
		}

		Page<GjtCourse> pageInfo = gjtCourseService.queryAllAndShare(user.getGjtOrg().getId(), searchParams, pageRequst,
				coursIds);

		model.addAttribute("user", user);
		model.addAttribute("pageInfo", pageInfo);
		return "edumanage/course/courseList";
	}

	@RequestMapping(value = "enable")
	public String enable(String id, HttpServletRequest request, RedirectAttributes redirectAttributes)
			throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "操作成功");
		GjtCourse modifyInfo = gjtCourseService.queryBy(id);
		try {
			modifyInfo.setIsEnabled("1");
			modifyInfo.setUpdatedBy(user.getId());
			gjtCourseService.update(modifyInfo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "操作失败");
		}

		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/course/list?search_EQ_courseNature=" + modifyInfo.getCourseNature();
	}

	@RequestMapping(value = "disable")
	public String disable(String id, HttpServletRequest request, RedirectAttributes redirectAttributes)
			throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "操作成功");
		GjtCourse modifyInfo = gjtCourseService.queryBy(id);
		try {
			modifyInfo.setIsEnabled("0");
			modifyInfo.setUpdatedBy(user.getId());
			gjtCourseService.update(modifyInfo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "操作失败");
		}

		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/course/list?search_EQ_courseNature=" + modifyInfo.getCourseNature();
	}

	// 更改课程上线状态
	@RequestMapping(value = "construction")
	public String construction(String id, HttpServletRequest request, RedirectAttributes redirectAttributes,
			String status) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "操作成功");
		GjtCourse modifyInfo = gjtCourseService.queryBy(id);
		boolean bool = "4".equals(modifyInfo.getIsEnabled());
		try {

			gjtCourseService.updateIsEnabled(id, status);

			// 提交审核
			if ("3".equals(status)) {
				GjtCourseCheck item = new GjtCourseCheck();
				item.setId(UUIDUtils.random().toString());
				item.setAuditDt(DateUtils.getNowTime());
				if (bool) {
					item.setAuditState("3");
					item.setSubmitUser(user);
				} else {
					item.setAuditState("0");
				}
				item.setGjtCourse(modifyInfo);
				item.setSubmitUser(user);
				item.setCreatedBy(user.getRealName());
				item.setCreatedDt(DateUtils.getNowTime());
				item.setIsDeleted("N");
				item.setVersion(new BigDecimal("1"));
				gjtCourseService.sumbitCheck(item);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "操作失败");
		}

		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/course/list?search_EQ_courseNature=" + modifyInfo.getCourseNature();
	}

	@RequestMapping(value = "changeCourseNature")
	public String changeCourseNature(String id, HttpServletRequest request, RedirectAttributes redirectAttributes)
			throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "转换成功");
		GjtCourse modifyInfo = gjtCourseService.queryBy(id);
		try {
			if ("1".equals(modifyInfo.getCourseNature())) {
				modifyInfo.setCourseNature("2");
			} else if ("2".equals(modifyInfo.getCourseNature())) {
				modifyInfo.setCourseNature("1");
			}
			modifyInfo.setUpdatedBy(user.getId());
			gjtCourseService.update(modifyInfo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "转换失败");
		}

		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/course/list?search_EQ_courseNature=" + modifyInfo.getCourseNature();
	}

	/**
	 * 查询学习平台的课程数据
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "getPcourseList")
	public String getPcourseList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		// GjtUserAccount user = (GjtUserAccount)
		// request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		@SuppressWarnings("rawtypes")
		Page pageInfo = gjtCourseService.getPcourseList(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("oclassHost", AppConfig.getProperty("oclassHost"));
		return "edumanage/course/getPcourseList";
	}

	@RequestMapping(value = "checkCourse", method = RequestMethod.GET)
	public String checkCourse(Model model, HttpServletRequest request, String courseId, String action) {

		String originalSatus = "1";// 原始课程状态，看是否全部发布，还是部分发布
		int totalSection = 0;// 判断部分启用和全部启用
		int totalIsEn = 0;// 部分启用
		boolean isCheckBtn = false;

		GjtCourse info = gjtCourseService.queryBy(courseId);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("courseId", courseId);
		List<Map<String, Object>> courseStageList = gjtCourseService.queryCourseStage(searchParams);// 阶段
		for (Map<String, Object> map : courseStageList) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("periodId", map.get("PERIOD_ID"));
			List<Map<String, Object>> courseStageAreaList = gjtCourseService.queryCourseStageArea(params);// 区块
			map.put("courseStageArea", courseStageAreaList);

			for (Map<String, Object> item : courseStageAreaList) {
				Map<String, Object> formMap = new HashMap<String, Object>();
				formMap.put("areaId", item.get("AREA_ID"));
				List<Map<String, Object>> queryCourseSection = gjtCourseService.queryCourseSection(formMap);// 章节
				item.put("courseSection", queryCourseSection);

				for (Map<String, Object> map2 : queryCourseSection) {
					String object = (String) map2.get("DO_FINISH");
					if ("N".equals(object)) {
						originalSatus = "2";
					} else {
						String isCheck = (String) map2.get("IS_CHECK");
						if ("0".equals(isCheck) || "2".equals(isCheck) || "3".equals(info.getIsEnabled())) {// 已发布没有验收的章节，有就显示按钮，没有则不显示
							isCheckBtn = true;
						} else {
							totalIsEn++;
						}
					}
					totalSection++;
				}
			}
		}

		if ("view".equals(action)) {// 详情 审核记录
			List<GjtCourseCheck> checkList = info.getGjtCourseCheckList();
			model.addAttribute("checkList", checkList);
		}

		model.addAttribute("courseStageList", courseStageList);
		model.addAttribute("action", action);
		model.addAttribute("info", info);
		model.addAttribute("isCheckBtn", isCheckBtn);
		model.addAttribute("totalSection", totalSection);// 一共多少章节，拿来判断是否部分发布
		model.addAttribute("originalSatus", originalSatus);// 1。全部发布，2。部分发布
		model.addAttribute("totalIsEn", totalIsEn);// 2。部分发布

		model.addAttribute("oclassHost", AppConfig.getProperty("oclassHost"));
		return "edumanage/course/checkCourse";
	}

	// 添加审批记录
	@RequestMapping(value = "submitCheck", method = RequestMethod.POST)
	@ResponseBody
	public Feedback submitCheck(Model model, HttpServletRequest request, String courseId, String status,
			String auditContent) {
		Feedback fb = new Feedback(true, "审批成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtCourse modifyInfo = gjtCourseService.queryBy(courseId);
		try {
			String[] checkValues = request.getParameterValues("taskIds[]");

			if ("1".equals(status)) {// 通过
				if (EmptyUtils.isNotEmpty(checkValues)) {
					// 更改学习平台的章节为已验收
					gjtCourseService.updateCheck(checkValues, status);
					log.info("课程验收通过taskId={}，status={},isEnabled={}", checkValues, status);
				}
				// 查询是否还有没有启用的
				long checkCourseCount = gjtCourseService.checkCourseCount(courseId, "N");
				String isEnabled = checkCourseCount < 1 ? "1" : "5";

				// 更改教学教务课程的启用或者部分启用状态；2018年3月27日 新增是否可以开课学习状态，只要一门验收通过了，就可以开课
				gjtCourseService.updateIsEnabled(modifyInfo.getCourseId(), isEnabled, user.getRealName());
			} else {
				// 只要有一章不通过，状态就是验收不通过
				gjtCourseService.updateIsEnabled(modifyInfo.getCourseId(), "4");
				log.info("课程验收不通过taskId={}，status={}", checkValues, status);
				if (EmptyUtils.isNotEmpty(checkValues)) {
					gjtCourseService.updateCheck(checkValues, status);
				}
			}
			// 记录审批记录
			GjtCourseCheck item = new GjtCourseCheck();
			item.setId(UUIDUtils.random().toString());
			item.setAuditContent(auditContent);
			item.setAuditDt(DateUtils.getNowTime());
			item.setAuditState(status);
			item.setGjtCourse(modifyInfo);
			item.setAuditUser(user);
			item.setCreatedBy(user.getId());
			item.setCreatedDt(DateUtils.getNowTime());
			item.setIsDeleted("N");
			item.setVersion(new BigDecimal("1"));
			gjtCourseService.sumbitCheck(item);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, "审批失败，系统异常");
		}

		return fb;
	}

	@RequestMapping(value = "queryFirstCoursesList", method = RequestMethod.GET)
	public String queryFirstCoursesList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Sort sort = new Sort(Direction.DESC, "createdDt");
		PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize, sort);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("orgId", user.getGjtOrg().getId());
		Page<GjtFirstCourse> page = gjtFirstCourseService.queryFirstCoursesListByPage(searchParams, pageRequest);
		Iterator<GjtFirstCourse> iterator = page.iterator();
		while (iterator.hasNext()) {
			GjtFirstCourse fcourse = iterator.next();
			long count;
			if (fcourse.getType() == 0) {
				count = gjtFirstCourseService.countAllFirstCourseStudentNum(fcourse.getId());
			} else {
				count = gjtFirstCourseService.countStudentByOrgId(user.getGjtOrg().getId());
			}
			fcourse.setAllViewNum(count);
			fcourse.setViewNum(gjtFirstCourseService.countFirstCourseStudentNum(fcourse.getId()));
		}
		model.addAttribute("pageInfo", page);
		model.addAttribute("specialtyBaseMap", commonMapService.getSpecialtyBaseMap(user.getGjtOrg().getId()));
		return "edumanage/course/firstCourseList";
	}

	@RequestMapping(value = "createFirstCourse", method = RequestMethod.GET)
	public String createFirstCourse(Model model, HttpServletRequest request, String firstCourseId) {
		if (StringUtils.isNotBlank(firstCourseId)) {
			GjtFirstCourse firstCourse = gjtFirstCourseService.queryById(firstCourseId);
			model.addAttribute("item", firstCourse);
		} else {
			model.addAttribute("item", new GjtFirstCourse());
		}
		return "edumanage/course/firstCourseForm";
	}

	@ResponseBody
	@RequestMapping(value = "saveFirstCourse", method = RequestMethod.POST)
	public Feedback saveFirstCourse(HttpServletRequest request, String id, String title, String content,
			@RequestParam(value = "videos[]") List<String> videos,
			@RequestParam(value = "specialtyIds[]", required = false) List<String> specialtyIds) {
		Feedback feedback = new Feedback(true, null);

		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtFirstCourse gjtFirstCourse;
			Date now = new Date();
			if (StringUtils.isNotBlank(id)) {
				gjtFirstCourse = gjtFirstCourseService.queryById(id);
				gjtFirstCourse.setUpdateBy(user.getId());
				gjtFirstCourse.setUpdateDt(now);
			} else {
				gjtFirstCourse = new GjtFirstCourse();
				gjtFirstCourse.setCreatedBy(user.getId());
				gjtFirstCourse.setCreatedDt(now);
				gjtFirstCourse.setOrgId(user.getGjtOrg().getId());
				gjtFirstCourse.setId(UUIDUtils.random());
				gjtFirstCourse.setIsDeleted("N");
			}
			gjtFirstCourse.setTitle(title);
			gjtFirstCourse.setContent(content);
			gjtFirstCourseService.saveFirstCourse(gjtFirstCourse, videos, specialtyIds);
		} catch (Exception e) {
			feedback.setSuccessful(false);
			feedback.setMessage("系统异常");
			log.error(e.getMessage(), e);
		}
		return feedback;
	}

	@RequestMapping(value = "chooseSpecialty", method = RequestMethod.GET)
	public String chooseSpecialty(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("orgId", user.getGjtOrg().getId());
		Page<GjtSpecialtyBase> page = gjtFirstCourseService.querySpecialtyBasePage(searchParams, pageRequst);
		model.addAttribute("pageInfo", page);
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		model.addAttribute("pyccMap", pyccMap);
		return "/edumanage/course/chooseSpecialtyList";
	}

	@ResponseBody
	@RequestMapping(value = "queryVideoImage", method = RequestMethod.GET)
	public String queryVideoImage(String videoUrl) {
		try {
			String xml = HttpClientUtils.doHttpGet(
					"http://eefile.gzedu.com/video/getVideoInfoByUrl.do?formMap.VIDEO_URL=" + videoUrl,
					new HashMap<String, Object>(), 10000, "UTF-8");
			if (StringUtils.isNotBlank(xml)) {
				Document document = DocumentHelper.parseText(xml);
				Element root = document.getRootElement();
				return root.element("VIDEO_IMG").getText();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@RequestMapping(value = "choiceTextbookList", method = RequestMethod.GET)
	public String choiceTextbookList(Model model, HttpServletRequest request, String courseId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtCourse course = gjtCourseService.queryById(courseId);
		model.addAttribute("entity", course);
		List<GjtTextbook> textbooks = gjtCourseService.queryCourseTextbook(courseId);
		model.addAttribute("textbooks", textbooks);
		model.addAttribute("wsjxzkMap", commonMapService.getDates("OnlineTeaching")); // 教学方式
		model.addAttribute("courseCategoryMap", commonMapService.getDatesBy("CourseCategoryInfo")); // 课程类型

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		searchParams.put("NEQ_courseId", courseId);
		Page<GjtTextbook> pageInfo = gjtTextbookService.findAll(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);

		return "edumanage/course/choiceTextbookList";
	}

	/**
	 * 课程添加教材
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月17日 下午3:30:37
	 * @param courseId
	 * @param textBookIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveCourseTextBook", method = RequestMethod.POST)
	public Feedback saveCourseTextBook(String courseId,
			@RequestParam(value = "textbookIds[]") List<String> textbookIds) {
		Feedback feedback = new Feedback(true, null);
		try {
			GjtCourse gjtCourse = gjtCourseService.queryById(courseId);
			for (String textbookId : textbookIds) {
				gjtCourse.getGjtTextbookList().add(new GjtTextbook(textbookId));
			}
			gjtCourseService.save(gjtCourse);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback.setSuccessful(false);
			feedback.setMessage("系统异常");
		}
		return feedback;
	}

	/**
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月17日 下午4:46:30
	 * @param courseId
	 * @param textbookId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "deleteCourseTextBook", method = RequestMethod.GET)
	public Feedback deleteCourseTextBook(String courseId, String textbookId) {
		Feedback feedback = new Feedback(true, null);
		try {
			GjtCourse gjtCourse = gjtCourseService.queryById(courseId);
			Iterator<GjtTextbook> gjtTextbooks = gjtCourse.getGjtTextbookList().iterator();
			while (gjtTextbooks.hasNext()) {
				GjtTextbook textbook = gjtTextbooks.next();
				if (textbook.getTextbookId().equals(textbookId)) {
					gjtCourse.getGjtTextbookList().remove(textbook);
					break;
				}
			}
			gjtCourseService.save(gjtCourse);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback.setSuccessful(false);
			feedback.setMessage("系统异常");
		}
		return feedback;
	}

}
