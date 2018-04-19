package com.gzedu.xlims.serviceImpl.graduation;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.graduation.GjtGraduationAdviserDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationNativeDao;
import com.gzedu.xlims.service.graduation.GjtGraduationAdviserService;

@Service
public class GjtGraduationAdviserServiceImpl implements GjtGraduationAdviserService {
	
	private static final Log log = LogFactory.getLog(GjtGraduationAdviserServiceImpl.class);

	@Autowired
	private GjtGraduationAdviserDao gjtGraduationAdviserDao;

	@Autowired
	private GjtGraduationNativeDao gjtGraduationNativeDao;

	@Override
	public void deleteBySettingId(String settingId) {
		log.info("settingId:" + settingId);
		gjtGraduationAdviserDao.deleteBySettingId(settingId);
	}

	@Override
	public Page<Map<String, Object>> queryGraduationAdviser(int adviserType, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		log.info("adviserType:" + adviserType + ", searchParams:[" + searchParams + "]");
		return gjtGraduationNativeDao.queryGraduationAdviser(adviserType, searchParams, pageRequst);
	}

}
