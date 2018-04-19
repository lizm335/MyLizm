/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.graduation;

import com.gzedu.xlims.pojo.graduation.GjtPhotographData;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月28日
 * @version 3.0
 *
 */
public interface GjtPhotographDataService {
	GjtPhotographData save(GjtPhotographData entity);

	GjtPhotographData update(GjtPhotographData entity);

	GjtPhotographData findByXxId(String xxId);
}
