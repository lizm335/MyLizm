/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtClassStudent;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.api.ApiOucSyncService;
import com.gzedu.xlims.service.edumanage.GjtStudyYearService;
import com.gzedu.xlims.service.edumanage.GjtTeachCourseService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 
 * 功能说明：课程班级管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月8日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Controller
@RequestMapping("/edumanage/courseclass")
public class GjtCourseClassController {

	private static final Logger log = LoggerFactory.getLogger(GjtCourseClassController.class);

	@Autowired
	GjtClassInfoService gjtClassInfoService;

	@Autowired
	GjtStudyYearService gjtStudyYearService;

	@Autowired
	GjtGradeService gjtGradeService;

	@Autowired
	GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	GjtClassStudentService gjtClassStudentService;
	@Autowired
	private GjtTeachPlanService gjtTeachPlanService;
	@Autowired
	ApiOucSyncService apiOucSyncService;
	
	@Autowired
	GjtTeachCourseService gjtTeachCourseService;

	// @Autowired
	// GjtClassTeacherService gjtClassTeacherService;

	// 查询所有课程班级
	@RequestMapping(value = "getClassList", method = RequestMethod.GET)
	public String getClassList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, String judge,
			HttpServletRequest request) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);

		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("XX_ID", user.getGjtOrg().getId());

		if (EmptyUtils.isNotEmpty(user.getGjtOrg().getGjtStudyCenter())
				&& EmptyUtils.isNotEmpty(user.getGjtOrg().getGjtStudyCenter().getId())) {
			searchParams.put("XXZX_ID", user.getGjtOrg().getGjtStudyCenter().getId());
		}

		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);

		Map<String, String> courseMap = commonMapService.getCourseMap(user.getGjtOrg().getId());
		model.addAttribute("courseMap", courseMap);

		// 默认学期
		if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			GjtGrade grade = gjtGradeService.findCurrentGrade(user.getGjtOrg().getId());
			if (EmptyUtils.isNotEmpty(grade)) {
				searchParams.put("GRADE_ID", grade.getGradeId());
			}
		} else if ("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			searchParams.put("GRADE_ID", "");
		}
		Page page = gjtClassInfoService.getClassList(searchParams, pageRequst);
		model.addAttribute("pageInfo", page);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnCreate", subject.isPermitted("/edumanage/courseclass/getClassList$create"));
		model.addAttribute("isBtnSettingRule", subject.isPermitted("/edumanage/courseclass/getClassList$settingRule"));
		model.addAttribute("isBtnView", subject.isPermitted("/edumanage/courseclass/getClassList$viewClassStudent"));
		model.addAttribute("isBtnAllocationFD",
				subject.isPermitted("/edumanage/courseclass/getClassList$allocationFD"));
		model.addAttribute("isBtnAllocationDD",
				subject.isPermitted("/edumanage/courseclass/getClassList$allocationDD"));
		model.addAttribute("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));

		return "edumanage/courseclass/getClassList";
	}

	/**
	 * 统计项查询
	 * 
	 * @return
	 */
	@RequestMapping(value = "getClassCount")
	@ResponseBody
	public Map getClassCount(HttpServletRequest request) {
		Map resultMap = new HashMap();
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map searchParams = Servlets.getParametersStartingWith(request, "");

			if ("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
				searchParams.put("GRADE_ID", "");
			}

			String xxId = user.getGjtOrg().getId();
			searchParams.put("XX_ID", xxId);

			if (EmptyUtils.isNotEmpty(user.getGjtOrg().getGjtStudyCenter())
					&& EmptyUtils.isNotEmpty(user.getGjtOrg().getGjtStudyCenter().getId())) {
				searchParams.put("XXZX_ID", user.getGjtOrg().getGjtStudyCenter().getId());
			}

			// 查询条件统计项
			searchParams.put("NOTEACHER", "");
			searchParams.put("TIME_FLG", "1");
			int time_flg1 = gjtClassInfoService.getClassCount(searchParams);
			resultMap.put("TIME_FLG1", time_flg1);

			searchParams.put("TIME_FLG", "2");
			int time_flg2 = gjtClassInfoService.getClassCount(searchParams);
			resultMap.put("TIME_FLG2", time_flg2);

			searchParams.put("TIME_FLG", "3");
			int time_flg3 = gjtClassInfoService.getClassCount(searchParams);
			resultMap.put("TIME_FLG3", time_flg3);

			searchParams.put("TIME_FLG", "");
			searchParams.put("NOTEACHER", "1");
			int noteacher1 = gjtClassInfoService.getClassCount(searchParams);
			resultMap.put("NOTEACHER1", noteacher1);

			searchParams.put("NOTEACHER", "2");
			int noteacher2 = gjtClassInfoService.getClassCount(searchParams);
			resultMap.put("NOTEACHER2", noteacher2);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	// 查询所有课程班级
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, String judge,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_classType", "course");// 课程班级管理
		if (StringUtils.isNotBlank(judge)) {// 代办事项中，查询出没有分配辅导老师的班级
			searchParams.put("ISNULL_gjtBzr", "1");
			model.addAttribute("judge", judge);
		}

		Object gradeId = searchParams.get("EQ_actualGrade.gradeId");
		if (gradeId == null) { // 默认选择当前学期
			GjtGrade grade = gjtGradeService.findCurrentGrade(user.getGjtOrg().getId());
			if (grade != null) {
				searchParams.put("EQ_actualGrade.gradeId", grade.getGradeId());
				model.addAttribute("gradeId", grade.getGradeId());
			}
		} else if ("-1".equals(gradeId.toString())) {
			searchParams.remove("EQ_actualGrade.gradeId");
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.putAll(searchParams);
		map1.put("EQ_classType", "course");

		long all = gjtClassInfoService.queryAll(user.getGjtOrg().getId(), map1, pageRequst).getTotalElements();
		model.addAttribute("all", all);

		map1.put("GT_actualGrade.startDate", sdf.format(new Date())); // 待开班
		long status1 = gjtClassInfoService.queryAll(user.getGjtOrg().getId(), map1, pageRequst).getTotalElements();
		model.addAttribute("status1", status1);

		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.putAll(searchParams);
		map2.put("EQ_classType", "course");
		map2.put("LTE_actualGrade.startDate", sdf.format(new Date())); // 开班中
		map2.put("GTE_actualGrade.endDate", sdf.format(new Date()));
		long status2 = gjtClassInfoService.queryAll(user.getGjtOrg().getId(), map2, pageRequst).getTotalElements();
		model.addAttribute("status2", status2);

		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.putAll(searchParams);
		map3.put("EQ_classType", "course");
		map3.put("LT_actualGrade.endDate", sdf.format(new Date())); // 已结束
		long status3 = gjtClassInfoService.queryAll(user.getGjtOrg().getId(), map3, pageRequst).getTotalElements();
		model.addAttribute("status3", status3);

		Map<String, Object> map4 = new HashMap<String, Object>();
		map4.putAll(searchParams);
		map4.put("EQ_classType", "course");
		map4.put("ISNULL_gjtDuDao", "1"); // 未设置督导教师
		long nodd = gjtClassInfoService.queryAll(user.getGjtOrg().getId(), map4, pageRequst).getTotalElements();
		model.addAttribute("nodd", nodd);

		Map<String, Object> map5 = new HashMap<String, Object>();
		map5.putAll(searchParams);
		map5.put("EQ_classType", "course");
		map5.put("ISNULL_gjtBzr", "1"); // 未设置辅导教师
		long nobzr = gjtClassInfoService.queryAll(user.getGjtOrg().getId(), map5, pageRequst).getTotalElements();
		model.addAttribute("nobzr", nobzr);

		String status = request.getParameter("status");
		if (status != null) {
			if ("1".equals(status)) { // 待开班
				searchParams.put("GT_actualGrade.startDate", sdf.format(new Date()));
			} else if ("2".equals(status)) { // 开班中
				searchParams.put("LTE_actualGrade.startDate", sdf.format(new Date()));
				searchParams.put("GTE_actualGrade.endDate", sdf.format(new Date()));
			} else if ("3".equals(status)) { // 已结束
				searchParams.put("LT_actualGrade.endDate", sdf.format(new Date()));
			}
		}

		String noTeacher = request.getParameter("noTeacher");
		if (noTeacher != null) {
			if ("1".equals(noTeacher)) { // 未设置督导教师
				searchParams.put("ISNULL_gjtDuDao", "1");
			} else if ("2".equals(noTeacher)) { // 未设置辅导教师
				searchParams.put("ISNULL_gjtBzr", "1");
			}
		}

		Page<GjtClassInfo> page = gjtClassInfoService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);

		List<String> classIds = new ArrayList<String>();

		List<GjtClassInfo> list = page.getContent();
		if (list != null && list.size() > 0) {
			// 设置平均进度和平均分，从学习平台获取
			// gjtClassInfoService.setProgressAvg(list);
			// gjtClassInfoService.setScoreAvg(list);

			for (int i = 0; i < list.size(); i++) {
				GjtClassInfo gjtClassInfo = list.get(i);
				classIds.add(gjtClassInfo.getClassId());
				// 设置状态
				Date now = new Date();
				GjtGrade actualGrade = gjtClassInfo.getActualGrade();
				if (actualGrade != null) {
					if (actualGrade.getStartDate() != null && actualGrade.getEndDate() != null) {
						if (actualGrade.getEndDate().compareTo(now) < 0) {
							gjtClassInfo.setStatus(3);
						} else if (actualGrade.getStartDate().compareTo(now) > 0) {
							gjtClassInfo.setStatus(1);
							long n = actualGrade.getStartDate().getTime() - now.getTime();
							gjtClassInfo.setDay(n / (3600 * 24 * 1000));
						} else {
							gjtClassInfo.setStatus(2);
							long n = actualGrade.getEndDate().getTime() - now.getTime();
							gjtClassInfo.setDay(n / (3600 * 24 * 1000));
						}
					}
				}
			}

			// 设置班级人数
			if (EmptyUtils.isNotEmpty(classIds)) {
				Map<String, Integer> map = gjtClassInfoService.queryClassPeople(classIds);
				for (GjtClassInfo gjtClassInfo : page) {
					gjtClassInfo.setNum(map.get(gjtClassInfo.getClassId()));
				}
			}
		}

		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);

		Map<String, String> courseMap = commonMapService.getCourseMap(user.getGjtOrg().getId());
		model.addAttribute("courseMap", courseMap);

		model.addAttribute("pageInfo", page);

		String prefix = Servlets.encodeParameterStringWithPrefix(searchParams, "search_");
		model.addAttribute("searchParams", prefix);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnCreate", subject.isPermitted("/edumanage/courseclass/list$create"));
		model.addAttribute("isBtnSettingRule", subject.isPermitted("/edumanage/courseclass/list$settingRule"));
		model.addAttribute("isBtnView", subject.isPermitted("/edumanage/courseclass/list$viewClassStudent"));
		model.addAttribute("isBtnAllocationFD", subject.isPermitted("/edumanage/courseclass/list$allocationFD"));
		model.addAttribute("isBtnAllocationDD", subject.isPermitted("/edumanage/courseclass/list$allocationDD"));

		return "edumanage/courseclass/list";
	}

	// 查询分班的学员 exists 1 已分班、0 未分班
	@RequestMapping(value = "queryNoBrvbar", method = RequestMethod.GET)
	public String queryNoBrvbar(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize, Model model,
			HttpServletRequest request, String classId, String className, int studyYearCode, String courseId,
			@RequestParam(value = "isExists", defaultValue = "0") int isExists) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		boolean exists = false;
		if (isExists == 0) {
			exists = false;// 未分班的学员
		} else {
			exists = true;// 已分班的学员
		}

		Page<GjtStudentInfo> page = gjtStudentInfoService.queryByCourseIdAndStudyYearId(courseId, studyYearCode, exists,
				searchParams, pageRequst);

		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		Map<String, String> orgMap = commonMapService.getOrgMap(user.getId());
		Map<String, String> counselorMap = commonMapService.getEmployeeMap(user.getGjtOrg().getId(),
				EmployeeTypeEnum.辅导教师);// 辅导教师
		Map<String, String> inspectorMap = commonMapService.getEmployeeMap(user.getGjtOrg().getId(),
				EmployeeTypeEnum.督导教师);// 督导教师

		model.addAttribute("isExists", isExists);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("counselorMap", counselorMap);
		model.addAttribute("inspectorMap", inspectorMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("orgMap", orgMap);
		model.addAttribute("studyCenterMap", orgMap);
		model.addAttribute("classId", classId);
		model.addAttribute("studyYearCode", studyYearCode);
		model.addAttribute("courseId", courseId);
		model.addAttribute("className", className);
		model.addAttribute("pageInfo", page);

		return "edumanage/courseclass/nobrvbar";
	}

	// 添加学员进入某个班级
	@RequestMapping(value = "brvbar")
	@ResponseBody
	public Feedback brvbar(String ids, String classId) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			GjtClassInfo gc = gjtClassInfoService.queryById(classId);
			GjtGrade gjtGrade = gc.getGjtGrade();
			GjtOrg gjtOrg = gc.getGjtOrg();
			GjtSchoolInfo gjtSchoolInfo = gc.getGjtSchoolInfo();
			GjtStudyCenter gjtStudyCenter = gc.getGjtStudyCenter();
			try {
				for (String studentid : selectedIds) {
					GjtClassStudent item = new GjtClassStudent();
					item.setClassStudentId(UUIDUtils.random());
					item.setGjtClassInfo(gc);
					item.setGjtStudentInfo(new GjtStudentInfo(studentid));
					item.setCreatedDt(DateUtils.getNowTime());
					item.setIsDeleted("N");
					item.setGjtGrade(gjtGrade);
					item.setGjtOrg(gjtOrg);
					item.setGjtSchoolInfo(gjtSchoolInfo);
					item.setGjtStudyCenter(gjtStudyCenter);
					item.setIsEnabled("1");
					item.setVersion(BigDecimal.valueOf(2.5));
					item.setOrgCode(gjtOrg.getCode());
					gjtClassStudentService.save(item);
				}
				return new Feedback(true, "为学员分班成功");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return new Feedback(false, "班级添加学员失败");
			}
		}
		return new Feedback(false, "班级添加学员失败");
	}

	// 单个删除和多个删除
	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids) {
		Feedback fb = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				gjtClassInfoService.deleteById(selectedIds);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				fb = new Feedback(false, "删除失败");
			}
		}
		return fb;
	}

	// 查询出院校所有辅导教师.督导教师，所有的班级，进行1对多批量分配 没有页面，还没用上
	@RequestMapping(value = "allotHeadTeacher", method = RequestMethod.GET)
	public String allotHeadTeacher(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map<String, String> classInfoMap = commonMapService.getClassInfoMap(user.getGjtOrg().getId(), "course"); // 学校

		Map<String, String> counselorMap = commonMapService.getEmployeeMap(user.getGjtOrg().getId(),
				EmployeeTypeEnum.辅导教师);// 辅导教师
		Map<String, String> inspectorMap = commonMapService.getEmployeeMap(user.getGjtOrg().getId(),
				EmployeeTypeEnum.督导教师);// 督导教师
		model.addAttribute("classInfoMap", classInfoMap);
		model.addAttribute("counselorMap", counselorMap);
		model.addAttribute("inspectorMap", inspectorMap);
		return "edumanage/courseclass/allotHeadTeacher";
	}

	// 打开页面，传入选中的班级ID，已经所有的班主任信息
	@RequestMapping(value = "editeAllot", method = RequestMethod.GET)
	public String editeAllot(Model model, String ids, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> counselorMap = commonMapService.getEmployeeMap(user.getGjtOrg().getId(),
				EmployeeTypeEnum.辅导教师);// 辅导教师
		Map<String, String> inspectorMap = commonMapService.getEmployeeMap(user.getGjtOrg().getId(),
				EmployeeTypeEnum.督导教师);// 督导教师
		List<String> list = new ArrayList<String>();
		if (ids != null) {
			String[] str = ids.split(",");
			for (String string : str) {
				list.add(string);
			}
		}
		List<Map<String, String>> classInfoList = gjtClassInfoService.queryClassInfo(list);
		model.addAttribute("classInfoMap", classInfoList);
		model.addAttribute("counselorMap", counselorMap);
		model.addAttribute("inspectorMap", inspectorMap);
		return "edumanage/courseclass/editeAllot";
	}

	// 给选中的班级，批量的更改为一个班主任
	@RequestMapping(value = "allotHeadTeacherUpdate", method = RequestMethod.POST)
	public String allotHeadTeacherUpdate(String ids, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		String counselorId = request.getParameter("counselorId");
		String inspectorId = request.getParameter("inspectorId");
		GjtEmployeeInfo counselor = gjtEmployeeInfoService.queryById(counselorId);// 辅导
		GjtEmployeeInfo inspector = gjtEmployeeInfoService.queryById(inspectorId); // 督导
		Feedback feedback = new Feedback(false, "分配失败");
		try {
			if (ids != null) {
				String[] classIds = ids.split(",");
				for (String id : classIds) {
					GjtClassInfo gjtClassInfo = gjtClassInfoService.queryById(id);
					gjtClassInfo.setGjtBzr(counselor);// 班主任和辅导一样
					gjtClassInfo.setGjtDuDao(inspector);// 督导
					gjtClassInfoService.updateEntity(gjtClassInfo);
				}
			}
			feedback = new Feedback(true, "分配成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "网络异常");
		}

		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/courseclass/list";
	}

	// 老师树结构
	@RequestMapping(value = "teacherChoiceTree/{id}/{classId}", method = RequestMethod.GET)
	public String teacherChoiceTree(@PathVariable("id") String type, @PathVariable("classId") String classId,
			ModelMap model, HttpServletRequest request) {
		model.addAttribute("type", type);
		model.addAttribute("classId", classId);

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String xx_id = user.getGjtOrg().getId();

		GjtClassInfo gjtClassInfo = gjtClassInfoService.queryById(classId);

		if ("2".equals(type)) {// 辅导
			List<GjtEmployeeInfo> gjtEmployeeInfoList = gjtEmployeeInfoService.queryAllNotClassEmployee(2, classId,
					xx_id);
			model.addAttribute("gjtEmployeeInfoList", gjtEmployeeInfoList);
			GjtEmployeeInfo employeeInfo = gjtClassInfo.getGjtBzr();
			model.addAttribute("employeeInfo", employeeInfo);
		} else {// 督导
			List<GjtEmployeeInfo> gjtEmployeeInfoList = gjtEmployeeInfoService.queryAllNotClassEmployee(4, classId,
					xx_id);
			model.addAttribute("gjtEmployeeInfoList", gjtEmployeeInfoList);
			GjtEmployeeInfo employeeInfo = gjtClassInfo.getGjtDuDao();
			model.addAttribute("employeeInfo", employeeInfo);
		}

		return "/edumanage/courseclass/teacherChoiceTree";
	}

	// 班级人员管理
	@RequestMapping(value = "classMemberManageList/{id}/{classId}", method = RequestMethod.GET)
	public String classMemberManageList(@PathVariable("id") String type, @PathVariable("classId") String classId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, ModelMap model,
			HttpServletRequest request) {
		// GjtUserAccount user = (GjtUserAccount)
		// request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		model.addAttribute("classId", classId);
		GjtClassInfo gjtClassInfo = gjtClassInfoService.queryById(classId);
		model.addAttribute("classInfo", gjtClassInfo);
		if ("1".equals(type)) {// 跳转到学员管理
			searchParams.put("EQ_classId", classId);
			// Page<GjtClassStudent> page =
			// gjtClassStudentService.queryAll(user.getGjtOrg().getId(),
			// searchParams, pageRequst);
			Page<Map<String, Object>> page = gjtClassStudentService.queryClassStudentInfo(searchParams, pageRequst);
			model.addAttribute("pageInfo", page);
			return "/edumanage/courseclass/classMemberManageList";
		} else if ("2".equals(type)) {// 跳转到辅导老师
			/*
			 * Page<GjtClassTeacher> page = gjtClassTeacherService.queryAll(2,
			 * classId, searchParams, pageRequst);
			 * model.addAttribute("pageInfo", page);
			 */
			model.addAttribute("teacher", gjtClassInfo.getGjtBzr());
			model.addAttribute("type", "2");
			return "/edumanage/courseclass/classTeacherList";
		} else {
			model.addAttribute("teacher", gjtClassInfo.getGjtDuDao());
			model.addAttribute("type", "4");// 跳转到督导老师
			model.addAttribute("termcourseId", gjtClassInfo.getTermcourseId());// 跳转到督导老师
			model.addAttribute("classId", gjtClassInfo.getClassId());// 跳转到督导老师

			/*
			 * Page<GjtClassTeacher> page = gjtClassTeacherService.queryAll(4,
			 * classId, searchParams, pageRequst);
			 * model.addAttribute("pageInfo", page);
			 */
			return "/edumanage/courseclass/classTeacherList";
		}

	}

	@RequestMapping(value = "termCourseStudentList", method = RequestMethod.GET)
	public String courseClassStudentList(@RequestParam String termCourseId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, ModelMap model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_termCourseId", termCourseId);
		searchParams.put("EQ_classTeacher", user.getId());
		Page<Map<String, Object>> page = gjtClassStudentService.queryTermCourseStudentList(searchParams, pageRequst);
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("pageInfo", page);
		model.addAttribute("gradeMap", gradeMap);
		return "/edumanage/courseclass/termCourseStudentList";
	}

	// 配置老师
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "createClassTeacher", method = RequestMethod.POST)
	@ResponseBody
	public Feedback createClassTeacher(HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			String classId = request.getParameter("classId");
			String zgh = "";
			String employeeType = request.getParameter("type");
			GjtClassInfo gjtClassInfo = gjtClassInfoService.queryById(classId);

			String[] employeeIdList = request.getParameter("employeeIdList").split(",");
			if (employeeIdList == null || employeeIdList.length == 0) {
				if ("2".equals(employeeType)) {
					gjtClassInfo.setGjtBzr(null);
				} else if ("4".equals(employeeType)) {
					gjtClassInfo.setGjtDuDao(null);
				}
			} else {
				GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryById(employeeIdList[0]);
				zgh = employeeInfo.getZgh();
				if ("2".equals(employeeType)) {
					gjtClassInfo.setGjtBzr(employeeInfo);
				} else if ("4".equals(employeeType)) {
					gjtClassInfo.setGjtDuDao(employeeInfo);
				}
			}

			gjtClassInfo.setUpdatedBy(user.getId());
			gjtClassInfo.setUpdatedDt(new Date());
			gjtClassInfoService.updateEntity(gjtClassInfo);
			
			//辅导老师同步到学习网
			if ("5".equals(user.getGjtOrg().getSchoolModel())){
				List classList = new ArrayList();
				Map cMap = new HashMap();
				cMap.put("CourseClassCode", ObjectUtils.toString(gjtClassInfo.getBh()));
            	cMap.put("UserName", ObjectUtils.toString(zgh));
            	cMap.put("CourseCode", ObjectUtils.toString(gjtClassInfo.getGjtCourse().getKch()));
            	cMap.put("OrgCode", ObjectUtils.toString(user.getGjtOrg().getVirtualXxzxCode()));
            	classList.add(cMap);
            	
            	Map dataMap = new HashMap();
        		dataMap.put("operatingUserName", ObjectUtils.toString(user.getLoginAccount()));
        		dataMap.put("functionType", "SynchCourseClassTeacher");
        		dataMap.put("synchDATA", classList);
        		Map msgMap =apiOucSyncService.addAPPDataSynch(dataMap);
        		String status = ObjectUtils.toString(msgMap.get("code"));
        		String message = ObjectUtils.toString(msgMap.get("message"));
        		// 同步失败
        		if ("0".equals(status)) {
	            	Map tempMap = new HashMap();
	            	tempMap.put("XXW_SYNC_STATUS", "2");
	            	tempMap.put("XXW_SYNC_MSG", message);
	            	tempMap.put("CLASS_ID", classId);
	            	gjtTeachCourseService.updClassXxwSyncStatus(tempMap);
        		} else {
        			List dataList = (List)JSON.parse(ObjectUtils.toString(msgMap.get("data")));
        			if (EmptyUtils.isNotEmpty(dataList) && dataList.size() > 0) {
    					for (int i=0;i<dataList.size(); i++) {
    						Map dataMaps = (Map)dataList.get(i);
    						String ErrorCode = ObjectUtils.toString(dataMaps.get("ErrorCode"));
    						Map tempMap = new HashMap();
    						tempMap.put("CLASS_ID", classId);
    						if ("0".equals(ErrorCode)) {
    							tempMap.put("XXW_SYNC_STATUS", "2");
    	    	            	tempMap.put("XXW_SYNC_MSG", ErrorCode);
    	    	            	gjtTeachCourseService.updClassXxwSyncStatus(tempMap);
    						} else {
    							tempMap.put("XXW_SYNC_STATUS", "1");
    	    	            	tempMap.put("XXW_SYNC_MSG", message);
    	    	            	gjtTeachCourseService.updClassXxwSyncStatus(tempMap);
    						}
    					}
    				}
        		}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "创建失败;" + e.getMessage());
		}
		return feedback;
	}

	@RequiresPermissions("/edumanage/courseclass/list$create")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map<String, String> termMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("termMap", termMap);

		Map<String, String> courseMap = commonMapService.getCourseMap(user.getGjtOrg().getId());
		model.addAttribute("courseMap", courseMap);

		return "edumanage/courseclass/addCourseClass";
	}

	@SuppressWarnings("unchecked")
	@RequiresPermissions("/edumanage/courseclass/list$create")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback create(GjtClassInfo item, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "创建成功");

		try {
			item.setClassId(UUIDUtils.random());
			item.setClassType("course");
			item.setCreatedBy(user.getId());
			item.setCreatedDt(new Date());
			item.setVersion(BigDecimal.valueOf(2.5));
			item.setIsDeleted("N");
			item.setIsEnabled("1");
			item.setSyncStatus("N");
			item.setOrgCode(user.getGjtOrg().getCode());
			item.setGjtOrg(user.getGjtOrg());

			if (user.getGjtOrg().getGjtSchoolInfo() != null) {
				item.setGjtSchoolInfo(user.getGjtOrg().getGjtSchoolInfo());
			} else {
				item.setGjtStudyCenter(user.getGjtOrg().getGjtStudyCenter());
				item.setGjtSchoolInfo(user.getGjtOrg().getParentGjtOrg().getGjtSchoolInfo());
			}

			gjtClassInfoService.saveEntity(item);
			
			//新建班级同步到学习网
			if ("5".equals(user.getGjtOrg().getSchoolModel())){
				List classList = new ArrayList();
				Map cMap = new HashMap();
				cMap.put("CourseClassCode", ObjectUtils.toString(item.getBh()));
            	cMap.put("CourseClassName", ObjectUtils.toString(item.getBjmc()));
            	cMap.put("CourseCode", item.getGjtCourse().getKch());
            	cMap.put("OrgCode", user.getGjtOrg().getVirtualXxzxCode());
            	classList.add(cMap);
            	
            	Map dataMap = new HashMap();
        		dataMap.put("operatingUserName", ObjectUtils.toString(user.getLoginAccount()));
        		dataMap.put("functionType", "SynchCourseClass");
        		dataMap.put("synchDATA", classList);
        		Map msgMap =apiOucSyncService.addAPPDataSynch(dataMap);
        		String status = ObjectUtils.toString(msgMap.get("code"));
        		String message = ObjectUtils.toString(msgMap.get("message"));
        		// 同步失败
        		if ("0".equals(status)) {
	            	Map tempMap = new HashMap();
	            	tempMap.put("XXW_SYNC_STATUS", "2");
	            	tempMap.put("XXW_SYNC_MSG", message);
	            	tempMap.put("CLASS_ID", item.getClassId());
	            	gjtTeachCourseService.updClassXxwSyncStatus(tempMap);
        		} else {
        			List dataList = (List)JSON.parse(ObjectUtils.toString(msgMap.get("data")));
        			if (EmptyUtils.isNotEmpty(dataList) && dataList.size() > 0) {
    					for (int i=0;i<dataList.size(); i++) {
    						Map dataMaps = (Map)dataList.get(i);
    						String ErrorCode = ObjectUtils.toString(dataMaps.get("ErrorCode"));
    						Map tempMap = new HashMap();
    						tempMap.put("CLASS_ID", ObjectUtils.toString(item.getClassId()));
    						if ("0".equals(ErrorCode)) {
    							tempMap.put("XXW_SYNC_STATUS", "2");
    	    	            	tempMap.put("XXW_SYNC_MSG", message);
    	    	            	gjtTeachCourseService.updClassXxwSyncStatus(tempMap);
    						} else {
    							tempMap.put("XXW_SYNC_STATUS", "1");
    	    	            	tempMap.put("XXW_SYNC_MSG", message);
    	    	            	gjtTeachCourseService.updClassXxwSyncStatus(tempMap);
    						}
    					}
    				}
        		}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "创建失败");
		}

		return feedback;
	}

	// 模拟登陆
	@RequestMapping(value = "/simulation", method = RequestMethod.GET)
	public String simulation(String id, String termcourseId, String classId) {
		String url = "";
		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryById(id);

		if ("2".equals(employeeInfo.getEmployeeType())) { // 辅导教师
			url = gjtUserAccountService.coachTeacherSimulation(employeeInfo.getEmployeeId());
		} else if ("4".equals(employeeInfo.getEmployeeType())) { // 督导教师
			if (EmptyUtils.isNotEmpty(termcourseId) && EmptyUtils.isNotEmpty(classId)) {
				url = gjtUserAccountService.supervisorTeacherSimulation(termcourseId, classId,
						employeeInfo.getEmployeeId());
			} else {
				List<Object[]> classInfoList = gjtClassInfoService
						.findClassIdANDTermcourseId(employeeInfo.getEmployeeId(), "course");
				if (classInfoList != null && classInfoList.size() > 0) {
					for (Object[] object : classInfoList) {
						termcourseId = ObjectUtils.toString(object[1]);
						classId = ObjectUtils.toString(object[0]);
						if (StringUtils.isNotEmpty(termcourseId)) {
							url = gjtUserAccountService.supervisorTeacherSimulation(termcourseId, classId,
									employeeInfo.getEmployeeId());
							break;
						}
					}
				}
			}

		}

		return "redirect:" + url; // 修改完重定向
	}

	/**
	 * 获取教学辅导机构的课程列表
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getXxzxClassList", method = RequestMethod.GET)
	public String getXxzxClassList(HttpServletRequest request, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map formMap = new HashMap();
		formMap.put("XX_ID", user.getGjtOrg().getId());

		List xxzxList = gjtClassInfoService.getXxzxClassList(formMap);
		model.addAttribute("info", xxzxList);
		model.addAttribute("XXZX_ID", ObjectUtils.toString(formMap.get("XXZX_ID")));
		return "/edumanage/courseclass/getXxzxClassList";
	}
	
	/**
	 * 跳转批量调班
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "changeClass", method = RequestMethod.GET)
	public String changeClass(@RequestParam Map<String,Object> param, HttpServletRequest request, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map resultMap = new HashMap();
		
		String class_id = (String) param.get("CLASS_ID");
		Map classMap = commonMapService.getBjmcDatesExceptBS(class_id,user.getGjtOrg().getId());
		
		List classInfo = gjtTeachCourseService.getClassInfo(param);
		if(EmptyUtils.isNotEmpty(classInfo) && classInfo.size() > 0){
			resultMap = (Map) classInfo.get(0);
		}
		model.addAttribute("resultMap", resultMap);
		model.addAttribute("classMap", classMap);
		return "/edumanage/courseclass/changeClass";
	}
	
	/**
	 * 批量调班
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "updClassStudent", method = RequestMethod.GET)
	public int updClassStudent(@RequestParam Map<String,Object> param, HttpServletRequest request, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		param.put("UPDATED_BY", user.getId());
		String OrgCode = user.getGjtOrg().getVirtualXxzxCode();
		String operatingUserName = user.getLoginAccount();
		
		int num = gjtTeachCourseService.updClassStudent(param);
		
		if(num > 0 && "5".equals(user.getGjtOrg().getSchoolModel())){
			List studentList = gjtTeachCourseService.getClassStudent(param);
			if(EmptyUtils.isNotEmpty(studentList) && studentList.size() > 0){
				int perCount=20,index=0;
		        int times= studentList.size()/perCount;
				do {
		            List<Map> listTemp=null;
		            // listTemp是分段处理逻辑的参数
		            if(studentList.size()>=perCount){
		                listTemp=studentList.subList(0, perCount);
		            } else {
		                listTemp=studentList.subList(0, studentList.size());
		            }
		            Map sMap = new HashMap();
		            List sList = new ArrayList();
		            for (int i= 0; i<listTemp.size(); i++) {
		            	Map tempMap = (Map)listTemp.get(i);
		            	sMap.put(ObjectUtils.toString(tempMap.get("BH"))+"_"+tempMap.get("XH"), ObjectUtils.toString(tempMap.get("CLASS_STUDENT_ID")));
		            	Map cMap = new HashMap();
		            	cMap.put("UserName", ObjectUtils.toString(tempMap.get("XH")));
		            	cMap.put("CourseClassCode", ObjectUtils.toString(tempMap.get("BH")));
		            	cMap.put("CourseCode", ObjectUtils.toString(tempMap.get("KCH")));
		            	cMap.put("OrgCode", ObjectUtils.toString(OrgCode));
		            	sList.add(cMap);
		            }
		            
		            if (EmptyUtils.isNotEmpty(sList)) {
		        		Map dataMap = new HashMap();
		        		dataMap.put("operatingUserName", operatingUserName);
		        		dataMap.put("functionType", "SynchCourseClassStudent");
		        		dataMap.put("synchDATA", sList);
		        		Map msgMap =apiOucSyncService.addAPPDataSynch(dataMap);
		        		String status = ObjectUtils.toString(msgMap.get("code"));
		        		String message = ObjectUtils.toString(msgMap.get("message"));
		        		// 同步失败
		        		if ("1".equals(status)) {
		        			for (int i= 0; i<listTemp.size(); i++) {
		    	            	Map tempMap = (Map)listTemp.get(i);
		    	            	tempMap.put("XXW_SYNC_STATUS", "1");
		    	            	tempMap.put("XXW_SYNC_MSG", message);
		    	            	String bh = ObjectUtils.toString(tempMap.get("CourseClassCode"));
		    	            	String xh = ObjectUtils.toString(tempMap.get("UserName"));
		    	            	tempMap.put("CLASS_STUDENT_ID", sMap.get(bh+"_"+xh));
		    	            	gjtTeachCourseService.updStudClassXxwSyncStatus(tempMap);
		    	            }
		        		} else {
		        			List dataList = (List)JSON.parse(ObjectUtils.toString(msgMap.get("data")));
		        			if (EmptyUtils.isNotEmpty(dataList) && dataList.size() > 0) {
		    					for (int i=0;i<dataList.size(); i++) {
		    						Map dataMaps = (Map)dataList.get(i);
		    						String bh = ObjectUtils.toString(dataMaps.get("CourseClassCode"));
		    						String xh = ObjectUtils.toString(dataMaps.get("UserName"));
		    						String ErrorCode = ObjectUtils.toString(dataMaps.get("ErrorCode"));
		    						String ErrorMessage = ObjectUtils.toString(dataMaps.get("ErrorMessage"));
		    						String class_student_id = ObjectUtils.toString(sMap.get(bh+"_"+xh));
		    						Map tempMap = new HashMap();
		    						tempMap.put("CLASS_STUDENT_ID", class_student_id);
		    						if ("0".equals(ErrorCode)) {
		    							tempMap.put("XXW_SYNC_STATUS", "2");
		    	    	            	tempMap.put("XXW_SYNC_MSG", message);
		    	    	            	gjtTeachCourseService.updStudClassXxwSyncStatus(tempMap);
		    						} else {
		    							tempMap.put("XXW_SYNC_STATUS", "1");
		    	    	            	tempMap.put("XXW_SYNC_MSG", message);
		    	    	            	gjtTeachCourseService.updStudClassXxwSyncStatus(tempMap);
		    						}
		    					}
		    				}
		        		}
		            }
		            studentList.removeAll(listTemp);  
		            index++;  
		        }  
		        while (index<=times);
			}
		}
		return num;
	}

	/**
	 * 获取教学辅导机构的课程列表
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "getXxzxCourseList", method = RequestMethod.GET)
	public String getXxzxCourseList(HttpServletRequest request, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map formMap = Servlets.getParametersStartingWith(request, "");
		formMap.put("XX_ID", user.getGjtOrg().getId());

		List xxzxList = gjtClassInfoService.getXxzxCourseList(formMap);
		model.addAttribute("info", xxzxList);
		model.addAttribute("XXZX_ID", ObjectUtils.toString(formMap.get("XXZX_ID")));
		return "/edumanage/courseclass/getXxzxCourseList";
	}

	/**
	 * 获取学习中心选择的期课程
	 * 
	 * @return
	 */
	@RequestMapping(value = "getXxzxChooseList")
	@ResponseBody
	public List getXxzxChooseList(HttpServletRequest request) {
		List xxzxList = new ArrayList();
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map formMap = Servlets.getParametersStartingWith(request, "");
			formMap.put("XX_ID", user.getGjtOrg().getId());
			xxzxList = gjtClassInfoService.getXxzxCourseList(formMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xxzxList;
	}

	/**
	 * 新增学习中心和期课程关系
	 * 
	 * @param formMap
	 * @return
	 */
	@RequestMapping(value = "addTermcourseOrg")
	@ResponseBody
	public Map addTermcourseOrg(HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map resultMap = new HashMap();
		Map formMap = Servlets.getParametersStartingWith(request, "");
		formMap.put("XX_ID", user.getGjtOrg().getId());
		int num = gjtClassInfoService.addTermcourseOrg(formMap);
		resultMap.put("num", num);
		return resultMap;
	}

	/**
	 * 删除学习中心和期课程关系
	 * 
	 * @param formMap
	 * @return
	 */
	@RequestMapping(value = "delTermcourseOrg")
	@ResponseBody
	public Map delTermcourseOrg(HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map resultMap = new HashMap();
		Map formMap = Servlets.getParametersStartingWith(request, "");
		formMap.put("XX_ID", user.getGjtOrg().getId());
		int num = gjtClassInfoService.delTermcourseOrg(formMap);
		resultMap.put("num", num);
		return resultMap;
	}
}
