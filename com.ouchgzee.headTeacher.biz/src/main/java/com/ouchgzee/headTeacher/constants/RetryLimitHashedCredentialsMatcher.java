/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.constants;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import com.gzedu.xlims.common.Md5Util;

/**
 * 功能说明：
 * 
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月11日
 * @version 2.5
 *
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
	private Cache<String, AtomicInteger> passwordRetryCache;

	public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
		passwordRetryCache = cacheManager.getCache("passwordRetryCache");
	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

		String username = (String) token.getPrincipal();
		// retry count + 1
		// AtomicInteger retryCount = passwordRetryCache.get(username);
		// if(retryCount == null) {
		// retryCount = new AtomicInteger(0);
		// passwordRetryCache.put(username, retryCount);
		// }
		// if(retryCount.incrementAndGet() > 5) {
		// //if retry count > 5 throw
		// throw new ExcessiveAttemptsException();
		// }
		boolean matches = false;
		try {
			UsernamePasswordToken newToken = (UsernamePasswordToken) token;
			Object tokenCredentials = newToken.getPassword();
			Object accountCredentials = info.getCredentials();
			matches = equals(tokenCredentials, accountCredentials);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// boolean matches = super.doCredentialsMatch(token, info);
		if (matches) {
			// clear retry count
			passwordRetryCache.remove(username);
		}
		return matches;
	}

	/**
	 * 由于数据库的密码都是无盐值的md5加密的，故此处需要自定义密码验证
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	/*private char[] encrypt(char[] data) throws Exception {
		String md5Data = Md5Util.encrypt(String.valueOf(data));
		char[] result = null;
		if (md5Data != null && md5Data != "") {
			result = md5Data.toCharArray();
		}
		return result;
	}*/
}
