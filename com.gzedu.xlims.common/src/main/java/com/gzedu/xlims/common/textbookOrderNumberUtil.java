package com.gzedu.xlims.common;

import java.util.Date;

/**
 * 
 * 功能说明：生产订单号 日期+序号 如：20180101+001
 * 
 * @author ouguohao@eenet.com
 * @Date 2018年1月23日
 * @version 3.0
 *
 */
public class textbookOrderNumberUtil {
	private static int index;
	private static String today = DateUtil.dateToString(new Date(), "yyyyMMdd");

	public synchronized static String getTextBookOrderNumber() {
		String temp=DateUtil.dateToString(new Date(), "yyyyMMdd");
		if(!temp.equals(today) ){
			today=temp;
			index=0;
		}
		index++;
		
		return today + "-" + String.format("%03d", index);
	}

	public void setIndex(int maxIndex) {
		index = maxIndex;
	}
	
	


}
