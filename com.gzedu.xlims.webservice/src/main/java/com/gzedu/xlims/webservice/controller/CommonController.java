package com.gzedu.xlims.webservice.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.dao.Collections3;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.json.JsonUtils;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.webservice.response.ResponseResult;
import com.gzedu.xlims.webservice.response.ResponseStatus;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;

/**
 * 公共接口
 * 
 * @author lyj
 * @time 2017年5月18日 TODO
 */
@Controller
@RequestMapping("/interface/comm")
public class CommonController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(CommonController.class);
	@Autowired
	private MemcachedClient memcachedClient;

	@RequestMapping("/getMemcachedValue")
	@ResponseBody
	public ResponseResult getMemcachedValue(String key, String up, HttpServletRequest request) {
		ResponseResult result = null;
		Object value = null;
		try {
			if (StringUtils.isNotEmpty(up)) {
				key = key.toUpperCase();
			}
			value = memcachedClient.get(key);

			if (value instanceof SimpleSession) {
				SimpleSession session = (SimpleSession) value;
				value = "sessionId:" + session.getId() + " lastAccessTime:" + session.getLastAccessTime() + " timeout:"
						+ session.getTimeout();
			}
			result = new ResponseResult(ResponseStatus.SUCCESS, value);
		} catch (Exception e) {
			e.printStackTrace();
			result = new ResponseResult(ResponseStatus.FAIL, e);
		}

		return result;

	}

	@RequestMapping("/initMemcachedValue")
	@ResponseBody
	public ResponseResult initMemcachedValue(String key) {
		ResponseResult result = null;
		Object value = null;
		try {
			value = memcachedClient.delete(key);
			result = new ResponseResult(ResponseStatus.SUCCESS, value);
		} catch (Exception e) {
			e.printStackTrace();
			result = new ResponseResult(ResponseStatus.FAIL, e);
		}
		return result;
	}

	@RequestMapping("/cleanMemcachedKey")
	@ResponseBody
	public ResponseResult cleanMemcachedKey(String keyName) {
		ResponseResult result = null;
		Set<String> keys = (HashSet<String>) memcachedClient.get(keyName.toUpperCase());
		Set<String> yesValidSessionSet = Sets.newHashSet();// 有效的session Key
		Set<String> noValidSessionKeySet = Sets.newHashSet();// 因为只有session没有用户对象在里面所以删除session
		Set<String> noValidSessionIdSet = Sets.newHashSet();// 删除xllms总集合里面的sessionkey
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (EmptyUtils.isNotEmpty(keys)) {
			for (String key : keys) {
				String xllmsKey = "shiro-activeSessionCache_" + key;
				// Object object = memcachedClient.get(xllmsKey.toUpperCase());
				Session session = (Session) memcachedClient.get(xllmsKey.toUpperCase());
				if (session == null) {
					noValidSessionIdSet.add(key);
					continue;
				}
				long time = DateUtils.getDate().getTime() - session.getLastAccessTime().getTime();
				long num = (time / 1000 / 60);
				if (num > 2) {// 如果2分钟内还没对象，说明不是登陆的，删除无效的sessionId
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
		log.info("有效的Key：" + yesValidSessionSet);
		log.info("无效的sessionkey:" + noValidSessionKeySet);
		log.info("无效的sessionId:" + noValidSessionIdSet);

		CASValue<Object> casValue = memcachedClient.gets(keyName.toUpperCase());
		if (casValue != null) {// 删除key
			Set<String> keySet = (Set<String>) casValue.getValue();
			keySet.removeAll(noValidSessionIdSet);
			CASResponse response = memcachedClient.cas(keyName.toUpperCase(), casValue.getCas(), keySet);
			if (response.equals(CASResponse.OK)) {
				log.info("task remove cachedKey(" + keySet.size() + "):" + JsonUtils.toJson(noValidSessionIdSet));
				resultMap.put("removeSessionIdsSize", noValidSessionIdSet.size());
				resultMap.put("removeSessionIds", noValidSessionIdSet);
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
		resultMap.put("removeSessionsSize", list == null ? 0 : list.size());
		resultMap.put("removeSessionLists", list);
		result = new ResponseResult(ResponseStatus.SUCCESS, resultMap);
		return result;
	}

}
