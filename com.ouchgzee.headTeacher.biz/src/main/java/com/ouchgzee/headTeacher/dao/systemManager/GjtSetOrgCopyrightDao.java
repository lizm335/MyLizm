package com.ouchgzee.headTeacher.dao.systemManager;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import org.springframework.data.jpa.repository.Query;

import com.ouchgzee.headTeacher.pojo.BzrGjtSetOrgCopyright;
import org.springframework.stereotype.Repository;

/**
 * 功能说明：院校管理-->版权设置
 * 
 * @author 卢林林 lulinlin@eenet.com
 * @Date 2017年1月17日
 * @version 2.5
 */
@Deprecated @Repository("bzrGjtSetOrgCopyrightDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtSetOrgCopyrightDao extends BaseDao<BzrGjtSetOrgCopyright, String> {
	/**
	 * 根据院校ID和平台类型查询数据
	 * 
	 * @param xxId
	 * @param platfromType
	 * @return
	 */
	BzrGjtSetOrgCopyright findByXxIdAndPlatfromType(String xxId, String platfromType);

	BzrGjtSetOrgCopyright findBySchoolRealmNameAndPlatfromType(String schoolRealmName, String platfromType);
	
	@Query(value = "SELECT GSOC.*  FROM GJT_SET_ORG_COPYRIGHT GSOC, GJT_USER_ACCOUNT GUA WHERE GSOC.IS_DELETED = 'N' "
			+ "AND GUA.IS_DELETED = 'N' AND GUA.ORG_ID = GSOC.XX_ID AND GUA.LOGIN_ACCOUNT = ?1 AND GSOC.PLATFROM_TYPE = ?2 ",
			nativeQuery = true)
	BzrGjtSetOrgCopyright findByLoginAccountAndPlatfromType(String loginAccount, String platfromType);
	
}
