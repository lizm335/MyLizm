/**
 * @Package com.ouchgzee.headTeacher.web.controller 
 * @Project com.ouchgzee.headTeacher.web
 * @File RedisClient.java
 * @Date:2016年4月19日下午5:09:32
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.ouchgzee.headTeacher.web.controller;
/**
 * @Function: TODO ADD FUNCTION. 
 * @ClassName:RedisClient 
 * @Date:     2016年4月19日 下午5:09:32 
 *
 * @author   lm
 * @version  V2.5  
 * @since    JDK 1.6	 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.redisson.Config;
import org.redisson.Redisson;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisClient {

	private String host = "172.16.165.222:6379";

	private int port = 6379;

	// 0 - never expire
	private int expire = 0;

	// timeout for jedis try to connect to redis server, not expire time! In
	// milliseconds
	private int timeout = 0;

	private String password = "REDIS";

	private Jedis jedis;// 非切片额客户端连接
	private JedisPool jedisPool;// 非切片连接池
	private ShardedJedis shardedJedis;// 切片额客户端连接
	private ShardedJedisPool shardedJedisPool;// 切片连接池

	public RedisClient() {
		initialPool();
		initialShardedPool();
		shardedJedis = shardedJedisPool.getResource();
		jedis = jedisPool.getResource();

	}

	/**
	 * 初始化非切片池
	 */
	private void initialPool() {
		if (jedisPool == null) {
			if (password != null && !"".equals(password)) {
				jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password);
			} else if (timeout != 0) {
				jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout);
			} else {
				jedisPool = new JedisPool(new JedisPoolConfig(), host, port);
			}
		}
	}

	/**
	 * 初始化切片池
	 */
	private void initialShardedPool() {
		// 池基本配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(5);
		config.setMaxWaitMillis(1000l);
		config.setTestOnBorrow(false);
		// slave链接
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		shards.add(new JedisShardInfo(host, port, password));

		// 构造池
		shardedJedisPool = new ShardedJedisPool(config, shards);
	}

	public void show() {
		KeyOperate();
		StringOperate();
		ListOperate();
		SetOperate();
		SortedSetOperate();
		HashOperate();
		jedisPool.returnResource(jedis);
		shardedJedisPool.returnResource(shardedJedis);
	}

	private void KeyOperate() {
	}

	private void StringOperate() {
	}

	private void ListOperate() {
	}

	private void SetOperate() {
	}

	private void SortedSetOperate() {
	}

	private void HashOperate() {
	}

	public static void main(String[] args) {
		// 连接本地的 Redis 服务
		Config config = new Config();
		config.setConnectionPoolSize(10);
		config.addAddress("172.16.165.222:6379");
		Redisson redisson = Redisson.create(config);
		System.out.println("reids连接成功...");

		// 2.测试concurrentMap,put方法的时候就会同步到redis中
		ConcurrentMap<String, Object> map = redisson.getMap("FirstMap");
		map.put("wuguowei", "男");
		map.put("zhangsan", "nan");
		map.put("lisi", "女");

		ConcurrentMap resultMap = redisson.getMap("FirstMap");
		System.out.println("resultMap==" + resultMap.keySet());

		// 2.测试Set集合
		Set mySet = redisson.getSet("MySet");
		mySet.add("wuguowei");
		mySet.add("lisi");

		Set resultSet = redisson.getSet("MySet");
		System.out.println("resultSet===" + resultSet.size());

		// 3.测试Queue队列
		Queue myQueue = redisson.getQueue("FirstQueue");
		myQueue.add("wuguowei");
		myQueue.add("lili");
		myQueue.add("zhangsan");
		myQueue.peek();
		myQueue.poll();

		Queue resultQueue = redisson.getQueue("FirstQueue");
		System.out.println("resultQueue===" + resultQueue);

		// 关闭连接
		redisson.shutdown();
	}
}
