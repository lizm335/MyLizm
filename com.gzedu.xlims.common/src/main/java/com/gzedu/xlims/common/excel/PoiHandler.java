package com.gzedu.xlims.common.excel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class PoiHandler {


	private static Log logger = LogFactory.getLog(PoiHandler.class);

	/**
	 * 读取excel的方法，支持xls，xlsx的读取，对规则的数据封装成List<List<Object>></p>
	 * 注意:判断每行的最大长度，防止出现空指针问题</p>
	 * 注意:读取格式根据后缀匹配。
	 * @param is
	 * @param fileName
	 */
	public static List<List<Object>> readExcel(InputStream is,String fileName) throws IOException {
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equals(extension)) {
			return read2003Excel(is);
		} else if ("xlsx".equals(extension)) {
			return read2007Excel(is);
		} else {
			throw new IOException("不支持的文件类型");
		}
	}

	/**
	 * 读取 office 2003 excel
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static List<List<Object>> read2003Excel(InputStream is) throws IOException {
		
		DecimalFormat df = new DecimalFormat("0");// 格式化 number String
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
		DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
		
		List<List<Object>> list = new LinkedList<List<Object>>();
		HSSFWorkbook hwb = new HSSFWorkbook(is);
		HSSFSheet sheet = hwb.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		int counter = 0;
		for (int i = sheet.getFirstRowNum(); counter < sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			} else {
				counter++;
			}
			List<Object> linked = new LinkedList<Object>();
			for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				Object value = null;
				if (cell!= null) {
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING://String类型
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC://number类型
						if ("@".equals(cell.getCellStyle().getDataFormatString())) {
							value = df.format(cell.getNumericCellValue());
						} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
							value = nf.format(cell.getNumericCellValue());
						} else {
							value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
						}
						break;
					case XSSFCell.CELL_TYPE_BOOLEAN://boolean类型
						value = cell.getBooleanCellValue();
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						value = "";
						break;
					default:
						value = cell.toString();
					}		
					linked.add(value);	
				}else{
					linked.add("");
				}
				
			}
			list.add(linked);
		}
		return list;
	}

	/**
	 * 读取Office 2007 excel
	 */
	private static List<List<Object>> read2007Excel(InputStream is) throws IOException {
		
		DecimalFormat df = new DecimalFormat("0");// 格式化 number String
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
		DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
		
		List<List<Object>> list = new LinkedList<List<Object>>();
		// 构造 XSSFWorkbook 对象，strPath 传入文件路径
		XSSFWorkbook xwb = new XSSFWorkbook(is);
		// 读取第一章表格内容
		XSSFSheet sheet = xwb.getSheetAt(0);
		XSSFRow row = null;
		XSSFCell cell = null;
		int counter = 0;
		for (int i = sheet.getFirstRowNum(); counter < sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			} else {
				counter++;
			}
			List<Object> linked = new LinkedList<Object>();
			for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				Object value = null;
				if (cell != null) {
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						if ("@".equals(cell.getCellStyle().getDataFormatString())) {
							value = df.format(cell.getNumericCellValue());
						} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
							value = nf.format(cell.getNumericCellValue());
						} else {
							value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
						}
						break;
					case XSSFCell.CELL_TYPE_BOOLEAN:
						value = cell.getBooleanCellValue();
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						value = "";
						break;
					default:
						value = cell.toString();
					}
					linked.add(value);
				}else{
					linked.add("");
				}

			}
			list.add(linked);
		}
		return list;
	}



	/**
	 * 把WorkBook对象,文件名字(带扩展名)传入，返回ResponseEntity<byte[]>，前端controller返回此对象即可实现下载（Spring处理下载文档）
	 * @param  wb
	 * @param fileName
	 * @return {@link ResponseEntity}
	 * @author llx
	 *
	 */
	public static ResponseEntity<byte[]> exportExcel(Workbook wb, String fileName){
		ByteArrayOutputStream bos = null;
		ResponseEntity<byte[]> entity = null;
		try {
			bos = new ByteArrayOutputStream();
			wb.write(bos);
			bos.flush();
			HttpHeaders headers = new HttpHeaders();
			fileName=new String(fileName.getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
			headers.setContentDispositionFormData("attachment", fileName);
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			entity = new ResponseEntity<byte[]>(bos.toByteArray(),headers, HttpStatus.CREATED);

			bos.close();
		} catch (Exception e) {
			logger.info(e);
		}
		return entity;
	}

	public static void main(String[] args) {
		try {
			List<List<Object>> list = readExcel(new FileInputStream(new File("D:\\教学教务平台工作计划(改).xlsx")),"教学教务平台工作计划(改).xlsx");
			System.out.println(list);
			// readExcel(new File("D:\\test.xls"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
