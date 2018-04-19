/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.common.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.google.common.collect.Lists;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;

/**
 * 功能说明：
 * 
 * @author liming
 * @Date 2016年4月27日
 * @version 2.5
 *
 */
public class DynamicSpecifications {
	public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters, final Class<T> clazz) {
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (Collections3.isNotEmpty(filters)) {
					List<Predicate> predicates = toPredicates(filters, root, query, builder);
					// 将所有条件用 and 联合起来
					if (predicates.size() > 0) {
						return builder.and(predicates.toArray(new Predicate[predicates.size()]));
					}
				}

				return builder.conjunction();
			}
		};
	}

	public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters,
			final Collection<SearchFilter> filters2, final Class<T> clazz) {
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (Collections3.isNotEmpty(filters)) {
					query.distinct(true);
					List<Predicate> predicates = toPredicates(filters, root, query, builder);
					List<Predicate> predicates2 = toPredicates(filters2, root, query, builder);

					if (predicates.size() > 0) {
						Predicate and = builder.and(predicates.toArray(new Predicate[predicates.size()]));
						Predicate and2 = builder.and(predicates2.toArray(new Predicate[predicates2.size()]));
						// 将条件用 or 联合起来
						return builder.or(and, and2);

					}
				}

				return builder.conjunction();
			}
		};
	}

	private static List<Predicate> toPredicates(Collection<SearchFilter> filters, Root root, CriteriaQuery<?> query,
			CriteriaBuilder builder) {
		List<Predicate> predicates = Lists.newArrayList();
		// fromList防止表重复关联
		Map<String, From> fromList = new HashMap<String, From>();
		fromList.put("this", root);
		for (SearchFilter filter : filters) {

			String[] names = StringUtils.split(filter.fieldName, ".");
			Path expression = getPath(names, 0, root, fromList, new StringBuilder());

			Object obj = null;

			// 转换 boolean 类型
			if (expression.getJavaType().getName().equals("boolean")
					|| expression.getJavaType().equals(Boolean.class)) {
				if ((filter.value).equals("1") || (filter.value).equals(true)) {
					obj = true;
				} else if ((filter.value).equals("0") || (filter.value).equals(false)) {
					obj = false;
				}
			} else if (expression.getJavaType().equals(Date.class)) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if (String.valueOf(filter.value).length() < 11) {
						if (filter.operator.equals(Operator.GT) || filter.operator.equals(Operator.GTE)) {
							obj = sdf.parse(String.valueOf(filter.value) + " 00:00:00");
						} else {
							obj = sdf.parse(String.valueOf(filter.value) + " 23:59:59");
						}
					} else {
						obj = sdf.parse(String.valueOf(filter.value));
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				obj = filter.value;
			}

			// logic operator
			switch (filter.operator) {
				case EQ:
					predicates.add(builder.equal(expression, obj));
					break;
				case NE:
					predicates.add(builder.notEqual(expression, obj));
					break;
				case LIKE:
					if (obj.toString().indexOf('%') == -1) {
						predicates.add(builder.like(expression, "%" + obj + "%"));
					} else {
						predicates.add(builder.like(expression, obj.toString()));
					}
					break;
				case GT:
					predicates.add(builder.greaterThan(expression, (Comparable) obj));
					break;
				case LT:
					predicates.add(builder.lessThan(expression, (Comparable) obj));
					break;
				case GTE:
					predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) obj));
					break;
				case LTE:
					predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) obj));
					break;
				case IN:
					predicates.add(builder.in(expression).value(obj));
					break;
				case NOTIN:
					predicates.add(builder.not(builder.in(expression).value(obj)));
					break;
				case ISNULL:
					predicates.add(builder.isNull(expression));
					break;
				case ISNOTNULL:
					predicates.add(builder.isNotNull(expression));
					break;
				case ISEMPTY:
					predicates.add(builder.isEmpty(expression));
					break;
				case ISNOTEMPTY:
					predicates.add(builder.isNotEmpty(expression));
					break;
			}
		}
		return predicates;
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
	private static Path getPath(String[] names, int i, From childs, Map<String, From> fromList,
			StringBuilder fromKeySbd) {
		Path expression = childs.get(names[i]);
		if (i < (names.length - 1)) {
			/** begin 检查两表是否已经关联了 begin **/
			fromKeySbd.append(names[i] + ".");
			String fromKey = fromKeySbd.toString().substring(0, fromKeySbd.length() - 1);
			From childs2 = fromList.get(fromKey);
			if (childs2 == null) {
				childs2 = childs.join(names[i]);
				fromList.put(fromKey, childs2);
			}
			/** end 检查两表是否已经关联了 end **/
			return getPath(names, i + 1, childs2, fromList, fromKeySbd);
		}
		return expression;
	}

}
