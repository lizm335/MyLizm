package com.gzedu.xlims.common.gzdec.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.gzedu.xlims.common.gzedu.EmptyUtils;

/**
 * 2017-1-12
 * 
 * @author eenet09
 *
 */
public class ExcelUtil {

	private static final Log log = LogFactory.getLog(ExcelUtil.class);

	public static List<String[]> readAsStringList(File file, int startRowNum, int colNum)
			throws InvalidFormatException, IOException {
		InputStream in = new FileInputStream(file);
		return readAsStringList(in, startRowNum, colNum);
	}

	public static List<String[]> readAsStringList(InputStream in, int startRowNum, int colNum)
			throws InvalidFormatException, IOException {
		Workbook workbook = WorkbookFactory.create(in);
		return readAsStringList(workbook, startRowNum, colNum);
	}

	public static List<String[]> readAsStringList(Workbook workbook, int startRowNum, int colNum) {
		List<String[]> list = new ArrayList<String[]>();
		Sheet sheet = workbook.getSheetAt(0);
		Row row = null;
		int rowNum = sheet.getLastRowNum();

		// 读取excel数据到List
		for (int i = startRowNum; i <= rowNum; i++) {
			String[] s = new String[colNum];
			row = sheet.getRow(i);
			for (int colIndex = 0; colIndex < colNum; colIndex++) {
				s[colIndex] = getCellValue(row.getCell(colIndex));
			}
			if (EmptyUtils.isNotEmpty(s))
				list.add(s);
		}

		return list;
	}

	public static Workbook getWorkbook(String[] titles, List<? extends Object[]> datas, String sheetName) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);

		if (titles != null && titles.length > 0) {
			HSSFRow row = sheet.createRow(0);
			for (int i = 0; i < titles.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(titles[i]);
			}
		}

		if (datas != null && datas.size() > 0) {
			for (int i = 0; i < datas.size(); i++) {
				HSSFRow row = sheet.createRow(i + 1);
				Object[] data = datas.get(i);
				for (int j = 0; j < data.length; j++) {
					HSSFCell cell = row.createCell(j);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					if (data[j] == null) {
						continue;
					} else {
						cell.setCellValue(data[j].toString());
					}
				}
			}
		}

		return wb;
	}

	/**
	 * 获取单元格的值
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell) {
		String value = "";
		if (cell != null) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
			value = cell.getStringCellValue().trim();
		}

		return value;
	}

	public static void writeWorkbook(Workbook workbook, File file) {
		OutputStream output = null;
		try {
			output = new FileOutputStream(file);
			workbook.write(output);
		} catch (Exception e) {
			log.error(e);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	}

	/**
	 * 设置让浏览器弹出下载[Excel文件]对话框的Header.
	 * 
	 * @param response
	 * @param workbook
	 * @param outputFileName
	 * @throws IOException
	 */
	public static void downloadExcelFile(HttpServletResponse response, HSSFWorkbook workbook, String outputFileName)
			throws IOException {
		if (workbook != null) {
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(outputFileName.getBytes("UTF-8"), "ISO8859-1"));
			workbook.write(response.getOutputStream());
		} else {
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write("<script type='application/javascript'>/*自动关闭当前窗口*/window.close();</script>");
			response.getWriter().flush();
		}
	}

}
