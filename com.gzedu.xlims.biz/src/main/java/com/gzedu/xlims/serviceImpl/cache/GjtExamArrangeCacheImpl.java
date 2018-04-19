package com.gzedu.xlims.serviceImpl.cache;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.service.cache.GjtExamArrangeCache;

@Service
public class GjtExamArrangeCacheImpl implements GjtExamArrangeCache {

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, String> redisTemplate;
	
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Object> hashOps;
	
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valOps;

	/**
	 * 排考标记. 
	 * 自动排考结束后消除或30min 后自动清除
	 */
	@Override
	public Long arrangeLock(String examBatchCode, String examPointCode) {
		String key = "lock:"+examBatchCode+":"+examPointCode;
		Long lock = valOps.increment(key, 1);
		redisTemplate.expire(key, 30, TimeUnit.MINUTES);
		return lock;
	}

	@Override
	public void clearArrangeLock(String examBatchCode, String examPointCode) {
		redisTemplate.delete("lock:"+examBatchCode+":"+examPointCode);
	}
}
