package com.gzedu.xlims.common.gzedu;

import java.util.HashMap;
import java.util.Map;

import com.gzedu.xlims.common.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

/**
 * EE提醒工具类
 * 
 */
public class EEUtils {

	private static final Logger logger = LoggerFactory.getLogger(EEUtils.class);

	// private static final String ee_url_cs =
	// "http://eechat.gzedu.com/message/management//batchSendMsg.do";

	private static final String ee_url = "http://eechat.tt.gzedu.com/message/management//batchSendMsg.do";

	private static final String ee_app_id = "APP038";

	private static final String ee_tesxt_msgType = "TIMTextElem"; // 文本消息
	// private static final String ee_face_msgType = "TIMFaceElem";//表情消息
	// private static final String ee_location_msgType =
	// "TIMLocationElem";//位置消息
	// private static final String ee_custom_msgType = "TIMCustomElem";//自定义消息

	/**
	 * sendAccount 发送方
	 * 
	 * to_account 消息接收方用户列表
	 * 
	 * msgContent 消息内容
	 */
	public static String sendEEMessage(String sendAccount, String to_account, String msgContent) {
		try {
			JSONObject json = new JSONObject();
			Map<String, Object> params = new HashMap<String, Object>();

			json.put("sendAccount", sendAccount);
			json.put("To_Account", to_account);
			json.put("MsgType", ee_tesxt_msgType);
			json.put("MsgContent", msgContent);
			json.put("APP_ID", ee_app_id);
			System.out.println("参数json：" + json.toString());
			params.put("data", json.toString());
			String rspXML = HttpClientUtils.doHttpGet(ee_url, params, 2000, "UTF-8");
			// System.out.println("rspXML:"+rspXML);
			logger.info("发送短信返回: {}", rspXML);
			if (!"".equals(rspXML) && rspXML != null) {
				if ("OK".equals(JSONObject.fromObject(rspXML).get("resultStatus"))) {
					return "1";
				}
			}
			return "0";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return "0";
		}
	}

	public static void main(String[] args) {
		String sendAccount = "1b0f10dd00fe4cd3a7a77f1817a7b334";
		String to_account = "4e7ed0a47f00000122d555b2f2574bb2";
		String msgContent = "测试";
		System.out.println(sendEEMessage(sendAccount, to_account, msgContent));

	}

	/**
	 * 
	 * @param sendAccount
	 * @param to_account
	 * @param msgContent
	 * @param appId
	 * @return
	 */
	public static String sendEE(String sendAccount, String to_account, String msgContent, String appId,
			String eeServeUrl) {
		String url = eeServeUrl + "/message/management/sendMessage.do";
		JSONObject json = new JSONObject();
		Map<String, Object> params = new HashMap<String, Object>();
		json.put("SEND_ID", sendAccount);
		json.put("RECEIVERS", to_account);
		json.put("CONTENT", msgContent);
		json.put("APP_ID", appId);
		System.out.println("参数json：" + json.toString());
		params.put("data", json.toString());
		String result = HttpClientUtils.doHttpGet(url, params, 2000, "UTF-8");
		if (!"".equals(result) && result != null) {
			if ("OK".equals(JSONObject.fromObject(result).get("resultStatus"))) {
				return "1";
			}
		}
		return "0";
	}
}
