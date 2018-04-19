/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.home.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gzedu.xlims.pojo.message.GjtMessageInportUser;

/**
 * 
 * 功能说明：Excel导入的学员
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年1月11日
 * @version 3.0
 *
 */
public interface GjtMessageInportUserDao
		extends JpaRepository<GjtMessageInportUser, String>, JpaSpecificationExecutor<GjtMessageInportUser> {

	/**
	 * 统计
	 * 
	 * @param messageId
	 * @return
	 */
	@Query("select count(distinct g.userId) from GjtMessageInportUser g  where g.messageId=:messageId")
	public long findByMessageIdCount(@Param("messageId") String messageId);
}
