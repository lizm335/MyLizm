
package com.gzedu.xlims.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月26日
 * @version 2.5
 * @since JDK1.7
 *
 */

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月4日
 * @version 2.5
 * @since JDK1.7
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring-config.xml" })
public class GjtSradeTest {

	@Autowired
	private MemcachedClient memcachedClient;

	@Test
	public void getPage() {
		try {
			// Store a value (async) for one hour
			memcachedClient.set("someKey", 3600, "中文值!");
			// replace the value
			memcachedClient.replace("someKey", 33, "另一个值!");
			// Retrieve a value.
			Object ret = memcachedClient.get("someKey");
			System.out.println((String) ret);
			OperationFuture<Boolean> retb = memcachedClient.delete("someKey");
		} catch (Exception e) {
		}
	}
}
