package com.gzedu.xlims.common.gzedu;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 
 * 验证数据是否为空，支持String 集合 Map 数组为空的验证。。。 数组 集合 如果里面的没数据同样返回true 
 * 
 */
public class EmptyUtils {

	public static boolean isEmpty(Object obj) {
		boolean empty = false;
		if (obj == null) {
			empty = true;
		} else if (obj instanceof String || obj instanceof StringBuffer || obj instanceof StringBuilder) {
			empty = "".equals(String.valueOf(obj).trim());
		} // Collection集合
		else if (obj instanceof Collection) {
			Collection conn = (Collection) obj;
			empty = conn.isEmpty();
		} // Map集合
		else if (obj instanceof Map) {
			Map map = (Map) obj;
			empty = map.isEmpty();
		} // 数组
		else if (obj.getClass().isArray()) {
			empty = Array.getLength(obj) < 1;
		} else {
			// throw new RuntimeException("不支持的类型"+obj+"，判断是否为空！");
		}
		return empty;
	}

	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	public static void main(String[] args) {

	}
}
