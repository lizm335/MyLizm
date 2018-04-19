package com.ouchgzee.headTeacher.dao.model;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrPriModelInfo;
import com.ouchgzee.headTeacher.pojo.BzrPriRoleInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * 功能说明：菜单
 *
 * @author 李明 liming@eenet.com
 * @Date 2016年5月6日
 * @version 2.5
 *
 */
@Deprecated @Repository("bzrPriModelInfoDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface PriModelInfoDao extends BaseDao<BzrPriModelInfo, String> {

	BzrPriModelInfo findByPriRoleInfosAndModelName(BzrPriRoleInfo role, String systemName);

	List<BzrPriModelInfo> findByPriRoleInfos(BzrPriRoleInfo role);

	List<BzrPriModelInfo> findByParentModelModelNameOrderByOrderNoAsc(String modelName);

	List<BzrPriModelInfo> findByParentModelModelNameInOrderByOrderNoAsc(List<String> modelNames);

	Page<BzrPriModelInfo> findByParentModelModelName(String modelName, Pageable pageable);

	List<BzrPriModelInfo> findByIsdeletedAndParentModelIsNullOrderByOrderNoAsc(String isdeleted);

	BzrPriModelInfo findByModelName(String modelName);

}