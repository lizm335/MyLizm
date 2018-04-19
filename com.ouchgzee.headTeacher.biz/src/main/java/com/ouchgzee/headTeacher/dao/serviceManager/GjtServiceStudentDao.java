/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.serviceManager;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtServiceStudent;
import org.springframework.stereotype.Repository;

/**
 * 服务记录与学生关系操作类<br>
 * 功能说明：
 * 
 * @author 李建华 lijianhua@gzedu.net
 * @Date 2016年5月09日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtServiceStudentDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtServiceStudentDao  extends BaseDao<BzrGjtServiceStudent, String> {

}