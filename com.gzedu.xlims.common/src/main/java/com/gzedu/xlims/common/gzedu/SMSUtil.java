package com.gzedu.xlims.common.gzedu;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.HttpClientUtils;

/**
 * 短信工具类
 * 
 * @author Administrator
 * 
 */
public class SMSUtil {
	// 手机发送短信
	// private static final String pwd = AppConfig.getProperty("sms.pwd");
	// private static final String account =
	// AppConfig.getProperty("sms.account");
	// private static final String url = AppConfig.getProperty("sms.url");
	// private static final String sign = AppConfig.getProperty("sms.sign");
	// private static final String subcode =
	// AppConfig.getProperty("sms.subcode");
	private static final Logger log = LoggerFactory.getLogger(SMSUtil.class);

	// 云之讯---->
	private static final String sms_url = "http://eesms.gzedu.com/sms/sendSms.do";
	private static final String lgzz_sms_signature_id = "83c53152ac10a57e01b67f741d994f27";
	private static final String lgzz_sms_app_id = "ace2b23c7ff444168952228b5fd5b451";
	private static final String gk_sms_signature_id = "836b33b9ac10a57e0069b332810c9a2e";
	private static final String gk_sms_app_id = "a775aba4302347f1b0766e78049ad240";

	// 新短信接口 2017年2月22日 17:38:38
	private static final String sms_template_url = "http://eesms.gzedu.com/sms/sendYunSms.do";
	private static final String gk_sms_template_id = "83c22dd1ac10a57e01a758cbdfcc46c0"; // 国开发送验证码的模板

	// 学员报读短信模板
	private static final String lgzz_signup_sms_template_id = "52110020";
	private static final String gk_signup_sms_template_id = "304654";
	// 班主任短信模板
	private static final String sms_template_id = "52110224";

	// <----云之讯

	public static int sendMessage(String phones, String content, String type) {
		String devMode = null;
		try {
			devMode = AppConfig.getProperty("devMode");
		} catch (Exception e) {
			// TODO: handle exception
		}
		if ("1".equals(devMode)) {
			return -1; // 测试环境下不发送短信
		}
		try {
			if (StringUtils.isBlank(phones)) {
				return 0;
			}
			String params = null;
			if ("lgzz".equals(type)) {
				params = buildSMSContent(lgzz_sms_app_id, lgzz_sms_signature_id, phones, content);
			} else if ("gk".equals(type)) {
				params = buildSMSContent(gk_sms_app_id, gk_sms_signature_id, phones, content);
			} else {
				return 0;
			}
			String rspXML = HttpClientUtils.doHttpPostXml2(sms_url, params, 10000, "UTF-8");
			log.info("发送短信返回: {}", rspXML);
			return rspXML.contains("<tranceData><result><![CDATA[1]]></result></tranceData>") ? 1 : 0;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return 0;
		}
	}

	/*
	 * @Deprecated private static Map<String, Object> buildSMS(String phones,
	 * String content) { Map<String, Object> sms = new HashMap<String,
	 * Object>(); Map<String, Object> smsXML = new HashMap<String, Object>();
	 * sms.put("account", account); sms.put("password", pwd); sms.put("phones",
	 * phones); sms.put("content", content); sms.put("sign", sign);
	 * sms.put("subcode", subcode); String params =
	 * AnalyXmlUtil.createSJXml(sms); smsXML.put("message", params); return
	 * smsXML; }
	 */

	private static String buildSMSContent(String appId, String signatureId, String phones, String content) {
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version='1.0' encoding='UTF-8'?>");
		xml.append("<tranceData>");
		xml.append("<MOBILE><![CDATA[");
		xml.append(phones);
		xml.append("]]></MOBILE>");
		xml.append("<SMS_SIGNATURE_ID><![CDATA[");
		xml.append(signatureId);
		xml.append("]]></SMS_SIGNATURE_ID>");
		xml.append("<SMS_APP_ID><![CDATA[");
		xml.append(appId);
		xml.append("]]></SMS_APP_ID>");
		xml.append("<SMS_CONTENT><![CDATA[");
		xml.append(content);
		xml.append("]]></SMS_CONTENT>");
		xml.append("</tranceData>");
		return xml.toString();
	}

	/**
	 * 发送验证码模板短信
	 * 
	 * @param phones
	 * @param code
	 * @param type
	 * @return
	 */
	public static int sendTemplateMessageCode(String phones, String code, String type) {
		/*
		 * String content = code;
		 * 
		 * if ("lgzz".equals(type)) { return
		 * sendTemplateMessage(lgzz_sms_app_id, lgzz_sms_template_id, phones,
		 * content); } else
		 */
		String devMode = AppConfig.getProperty("devMode");
		if ("1".equals(devMode)) {
			log.info("测试环境已屏蔽发送短信，发送信息：" + code);
			return 1; // 测试环境下不发送短信
		}
		
		if ("gk".equals(type)) {
			return sendTemplateMessage(gk_sms_app_id, gk_sms_template_id, phones, code);
		}
		return 0;
	}

	/**
	 * 发送模板短信
	 * 
	 * @param appId
	 * @param templateId
	 * @param phones
	 * @param code
	 * @return
	 */
	private static int sendTemplateMessage(String appId, String templateId, String phones, String code) {
		if (StringUtils.isBlank(phones)) {
			return 0;
		}
		try {
			String params = buildSMSTemplateContent(appId, templateId, phones, code);
			log.info("发送短信参数:appId={}, templateId={},phones={},code={}", appId, templateId, phones, code);
			String resultJson = HttpClientUtils.doHttpPostXml2(sms_template_url, params, 10000, "UTF-8");
			log.info("发送短信返回: {}", resultJson);
//			JSONObject ob = JSONObject.fromObject(resultJson);
//			String result = (String) ob.get("RESULT");
			return resultJson.contains("<tranceData><result><![CDATA[1]]></result></tranceData>") ? 1 : 0;
		} catch (Exception e) {
			log.error(e.getMessage());
			return 0;
		}
	}

	private static String buildSMSTemplateContent(String appId, String templateId, String phones, String params) {
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version='1.0' encoding='UTF-8'?>");
		xml.append("<tranceData>");
		xml.append("<MOBILE><![CDATA[");
		xml.append(phones);
		xml.append("]]></MOBILE>");
		xml.append("<SMS_TEMPLATE_ID><![CDATA[");
		xml.append(templateId);
		xml.append("]]></SMS_TEMPLATE_ID>");
		xml.append("<SMS_APP_ID><![CDATA[");
		xml.append(appId);
		xml.append("]]></SMS_APP_ID>");
		xml.append("<PARAMS><![CDATA[");
		xml.append(params);
		xml.append("]]></PARAMS>");
		xml.append("</tranceData>");
		return xml.toString();
	}

	public static int sendMessageSignup(String phones, String content, String type) {
		if (StringUtils.isBlank(phones)) {
			return 0;
		}
		log.info("给{}发送短信", phones);
		try {
			String params = null;
			if ("lgzz".equals(type)) {
				params = buildLgzzSMSTemplateContent(phones, content);
			} else if ("gk".equals(type)) {
				params = buildGKSMSTemplateContent(phones, content);
			} else {
				return 0;
			}
			String resultJson = HttpClientUtils.doHttpPostXml2(sms_template_url, params, 10000, "UTF-8");
			log.info("发送短信返回: {}", resultJson);
			return resultJson.contains("<tranceData><result><![CDATA[1]]></result></tranceData>") ? 1 : 0;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return 0;
		}
	}

	/**
	 * 您的学习帐号是{1}，密码为：{2}，请登录{3}及时确认您的报读信息并开始学习，如有问题，请联系{4}
	 *
	 * @param phones
	 * @param params
	 * @return
	 */

	private static String buildLgzzSMSTemplateContent(String phones, String params) {
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version='1.0' encoding='UTF-8'?>");
		xml.append("<tranceData>");
		xml.append("<MOBILE><![CDATA[");
		xml.append(phones);
		xml.append("]]></MOBILE>");
		xml.append("<SMS_TEMPLATE_ID><![CDATA[");
		xml.append(lgzz_signup_sms_template_id);
		xml.append("]]></SMS_TEMPLATE_ID>");
		xml.append("<SMS_APP_ID><![CDATA[");
		xml.append(lgzz_sms_app_id);
		xml.append("]]></SMS_APP_ID>");
		xml.append("<PARAMS><![CDATA[");
		xml.append(params);
		xml.append("]]></PARAMS>");
		xml.append("</tranceData>");
		return xml.toString();
	}

	private static String buildGKSMSTemplateContent(String phones, String params) {
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version='1.0' encoding='UTF-8'?>");
		xml.append("<tranceData>");
		xml.append("<MOBILE><![CDATA[");
		xml.append(phones);
		xml.append("]]></MOBILE>");
		xml.append("<SMS_TEMPLATE_ID><![CDATA[");
		xml.append(gk_signup_sms_template_id);
		xml.append("]]></SMS_TEMPLATE_ID>");
		xml.append("<SMS_APP_ID><![CDATA[");
		xml.append(gk_sms_app_id);
		xml.append("]]></SMS_APP_ID>");
		xml.append("<PARAMS><![CDATA[");
		xml.append(params);
		xml.append("]]></PARAMS>");
		xml.append("</tranceData>");
		return xml.toString();
	}

	/**
	 * 调用接口平台参数
	 *
	 * @param phones
	 * @param content
	 * @return
	 */
	public static String sendMessageHeardTeacher(String phones, String content) {
		log.info("给{}发送短信", phones);
		try {
			String params = templateContent(phones, content);
			String resultJson = HttpClientUtils.doHttpPostXml2(sms_template_url, params, 10000, "UTF-8");
			log.info("发送短信返回: {}", resultJson);
			// JSONObject json = JSONObject.fromObject(resultJson);
			return resultJson;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return "0";
		}
	}

	/**
	 * 班主任短信发送参数拼接
	 *
	 * @param phones
	 * @param params
	 * @return
	 */
	private static String templateContent(String phones, String params) {
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version='1.0' encoding='UTF-8'?>");
		xml.append("<tranceData>");
		xml.append("<MOBILE><![CDATA[");
		xml.append(phones);
		xml.append("]]></MOBILE>");
		xml.append("<SMS_TEMPLATE_ID><![CDATA[");
		xml.append(sms_template_id);
		xml.append("]]></SMS_TEMPLATE_ID>");
		xml.append("<SMS_APP_ID><![CDATA[");
		xml.append(gk_sms_app_id);
		xml.append("]]></SMS_APP_ID>");
		xml.append("<PARAMS><![CDATA[");
		xml.append(params);
		xml.append("]]></PARAMS>");
		xml.append("</tranceData>");
		return xml.toString();
	}

	public static void main(String[] args) {
		String content = String.format("%s,%s,%s,%s,%s%s", "黄黄黄", "18826489528", "888888", "http://www.oucgz.cn ",
				StringUtils.isNotBlank("18826489528") ? "学习中心：" + "18826489528" + "，或" : "",
				"统一教育服务热线：020-969300，祝学习愉快。");
		int smsResult = SMSUtil.sendMessageSignup("18826489528", content, "gk");
		System.out.println(smsResult);
		
//		String messageCode = sendTemplateMessageCode("18826489528", "123468", "gk");
//		System.out.println(messageCode);
		
		// String params = "大帅哥,小帅哥";
		// sendMessageHeardTeacher("18826489528", params);
		// System.out.println(params);

//		int smsResult2 = SMSUtil.sendMessage("18826489528", "帅哥干哈呢？", "gk");
//		System.out.println(smsResult2);
	}

}
