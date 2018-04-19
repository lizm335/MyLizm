/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.LcmsMutualSubject;

/**
 * 功能说明：答疑（学习平台的同义词表，没有授权删除，只有查询，修改，新增权限）
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年6月19日
 * @version 2.5
 *
 */
public interface LcmsMutualSubjectDao
		extends JpaRepository<LcmsMutualSubject, String>, JpaSpecificationExecutor<LcmsMutualSubject> {

	@Modifying
	@Transactional
	@Query("update LcmsMutualSubject g set g.isCommend=:state,g.oftenType=:oftenType,g.isCommendType=:isCommendType where g.subjectId=:id ")
	public int updateIsComm(@Param("id") String id, @Param("state") String state, @Param("oftenType") String oftenType,
			@Param("isCommendType") String isCommendType);

	/**
	 * 删除自己创建的问题和回复
	 * 
	 * @param id
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update LcmsMutualSubject g set g.isdeleted='Y' where g.subjectId=:id ")
	public int updateIsDelete(@Param("id") String id);

	/**
	 * 转发给其他用户
	 * 
	 * @param id
	 * @param initialId
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update LcmsMutualSubject g set g.forwardAccountId=:forwardId,g.isTranspond='0'  where g.subjectId=:id ")
	public int updateForward(@Param("id") String id, @Param("forwardId") String forwardId);

}
