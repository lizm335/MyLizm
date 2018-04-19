/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.home;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtWorkOrderAssignPerson;

/**
 * 
 * 功能说明：工单任务-指定人
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年9月5日
 * @version 3.0
 *
 */
public interface GjtWorkOrderAssignPersonService {

	public Page<GjtWorkOrderAssignPerson> queryPageInfo(Map<String, Object> searchParams, PageRequest pageRequst);

	public long queryAllCount(Map<String, Object> searchParams);

	public GjtWorkOrderAssignPerson save(GjtWorkOrderAssignPerson item);

}
