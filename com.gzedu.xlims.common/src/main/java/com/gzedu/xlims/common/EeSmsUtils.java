package com.gzedu.xlims.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.ObjectUtils;

import com.gzedu.xlims.common.gzedu.AnalyXmlUtil;



public class EeSmsUtils {
	private static final String URL = "http://eesms.gzedu.com";// 平台服务地址
	private static final String SEND_SMS = "/sms/sendSms.do";// 普通短信发送接口地址
	private static final String SEND_YUN_SMS = "/sms/sendYunSms.do";// 模版短信发送接口地址
	private static String SMS_SIGNATURE_ID = "";// 签名id
	private static String SMS_APP_ID = "";// 应用id
	private static String TEMPLATE_ID = "";// 模版id
	private static Map<String, String> signMap = new HashMap<String, String>();// 存放应用id(SMS_APP_ID)、签名id(SMS_SIGNATURE_ID)。

	// 云之讯---->
	private static final String sms_url = "http://eesms.gzedu.com/sms/sendSms.do";
	private static final String lgzz_sms_signature_id = "83c53152ac10a57e01b67f741d994f27";
	private static final String lgzz_sms_app_id = "ace2b23c7ff444168952228b5fd5b451";
	private static final String gk_sms_signature_id = "836b33b9ac10a57e0069b332810c9a2e";
	private static final String gk_sms_app_id = "a775aba4302347f1b0766e78049ad240";
	
	private static final String YZM_SMS_TEMPLATE_ID = "0efa8afdac1082a6459bdb65a65b3fe3";
	private static final String YZM_SMS_APP_ID = "96647447ac1082a631ec0130a292f906";

	// <----云之讯

	public static Map getSmsCode(Map formMap, String... params) throws Exception {
		String postUrl = URL + SEND_YUN_SMS;
		Map resultMap = new HashMap();
		EeSmsUtils utils = new EeSmsUtils();
		StringBuffer otherParam = new StringBuffer();
		for (String string : params) {
			otherParam.append(string);
			otherParam.append(",");
		}
		String result = "failure";
		try {
			String mobiles = ObjectUtils.toString(formMap.get("MOBILE"));
			StringBuffer param = new StringBuffer();
			Random random = new Random();
			for (int i = 0; i < 6; i++) {
				param.append(random.nextInt(10));
			}
			resultMap.put("code", param.toString());
			Map map = new HashMap();
			map.put("MOBILE", mobiles);
			map.put("SMS_TEMPLATE_ID", "716265deac1082a62882545911964048");
			map.put("SMS_APP_ID", "f63e6023ac1082a639341183ebda5e2e");
			map.put("PARAMS", otherParam.toString() + param.toString());
			String xmlString = AnalyXmlUtil.createXml(map, "UTF-8");
			String resultXml = HttpClientUtils.doHttpPostXml3(postUrl, xmlString, 100000, "UTF-8");
			Map rMap = AnalyXmlUtil.parserXml(resultXml);
			result = ObjectUtils.toString(rMap.get("result"));
			if (JsonState.SUCCESS.getValue() == Integer.parseInt(result)) {
				result = JsonState.SUCCESS.getKey();
			} else {
				result = JsonState.ERROR.getKey();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * 获取短信验证码
	 * @param formMap
	 * @return rcode+","+result
	 * @throws Exception
	*/
	public static Map getSmsCodeServc(Map formMap) {
		Map resultMap = new HashMap();
		String result = "failure";
		try {
			Map pMap = new HashMap();
			//接收人
			String to = ObjectUtils.toString(formMap.get("MOBILE"));
			//验证码
			StringBuffer param=new StringBuffer("");  
			String randomCharacter="";
			for (int i = 0; i < 6; i++) {  
				randomCharacter=ObjectUtils.toString(new Random().nextInt(10));  
	            param.append(randomCharacter);  
	        }
	        pMap.put("MOBILE", to);
	        pMap.put("SMS_TEMPLATE_ID", YZM_SMS_TEMPLATE_ID);
	        pMap.put("SMS_APP_ID", YZM_SMS_APP_ID);
	        pMap.put("PARAMS", ObjectUtils.toString(formMap.get("USER_NAME"))+","+param.toString());
	        String xmlStr = AnalyXmlUtil.createXml(pMap, "UTF-8");
	        
	        String resultXml = HttpClientUtils.doHttpPostXml1("http://eesms.gzedu.com/sms/sendYunSms.do", xmlStr, 100*1000, "UTF-8");
	        Map rMap = AnalyXmlUtil.parserXml(resultXml);
			result = ObjectUtils.toString(rMap.get("result"));
			if ("1".equals(result)) {
				result = "success";
				resultMap.put("code", param.toString());
			} else {
				resultMap.put("code", "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resultMap.put("result", result);
		return resultMap;
	}

	public static void main(String[] args) {
		Map formMap = new HashMap();
		formMap.put("MOBILE", "15652332700");
		formMap.put("USER_NAME", "李老师");
		getSmsCodeServc(formMap);
	}

}
