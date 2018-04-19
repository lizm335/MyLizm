package com.ouchgzee.headTeacher.daoImpl;

import org.springframework.stereotype.Repository;

import com.ouchgzee.headTeacher.dao.SysDictionaryDao;
import com.ouchgzee.headTeacher.pojo.BzrSysDictionary;

/**
 * 字典数据库操作类接口实现类
 * 
 * @author LiuJunGuang
 * @date 2014年3月3日下午2:27:45
 */
@Deprecated @Repository("bzrSysDictionaryDaoImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class SysDictionaryDaoImpl extends BaseDaoImpl<BzrSysDictionary> implements SysDictionaryDao {

}
