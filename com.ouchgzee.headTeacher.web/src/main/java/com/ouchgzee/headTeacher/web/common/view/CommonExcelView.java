package com.ouchgzee.headTeacher.web.common.view;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.servlet.view.AbstractView;

/**
 * 通用的excel视图,统一excel导出规范
 * @author 欧集红 
 * @Date 2018年4月16日
 * @version 1.0
 * 
 */
public class CommonExcelView extends AbstractView{
	
	private Workbook workbook;//提供工作薄
	
	private String fileName;//下载名称，不需要后缀
	
	private Collection<?> dataCollection;//数据
	
	private Class<?> convertType;//转换的对象类型
	
	/** The content type for an Excel response */
	private static final String CONTENT_TYPE = "application/vnd.ms-excel;charset=utf-8";
	
	
	/**
	 * 兼容做法，直接使用产生的工作薄
	 * @param workbook
	 * @param fileName
	 */
	public CommonExcelView(Workbook workbook, String fileName) {
		setContentType(CONTENT_TYPE);
		this.workbook = workbook;
		this.fileName = fileName;
	}
	
	/**
	 * 数据和类型保持一致
	 * @param dataCollection
	 * @param convertType
	 */
	public <T> CommonExcelView(Collection<T> dataCollection, Class<T> convertType) {
		setContentType(CONTENT_TYPE);
		this.dataCollection = dataCollection;
		this.convertType = convertType;
	}
	
	
	/**
	 * 数据和类型保持一致
	 * @param dataCollection
	 * @param convertType
	 * @param fileName
	 */
	public <T> CommonExcelView(Collection<T> dataCollection, Class<T> convertType, String fileName) {
		setContentType(CONTENT_TYPE);
		this.fileName = fileName;
		this.dataCollection = dataCollection;
		this.convertType = convertType;
	}
	
	
	
	@Override
	protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
		super.prepareResponse(request, response);
		//附加响应头
		if(this.fileName != null) {
			//自动识别后缀
			String unwrapName = this.fileName.replaceAll("\\.xls\\s*$|\\.xlsx\\s*$", "");
			if(this.workbook instanceof XSSFWorkbook) {
				unwrapName = unwrapName + ".xlsx";//数据行数不限量
			}else {
				unwrapName = unwrapName + ".xls";
			}
			
			
			try {
				//待参考方法：StringUtils.getBrowserStr 
				String encodedName = new String(unwrapName.getBytes(), "ISO-8859-1");
				response.setHeader("Content-Disposition","attachment; filename=" + encodedName);
			} catch (UnsupportedEncodingException e) {
				logger.error("文件名编码失败", e);
			}
			
		}
	}
	

	/* (non-Javadoc)
	 * 表示下载内容
	 * @see org.springframework.web.servlet.view.AbstractView#generatesDownloadContent()
	 */
	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	/**
	 * Renders the Excel view, given the specified model.
	 */
	@Override
	protected final void renderMergedOutputModel(
			Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if(this.workbook  == null) {
			this.workbook  = new HSSFWorkbook();
			logger.debug("Created Excel Workbook from scratch");
			//写入数据
			if(this.dataCollection != null && this.convertType != null) {
				ExcelFieldProcessor excelFieldProcessor = new ExcelFieldProcessor(this.workbook, this.convertType);
				excelFieldProcessor.process(dataCollection);
				excelFieldProcessor.autoSizeColumn();//可选操作
			}
		}
		
		 
		// Set the content type.
		response.setContentType(getContentType());

		// Should we set the content length here?
		// response.setContentLength(workbook.getBytes().length);

		// Flush byte array to servlet output stream.
		ServletOutputStream out = response.getOutputStream();
		workbook.write(out);
		out.flush();
		
	}

	
	
}
