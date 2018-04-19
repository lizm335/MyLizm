package com.ouchgzee.study.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.gzedu.xlims.service.CommonMapService;
import com.ouchgzee.study.service.course.CourseLearningService;

@ContextConfiguration(locations = { "classpath:spring-config.xml","classpath:spring-mvc.xml" })
public class CourseLearningControllerTest {

	@Autowired
	CommonMapService commonMapService;
	
	@Autowired
	CourseLearningService courseLearningService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void courseLearning() {
		Map searchParams = new HashMap();
		searchParams.put("STUDENT_ID", "ebc806cd61fb4982b3e54407662ee08b");
		searchParams.put("TERM_ID", "9fcdd09a61414319922cc11460c4a09c");
		searchParams.put("KKZY", "22e0b3b56f594ca78cb4adc1b90f36a4");
		searchParams.put("PYCC", 0);
		Map resultMap = courseLearningService.courseLearning(searchParams);
		System.out.println(resultMap.toString());

	}

	
}
