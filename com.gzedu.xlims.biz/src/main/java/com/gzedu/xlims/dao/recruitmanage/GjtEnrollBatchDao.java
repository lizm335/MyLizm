/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.recruitmanage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtEnrollBatch;
import com.gzedu.xlims.pojo.GjtGrade;

/**
 * 
 * 功能说明：招生批次
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月21日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtEnrollBatchDao
		extends JpaRepository<GjtEnrollBatch, String>, JpaSpecificationExecutor<GjtEnrollBatch> {

	List<GjtEnrollBatch> findByGjtGrade(GjtGrade gjtGradeId);

	/**
	 * 是否删除 N Y
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtEnrollBatch g set g.isDeleted=?2 where g.enrollBatchId=?1 ")
	public int deleteById(String id, String str);

	/**
	 * 是否启用1启用，0停用
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtEnrollBatch g set g.isEnabled=?2 where g.enrollBatchId=?1 ")
	public int updateIsEnabled(String id, String str);

}
