/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.ToolUtil;
import com.gzedu.xlims.web.controller.exam.vo.ExportExamStudentRoomService2;

/**
 * 
 * 功能说明：下载模板，模板必需放在excel/model/
 * 
 * @author
 * @Date 2017年3月24日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/excel")
public class ExcelModelController {

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	@ResponseBody
	public void exportPlanAppoinments(String name, HttpServletRequest request, HttpServletResponse response) {
		try {
			InputStream fis = ExportExamStudentRoomService2.class.getResourceAsStream("/excel/model/" + name);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			response.reset();
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(name.getBytes("UTF-8"), "ISO8859-1"));
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/vnd.ms-excel;charset=gb2312");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 文件下载
	 * 
	 * @Description:
	 * @param fileName
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
	public void downloadFile(@RequestParam("fileName") String fileName, HttpServletRequest request,
			HttpServletResponse response) {
		if (StringUtils.isEmpty(fileName))
			return ;
		fileName = request.getSession().getServletContext().getRealPath("") + File.separator + "tmp" + File.separator+ "roll" + File.separator+fileName;
		ToolUtil.download(fileName,response);
	}
}