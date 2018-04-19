/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.home;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtWorkOrderUser;

/**
 * 
 * 功能说明：工单管理-抄送对象
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月21日
 * @version 2.5
 *
 */
public interface GjtWorkOrderUserDao
		extends JpaRepository<GjtWorkOrderUser, String>, JpaSpecificationExecutor<GjtWorkOrderUser> {

}
