package com.ouchgzee.headTeacher.serviceImpl.fees;

import com.ouchgzee.headTeacher.dao.fees.FeesDao;
import com.ouchgzee.headTeacher.service.fees.BzrFeesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Deprecated @Service("bzrFeesServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class FeesServiceImpl implements BzrFeesService {

	@Autowired
	FeesDao feesDao;
	
	@Override
	public Page getFeeslist(Map searchParams, PageRequest pageRequst) {
		return feesDao.getFeeslist(searchParams, pageRequst);
	}
    
}
