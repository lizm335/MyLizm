package com.ouchgzee.headTeacher.service;


import java.util.Set;

/**
* @ClassName: RedisService
* @Description: 
* @author zhy(505177876@qq.com)
* @date 2016年3月13日
*
 */
public interface RedisService {
	/**
	 * 删除 缓存
	 * @param key
	 * @return
	 * @throws Exception
	 */
	String deleteCached(String key)throws Exception;
	/**
	 * 更新 缓存
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	Object updateCached(String key,Object value)throws Exception;
	/**
	 * 获取缓存
	 * @param key
	 * @return
	 * @throws Exception
	 */
	Object getCached(String key)throws Exception;
	
	Set getShiroSessionByKeys(String keys) throws Exception;
	
	
}
