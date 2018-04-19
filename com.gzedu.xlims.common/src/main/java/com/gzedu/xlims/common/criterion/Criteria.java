/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.common.criterion;

import com.gzedu.xlims.common.SerializeUtil;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 定义一个查询条件容器<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年4月29日
 * @version 2.5
 * @since JDK 1.7
 */
public class Criteria<T> implements Specification<T> {

	private List<Criterion> criterions = new ArrayList<Criterion>();

	/**
	 * 是否去重复
	 */
	private boolean distinct;

	/**
	 * 防止表重复关联，及设置连接方式
	 */
	private HashMap<String, JoinSet> fromList = new HashMap<String, JoinSet>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.data.jpa.domain.Specification#toPredicate(javax.
	 * persistence.criteria.Root, javax.persistence.criteria.CriteriaQuery,
	 * javax.persistence.criteria.CriteriaBuilder)
	 */
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if(distinct) query.distinct(true);
		if (!criterions.isEmpty()) {
			List<Predicate> predicates = new ArrayList<Predicate>();
			// 使用序列化深克隆fromList对象
			HashMap<String, JoinSet> cpFromList = SerializeUtil.unserialize(SerializeUtil.serialize(fromList));
			for (Criterion c : criterions) {
				c.setFromList(cpFromList);
				predicates.add(c.toPredicate(root, query, cb));
			}
			// 将所有条件用 and 联合起来
			if (predicates.size() > 0) {
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}
		return cb.conjunction();
	}

	/**
	 * 增加简单条件表达式
	 *
	 * @param criterion
	 */
	public void add(Criterion criterion) {
		if (criterion != null) {
			criterions.add(criterion);
		}
	}

	/**
	 * 增加多个简单条件表达式
	 *
	 * @param criterions
	 */
	public void addAll(List<Criterion> criterions) {
		if (criterions != null && criterions.size() != 0) {
			this.criterions.addAll(criterions);
		}
	}

	/**
	 * 设置是否去重复
	 * @param distinct
     */
	public void distinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * 提前设置表的连接方式
	 * @param fromKey
	 * @param joinType
     */
	public void setJoinType(String fromKey, JoinType joinType) {
		JoinSet joinSet = new JoinSet(joinType);
		fromList.put(fromKey, joinSet);
	}

}
