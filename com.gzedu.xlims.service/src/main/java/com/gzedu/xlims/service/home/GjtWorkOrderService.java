/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.home;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtWorkOrder;

/**
 * 
 * 功能说明：工单管理接口
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月21日
 * @version 2.5
 *
 */
public interface GjtWorkOrderService {

	public Page<GjtWorkOrder> queryPageInfo(Map<String, Object> searchParams, PageRequest pageRequst);

	public long queryAllCount(Map<String, Object> searchParams);

	public GjtWorkOrder queryById(String id);

	public GjtWorkOrder save(GjtWorkOrder item);

	public GjtWorkOrder update(GjtWorkOrder item);

	public boolean delete(String id);

	public boolean updateIsState(String id, String isState);

}
