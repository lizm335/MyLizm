/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.home.mobileMessage;

import java.util.List;

import com.gzedu.xlims.pojo.mobileMessage.GjtMoblieMessageSearch;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月16日
 * @version 3.0
 *
 */
public interface GjtMoblieMessageSearchService {

	GjtMoblieMessageSearch save(GjtMoblieMessageSearch entity);

	void save(List<GjtMoblieMessageSearch> entities);

}
