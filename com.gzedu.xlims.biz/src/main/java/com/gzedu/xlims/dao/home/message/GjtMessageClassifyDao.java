/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.home.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.message.GjtMessageClassify;

/**
 * 
 * 功能说明：通知类型分类表，最多只能6个
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年1月11日
 * @version 3.0
 *
 */
public interface GjtMessageClassifyDao
		extends JpaRepository<GjtMessageClassify, String>, JpaSpecificationExecutor<GjtMessageClassify> {

	public List<GjtMessageClassify> findByInfoType(String infoType);

}
