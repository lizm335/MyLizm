/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.student;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtStudentEnteringSchool;
import org.springframework.stereotype.Repository;

/**
 * 学员入学确认操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年06月16日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtStudentEnteringSchoolDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtStudentEnteringSchoolDao extends BaseDao<BzrGjtStudentEnteringSchool, String> {

}
