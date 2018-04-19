/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.home;

import com.gzedu.xlims.pojo.GjtWorkOrderComment;

/**
 * 
 * 功能说明：工单详情-讨论内容详情接口
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月21日
 * @version 2.5
 *
 */
public interface GjtWorkOrderCommentService {

	// 增加讨论内容
	public GjtWorkOrderComment save(GjtWorkOrderComment item);

	// 修改讨论内容
	public GjtWorkOrderComment update(GjtWorkOrderComment item);

	// 删除讨论内容
	public boolean delete(String id);

}
