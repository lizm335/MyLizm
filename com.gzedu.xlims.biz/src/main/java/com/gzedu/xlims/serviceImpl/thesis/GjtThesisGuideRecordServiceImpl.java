package com.gzedu.xlims.serviceImpl.thesis;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.thesis.GjtThesisGuideRecordDao;
import com.gzedu.xlims.pojo.thesis.GjtThesisGuideRecord;
import com.gzedu.xlims.service.thesis.GjtThesisGuideRecordService;

@Service
public class GjtThesisGuideRecordServiceImpl implements GjtThesisGuideRecordService {
	
	private static final Log log = LogFactory.getLog(GjtThesisGuideRecordServiceImpl.class);
	
	@Autowired
	private GjtThesisGuideRecordDao gjtThesisGuideRecordDao;

	@Override
	public Page<GjtThesisGuideRecord> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtThesisGuideRecord> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtThesisGuideRecord.class);
		return gjtThesisGuideRecordDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtThesisGuideRecord insert(GjtThesisGuideRecord entity) {
		log.info("entity:[" + entity + "]");
		entity.setRecordId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		
		return gjtThesisGuideRecordDao.save(entity);
	}

	@Override
	public void update(GjtThesisGuideRecord entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		
		gjtThesisGuideRecordDao.save(entity);
	}

	@Override
	public GjtThesisGuideRecord findOne(String id) {
		return gjtThesisGuideRecordDao.findOne(id);
	}

	@Override
	public List<GjtThesisGuideRecord> findByThesisPlanIdAndStudentId(String thesisPlanId, String studentId) {
		return gjtThesisGuideRecordDao.findByThesisPlanIdAndStudentIdAndIsDeleted(thesisPlanId, studentId, "N");
	}

	@Override
	public List<GjtThesisGuideRecord> findStudentSubmitRecordByCode(String thesisPlanId, String studentId,
			String progressCode) {
		return gjtThesisGuideRecordDao.findStudentSubmitRecordByCode(thesisPlanId, studentId, progressCode);
	}

	@Override
	public List<GjtThesisGuideRecord> findTeacherSubmitRecordByCode(String thesisPlanId, String studentId,
			String progressCode) {
		return gjtThesisGuideRecordDao.findTeacherSubmitRecordByCode(thesisPlanId, studentId, progressCode);
	}

}
