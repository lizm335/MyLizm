/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.feedback;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.LcmsMutualReply;

/**
 * 功能说明：答疑（学习平台的同义词表，没有授权删除，只有查询，修改，新增权限）
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年6月19日
 * @version 2.5
 *
 */
public interface LcmsMutualReplyDao
		extends JpaRepository<LcmsMutualReply, String>, JpaSpecificationExecutor<LcmsMutualReply> {

	@Modifying
	@Transactional
	@Query("update LcmsMutualReply g set g.isdeleted='Y'  where g.subjectId=:id ")
	public int deleteBySubjectId(@Param("id") String id);

	public LcmsMutualReply findBySubjectIdAndParentIdIsNull(String pid);

	public List<LcmsMutualReply> findBySubjectIdAndParentIdIsNotNull(String pid);
}
