/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.serviceImpl.student;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.dao.student.GjtSpecialtyDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtSpecialty;
import com.ouchgzee.headTeacher.service.student.BzrGjtSpecialtyService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年03月29日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Service("bzrGjtSpecialtyServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtSpecialtyServiceImpl extends BaseServiceImpl<BzrGjtSpecialty> implements BzrGjtSpecialtyService {

	private static Logger log = LoggerFactory.getLogger(GjtSpecialtyServiceImpl.class);

	@Autowired
	private GjtSpecialtyDao gjtSpecialtyDao;

	@Override
	protected BaseDao<BzrGjtSpecialty, String> getBaseDao() {
		return gjtSpecialtyDao;
	}

}
