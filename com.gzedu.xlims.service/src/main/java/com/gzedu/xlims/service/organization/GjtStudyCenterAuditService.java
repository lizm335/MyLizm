/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.organization;

import java.util.List;

import com.gzedu.xlims.pojo.GjtStudyCenterAudit;

/**
 * 
 * 功能说明：学习中心审核
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月15日
 * @version 3.0
 *
 */
public interface GjtStudyCenterAuditService {

	List<GjtStudyCenterAudit> queryByIncidentId(String orgId);

	boolean update(GjtStudyCenterAudit item);

	boolean save(GjtStudyCenterAudit item);
}
