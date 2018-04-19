/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common;

/**
 * <p>Title: MD5加密算法(推荐)</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author 欧阳春
 * @version 1.0
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.gzedu.xlims.common.gzdec.framework.util.NrmsEncrypt;

//import java.math.*;

public class Md5Util {
	public static final String MD5 = "MD5";

	/**
	 * 提供md5加密
	 *
	 * @param sInput
	 * @return 返回md5加密字符串
	 */
	public static String encode(String sInput) {

		// String algorithm = "";
		// 输入不能为空
		if (sInput == null) {
			return "null";
		}
		String hs = "";
		try {
			MessageDigest md = MessageDigest.getInstance(Md5Util.MD5);

			// 按照系统缺省的字符编码方式把sInput 转换成字节，并把结果存到一新的字节数组buffer中
			// byte buffer[] = sInput.getBytes();
			md.update(sInput.getBytes());
			byte buffer[] = md.digest();
			String stmp = "";

			for (int n = 0; n < buffer.length; n++) {
				stmp = Integer.toHexString(buffer[n] & 0xff);
				if (stmp.length() == 1)
					hs = hs + "0" + stmp;
				else
					hs = hs + stmp;
			}
		} catch (NoSuchAlgorithmException e) {

		}
		return hs.toUpperCase();

	}

	public static String encodeLower(String sInput) {

		// String algorithm = "";
		// 输入不能为空
		if (sInput == null) {
			return "null";
		}
		String hs = "";
		try {
			MessageDigest md = MessageDigest.getInstance(Md5Util.MD5);

			// 按照系统缺省的字符编码方式把sInput 转换成字节，并把结果存到一新的字节数组buffer中
			// byte buffer[] = sInput.getBytes();
			md.update(sInput.getBytes());
			byte buffer[] = md.digest();
			String stmp = "";

			for (int n = 0; n < buffer.length; n++) {
				stmp = Integer.toHexString(buffer[n] & 0xff);
				if (stmp.length() == 1)
					hs = hs + "0" + stmp;
				else
					hs = hs + stmp;
			}
		} catch (NoSuchAlgorithmException e) {

		}
		return hs.toLowerCase();

	}

	public static String encode2(String sInput) {

		// String algorithm = "";
		// 输入不能为空
		if (sInput == null) {
			return "null";
		}
		String hs = "";
		try {
			MessageDigest md = MessageDigest.getInstance(Md5Util.MD5);

			// 按照系统缺省的字符编码方式把sInput 转换成字节，并把结果存到一新的字节数组buffer中
			// byte buffer[] = sInput.getBytes();
			md.update(sInput.getBytes());
			byte buffer[] = md.digest();
			String stmp = "";

			for (int n = 0; n < buffer.length; n++) {
				stmp = Integer.toHexString(buffer[n] & 0xff);
				if (stmp.length() == 1)
					hs = hs + "0" + stmp;
				else
					hs = hs + stmp;
			}
		} catch (NoSuchAlgorithmException e) {

		}
		return hs;

	}

	/**
	 * MD5算法加密
	 * 
	 * @param plaintext
	 * @return
	 * @throws EncryptException
	 *             2016年6月15日
	 * @author Orion
	 * @throws Exception
	 */
	public final static String encrypt(String plaintext) throws Exception {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			if (plaintext == null || plaintext.equals(""))
				throw new Exception("待加密内容为空(" + Md5Util.class.getName() + ")");

			byte[] btInput = plaintext.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			throw new Exception("MD5加密失败 : ", e);
		}
	}

	/**
	 * 不能用系统自带的编码
	 * 
	 * @param sInput
	 * @return
	 */
	public static String encodeLower(String sInput, String code) {

		// String algorithm = "";
		// 输入不能为空
		if (sInput == null) {
			return "null";
		}
		String hs = "";
		try {
			MessageDigest md = MessageDigest.getInstance(Md5Util.MD5);

			// 按照系统缺省的字符编码方式把sInput 转换成字节，并把结果存到一新的字节数组buffer中
			// byte buffer[] = sInput.getBytes();
			md.update(sInput.getBytes(code));
			byte buffer[] = md.digest();
			String stmp = "";

			for (int n = 0; n < buffer.length; n++) {
				stmp = Integer.toHexString(buffer[n] & 0xff);
				if (stmp.length() == 1)
					hs = hs + "0" + stmp;
				else
					hs = hs + stmp;
			}
		} catch (Exception e) {

		}
		return hs.toLowerCase();

	}
	
	/**
	 * 
	 * @param plainText
	 *            明文
	 * @return 32位密文
	 */
	public static String encryption(String plainText) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			re_md5 = buf.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return re_md5;
	}
	
	public static String getMD5(String info) {
		try {
			// 获取 MessageDigest 对象，参数为 MD5 字符串，表示这是一个 MD5 算法（其他还有 SHA1 算法等）：
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			// update(byte[])方法，输入原数据
			// 类似StringBuilder对象的append()方法，追加模式，属于一个累计更改的过程
			md5.update(info.getBytes("UTF-8"));
			// digest()被调用后,MessageDigest对象就被重置，即不能连续再次调用该方法计算原数据的MD5值。可以手动调用reset()方法重置输入源。
			// digest()返回值16位长度的哈希值，由byte[]承接
			byte[] md5Array = md5.digest();
			// byte[]通常我们会转化为十六进制的32位长度的字符串来使用,本文会介绍三种常用的转换方法
			return bytesToHex1(md5Array);
		} catch (Exception e) {
			return "";
		}
	}

	private static String bytesToHex1(byte[] md5Array) {
		StringBuilder strBuilder = new StringBuilder();
		for (int i = 0; i < md5Array.length; i++) {
			int temp = 0xff & md5Array[i];// TODO:此处为什么添加 0xff & ？
			String hexString = Integer.toHexString(temp);
			if (hexString.length() == 1) {// 如果是十六进制的0f，默认只显示f，此时要补上0
				strBuilder.append("0").append(hexString);
			} else {
				strBuilder.append(hexString);
			}
		}
		return strBuilder.toString();
	}
	
	public static void main(String[] args) {
		String a = "plaintext[]";
		try {
			System.out.println(encrypt(a));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
