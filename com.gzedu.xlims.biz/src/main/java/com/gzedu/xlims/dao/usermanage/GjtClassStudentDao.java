/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.usermanage;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtClassStudent;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月25日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtClassStudentDao extends BaseDao<GjtClassStudent, String> {
	/**
	 * 是否删除 N Y
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtClassStudent g set g.isDeleted=?2 where g.classStudentId=?1 ")
	int deleteById(String id, String str);

	/**
	 * 是否启用1启用，0停用
	 * 
	 * @param id
	 * @param str
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("update GjtClassStudent g set g.isEnabled=?2 where g.classStudentId=?1 ")
	int updateIsEnabled(String id, String str);

	/**
	 * 根据学员ID删除学员的教学班和课程班
	 * @param studentId
	 * @param memo
	 * @param string
	 */
	@Modifying
	@Transactional
	@Query("update GjtClassStudent g set g.isDeleted=?4,g.memo=?2,updated_dt=sysdate where g.studentId=?1 and g.classId=?3 ")
	public int deleteByStudentId(String studentId, String memo,String classId, String isDeleted);

	List<GjtClassStudent> queryByClassId(String classId);
}
