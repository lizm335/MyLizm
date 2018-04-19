package com.gzedu.xlims.common;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * service 封装并生成excel
 * @author paul
 *
 */
@SuppressWarnings({ "unused", "unchecked", "rawtypes", "deprecation" })
public class ExcelService {

    public static final String TYPE_03    = "xls", TYPE_07 = "xlsx";
    
    private static final double EXP        = 0.0000000000000001;
	private static final String EXCEL_SIGN = ".xls";
    //private static RedisCacheManager redis = new RedisCacheManager();
	private static final int SLEEP_TIME = 600;

	private ExcelService(){
		
	}

	/**保存失败成功数组对象到缓存*/
	public static boolean setToRedis(List datas){
        return  true;
    }

	public String getFileByRedis(){
        return  "";
    }

	/**获取缓存中的数组对象*/


    /**
     * 简单表格文件生成( 字段名第一天 第二行开始数据)
     * 
     * @param datas
     *            数据项列表
     * @param fields
     *            字段名(中文名)
     * @param dataNames
     *            数据项名（数据库列,必须跟字段名对应）
     * @return 随机生成的表格名(由SequenceUUID.getSequence())
     */
    public static String renderExcel(List datas, List fields, List dataNames) {
		String fileName = SequenceUUID.getSequence()+EXCEL_SIGN;
		try {
			// 写到客户指定的目录文件下
			File file = new File(fileName);
			// 建立新HSSFWorkbook对象
			HSSFWorkbook wb = null;
			wb = new HSSFWorkbook();
			// 使用默认的构造方法创建workbook
			FileOutputStream fileout;
			fileout = new FileOutputStream(file);
			// 建立新的sheet对象
			HSSFSheet sheet = wb.createSheet();
			wb.setSheetName(0, "sheet0");
			
			HSSFRow row2 = null;
			HSSFRow row3 = null;
			
			//设置字体
			HSSFFont workFont1 = wb.createFont();
			workFont1.setFontHeightInPoints((short) 14);
			workFont1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			
			HSSFFont workFont2 = wb.createFont();
			workFont2.setFontHeightInPoints((short) 10);
			workFont2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			
			//样式
			HSSFCellStyle dtStyle1 = wb.createCellStyle();
			dtStyle1.setFont(workFont1);
			dtStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直对齐
			dtStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平对齐
			dtStyle1.setFillPattern(HSSFColor.BLUE.index);
			
			HSSFCellStyle dtStyle2 = wb.createCellStyle();
			dtStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直对齐
			dtStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平对齐
			dtStyle2.setFont(workFont2);
			
			
			// 列名称
			row2 = sheet.createRow((short) 0);
            for (int i = 0; i < fields.size(); i++) {
				HSSFCell cell2 = row2.createCell((short) i);
				cell2.setCellStyle(dtStyle2);
                cell2.setCellValue(ObjectUtils.toString(fields.get(i)));
			}
			
			int index = 0;
			for (int j=0; j<datas.size(); j++) {
				index++;
				HashMap clsBean = (HashMap)datas.get(j);
				row3 = sheet.createRow(index);
                for (int k = 0; k < dataNames.size(); k++) {
                    row3.createCell((short) k).setCellValue(ObjectUtils.toString(clsBean.get(ObjectUtils.toString(dataNames.get(k)))));
				}
			}
			
			HSSFFont font = wb.createFont();
			font.setColor(HSSFFont.COLOR_RED);
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			cellStyle.setWrapText(true); 
			cellStyle.setFont(font);
			wb.write(fileout);
			fileout.close();
		}catch (Exception e) {
			e.printStackTrace();
		}	
		return fileName;
	}
    
    /**
     * 
     * @param wb
     * @return 单元格样式(居中 四边框)
     */
    private static HSSFCellStyle getStyle(HSSFWorkbook wb) {
        HSSFCellStyle style = wb.createCellStyle();
        short size = 1;
        // 边框
        style.setBorderBottom(size);
        style.setBorderLeft(size);
        style.setBorderTop(size);
        style.setBorderRight(size);
        
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直对齐
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平对齐
        
        return style;
    }
    
    /**
     * 
     * @param wb
     * @param size
     *            大小
     * @param fontType
     *            字体
     * @param fine
     *            粗细
     * @return
     */
    private static HSSFFont getFont(HSSFWorkbook wb, short size, String fontType, short fine) {
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints(size);
        font.setFontName(fontType);
        font.setBoldweight(fine);
        return font;
    }

    /**
     * 简单表格文件生成( 字段名第一天 第二行开始数据,数组 效率更佳)
     *
     * @param datas
     *            数据项列表
     * @param fields
     *            字段名(中文名)
     * @param dataNames
     *            数据项名（数据库列,必须跟字段名对应）
     * @param fileName
     *            导出的文件名
     * @return 随机生成的表格名(由SequenceUUID.getSequence())
     */
    public static String renderExcel(List datas, String[] fields, String[] dataNames, String title, String path, String fileName) {
        fileName = fileName.contains(EXCEL_SIGN) ? fileName : fileName + EXCEL_SIGN;
        try {
            // 写到客户指定的目录文件下
            File file = new File(fileName);
            // 建立新HSSFWorkbook对象
            HSSFWorkbook wb = new HSSFWorkbook();
            // 使用默认的构造方法创建workbook
            FileOutputStream fileout;
            fileout = new FileOutputStream(file);
            // 建立新的sheet对象
            HSSFSheet sheet = wb.createSheet();
            wb.setSheetName(0, "sheet0");

            HSSFRow row1, row2, row3;

            // 设置字体
            HSSFFont workFont1 = getFont(wb, (short) 12, "微软雅黑", HSSFFont.BOLDWEIGHT_BOLD);// 属性名字体
            HSSFFont workFont2 = getFont(wb, (short) 8, "微软雅黑", HSSFFont.BOLDWEIGHT_NORMAL);// 数据源字体
            HSSFFont titleFont = getFont(wb, (short) 20, "微软雅黑", HSSFFont.BOLDWEIGHT_BOLD);// 标题字体

            // 设置样式
            HSSFCellStyle titleCellStyle = getStyle(wb);
            titleCellStyle.setFont(titleFont);

            HSSFCellStyle fieldStyle = getStyle(wb);
            // 设置背景色 灰色
            fieldStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            fieldStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            fieldStyle.setFont(workFont1);

            HSSFCellStyle datasStyle = getStyle(wb);
            datasStyle.setFont(workFont2);
            datasStyle.setWrapText(true);

            // 标题
            row1 = sheet.createRow((short) 0);
            row1.setHeight((short) 600);

            HSSFCell titleCell = row1.createCell(0);
            // 定义单元格为字符串类型
            titleCell.setCellType(HSSFCell.CELL_TYPE_STRING);
            titleCell.setCellStyle(titleCellStyle);

            /**
             * 填充数据
             */

            titleCell.setCellValue(new HSSFRichTextString(title));

            // 列名称

            row2 = sheet.createRow((short) 1);
            row2.setHeight((short) 500);
            for (int i = 0; i < fields.length; i++) {
                HSSFCell cell2 = row2.createCell((short) i);
                sheet.setColumnWidth(i, (fields[i].getBytes().length + 2) * 512);// 中文宽度自适应
                // sheet.autoSizeColumn(i);// 宽度自适应
                cell2.setCellStyle(fieldStyle);
                cell2.setCellValue(ObjectUtils.toString(fields[i]));

            }

            int index = 1;
            for (int j = 0; j < datas.size(); j++) {
                index++;
                HashMap clsBean = (HashMap) datas.get(j);
                row3 = sheet.createRow(index);
                row3.setHeight((short) 500);
                for (int k = 0; k < dataNames.length; k++) {
                    HSSFCell dataCell = row3.createCell((short) k);
                    dataCell.setCellStyle(datasStyle);
                    dataCell.setCellValue(ObjectUtils.toString(clsBean.get(ObjectUtils.toString(dataNames[k]))));
                }
            }

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, dataNames.length - 1)); // 合并标题单元格

            wb.write(fileout);
            fileout.close();
            FileUtils.copyFile(file,new File(path+fileName));
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }
    
    /**
     * 简单表格文件生成( 字段名第一天 第二行开始数据,数组 效率更佳)
     * 
     * @param datas
     *            数据项列表
     * @param fields
     *            字段名(中文名)
     * @param dataNames
     *            数据项名（数据库列,必须跟字段名对应）
     * @return 随机生成的表格名(由SequenceUUID.getSequence())
     */
    public static String renderExcel(List datas, String[] fields, String[] dataNames, String title, String path) {
        String fileName = SequenceUUID.getSequence() + EXCEL_SIGN;
        return renderExcel(datas, fields, dataNames, title, path, fileName);
    }
    
    /**
     * 简单表格文件生成(标题第一行 字段名第二天 第三行开始数据)  保存在temp文件内
     * 
     * @param datas
     *            数据项列表
     * @param fields
     *            字段名(中文名)
     * @param dataNames
     *            数据项名（数据库列,必须跟字段名对应）
     * @param titleName
     *            标题名
     * @return 随机生成的表格名(由SequenceUUID.getSequence())  不包含 path
     */
    public static String renderExcelHadTitle(List datas, List fields, List dataNames, String titleName,String path) {
        String fileName = SequenceUUID.getSequence() + EXCEL_SIGN;
        try {
            // 写到客户指定的目录文件下
            File file = new File(fileName);
            // 建立新HSSFWorkbook对象
            HSSFWorkbook wb = null;
            wb = new HSSFWorkbook();
            // 使用默认的构造方法创建workbook
            FileOutputStream fileout;
            fileout = new FileOutputStream(file);
            // 建立新的sheet对象
            HSSFSheet sheet = wb.createSheet();
            wb.setSheetName(0, "sheet0");
            
            HSSFRow row2 = null;
            HSSFRow row3 = null;
            
            // 设置字体
            HSSFFont workFont1 = wb.createFont();
            workFont1.setFontHeightInPoints((short) 14);
            workFont1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            
            HSSFFont workFont2 = wb.createFont();
            workFont2.setFontHeightInPoints((short) 10);
            workFont2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            
            // 样式
            HSSFCellStyle dtStyle1 = wb.createCellStyle();
            dtStyle1.setFont(workFont1);
            dtStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直对齐
            dtStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平对齐
            dtStyle1.setFillPattern(HSSFColor.BLUE.index);
            
            HSSFCellStyle dtStyle2 = wb.createCellStyle();
            dtStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER); // 垂直对齐
            dtStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平对齐
            dtStyle2.setFont(workFont2);
            
            // 列名称
            row2 = sheet.createRow((short) 0);
            for (int i = 0; i < fields.size(); i++) {
                HSSFCell cell2 = row2.createCell((short) i);
                cell2.setCellStyle(dtStyle2);
                cell2.setCellValue(ObjectUtils.toString(fields.get(i)));
            }
            
            int index = 0;
            for (int j = 0; j < datas.size(); j++) {
                index++;
                HashMap clsBean = (HashMap) datas.get(j);
                row3 = sheet.createRow(index);
                for (int k = 0; k < dataNames.size(); k++) {
                    row3.createCell((short) k).setCellValue(ObjectUtils.toString(clsBean.get(ObjectUtils.toString(dataNames.get(k)))));
                }
            }
            
            HSSFFont font = wb.createFont();
            font.setColor(HSSFFont.COLOR_RED);
            HSSFCellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            cellStyle.setWrapText(true);
            cellStyle.setFont(font);
            wb.write(fileout);
            fileout.close();
            FileUtils.copyFile(file,new File(path+fileName));
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * @param file
     *            表格文件
     * @param rowLength
     *            每行数据项长度(标题长度)
     * @param start
     *            数据开始行数 0开始算
     * @return 读取表格 将数据填充到List
     */
    public static List<String[]> getDateFormExcl(File file, short rowLength, int start, String file_suffix) {
        List<String[]> datas = new ArrayList();
        try {
            String[] temp = file.getName().split("\\.");
            String suffix = temp[1];
            if (TYPE_07.equals(suffix.toLowerCase()) || TYPE_07.equals(file_suffix)) {
                datas = getExcel07(file, rowLength, start);
            } else {
                datas = getExcel03(file, rowLength, start);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        return datas;
    }

    private static List<String[]> getExcel03(File file, short rowLength, int start) throws FileNotFoundException, IOException {
        List<String[]> datas = new ArrayList();
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheet = workbook.getSheetAt(0);
        int rownum = sheet.getLastRowNum(); // 得到总行数
        short column = rowLength;//
        for (int i = start; i <= rownum; i++) {
            HSSFRow hssfRow = sheet.getRow(i);
            if (hssfRow == null) {
                break;
            }
            String[] cells = getCells(hssfRow, column);
            if (cells == null) {
                break;
            }
            datas.add(cells);
        }
        return datas;
    }
    
    private static List<String[]> getExcel07(File file, short rowLength, int start) throws FileNotFoundException, IOException {
        List<String[]> datas = new ArrayList();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(file));
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        int rownum = sheet.getLastRowNum(); // 得到总行数
        short column = rowLength;//
        for (int i = start; i <= rownum; i++) {
            XSSFRow hssfRow = sheet.getRow(i);
            if (hssfRow == null) {
                break;
            }
            String[] cells = getCells(hssfRow, column);
            if (cells == null) {
                break;
            }
            datas.add(cells);
        }
        return datas;
    }
    
    
    /**
     * @param file
     *            表格文件
     * @param rowLength
     *            每行数据项长度(标题长度)
     * @param startcount
     *            数据开始行数 0开始算
     * @return 读取表格 将数据填充到List
     */
    public static List<String[]> getDateFormExclByCount(File file, short rowLength, int startcount,int redcount) 
    {
    	List<String[]> datas = new ArrayList();
    	try 
    	{
	         XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(file));
	         XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
	         int rownum = sheet.getLastRowNum(); // 得到总行数
	         short column = rowLength;//
	         for (int i = startcount; i <= redcount; i++) {
	             XSSFRow hssfRow = sheet.getRow(i);
	             if (hssfRow == null) {
	                 break;
	             }
	             String[] cells = getCells(hssfRow, column);
	             if (cells == null) {
	                 break;
	             }
	             datas.add(cells);
	         }
    	} catch (Exception e) 
    	{
			e.printStackTrace();
		}
         return datas;
    }

    /**
     * 
     * @param hssfRow
     *            此行对象
     * @param maxCell
     *            列长
     * @return 获取每项值
     */
    public static String[] getCells(XSSFRow hssfRow, short maxCell) {
        String[] cells = new String[maxCell];
        if (hssfRow == null) {
            return cells;
        }
        for (short cell = 0; cell < maxCell; cell++) {// 排除序号列 -1
            XSSFCell hssrCell = hssfRow.getCell(cell);
            if (hssrCell == null) {
                cells[cell] = "";
            } else {
                String value = getCellStringValue(hssrCell);
                if (EmptyUtils.isEmpty(value)) {
                    cells[cell] = "";
                } else {
                    cells[cell] = value;
                }
            }
        }
        return cells;
    }
    
    /**
     * 
     * @param exlFile
     *            表格文件
     * @param fieldNames
     *            标准字段名
     * @param start
     *            开始行下标 0开始算
     * @return 是否该行字段名同标准字段名
     */
    public static boolean checkDatas(File exlFile, String[] fieldNames, int start, String file_suffix) {
        try {
            if (exlFile.isFile() && exlFile.length() == 0) {
                return false;
            }
            String[] temp = exlFile.getName().split("\\.");
            String suffix = temp[1];
            if (TYPE_07.equals(suffix.toLowerCase()) || TYPE_07.equals(file_suffix)) {
                XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(exlFile));
                if (workbook == null) {
                    return false;
                }
                XSSFSheet sheet = workbook.getSheetAt(0);
                // 检验第二行(字段名行)是否符合要求
                XSSFRow hssfRow = sheet.getRow(start);
                for (int col = 0; col < fieldNames.length; col++) {
                    String value = getCellStringValue(hssfRow.getCell(col));
                    if (!fieldNames[col].equals(ObjectUtils.toString(value, "").trim())) {
                        return false;
                    }
                }
            } else {
                HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(exlFile));
                if (workbook == null) {
                    return false;
                }
                HSSFSheet sheet = workbook.getSheetAt(0);
                // 检验第二行(字段名行)是否符合要求
                HSSFRow hssfRow = sheet.getRow(start);
                for (int col = 0; col < fieldNames.length; col++) {
                    String value = getCellStringValue(hssfRow.getCell(col));
                    if (!fieldNames[col].equals(ObjectUtils.toString(value, "").trim())) {
                        return false;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    

    /**
     * 
     * @param xssfCell
     *            表格中的项
     * @return 项所属类型
     */
    private static String getCellStringValue(XSSFCell xssfCell) {
        String retStr = "";
        if (xssfCell != null) {
            int type = xssfCell.getCellType();
            switch (type) {
            case HSSFCell.CELL_TYPE_NUMERIC: // Numeric
                if (HSSFDateUtil.isCellDateFormatted(xssfCell)) {
                    retStr = (new SimpleDateFormat("yyyy-MM-dd").format(xssfCell.getDateCellValue()));
                } else {
                    double value = xssfCell.getNumericCellValue();
                    if (Math.abs(value - (long) value) < EXP) {
                        retStr = String.valueOf((long) value);
                    } else {
                        retStr = String.valueOf(xssfCell.getNumericCellValue());
                    }
                }
                break;
            case HSSFCell.CELL_TYPE_STRING: // String
                retStr = xssfCell.getRichStringCellValue().getString();
                break;
            case HSSFCell.CELL_TYPE_FORMULA: // Formula 公式, 方程式
                retStr = String.valueOf(xssfCell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK: // Blank
                retStr = "";
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN: // boolean
                retStr = Boolean.valueOf(xssfCell.getBooleanCellValue()).toString();
                break;
            case HSSFCell.CELL_TYPE_ERROR: // Error
                break;
            }
        }
        return retStr.trim();
    }
    
    /**
     * 
     * @param hssfCell
     *            表格中的项
     * @return 项所属类型
     */
    private static String getCellStringValue(HSSFCell hssfCell) {
        String retStr = "";
        if (hssfCell != null) {
            int type = hssfCell.getCellType();
            switch (type) {
            case HSSFCell.CELL_TYPE_NUMERIC: // Numeric
                if (HSSFDateUtil.isCellDateFormatted(hssfCell)) {
                    retStr = (new SimpleDateFormat("yyyy-MM-dd").format(hssfCell.getDateCellValue()));
                } else {
                    double value = hssfCell.getNumericCellValue();
                    if (Math.abs(value - (long) value) < EXP) {
                        retStr = String.valueOf((long) value);
                    } else {
                        retStr = String.valueOf(hssfCell.getNumericCellValue());
                    }
                }
                break;
            case HSSFCell.CELL_TYPE_STRING: // String
                retStr = hssfCell.getRichStringCellValue().getString();
                break;
            case HSSFCell.CELL_TYPE_FORMULA: // Formula 公式, 方程式
                retStr = String.valueOf(hssfCell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK: // Blank
                retStr = "";
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN: // boolean
                retStr = Boolean.valueOf(hssfCell.getBooleanCellValue()).toString();
                break;
            case HSSFCell.CELL_TYPE_ERROR: // Error
                break;
            }
        }
        return retStr.trim();
    }
    
    /**
     * 
     * @param hssfRow
     *            此行对象
     * @param maxCell
     *            列长
     * @return 获取每项值
     */
    public static String[] getCells(HSSFRow hssfRow, short maxCell) {
        String[] cells = new String[maxCell];
        if (hssfRow == null) {
            return cells;
        }
        for (short cell = 0; cell < maxCell; cell++) {// 排除序号列 -1
            HSSFCell hssrCell = hssfRow.getCell(cell);
            if (hssrCell == null) {
                cells[cell] = "";
            } else {
                String value = getCellStringValue(hssrCell);
                if (EmptyUtils.isEmpty(value)) {
                    cells[cell] = "";
                } else {
                    cells[cell] = value;
                }
            }
        }
        return cells;
    }
    
    /**
     * 
     * 导出到本地 返回表格文件名
     * 
     * @param datas
     *            数据项列表
     * @param fields
     *            字段名(中文名)
     * @param dataNames
     *            数据项名（数据库列,必须跟字段名对应）
     * @param appNames
     *            院校名
     * @param fileName
     *            生成的文件名
     * @return fileName
     */
//    public static String renderExcelPagination(List datas, String[] fields, String[] dataNames, String title, String fileName) {
//        try {
//            File file = new File(fileName);
//            HSSFWorkbook wb = new HSSFWorkbook();
//            FileOutputStream fileout;
//            fileout = new FileOutputStream(file);
//            HSSFSheet sheet = wb.createSheet();
//            wb.setSheetName(0, fileName);
//            HSSFRow row1, row2, row3;
//            
//            // 设置字体
//            HSSFFont workFont1 = getFont(wb, (short) 12, "微软雅黑", HSSFFont.BOLDWEIGHT_BOLD);// 属性名字体
//            HSSFFont workFont2 = getFont(wb, (short) 8, "微软雅黑", HSSFFont.BOLDWEIGHT_NORMAL);// 数据源字体
//            HSSFFont titleFont = getFont(wb, (short) 20, "微软雅黑", HSSFFont.BOLDWEIGHT_BOLD);// 标题字体
//            // 设置样式
//            HSSFCellStyle titleCellStyle = getStyle(wb);
//            titleCellStyle.setFont(titleFont);
//            
//            HSSFCellStyle fieldStyle = getStyle(wb);
//            // 设置背景色 灰色
//            fieldStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
//            fieldStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
//            fieldStyle.setFont(workFont1);
//            HSSFCellStyle datasStyle = getStyle(wb);
//            datasStyle.setFont(workFont2);
//            // 标题
//            row1 = sheet.createRow((short) 0);
//            row1.setHeight((short) 600);
//
//            HSSFCell titleCell = row1.createCell(0);
//            // 定义单元格为字符串类型
//            titleCell.setCellType(HSSFCell.CELL_TYPE_STRING);
//            titleCell.setCellStyle(titleCellStyle);
//            titleCell.setCellValue(new HSSFRichTextString(title));
//            row2 = sheet.createRow((short) 1);
//            row2.setHeight((short) 500);
//            for (int i = 0; i < fields.length; i++) {
//                HSSFCell cell2 = row2.createCell((short) i);
//                sheet.setColumnWidth(i, (fields[i].getBytes().length + 2) * 512);// 中文宽度自适应
//                // sheet.autoSizeColumn(i);// 宽度自适应
//                cell2.setCellStyle(fieldStyle);
//                cell2.setCellValue(ObjectUtils.toString(fields[i]));
//
//            }
//            
//            int index = 1;
//            for (int j = 0; j < datas.size(); j++) {
//                index++;
//                HashMap clsBean = (HashMap) datas.get(j);
//                row3 = sheet.createRow(index);
//                row3.setHeight((short) 500);
//                for (int k = 0; k < dataNames.length; k++) {
//                    HSSFCell dataCell = row3.createCell((short) k);
//                    dataCell.setCellStyle(datasStyle);
//                    dataCell.setCellValue(ObjectUtils.toString(clsBean.get(ObjectUtils.toString(dataNames[k]))));
//                }
//            }
//            
//            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, dataNames.length - 1)); // 合并标题单元格
//
//            wb.write(fileout);
//            fileout.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            String target = ServletActionContext.getServletContext().getRealPath("/upload/data/records/zx/fx_") + fileName + EXCEL_SIGN;
//            FileUtils.copyFile(new File(fileName), new File(target));
//            file.delete();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return fileName;
//    
//    }
    
    
//    /**
//     *
//     *@param fileNames 表格文件名数组
//     *@return
//     */
//    public static String zipExcel(List<String>  fileNames,String zipName) {
//        File zip = new File(ServletActionContext.getServletContext().getRealPath("/upload/data/records/zx/zx_") + zipName);// 压缩文件
//        File srcfile[] = new File[fileNames.size()];
//        for (int i = 0, n = fileNames.size(); i < n; i++) {
//            srcfile[i] = new File(ServletActionContext.getServletContext().getRealPath("/upload/data/records/zx/fx_") + fileNames.get(i) + EXCEL_SIGN);
//        }
//        zip(srcfile, zip);
//        for (File file : srcfile) {
//            file.delete();
//        }
//        return "zx_" + zipName;
//    }
    

    private static void zip(File[] srcfile, File zipfile) {
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            out.setEncoding("UTF-8");
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
