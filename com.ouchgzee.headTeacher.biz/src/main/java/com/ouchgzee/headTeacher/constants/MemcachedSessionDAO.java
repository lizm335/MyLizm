package com.ouchgzee.headTeacher.constants;

import java.io.Serializable;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;

import com.gzedu.xlims.common.constants.WebConstants;

import net.spy.memcached.MemcachedClient;

public class MemcachedSessionDAO extends EnterpriseCacheSessionDAO {

	private MemcachedClient memcachedClient;

	public static String keyPrefix = "shiro-activeSessionCache";
	
	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}
	
	public String getSessionKey(String sessionId) {
		StringBuilder key = new StringBuilder();
		key.append(keyPrefix.toUpperCase());
		key.append("_");
		key.append(sessionId);
		return key.toString().toUpperCase();
	}
	
	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = super.doCreate(session);
		memcachedClient.set(getSessionKey(sessionId.toString()), WebConstants.EXPIRE, session);
		return sessionId;
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		Session session = super.doReadSession(sessionId);
		if (session == null) {
			session = (Session)memcachedClient.get(getSessionKey(sessionId.toString()));
		}
		return session;
	}

	@Override
	protected void doUpdate(Session session) {
		super.doUpdate(session);
		memcachedClient.set(getSessionKey(session.getId().toString()), WebConstants.EXPIRE, session);
	}

	@Override
	protected void doDelete(Session session) {
		super.doDelete(session);
		memcachedClient.delete(getSessionKey(session.getId().toString()));
	}

}
