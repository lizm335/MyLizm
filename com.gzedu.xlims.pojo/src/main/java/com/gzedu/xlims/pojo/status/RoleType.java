/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.status;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月26日
 * @version 2.5
 *
 */
public enum RoleType {
	系统管理员("1"), 院校管理员("2"), 分校管理员("3"), 教学点管理员("4"), 教务管理员("5"), 考务管理员("6"),
	学员("7"),班主任("8"),辅导教师("9"),督导教师("10");
	String code;

	private RoleType(String code) {
		this.code = code;
	}

	public static String getName(String code) {
		for (RoleType entity : RoleType.values()) {
			if (entity.getCode() == code) {
				return entity.toString();
			}
		}
		return "";
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
