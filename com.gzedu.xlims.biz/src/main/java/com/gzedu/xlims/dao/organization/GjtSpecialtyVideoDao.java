/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtSpecialtyVideo;

/**
 * 
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年9月29日
 * @version 3.0
 *
 */
public interface GjtSpecialtyVideoDao
		extends BaseDao<GjtSpecialtyVideo, String>, JpaRepository<GjtSpecialtyVideo, String>, JpaSpecificationExecutor<GjtSpecialtyVideo> {

	@Query("from GjtSpecialtyVideo v where v.typeName=?1 and isDeleted='N'")
	public GjtSpecialtyVideo queryByTypeName(String typeName);
}
