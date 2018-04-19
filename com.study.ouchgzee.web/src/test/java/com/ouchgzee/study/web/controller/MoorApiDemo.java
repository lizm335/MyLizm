package com.ouchgzee.study.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * 7moor 七陌 接口测试<br/>
 * https://developer.7moor.com<br/>
 */
public class MoorApiDemo {
	private static final String account = "N00000009991";//替换为您的账户
	private static final String secret = "5fdb6380-02e1-11e7-a6f4-5f4d6bb88e4b";//替换为您的api密码
	private static final String host = "http://apis.7moor.com";
	private static Log logger = LogFactory.getLog(MoorApiDemo.class);
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		queryUserState("8000");
		checkTokenLegal("8000", "123456");
	}

	/**
	 * 校验token接口
	 * @param token
	 * @param tokenId
	 * @return
	 */
	public static String checkTokenLegal(String token, String tokenId) {
		String time = getDateTime();
		String sig = md5(account + secret + time);
		//查询坐席状态接口
		String interfacePath = "/v20160818/sso/checkTokenLegal/";
		String url = host + interfacePath + account + "?sig=" + sig;
		String auth = base64(account + ":" + time);
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.build();
		HttpPost post = new HttpPost(url);
		post.addHeader("Accept", "application/json");
		post.addHeader("Content-Type","application/json;charset=utf-8");
		post.addHeader("Authorization",auth);
		CloseableHttpResponse response = null;
		try {
			StringEntity requestEntity = null;
			//根据需要发送的数据做相应替换
			requestEntity = new StringEntity("{\"token\":\"" + token + "\",\"tokenId\":\"" + tokenId + "\"}","UTF-8");
			post.setEntity(requestEntity);
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity,"utf8");
			logger.info("the response is : " + result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null){
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	/**
	 * 查询坐席状态接口
	 * @param exten
	 * @return
	 */
	public static String queryUserState(String exten) {
		String time = getDateTime();
		String sig = md5(account + secret + time);
		//查询坐席状态接口
		String interfacePath = "/v20160818/user/queryUserState/";
		String url = host + interfacePath + account + "?sig=" + sig;
		String auth = base64(account + ":" + time);
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.build();
		HttpPost post = new HttpPost(url);
		post.addHeader("Accept", "application/json");
		post.addHeader("Content-Type","application/json;charset=utf-8");
		post.addHeader("Authorization",auth);
		CloseableHttpResponse response = null;
		try {
			StringEntity requestEntity = null;
			//根据需要发送的数据做相应替换
			requestEntity = new StringEntity("{\"exten\":\"" + exten + "\"}","UTF-8");
			post.setEntity(requestEntity);
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity,"utf8");
			logger.info("the response is : " + result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null){
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}


	public static String md5 (String text) {
		return DigestUtils.md5Hex(text).toUpperCase();
	}

	public static String base64 (String text) {
		byte[] b = text.getBytes();
		Base64 base64 = new Base64();
		b = base64.encode(b);
		String s = new String(b);
		return s;
	}

	public static String getDateTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}
}
