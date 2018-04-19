package com.gzedu.xlims.third.sms.jiguang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.gzedu.xlims.common.Encodes;
import com.gzedu.xlims.common.dao.Collections3;
import com.gzedu.xlims.third.sms.SmsSender;

public class JiGuanSender implements SmsSender {
	
	
	private Logger logger = LoggerFactory.getLogger(JiGuanSender.class);
	
	private JiGuangConfig config;

	@Override
	public String send(Map<String, Object> params) { 
		String alert = (String) params.get("alert");
		if(StringUtils.isEmpty(alert)) {
			logger.info("alert不能为空。 ");
			return null;
		}
		String title = (String) params.get("title");
		/*if(Strings.isEmpty(title)) {
			logger.info("title不能为空。 ");
			return null;
		} */
		String tag = (String) params.get("tag");
		List<String> aliasList = (ArrayList<String>) params.get("alias");
		if(StringUtils.isEmpty(tag) && Collections3.isEmpty(aliasList)) {
			//jsonObject.put("audience", jiGuangConfig.getAudience());// all
			logger.info("推送对象不能为空。 ");
			//return null;
		}
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		
		HttpPost post = new HttpPost(config.getUrl()); 
		post.addHeader(new BasicHeader("Content-Type", config.getContentType()));
		post.addHeader(new BasicHeader("Authorization", "Basic " + getBase64Auth(config.getAppKey(), 
				config.getMasterSecret())));
		JSONObject jsonObject = new JSONObject();
		// 1. platform:推送平台设置
		jsonObject.put("platform", config.getPlatform());// all
		// 2. audience:推送设备指定
		JSONObject audience = new JSONObject();
		if(StringUtils.isNotEmpty(tag)) {
			audience.put("tag", new String[]{tag});
		}
		if(Collections3.isNotEmpty(aliasList)) {
			audience.put("alias", aliasList);
		}
		jsonObject.put("audience",  audience);//test
		
		// 3. notification	可选	通知内容体。是被推送到客户端的内容。与 message 一起二者必须有其一，可以二者并存
		jsonObject.put("notification", getNotificationJson(alert, title, params));
		// 4.message	可选	消息内容体。是被推送到客户端的内容。与 notification 一起二者必须有其一，可以二者并存
		JSONObject message = getMessageJson(alert, title, getExtras(params));
		jsonObject.put("message", message);
		// options	可选	推送参数
		jsonObject.put("options", getOptionJson());
		logger.info("推送内容:"+jsonObject.toString());
		try {
			post.setEntity(new StringEntity(jsonObject.toString(), "utf-8"));
			HttpResponse response = httpClient.execute(post);
			String body = EntityUtils.toString(response.getEntity());  
			logger.info(body);//- {"sendno":"0","msg_id":"996185457"}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";
	}
	
	private JSONObject getExtras(Map<String,Object> params) {
		JSONObject extras = new JSONObject();
		extras.put("time", params.get("time"));
		extras.put("content", params.get("alert"));
		extras.put("title", params.get("title"));
		extras.put("id", params.get("id"));
		extras.put("type", params.get("type"));
		return extras;
	}
	
	private JSONObject getMessageJson(String alert,String title,Object extras) {
		JSONObject message = new JSONObject();
		message.put( "msg_content",alert);
		message.put( "title",title);
		message.put( "content_type","text");
		message.put( "extras",extras);
		return message;
	}

	private JSONObject getNotificationJson(String alert,String title,Map<String,Object> params) {
		JSONObject notificationJson = new JSONObject();
		notificationJson.put("android", getAndroidJson(alert, title, getExtras(params)));
		notificationJson.put("ios", getIosJson(alert, getExtras(params)));
		return notificationJson;
	}

	private JSONObject getIosJson(String alert,JSONObject extras){
		JSONObject iosJson = new JSONObject();
		iosJson.put("alert", alert);
		iosJson.put("sound", "sound.caf");
		iosJson.put("badge", 1);
		iosJson.put("content-available", true);
		iosJson.put("extras", extras);
		return iosJson;
	}
	
	private JSONObject getOptionJson() {
		JSONObject optionJson = new JSONObject();
		/**
		 * 如果目标平台为 iOS 平台 需要在 options 中通过 apns_production 字段来设定推送环境。True 表示推送生产环境，False 表示要推送开发环境； 如果不指定则为推送生产环境
		 */
		optionJson.put("apns_production", config.isApnsProduction()); 
		return optionJson;
	}
	private JSONObject getAndroidJson(String alert,String title,JSONObject extras) {
		JSONObject androidJson = new JSONObject();
		androidJson.put("alert", alert);
		androidJson.put("title", title);
		androidJson.put("builder_id", 1);
		androidJson.put("extras", extras);
		return androidJson;
	}
	private String getBase64Auth(String appKey,String masterSecret) {
		return Encodes.encodeBase64(new String(appKey+":"+masterSecret).getBytes());
	}

	public JiGuangConfig getConfig() {
		return config;
	}

	public void setConfig(JiGuangConfig config) {
		this.config = config;
	}

}
