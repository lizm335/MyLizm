/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.exam;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.exam.GjtExamAppointmentNewDao;
import com.gzedu.xlims.dao.exam.GjtExamBatchNewDao;
import com.gzedu.xlims.dao.exam.GjtExamPlanNewDao;
import com.gzedu.xlims.dao.exam.GjtExamPointNewDao;
import com.gzedu.xlims.dao.exam.GjtExamRoomNewDao;
import com.gzedu.xlims.dao.exam.GjtExamRoundNewDao;
import com.gzedu.xlims.dao.exam.GjtExamStudentRoomNewDao;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.service.exam.GjtExamAppointmentNewService;
import com.gzedu.xlims.serviceImpl.exam.temp.RoomVO;
import com.gzedu.xlims.serviceImpl.exam.temp.StudentVO;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年11月30日
 * @version 2.5
 *
 */
@Service
public class ExportExamStudentRoomService {

	@Autowired
	GjtExamStudentRoomNewDao gjtExamStudentRoomNewDao;
	@Autowired
	GjtExamRoomNewDao gjtExamRoomNewDao;
	@Autowired
	GjtExamAppointmentNewDao gjtExamAppointmentNewDao;
	@Autowired
	GjtExamRoundNewDao gjtExamRoundNewDao;
	@Autowired
	GjtExamAppointmentNewService gjtExamAppointmentNewService;
	@Autowired
	GjtStudentInfoDao gjtStudentInfoDao;
	@Autowired
	GjtExamBatchNewDao gjtExamBatchNewDao;
	@Autowired
	GjtExamPointNewDao gjtExamPointNewDao;
	@Autowired
	GjtExamPlanNewDao gjtExamPlanNewDao;

	// 表单座位数量32一张纸
	static final int size = 32;

	public static void main(String[] args) throws Exception {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Integer rowIndex = 0;
		sheet.setColumnWidth(0, 23 * 256);
		sheet.setColumnWidth(1, 8 * 256);
		sheet.setColumnWidth(2, 17 * 256);
		sheet.setColumnWidth(3, 9 * 256);
		sheet.setColumnWidth(4, 9 * 256);
		sheet.setColumnWidth(5, 22 * 256);
		sheet.setColumnWidth(6, 8 * 256);
		sheet.setColumnWidth(7, 13 * 256);

		ExportExamStudentRoomService s = new ExportExamStudentRoomService();
		List<RoomVO> roomVOs = new ArrayList<RoomVO>();
		roomVOs.add(s.getTestData5());
		roomVOs.add(s.getTestData4());
		roomVOs.add(s.getTestData2());
		roomVOs.add(s.getTestData());
		roomVOs.add(s.getTestData3());
		Collections.sort(roomVOs, new Comparator<RoomVO>() {
			public int compare(RoomVO o1, RoomVO o2) {
				return o1.roomName.compareTo(o2.roomName);
			}
		});
		Collections.sort(roomVOs);

		for (RoomVO roomVo : roomVOs) {
			Collections.sort(roomVo.students);
			// 获得9位的学号学员信息
			List<StudentVO> equal9Students = roomVo.getEqual9Students();
			if (equal9Students.size() > 0) {
				for (int i = 0; i < equal9Students.size() / size + 1; i++) {
					int startIndex = i * size;
					int endIndex = equal9Students.size() > startIndex + size ? startIndex + size
							: equal9Students.size();
					rowIndex = s.createTitle(workbook, sheet, roomVo, rowIndex++);
					rowIndex = s.createList(workbook, sheet, equal9Students, rowIndex++, startIndex, endIndex);
					rowIndex++;

				}
			}

			// 不是9位学号学员信息
			List<StudentVO> than9Students = roomVo.getThan9Students();
			if (than9Students.size() > 0) {
				for (int i = 0; i < than9Students.size() / size + 1; i++) {
					int startIndex = i * size;
					int endIndex = than9Students.size() > startIndex + size ? startIndex + size : than9Students.size();
					rowIndex = s.createTitle(workbook, sheet, roomVo, rowIndex++);
					rowIndex = s.createList(workbook, sheet, than9Students, rowIndex++, startIndex, endIndex);
					rowIndex++;
				}
			}
		}

		FileOutputStream outputStream = new FileOutputStream("E://abc.xlsx");

		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();
	}

	public int createTitle(Workbook workbook, Sheet sheet, RoomVO roomVo, int rowIndex) {
		Cell cell;
		// 设置标题字体
		Font headfont = workbook.createFont();
		headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short) 16);// 字体大小

		Font cellfont = workbook.createFont();
		cellfont.setFontName("微软雅黑");
		cellfont.setFontHeightInPoints((short) 9);// 字体大小

		Font tablefont = workbook.createFont();
		tablefont.setFontName("微软雅黑");
		tablefont.setFontHeightInPoints((short) 10);// 字体大小

		CellStyle headstyle = workbook.createCellStyle();
		headstyle.setFont(headfont);
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中

		CellStyle cellstyle = workbook.createCellStyle();
		cellstyle.setFont(cellfont);
		cellstyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 靠左
		cellstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中

		CellStyle tablestyle = workbook.createCellStyle();
		tablestyle.setFont(cellfont);
		tablestyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		tablestyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		tablestyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
		tablestyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
		tablestyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
		tablestyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体

		// 0
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 7));
		Row row0 = sheet.createRow(rowIndex++);
		row0.setHeight((short) 660);
		row0.setRowStyle(headstyle);
		cell = row0.createCell(0);
		cell.setCellValue(roomVo.title);
		cell.setCellStyle(headstyle);
		// 1
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 2));
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 3, 5));
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 6, 7));
		Row row1 = sheet.createRow(rowIndex++);
		row1.setRowStyle(cellstyle);
		row1.setHeight((short) 440);
		// 1-1
		cell = row1.createCell(0);
		cell.setCellValue("考点：" + roomVo.pointName);
		cell.setCellStyle(cellstyle);
		// sheet.setColumnWidth(0, 20 * 100);

		// 1-2
		cell = row1.createCell(3);
		cell.setCellValue("科目：" + roomVo.courseName);
		cell.setCellStyle(cellstyle);

		// 1-3
		cell = row1.createCell(6);
		cell.setCellValue("考试方式：开/闭卷");
		cell.setCellStyle(cellstyle);

		// 2
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 2));
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 6, 7));
		Row row2 = sheet.createRow(rowIndex++);
		row2.setRowStyle(cellstyle);
		row2.setHeight((short) 440);
		// 2-1
		cell = row2.createCell(0);
		cell.setCellValue("试卷号：" + roomVo.examNo);
		cell.setCellStyle(cellstyle);

		// 2-2
		cell = row2.createCell(3);
		cell.setCellValue("考场号：" + roomVo.roomName);
		cell.setCellStyle(cellstyle);

		// 2-3
		cell = row2.createCell(5);
		cell.setCellValue("考试时间：" + roomVo.examDay);
		cell.setCellStyle(cellstyle);

		// 2-4
		cell = row2.createCell(6);
		cell.setCellValue(roomVo.examTime);
		cell.setCellStyle(cellstyle);

		// 3考生签名 座位号 学号 姓名 性别 考生身份证号 留考标志 成绩
		Row row3 = sheet.createRow(rowIndex++);
		// row3.setRowStyle(tablestyle);
		row3.setHeight((short) 550);
		// 3-0
		cell = row3.createCell(0);
		cell.setCellValue("考生签名");
		cell.setCellStyle(tablestyle);
		// 3-1
		cell = row3.createCell(1);
		cell.setCellValue("座位号");
		cell.setCellStyle(tablestyle);
		// 3-2
		cell = row3.createCell(2);
		cell.setCellValue("学号");
		cell.setCellStyle(tablestyle);
		// 3-3
		cell = row3.createCell(3);
		cell.setCellValue("姓名");
		cell.setCellStyle(tablestyle);
		// 3-4
		cell = row3.createCell(4);
		cell.setCellValue("性别");
		cell.setCellStyle(tablestyle);
		// 3-5
		cell = row3.createCell(5);
		cell.setCellValue("考生身份证号");
		cell.setCellStyle(tablestyle);
		// 3-6
		cell = row3.createCell(6);
		cell.setCellValue("留考标志");
		cell.setCellStyle(tablestyle);
		// 3-7
		cell = row3.createCell(7);
		cell.setCellValue("成绩");
		cell.setCellStyle(tablestyle);

		return rowIndex;
	}

	public Workbook exportStudentSeat(Workbook workbook, Sheet sheet, List<RoomVO> roomVOs) {
		Integer rowIndex = 0;
		// api 段信息 Set the width (in units of 1/256th of a character width)
		sheet.setColumnWidth(0, 23 * 256);
		sheet.setColumnWidth(1, 8 * 256);
		sheet.setColumnWidth(2, 17 * 256);
		sheet.setColumnWidth(3, 9 * 256);
		sheet.setColumnWidth(4, 9 * 256);
		sheet.setColumnWidth(5, 22 * 256);
		sheet.setColumnWidth(6, 8 * 256);
		sheet.setColumnWidth(7, 13 * 256);

		ExportExamStudentRoomService s = new ExportExamStudentRoomService();
		// 排序
		Collections.sort(roomVOs, new Comparator<RoomVO>() {
			public int compare(RoomVO o1, RoomVO o2) {
				return o1.roomName.compareTo(o2.roomName);
			}
		});
		Collections.sort(roomVOs);

		for (RoomVO roomVo : roomVOs) {
			Collections.sort(roomVo.students);
			// 获得9位的学号学员信息
			List<StudentVO> equal9Students = roomVo.getEqual9Students();
			if (equal9Students.size() > 0) {
				for (int i = 0; i < equal9Students.size() / size + 1; i++) {
					int startIndex = i * size;
					int endIndex = equal9Students.size() > startIndex + size ? startIndex + size
							: equal9Students.size();
					rowIndex = s.createTitle(workbook, sheet, roomVo, rowIndex++);
					rowIndex = s.createList(workbook, sheet, equal9Students, rowIndex++, startIndex, endIndex);
					rowIndex++;

				}
			}

			// 不是9位学号学员信息
			List<StudentVO> than9Students = roomVo.getThan9Students();
			if (than9Students.size() > 0) {
				for (int i = 0; i < than9Students.size() / size + 1; i++) {
					int startIndex = i * size;
					int endIndex = than9Students.size() > startIndex + size ? startIndex + size : than9Students.size();
					rowIndex = s.createTitle(workbook, sheet, roomVo, rowIndex++);
					rowIndex = s.createList(workbook, sheet, than9Students, rowIndex++, startIndex, endIndex);
					rowIndex++;

				}
			}
		}

		return workbook;
	}

	boolean isNotNull(List<StudentVO> list, int startIndex, int endIndex) {
		for (int j = startIndex; j < endIndex; j++) {
			for (StudentVO student : list) {
				int seatNo = student.seatNo;
				if (seatNo == j) {
					return true;
				}
			}
		}
		return false;
	}

	public int createList(Workbook workbook, Sheet sheet, List<StudentVO> students, int rowIndex, int startIndex,
			int endIndex) {
		Font talbeCellFont = workbook.createFont();
		talbeCellFont.setFontName("微软雅黑");
		talbeCellFont.setFontHeightInPoints((short) 10);// 字体大小

		CellStyle talbeCellStyle = workbook.createCellStyle();
		talbeCellStyle.setFont(talbeCellFont);
		talbeCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		talbeCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		talbeCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
		talbeCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
		talbeCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
		talbeCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体

		Row row;
		Cell cell;

		int spaceCount = size;// 需要填充的行数
		for (int i = startIndex; i < endIndex; i++) {

			StudentVO student = students.get(i);

			// int seatNo = student.seatNo;
			spaceCount--;
			row = sheet.createRow(rowIndex++);
			row.setHeight((short) 550);
			cell = row.createCell(0);
			cell.setCellStyle(talbeCellStyle);
			cell.setCellValue("");
			cell = row.createCell(1);
			cell.setCellStyle(talbeCellStyle);
			cell.setCellValue(student.seatNo);
			cell = row.createCell(2);
			cell.setCellStyle(talbeCellStyle);
			cell.setCellValue(student.xh);
			cell = row.createCell(3);
			cell.setCellStyle(talbeCellStyle);
			cell.setCellValue(student.name);
			cell = row.createCell(4);
			cell.setCellStyle(talbeCellStyle);
			cell.setCellValue(student.sex);
			cell = row.createCell(5);
			cell.setCellStyle(talbeCellStyle);
			cell.setCellValue(student.cardNo);
			cell = row.createCell(6);
			cell.setCellStyle(talbeCellStyle);
			cell.setCellValue(student.isRepeat);
			cell = row.createCell(7);
			cell.setCellStyle(talbeCellStyle);
			cell.setCellValue("");
		}

		for (int i = 0; i < spaceCount; i++) {
			row = sheet.createRow(rowIndex++);
			row.setHeight((short) 550);
			cell = row.createCell(0);
			cell.setCellStyle(talbeCellStyle);
			cell.setCellValue("");
			cell = row.createCell(1);
			cell.setCellStyle(talbeCellStyle);
			cell.setCellValue("");
			cell = row.createCell(2);
			cell.setCellStyle(talbeCellStyle);
			cell.setCellValue("");
			cell = row.createCell(3);
			cell.setCellStyle(talbeCellStyle);
			cell.setCellValue("");
			cell = row.createCell(4);
			cell.setCellStyle(talbeCellStyle);
			cell.setCellValue("");
			cell = row.createCell(5);
			cell.setCellStyle(talbeCellStyle);
			cell.setCellValue("");
			cell = row.createCell(6);
			cell.setCellStyle(talbeCellStyle);
			cell.setCellValue("");
			cell = row.createCell(7);
			cell.setCellStyle(talbeCellStyle);
			cell.setCellValue("");
		}

		sheet.createRow(rowIndex);
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 7));
		rowIndex++;

		CellStyle bottomstyle = workbook.createCellStyle();
		bottomstyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 靠左
		bottomstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		bottomstyle.setWrapText(true);// 自动换行

		Row bottomRow = sheet.createRow(rowIndex);
		bottomRow.setHeight((short) 600);
		Cell bottomCell = bottomRow.createCell(0);
		bottomCell.setCellValue("注意：请监考人员将缺考考生的试卷订入相应位置，并在签到栏上填写“缺考”；请严格按照表中座位号的顺序装订试卷！");
		bottomCell.setCellStyle(bottomstyle);
		sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + 1, 0, 7));
		rowIndex++;
		rowIndex++;

		Font fontSimSun11 = workbook.createFont();
		fontSimSun11.setFontName("SimSun");
		fontSimSun11.setFontHeightInPoints((short) 11);// 字体大小
		CellStyle styleSimSun11 = workbook.createCellStyle();
		styleSimSun11.setFont(fontSimSun11);

		Row bottomRow1 = sheet.createRow(rowIndex++);
		bottomRow1.setHeight((short) 600);
		Cell bottomCell1 = bottomRow1.createCell(1);
		bottomCell1.setCellValue("监考人员:");
		bottomCell1.setCellStyle(styleSimSun11);
		Cell bottomCell12 = bottomRow1.createCell(5);
		bottomCell12.setCellValue("登分人:");
		bottomCell12.setCellStyle(styleSimSun11);

		Row bottomRow2 = sheet.createRow(rowIndex++);
		bottomRow2.setHeight((short) 600);
		Cell bottomCell21 = bottomRow2.createCell(1);
		bottomCell21.setCellValue("（签名）:");
		bottomCell21.setCellStyle(styleSimSun11);
		Cell bottomCell22 = bottomRow2.createCell(5);
		bottomCell22.setCellValue("复核人:");
		bottomCell22.setCellStyle(styleSimSun11);

		return rowIndex;

	}

	RoomVO getTestData() {
		RoomVO r = new RoomVO();
		r.title = " 2016年7月 期末考试考生签到表（成绩表）";
		r.pointName = "佛山科院";
//		r.subjectName = "成本会计";
		r.courseName = "成本会计";
		r.examType = "闭卷";
		r.examNo = "2134";
		r.roomName = "301室";
		r.examDay = "2016年7月9日";
		r.examTime = "08:30-10:00";
		r.seatTotal = 80;
		r.startDate = new Date(2016, 1, 1);
		r.endDate = new Date(2016, 1, 2);

		StudentVO student = new StudentVO();
		student.seatNo = 99;
		student.xh = "123456789";
		student.name = "测试" + 99;
		student.sex = "男";
		student.cardNo = "430426198911565656";
		student.isRepeat = "是";
		r.students.add(student);

		for (int i = 20; i < 51; i++) {
			student = new StudentVO();
			student.seatNo = i;
			if (i % 2 == 0) {
				student.xh = "123456789";
			} else {
				student.xh = "123456789111111";
			}
			student.name = "测试" + i;
			student.sex = "男";
			student.cardNo = "430426198911565656";
			student.isRepeat = "是";
			r.students.add(student);
		}

		return r;
	}

	RoomVO getTestData2() {
		RoomVO r = new RoomVO();
		r.title = " 2016年7月 期末考试考生签到表（成绩表）2";
		r.pointName = "佛山科院2";
//		r.subjectName = "成本会计2";
		r.courseName = "成本会计2";
		r.examType = "闭卷2";
		r.examNo = "21342";
		r.roomName = "302室";
		r.examDay = "2016年7月9日2";
		r.examTime = "08:30-10:002";
		r.seatTotal = 80;
		r.startDate = new Date(2016, 1, 2);
		r.endDate = new Date(2016, 1, 2);

		for (int i = 1; i < 51; i++) {
			StudentVO student = new StudentVO();
			student.seatNo = i;
			if (i % 2 == 0) {
				student.xh = "123456789";
			} else {
				student.xh = "123456789111111";
			}
			student.name = "测试" + i;
			student.sex = "男";
			student.cardNo = "430426198911565656";
			student.isRepeat = "是";
			r.students.add(student);
		}

		return r;
	}

	RoomVO getTestData3() {
		RoomVO r = new RoomVO();
		r.title = " 2016年7月 期末考试考生签到表（成绩表）2";
		r.pointName = "佛山科院2";
//		r.subjectName = "成本会计2";
		r.courseName = "成本会计2";
		r.examType = "闭卷2";
		r.examNo = "21342";
		r.roomName = "303室";
		r.examDay = "2016年7月9日";
		r.examTime = "08:30-10:002";
		r.seatTotal = 80;
		r.startDate = new Date(2016, 1, 2);
		r.endDate = new Date(2016, 1, 2);

		for (int i = 33; i < 51; i++) {
			StudentVO student = new StudentVO();
			student.seatNo = i;
			if (i % 2 == 0) {
				student.xh = "123456789";
			} else {
				student.xh = "123456789111111";
			}
			student.name = "测试" + i;
			student.sex = "男";
			student.cardNo = "430426198911565656";
			student.isRepeat = "是";
			r.students.add(student);
		}

		return r;
	}

	RoomVO getTestData4() {
		RoomVO r = new RoomVO();
		r.title = " 2016年7月 期末考试考生签到表（成绩表）2";
		r.pointName = "佛山科院2";
//		r.subjectName = "成本会计2";
		r.courseName = "成本会计2";
		r.examType = "闭卷2";
		r.examNo = "21342";
		r.roomName = "303室";
		r.examDay = "2016年7月9日";
		r.examTime = "08:30-10:002";
		r.seatTotal = 80;
		r.startDate = new Date(2016, 1, 2);
		r.endDate = new Date(2016, 1, 2);

		for (int i = 33; i < 51; i++) {
			StudentVO student = new StudentVO();
			student.seatNo = i;
			if (i % 2 == 0) {
				student.xh = "123456789";
			} else {
				student.xh = "123456789111111";
			}
			student.name = "测试" + i;
			student.sex = "男";
			student.cardNo = "430426198911565656";
			student.isRepeat = "是";
			r.students.add(student);
		}

		return r;
	}

	RoomVO getTestData5() {
		RoomVO r = new RoomVO();
		r.title = " 2016年7月 期末考试考生签到表（成绩表）2";
		r.pointName = "佛山科院2";
//		r.subjectName = "成本会计2";
		r.courseName = "成本会计2";
		r.examType = "闭卷2";
		r.examNo = "21342";
		r.roomName = "301室";
		r.examDay = "2016年7月9日";
		r.examTime = "08:30-10:002";
		r.seatTotal = 80;
		r.startDate = new Date(2016, 1, 2);
		r.endDate = new Date(2016, 1, 3);

		for (int i = 33; i < 51; i++) {
			StudentVO student = new StudentVO();
			student.seatNo = i;
			if (i % 2 == 0) {
				student.xh = "123456789";
			} else {
				student.xh = "123456789111111";
			}
			student.name = "测试" + i;
			student.sex = "男";
			student.cardNo = "430426198911565656";
			student.isRepeat = "是";
			r.students.add(student);
		}

		return r;
	}

}
