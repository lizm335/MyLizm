package com.gzedu.xlims.web.controller.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.constants.WebConstants;

@Controller
@RequestMapping("/excelImport")
public class ExcelImportController {
	
	/**
	 * 返回文件导入框
	 * @param actionName 导入文件的方法
	 * @param fileParamName  接收文件参数名
	 * @param modelName  模块名，用于生成单独的文件夹保存导入结果，默认为"default"
	 * @param downloadFileUrl  下载模板的路径
	 * @param downloadButton  下载模板的按钮内容
	 * @param title  导入框显示的标题
	 * @param step1    第一步的标题
	 * @param step2    第二步的标题
	 * @param step3    第三步的标题
	 * @param content1  第一步的提示内容
	 * @param content2 第二步的提示内容
	 * @param alert  第一步中的标注内容
	 * @return
	 */
	@RequestMapping(value = "/importForm")
	public String importForm(@RequestParam String actionName, @RequestParam String fileParamName,
			@RequestParam String modelName, @RequestParam String downloadFileUrl, @RequestParam String downloadButton,
			@RequestParam String title, @RequestParam String step1, @RequestParam String step2, @RequestParam String step3,
			@RequestParam String content1, @RequestParam String content2, @RequestParam String alert) {
		
		return "excelImport/excel_import_form";
	}

	@RequestMapping(value = "/downloadModel")
	@ResponseBody
	public void downloadModel(@RequestParam String name, HttpServletRequest request, HttpServletResponse response) {
		InputStream fis = null;
		OutputStream toClient = null;
		try {
			fis = ExcelImportController.class.getResourceAsStream(WebConstants.EXCEL_MODEL_URL + name);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			
			response.reset();
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(name.getBytes("UTF-8"), "ISO8859-1"));
			toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/vnd.ms-excel;charset=gb2312");
			toClient.write(buffer);
			toClient.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
			if (toClient != null) {
				try {
					toClient.close();
				} catch (Exception e) {
				}
			}
		}
	}

	@RequestMapping(value = "/downloadResult")
	@ResponseBody
	public void downloadResult(@RequestParam String path, @RequestParam String name, HttpServletRequest request, HttpServletResponse response) {
		InputStream fis = null;
		OutputStream toClient = null;
		try {
			String file = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL + path + File.separator + name;
			fis = new FileInputStream(file);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			
			response.reset();
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(name.getBytes("UTF-8"), "ISO8859-1"));
			toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/vnd.ms-excel;charset=gb2312");
			toClient.write(buffer);
			toClient.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
			if (toClient != null) {
				try {
					toClient.close();
				} catch (Exception e) {
				}
			}
		}
	}

}
