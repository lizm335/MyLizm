/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.daoImpl.base;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作基础类<br/>
 * 自写HQL语句，SQL语句数据库操作，参考org.springframework.data.jpa.repository.support.
 * SimpleJpaRepository <br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月26日
 * @version 2.5
 * @since JDK 1.7
 */
public class BaseDaoImpl {

	@PersistenceContext(unitName = "entityManagerFactoryBzr")
	protected EntityManager em;

	/**
	 * 分页查询，使用HQL语句拼接
	 *
	 * @param queryHql
	 * @param params
	 * @param pageRequest
	 * @param resultClass
	 * @param <T>
	 * @return
	 */
	public <T> Page<T> findByPageHql(StringBuilder queryHql, Map<String, Object> params, PageRequest pageRequest, Class<T> resultClass) {
		long total = countByHqlQuery(queryHql, params);
		Page<T> page = new PageImpl(Collections.emptyList(), pageRequest, total);
		// 根据总条数判断查询的当页是否有数据
		if (total > pageRequest.getOffset()) {
			if (pageRequest.getSort() != null) {
				queryHql.append(" ORDER BY ");
				for (Iterator<Sort.Order> iter = pageRequest.getSort().iterator(); iter.hasNext(); ) {
					Sort.Order order = iter.next();
					queryHql.append(order.getProperty());
					if (!order.isAscending())
						queryHql.append(" DESC");
					queryHql.append(",");
				}
				queryHql.setLength(queryHql.length() - 1);
			}

			TypedQuery<T> tq = em.createQuery(queryHql.toString(), resultClass);
			for (Map.Entry<String, Object> param : params.entrySet()) {
				tq.setParameter(param.getKey(), param.getValue());
			}
			tq.setFirstResult(pageRequest.getOffset());
			tq.setMaxResults(pageRequest.getPageSize());
			page = new PageImpl(tq.getResultList(), pageRequest, total);
		}
		return page;
	}

	/**
	 * 根据条件查询所有，使用HQL语句拼接
	 *
	 * @param queryHql
	 * @param params
	 * @param resultClass
	 * @param <T>
	 * @return
	 */
	public <T> List<T> findAllByHql(StringBuilder queryHql, Map<String, Object> params, Sort sort, Class<T> resultClass) {
		if (sort != null) {
			queryHql.append(" ORDER BY ");
			for (Iterator<Sort.Order> iter = sort.iterator(); iter.hasNext(); ) {
				Sort.Order order = iter.next();
				queryHql.append(order.getProperty());
				if (!order.isAscending())
					queryHql.append(" DESC");
				queryHql.append(",");
			}
			queryHql.setLength(queryHql.length() - 1);
		}

		TypedQuery<T> tq = em.createQuery(queryHql.toString(), resultClass);
		for (Map.Entry<String, Object> param : params.entrySet()) {
			tq.setParameter(param.getKey(), param.getValue());
		}
		return tq.getResultList();
	}

	/**
	 * 根据条件查询记录条数，FROM前面的SELECT可省略
	 *
	 * @param countHql
	 * @param params
     * @return
     */
	public long countByHql(StringBuilder countHql, Map<String, Object> params) {
		TypedQuery<Long> tqc = em.createQuery(countHql.toString(), Long.class);
		for (Map.Entry<String, Object> param : params.entrySet()) {
			tqc.setParameter(param.getKey(), param.getValue());
		}
		long total = tqc.getSingleResult();
		return total;
	}

	protected long countByHqlQuery(StringBuilder queryHql, Map<String, Object> params) {
		TypedQuery<Long> tqc = em.createQuery(getCountSql(queryHql), Long.class);
		for (Map.Entry<String, Object> param : params.entrySet()) {
			tqc.setParameter(param.getKey(), param.getValue());
		}
		long total = tqc.getSingleResult();
		return total;
	}

	//--------------------------------------- HQL和SQL 华丽丽的分割线 牛不牛掰 ---------------------------------------//

	/**
	 * 分页查询，使用SQL语句拼接
	 *
	 * @param querySql
	 * @param params
	 * @param pageRequest
	 * @return
	 */
	public Page<Map> findByPageToMap(StringBuilder querySql, Map<String, Object> params, PageRequest pageRequest) {
		return findByPageSql(querySql, params, pageRequest, Map.class);
	}

	/**
	 * 分页查询，使用SQL语句拼接
	 *
	 * @param querySql
	 * @param params
	 * @param pageRequest
	 * @return
	 */
	public Page<List> findByPageToList(StringBuilder querySql, Map<String, Object> params, PageRequest pageRequest) {
		return findByPageSql(querySql, params, pageRequest, List.class);
	}

	/**
	 * 分页查询，使用SQL语句拼接
	 *
	 * @param querySql
	 * @param params
	 * @param pageRequest
	 * @param resultClass
	 * @param <T>         Map List Object
	 * @return
	 */
	public <T> Page<T> findByPageSql(StringBuilder querySql, Map<String, Object> params, PageRequest pageRequest, Class<T> resultClass) {
		long total = countBySqlQuery(querySql, params);
		Page<T> page = new PageImpl(Collections.emptyList(), pageRequest, total);
		// 根据总条数判断查询的当页是否有数据
		if (total > pageRequest.getOffset()) {
			if (pageRequest.getSort() != null) {
				querySql.append(" ORDER BY ");
				for (Iterator<Sort.Order> iter = pageRequest.getSort().iterator(); iter.hasNext(); ) {
					Sort.Order order = iter.next();
					querySql.append(order.getProperty());
					if (!order.isAscending())
						querySql.append(" DESC");
					querySql.append(",");
				}
				querySql.setLength(querySql.length() - 1);
			}

			Query q = em.createNativeQuery(querySql.toString());
			// 数据返回的类型
			if (resultClass == Map.class) {
				q.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			} else if (resultClass == List.class) {
				q.unwrap(SQLQuery.class).setResultTransformer(Transformers.TO_LIST);
			} else {
				// 转为对象时，由于Oracle的特性列名都是大写，则该对象的属性必须也全为大写
				q.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(resultClass));
			}
			for (Map.Entry<String, Object> param : params.entrySet()) {
				q.setParameter(param.getKey(), param.getValue());
			}
			q.setFirstResult(pageRequest.getOffset());
			q.setMaxResults(pageRequest.getPageSize());
			page = new PageImpl(q.getResultList(), pageRequest, total);
		}
		return page;
	}

	/**
	 * 根据条件查询记录条数
	 *
	 * @param countSql
	 * @param params
     * @return
     */
	public long countBySql(StringBuilder countSql, Map<String, Object> params) {
		Query qc = em.createNativeQuery(countSql.toString());
		for (Map.Entry<String, Object> param : params.entrySet()) {
			qc.setParameter(param.getKey(), param.getValue());
		}
		long total = ((BigDecimal) qc.getSingleResult()).longValue();
		return total;
	}

	protected long countBySqlQuery(StringBuilder querySql, Map<String, Object> params) {
		Query qc = em.createNativeQuery(getCountSql(querySql));
		for (Map.Entry<String, Object> param : params.entrySet()) {
			qc.setParameter(param.getKey(), param.getValue());
		}
		long total = ((BigDecimal) qc.getSingleResult()).longValue();
		return total;
	}

	/**
	 * 根据条件查询所有，使用SQL语句拼接
	 * 
	 * @param querySql
	 * @param params
     * @return
     */
	public List<Map> findAllByToMap(StringBuilder querySql, Map<String, Object> params, Sort sort) {
		return findAllBySql(querySql, params, sort, Map.class);
	}

	/**
	 * 根据条件查询所有，使用SQL语句拼接
	 * 
	 * @param querySql
	 * @param params
     * @return
     */
	public List<List> findAllByToList(StringBuilder querySql, Map<String, Object> params, Sort sort) {
		return findAllBySql(querySql, params, sort, List.class);
	}

	/**
	 * 根据条件查询所有，使用SQL语句拼接
	 * 
	 * @param querySql
	 * @param params
	 * @param resultClass
	 * @param <T>
     * @return
     */
	public <T> List<T> findAllBySql(StringBuilder querySql, Map<String, Object> params, Sort sort, Class<T> resultClass) {
		if (sort != null) {
			querySql.append(" ORDER BY ");
			for (Iterator<Sort.Order> iter = sort.iterator(); iter.hasNext(); ) {
				Sort.Order order = iter.next();
				querySql.append(order.getProperty());
				if (!order.isAscending())
					querySql.append(" DESC");
				querySql.append(",");
			}
			querySql.setLength(querySql.length() - 1);
		}
		
		Query q = em.createNativeQuery(querySql.toString());
		// 数据返回的类型
		if (resultClass == Map.class) {
			q.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		} else if (resultClass == List.class) {
			q.unwrap(SQLQuery.class).setResultTransformer(Transformers.TO_LIST);
		} else {
			// 转为对象时，由于Oracle的特性列名都是大写，则该对象的属性必须也全为大写
			q.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(resultClass));
		}
		for (Map.Entry<String, Object> param : params.entrySet()) {
			q.setParameter(param.getKey(), param.getValue());
		}
		return q.getResultList();
	}

	/**
	 * 获取统计HQL/SQL
	 * @param querySql
	 * @return
	 */
	protected String getCountSql(StringBuilder querySql) {
		String upperSql = querySql.toString().toUpperCase();
		int tIndex = upperSql.indexOf(" T "); // 是否有主表标识
		String countSql;
		if(tIndex != -1) {
			int groupIndex = upperSql.lastIndexOf("GROUP BY");
			int partitionIndex = upperSql.lastIndexOf("PARTITION BY");
			boolean multiWhere = upperSql.indexOf(" WHERE ", tIndex) != upperSql.lastIndexOf(" WHERE ", upperSql.indexOf(" WHERE ", tIndex));
			if (multiWhere || partitionIndex != -1) {
				countSql = "SELECT COUNT(*) FROM (" + querySql + ") TEMP";
			} else if (groupIndex == -1) { // 没有分组时
				// 找到真正的FROM主表索引位置
				int fromIndex = upperSql.lastIndexOf(" FROM ", tIndex);
				countSql = "SELECT COUNT(*) " + querySql.substring(fromIndex);
			} else {
				countSql = "SELECT COUNT(*) FROM (" + querySql + ") TEMP";
			}
		} else {
			int groupIndex = upperSql.lastIndexOf("GROUP BY");
			int partitionIndex = upperSql.lastIndexOf("PARTITION BY");
			boolean multiWhere = upperSql.indexOf(" WHERE ") != upperSql.lastIndexOf(" WHERE ");
			if (multiWhere || partitionIndex != -1) {
				countSql = "SELECT COUNT(*) FROM (" + querySql + ") TEMP";
			} else if (groupIndex == -1) { // 没有分组时
				// 找到真正的FROM主表索引位置
				int joinIndex = upperSql.indexOf(" JOIN ");
				int whereIndex = upperSql.lastIndexOf(" WHERE ");
				int endIndex = joinIndex != -1 ? joinIndex
						: whereIndex != -1 ? whereIndex
						: groupIndex != -1 ? groupIndex : querySql.length();
				int fromIndex = upperSql.lastIndexOf(" FROM ", endIndex);
				countSql = "SELECT COUNT(*) "
						+ querySql.substring(fromIndex);
			} else {
				countSql = "SELECT COUNT(*) FROM (" + querySql + ") TEMP";
			}
		}
		return countSql;
	}
	
}
