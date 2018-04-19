package com.ouchgzee.headTeacher.serviceImpl;

import org.springframework.stereotype.Service;

import com.ouchgzee.headTeacher.service.BzrCodeGeneratorService;

/**
 * 编码自增工具
 */
@Deprecated @Service("bzrCodeGeneratorServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class CodeGeneratorServiceImpl implements BzrCodeGeneratorService {

	// @Autowired
	// @Qualifier("redisTemplate")
	// private RedisTemplate<String, String> redisTemplate;
	//
	// @Resource(name = "redisTemplate")
	// private HashOperations<String, String, Object> hashOps;
	//
	// @Resource(name = "redisTemplate")
	// private ValueOperations<String, String> valOps;

	@Override
	public String codeGenerator(String type) {
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// String date = sdf.format(new Date());
		// long rs = hashOps.increment(type, date, 1);
		// String index = String.valueOf(rs);
		// redisTemplate.expire(type, 30, TimeUnit.DAYS);
		// if (rs < 10) {
		// index = "000" + index;
		// } else if (rs < 100) {
		// index = "00" + index;
		// } else if (rs < 1000) {
		// index = "0" + index;
		// }
		// return date + index;
		return null;
	}

	@Override
	public long incrRoundIndex(String examBatchCode) {
		// return valOps.increment(examBatchCode, 1);
		return 0;
	}

}
