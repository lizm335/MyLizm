package com.ouchgzee.headTeacher.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ouchgzee.headTeacher.dao.BaseDao;
import com.ouchgzee.headTeacher.dao.SysDictionaryDao;
import com.ouchgzee.headTeacher.pojo.BzrSysDictionary;
import com.ouchgzee.headTeacher.service.BzrSysDictionaryService;

/**
 * 字典信息服务类接口实现
 * 
 * @author zhy
 * @date 2014年3月7日下午2:27:08
 */
@Deprecated @Service("bzrSysDictionaryServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class SysDictionaryServiceImpl extends BaseServiceImpl<BzrSysDictionary> implements BzrSysDictionaryService {
	@Autowired
	private SysDictionaryDao sysDictionaryDao;

	@Override
	protected BaseDao<BzrSysDictionary> getBaseDao() {

		return sysDictionaryDao;
	}

}
