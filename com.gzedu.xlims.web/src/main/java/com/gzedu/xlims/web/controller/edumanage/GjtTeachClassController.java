/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.edumanage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.organization.GjtClassInfoDao;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtClassStudent;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.service.organization.GjtStudyCenterService;
import com.gzedu.xlims.service.signup.SignupDataAddService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 
 * 功能说明：教学班级管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月24日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Controller
@RequestMapping("/edumanage/teachclass")
public class GjtTeachClassController {

	private static final Logger log = LoggerFactory.getLogger(GjtTeachClassController.class);

	@Autowired
	GjtClassInfoService gjtClassInfoService;

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
	CacheService cacheService;

	@Autowired
	private SignupDataAddService signupDataAddService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtStudyCenterService gjtStudyCenterService;
	
	@Autowired
	GjtSpecialtyService gjtSpecialtyService;
	
	@Autowired
	GjtStudentInfoService gjtStudentInfoService;
	
	@Autowired
	GjtClassInfoDao gjtClassInfoDao;
	
	@Autowired
	GjtClassStudentService gjtClassStudentService;
	
	// 查询所有教学班级
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, String judge,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		if (request.getParameter("search_EQ_gjtGrade.gradeId") == null) {
			String currentGradeId = gjtGradeService.getCurrentGradeId(user.getGjtOrg().getId());
			searchParams.put("EQ_gjtGrade.gradeId", currentGradeId);
			model.addAttribute("currentGradeId", currentGradeId);
		}
		searchParams.put("EQ_classType", "teach");// 教学班级管理
		if ("0".equals(judge)) {// 查询有班主任的教学班级
			searchParams.put("ISNOTNULL_gjtBzr", "1");
		} else if ("1".equals(judge)) {// 查询没有班主任的教学班级
			searchParams.put("ISNULL_gjtBzr", "1");
		}

		log.info("教务班级查询参数：{},orgId={}", searchParams, user.getGjtOrg().getId());
		Page<GjtClassInfo> page = gjtClassInfoService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);

		List<String> classIds = new ArrayList<String>();
		for (GjtClassInfo gjtClassInfo : page) {
			classIds.add(gjtClassInfo.getClassId());
		}

		if (EmptyUtils.isNotEmpty(classIds)) {
			Map<String, Integer> map = gjtClassInfoService.queryClassPeople(classIds);// 统计班级人数
			for (GjtClassInfo gjtClassInfo : page) {
				gjtClassInfo.setNum(map.get(gjtClassInfo.getClassId()));// 班级人数
			}
		}

		searchParams.put("ISNOTNULL_gjtBzr", "1");// 统计设置班主任的班级
		searchParams.remove("ISNULL_gjtBzr");
		model.addAttribute("isSet", gjtClassInfoService.countClassInfo(user.getGjtOrg().getId(), searchParams));
		searchParams.put("ISNULL_gjtBzr", "1");// 统计未设置班主任的班级
		searchParams.remove("ISNOTNULL_gjtBzr");
		long count = gjtClassInfoService.countClassInfo(user.getGjtOrg().getId(), searchParams);
		model.addAttribute("unSet", count);
		Map<String, String> orgMap = commonMapService.getOrgTree(user.getGjtOrg().getId(), false);
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		// List<CacheService.Value> specialtyCategory =
		// cacheService.getCachedDictionary("SPECIALTY_CATEGORY");// 专业性质
		// List<CacheService.Value> trainingLevel =
		// cacheService.getCachedDictionary("TrainingLevel");// 培养层次

		model.addAttribute("orgMap", orgMap);
		model.addAttribute("gradeMap", gradeMap);
		// model.addAttribute("specialtyCategory", specialtyCategory);
		// model.addAttribute("trainingLevel", trainingLevel);
		model.addAttribute("pageInfo", page);

		String prefix = Servlets.encodeParameterStringWithPrefix(searchParams, "search_");
		model.addAttribute("searchParams", prefix);
		return "edumanage/teachclass/list";
	}

	@SysLog("教务班级管理-导出教务班级列表")
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public void export(Model model, String judge, HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		searchParams.put("EQ_classType", "teach");// 教学班级管理

		log.info("教务班级查询参数：{},orgId={}", searchParams, user.getGjtOrg().getId());
		List<GjtClassInfo> list = gjtClassInfoService.queryAllExport(user.getGjtOrg().getId(), searchParams);

		List<String> classIds = new ArrayList<String>();
		for (GjtClassInfo gjtClassInfo : list) {
			classIds.add(gjtClassInfo.getClassId());
		}

		if (EmptyUtils.isNotEmpty(classIds)) {
			Map<String, Integer> map = gjtClassInfoService.queryClassPeople(classIds);// 统计班级人数
			for (GjtClassInfo gjtClassInfo : list) {
				gjtClassInfo.setNum(map.get(gjtClassInfo.getClassId()));// 班级人数
			}
		}
		try {
			HSSFWorkbook workbook = this.getTeachClassExcel(list);
			ExcelUtil.downloadExcelFile(response, workbook, "教务班级列表.xls");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private HSSFWorkbook getTeachClassExcel(List<GjtClassInfo> list) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("教务班级列表");
			String[] heads = { "学期", "班级名称", "班级人数", "班主任账号", "班主任名称", "所属机构" };

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

			for (GjtClassInfo item : list) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;
				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				GjtGrade gjtGrade = item.getGjtGrade();
				if (gjtGrade != null) {
					cell.setCellValue(gjtGrade.getGradeName());
				} else {
					cell.setCellValue("学期信息错误");
				}

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(item.getBjmc());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (item.getNum() != null) {
					cell.setCellValue(item.getNum());
				} else {
					cell.setCellValue(0);
				}

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				GjtEmployeeInfo gjtBzr = item.getGjtBzr();
				if (gjtBzr != null) {
					cell.setCellValue(gjtBzr.getZgh());
				} else {
					cell.setCellValue("");
				}

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (gjtBzr != null) {
					cell.setCellValue(gjtBzr.getXm());
				} else {
					cell.setCellValue("未分配班主任");
				}

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				GjtStudyCenter gjtStudyCenter = item.getGjtStudyCenter();
				if(gjtStudyCenter != null){
					cell.setCellValue(gjtStudyCenter.getScName());
				}else{
					GjtOrg gjtOrg=item.getGjtOrg();
					if(gjtOrg!=null){
						cell.setCellValue(gjtOrg.getOrgName());
					}else{
						cell.setCellValue("查询不到所属机构");
					}
				}
				/*if (gjtStudyCenter != null) {
					cell.setCellValue(gjtStudyCenter.getScName());
				} else {
					cell.setCellValue("查询不到所属机构");
				}*/
			}
			return wb;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 *
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId()); // 学校
		Map<String, String> headTeacherMap = commonMapService.getEmployeeMap(user.getGjtOrg().getId(),
				EmployeeTypeEnum.班主任);// 班主任

		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("headTeacherMap", headTeacherMap);
		model.addAttribute("item", gjtClassInfoService.queryById(id));
		model.addAttribute("action", "view");
		return "edumanage/teachclass/form";
	}

	@RequiresPermissions("/edumanage/teachclass/list$create")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtOrg gjtOrg = user.getGjtOrg();
		Map<String, String> schoolInfoMap = new HashMap<String, String>();
		GjtOrg gjtSchoolInfo = gjtOrgService.queryParentBySonId(gjtOrg.getId());

		schoolInfoMap.put("id", gjtSchoolInfo.getId());
		schoolInfoMap.put("name", gjtSchoolInfo.getOrgName());
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		GjtGrade gjtGrade = gjtGradeService.findCurrentGrade(gjtSchoolInfo.getId());
		model.addAttribute("currentGradeId", gjtGrade.getGradeId());
		Map<String, String> gradeMap = commonMapService.getGradeMap(gjtSchoolInfo.getId());
		model.addAttribute("gradeMap", gradeMap);

		// model.addAttribute("headTeacherMap", headTeacherMap);
		model.addAttribute("orgName", gjtOrg.getOrgName());
		model.addAttribute("item", new GjtClassInfo());
		model.addAttribute("action", "create");
		return "edumanage/teachclass/form";
	}

	/**
	 * 根据学院id获取该学院的年级
	 * 
	 * @param gjtSchoolInfoId
	 * @return
	 */
	@RequestMapping(value = "queryGradeBySchool", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryGradeBySchool(String gjtSchoolInfoId) {
		Map<String, String> map = commonMapService.getGradeMap(gjtSchoolInfoId);
		Set set = map.entrySet();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>(set);
		return list;
	}

	@SysLog("教务班级新增")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtClassInfo item, RedirectAttributes redirectAttributes, String gradeId, String bjmc,
			String gjtSchoolInfoId, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		// GjtEmployeeInfo gjtEmployeeInfo =
		// gjtEmployeeInfoService.queryById(gjtEmployeeId);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		GjtGrade gjtGrade = gjtGradeService.queryById(gradeId);
		GjtOrg gjtOrg = gjtOrgService.queryById(gjtSchoolInfoId);
		GjtSchoolInfo gjtSchoolInfo = gjtOrg.getGjtSchoolInfo();
		GjtStudyCenter gjtStudyCenter = user.getGjtOrg().getGjtStudyCenter();
		try {
			item.setClassId(UUIDUtils.random());
			item.setClassType("teach");
			item.setCreatedDt(DateUtils.getNowTime());
			item.setCreatedBy(user.getId());
			item.setGjtSchoolInfo(gjtSchoolInfo);
			// item.setGjtBzr(gjtEmployeeInfo);
			item.setGjtStudyCenter(gjtStudyCenter);
			item.setGjtOrg(user.getGjtOrg());
			item.setOrgCode(user.getGjtOrg().getCode());
			item.setGjtGrade(gjtGrade);
			item.setBjmc(bjmc.trim());
			item.setIsDeleted("N");
			item.setVersion(BigDecimal.valueOf(2.5));
			item.setIsEnabled("1");
			gjtClassInfoService.saveEntity(item);
		} catch (Exception e) {
			feedback = new Feedback(false, "创建失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/teachclass/list";
	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtClassInfo classinfo = gjtClassInfoService.queryById(id);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtOrg gjtOrg = user.getGjtOrg();
		GjtOrg gjtSchoolInfo = gjtOrgService.queryParentBySonId(user.getGjtOrg().getId());
		Map<String, String> schoolInfoMap = new HashMap<String, String>();
		schoolInfoMap.put("id", gjtSchoolInfo.getId());
		schoolInfoMap.put("name", gjtSchoolInfo.getOrgName());
		Map<String, String> gradeMap = commonMapService.getGradeMap(classinfo.getGjtSchoolInfo().getId());// 年级列表

		// Map<String, String> headTeacherMap =
		// commonMapService.getEmployeeMap(user.getGjtOrg().getId(),EmployeeTypeEnum.班主任);//
		// 班主任
		model.addAttribute("item", classinfo);
		// model.addAttribute("headTeacherMap", headTeacherMap);
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("orgName", gjtOrg.getOrgName());
		model.addAttribute("action", "update");
		return "edumanage/teachclass/form";
	}

	@SysLog("教务班级修改")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(GjtClassInfo item, RedirectAttributes redirectAttributes, String gjtSchoolInfoId,
			String gjtEmployeeId, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "更新成功");
		// GjtEmployeeInfo gjtEmployeeInfo =
		// gjtEmployeeInfoService.queryById(gjtEmployeeId);
		// GjtSchoolInfo gjtSchoolInfo =
		// gjtSchoolInfoService.queryById(gjtSchoolInfoId);
		try {
			GjtClassInfo gjtClassInfo = gjtClassInfoService.queryById(item.getClassId());
			// gjtClassInfo.setGjtSchoolInfo(gjtSchoolInfo);
			// gjtClassInfo.setGjtBzr(gjtEmployeeInfo);
			gjtClassInfo.setBjmc(item.getBjmc());
			gjtClassInfo.setUpdatedDt(DateUtils.getNowTime());
			gjtClassInfo.setCreatedBy(user.getId());
			gjtClassInfo.setMemo(item.getMemo());
			gjtClassInfo.setLimitNum(item.getLimitNum());
			gjtClassInfoService.updateEntity(gjtClassInfo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/teachclass/list";
	}

	// 单个删除和多个删除
	@SysLog("教务班级删除")
	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids) throws IOException {
		if (StringUtils.isNotBlank(ids)) {
			String[] selectedIds = ids.split(",");
			try {
				gjtClassInfoService.deleteById(selectedIds);
				return new Feedback(true, "删除成功");
			} catch (Exception e) {
				return new Feedback(false, "删除失败");
			}
		}
		return new Feedback(false, "删除失败");
	}

	// 校验帐号是否存在
	@RequestMapping(value = "checkLogin")
	@ResponseBody
	public Feedback checkLogin(HttpServletRequest request, String bjmc) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Boolean boolean1 = gjtClassInfoService.queryByBjmc(bjmc, user.getGjtOrg().getId());
		Feedback fe = new Feedback(boolean1, "");
		fe.setSuccessful(boolean1);
		return fe;
	}

	// 查询出院校所有班主任，所有的班级，进行1对多批量分配班主任
	@RequestMapping(value = "allotHeadTeacher", method = RequestMethod.GET)
	public String allotHeadTeacher(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map<String, String> classInfoMap = commonMapService.getClassInfoMap(user.getGjtOrg().getId(), "teach"); // 学校

		Map<String, String> headTeacherMap = commonMapService.getEmployeeMap(user.getGjtOrg().getId(),
				EmployeeTypeEnum.班主任);// 班主任
		model.addAttribute("classInfoMap", classInfoMap);
		model.addAttribute("headTeacherMap", headTeacherMap);
		return "edumanage/teachclass/allotHeadTeacher";
	}

	// 批量分配班主任，弹窗，列表，传参
	@RequestMapping(value = "editeAllot", method = RequestMethod.GET)
	public String editeAllot(Model model, String ids, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<Map<String, String>> headTeacherList = gjtEmployeeInfoService
				.queryAllEmployeeByOrgId(user.getGjtOrg().getId(), EmployeeTypeEnum.班主任.getNum());
		model.addAttribute("headTeacherList", headTeacherList);
		model.addAttribute("ids", ids);
		return "edumanage/teachclass/editeAllot";
	}

	// 保存批量分配班主任
	@SysLog("教务班级批量分配班主任")
	@ResponseBody
	@RequestMapping(value = "allotHeadTeacherUpdate", method = RequestMethod.POST)
	public Feedback allotHeadTeacherUpdate(HttpServletRequest request, Model model, String employeeId, String ids) {
		Feedback fb = new Feedback(true, "分配班主任成功！ ");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryById(employeeId);
			if (StringUtils.isNotBlank(ids)) {
				String[] selectedIds = ids.split(",");
				for (String classId : selectedIds) {
					GjtClassInfo classInfo = gjtClassInfoService.queryById(classId);
					classInfo.setUpdatedBy(user.getId());
					classInfo.setUpdatedDt(DateUtils.getNowTime());
					classInfo.setGjtBzr(employeeInfo);
					gjtClassInfoService.updateEntity(classInfo);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, "保存失败 " + e.getMessage());
		}
		return fb;
	}

	@RequestMapping(value = "setHeadTeacher")
	public String setHeadTeacher(Model model, HttpServletRequest request, String classId) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		List<Map<String, String>> headTeacherList = gjtEmployeeInfoService
				.queryAllEmployeeByOrgId(user.getGjtOrg().getId(), EmployeeTypeEnum.班主任.getNum());

		Map<String, String> employee = gjtEmployeeInfoService.queryByClassId(classId);

		model.addAttribute("headTeacherList", headTeacherList);
		model.addAttribute("employee", employee);
		model.addAttribute("classId", classId);
		return "/edumanage/teachclass/setHeadTeacher";
	}

	@SysLog("教务班级分配班主任")
	@ResponseBody
	@RequestMapping(value = "saveHeadTeacher", method = RequestMethod.POST)
	public Feedback saveHeadTeacher(HttpServletRequest request, Model model, String employeeId, String classId) {
		Feedback fb = null;
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			GjtClassInfo classInfo = gjtClassInfoService.queryById(classId);
			GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryById(employeeId);
			classInfo.setUpdatedBy(user.getId());
			classInfo.setUpdatedDt(DateUtils.getNowTime());
			classInfo.setGjtBzr(employeeInfo);
			boolean result=false;
			//更换教务班级的班主任
			boolean flag = gjtClassInfoService.updateEntity(classInfo);
			if(flag){
				//把教务班级新的班主任同步至EE
				result=gjtClassInfoService.syncClassTeacherToEeChat(classInfo);
				if(result){
					fb = new Feedback(flag, "更换班主任成功");
				}else{
					fb = new Feedback(flag, "更换班主任失败");
				}
			}else{
				fb = new Feedback(flag, "保存失败");
			}			
			
		} catch (Exception e) {
			fb = new Feedback(false, "保存失败 " + e.getMessage());
		}
		return fb;

	}

	/**
	 * 同步教务班级至EE平台
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequiresPermissions("/edumanage/teachclass/list$syncClassToEeChat")
	@ResponseBody
	@RequestMapping(value = "syncClassToEeChat", method = RequestMethod.POST)
	public Feedback syncClassToEeChat(HttpServletRequest request, Model model) {
		try {
			String classId = request.getParameter("classId");// 班级ID
			GjtClassInfo classInfo = gjtClassInfoService.queryById(classId);
			if (classInfo.getGjtBzr() != null) {
				Boolean result = signupDataAddService.createGroupNoAndAddAllStudent(classId);
				if (result) {
					return new Feedback(true, "同步EE平台成功");
				}
			} else {
				return new Feedback(false, "请对本教务班级分配班主任");
			}
		} catch (Exception e) {
			return new Feedback(false, "同步EE平台失败，服务器异常！");
		}
		return new Feedback(false, "同步EE平台失败");
	}
	/**
	 * 同步教务班级至EE平台，临时处理方法
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "syncClassToEeChat1", method = RequestMethod.POST)
	public Feedback syncClassToEeChat1(HttpServletRequest request, Model model) {
		try {
			String classIds = request.getParameter("classId");// 班级ID
			String[] split = classIds.split(",");
			for (int i = 0; i < split.length; i++) {
				String classId = split[i];
				GjtClassInfo classInfo = gjtClassInfoService.queryById(classId);
				if (classInfo.getGjtBzr() != null) {
					Boolean result = signupDataAddService.createGroupNoAndAddAllStudent(classId);
					if (result) {
						System.out.println("同步EE平台成功");
					}
				} else {
					System.out.println("请对本教务班级分配班主任:"+classId);
				}
			}
		} catch (Exception e) {
			return new Feedback(false, "同步EE平台失败，服务器异常！");
		}
		return new Feedback(false, "同步EE平台成功");
	}
	
	/**
	 * 重新分配教学班  1/年级+专业+学习中心（简称）+层次+序号;2/年级+专业+层次+序号
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "createGjtClassInfo", method = RequestMethod.GET)
	public Feedback createGjtClassInfo(HttpServletRequest request){
		try {
			String xxId = request.getParameter("xxId");// 院校ID
			List<GjtGrade> gjtGradeList=gjtGradeService.findGradeIdByOrgIdNew(xxId);//查询院校下的学期
			for(int i=0;i<gjtGradeList.size();i++){
				GjtGrade gjtGrade=gjtGradeList.get(i);
				List<GjtSpecialty> specialtyList=gjtSpecialtyService.findSpecialtyByOrgId(xxId);
				for(int k=0;k<specialtyList.size();k++){
					GjtSpecialty gjtSpecialty=specialtyList.get(k);
					//查询年级专业下学员
					List<GjtStudentInfo> studentInfoList=gjtStudentInfoService.findStudent(xxId, gjtGrade.getGradeId(), gjtSpecialty.getSpecialtyId());
					if(studentInfoList!=null&&studentInfoList.size()>0){
						//创建教学班						
						GjtClassInfo classInfos = new GjtClassInfo();
						String classId=UUIDUtils.random();
						String pyccName = "";
						if ("0".equals(gjtSpecialty.getPycc())) {
							pyccName = "Z";
						} else if ("2".equals(gjtSpecialty.getPycc())) {
							pyccName = "B";
						}
						String bjmc = gjtGrade.getGradeName() + gjtSpecialty.getZymc()+ pyccName+"01班";
						if("2f5bfcce71fa462b8e1f65bcd0f4c632".equals(xxId)){
							if("98b4d0bb7599458dbf0f9aa147c67f1d".equals(gjtGrade.getGradeId())){//2018春
								classInfos.setGjtBzr(gjtEmployeeInfoService.queryById("757356e506f54d38b18ecfc409d5f983"));								
							}else{
								classInfos.setGjtBzr(gjtEmployeeInfoService.queryById("62698cb7ef394eaa9a3528d3640a57be"));	
							}
						}  
						if("9b2f42ececf64f38af621554d1ea5b79".equals(xxId)){
							classInfos.setGjtBzr(gjtEmployeeInfoService.queryById("09ff9be24f6d4e34815bcb835c97e9c7"));
						}
						classInfos.setClassId(classId);
						classInfos.setGjtGrade(gjtGrade);
						classInfos.setActualGradeId(gjtGrade.getGradeId());
						classInfos.setSpecialtyId(gjtSpecialty.getSpecialtyId());
						classInfos.setPycc(gjtSpecialty.getPycc());
						classInfos.setClassType("teach");
						classInfos.setGjtSchoolInfo(gjtGrade.getGjtSchoolInfo());
						classInfos.setGjtOrg(new GjtOrg(xxId));
						classInfos.setBjmc(bjmc);
						classInfos.setMemo("201712重新初始化教务班级");
						GjtClassInfo teachClassInfo=gjtClassInfoDao.save(classInfos);
						//添加学员至教务班级
						for(int j=0;j<studentInfoList.size();j++){
							GjtStudentInfo gjtStudentInfo=studentInfoList.get(j);
							GjtClassStudent gjtClassStudent=new GjtClassStudent();
							gjtClassStudent.setClassStudentId(UUIDUtils.random());
							gjtClassStudent.setGjtClassInfo(teachClassInfo);
							gjtClassStudent.setGjtStudentInfo(gjtStudentInfo);
							gjtClassStudent.setGjtSchoolInfo(new GjtSchoolInfo(xxId));
							gjtClassStudent.setGjtStudyCenter(new GjtStudyCenter(xxId));
							gjtClassStudent.setGjtOrg(new GjtOrg(xxId));
							gjtClassStudent.setMemo("201712重新初始化教务班级");
							gjtClassStudentService.save(gjtClassStudent);
						}
						//把教务班级同步至EE
//						Boolean result=signupDataAddService.createGroupNoAndAddAllStudent(classId);
					}				
						
				}
			}
		} catch (Exception e) {
			return new Feedback(false, "创建教学班失败");
		}
		return new Feedback(false, "创建教学班失败");
	}
}
