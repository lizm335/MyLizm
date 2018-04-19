/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.usermanage;

import java.util.List;

import com.gzedu.xlims.pojo.PriRoleInfoRun;

/**
 * 
 * 功能说明：工单发送角色
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月25日
 * @version 2.5
 *
 */
public interface PriRoleInfoRunService {

	public void save(List<PriRoleInfoRun> entity);

	public void delete(String roleId);
}
