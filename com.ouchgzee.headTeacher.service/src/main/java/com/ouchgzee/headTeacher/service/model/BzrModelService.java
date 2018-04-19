/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.service.model;

import com.ouchgzee.headTeacher.pojo.BzrPriModelInfo;
import com.ouchgzee.headTeacher.pojo.BzrPriOperateInfo;
import com.ouchgzee.headTeacher.pojo.BzrTreeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月6日
 * @version 2.5
 *
 */
@Deprecated public interface BzrModelService {

	/**
	 * 查询主菜单
	 * 
	 * @return
	 */
	List<BzrPriModelInfo> queryAll();

	List<BzrPriModelInfo> queryMainModel(String systemName);

	List<BzrPriModelInfo> queryMainModelIn(List<String> systemNames);
	
	List<BzrPriModelInfo> queryTopModel();

	Page<BzrPriModelInfo> queryMainModel(String systemName, Pageable pageable);

	Page<BzrPriModelInfo> queryMainModel(String systemName, Map<String, Object> searchParams, PageRequest pageRequst);

	BzrPriModelInfo getModelInfo(String id);

	BzrPriModelInfo getModelInfoByName(String modelName);

	BzrPriModelInfo createPriModelInfo(BzrPriModelInfo info);

	BzrPriModelInfo updatePriModelInfo(BzrPriModelInfo info);

	BzrPriModelInfo getOne(String id);

	boolean deletePriModelInfo(String id);

	void addOperateInfo(BzrPriModelInfo modelInfo, BzrPriOperateInfo operateInfo);

	List<BzrPriModelInfo> queryAll(Iterable<String> ids);

	List<BzrTreeModel> queryModelTree();

	List<BzrTreeModel> queryModelAndOperateTree();
}
