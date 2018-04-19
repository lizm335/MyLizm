/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.home.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.message.GjtMessageUser;

/**
 * 
 * 功能说明：用户信息表
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年7月11日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtMessageUserDao
		extends JpaRepository<GjtMessageUser, String>, JpaSpecificationExecutor<GjtMessageUser> {

	GjtMessageUser findByUserIdAndMessageId(String userId, String messageId);

	@Query(value = "select count(*)num,m.message_id from gjt_message_user m where m.message_id "
			+ "in(:messageIds) group by m.message_id", nativeQuery = true)
	List<Object[]> queryPutMessageIds(@Param("messageIds") List<String> messageIds);

	@Query(value = "select count(*)num,m.message_id,m.platform from gjt_message_user m where m.message_id "
			+ "in(:messageIds) and m.is_enabled='1' group by m.message_id, m.platform", nativeQuery = true)
	List<Object[]> queryReadMessageIds(@Param("messageIds") List<String> messageIds);

	@Query(value = "select count(*)num,m.message_id from gjt_message_user m where m.message_id "
			+ "in(:messageIds) and m.is_enabled='1' group by m.message_id", nativeQuery = true)
	List<Object[]> findReadMessageIds(@Param("messageIds") List<String> messageIds);

	@Query(value = "select count(0)num,u.message_id from gjt_message_user u where u.message_id "
			+ " in(:messageIds) and u.is_like='1' group by u.message_id", nativeQuery = true)
	List<Object[]> queryLikeMessageIds(@Param("messageIds") List<String> messageIds);

	@Query(value = "select count(0)num,u.message_id from gjt_message_user u where u.message_id "
			+ " in(:messageIds) and u.is_tickling='1' group by u.message_id", nativeQuery = true)
	List<Object[]> queryTicklingMessageIds(@Param("messageIds") List<String> messageIds);

	@Query(value = "select count(0)num,m.message_id from gjt_message_comment m where m.message_id "
			+ " in (:messageIds) and m.is_deleted='N' group by m.message_id", nativeQuery = true)
	List<Object[]> queryCommMessageIds(@Param("messageIds") List<String> messageIds);

	@Modifying
	@Transactional
	@Query(value = "update GjtMessageUser t set t.isConstraint='1' where t.messageId=:messageId and t.isEnabled='0' ")
	int updateConstraint(@Param("messageId") String messageId);

	@Modifying
	@Transactional
	@Query(value = "update GjtMessageUser t set t.isConstraint=:isConstraint where t.id=:id ")
	int settingsIsConstraint(@Param("id") String id, @Param("isConstraint") String isConstraint);

	List<GjtMessageUser> findByMessageIdAndIsEnabled(String messageId, String isEnabled);
}
