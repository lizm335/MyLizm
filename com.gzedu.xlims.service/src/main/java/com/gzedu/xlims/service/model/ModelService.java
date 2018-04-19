/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.gzedu.xlims.pojo.PriModelInfo;
import com.gzedu.xlims.pojo.PriOperateInfo;
import com.gzedu.xlims.pojo.TreeModel;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月6日
 * @version 2.5
 *
 */
public interface ModelService {

	/**
	 * 查询主菜单
	 * 
	 * @return
	 */
	List<PriModelInfo> queryAll();

	List<PriModelInfo> queryMainModel(String systemName);

	List<PriModelInfo> queryMainModelIn(List<String> systemNames);
	
	List<PriModelInfo> queryTopModel();

	Page<PriModelInfo> queryMainModel(String systemName, Pageable pageable);

	Page<PriModelInfo> queryMainModel(String systemName, Map<String, Object> searchParams, PageRequest pageRequst);

	PriModelInfo getModelInfo(String id);

	PriModelInfo getModelInfoByName(String modelName);

	PriModelInfo createPriModelInfo(PriModelInfo info);

	PriModelInfo updatePriModelInfo(PriModelInfo info);

	PriModelInfo getOne(String id);

	boolean deletePriModelInfo(String id);

	void addOperateInfo(PriModelInfo modelInfo, PriOperateInfo operateInfo);

	List<PriModelInfo> queryAll(Iterable<String> ids);

	List<TreeModel> queryModelTree();

	List<TreeModel> queryModelAndOperateTree();
}
