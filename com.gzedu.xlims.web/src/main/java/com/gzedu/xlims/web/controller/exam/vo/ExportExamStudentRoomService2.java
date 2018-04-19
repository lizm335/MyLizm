/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.exam.vo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年11月30日
 * @version 2.5
 *
 */
public class ExportExamStudentRoomService2 {

	public static void main(String[] args) throws Exception {
		InputStream in = ExportExamStudentRoomService2.class.getResourceAsStream("/excel/exam/考点签到表.xlsx");
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ExportExamStudentRoomService2 s = new ExportExamStudentRoomService2();
		List<RoomVO> roomVOs = new ArrayList<ExportExamStudentRoomService2.RoomVO>();

		// 查询院校
		// 查询学员排考位置记录
		roomVOs.add(s.getTestData());
		s.export(workbook, roomVOs);

		FileOutputStream outputStream = new FileOutputStream("E://abc.xlsx");

		workbook.write(outputStream);
		outputStream.flush();
		outputStream.close();
	}

	public XSSFWorkbook export(XSSFWorkbook workbook, List<RoomVO> roomVOs) {

		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow row;
		XSSFCell cell;
		int rowIndex = 0;

		XSSFRow row1 = sheet.getRow(0); // 2016年7月 期末考试考生签到表（成绩表）
		XSSFRow row2 = sheet.getRow(1);// 考点：佛山科院 科目：管理案例分析 考试方式：开卷
		// 试卷号：1304 考场号：201 考试时间：2016年7月10日08:30-10:00
		XSSFRow row3 = sheet.getRow(2);
		XSSFRow row4 = sheet.getRow(3);// 考生签名 座位号 学号 姓名 性别 考生身份证号 留考标志 成绩
		XSSFRow row5 = sheet.getRow(4);// 1444101259667 劳嘉茵 女 440681198905153183

		XSSFCellStyle row1Style = sheet.getRow(0).getRowStyle();
		XSSFCellStyle row2Style = sheet.getRow(1).getRowStyle();
		XSSFCellStyle row3Style = sheet.getRow(2).getRowStyle();
		XSSFCellStyle row4Style = sheet.getRow(3).getRowStyle();
		XSSFCellStyle row5Style = sheet.getRow(4).getRowStyle();

		XSSFCell cell1 = sheet.getRow(0).getCell(0);
		XSSFCell cell2 = sheet.getRow(1).getCell(0);
		XSSFCell cell3 = sheet.getRow(2).getCell(0);
		XSSFCell cell4 = sheet.getRow(3).getCell(0);
		XSSFCell cell5 = sheet.getRow(4).getCell(0);

		XSSFFont talbeCellFont = workbook.createFont();
		talbeCellFont.setFontName("宋体");
		talbeCellFont.setFontHeightInPoints((short) 10);// 字体大小
		XSSFCellStyle talbeCellStyle = workbook.createCellStyle();
		talbeCellStyle.setFont(talbeCellFont);
		talbeCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		talbeCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		talbeCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
		talbeCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
		talbeCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
		talbeCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体

		for (RoomVO roomVO : roomVOs) {
			// 1
			row1 = sheet.getRow(rowIndex++);
			cell = row1.getCell(0);
			cell.setCellValue(roomVO.title);
			rowIndex++;

			// 2
			row2 = sheet.getRow(rowIndex++);
			row2.getCell(0).setCellValue("考点：" + roomVO.pointName);
			row2.getCell(3).setCellValue("科目：" + roomVO.subjectName);
			row2.getCell(6).setCellValue("考试方式：" + roomVO.examType);

			// 3
			row3 = sheet.getRow(rowIndex++);
			row3.getCell(0).setCellValue("试卷号：" + roomVO.examNo);
			row3.getCell(3).setCellValue("考场号：" + roomVO.roomName);
			row3.getCell(5).setCellValue("考试时间：" + roomVO.examDay);
			row3.getCell(6).setCellValue(roomVO.examTime);

			// 4
			row4 = sheet.getRow(rowIndex++);
			row4.getCell(0).setCellValue("考生签名");
			row4.getCell(1).setCellValue("座位号");
			row4.getCell(2).setCellValue("学号");
			row4.getCell(3).setCellValue("姓名");
			row4.getCell(4).setCellValue("性别");
			row4.getCell(5).setCellValue("考生身份证号");
			row4.getCell(6).setCellValue("留考标志");
			row4.getCell(7).setCellValue("成绩");

			for (StudentVO student : roomVO.students) {
				row = sheet.getRow(rowIndex++);
				row.setRowStyle(row5.getRowStyle());

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

			sheet.createRow(rowIndex);
			sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 7));
			rowIndex++;

			XSSFCellStyle bottomstyle = workbook.createCellStyle();
			bottomstyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 靠左
			bottomstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
			bottomstyle.setWrapText(true);// 自动换行

			XSSFRow bottomRow = sheet.createRow(rowIndex);
			bottomRow.setHeight((short) 600);
			XSSFCell bottomCell = bottomRow.createCell(0);
			bottomCell.setCellValue("注意：请监考人员将缺考考生的试卷订入相应位置，并在签到栏上填写“缺考”；请严格按照表中座位号的顺序装订试卷！");
			bottomCell.setCellStyle(bottomstyle);
			sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + 1, 0, 7));
			rowIndex++;
			rowIndex++;

			XSSFFont fontSimSun11 = workbook.createFont();
			fontSimSun11.setFontName("SimSun");
			fontSimSun11.setFontHeightInPoints((short) 11);// 字体大小
			XSSFCellStyle styleSimSun11 = workbook.createCellStyle();
			styleSimSun11.setFont(fontSimSun11);

			XSSFRow bottomRow1 = sheet.createRow(rowIndex++);
			bottomRow1.setHeight((short) 300);
			XSSFCell bottomCell1 = bottomRow1.createCell(1);
			bottomCell1.setCellValue("监考人员:");
			bottomCell1.setCellStyle(styleSimSun11);
			XSSFCell bottomCell12 = bottomRow1.createCell(5);
			bottomCell12.setCellValue("登分人:");
			bottomCell12.setCellStyle(styleSimSun11);

			XSSFRow bottomRow2 = sheet.createRow(rowIndex++);
			bottomRow2.setHeight((short) 300);
			XSSFCell bottomCell21 = bottomRow2.createCell(1);
			bottomCell21.setCellValue("（签名）:");
			bottomCell21.setCellStyle(styleSimSun11);
			XSSFCell bottomCell22 = bottomRow2.createCell(5);
			bottomCell22.setCellValue("复核人:");
			bottomCell22.setCellStyle(styleSimSun11);

			sheet.createRow(rowIndex++);
			sheet.createRow(rowIndex++);
		}

		return workbook;
	}

	RoomVO getTestData() {
		RoomVO r = new RoomVO();
		r.title = " 2016年7月 期末考试考生签到表（成绩表）";
		r.pointName = "佛山科院";
		r.subjectName = "成本会计";
		r.examType = "闭卷";
		r.examNo = "2134";
		r.roomName = "301室";
		r.examDay = "2016年7月9日";
		r.examTime = "08:30-10:00";

		for (int i = 0; i < 30; i++) {
			StudentVO student = new StudentVO();
			student.seatNo = "" + i;
			student.xh = "1544101411131";
			student.name = "洗洗";
			student.sex = "男";
			student.cardNo = "430426198911565656";
			student.isRepeat = "是";
			r.students.add(student);
		}

		return r;
	}

	class RoomVO {
		String title;// 标题
		String pointName;// 考点名称
		String subjectName;// 科目名称
		String examType;// 考试方式
		String examNo;// 试卷号
		String roomName;// 考场名称
		String examDay;// 考试日期
		String examTime;// 考试范围

		List<StudentVO> students = new ArrayList<ExportExamStudentRoomService2.StudentVO>();
	}

	class StudentVO {
		String seatNo;// 座位号
		String xh;// 学号
		String name;// 姓名
		String sex;// 性别
		String cardNo;// 身份证号码
		String isRepeat;// 是否留级
	}

}
