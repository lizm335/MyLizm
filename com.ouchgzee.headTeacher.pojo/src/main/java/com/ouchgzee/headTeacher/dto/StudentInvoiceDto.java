/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dto;

import java.util.List;
import java.util.Map;

/**
 * 学员发票信息DTO<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月20日
 * @version 2.5
 * @since JDK 1.7
 */
public class StudentInvoiceDto {

	private String studentId;

	private String xm;

	private String sfzh;

	/**
	 * 报读产品
	 */
	private String zymc;

	/**
	 * 年级
	 */
	private String gradeName;

	/**
	 * 缴费方式 A:全额缴费 B:首年缴费 C:分期付款
	 */
	private String gkxlPaymentTpye;

	/**
	 * 已发放次数
	 */
	private int issueNum;

	/**
	 * 总申请次数
	 */
	private int totalApplyNum;

	/**
	 * 发票状态 A:已申请 B:已开具 C:已发放 D:已领取
	 */
	private String state;

	/**
	 * 远程调用后返回的发票信息
	 */
	private List<Map> invoiceList;
	
	/**
	 * 性别码
	 */
	private String xbm;
	
	/**
	 * 学号
	 */
	private String xh;
	
	/**
	 * 手机号
	 */
	private String sjh;
	
	/**
	 * 层次
	 */
	private String pycc;
	
	/**
	 * 年级
	 */
	private String nj;



	/**
	 * @return the studentId
	 */
	public String getStudentId() {
		return studentId;
	}

	/**
	 * @param studentId
	 *            the studentId to set
	 */
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	/**
	 * @return the xm
	 */
	public String getXm() {
		return xm;
	}

	/**
	 * @return the sfzh
	 */
	public String getSfzh() {
		return sfzh;
	}

	/**
	 * @param sfzh
	 *            the sfzh to set
	 */
	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	/**
	 * @param xm
	 *            the xm to set
	 */
	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getZymc() {
		return zymc;
	}

	public void setZymc(String zymc) {
		this.zymc = zymc;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getGkxlPaymentTpye() {
		return gkxlPaymentTpye;
	}

	public void setGkxlPaymentTpye(String gkxlPaymentTpye) {
		this.gkxlPaymentTpye = gkxlPaymentTpye;
	}

	public int getIssueNum() {
		return issueNum;
	}

	public void setIssueNum(int issueNum) {
		this.issueNum = issueNum;
	}

	public int getTotalApplyNum() {
		return totalApplyNum;
	}

	public void setTotalApplyNum(int totalApplyNum) {
		this.totalApplyNum = totalApplyNum;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<Map> getInvoiceList() {
		return invoiceList;
	}

	public void setInvoiceList(List<Map> invoiceList) {
		this.invoiceList = invoiceList;
	}

	public String getXbm() {
		return xbm;
	}

	public void setXbm(String xbm) {
		this.xbm = xbm;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getSjh() {
		return sjh;
	}

	public void setSjh(String sjh) {
		this.sjh = sjh;
	}

	public String getPycc() {
		return pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	public String getNj() {
		return nj;
	}

	public void setNj(String nj) {
		this.nj = nj;
	}
	
	
	
	
}
