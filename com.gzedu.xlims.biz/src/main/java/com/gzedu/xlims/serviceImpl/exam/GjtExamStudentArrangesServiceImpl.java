package com.gzedu.xlims.serviceImpl.exam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.dao.exam.GjtExamAppointmentNewDao;
import com.gzedu.xlims.dao.exam.GjtExamBatchNewDao;
import com.gzedu.xlims.dao.exam.GjtExamPlanNewDao;
import com.gzedu.xlims.dao.exam.GjtExamPointNewDao;
import com.gzedu.xlims.dao.exam.GjtExamRoomNewDao;
import com.gzedu.xlims.pojo.exam.GjtExamAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamBatchNew;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.pojo.exam.GjtExamRoomNew;
import com.gzedu.xlims.service.cache.GjtExamArrangeCache;
import com.gzedu.xlims.service.exam.GjtExamStudentArrangesService;

/**
 * 自动排考相关
 * 
 * @author micarol
 */
@Service
public class GjtExamStudentArrangesServiceImpl implements GjtExamStudentArrangesService {

	@Autowired
	private GjtExamAppointmentNewDao gjtExamAppointmentNewDao;

	@Autowired
	private GjtExamArrangeCache gjtExamArrangeCache;

	@Autowired
	private GjtExamBatchNewDao gjtExamBatchNewDao;
	@Autowired
	private GjtExamPlanNewDao gjtExamPlanNewDao;
	@Autowired
	private GjtExamRoomNewDao gjtExamRoomNewDao;
	@Autowired
	private GjtExamPointNewDao gjtExamPointNewDao;

	/**
	 * 导出排考数据
	 */
	@Override
	public HSSFWorkbook exportArrange(List<Map<String, Object>> list) {
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
		cell.setCellValue("专业");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试批次");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试批次编码");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试科目");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试科目编码");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考试形式");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("试卷号");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考点");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("考场");
		cell = row.createCell(cellIndex++);
		cell.setCellValue("座位号");

		for (Map<String, Object> map : list) {
			cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("STUDENT_NAME")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("XH")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(String.valueOf(map.get("SJH"))));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(getPyccStr(String.valueOf(map.get("PYCC"))));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("GRADE_NAME")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("ZYMC")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("BATCH_NAME")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("EXAM_BATCH_CODE")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("SUBJECT_NAME")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("SUBJECT_CODE")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(getTypeStr((BigDecimal) map.get("TYPE")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("EXAM_NO")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("POINT_NAME")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("ROOM_NAME")));
			cell = row.createCell(cellIndex++);
			cell.setCellValue(String.valueOf(map.get("SEAT_NO")));
		}

		return workbook;
	}

	public static String getTypeStr(BigDecimal type) {
		if (null != type) {
			if (type.intValue() == 1) {
				return "网考";
			} else if (type.intValue() == 2) {
				return "笔试";
			} else if (type.intValue() == 3) {
				return "机考";
			} else if (type.intValue() == 4) {
				return "形考";
			} else if (type.intValue() == 5) {
				return "网考（省）";
			}
		}
		return "";
	}

	// 数据来源: SELECT CODE as ID,NAME FROM TBL_SYS_DATA WHERE TYPE_CODE =
	// 'TrainingLevel' AND IS_DELETED = 'N'
	public static String getPyccStr(String pycc) {
		if (null != pycc) {
			if ("0".equals(pycc)) {
				return "专科";
			} else if ("2".equals(pycc)) {
				return "本科";
			} else if ("4".equals(pycc)) {
				return "中专";
			} else if ("6".equals(pycc)) {
				return "高起专_助力计划";
			} else if ("8".equals(pycc)) {
				return "专升本";
			} else {
				return pycc;
			}
		}
		return "";
	}

	@Override
	public Map<String, Object> autoArrange(String examBatchCode, String examPointId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put(WebConstants.SUCCESSFUL, true);
		Long lock = gjtExamArrangeCache.arrangeLock(examBatchCode, examPointId);// 是否已经正在执行自动排考
		if (lock <= 1) {
			Date now = new Date();
			GjtExamBatchNew examBatch = gjtExamBatchNewDao.queryByExamBatchCode(examBatchCode);
			List<GjtExamRoomNew> rooms = gjtExamRoomNewDao.roomsByPointid(examPointId);// 考场列表
			resultMap = this.isArrangeAvailable(examBatch, rooms.size(), resultMap);
			if ((Boolean) resultMap.get(WebConstants.SUCCESSFUL)) {
				// 获取学年度id
				List<GjtExamPlanNew> plans = gjtExamPlanNewDao.plansForArrangeByBatchCode(examBatchCode);
				Map<String, List<GjtExamPlanNew>> timePlanMap = new LinkedHashMap<String, List<GjtExamPlanNew>>();// 根据考试开始时间对考试计划分组
				List<String> planidList = new ArrayList<String>();
				List<GjtExamPlanNew> tempList = null;
				for (GjtExamPlanNew plan : plans) {
					if (timePlanMap.containsKey(plan.getExamSt().getTime() + "")) {
						timePlanMap.get(plan.getExamSt().getTime() + "").add(plan);
					} else {
						tempList = new ArrayList<GjtExamPlanNew>();
						tempList.add(plan);
						timePlanMap.put(plan.getExamSt().getTime() + "", tempList);
					}
					planidList.add(plan.getExamPlanId());
				}
				int totalSeats = gjtExamPointNewDao.totalSeatsByPoint(examPointId);// 该考点启用考场座位总数
				// 获取预约当前考点的考生id 列表studentidList (term_id 为学年度id)(用于5.3)
				List<String> studentidList = gjtExamPointNewDao.appointStudentidList(examPointId,
						examBatch.getStudyYearId());
				// 获取该考点需要排考的预约记录 arrangeList(后续排考需要使用)
				List<GjtExamAppointmentNew> arrangeList = gjtExamAppointmentNewDao
						.appointmentListForArrange(studentidList, planidList);
				HashMap<String, List<GjtExamAppointmentNew>> planArrangeMap = new HashMap<String, List<GjtExamAppointmentNew>>(
						1024);// 某个考试安排excel 表里有300几门考试安排, 1024 应该最合适
				for (GjtExamAppointmentNew appointment : arrangeList) {
					if (planArrangeMap.containsKey(appointment.getExamPlanId())) {
						planArrangeMap.get(appointment.getExamPlanId()).add(appointment);
					} else {
						List<GjtExamAppointmentNew> tmpList = new ArrayList<GjtExamAppointmentNew>();
						tmpList.add(appointment);
						planArrangeMap.put(appointment.getExamPlanId(), tmpList);
					}
				}

			}
		} else {
			resultMap.put(WebConstants.MESSAGE, "您所选择的批次与考点正在执行上一次自动排考命令, 请稍后再试.");
		}
		return resultMap;
	}

	// 0.1 判断该批次是否符合排考条件:
	// 0.1.0 预约已结束
	// 0.1.1 有对应考点, 考场.
	private Map<String, Object> isArrangeAvailable(GjtExamBatchNew examBatch, int roomCount,
			Map<String, Object> resultMap) {
		resultMap.put(WebConstants.SUCCESSFUL, false);
		if (null != examBatch) {
			if (examBatch.getBookEnd().before(new Date())) {// 预约已结束
				if (roomCount > 0) {
					resultMap.put(WebConstants.SUCCESSFUL, true);
				} else {
					resultMap.put(WebConstants.MESSAGE, "无可用考场, 无法排考");
				}
			} else {
				resultMap.put(WebConstants.MESSAGE, "预约尚未结束, 不能进行排考");
			}

		} else {
			resultMap.put(WebConstants.MESSAGE, "找不到对应 考试批次");
		}
		return resultMap;
	}

}
