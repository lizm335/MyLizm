package com.ouchgzee.headTeacher.serviceImpl.exam;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import com.ouchgzee.headTeacher.pojo.exam.BzrGjtExamBatchNew;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ouchgzee.headTeacher.dao.exam.GjtExamAppointmentDao;
import com.ouchgzee.headTeacher.service.exam.BzrGjtExamAppointmentService;

@Deprecated @Service("bzrGjtExamAppointmentServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtExamAppointmentServiceImpl implements BzrGjtExamAppointmentService {

	@Autowired
	private GjtExamAppointmentDao gjtExamAppointmentDao;

	@Autowired
	private BzrCommonMapService commonMapService;

	@Override
	public Page<Map<String, Object>> findCurrentAppointmentList(String classId, String batchCode,
			Map<String, Object> searchParams, PageRequest pageRequst) {
		return gjtExamAppointmentDao.findCurrentAppointmentList(classId, batchCode, searchParams, pageRequst);
	}

	@Override
	public Page<Map<String, Object>> findHistoryAppointmentList(String classId, String batchCode,
			Map<String, Object> searchParams, PageRequest pageRequst) {
		return gjtExamAppointmentDao.findHistoryAppointmentList(classId, batchCode, searchParams, pageRequst);
	}

	@Override
	public List<Map<String, Object>> findCurrentStudentAppointment(String studentId, String batchCode) {
		return gjtExamAppointmentDao.findCurrentStudentAppointment(studentId, batchCode);
	}

	@Override
	public List<Map<String, Object>> findHistoryStudentAppointment(String studentId, String batchCode) {
		return gjtExamAppointmentDao.findHistoryStudentAppointment(studentId, batchCode);
	}

	@Override
	public List<Map<String, Object>> findStudentPointAppointment(String studentId, String batchCode) {
		return gjtExamAppointmentDao.findStudentPointAppointment(studentId, batchCode);
	}

	/**
	 * 导出考试预约数据
	 */
	@Override
	public HSSFWorkbook exportAppointPlan(List<Map<String, Object>> list,BzrGjtExamBatchNew examBatch ) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row;
		HSSFCell cell;
		int rowIndex = 0;
		int cellIndex = 0;

		// 标题行
		row = sheet.createRow(rowIndex++);
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学生姓名");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学号");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("手机");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("层次");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("年级");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("学期");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("专业");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试预约时间");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("已预约科目数");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("未预约科目数");
//		cell = row.createCell(cellIndex++);
//		cell.setCellValue("考试计划");
//		cell = row.createCell(cellIndex++);
//		cell.setCellValue("考试计划编码");
//		cell = row.createCell(cellIndex++);
//		cell.setCellValue("考试科目");
//		cell = row.createCell(cellIndex++);
//		cell.setCellValue("试卷号");
//		cell = row.createCell(cellIndex++);
//		cell.setCellValue("考试形式");
//		cell = row.createCell(cellIndex++);
//		cell.setCellValue("预约考点");

//		cell = row.createCell(cellIndex++);
//		cell.setCellValue("排考状态");
//		cell = row.createCell(cellIndex++);
//		cell.setCellValue("是否补考");
//		cell = row.createCell(cellIndex++);
//		cell.setCellValue("补考费用");

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String bootTime = df.format(examBatch.getBookSt())+"至"+df.format(examBatch.getBookEnd());// 批次的考试预约时间


		for (Map<String, Object> map : list) {
			cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("xm")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("xh")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("sjh")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(ObjectUtils.toString(map.get("pycc"),"--"));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("year")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("grade")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("specialty")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(bootTime);
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("count1")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("count2")));
//			cell = row.createCell(cellIndex++);
//			cell.setCellValue(String.valueOf(map.get("BATCH_NAME")));
//			cell = row.createCell(cellIndex++);
//			cell.setCellValue(String.valueOf(map.get("EXAM_BATCH_CODE")));
//			cell = row.createCell(cellIndex++);
//			cell.setCellValue(String.valueOf(map.get("EXAM_PLAN_NAME")));
//			cell = row.createCell(cellIndex++);
//			cell.setCellValue(String.valueOf(map.get("EXAM_NO")));
//			cell = row.createCell(cellIndex++);
//			cell.setCellValue(String.valueOf(map.get("EXAM_TYPE_NAME")));
//			cell = row.createCell(cellIndex++);
//			cell.setCellValue(String.valueOf(map.get("POINT_NAME")));
//			cell = row.createCell(cellIndex++);
//			if (map.get("UPDATED_DT") != null) {
//				cell.setCellValue(df.format(map.get("UPDATED_DT")));
//			} else {
//				cell.setCellValue(df.format(map.get("CREATED_DT")));
//			}
//			cell = row.createCell(cellIndex++);
//			cell.setCellValue(getStatusStr((BigDecimal) map.get("STATUS")));
//
//			if (map.get("COURSE_COST") != null) {
//				cell = row.createCell(cellIndex++);
//				cell.setCellValue("是");
//				cell = row.createCell(cellIndex++);
//				cell.setCellValue(String.valueOf(map.get("COURSE_COST")));
//			} else {
//				cell = row.createCell(cellIndex++);
//				cell.setCellValue("否");
//				cell = row.createCell(cellIndex++);
//				cell.setCellValue("--");
//			}
		}

		return workbook;
	}

	private String getStatusStr(BigDecimal status) {
		if (null != status) {
			if (status.intValue() == 0) {
				return "未排考";
			} else if (status.intValue() == 1) {
				return "已排考";
			} else {
				return "已过期";
			}
		}
		return "";
	}
}
