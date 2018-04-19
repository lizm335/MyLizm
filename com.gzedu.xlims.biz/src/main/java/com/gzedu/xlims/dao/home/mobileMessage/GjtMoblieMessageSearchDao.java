/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.home.mobileMessage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.mobileMessage.GjtMoblieMessageSearch;

/**
 * 功能说明：查询用户条件
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月16日
 * @version 3.0
 *
 */
public interface GjtMoblieMessageSearchDao
		extends JpaRepository<GjtMoblieMessageSearch, String>, JpaSpecificationExecutor<GjtMoblieMessageSearch> {

}
