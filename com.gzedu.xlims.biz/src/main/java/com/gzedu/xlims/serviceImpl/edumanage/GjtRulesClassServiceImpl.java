/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.edumanage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.edumanage.GjtRulesClassDao;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtRulesClass;
import com.gzedu.xlims.service.edumanage.GjtRulesClassService;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2017年2月15日
 * @version 1.0
 *
 */
@Service
public class GjtRulesClassServiceImpl implements GjtRulesClassService {

	@Autowired
	GjtRulesClassDao gjtRulesClassDao;

	@Override
	public GjtRulesClass queryById(String id) {
		return gjtRulesClassDao.findOne(id);
	}

	// 相当于save，有就update，无就insert
	@Override
	public void update(GjtRulesClass gjtRulesClass) {
		gjtRulesClassDao.save(gjtRulesClass);
	}

	@Override
	public GjtRulesClass getByGradeId(String gradeId) {
		return gjtRulesClassDao.getByGradeId(gradeId);
	}

	@Override
	public GjtRulesClass queryCourseClassRule(String orgId) {
		return gjtRulesClassDao.queryCourseClassRule(orgId);
	}

	@Override
	public List<GjtRulesClass> findByOrgIdAndGradeId(String orgId, String currentGradeId) {
		return gjtRulesClassDao.findByOrgIdAndGradeId(orgId,currentGradeId);
	}

	@Override
	public List<GjtRulesClass> findByOrgIdAndGradeIdAndPointClassType(String id, String currentGradeId,
			String pointClassType) {
		return gjtRulesClassDao.findByOrgIdAndGradeIdAndPointClassType(id,currentGradeId,pointClassType);
	}

	@Override
	public GjtRulesClass findByOrgIdAndGradeIdAndPointClassTypeAndXxzxId(String id, String gradeId,String pointClassType,
			String xxzxId) {
		return gjtRulesClassDao.findByOrgIdAndGradeIdAndPointClassTypeAndXxzxId(id,gradeId,pointClassType,xxzxId);
	}

	@Override
	public List<GjtRulesClass> queryGjtRulesClassInfo(String id, String gradeId, String pointClassType, String xxzxId) {
		return gjtRulesClassDao.queryGjtRulesClassInfo(id,gradeId,pointClassType,xxzxId);
	}
	
	@Override
	public void insertPyccGjtRuleClass(GjtRulesClass entity,GjtUserAccount user) {
		try {
			if(entity!=null){
				entity.setXxid(UUIDUtils.random());
				entity.setOrgId(user.getGjtOrg().getId());
				entity.setPointClassType("2");
				entity.setXxzxId(null);				
				gjtRulesClassDao.save(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public GjtRulesClass findByOrgIdAndGradeIdAndXxzxId(String xxId, String gradeId, String xxzxId) {
		return gjtRulesClassDao.findByOrgIdAndGradeIdAndXxzxId(xxId,gradeId,xxzxId);
	}

	@Override
	public GjtRulesClass queryByOrgIdAndGradeId(String xxId, String gradeId,String pointClassType) {
		return gjtRulesClassDao.queryByOrgIdAndGradeId(xxId,gradeId,pointClassType);
	}
}
