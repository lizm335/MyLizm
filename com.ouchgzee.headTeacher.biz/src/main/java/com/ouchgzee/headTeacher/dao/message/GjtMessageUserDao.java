package com.ouchgzee.headTeacher.dao.message;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtMessageUser;

@Deprecated @Repository("bzrGjtMessageUserDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtMessageUserDao extends BaseDao<BzrGjtMessageUser, String> {
	/**
	 * 删除消息箱信息
	 *
	 * @param messageId
	 * @param userId
	 * @param updatedDt
	 */
	@Modifying
	@Query("UPDATE BzrGjtMessageUser t SET t.isEnabled='1',t.version=t.version+1,t.updatedBy=:userId,t.updatedDt=:updatedDt WHERE t.gjtMessageInfo.messageId=:messageId and t.userId=:userId")
	@Transactional(value="transactionManagerBzr")
	int updateRead(@Param("messageId") String messageId, @Param("userId") String userId,
			@Param("updatedDt") Date updatedDt);

	@Query(value = "select count(*)num,m.message_id from gjt_message_user m where m.message_id "
			+ "in(:messageIds) group by m.message_id", nativeQuery = true)
	List<Object[]> queryPutMessageIds(@Param("messageIds") List<String> messageIds);

	@Query(value = "select count(*)num,m.message_id from gjt_message_user m where m.message_id "
			+ "in(:messageIds) and m.is_enabled='1' group by m.message_id", nativeQuery = true)
	List<Object[]> queryReadMessageIds(@Param("messageIds") List<String> messageIds);

	List<BzrGjtMessageUser> findByMessageId(String messageId);
}