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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtCourseOwnership;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtGradeSpecialty;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtSpecialtyPlan;
import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.TblSysData;
import com.gzedu.xlims.pojo.status.CourseCategory;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.TblSysDataService;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyPlanService;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyService;
import com.gzedu.xlims.service.edumanage.GjtSpecialtyPlanService;
import com.gzedu.xlims.service.edumanage.GjtStudyYearService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.exam.GjtExamSubjectNewService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.service.pcourse.PCourseServer;
import com.gzedu.xlims.service.share.GjtShareService;
import com.gzedu.xlims.service.textbook.GjtTextbookService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/edumanage/teachPlan")
public class GjtTeachPlanController {
	private static final Logger log = LoggerFactory.getLogger(GjtTeachPlanController.class);

	@Autowired
	GjtGradeService gjtGradeService;

	@Autowired
	GjtTextbookService gjtTextbookService;

	@Autowired
	GjtSpecialtyService gjtSpecialtyService;

	@Autowired
	GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtClassInfoService gjtClassInfoService;

	@Autowired
	GjtCourseService gjtCourseService;

	@Autowired
	GjtStudyYearService gjtStudyYearService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtGradeSpecialtyPlanService gjtGradeSpecialtyPlanService;

	@Autowired
	PCourseServer pCourseServer;
	@Autowired
	GjtExamSubjectNewService gjtExamSubjectNewService;

	@Autowired
	GjtTeachPlanService gjtTeachPlanService;

	@Autowired
	GjtGradeSpecialtyService gjtGradeSpecialtyService;

	@Autowired
	TblSysDataService tblSysDataService;

	@Autowired
	GjtSpecialtyPlanService gjtSpecialtyPlanService;

	@Autowired
	GjtShareService gjtShareService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, "gjtGrade.startDate", "DESC");

		String xxId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(user.getGjtOrg().getId());
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		if (request.getParameter("search_EQ_gjtGrade.gradeId") == null) {
			String currentGradeId = gjtGradeService.getCurrentGradeId(xxId);
			if (currentGradeId != null) {
				searchParams.put("EQ_gjtGrade.gradeId", currentGradeId);
				model.addAttribute("currentGradeId", currentGradeId);
			}
		}
		Page<GjtGradeSpecialty> page = gjtGradeSpecialtyService.queryAll(xxId, searchParams, pageRequst);
		searchParams.put("EQ_status", 1);
		long set = gjtGradeSpecialtyService.queryAll(xxId, searchParams, pageRequst).getTotalElements();
		searchParams.put("EQ_status", 0);
		long unset = gjtGradeSpecialtyService.queryAll(xxId, searchParams, pageRequst).getTotalElements();
		Map<String, Long> countPlanMap = new HashMap<String, Long>();
		List<GjtGradeSpecialty> content = page.getContent();
		for (GjtGradeSpecialty ggs : content) {
			long l = gjtTeachPlanService.countGjtTeachPlan(ggs.getId());
			countPlanMap.put(ggs.getId(), l);
		}
		model.addAttribute("countPlanMap", countPlanMap);
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		Map<String, String> syhyMap = commonMapService.getDates("TRADE_CODE");
		Map<String, String> zyxzMap = commonMapService.getDates("SPECIALTY_CATEGORY");
		Map<String, String> gradeMap = commonMapService.getGradeMap(xxId);// 年级
		model.addAttribute("syhyMap", commonMapService.getDates("TRADE_CODE"));// 适用行业
		model.addAttribute("subjectMap", commonMapService.getExsubjectMap()); // 学科-
		model.addAttribute("subjectCategoryMap", commonMapService.getExsubjectAndkindMap()); // 门类
		model.addAttribute("zyxzMap", zyxzMap);
		model.addAttribute("syhyMap", syhyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("pageInfo", page);
		model.addAttribute("set", set);
		model.addAttribute("unset", unset);
		model.addAttribute("gradeMap", gradeMap);
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());
		model.addAttribute("studyCenterMap", studyCenterMap);
		return "edumanage/teachPlan/list";
	}

	@RequestMapping(value = "plan", method = RequestMethod.GET)
	public String planForm(String id, String gradeId, String specialtyId, Model model, HttpServletRequest request) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		List<GjtTeachPlan> gjtTeachPlans = gjtTeachPlanService.queryGjtTeachPlan(id);
		String xxId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(user.getGjtOrg().getId());

		GjtGrade gjtGrade = gjtGradeService.queryById(gradeId);
		GjtSpecialty gjtSpecialty = gjtSpecialtyService.queryById(specialtyId);
		GjtGradeSpecialty gjtGradeSpecialty = gjtGradeSpecialtyService.findOne(id);
		model.addAttribute("status", gjtGradeSpecialty.getStatus());

		// 当前学期
		GjtGrade currentGrade = gjtGradeService.findCurrentGrade(xxId);
		Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
		int maxKkxq = 0;
		int currentTerm = 0;
		for (GjtTeachPlan plan : gjtTeachPlans) {
			if (plan.getKkxq() > maxKkxq) {
				maxKkxq = plan.getKkxq();
			}
			if (currentTerm == 0 && currentGrade.getGradeId().equals(plan.getActualGradeId())) {
				currentTerm = plan.getKkxq();
			}
			countMap.put(plan.getKkxq(), 1);
		}
		// 教学计划中找不到当前学期
		if (currentTerm == 0) {
			// 当前学期大于开设学期 说明所有教学计划都早于当前学期
			if (currentGrade.getStartDate().getTime() > gjtGrade.getStartDate().getTime()) {
				currentTerm = 99;// 设置一个较大的值
			}
		}
		List<Map<String, String>> modelList = gjtSpecialtyService.querySpecialtyCourse(specialtyId);// 专业对应课程模块

		model.addAttribute("kkxq", maxKkxq);
		model.addAttribute("currentTerm", currentTerm);
		model.addAttribute("list", modelList);
		model.addAttribute("countMap", countMap);

		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		model.addAttribute("pyccMap", pyccMap);
		Map<String, String> ksfsMap = commonMapService.getDates("ExaminationMode");// 考试方式
		model.addAttribute("ksfsMap", ksfsMap);

		Map<String, String> syhyMap = commonMapService.getDates("TRADE_CODE");
		model.addAttribute("syhyMap", syhyMap);
		model.addAttribute("gjtTeachPlans", gjtTeachPlans);
		model.addAttribute("courseTypeMap", commonMapService.getIdNameMap("CourseType"));
		model.addAttribute("courseTypeMap2", commonMapService.getDates("CourseType"));
		model.addAttribute("studentTypeMap", commonMapService.getDates("StudentType2"));// 学生类型
		model.addAttribute("categoryMap", commonMapService.getExsubjectkindMap(gjtSpecialty.getSubject())); // 门类
		model.addAttribute("subjectMap", commonMapService.getExsubjectMap()); // 学科
		model.addAttribute("gjtGrade", gjtGrade);
		model.addAttribute("gjtSpecialty", gjtSpecialty);
		String action = request.getParameter("action");
		if ("view".equals(action) || "confirm".equals(action)) {
			return "edumanage/teachPlan/planView";
		}
		return "edumanage/teachPlan/plan";
	}

	@RequestMapping(value = "updatePlan", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updatePlan(String planId, int kkxq, HttpServletRequest request) {
		Feedback feedback = null;
		try {
			GjtTeachPlan plan = gjtTeachPlanService.findOne(planId);
			if (null != plan) {
				plan.setKkxq(kkxq);
				List<GjtGrade> grades = gjtGradeService.findGradeBySize(plan.getGjtGrade(), WebConstants.MAX_TEARM);
				if (kkxq > grades.size()) {
					log.error("专业规则开课学期：" + kkxq + " 已开设学期数量：" + grades.size());
					throw new RuntimeException("请完善未来三年学期信息后再操作");
				}
				String actualGradeId = grades.get(kkxq - 1).getGradeId();
				plan.setActualGradeId(actualGradeId);
				gjtTeachPlanService.update(plan);
			}
			feedback = new Feedback(true, "修改成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "修改失败 " + e.getMessage());
		}
		return feedback;
	}

	@ResponseBody
	@RequestMapping(value = "updateReplaceCourse", method = RequestMethod.GET)
	public Feedback updateReplaceCourse(@RequestParam String planId, String courseId, HttpServletRequest request) {
		Feedback feedback = null;
		try {
			GjtTeachPlan plan = gjtTeachPlanService.findOne(planId);
			if (null != plan) {
				plan.setReplaceCourseId(courseId);
				gjtTeachPlanService.update(plan);
			}
			feedback = new Feedback(true, "修改成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "修改失败 " + e.getMessage());
		}
		return feedback;
	}

	/**
	 * 修改教学计划是否发放资料
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年7月28日 下午3:00:16
	 * @param planId
	 * @param grantData
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateGrantData", method = RequestMethod.GET)
	public Feedback updateGrantData(@RequestParam String planId, int grantData, HttpServletRequest request) {
		Feedback feedback = null;
		try {
			GjtTeachPlan plan = gjtTeachPlanService.findOne(planId);
			if (null != plan) {
				plan.setGrantData(grantData);
				gjtTeachPlanService.update(plan);
			}
			feedback = new Feedback(true, "修改成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "系统异常 " + e.getMessage());
		}
		return feedback;
	}

	@ResponseBody
	@RequestMapping(value = "updateLearningStyle", method = RequestMethod.GET)
	public Feedback updateLearningStyle(@RequestParam String planId, int learningStyle, HttpServletRequest request) {
		Feedback feedback = null;
		try {
			GjtTeachPlan plan = gjtTeachPlanService.findOne(planId);
			if (null != plan) {
				plan.setLearningStyle(learningStyle);
				gjtTeachPlanService.update(plan);
			}
			feedback = new Feedback(true, "修改成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "系统异常 " + e.getMessage());
		}
		return feedback;
	}

	/**
	 * 修改教学计划的课程类型
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年7月28日 下午3:00:57
	 * @param planId
	 * @param grantData
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateCourseCategory", method = RequestMethod.GET)
	public Feedback updateCourseCategory(@RequestParam String planId, int category, HttpServletRequest request) {
		Feedback feedback = null;
		try {
			GjtTeachPlan plan = gjtTeachPlanService.findOne(planId);
			if (null != plan) {
				plan.setCourseCategory(category);
				gjtTeachPlanService.update(plan);
			}
			feedback = new Feedback(true, "修改成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "系统异常 " + e.getMessage());
		}
		return feedback;
	}

	@RequestMapping(value = "removePlan", method = RequestMethod.POST)
	@ResponseBody
	public Feedback removePlan(String planId, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "删除成功");
		try {
			if (StringUtils.isNotBlank(planId)) {
				gjtTeachPlanService.deleteById(planId);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "删除失败");
		}
		return feedback;
	}

	/**
	 * 开设专业
	 *
	 * @param ids
	 * @param gradeId
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "addSpecialty")
	@ResponseBody
	public Feedback addSpecialty(String ids, String gradeId, HttpServletRequest request) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			GjtGrade grade = gjtGradeService.queryById(gradeId);
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			try {
				if (selectedIds != null) {
					for (String spId : selectedIds) {
						GjtSpecialty gjtSpecialty = gjtSpecialtyService.queryById(spId);
						GjtGradeSpecialty ggs = new GjtGradeSpecialty();
						ggs.setCreatedBy(user.getRealName());
						ggs.setCreatedDt(new Date());
						ggs.setGjtGrade(grade);
						ggs.setGjtSpecialty(gjtSpecialty);
						ggs.setId(UUIDUtils.random());
						ggs.setIsDeleted("N");
						ggs.setVersion(new BigDecimal(2.5));

						// 创建一个 年级和专业关系，就克隆专业的教学计划
						boolean b = gjtTeachPlanService.createTeachPlan(ggs);
						if (b) {
							gjtGradeSpecialtyService.insert(ggs);
						} else {
							return new Feedback(false, grade.getGradeName() + "开设专业失败");
						}
					}
				}
				return new Feedback(true, "开设专业成功");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return new Feedback(false, "开设专业失败" + e.getMessage());
			}
		}
		return new Feedback(false, "开设专业失败");
	}

	/**
	 * 
	 * @param model
	 * @param gradeId
	 *            年级ＩＤ
	 * @param kkxq
	 *            学期num
	 * @param specialtyId
	 *            专业ＩＤ
	 * @return
	 */
	@RequestMapping(value = "courseForm", method = RequestMethod.GET)
	public String courseForm(Model model, String gradeId, int kkxq, String specialtyId) {

		GjtTeachPlan gjtTeachPlan = new GjtTeachPlan();

		gjtTeachPlan.setGradeId(gradeId);
		gjtTeachPlan.setKkzy(specialtyId);
		gjtTeachPlan.setKkxq(kkxq);

		model.addAttribute("entity", gjtTeachPlan);
		model.addAttribute("action", "savePlan");
		Map<String, String> ksfsMap = commonMapService.getDates("ExaminationMode");// 考试方式
		model.addAttribute("ksfsMap", ksfsMap);
		return "edumanage/teachPlan/courseForm";
	}

	@RequestMapping(value = "courseFormUpdate", method = RequestMethod.GET)
	public String courseFormUpdate(Model model, String id) {
		GjtTeachPlan gjtTeachPlan = gjtTeachPlanService.findOne(id);
		model.addAttribute("entity", gjtTeachPlan);
		model.addAttribute("action", "savePlan");
		Map<String, String> ksfsMap = commonMapService.getDates("ExaminationMode");// 考试方式
		Map<String, String> examStyleMap = commonMapService.getDates("EXAM_STYLE");// 考试形式
		// Map<String, String> courseTypeMap =
		// commonMapService.getIdNameMap("CourseType");// 课程类型
		Map<String, String> courseModeMap = commonMapService.getDates("CourseType");// 课程模块
		model.addAttribute("ksfsMap", ksfsMap);
		model.addAttribute("examStyleMap", examStyleMap);
		// model.addAttribute("courseTypeMap", courseTypeMap);
		model.addAttribute("courseModeMap", courseModeMap);
		return "edumanage/teachPlan/courseForm";
	}

	@RequestMapping(value = "courseFormView", method = RequestMethod.GET)
	public String courseFormView(Model model, @RequestParam("id") String id) {
		GjtTeachPlan gjtTeachPlan = gjtTeachPlanService.findOne(id);
		model.addAttribute("entity", gjtTeachPlan);
		model.addAttribute("action", "viewPlan");
		Map<String, String> ksfsMap = commonMapService.getDates("ExaminationMode");// 考试方式
		model.addAttribute("ksfsMap", ksfsMap);
		return "edumanage/teachPlan/courseForm";
	}

	/**
	 * 添加教学计划课程
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月12日 下午4:30:15
	 * @param gradeId
	 * @param specialtyId
	 * @return
	 */
	@RequestMapping(value = "choiceCourseList", method = RequestMethod.GET)
	public String choiceCourseList(HttpServletRequest request, Model model, @RequestParam String id,
			@RequestParam String specialtyId, @RequestParam int term, String notInId) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<String> notInCourseIds = null;
		if (StringUtils.isNoneBlank(notInId)) {
			notInCourseIds = Arrays.asList(notInId.split(","));
		}
		List<GjtSpecialtyPlan> list = gjtSpecialtyPlanService.findBySpecialtyId(id, specialtyId, notInCourseIds);
		model.addAttribute("list", list);
		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次
		model.addAttribute("courseTypeMap", commonMapService.getDates("CourseType")); // 课程模块
		model.addAttribute("wsjxzkMap", commonMapService.getDates("OnlineTeaching")); // 教学方式
		model.addAttribute("courseNatureMap", commonMapService.getDates("CourseNature")); // 课程性质
		model.addAttribute("subjectMap", commonMapService.getExsubjectMap()); // 学科
		model.addAttribute("categoryMap", commonMapService.getExsubjectAndkindMap()); // 门类
		model.addAttribute("syhyMap", commonMapService.getDates("TRADE_CODE")); // 所属行业
		model.addAttribute("syzyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId())); // 所属专业
		return "edumanage/teachPlan/choiceCourseList";
	}

	/**
	 * 给教学计划添加课程
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月13日 下午4:45:47
	 * @param specialtyIds
	 * @param gradeId
	 * @param courseIds
	 * @param term
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addCourse", method = RequestMethod.POST)
	public Feedback addCourse(@RequestParam String id, @RequestParam(value = "courseIds[]") List<String> courseIds,
			@RequestParam int term) {
		Feedback feedback = new Feedback(true, "新增课程成功");
		try {

			List<GjtTeachPlan> gjtTeachPlans = gjtTeachPlanService.batchAddPlan(id, courseIds, term);
			Map<String, String> map = new HashMap<String, String>();
			for (GjtTeachPlan plan : gjtTeachPlans) {
				map.put(plan.getCourseId(), plan.getTeachPlanId());
			}
			feedback.setObj(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback.setMessage("新增课程失败");
			feedback.setSuccessful(false);
		}
		return feedback;
	}

	/**
	 * 批量修改教学计划
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年7月26日 下午6:06:07
	 * @param id
	 * @param updateCourse
	 * @param teachPlans
	 * @return
	 */
	@SysLog("教学计划-修改教学计划")
	@ResponseBody
	@RequestMapping(value = "updateTeachPlan", method = RequestMethod.POST)
	public Feedback updateTeachPlan(@RequestParam String id,
			@RequestParam(value = "teachPlans[]") List<String> teachPlans) {
		Feedback feedback = new Feedback(true, null);
		try {
			List<JSONObject> plans = new ArrayList<JSONObject>();
			for (String s : teachPlans) {
				JSONObject json = JSONObject.fromObject(s);
				plans.add(json);
			}
			gjtTeachPlanService.updateTeachPlan(id, plans);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback.setMessage("保存失败 " + e.getMessage());
			feedback.setSuccessful(false);
		}
		return feedback;
	}

	@ResponseBody
	@RequestMapping(value = "validaTeachPlan", method = RequestMethod.GET)
	public Feedback validaTeachPlan(Model model, @RequestParam String specialtyId, @RequestParam String id) {
		Feedback feedback = new Feedback(false, null);
		List<GjtTeachPlan> gjtTeachPlans = gjtTeachPlanService.queryGjtTeachPlan(id);
		int requiredScore = 0;// 必修学分
		int examScore = 0;// 中央考试学分
		Set<Integer> set = new HashSet<Integer>();
		for (GjtTeachPlan plan : gjtTeachPlans) {
			set.add(plan.getKkxq());
			if (String.valueOf(CourseCategory.必修.getNum()).equals(plan.getKcsx())) {
				requiredScore += plan.getXf();
			}
			if ("2".equals(plan.getKsdw())) {
				examScore += plan.getXf();
			}
		}
		for (int i = 0; i < set.size(); i++) {
			Integer term = i + 1;
			if (!set.contains(term)) {
				feedback.setMessage("部分学期还未添加课程");
				return feedback;
			}
		}
		GjtSpecialty specialty = gjtSpecialtyService.findBySpecialtyById(specialtyId);
		if (specialty.getBxxf() != requiredScore) {
			feedback.setMessage("设置必修课程的总学分必须等于专业的必修学分");
			return feedback;
		}
		if (specialty.getZyddksxf() > examScore) {
			feedback.setMessage("考试单位为中央的课程总学分应不少于中央电大考试学分");
			return feedback;
		}

		List<Map<String, String>> module = gjtSpecialtyService.querySpecialtyCourse(specialtyId);// 专业对应课程模块
		Map<String, String> courseTypeMap = commonMapService.getIdNameMap("CourseType");
		for (Map<String, String> map : module) {
			TblSysData tblSysData = tblSysDataService.queryById(map.get("id"));
			int totalScore = 0;// 总学分
			examScore = 0;// 中央电大考试学分
			for (GjtTeachPlan plan : gjtTeachPlans) {
				if (plan.getKclbm().equals(tblSysData.getCode())) {
					totalScore += plan.getXf();
					if ("2".equals(plan.getKsdw())) {
						examScore += plan.getXf();
					}
				}
			}
			if (totalScore < (map.get("score") == null ? 0 : Integer.parseInt(map.get("score")))) {
				feedback.setMessage("课程模块\"" + courseTypeMap.get(map.get("id")) + "\"下的课程总学分不能少于模块最低毕业学分");
				return feedback;
			}
			if (examScore < (map.get("crtvuScore") == null ? 0 : Integer.parseInt(map.get("crtvuScore")))) {
				feedback.setMessage("课程模块\"" + courseTypeMap.get(map.get("id")) + "\"考试单位为中央的课程总学分不能少于模块中央电大考试最低学分");
				return feedback;
			}
		}
		feedback.setSuccessful(true);
		return feedback;
	}

	// 发布专业
	@SysLog("教学计划-发布教学计划")
	@ResponseBody
	@RequestMapping(value = "releaseSpecialty", method = RequestMethod.GET)
	public Feedback releaseSpecialty(HttpServletRequest request, @RequestParam String id) {
		Feedback feedback = null;
		try {
			GjtGradeSpecialty entity = gjtGradeSpecialtyService.queryBy(id);
			entity.setStatus(1);
			gjtGradeSpecialtyService.update(entity);
			feedback = new Feedback(true, "发布专业成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "发布专业失败 " + e.getMessage());
		}
		return feedback;

	}

	@ResponseBody
	@RequestMapping(value = "exchangeCourse", method = RequestMethod.GET)
	public Feedback exchangeCourse(HttpServletRequest request, String planId) {
		Feedback feedback = null;
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			GjtTeachPlan gjtTeachPlan = gjtTeachPlanService.findOne(planId);
			if (null != gjtTeachPlan) {
				String temp = gjtTeachPlan.getReplaceCourseId();
				gjtTeachPlan.setReplaceCourseId(gjtTeachPlan.getCourseId());
				gjtTeachPlan.setCourseId(temp);
				GjtSpecialtyPlan gjtSpecialtyPlan = gjtSpecialtyPlanService
						.findBySpecialtyIdAndCourseId(gjtTeachPlan.getKkzy(), temp);
				gjtTeachPlan.setUpdatedDt(new Date());
				gjtTeachPlan.setUpdatedBy(user.getId());
				if (gjtSpecialtyPlan != null) {
					gjtTeachPlanService.copyTeachPlan(gjtTeachPlan, gjtSpecialtyPlan);
				} else {
					gjtTeachPlanService.update(gjtTeachPlan);
				}
			}
			feedback = new Feedback(true, "修改成功");
		} catch (Exception e) {
			feedback = new Feedback(false, "修改失败 " + e.getMessage());
		}
		return feedback;
	}

	@RequestMapping(value = "choiceTextbookList", method = RequestMethod.GET)
	public String choiceTextbookList(String courseId, @RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		searchParams.put("EQ_textbookType", 1);
		searchParams.put("EQ_courseId", courseId);
		Page<GjtTextbook> pageInfo = gjtTextbookService.findAll(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		return "edumanage/teachPlan/choiceTextbookList";
	}

	@ResponseBody
	@RequestMapping(value = "updateTextbook", method = RequestMethod.POST)
	public Feedback updateTextbook(@RequestParam String planId,
			@RequestParam(value = "textbookIds[]", required = false) List<String> textbookIds) {
		Feedback feedback = new Feedback(true, "新增课程成功");
		try {
			gjtTeachPlanService.updateTextbook(planId, textbookIds);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback.setMessage("系统异常，新增教材失败");
			feedback.setSuccessful(false);
		}
		return feedback;
	}

	@RequestMapping(value = "choiceReplaceCourseList", method = RequestMethod.GET)
	public String choiceReplaceCourseList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, String notInCoursIds, String gradeSpecialtyId) {
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
		Set<String> notInCourseList = new HashSet<String>();
		if (StringUtils.isNotEmpty(gradeSpecialtyId)) {
			List<GjtTeachPlan> plans = gjtTeachPlanService.queryGjtTeachPlan(gradeSpecialtyId);
			for (GjtTeachPlan plan : plans) {
				notInCourseList.add(plan.getCourseId());
				if (StringUtils.isEmpty(notInCoursIds) && StringUtils.isNotEmpty(plan.getReplaceCourseId())) {
					notInCourseList.add(plan.getReplaceCourseId());
				}
			}
		}
		if (StringUtils.isNotBlank(notInCoursIds)) {
			notInCourseList.addAll(Arrays.asList(notInCoursIds.split(",")));
		}
		if (!notInCourseList.isEmpty()) {
			searchParams.put("notIncoursIds", new ArrayList<String>(notInCourseList));
		}
		Page<GjtCourse> pageInfo = gjtCourseService.queryAllAndShare(user.getGjtOrg().getId(), searchParams, pageRequst,
				coursIds);
		model.addAttribute("user", user);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次
		model.addAttribute("courseNatureMap", commonMapService.getDates("CourseNature")); // 课程性质
		model.addAttribute("wsjxzkMap", commonMapService.getDates("OnlineTeaching")); // 教学方式
		model.addAttribute("syhyMap", commonMapService.getDates("TRADE_CODE")); // 所属行业
		model.addAttribute("syzyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId())); // 所属专业
		return "edumanage/teachPlan/choiceReplaceCourseList";
	}

	/**
	 * 导出数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "expCoursePlan")
	@SysLog("教学计划-导出数据")
	@ResponseBody
	public void expCoursePlan(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		String xxId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(user.getGjtOrg().getId());
		Map<String, Object> formMap = Servlets.getParametersStartingWith(request, "search_");
		formMap.put("xxId", xxId);
		try {
			Workbook wb = gjtTeachPlanService.expCoursePlan(formMap, request, response);
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(("教学计划.xls").getBytes("UTF-8"), "ISO8859-1"));
			wb.write(response.getOutputStream());
			request.getSession().setAttribute(user.getSjh(), "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
