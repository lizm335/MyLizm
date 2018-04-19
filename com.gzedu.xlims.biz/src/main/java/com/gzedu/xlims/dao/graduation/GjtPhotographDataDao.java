/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.graduation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.graduation.GjtPhotographData;

/**
 * 功能说明：拍摄点资料录入
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月28日
 * @version 3.0
 *
 */
public interface GjtPhotographDataDao
		extends JpaRepository<GjtPhotographData, String>, JpaSpecificationExecutor<GjtPhotographData> {

	public GjtPhotographData findByXxId(String xxId);
}
