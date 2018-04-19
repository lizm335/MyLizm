/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.feedback;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrLcmsMutualSubject;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 功能说明：答疑（学习平台的同义词表，没有授权删除，只有查询，修改，新增权限）
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年6月19日
 * @version 2.5
 *
 */
@Deprecated @Repository("bzrLcmsMutualSubjectDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface LcmsMutualSubjectDao extends BaseDao<BzrLcmsMutualSubject, String> {

	@Modifying
	@Transactional(value="transactionManagerBzr")
	@Query("update BzrLcmsMutualSubject g set g.isCommend=:state where g.subjectId=:id ")
	public int updateIsComm(@Param("id") String id, @Param("state") String state);

	/**
	 * 删除自己创建的问题和回复
	 * 
	 * @param id
	 * @return
	 */
	@Modifying
	@Transactional(value="transactionManagerBzr")
	@Query("update BzrLcmsMutualSubject g set g.isdeleted='Y' where g.subjectId=:id ")
	public int updateIsDelete(@Param("id") String id);

	/**
	 * 转发给其他用户
	 * 
	 * @param id
	 * @param initialId
	 * @return
	 */
	@Modifying
	@Transactional(value="transactionManagerBzr")
	@Query("update BzrLcmsMutualSubject g set g.forwardAccountId=:forwardId  where g.subjectId=:id ")
	public int updateForward(@Param("id") String id, @Param("forwardId") String forwardId);

}
