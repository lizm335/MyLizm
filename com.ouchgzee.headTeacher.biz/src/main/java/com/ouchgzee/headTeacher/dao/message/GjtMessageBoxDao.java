/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.message;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtMessageBox;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 消息箱信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月9日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtMessageBoxDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtMessageBoxDao extends BaseDao<BzrGjtMessageBox, String> {

	/**
	 * 查询消息阅读人数
	 * 
	 * @param messageId
	 * @return HQL-返回Long<br>
	 *         SQL-返回BigDecimal<br>
	 */
	@Query("SELECT COUNT(*) FROM BzrGjtMessageBox t WHERE t.isDeleted='N' AND t.isRead='1' AND t.gjtMessageInfo.messageId=:messageId")
	Long countRead(@Param("messageId") String messageId);

	/**
	 * 查询消息阅读人数情况
	 * 
	 * @param messageId
	 * @return HQL-返回Long extends Object<br>
	 *         SQL-返回BigDecimal extends Object<br>
	 */
	@Query("SELECT COUNT(*),COUNT(CASE t.isRead WHEN '1' THEN 1 ELSE NULL END) FROM BzrGjtMessageBox t WHERE t.isDeleted='N' AND t.gjtMessageInfo.messageId=:messageId")
	Object[] countReadSituation(@Param("messageId") String messageId);

	/**
	 * 查询批量消息阅读人数情况
	 * 
	 * @param messageIdList
	 * @return
	 */
	@Query("SELECT t.gjtMessageInfo.messageId,COUNT(*),COUNT(CASE t.isRead WHEN '1' THEN 1 ELSE NULL END) FROM BzrGjtMessageBox t WHERE t.isDeleted='N' AND t.gjtMessageInfo.messageId IN :messageIdList GROUP BY t.gjtMessageInfo.messageId")
	List<Object[]> countReadSituations(@Param("messageIdList") List<String> messageIdList);

	/**
	 * 用户的消息是否阅读
	 * 
	 * @param messageId
	 * @param receiveUser
	 */
	@Query("SELECT CASE COUNT(*) WHEN 0 THEN 0 ELSE 1 END FROM BzrGjtMessageBox t WHERE t.isDeleted='N' AND t.isRead='1' AND t.gjtMessageInfo.messageId=:messageId AND t.receiveUser=:receiveUser")
	Integer isMsgUserRead(@Param("messageId") String messageId, @Param("receiveUser") String receiveUser);

	/**
	 * 删除消息箱信息
	 * 
	 * @param messageId
	 * @param updatedBy
	 * @param updatedDt
	 */
	@Modifying
	@Query("UPDATE BzrGjtMessageBox t SET t.isDeleted='Y',t.version=t.version+1,t.updatedBy=:updatedBy,t.updatedDt=:updatedDt WHERE t.gjtMessageInfo.messageId=:messageId")
	@Transactional(value="transactionManagerBzr")
	int updateDeleteStatus(@Param("messageId") String messageId, @Param("updatedBy") String updatedBy,
			@Param("updatedDt") Date updatedDt);

}
