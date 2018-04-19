/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import com.gzedu.xlims.pojo.GjtStudyCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * 功能说明：学习中心管理
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年4月28日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtStudyCenterDao
		extends JpaRepository<GjtStudyCenter, String>, JpaSpecificationExecutor<GjtStudyCenter> {
	/**
	 * 假删除
	 * 
	 * @param id
	 * @param isDeleted
	 */
	@Modifying
	@Transactional
	@Query("update GjtStudyCenter g set g.isDeleted=?2,g.updatedDt =sysdate where g.id=?1")
	void deleteStudyCenterById(String id, String isDeleted);

	List<GjtStudyCenter> findByScName(String scName);
}
