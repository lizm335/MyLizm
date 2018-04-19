/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.edumanage;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtStudyYearInfo;

/**
 * 
 * 功能说明：学年度基础信息
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月16日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtStudyYearInfoDao
		extends JpaRepository<GjtStudyYearInfo, String>, JpaSpecificationExecutor<GjtStudyYearInfo> {

	public List<GjtStudyYearInfo> findByXxId(String orgId);
	
	public List<GjtStudyYearInfo> findByXxIdAndIsDeleted(String orgId, String isDeleted);

	public List<GjtStudyYearInfo> findByStudyyearStartDateAfterOrderByStudyyearStartDateAsc(Date date);

	public GjtStudyYearInfo findByStudyYearCodeAndXxId(int studyYearCode, String xxId);

}
