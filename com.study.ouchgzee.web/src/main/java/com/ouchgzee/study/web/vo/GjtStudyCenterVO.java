/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.study.web.vo;

import com.gzedu.xlims.pojo.GjtStudyCenter;

/**
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年8月14日
 * @version 3.0
 *
 */
public class GjtStudyCenterVO {
	private String linkman;

	private String linkTel;

	private String officeAddr;

	private String officeTel;

	private String studyCenterName;

	public GjtStudyCenterVO(GjtStudyCenter gjtStudyCenter) {
		this.linkman = gjtStudyCenter.getLinkman();
		this.linkTel = gjtStudyCenter.getLinkTel();
		this.officeAddr = gjtStudyCenter.getOfficeAddr();
		this.officeTel = gjtStudyCenter.getOfficeTel();
		this.studyCenterName = gjtStudyCenter.getScName();
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getLinkTel() {
		return linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public String getOfficeAddr() {
		return officeAddr;
	}

	public void setOfficeAddr(String officeAddr) {
		this.officeAddr = officeAddr;
	}

	public String getOfficeTel() {
		return officeTel;
	}

	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}

	public String getStudyCenterName() {
		return studyCenterName;
	}

	public void setStudyCenterName(String studyCenterName) {
		this.studyCenterName = studyCenterName;
	}

}
