/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.signup;

import com.gzedu.xlims.common.*;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.constants.OrgUtil;
import com.gzedu.xlims.pojo.*;
import com.gzedu.xlims.pojo.dto.StudentSignupInfoDto;
import com.gzedu.xlims.pojo.flow.GjtFlowRecord;
import com.gzedu.xlims.pojo.status.SignupAuditStateEnum;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.api.ApiOpenClassService;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.flow.GjtFlowService;
import com.gzedu.xlims.service.organization.*;
import com.gzedu.xlims.service.signup.GjtSignupService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * 学籍信息控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年09月21日
 * @version 2.5
 */
@Controller
@RequestMapping("/edumanage/roll")
public class RollController extends BaseController {

	public static final Logger log = LoggerFactory.getLogger(RollController.class);

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtClassStudentService gjtClassStudentService;

	@Autowired
	GjtSpecialtyService gjtSpecialtyService;

	@Autowired
	GjtGradeSpecialtyService gjtGradeSpecialtyService;

	@Autowired
	private ApiOpenClassService apiOpenClassService;

	@Autowired
	GjtClassInfoService gjtClassInfoService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtRecResultService gjtRecResultService;

	@Autowired
	private GjtSignupService gjtSignupService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtFlowService gjtFlowService;

	@Autowired
	private CacheService cacheService;
	

	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@RequestMapping(value = "/list")
	public String querySource(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, ModelMap model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		String xxId = searchParams.get("EQ_gjtSchoolInfo.id") != null
				? searchParams.get("EQ_gjtSchoolInfo.id").toString() : null;
		if (StringUtils.isBlank(xxId)) {
			xxId = user.getGjtOrg().getId();
			searchParams.put("EQ_gjtSchoolInfo.id", xxId);
			model.addAttribute("defaultXxId", xxId);
		}
		/*
		 * Object gradeId = searchParams.get("EQ_viewStudentInfo.gradeId"); if
		 * (gradeId == null) { gradeId =
		 * gjtGradeService.getCurrentGradeId(xxId);
		 * searchParams.put("EQ_viewStudentInfo.gradeId", gradeId);
		 * model.addAttribute("defaultGradeId", gradeId); }
		 */
		searchParams.put("EQ_signupAuditState", SignupAuditStateEnum.通过.getValue() + "");
		// Page<GjtStudentInfo> page =
		// gjtStudentInfoService.querySource(searchParams, pageRequst);
		Page<StudentSignupInfoDto> page = gjtStudentInfoService.queryStudentSignupInfoByPage(searchParams, pageRequst);
		Object xjzt = searchParams.remove("EQ_xjzt");
		Map<String, BigDecimal> countXjztMap = gjtStudentInfoService.countGroupbyXjzt(searchParams);
		searchParams.put("EQ_xjzt", xjzt);

		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId()); // 学校
		// Map<String, String> orgMap =
		// commonMapService.getOrgMap(user.getId()); // 机构
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		Map<String, String> pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());
		Map<String, String> rollTypeMap = commonMapService.getRollTypeMap();// 学籍状态

		model.addAttribute("pageInfo", page);
		model.addAttribute("countXjztMap", countXjztMap);
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		// model.addAttribute("orgMap", orgMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("rollTypeMap", rollTypeMap);
		model.addAttribute("specialtyMap", specialtyMap);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "edumanage/roll/edumanage_roll_list";
	}

	@RequiresPermissions("/edumanage/roll/list$view")
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String queryById(@PathVariable("id") String id, ModelMap model, HttpServletRequest request) {
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(id);

		// 证件资料
		Map<String, String> signupCopyData = gjtSignupService.getSignupCopyData(id);
		model.addAttribute("signupCopyData", signupCopyData);

		List<GjtFlowRecord> flowRecordList = gjtFlowService.queryFlowRecordByStudentId(id);

		// isUndergraduateCourse 培养层次是否为本科
		boolean isUndergraduateCourse = isUndergraduateCourse(studentInfo.getPycc());

		model.addAttribute("item", studentInfo);
		model.addAttribute("flowRecordList", flowRecordList);
		model.addAttribute("isUndergraduateCourse", isUndergraduateCourse);
		boolean isOffsite = OrgUtil.isOffsite(studentInfo.getGjtSchoolInfo().getGjtOrg().getCode(),
				studentInfo.getSfzh());
		model.addAttribute("isOffsite", isOffsite ? 1 : 0); // 是否是异地学员(非广东) 1-是
															// 0-否
		model.addAttribute("action", "view");
		return "edumanage/roll/edumanage_roll_form";
	}

	// 修改对象
	@RequiresPermissions("/edumanage/roll/list$update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(RedirectAttributes redirectAttributes, GjtStudentInfo item) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtStudentInfo info = gjtStudentInfoService.queryById(item.getStudentId());
		if (StringUtils.isNotBlank(item.getAvatar())) {
			info.setAvatar(item.getAvatar());
		}
		info.setXm(item.getXm());
		info.setXbm(item.getXbm());
		info.setNation(item.getNation());
		info.setPoliticsstatus(item.getPoliticsstatus());
		info.setUserType(item.getUserType());
		info.setSfzh(item.getSfzh());
		info.setXjzt(item.getXjzt());
		info.setRxny(item.getRxny());
		info.setExedulevel(item.getExedulevel());
		info.setTxdz(item.getTxdz());
		info.setYzbm(item.getYzbm());
		info.setDzxx(item.getDzxx());
		info.setSjh(item.getSjh());
		info.setScCo(item.getScCo());
		info.setScCoAddr(item.getScCoAddr());
		info.setScName(item.getScName());
		info.setScPhone(item.getScPhone());

		Boolean updateEntity = gjtStudentInfoService.updateEntityAndFlushCache(info);
		if (!updateEntity) {
			feedback = new Feedback(true, "网络异常");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/roll/list";
	}

	// 跳转到修改页面
	@RequiresPermissions("/edumanage/roll/list$update")
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(String id, ModelMap model) {
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(id);
		model.addAttribute("gjtStudentInfo", gjtStudentInfo);
		return "";
	}

	// 模拟登录
	@RequestMapping(value = "/simulation", method = RequestMethod.GET)
	public String simulation(String id, HttpServletRequest request) {
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(id);
		String url = "";
		if (studentInfo != null) {
			url = gjtUserAccountService.studentSimulation(studentInfo.getStudentId(), studentInfo.getXh());
		}
		return "redirect:" + url; // 修改完重定向
	}

	// 重置密码
	@SysLog("学籍信息-重置学员密码")
	@RequiresPermissions("/edumanage/roll/list$reset")
	@RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updatePwd(String id) throws Exception {
		Feedback feedback = new Feedback(false, "网络异常");
		int i = gjtUserAccountService.updatePwd(id, Md5Util.encode(Constants.STUDENT_ACCOUNT_PWD_DEFAULT),
				Constants.STUDENT_ACCOUNT_PWD_DEFAULT);
		if (i > 0) {
			feedback = new Feedback(true, "密码重置成功");
		}
		return feedback;
	}

	// 录入学籍
	@RequestMapping(value = "/enteringSignup", method = RequestMethod.POST)
	@ResponseBody
	public Feedback enteringSignup(String studentId, HttpServletRequest request) throws Exception {
		Feedback feedback = new Feedback(false, "网络异常");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		boolean flag = gjtStudentInfoService.enteringSignup(studentId, user.getId());
		if (flag) {
			feedback = new Feedback(true, "录入成功");
		}
		return feedback;
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());
		model.addAttribute("gradeMap", gradeMap);
		return "edumanage/roll/simple_form";
	}

	// @RequestMapping(value = "create", method = RequestMethod.POST)
	// public String create(Signupdata data, RedirectAttributes
	// redirectAttributes) {
	// Feedback feedback = new Feedback(true, "创建成功");
	// try {
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("studentId", UUIDUtils.random().toString());
	// map.put("name", data.getName());
	// map.put("studentNo", data.getStudentNo());
	// map.put("gradeId", data.getGradeId());
	// map.put("majorId", data.getMajorId());
	//
	// String url = "http://localhost:8088/api/signupdata/add.do";
	// String result = HttpClientUtils.doHttpPost(url, map, 3000, "UTF-8");
	// System.out.println(result);
	// } catch (Exception e) {
	// feedback = new Feedback(false, "创建失败;" + e.getMessage());
	// }
	// redirectAttributes.addFlashAttribute("feedback", feedback);
	// return "redirect:/api/signupdata/add.do";
	// }

	/**
	 * 导出未注册学员学籍资料功能
	 */
	@SysLog("学籍信息-导出未注册学员学籍资料")
	@RequiresPermissions("/edumanage/roll/list$exportNotRegInfo")
	@RequestMapping(value = "exportNotRegInfo")
	public void exportInfo(HttpServletRequest request, HttpServletResponse response, HttpSession session)
			throws IOException {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_gjtSignup.auditState", SignupAuditStateEnum.通过.getValue() + "");
		searchParams.put("EQ_xjzt", "3");
		String outputFilePath = gjtStudentInfoService.exportNotRegInfoToPath(searchParams, null,
				request.getSession().getServletContext().getRealPath(""));

		super.downloadFile(request, response, outputFilePath);
		FileKit.delFile(outputFilePath);
	}

	/**
	 * 报名表下载
	 */
	@SysLog("学籍信息-报名表下载")
	@RequiresPermissions("/edumanage/roll/list$outStuWord")
	@RequestMapping(value = "outStuWord/{id}")
	public void outStuWord(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id)
			throws IOException {
		String outputFilePath = gjtStudentInfoService
				.outStuWord(request.getSession().getServletContext().getRealPath(""), id);

		super.downloadFile(request, response, outputFilePath);
		FileKit.delFile(outputFilePath);
	}

	/** 进入导入页面 */
	@RequestMapping(value = "toImport")
	public String toImport() {
		return "edumanage/roll/edumanage_roll_import";
	}

	/** 批量导入修改学籍信息 */
	@SysLog("学籍信息-批量导入修改学籍信息")
	@RequiresPermissions("/edumanage/roll/list$importStuInfo")
	@ResponseBody
	@RequestMapping(value = "importStuInfo")
	public Map importStuInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		String path = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
				+ "roll" + File.separator;
		String fileName = file.getOriginalFilename();
		File targetFile = new File(path, fileName);
		if (!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}
		file.transferTo(targetFile);
		Map result = gjtStudentInfoService.importStuInfo(targetFile, path);
		targetFile.delete();
		return result;
	}

	/** 下载文件 */
	@ResponseBody
	@RequestMapping(value = "download")
	public void download(HttpServletRequest request, HttpServletResponse response, String file) throws IOException {
		if ("template".equals(file)) {
			InputStream in = this.getClass().getResourceAsStream("/excel/model/导入修改学籍信息.xls");
			super.downloadInputStream(response, in, "导入修改学籍信息.xls");
		} else {
			String path = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
					+ "roll" + File.separator;
			super.downloadFile(request, response, path + file);
		}
	}

	/** 根据列表框条件导出学生列表信息 */
	@SysLog("学籍信息-按条件批量导出")
	@RequiresPermissions("/edumanage/roll/list$exportStuInfo")
	@RequestMapping(value = "exportStuInfo", method = RequestMethod.POST)
	public void exportStuInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		String xxId = searchParams.get("EQ_gjtSchoolInfo.id") != null
				? searchParams.get("EQ_gjtSchoolInfo.id").toString() : null;
		if (StringUtils.isBlank(xxId)) {
			xxId = user.getGjtOrg().getId();
			searchParams.put("EQ_gjtSchoolInfo.id", xxId);
		}
		/*
		 * Object gradeId = searchParams.get("EQ_viewStudentInfo.gradeId"); if
		 * (gradeId == null) { gradeId =
		 * gjtGradeService.getCurrentGradeId(xxId);
		 * searchParams.put("EQ_viewStudentInfo.gradeId", gradeId); }
		 */
		searchParams.put("EQ_signupAuditState", SignupAuditStateEnum.通过.getValue() + "");
		String path = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
				+ "roll" + File.separator;
		String outFile = gjtStudentInfoService.outStudentSignupInfo(searchParams, path);
		ToolUtil.download(path + outFile, response);
		FileKit.delFile(path + outFile);
	}

	@ResponseBody
	@RequestMapping(value = "upPic")
	public Map upPic(HttpServletRequest request) {
		Map formMap = new HashMap();
		String path = request.getSession().getServletContext().getRealPath("") + File.separator + "tmp";
		formMap.put("path", path);
		return gjtStudentInfoService.upPic(formMap);
	}

	@RequestMapping(value = "/analogLogin")
	public void analogLogin(String id, 
			HttpServletRequest request, HttpServletResponse response) {		
		try {
			GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(id);

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

	/**
	 * 进入正式学员打印页面
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("/edumanage/roll/list$studentCardPrint")
	@RequestMapping(value = "studentCardPrint/{id}/{userType}")
	public String studentCardPrint(@PathVariable("id") String id, @PathVariable("userType") String userType,
			Model model) {
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(id);
		model.addAttribute("gjtStudentInfo", gjtStudentInfo);
		if (userType.equals("11")) {
			// 正式生
			return "edumanage/roll/formal_student_print";
		} else {
			// 跟读生
			return "edumanage/roll/follow_student_print";
		}
	}

	/**
	 * 跳转到学员转专业页面
	 * 
	 * @param studentId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions("/edumanage/roll/list$toForwordStudentMajor")
	@RequestMapping(value = "toForwordStudentMajor/{studentId}", method = RequestMethod.GET)
	public String toForwordStudentMajor(@PathVariable("studentId") String studentId, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(studentId);// 学员基本信息
		Map<String, String> specialtyMap = null;
		if ("3".equals(user.getGjtOrg().getSchoolModel())) {
			specialtyMap = commonMapService.getSchoolModelSpecialtyMap(user.getGjtOrg().getId());
		} else {
			// specialtyMap =
			// commonMapService.getSpecialtyMap(user.getGjtOrg().getId());
			// 查询学员当前学期下已启用的专业
			specialtyMap = gjtGradeSpecialtyService.getSpecialtyMap(gjtStudentInfo.getXxId(),
					gjtStudentInfo.getViewStudentInfo().getGradeId(),gjtStudentInfo.getPycc());
		}
		model.addAttribute("gjtStudentInfo", gjtStudentInfo);
		model.addAttribute("specialtyMap", specialtyMap);
		return "edumanage/roll/forword_student_major";
	}

	/**
	 * 更新学员的专业
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "forwordStudentMajor", method = RequestMethod.POST)
	public Feedback forwordStudentMajor(HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String newSpecialty = request.getParameter("newSpecialty");// 新专业
		String gradeId = request.getParameter("gradeId");// 学期
		String studentId = request.getParameter("studentId");// 学生ID
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(studentId);// 学员基本信息
		GjtClassInfo gjtClassInfo = gjtClassInfoService.queryTeachClassInfo(studentId);
		String classId = gjtClassInfo.getClassId();// 班级ID
		int bh = 1;// 班号默认为1;
		// 查询学学员所属的学习中心
		GjtOrg gjtOrg = gjtOrgService.queryStudyCenterInfo(gjtStudentInfo.getXxzxId(), "3");
		// 查询新的专业下的产品状态是否开设
		GjtGradeSpecialty gjtGradeSpecialty = gjtGradeSpecialtyService.queryByGradeIdAndSpecialtyId(gradeId,
				newSpecialty);
		if (gjtGradeSpecialty != null) {
			if (gjtGradeSpecialty.getStatus() == 1) {
				//只需要删除教务班级,不需要删除选课和课程班
				gjtClassStudentService.deleteByStudentId(studentId, classId,"学员转学期");
				//创建新的教学班和分班
				try {
					boolean result=gjtClassInfoService.createTeachClassAndRec(gjtStudentInfo,gjtOrg,gjtGradeSpecialty,newSpecialty,gradeId,bh);
					if(!result){
						return new Feedback(false, "内部异常-分配教学班失败");
					}					 
				} catch (Exception e) {
					e.printStackTrace();
					return new Feedback(false, "内部异常-分配教学班失败");
				}
			} else {
				log.error("学期ID：" + gradeId + ";新专业ID" + newSpecialty);
				return new Feedback(false, "新专业暂未启用");
			}
		} else {
			log.error("学期ID：" + gradeId + ";新专业ID" + newSpecialty);
			return new Feedback(false, "该学期暂未开设本专业");
		}
		return new Feedback(true, "转专业成功");
	}
	/**
	 * 跳转到转学期页面
	 * @param studentId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequiresPermissions("/edumanage/roll/list$toForwordStudentGradeId")
	@RequestMapping(value = "toForwordStudentGradeId/{studentId}", method = RequestMethod.GET)
	public String toForwordStudentGradeId(@PathVariable("studentId") String studentId, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(studentId);// 学员基本信息
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		//查询当前的学期
		String currentGradeId = gjtGradeService.getCurrentGradeId(user.getGjtOrg().getId());
		Map<String, String> specialtyMap = null;
		if ("3".equals(user.getGjtOrg().getSchoolModel())) {
			specialtyMap = commonMapService.getSchoolModelSpecialtyMap(user.getGjtOrg().getId());
		} else {
			// 查询学员当前学期下已启用的专业
			specialtyMap = gjtGradeSpecialtyService.getSpecialtyMap(gjtStudentInfo.getXxId(),
					currentGradeId,gjtStudentInfo.getPycc());
		}
		model.addAttribute("gjtStudentInfo", gjtStudentInfo);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("currentGradeId", currentGradeId);
		return "edumanage/roll/forword_student_grade";
	}
	/**
	 * 根据学期查询学期下的专业
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryGjtSpecialty", method = RequestMethod.GET)
	public Map<String,Object> queryGjtSpecialty(HttpServletRequest request){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(searchParams.get("studentId").toString());// 学员基本信息
		Map<String,Object> map=new HashMap<String,Object>();
		Map<String,Object> resultMap=null;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		//查询学期下的专业
		Map<String,String> specialtyMap=gjtGradeSpecialtyService.getSpecialtyMap(gjtStudentInfo.getXxId(),
				ObjectUtils.toString(searchParams.get("gradeId")),gjtStudentInfo.getPycc());
		Set set = specialtyMap.entrySet();
		for(Iterator iter = set.iterator(); iter.hasNext();){
			resultMap=new HashMap();
			Map.Entry entry = (Map.Entry)iter.next();
			 String key = (String)entry.getKey();
			 String value = (String)entry.getValue();
			 resultMap.put("majorId", key);
			 resultMap.put("majorName", value);
			 list.add(resultMap);
		}
		map.put("specialtyMap", list);
		return map;
	}
	/**
	 * 更新学员的学期
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "forwordStudentGrade", method = RequestMethod.POST)
	public Feedback forwordStudentGrade(HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String newSpecialty = request.getParameter("newSpecialty");// 新专业
		String newGradeId = request.getParameter("newGradeId");// 学期
		String studentId = request.getParameter("studentId");// 学生ID
		GjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(studentId);// 学员基本信息
		GjtClassInfo gjtClassInfo = gjtClassInfoService.queryTeachClassInfo(studentId);
		String classId = gjtClassInfo.getClassId();// 班级ID
		int bh=1;
		// 查询学学员所属的学习中心
		GjtOrg gjtStudyCenter = gjtOrgService.queryStudyCenterInfo(gjtStudentInfo.getXxzxId(), "3");
		// 查询新的专业和学期下的产品状态是否开设
		GjtGradeSpecialty gjtGradeSpecialty = gjtGradeSpecialtyService.queryByGradeIdAndSpecialtyId(newGradeId,newSpecialty);
		if(gjtGradeSpecialty!=null){
			if(gjtGradeSpecialty.getStatus()==1){
				//只需要删除教务班级,不需要删除选课和课程班
				gjtClassStudentService.deleteByStudentId(studentId, classId,"学员转学期");
				//创建新的教学班和分班
				try {
					boolean result=gjtClassInfoService.createTeachClassAndRec(gjtStudentInfo,gjtStudyCenter,gjtGradeSpecialty,newSpecialty,newGradeId,bh);
					if(!result){
						return new Feedback(false, "内部异常-分配教学班失败");
					}					 
				} catch (Exception e) {
					e.printStackTrace();
					return new Feedback(false, "内部异常-分配教学班失败");
				}
			}else{
				log.error("学期ID：" + newGradeId + ";新专业ID" + newSpecialty);
				return new Feedback(false, "新专业暂未启用");
			}
		}else{
			log.error("新学期ID：" + newGradeId + ";新专业ID" + newSpecialty);
			return new Feedback(false, "该学期暂未开设本专业");
		}		
		return new Feedback(true, "转学期成功");
	}

	// =============================================== 以下是做批量处理调用 =============================================== //

	/**
	 * 批量更新学期，上传csv/txt格式文件 格式：学号,学期名称,专业规则号
	 * @throws Exception
	 */
	@RequestMapping(value = "/batchForward", method = RequestMethod.GET)
	public void batchForward(HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.outputHtml(
				response,
				FormSubmitUtil.buildRequest("batchForward", Collections.EMPTY_MAP, RequestMethod.POST.name(), "上传", "file")
		);
	}

	/**
	 * 批量更新学期，可以使用Postman请求<br/>
	 * 请求头配置：Content-Type: multipart/form-data 和 Cookie<br/>
	 * 请求体配置：file: 文件<br/>
	 * @param file csv/txt格式文件 格式：学号,学期名称,专业规则号
	 * @throws Exception
	 */
	@RequestMapping(value = "/batchForward", method = RequestMethod.POST)
	public void batchForward(HttpServletRequest request, HttpServletResponse response,
							 @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String xxId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(user.getGjtOrg().getId());
		String path = request.getSession().getServletContext().getRealPath("") + "/static" + File.separator;
		BufferedReader bf = new BufferedReader(new InputStreamReader(file.getInputStream(), Constants.CHARSET));
		String csvFileName = "batchForward_result.csv";
		File csvFile = new File(path + csvFileName);
		if (!csvFile.getParentFile().exists()) {
			csvFile.getParentFile().mkdirs();
		}
		OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(csvFile), Constants.CHARSET);
		BufferedWriter bw = new BufferedWriter(osw);

		String cookie = request.getHeader("Cookie");
		String url = AppConfig.getProperty("syncCourseRecInterface") + "/edumanage/roll/forwordStudentGrade";
		bw.write("请求结果文件地址：" + AppConfig.getProperty("syncCourseRecInterface") + "/static/" + csvFileName);
		bw.newLine();
		String str = null;
		while ((str = bf.readLine()) != null) {
			try {
				String[] strs = str.split(",");
				String studentId = gjtStudentInfoService.queryStudentIdByXh(strs[0]);
				if(studentId == null) {
					bw.write(str + ",学员不存在");
					bw.newLine();
					continue;
				}
				GjtGrade grade = gjtGradeService.findByGradeNameAndGjtSchoolInfoId(strs[1], xxId);
				if(grade == null) {
					bw.write(str + ",年级不存在");
					bw.newLine();
					continue;
				}
				GjtSpecialty specialty = gjtSpecialtyService.queryByRuleCodeAndXxId(strs[2], xxId);
				if(specialty == null) {
					bw.write(str + ",专业不存在");
					bw.newLine();
					continue;
				}

				Map<String, Object> headers = new HashMap<String, Object>();
				headers.put("Cookie", cookie);
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("studentId", studentId);
				params.put("newGradeId", grade.getGradeId());
				params.put("newSpecialty", specialty.getSpecialtyId());
				// 处理有点慢，设置30s超时时间
				String result = HttpClientUtils.doHttpPost(url, headers, params, 30000, Constants.CHARSET);
				bw.write(str + "," + result);
				bw.newLine();
			} catch (Exception e) {
				bw.write(str + "," + e.getMessage());
				bw.newLine();
			}
			bw.flush();
		}

		bf.close();
		bw.close();
		osw.close();
		super.downloadFile(request, response, csvFile.getAbsolutePath());
	}

	/**
	 * 批量导入助学师账号
	 * @throws Exception
	 */
	@RequestMapping(value = "/batchImport", method = RequestMethod.GET)
	public void batchImport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		super.outputHtml(
				response,
				FormSubmitUtil.buildRequest("batchImport", Collections.EMPTY_MAP, RequestMethod.POST.name(), "上传", "file")
		);
	}

	/**
	 * 批量导入助学师账号，可以使用Postman请求<br/>
	 * 请求头配置：Content-Type: multipart/form-data 和 Cookie<br/>
	 * 请求体配置：file: 文件<br/>
	 * @param file csv/txt格式文件 格式：姓名,手机号
	 * @throws Exception
	 */
	@RequestMapping(value = "/batchImport", method = RequestMethod.POST)
	public void batchImport(HttpServletRequest request, HttpServletResponse response,
							@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		BufferedReader bf = new BufferedReader(new InputStreamReader(file.getInputStream(), Constants.CHARSET));
		String outputFileName = "导入结果.csv";
		OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), Constants.CHARSET);
		BufferedWriter bw = new BufferedWriter(osw);

		String fileName = com.gzedu.xlims.common.StringUtils.getBrowserStr(request, outputFileName); // 解决下载文件名乱码问题（兼容性）
		response.setContentType("application/x-msdownload;charset=utf-8");
		// 解决IE下载文件名乱码问题
		// response.setHeader("Content-Disposition", "attachment; filename=" + new String(outputFileName.getBytes("GB2312"), "ISO8859-1");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

		String url = AppConfig.getProperty("syncCourseRecInterface") + "/interface/signupdata/syncSignupData";
		String str = null;
		while ((str = bf.readLine()) != null) {
			try {
				String[] strs = str.split(",");
				String xm = strs[0];
				if(StringUtils.isBlank(xm)) {
					bw.write(str + ",姓名不能为空");
					bw.newLine();
					continue;
				}
				String sjh = strs[1];
				GjtStudentInfo student = gjtStudentInfoService.querySSOByXhOrSfzhOrSjh(sjh);
				if(student != null) {
					bw.write(str + ",学员已存在");
					bw.newLine();
					continue;
				}

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("studentId", UUIDUtils.random());
				params.put("atid", params.get("studentId"));
				params.put("xh", sjh);
				params.put("xm", xm);
				params.put("sfzh", sjh);
				params.put("sjh", sjh);
				params.put("userType", "13");
				params.put("collegeCode", "106");
				params.put("learncenterCode", "SQXXZX201707240002");
				params.put("gradeSpecialtyId", "7b21defdd1334c0d8fff31831557cf0b");
				params.put("orderSn", sjh);
				params.put("charge", "0");
				// 处理有点慢，设置30s超时时间
				String result = HttpClientUtils.doHttpPost(url, params, 30000, Constants.CHARSET);
				bw.write(str + "," + result);
				bw.newLine();
			} catch (Exception e) {
				bw.write(str + "," + e.getMessage());
				bw.newLine();
			}
			bw.flush();
		}

		bf.close();
		bw.close();
		osw.close();
	}

}