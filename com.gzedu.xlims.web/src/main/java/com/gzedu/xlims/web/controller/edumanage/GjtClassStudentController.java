/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.FormSubmitUtil;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.constants.OrgUtil;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtClassStudent;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtRecResult;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

import net.sf.json.JSONObject;

/**
 * 
 * 功能说明：教学班级管理-学员管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月25日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Controller
@RequestMapping("/edumanage/classstudent")
public class GjtClassStudentController extends BaseController {

	public static final Logger log = LoggerFactory.getLogger(GjtClassStudentController.class);

	@Autowired
	GjtClassStudentService gjtClassStudentService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	GjtClassInfoService gjtClassInfoService;

	@Autowired
	GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	GjtRecResultService gjtRecResultService;
	
	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@Autowired
	GjtGradeService gjtGradeService;
	
	@Autowired
	CacheService cacheService;

	@Value("#{configProperties['courseSyncServer']}")
	private String courseSyncServer;

	@Value("#{configProperties['eeChatInterface']}")
	String eeServer;

	// 查询某个班级的学员
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, String classId) {
		String action = request.getParameter("action");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		// String classType = request.getParameter("classType");
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		if (StringUtils.isNotBlank(classId)) {
			searchParams.put("EQ_gjtClassInfo.classId", classId);
		}

		Page<GjtClassStudent> page = gjtClassStudentService.queryAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);

		// Map<String, String> gradeMap =
		// commonMapService.getGradeMap(user.getGjtOrg().getId());
		// Map<String, String> headTeacherMap =
		// commonMapService.getHeadTeacherMap();
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 培养层次
		Map<String, String> specialtyMap = null;
		if("3".equals(user.getGjtOrg().getSchoolModel())) {
			specialtyMap = commonMapService.getSchoolModelSpecialtyMap(user.getGjtOrg().getId());
		} else {
			specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		}
		// Map<String, String> orgMap =
		// commonMapService.getOrgMap(user.getId());
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());

		// model.addAttribute("gradeMap", gradeMap);
		// model.addAttribute("classType", classType);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("classId", classId);
		// model.addAttribute("orgMap", orgMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("pageInfo", page);
		model.addAttribute("action", action);

		return "edumanage/classstudent/list";
	}

	// 查询所有教学班级学员
	@RequestMapping(value = "listall", method = RequestMethod.GET)
	public String listAll(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		searchParams.put("EQ_gjtSchoolInfo.id", user.getGjtOrg().getId());
		searchParams.put("EQ_gjtClassInfo.classType", "teach");

		Page<GjtClassStudent> page = gjtClassStudentService.queryAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);

		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		Map<String, String> headTeacherMap = commonMapService.getEmployeeMap(user.getGjtOrg().getId(),
				EmployeeTypeEnum.班主任);
		Map<String, String> pyccMap = commonMapService.getPyccMap();// 培养层次
		Map<String, String> classInfoMap = commonMapService.getClassInfoMap(user.getGjtOrg().getId(), "teach");
		Map<String, String> orgMap = commonMapService.getOrgMap(user.getId());
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());

		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("headTeacherMap", headTeacherMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("classInfoMap", classInfoMap);
		model.addAttribute("orgMap", orgMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("pageInfo", page);

		return "edumanage/classstudent/listall";
	}

	// 查询没有分班的学员
	@RequestMapping(value = "queryNoBrvbar", method = RequestMethod.GET)
	public String queryNoBrvbar(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request, String classId, String className) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<GjtStudentInfo> page = gjtStudentInfoService.queryNoBrvbar(user.getGjtOrg().getId(), searchParams,
				pageRequst);

		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		Map<String, String> headTeacherMap = commonMapService.getEmployeeMap(user.getGjtOrg().getId(),
				EmployeeTypeEnum.班主任);
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
		Map<String, String> orgMap = commonMapService.getOrgMap(user.getId());

		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("headTeacherMap", headTeacherMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("orgMap", orgMap);
		model.addAttribute("studyCenterMap", orgMap);
		model.addAttribute("classId", classId);
		model.addAttribute("className", className);
		model.addAttribute("pageInfo", page);

		return "edumanage/classstudent/nobrvbar";
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
				return new Feedback(false, "班级添加学员表失败");
			}
		}
		return new Feedback(false, "班级添加学员表失败");
	}

	// 删除班级学员
	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				gjtClassStudentService.deleteById(selectedIds);
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

	// 批量调班
	@RequestMapping(value = "moveClass/{classId}", method = RequestMethod.GET)
	public String moveClass(@PathVariable("classId") String classId, @RequestParam("studentIds") String studentIds,
			Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		if (studentIds != null) {
			model.addAttribute("num", studentIds.split(",").length);
		}

		GjtClassInfo gjtClassInfo = gjtClassInfoService.queryById(classId);
		Map<String, String> gjtClassInfoMap = commonMapService.getBjmcDatesExceptBS(classId, user.getGjtOrg().getId());
		model.addAttribute("gjtClassInfo", gjtClassInfo);
		model.addAttribute("gjtClassInfoMap", gjtClassInfoMap);
		model.addAttribute("studentIds", studentIds);
		return "edumanage/courseclass/moveClass";
	}

	// 批量调班
	@RequestMapping(value = "moveTeachClass/{classId}", method = RequestMethod.GET)
	public String moveTeachClass(@PathVariable("classId") String classId, @RequestParam("studentIds") String studentIds,
			Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		if (studentIds != null) {
			model.addAttribute("num", studentIds.split(",").length);
		}
		GjtClassInfo gjtClassInfo = gjtClassInfoService.queryById(classId);
		Map<String, String> gjtClassInfoMap = commonMapService.getBjmcDatesExceptBS(gjtClassInfo,
				user.getGjtOrg().getId());
		model.addAttribute("gjtClassInfo", gjtClassInfo);
		model.addAttribute("gjtClassInfoMap", gjtClassInfoMap);
		model.addAttribute("studentIds", studentIds);
		return "edumanage/courseclass/moveTeachClass";
	}

	@RequestMapping(value = "moveClassByOne/{classId}/{studentId}", method = RequestMethod.GET)
	public String moveClassByOne(@PathVariable("classId") String classId, @PathVariable("studentId") String studentId,
			Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtClassInfo gjtClassInfo = gjtClassInfoService.queryById(classId);
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(studentId);
		Map<String, String> gjtClassInfoMap = commonMapService.getBjmcDatesExceptBS(gjtClassInfo,
				user.getGjtOrg().getId());
		model.addAttribute("gjtStudentInfo", gjtStudentInfo);
		model.addAttribute("gjtClassInfo", gjtClassInfo);
		model.addAttribute("gjtClassInfoMap", gjtClassInfoMap);
		return "edumanage/courseclass/moveClassByOne";
	}

	/**
	 * 教学班批量调班
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年6月19日 上午10:05:25
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "toMoveTeachClass", method = RequestMethod.POST)
	public Feedback toMoveTeachClass(HttpServletRequest request) {
		String sendClass = request.getParameter("sendClass");
		String receiveClass = request.getParameter("receiveClass");
		String[] studentIds = request.getParameterValues("studentIds[]");
		PageRequest pageRequst = new PageRequest(0, Integer.MAX_VALUE);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_classId", sendClass);
		Set<String> ids = new HashSet<String>();
		CollectionUtils.addAll(ids, studentIds);
		searchParams.put("IN_gjtStudentInfo.studentId", ids);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			List<GjtClassStudent> list = gjtClassStudentService
					.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst).getContent();
			if (list != null && list.size() > 0) {
				//根据当前登录用户查询所属院校信息
				GjtSchoolInfo gjtSchoolInfo=gjtSchoolInfoService.queryAppidByOrgId(user.getGjtOrg().getId());
//				String appId = user.getGjtOrg().getGjtSchoolInfo().getAppid();
				GjtClassInfo gjtClassInfo = gjtClassInfoService.queryById(receiveClass);
				if(gjtClassInfo.getGjtBzr()!=null){
					List<GjtClassStudent> result = deleteEEGroup(list, gjtSchoolInfo.getAppid());
					if (result.size() > 0) {
						for (GjtClassStudent s : result) {
							s.setUpdatedBy(user.getId());
						}
						try {
							gjtClassStudentService.updateClassStudent(result, gjtClassInfo, gjtSchoolInfo.getAppid());
						} catch (Exception e) {
							log.error(e.getMessage(), e);
							result.clear();
						}
					}
					if (result.size() == ids.size()) {
						return new Feedback(true, "调班成功");
					} else if (result.size() == 0) {
						return new Feedback(true, "调班失败");
					} else {
						List<String> failed = new ArrayList<String>();
						for (GjtClassStudent cs : list) {
							boolean flag = false;
							for (GjtClassStudent cs2 : result) {
								if (cs.getClassStudentId().equals(cs2.getClassStudentId())) {
									flag = true;
									break;
								}
							}
							if (!flag) {
								failed.add(cs.getGjtStudentInfo().getXm() + "(" + cs.getGjtStudentInfo().getXh() + ")");
							}
						}
						return new Feedback(true, "部分学生调班失败，失败学生列表如下：" + StringUtils.join(failed, ","));
					}
				}else{
					return new Feedback(true, "新班级暂未分配班主任，请先分配班主任后再调班！");
				}										
			} else {
				return new Feedback(false, "调班失败，找不到对应学生");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Feedback(false, "调班失败 ");
		}
	}

	@ResponseBody
	@RequestMapping(value = "toMoveClass", method = RequestMethod.POST)
	public Feedback toMoveClass(HttpServletRequest request) {
		String sendClass = request.getParameter("sendClass");
		String receiveClass = request.getParameter("receiveClass");
		String[] studentIds = request.getParameterValues("studentIds[]");
		PageRequest pageRequst = new PageRequest(0, Integer.MAX_VALUE);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_classId", sendClass);
		Set<String> ids = new HashSet<String>();
		CollectionUtils.addAll(ids, studentIds);
		searchParams.put("IN_gjtStudentInfo.studentId", ids);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			List<GjtClassStudent> list = gjtClassStudentService
					.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst).getContent();
			if (list != null && list.size() > 0) {
				//根据当前登录用户查询所属院校信息
				GjtSchoolInfo gjtSchoolInfo=gjtSchoolInfoService.queryAppidByOrgId(user.getGjtOrg().getId());
//				String appId = user.getGjtOrg().getGjtSchoolInfo().getAppid();
				GjtClassInfo gjtClassInfo = gjtClassInfoService.queryById(receiveClass);
				List<String> success = new ArrayList<String>();
				List<String> failed = new ArrayList<String>();
				for (GjtClassStudent gjtClassStudent : list) {
					gjtClassStudent.setClassId(receiveClass);
					gjtClassStudent.setUpdatedBy(user.getId());
					gjtClassStudent.setUpdatedDt(new Date());
					gjtClassStudentService.save(gjtClassStudent);

					GjtRecResult gjtRecResult = gjtRecResultService.findByStudentIdAndTeachPlanIdAndCourseId(
							gjtClassStudent.getStudentId(), gjtClassStudent.getGjtClassInfo().getTeachPlanId(),
							gjtClassStudent.getGjtClassInfo().getCourseId());
					gjtRecResult.setTeachPlanId(gjtClassInfo.getTeachPlanId());
					gjtRecResult.setUpdatedBy(user.getId());
					gjtRecResult.setUpdatedDt(new Date());
					gjtRecResultService.update(gjtRecResult);

					try {
						boolean b = this.synchroChangeClass(gjtSchoolInfo.getAppid(), gjtClassStudent.getStudentId(),
								gjtClassStudent.getGjtClassInfo().getTeachPlanId(), sendClass,
								gjtClassInfo.getTeachPlanId(), receiveClass);
						if (!b) {
							// 回滚
							gjtClassStudent.setClassId(sendClass);
							gjtClassStudentService.save(gjtClassStudent);

							gjtRecResult.setTeachPlanId(gjtClassStudent.getGjtClassInfo().getTeachPlanId());
							gjtRecResultService.update(gjtRecResult);

							failed.add(gjtClassStudent.getGjtStudentInfo().getXm() + "("
									+ gjtClassStudent.getGjtStudentInfo().getXh() + ")");
						} else {
							success.add(gjtClassStudent.getGjtStudentInfo().getXm() + "("
									+ gjtClassStudent.getGjtStudentInfo().getXh() + ")");
						}
					} catch (Exception e) {
						log.error(e.getMessage(), e);

						// 回滚
						gjtClassStudent.setClassId(sendClass);
						gjtClassStudentService.save(gjtClassStudent);

						gjtRecResult.setTeachPlanId(gjtClassStudent.getGjtClassInfo().getTeachPlanId());
						gjtRecResultService.update(gjtRecResult);

						failed.add(gjtClassStudent.getGjtStudentInfo().getXm() + "("
								+ gjtClassStudent.getGjtStudentInfo().getXh() + ")");
					}
				}

				if (failed.size() == 0) {
					return new Feedback(true, "调班成功");
				} else {
					if (success.size() == 0) {
						return new Feedback(true, "调班失败");
					} else {
						return new Feedback(true, "部分学生调班失败，失败学生列表如下：" + StringUtils.join(failed, ","));
					}
				}
			} else {
				return new Feedback(false, "调班失败，找不到对应学生");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Feedback(false, "调班失败 ");
		}
	}

	@ResponseBody
	@RequestMapping(value = "toMoveClassByOne", method = RequestMethod.POST)
	public Feedback toMoveClassByOne(String studentId, String sendClass, String receiveClass,
			HttpServletRequest request) {
		PageRequest pageRequst = new PageRequest(0, 1000);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_classId", sendClass);
		searchParams.put("EQ_gjtStudentInfo.studentId", studentId);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			List<GjtClassStudent> list = gjtClassStudentService
					.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst).getContent();
			for (GjtClassStudent gjtClassStudent : list) {
				gjtClassStudent.setClassId(receiveClass);
			}
			gjtClassStudentService.save(list);
			return new Feedback(true, "调班成功");
		} catch (Exception e) {
			return new Feedback(false, "调班失败 " + e.getMessage());
		}
	}

	/**
	 * 教务管理管理-班级人员管理-查看班主任信息
	 */

	@RequestMapping(value = "queryHeadTeacher", method = RequestMethod.GET)
	public String queryHeadTeacher(HttpServletRequest request, Model model, String action, String classId) {
		model.addAttribute("action", action);
		model.addAttribute("classId", classId);
		GjtClassInfo classInfo = gjtClassInfoService.queryById(classId);

		GjtEmployeeInfo gjtEmployeeInfo = null;

		GjtEmployeeInfo gjtBzr = classInfo.getGjtBzr();
		if (gjtBzr != null) {
			gjtEmployeeInfo = gjtEmployeeInfoService.queryById(gjtBzr.getEmployeeId());
		}
		model.addAttribute("classInfo", classInfo);
		model.addAttribute("gjtEmployeeInfo", gjtEmployeeInfo);
		return "edumanage/classstudent/headTearcher_info";
	}

	private boolean synchroChangeClass(String appId, String studentId, String sourceTeachPlanId, String sourceClassId,
			String targetTeachPlanId, String targetClassId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("formMap.APPID", appId);
		params.put("formMap.USER_ID", studentId);
		params.put("formMap.SOURCE_TERMCOURSE_ID", sourceTeachPlanId);
		params.put("formMap.SOURCE_CLASS_ID", sourceClassId);
		params.put("formMap.TARGET_TERMCOURSE_ID", targetTeachPlanId);
		params.put("formMap.TARGET_CLASS_ID", targetClassId);

		String s = HttpClientUtils.doHttpGet(courseSyncServer + "/repairdata/manager/userChangeClassData.do", params,
				3000, "utf-8");

		JsonParser parse = new JsonParser();
		JsonObject json = (JsonObject) parse.parse(s);
		String status = json.get("STATUS").getAsString();
		if ("1".equals(status)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 删除EE群关系， 返回删除成功的数据
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年6月16日 下午5:52:04
	 * @param list
	 * @param appId
	 * @return
	 */
	private List<GjtClassStudent> deleteEEGroup(List<GjtClassStudent> list, String appId) {
		List<GjtClassStudent> successList = new ArrayList<GjtClassStudent>();
		Map<String, Object> parmsMap = new HashMap<String, Object>();
		String url = eeServer + "/contactGroup/management/deleteGroupMember.do";
		try {
			for (GjtClassStudent s : list) {
				parmsMap.put("USERID", s.getGjtStudentInfo().getStudentId());
				parmsMap.put("EENO", s.getGjtStudentInfo().getEeno());
				parmsMap.put("GROUP_ID", s.getClassId());
				parmsMap.put("GROUP_EEIM_NO", s.getGjtClassInfo().getEegroup());
				parmsMap.put("APP_ID", appId);
				JSONObject jsonObject = JSONObject.fromObject(parmsMap);
				parmsMap.clear();
				parmsMap.put("data", jsonObject.toString());
				String result = HttpClientUtils.doHttpPost(url, parmsMap, 3000, "utf-8");
				if (StringUtils.isNoneEmpty(result)) {
					JSONObject jsonResult = JSONObject.fromObject(result);
					log.info("删除群关系返回结果：" + s.getGjtClassInfo().getEegroup());
					log.info("删除群关系返回结果：" + jsonResult);
					String status = jsonResult.getString("Status");
					if ("0".equals(status)) {
						successList.add(s);
						continue;
					}
				}

				log.error("删除群关系失败 studentId:" + s.getStudentId());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return successList;
	}

	@RequestMapping(value = "openEEchatToStudent", method = RequestMethod.GET)
	public String openEEchatToStudent(String studentId, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo student = gjtStudentInfoService.queryById(studentId);
		String deurl = "{\"IM_USERID\":\"" + user.getEeno() + "\",\"TO_IM_USERID\":\"" + student.getEeno() + "\"}";
		String eeurl = eeServer + "/openChatWithEENO.do?data=" + EncryptUtils.encrypt(deurl);
		return "redirect:" + eeurl;
	}


	@RequestMapping(value = "/analogLogin")
	public void analogLogin(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		String studentId = ObjectUtils.toString(searchParams.get("studentId"));
		try {
			GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);

			String synUrl = OrgUtil.getOucnetDomain(studentInfo.getGjtSchoolInfo().getGjtOrg().getCode());
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("id_card", ObjectUtils.toString(studentInfo.getSfzh(), ""));
			resultMap.put("phone", ObjectUtils.toString(studentInfo.getSjh(), ""));
			resultMap.put("account", ObjectUtils.toString(studentInfo.getXh(), ""));
			resultMap.put("organ", ObjectUtils.toString(studentInfo.getGjtSchoolInfo().getGjtOrg().getCode(), ""));

			String base64 = studentInfo.getSfzh() + "|" + (System.currentTimeMillis()) / 1000 + "|"
					+ Md5Util.encodeLower(studentInfo.getSfzh() + "oucnet", "UTF-8");

			String base64Code = Base64.encodeBase64String(base64.getBytes("UTF-8"));
			resultMap.put("code", base64Code);
			log.info("单点到应用平台参数:{}", resultMap);
			
			super.outputHtml(response, FormSubmitUtil.buildRequest(synUrl, resultMap, RequestMethod.POST.toString(), "提交"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@RequestMapping(value = "queryTeachClass/{gradeId}/{classId}",method = RequestMethod.GET)
    public String toQueryTeachClass(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,@PathVariable("classId") String classId,
			@PathVariable("gradeId") String gradeId,Model model, HttpServletRequest request){
    	GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
    	Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
    	PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
    	searchParams.put("NE_classId", classId);
    	searchParams.put("EQ_gjtGrade.gradeId", gradeId);
    	GjtGrade gjtGrade=gjtGradeService.queryById(gradeId);
    	GjtClassInfo gjtClassInfo=gjtClassInfoService.queryById(classId);
    	Page<GjtClassInfo> page = gjtClassInfoService.queryGjtClassInfo(user.getGjtOrg().getId(), searchParams, pageRequst);    	
    	Map<String, String> orgMap = commonMapService.getOrgTree(user.getGjtOrg().getId(), false);
    	model.addAttribute("orgMap", orgMap);
		model.addAttribute("gjtGrade", gjtGrade);
		model.addAttribute("classId", gjtClassInfo.getClassId());
		model.addAttribute("pageInfo", page);
    	return "edumanage/courseclass/queryTeachClass";
    }
}
