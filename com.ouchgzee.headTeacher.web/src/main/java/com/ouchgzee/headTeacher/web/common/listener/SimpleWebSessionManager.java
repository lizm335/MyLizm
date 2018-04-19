/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.common.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ouchgzee.headTeacher.constants.ShiroCacheManager;
import com.ouchgzee.headTeacher.dao.account.GjtUserAccountDao;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年7月4日
 * @version 2.5
 *
 */
public class SimpleWebSessionManager extends DefaultWebSessionManager implements WebSessionManager {

	private ShiroCacheManager shiroCacheManager;

	private static String keyPrefix = "shiro-activeSessionCache";

	private GjtUserAccountDao gjtUserAccountDao;

	private final static Logger log = LoggerFactory.getLogger(SimpleWebSessionManager.class);

	public SimpleWebSessionManager() {
		super();
	}

	public void validateSessions() {
		if (log.isInfoEnabled())
			log.info("Validating all active sessions...");
		int invalidCount = 0;

		Cache<Object, Object> cache = shiroCacheManager.getCache(keyPrefix);
		Collection<Session> activeSessions = new ArrayList<Session>();
		List<String> list = gjtUserAccountDao.findSeesionId();
		for (String id : list) {
			try {
				if (cache.get(id) != null) {
					activeSessions.add((Session) cache.get(id));
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		log.info("查找所有要遍历的sessionId集合：" + list.size());
		if (activeSessions != null && !activeSessions.isEmpty()) {
			for (Session s : activeSessions) {
				if (s != null) {
					try {
						log.info("判断是否过期的SessionId:{}", s.getId());
						SessionKey key = new DefaultSessionKey(s.getId());
						validate(s, key);
					} catch (InvalidSessionException e) {
						if (log.isDebugEnabled()) {
							boolean expired = (e instanceof ExpiredSessionException);
							String msg = "Invalidated session with id [" + s.getId() + "]"
									+ (expired ? " (expired)" : " (stopped)");
							log.debug(msg);
						}
						invalidCount++;
					}
				}
			}
		}

		if (log.isInfoEnabled()) {
			String msg = "Finished session validation.";
			if (invalidCount > 0)
				msg = (new StringBuilder()).append(msg).append("  [").append(invalidCount)
						.append("] sessions were stopped.").toString();
			else
				msg = (new StringBuilder()).append(msg).append("  No sessions were stopped.").toString();
			log.info(msg);
		}
	}

	public void setShiroCacheManager(ShiroCacheManager shiroCacheManager) {
		this.shiroCacheManager = shiroCacheManager;
	}

	public GjtUserAccountDao getGjtUserAccountDao() {
		return gjtUserAccountDao;
	}

	public void setGjtUserAccountDao(GjtUserAccountDao gjtUserAccountDao) {
		this.gjtUserAccountDao = gjtUserAccountDao;
	}

}
