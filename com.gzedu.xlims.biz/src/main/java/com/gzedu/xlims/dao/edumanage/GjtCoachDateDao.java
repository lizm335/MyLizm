/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.edumanage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.openClass.GjtCoachDate;

/**
 * 
 * 功能说明：辅导资料
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年8月24日
 * @version 3.0
 *
 */
public interface GjtCoachDateDao extends BaseDao<GjtCoachDate, String>, JpaRepository<GjtCoachDate, String>, JpaSpecificationExecutor<GjtCoachDate> {

	@Query("from GjtCoachDate where termCourseId=?1 and isDeleted='N'")
	public List<GjtCoachDate> queryByTermCourseId(String termCourseId);
}