package com.gzedu.xlims.serviceImpl.graduation;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.graduation.GjtGraduationAdviserDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationApplyDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationNativeDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationSpecialtyDao;
import com.gzedu.xlims.pojo.graduation.GjtGraduationAdviser;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApply;
import com.gzedu.xlims.pojo.graduation.GjtGraduationSpecialty;
import com.gzedu.xlims.service.graduation.GjtGraduationSpecialtyService;

@Service
public class GjtGraduationSpecialtyServiceImpl implements GjtGraduationSpecialtyService {
	
	private static final Log log = LogFactory.getLog(GjtGraduationSpecialtyServiceImpl.class);
	
	@Autowired
	private GjtGraduationNativeDao gjtGraduationNativeDao;

	@Autowired
	private GjtGraduationApplyDao gjtGraduationApplyDao;

	@Autowired
	private GjtGraduationSpecialtyDao gjtGraduationSpecialtyDao;

	@Autowired
	private GjtGraduationAdviserDao gjtGraduationAdviserDao;

	@Override
	public Page<Map<String, Object>> queryGraduationSpecialtyList(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		Page<Map<String, Object>> graduationSpecialtyPage = gjtGraduationNativeDao.queryGraduationSpecialtyList(searchParams, pageRequst);
		
		//查询申请学员和指导老师
		if (graduationSpecialtyPage != null && graduationSpecialtyPage.getContent() != null) {
			for (Map<String, Object> graduationSpecialty : graduationSpecialtyPage.getContent()) {
				String batchId = (String)graduationSpecialty.get("batchId");
				String specialtyId = (String)graduationSpecialty.get("specialtyId");
				String trainingLevel = (String)graduationSpecialty.get("trainingLevel");
				
				List<GjtGraduationApply> applyList = gjtGraduationApplyDao.queryListBySpecialty(batchId, specialtyId);
				graduationSpecialty.put("applyList", applyList);
				
				GjtGraduationSpecialty gjtGraduationSpecialty = gjtGraduationSpecialtyDao.queryOneBySpecialty(batchId, specialtyId, trainingLevel);
				if (gjtGraduationSpecialty != null) {
					List<GjtGraduationAdviser> adviserList = gjtGraduationSpecialty.getGjtGraduationAdvisers();
					graduationSpecialty.put("adviserList", adviserList);
				}
				
			}
		}
		
		return graduationSpecialtyPage;
	}

	@Override
	public GjtGraduationSpecialty queryOneBySpecialty(String batchId, String specialtyId, String trainingLevel) {
		log.info("batchId:" + batchId + ", specialtyId:" + specialtyId + ", trainingLevel:" + trainingLevel);
		return gjtGraduationSpecialtyDao.queryOneBySpecialty(batchId, specialtyId, trainingLevel);
	}

	@Override
	public void insert(GjtGraduationSpecialty entity) {
		log.info("entity:[" + entity + "]");
		
		entity.setSettingId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		
		gjtGraduationSpecialtyDao.save(entity);
		if (entity.getGjtGraduationAdvisers() != null) {
			gjtGraduationAdviserDao.save(entity.getGjtGraduationAdvisers());
		}
	}

	@Override
	public GjtGraduationSpecialty queryById(String id) {
		log.info("id:" + id);
		return gjtGraduationSpecialtyDao.findOne(id);
	}

	@Override
	public void update(GjtGraduationSpecialty entity) {
		log.info("entity:[" + entity + "]");
		
		entity.setUpdatedDt(new Date());
		
		gjtGraduationSpecialtyDao.save(entity);
		if (entity.getGjtGraduationAdvisers() != null) {
			gjtGraduationAdviserDao.save(entity.getGjtGraduationAdvisers());
		}
		
	}

}
