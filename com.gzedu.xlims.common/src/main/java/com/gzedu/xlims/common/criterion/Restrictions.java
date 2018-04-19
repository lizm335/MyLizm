/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common.criterion;

import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.criterion.Criterion.Operator;
import org.hibernate.criterion.MatchMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 条件构造器<br>
 * 功能说明：用于创建条件表达式
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年4月29日
 * @version 2.5
 * @since JDK 1.7
 */
public class Restrictions {

	/**
	 * 等于
	 * 
	 * @param fieldName
	 * @param value
	 * @param ignoreNull
	 * @return
	 */
	public static SimpleExpression eq(String fieldName, Object value, boolean ignoreNull) {
		if (StringUtils.isEmpty(value))
			return null;
		return new SimpleExpression(fieldName, value, Operator.EQ);
	}

	/**
	 * 不等于
	 * 
	 * @param fieldName
	 * @param value
	 * @param ignoreNull
	 * @return
	 */
	public static SimpleExpression ne(String fieldName, Object value, boolean ignoreNull) {
		if (StringUtils.isEmpty(value))
			return null;
		return new SimpleExpression(fieldName, value, Operator.NE);
	}

	/**
	 * 模糊匹配
	 * 
	 * @param fieldName
	 * @param value
	 * @param ignoreNull
	 * @return
	 */
	public static SimpleExpression like(String fieldName, String value, boolean ignoreNull) {
		if (StringUtils.isEmpty(value))
			return null;
		return new SimpleExpression(fieldName, MatchMode.ANYWHERE.toMatchString(value), Operator.LIKE);
	}

	/**
	 * 特殊模糊匹配
	 * 
	 * @param fieldName
	 * @param value
	 * @param matchMode
	 * @param ignoreNull
	 * @return
	 */
	public static SimpleExpression like(String fieldName, String value, MatchMode matchMode, boolean ignoreNull) {
		if (StringUtils.isEmpty(value))
			return null;
		return new SimpleExpression(fieldName, matchMode.toMatchString(value), Operator.LIKE);
	}

	/**
	 * 大于
	 * 
	 * @param fieldName
	 * @param value
	 * @param ignoreNull
	 * @return
	 */
	public static SimpleExpression gt(String fieldName, Object value, boolean ignoreNull) {
		if (StringUtils.isEmpty(value))
			return null;
		return new SimpleExpression(fieldName, value, Operator.GT);
	}

	/**
	 * 大于等于
	 *
	 * @param fieldName
	 * @param value
	 * @param ignoreNull
	 * @return
	 */
	public static SimpleExpression gte(String fieldName, Object value, boolean ignoreNull) {
		if (StringUtils.isEmpty(value))
			return null;
		return new SimpleExpression(fieldName, value, Operator.GTE);
	}

	/**
	 * 小于
	 * 
	 * @param fieldName
	 * @param value
	 * @param ignoreNull
	 * @return
	 */
	public static SimpleExpression lt(String fieldName, Object value, boolean ignoreNull) {
		if (StringUtils.isEmpty(value))
			return null;
		return new SimpleExpression(fieldName, value, Operator.LT);
	}

	/**
	 * 小于等于
	 * 
	 * @param fieldName
	 * @param value
	 * @param ignoreNull
	 * @return
	 */
	public static SimpleExpression lte(String fieldName, Object value, boolean ignoreNull) {
		if (StringUtils.isEmpty(value))
			return null;
		return new SimpleExpression(fieldName, value, Operator.LTE);
	}

	/**
	 * 包含于
	 *
	 * @param fieldName
	 * @param value
	 * @param ignoreNull
	 * @return
	 */
	public static SimpleExpression in(String fieldName, Collection value, boolean ignoreNull) {
		if (ignoreNull && (value == null || value.isEmpty()))
			return null;
		return new SimpleExpression(fieldName, value, Operator.IN);
	}

	/**
	 * 不包含于
	 *
	 * @param fieldName
	 * @param value
	 * @param ignoreNull
	 * @return
	 */
	public static SimpleExpression notIn(String fieldName, Collection value, boolean ignoreNull) {
		if (ignoreNull && (value == null || value.isEmpty()))
			return null;
		return new SimpleExpression(fieldName, value, Operator.NOTIN);
	}

//	/**
//	 * 包含于
//	 *
//	 * @param fieldName
//	 * @param value
//	 * @param ignoreNull
//	 * @return
//	 */
//	public static LogicalExpression in(String fieldName, Collection value, boolean ignoreNull) {
//		if(ignoreNull&&(value==null||value.isEmpty())){
//			return null;
//		}
//		SimpleExpression[] ses = new SimpleExpression[value.size()];
//		int i=0;
//		for(Object obj : value){
//			ses[i]=new SimpleExpression(fieldName,obj,Operator.EQ);
//			i++;
//		}
//		return new LogicalExpression(ses,Operator.OR);
//	}


	/**
	 * 为Null
	 *
	 * @param fieldName
	 * @return
	 */
	public static SimpleExpression isNull(String fieldName) {
		return new SimpleExpression(fieldName, Operator.ISNULL);
	}


	/**
	 * 不为Null
	 *
	 * @param fieldName
	 * @return
	 */
	public static SimpleExpression isNotNull(String fieldName) {
		return new SimpleExpression(fieldName, Operator.ISNOTNULL);
	}

	/**
	 * 并且
	 * 
	 * @param criterions
	 * @return
	 */
	public static LogicalExpression and(Criterion... criterions) {
		return new LogicalExpression(criterions, Operator.AND);
	}

	/**
	 * 或者
	 * 
	 * @param criterions
	 * @return
	 */
	public static LogicalExpression or(Criterion... criterions) {
		return new LogicalExpression(criterions, Operator.OR);
	}


	/**
	 * searchParams中key的格式为OPERATOR_FIELDNAME
	 * @param searchParams
	 * @return
     */
	public static List<Criterion> parse(Map<String, Object> searchParams) {
		List<Criterion> filters = new ArrayList<Criterion>();

		for (Map.Entry<String, Object> entry : searchParams.entrySet()) {
			// 过滤掉空值
			String key = entry.getKey();
			Object value = entry.getValue();
			if (StringUtils.isEmpty(value)) {
				continue;
			}

			// 拆分operator与filedAttribute
			String[] names = org.apache.commons.lang3.StringUtils.split(key, "_");
			if (names.length != 2) {
				throw new IllegalArgumentException(key + " is not a valid search filter name");
			}
			String filedName = names[1];
			Operator operator = Operator.valueOf(names[0]);

			// 创建searchFilter
			SimpleExpression filter = new SimpleExpression(filedName, value, operator);
			filters.add(filter);
		}

		return filters;
	}

}
