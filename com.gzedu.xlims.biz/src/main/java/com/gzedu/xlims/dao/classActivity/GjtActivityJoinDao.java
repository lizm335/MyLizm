/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.classActivity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtActivityJoin;
import com.gzedu.xlims.pojo.GjtActivityJoinPK;

/**
 * 
 * 功能说明：活动参加表操作类
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年2月28日
 * @version 2.5
 *
 */
public interface GjtActivityJoinDao
		extends JpaRepository<GjtActivityJoin, String>, JpaSpecificationExecutor<GjtActivityJoin> {

	List<GjtActivityJoin> findByIdStudentId(String studentId);

	// GjtActivityJoin findByIdStudentIdAndActivityId(String studentId, String
	// activityId);
	GjtActivityJoin findById(GjtActivityJoinPK id);
}
