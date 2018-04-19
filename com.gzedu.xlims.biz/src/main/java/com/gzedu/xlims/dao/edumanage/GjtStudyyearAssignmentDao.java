/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.edumanage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtStudyYearAssignment;

/**
 * 
 * 功能说明：学年度任务计划
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月16日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtStudyyearAssignmentDao
		extends JpaRepository<GjtStudyYearAssignment, String>, JpaSpecificationExecutor<GjtStudyYearAssignment> {
	/**
	 * 是否删除 N Y
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtStudyYearAssignment g set g.isDeleted=?2 where g.assignmentId=?1 ")
	public int deleteById(String id, String str);

	/**
	 * 修改状态为完成
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtStudyYearAssignment g set g.status=?2 where g.assignmentId=?1 ")
	public int updateStatus(String id, String status);
}
