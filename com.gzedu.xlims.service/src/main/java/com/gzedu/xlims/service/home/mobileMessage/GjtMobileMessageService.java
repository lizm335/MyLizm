/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.home.mobileMessage;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.mobileMessage.GjtMobileMessage;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月16日
 * @version 3.0
 *
 */
public interface GjtMobileMessageService {
	Page<GjtMobileMessage> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	long queryAllCount(Map<String, Object> searchParams);

	GjtMobileMessage save(GjtMobileMessage entity);

	GjtMobileMessage queryById(String id);

	GjtMobileMessage update(GjtMobileMessage entity);

}
