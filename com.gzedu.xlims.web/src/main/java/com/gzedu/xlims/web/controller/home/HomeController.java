/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.home;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import com.gzedu.xlims.common.MapKit;
import com.gzedu.xlims.common.PlatfromTypeEnum;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.menu.SystemName;
import com.gzedu.xlims.pojo.GjtSetOrgCopyright;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.PriModelInfo;
import com.gzedu.xlims.pojo.PriRoleInfo;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.exam.GjtExamBatchNewService;
import com.gzedu.xlims.service.exam.GjtExamRecordNewService;
import com.gzedu.xlims.service.home.HomeService;
import com.gzedu.xlims.service.model.ModelService;
import com.gzedu.xlims.service.organization.GjtSetOrgCopyrightService;
import com.gzedu.xlims.service.studymanage.StudyManageService;
import com.gzedu.xlims.service.systemManage.TblPriLoginLogService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.common.login.vo.LoginNamespaceCopyright;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月10日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/admin/home")
public class HomeController extends BaseController {

	private final static Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private TblPriLoginLogService tblPriLoginLogService;

	@Autowired
	private HomeService homeService;

	@Autowired
	private GjtSetOrgCopyrightService gjtSetOrgCopyrightService;

	@Autowired
	private StudyManageService studyManageService;

	@Autowired
	private GjtExamRecordNewService gjtExamRecordNewService;
	
	@Autowired
	private GjtExamBatchNewService gjtExamBatchNewService;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private CommonMapService commonMapService;

	@RequestMapping(value = "main", method = RequestMethod.GET)
	public String index(ModelMap model, HttpServletResponse response, HttpSession session, HttpServletRequest request) {
		String userId = (String) session.getAttribute("userId");
		GjtUserAccount user = gjtUserAccountService.findOne(userId);

		/*
		 * List<PriModelInfo> modelInfoList =
		 * modelService.queryMainModel(SystemName.办学组织管理平台.name());
		 * List<PriModelInfo> modelInfoList2 =
		 * modelService.queryMainModel("教辅组织管理平台（一级模式）"); if (modelInfoList2 !=
		 * null && modelInfoList2.size() > 0) {
		 * modelInfoList.addAll(modelInfoList2); }
		 */
		List<String> modelNames = new ArrayList<String>();
		List<PriModelInfo> topModels = modelService.queryTopModel();
		for (PriModelInfo topModel : topModels) {
			if (!SystemName.班主任平台.name().equals(topModel.getModelName())) {
				modelNames.add(topModel.getModelName());
			}
		}
		List<PriModelInfo> modelInfoList = modelService.queryMainModelIn(modelNames);

		// 是否是超级管理员 0
		if (user.getIsSuperMgr() != null && user.getIsSuperMgr()) {
			modelInfoList = modelService.queryMainModel(SystemName.办学组织管理平台.name());
			List<PriModelInfo> adminModelList = modelService.queryAll();
			for (PriModelInfo roleModel : adminModelList) {
				walk(modelInfoList, roleModel);
			}
		} else {
			PriRoleInfo roleInfo = user.getPriRoleInfo();
			if (roleInfo != null) {
				List<PriModelInfo> roleModelList = roleInfo.getPriModelInfos();
				for (PriModelInfo roleModel : roleModelList) {
					walk(modelInfoList, roleModel);
				}
			}
		}
		boolean bool = false;
		try {
			if (!session.getId().equals(user.getCurrentLoginIp()) && "Y".equals(user.getIsOnline())) {
				// 如果浏览器关闭，日志表里面旧的sessionId更改为新的
				tblPriLoginLogService.updateNewSessionByOldSession(user.getCurrentLoginIp(), session.getId());
				// 更改旧的sessionId为新的，要改user表
				bool = gjtUserAccountService.updateSessionId(user.getId(), user.getLoginCount(), session.getId());
			} else {
				if ("N".equals(user.getIsOnline()) || StringUtils.isBlank(user.getIsOnline())) {// Y是刷新,N是离线
					bool = gjtUserAccountService.updateLoginState(user.getId(), user.getLoginCount(), session.getId());
					// 插入日志表
					tblPriLoginLogService.save(user, request, session.getId());
				}
			}
			if (bool) {
				user = gjtUserAccountService.findOne(user.getId());
				session.setAttribute(WebConstants.CURRENT_USER, user);// 更新缓存
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		// 获取当前登录用户的角色是属于哪个平台
		LoginNamespaceCopyright copyright = new LoginNamespaceCopyright();
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolRealmName(request,
				PlatfromTypeEnum.MANAGEPLATFORM.getNum());
		if (item != null) {
			copyright = new LoginNamespaceCopyright(null, item.getPlatformName());
			copyright.setLoginHeadLogo(item.getLoginHeadLogo());
			copyright.setLoginFooterCopyright(item.getLoginFooterCopyright());
			copyright.setHomeFooterCopyright(item.getHomeFooterCopyright());
			copyright.setLoginBackground(item.getLoginBackground());
		}
		copyright.setLoginTitle(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.PLATFORM_TYPE,
				user.getPriRoleInfo().getPlatformType()));
		super.setLoginNamespaceCopyright(session, copyright);

		model.put("modelInfoList", modelInfoList);
		model.put("user", user);
		return "/main";
	}

	private void walk(List<PriModelInfo> treeModelList, PriModelInfo model) {
		for (PriModelInfo treeModel : treeModelList) {
			if (treeModel.getModelId().equals(model.getModelId())) {
				treeModel.setIsShow(true);
				break;
			}
			if (treeModel.getChildModelList() != null) {
				walk(treeModel.getChildModelList(), model);
			}
		}
	}

	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index2(ModelMap model, HttpServletResponse response, HttpSession session) {
		// 首页
		List<PriModelInfo> modelInfoList = modelService.queryMainModel(SystemName.办学组织管理平台.name());

		model.put("modelInfoList", modelInfoList);
		return "/main_index";
	}

	@RequestMapping(value = "header", method = RequestMethod.GET)
	public String header(ModelMap model, HttpServletResponse response, HttpSession session) {
		// 首页
		List<PriModelInfo> modelInfoList = modelService.queryMainModel(SystemName.办学组织管理平台.name());

		model.put("modelInfoList", modelInfoList);
		return "/layouts/header";
	}

	@RequestMapping(value = "myWorkbench")
	public String myWorkbench(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		model.addAttribute("todoList", homeService.getTodoList(user));
		model.addAttribute("messageList", homeService.getMessageList(user));
		model.addAttribute("infoTypeMap", commonMapService.getDates("InfoType"));
		model.addAttribute("workOrderList", homeService.getWorkOrderList(user));
		model.addAttribute("workTypeMap", commonMapService.getDates("WorkOrderType"));

		return "/home/myWorkbench";
	}

	@RequestMapping(value = "statistical")
	public String statistical(Model model, HttpServletRequest request) {
		return "/home/statistical";
	}

	/**
	 * 学生综合信息查询
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "studentSynthesizeStatistical")
	public String studentSynthesizeStatistical(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			  @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			  HttpServletRequest request) {
		return "/home/synthesize/studentSynthesizeStatistical";
	}

	/**
	 * 查看详情
	 * @param studentId
	 * @throws IOException
	 */
	@RequestMapping(value = "studentSynthesizeStatistical/detail/{studentId}")
	public String studentSynthesizeStatistical(@PathVariable String studentId, 
			HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(studentId);
		String singUrl = gjtUserAccountService.studentSimulationNew("APP001", studentInfo.getStudentId(),
				studentInfo.getXh(), "2");
		model.addAttribute("url", singUrl);
		return "/home/synthesize/studentSynthesizeStatisticalDetail";
	}

	/**
	 * 学生综合信息查询-缴费
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "studentSynthesizeStatisticalFee")
	public String studentSynthesizeStatisticalFee(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			  @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			  HttpServletRequest request) {
		return "/home/synthesize/studentSynthesizeStatisticalFee";
	}

	/**
	 * 学生综合信息查询-学习
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "studentSynthesizeStatisticalStudy")
	public String studentSynthesizeStatisticalStudy(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			  @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			  HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 获取学期列表
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 学习中心
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map pyccMap = commonMapService.getPyccMap();// 层次
		Map<String, String> classMap = commonMapService.getClassInfoMap(user.getGjtOrg().getId(), "teach");// 教务班级

		Map searchParams = Servlets.getParametersStartingWith(request, "");
		if("1".equals(user.getGjtOrg().getOrgType())) {
			searchParams.put("XX_ID", user.getGjtOrg().getId());
		} else {
			searchParams.put("EQ_studyId", user.getGjtOrg().getId());
		}
		Map currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(currentGradeMap)) {
			String currentGradeId = (String) currentGradeMap.keySet().iterator().next();
			model.addAttribute("currentGradeId", currentGradeId);
			if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {// 如果没有选择，则默认查当前学期
				searchParams.put("GRADE_ID", currentGradeId);
			} else if ("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
				searchParams.remove("GRADE_ID");
			}
		}
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = studyManageService.getStudentCourseList(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("classMap", classMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("totalNum", pageInfo.getTotalElements());
		StringBuffer prefixName = new StringBuffer();// 导出文件名字根据搜索条件命名前缀
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			prefixName.append(
					ObjectUtils.toString(studyCenterMap.get(ObjectUtils.toString(searchParams.get("XXZX_ID")))) + "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			prefixName.append(
					ObjectUtils.toString(gradeMap.get(ObjectUtils.toString(searchParams.get("GRADE_ID")))) + "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			prefixName.append(
					ObjectUtils.toString(specialtyMap.get(ObjectUtils.toString(searchParams.get("SPECIALTY_ID"))))
							+ "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))) {
			prefixName.append(
					ObjectUtils.toString(classMap.get(ObjectUtils.toString(searchParams.get("CLASS_ID")))) + "-");
		}
		searchParams.put("prefixName", ObjectUtils.toString(prefixName.toString(), ""));
		request.getSession().setAttribute("student_course_export", searchParams);// studentCourseListExport
		request.getSession().setAttribute("commonCourseConditionDetailExport", searchParams);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, ""));
		return "/home/synthesize/studentSynthesizeStatisticalStudy";
	}

	/**
	 * 学生综合信息查询-学习统计项
	 * @return
	 */
	@RequestMapping(value = "getStudentCourseCount")
	@ResponseBody
	public Map getStudentCourseCount(HttpServletRequest request) {
		Map resultMap = new HashMap();
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map searchParams = Servlets.getParametersStartingWith(request, "");
			if("1".equals(user.getGjtOrg().getOrgType())) {
				searchParams.put("XX_ID", user.getGjtOrg().getId());
			} else {
				searchParams.put("EQ_studyId", user.getGjtOrg().getId());
			}
			if ("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
				searchParams.remove("GRADE_ID");
			}
			searchParams.put("STUDY_STATUS", ObjectUtils.toString(searchParams.get("STUDY_STATUS_TEMP")));
			// 查询条件统计项
			long LOGIN_STATE_COUNT = studyManageService.getStudentCourseCount(searchParams);
			resultMap.put("STUDY_STATUS_COUNT", LOGIN_STATE_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 学生综合信息查询-学习列表下载
	 */
	@SysLog("学生综合信息查询-导出学员学习")
	@RequiresPermissions("/admin/home/myWorkbench$exportStudy")
	@RequestMapping(value = "downLoadStudentCourseListExportXls", method = RequestMethod.POST)
	public void downLoadStudentCourseListExportXls(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			try {
				Map searchParams = (Map) request.getSession().getAttribute("student_course_export");
				Workbook wb = studyManageService.downLoadStudentCourseListExportXls(searchParams);
				super.downloadExcelFile(request, response, wb, ObjectUtils.toString(searchParams.get("prefixName"), "") + "学员学情列表.xls");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("您没有权限");
		}
	}

	/**
	 * 学生综合信息查询-考勤
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "studentSynthesizeStatisticalClockingIn")
	public String studentSynthesizeStatisticalClockingIn(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			  @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			  HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 学习中心
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业名称
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		Map pyccMap = commonMapService.getPyccMap();// 层次
		
		Map<String, String> classMap = commonMapService.getClassInfoMap(user.getGjtOrg().getId(), "teach");
		
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		if("1".equals(user.getGjtOrg().getOrgType())) {
			searchParams.put("XX_ID", user.getGjtOrg().getId());
		} else {
			searchParams.put("EQ_studyId", user.getGjtOrg().getId());
		}
		Map currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(currentGradeMap)) {
			String currentGradeId = (String) currentGradeMap.keySet().iterator().next();
				model.addAttribute("currentGradeId", currentGradeId);
			if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {// 如果没有选择，则默认查当前学期
				searchParams.put("GRADE_ID", currentGradeId);
			} else if ("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
				searchParams.remove("GRADE_ID");
			}
		}
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = studyManageService.getStudentLoginList(searchParams, pageRequst);
		
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("classMap", classMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		StringBuffer prefixName = new StringBuffer();// 导出文件名字根据搜索条件命名前缀
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			prefixName.append(ObjectUtils.toString(studyCenterMap.get(ObjectUtils.toString(searchParams.get("XXZX_ID")))) + "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			prefixName.append(ObjectUtils.toString(gradeMap.get(ObjectUtils.toString(searchParams.get("GRADE_ID")))) + "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			prefixName.append(ObjectUtils.toString(specialtyMap.get(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) + "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))) {
			prefixName.append(ObjectUtils.toString(classMap.get(ObjectUtils.toString(searchParams.get("CLASS_ID")))) + "-");
		}
		searchParams.put("prefixName", ObjectUtils.toString(prefixName.toString(), ""));
		request.getSession().setAttribute("getStudentLoginList", searchParams);// 用于导出取此条件的参数
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, ""));
		return "/home/synthesize/studentSynthesizeStatisticalClockingIn";
	}

	/**
	 * 学生综合信息查询-考勤统计项
	 * @return
	 */
	@RequestMapping(value = "getStudentLoginCount")
	@ResponseBody
	public Map getStudentLoginCount(HttpServletRequest request) {
		Map resultMap = new HashMap();
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map searchParams = Servlets.getParametersStartingWith(request, "");
			if("1".equals(user.getGjtOrg().getOrgType())) {
				searchParams.put("XX_ID", user.getGjtOrg().getId());
			} else {
				searchParams.put("EQ_studyId", user.getGjtOrg().getId());
			}
			if ("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
				searchParams.remove("GRADE_ID");
			}
			searchParams.put("STUDY_STATUS", ObjectUtils.toString(searchParams.get("STUDY_STATUS_TEMP")));
			// 查询条件统计项
			int LOGIN_STATE_COUNT = studyManageService.getStudentLoginCount(searchParams);
			resultMap.put("STUDY_STATUS_COUNT", LOGIN_STATE_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 学生综合信息查询-考勤列表下载
	 */
	@SysLog("学生综合信息查询-导出学员考勤")
	@RequiresPermissions("/admin/home/myWorkbench$exportClockingIn")
	@RequestMapping(value = "downLoadStudentLoginListExportXls", method = RequestMethod.POST)
	public void downLoadStudentLoginListExportXls(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			try {
				Map searchParams = (Map) request.getSession().getAttribute("getStudentLoginList");
				Workbook wb = studyManageService.downLoadStudentLoginListExportXls(searchParams);
				super.downloadExcelFile(request, response, wb, ObjectUtils.toString(searchParams.get("prefixName"), "") + "学员课程考勤列表.xls");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("您没有权限");
		}
	}

	/**
	 * 学生综合信息查询-考试
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "studentSynthesizeStatisticalExam")
	public String studentSynthesizeStatisticalExam(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			  @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			  HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String schoolId= user.getGjtOrg().getId();
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		
		if("1".equals(user.getGjtOrg().getOrgType())) {
			searchParams.put("XX_ID", user.getGjtOrg().getId());
		} else {
			searchParams.put("EQ_studyId", user.getGjtOrg().getId());
		}
		if ("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			searchParams.remove("GRADE_ID");
		}
        // 默认选择当前期(批次)
        if(EmptyUtils.isEmpty(searchParams)|| StringUtils.isBlank((String) searchParams.get("examBatchCode"))) {
            String code = commonMapService.getCurrentGjtExamBatchNew(schoolId);
            searchParams.put("examBatchCode", code);
            model.addAttribute("examBatchCode",code);
        }
        GjtExamBatchNew examBatchNew = gjtExamBatchNewService.queryByExamBatchCode((String) searchParams.get("examBatchCode"));
        if (examBatchNew.getRecordEnd() != null && examBatchNew.getRecordEnd().compareTo(new Date()) < 0) {
        	examBatchNew.setPlanStatus("4");
		}

        Page<Map<String, Object>> pageInfo = gjtExamRecordNewService.queryStudentExamListByPage(searchParams, pageRequst);
        for (Iterator<Map<String, Object>> iter = pageInfo.iterator(); iter.hasNext();) {
			Map<String, Object> e = (Map<String, Object>) iter.next();
			int status;
			// 获取预约时间
			Date now = new Date();
			Date bookSt = (Date) e.get("BOOK_ST");
			Date bookEnd = (Date) e.get("BOOK_END");
			Date booksSt = (Date) e.get("BOOKS_ST");
			Date booksEnd = (Date) e.get("BOOKS_END");
			BigDecimal shouldExamCount = (BigDecimal) e.get("SHOULD_EXAM_COUNT");
			BigDecimal makeCount = (BigDecimal) e.get("MAKE_COUNT");
			/**
			 * 这里的逻辑是:
			 * ├─1.第一种情况：应考科目 = 已约科目					===→ 考试正常
			 * ├─2.第二种情况：应考科目 > 已约科目 (再次细分)
			 * │  ├─2.1.当前时间未到预约时间						===→ 考试正常
			 * │  ├─2.2.当前时间在预约范围之内(再次细分)
			 * │  │  ├─2.2.1.已约满8科							===→ 异常，已约满，需下次再约
			 * │  │  └─2.2.2.未约满8科							===→ 异常，预约范围内，需督促
			 * │  │  
			 * │  └─2.3.当前时间在预约结束之后（如果有第二次预约时间，那么有两个区间，第一次预约结束时间至第二次预约开始时间/第二次预约结束时间之后）(再次细分)
			 * │     ├─2.3.1.已约满8科							===→ 异常，已约满，需下次再约
			 * │     └─2.3.2.未约满8科							===→ 异常，预约已过期，漏报考
			 * │     
			 */
			if(shouldExamCount.intValue() == makeCount.intValue()) {
				status = 1;
			} else {
				if(now.getTime() < bookSt.getTime()) {
					status = 1;
				} else if(makeCount.intValue() >= 8) { // 合并2.2和2.3的已约满8科 ===→ 异常，已约满，需下次再约
					status = 4;
				} else {
					if(now.getTime() >= bookSt.getTime() && now.getTime() <= bookEnd.getTime() || (booksEnd != null && now.getTime() >= booksSt.getTime() && now.getTime() <= booksEnd.getTime())) {
						status = 2;
					} else {
						status = 3;
					}
				}
			}
			e.put("status", status);
		}

        Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId()); // 学习中心
        Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId()); // 学期
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业名称
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("batchMap", commonMapService.getGjtExamBatchNewIdNameMap(schoolId));
		model.addAttribute("examTypeMap", MapKit.toIntAscMap(commonMapService.getExamTypeIntMap()));
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("examBatchNew", examBatchNew);
		request.getSession().setAttribute("downLoadExcelExportByExamAppointment", searchParams);// 导出数据的查询条件
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, ""));
		StringBuffer prefixName = new StringBuffer();// 导出文件名字根据搜索条件命名前缀
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			prefixName.append(ObjectUtils.toString(studyCenterMap.get(ObjectUtils.toString(searchParams.get("XXZX_ID")))) + "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			prefixName.append(ObjectUtils.toString(gradeMap.get(ObjectUtils.toString(searchParams.get("GRADE_ID")))) + "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			prefixName.append(ObjectUtils.toString(specialtyMap.get(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) + "-");
		}
		searchParams.put("prefixName", ObjectUtils.toString(prefixName.toString(), ""));
		return "/home/synthesize/studentSynthesizeStatisticalExam";
	}

	/**
	 * 学生综合信息查询-考试统计项
	 * @return
	 */
	@RequestMapping(value = "getStudentExamCount")
	@ResponseBody
	public Map getStudentExamCount(HttpServletRequest request) {
		Map resultMap = new HashMap();
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map searchParams = Servlets.getParametersStartingWith(request, "");
			if("1".equals(user.getGjtOrg().getOrgType())) {
				searchParams.put("XX_ID", user.getGjtOrg().getId());
			} else {
				searchParams.put("EQ_studyId", user.getGjtOrg().getId());
			}
			if ("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
				searchParams.remove("GRADE_ID");
			}
			searchParams.put("STATUS", ObjectUtils.toString(searchParams.get("STATUS_TEMP")));
			// 查询条件统计项
			long LOGIN_STATE_COUNT = gjtExamRecordNewService.getStudentExamCount(searchParams);
			resultMap.put("STATUS_COUNT", LOGIN_STATE_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 学生综合信息查询-学员考情列表下载
	 */
	@SysLog("学生综合信息查询-导出学员考情")
	@RequiresPermissions("/admin/home/myWorkbench$exportExam")
	@RequestMapping(value = "downLoadExcelExportByExamAppointment", method = RequestMethod.POST)
	public void downLoadExcelExportByExamAppointment(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			try {
				Map searchParams = (Map) request.getSession().getAttribute("downLoadExcelExportByExamAppointment");
				Workbook wb = gjtExamRecordNewService.downLoadExcelExportByExamAppointment(searchParams);
				super.downloadExcelFile(request, response, wb, ObjectUtils.toString(searchParams.get("prefixName"), "") + "学员考情列表.xls");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("您没有权限");
		}
	}

	/**
	 * 学生综合信息查询-论文
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "studentSynthesizeStatisticalThesis")
	public String studentSynthesizeStatisticalThesis(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			  @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			  HttpServletRequest request) {
		return "/home/synthesize/studentSynthesizeStatisticalThesis";
	}

	/**
	 * 学生综合信息查询-实践
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "studentSynthesizeStatisticalPractice")
	public String studentSynthesizeStatisticalPractice(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			  @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			  HttpServletRequest request) {
		return "/home/synthesize/studentSynthesizeStatisticalPractice";
	}

	/**
	 * 学生综合信息查询-毕业
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "studentSynthesizeStatisticalGraduation")
	public String studentSynthesizeStatisticalGraduation(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			  @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			  HttpServletRequest request) {
		return "/home/synthesize/studentSynthesizeStatisticalGraduation";
	}

	/**
	 * 学生综合信息查询-链接
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "studentSynthesizeStatisticalLink")
	public String studentSynthesizeStatisticalLink(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			  @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			  HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 学习中心
		Map specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业名称
		Map gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		Map pyccMap = commonMapService.getPyccMap();// 层次
		
		Map<String, String> classMap = commonMapService.getClassInfoMap(user.getGjtOrg().getId(), "teach");
		
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		if("1".equals(user.getGjtOrg().getOrgType())) {
			searchParams.put("XX_ID", user.getGjtOrg().getId());
		} else {
			searchParams.put("EQ_studyId", user.getGjtOrg().getId());
		}
		Map currentGradeMap = commonMapService.getCurrentGradeMap(user.getGjtOrg().getId());
		if (EmptyUtils.isNotEmpty(currentGradeMap)) {
			String currentGradeId = (String) currentGradeMap.keySet().iterator().next();
				model.addAttribute("currentGradeId", currentGradeId);
			if (EmptyUtils.isEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {// 如果没有选择，则默认查当前学期
				searchParams.put("GRADE_ID", currentGradeId);
			} else if ("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
				searchParams.remove("GRADE_ID");
			}
		}
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = studyManageService.getStudentLinkList(searchParams, pageRequst);
		
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("classMap", classMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		StringBuffer prefixName = new StringBuffer();// 导出文件名字根据搜索条件命名前缀
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			prefixName.append(ObjectUtils.toString(studyCenterMap.get(ObjectUtils.toString(searchParams.get("XXZX_ID")))) + "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			prefixName.append(ObjectUtils.toString(gradeMap.get(ObjectUtils.toString(searchParams.get("GRADE_ID")))) + "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			prefixName.append(ObjectUtils.toString(specialtyMap.get(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) + "-");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("CLASS_ID")))) {
			prefixName.append(ObjectUtils.toString(classMap.get(ObjectUtils.toString(searchParams.get("CLASS_ID")))) + "-");
		}
		searchParams.put("prefixName", ObjectUtils.toString(prefixName.toString(), ""));
		request.getSession().setAttribute("getStudentLinkList", searchParams);// 用于导出取此条件的参数
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, ""));
		return "/home/synthesize/studentSynthesizeStatisticalLink";
	}

	/**
	 * 学生综合信息查询-链接统计项
	 * @return
	 */
	@RequestMapping(value = "getStudentLinkCount")
	@ResponseBody
	public Map getStudentLinkCount(HttpServletRequest request) {
		Map resultMap = new HashMap();
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map searchParams = Servlets.getParametersStartingWith(request, "");
			if("1".equals(user.getGjtOrg().getOrgType())) {
				searchParams.put("XX_ID", user.getGjtOrg().getId());
			} else {
				searchParams.put("EQ_studyId", user.getGjtOrg().getId());
			}
			if ("all".equals(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
				searchParams.remove("GRADE_ID");
			}
			searchParams.put("STUDY_STATUS", ObjectUtils.toString(searchParams.get("STUDY_STATUS_TEMP")));
			// 查询条件统计项
			long LOGIN_STATE_COUNT = studyManageService.getStudentLinkCount(searchParams);
			resultMap.put("STUDY_STATUS_COUNT", LOGIN_STATE_COUNT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 学生综合信息查询-链接列表下载
	 */
	@SysLog("学生综合信息查询-导出学员链接")
	@RequiresPermissions("/admin/home/myWorkbench$exportLink")
	@RequestMapping(value = "downLoadStudentLinkListExportXls", method = RequestMethod.POST)
	public void downLoadStudentLinkListExportXls(HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean flag = (Boolean) request.getSession().getAttribute("hasPermission");
		if (flag != null && flag == true) {
			try {
				Map searchParams = (Map) request.getSession().getAttribute("getStudentLinkList");
				Workbook wb = studyManageService.downLoadStudentLinkListExportXls(searchParams);
				super.downloadExcelFile(request, response, wb, ObjectUtils.toString(searchParams.get("prefixName"), "") + "学员链接列表.xls");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("您没有权限");
		}
	}

	/**
	 * 业务综合信息查询
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "businessSynthesizeStatistical")
	public String businessSynthesizeStatistical(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			  @RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			  HttpServletRequest request) {
		return "/home/synthesize/businessSynthesizeStatistical";
	}

}
