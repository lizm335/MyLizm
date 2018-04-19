/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.home.mobileMessage;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.mobileMessage.GjtMoblieMessageUser;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月16日
 * @version 3.0
 *
 */
public interface GjtMoblieMessageUserService {
	void insert(List<GjtMoblieMessageUser> entities);

	List<Map<String, Object>> findMobileMessageUser(String id);

	List<Map<String, Object>> findSendCount(List<String> ids);

	Page<GjtMoblieMessageUser> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	public long queryAllCount(Map<String, Object> searchParams);
}
