/**
 * @Package com.gzedu.xlims.common 
 * @Project com.gzedu.xlims.common
 * @File RandomData.java
 * @Date:2016年4月27日下午5:23:25
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.gzedu.xlims.common;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @Function: TODO ADD FUNCTION.
 * @ClassName:RandomData
 * @Date: 2016年4月27日 下午5:23:25
 *
 * @author lm
 * @version V2.5
 * @since JDK 1.6
 */
public class RandomData {

	private static Random random = new Random();

	/**
	 * 返回随机ID.
	 */
	public static long randomId() {
		return random.nextLong();
	}

	/**
	 * 返回随机名称, prefix字符串+5位随机数字.
	 */
	public static String randomName(String prefix) {
		return prefix + random.nextInt(10000);
	}

	/**
	 * 从输入list中随机返回一个对象.
	 */
	public static <T> T randomOne(List<T> list) {
		Collections.shuffle(list);
		return list.get(0);
	}

	/**
	 * 从输入list中随机返回n个对象.
	 */
	public static <T> List<T> randomSome(List<T> list, int n) {
		Collections.shuffle(list);
		return list.subList(0, n);
	}

	/**
	 * 从输入list中随机返回随机个对象.
	 */
	public static <T> List<T> randomSome(List<T> list) {
		int size = random.nextInt(list.size());
		if (size == 0) {
			size = 1;
		}
		return randomSome(list, size);
	}
}
