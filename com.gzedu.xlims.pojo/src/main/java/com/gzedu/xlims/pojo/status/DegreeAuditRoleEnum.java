package com.gzedu.xlims.pojo.status;
/**
 * 功能说明：学籍异动审核人角色
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年9月13日
 * @version 2.5
 */
public enum DegreeAuditRoleEnum {
	/**
	 * 毕业管理员
	 */
	GRADUATION_MANAGER("97e31c2c70a442208443751fdeede0ff", 2);
	String num;
	
	int code;
	
	private DegreeAuditRoleEnum(String num, int code) {
		this.num = num;
		this.code = code;
	}
	
	public static Integer getCode(String num) {
		for (DegreeAuditRoleEnum transAuditRoleEnum : DegreeAuditRoleEnum.values()) {
			if (num.equals(transAuditRoleEnum.getNum())) {
				return transAuditRoleEnum.getCode();
			}
		}
		return null;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
