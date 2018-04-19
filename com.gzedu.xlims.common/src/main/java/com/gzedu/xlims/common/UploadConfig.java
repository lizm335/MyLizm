/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common;

import com.gzedu.xlims.common.gzdec.framework.fileupload.UploadFileParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * 功能说明：头像上传
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月13日
 * @version 2.5
 * @since JDK1.7
 *
 */
public class UploadConfig {

	/**
	 * 头像上传服务器回调本项目的地址
	 */
	public final static String HEAD_IMG_TRANSFER = "/upload/head";
	
	/**
	 * 论文上传服务器回调本项目的地址
	 */
	public final static String THESIS_TRANSFER = "/upload/thesis";

	/**
	 * 生成回调URL
	 * 
	 * @param request
	 * @return
	 */
	public static String createUploadUrl(HttpServletRequest request) {
		String serverName = request.getServerName();
		String ctx = request.getContextPath();
		int port = request.getServerPort();

		String callbackUrl = "http://" + serverName + ":" + port + ctx + UploadConfig.HEAD_IMG_TRANSFER;

		UploadFileParam uploadFileParam = new UploadFileParam();
		uploadFileParam.setUrl(callbackUrl);
		String urldata = uploadFileParam.toString();

		String path = AppConfig.getProperty("file.upload.url.action") + "?formMap.urldata=" + urldata;

		return path;
	}

	public static String createUploadUrl(HttpServletRequest request, String callback) {
		String serverName = request.getServerName();
		String ctx = request.getContextPath();
		int port = request.getServerPort();

		String callbackUrl = "http://" + serverName + ":" + port + ctx + callback;

		UploadFileParam uploadFileParam = new UploadFileParam();
		uploadFileParam.setUrl(callbackUrl);
		uploadFileParam.setTypes(new String[]{"doc"});
		String urldata = uploadFileParam.toString();

		String path = AppConfig.getProperty("file.upload.url.action") + "?formMap.urldata=" + urldata;

		return path;
	}

}
