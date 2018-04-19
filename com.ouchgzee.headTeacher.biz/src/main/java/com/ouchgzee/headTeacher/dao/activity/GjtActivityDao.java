/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.dao.activity;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtActivity;
import org.springframework.stereotype.Repository;

/**
 * 班级活动操作类<br>
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月12日
 * @version 1.0
 *
 */
@Deprecated @Repository("bzrGjtActivityDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtActivityDao extends BaseDao<BzrGjtActivity, String> {

}
