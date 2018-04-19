package com.gzedu.xlims.web;

import static org.junit.Assert.*;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4jTest {
	
	private static final Logger logger = LoggerFactory.getLogger(Test.class);

	@Test
	public void test() {
		logger.debug("debug...");
		logger.info("info...");
		logger.warn("warn....");
		logger.error("error...");
	}

}
