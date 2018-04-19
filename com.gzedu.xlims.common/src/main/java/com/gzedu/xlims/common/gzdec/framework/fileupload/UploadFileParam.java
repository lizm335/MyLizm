package com.gzedu.xlims.common.gzdec.framework.fileupload;

import java.net.URLEncoder;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.gzedu.xlims.common.gzdec.framework.util.EncryptUtil;
import com.gzedu.xlims.common.gzdec.framework.util.UrlToXml;

public class UploadFileParam {
	/**
	 * 应用名:属性文件配置
	 */
	private String app = "ouchgzee";
	/**
	 * 模块名
	 */
	private String module = DateFormatUtils.format(new Date(), "yyyyMMdd");
	/**
	 * 模块ID
	 */
	private String moduleId;
	/**
	 * 回调页面:callbackUrl 上传成功后返回的页面
	 */
	private String url;
	/**
	 * 上传类型：http、ftp 当前支持类型为http
	 */
	private String type = "http";
	/**
	 * 页面加载方式：iframe、windowopen 建议windowopen方式弹出
	 */
	private String openType = "windowopen";
	/**
	 * 回调页面将接受该参数值 为上传文件后的路径 可为空
	 */
	private String rtpath = "";
	/**
	 * 表单元素值 唯一ID 回调页面接受路径信息后可以将路径值赋给改表单元素
	 */
	private String rtid = "";
	/**
	 * 新增
	 */
	private String con = "a";
	/**
	 * //size=100 --限制的文件大小 单位为KB
	 */
	private int size = 2048;
	/**
	 * //types=doc,txt,zip // --限制的文件类型 以,分隔
	 */
	private String[] types = new String[] { "bmp", "jpeg", "jpg", "gif", "psd", "png", "doc", "txt", "zip", "pdf" };

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	public String getRtpath() {
		return rtpath;
	}

	public void setRtpath(String rtpath) {
		this.rtpath = rtpath;
	}

	public String getRtid() {
		return rtid;
	}

	public void setRtid(String rtid) {
		this.rtid = rtid;
	}

	public String getCon() {
		return con;
	}

	public void setCon(String con) {
		this.con = con;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String[] getTypes() {
		return types;
	}

	public void setTypes(String[] types) {
		this.types = types;
	}

	@Override
	public String toString() {
		StringBuffer url = new StringBuffer();
		url.append("app=").append(this.app);
		url.append("&module=").append(this.module);
		url.append("&moduleid=").append(this.moduleId);
		url.append("&type=").append(this.type);
		url.append("&url=").append(this.url);
		url.append("&opentype=").append(this.openType);
		url.append("&rtpath=");
		url.append("&rtid=").append(this.rtid);
		url.append("&con=").append(this.con);
		url.append("&size=").append(this.size);
		url.append("&types=").append(StringUtils.join(this.types, ","));

		String urlXml = null, urlData = null;
		try {
			urlXml = UrlToXml.eUploadUrlToMap(url.toString());
			urlData = URLEncoder.encode(EncryptUtil.encrypt(urlXml), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return urlData;

	}

}
