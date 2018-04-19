/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.common.excel;

import java.lang.reflect.Field;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * 功能说明：导入excel数据转换抽象类
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年12月5日
 * @version 2.5
 *
 */
public abstract class ImportModel {

	// 一行数据填充成一个对象
	public void fill(Row row) {
		Field[] fields = getClass().getDeclaredFields();

		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			try {
				field.set(this, getCellValue(row.getCell(i)));
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
		}
	}

	// 把数据对象转成一行数据
	public void convert(Row row) {
		Field[] fields = getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			try {
				row.createCell(i).setCellValue(String.valueOf(field.get(this)));
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
		}
	}

	private String getCellValue(Cell cell) {
		String value = "";
		if (cell != null) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
			value = cell.getStringCellValue().trim();
		}
		return value;
	}
}
