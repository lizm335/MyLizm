package com.ouchgzee.headTeacher.web.controller.practice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.json.JsonUtils;
import com.gzedu.xlims.pojo.practice.GjtPracticeApply;
import com.gzedu.xlims.pojo.practice.GjtPracticeGuideRecord;
import com.gzedu.xlims.pojo.practice.GjtPracticePlan;
import com.gzedu.xlims.pojo.practice.GjtPracticeStudentProg;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.practice.GjtPracticeApplyService;
import com.gzedu.xlims.service.practice.GjtPracticeGuideRecordService;
import com.gzedu.xlims.service.practice.GjtPracticePlanService;
import com.gzedu.xlims.service.practice.GjtPracticeStudentProgService;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.service.student.BzrGjtClassService;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

@Controller
@RequestMapping("/home/class/practiceApply")
public class GjtPracticeApplyController extends BaseController {

	@Autowired
	private GjtPracticePlanService gjtPracticePlanService;

	@Autowired
	private GjtPracticeApplyService gjtPracticeApplyService;

	@Autowired
	private GjtPracticeStudentProgService gjtPracticeStudentProgService;

	@Autowired
	private GjtPracticeGuideRecordService gjtPracticeGuideRecordService;

	@Autowired
	BzrGjtClassService gjtClassService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private CommonMapService commonMapService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());
		BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_gjtPracticePlan.orgId", employeeInfo.getXxId());

		String classId = super.getCurrentClassId(request.getSession());
		List<String> students = gjtClassService.queryTeacherClassStudent(classId);
		if (students == null) {
			students = new ArrayList<String>();
			students.add("-1");
		} else if (students.size() == 0) {
			students.add("-1");
		}
		searchParams.put("IN_studentId", students);

		// 默认选择当前社会实践计划
		if (EmptyUtils.isEmpty(searchParams) || StringUtils.isBlank((String) searchParams.get("EQ_practicePlanId"))) {
			String gradeId = gjtGradeService.getCurrentGradeId(employeeInfo.getXxId());
			GjtPracticePlan practicePlan = gjtPracticePlanService.findByGradeIdAndOrgIdAndStatus(gradeId,
					employeeInfo.getXxId(), 3);
			if (practicePlan != null) {
				searchParams.put("EQ_practicePlanId", practicePlan.getPracticePlanId());
				model.addAttribute("practicePlanId", practicePlan.getPracticePlanId());
			}
		} else if (EmptyUtils.isNotEmpty(searchParams)
				&& EmptyUtils.isNotEmpty(searchParams.get("EQ_practicePlanId"))) {
			model.addAttribute("practicePlanId", ObjectUtils.toString(searchParams.get("EQ_practicePlanId")));
		}

		Page<GjtPracticeApply> pageInfo = gjtPracticeApplyService.findAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("practicePlanMap", gjtPracticePlanService.getPracticePlanMap(employeeInfo.getXxId()));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(employeeInfo.getXxId()));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(employeeInfo.getXxId()));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("practiceStatusMap", EnumUtil.getPracticeApplyStatusMap());

		return "new/class/practice/practiceApply_list";
	}

	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(@RequestParam("practicePlanId") String practicePlanId,
			@RequestParam("studentId") String studentId, Model model, HttpServletRequest request) {
		GjtPracticeApply practiceApply = gjtPracticeApplyService.findByPracticePlanIdAndStudentId(practicePlanId,
				studentId);
		List<GjtPracticeStudentProg> studentProgs = gjtPracticeStudentProgService
				.findByPracticePlanIdAndStudentId(practicePlanId, studentId);
		List<GjtPracticeGuideRecord> guideRecords = gjtPracticeGuideRecordService
				.findByPracticePlanIdAndStudentId(practicePlanId, studentId, "Asc");

		// 构建进度和进度对应的指导记录
		Map<String, List<GjtPracticeGuideRecord>> studentProgRecord = new HashMap<String, List<GjtPracticeGuideRecord>>();
		if (studentProgs != null && studentProgs.size() > 0 && guideRecords != null && guideRecords.size() > 0) {
			for (GjtPracticeStudentProg studentProg : studentProgs) {
				for (GjtPracticeGuideRecord guideRecord : guideRecords) {
					if (guideRecord.getProgressCode().equals(studentProg.getProgressCode())) {
						List<GjtPracticeGuideRecord> records = studentProgRecord.get(studentProg.getProgressId());
						if (records == null) {
							records = new ArrayList<GjtPracticeGuideRecord>();
							records.add(guideRecord);
							studentProgRecord.put(studentProg.getProgressId(), records);
						} else {
							records.add(guideRecord);
						}
					}
				}
			}
		}

		model.addAttribute("practiceApply", practiceApply);
		model.addAttribute("studentProgs", studentProgs);
		model.addAttribute("studentProgRecord", studentProgRecord);
		model.addAttribute("practiceStatusMap", EnumUtil.getPracticeApplyStatusMap());
		model.addAttribute("practiceProgressCodeMap", EnumUtil.getPracticeProgressCodeMap());
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("expressMap", EnumUtil.getExpressMap());

		return "new/class/practice/practiceApply_detail";
	}

	@RequestMapping(value = "export", method = RequestMethod.GET)
	public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());
		BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();
		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_gjtThesisPlan.orgId", employeeInfo.getXxId());

		String classId = super.getCurrentClassId(request.getSession());
		List<String> students = gjtClassService.queryTeacherClassStudent(classId);
		if (students == null) {
			students = new ArrayList<String>();
			students.add("-1");
		} else if (students.size() == 0) {
			students.add("-1");
		}
		searchParams.put("IN_studentId", students);

		Page<GjtPracticeApply> pageInfo = gjtPracticeApplyService.findAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		HSSFWorkbook workbook = this.getWorkbook(pageInfo.getContent());
		ExcelUtil.downloadExcelFile(response, workbook, "社会实践记录明细表.xls");
	}

	private HSSFWorkbook getWorkbook(List<GjtPracticeApply> list) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("社会实践记录明细表");
			String[] heads = { "实践计划名称", "实践计划编号", "姓名", "学号", "手机", "邮箱", "层次", "学期", "专业", "指导老师", "实践成绩", "状态" };

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

			Map<Integer, String> statusMap = EnumUtil.getPracticeApplyStatusMap();
			Map<String, String> pyccMap = commonMapService.getPyccMap();

			for (GjtPracticeApply apply : list) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtPracticePlan().getPracticePlanName());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtPracticePlan().getPracticePlanCode());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtStudentInfo().getXm());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtStudentInfo().getXh());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtStudentInfo().getSjh());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtStudentInfo().getDzxx());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(pyccMap.get(apply.getGjtStudentInfo().getGjtSpecialty().getPycc()));

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtStudentInfo().getGjtGrade().getGradeName());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtStudentInfo().getGjtSpecialty().getZymc());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (apply.getGuideTeacher1() != null) {
					cell.setCellValue(apply.getGuideTeacher1().getXm());
				} else {
					cell.setCellValue("--");
				}

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (apply.getReviewScore() != null) {
					cell.setCellValue(apply.getReviewScore().toString());
				} else {
					cell.setCellValue("--");
				}

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(statusMap.get(apply.getStatus()));
			}

			return wb;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 查询物流信息
	 * 
	 * @param applyId
	 * @return
	 * @throws CommonException
	 */
	@RequestMapping(value = "queryLogistics")
	@SuppressWarnings("unchecked")
	public String queryLogistics(@RequestParam String applyId, Model model) {
		GjtPracticeApply practiceApply = gjtPracticeApplyService.findOne(applyId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", practiceApply.getExpressCompany());
		params.put("postid", practiceApply.getExpressNumber());
		String returnStr = HttpClientUtils.doHttpPost("https://www.kuaidi100.com/query", params, 3000, "utf-8");
		Map<String, Object> returnMap = JsonUtils.toObject(returnStr, HashMap.class);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (returnMap.get("status") != null && "200".equals(returnMap.get("status").toString())) {
			list = (List<Map<String, Object>>) returnMap.get("data");
		} else {
			if (returnMap.get("message") != null) {
				model.addAttribute("errorMsg", returnMap.get("message").toString());
			} else {
				model.addAttribute("errorMsg", "查询物流信息异常");
			}
		}

		Map<String, String> expressMap = EnumUtil.getExpressMap();
		model.addAttribute("expressCompany", expressMap.get(practiceApply.getExpressCompany()));
		model.addAttribute("expressNumber", practiceApply.getExpressNumber());
		model.addAttribute("list", list);

		return "new/class/practice/practiceApply_logistics";
	}

}
