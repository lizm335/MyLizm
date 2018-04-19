package com.gzedu.xlims.pojo.comm;

/**
 * 审核状态，默认0 0-待审核 1-通过 2-不通过
 * 
 * @author lyj
 * @time 2017年8月4日 TODO
 */

public enum AuditStatus {
	/**
	 * 待审核
	 */
	AUDIT_WAIT("待审核"), 
	/**
	 * 审核通过
	 */
	AUDIT_PASS("审核通过"), 
	/**
	 * 审核不通过
	 */
	AUDIT_NOT_PASS("审核不通过");
	
	private String name;

	private AuditStatus(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
