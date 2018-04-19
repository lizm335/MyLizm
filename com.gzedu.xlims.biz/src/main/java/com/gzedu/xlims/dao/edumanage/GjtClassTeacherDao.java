/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.edumanage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.GjtClassTeacher;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2017年1月22日
 * @version 1.0
 *
 */
public interface GjtClassTeacherDao
		extends JpaRepository<GjtClassTeacher, String>, JpaSpecificationExecutor<GjtClassTeacher> {
	@Modifying
	@Transactional
	@Query("delete GjtClassTeacher g where g.classId=?1 and g.employeeType=?2")
	public int deleteByClassId(String classId, String employeeType);
}
