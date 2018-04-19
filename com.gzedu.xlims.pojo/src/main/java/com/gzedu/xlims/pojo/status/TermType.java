/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.status;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月15日
 * @version 2.5
 *
 */
public enum TermType {
	第一学期("1", "第1学期"), 第二学期("2", "第2学期"), 第三学期("3", "第3学期"), 第四学期("4", "第4学期"), 第五学期("5", "第5学期"), 第六学期("6", "第6学期");
	private String code;
	private String text;

	private TermType(String code,String text) {
		this.code = code;
		this.text = text;
	}

	public static String getName(String code) {
		for (TermType entity : TermType.values()) {
			if (entity.getCode().equals(code)) {
				return entity.toString();
			}
		}
		return "";
	}

	public static String getCodeByText(String text) {
		for (TermType entity : TermType.values()) {
			if (entity.getText().equals(text)) {
				return entity.getCode();
			}
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	

	public String getText() {
		return text;
	}

	public static void main(String[] args) {
		for (TermType string : TermType.values()) {
			System.out.println(string);
		}
		System.out.println(TermType.valueOf("第二学期").code);

		List s = new ArrayList();
		System.out.println(s.get(10));
	}
}
