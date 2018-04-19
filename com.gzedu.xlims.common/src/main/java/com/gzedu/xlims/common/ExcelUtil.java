/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import net.sf.jxls.transformer.XLSTransformer;

/**
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月12日
 * @version 2.5
 * @since JDK 1.7
 */
public class ExcelUtil {
	// excel文件名
	private String fileName = "";

	// 服务器端excel文件完整文件名
	private String excelFileName = "";

	// 浏览器访问excel文件路径
	private String fileUrl = "";

	// 记录集合
	private ResultSet dateRs = null;

	// 记录集合
	private int rsSize = 0;

	// 表标题
	private String fileTitle = null;
	
	private static BufferedInputStream bis = null;
	private static BufferedOutputStream bos = null;

	/**
	 * 初始化参数 所有参数必须有值。
	 * 
	 * @param request
	 * @param response
	 * @param map
	 * @throws Exception
	 */
	public ExcelUtil(HttpServletRequest request, HttpServletResponse response, Map map) throws Exception {
		this.fileName = BaseUtil.toString(map.get("ACTION")).trim() + ".xls";
		if (!BaseUtil.toString(request.getServerPort()).equals("80")) {
			this.fileUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + "/excel/" + fileName;
		} else {
			this.fileUrl = "http://" + request.getServerName() + "/excel/" + fileName;
		}

		this.excelFileName = BaseUtil.toString(map.get("REALPATH")).trim() + "\\excel\\" + fileName;
		/*
		 * this.excelFileName =
		 * BaseUtil.toString(AppConfig.getProperty("excel_path")).trim()+
		 * fileName;
		 */
		this.fileTitle = BaseUtil.toString(map.get("FILETITLE")).trim();
		if (map.get("dateRs") != null) {
			this.dateRs = (ResultSet) map.get("dateRs");
			if (BaseUtil.toInt(map.get("DBCOUNT")) > 0) {
				this.rsSize = BaseUtil.toInt(map.get("DBCOUNT"));
			} else if (BaseUtil.toInt(map.get("dbcount")) > 0) {
				this.rsSize = BaseUtil.toInt(map.get("dbcount"));
			}

		}
	}

	/**
	 * 创建Excel
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	/*
	 * 使用Jxl导出Excel.在linux下无法产生jxl.write.biff.WritableWorkbookImpl，无法生成excel.
	 * public void createExcel(HttpServletRequest request, HttpServletResponse
	 * response) throws Exception { //当记录大于10000时禁止导出
	 * if(dateRs!=null&&rsSize>10000){ dateRs.close();
	 * response.sendRedirect(request.getContextPath()+"/info/excelinfopage.jsp")
	 * ; return ; } // 构建Workbook对象, 只读Workbook对象 // Method 1：创建可写入的Excel工作薄
	 * WritableWorkbook wwb; try { wwb = Workbook.createWorkbook(new
	 * File(excelFileName)); // 创建Excel工作表 WritableSheet ws =
	 * wwb.createSheet(fileTitle, 0);
	 * 
	 * //标题样式 WritableFont titleFont= new
	 * WritableFont(WritableFont.createFont("宋体"),16,WritableFont.BOLD);
	 * WritableCellFormat titleFormat=new WritableCellFormat(titleFont);
	 * titleFormat.setAlignment(Alignment.CENTRE); if(dateRs!=null){ int i = 0;
	 * int columnCount = dateRs.getMetaData().getColumnCount();
	 * 
	 * //标题 ws.addCell(new Label(0, 0, fileTitle,titleFormat));
	 * ws.mergeCells(0,0,columnCount-1,0); //字段 for (int k = 0; k < columnCount;
	 * k++) { ws.addCell(new Label(k, 1, dateRs.getMetaData().getColumnName( k +
	 * 1))); } //内容 while (dateRs.next()) { for (int k = 0; k < columnCount;
	 * k++) { if(k==0){} ws.addCell(new Label(k,i + 2, dateRs .getString(k +
	 * 1))); } i++; } } // 写入Exel工作表 wwb.write(); // 关闭Excel工作薄对象 wwb.close();
	 * exportExcel(request, response); }catch(Exception e){ e.printStackTrace();
	 * }finally{ if(dateRs!=null){ dateRs.close(); } } }
	 */

	/**
	 * POI创建Excel modify by lianghuahuang 2008-9-16
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 当记录大于10000时禁止导出
		// if (dateRs != null && rsSize > 10000) {
		// dateRs.close();
		// response.sendRedirect(request.getContextPath()
		// + "/info/excelinfopage.jsp");
		// return;
		// }

		try {
			response.setContentType("application/doc"); // MIME type for pdf doc
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			// 创建workbook
			HSSFWorkbook wb = new HSSFWorkbook();

			// 使用默认的构造方法创建workbook
			FileOutputStream fileOut;

			// fileOut = new FileOutputStream(file);

			HSSFSheet sheet = wb.createSheet();

			wb.setSheetName(0, "sheet0");

			// 生成一个样式
			HSSFCellStyle style = wb.createCellStyle();

			// 生成一个字体
			HSSFFont font = wb.createFont();

			font.setFontName("宋体");
			font.setFontHeightInPoints((short) 16);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			// 把字体应用到当前的样式
			style.setFont(font);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			if (dateRs != null) {
				int i = 2;
				int columnCount = dateRs.getMetaData().getColumnCount();

				// 合並單元格,下標從0開始
				sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (columnCount - 1)));

				// 标题
				HSSFRow rowT = sheet.createRow(0);
				HSSFCell cellT = rowT.createCell((short) 0);

				// cellT.setEncoding(HSSFCell.ENCODING_UTF_16);
				cellT.setCellValue(fileTitle);
				cellT.setCellStyle(style);

				// 字段
				HSSFRow colrow = sheet.createRow((short) 1);
				for (int k = 0; k < columnCount; k++) {
					HSSFCell cell = colrow.createCell((short) k);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dateRs.getMetaData().getColumnName(k + 1));
				}
				// 内容
				while (dateRs.next()) {
					HSSFRow datarow = sheet.createRow((short) i);
					for (int k = 0; k < columnCount; k++) {
						HSSFCell cell = datarow.createCell((short) k);
						// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(BaseUtil.toString(dateRs.getObject(k + 1)));
					}
					i++;
				}
			}

			// 指定文件名
			wb.write(response.getOutputStream());

			// 输出到文件
			response.getOutputStream().close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dateRs != null) {
				dateRs.close();
			}
		}

		// 此处为原来导出文件的方式，在导出时会在服务器上同时生成一个临时文件

		/*
		 * // 当记录大于10000时禁止导出 if (dateRs != null && rsSize > 10000) {
		 * dateRs.close(); response.sendRedirect(request.getContextPath() +
		 * "/info/excelinfopage.jsp"); return; }
		 * 
		 * try { // 创建文件 File file = new File(excelFileName); // 创建workbook
		 * HSSFWorkbook wb = new HSSFWorkbook();
		 * 
		 * // 使用默认的构造方法创建workbook FileOutputStream fileOut;
		 * 
		 * fileOut = new FileOutputStream(file);
		 * 
		 * HSSFSheet sheet = wb.createSheet();
		 * 
		 * wb.setSheetName(0, "sheet0");
		 * 
		 * // 生成一个样式 HSSFCellStyle style = wb.createCellStyle();
		 * 
		 * // 生成一个字体 HSSFFont font = wb.createFont();
		 * 
		 * font.setFontName("宋体"); font.setFontHeightInPoints((short) 16);
		 * font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		 * 
		 * // 把字体应用到当前的样式 style.setFont(font);
		 * style.setAlignment(HSSFCellStyle.ALIGN_CENTER );
		 * 
		 * if (dateRs != null) { int i = 2; int columnCount =
		 * dateRs.getMetaData().getColumnCount();
		 * 
		 * // 合並單元格,下標從0開始 sheet.addMergedRegion(new Region(0, (short) 0, 0,
		 * (short) (columnCount-1)));
		 * 
		 * // 标题 HSSFRow rowT = sheet.createRow(0); HSSFCell cellT =
		 * rowT.createCell((short) 0);
		 * 
		 * cellT.setEncoding(HSSFCell.ENCODING_UTF_16);
		 * cellT.setCellValue(fileTitle); cellT.setCellStyle(style);
		 * 
		 * // 字段 HSSFRow colrow = sheet.createRow((short) 1); for (int k = 0; k
		 * < columnCount; k++) { HSSFCell cell = colrow.createCell((short) k);
		 * cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		 * cell.setCellValue(dateRs.getMetaData().getColumnName(k + 1)); } // 内容
		 * while (dateRs.next()) { HSSFRow datarow = sheet.createRow((short) i);
		 * for (int k = 0; k < columnCount; k++) { HSSFCell cell =
		 * datarow.createCell((short) k);
		 * cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		 * cell.setCellValue(BaseUtil.toString(dateRs.getObject(k + 1))); } i++;
		 * } }
		 * 
		 * // 指定文件名
		 * 
		 * wb.write(fileOut);
		 * 
		 * // 输出到文件 fileOut.close(); exportExcel(request, response); } catch
		 * (Exception e) { e.printStackTrace(); } finally { if (dateRs != null)
		 * { dateRs.close(); } }
		 */
	}

	public void createExcel(HttpServletRequest request, HttpServletResponse response, Map bill) throws Exception {
		// 当记录大于10000时禁止导出
		if (dateRs != null && rsSize > 10000) {
			dateRs.close();
			response.sendRedirect(request.getContextPath() + "/info/excelinfopage.jsp");
			return;
		}

		try {
			response.setContentType("application/doc"); // MIME type for pdf doc
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);

			// 创建workbook
			HSSFWorkbook wb = new HSSFWorkbook();

			// 使用默认的构造方法创建workbook
			FileOutputStream fileOut;

			HSSFSheet sheet = wb.createSheet();

			wb.setSheetName(0, "sheet0");

			// 生成一个样式
			HSSFCellStyle style = wb.createCellStyle();

			// 生成一个字体
			HSSFFont font = wb.createFont();

			font.setFontName("宋体");
			font.setFontHeightInPoints((short) 16);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			// 把字体应用到当前的样式
			style.setFont(font);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			if (dateRs != null) {
				int i = 4;
				int columnCount = dateRs.getMetaData().getColumnCount();

				// 合並單元格,下標從0開始
				sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (columnCount - 1)));
				sheet.addMergedRegion(new Region(1, (short) 0, 1, (short) (columnCount - 1)));
				sheet.addMergedRegion(new Region(2, (short) 0, 2, (short) (columnCount - 1)));

				// 标题
				HSSFRow rowT = sheet.createRow(0);
				HSSFCell cellT = rowT.createCell((short) 0);

				// cellT.setEncoding(HSSFCell.ENCODING_UTF_16);
				cellT.setCellValue(fileTitle);
				cellT.setCellStyle(style);

				// 帐单
				/*
				 * HSSFRow rowbill_id = sheet.createRow(1); HSSFCell cellbill_id
				 * = rowbill_id.createCell((short) 0);
				 * 
				 * cellbill_id.setEncoding(HSSFCell.ENCODING_UTF_16);
				 * cellbill_id.setCellValue("帐单号：  "
				 * +bill.get("bill_id").toString());
				 * //cellbill_id.setCellStyle(style);
				 * 
				 * HSSFRow rowbill_name = sheet.createRow(2); HSSFCell
				 * cellbill_name = rowbill_name.createCell((short) 0);
				 * 
				 * cellbill_name.setEncoding(HSSFCell.ENCODING_UTF_16);
				 * cellbill_name.setCellValue("帐单名：  "
				 * +bill.get("bill_name").toString());
				 * //cellbill_name.setCellStyle(style);
				 */
				// 字段
				HSSFRow colrow = sheet.createRow((short) 3);
				for (int k = 0; k < columnCount; k++) {
					HSSFCell cell = colrow.createCell((short) k);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dateRs.getMetaData().getColumnName(k + 1));
				}
				// 内容
				while (dateRs.next()) {
					HSSFRow datarow = sheet.createRow((short) i);
					for (int k = 0; k < columnCount; k++) {
						HSSFCell cell = datarow.createCell((short) k);
						// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(BaseUtil.toString(dateRs.getObject(k + 1)));
					}
					i++;
				}

				// 合计
				/*
				 * HSSFRow rowcount = sheet.createRow(i); HSSFCell cellcountfee
				 * = rowcount.createCell((short) 0); HSSFCell cellpxcountfee =
				 * rowcount.createCell((short) 6); HSSFCell cellzlcountfee =
				 * rowcount.createCell((short) 7); HSSFCell cellfee =
				 * rowcount.createCell((short) 8);
				 * 
				 * cellcountfee.setEncoding(HSSFCell.ENCODING_UTF_16);
				 * cellcountfee.setCellValue("合计：");
				 * 
				 * cellpxcountfee.setEncoding(HSSFCell.ENCODING_UTF_16);
				 * cellpxcountfee.setCellValue(bill.get("countpxfee").toString()
				 * );
				 * 
				 * cellzlcountfee.setEncoding(HSSFCell.ENCODING_UTF_16);
				 * cellzlcountfee.setCellValue(bill.get("countzlfee").toString()
				 * );
				 * 
				 * cellfee.setEncoding(HSSFCell.ENCODING_UTF_16);
				 * cellfee.setCellValue(bill.get("countfee").toString());
				 */
			}

			// 指定文件名

			wb.write(response.getOutputStream());

			response.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dateRs != null) {
				dateRs.close();
			}
		}
	}

	/**
	 * type没有用，只是重载这方法，单独给chooseCourseAction 里的downloadbillselectInfo方法调用
	 * 
	 * @param request
	 * @param response
	 * @param bill
	 * @param type
	 * @throws Exception
	 */
	public void createExcel(HttpServletRequest request, HttpServletResponse response, Map bill, String type)
			throws Exception {
		// 当记录大于10000时禁止导出
		if (dateRs != null && rsSize > 10000) {
			dateRs.close();
			response.sendRedirect(request.getContextPath() + "/info/excelinfopage.jsp");
			return;
		}

		try {
			response.setContentType("application/doc"); // MIME type for pdf doc
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);

			// 创建workbook
			HSSFWorkbook wb = new HSSFWorkbook();

			// 使用默认的构造方法创建workbook
			FileOutputStream fileOut;

			HSSFSheet sheet = wb.createSheet();

			wb.setSheetName(0, "sheet0");

			// 生成一个样式
			HSSFCellStyle style = wb.createCellStyle();

			// 生成一个字体
			HSSFFont font = wb.createFont();

			font.setFontName("宋体");
			font.setFontHeightInPoints((short) 16);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			// 把字体应用到当前的样式
			style.setFont(font);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			if (dateRs != null) {
				int i = 4;
				int columnCount = dateRs.getMetaData().getColumnCount();

				// 合並單元格,下標從0開始
				sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (columnCount - 1)));
				sheet.addMergedRegion(new Region(1, (short) 0, 1, (short) (columnCount - 1)));
				sheet.addMergedRegion(new Region(2, (short) 0, 2, (short) (columnCount - 1)));

				// 标题
				HSSFRow rowT = sheet.createRow(0);
				HSSFCell cellT = rowT.createCell((short) 0);

				// cellT.setEncoding(HSSFCell.ENCODING_UTF_16);
				cellT.setCellValue(fileTitle);
				cellT.setCellStyle(style);

				// 帐单
				HSSFRow rowbill_id = sheet.createRow(1);
				HSSFCell cellbill_id = rowbill_id.createCell((short) 0);

				// cellbill_id.setEncoding(HSSFCell.ENCODING_UTF_16);
				cellbill_id.setCellValue("帐单号：  " + bill.get("bill_id").toString());
				cellbill_id.setCellStyle(style);

				HSSFRow rowbill_name = sheet.createRow(2);
				HSSFCell cellbill_name = rowbill_name.createCell((short) 0);

				// cellbill_name.setEncoding(HSSFCell.ENCODING_UTF_16);
				cellbill_name.setCellValue("帐单名：  " + bill.get("bill_name").toString());
				cellbill_name.setCellStyle(style);

				// 字段
				HSSFRow colrow = sheet.createRow((short) 3);
				for (int k = 0; k < columnCount; k++) {
					HSSFCell cell = colrow.createCell((short) k);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dateRs.getMetaData().getColumnName(k + 1));
				}
				// 内容
				while (dateRs.next()) {
					HSSFRow datarow = sheet.createRow((short) i);
					for (int k = 0; k < columnCount; k++) {
						HSSFCell cell = datarow.createCell((short) k);
						// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(BaseUtil.toString(dateRs.getObject(k + 1)));
					}
					i++;
				}

				// 合计
				/*
				 * HSSFRow rowcount = sheet.createRow(i); HSSFCell cellcountfee
				 * = rowcount.createCell((short) 0); HSSFCell cellpxcountfee =
				 * rowcount.createCell((short) 6); HSSFCell cellzlcountfee =
				 * rowcount.createCell((short) 7); HSSFCell cellfee =
				 * rowcount.createCell((short) 8);
				 * 
				 * cellcountfee.setEncoding(HSSFCell.ENCODING_UTF_16);
				 * cellcountfee.setCellValue("合计：");
				 * 
				 * cellpxcountfee.setEncoding(HSSFCell.ENCODING_UTF_16);
				 * cellpxcountfee.setCellValue(bill.get("countpxfee").toString()
				 * );
				 * 
				 * cellzlcountfee.setEncoding(HSSFCell.ENCODING_UTF_16);
				 * cellzlcountfee.setCellValue(bill.get("countzlfee").toString()
				 * );
				 * 
				 * cellfee.setEncoding(HSSFCell.ENCODING_UTF_16);
				 * cellfee.setCellValue(bill.get("countfee").toString());
				 */
			}

			// 指定文件名

			wb.write(response.getOutputStream());

			response.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dateRs != null) {
				dateRs.close();
			}
		}
	}

	/**
	 * 导出Excel
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/doc"); // MIME type for pdf doc
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);

		URL url = new URL(fileUrl);
		BufferedInputStream bis = new BufferedInputStream(url.openStream());
		BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = (bis.read(buff, 0, buff.length)))) {
			bos.write(buff, 0, bytesRead);
		}
		if (bis != null) {
			bis.close();
		}
		if (bos != null) {
			bos.close();
		}
	}

	public void createExcel(HttpServletRequest request, HttpServletResponse response, List hlist, List tlist)
			throws Exception {
		// 当记录大于10000时禁止导出
		// if (dateRs != null && rsSize > 10000) {
		// dateRs.close();
		// response.sendRedirect(request.getContextPath()
		// + "/info/excelinfopage.jsp");
		// return;
		// }

		try {
			response.setContentType("application/doc"); // MIME type for pdf doc
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			// 创建workbook
			HSSFWorkbook wb = new HSSFWorkbook();

			// 使用默认的构造方法创建workbook
			FileOutputStream fileOut;

			// fileOut = new FileOutputStream(file);

			HSSFSheet sheet = wb.createSheet();

			wb.setSheetName(0, "sheet0");

			// 生成一个样式
			HSSFCellStyle style = wb.createCellStyle();

			// 生成一个字体
			HSSFFont font = wb.createFont();

			font.setFontName("宋体");
			font.setFontHeightInPoints((short) 16);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			// 把字体应用到当前的样式
			style.setFont(font);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			if (dateRs != null) {
				int i = 2;
				int columnCount = dateRs.getMetaData().getColumnCount();

				// 合並單元格,下標從0開始
				sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (columnCount - 1)));

				// 标题
				HSSFRow rowT = sheet.createRow(0);
				HSSFCell cellT = rowT.createCell((short) 0);

				// cellT.setEncoding(HSSFCell.ENCODING_UTF_16);
				cellT.setCellValue(fileTitle);
				cellT.setCellStyle(style);

				// 表头
				for (int m = 0; m < hlist.size(); m++) {
					HSSFRow rowHead = sheet.createRow(i);
					DynaBean head = (DynaBean) hlist.get(m);

					HSSFCell cellHead = rowHead.createCell((short) 0);
					// cellHead.setEncoding(HSSFCell.ENCODING_UTF_16);
					cellHead.setCellValue(BaseUtil.toString(head.get("headInfo")));

					i++;
				}

				// 字段
				HSSFRow colrow = sheet.createRow((short) i);
				i++;
				for (int k = 0; k < columnCount; k++) {
					HSSFCell cell = colrow.createCell((short) k);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dateRs.getMetaData().getColumnName(k + 1));
				}
				// 内容
				while (dateRs.next()) {
					HSSFRow datarow = sheet.createRow((short) i);
					for (int k = 0; k < columnCount; k++) {
						HSSFCell cell = datarow.createCell((short) k);
						// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(BaseUtil.toString(dateRs.getObject(k + 1)));
					}
					i++;
				}

				// 表尾
				for (int m = 0; m < tlist.size(); m++) {
					HSSFRow rowHead = sheet.createRow(i);
					DynaBean tail = (DynaBean) tlist.get(m);
					HSSFCell cellHead = rowHead.createCell((short) 0);
					// cellHead.setEncoding(HSSFCell.ENCODING_UTF_16);
					cellHead.setCellValue(BaseUtil.toString(tail.get("tailInfo")));
					i++;
				}

			}

			// 指定文件名
			wb.write(response.getOutputStream());

			// 输出到文件
			response.getOutputStream().close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dateRs != null) {
				dateRs.close();
			}
		}
	}

	// 创建 excel并导出
	public static void createExcel(HttpServletResponse response, String downFilename, String[] titles,
			short[] columnWidths, List valueList, String showMapColumn[]) throws Exception {

		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Content-Disposition", "attachment;filename=" + downFilename);

		HSSFWorkbook reBook = new HSSFWorkbook();
		HSSFSheet sheet = reBook.createSheet("sheet1");

		// 设置列的长度
		int line = 1;

		HSSFRow row1 = sheet.createRow(0);
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 4));

		HSSFCell cell1 = row1.createCell((short) 0);
		// cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell1.setCellValue("导出结果");

		// 第一行记录
		HSSFRow row = sheet.createRow(line);

		// 样式
		HSSFCellStyle hhsfCellStyle = reBook.createCellStyle();
		HSSFFont font = reBook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		hhsfCellStyle.setBorderBottom((short) 1);
		hhsfCellStyle.setBorderLeft((short) 1);
		hhsfCellStyle.setBorderRight((short) 1);
		hhsfCellStyle.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
		hhsfCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		hhsfCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		hhsfCellStyle.setFont(font);

		HSSFCellStyle errStyle = reBook.createCellStyle();
		HSSFFont errFont = reBook.createFont();
		errFont.setColor(HSSFFont.COLOR_RED);
		errStyle.setFont(errFont);

		for (short j = 0; j < titles.length; j++) {
			HSSFCell cell = row.createCell(j);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(titles[j]);// 改 new HSSFRichTextString(titles[j])
			cell.setCellStyle(hhsfCellStyle);
			sheet.setColumnWidth((short) (j), columnWidths[j]);
		}
		if (valueList != null && valueList.size() > 0) {
			HSSFCell valueCell = null;
			// for (Map<String, Object> map : valueList) {
			for (int j = 0; j < valueList.size(); j++) {
				Map map = (Map) valueList.get(j);
				// 创建下一行
				// index = 0;
				line++;
				row = sheet.createRow(line);
				// 获取map数据

				for (short i = 0; i < showMapColumn.length; i++) {
					// for(String key : showMapColumn){
					String valueStr = ObjectUtils.toString(map.get(showMapColumn[i]));
					valueCell = row.createCell(i);
					if (valueStr.indexOf("未填写") > 0) {
						valueCell.setCellStyle(errStyle);
					}

					// valueCell.setEncoding(HSSFCell.ENCODING_UTF_16);
					valueCell.setCellType(HSSFCell.CELL_TYPE_STRING);
					valueCell.setCellValue(valueStr);

				}

			}

		}

		reBook.write(response.getOutputStream());

	}

	// 创建 excel并导出(将List 转换DynaBean)
	public static void createExcelDyna(HttpServletResponse response, String downFilename, String[] titles,
			short[] columnWidths, List valueList, String showMapColumn[]) throws Exception {

		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Content-Disposition", "attachment;filename=" + downFilename);

		HSSFWorkbook reBook = new HSSFWorkbook();
		HSSFSheet sheet = reBook.createSheet("sheet1");

		// 设置列的长度
		int line = 1;

		HSSFRow row1 = sheet.createRow(0);
		sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 4));

		HSSFCell cell1 = row1.createCell((short) 0);
		// cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell1.setCellValue("导出结果");

		// 第一行记录
		HSSFRow row = sheet.createRow(line);

		// 样式
		HSSFCellStyle hhsfCellStyle = reBook.createCellStyle();
		HSSFFont font = reBook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		hhsfCellStyle.setBorderBottom((short) 1);
		hhsfCellStyle.setBorderLeft((short) 1);
		hhsfCellStyle.setBorderRight((short) 1);
		hhsfCellStyle.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
		hhsfCellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		hhsfCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		hhsfCellStyle.setFont(font);

		HSSFCellStyle errStyle = reBook.createCellStyle();
		HSSFFont errFont = reBook.createFont();
		errFont.setColor(HSSFFont.COLOR_RED);
		errStyle.setFont(errFont);

		for (short j = 0; j < titles.length; j++) {
			HSSFCell cell = row.createCell(j);
			// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellValue(titles[j]);// 改 new HSSFRichTextString(titles[j])
			cell.setCellStyle(hhsfCellStyle);
			sheet.setColumnWidth((short) (j), columnWidths[j]);
		}
		if (valueList != null && valueList.size() > 0) {
			HSSFCell valueCell = null;
			// for (Map<String, Object> map : valueList) {
			for (int j = 0; j < valueList.size(); j++) {
				DynaBean map = (DynaBean) valueList.get(j);
				// 创建下一行
				// index = 0;
				line++;
				row = sheet.createRow(line);
				// 获取map数据

				for (short i = 0; i < showMapColumn.length; i++) {
					// for(String key : showMapColumn){
					String valueStr = ObjectUtils.toString(map.get(showMapColumn[i]));
					valueCell = row.createCell(i);
					if (valueStr.indexOf("未填写") > 0) {
						valueCell.setCellStyle(errStyle);
					}

					// valueCell.setEncoding(HSSFCell.ENCODING_UTF_16);
					valueCell.setCellType(HSSFCell.CELL_TYPE_STRING);
					valueCell.setCellValue(valueStr);

				}

			}

		}

		reBook.write(response.getOutputStream());

	}

	public void createExcels(HttpServletRequest request, HttpServletResponse response, Map map) throws Exception {

		try {
			response.setContentType("application/doc"); // MIME type for pdf doc
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			// 创建workbook
			HSSFWorkbook wb = new HSSFWorkbook();

			// 使用默认的构造方法创建workbook
			FileOutputStream fileOut;

			// fileOut = new FileOutputStream(file);

			HSSFSheet sheet = wb.createSheet();

			wb.setSheetName(0, "sheet0");

			// 生成一个样式
			HSSFCellStyle style = wb.createCellStyle();

			// 生成一个字体
			HSSFFont font = wb.createFont();

			font.setFontName("宋体");
			font.setFontHeightInPoints((short) 16);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

			// 把字体应用到当前的样式
			style.setFont(font);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			if (dateRs != null) {
				int i = 2;
				int columnCount = dateRs.getMetaData().getColumnCount();

				// 合並單元格,下標從0開始
				sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (columnCount - 1)));

				// 标题
				HSSFRow rowT = sheet.createRow(0);
				HSSFCell cellT = rowT.createCell((short) 0);

				// cellT.setEncoding(HSSFCell.ENCODING_UTF_16);
				cellT.setCellValue(fileTitle);
				cellT.setCellStyle(style);

				// 字段
				HSSFRow colrow = sheet.createRow((short) 1);
				for (int k = 0; k < columnCount; k++) {
					HSSFCell cell = colrow.createCell((short) k);
					// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					cell.setCellValue(dateRs.getMetaData().getColumnName(k + 1));
				}
				// 内容
				while (dateRs.next()) {
					HSSFRow datarow = sheet.createRow((short) i);
					for (int k = 0; k < columnCount; k++) {
						HSSFCell cell = datarow.createCell((short) k);
						// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
						cell.setCellValue(BaseUtil.toString(dateRs.getObject(k + 1)));
					}
					i++;
				}
				/*
				 * sheet.addMergedRegion(new Region(i, (short) 0, i, (short)
				 * (columnCount-1))); HSSFRow rowbill_names =
				 * sheet.createRow(i); HSSFCell cellbill_names =
				 * rowbill_names.createCell((short) 0);
				 * 
				 * cellbill_names.setEncoding(HSSFCell.ENCODING_UTF_16);
				 * cellbill_names.setCellValue("合计："+map.get("TOTALFEE").
				 * toString()); cellbill_names.setCellStyle(style);
				 */
			}

			// 指定文件名
			wb.write(response.getOutputStream());

			// 输出到文件
			response.getOutputStream().close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dateRs != null) {
				dateRs.close();
			}
		}
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @author zhanglb 2012-6-1
	 * @param cell
	 * @return
	 */
		public static String getStringCellValue(HSSFCell cell) {
		String strCell = "";
		if (cell != null) {
			switch (cell.getCellType()) {
				case HSSFCell.CELL_TYPE_STRING:
					strCell = cell.getStringCellValue().trim();
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:

					java.text.NumberFormat nf = NumberFormat.getCurrencyInstance();
					DecimalFormat df = (DecimalFormat) nf;
					df.setDecimalSeparatorAlwaysShown(true);
					df.applyPattern("###############");
					String value = df.format(new Double(cell.getNumericCellValue()));

					strCell = String.valueOf(value);
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					strCell = String.valueOf(cell.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_BLANK:
					strCell = "";
					break;
				default:
					strCell = "";
					break;
			}
		}

		if (strCell == null) {
			return "";
		}
		if (cell == null) {
			return "";
		}
		return strCell;
	}

	public static List<Map<String, String>> getListFromExcel(Map<String, String> formMap, File file) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
		int startNum = 1;
		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();
		HSSFRow startRow = workbook.getSheetAt(0).getRow(startNum);
		HashMap<Integer, String> columnMap = new HashMap<Integer, String>();
		for (Cell cell : startRow) {
			columnMap.put(cell.getColumnIndex(), formMap.get(getCellValue(cell)));
		}
		for (int i = 0; i <= workbook.getActiveSheetIndex(); i++) {
			for (Row row : workbook.getSheetAt(i)) {
				HashMap<String, String> resultMap = new HashMap<String, String>();
				int rowNum = row.getRowNum();
				if (rowNum > startNum) {
					for (Cell cell : row) {
						resultMap.put(columnMap.get(cell.getColumnIndex()), getCellValue(cell));
					}
					resultList.add(resultMap);
				}
			}
		}
		return resultList;
	}

	private static String getCellValue(Cell cell) {
		int cellType = cell.getCellType();
		String cellValue = "";
		switch (cellType) {
			case Cell.CELL_TYPE_BLANK:// 空值
				break;
			case Cell.CELL_TYPE_BOOLEAN:// 布尔型
				cellValue = ((Boolean) cell.getBooleanCellValue()).toString();
				break;
			case Cell.CELL_TYPE_ERROR:// 错误
				break;
			case Cell.CELL_TYPE_FORMULA:// 公式型
				cellValue = cell.getCellFormula();
				break;
			case Cell.CELL_TYPE_NUMERIC:// 数值型
				cellValue = ((Double) cell.getNumericCellValue()).toString();
				break;
			case Cell.CELL_TYPE_STRING:// 字符串型
				cellValue = cell.getStringCellValue();
				break;
			default:
				cellValue = cell.getRichStringCellValue().getString();
				break;
		}
		return cellValue;
	}

	/**
	 * 导出Excel
	 *
	 * @param response
	 * @throws IOException
	 */

	public static void exportExcel(String fileName, HSSFWorkbook workbook, HttpServletResponse response)
			throws IOException {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
		OutputStream ouputStream = response.getOutputStream();
		workbook.write(ouputStream);

		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 使用POI读取Excel表格
	 *
	 * @param filename
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public static List readExcelToDB2(File filename) {
		String[] cellValues = null; // 保存excel中单元格的值；
		HSSFWorkbook wb = null;
		List list1 = new ArrayList();
		try {
			wb = new HSSFWorkbook(new FileInputStream(filename));
		} catch (IOException e) {
			e.printStackTrace();
			return list1;
		}

		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row = null;
		HSSFCell cell = null;
		int cellLength = sheet.getRow(2).getPhysicalNumberOfCells();
		int rowNum = sheet.getLastRowNum();

		for (int i = 3; i <= rowNum; i++) {
			row = sheet.getRow(i);
			List list = new ArrayList();
			cellValues = new String[cellLength];
			boolean falg = false;
			for (int j = 0; j < cellLength; j++) {
				cell = row.getCell((short) j);

				try {
					cellValues[j] = getStringCellValue(cell);
				} catch (Exception e) {

				}
				if ((cellValues[j] != null) && !"".equals(cellValues[j])) {
					falg = true;
				}
				list.add(cellValues[j]);
			}
			if (falg) {
				list1.add(list);
			}
		}

		return list1;
	}
	
	
	public static void exportExcel(String fileName,HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {

			// 把数据以规定的格式输出
			response.setContentType("application/xls");
			response.setHeader("Content-disposition", "attachment; filename="+ fileName);

			// 将内存里导出的EXCEL文件包装成FileInputStream流然后返回给ACTION
			FileInputStream fis = new FileInputStream(fileName);
			bis = new BufferedInputStream(fis);
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			while (-1 != (bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, buff.length);

			}
			fis.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				bis.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
	}

	/**
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月1日 下午4:25:30
	 * @param excelBeans
	 *            数据
	 * @param templatePath
	 *            模板路径
	 * @param response
	 * @param fileName
	 *            导出文件名
	 */
	public static void exportExcel(Map<String, Object> excelBeans, String templatePath, HttpServletResponse response, String fileName) {
		XLSTransformer transformer = new XLSTransformer();
		InputStream is = null;
		ServletOutputStream os = null;
		try {
			response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
			response.setContentType("application/x-msdownload;charset=utf-8");
			is = new BufferedInputStream(new FileInputStream(templatePath));
			Workbook hssfWorkbook = transformer.transformXLS(is, excelBeans);
			os = response.getOutputStream();
			response.setContentType("application/vnd.ms-excel;charset=gb2312");
			hssfWorkbook.write(os);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
