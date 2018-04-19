package com.ouchgzee.headTeacher.dao.model;

import com.ouchgzee.headTeacher.pojo.BzrPriRoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * 功能说明：角色菜单关联关系表
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月6日
 * @version 2.5
 *
 */
@Deprecated @Repository("bzrPriRoleModelDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface PriRoleModelDao extends JpaRepository<BzrPriRoleModel, String>, JpaSpecificationExecutor<BzrPriRoleModel> {

	List<BzrPriRoleModel> findByRoleId(String roleId);
}