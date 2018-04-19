/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.organization;

import com.gzedu.xlims.pojo.GjtSpecialtyVideo;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年10月17日
 * @version 3.0
 *
 */
public interface GjtSpecialtyVideoService extends BaseService<GjtSpecialtyVideo> {

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月30日 上午10:21:26
	 * @param typeName
	 * @return
	 */
	GjtSpecialtyVideo queryByTypeName(String typeName);
	

}
