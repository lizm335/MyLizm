package com.ouchgzee.headTeacher.dao.textbook.repository;

import com.ouchgzee.headTeacher.dao.base.BaseDao;

import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookPlan;
import org.springframework.stereotype.Repository;

@Deprecated @Repository("bzrGjtTextbookPlanRepository") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtTextbookPlanRepository extends BaseDao<BzrGjtTextbookPlan, String> {

}
