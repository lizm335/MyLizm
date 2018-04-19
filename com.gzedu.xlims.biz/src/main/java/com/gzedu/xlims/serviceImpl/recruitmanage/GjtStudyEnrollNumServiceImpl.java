package com.gzedu.xlims.serviceImpl.recruitmanage;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.recruitmanage.GjtStudyEnrollNumDao;
import com.gzedu.xlims.pojo.GjtEnrollBatchNew;
import com.gzedu.xlims.pojo.GjtStudyEnrollNum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.recruitmanage.GjtStudyEnrollNumService;

/**
 * 功能说明：
 * @author lulinlin@eenet.com
 * @Date 2016年11月20日
 *
 */
@Service
public class GjtStudyEnrollNumServiceImpl  implements GjtStudyEnrollNumService{
	
	private static final Log log = LogFactory.getLog(GjtStudyEnrollNumServiceImpl.class);
	
	@Autowired
	CommonMapService commonMapService;
	
	@Autowired
	GjtStudyEnrollNumDao gjtStudyEnrollNumDao;
	
	@Override
	public GjtStudyEnrollNum queryByIdOrEnrId(String enrollBatchId, String xxzxId) {
		log.info("enrollBatchId="+enrollBatchId+";xxzxId="+xxzxId);
		return gjtStudyEnrollNumDao.findByEnrollBatchIdAndXxzxId(enrollBatchId, xxzxId);
	}

	@Override
	public int update(GjtStudyEnrollNum entity) {
		gjtStudyEnrollNumDao.save(entity);
		return 1;
	}

	@Override
	public void insert(GjtStudyEnrollNum entity) {
		gjtStudyEnrollNumDao.save(entity);
		
	}

	@Override
	public void delete(String id) {
		gjtStudyEnrollNumDao.deleteById(id, "Y");		
	}	
}
