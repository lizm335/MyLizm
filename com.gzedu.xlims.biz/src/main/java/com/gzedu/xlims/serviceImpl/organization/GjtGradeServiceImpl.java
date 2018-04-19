/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.organization;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtil;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.Collections3;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationApplyCertifDao;
import com.gzedu.xlims.dao.organization.GjtGradeDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.organization.GjtYearDao;
import com.gzedu.xlims.dao.recruitmanage.GjtEnrollBatchDao;
import com.gzedu.xlims.pojo.*;
import com.gzedu.xlims.pojo.dto.GjtGradeDto;
import com.gzedu.xlims.pojo.graduation.GjtApplyDegreeCertif;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyCertif;
import com.gzedu.xlims.pojo.graduation.GjtRulesClass;
import com.gzedu.xlims.pojo.opi.OpiTerm;
import com.gzedu.xlims.pojo.opi.OpiTermCourseData;
import com.gzedu.xlims.pojo.opi.RSimpleData;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtRulesClassService;
import com.gzedu.xlims.service.graduation.GjtApplyDegreeCertifService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.pcourse.PCourseServer;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

/**
 * 
 * 功能说明：年级管理 实现接口
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月13日
 * @version 2.5
 * @since JDK1.7
 *
 */

@Service
public class GjtGradeServiceImpl implements GjtGradeService {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(GjtGradeServiceImpl.class);
	@Autowired
	private GjtGradeDao gjtGradeDao;

	@Autowired
	private GjtGraduationApplyCertifDao gjtGraduationApplyCertifDao;

	@Autowired
	private GjtApplyDegreeCertifService gjtApplyDegreeCertifService;

	@Autowired
	private GjtEnrollBatchDao gjtEnrollBatchDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	private EntityManager em;
	
	@Autowired
	GjtRulesClassService gjtRulesClassService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private GjtYearDao gjtYearDao;

	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Autowired
	private PCourseServer pCourseServer;

	/**
	 * 按条件查询，分页
	 * 
	 * @param seachEntity
	 * @param pageRequest
	 * @return
	 */
	@Override
	public Page<GjtGradeDto> querySource(final GjtGrade seachEntity, PageRequest pageRequest) {
		StringBuffer sqlCount = new StringBuffer("select count(*) ");
		StringBuffer sql = new StringBuffer(" select  case when gst.num is null then 0  else gst.num end num,"
				+ "case when gggsp.specNum is null then 0  else gggsp.specNum end specNum, gg.*,xxmc ");
		StringBuffer sqlComm = new StringBuffer(
				" from GJT_GRADE gg left join  (select count(ti.grade_id)num,grade_id  from GJT_TERM_INFO"
						+ " ti where ti.is_deleted='N' group by ti.grade_id) gst on gg.grade_id=gst.grade_id "
						+ " left join GJT_SCHOOL_INFO gsi on gg.xx_id=gsi.id   left join  (select count(gsp.specialty_id) specNUM,gg.grade_id  "
						+ " from VIEW_TEACH_PLAN gtp inner join gjt_grade gg on gtp.grade_id = gg.grade_id  "
						+ " left join gjt_specialty gsp on gtp.kkzy = gsp.specialty_id where gg.is_deleted='N' and gsp.is_deleted='N' "
						+ " and gtp.is_deleted='N' group by gsp.specialty_id,gg.grade_id ) gggsp on gg.grade_id=gggsp.grade_id "
						+ " where gg.is_deleted='N' order by gg.CREATED_DT ");

		Map<String, String> map = new HashMap<String, String>();
		// 院校
		if (StringUtils.isNotBlank(seachEntity.getGradeCode())) {
			sqlComm.append(" and gg.grade_Code LIKE :gradeCode");
			map.put("gradeCode", "%" + seachEntity.getGradeCode() + "%");
		}
		// 学习中心
		if (StringUtils.isNotBlank(seachEntity.getGradeName())) {
			sqlComm.append(" and gg.grade_Name LIKE :gradeName");
			map.put("gradeName", "%" + seachEntity.getGradeName() + "%");
		}

		Query queryTotal = em.createNativeQuery(sqlCount.toString() + sqlComm.toString());
		for (Map.Entry<String, String> entry : map.entrySet()) {
			queryTotal.setParameter(entry.getKey(), entry.getValue());
		}

		Query query = em.createNativeQuery(sql.toString() + sqlComm.toString());
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(GjtGradeDto.class));
		for (Map.Entry<String, String> entry : map.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		query.setFirstResult(pageRequest.getOffset());
		query.setMaxResults(pageRequest.getPageSize());
		List<GjtGradeDto> resultList = query.getResultList();

		Page<GjtGradeDto> page = new PageImpl<GjtGradeDto>(resultList, pageRequest,
				NumberUtils.toLong(queryTotal.getSingleResult().toString()));
		return page;

	}

	/**
	 * 添加年级
	 * 
	 * @param entity
	 */
	@Override
	public Boolean saveEntity(GjtGrade entity) {
		GjtGrade save = gjtGradeDao.save(entity);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean saveAdd(GjtGrade item) {
		GjtGrade gjtGrade = new GjtGrade();
		gjtGrade.setGradeId(UUIDUtils.random());
		gjtGrade.setBelongYear(item.getBelongYear());
		gjtGrade.setGjtSchoolInfo(item.getGjtSchoolInfo());
		gjtGrade.setCreatedDt(DateUtils.getNowTime());
		gjtGrade.setEnterDt(item.getEnterDt());
		gjtGrade.setGradeCode(item.getGradeCode());
		gjtGrade.setGradeName(item.getGradeName());
		gjtGrade.setIsDeleted("N");
		gjtGrade.setIsEnabled("1");
		gjtGrade.setVersion(BigDecimal.valueOf(2.5));
		gjtGrade.setCreatedBy(item.getCreatedBy());
		// gjtGrade.setGjtStudyYearInfo(item.getGjtStudyYearInfo());
		return this.saveEntity(gjtGrade);
	}

	/**
	 * 修改年级信息
	 */
	@Override
	public Boolean updateEntity(GjtGrade gjtGrade) {
		GjtGrade save = gjtGradeDao.save(gjtGrade);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public Boolean updateAdd(GjtGrade item,GjtUserAccount user) {
		GjtGrade gjtGrade = gjtGradeDao.findOne(item.getGradeId());
		String isEnabled = gjtGrade.getIsEnabled();
		gjtGrade.setIsEnabled("1");
		gjtGrade.setUpdatedDt(DateUtils.getNowTime());
		gjtGrade.setUpdatedBy(item.getUpdatedBy());
		if (item.getStartDate() != null)
			gjtGrade.setStartDate(item.getStartDate());
		if (item.getEndDate() != null)
			gjtGrade.setEndDate(item.getEndDate());
		gjtGrade.setCourseStartDate(item.getCourseStartDate());
		gjtGrade.setCourseEndDate(item.getCourseEndDate());
		gjtGrade.setUpCourseEndDate(item.getUpCourseEndDate());
		gjtGrade.setUpAchievementDate(item.getUpAchievementDate());

		gjtGrade.setEnrollStartDate(item.getEnrollStartDate());
		gjtGrade.setEnrollEndDate(item.getEnrollEndDate());
		gjtGrade.setEnrollResponsible(item.getEnrollResponsible());
		gjtGrade.setEnrollResponsiblePer(item.getEnrollResponsiblePer());

		gjtGrade.setTextbookStartDate(item.getTextbookStartDate());
		gjtGrade.setTextbookEndDate(item.getTextbookEndDate());
		gjtGrade.setTextbookResponsible(item.getTextbookResponsible());
		gjtGrade.setTextbookResponsiblePer(item.getTextbookResponsiblePer());

		gjtGrade.setSchoolrollStartDate(item.getSchoolrollStartDate());
		gjtGrade.setSchoolrollEndDate(item.getSchoolrollEndDate());
		gjtGrade.setSchoolrollResponsible(item.getSchoolrollResponsible());
		gjtGrade.setSchoolrollResponsiblePer(item.getSchoolrollResponsiblePer());
		// gjtGrade.setEducationalStartDate(item.getEducationalStartDate());
		// gjtGrade.setEducationalEndDate(item.getEducationalEndDate());
		// gjtGrade.setEducationalResponsible(item.getEducationalResponsible());
		// gjtGrade.setEducationalResponsiblePer(item.getEducationalResponsiblePer());
		gjtGrade.setTeachingStartDate(item.getTeachingStartDate());
		gjtGrade.setTeachingEndDate(item.getTeachingEndDate());
		gjtGrade.setTeachingResponsible(item.getTeachingResponsible());
		gjtGrade.setTeachingResponsiblePer(item.getTeachingResponsiblePer());
		gjtGrade.setExamStartDate(item.getExamStartDate());
		gjtGrade.setExamEndDate(item.getExamEndDate());
		gjtGrade.setExamResponsible(item.getExamResponsible());
		gjtGrade.setExamResponsiblePer(item.getExamResponsiblePer());
		gjtGrade.setGraduationStartDate(item.getGraduationStartDate());
		gjtGrade.setGraduationEndDate(item.getGraduationEndDate());
		gjtGrade.setGraduationResponsible(item.getGraduationResponsible());
		gjtGrade.setGraduationResponsiblePer(item.getGraduationResponsiblePer());
		gjtGrade.setUpdatedDt(DateUtils.getNowTime());
		gjtGrade.setUpdatedBy(item.getUpdatedBy());
		gjtGrade.setPayStartDate(item.getPayStartDate());
		gjtGrade.setPayEndDate(item.getPayEndDate());
		gjtGrade.setOldStudentEnterDate(item.getOldStudentEnterDate());
		gjtGrade.setNewStudentEnterDate(item.getNewStudentEnterDate());
		this.updateEntity(gjtGrade);
		// 生成教务班级分班规则：当前学期复制上一学期的分班规则
		String orgId=user.getGjtOrg().getId();
		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);
		String prevGradeId=gjtGradeDao.queryGradeByYear(xxId);
		// 查询当前学期的上一学期是否存在分班规则
		List<GjtRulesClass> gjtRulesClass = gjtRulesClassService.findByOrgIdAndGradeId(xxId, prevGradeId);
		if (gjtRulesClass != null && gjtRulesClass.size() > 0 && "0".equals(isEnabled)) {
			// 当前学期是否存在分班规则
			List<GjtRulesClass> currentRulesClassList = gjtRulesClassService.findByOrgIdAndGradeId(xxId, gjtGrade.getGradeId());
			if (currentRulesClassList == null || currentRulesClassList.size() == 0) {
				for (int i = 0; i < gjtRulesClass.size(); i++) {
					GjtRulesClass classInfo = new GjtRulesClass();
					GjtRulesClass ruleClass = gjtRulesClass.get(i);
					classInfo.setXxid(UUIDUtils.random());
					classInfo.setOrgId(ruleClass.getOrgId());
					classInfo.setGradeId(gjtGrade.getGradeId());
					classInfo.setPointClassType(ruleClass.getPointClassType());
					classInfo.setPointClassNum(ruleClass.getPointClassNum());
					classInfo.setXxzxId(ruleClass.getXxzxId());
					classInfo.setCreatedBy(user.getId());
					gjtRulesClassService.update(classInfo);
				}
			}
		} else {
			log.error("当前学期的上个学期未存在分班规则！上个学期"+prevGradeId + ",当前学期" + gjtGrade.getGradeId());
		}
		OpiTerm opiTerm = new OpiTerm();
		opiTerm.setCREATED_BY("");
		opiTerm.setTERM_CODE(String.valueOf(gjtGrade.getGradeCode()));
		opiTerm.setTERM_END_DT(DateUtils.getTimeYMD(gjtGrade.getCourseEndDate()));
		opiTerm.setTERM_START_DT(DateUtils.getTimeYMD(gjtGrade.getCourseStartDate()));
		opiTerm.setTERM_NAME(gjtGrade.getGradeName());
		opiTerm.setTERM_ID(gjtGrade.getGradeId());
		OpiTermCourseData data = new OpiTermCourseData(gjtGrade.getGjtSchoolInfo().getAppid(), opiTerm, null);
		RSimpleData rSimpleData = pCourseServer.synchroTermCourse(data);
		if (rSimpleData == null || rSimpleData.getStatus() != 1) {
			log.error("同步课程平台失败");
			// throw new ServiceException("同步课程平台失败");
		}
		return true;
	}

	/**
	 * 查询单个年级信息
	 */
	@Override
	public GjtGrade queryById(String id) {
		return gjtGradeDao.findOne(id);
	}

	/**
	 * 查询所有年级
	 */
	@Override
	public List<GjtGrade> queryAll() {
		return gjtGradeDao.findAll();
	}

	@Override
	public String getCurrentGradeId(String xxId) {
		String gradeId = gjtGradeDao.getCurrentGradeId(xxId);
		if (StringUtils.isEmpty(gradeId)) {
			gradeId = gjtGradeDao.getPrevGradeId(xxId);
		}
		return gradeId;
	}

	@Override
	public void delete(String id) {
		gjtGradeDao.delete(id);
	}

	@Override
	public Boolean updateIsEnabled(String id, String str) {
		int i = gjtGradeDao.updateIsEnabled(id, str);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean deleteById(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				gjtGradeDao.deleteById(id, "Y");
			}
		}
		return true;
	}

	/**
	 * 查询帐号是否存在
	 * 
	 * @param gradeCode
	 * @return
	 */
	@Override
	public Boolean queryByGradeCode(String gradeCode) {
		GjtGrade gjtGrade = gjtGradeDao.findByGradeCode(gradeCode);
		if (gjtGrade != null) {
			return true;
		}
		return false;
	}

	@Override
	public GjtGrade findByGradeNameAndGjtSchoolInfoId(String gradeName, String xxId) {
		return gjtGradeDao.findByGradeNameAndGjtSchoolInfoIdAndIsDeleted(gradeName, xxId, Constants.BOOLEAN_NO);
	}

	@Override
	public GjtGrade queryByGjtGradeCode(String gradeCode) {
		return gjtGradeDao.findByGradeCodeAndIsDeleted(gradeCode, "N");
	}

	@Override
	public List<GjtEnrollBatch> findByGjtGrade(GjtGrade gjtGrade) {
		return gjtEnrollBatchDao.findByGjtGrade(gjtGrade);

	}

	@Override
	public Page<GjtGrade> queryAll(String schoolId, Map<String, Object> searchParams, PageRequest pageRequest) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Map<String, String> map = commonMapService.getOrgMapByOrgId(schoolId);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("isEnabled", new SearchFilter("isEnabled", Operator.EQ, "1"));
		filters.put("gjtSchoolInfo.id", new SearchFilter("gjtSchoolInfo.id", Operator.IN, map.keySet()));
		Specification<GjtGrade> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtGrade.class);
		return gjtGradeDao.findAll(spec, pageRequest);
	}

	@Override
	public Map<String, String> findGradeMapByOrgId(String orgId) {
		StringBuilder sb = new StringBuilder("select g.grade_id  ID,g.grade_name NAME from gjt_grade g");
		sb.append(" where g.is_deleted = 'N'");
		sb.append(" and exists(select 1 from gjt_org o where o.id=g.xx_id and (o.id='" + orgId + "' or o.perent_id='"
				+ orgId + "')) order by start_date");
		return commonMapService.getMap(sb.toString());
	}

	@Override
	public Boolean updateGjtGrade(GjtGrade item) {
		GjtGrade gjtGrade = gjtGradeDao.findOne(item.getGradeId());
		gjtGrade.setEnrollStartDate(item.getEnrollStartDate());
		gjtGrade.setEnrollEndDate(item.getEnrollEndDate());
		gjtGrade.setEnrollResponsible(item.getEnrollResponsible());
		gjtGrade.setEnrollResponsiblePer(item.getEnrollResponsiblePer());

		gjtGrade.setSchoolrollStartDate(item.getSchoolrollStartDate());
		gjtGrade.setSchoolrollEndDate(item.getSchoolrollEndDate());
		gjtGrade.setSchoolrollResponsible(item.getSchoolrollResponsible());
		gjtGrade.setSchoolrollResponsiblePer(item.getSchoolrollResponsiblePer());
		gjtGrade.setEducationalStartDate(item.getEducationalStartDate());
		gjtGrade.setEducationalEndDate(item.getEducationalEndDate());
		gjtGrade.setEducationalResponsible(item.getEducationalResponsible());
		gjtGrade.setEducationalResponsiblePer(item.getEducationalResponsiblePer());
		gjtGrade.setTeachingStartDate(item.getTeachingStartDate());
		gjtGrade.setTeachingEndDate(item.getTeachingEndDate());
		gjtGrade.setTeachingResponsible(item.getTeachingResponsible());
		gjtGrade.setTeachingResponsiblePer(item.getTeachingResponsiblePer());
		gjtGrade.setExamStartDate(item.getExamStartDate());
		gjtGrade.setExamEndDate(item.getExamEndDate());
		gjtGrade.setExamResponsible(item.getExamResponsible());
		gjtGrade.setExamResponsiblePer(item.getExamResponsiblePer());
		gjtGrade.setGraduationStartDate(item.getGraduationStartDate());
		gjtGrade.setGraduationEndDate(item.getGraduationEndDate());
		gjtGrade.setGraduationResponsible(item.getGraduationResponsible());
		gjtGrade.setGraduationResponsiblePer(item.getGraduationResponsiblePer());
		gjtGrade.setUpdatedDt(DateUtils.getNowTime());
		gjtGrade.setUpdatedBy(item.getUpdatedBy());
		gjtGrade.setPayStartDate(item.getPayStartDate());
		gjtGrade.setPayEndDate(item.getPayEndDate());
		gjtGrade.setOldStudentEnterDate(item.getOldStudentEnterDate());
		gjtGrade.setNewStudentEnterDate(item.getNewStudentEnterDate());
		return this.updateEntity(gjtGrade);
	}

	public GjtGrade findByPYCCAndSpecialtyIdAndGradeId(String pycc, String specialtyId, String gradeId) {
		StringBuilder sql = new StringBuilder("select grade.*");
		sql.append(" from gjt_grade grade, ");
		sql.append(" (select actual_grade_id, kkxq ");
		sql.append(" 	from VIEW_TEACH_PLAN ");
		sql.append(" 	where is_deleted = 'N' ");
		sql.append(" 	 and pycc = ? ");
		sql.append(" 	 and KKZY = ? ");
		sql.append(" 	 and grade_id = ?) teach, ");
		sql.append(" (select max(kkxq) kkxq ");
		sql.append(" 	from VIEW_TEACH_PLAN ");
		sql.append(" 	where pycc = ? ");
		sql.append(" 	 and KKZY = ? ");
		sql.append(" 	 and grade_id = ?) t ");
		sql.append(" where 1 = 1and grade.is_deleted = 'N' ");
		sql.append(" and grade.grade_id = teach.actual_grade_id ");
		sql.append(" and teach.kkxq = t.kkxq ");
		Query query = em.createNativeQuery(sql.toString(), GjtGrade.class);
		query.setParameter(1, pycc);
		query.setParameter(2, specialtyId);
		query.setParameter(3, gradeId);
		query.setParameter(4, pycc);
		query.setParameter(5, specialtyId);
		query.setParameter(6, gradeId);
		List<GjtGrade> list = query.getResultList();
		if (Collections3.isNotEmpty(list)) {
			GjtGrade grade = list.get(0);
			return grade;
		} else {
			return null;
		}

	}

	@Override
	public Map<String, Object> countGradeByStudent(String pycc, String specialtyId, String gradeId) {
		StringBuilder sql = new StringBuilder(" select count(g.grade_id) countGrade, ");
		sql.append(" count(case when sysdate > g.start_date then 1 else null end ) currentGrade ");
		sql.append(" from gjt_grade g, ");
		sql.append(" (select actual_grade_id ");
		sql.append("	from VIEW_TEACH_PLAN ");
		sql.append(" 	where is_deleted = 'N' ");
		sql.append(" 	 and pycc = ? ");
		sql.append(" 	 and KKZY = ? ");
		sql.append(" 	 and grade_id = ?) t ");
		sql.append(" where g.is_deleted = 'N' ");
		sql.append(" and g.grade_id = t.actual_grade_id ");
		Query query = em.createNativeQuery(sql.toString());
		query.setParameter(1, pycc);
		query.setParameter(2, specialtyId);
		query.setParameter(3, gradeId);
		List list = query.getResultList();
		Map<String, Object> map = Maps.newHashMap();
		if (Collections3.isNotEmpty(list)) {
			Object[] objects = (Object[]) list.get(0);
			int countGrade = ((BigDecimal) objects[0]).intValue();
			int currentGrade = ((BigDecimal) objects[1]).intValue();
			map.put("countGrade", countGrade);
			map.put("currentGrade", currentGrade);
		}
		return map;
	}

	@Override
	public List<GjtGrade> findGradeBySize(GjtGrade grade, int size) {
		StringBuilder sb = new StringBuilder();
		sb.append(
				"from GjtGrade g where g.isDeleted='N' and g.startDate >= ?1 and g.gjtSchoolInfo.id=?2  order by g.startDate");
		Query query = em.createQuery(sb.toString());
		query.setParameter(1, grade.getStartDate()).setParameter(2, grade.getGjtSchoolInfo().getId())
				.setMaxResults(size);
		return query.getResultList();
	}

	@Override
	public GjtGrade findCurrentGrade(String xxId) {
		List<GjtGrade> currentGrade = gjtGradeDao.findCurrentGrade(xxId);
		if (currentGrade != null && currentGrade.size() > 0) {
			return currentGrade.get(0);
		}
		currentGrade = gjtGradeDao.findPrevGrade(xxId);
		if (currentGrade != null && currentGrade.size() > 0) {
			return currentGrade.get(0);
		}
		return null;
	}

	@Override
	public List<String> findGradeIdByStartDate(String orgId, Date startTime) {
		String sql = "select grade_id from gjt_grade where start_date >= ?1 and xx_id=?2 and is_deleted = 'N' order by start_date asc";
		List<String> ids = em.createNativeQuery(sql).setParameter(1, startTime).setParameter(2, orgId).getResultList();
		return ids;
	}

	@Override
	public List<GjtGrade> findGradeIdByOrgId(String orgId) {
		return this.gjtGradeDao.findGradeIdByOrgId(orgId);

	}

	@Override
	public List<GjtGrade> findGradeByYearId(String yearId) {
		return gjtGradeDao.findGradeByYearIdAndIsEnabled(yearId, "0");
	}

	@Override
	@Transactional
	public boolean batchCreateGrade() {
		String sql = "select o.* from gjt_org o where o.is_deleted='N' and exists( select 1 from gjt_year where org_id=o.id)";
		List<GjtOrg> gjtOrgs = em.createNativeQuery(sql, GjtOrg.class).getResultList();
		for (GjtOrg gjtOrg : gjtOrgs) {
			// 查询最大年级
			BigDecimal maxYear = (BigDecimal) em
					.createNativeQuery("select max(start_year) from gjt_year where org_id=:orgId")
					.setParameter("orgId", gjtOrg.getId()).getSingleResult();
			String year = String.valueOf(maxYear.intValue() + 1);
			createYearAndGrade(gjtOrg, year);
		}
		return true;
	}

	@Override
	@Transactional
	public void initYearAndGrade(String orgId) {
		Calendar calendar = Calendar.getInstance();
		// 从上上年开始，建10年
		int currentYear = calendar.get(Calendar.YEAR) - 2;
		try {
			GjtOrg gjtOrg = gjtOrgDao.findOne(orgId);
			for (int i = currentYear; i < currentYear + 10; i++) {
				String year = String.valueOf(i);
				createYearAndGrade(gjtOrg, year);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
	}

	private void createYearAndGrade(GjtOrg gjtOrg, String year) {
		Date now = new Date();
		String orgCode = StringUtils.isBlank(gjtOrg.getCode()) ? "" : "_" + gjtOrg.getCode();
		GjtYear gjtYear = new GjtYear();
		gjtYear.setGradeId(UUIDUtils.random());
		gjtYear.setCode("NJ" + year + orgCode);
		gjtYear.setName(year);
		gjtYear.setCreatedBy("system");
		gjtYear.setCreatedDt(now);
		gjtYear.setStartYear(Integer.parseInt(year));
		gjtYear.setVersion(new BigDecimal(3));
		gjtYear.setOrgId(gjtOrg.getId());
		gjtYearDao.save(gjtYear);

		GjtGrade grade = new GjtGrade();
		grade.setGradeId(UUIDUtils.random());
		grade.setBelongYear(year);
		grade.setCreatedBy("system");
		grade.setCreatedDt(now);
		grade.setGradeCode("XQ" + year + "03" + orgCode);
		grade.setGradeName(year + "春");
		grade.setStartDate(DateUtil.strToYYMMDDDate(year + "-03-01"));
		grade.setEndDate(DateUtil.strToYYMMDDDate(year + "-09-01"));
		grade.setIsEnabled("0");
		grade.setIsDeleted("N");
		grade.setVersion(new BigDecimal(3));
		grade.setGjtYear(gjtYear);
		grade.setGjtSchoolInfo(new GjtSchoolInfo(gjtOrg.getId()));
		gjtGradeDao.save(grade);

		grade = new GjtGrade();
		grade.setGradeId(UUIDUtils.random());
		grade.setBelongYear(year);
		grade.setCreatedBy("system");
		grade.setCreatedDt(now);
		grade.setGradeCode("XQ" + year + "09" + orgCode);
		grade.setGradeName(year + "秋");
		grade.setStartDate(DateUtil.strToYYMMDDDate(year + "-09-01"));
		grade.setEndDate(DateUtil.strToYYMMDDDate((NumberUtils.toInt(year) + 1) + "-03-01"));
		grade.setIsEnabled("0");
		grade.setIsDeleted("N");
		grade.setVersion(new BigDecimal(3));
		grade.setGjtYear(gjtYear);
		grade.setGjtSchoolInfo(new GjtSchoolInfo(gjtOrg.getId()));
		gjtGradeDao.save(grade);
	}

	@Override
	public List<GjtGrade> findGradesBefore(String xxId, String gradeId) {
		return gjtGradeDao.findGradesBefore(xxId, gradeId);
	}

	@Override
	public GjtGrade findPrevGradeBefore(String xxId, String gradeId) {
		List<GjtGrade> gradeList = gjtGradeDao.findGradesBefore(xxId, gradeId);
		if (gradeList != null && gradeList.size() > 0) {
			return gradeList.get(0);
		}

		return null;
	}

	@Override
	public List<GjtGrade> findGradeIdByOrgIdNew(String xxId) {
		return this.gjtGradeDao.findGradeIdByOrgIdNew(xxId);
	}

	@Override
	public List<GjtGrade> getGradeList(Date currentGradeDate, String xxId) {
		return gjtGradeDao.getGradeList(currentGradeDate,xxId);
	}

	@Override
	public List<Map<String, Object>> findByGradeSpecialtyId(String gradeSpecialtyId, String studentId) {
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GG.GRADE_ID \"gradeId\", GG.TEXTBOOK_STATUS \"status\",");
		sql.append(
				"  (select max(gtd.distribute_id) from gjt_textbook_distribute gtd where gtd.grade_id = gg.grade_id and gtd.is_deleted = 'N' and gtd.status>0 and gtd.student_id=:studentId) \"distributeId\"");
		sql.append("  from gjt_grade gg, gjt_teach_plan gtp");
		sql.append("  where gg.grade_id = gtp.actual_grade_id");
		sql.append("  and gtp.is_deleted = 'N'");
		sql.append("  and gtp.grade_specialty_id = :gradeSpecialtyId");
		sql.append("  group by gg.grade_id, gg.textbook_status, gtp.kkxq");
		sql.append("  order by gtp.kkxq");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("gradeSpecialtyId", gradeSpecialtyId);
		params.put("studentId", studentId);
		return commonDao.queryForMapList(sql.toString(), params);
	}

	@Override
	public long getStudentCurrentTerm(String currentGradeId, String studentId) {
		StringBuffer sql = new StringBuffer();
		sql.append("  select distinct  a.KKXQ ");
		sql.append("  from GJT_STUDENT_INFO t ");
		sql.append("  left join  GJT_TEACH_PLAN a  on  t.GRADE_SPECIALTY_ID = a.GRADE_SPECIALTY_ID");
		sql.append("  where t.STUDENT_ID= :studentId");
		sql.append("  and a.ACTUAL_GRADE_ID = :gradeId");
		sql.append("  and t.IS_DELETED = 'N' ");
		sql.append("  and a.IS_DELETED = 'N' ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("studentId", studentId);
		params.put("gradeId", currentGradeId);
		BigDecimal num = (BigDecimal) commonDao.queryObjectNative(sql.toString(), params);
		//TODO
		if (Strings.isNullOrEmpty(ObjectUtils.toString(num))) {
			return 5;
		}
		return Integer.parseInt(ObjectUtils.toString(num));
	}

	@Override
	public long getStudentCurrentCredit(String studentId) {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT NVL(SUM(NVL(GRR.GET_CREDITS, 0)),0) ");
		sql.append("  FROM GJT_REC_RESULT GRR ");
		sql.append("  WHERE GRR.IS_DELETED = 'N' ");
		sql.append("  AND GRR.STUDENT_ID= :studentId");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("studentId", studentId);
		BigDecimal num = (BigDecimal) commonDao.queryObjectNative(sql.toString(), params);
		return Integer.parseInt(ObjectUtils.toString(num));
	}

	@Override
	public long getStudentCreditTotal(String studentId) {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT SUM(NVL(p.XF, 0)) ");
		sql.append("  FROM gjt_teach_plan p, gjt_student_info s ");
		sql.append("  WHERE p.IS_DELETED = 'N' ");
		sql.append("  AND s.IS_DELETED = 'N' ");
		sql.append("  AND s.GRADE_SPECIALTY_ID =p.GRADE_SPECIALTY_ID");
		sql.append("  AND s.STUDENT_ID= :studentId");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("studentId", studentId);
		BigDecimal num = (BigDecimal) commonDao.queryObjectNative(sql.toString(), params);
		return Integer.parseInt(ObjectUtils.toString(num));
	}

	/**
	 * 毕业状态
	 * 0：正常未毕业；
	 * 1：满足毕业条件（专科）；
	 * 2：可毕业无学位（本科）；
	 * 3：可毕业有学位（本科：跳转学位承诺书）；
	 * 4：申请已提交；
	 * 5：总部审核不通过（待学员确认）；
	 * 6：不满足条件未能毕业（等待学位，延迟毕业）；
	 * 7：不满足条件未能毕业（总部审核不通过，延迟毕业）；
	 * 8：已毕业（总部审核通过）；
	 * @param student
	 * @return
	 */
	@Override
	public int getGraduationStateByStudent(GjtStudentInfo student, String planId) {
		GjtGraduationApplyCertif item = gjtGraduationApplyCertifDao.queryByStudentIdAndPlanId(student.getStudentId(), planId);
		if (item == null || item.getAuditState() == 0 || item.getAuditState() == 2) {
			return 0;
		}

		//6：不满足条件未能毕业（等待学位，延迟毕业）；
		if (item.getAuditState() == 1 && item.getGraduationState() == 1){
			return 6;
		}

		if (item.getAuditState() == 1) {
			//判断该学生是否是本科(对应字典表里面的code)
			if ( "2".equals(student.getPycc())){
				//查找学位申请是否总部允许申请
				GjtApplyDegreeCertif certif = gjtApplyDegreeCertifService.queryApplyDegreeCertifByStudentIdAndPlanId(student.getStudentId(),planId);
				if( certif !=null && (certif.getAuditState()==1 ||certif.getAuditState() ==11) ){
					return 3;
				}
				return 2;
			}
			return 1;
		}
		//4：申请已提交；
		if (item.getAuditState() == 6) {
			return 4;
		}
		if (item.getAuditState() == 11) {
			return 8;
		}

		if (item.getAuditState() == 12  ) {
			//7：不满足条件未能毕业（总部审核不通过，延迟毕业）；
			if (item.getGraduationState() == 1 ){
				return 7;
			}
			//5: 总部审核不通过（待学员确认）；
			return 5;
		}

		return 0;
	}

	@Override
	public List<Map<String, Object>> queryGraduationStudent(int state, String planId) {

		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT  s.student_id as ID, s.sfzh as SFZH, s.xm as NAME, s.avatar as AVATAR  ");
		sql.append("  FROM gjt_student_info s, gjt_graduation_apply_certif c ");
		sql.append("  WHERE s.IS_DELETED = 'N' ");
		sql.append("  AND c.IS_DELETED = 'N' ");
		sql.append("  AND s.STUDENT_ID =c.STUDENT_ID");
		sql.append("  AND c.GRADUATION_PLAN_ID =:planId");
		sql.append("  AND c.AUDIT_STATE = :state");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("state", state);
		params.put("planId", planId);
		return commonDao.queryForMapList(sql.toString(), params);
	}
}
