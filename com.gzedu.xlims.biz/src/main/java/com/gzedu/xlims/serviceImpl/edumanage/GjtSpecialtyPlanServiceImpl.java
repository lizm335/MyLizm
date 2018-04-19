package com.gzedu.xlims.serviceImpl.edumanage;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.edumanage.GjtTextbookPlanOwnershipDao;
import com.gzedu.xlims.dao.organization.GjtSpecialtyPlanDao;
import com.gzedu.xlims.pojo.GjtSpecialtyPlan;
import com.gzedu.xlims.pojo.GjtTextbookPlanOwnership;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtSpecialtyPlanService;

/**
 * 
 * 功能说明：课程管理
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月17日
 * @version 2.5
 *
 */
@Service
public class GjtSpecialtyPlanServiceImpl implements GjtSpecialtyPlanService {
	@Autowired
	private GjtSpecialtyPlanDao gjtSpecialtyPlanDao;
	@Autowired
	private GjtTextbookPlanOwnershipDao gjtTextbookPlanOwnershipDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;

	@Override
	public Page<GjtSpecialtyPlan> queryAll(final String schoolId, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("xxId", new SearchFilter("xxId", Operator.EQ, schoolId));

		Specification<GjtSpecialtyPlan> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtSpecialtyPlan.class);

		return gjtSpecialtyPlanDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtSpecialtyPlan queryBy(String id) {
		return gjtSpecialtyPlanDao.findOne(id);
	}

	@Override
	public List<GjtSpecialtyPlan> findBySpecialtyId(String id) {
		return gjtSpecialtyPlanDao.findBySpecialtyId(id);
	}

	@SuppressWarnings("unchecked")
	public List<GjtSpecialtyPlan> findBySpecialtyId(String gradeSpecialtyId, String specialtyId,
			List<String> notInCourseIds) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from gjt_specialty_plan sp where sp.specialty_id=:specialtyId ");

		if (notInCourseIds == null) {
			sql.append(" and not exists ");
			sql.append(" ( ");
			sql.append(" select 1 from gjt_teach_plan tp,gjt_course c ");
			sql.append(" where tp.course_id = sp.course_id and c.course_id=tp.course_id ");
			sql.append(" and tp.is_deleted='N' and c.is_deleted='N'");
			sql.append(" and tp.grade_specialty_id =:gradeSpecialtyId");
			sql.append(" )");
		}else{
			sql.append(" and sp.course_id not in (:notInId)");
		}

		sql.append(" order by sp.term_type_code");

		Query query = em.createNativeQuery(sql.toString(), GjtSpecialtyPlan.class)
				.setParameter("specialtyId", specialtyId);
		if (notInCourseIds == null)
			query.setParameter("gradeSpecialtyId", gradeSpecialtyId);
		else
			query.setParameter("notInId", notInCourseIds);
		List<GjtSpecialtyPlan> list = query.getResultList();
		return list;
	}

	@Override
	public void delete(String[] ids) {
		for (String id : ids) {
			gjtSpecialtyPlanDao.delete(id);
		}
	}

	@Override
	public void delete(String id) {
		gjtSpecialtyPlanDao.delete(id);
	}

	@Override
	@Transactional
	public void insert(GjtSpecialtyPlan entity) {
		entity.setId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		gjtSpecialtyPlanDao.save(entity);
	}

	@Override
	@Transactional
	public void insert(GjtSpecialtyPlan entity, List<GjtTextbookPlanOwnership> gjtTextbookPlanOwnershipList) {
		entity.setId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		gjtSpecialtyPlanDao.save(entity);
		if (gjtTextbookPlanOwnershipList != null) {
			for (GjtTextbookPlanOwnership gjtTextbookPlanOwnership : gjtTextbookPlanOwnershipList) {
				gjtTextbookPlanOwnership.setId(UUIDUtils.random());
				gjtTextbookPlanOwnership.setSpecialtyPlanId(entity.getId());
				gjtTextbookPlanOwnershipDao.save(gjtTextbookPlanOwnership);
			}
		}
	}

	@Override
	@Transactional
	public void update(GjtSpecialtyPlan entity) {
		gjtSpecialtyPlanDao.save(entity);
	}

	@Override
	@Transactional
	public void update(GjtSpecialtyPlan entity, List<GjtTextbookPlanOwnership> gjtTextbookPlanOwnershipList) {
		gjtSpecialtyPlanDao.save(entity);
		if (gjtTextbookPlanOwnershipList != null) {
			for (GjtTextbookPlanOwnership gjtTextbookPlanOwnership : gjtTextbookPlanOwnershipList) {
				gjtTextbookPlanOwnership.setId(UUIDUtils.random());
				gjtTextbookPlanOwnership.setSpecialtyPlanId(entity.getId());
				gjtTextbookPlanOwnershipDao.save(gjtTextbookPlanOwnership);
			}
		}
	}

	@Override
	public int deleteBySpecialtyId(String specialtyId) {
		return gjtSpecialtyPlanDao.deleteBySpecialtyId(specialtyId);
	}

	@Override
	public void insert(List<GjtSpecialtyPlan> entities) {
		gjtSpecialtyPlanDao.save(entities);
	}

	@Override
	public boolean isExists(String specialtyId, String courseId) {
		GjtSpecialtyPlan plan = gjtSpecialtyPlanDao.findBySpecialtyIdAndCourseId(specialtyId, courseId);
		return plan != null;
	}

	@Autowired
	CommonMapService commonMapService;

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Long> countBySpecialtyId(List<String> ids) {
		String sql = "select specialty_id ID,COUNT(1) NAME from gjt_specialty_plan where specialty_id in ?1 group by  specialty_id";
		List<Object[]> list = em.createNativeQuery(sql).setParameter(1, ids).getResultList();
		Map map = new HashMap<String, Long>();
		for (Object[] obj : list) {
			map.put(obj[0].toString(), Long.valueOf(obj[1].toString()));
		}
		return map;
	}

	@Override
	public GjtSpecialtyPlan findBySpecialtyIdAndCourseId(String specialtyId, String courseId) {
		return gjtSpecialtyPlanDao.findBySpecialtyIdAndCourseId(specialtyId, courseId);
	}

}
