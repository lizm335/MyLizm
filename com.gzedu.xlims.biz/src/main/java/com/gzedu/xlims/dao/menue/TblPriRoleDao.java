/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.menue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.TblPriRole;

/**
 * 
 * 功能说明：角色
 * 
 * @author liming
 * @Date 2016年5月4日
 * @version 2.5
 *
 */
public interface TblPriRoleDao extends JpaRepository<TblPriRole, String>, JpaSpecificationExecutor<TblPriRole> {

}
