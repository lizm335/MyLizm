/**
 * @Package test.springmvc 
 * @Project com.ouchgzee.headTeacher.test
 * @File test.java
 * @Date:2016年4月18日下午2:50:38
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.ouchgzee.headTeacher.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ouchgzee.headTeacher.service.BzrSysDictionaryService;
import com.ouchgzee.headTeacher.service.BzrTestService;

/**
 * @Function: TODO ADD FUNCTION.
 * @ClassName:test
 * @Date: 2016年4月18日 下午2:50:38
 *
 * @author lm
 * @version V2.5
 * @since JDK 1.6
 */
@Controller
@RequestMapping("/mvc")
public class Test {

	@Autowired
	BzrTestService testService;

	@Autowired
	BzrSysDictionaryService sysDictionaryService;

	@RequestMapping("/hello")
	public String hello() {
		System.out.println("sdfsdfsdf123f" + testService.hello());
		System.out.println(sysDictionaryService.queryAll().size());

		return "/test/hello";
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new RedisClient().show();
	}
}
