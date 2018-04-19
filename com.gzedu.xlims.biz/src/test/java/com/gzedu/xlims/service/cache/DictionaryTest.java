package com.gzedu.xlims.service.cache;

import com.gzedu.xlims.service.CacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class DictionaryTest {

	@Autowired
	private CacheService cacheService;
	
	@Test
	public void dicTest() {
		// 第一次获取
		String sex1 = cacheService.getCachedDictionaryName(CacheService.DictionaryKey.SEX, "1");
		System.out.println(sex1);
		// 第二次获取
		String sex2 = cacheService.getCachedDictionaryName(CacheService.DictionaryKey.SEX, "1");
		System.out.println(sex2);
	}

}
