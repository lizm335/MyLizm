/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.systemManage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.GjtMenu;

/**
 * 功能说明：个人中心菜单
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年5月7日
 * @version 2.5
 *
 */
public interface GjtMenuDao extends JpaRepository<GjtMenu, String>, JpaSpecificationExecutor<GjtMenu>{

	@Query("select g.id,g.name from GjtMenu g where g.gjtMenu.id =?1")
	Page<GjtMenu> findByParentId(String parentId, Pageable pageable);
}
