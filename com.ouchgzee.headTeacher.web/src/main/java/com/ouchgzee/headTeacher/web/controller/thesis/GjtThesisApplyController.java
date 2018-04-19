package com.ouchgzee.headTeacher.web.controller.thesis;

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
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.pojo.thesis.GjtThesisApply;
import com.gzedu.xlims.pojo.thesis.GjtThesisGuideRecord;
import com.gzedu.xlims.pojo.thesis.GjtThesisPlan;
import com.gzedu.xlims.pojo.thesis.GjtThesisStudentProg;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.thesis.GjtThesisApplyService;
import com.gzedu.xlims.service.thesis.GjtThesisGuideRecordService;
import com.gzedu.xlims.service.thesis.GjtThesisPlanService;
import com.gzedu.xlims.service.thesis.GjtThesisStudentProgService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.service.student.BzrGjtClassService;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

@Controller
@RequestMapping("/home/class/thesisApply")
public class GjtThesisApplyController extends BaseController {

	@Autowired
	private GjtThesisPlanService gjtThesisPlanService;

	@Autowired
	private GjtThesisApplyService gjtThesisApplyService;

	@Autowired
	private GjtThesisStudentProgService gjtThesisStudentProgService;

	@Autowired
	private GjtThesisGuideRecordService gjtThesisGuideRecordService;

	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	BzrGjtClassService gjtClassService;

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

		// 默认选择当前论文计划
		if (EmptyUtils.isEmpty(searchParams) || StringUtils.isBlank((String) searchParams.get("EQ_thesisPlanId"))) {
			String gradeId = gjtGradeService.getCurrentGradeId(employeeInfo.getXxId());
			GjtThesisPlan thesisPlan = gjtThesisPlanService.findByGradeIdAndOrgIdAndStatus(gradeId,
					employeeInfo.getXxId(), 3);
			if (thesisPlan != null) {
				searchParams.put("EQ_thesisPlanId", thesisPlan.getThesisPlanId());
				model.addAttribute("thesisPlanId", thesisPlan.getThesisPlanId());
			}
		} else if (EmptyUtils.isNotEmpty(searchParams) && EmptyUtils.isNotEmpty(searchParams.get("EQ_thesisPlanId"))) {
			model.addAttribute("thesisPlanId", ObjectUtils.toString(searchParams.get("EQ_thesisPlanId")));
		}

		Page<GjtThesisApply> pageInfo = gjtThesisApplyService.findAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("thesisPlanMap", gjtThesisPlanService.getThesisPlanMap(employeeInfo.getXxId()));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(employeeInfo.getXxId()));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(employeeInfo.getXxId()));
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("thesisStatusMap", EnumUtil.getThesisApplyStatusMap());

		List<GjtEmployeeInfo> defenceTeachers = gjtEmployeeInfoService.findListByType(EmployeeTypeEnum.论文教师.getNum(),
				EmployeeTypeEnum.论文答辩教师.getNum(), employeeInfo.getXxId());
		model.addAttribute("defenceTeachers", defenceTeachers);

		return "new/class/thesis/thesisApply_list";
	}

	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(@RequestParam("thesisPlanId") String thesisPlanId, @RequestParam("studentId") String studentId,
			Model model, HttpServletRequest request) {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());

		GjtThesisApply thesisApply = gjtThesisApplyService.findByThesisPlanIdAndStudentId(thesisPlanId, studentId);
		List<GjtThesisStudentProg> studentProgs = gjtThesisStudentProgService
				.findByThesisPlanIdAndStudentId(thesisPlanId, studentId);
		List<GjtThesisGuideRecord> guideRecords = gjtThesisGuideRecordService
				.findByThesisPlanIdAndStudentId(thesisPlanId, studentId);

		// 构建进度和进度对应的指导记录
		Map<String, List<GjtThesisGuideRecord>> studentProgRecord = new HashMap<String, List<GjtThesisGuideRecord>>();
		if (studentProgs != null && studentProgs.size() > 0 && guideRecords != null && guideRecords.size() > 0) {
			for (GjtThesisStudentProg studentProg : studentProgs) {
				for (GjtThesisGuideRecord guideRecord : guideRecords) {
					if (guideRecord.getProgressCode().equals(studentProg.getProgressCode())) {
						List<GjtThesisGuideRecord> records = studentProgRecord.get(studentProg.getProgressId());
						if (records == null) {
							records = new ArrayList<GjtThesisGuideRecord>();
							records.add(0, guideRecord);
							studentProgRecord.put(studentProg.getProgressId(), records);
						} else {
							records.add(0, guideRecord);
						}
					}
				}
			}
		}

		model.addAttribute("thesisApply", thesisApply);
		model.addAttribute("studentProgs", studentProgs);
		model.addAttribute("studentProgRecord", studentProgRecord);
		model.addAttribute("thesisStatusMap", EnumUtil.getThesisApplyStatusMap());
		model.addAttribute("thesisProgressCodeMap", EnumUtil.getThesisProgressCodeMap());
		model.addAttribute("pyccMap", commonMapService.getPyccMap());
		model.addAttribute("expressMap", EnumUtil.getExpressMap());

		List<GjtEmployeeInfo> defenceTeachers = gjtEmployeeInfoService.findListByType(EmployeeTypeEnum.论文教师.getNum(),
				EmployeeTypeEnum.论文答辩教师.getNum(), employeeInfo.getXxId());
		model.addAttribute("defenceTeachers", defenceTeachers);

		return "new/class/thesis/thesisApply_detail";
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

		Page<GjtThesisApply> pageInfo = gjtThesisApplyService.findAll(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		HSSFWorkbook workbook = this.getWorkbook(pageInfo.getContent(), employeeInfo.getXxId());
		ExcelUtil.downloadExcelFile(response, workbook, "毕业论文记录明细表.xls");
	}

	private HSSFWorkbook getWorkbook(List<GjtThesisApply> list, String orgId) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("毕业论文记录明细表");
			String[] heads = { "论文计划名称", "论文计划编号", "姓名", "学号", "手机", "邮箱", "层次", "学期", "专业", "指导老师", "初评成绩", "答辩老师",
					"答辩形式", "答辩成绩", "状态" };

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

			Map<Integer, String> statusMap = EnumUtil.getThesisApplyStatusMap();
			Map<String, String> pyccMap = commonMapService.getPyccMap();
			List<GjtEmployeeInfo> defenceTeachers = gjtEmployeeInfoService
					.findListByType(EmployeeTypeEnum.论文教师.getNum(), EmployeeTypeEnum.论文答辩教师.getNum(), orgId);

			for (GjtThesisApply apply : list) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtThesisPlan().getThesisPlanName());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(apply.getGjtThesisPlan().getThesisPlanCode());

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

				String defenceTeacherStr = "--";
				if (apply.getDefenceTeacher1() != null || apply.getDefenceTeacher2() != null) {
					if (apply.getDefenceTeacher1() != null) {
						String[] ids = apply.getDefenceTeacher1().split(",");
						for (String id : ids) {
							for (GjtEmployeeInfo defenceTeacher : defenceTeachers) {
								if (defenceTeacher.getEmployeeId().equals(id)) {
									defenceTeacherStr += defenceTeacher.getXm() + ",";
								}
							}
						}
					}

					if (apply.getDefenceTeacher2() != null) {
						String[] ids = apply.getDefenceTeacher2().split(",");
						for (String id : ids) {
							for (GjtEmployeeInfo defenceTeacher : defenceTeachers) {
								if (defenceTeacher.getEmployeeId().equals(id)) {
									defenceTeacherStr += defenceTeacher.getXm() + ",";
								}
							}
						}
					}
				}
				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				cell.setCellValue(defenceTeacherStr);

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (apply.getNeedDefence() == 1) {
					if (apply.getDefenceType() == 1) {
						cell.setCellValue("现场答辩");
					} else if (apply.getDefenceType() == 2) {
						cell.setCellValue("远程答辩");
					} else {
						cell.setCellValue("--");
					}
				} else {
					cell.setCellValue("无需答辩");
				}

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (apply.getDefenceScore() != null) {
					cell.setCellValue(apply.getDefenceScore().toString());
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
		GjtThesisApply thesisApply = gjtThesisApplyService.findOne(applyId);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", thesisApply.getExpressCompany());
		params.put("postid", thesisApply.getExpressNumber());
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
		model.addAttribute("expressCompany", expressMap.get(thesisApply.getExpressCompany()));
		model.addAttribute("expressNumber", thesisApply.getExpressNumber());
		model.addAttribute("list", list);

		return "new/class/thesis/thesisApply_logistics";
	}

}
