/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.third.eechat.util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年8月17日
 * @version 2.5
 *
 */
public class EEIMHttpClientUtil {

	public static String doHttpGet(String url, String data) {
		String responseMsg = "";
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setContentCharset("UTF-8");
		url = url + "?data=" + data;
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		try {
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
			int num = httpClient.executeMethod(getMethod);
			if (num == 200) {
				byte[] responseBody = getMethod.getResponseBodyAsString().getBytes("GBK");
				responseMsg = new String(responseBody);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			getMethod.releaseConnection();
		}
		return responseMsg;
	}

	public static String doHttpPost(String url, String data) {
		String responseMsg = "";
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setContentCharset("UTF-8");
		PostMethod postMethod = new PostMethod(url);

		postMethod.addParameter("data", data);
		try {
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
			int num = httpClient.executeMethod(postMethod);
			if (num == 200) {
				responseMsg = postMethod.getResponseBodyAsString().trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return responseMsg;
	}
	
	public static String doHttpPostNew(String url, String data) {
		String responseMsg = "";
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setContentCharset("UTF-8");
		PostMethod postMethod = new PostMethod(url);

		postMethod.addParameter("data", data);
		postMethod.setRequestHeader("Referer", "http://eechat.tt.gzedu.com");
		try {
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
			int num = httpClient.executeMethod(postMethod);
			if (num == 200) {
				responseMsg = postMethod.getResponseBodyAsString().trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			postMethod.releaseConnection();
		}
		return responseMsg;
	}
}
