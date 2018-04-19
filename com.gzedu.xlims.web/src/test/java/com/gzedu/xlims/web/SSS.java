/**
 * @Package com.gzedu.xlims.web 
 * @Project com.gzedu.xlims.web
 * @File SSS.java
 * @Date:2016年4月19日下午6:29:39
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.gzedu.xlims.web;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.gzedu.xlims.common.DateUtils;

/**
 * @Function: TODO ADD FUNCTION.
 * @ClassName:SSS
 * @Date: 2016年4月19日 下午6:29:39
 *
 * @author lm
 * @version V2.5
 * @since JDK 1.6
 */
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class SSS {

	@Test
	public void test() {

	}

	public static void main(String[] args) throws ParseException {
		String date1 = "2017-11-17 12:00:00";
		Date strToDate = DateUtils.getStrToDate(date1);
		long time = DateUtils.getDate().getTime();
		long time2 = time - (strToDate.getTime());
		System.out.println((time2 / 1000 / 60 / 60));
	}

}
