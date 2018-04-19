/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.student;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtSpecialty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 专业信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月24日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtSpecialtyDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtSpecialtyDao extends BaseDao<BzrGjtSpecialty, String> {

	/**
	 * 根据学校ID获取学校的所有专业信息
	 * 
	 * @param xxId
	 * @return
	 */
	@Query("SELECT t FROM BzrGjtSpecialty t WHERE t.isDeleted='N' AND t.xxId=:xxId ORDER BY t.createdDt DESC")
	List<BzrGjtSpecialty> findByXxId(@Param("xxId") String xxId);

}
