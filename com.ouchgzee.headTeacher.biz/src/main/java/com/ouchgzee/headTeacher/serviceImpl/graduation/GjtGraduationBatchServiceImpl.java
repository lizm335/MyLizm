package com.ouchgzee.headTeacher.serviceImpl.graduation;

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
import com.ouchgzee.headTeacher.dao.graduation.GjtGraduationBatchDao;
import com.ouchgzee.headTeacher.dao.graduation.GjtGraduationNativeDao;
import com.ouchgzee.headTeacher.pojo.graduation.BzrGjtGraduationBatch;
import com.ouchgzee.headTeacher.service.graduation.BzrGjtGraduationBatchService;

@Deprecated @Service("bzrGjtGraduationBatchServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtGraduationBatchServiceImpl implements BzrGjtGraduationBatchService {
	
	private static final Log log = LogFactory.getLog(GjtGraduationBatchServiceImpl.class);
	
	//private static final DateFormat df = new SimpleDateFormat("yyyyMMdd");
	
	@Autowired
	private GjtGraduationBatchDao gjtGraduationBatchDao;

	@Autowired
	private GjtGraduationNativeDao gjtGraduationNativeDao;

	@Override
	public Page<BzrGjtGraduationBatch> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<BzrGjtGraduationBatch> spec = DynamicSpecifications.bySearchFilter(filters.values(), BzrGjtGraduationBatch.class);
		return gjtGraduationBatchDao.findAll(spec, pageRequst);
	}

	@Override
	public void insert(BzrGjtGraduationBatch entity) {
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
	public BzrGjtGraduationBatch queryById(String id) {
		log.info("id:" + id);
		return gjtGraduationBatchDao.findOne(id);
	}

	@Override
	public void update(BzrGjtGraduationBatch entity) {
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
	public BzrGjtGraduationBatch findByStudyYearCode(int studyYearCode, String orgId) {
		log.info("studyYearCode:" + studyYearCode);
		return gjtGraduationBatchDao.findByStudyYearCode(studyYearCode, orgId);
	}

	@Override
	public Map<String, String> getGraduationBatchMap(String orgId) {
		return gjtGraduationNativeDao.getGraduationBatchMap(orgId);
	}

}
