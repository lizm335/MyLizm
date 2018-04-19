package com.gzedu.xlims.dao.comm;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
public class CommonDao {

	@PersistenceContext(unitName = "entityManagerFactory")
	private EntityManager em;

	/**
	 * 查询分页对象
	 * 
	 * @param params
	 * @param pageRequst
	 * @return
	 */
	public Page<?> queryForPage(String hql, Map<String, Object> params, PageRequest pageRequst) {
		Query query = em.createQuery(hql);

		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		long count = query.getResultList().size();

		query.setFirstResult(pageRequst.getOffset());
		query.setMaxResults(pageRequst.getPageSize());
		List<?> resultList = query.getResultList();

		@SuppressWarnings({ "unchecked", "rawtypes" })
		Page<?> page = new PageImpl(resultList, pageRequst, count);

		return page;
	}

	/**
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年10月20日 下午5:03:32
	 * @param Hql
	 * @param params
	 * @param clazz
	 * @return
	 */
	public <T> List<T> queryForList(String hql, Map<String, Object> params, Class<T> clazz) {
		Query query = em.createQuery(hql, clazz);

		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		List<T> resultList = query.getResultList();
		return resultList;
	}

	/**
	 * 
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 */
	public <T> List<T> querySqlForList(String sql, Map<String, Object> params, Class<T> clazz) {
		Query query = em.createNativeQuery(sql, clazz);

		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}
		List<T> resultList = query.getResultList();
		return resultList;
	}

	/**
	 * 查询分页对象（原生sql）
	 * 
	 * @param sql
	 * @param params
	 * @param pageRequst
	 * @param clazz
	 * @return
	 */
	public <T> Page<T> queryForPageNative(String sql, Map<String, Object> params, PageRequest pageRequst,
			Class<T> clazz) {
		String countSql = "select count(1) from (" + sql + ")";
		Query queryCount = em.createNativeQuery(countSql);
		Query query = em.createNativeQuery(sql, clazz);

		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				queryCount.setParameter(entry.getKey(), entry.getValue());
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		long count = NumberUtils.toLong(queryCount.getSingleResult().toString());

		query.setFirstResult(pageRequst.getOffset());
		query.setMaxResults(pageRequst.getPageSize());
		@SuppressWarnings("unchecked")
		List<T> resultList = query.getResultList();

		Page<T> page = new PageImpl<T>(resultList, pageRequst, count);

		return page;
	}

	/**
	 * 查询分页对象（原生sql）
	 * 
	 * @param sql
	 * @param params
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryForPageNative(String sql, Map<String, Object> params,
			PageRequest pageRequst) {
		String countSql = "select count(1) from (" + sql + ")";
		Query queryCount = em.createNativeQuery(countSql);
		Query query = em.createNativeQuery(sql);

		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				queryCount.setParameter(entry.getKey(), entry.getValue());
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		long count = NumberUtils.toLong(queryCount.getSingleResult().toString());

		query.setFirstResult(pageRequst.getOffset());
		query.setMaxResults(pageRequst.getPageSize());
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> resultList = query.getResultList();

		Page<Map<String, Object>> page = new PageImpl<Map<String, Object>>(resultList, pageRequst, count);

		return page;
	}

	/**
	 * 查询对象数量（原生sql）
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public long queryForCountNative(String sql, Map<String, Object> params) {
		String countSql = "select count(1) from (" + sql + ")";
		Query queryCount = em.createNativeQuery(countSql);

		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				queryCount.setParameter(entry.getKey(), entry.getValue());
			}
		}

		long count = NumberUtils.toLong(queryCount.getSingleResult().toString());
		return count;
	}

	/**
	 * 查询列表（原生sql）
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Object[]> queryForObjectListNative(String sql, Map<String, Object> params) {
		Query query = em.createNativeQuery(sql);

		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		@SuppressWarnings("unchecked")
		List<Object[]> resultList = query.getResultList();

		return resultList;
	}

	/**
	 * 查询列表（原生sql）
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<String> queryForStringListNative(String sql, Map<String, Object> params) {
		Query query = em.createNativeQuery(sql);

		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		@SuppressWarnings("unchecked")
		List<String> resultList = query.getResultList();

		return resultList;
	}

	/**
	 * 查询分页对象（原生sql）
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map<String, String>> queryForMapListNative(String sql, Map<String, Object> params) {
		Query query = em.createNativeQuery(sql);
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		@SuppressWarnings("unchecked")
		List<Map<String, String>> resultList = query.getResultList();
		return resultList;
	}

	/**
	 * 原生SQL查询
	 * 
	 * @param sql
	 * @param params
	 * @return Map<String, Object>
	 */
	public List<Map<String, Object>> queryForMapList(String sql, Map<String, Object> params) {
		Query query = em.createNativeQuery(sql);
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> resultList = query.getResultList();
		return resultList;
	}

	/**
	 * 查询分页对象（原生sql）
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> queryForStringObjectMapListNative(String sql, Map<String, Object> params) {
		Query query = em.createNativeQuery(sql);
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> resultList = query.getResultList();
		return resultList;
	}

	/**
	 * 查询键值对集合（原生sql）
	 * 
	 * @param sql
	 * @return
	 */
	public Map<String, String> getMapNative(String sql) {
		/*
		 * Query query = em.createNativeQuery(sql);
		 * query.unwrap(SQLQuery.class).setResultTransformer(Transformers.
		 * ALIAS_TO_ENTITY_MAP);
		 * 
		 * @SuppressWarnings("unchecked") List<Object> rows =
		 * query.getResultList(); Map<String, String> resultMap = new
		 * LinkedHashMap<String, String>(); for (Object obj : rows) {
		 * 
		 * @SuppressWarnings("unchecked") Map<String, String> m = (Map<String,
		 * String>) obj; resultMap.put(m.get("ID"), m.get("NAME")); } return
		 * resultMap;
		 */
		return getMapNative(sql, null);
	}

	/**
	 * 查询键值对集合（原生sql）
	 * 
	 * @param sql
	 * @return
	 */
	public Map<String, String> getMapNative(String sql, Map<String, Object> params) {
		Map<String, String> resultMap = new LinkedHashMap<String, String>();

		Query query = em.createNativeQuery(sql);
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		@SuppressWarnings("unchecked")
		List<Map<String, String>> rows = query.getResultList();
		for (Map<String, String> m : rows) {
			resultMap.put(m.get("ID"), m.get("NAME"));
		}

		return resultMap;
	}

	/**
	 * 查询对象，以map返回（原生sql）
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public Map<String, Object> queryObjectToMapNative(String sql, Map<String, Object> params) {
		Query query = em.createNativeQuery(sql);

		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> resultMap = (Map<String, Object>) query.getSingleResult();
			return resultMap;
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * 查询对象（原生sql）
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public Object queryObjectNative(String sql, Map<String, Object> params) {
		Query query = em.createNativeQuery(sql);

		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	/**
	 * 插入数据(原生SQL)
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public int insertForMapNative(String sql, Map<String, Object> params) {
		Query query = em.createNativeQuery(sql);
		for (Map.Entry<String, Object> e : params.entrySet()) {
			query.setParameter(e.getKey(), e.getValue());
		}
		try {
			return query.executeUpdate();
		} catch (NoResultException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 更新数据(原生SQL)
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public int updateForMapNative(String sql, Map<String, Object> params) {
		Query query = em.createNativeQuery(sql);
		for (Map.Entry<String, Object> e : params.entrySet()) {
			query.setParameter(e.getKey(), e.getValue());
		}
		try {
			return query.executeUpdate();
		} catch (NoResultException e) {
			return 0;
		}
	}

	public <T> Page<T> queryBeanForPageNative(String sql, Map<String, Object> params, PageRequest pageRequst, Class<T> clazz) {
		String countSql = "select count(1) from (" + sql + ")";
		Query queryCount = em.createNativeQuery(countSql);
		Query query = em.createNativeQuery(sql);

		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				queryCount.setParameter(entry.getKey(), entry.getValue());
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		long count = NumberUtils.toLong(queryCount.getSingleResult().toString());

		query.setFirstResult(pageRequst.getOffset());
		query.setMaxResults(pageRequst.getPageSize());
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(clazz));
		@SuppressWarnings("unchecked")
		List<T> resultList = query.getResultList();

		Page<T> page = new PageImpl<T>(resultList, pageRequst, count);

		return page;
	}

}
