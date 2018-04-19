/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.PriModelOperate;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月8日
 * @version 2.5
 *
 */
public interface PriModelOperateDao
		extends JpaRepository<PriModelOperate, String>, JpaSpecificationExecutor<PriModelOperate> {

}
