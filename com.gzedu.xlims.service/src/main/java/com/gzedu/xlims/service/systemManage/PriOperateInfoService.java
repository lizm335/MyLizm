/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.systemManage;

import java.util.List;
import java.util.Map;

import com.gzedu.xlims.service.base.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.PriOperateInfo;

/**
 * 功能说明： 操作
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月16日
 * @version 2.5
 *
 */
public interface PriOperateInfoService extends BaseService<PriOperateInfo> {

	List<PriOperateInfo> queryAll();

	List<PriOperateInfo> queryAll(Iterable<String> ids);

	Page<PriOperateInfo> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	PriOperateInfo queryBy(String id);

	boolean update(PriOperateInfo entity);

	void delete(Iterable<String> ids);
}
