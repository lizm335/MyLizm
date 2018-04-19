package com.gzedu.xlims.serviceImpl.cache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.service.cache.CodeGeneratorService;

/**
 * 编码自增工具
 */
@Service
public class CodeGeneratorServiceImpl implements CodeGeneratorService {

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, String> redisTemplate;

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Object> hashOps;

	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valOps;

	@Override
	public String codeGenerator(String type) {
		return codeGenerator(type, 12);
	}

	@Override
	public String codeGenerator(String type, int digit) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(new Date());
		long rs = hashOps.increment(type, date, 1);
		String index = String.valueOf(rs);
		redisTemplate.expire(type, 30, TimeUnit.DAYS);
		String zero = "";
		for (int i = 0; i < digit - date.length() - index.length(); i++) {
			zero += "0";
		}
		return date + zero + index;
	}

	@Override
	public long incrRoundIndex(String examBatchCode) {
		return valOps.increment(examBatchCode, 1);
	}

}
