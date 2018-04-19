/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.opi;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年7月22日
 * @version 2.5
 *
 */
@XStreamAlias("tranceData")
public class OpiTermChooseData {
	OpiStudchoose STUDCHOOSE;
	OpiTchchoose TCHCHOOSE;

	public OpiTermChooseData(OpiStudchoose sTUDCHOOSE, OpiTchchoose tCHCHOOSE) {
		super();
		STUDCHOOSE = sTUDCHOOSE;
		TCHCHOOSE = tCHCHOOSE;
	}

	public OpiTermChooseData(OpiStudchoose sTUDCHOOSE) {
		super();
		STUDCHOOSE = sTUDCHOOSE;
	}

	public OpiStudchoose getSTUDCHOOSE() {
		return STUDCHOOSE;
	}

	public void setSTUDCHOOSE(OpiStudchoose sTUDCHOOSE) {
		STUDCHOOSE = sTUDCHOOSE;
	}

	public OpiTchchoose getTCHCHOOSE() {
		return TCHCHOOSE;
	}

	public void setTCHCHOOSE(OpiTchchoose tCHCHOOSE) {
		TCHCHOOSE = tCHCHOOSE;
	}

}
