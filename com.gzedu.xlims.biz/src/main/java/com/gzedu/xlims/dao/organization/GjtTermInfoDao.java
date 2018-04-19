/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtTermInfo;

/**
 * 
 * 功能说明：学期管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月28日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtTermInfoDao extends JpaRepository<GjtTermInfo, String>, JpaSpecificationExecutor<GjtTermInfo> {
	/**
	 * 是否删除 N Y
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtTermInfo g set g.isDeleted=?2 where g.termId=?1 ")
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
	@Query("update GjtTermInfo g set g.isEnabled=?2 where g.termId=?1 ")
	public int updateIsEnabled(String id, String str);

}
