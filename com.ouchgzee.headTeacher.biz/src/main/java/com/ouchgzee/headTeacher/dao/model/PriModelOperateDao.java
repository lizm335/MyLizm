/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.dao.model;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrPriModelOperate;
import org.springframework.stereotype.Repository;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月8日
 * @version 2.5
 *
 */
@Deprecated @Repository("bzrPriModelOperateDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface PriModelOperateDao extends BaseDao<BzrPriModelOperate, String> {

}
