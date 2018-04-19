package com.gzedu.xlims.dao.thesis;

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
import com.gzedu.xlims.dao.thesis.repository.GjtThesisPlanRepository;
import com.gzedu.xlims.pojo.thesis.GjtThesisPlan;

@Repository
public class GjtThesisPlanDao {

	@Autowired
	private GjtThesisPlanRepository gjtThesisPlanRepository;

	@Autowired
	private CommonDao commonDao;

	public Page<GjtThesisPlan> findAll(Specification<GjtThesisPlan> spec, PageRequest pageRequst) {
		return gjtThesisPlanRepository.findAll(spec, pageRequst);
	}

	public GjtThesisPlan save(GjtThesisPlan entity) {
		return gjtThesisPlanRepository.save(entity);
	}

	public GjtThesisPlan findOne(String id) {
		return gjtThesisPlanRepository.findOne(id);
	}

	public GjtThesisPlan findByGradeIdAndOrgIdAndIsDeleted(String gradeId, String orgId, String isDeleted) {
		return gjtThesisPlanRepository.findByGradeIdAndOrgIdAndIsDeleted(gradeId, orgId, isDeleted);
	}

	public GjtThesisPlan findByGradeIdAndOrgIdAndStatusAndIsDeleted(String gradeId, String orgId, int status,
			String isDeleted) {
		return gjtThesisPlanRepository.findByGradeIdAndOrgIdAndStatusAndIsDeleted(gradeId, orgId, status, isDeleted);
	}

	public Map<String, String> getThesisPlanMap(String orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("		 select ").append("        	THESIS_PLAN_ID, ").append("        	THESIS_PLAN_NAME ")
				.append("    from ").append("        	GJT_THESIS_PLAN ").append("    where ")
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

	public List<GjtThesisPlan> findByOrgIdAndStatusAndIsDeleted(String orgId, int status, String isDeleted) {
		return gjtThesisPlanRepository.findByOrgIdAndStatusAndIsDeleted(orgId, status, isDeleted);
	}
}
