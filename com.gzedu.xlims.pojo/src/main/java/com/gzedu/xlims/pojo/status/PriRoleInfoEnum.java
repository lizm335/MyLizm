/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 角色枚举<br>
 * 功能说明：管理数据库中角色表的编号及名称；<br>
 * 			用在程序中做业务处理，编号就不能随便变来变去！<br>
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年11月23日
 * @version 3.0
 * @since JDK1.7
 */
public enum PriRoleInfoEnum {

	ROLE_0("0", "院长"),
	ROLE_1("1", "系统管理员"),
	ROLE_4("4", "教学部长"),
	ROLE_5("5", "教务部长"),
	ROLE_6("6", "考务科长"),
	ROLE_7("7", "学籍科长"),
	ROLE_8("8", "教材科长"),
	ROLE_9("9", "毕业科长"),
	ROLE_13("13", "招生主任"),
	ROLE_20("20", "班主任"),
	ROLE_21("21", "辅导教师"),
	ROLE_22("22", "督导教师"),
	ROLE_16("16", "学生事务科长"),
	ROLE_18("18", "招生点主任"),
	ROLE_25("25", "院校教辅管理员"),
	ROLE_26("26", "平台运管员"),
	ROLE_27("27", "专业与课程主任"),
	ROLE_24("24", "课程体验员"),
	ROLE_XXZX_ZSY("xxzx_zsy", "学习中心招生员"),
	ROLE_ZSDZSGLY("zsdzsgly", "招生点招生员"),
	ROLE_XXZXGLY("xxzxgly", "学习中心主任"),
	ROLE_30("30", "班主任（有考试院校模式）"),
	ROLE_31("31", "班主任（无考试院校模式）"),
	ROLE_32("32", "任课教师"),
	ROLE_LWJS("lwjs", "论文教师"),
	ROLE_1001("1001", "提问者");

	private String code;

	private String name;

	private PriRoleInfoEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static PriRoleInfoEnum getItem(String value) {
		for (PriRoleInfoEnum item : values()) {
			if (item.getCode().equals(value))
				return item;
		}
		return null;
	}
	
}
