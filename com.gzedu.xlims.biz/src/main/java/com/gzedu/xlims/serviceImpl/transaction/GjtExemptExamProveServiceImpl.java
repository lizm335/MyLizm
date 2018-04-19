package com.gzedu.xlims.serviceImpl.transaction;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.transaction.GjtExemptExamProveDao;
import com.gzedu.xlims.pojo.GjtExemptExamProve;
import com.gzedu.xlims.service.transaction.GjtExemptExamProveService;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月29日
 * @version 2.5
 */
@Service
public class GjtExemptExamProveServiceImpl implements GjtExemptExamProveService{
	private static final Logger log = LoggerFactory.getLogger(GjtExemptExamProveServiceImpl.class);
	
	@Autowired
	private GjtExemptExamProveDao gjtExemptExamProveDao;

	@Override
	public List<GjtExemptExamProve> findByExemptExamId(String exemptExamId) {	
		return gjtExemptExamProveDao.findByExemptExamId(exemptExamId);
	}

	@Override
	public List<GjtExemptExamProve> findByExemptExamIdAndStudentId(String exemptExamId, String studentId) {
		return gjtExemptExamProveDao.findByExemptExamIdAndStudentId(exemptExamId,studentId);
	}
}
