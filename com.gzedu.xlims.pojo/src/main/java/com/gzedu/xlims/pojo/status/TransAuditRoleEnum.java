package com.gzedu.xlims.pojo.status;
/**
 * 功能说明：学籍异动审核人角色
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年9月13日
 * @version 2.5
 */
public enum TransAuditRoleEnum {

	班主任("fcf94a20da1c44a1a31f94eafaf4b707","2"),
	学习中心管理员("7ab10371c2f040df8289b09cde4b9510","3"),
	学籍科("3f5f7ca336a64c42bc5d3a4c1986289e","5"),
	院长("d4b27a66c0a87b010120da231915c223","0"),
	学支管理员("9a6f05b3e24d456fb84435dd75e934c2","2");
	
	String num;
	
	String code;	
	
	private TransAuditRoleEnum(String num, String code) {
		this.num = num;
		this.code = code;
	}
	
	public static String getCode(String num) {
		for (TransAuditRoleEnum transAuditRoleEnum : TransAuditRoleEnum.values()) {
			if (num.equals(transAuditRoleEnum.getNum())) {
				return transAuditRoleEnum.getCode().toString();
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
