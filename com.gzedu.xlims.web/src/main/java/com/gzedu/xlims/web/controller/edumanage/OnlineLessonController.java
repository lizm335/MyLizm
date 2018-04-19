/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.openClass.GjtOnlineLessonFile;
import com.gzedu.xlims.pojo.openClass.LcmsOnlineLesson;
import com.gzedu.xlims.pojo.openClass.LcmsOnlineObject;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.LcmsOnlineLessonService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;

import net.spy.memcached.MemcachedClient;

@Controller
@RequestMapping("/edumanage/onlinelesson")
public class OnlineLessonController {

	private static final Logger log = LoggerFactory.getLogger(OnlineLessonController.class);

	@Autowired
	private LcmsOnlineLessonService lcmsOnlineLessonService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private MemcachedClient memcachedClient;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	/**
	 * 查询活动直播
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月29日 上午10:25:33
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryOnlineActivityList")
	public String queryOnlineActivityList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("createdBy", user.getId());// user.getId()
		Page<LcmsOnlineLesson> page = (Page<LcmsOnlineLesson>) lcmsOnlineLessonService.findOnlineActivityList(searchParams, pageRequst);
		List<LcmsOnlineLesson> list = page.getContent();
		for (LcmsOnlineLesson lesson : list) {
			List<LcmsOnlineObject> objs = lcmsOnlineLessonService.findOnlineObjectByOnlinetutorId(lesson.getOnlinetutorId());
			lesson.setLcmsOnlineObjects(objs);
		}
		model.addAttribute("pageInfo", page);
		pageRequst = Servlets.buildPageRequest(1, 1);
		searchParams.remove("lessonType");
		long lessonNum = lcmsOnlineLessonService.findOnlineActivityList(searchParams, pageRequst).getTotalElements();
		searchParams.put("lessonType", "1");
		long lessoningNum = lcmsOnlineLessonService.findOnlineActivityList(searchParams, pageRequst).getTotalElements();
		searchParams.put("lessonType", "0");
		long needLessonNum = lcmsOnlineLessonService.findOnlineActivityList(searchParams, pageRequst).getTotalElements();
		searchParams.put("lessonType", "2");
		long lessonedNum = lcmsOnlineLessonService.findOnlineActivityList(searchParams, pageRequst).getTotalElements();
		model.addAttribute("lessonNum", lessonNum);
		model.addAttribute("lessoningNum", lessoningNum);
		model.addAttribute("needLessonNum", needLessonNum);
		model.addAttribute("lessonedNum", lessonedNum);
		model.addAttribute("userName", user.getRealName());
		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次
		model.addAttribute("gradeMap", commonMapService.getGradeMap(user.getGjtOrg().getId())); // 年级
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId()));
		model.addAttribute("courseMap", commonMapService.getCourseMap(user.getGjtOrg().getId()));

		return "edumanage/onlineLesson/online_activity_list";
	}

	@RequestMapping(value = "queryOnlineActivityDetail")
	public String queryOnlineActivityDetail(String id, @RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, ModelMap model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> params = Servlets.getParametersStartingWith(request, "search_");

		LcmsOnlineLesson lesson = lcmsOnlineLessonService.queryById(id);
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("createdBy", user.getId());
		temp.put("onlinetutorId", lesson.getOnlinetutorId());
		LcmsOnlineLesson onlineLesson = (LcmsOnlineLesson) lcmsOnlineLessonService.findOnlineActivityList(temp, Servlets.buildPageRequest(1, 1))
				.getContent().get(0);
		long endTime = onlineLesson.getOnlinetutorFinish().getTime();
		long diff = Math.abs(System.currentTimeMillis() - endTime);
		long day =diff/(24*60*60*1000);
		long hour = diff % (24 * 60 * 60 * 1000) / (1000 * 60 * 60);
		long min = diff % (24 * 60 * 60 * 1000) % (1000 * 60 * 60) / (1000 * 60);
		StringBuilder sb = new StringBuilder();
		if (day > 0) {
			sb.append(day).append("天 ");
		}
		if (hour > 0 || day > 0) {
			sb.append(hour).append("时 ");
		}
		sb.append(min).append("分");
		model.addAttribute("diff", sb);
		BeanUtils.copyProperties(lesson, onlineLesson);
		model.addAttribute("onlineLesson", onlineLesson);
		long allNum = lcmsOnlineLessonService.countJoinStudent(lesson.getOnlinetutorId(), null, params);
		long joinNum = lcmsOnlineLessonService.countJoinStudent(lesson.getOnlinetutorId(), "1", params);
		long viewNum = lcmsOnlineLessonService.countJoinStudent(lesson.getOnlinetutorId(), "2", params);
		model.addAttribute("allNum", allNum);
		model.addAttribute("unjoinNum", allNum - joinNum);
		model.addAttribute("joinNum", joinNum);
		model.addAttribute("viewNum", viewNum);
		params.put("onlinetutorId", lesson.getOnlinetutorId());
		model.addAttribute("pageInfo", lcmsOnlineLessonService.findActivityStudent(params, Servlets.buildPageRequest(pageNumber, pageSize)));
		model.put("userName", user.getRealName());
		model.addAttribute("specialtyBaseMap", commonMapService.getSpecialtyBaseMap(user.getGjtOrg().getId()));
		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次
		model.addAttribute("gradeMap", commonMapService.getGradeMap(user.getGjtOrg().getId())); // 年级
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId()));
		model.addAttribute("courseMap", commonMapService.getCourseMap(user.getGjtOrg().getId()));
		model.addAttribute("courseCategoryMap", commonMapService.getDatesBy("CourseCategoryInfo")); // 课程类型
		return "/edumanage/onlineLesson/online_activity_detail";
	}

	@RequestMapping(value = "toExportStudent", method = RequestMethod.GET)
	public String toExportStudent(Model model, HttpServletRequest request, String id) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String phone = ObjectUtils.toString(user.getSjh());
		if (EmptyUtils.isNotEmpty(phone)) {
			model.addAttribute("mobileNumber", phone.substring(phone.length() - 4, phone.length()));
		}
		Map<String, Object> params = Servlets.getParametersStartingWith(request, "search_");
		LcmsOnlineLesson lesson = lcmsOnlineLessonService.queryById(id);
		params.put("onlinetutorId", lesson.getOnlinetutorId());
		params.put("joinType", params.get("exportJoinType"));
		Page<Map<String, Object>> page;
		String isLesson = request.getParameter("isLesson");
		if (StringUtils.isNotBlank(isLesson)) {
			page = lcmsOnlineLessonService.findLessonStudent(params, Servlets.buildPageRequest(1, 1));
		} else {
			page = lcmsOnlineLessonService.findActivityStudent(params, Servlets.buildPageRequest(1, 1));
		}
		model.addAttribute("count", page.getTotalElements());
		return "/edumanage/onlineLesson/export_form";
	}

	@RequestMapping(value = "exportStudent")
	public void exportLessonStudent(String id, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = Servlets.getParametersStartingWith(request, "search_");
		LcmsOnlineLesson lesson = lcmsOnlineLessonService.queryById(id);
		params.put("onlinetutorId", lesson.getOnlinetutorId());
		params.put("joinType", params.get("exportJoinType"));
		String isLesson = request.getParameter("isLesson");
		Page<Map<String, Object>> page;
		if (StringUtils.isNotBlank(isLesson)) {
			page = lcmsOnlineLessonService.findLessonStudent(params, Servlets.buildPageRequest(1, Integer.MAX_VALUE));
		} else {
			page = lcmsOnlineLessonService.findActivityStudent(params, Servlets.buildPageRequest(1, Integer.MAX_VALUE));
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", page.getContent());
		map.put("lessonName", lesson.getOnlinetutorName());
		String path = getClass().getResource(WebConstants.EXCEL_MODEL_URL).getPath() + "参与直播学生记录导出模板.xls";
		com.gzedu.xlims.common.ExcelUtil.exportExcel(map, path, response, "参与直播学生记录.xls");
	}

	@ResponseBody
	@RequestMapping("queryVideoUrl")
	public Feedback queryVideoUrl(String id, HttpServletRequest request, HttpServletResponse response) {
		Feedback feedback = new Feedback(false, null);
		try {
			LcmsOnlineLesson lesson = lcmsOnlineLessonService.queryById(id);
			boolean rs = lcmsOnlineLessonService.saveVideoUrl(lesson);
			if (rs) {
				feedback.setSuccessful(rs);
				feedback.setObj(lesson);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return feedback;

	}

	@RequestMapping("toActivityForm")
	public String toActivityForm(String id, HttpServletRequest request, HttpServletResponse response, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		LcmsOnlineLesson lesson;
		int stunum = 0;
		if (StringUtils.isNotBlank(id)) {
			lesson = lcmsOnlineLessonService.queryById(id);
			// 直播对象
			List<String> objectIds = new ArrayList<String>();
			for (LcmsOnlineObject obj : lesson.getLcmsOnlineObjects()) {
				objectIds.add(obj.getObjectId());
				model.addAttribute("objectIds", objectIds);
			}
			if (objectIds.size() == 1 && lesson.getLcmsOnlineObjects().get(0).getObjectType().equals("0")) {
				model.addAttribute("chooseStu", true);
			}
			// 直播文件
			List<GjtOnlineLessonFile> files = lcmsOnlineLessonService.findLessonFileByOnlinetutorId(lesson.getOnlinetutorId());
			model.addAttribute("files", files);
			List<String> studentIds = lcmsOnlineLessonService.queryActivityStudentIds(lesson.getOnlinetutorId());
			String key = ACTIVITYSTUDENT_CODE + user.getLoginAccount();
			memcachedClient.delete(key);
			memcachedClient.add(key, 60 * 60, studentIds);
			stunum = studentIds.size();
		} else {
			lesson = new LcmsOnlineLesson();
			String key = ACTIVITYSTUDENT_CODE + user.getLoginAccount();
			memcachedClient.delete(key);
		}
		model.addAttribute("lesson", lesson);
		model.addAttribute("stunum", stunum);
		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次
		model.addAttribute("gradeMap", commonMapService.getGradeMap(user.getGjtOrg().getId())); // 年级
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId()));
		model.addAttribute("courseMap", commonMapService.getCourseMap(user.getGjtOrg().getId()));
		return "/edumanage/onlineLesson/online_activity_form";
	}

	private final static String ACTIVITYSTUDENT_CODE = "ACTIVITYSTUDENT_CODE";

	/**
	 * 根据各种条件范围筛选学生参与直播
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年12月2日 下午7:29:50
	 * @param pyccIds
	 * @param gradeIds
	 * @param specialtyIds
	 * @param courseIds
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("queryActivityStudent")
	public int queryActivityStudent(@RequestParam(value = "pyccIds[]", required = false) List<String> pyccIds,
			@RequestParam(value = "gradeIds[]", required = false) List<String> gradeIds,
			@RequestParam(value = "specialtyIds[]", required = false) List<String> specialtyIds,
			@RequestParam(value = "courseIds[]", required = false) List<String> courseIds, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<String> studentIds = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(gradeIds)) {
			studentIds = lcmsOnlineLessonService.queryActivityStudentIds(user.getGjtOrg().getId(), pyccIds, gradeIds, specialtyIds, courseIds);
		}
		String key = ACTIVITYSTUDENT_CODE + user.getLoginAccount();
		memcachedClient.delete(key);
		memcachedClient.add(key, 60 * 60, studentIds);
		return studentIds.size();
	}

	@ResponseBody
	@RequestMapping("clearActivityStudent")
	public void clearActivityStudent(HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String key = ACTIVITYSTUDENT_CODE + user.getLoginAccount();
		memcachedClient.delete(key);
	}

	@RequestMapping("queryOnlineActivityStudent")
	public String queryOnlineActivityStudent(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		String key = ACTIVITYSTUDENT_CODE + user.getLoginAccount();
		List<String> studentIds = (List<String>) memcachedClient.get(key);
		Page<Map<String, Object>> page = null;
		if (CollectionUtils.isNotEmpty(studentIds)) {
			searchParams.put("studentIds", studentIds);
			searchParams.put("orgId", user.getGjtOrg().getId());
			page = lcmsOnlineLessonService.queryActivityStudent(searchParams, pageRequst);
		} else {
			page = new PageImpl<Map<String, Object>>(new ArrayList());
		}
		model.addAttribute("pageInfo", page);
		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次
		model.addAttribute("gradeMap", commonMapService.getGradeMap(user.getGjtOrg().getId())); // 年级
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId()));
		model.addAttribute("studyCenterMap", commonMapService.getStudyCenterMap(user.getGjtOrg().getId()));
		model.addAttribute("signupStatusMap", commonMapService.getXjzt());// 学籍状态

		return "/edumanage/onlineLesson/online_activity_student";
	}

	@ResponseBody
	@RequestMapping("deleteOnlineActivityStudent")
	public Feedback deleteOnlineActivityStudent(@RequestParam(value = "studentIds[]") List<String> studentIds, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, null);
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String key = ACTIVITYSTUDENT_CODE + user.getLoginAccount();
			List<String> studentIdList = (List<String>) memcachedClient.get(key);
			studentIdList.removeAll(studentIds);
			memcachedClient.delete(key);
			memcachedClient.add(key, 60 * 60, studentIdList);
			feedback.setObj(studentIdList.size());
		} catch (Exception e) {
			// TODO: handle exception
			feedback.setMessage("系统异常");
			feedback.setSuccessful(true);
		}
		return feedback;
	}

	/**
	 * 删除活动直播
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年12月4日 下午4:52:06
	 * @param id
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("deleteOnlineActivity")
	public Feedback deleteOnlineActivity(@RequestParam String id, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, null);
		try {
			LcmsOnlineLesson lesson = lcmsOnlineLessonService.queryById(id);
			lesson.setIsDeleted(Constants.ISDELETED);
			lcmsOnlineLessonService.save(lesson);
		} catch (Exception e) {
			// TODO: handle exception
			feedback.setMessage("系统异常");
			feedback.setSuccessful(true);
		}
		return feedback;
	}

	@RequestMapping("onlineChooseStudent")
	public String toChooseStudent(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("orgId", user.getGjtOrg().getId());
		Page<Map<String, Object>> page = lcmsOnlineLessonService.queryActivityStudent(searchParams, pageRequst);
		model.addAttribute("pageInfo", page);
		model.addAttribute("pyccMap", commonMapService.getPyccMap()); // 课程层次
		model.addAttribute("gradeMap", commonMapService.getGradeMap(user.getGjtOrg().getId())); // 年级
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(user.getGjtOrg().getId()));
		model.addAttribute("studyCenterMap", commonMapService.getStudyCenterMap(user.getGjtOrg().getId()));
		model.addAttribute("signupStatusMap", commonMapService.getXjzt());// 学籍状态
		return "/edumanage/onlineLesson/online_choose_student";
	}

	@ResponseBody
	@RequestMapping("saveChooseStudent")
	public Feedback saveChooseStudent(@RequestParam(value = "studentIds[]", required = false) List<String> studentIds, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, null);
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String key = ACTIVITYSTUDENT_CODE + user.getLoginAccount();
			List<String> studentIdList = (List<String>) memcachedClient.get(key);
			if (studentIdList == null) {
				studentIdList = new ArrayList<String>();
			}
			if (CollectionUtils.isEmpty(studentIds)) {
				Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
				searchParams.put("orgId", user.getGjtOrg().getId());
				studentIds = lcmsOnlineLessonService.queryActivityStudent(searchParams);
			}
			studentIdList.addAll(studentIds);
			studentIdList = new ArrayList<String>(new HashSet<String>(studentIdList));
			memcachedClient.delete(key);
			memcachedClient.add(key, 60 * 60, studentIdList);
			feedback.setObj(studentIdList.size());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback.setMessage("系统异常");
			feedback.setSuccessful(false);
		}
		return feedback;
	}

	/**
	 * 导入直播学员
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年6月6日 下午3:55:05
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@SysLog("直播活动-导入直播学员")
	@RequestMapping(value = "importOnlineActivityStudent")
	@ResponseBody
	public ImportFeedback importOnlineActivityStudent(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "学号", "姓名", "导入结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			List<String> studentIds = new ArrayList<String>();
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, heads.length - 1);
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}
			if (dataList != null && dataList.size() > 0) {
				for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据

					if (datas.length < heads.length - 1) {
						result[heads.length - 1] = "数据不全";
						failedList.add(result);
						continue;
					}
					// 字段验证
					String studentCode = datas[0];
					if (StringUtils.isEmpty(studentCode)) {
						result[heads.length - 1] = "学号不能为空";
						failedList.add(result);
						continue;
					}

					String studentName = datas[1];
					if (StringUtils.isEmpty(studentName)) {
						result[heads.length - 1] = "姓名不能为空";
						failedList.add(result);
						continue;
					}

					GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(studentCode);
					if (studentInfo == null) {
						result[heads.length - 1] = "学号不存在";
						failedList.add(result);
						continue;
					}

					if (!studentInfo.getXm().equals(studentName.trim())) {
						result[heads.length - 1] = "学号和姓名不对应";
						failedList.add(result);
						continue;
					}

					result[heads.length - 1] = "新增成功";
					studentIds.add(studentInfo.getStudentId());
					successList.add(result);
				}
			}
			String key = ACTIVITYSTUDENT_CODE + user.getLoginAccount();
			List<String> studentIdList = (List<String>) memcachedClient.get(key);
			if (studentIdList == null) {
				studentIdList = studentIds;
			} else {
				studentIdList.addAll(studentIds);
			}
			studentIdList = new ArrayList<String>(new HashSet<String>(studentIdList));
			memcachedClient.delete(key);
			memcachedClient.add(key, 60 * 60, studentIdList);

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "onlineStudent_success_" + currentTimeMillis + ".xls";
			String failedFileName = "onlineStudent_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "直播学员导入成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "直播学员导入失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL + "secialtyPlan"
					+ File.separator;
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
			ImportFeedback feedback = new ImportFeedback(true, dataList.size(), successList.size(), failedList.size(), successFileName,
					failedFileName);
			feedback.setResult(studentIdList.size());
			return feedback;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ImportFeedback(false, "系统异常！");
		}
	}

	/**
	 * 新增、修改活动直播
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年12月4日 下午4:30:11
	 * @param request
	 * @param lesson
	 * @param gradeIds
	 * @param pyccIds
	 * @param specialtyIds
	 * @param courseIds
	 * @param files
	 * @param objType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("saveOnlineActivity")
	public Feedback saveOnlineActivity(HttpServletRequest request, LcmsOnlineLesson lesson,
			@RequestParam(value = "gradeIds[]", required = false) List<String> gradeIds,
			@RequestParam(value = "pyccIds[]", required = false) List<String> pyccIds,
			@RequestParam(value = "specialtyIds[]", required = false) List<String> specialtyIds,
			@RequestParam(value = "courseIds[]", required = false) List<String> courseIds,
			@RequestParam(value = "files[]", required = false) List<String> files, int objType) {
		Feedback feedback = new Feedback(true, null);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String key = ACTIVITYSTUDENT_CODE + user.getLoginAccount();
		try {
			List<String> studentIds = (List<String>) memcachedClient.get(key);
			List<LcmsOnlineObject> onlineObjects = new ArrayList<LcmsOnlineObject>();
			if (objType == 0) {
				onlineObjects.addAll(this.generateLcmsOnlineObject(gradeIds, "4", user.getId()));
				onlineObjects.addAll(this.generateLcmsOnlineObject(pyccIds, "8", user.getId()));
				onlineObjects.addAll(this.generateLcmsOnlineObject(specialtyIds, "5", user.getId()));
				onlineObjects.addAll(this.generateLcmsOnlineObject(courseIds, "9", user.getId()));
			} else if (objType == 1) {
				LcmsOnlineObject obj = new LcmsOnlineObject();
				obj.setObjectType("0");
				onlineObjects.add(obj);
			}
			if (StringUtils.isNotBlank(lesson.getId())) {// 更新
				lesson.setUpdatedBy(user.getId());
				lcmsOnlineLessonService.updateOnlineActivity(lesson, onlineObjects, studentIds, files);
			} else {// 新增
				lesson.setCreatedBy(user.getId());
				lcmsOnlineLessonService.saveOnlineActivity(lesson, onlineObjects, studentIds, files);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback.setMessage("系统异常");
			feedback.setSuccessful(false);
		}
		return feedback;
	}

	/**
	 * 初始化新学员直播
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月12日 下午5:45:52
	 * @param request
	 * @param studentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("initStudentOnlineLesson")
	public Feedback initStudentOnlineLesson(HttpServletRequest request, String studentId) {
		Feedback feedback = new Feedback(true, null);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		try {
			lcmsOnlineLessonService.initStudentOnlineLesson(studentId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback.setMessage("系统异常:" + e.getMessage());
			feedback.setSuccessful(false);
		}
		return feedback;
	}

	private List<LcmsOnlineObject> generateLcmsOnlineObject(List<String> ids, String type, String userId) {
		List<LcmsOnlineObject> onlineObjects = new ArrayList<LcmsOnlineObject>();
		if (ids == null) {
			return onlineObjects;
		}
		for (String id : ids) {
			LcmsOnlineObject obj = new LcmsOnlineObject();
			obj.setObjectId(id);
			obj.setObjectType(type);
			obj.setCreatedBy(userId);
			onlineObjects.add(obj);
		}
		return onlineObjects;
	}

	@InitBinder
	protected void InitBinder(WebDataBinder dataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
