package com.gzedu.xlims.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.gzedu.xlims.common.gzedu.EmptyUtils;

public class ExcelReader {

	private File file;
	private String[] titles;
	private int startRowNum = 2;

	private InputStream in;
	private Workbook workbook;
	private List<Object> excelData = new ArrayList<Object>();

	public ExcelReader(File file, String[] titles, int startRowNum) {
		super();
		this.file = file;
		this.titles = titles;
		this.startRowNum = startRowNum;

		this.init();
	}

	private void init() {
		try {
			this.in = new FileInputStream(file);
			this.workbook = WorkbookFactory.create(this.in);
//			this.workbook = new HSSFWorkbook(this.in);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		} catch (InvalidFormatException ife) {
			ife.printStackTrace();
		}
	}

	private void release() {
		try {
			if (in != null) {
				in.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Object> readAsList() {
		Sheet sheet = workbook.getSheetAt(0);
		Row row = null;
		int rowNum = sheet.getLastRowNum();
		int colNum = titles.length;

		String key = null;
		Object value = null;

		// 读取excel数据到List
		for (int i = startRowNum; i < rowNum + 1; i++) {
			row = sheet.getRow(i);
			if (EmptyUtils.isNotEmpty(row)) {
				Map<String, Object> e = new HashMap<String, Object>();
				e.put("rownum", i);
				for (int colIndex = 0; colIndex < colNum; colIndex++) {
					key = titles[colIndex];
					value = CellUtils.getCellValue(row.getCell(colIndex));
					e.put(key, value);
				}
				excelData.add(e);
			}
		}

		this.release();

		return this.excelData;
	}

	public int getStartRowNum() {
		return startRowNum;
	}

	public void setStartRowNum(int startRowNum) {
		this.startRowNum = startRowNum;
	}

	public static void main(String[] args) throws Exception{
		File file = new File("/Users/micarol/Downloads/testplan.xlsx");
		String[] titles = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
		ExcelReader reader = new ExcelReader(file, titles, 1);
		List<Object> list = reader.readAsList();
		for (Object object : list) {
			System.out.println(object);
		}
	}

}
