package com.gzedu.xlims.web.controller.practice;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.practice.GjtPracticeAdviser;
import com.gzedu.xlims.pojo.practice.GjtPracticeApply;
import com.gzedu.xlims.pojo.practice.GjtPracticeArrange;
import com.gzedu.xlims.pojo.practice.GjtPracticePlan;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.pojo.status.PracticeStatusEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.practice.GjtPracticeAdviserService;
import com.gzedu.xlims.service.practice.GjtPracticeApplyService;
import com.gzedu.xlims.service.practice.GjtPracticeArrangeService;
import com.gzedu.xlims.service.practice.GjtPracticePlanService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

@Controller
@RequestMapping("/practiceArrange")
public class GjtPracticeArrangeController {

	private final static Logger log = LoggerFactory.getLogger(GjtPracticeArrangeController.class);

	@Autowired
	private GjtPracticeArrangeService gjtPracticeArrangeService;

	@Autowired
	private GjtPracticeAdviserService gjtPracticeAdviserService;

	@Autowired
	private GjtPracticePlanService gjtPracticePlanService;

	@Autowired
	private GjtPracticeApplyService gjtPracticeApplyService;

	@Autowired
	private GjtOrgService gjtOrgService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	private CommonMapService commonMapService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		String orgId = user.getGjtOrg().getId();
		if (!"1".equals(user.getGjtOrg().getOrgType())) {
			orgId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(orgId);
		}

		searchParams.put("EQ_gjtPracticePlan.orgId", orgId);

		// 默认选择当前社会实践计划
		if (EmptyUtils.isEmpty(searchParams) || StringUtils.isBlank((String) searchParams.get("EQ_practicePlanId"))) {
			String gradeId = gjtGradeService.getCurrentGradeId(orgId);
			GjtPracticePlan practicePlan = gjtPracticePlanService.findByGradeIdAndOrgIdAndStatus(gradeId, orgId, 3);
			if (practicePlan != null) {
				searchParams.put("EQ_practicePlanId", practicePlan.getPracticePlanId());
				model.addAttribute("practicePlanId", practicePlan.getPracticePlanId());
			}
		} else if (EmptyUtils.isNotEmpty(searchParams)
				&& EmptyUtils.isNotEmpty(searchParams.get("EQ_practicePlanId"))) {
			model.addAttribute("practicePlanId", ObjectUtils.toString(searchParams.get("EQ_practicePlanId")));
		}

		// 查询“未开始”、“进行中”、“已结束”和“未设置指导老师”
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(searchParams);
		map.put("GT_gjtPracticePlan.applyBeginDt", today); // 未开始
		model.addAttribute("notStart", gjtPracticeArrangeService.findAll(map, pageRequst).getTotalElements());
		map.remove("GT_gjtPracticePlan.applyBeginDt");
		map.put("LTE_gjtPracticePlan.applyBeginDt", today);
		map.put("GTE_gjtPracticePlan.reviewDt", today);
		model.addAttribute("starting", gjtPracticeArrangeService.findAll(map, pageRequst).getTotalElements());
		map.remove("LTE_gjtPracticePlan.applyBeginDt");
		map.remove("GTE_gjtPracticePlan.reviewDt");
		map.put("LT_gjtPracticePlan.reviewDt", today); // 已结束
		model.addAttribute("end", gjtPracticeArrangeService.findAll(map, pageRequst).getTotalElements());
		map.remove("LT_gjtPracticePlan.reviewDt");
		map.put("ISNULL_gjtPracticeAdvisers.adviserId", null); // 未设置指导老师
		model.addAttribute("notSetGuideTeacher", gjtPracticeArrangeService.findAll(map, pageRequst).getTotalElements());

		// 状态判断
		if (request.getParameter("status") != null) {
			int status = Integer.parseInt(request.getParameter("status").toString());
			if (status == 1) { // 未开始
				searchParams.put("GT_gjtPracticePlan.applyBeginDt", today);
			} else if (status == 2) { // 进行中
				searchParams.put("LTE_gjtPracticePlan.applyBeginDt", today);
				searchParams.put("GTE_gjtPracticePlan.reviewDt", today);
			} else if (status == 3) { // 已结束
				searchParams.put("LT_gjtPracticePlan.reviewDt", today);
			} else if (status == 4) { // 未设置指导老师
				searchParams.put("ISNULL_gjtPracticeAdvisers.adviserId", null);
			}
		}

		log.info("searchParams:[" + searchParams + "]");
		Page<GjtPracticeArrange> pageInfo = gjtPracticeArrangeService.findAll(searchParams, pageRequst);
		Date now = new Date();
		for (GjtPracticeArrange practiceArrange : pageInfo) {
			// 设置状态
			if (practiceArrange.getGjtPracticePlan().getApplyBeginDt().compareTo(now) > 0) {
				practiceArrange.setStatus(1);
			} else if (practiceArrange.getGjtPracticePlan().getApplyBeginDt().compareTo(now) <= 0
					&& practiceArrange.getGjtPracticePlan().getReviewDt().compareTo(now) >= 0) {
				practiceArrange.setStatus(2);
			} else if (practiceArrange.getGjtPracticePlan().getReviewDt().compareTo(now) < 0) {
				practiceArrange.setStatus(3);
			}

			if (practiceArrange.getGjtPracticePlan().getSubmitPracticeEndDt().compareTo(now) > 0) {
				practiceArrange.setCanUpdate(true);
			} else {
				practiceArrange.setCanUpdate(false);
			}

			// 统计申请人数
			List<GjtPracticeApply> practiceApplys = gjtPracticeApplyService.findByPracticePlanIdAndSpecialtyBaseId(
					practiceArrange.getPracticePlanId(), practiceArrange.getSpecialtyBaseId());
			if (practiceApplys != null && practiceApplys.size() > 0) {
				practiceArrange.setApplyNum(practiceApplys.size());

				int passNum = 0;
				for (GjtPracticeApply practiceApply : practiceApplys) {
					if (PracticeStatusEnum.PRACTICE_COMPLETED.getValue() == practiceApply.getStatus()) {
						passNum++;
					}
				}
				practiceArrange.setPassNum(passNum);
			} else {
				practiceArrange.setApplyNum(0);
				practiceArrange.setPassNum(0);
			}
		}

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("practicePlanMap", gjtPracticePlanService.getPracticePlanMap(user.getGjtOrg().getId()));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyBaseMap(user.getGjtOrg().getId()));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnUpdate", subject.isPermitted("/practiceArrange/list$update"));
		model.addAttribute("isBtnCreate", subject.isPermitted("/practiceArrange/list$create"));
		model.addAttribute("isBtnView", subject.isPermitted("/practiceArrange/list$view"));

		return "practice/practiceArrange_list";
	}

	/**
	 * 返回新增页面
	 * 
	 * @param model
	 * @return
	 */
	@RequiresPermissions("/practiceArrange/list$create")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request, String practicePlanId) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<GjtPracticePlan> list = gjtPracticePlanService.findByOrgIdAndStatus(user.getGjtOrg().getId(), 3);
		GjtPracticePlan practicePlan = null;
		if (StringUtils.isBlank(practicePlanId)) {
			GjtGrade gjtGrade = gjtGradeService.findCurrentGrade(user.getGjtOrg().getId());
			practicePlan = gjtPracticePlanService.findByGradeIdAndOrgIdAndStatus(gjtGrade.getGradeId(),
					user.getGjtOrg().getId(), 3);
			practicePlanId = practicePlan.getPracticePlanId();// 赋值默认

		} else {
			practicePlan = gjtPracticePlanService.findOne(practicePlanId);
		}

		if (practicePlan != null) {
			// 查询可申请社会实践的专业
			List<Map<String, Object>> specialtyList = gjtPracticeArrangeService.getCanApplySpecialty(
					user.getGjtOrg().getId(), practicePlan.getGradeId(), practicePlan.getPracticePlanId());
			model.addAttribute("specialtyList", specialtyList);
		}
		model.addAttribute("list", list);
		model.addAttribute("practicePlanId", practicePlanId);

		return "practice/practiceArrange_setting";
	}

	@RequiresPermissions("/practiceArrange/list$create")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback create(@RequestParam("practicePlanId") String practicePlanId,
			@RequestParam("specialtyBaseIds") String specialtyBaseIds,
			@RequestParam("canApplyNums") String canApplyNums, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "设置成功");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			List<GjtPracticeArrange> practiceArranges = new ArrayList<GjtPracticeArrange>();
			String[] specialtyBaseIds2 = specialtyBaseIds.split(",");
			String[] canApplyNums2 = canApplyNums.split(",");
			for (int i = 0; i < specialtyBaseIds2.length; i++) {
				GjtPracticeArrange practiceArrange = new GjtPracticeArrange();
				practiceArrange.setPracticePlanId(practicePlanId);
				practiceArrange.setSpecialtyBaseId(specialtyBaseIds2[i]);
				practiceArrange.setCanApplyNum(Integer.parseInt(canApplyNums2[i]));
				practiceArrange.setCreatedBy(user.getId());

				practiceArranges.add(practiceArrange);
			}

			if (practiceArranges.size() > 0) {
				gjtPracticeArrangeService.insert(practiceArranges);
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "设置失败");
			log.error(e.getMessage(), e);
		}

		return feedback;
	}

	/**
	 * 返回编辑页面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("/practiceArrange/list$update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		GjtPracticeArrange practiceArrange = gjtPracticeArrangeService.findOne(id);
		// 统计申请人数
		List<GjtPracticeApply> practiceys = gjtPracticeApplyService.findByPracticePlanIdAndSpecialtyBaseId(
				practiceArrange.getPracticePlanId(), practiceArrange.getSpecialtyBaseId());
		if (practiceys != null && practiceys.size() > 0) {
			practiceArrange.setApplyNum(practiceys.size());
		} else {
			practiceArrange.setApplyNum(0);
		}

		List<GjtPracticeAdviser> advisers = practiceArrange.getGjtPracticeAdvisers();
		if (advisers != null && advisers.size() > 0) {
			for (GjtPracticeAdviser adviser : advisers) {
				Map<String, Object> guideNum = gjtPracticeArrangeService.getGuideNum(
						practiceArrange.getPracticePlanId(), adviser.getTeacherId(),
						practiceArrange.getSpecialtyBaseId());
				if (guideNum.get("num1") != null) {
					adviser.setGuideNum1(((BigDecimal) guideNum.get("num1")).intValue());
				} else {
					adviser.setGuideNum1(0);
				}

				if (guideNum.get("num2") != null) {
					adviser.setGuideNum2(((BigDecimal) guideNum.get("num2")).intValue());
				} else {
					adviser.setGuideNum2(0);
				}
			}
		}

		model.addAttribute("entity", practiceArrange);
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("action", "update");

		return "practice/practiceArrange_form";
	}

	@RequiresPermissions("/practiceArrange/list$update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(@ModelAttribute("entity") GjtPracticeArrange entity, String[] advisers,
			Integer[] adviserNums, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback feedback = new Feedback(true, "保存成功");

		try {
			// 先删除指导老师
			gjtPracticeAdviserService.deleteByArrangeId(entity.getArrangeId());

			GjtPracticeArrange practiceArrange = gjtPracticeArrangeService.findOne(entity.getArrangeId());
			practiceArrange.setUpdatedBy(user.getId());

			practiceArrange.setGjtPracticeAdvisers(new ArrayList<GjtPracticeAdviser>());
			if (advisers != null && advisers.length > 0) {
				for (int i = 0; i < advisers.length; i++) {
					GjtPracticeAdviser adviser = new GjtPracticeAdviser();
					adviser.setArrangeId(practiceArrange.getArrangeId());
					adviser.setAdviserType(1);
					adviser.setTeacherId(advisers[i]);
					adviser.setAdviserNum(adviserNums[i]);

					practiceArrange.getGjtPracticeAdvisers().add(adviser);
				}
			}

			gjtPracticeArrangeService.update(practiceArrange);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "保存失败");
		}

		return feedback;
	}

	@RequiresPermissions("/practiceArrange/list$view")
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		GjtPracticeArrange practiceArrange = gjtPracticeArrangeService.findOne(id);
		// 统计申请人数
		List<GjtPracticeApply> practiceApplys = gjtPracticeApplyService.findByPracticePlanIdAndSpecialtyBaseId(
				practiceArrange.getPracticePlanId(), practiceArrange.getSpecialtyBaseId());
		if (practiceApplys != null && practiceApplys.size() > 0) {
			practiceArrange.setApplyNum(practiceApplys.size());
		} else {
			practiceArrange.setApplyNum(0);
		}

		List<GjtPracticeAdviser> advisers = practiceArrange.getGjtPracticeAdvisers();
		if (advisers != null && advisers.size() > 0) {
			for (GjtPracticeAdviser adviser : advisers) {
				Map<String, Object> guideNum = gjtPracticeArrangeService.getGuideNum(
						practiceArrange.getPracticePlanId(), adviser.getTeacherId(),
						practiceArrange.getSpecialtyBaseId());
				if (guideNum.get("num1") != null) {
					adviser.setGuideNum1(((BigDecimal) guideNum.get("num1")).intValue());
				} else {
					adviser.setGuideNum1(0);
				}

				if (guideNum.get("num2") != null) {
					adviser.setGuideNum2(((BigDecimal) guideNum.get("num2")).intValue());
				} else {
					adviser.setGuideNum2(0);
				}
			}
		}

		model.addAttribute("entity", practiceArrange);
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("action", "view");

		return "practice/practiceArrange_form";
	}

	@RequestMapping(value = "choiceGuideTeacher", method = RequestMethod.GET)
	public String choiceGuideTeacher(@RequestParam("arrangeId") String arrangeId,
			@RequestParam("teacherIds") String teacherIds, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<GjtEmployeeInfo> guideTeachers = gjtEmployeeInfoService.findListByType(EmployeeTypeEnum.论文教师.getNum(),
				EmployeeTypeEnum.社会实践教师.getNum(), user.getGjtOrg().getId());

		if (guideTeachers != null && teacherIds != null && !"".equals(teacherIds)) {
			String[] ids = teacherIds.split(",");
			for (String id : ids) {
				for (GjtEmployeeInfo guideTeacher : guideTeachers) {
					if (id.equals(guideTeacher.getEmployeeId())) {
						guideTeachers.remove(guideTeacher);
						break;
					}
				}
			}
		}

		model.addAttribute("guideTeachers", guideTeachers);

		GjtPracticeArrange practiceArrange = gjtPracticeArrangeService.findOne(arrangeId);
		GjtGrade grade = gjtGradeService.findPrevGradeBefore(practiceArrange.getGjtPracticePlan().getOrgId(),
				practiceArrange.getGjtPracticePlan().getGradeId());
		List<Map<String, Object>> list = gjtPracticeArrangeService.getPreNoPass(
				practiceArrange.getGjtPracticePlan().getOrgId(), grade.getGradeId(),
				practiceArrange.getSpecialtyBaseId());
		model.addAttribute("list", list);

		return "practice/choiceGuideTeacher";
	}

	@RequestMapping(value = "findGuideNum")
	@ResponseBody
	public Map<String, Object> findGuideNum(@RequestParam("arrangeId") String arrangeId,
			@RequestParam("teacherId") String teacherId, HttpServletRequest request) {
		GjtPracticeArrange practiceArrange = gjtPracticeArrangeService.findOne(arrangeId);
		Map<String, Object> guideNum = gjtPracticeArrangeService.getGuideNum(practiceArrange.getPracticePlanId(),
				teacherId, practiceArrange.getSpecialtyBaseId());
		if (guideNum.get("num1") == null) {
			guideNum.put("num1", 0);
		}

		if (guideNum.get("num2") == null) {
			guideNum.put("num2", 0);
		}

		return guideNum;
	}

	@RequestMapping(value = "listStudent", method = RequestMethod.GET)
	public String listStudent(@RequestParam("practicePlanId") String practicePlanId,
			@RequestParam("specialtyBaseId") String specialtyBaseId,
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		GjtPracticePlan practicePlan = gjtPracticePlanService.findOne(practicePlanId);
		Page<Map<String, Object>> pageInfo = gjtPracticeArrangeService.getCanApplyStudent(practicePlan.getOrgId(),
				practicePlan.getGradeId(), practicePlanId, specialtyBaseId, searchParams, pageRequst);

		model.addAttribute("practicePlan", practicePlan);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMapBySpecialtyBaseId(specialtyBaseId));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(user.getGjtOrg().getId()));
		model.addAttribute("practiceStatusMap", EnumUtil.getPracticeApplyStatusStringMap());

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnExport", subject.isPermitted("/practiceArrange/list$exportCanApplyStudent"));

		return "practice/practiceArrange_studentList";
	}

	@RequestMapping(value = "exportStudentList", method = RequestMethod.GET)
	public void exportStudentList(@RequestParam("practicePlanId") String practicePlanId,
			@RequestParam("specialtyBaseId") String specialtyBaseId, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Map<String, Object> searchParams = new HashMap<String, Object>();

		GjtPracticePlan practicePlan = gjtPracticePlanService.findOne(practicePlanId);
		Page<Map<String, Object>> pageInfo = gjtPracticeArrangeService.getCanApplyStudent(practicePlan.getOrgId(),
				practicePlan.getGradeId(), practicePlanId, specialtyBaseId, searchParams, pageRequst);
		HSSFWorkbook workbook = this.getStudentListWorkbook(practicePlan, pageInfo.getContent());
		ExcelUtil.downloadExcelFile(response, workbook, "可申请学生明细表.xls");
	}

	private HSSFWorkbook getStudentListWorkbook(GjtPracticePlan practicePlan, List<Map<String, Object>> list) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("可申请学生明细表");
			String[] heads = { "实践计划名称", "实践计划编号", "姓名", "学号", "手机", "层次", "学期", "专业名称", "专业代码", "专业规则号", "所属学习中心",
					"状态" };

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

			Map<String, String> practiceStatusMap = EnumUtil.getPracticeApplyStatusStringMap();

			for (Map<String, Object> obj : list) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(practicePlan.getPracticePlanName());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(practicePlan.getPracticePlanCode());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("studentName") != null ? obj.get("studentName").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("studentCode") != null ? obj.get("studentCode").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("mobile") != null ? obj.get("mobile").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("trainingLevel") != null ? obj.get("trainingLevel").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("gradeName") != null ? obj.get("gradeName").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("specialtyName") != null ? obj.get("specialtyName").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("specialtyCode") != null ? obj.get("specialtyCode").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("ruleCode") != null ? obj.get("ruleCode").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(obj.get("scName") != null ? obj.get("scName").toString() : "--");

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(
						obj.get("status") != null ? practiceStatusMap.get(obj.get("status").toString()) : "--");
			}

			return wb;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
