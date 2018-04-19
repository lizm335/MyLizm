package com.gzedu.xlims.common.gzedu;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.HttpClientUtils;

import net.sf.json.JSONObject;

/**
 * 招生接口签名<br/>
 * 由于其他模块要用到，这个类需要迁移到公共模块中
 * 
 * @author Administrator
 */
public class SignUtil {

	public final static String APPID = "YX_1henfyhgjb";
	private final static String APPSECRET = "99g7NTFXYYPVfwPwDIBksHoMrkSM5Ddi";
	
	public final static String TX_APPID = "YX_1hxpe4tppc";//退学接口专用
	private final static String TX_APPSECRET = "SuK6KSg0N9Nl51TF5QseeuNhVwDeRNxp";//退学接口专用
	
	/**
	 * 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
	 * 
	 * @param paraMap
	 * @return
	 */
	public static String formatUrlMap(Map<String, String> paraMap, long time) {
		String buff = "";
		Map<String, String> tmpMap = paraMap;
		// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
		List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
		Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
			@Override
			public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
				return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});

		for (Map.Entry<String, String> item : infoIds) {
			if (StringUtils.isNotEmpty(item.getKey())) {
				String key = item.getKey();
				String val = item.getValue();
				buff += key + "=" + val + "&";
			}
		}
		buff += "appsecret=" + APPSECRET + "&time=" + time;
		System.out.println("参数加密拼接：" + buff);
		return sha1(buff).toUpperCase();

	}
	/**
	 * 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）--针对退学专用
	 * 
	 * @param paraMap
	 * @return
	 */
	public static String formatUrlMapTX(Map<String, String> paraMap, long time) {
		String buff = "";
		Map<String, String> tmpMap = paraMap;
		// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
		List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
		Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
			@Override
			public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
				return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});

		for (Map.Entry<String, String> item : infoIds) {
			if (StringUtils.isNotEmpty(item.getKey())) {
				String key = item.getKey();
				String val = item.getValue();
				buff += key + "=" + val + "&";
			}
		}
		buff += "appsecret=" + TX_APPSECRET + "&time=" + time;
		System.out.println("参数加密拼接：" + buff);
		return sha1(buff).toUpperCase();

	}

	/**
	 * sha1 加密
	 * 
	 * @param str
	 * @return
	 */
	public static String sha1(String str) {
		if (null == str || 0 == str.length()) {
			return "";
		}
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("UTF-8"));

			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] buf = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static void main(String[] args) {
		// 审核记录
		String url = "https://api.emp.eenet.com/Learncenterapi/auditCenter.html";
		Map<String, String> params = new HashMap<String, String>();
		params.put("LEARNCENTER_CODE", "GDSJXJYXHXXZX");// 编码
		params.put("OPERATING_STATE", "Y");// Y通过,E不通过
		params.put("AUDIT_REMARK", "同意");// 备注
		long time = DateUtils.getDate().getTime();

		// 额外参数
		params.put("school_code", "041");// 院校ID
		params.put("sign", formatUrlMap(params, 1511934626921L).toUpperCase());
		params.put("appid", "YX_1henfyhgjb");// APPID不需要参与加密
		params.put("time", String.valueOf(1511934626921L));

		System.out.println("调用接口参数：" + params);

		String result = HttpClientUtils.doHttpPost(url, params, 3000, "utf-8");
		System.out.println(result);
		if (StringUtils.isNotEmpty(result)) {
			JSONObject json = JSONObject.fromObject(result);
		}

	}

}
