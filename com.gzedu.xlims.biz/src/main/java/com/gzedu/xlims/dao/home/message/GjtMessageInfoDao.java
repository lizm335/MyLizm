/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.home.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.message.GjtMessageInfo;

/**
 * 功能说明：通知公告
 * 
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月11日
 * @version 2.5
 *
 */
public interface GjtMessageInfoDao
		extends JpaRepository<GjtMessageInfo, String>, JpaSpecificationExecutor<GjtMessageInfo> {

	/**
	 * 更改消息为已读
	 * 
	 * @param messageId
	 * @param userId
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("UPDATE GjtMessageUser t SET t.isEnabled='1',updatedDt=sysdate,t.platform=:platform WHERE t.messageId=:messageId AND t.userId=:userId")
	int updateIsRead(@Param("messageId") String messageId, @Param("userId") String userId,
			@Param("platform") String platform);

	/**
	 * 删除
	 * 
	 * @param messageId
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("UPDATE GjtMessageInfo  t SET t.isDeleted='Y' WHERE t.messageId=:messageId ")
	int updateIsDelete(@Param("messageId") String messageId);

	@Query("select  count(m)  from GjtMessageInfo m  where m.isDeleted='N' and m.typeClassify=:typeClassify  ")
	public long findByTypeClassifyCount(@Param("typeClassify") String typeClassify);

}
