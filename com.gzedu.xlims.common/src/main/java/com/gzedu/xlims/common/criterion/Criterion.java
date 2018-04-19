/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common.criterion;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;

/**
 * 条件接口<br>
 * 功能说明： 用户提供条件表达式接口
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年4月29日
 * @version 2.5
 * @since JDK 1.7
 */
public interface Criterion {

	public enum Operator {
		EQ, NE, LIKE, GT, LT, GTE, LTE, IN, NOTIN, ISNULL, ISNOTNULL, AND, OR
	}

	public void setFromList(Map<String, JoinSet> fromList);

	public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder);

}
