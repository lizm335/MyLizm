/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.employee;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 班主任信息操作类<br>
 * 功能说明：
 * 
 * @author 李建华  lijianhua@gzedu.net
 * @Date 2016年5月10日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtEmployeeInfoDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtEmployeeInfoDao extends BaseDao<BzrGjtEmployeeInfo, String> {

	/**
	 * 根据教职工登录账号获取教职工信息
	 *
	 * @param loginAccount
	 * @return
	 */
	@Query("SELECT t FROM BzrGjtEmployeeInfo t INNER JOIN t.gjtUserAccount ua WHERE t.isDeleted='N' AND ua.isDeleted='N' AND ua.loginAccount=:loginAccount")
	BzrGjtEmployeeInfo findByLoginAccount(@Param("loginAccount") String loginAccount);

	/**
	 * 更改头像
	 * @param employeeId
	 * @param photoUrl
	 * @param updatedDt
	 * @param updatedBy
	 * @return
	 */
	@Modifying
	@Query("UPDATE BzrGjtEmployeeInfo e SET e.zp=:photoUrl,e.updatedBy=:updatedBy,e.updatedDt=:updatedDt,e.version=e.version+1 WHERE e.employeeId=:employeeId")
	@Transactional(value="transactionManagerBzr")
	int updatePhoto(@Param("employeeId") String employeeId, @Param("photoUrl") String photoUrl,
			@Param("updatedBy") String updatedBy,@Param("updatedDt") Date updatedDt);

	@Query("select g1 from BzrGjtEmployeeInfo g1, BzrGjtEmployeePosition g2 where g1.isDeleted='N' and g1.employeeType = ?1 and g1.employeeId = g2.id.employeeId and g2.id.type = ?2 and g1.xxId = ?3 ")
	public List<BzrGjtEmployeeInfo> findListByType(String type, int subType, String orgId);

}
