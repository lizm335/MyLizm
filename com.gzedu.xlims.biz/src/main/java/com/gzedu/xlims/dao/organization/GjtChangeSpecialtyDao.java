/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtChangeSpecialty;

/**
 * 
 * 功能说明：学籍异动
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月6日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtChangeSpecialtyDao
		extends JpaRepository<GjtChangeSpecialty, String>, JpaSpecificationExecutor<GjtChangeSpecialty> {

}
