
package com.gzedu.xlims.serviceImpl.edumanage;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.edumanage.GjtGradeSpecialtyDao;
import com.gzedu.xlims.dao.edumanage.GjtTeachPlanDao;
import com.gzedu.xlims.dao.organization.GjtGradeDao;
import com.gzedu.xlims.dao.organization.GjtSpecialtyDao;
import com.gzedu.xlims.pojo.*;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyPlanService;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyService;
import com.gzedu.xlims.service.edumanage.GjtTeachPlanService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * 功能说明：年级专业实现接口
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月20日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Service
public class GjtGradeSpecialtyServiceImpl extends BaseServiceImpl<GjtGradeSpecialty>
		implements GjtGradeSpecialtyService {

	@Autowired
	GjtGradeSpecialtyDao gjtGradeSpecialtyDao;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtGradeSpecialtyPlanService gjtGradeSpecialtyPlanService;

	@Autowired
	GjtSpecialtyDao gjtSpecialtyDao;

	@Autowired
	GjtGradeDao gjtGradeDao;

	@Autowired
	GjtTeachPlanService gjtTeachPlanService;

	@Autowired
	GjtTeachPlanDao gjtTeachPlanDao;

	@Override
	protected BaseDao<GjtGradeSpecialty, String> getBaseDao() {
		return this.gjtGradeSpecialtyDao;
	}

	@Override
	public Page<GjtGradeSpecialty> queryAll(String schoolId, Map<String, Object> searchParams,
			PageRequest pageRequest) {
		Map<String, String> orgId = commonMapService.getOrgMapByOrgId(schoolId);
		Criteria<GjtGradeSpecialty> spec = new Criteria<GjtGradeSpecialty>();
		spec.add(Restrictions.eq("isDeleted", "N", true));
		spec.add(Restrictions.eq("gjtGrade.isDeleted", "N", true));
		spec.add(Restrictions.eq("gjtSpecialty.isDeleted", "N", true));
		spec.add(Restrictions.in("gjtGrade.gjtSchoolInfo.id", orgId.keySet(), true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtGradeSpecialtyDao.findAll(spec, pageRequest);
	}

	@Override
	public GjtGradeSpecialty queryBy(String id) {
		return gjtGradeSpecialtyDao.findOne(id);
	}

	@Override
	public void delete(Iterable<String> ids) {
		if (ids != null) {
			for (String id : ids) {
				gjtGradeSpecialtyDao.delete(id);
			}
		}
	}

	@Override
	public boolean delete(String id) {
		gjtGradeSpecialtyDao.delete(id);
		return true;
	}

	@Override
	public boolean insert(GjtGradeSpecialty entity) {
		return gjtGradeSpecialtyDao.save(entity) != null;
	}

	@Override
	public void update(GjtGradeSpecialty entity) {
		gjtGradeSpecialtyDao.save(entity);
	}

	@Override
	public Iterable<GjtGradeSpecialty> save(Iterable<GjtGradeSpecialty> entities) {
		return gjtGradeSpecialtyDao.save(entities);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void saveGradeSpecialty(String gradeId, List<JSONObject> gradeSpecialties, String realName,
			String copyGradeId) throws Exception {
		GjtGrade grade = gjtGradeDao.findOne(gradeId);
		List<GjtGradeSpecialty> gjtGradeSpecialties = gjtGradeSpecialtyDao.findByGradeId(gradeId);
		List<String> containIds = new ArrayList<String>();
		Date now = new Date();
		for (JSONObject json : gradeSpecialties) {
			String gradeSpecialtyId = (String) json.get("id");// 年级专业关联id
			String specialtyId = json.getString("specialtyId");
			List<GjtStudyCenter> gjtStudyCenters = new ArrayList<GjtStudyCenter>();
			if (json.get("applyRange") != null) {
				for (Object id : json.getJSONArray("applyRange")) {
					gjtStudyCenters.add(new GjtStudyCenter((String) id));
				}
			}
			GjtGradeSpecialty ggs;
			if (this.isContainSpecialty(gjtGradeSpecialties, gradeSpecialtyId)) {
				ggs = gjtGradeSpecialtyDao.findOne(gradeSpecialtyId);
				ggs.setGjtStudyCenters(gjtStudyCenters);
				ggs.setUpdatedDt(now);
				ggs.setUpdatedBy(realName);
				gjtGradeSpecialtyDao.save(ggs);
				containIds.add(gradeSpecialtyId);
				continue;
			}

			ggs = new GjtGradeSpecialty();
			ggs.setCreatedBy(realName);
			ggs.setCreatedDt(now);
			ggs.setGjtGrade(grade);
			ggs.setGjtSpecialty(new GjtSpecialty(specialtyId));
			ggs.setId(UUIDUtils.random());
			ggs.setIsDeleted("N");
			ggs.setGjtStudyCenters(gjtStudyCenters);
			ggs.setVersion(new BigDecimal(3));
			// 创建一个 年级和专业关系，就克隆专业的教学计划
			if (copyGradeId == null || StringUtils.isEmpty(gradeSpecialtyId))
				gjtTeachPlanService.createTeachPlan(ggs);
			else {
				gjtTeachPlanService.createTeachPlan(ggs, gradeSpecialtyId);
			}
			this.insert(ggs);
		}
		for (GjtGradeSpecialty ggs : gjtGradeSpecialties) {
			if (!containIds.contains(ggs.getId())) {
				gjtGradeSpecialtyDao.deleteById(ggs.getId(), "Y");
				List<GjtTeachPlan> plans = gjtTeachPlanService.queryGjtTeachPlan(ggs.getId());
				if (plans == null)
					continue;
				for (GjtTeachPlan plan : plans) {
					gjtTeachPlanDao.deleteById(plan.getTeachPlanId(), "Y");
				}

			}

		}
	}

	private boolean isContainSpecialty(List<GjtGradeSpecialty> list, String id) {
		if (list == null || list.isEmpty())
			return false;
		for (GjtGradeSpecialty g : list) {
			if (g.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Map<String, String> findSpecialtyGrade(String orgId) {
		StringBuilder sb = new StringBuilder("select g.grade_id  ID,g.grade_name NAME from gjt_grade g");
		sb.append(" where");
		sb.append(" exists (select 1 from gjt_grade_specialty gs where gs.grade_id=g.grade_id and gs.is_deleted='N') ");
		sb.append(" and g.xx_id='" + orgId + "'");
		sb.append(" and g.is_deleted = 'N' AND g.start_date < sysdate order by g.start_date desc");
		return commonMapService.getMap(sb.toString());
	}

	@Override
	public List<GjtGradeSpecialty> findByGradeId(String gradeId) {
		return gjtGradeSpecialtyDao.findByGradeId(gradeId);

	}

	@Override
	public GjtGradeSpecialty findOne(String id) {
		return gjtGradeSpecialtyDao.findOne(id);
	}

	@Override
	public GjtGradeSpecialty queryByGradeIdAndSpecialtyId(String gradeId, String specialtyId) {
		return gjtGradeSpecialtyDao.findByGjtGradeGradeIdAndGjtSpecialtySpecialtyIdAndIsDeleted(gradeId, specialtyId,
				"N");
	}

	@Override
	public GjtGradeSpecialty queryByGradeIdAndSpecialtyIdAndStudyCenterId(String gradeId, String specialtyId,
			String studyCenterId) {
		return gjtGradeSpecialtyDao.findByGjtGradeGradeIdAndGjtSpecialtySpecialtyIdAndGjtStudyCentersIdAndIsDeleted(
				gradeId, specialtyId, studyCenterId, "N");
	}

	@Override
	public Map<String, String> getSpecialtyMap(String id, String gradeId,String pycc) {
		StringBuffer sql = new StringBuffer();
		sql.append("  select gs.SPECIALTY_ID ID, gs.ZYMC || '(' || gs.RULE_CODE || ')' NAME");
		sql.append("  from GJT_SPECIALTY gs");
		sql.append("  inner join gjt_grade_specialty ggs");
		sql.append("  on gs.specialty_id = ggs.specialty_id");
		sql.append("  and ggs.is_deleted = 'N'");
		sql.append("  where gs.is_deleted = 'N'");
		sql.append("  and gs.is_enabled = '1'");
		sql.append("  and gs.pycc = '"+pycc+"' ");
		sql.append("  and ggs.grade_id ='" + gradeId + "'");
		sql.append("  and gs.xx_id = (SELECT org.ID");
		sql.append("  FROM GJT_ORG org");
		sql.append("  WHERE org.IS_DELETED = 'N'");
		sql.append("  AND org.ORG_TYPE = '1'");
		sql.append("  START WITH org.ID ='" + id + "'");
		sql.append("  CONNECT BY PRIOR ORG.PERENT_ID = ORG.ID)");
		return commonMapService.getMap(sql.toString());
	}

	@Override
	public GjtGradeSpecialty findByGradeIdAndSpecialtyIdAndIsDeletedNew(String newGradeId, String newSpecialty,String xxzxId) {
		
		return gjtGradeSpecialtyDao.findByGradeIdAndSpecialtyIdAndIsDeletedNew(newGradeId,newSpecialty,xxzxId);
	}

}
