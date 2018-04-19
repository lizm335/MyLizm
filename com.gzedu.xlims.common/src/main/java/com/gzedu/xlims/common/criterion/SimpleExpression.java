/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common.criterion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单条件表达式<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年4月29日
 * @version 2.5
 * @since JDK 1.7
 */
public class SimpleExpression implements Criterion {

	private static Logger log = LoggerFactory.getLogger(SimpleExpression.class);

	private String fieldName; // 属性名
	private Object value; // 对应值
	private Operator operator; // 计算符

	private Map<String, JoinSet> fromList;

	protected SimpleExpression(String fieldName, Object value, Operator operator) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}

	protected SimpleExpression(String fieldName, Operator operator) {
		this.fieldName = fieldName;
		this.operator = operator;
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @return the operator
	 */
	public Operator getOperator() {
		return operator;
	}

	public void setFromList(Map<String, JoinSet> fromList) {
		this.fromList = fromList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.gzedu.xlims.common.criterion.Criterion#toPredicate(javax.
	 * persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery,
	 * javax.persistence.criteria.CriteriaBuilder)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		Path expression = null;
		if (fieldName.contains(".")) {
			String[] names = fieldName.split("\\.");
			expression = getPath(names, 0, root, fromList, new StringBuilder());
		} else {
			expression = root.get(fieldName);
		}

		Object obj = null;
		// 转换 boolean 类型
		if (expression.getJavaType().equals(Boolean.class)) {
			if ("1".equals(value) || (value).equals(true)) {
				obj = true;
			} else if ("0".equals(value) || (value).equals(false)) {
				obj = false;
			}
		} else if (expression.getJavaType().equals(Date.class)) {
			try {
				// 日期格式yyyy-MM-dd
				Pattern pattern = Pattern.compile("^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])$");
				Matcher matcher = pattern.matcher(String.valueOf(value));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				if (matcher.matches()) {
					if (operator == Operator.GT || operator == Operator.GTE) {
						obj = sdf.parse(String.valueOf(value) + " 00:00:00");
					} else {
						obj = sdf.parse(String.valueOf(value) + " 23:59:59");
					}
				} else {
					obj = sdf.parse(String.valueOf(value));
				}
			} catch (ParseException e) {
				log.error("时间类型转化异常", e);
			}
		} else {
			obj = value;
		}

		switch (operator) {
			case EQ:
				return builder.equal(expression, obj);
			case NE:
				return builder.notEqual(expression, obj);
			case LIKE:
				if (obj.toString().indexOf('%') == -1) {
					return builder.like((Expression<String>) expression, "%" + obj + "%");
				} else {
					return builder.like((Expression<String>) expression, obj.toString());
				}
			case LT:
				return builder.lessThan(expression, (Comparable) obj);
			case GT:
				return builder.greaterThan(expression, (Comparable) obj);
			case LTE:
				return builder.lessThanOrEqualTo(expression, (Comparable) obj);
			case GTE:
				return builder.greaterThanOrEqualTo(expression, (Comparable) obj);
			case IN:
				return builder.in(expression).value((Collection) obj);
			case NOTIN:
				return builder.not(builder.in(expression).value((Collection) obj));
			case ISNULL:
				return builder.isNull(expression);
			case ISNOTNULL:
				return builder.isNotNull(expression);
			default:
				return null;
		}
	}

	/**
	 * 关联表与表之间的关系，获取查询字段的Path
	 * 
	 * @param names
	 *            查询字段数值
	 * @param i
	 *            索引
	 * @param childs
	 *            关联表
	 * @param fromList
	 *            防止表重复关联
	 * @return
	 */
	private static Path getPath(String[] names, int i, From childs, Map<String, JoinSet> fromList,
			StringBuilder fromKeySbd) {
		Path expression = childs.get(names[i]);
		if (i < (names.length - 1)) {
			/** begin 检查两表是否已经关联了 begin **/
			fromKeySbd.append(names[i] + ".");
			String fromKey = fromKeySbd.toString().substring(0, fromKeySbd.length() - 1);
			JoinSet joinSet = fromList.get(fromKey);
			Join childs2 = null;
			if (joinSet == null) {
				childs2 = childs.join(names[i]);
				fromList.put(fromKey, new JoinSet(childs2));
			} else if(joinSet.getJoin() == null) {
				childs2 = childs.join(names[i], joinSet.getJoinType());
				joinSet.setJoin(childs2);
			} else {
				childs2 = joinSet.getJoin();
			}
			/** end 检查两表是否已经关联了 end **/
			return getPath(names, i + 1, childs2, fromList, fromKeySbd);
		}
		return expression;
	}

}
