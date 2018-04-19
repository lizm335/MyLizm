/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 
 * 功能说明：机构类型
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月30日
 * @version 2.5
 *
 */
public enum OrgTypeEnum {
	/**
	 * 总部
	 */
	HEADQUARTERS("0", "总部"),
	/**
	 * 分部
	 */
	BRANCH("10", "分部"),
	/**
	 * 分校
	 */
	BRANCHSCHOOL("1", "分校"),
	/**
	 * 学习中心
	 */
	STUDYCENTER("3", "学习中心"),
	/**
	 * 学习体验中心（招生点）
	 */
	ENROLLSTUDENT("6", "学习体验中心（招生点）");
	String num;
	String code;

	private OrgTypeEnum(String num, String code) {
		this.num = num;
		this.code = code;
	}

	public static String getName(String number) {
		for (OrgTypeEnum studentTypeEnum : OrgTypeEnum.values()) {
			if (number.equals(studentTypeEnum.getNum())) {
				return studentTypeEnum.toString();
			}
		}
		return "";
	}

	public static String getCode(String number) {
		for (OrgTypeEnum studentTypeEnum : OrgTypeEnum.values()) {
			if (number.equals(studentTypeEnum.getNum())) {
				return studentTypeEnum.getCode().toString();
			}
		}
		return "";
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
