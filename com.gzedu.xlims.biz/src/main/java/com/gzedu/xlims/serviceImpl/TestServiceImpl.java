/**
 * @Package com.ouchgzee.headTeacher.serviceImpl 
 * @Project com.ouchgzee.headTeacher.biz
 * @File TestServiceImpl.java
 * @Date:2016年4月18日下午5:11:41
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.gzedu.xlims.serviceImpl;

import com.gzedu.xlims.service.TestService;
import org.springframework.stereotype.Service;

/**
 * @Function: TODO ADD FUNCTION.
 * @ClassName:TestServiceImpl
 * @Date: 2016年4月18日 下午5:11:41
 *
 * @author lm
 * @version V2.5
 * @since JDK 1.6
 */
@Service
public class TestServiceImpl implements TestService {

	public String hello() {
		return "hello2";
	}

}
