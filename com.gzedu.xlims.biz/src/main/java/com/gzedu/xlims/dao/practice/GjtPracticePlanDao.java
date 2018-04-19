package com.gzedu.xlims.dao.practice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.practice.repository.GjtPracticePlanRepository;
import com.gzedu.xlims.pojo.practice.GjtPracticePlan;

@Repository
public class GjtPracticePlanDao {

	@Autowired
	private GjtPracticePlanRepository gjtPracticePlanRepository;

	@Autowired
	private CommonDao commonDao;

	public Page<GjtPracticePlan> findAll(Specification<GjtPracticePlan> spec, PageRequest pageRequst) {
		return gjtPracticePlanRepository.findAll(spec, pageRequst);
	}

	public GjtPracticePlan save(GjtPracticePlan entity) {
		return gjtPracticePlanRepository.save(entity);
	}

	public GjtPracticePlan findOne(String id) {
		return gjtPracticePlanRepository.findOne(id);
	}

	public GjtPracticePlan findByGradeIdAndOrgIdAndIsDeleted(String gradeId, String orgId, String isDeleted) {
		return gjtPracticePlanRepository.findByGradeIdAndOrgIdAndIsDeleted(gradeId, orgId, isDeleted);
	}

	public GjtPracticePlan findByGradeIdAndOrgIdAndStatusAndIsDeleted(String gradeId, String orgId, int status,
			String isDeleted) {
		return gjtPracticePlanRepository.findByGradeIdAndOrgIdAndStatusAndIsDeleted(gradeId, orgId, status, isDeleted);
	}

	public List<GjtPracticePlan> findByOrgIdAndStatus(String orgId, int status, String isDeleted) {
		return gjtPracticePlanRepository.findByOrgIdAndStatusAndIsDeleted(orgId, status, isDeleted);
	}

	public Map<String, String> getPracticePlanMap(String orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("		 select ").append("        	PRACTICE_PLAN_ID, ").append("        	PRACTICE_PLAN_NAME ")
				.append("    from ").append("        	GJT_PRACTICE_PLAN ").append("    where ")
				.append("        	IS_DELETED = 'N' ").append("        	and STATUS = 3 ")
				.append("        	and ORG_ID in ").append("        		( ").append("        		 select ")
				.append("        		 		org.ID ").append("        		 from ")
				.append("        		 		GJT_ORG org ").append("        		 where ")
				.append("        		 		org.IS_DELETED = 'N' ")
				.append("        		 		start with org.ID = :orgId ")
				.append("        		 		connect by prior ORG.PERENT_ID = ORG.ID ").append("        		) ")
				.append("    order by CREATED_DT desc ");

		map.put("orgId", orgId);

		List<Object[]> objs = commonDao.queryForObjectListNative(sb.toString(), map);
		Map<String, String> resultMap = new LinkedHashMap<String, String>();
		for (Object[] obj : objs) {
			resultMap.put(obj[0].toString(), obj[1].toString());
		}

		return resultMap;
	}

}
