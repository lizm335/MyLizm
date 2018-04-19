package com.ouchgzee.headTeacher.serviceImpl.textbook;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ouchgzee.headTeacher.dao.textbook.GjtTextbookDistributeDetailDao;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookDistributeDetail;
import com.ouchgzee.headTeacher.service.textbook.BzrGjtTextbookDistributeDetailService;

@Deprecated @Service("bzrGjtTextbookDistributeDetailServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookDistributeDetailServiceImpl implements BzrGjtTextbookDistributeDetailService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookDistributeDetailServiceImpl.class);

	@Autowired
	private GjtTextbookDistributeDetailDao gjtTextbookDistributeDetailDao;

	@Override
	public void update(BzrGjtTextbookDistributeDetail entity) {
		log.info("entity:[" + entity + "]");
		gjtTextbookDistributeDetailDao.save(entity);
	}

	@Override
	public void update(List<BzrGjtTextbookDistributeDetail> entities) {
		log.info("entities:[" + entities + "]");
		gjtTextbookDistributeDetailDao.save(entities);
	}

}
