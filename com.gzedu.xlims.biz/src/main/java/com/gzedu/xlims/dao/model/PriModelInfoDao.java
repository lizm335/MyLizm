package com.gzedu.xlims.dao.model;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.PriModelInfo;
import com.gzedu.xlims.pojo.PriRoleInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
public interface PriModelInfoDao extends BaseDao<PriModelInfo, String> {

	PriModelInfo findByPriRoleInfosAndModelName(PriRoleInfo role, String systemName);

	List<PriModelInfo> findByPriRoleInfos(PriRoleInfo role);

	List<PriModelInfo> findByParentModelModelNameOrderByOrderNoAsc(String modelName);

	List<PriModelInfo> findByParentModelModelNameInOrderByOrderNoAsc(List<String> modelNames);

	Page<PriModelInfo> findByParentModelModelName(String modelName, Pageable pageable);

	List<PriModelInfo> findByIsdeletedAndParentModelIsNullOrderByOrderNoAsc(String isdeleted);

	PriModelInfo findByModelName(String modelName);

}