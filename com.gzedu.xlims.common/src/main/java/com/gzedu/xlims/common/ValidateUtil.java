package com.gzedu.xlims.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {

	public static boolean isNumeric(String str) {
		// Pattern pattern = Pattern.compile("^[0-9]*$");
		Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]{0,2})?$");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 校验是否全部是中文
	 * 校验规则:账号中只能是有中文。
	 * @param zj
	 * @return 失败FALSE，成功：TRUE
	 */
	public static boolean isChinese(String zj){
		Matcher m = Pattern.compile("[\u2E80-\u9FFF]*").matcher(zj);
		return m.matches();
	}

	/**
	 * 检验字符串站的左括号与右括号数量是否相同
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMatchParenTheses(String str) {
		String s = replaceParenTheses(str);
		Pattern pattern = Pattern.compile("[()]");
		Matcher isParenTheses = pattern.matcher(s);
		return !isParenTheses.find();
	}

	private static String replaceParenTheses(String str) {
		Pattern pattern = Pattern.compile("[(][^()]*[)]");
		Matcher isParenTheses = pattern.matcher(str);
		if (isParenTheses.find()) {
			str = str.replaceAll("[(][^()]*[)]", "");
			return replaceParenTheses(str);
		} else {
			return str;
		}
	}

	private static boolean isMatchParenTheses2(String str) {
		Pattern pattern = Pattern.compile("<[^<>]*(((?'Open'>)[^<>]*)+((?\'-Open\'>)[^<>]*)+)*(?(Open)(?!))>");
		Matcher isParenTheses = pattern.matcher(str);
		return isParenTheses.matches();
	}

	// 匹配电话、手机号，如：13112341234,010-12456789,01012456789,(010)12456789,00861012456789,+861012456789
	public static boolean isPhoneNumber(String input) {
		// String regex="1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}";
		String regex = "^((((13|15|18|17)[0-9])|147)\\d{8})$|^(\\d{3,4})?(-)?\\d{7,8}$";
		return Pattern.matches(regex, input);
	}
	
	// 判断手机
	public static boolean isMobile(String mobile) {
		Pattern pattern = Pattern.compile("^(13|14|15|17|18|19)\\d{9}$");
		Matcher matcher = pattern.matcher(mobile);
		return matcher.matches();
	}

	// 判断邮箱
	public static boolean isEmail(String email) {
		String str = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	// 判断用户名
	public static boolean isUsername(String username) {
		String str = "^[a-zA-Z0-9_-]{3,20}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(username);
		return m.matches();
	}

	// 判断ip地址
	public static boolean isIp(String ip) {
		String str = "([1-9]{1,3}\\.){3}[1-9]";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(ip);
		return m.matches();
	}
	
	/**
	 * 替换Emoji表情符号
	 * @param str
	 * @return
	 */
	public static String replaceEmoji(String str) {
		Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]" ,
		         Pattern . UNICODE_CASE | Pattern . CASE_INSENSITIVE );
		Matcher isEmoji = pattern.matcher(str);
		if(isEmoji.find()){
			str = str.replaceAll("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", "");
		}
		return str;
	}

	public static void main(String[] args) {
		String s = " where (FID=3 and (FNumber='ad' or FName='ea')";
		System.out.println(ValidateUtil.isMatchParenTheses(s));
		/*
		 * String s1="<<das>"; System.out.println(isMatchParenTheses2(s1));
		 */
		System.out.println(ValidateUtil.isPhoneNumber("010-12456789"));
		System.out.println(ValidateUtil.isMobile("13712456789"));
		System.out.println("mail = " + ValidateUtil.isEmail("无"));
	}

}
