/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common.criterion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * 逻辑条件表达式<br>
 * 功能说明：用于复杂条件时使用，如但属性多对应值的OR查询等
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年4月29日
 * @version 2.5
 * @since JDK 1.7
 */
public class LogicalExpression implements Criterion {

	private Criterion[] criterion; // 逻辑表达式中包含的表达式
	private Operator operator; // 计算符

	private Map<String, JoinSet> fromList;

	protected LogicalExpression(Criterion[] criterions, Operator operator) {
		this.criterion = criterions;
		this.operator = operator;
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
	public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		for (int i = 0; i < this.criterion.length; i++) {
			this.criterion[i].setFromList(fromList);
			predicates.add(this.criterion[i].toPredicate(root, query, builder));
		}
		switch (operator) {
			case AND:
				return builder.and(predicates.toArray(new Predicate[predicates.size()]));
			case OR:
				return builder.or(predicates.toArray(new Predicate[predicates.size()]));
			default:
				return null;
		}
	}

}
