package com.ouchgzee.study.web.controller.roll.vo;
/**
 * 功能说明：审核流程记录VO
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2017年9月11日
 * @version 2.5
 */
public class RollTransactionAudtVO {
	
	private String auditState;//审核状态
	private String auditContent;//审核内容
	private String auditOperatoRole;//审核人角色
	private String auditDt;//审核时间
	private String auditOperator;//审核人姓名
	
	public RollTransactionAudtVO() {
	}
	
	public String getAuditState() {
		return auditState;
	}
	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}
	public String getAuditContent() {
		return auditContent;
	}
	public void setAuditContent(String auditContent) {
		this.auditContent = auditContent;
	}
	public String getAuditOperatoRole() {
		return auditOperatoRole;
	}
	public void setAuditOperatoRole(String auditOperatoRole) {
		this.auditOperatoRole = auditOperatoRole;
	}
	public String getAuditDt() {
		return auditDt;
	}
	public void setAuditDt(String auditDt) {
		this.auditDt = auditDt;
	}
	public String getAuditOperator() {
		return auditOperator;
	}
	public void setAuditOperator(String auditOperator) {
		this.auditOperator = auditOperator;
	}	
}
