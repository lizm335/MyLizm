package com.gzedu.xlims.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfFileExport {
	
	private static final Log log = LogFactory.getLog(PdfFileExport.class);

	private final static DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
	private final static DateFormat df2 = new SimpleDateFormat("HH:mm");

	public boolean exportTableContent(String fullFilePath, String[] tableContent, int rowsNumber, int submitAmount,
			List list, Map temp,String outputTmpPath) throws MalformedURLException, IOException {
		
		Document pdfDocument = new Document(PageSize.A4, 25, 25, 25, 25);
		try {
			// 构建一个PDF文档输出流程
			OutputStream pdfFileOutputStream = new FileOutputStream(new File(fullFilePath));
			PdfWriter pdfWriter = PdfWriter.getInstance(pdfDocument, pdfFileOutputStream);

			// 设置中文字体和字体样式
			BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			Font f8 = new Font(bfChinese, 8, Font.NORMAL);
			// 打开PDF文件流
			pdfDocument.open();
			Font font = new Font(bfChinese, 16);
			Font font2 = new Font(bfChinese, 8);
			Font font3 = new Font(bfChinese, 12);

			String text = ObjectUtils.toString(temp.get("ADMISSION_ZKZ"));
			String text0 = " ";

			String text3 = "考试开始30分钟后，考生不得进入考场。";
			String text4 = "考生必须带齐身份证、准考证、学生证参加考试,否则不得进入考场。";
			String text13 = "考试期间禁止使用手机，请同学们严格遵守考场规则。";
			String text5 = " 注：";
			String text6 = "1、新生的学生证在第一场考试前发放，请新生提前半小时联系该考点负责考务工作的教师领取学生证。";
			String text7 = "2、请同一时间段考两科或两科以上的学员，考完每一科交卷的时候，要举手告诉监考教师自己是连考学员，监考教师会安排你下一科场考试。";
			String text8 = "关于连考科目考试说明";
			String text9 = "1、 连考就是指同一日期同一时间段考两科或两科以上；";
			String text10 = "2、 查看准考证，仔细查看自己是否有连考科目；";
			String text11 = "3、 准考证没有明确标明先考具体连考科目，一般是先考卷号小的科目，请去对应的考室考试； ";
			String text12 = "4、 连考科目的同学，请告知监考教师，自己有连考科目；交卷时举手告诉监考教师自己须连考的，并告知科目名称；交卷时，请谨记不可走出教室，等待监考教师安排。 ";

//			String xhxm = "照片：" + temp.get("STU_PHOTO") + "　" + "　" + "姓名：" + temp.get("ADMISSION_NAME") + "　" + "　"
//					+ "学号：" + temp.get("STU_NUMBER") + "　" + "　" + "考点：" + temp.get("EXAM_POINT_NAME") + "　" + "　"
//					+ "考点地址：" + temp.get("EXAM_ADDRESS");
			String xhxm =  "姓名：" + temp.get("ADMISSION_NAME") + "　" + "\n"
					+ "学号：" + temp.get("STU_NUMBER") + "　" + "\n" + "考点：" + temp.get("EXAM_POINT_NAME") + "　" + "\n"
					+ "考点地址：" + temp.get("EXAM_ADDRESS");
			
			String zp = ObjectUtils.toString(temp.get("STU_PHOTO"));
			String xm = ObjectUtils.toString(temp.get("ADMISSION_NAME"));
			String xh = ObjectUtils.toString(temp.get("STU_NUMBER"));
			String kd = ObjectUtils.toString(temp.get("EXAM_POINT_NAME"));
			String dz = ObjectUtils.toString(temp.get("EXAM_ADDRESS"));
			
//			Map<String, String> urls = new HashMap<String, String>();
//			urls.put("zp", zp);
//			urls.put("xm", xm);
//			urls.put("xh", xh);
//			urls.put("kd", kd);
//			urls.put("dz", dz);
			
			Paragraph paragraph = new Paragraph(text, font);
			paragraph.setAlignment(1);

			Paragraph paragraph0 = new Paragraph(text0, font);
			paragraph0.setAlignment(1);

			//Paragraph paragraphxx = new Paragraph(xhxm, font3);
			//paragraphxx.setAlignment(1);
			//pdfDocument.add(paragraph0);
			//pdfDocument.add(paragraph0);
			pdfDocument.add(paragraph0);
			pdfDocument.add(paragraph);
			pdfDocument.add(paragraph0);
			//pdfDocument.add(paragraphxx);
			pdfDocument.add(paragraph0);
			
			
			
			float[] widths2 = { 0.1f, 0.1f, 0.1f };
			PdfPTable table2 = new PdfPTable(widths2);
			table2.setWidthPercentage(80);
			table2.setHorizontalAlignment(Element.ALIGN_CENTER);
//			zp = "http://eefile.download.eenet.com//interface/files2/xlims/image/2c711b2b13e06b8eb2449cc0efc6e178.png";
			Object[][] datas = {{zp}, {"姓名：", xm }, { "学号：", xh }, { "考点：", kd }, {"考点地址：", dz } };
			
			for(int i=0;i<datas.length;i++){
				for(int j=0;j<datas[i].length;j++){
					PdfPCell pdfCell = new PdfPCell();
	                pdfCell.setMinimumHeight(30);
	                //pdfCell.setBorder(PdfPCell.NO_BORDER);
	                
	                //合并单元格
	                if(i == 0 && j == 0) {
	                    pdfCell.setRowspan(4);
	                    pdfCell.setColspan(1);
	                    pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	                    pdfCell.setVerticalAlignment(Element.ALIGN_CENTER);
	                }
	                
	                
	                Paragraph paragraph2 = new Paragraph(ObjectUtils.toString(datas[i][j]), f8);
	                pdfCell.setPhrase(paragraph2);
	                                 
	                table2.addCell(pdfCell);
	                
				}
			}
			
			pdfDocument.add(table2);
			
			Paragraph paragraph1 = new Paragraph(text0, font);
			pdfDocument.add(paragraph1);
			

			// 创建一个N列的表格控件
			// PdfPTable pdfTable = new PdfPTable(tableContent.length);
			float[] widths = { 0.2f, 0.1f, 0.1f, 0.2f, 0.2f, 0.2f,0.2f };
			PdfPTable pdfTable = new PdfPTable(widths);
			// 设置表格占PDF文档100%宽度
			pdfTable.setWidthPercentage(80);
			// 水平方向表格控件左对齐
			pdfTable.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
			// 创建一个表格的表头单元格
			PdfPCell pdfTableHeaderCell = new PdfPCell();
			// 设置表格的表头单元格颜色
			pdfTableHeaderCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			for (String tableHeaderInfo : tableContent) {
				pdfTableHeaderCell.setPhrase(new Paragraph(tableHeaderInfo, f8));
				pdfTable.addCell(pdfTableHeaderCell);
			}
			// 创建一个表格的正文内容单元格
			PdfPCell pdfTableContentCell = new PdfPCell();
			pdfTableContentCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			pdfTableContentCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			// 表格内容行数的填充
			for (int i = 0; i < rowsNumber; i++) {
				for (Iterator it = list.iterator(); it.hasNext();) {
					Map next = (Map) it.next();

					String exam_no = (String) next.get("EXAM_NO");
					pdfTableContentCell.setPhrase(new Paragraph(exam_no, f8));
					pdfTable.addCell(pdfTableContentCell);

					String course_name = (String) next.get("COURSE_NAME");
					pdfTableContentCell.setPhrase(new Paragraph(course_name, f8));
					pdfTable.addCell(pdfTableContentCell);

					String exam_type = (String) next.get("EXAM_TYPE");
					pdfTableContentCell.setPhrase(new Paragraph(exam_type, f8));
					pdfTable.addCell(pdfTableContentCell);

					String exam_style = (String) next.get("EXAM_STYLE");
					pdfTableContentCell.setPhrase(new Paragraph(exam_style, f8));
					pdfTable.addCell(pdfTableContentCell);

					String exam_date = (String) next.get("EXAM_DATE");
					pdfTableContentCell.setPhrase(new Paragraph(exam_date, f8));
					pdfTable.addCell(pdfTableContentCell);

					String exam_time = (String) next.get("EXAM_TIME");
					pdfTableContentCell.setPhrase(new Paragraph(exam_time, f8));
					pdfTable.addCell(pdfTableContentCell);

					String seat_no = (String) next.get("SEAT_NO");
					pdfTableContentCell.setPhrase(new Paragraph(seat_no, f8));
					pdfTable.addCell(pdfTableContentCell);

					// 表格内容每写满某个数字的行数时，其内容一方面写入物理文件，另一方面释放内存中存留的内容。
					if ((i % submitAmount) == 0) {
						pdfDocument.add(pdfTable);
						pdfTable.deleteBodyRows();
					} else if (i == rowsNumber) {
						// 如果全部类容完毕且又没达到某个行数限制，则也要写入物理文件中。
						pdfDocument.add(pdfTable);
						pdfTable.deleteBodyRows();
					}
				}
			}

			Paragraph paragraph3 = new Paragraph(text3, font2);
			Paragraph paragraph4 = new Paragraph(text4, font2);
			Paragraph paragraph13 = new Paragraph(text13, font2);
			Paragraph paragraph5 = new Paragraph(text5, font2);
			Paragraph paragraph6 = new Paragraph(text6, font2);
			Paragraph paragraph7 = new Paragraph(text7, font2);
			Paragraph paragraph8 = new Paragraph(text8, font2);
			Paragraph paragraph9 = new Paragraph(text9, font2);
			Paragraph paragraph10 = new Paragraph(text10, font2);
			Paragraph paragraph11 = new Paragraph(text11, font2);
			Paragraph paragraph12 = new Paragraph(text12, font2);

			paragraph3.setIndentationLeft(52);
			paragraph4.setIndentationLeft(52);
			paragraph13.setIndentationLeft(52);
			paragraph5.setIndentationLeft(52);
			paragraph6.setIndentationLeft(52);
			paragraph7.setIndentationLeft(52);
			paragraph8.setIndentationLeft(52);
			paragraph9.setIndentationLeft(52);
			paragraph10.setIndentationLeft(52);
			paragraph11.setIndentationLeft(52);
			paragraph12.setIndentationLeft(52);

			pdfDocument.add(paragraph3);
			pdfDocument.add(paragraph4);
			pdfDocument.add(paragraph13);
			pdfDocument.add(paragraph5);
			pdfDocument.add(paragraph6);
			pdfDocument.add(paragraph7);
			pdfDocument.add(paragraph8);
			pdfDocument.add(paragraph9);
			pdfDocument.add(paragraph10);
			pdfDocument.add(paragraph11);
			pdfDocument.add(paragraph12);

			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("pdf file: " + e.getMessage());
			return false;
		} catch (DocumentException e) {
			e.printStackTrace();
			System.err.println("document: " + e.getMessage());
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("pdf font: " + e.getMessage());
			return false;
		} finally {
			// 关闭PDF文档流，OutputStream文件输出流也将在PDF文档流关闭方法内部关闭
			if (pdfDocument != null) {
				pdfDocument.close();
			}
		}

	}

}
