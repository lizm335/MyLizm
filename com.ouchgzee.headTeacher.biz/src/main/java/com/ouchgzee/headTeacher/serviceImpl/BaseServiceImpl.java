package com.ouchgzee.headTeacher.serviceImpl;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.Identifiable;
import com.ouchgzee.headTeacher.dao.BaseDao;
import com.ouchgzee.headTeacher.service.BaseService;


/**
 * @Function: TODO ADD FUNCTION.
 * @ClassName: BaseServiceImpl 
 * @date: 2016年4月17日 下午11:25:17 
 *
 * @author  zhy
 * @version V2.5
 * @since   JDK 1.6
 */
public abstract class BaseServiceImpl<T extends Identifiable> implements BaseService<T> {

	/**
	 * 获取基础数据库操作类
	 * @return
	 */
	protected abstract BaseDao<T> getBaseDao();

	
	public <V extends T> V queryOne(T query) {
		return getBaseDao().selectOne(query);
	}

	
	public <V extends T> V queryById(String id) {
		return getBaseDao().selectById(id);
	}

	
	public <V extends T> List<V> queryList(T query) {
		return getBaseDao().selectList(query);
	}

	
	public <V extends T> List<V> queryAll() {
		return getBaseDao().selectAll();
	}

	
	public <K, V extends T> Map<K, V> queryMap(T query, String mapKey) {
		return getBaseDao().selectMap(query, mapKey);
	}

	
	public Long queryCount() {
		return getBaseDao().selectCount();
	}

	
	public Long queryCount(T query) {
		return getBaseDao().selectCount(query);
	}

	
	public void insert(T entity) {
		getBaseDao().insert(entity);
	}

	
	public int delete(T query) {
		return getBaseDao().delete(query);
	}

	
	public int deleteById(String id) {
		return getBaseDao().deleteById(id);
	}

	
	public int deleteAll() {
		return getBaseDao().deleteAll();
	}

	
	public int updateById(T entity) {
		return getBaseDao().updateById(entity);
	}

	
	public int updateByIdSelective(T entity) {
		return getBaseDao().updateByIdSelective(entity);
	}

	
	@Transactional(value="transactionManagerBzr")
	public void deleteByIdInBatch(List<String> idList) {
		getBaseDao().deleteByIdInBatch(idList);
	}

	
	@Transactional(value="transactionManagerBzr")
	public void insertInBatch(List<T> entityList) {
		getBaseDao().insertInBatch(entityList);
	}

	
	@Transactional(value="transactionManagerBzr")
	public void updateInBatch(List<T> entityList) {
		getBaseDao().updateInBatch(entityList);
	}

	
	public <V extends T> List<V> queryList(T query, Pageable pageable) {
		return getBaseDao().selectList(query, pageable);
	}

	
	public <V extends T> Page<V> queryPageList(T query, Pageable pageable) {
		return getBaseDao().selectPageList(query, pageable);
	}

	
	public <K, V extends T> Map<K, V> queryMap(T query, String mapKey, Pageable pageable) {
		return getBaseDao().selectMap(query, mapKey, pageable);
	}

}
