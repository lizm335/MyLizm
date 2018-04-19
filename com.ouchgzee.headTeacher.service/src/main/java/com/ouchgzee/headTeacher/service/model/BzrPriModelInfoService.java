package com.ouchgzee.headTeacher.service.model;

import com.ouchgzee.headTeacher.pojo.BzrPriModelInfo;
import com.ouchgzee.headTeacher.service.base.BaseService;

import java.util.List;

@Deprecated public interface BzrPriModelInfoService extends BaseService<BzrPriModelInfo> {
	
	List<BzrPriModelInfo> findAll();

}
