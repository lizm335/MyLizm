package com.gzedu.xlims.serviceImpl.graduation;

import java.util.Date;
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
import com.gzedu.xlims.dao.graduation.GjtGraduationBatchDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationNativeDao;
import com.gzedu.xlims.pojo.graduation.GjtGraduationBatch;
import com.gzedu.xlims.service.graduation.GjtGraduationBatchService;

@Service
public class GjtGraduationBatchServiceImpl implements GjtGraduationBatchService {
	
	private static final Log log = LogFactory.getLog(GjtGraduationBatchServiceImpl.class);
	
	//private static final DateFormat df = new SimpleDateFormat("yyyyMMdd");
	
	@Autowired
	private GjtGraduationBatchDao gjtGraduationBatchDao;

	@Autowired
	private GjtGraduationNativeDao gjtGraduationNativeDao;

	@Override
	public Page<GjtGraduationBatch> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtGraduationBatch> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtGraduationBatch.class);
		return gjtGraduationBatchDao.findAll(spec, pageRequst);
	}

	@Override
	public void insert(GjtGraduationBatch entity) {
		log.info("entity:[" + entity + "]");
		
		entity.setBatchId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		
		//获取毕业批次编号
		/*Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 1);
		Date createdDtStart = cal.getTime();
		
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		Date createdDtEnd = cal.getTime();
		
		String maxCode = this.queryGraduationBatchMaxCode(createdDtStart, createdDtEnd);
		if (maxCode != null && !"".equals(maxCode)) {
			entity.setBatchCode(String.valueOf(Integer.parseInt(maxCode) + 1));
		} else {
			entity.setBatchCode(df.format(new Date()) + "01");
		}*/
		
		gjtGraduationBatchDao.save(entity);
	}

	@Override
	public String queryGraduationBatchMaxCode(Date createdDtStart, Date createdDtEnd) {
		log.info("createdDtStart:" + createdDtStart + ", createdDtEnd:" + createdDtEnd);
		return gjtGraduationBatchDao.queryGraduationBatchMaxCode(createdDtStart, createdDtEnd);
	}

	@Override
	public GjtGraduationBatch queryById(String id) {
		log.info("id:" + id);
		return gjtGraduationBatchDao.findOne(id);
	}

	@Override
	public void update(GjtGraduationBatch entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtGraduationBatchDao.save(entity);
	}

	@Override
	public void delete(String id, String deletedBy) {
		log.info("id:" + id);
		gjtGraduationBatchDao.delete(id, deletedBy, new Date());
	}

	@Override
	public GjtGraduationBatch findByGradeId(String gradeId, String orgId) {
		log.info("gradeId:" + gradeId);
		return gjtGraduationBatchDao.findByGradeId(gradeId, orgId);
	}

	@Override
	public Map<String, String> getGraduationBatchMap(String orgId) {
		return gjtGraduationNativeDao.getGraduationBatchMap(orgId);
	}

}
