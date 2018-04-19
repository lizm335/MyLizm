/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.home.message;

import java.util.List;

import com.gzedu.xlims.pojo.message.GjtMessagePutObject;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年1月31日
 * @version 3.0
 *
 */
public interface GjtMessagePutObjectService {

	void save(List<GjtMessagePutObject> entities);

	void save(GjtMessagePutObject entity);
}
