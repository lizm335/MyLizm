package com.gzedu.xlims.common;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月13日
 * @version 2.5
 * @since JDK1.7
 *
 */
public class EncryptUtil {

	/**
	 * 解密数据
	 * 
	 * @param encryptedStr
	 * @return
	 */
	public static String decrypt(String encryptedStr) { // 接收人事局数据，并解密
		int iL = 0, i = 0, j = 0, mData, m = 0;
		String sMyKey = "", sReturn = "", mKey = "", tMsg = "";
		String[] arrMsg;
		char mChar;
		String sKey = "%Ed6$^%%F^FD*FD)((_FD)F)D)F_D_F_F__FD_*UIJJKLJLDOFJDIF*&*&*DU(FD)F)D))D)FIF*Df9duf9d8uf9divc0d8f9d0"; // 解密key

		if (encryptedStr == null) {
			return null;
		}

		if (sKey.length() < encryptedStr.length()) {
			iL = (encryptedStr.length() / sKey.length()) + 1;
			for (j = 0; j < iL; j++) {
				sMyKey = sMyKey + sKey;
			}
			sKey = sMyKey;
		}

		tMsg = encryptedStr;
		while (tMsg.indexOf("%") > 0) {
			m = m + 1;
			tMsg = tMsg.substring(tMsg.indexOf("%") + 1, tMsg.length());
		}
		arrMsg = new String[m + 1];
		tMsg = encryptedStr;
		m = 0;
		while (tMsg.indexOf("%") > 0) {
			arrMsg[m] = tMsg.substring(0, tMsg.indexOf("%"));
			tMsg = tMsg.substring(tMsg.indexOf("%") + 1, tMsg.length());
			m = m + 1;
		}
		arrMsg[m] = tMsg.substring(0, tMsg.length());

		for (i = 0; i < arrMsg.length; i++) {
			mKey = sKey.charAt(i) + "";
			mData = Integer.parseInt(arrMsg[i].toUpperCase(), 16) ^ mKey.hashCode();
			mChar = (char) mData;
			sReturn = sReturn + mChar;
		}
		return sReturn;
	}

	/**
	 * 加密数据
	 * 
	 * @param sMsg
	 * @return
	 */
	public static String encrypt(String sMsg) {
		int iL = 0, i = 0, j = 0;
		String sMyKey = "", sReturn = "", mKey = "", Result = "";
		String sKey = "%Ed6$^%%F^FD*FD)((_FD)F)D)F_D_F_F__FD_*UIJJKLJLDOFJDIF*&*&*DU(FD)F)D))D)FIF*Df9duf9d8uf9divc0d8f9d0"; // \u00BC\u00D3\u00C3\u00DCkey
		try {
			if (sMsg != null) {
				if (sKey.length() < sMsg.length()) {
					iL = (sMsg.length() / sKey.length()) + 1;
					for (j = 0; j < iL; j++) {
						sMyKey = sMyKey + sKey;
					}
					sKey = sMyKey;
				}

				for (i = 0; i < sMsg.length(); i++) {
					mKey = sKey.charAt(i) + "";
					sReturn = sReturn + (Integer.toHexString(sMsg.charAt(i) ^ mKey.hashCode())).toString() + "%";
				}

				Result = sReturn.substring(0, sReturn.length() - 1);
			}
		} catch (Exception e) {
			System.out.println("Send EnCrypt Error : " + e);
		}
		return Result;
	}

	public EncryptUtil() {
	}

	/**
	 * ftp上传控件加密算法 1.依次处理每一个字符； 2.获取每一个字符的asc码(如,A的asc码为65)
	 * 3.获取asc码每一位的值,相加后再与65相加得到标志位(如,A的asc码为65,6+5+65=76,标志位为76(L))
	 * 4.每一位asc码值加65,得到加密字符(如,A的asc码为65,加密字符分别为6+65=71(G),5+65=70(F))
	 * 5.每个字符加密后的字符由“标志位”和“加密字符”组成，所以A加密后的字符为:76(L)+71(G)+70(F),也就是LGF;
	 */
	public static String encryptInfo(String encryptStr) {
		String encryptedStr = "";

		int ascCode;
		String ascCodeStr, codeStr;
		char firstChar;
		int charAscCodeSum, firstCharAscCode, subCharAscCode;
		char charAscCodeCharArray[];

		for (int i = 0; i < encryptStr.length(); i++) {
			ascCode = encryptStr.charAt(i);
			ascCodeStr = String.valueOf(ascCode);
			charAscCodeSum = 0;
			firstCharAscCode = 0;
			codeStr = "";
			charAscCodeCharArray = ascCodeStr.toCharArray();
			for (char element : charAscCodeCharArray) {
				charAscCodeSum += Integer.parseInt(String.valueOf(element));
				subCharAscCode = 65 + Integer.parseInt(String.valueOf(element));
				codeStr += String.valueOf((char) subCharAscCode);
			}
			firstCharAscCode = 65 + charAscCodeSum;
			firstChar = (char) firstCharAscCode;
			encryptedStr += firstChar + codeStr;
		}
		return encryptedStr;
	}

	public static String getEncryptFtpInfo(String ftpIp, String ftpUser, String ftpPwd) {
		String encryptedFtpStr = "";
		encryptedFtpStr = "'" + encryptInfo(ftpIp) + "','" + encryptInfo(ftpUser) + "','" + encryptInfo(ftpPwd) + "'";
		return encryptedFtpStr;
	}

}
