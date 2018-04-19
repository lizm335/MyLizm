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
import com.gzedu.xlims.dao.thesis.GjtThesisPlanApprovalDao;
import com.gzedu.xlims.dao.thesis.GjtThesisPlanDao;
import com.gzedu.xlims.pojo.thesis.GjtThesisPlan;
import com.gzedu.xlims.pojo.thesis.GjtThesisPlanApproval;
import com.gzedu.xlims.service.thesis.GjtThesisPlanService;

@Service
public class GjtThesisPlanServiceImpl implements GjtThesisPlanService {

	private static final Log log = LogFactory.getLog(GjtThesisPlanServiceImpl.class);

	@Autowired
	private GjtThesisPlanDao gjtThesisPlanDao;

	@Autowired
	private GjtThesisPlanApprovalDao gjtThesisPlanApprovalDao;

	@Override
	public Page<GjtThesisPlan> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		searchParams.put("EQ_isDeleted", "N");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtThesisPlan> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtThesisPlan.class);
		return gjtThesisPlanDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtThesisPlan insert(GjtThesisPlan entity) {
		log.info("entity:[" + entity + "]");
		entity.setThesisPlanId(UUIDUtils.random());
		entity.setStatus(1);
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		GjtThesisPlan gjtThesisPlan = gjtThesisPlanDao.save(entity);

		// 增加审批记录
		GjtThesisPlanApproval approval = new GjtThesisPlanApproval();
		approval.setApprovalId(UUIDUtils.random());
		approval.setThesisPlanId(entity.getThesisPlanId());
		approval.setOperaRole(1);
		approval.setOperaType(1);
		approval.setCreatedBy(entity.getCreatedBy());
		approval.setCreatedDt(new Date());
		approval.setIsDeleted("N");
		gjtThesisPlanApprovalDao.save(approval);

		return gjtThesisPlan;
	}

	@Override
	public void update(GjtThesisPlan entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtThesisPlanDao.save(entity);
	}

	@Override
	public GjtThesisPlan findOne(String id) {
		return gjtThesisPlanDao.findOne(id);
	}

	@Override
	public GjtThesisPlan findByGradeIdAndOrgId(String gradeId, String orgId) {
		return gjtThesisPlanDao.findByGradeIdAndOrgIdAndIsDeleted(gradeId, orgId, "N");
	}

	@Override
	public GjtThesisPlan findByGradeIdAndOrgIdAndStatus(String gradeId, String orgId, int status) {
		return gjtThesisPlanDao.findByGradeIdAndOrgIdAndStatusAndIsDeleted(gradeId, orgId, status, "N");
	}

	@Override
	public Map<String, String> getThesisPlanMap(String orgId) {
		return gjtThesisPlanDao.getThesisPlanMap(orgId);
	}

	@Override
	public List<GjtThesisPlan> findByOrgIdAndStatus(String orgId, int status) {
		return gjtThesisPlanDao.findByOrgIdAndStatusAndIsDeleted(orgId, status, "N");
	}

}
