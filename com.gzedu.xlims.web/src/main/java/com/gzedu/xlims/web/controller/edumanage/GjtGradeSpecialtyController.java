/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtGradeSpecialty;
import com.gzedu.xlims.pojo.GjtGradeSpecialtyPlan;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.dto.GjtSpecialtyDto;
import com.gzedu.xlims.pojo.exam.GjtExamSubjectNew;
import com.gzedu.xlims.pojo.status.CourseCategory;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.pojo.status.ExamType;
import com.gzedu.xlims.pojo.status.TermType;
import com.gzedu.xlims.pojo.system.StudyYear;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyPlanService;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyService;
import com.gzedu.xlims.service.edumanage.GjtStudyYearService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.exam.GjtExamSubjectNewService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.service.pcourse.PCourseServer;
import com.gzedu.xlims.service.textbook.GjtTextbookService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/edumanage/gradespecialty")
public class GjtGradeSpecialtyController {

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
	GjtGradeSpecialtyService gjtGradeSpecialtyService;

	@Autowired
	GjtGradeSpecialtyPlanService gjtGradeSpecialtyPlanService;

	@Autowired
	PCourseServer pCourseServer;
	@Autowired
	GjtExamSubjectNewService gjtExamSubjectNewService;

	@Autowired
	GjtTeachPlanService gjtTeachPlanService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<GjtGradeSpecialty> page = gjtGradeSpecialtyService.queryAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);

		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业名称

		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pageInfo", page);

		return "edumanage/gradespecialty/list";
	}

	@RequestMapping(value = "plan", method = RequestMethod.GET)
	public String planForm(String gradeId, String specialtyId, Model model, HttpServletRequest request) {

		GjtGrade gjtGrade = gjtGradeService.queryById(gradeId);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		List<GjtGradeSpecialtyPlan> gradeSpecialtyPlans = gjtGradeSpecialtyPlanService.queryGradeSpecialtyPlan(gradeId,
				specialtyId);
		String orgId = user.getGjtOrg().getId();

		Map<String, String> employeeMap = commonMapService.getEmployeeMap(orgId, EmployeeTypeEnum.辅导教师);
		Map<String, String> courseMap = commonMapService.getCourseMap(orgId);
		TermType[] termType = TermType.values();
		CourseCategory[] courseCategory = CourseCategory.values();
		ExamType[] examType = ExamType.values();

		GjtSpecialty gjtSpecialty = gjtSpecialtyService.queryById(specialtyId);

		Map<String, String> courseTypeMap = commonMapService.getCourseTypeMap();

		// 创建时间排序
		Collections.sort(gradeSpecialtyPlans, new Comparator<GjtGradeSpecialtyPlan>() {
			@Override
			public int compare(GjtGradeSpecialtyPlan o1, GjtGradeSpecialtyPlan o2) {
				return o2.getCreatedDt().before(o1.getCreatedDt()) ? -1 : 1;
			}
		});
		// 学期排序
		Collections.sort(gradeSpecialtyPlans, new Comparator<GjtGradeSpecialtyPlan>() {
			@Override
			public int compare(GjtGradeSpecialtyPlan o1, GjtGradeSpecialtyPlan o2) {
				return o2.getTermTypeCode() > o1.getTermTypeCode() ? -1 : 1;
			}
		});

		Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
		int count = 0;
		for (GjtGradeSpecialtyPlan plan : gradeSpecialtyPlans) {
			if (countMap.containsKey(plan.getTermTypeCode())) {
				count = countMap.get(plan.getTermTypeCode()) + 1;
				countMap.put(plan.getTermTypeCode(), count);
			} else {
				countMap.put(plan.getTermTypeCode(), 1);
			}
		}
		model.addAttribute("countMap", countMap);

		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		model.addAttribute("pyccMap", pyccMap);

		model.addAttribute("gradeSpecialtyPlans", gradeSpecialtyPlans);
		model.addAttribute("employeeMap", employeeMap);
		model.addAttribute("courseTypeMap", courseTypeMap);
		model.addAttribute("examType", examType);
		model.addAttribute("courseCategory", courseCategory);
		model.addAttribute("courseMap", courseMap);
		model.addAttribute("termType", termType);
		model.addAttribute("action", "plan");

		// 年级
		model.addAttribute("gjtGrade", gjtGrade);
		// 专业
		model.addAttribute("gjtSpecialty", gjtSpecialty);
		// 年级专业的教学计划

		return "edumanage/gradespecialty/plan";
	}

	@RequestMapping(value = "savePlan", method = RequestMethod.POST)
	@ResponseBody
	public Feedback savePlan(@Valid GjtGradeSpecialtyPlan entity, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		String id = entity.getId();
		GjtGradeSpecialtyPlan plan = new GjtGradeSpecialtyPlan();
		if (StringUtils.isNotBlank(id)) {
			plan = gjtGradeSpecialtyPlanService.queryBy(id);
		}
		GjtGrade gjtGrade = gjtGradeService.queryById(entity.getGradeId());
		int termTypeCode = entity.getTermTypeCode();
		int studyYearCode = StudyYear.getCode(gjtGrade.getEnterDt(), termTypeCode);
		plan.setStudyYearCode(studyYearCode);
		plan.setCreatedDt(new Date());
		plan.setCreatedBy(user.getRealName());

		plan.setGradeId(entity.getGradeId());
		plan.setStudyYearCode(studyYearCode);
		plan.setTermTypeCode(termTypeCode);
		plan.setSpecialtyId(entity.getSpecialtyId());
		plan.setXxId(user.getGjtOrg().getId());
		plan.setCourseId(entity.getCourseId());// 课程
		// plan.setCounselorId(values.get(1));// 辅导教师
		plan.setCourseCategory(entity.getCourseCategory());// 课程属性
		plan.setCourseTypeId(entity.getCourseTypeId());// 课程类别
		plan.setScore(entity.getScore());// 学分
		plan.setHours(entity.getHours());// 学时
		plan.setExamType(entity.getExamType());// 考试方式
		plan.setStudyRatio(entity.getStudyRatio());// 学习占比
		plan.setExamRatio(entity.getExamRatio());// 考试占比
		plan.setSubjectId(entity.getSubjectId());
		Feedback feedback = new Feedback(true, "添加成功");

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
			plan.setGjtTextbookList1(gjtTextbook1);
			plan.setGjtTextbookList2(gjtTextbook2);
		}

		try {
			if (StringUtils.isNotBlank(id)) {
				gjtGradeSpecialtyPlanService.update(plan);
				feedback = new Feedback(true, "更新成功", plan.getId());
			} else {
				gjtGradeSpecialtyPlanService.insert(plan);
				feedback = new Feedback(true, "添加成功", plan.getId());
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "添加失败!关联的课程已存在!");
		}

		return feedback;
	}

	@RequestMapping(value = "removePlan", method = RequestMethod.POST)
	@ResponseBody
	public Feedback removePlan(String planId, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "删除成功");
		try {
			if (StringUtils.isNotBlank(planId)) {
				gjtGradeSpecialtyPlanService.delete(planId);
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "删除失败");
		}
		return feedback;
	}

	// 实施计划,创建学年度课程班级
	@RequestMapping(value = "createClass", method = RequestMethod.POST)
	@ResponseBody
	public Feedback createClass(String gradeId, String specialtyId, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "实施计划成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			gjtClassInfoService.insertCreateClass(user.getGjtOrg().getId(), gradeId, specialtyId);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			feedback = new Feedback(false, "实施计划失败");
		}

		return feedback;
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids) throws IOException {

		try {
			if (StringUtils.isNotBlank(ids)) {
				String[] selectedIds = ids.split(",");
				for (String id : selectedIds) {
					GjtGradeSpecialty gradeSpecialty = gjtGradeSpecialtyService.queryBy(id);
					String gradeId = gradeSpecialty.getGjtGrade().getGradeId();
					String specialtyId = gradeSpecialty.getGjtSpecialty().getSpecialtyId();
					gjtGradeSpecialtyPlanService.removeGradeSpecialtyPlan(gradeId, specialtyId);
				}
				gjtGradeSpecialtyService.delete(Arrays.asList(selectedIds)); // 删除年级专业
				return new Feedback(true, "删除成功");
			}
		} catch (Exception e) {
			return new Feedback(false, "删除失败");
		}
		return new Feedback(false, "删除失败");
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
						ggs.setCreatedDt(DateUtils.getNowTime());
						ggs.setGjtGrade(grade);
						ggs.setGjtSpecialty(gjtSpecialty);
						ggs.setId(UUIDUtils.random());
						ggs.setIsDeleted("N");
						ggs.setVersion(new BigDecimal(2.5));

						// 创建一个 年级和专业关系，就克隆专业的教学计划
						boolean b = gjtGradeSpecialtyPlanService.createGradeSpecialtyPlan(grade, gjtSpecialty);
						if (b) {
							gjtGradeSpecialtyService.insert(ggs);
						} else {
							return new Feedback(false, grade.getGradeName() + "开设专业失败");
						}
					}
				}
				return new Feedback(true, "开设专业成功");
			} catch (Exception e) {
				e.printStackTrace();
				return new Feedback(false, "开设专业失败");
			}
		}
		return new Feedback(false, "开设专业失败");
	}

	@RequestMapping(value = "courseForm", method = RequestMethod.GET)
	public String courseForm(Model model, String gradeId, int termTypeCode, String specialtyId) {

		GjtGradeSpecialtyPlan gjtSpecialtyPlan = new GjtGradeSpecialtyPlan();
		gjtSpecialtyPlan.setGradeId(gradeId);
		gjtSpecialtyPlan.setSpecialtyId(specialtyId);
		gjtSpecialtyPlan.setTermTypeCode(termTypeCode);

		model.addAttribute("entity", gjtSpecialtyPlan);
		model.addAttribute("action", "savePlan");
		Map<String, String> ksfsMap = commonMapService.getDates("ExaminationMode");// 考试方式
		model.addAttribute("ksfsMap", ksfsMap);
		return "edumanage/gradespecialty/courseForm";
	}

	@RequestMapping(value = "courseFormUpdate", method = RequestMethod.GET)
	public String courseFormUpdate(Model model, String id) {
		GjtGradeSpecialtyPlan gjtSpecialtyPlan = gjtGradeSpecialtyPlanService.queryBy(id);
		model.addAttribute("entity", gjtSpecialtyPlan);
		model.addAttribute("action", "savePlan");
		Map<String, String> ksfsMap = commonMapService.getDates("ExaminationMode");// 考试方式
		model.addAttribute("ksfsMap", ksfsMap);
		return "edumanage/gradespecialty/courseForm";
	}

	@RequestMapping(value = "courseFormView", method = RequestMethod.GET)
	public String courseFormView(Model model, @RequestParam("id") String id) {
		GjtGradeSpecialtyPlan gjtSpecialtyPlan = gjtGradeSpecialtyPlanService.queryBy(id);
		model.addAttribute("entity", gjtSpecialtyPlan);
		model.addAttribute("action", "viewPlan");
		Map<String, String> ksfsMap = commonMapService.getDates("ExaminationMode");// 考试方式
		model.addAttribute("ksfsMap", ksfsMap);
		return "edumanage/gradespecialty/courseForm";
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

		return "edumanage/gradespecialty/choiceExamList";
	}

	@RequestMapping(value = "querySpecialty", method = RequestMethod.GET)
	public String querySpecialty(Model model, HttpServletRequest request, String gradeId) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		Map<String, String> copyGradeMap = gjtGradeSpecialtyService.findSpecialtyGrade(user.getGjtOrg().getId());// 年级
		if (StringUtils.isBlank(gradeId) && !gradeMap.isEmpty()) {
			gradeId = gjtGradeService.getCurrentGradeId(user.getGjtOrg().getId());
		}

		List<GjtGradeSpecialty> gjtGradeSpecialtyList = gjtGradeSpecialtyService.findByGradeId(gradeId);
		for (GjtGradeSpecialty g : gjtGradeSpecialtyList) {
			System.out.println(g.getGjtGrade().getGradeId() + "--->" + g.getGjtSpecialty().getSpecialtyId());
		}
		model.addAttribute("gradeMap", gradeMap);
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 层次
		model.addAttribute("pyccMap", JSONObject.fromObject(pyccMap));
		Map<String, String> syhyMap = commonMapService.getDates("TRADE_CODE");// 使用行业
		model.addAttribute("syhyMap", JSONObject.fromObject(syhyMap));
		model.addAttribute("subjectMap", JSONObject.fromObject(commonMapService.getExsubjectMap())); // 学科
		model.addAttribute("subjectCategoryMap", JSONObject.fromObject(commonMapService.getExsubjectAndkindMap())); // 门类
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());
		model.addAttribute("studyCenterMap", JSONObject.fromObject(studyCenterMap));
		model.addAttribute("gradeId", gradeId);
		model.addAttribute("gjtGradeSpecialtyList", gjtGradeSpecialtyList);
		model.addAttribute("copyGradeMap", copyGradeMap);
		return "edumanage/gradespecialty/specialty";
	}

	@RequestMapping(value = "addGradeSpecialty", method = RequestMethod.GET)
	public String addGradeSpecialty(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, String gradeId) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_status", 3);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Page<GjtSpecialty> page = gjtSpecialtyService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);
		model.addAttribute("syhyMap", commonMapService.getDates("TRADE_CODE"));//适用行业
		model.addAttribute("pyccMap", commonMapService.getPyccMap());//专业层次
		model.addAttribute("subjectMap", commonMapService.getExsubjectMap()); // 学科
		model.addAttribute("subjectCategoryMap", commonMapService.getExsubjectAndkindMap()); // 门类
		model.addAttribute("gradeId", gradeId);
		model.addAttribute("pageInfo", page);
		return "edumanage/gradespecialty/addGradeSpecialty";
	}

	/**
	 * 开设专业
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年5月12日 下午4:24:57
	 * @param request
	 * @param gradeId
	 * @param copyGradeId
	 * @param gradeSpecialties
	 * @return
	 */
	@SysLog("开设专业-开设专业")
	@ResponseBody
	@RequestMapping(value = "saveGradeSpecialty", method = RequestMethod.POST)
	public Feedback saveGradeSpecialty(HttpServletRequest request, String gradeId, String copyGradeId,
			@RequestParam(value = "gradeSpecialties[]") List<String> gradeSpecialties) {
		Feedback feedback = null;
		try {
			List<JSONObject> list = new ArrayList<JSONObject>();
			for (String s : gradeSpecialties) {
				JSONObject jsonObject = JSONObject.fromObject(s);
				list.add(jsonObject);
			}
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			gjtGradeSpecialtyService.saveGradeSpecialty(gradeId, list, user.getId(), copyGradeId);
			feedback = new Feedback(true, "开设专业成功");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			feedback = new Feedback(false, "开设专业失败 " + e.getMessage());
		}
		return feedback;

	}

	@ResponseBody
	@RequestMapping(value = "findSpecialtyByGradeId", method = RequestMethod.GET)
	public List<GjtSpecialtyDto> findSpecialtyByGradeId(String gradeId) {
		List<GjtSpecialtyDto> specialties = gjtSpecialtyService.findSpecialtyDtoByGradeId(gradeId);
		return specialties;
	}
}
