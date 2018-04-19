package com.gzedu.xlims.third.moor;

import com.alibaba.fastjson.JSONObject;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 7moor 七陌客服系统 接口调用<br/>
 * https://developer.7moor.com<br/>
 */
public class MoorApiUtil {
	private static Log logger = LogFactory.getLog(MoorApiUtil.class);

//	private static final String account = "N00000012430";//替换为您的账户
//	private static final String secret = "924322d0-0af8-11e7-b4ac-5367721ade39";//替换为您的api密码
	private static final String host = "http://apis.7moor.com";

	public enum Account {

		// 账号1
		ACCOUNT01("N00000012430", "924322d0-0af8-11e7-b4ac-5367721ade39"),
		// 账号2
		ACCOUNT02("N00000011808", "4447dad0-0a3e-11e8-9c5b-bbbf76a2bd18");

		private String account;

		private String secret;

		Account(String account, String secret) {
			this.account = account;
			this.secret = secret;
		}

	}
	
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
	public static boolean checkTokenLegal(String token, String tokenId) {
		return checkTokenLegal(Account.ACCOUNT01, token, tokenId);
	}

	/**
	 * 校验token接口
	 * @param act
	 * @param token
	 * @param tokenId
	 * @return
	 */
	public static boolean checkTokenLegal(Account act, String token, String tokenId) {
		String time = getDateTime();
		String sig = md5(act.account + act.secret + time);
		//查询坐席状态接口
		String interfacePath = "/v20160818/sso/checkTokenLegal/";
		String url = host + interfacePath + act.account + "?sig=" + sig;
		String auth = base64(act.account + ":" + time);
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
			JSONObject jsonObj = JSONObject.parseObject(result);
			if(jsonObj.getIntValue("code") == 200) {
				logger.info("the response is : " + result);
				return true;
			} else {
				logger.error("the response error is : " + result);
			}
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
		return false;
	}

	/**
	 * 查询坐席状态接口
	 * @param exten
	 * @return
	 */
	public static String queryUserState(String exten) {
		return queryUserState(Account.ACCOUNT01, exten);
	}

	/**
	 * 查询坐席状态接口
	 * @param act
	 * @param exten
	 * @return
	 */
	public static String queryUserState(Account act, String exten) {
		String time = getDateTime();
		String sig = md5(act.account + act.secret + time);
		//查询坐席状态接口
		String interfacePath = "/v20160818/user/queryUserState/";
		String url = host + interfacePath + act.account + "?sig=" + sig;
		String auth = base64(act.account + ":" + time);
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
