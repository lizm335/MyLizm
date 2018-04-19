package com.ouchgzee.headTeacher.serviceImpl.graduation;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.ouchgzee.headTeacher.dao.graduation.GjtGraduationGuideRecordDao;
import com.ouchgzee.headTeacher.pojo.graduation.BzrGjtGraduationGuideRecord;
import com.ouchgzee.headTeacher.service.graduation.BzrGjtGraduationGuideRecordService;

@Deprecated @Service("bzrGjtGraduationGuideRecordServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtGraduationGuideRecordServiceImpl implements BzrGjtGraduationGuideRecordService {
	
	private static final Log log = LogFactory.getLog(GjtGraduationGuideRecordServiceImpl.class);

	@Autowired
	private GjtGraduationGuideRecordDao gjtGraduationGuideRecordDao;

	@Override
	public List<BzrGjtGraduationGuideRecord> queryListByStudent(String batchId, String studentId, int recordType) {
		log.info("batchId:" + batchId + ", studentId:" + studentId + ", recordType:" + recordType);
		return gjtGraduationGuideRecordDao.queryListByStudent(batchId, studentId, recordType);
	}

	@Override
	public List<BzrGjtGraduationGuideRecord> queryListByStudentAndCode(String batchId, String studentId, int recordType,
																	   String progressCode) {
		log.info("batchId:" + batchId + ", studentId:" + studentId + ", recordType:" + recordType + ", progressCode:" + progressCode);
		return gjtGraduationGuideRecordDao.queryListByStudentAndCode(batchId, studentId, recordType, progressCode);
	}

	@Override
	public void insert(BzrGjtGraduationGuideRecord entity) {
		log.info("entity:[" + entity + "]");
		entity.setRecordId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		gjtGraduationGuideRecordDao.save(entity);
	}

}
