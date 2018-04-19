/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * 对象业务基础接口类<br>
 * 功能说明：实现基础的业务逻辑，如果不满足当前的业务，可重写方法或者新增方法
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月28日
 * @version 2.5
 * @since JDK 1.7
 */
public interface BaseService<T> {

	/**
	 * 分页根据条件查询对象信息
	 * 
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	Page<T> queryByPage(Map<String, Object> searchParams, PageRequest pageRequest);

	/**
	 * 根据条件查询对象信息
	 * 
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	List<T> queryBy(Map<String, Object> searchParams, Sort sort);

	/**
	 * 根据ID查询对象信息
	 * 
	 * @param id
	 * @return
	 */
	T queryById(String id);

	/**
	 * 根据条件查询对象的记录条数
	 *
	 * @param searchParams
	 * @return
     */
	long count(Map<String, Object> searchParams);

	/**
	 * 添加对象信息
	 *
	 * @param entity
	 * @return
	 */
	boolean insert(T entity);

	/**
	 * 删除对象信息
	 *
	 * @param id
	 * @return
	 */
	boolean delete(String id);

	/**
	 * 删除对象信息
	 *
	 * @param entity
	 * @return
	 */
	boolean delete(T entity);
	
}
