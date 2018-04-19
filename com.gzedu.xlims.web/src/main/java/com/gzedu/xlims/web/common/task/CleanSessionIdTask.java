/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.common.task;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.dao.Collections3;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtUserAccount;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年4月17日
 * @version 3.0
 *
 */
public class CleanSessionIdTask {
	private static final Logger log = LoggerFactory.getLogger(CleanSessionIdTask.class);

	@Autowired
	private MemcachedClient memcachedClient;

	public void cleanMemcachedKey() {
		String keyName = "shiro-activeSessionCache$xlims".toUpperCase();
		Set<String> keys = (HashSet<String>) memcachedClient.get(keyName);
		Set<String> yesValidSessionSet = Sets.newHashSet();// 有效的session Key
		Set<String> noValidSessionKeySet = Sets.newHashSet();// 因为只有session没有用户对象在里面所以删除session
		Set<String> noValidSessionIdSet = Sets.newHashSet();// 删除xllms总集合里面的sessionkey
		if (EmptyUtils.isNotEmpty(keys)) {
			for (String key : keys) {
				String xllmsKey = "shiro-activeSessionCache_" + key;
				Session session = (Session) memcachedClient.get(xllmsKey.toUpperCase());
				if (session == null) {
					noValidSessionIdSet.add(key);
					continue;
				}
				long time = DateUtils.getDate().getTime() - session.getLastAccessTime().getTime();
				long num = (time / 1000 / 60);
				if (num > 2) {// 如果2分钟内还没对象，说明不是登陆的，删除无效的sessionId
					// 就算误删了，浏览器会给用户新增一个sessionId
					GjtUserAccount user = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER);
					if (user == null) {
						// 无效sessionId
						noValidSessionKeySet.add(xllmsKey.toUpperCase());
						noValidSessionIdSet.add(key);
					} else {
						yesValidSessionSet.add(xllmsKey);
					}
				}
			}
		}
		// log.info("有效的Key：" + yesValidSessionSet);
		// log.info("无效的sessionkey:" + noValidSessionKeySet);
		// log.info("无效的sessionId:" + noValidSessionIdSet);

		CASValue<Object> casValue = memcachedClient.gets(keyName);
		if (casValue != null) {// 删除key
			Set<String> keySet = (Set<String>) casValue.getValue();
			keySet.removeAll(noValidSessionIdSet);
			CASResponse response = memcachedClient.cas(keyName, casValue.getCas(), keySet);
			if (response.equals(CASResponse.OK)) {
				log.info("task remove cachedKey size：" + noValidSessionIdSet.size());
			}
		}
		List<Object> list = Lists.newArrayList();
		if (Collections3.isNotEmpty(noValidSessionKeySet)) {
			for (String key : noValidSessionKeySet) {// 删除session
				try {
					Object value = memcachedClient.delete(key);
					list.add(value);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		log.info("remove size：" + list.size());
	}
}
