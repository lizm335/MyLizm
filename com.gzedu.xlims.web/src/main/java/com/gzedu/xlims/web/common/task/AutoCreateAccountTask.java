/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.common.task;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.daoImpl.StudentInfoDaoImpl;
import com.gzedu.xlims.service.GjtSyncLogService;
import com.gzedu.xlims.service.api.ApiOpenClassService;
import com.gzedu.xlims.service.flow.GjtFlowService;
import com.gzedu.xlims.service.signup.SignupDataAddService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.webservice.controller.gjt.signup.SignupDataRunnable;

/**
 * 已缴费没有生成账号的学员自动生成定时器<br/>
 * 功能说明：<br/>
 *		Schedule定时任务<br/>
 *		cron表达式，有专门的语法，而且感觉有点绕人，不过简单来说，大家记住一些常用的用法即可，特殊的语法可以单独去查。<br/>
 *		cron一共有7位，但是最后一位是年，可以留空，所以我们可以写6位：<br/>
 *		* 第一位，表示秒，取值0-59<br/>
 * 		第二位，表示分，取值0-59<br/>
 * 		第三位，表示小时，取值0-23<br/>
 * 		第四位，日期天/日，取值1-31<br/>
 * 		第五位，日期月份，取值1-12<br/>
 * 		第六位，星期，取值1-7，星期一，星期二...，注：不是第1周，第二周的意思 另外：1表示星期天，2表示星期一。<br/>
 * 		第7为，年份，可以留空，取值1970-2099<br/>
 *
 *		cron中，还有一些特殊的符号，含义如下：<br/>
 *		(*)星号：可以理解为每的意思，每秒，每分，每天，每月，每年...<br/>
 *		(?)问号：问号只能出现在日期和星期这两个位置，表示这个位置的值不确定，每天3点执行，所以第六位星期的位置，我们是不需要关注的，就是不确定的值。同时：日期和星期是两个相互排斥的元素，通过问号来表明不指定值。比如，1月10日，比如是星期1，如果在星期的位置是另指定星期二，就前后冲突矛盾了。<br/>
 *		(-)减号：表达一个范围，如在小时字段中使用“10-12”，则表示从10到12点，即10,11,12<br/>
 *		(,)逗号：表达一个列表值，如在星期字段中使用“1,2,4”，则表示星期一，星期二，星期四<br/>
 *		(/)斜杠：如：x/y，x是开始值，y是步长，比如在第一位（秒） 0/15就是，从0秒开始，每15秒，最后就是0，15，30，45，60    另：*\/y，等同于0/y<br/>
 *
 *		cron中，还有一些特殊的符号，含义如下：<br/>
 *		0 0 3 * * ?     每天3点执行<br/>
 *		0 5 3 * * ?     每天3点5分执行<br/>
 *		0 5 3 ? * *     每天3点5分执行，与上面作用相同<br/>
 *		0 5/10 3 * * ?  每天3点的 5分，15分，25分，35分，45分，55分这几个时间点执行<br/>
 *		0 10 3 ? * 1    每周星期天，3点10分 执行，注：1表示星期天<br/>
 *		0 10 3 ? * 1#3  每个月的第三个星期，星期天 执行，#号只能出现在星期的位置<br/>
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2018年02月25日
 * @version 3.0
 */
@Component
@EnableScheduling
public class AutoCreateAccountTask {

	private static final Logger log = LoggerFactory.getLogger(AutoCreateAccountTask.class);

	@Autowired
	private StudentInfoDaoImpl studentInfoDaoImpl;

	@Autowired
	private SignupDataAddService signupDataAddService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtSyncLogService gjtSyncLogService;

	@Autowired
	private ApiOpenClassService apiOpenClassService;

	@Autowired
	private GjtFlowService gjtFlowService;

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 已缴费没有生成账号的学员自动生成定时器<br/>
	 * 功能描述：针对那些缴费确认了但生成失败的学员，再次检测账号<br/>
	 * 参考文章：https://mp.weixin.qq.com/s/TBCEwLVAXdsTszRVpXhVug<br/>
	 * 第一次延时5秒后执行，之后每1小时触发一次
	 */
	@Scheduled(initialDelay = 5000, fixedRate = 3600000)
//	@Scheduled(initialDelay = 5000, fixedRate = 60 * 1000) // 测试时用
	public void task() {
		final Date now = new Date();
		String dateStr = DateUtils.getStringToDate(now);
		log.error("------------ 这是自动生成账号定时器，当前执行一次，当前时间 AutoCreateAccountTask ------------" + dateStr);

		List<Map> studentInfos = studentInfoDaoImpl.findStudentByNoAccount();

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < studentInfos.size(); i++) {
			final String studentId = (String) studentInfos.get(i).get("STUDENT_ID");
			// 缓存热点 key 重建优化，防止tomcat集群服务重复处理
			if(redisTemplate.execute(new RedisCallback<Boolean>() {

				@Override
				public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
					RedisSerializer stringRedisSerializer = redisTemplate.getKeySerializer(); // 配置文件配置的StringRedisSerializer
					RedisSerializer jdkSerializationRedisSerializer = redisTemplate.getValueSerializer(); // 配置文件配置的JdkSerializationRedisSerializer
					byte[] key = stringRedisSerializer.serialize("AutoCreateAccount" + studentId);
					byte[] value = jdkSerializationRedisSerializer.serialize("1");
					return redisConnection.setNX(key, value);
				}

			})) {
				// 如果加锁成功，设置个有效时间
				String key = "AutoCreateAccount" + studentId;
				redisTemplate.expire(key, 30, TimeUnit.MINUTES);
				log.error("------------ AutoCreateAccountTask studentId ------------" + studentId);
				// 开始执行任务
				new SignupDataRunnable(studentId, signupDataAddService, gjtStudentInfoService, apiOpenClassService, gjtFlowService, gjtSyncLogService)
					.run()
				;
			} else {
				// 获取锁失败跳过，执行下一个
				continue;
			}
		}
		long endTime = System.currentTimeMillis();
		log.error("AutoCreateAccountTask <br/> time consuming: " + (endTime - startTime) +
				"ms, student size: " + studentInfos.size() + ".");
	}

	/**
	 * 分布式锁测试
	 */
//	@Scheduled(initialDelay = 5000, fixedRate = 60 * 1000) // 测试时用
	public void test() {
		final Date now = new Date();
		String dateStr = DateUtils.getStringToDate(now);
		log.error("------------ 这是自动生成账号定时器，当前执行一次，当前时间 AutoCreateAccountTask ------------" + dateStr);

		final String studentId = "91d6be514e7544e590fdea62ebba0002";

		final CyclicBarrier cb = new CyclicBarrier(1000);
		for (int i = 0; i < 1000; i++) {
			Runnable runnable = new Runnable() {

				@Override
				public void run() {
					try {
						cb.await(); // 等待线程全部集合完毕
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						e.printStackTrace();
					}

					// 缓存热点 key 重建优化，防止tomcat集群服务重复处理
					if(redisTemplate.execute(new RedisCallback<Boolean>() {

						@Override
						public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
							RedisSerializer stringRedisSerializer = redisTemplate.getKeySerializer(); // 配置文件配置的StringRedisSerializer
							RedisSerializer jdkSerializationRedisSerializer = redisTemplate.getValueSerializer(); // 配置文件配置的JdkSerializationRedisSerializer
							byte[] key = stringRedisSerializer.serialize("AutoCreateAccount" + studentId);
							byte[] value = jdkSerializationRedisSerializer.serialize("1");
							return redisConnection.setNX(key, value);
						}

					})) {
						// 如果加锁成功，设置个有效时间
						String key = "AutoCreateAccount" + studentId;
						redisTemplate.expire(key, 30, TimeUnit.MINUTES);
						log.error("------------ AutoCreateAccountTask studentId ------------" + studentId);
					} else {
						// 获取锁失败跳过，执行下一个
					}
				}
			};
			new Thread(runnable).start();
		}
	}

}
