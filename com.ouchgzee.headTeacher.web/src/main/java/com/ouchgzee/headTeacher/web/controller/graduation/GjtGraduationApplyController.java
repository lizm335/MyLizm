package com.ouchgzee.headTeacher.web.controller.graduation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentInfo;
import com.ouchgzee.headTeacher.pojo.graduation.BzrGjtGraduationApply;
import com.ouchgzee.headTeacher.pojo.graduation.BzrGjtGraduationGuideRecord;
import com.ouchgzee.headTeacher.pojo.graduation.BzrGjtGraduationStudentProg;
import com.ouchgzee.headTeacher.pojo.status.EmployeeTypeEnum;
import com.ouchgzee.headTeacher.pojo.status.EnumUtil;
import com.ouchgzee.headTeacher.pojo.status.GraduationApplyStatusEnum;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.service.employee.BzrGjtEmployeeInfoService;
import com.ouchgzee.headTeacher.service.graduation.BzrGjtGraduationApplyService;
import com.ouchgzee.headTeacher.service.graduation.BzrGjtGraduationBatchService;
import com.ouchgzee.headTeacher.service.graduation.BzrGjtGraduationGuideRecordService;
import com.ouchgzee.headTeacher.service.graduation.BzrGjtGraduationStudentProgService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

@Controller
@RequestMapping("/home/class/graduation/apply")
public class GjtGraduationApplyController extends BaseController {
	
	private static final Log log = LogFactory.getLog(GjtGraduationApplyController.class);

	@Autowired
	private BzrGjtGraduationApplyService gjtGraduationApplyService;

	@Autowired
	private BzrGjtGraduationBatchService gjtGraduationBatchService;

	@Autowired
	private BzrCommonMapService commonMapService;

	@Autowired
	private BzrGjtStudentService gjtStudentInfoService;

	@Autowired
	private BzrGjtGraduationStudentProgService gjtGraduationStudentProgService;

	@Autowired
	private BzrGjtGraduationGuideRecordService gjtGraduationGuideRecordService;

	@Autowired
	private BzrGjtEmployeeInfoService gjtEmployeeInfoService;

	/**
	 * 查询毕业申请列表
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			Model model, HttpServletRequest request, HttpSession session) {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", employeeInfo.getXxId());
		searchParams.put("EQ_classId", super.getCurrentClassId(session));
		Page<?> pageInfo = gjtGraduationApplyService.queryAll(searchParams, pageRequst);
		model.addAttribute("pageInfo", pageInfo);
		
		//查询“论文终稿已确认”状态和“论文终稿待确认”状态的论文
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("EQ_orgId", employeeInfo.getXxId());
		map.put("EQ_classId", super.getCurrentClassId(session));
		map.put("EQ_isConfirm", "1");
		model.addAttribute("isConfirm", gjtGraduationApplyService.queryAll(map, pageRequst).getTotalElements());
		map.put("EQ_isConfirm", "0");
		model.addAttribute("notConfirm", gjtGraduationApplyService.queryAll(map, pageRequst).getTotalElements());
		
		model.addAttribute("thesisStatusMap", EnumUtil.getThesisApplyStatusMap());
		model.addAttribute("practiceStatusMap", EnumUtil.getPracticeApplyStatusMap());
		model.addAttribute("batchMap", gjtGraduationBatchService.getGraduationBatchMap(employeeInfo.getXxId()));
		model.addAttribute("specialtyMap", commonMapService.getSpecialtyMap(employeeInfo.getXxId()));
		model.addAttribute("gradeMap", commonMapService.getGradeMap(employeeInfo.getXxId()));
		model.addAttribute("trainingLevelMap", commonMapService.getPyccMap());

		return "new/class/graduation/apply/list";
	}
	
	/**
	 * 论文终稿确认
	 * @param applyId
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "confirm")
	@ResponseBody
	public Feedback confirm(@RequestParam("applyId") String applyId, HttpServletRequest request, HttpSession session) {
		log.info("applyId:" + applyId);
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		BzrGjtGraduationApply graduationApply = gjtGraduationApplyService.queryOne(applyId);
		if (graduationApply.getStatus() == GraduationApplyStatusEnum.THESIS_COLLEGE_CONFIRM_THESIS.getValue()) {
			graduationApply.setStatus(GraduationApplyStatusEnum.THESIS_REVIEW.getValue());
			graduationApply.setUpdatedBy(employeeInfo.getGjtUserAccount().getId());
			gjtGraduationApplyService.update(graduationApply);
			
			return new Feedback(true, "确认成功");
		} else {
			return new Feedback(false, "确认失败，原因:状态不符");
		}
	}
	
	/**
	 * 查询学生申请详情
	 * @param studentId
	 * @param batchId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(@RequestParam("studentId") String studentId, @RequestParam("batchId") String batchId,
			Model model, HttpServletRequest request, HttpSession session) {
		log.info("studentId:" + studentId + ", batchId:" + batchId);

		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		BzrGjtStudentInfo gjtStudentInfo = gjtStudentInfoService.queryById(studentId);
		
		//论文申请记录
		BzrGjtGraduationApply thesisApply = gjtGraduationApplyService.queryOneByStudent(batchId, studentId, 1);
		List<BzrGjtGraduationStudentProg> thesisProgList = gjtGraduationStudentProgService.queryListByStudent(batchId, studentId, 1);
		List<BzrGjtGraduationGuideRecord> thesisRecordList = gjtGraduationGuideRecordService.queryListByStudent(batchId, studentId, 1);
		
		//构建进度和进度对应的指导记录
		Map<String, List<BzrGjtGraduationGuideRecord>> thesisProgRecord = new HashMap<String, List<BzrGjtGraduationGuideRecord>>();
		if (thesisProgList != null && thesisProgList.size() > 0 && thesisRecordList != null && thesisRecordList.size() > 0) {
			for (BzrGjtGraduationStudentProg prog : thesisProgList) {
				for (BzrGjtGraduationGuideRecord record : thesisRecordList) {
					if (record.getProgressCode().equals(prog.getProgressCode())) {
						List<BzrGjtGraduationGuideRecord> records = thesisProgRecord.get(prog.getProgressId());
						if (records == null) {
							records = new ArrayList<BzrGjtGraduationGuideRecord>();
							records.add(record);
							thesisProgRecord.put(prog.getProgressId(), records);
						} else {
							records.add(record);
						}
					}
				}
			}
		}

		//社会实践申请记录
		BzrGjtGraduationApply practiceApply = gjtGraduationApplyService.queryOneByStudent(batchId, studentId, 2);
		List<BzrGjtGraduationStudentProg> practiceProgList = gjtGraduationStudentProgService.queryListByStudent(batchId, studentId, 2);
		List<BzrGjtGraduationGuideRecord> practiceRecordList = gjtGraduationGuideRecordService.queryListByStudent(batchId, studentId, 2);

		//构建进度和进度对应的指导记录
		Map<String, List<BzrGjtGraduationGuideRecord>> practiceProgRecord = new HashMap<String, List<BzrGjtGraduationGuideRecord>>();
		if (practiceProgList != null && practiceProgList.size() > 0 && practiceRecordList != null && practiceRecordList.size() > 0) {
			for (BzrGjtGraduationStudentProg prog : practiceProgList) {
				for (BzrGjtGraduationGuideRecord record : practiceRecordList) {
					if (record.getProgressCode().equals(prog.getProgressCode())) {
						List<BzrGjtGraduationGuideRecord> records = practiceProgRecord.get(prog.getProgressId());
						if (records == null) {
							records = new ArrayList<BzrGjtGraduationGuideRecord>();
							records.add(record);
							practiceProgRecord.put(prog.getProgressId(), records);
						} else {
							records.add(record);
						}
					}
				}
			}
		}

		model.addAttribute("studentInfo", gjtStudentInfo);
		model.addAttribute("thesisApply", thesisApply);
		model.addAttribute("thesisProgList", thesisProgList);
		model.addAttribute("thesisProgRecord", thesisProgRecord);
		model.addAttribute("practiceApply", practiceApply);
		model.addAttribute("practiceProgList", practiceProgList);
		model.addAttribute("practiceProgRecord", practiceProgRecord);
		model.addAttribute("trainingLevelMap", commonMapService.getPyccMap());
		model.addAttribute("thesisProgressCodeMap", EnumUtil.getThesisProgressCodeMap());
		model.addAttribute("practiceProgressCodeMap", EnumUtil.getPracticeProgressCodeMap());
		model.addAttribute("thesisStatusMap", EnumUtil.getThesisApplyStatusMap());
		model.addAttribute("practiceStatusMap", EnumUtil.getPracticeApplyStatusMap());
		
		//指导老师列表
		List<BzrGjtEmployeeInfo> thesisGuideTeacherList = gjtEmployeeInfoService.findListByType(EmployeeTypeEnum.论文教师.getNum(), EmployeeTypeEnum.Thesis.论文指导教师.getNum(), employeeInfo.getXxId());
		List<BzrGjtEmployeeInfo> practiceGuideTeacherList = gjtEmployeeInfoService.findListByType(EmployeeTypeEnum.论文教师.getNum(), EmployeeTypeEnum.Thesis.社会实践教师.getNum(), employeeInfo.getXxId());
		model.addAttribute("thesisGuideTeacherList", thesisGuideTeacherList);
		model.addAttribute("practiceGuideTeacherList", practiceGuideTeacherList);
		
		return "new/class/graduation/apply/view";
	}

	/**
	 * 导出明细
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public void export(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		String fileName = "毕业申请学生明细.xls";
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		PageRequest pageRequst = Servlets.buildPageRequest(1, Integer.MAX_VALUE);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", employeeInfo.getXxId());
		searchParams.put("EQ_classId", super.getCurrentClassId(session));
		Page<?> pageInfo = gjtGraduationApplyService.queryAll(searchParams, pageRequst);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>)pageInfo.getContent();
		HSSFWorkbook workbook = this.getWorkbook(list);
		this.downloadExcelFile(response, workbook, fileName);
	}
	
	private HSSFWorkbook getWorkbook(List<Map<String, Object>> list) {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();  
		    HSSFSheet sheet = wb.createSheet("毕业申请学生明细");
		    String[] heads = {"学号", "个人信息", "报读信息", "毕业批次", "毕业论文", "社会实践", "是否申请学位", "论文终稿确认"};

			Map<Integer, String> thesisStatusMap = EnumUtil.getThesisApplyStatusMap();
			Map<Integer, String> practiceStatusMap = EnumUtil.getPracticeApplyStatusMap();
		    
	    	HSSFRow row = sheet.createRow(0);
	    	for (int i = 0; i < heads.length; i++) {
	    		HSSFCell cell = row.createCell(i);
	    		cell.setCellType(Cell.CELL_TYPE_STRING);
	    		cell.setCellValue(heads[i]);
	    	}
			    
			int rowIdx = 1;
			int colIdx = 0;
			HSSFCell cell;
			StringBuffer sb = new StringBuffer();

			sheet.createFreezePane(0, 1); //冻结列：冻结0列1行
			
			//为了能够使用换行，需要设置单元格的样式 wrap=true  
	        HSSFCellStyle s = wb.createCellStyle();  
	        s.setWrapText(true); 

			for (Map<String, Object> map : list) {
				row = sheet.createRow(rowIdx++);
				colIdx = 0;
				
				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				sb.append((map.get("studentCode") == null ? "--" : map.get("studentCode").toString()));
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());
				
				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				sb.append("姓名：" + (map.get("studentName") == null ? "--" : map.get("studentName").toString()));
				sb.append("\n手机：" + (map.get("phone") == null ? "--" : map.get("phone").toString()));
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				sb.append("层次：" + (map.get("trainingLevel") == null ? "--" : map.get("trainingLevel").toString()));
				sb.append("\n年级：" + (map.get("grade") == null ? "--" : map.get("grade").toString()));
				sb.append("\n专业：" + (map.get("specialtyName") == null ? "--" : map.get("specialtyName").toString()));
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				sb.append(map.get("batchName1") == null ? map.get("batchName2").toString() : map.get("batchName1").toString());
				sb.append("(");
				sb.append(map.get("batchCode1") == null ? map.get("batchCode2").toString() : map.get("batchCode1").toString());
				sb.append(")");
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (map.get("applyId1") == null) {
					sb.append("答辩形式：--");
					sb.append("\n指导老师：--");
					sb.append("\n答辩老师：--");
					sb.append("\n初评成绩：--");
					sb.append("\n答辩成绩：--");
					sb.append("\n状态：未申请");
				} else {
					if (Integer.parseInt(map.get("needDefence1").toString()) == 0) {
						sb.append("答辩形式：无需答辩");
					} else if (Integer.parseInt(map.get("needDefence1").toString()) == 1) {
						sb.append("答辩形式：现场答辩");
					} else if (Integer.parseInt(map.get("needDefence1").toString()) == 2) {
						sb.append("答辩形式：远程答辩");
					}
					sb.append("\n指导老师：" + (map.get("guideTeacherName1") == null ? "--" : map.get("guideTeacherName1").toString()));
					sb.append("\n答辩老师：" + (map.get("defenceTeacherName1") == null ? "--" : map.get("defenceTeacherName1").toString()));
					sb.append("\n初评成绩：" + (map.get("reviewScore1") == null ? "--" : map.get("reviewScore1").toString()));
					sb.append("\n答辩成绩：" + (map.get("defenceScore1") == null ? "--" : map.get("defenceScore1").toString()));
					sb.append("\n状态：" + thesisStatusMap.get(Integer.parseInt(map.get("status1").toString())));
				}
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());

				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				if (map.get("applyId2") == null) {
					sb.append("指导老师：--");
					sb.append("\n实践成绩：--");
					sb.append("\n状态：未申请");
				} else {
					sb.append("指导老师：" + (map.get("guideTeacherName2") == null ? "--" : map.get("guideTeacherName2").toString()));
					sb.append("\n实践成绩：" + (map.get("reviewScore2") == null ? "--" : map.get("reviewScore2").toString()));
					sb.append("\n状态：" + practiceStatusMap.get(Integer.parseInt(map.get("status2").toString())));
				}
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());
				
				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				Object applyDegree1 = map.get("applyDegree1");
				if (applyDegree1 == null) {
					sb.append("--");
				} else if (Integer.parseInt(applyDegree1.toString()) == 0) {
					sb.append("否");
				} else if (Integer.parseInt(applyDegree1.toString()) == 1) {
					sb.append("是");
				}
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());
				
				cell = row.createCell(colIdx++);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(s);
				Object status1 = map.get("status1");
				if (status1 == null) {
					sb.append("--");
				} else if (Integer.parseInt(status1.toString()) > 6) {
					sb.append("已确认");
				} else {
					sb.append("待确认");
				}
				cell.setCellValue(sb.toString());
				sb.delete(0, sb.length());
				
				//增加单元格的高度 以能够容纳6行字  
		        row.setHeightInPoints(6 * sheet.getDefaultRowHeightInPoints());  
			}
			
			sheet.setColumnWidth(0, 10000);
			sheet.setColumnWidth(1, 10000);
			sheet.setColumnWidth(2, 10000);
			sheet.setColumnWidth(3, 10000);
			sheet.setColumnWidth(4, 10000);
			sheet.setColumnWidth(5, 10000);
			sheet.setColumnWidth(6, 10000);
			
			return wb;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
