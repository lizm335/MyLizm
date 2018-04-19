package pdf;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class CreatePdf {
	Document document;

	public static Font headfont;	// 设置字体大小
	public static Font keyfont;	// 设置字体大小
	public static Font textfont;	// 设置字体大小
	public static BaseColor gray1;     // 灰色

	static {
		BaseFont bfChinese;
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			headfont = new Font(bfChinese, 10, Font.BOLD);	// 设置字体大小
			keyfont = new Font(bfChinese, 8, Font.BOLD);	// 设置字体大小
			textfont = new Font(bfChinese, 8, Font.NORMAL);	// 设置字体大小
			gray1 = new BaseColor(208,221,213);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static PdfPCell createCell(String value, Font font, int align) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setPhrase(new Phrase(value, font));
		return cell;
	}

	public static PdfPCell createCell(String value, Font font, int rowspan, BaseColor color) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPhrase(new Phrase(value, font));
		if (rowspan>0) {
			cell.setRowspan(rowspan);
		}
		if (color!=null) {
			cell.setBackgroundColor(color);
		}
		return cell;
	}
	

	public static PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag, BaseColor color) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setColspan(colspan);
		cell.setPhrase(new Phrase(value, font));
		cell.setPadding(5.0f);
		if (color!=null) {
			cell.setBackgroundColor(gray1);
		}
		if (!boderFlag) {
			cell.setBorder(0);
			cell.setPaddingTop(15.0f);
			cell.setPaddingBottom(8.0f);
		}
		return cell;
	}

	public static PdfPTable createTable(int colNumber, int maxWidth) {
		PdfPTable table = new PdfPTable(colNumber);
		try {
			table.setTotalWidth(maxWidth);
			table.setLockedWidth(true);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setBorder(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}

	public static PdfPTable createTable(float[] widths, int maxWidth) {
		PdfPTable table = new PdfPTable(widths);
		try {
			table.setTotalWidth(maxWidth);
			table.setLockedWidth(true);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setBorder(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}

	public void generatePDF() throws Exception {
		
	}
}
