package com.gzedu.xlims.serviceImpl.practice;

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
import com.gzedu.xlims.dao.practice.GjtPracticeGuideRecordDao;
import com.gzedu.xlims.pojo.practice.GjtPracticeGuideRecord;
import com.gzedu.xlims.service.practice.GjtPracticeGuideRecordService;

@Service
public class GjtPracticeGuideRecordServiceImpl implements GjtPracticeGuideRecordService {

	private static final Log log = LogFactory.getLog(GjtPracticeGuideRecordServiceImpl.class);

	@Autowired
	private GjtPracticeGuideRecordDao gjtPracticeGuideRecordDao;

	@Override
	public Page<GjtPracticeGuideRecord> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtPracticeGuideRecord> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtPracticeGuideRecord.class);
		return gjtPracticeGuideRecordDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtPracticeGuideRecord insert(GjtPracticeGuideRecord entity) {
		log.info("entity:[" + entity + "]");
		entity.setRecordId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");

		return gjtPracticeGuideRecordDao.save(entity);
	}

	@Override
	public void update(GjtPracticeGuideRecord entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());

		gjtPracticeGuideRecordDao.save(entity);
	}

	@Override
	public GjtPracticeGuideRecord findOne(String id) {
		return gjtPracticeGuideRecordDao.findOne(id);
	}

	@Override
	public List<GjtPracticeGuideRecord> findByPracticePlanIdAndStudentId(String practicePlanId, String studentId,
			String order) {
		return gjtPracticeGuideRecordDao.findByPracticePlanIdAndStudentIdAndIsDeleted(practicePlanId, studentId, "N",
				order);
	}

	@Override
	public List<GjtPracticeGuideRecord> findStudentSubmitRecordByCode(String practicePlanId, String studentId,
			String progressCode) {
		return gjtPracticeGuideRecordDao.findStudentSubmitRecordByCode(practicePlanId, studentId, progressCode);
	}

	@Override
	public List<GjtPracticeGuideRecord> findTeacherSubmitRecordByCode(String practicePlanId, String studentId,
			String progressCode) {
		return gjtPracticeGuideRecordDao.findTeacherSubmitRecordByCode(practicePlanId, studentId, progressCode);
	}

}
