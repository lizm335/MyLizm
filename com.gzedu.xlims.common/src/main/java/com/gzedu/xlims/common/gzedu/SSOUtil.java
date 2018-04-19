package com.gzedu.xlims.common.gzedu;

import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import org.apache.commons.codec.binary.Base64;

import com.gzedu.xlims.common.gzdec.framework.util.NrmsEncrypt;

public class SSOUtil {

	public static final String APP_ID = "com.ouchgzee.gdzgw";

	public static final List<String> APP_ID_LIST = new ArrayList<String>();
	
	public static final String KEY = "0d3f912ef8d1429bb14522f8c3ce742e";
	
	static {
		APP_ID_LIST.add("com.ouchgzee.gdzgw"); // 兼容之前的数据
		APP_ID_LIST.add("APP001"); // 教学教务组织平台
		APP_ID_LIST.add("APP002"); // 班主任平台
		APP_ID_LIST.add("APP003"); // 招生平台
		APP_ID_LIST.add("APP005"); // 运营平台
		APP_ID_LIST.add("APP006"); // 微信公众号
		APP_ID_LIST.add("APP007"); // 学习空间
		APP_ID_LIST.add("APP008"); // 备用...
		APP_ID_LIST.add("APP009"); // 备用...
		APP_ID_LIST.add("APP010"); // 备用...
	}

	
	/**
	 * 获取单点登录参数。
	 * @param xh 学号
	 * @return String
	 */
	public static String getSignOnParam(String xh) {
		return encrypt(APP_ID, xh);
	}
	
	/**
	 * 加密单点登录参数。
	 * 
	 * @param appId
	 * @param xh
	 * @return
	 */
	public static String encrypt(String appId, String xh) {
		long timestamp = System.currentTimeMillis();
		String validateCode = NrmsEncrypt.encrypt(appId + xh + timestamp);
		
		StringBuffer sb = new StringBuffer(100);
		sb.append(appId);
		sb.append(",");
		sb.append(xh);
		sb.append(",");
		sb.append(timestamp);
		sb.append(",");
		sb.append(validateCode);
		
		return Base64.encodeBase64String(sb.toString().getBytes()).replace("\r\n", "");
	}
	
	public static String getOneParam(String encyStr) {
		return parseSignOnParam(encyStr).get("xh");
	}
	
	/**
	 * 解析单点登录参数。
	 * 
	 * @param encyStr
	 * @return
	 */
	public static Map<String, String> parseSignOnParam(String encyStr) {
		String str = new String(Base64.decodeBase64(encyStr)); // Base64解码
		String[] params = str.split(","); 
		Map<String, String> m = new HashMap<String, String>();
		if(params.length==4) {
			m.put("app_id", params[0]);
			m.put("xh", params[1]);
			m.put("timestamp", params[2]);
			m.put("validate_code", params[3]);
		}
		return m;
	}
	
	/**
	 * 校验签名
	 * @param encyStr
	 * @return
	 */
	public static boolean verifySign(String encyStr) {
		Map<String, String> param = parseSignOnParam(encyStr); // 解析参数
		// 参数验证
		String appId = param.get("app_id");
		String md5Str = NrmsEncrypt.encrypt(appId + param.get("xh") + param.get("timestamp"));
		if (md5Str.equals(param.get("validate_code"))) {
			return APP_ID_LIST.contains(appId);
		}
		return false;
	}
	
	public static void main(String[] args) {
		/*String xh = "ls001";
		SSOUtil sso = new SSOUtil();
		String p = sso.encrypt(SSOUtil.APP_ID, xh);
		System.out.println("http://localhost:5050/pcenter/api/sso/signon.do?p=" + p);*/
		String str="QVBQMDA1LG85STV4c3hQc0E5a3YybXZkQkVYWUdMdDFwaGcxMiwxNTExNDkxNzE4OTU0LDY1Qjk2MDMzQjdCMEY3RkUwOTlEQjg3MThGMUZCMkEy";
		str = new String(Base64.decodeBase64(str));
		System.out.println(str);
	}
}

class DES3 {

	/**
	 * 根据参数生成Key;
	 * 
	 * @param strKey
	 */
	private Key getKey(String strKey) {
		Key key = null;
		try {
			KeyGenerator _generator = KeyGenerator.getInstance("DES");
			_generator.init(new SecureRandom(strKey.getBytes()));
			key = _generator.generateKey();
			_generator = null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return key;
	}

	/**
	 * 加密以string 明文输入，string密文输出;
	 * 
	 * @param strMing
	 * @return
	 */
	public String getencString(String strMing, Key key) {
		byte[] byteMi = null;
		byte[] byteMing = null;
		String strMi = "";

		try {
			byteMing = strMing.getBytes("utf-8");
			byteMi = getEncCode(byteMing, key);
			strMi = Base64.encodeBase64String(byteMi);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			byteMi = null;
			byteMing = null;
		}
		return strMi;
	}

	/**
	 * 解密以string 密文输入,String 明文输出;
	 * 
	 * @param strMi
	 * @return
	 */
	public String getDecString(String strMi, Key key) {
		byte[] byteMing = null;
		byte[] byteMi = null;
		String strMing = "";
		try {
			byteMi = Base64.decodeBase64(strMi);
			byteMing = getDecCode(byteMi, key);
			strMing = new String(byteMing, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			byteMing = null;
			byteMi = null;
		}
		return strMing;

	}

	/**
	 * 加密以byte[] 明文输入，byte[] 密文输出;
	 * 
	 * @param byts
	 * @return
	 */
	private byte[] getEncCode(byte[] byts, Key key) {
		byte[] byteFina = null;
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byteFina = cipher.doFinal(byts);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 解密以byte[] 密文输入，byte[] 明文输出;
	 * 
	 * @param bytd
	 * @return
	 */
	private byte[] getDecCode(byte[] bytd, Key key) {
		byte[] byteFina = null;
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byteFina = cipher.doFinal(bytd);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}
	
	public String encrypt(String str, String key) {
		Key k = getKey(key);
		return getencString(str, k);
	}
	
	public String decrypt(String encyStr, String key) {
		Key k = getKey(key);
		return getDecString(encyStr, k);
	}
	
}