/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.organization;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.gzedu.xlims.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.organization.GjtSpecialtyDao;
import com.gzedu.xlims.dao.organization.GjtSpecialtyModuleLimitDao;
import com.gzedu.xlims.dao.organization.GjtSpecialtyPlanDao;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtSpecialtyModuleLimit;
import com.gzedu.xlims.pojo.GjtSpecialtyPlan;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.dto.GjtSpecialtyDto;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

/**
 * 
 * 功能说明：专业管理 实现接口
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月5日
 * @version 2.5
 * @since JDK1.7
 *
 */

@Service
public class GjtSpecialtyServiceImpl extends BaseServiceImpl<GjtSpecialty> implements GjtSpecialtyService {

	@PersistenceContext(unitName = "entityManagerFactory")
	protected EntityManager em;

	@Autowired
	private GjtSpecialtyDao gjtSpecialtyDao;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtSpecialtyModuleLimitDao gjtSpecialtyModuleLimitDao;
	
	@Autowired
	private GjtSpecialtyPlanDao gjtSpecialtyPlanDao;

	@Override
	protected BaseDao<GjtSpecialty, String> getBaseDao() {
		return this.gjtSpecialtyDao;
	}

	/**
	 * 按条件查询，分页
	 * 
	 * @param seachEntity
	 * @param pageRequest
	 * @return
	 */
	@Override
	public Page<GjtSpecialty> querySource(final GjtSpecialty seachEntity, PageRequest pageRequest) {
		Specification<GjtSpecialty> spec = new Specification<GjtSpecialty>() {

			@Override
			public Predicate toPredicate(Root<GjtSpecialty> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<String> pycc = root.get("pycc"); // 条件
				Path<String> zymc = root.get("zymc"); // 条件

				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();

				// 条件
				if (StringUtils.isNotBlank(seachEntity.getPycc())) {
					expressions.add(cb.equal(pycc, seachEntity.getPycc()));
				}
				// 条件
				if (StringUtils.isNotBlank(seachEntity.getZymc())) {
					expressions.add(cb.like(zymc, "%" + seachEntity.getZymc() + "%"));
				}

				return predicate;

			}
		};

		return gjtSpecialtyDao.findAll(spec, pageRequest);
	}

	/**
	 * 添加专业
	 * 
	 * @param entity
	 */
	@Override
	public Boolean saveEntity(GjtSpecialty entity) {
		GjtSpecialty save = gjtSpecialtyDao.save(entity);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 修改专业信息
	 */
	@Override
	public Boolean updateEntity(GjtSpecialty id) {
		GjtSpecialty save = gjtSpecialtyDao.save(id);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查询单个专业信息
	 */
	@Override
	public GjtSpecialty queryById(String id) {
		return gjtSpecialtyDao.findOne(id);
	}

	/**
	 * 查询所有专业
	 */
	@Override
	public List<GjtSpecialty> queryAll() {
		return gjtSpecialtyDao.findAll();
	}

	/**
	 * 假删除
	 */
	@Override
	public Boolean deleteById(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				gjtSpecialtyDao.deleteById(id, "Y");
			}
		}
		return true;
	}

	@Override
	public Page<GjtSpecialty> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));

		Map<String, String> orgIds = commonMapService.getOrgMapByOrgId(orgId);

		filters.put("gjtOrg.id", new SearchFilter("gjtOrg.id", Operator.IN, orgIds.keySet()));

		Specification<GjtSpecialty> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtSpecialty.class);
		return gjtSpecialtyDao.findAll(spec, pageRequst);
	}

	@Override
	public Page<GjtSpecialty> queryPage(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));

		// Map<String, String> orgIds =
		// commonMapService.getOrgMapByOrgId(orgId);

		// filters.put("gjtOrg.id", new SearchFilter("gjtOrg.id", Operator.IN,
		// orgIds.keySet()));

		Specification<GjtSpecialty> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtSpecialty.class);
		return gjtSpecialtyDao.findAll(spec, pageRequst);
	}

	/**
	 * 查询年级没有开设的专业
	 * 
	 * @param gradeId
	 * @return
	 */
	public Map<String, String> getSpecialty(String gradeId) {
		String sql = "select SPECIALTY_ID id from GJT_GRADE_SPECIALTY where GRADE_ID=:gradeId and IS_DELETED='N'";
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setParameter("gradeId", gradeId);
		List<Object> rows = query.getResultList();
		Map<String, String> resultMap = new LinkedHashMap<String, String>();
		for (Object obj : rows) {
			Map<String, String> m = (Map<String, String>) obj;
			resultMap.put(m.get("ID"), "");
		}
		return resultMap;
	}

	/**
	 * 查询不存在年级的专业
	 * 
	 * @param orgId
	 * @param gradeId
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@Override
	public Page<GjtSpecialty> queryGradeSpecialtyAll(String orgId, String gradeId, int type,
			Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Map<String, String> map = this.getSpecialty(gradeId);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		Map<String, String> orgIds = commonMapService.getOrgMapByOrgId(orgId);
		filters.put("gjtOrg.id", new SearchFilter("gjtOrg.id", Operator.IN, orgIds.keySet()));

		if (type == 0) {// 已选
			if ((map != null) && (map.size() > 0)) {
				filters.put("specialtyId", new SearchFilter("specialtyId", Operator.IN, map.keySet()));
			} else {
				filters.put("specialtyId", new SearchFilter("specialtyId", Operator.EQ, ""));
			}
		} else {// 未选
			if ((map != null) && (map.size() > 0)) {
				filters.put("specialtyId", new SearchFilter("specialtyId", Operator.NOTIN, map.keySet()));
			}
		}
		Specification<GjtSpecialty> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtSpecialty.class);
		return gjtSpecialtyDao.findAll(spec, pageRequst);
	}

	@Override
	public List<Map<String, String>> querySpecialtyCourse(String id) {
		String sql = "select m.module_id id, m.score NAME,m.TOTALSCORE,m.CRTVU_SCORE,d.code CODE from gjt_specialty_module_limit m  "
				+ " left join TBL_SYS_DATA d on m.module_id=d.id "
				+ " where m.is_deleted='N' and m.specialty_id =:id order by m.module_id";
		Query query = em.createNativeQuery(sql);
		query.setParameter("id", id);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Object> rows = query.getResultList();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (Object obj : rows) {
			Map<String, Object> m = (Map<String, Object>) obj;
			Map<String, String> resultMap = new HashMap<String, String>();
			resultMap.put("id", m.get("ID").toString());
			resultMap.put("score", m.get("NAME") == null ? "0" : m.get("NAME").toString());
			resultMap.put("totalScore", m.get("TOTALSCORE") == null ? "0" : m.get("TOTALSCORE").toString());
			resultMap.put("crtvuScore", m.get("CRTVU_SCORE") != null ? m.get("CRTVU_SCORE").toString() : "0");
			resultMap.put("code", (String) m.get("CODE"));
			list.add(resultMap);
		}
		return list;
	}

	@Override
	public Boolean updateSpecialtyModule(GjtSpecialtyModuleLimit sm) {
		GjtSpecialtyModuleLimit moduleLimit = gjtSpecialtyModuleLimitDao.save(sm);
		if (moduleLimit != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void deleteSpecialtyModule(String id) {
		gjtSpecialtyModuleLimitDao.deleteBySpecialtyId(id);
	}

	@Override
	public Boolean saveSpecialtyModule(GjtSpecialtyModuleLimit sm) {
		GjtSpecialtyModuleLimit moduleLimit = gjtSpecialtyModuleLimitDao.save(sm);
		if (moduleLimit != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<GjtSpecialtyModuleLimit> querySpecialtyModuleList(String specialtyId) {
		return gjtSpecialtyModuleLimitDao.findBySpecialtyIdAndIsDeleted(specialtyId, "N");
	}

	@Override
	public GjtSpecialty findBySpecialtyById(String SpecialtyId) {
		GjtSpecialty gjtSpecialty = gjtSpecialtyDao.findOne(SpecialtyId);
		return gjtSpecialty;
	}

	/**
	 * 共享专业
	 * 
	 * @return
	 */
	@Override
	public Page<GjtSpecialty> queryAllAndShare(String schoolIds, Map<String, Object> searchParams,
			PageRequest pageRequst, List<String> specialtyId) {
		Criteria<GjtSpecialty> spec = new Criteria<GjtSpecialty>();
		spec.add(Restrictions.eq("isDeleted", "N", true));
		if (specialtyId.size() != 0) {
			spec.add(Restrictions.or(Restrictions.in("specialtyId", specialtyId, true),
					Restrictions.eq("gjtOrg.id", schoolIds, true)));
		} else {
			spec.add(Restrictions.eq("gjtOrg.id", schoolIds, true));
		}
		spec.addAll(Restrictions.parse(searchParams));
		return gjtSpecialtyDao.findAll(spec, pageRequst);

	}

	@Override
	public List<GjtSpecialty> findSpecialtyByGradeId(String gradeId) {
		List<GjtSpecialty> list = gjtSpecialtyDao.findSpecialtyByGradeId(gradeId);
		return list;
	}

	@Override
	public List<GjtSpecialty> findSpecialtyBySpecialtyBaseId(String specialtyBaseId) {
		List<GjtSpecialty> list = gjtSpecialtyDao.findSpecialtyBySpecialtyBaseId(specialtyBaseId);
		return list;
	}

	public List<GjtSpecialtyDto> findSpecialtyDtoByGradeId(String gradeId) {
		List<GjtSpecialtyDto> list = gjtSpecialtyDao.findSpecialtyDtoByGradeId(gradeId);
		return list;
	}

	@Override
	public List<GjtSpecialty> findAll(Iterable<String> ids) {
		return (List<GjtSpecialty>)gjtSpecialtyDao.findAll(ids);
	}

	@Override
	public GjtSpecialty copy(String specialtyId, String ruleCode, GjtUserAccount user) {
		GjtSpecialty gjtSpecialty = gjtSpecialtyDao.findOne(specialtyId);
		if (gjtSpecialty == null) {
			return null;
		} else {
			GjtSpecialty copy = new GjtSpecialty();
			copy.setSpecialtyId(UUIDUtils.random());
			copy.setRuleCode(ruleCode);
			copy.setSpecialtyBaseId(gjtSpecialty.getSpecialtyBaseId());
			copy.setPycc(gjtSpecialty.getPycc());
			copy.setSpecialtyCategory(gjtSpecialty.getSpecialtyCategory());
			copy.setZymc(gjtSpecialty.getZymc());
			copy.setZylb(gjtSpecialty.getZylb());
			copy.setType(gjtSpecialty.getType());
			copy.setSyhy(gjtSpecialty.getSyhy());
			copy.setZyfm(gjtSpecialty.getZyfm());
			copy.setXslx(gjtSpecialty.getXslx());
			copy.setSubject(gjtSpecialty.getSubject());
			copy.setCategory(gjtSpecialty.getCategory());
			copy.setXz(gjtSpecialty.getXz());
			copy.setZxf(gjtSpecialty.getZxf());
			copy.setZdbyxf(gjtSpecialty.getZdbyxf());
			copy.setBxxf(gjtSpecialty.getBxxf());
			copy.setXxxf(gjtSpecialty.getXxxf());
			copy.setZyddksxf(gjtSpecialty.getZyddksxf());
			copy.setXxId(gjtSpecialty.getXxId());
			copy.setYxId(gjtSpecialty.getYxId());
			copy.setGjtOrg(gjtSpecialty.getGjtOrg());
			copy.setOrgCode(gjtSpecialty.getGjtOrg().getCode());
			copy.setCreatedDt(new Date());
			copy.setCreatedBy(user.getId());
			copy.setIsDeleted("N");
			copy.setIsEnabled("1");
			copy.setStatus(2);
			copy.setVersion(BigDecimal.valueOf(3));
			
			List<GjtSpecialtyModuleLimit> copyModules = new ArrayList<GjtSpecialtyModuleLimit>();
			List<Map<String, String>> modules = this.querySpecialtyCourse(specialtyId);// 专业对应课程模块
			for (Map<String, String> module : modules) {
				GjtSpecialtyModuleLimit sm = new GjtSpecialtyModuleLimit();
				sm.setId(UUIDUtils.random());
				sm.setCreatedDt(new Date());
				sm.setCreatedBy(user.getId());
				sm.setIsDeleted("N");
				sm.setModuleId(module.get("id"));
				sm.setGjtSpecialty(copy);
				sm.setScore(module.get("score"));
				sm.setTotalScore(module.get("totalScore"));
				sm.setCrtvuScore(Double.parseDouble(module.get("crtvuScore")));
				
				copyModules.add(sm);
			}
			
			List<GjtSpecialtyPlan> copyPlans = new ArrayList<GjtSpecialtyPlan>();
			List<GjtSpecialtyPlan> gjtSpecialtyPlans = gjtSpecialty.getGjtSpecialtyPlans();  //专业计划
			for (GjtSpecialtyPlan plan : gjtSpecialtyPlans) {
				GjtSpecialtyPlan copyPlan = new GjtSpecialtyPlan();
				copyPlan.setId(UUIDUtils.random());
				copyPlan.setXxId(plan.getXxId());
				copyPlan.setCreatedBy(user.getId());
				copyPlan.setCreatedDt(new Date());
				copyPlan.setSpecialtyId(copy.getSpecialtyId());
				copyPlan.setCourseCategory(plan.getCourseCategory());
				copyPlan.setCoursetype(plan.getCoursetype());
				copyPlan.setCourseId(plan.getCourseId());
				copyPlan.setCourseTypeId(plan.getCourseTypeId());
				copyPlan.setExamUnit(plan.getExamUnit());
				copyPlan.setHours(plan.getHours());
				copyPlan.setScore(plan.getScore());
				copyPlan.setTermTypeCode(plan.getTermTypeCode());

				copyPlans.add(copyPlan);
			}
			
			gjtSpecialtyDao.save(copy);
			gjtSpecialtyModuleLimitDao.save(copyModules);
			gjtSpecialtyPlanDao.save(copyPlans);
			
			return copy;
		}
	}

	@Override
	public List<GjtSpecialty> findSpecialtyByOrgId(String xxId) {
		return gjtSpecialtyDao.findSpecialtyByOrgId(xxId);
	}

	@Override
	public GjtSpecialty queryByRuleCodeAndXxId(String ruleCode, String xxId) {
		return gjtSpecialtyDao.findByRuleCodeAndXxIdAndIsDeleted(ruleCode, xxId, Constants.BOOLEAN_NO);
	}
}
