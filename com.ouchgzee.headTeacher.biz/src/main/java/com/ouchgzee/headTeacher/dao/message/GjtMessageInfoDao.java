/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.message;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtMessageInfo;

/**
 * 消息信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月9日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtMessageInfoDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtMessageInfoDao extends BaseDao<BzrGjtMessageInfo, String> {

	/**
	 * 删除消息信息
	 * 
	 * @param messageId
	 * @param updatedBy
	 * @param updatedDt
	 */
	@Modifying
	@Query("UPDATE BzrGjtMessageInfo t SET t.isDeleted='Y',t.version=t.version+1,t.updatedBy=:updatedBy,t.updatedDt=:updatedDt WHERE t.messageId=:messageId")
	@Transactional(value="transactionManagerBzr")
	int updateDeleteStatus(@Param("messageId") String messageId, @Param("updatedBy") String updatedBy,
			@Param("updatedDt") Timestamp updatedDt);

}
