package com.gzedu.xlims.common.gzedu;

import java.util.HashMap;
import java.util.Map;

import com.gzedu.xlims.common.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;
/** 
 * 新增督促提醒
 * 调用新个人中心平台提醒功能接口 
 * @author wc 
 */
public class sendSmsTeacher {
private static final Logger logger = LoggerFactory.getLogger(EEUtils.class);
	
	
	private static final String sms_url = "http://oclass.gzedu.com/app/teacher/sendSms.do";
	/**
	 * sendAccount  发送方
	 * 
	 * to_account 消息接收方用户列表
	 * 
	 * msgContent 消息内容
	 */
	public static String sendEEMessage(String sendAccount, String to_account,String msgContent,String type) {
		try {
			JSONObject json = new JSONObject();
			Map<String, Object> params = new HashMap<String,Object>();
			
			json.put("formMap.TCHR_ID", sendAccount);
			json.put("formMap.USER_IDS", to_account);
//			json.put("MsgType", ee_tesxt_msgType);
//			json.put("formMap.SMS_CONTENT", msgContent);
//			json.put("APP_ID", ee_app_id);
			json.put("formMap.TIPS_TYPE", type);
			System.out.println("参数json："+json.toString());		
			params.put("data", json.toString());
			String rspXML = HttpClientUtils.doHttpGet(sms_url, params, 2000, "UTF-8");
			//System.out.println("rspXML:"+rspXML);
			logger.info("发送短信返回: {}", rspXML);
			if(!"".equals(rspXML) && rspXML!=null){
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
}
