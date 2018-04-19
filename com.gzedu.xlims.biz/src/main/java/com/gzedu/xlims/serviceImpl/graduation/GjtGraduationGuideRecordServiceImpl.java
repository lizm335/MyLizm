package com.gzedu.xlims.serviceImpl.graduation;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.graduation.GjtGraduationGuideRecordDao;
import com.gzedu.xlims.pojo.graduation.GjtGraduationGuideRecord;
import com.gzedu.xlims.service.graduation.GjtGraduationGuideRecordService;

@Service
public class GjtGraduationGuideRecordServiceImpl implements GjtGraduationGuideRecordService {
	
	private static final Log log = LogFactory.getLog(GjtGraduationGuideRecordServiceImpl.class);

	@Autowired
	private GjtGraduationGuideRecordDao gjtGraduationGuideRecordDao;

	@Override
	public List<GjtGraduationGuideRecord> queryListByStudent(String batchId, String studentId, int recordType) {
		log.info("batchId:" + batchId + ", studentId:" + studentId + ", recordType:" + recordType);
		return gjtGraduationGuideRecordDao.queryListByStudent(batchId, studentId, recordType);
	}

	@Override
	public List<GjtGraduationGuideRecord> queryListByStudentAndCode(String batchId, String studentId, int recordType,
			String progressCode) {
		log.info("batchId:" + batchId + ", studentId:" + studentId + ", recordType:" + recordType + ", progressCode:" + progressCode);
		return gjtGraduationGuideRecordDao.queryListByStudentAndCode(batchId, studentId, recordType, progressCode);
	}

	@Override
	public void insert(GjtGraduationGuideRecord entity) {
		log.info("entity:[" + entity + "]");
		entity.setRecordId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		gjtGraduationGuideRecordDao.save(entity);
	}

}
