package com.gzedu.xlims.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;

public class CellUtils {

	public static String getCellValue(Cell cell) {

		if (cell == null) {
			return StringUtils.EMPTY;
		}

		String cellValue = null;

		switch (cell.getCellType()) {

			case HSSFCell.CELL_TYPE_STRING:
				cellValue = cell.getRichStringCellValue().getString().trim();
				break;

			case HSSFCell.CELL_TYPE_NUMERIC:
				java.text.NumberFormat nf = NumberFormat.getCurrencyInstance();
				DecimalFormat df = (DecimalFormat) nf;
				df.setDecimalSeparatorAlwaysShown(true);
				df.applyPattern("###############");
				String value = df.format(new Double(cell.getNumericCellValue()));
				cellValue = String.valueOf(value);
				break;

			case HSSFCell.CELL_TYPE_BOOLEAN:
				cellValue = ObjectUtils.toString(cell.getBooleanCellValue());
				break;

			case HSSFCell.CELL_TYPE_BLANK:
				cellValue = "";
				break;

			case HSSFCell.CELL_TYPE_ERROR:
				cellValue = "";
				break;

			default:
				cellValue = "";

		}

		if (StringUtils.isBlank(cellValue)) {
			return StringUtils.EMPTY;
		}

		return StringUtils.replace(cellValue, "\\s*", StringUtils.EMPTY);
	}
}
