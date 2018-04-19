/**
 * @Package com.ouchgzee.headTeacher.serviceImpl 
 * @Project com.ouchgzee.headTeacher.biz
 * @File TestServiceImpl.java
 * @Date:2016年4月18日下午5:11:41
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.ouchgzee.headTeacher.serviceImpl;

import org.springframework.stereotype.Service;

import com.ouchgzee.headTeacher.service.BzrTestService;

/**
 * @Function: TODO ADD FUNCTION.
 * @ClassName:TestServiceImpl
 * @Date: 2016年4月18日 下午5:11:41
 *
 * @author lm
 * @version V2.5
 * @since JDK 1.6
 */
@Deprecated @Service("bzrTestServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class TestServiceImpl implements BzrTestService {

	public String hello() {
		return "hello2";
	}

}
