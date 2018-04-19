package com.gzedu.xlims.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.exception.ServiceException;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.GjtCourseCheckDao;
import com.gzedu.xlims.dao.GjtCourseDao;
import com.gzedu.xlims.dao.api.ApiDao;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.share.GjtShareDao;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtCourseCheck;
import com.gzedu.xlims.pojo.GjtCourseOwnership;
import com.gzedu.xlims.pojo.opi.OpiCourse;
import com.gzedu.xlims.pojo.opi.OpiCourseData;
import com.gzedu.xlims.pojo.opi.RSimpleData;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.pcourse.PCourseServer;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

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
public class GjtCourseServiceImpl extends BaseServiceImpl<GjtCourse> implements GjtCourseService {

	private static Logger log = LoggerFactory.getLogger(GjtCourseServiceImpl.class);

	@Autowired
	private GjtCourseDao gjtCourseDao;

	@Autowired
	GjtCourseCheckDao gjtCourseCheckDao;

	@Autowired
	ApiDao apiDao;

	@Autowired
	private GjtSchoolInfoService schoolInfoService;

	@Autowired
	private GjtShareDao gjtShareDao;

	@Autowired
	private PCourseServer pCourseServer;

	@PersistenceContext(unitName = "entityManagerFactory")
	protected EntityManager em;

	@Autowired
	CommonDao commonDao;

	@Override
	protected BaseDao<GjtCourse, String> getBaseDao() {
		return this.gjtCourseDao;
	}

	@Override
	public Page<GjtCourse> queryAll(final String schoolId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("gjtOrg.id", new SearchFilter("gjtOrg.id", Operator.EQ, schoolId));

		// Map<String, SearchFilter> filters2 =
		// SearchFilter.parse(searchParams);
		// filters2.put("shareGjtSchoolInfos.id", new
		// SearchFilter("shareGjtSchoolInfos.id", Operator.EQ, schoolId));

		Specification<GjtCourse> spec = DynamicSpecifications.bySearchFilter(filters.values(), // filters2.values(),
				GjtCourse.class);

		return gjtCourseDao.findAll(spec, pageRequst);
	}

	@Override
	public Page<GjtCourse> queryAllAndShare(String schoolIds, Map<String, Object> searchParams, PageRequest pageRequst,
			List<String> coursIds) {
		Criteria<GjtCourse> spec = new Criteria<GjtCourse>();
		spec.add(Restrictions.eq("isDeleted", "N", true));
		if (coursIds.size() != 0) {
			spec.add(Restrictions.or(Restrictions.in("courseId", coursIds, true),
					Restrictions.eq("gjtOrg.id", schoolIds, true)));
		} else {
			spec.add(Restrictions.eq("gjtOrg.id", schoolIds, true));
		}

		if (searchParams.get("courseTypes") != null) {
			@SuppressWarnings("unchecked")
			List<String> courseTypes = (List<String>) searchParams.get("courseTypes");
			spec.add(Restrictions.in("courseType", courseTypes, true));
			searchParams.remove("courseTypes");
		}

		if (searchParams.get("notIncoursIds") != null) {
			@SuppressWarnings("unchecked")
			List<String> notIncoursIds = (List<String>) searchParams.get("notIncoursIds");
			spec.add(Restrictions.notIn("courseId", notIncoursIds, true));
			searchParams.remove("notIncoursIds");
		}

		spec.addAll(Restrictions.parse(searchParams));
		return gjtCourseDao.findAll(spec, pageRequst);
		/*
		 * Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		 * filters.put("gjtOrg.id", new SearchFilter("gjtOrg.id", Operator.EQ,
		 * schoolIds)); if (coursIds.size() != 0) { Map<String, SearchFilter>
		 * filters2 = SearchFilter.parse(searchParams); filters.put("courseId",
		 * new SearchFilter("courseId", Operator.IN, coursIds));
		 * 
		 * Specification<GjtCourse> spec =
		 * DynamicSpecifications.bySearchFilter(filters.values(),
		 * filters2.values(), GjtCourse.class); return
		 * gjtCourseDao.findAll(spec, pageRequst); } else {
		 * Specification<GjtCourse> spec =
		 * DynamicSpecifications.bySearchFilter(filters.values(),
		 * GjtCourse.class); return gjtCourseDao.findAll(spec, pageRequst); }
		 */

	}

	@Override
	public Page<GjtCourse> queryPage(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		// filters.put("gjtOrg.id", new SearchFilter("gjtOrg.id", Operator.EQ,
		// schoolId));

		// Map<String, SearchFilter> filters2 =
		// SearchFilter.parse(searchParams);
		// filters2.put("shareGjtSchoolInfos.id", new
		// SearchFilter("shareGjtSchoolInfos.id", Operator.EQ, schoolId));

		Specification<GjtCourse> spec = DynamicSpecifications.bySearchFilter(filters.values(), // filters2.values(),
				GjtCourse.class);

		return gjtCourseDao.findAll(spec, pageRequst);
	}

	@Override
	public void syncCourse(String appId, Iterable<String> ids) {
		Iterable<GjtCourse> findAll = gjtCourseDao.findAll(ids);
		for (GjtCourse course : findAll) {
			syncCourse(appId, course);
		}
	}

	@Override
	public void syncCourse(String appId, GjtCourse course) throws RuntimeException {
		/*
		 * if (Constants.BOOLEAN_YES.equals(course.getSyncStatus())) { return; }
		 */
		OpiCourse obj = new OpiCourse();
		obj.setCOURSE_ID(course.getCourseId());
		obj.setCOURSE_NAME(course.getKcmc());
		obj.setCOURSE_STATUS("active");
		obj.setCREATED_BY(course.getCreatedBy());
		obj.setPUBLISH_DT(DateUtils.getStringToDate(course.getCreatedDt()));

		if (EmptyUtils.isEmpty(ObjectUtils.toString(course.getKch()))) {
			obj.setCOURSE_CODE("001");
		} else {
			obj.setCOURSE_CODE(course.getKch());
		}
		obj.setCOURSE_DES(course.getKcjj());
		OpiCourseData data = new OpiCourseData(appId, obj);
		RSimpleData synchroCourse = pCourseServer.synchroCourse(data);
		if (synchroCourse == null) {
			throw new ServiceException("同步失败");
		} else {
			if (synchroCourse.getStatus() == 1) {
				gjtCourseDao.updateSyncStatus(Constants.BOOLEAN_YES, course.getCourseId());
			} else {
				log.error("function syncCourse error ========= " + synchroCourse.getMsg());
				throw new ServiceException(synchroCourse.getMsg());
			}
		}
	}

	@Override
	public GjtCourse queryBy(String id) {
		return gjtCourseDao.findOne(id);
	}

	@Override
	public void delete(Iterable<String> ids) {
		for (String id : ids) {
			gjtCourseDao.delete(id);
		}
	}

	@Override
	public boolean insert(GjtCourse entity) {
		entity.setCourseId(UUIDUtils.random());
		entity.setCreatedDt(new Date());
		entity.setIsDeleted("N");
		entity.setVersion(new BigDecimal(3));
		entity.setSyncStatus("N");
		// entity.setGjtSchoolInfo(new
		// GjtSchoolInfo(entity.getGjtOrg().getId()));
		gjtCourseDao.save(entity);

		// 新增课程同步到学习平台
		String appid = schoolInfoService.queryById(entity.getGjtOrg().getId()).getAppid();
		syncCourse(appid, entity);
		return entity != null;
	}

	@Override
	public void update(GjtCourse entity) {
		entity.setUpdatedDt(new Date());
		gjtCourseDao.save(entity);

		// 修改课程同步到学习平台    
		String appid = schoolInfoService.queryById(entity.getGjtOrg().getId()).getAppid();
		syncCourse(appid, entity);
	}

	@Override
	public GjtCourse queryByKch(String kch, String isDeleted) {
		return gjtCourseDao.findByKchAndIsDeleted(kch, isDeleted);
	}

	@Override
	public List<GjtCourse> findByXxid(String xxid) {
		return gjtCourseDao.findByXxId(xxid);
	}

	@Override
	public long queryIsEnabledNum(String isEnabled) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("isEnabled", new SearchFilter("isEnabled", Operator.EQ, isEnabled));
		Specification<GjtCourse> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtCourse.class);
		return gjtCourseDao.count(spec);
	}

	@Override
	public List<GjtCourse> findByXxidAndIsDeleted(String xxid, String isDeleted) {
		return gjtCourseDao.findByXxIdAndIsDeleted(xxid, isDeleted);
	}

	@Override
	public List<GjtCourse> findAll(Iterable<String> ids) {
		return (List<GjtCourse>) gjtCourseDao.findAll(ids);
	}

	@Override
	public List<GjtCourse> findByKchAndXxId(String kch, String xxid) {
		List<GjtCourseOwnership> gjtCourseOwnershipList = gjtShareDao.findByOrgId(xxid);
		List<String> courseIds = new ArrayList<String>();
		if (gjtCourseOwnershipList.size() != 0) {
			for (GjtCourseOwnership gjtCourseOwnership : gjtCourseOwnershipList) {
				if (gjtCourseOwnership != null) {
					courseIds.add(gjtCourseOwnership.getCourseId());
				}
			}
		}
		return gjtCourseDao.findByKchAndXxIdAndIsDeleted(kch, xxid, courseIds);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<GjtCourse> queryByGradeIdAndSpecialtyId(String gradeId, String specialtyId) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM gjt_course c");
		sb.append(
				" WHERE EXISTS ( SELECT 1 FROM gjt_specialty_plan sp WHERE c.course_id = sp.course_id AND sp.specialty_id = :specialtyId )");
		sb.append(
				" AND NOT EXISTS ( SELECT 1 FROM VIEW_TEACH_PLAN tp WHERE tp.course_id = c.course_id AND tp.grade_id = :gradeId AND tp.kkzy = :specialtyId )");

		List<GjtCourse> list = em.createNativeQuery(sb.toString(), GjtCourse.class).setParameter("gradeId", gradeId)
				.setParameter("specialtyId", specialtyId).getResultList();
		return list;
	}

	/**
	 * 查询学习平台的课程列表数据   
	 * 
	 * 
	 */
	public Page getPcourseList(Map formMap, PageRequest pageRequst) {
		return apiDao.getPcourseList(formMap, pageRequst);
	}

	@Override
	public List<Map<String, Object>> queryCourseStage(Map<String, Object> formMap) {
		log.info("查询课程验收中的阶段时的参数：{}", formMap);

		StringBuilder sql = new StringBuilder();
		
		sql.append("  SELECT LTP.PERIOD_ID, LTP.PERIOD_NAME, LTC.COURSE_ID");
		sql.append("  FROM LCMS_TERMCOURSE_PERIOD LTP,");
		sql.append("  LCMS_TERM_COURSEINFO   LTC,");
		sql.append("  LCMS_TERMCOURSE_CLASS  LTS");
		sql.append("  WHERE LTP.ISDELETED = 'N'");
		sql.append("  AND LTC.ISDELETED = 'N'");
		sql.append("  AND LTS.ISDELETED = 'N'");
		sql.append("  AND LTS.TERMCOURSE_ID = LTC.TERMCOURSE_ID");
		sql.append("  AND LTP.TERMCOURSE_ID = LTC.TERMCOURSE_ID");
		sql.append("  AND LTP.CLASS_ID = LTS.TERMCOURSE_CLASS_ID");
		sql.append("  AND LTC.TERM_ID = '614b0592ac1082a750505050c86c07e1'"); // 固定死的常量（制作期）
		sql.append("  AND LTC.COURSE_ID = :courseId");
		sql.append("  AND LTS.CLASS_NAME = '课程制作班级'");                     // 固定死的模板（制作班级）
		sql.append("  order by LTP.ORDER_NO");

		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", formMap.get("courseId"));
		return commonDao.queryForMapList(sql.toString(), params);
	}

	@Override
	public List<Map<String, Object>> queryCourseStageArea(Map<String, Object> formMap) {
		log.info("查询课程验收中的区块时的参数：{}", formMap);
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT LTR.AREA_NAME,LTR.AREA_ID");
		sql.append("  FROM LCMS_PERIOD_AREA LPA, LCMS_TERMCOURSE_AREA LTR");
		sql.append("  WHERE LTR.AREA_ID = LPA.AREA_ID");
		sql.append("  AND LPA.ISDELETED = 'N'");
		sql.append("  AND LTR.ISDELETED = 'N'");
		sql.append("  AND LPA.PERIOD_ID = :periodId");
		sql.append("  ORDER BY LTR.ORDER_NO");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("periodId", formMap.get("periodId"));
		return commonDao.queryForMapList(sql.toString(), params);
	}
    
	@Override
	public List<Map<String, Object>> queryCourseSection(Map<String, Object> formMap) {
		log.info("查询课程验收中的章节时的参数：{}", formMap);
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT LTT.TASK_ID,LTT.Task_Name, ltt.do_finish, ltt.is_check,");
		sql.append("  to_char(ltt.created_dt,'yyyy-mm-dd') created_dt ");
		sql.append("  FROM LCMS_TERMCOURSE_TASK LTT, LCMS_TASK_AREA LTK");
		sql.append("  where LTT.TASK_ID = ltk.task_id");
		sql.append("  AND LTT.ISDELETED = 'N'");
		sql.append("  AND LTK.ISDELETED = 'N'");
		sql.append("  and LTK.AREA_ID = :areaId");
		sql.append("  ORDER BY LTT.ORDER_NO");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("areaId", formMap.get("areaId"));
		return commonDao.queryForMapList(sql.toString(), params);
	}

	@Override
	public boolean sumbitCheck(GjtCourseCheck item) {
		gjtCourseCheckDao.save(item);
		return false;
	}

	@Override
	@Transactional
	public void updateCheck(String[] taskId, String status) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("update lcms_termcourse_task set is_check=:status  where task_id in(:taskId) ");

		params.put("taskId", Arrays.asList(taskId));
		params.put("status", status);
		commonDao.updateForMapNative(sql.toString(), params);
	}

	@Override
	public long checkCourseCount(String courseId, String doFish) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("  select ltc.Course_Id");
		sql.append("  from LCMS_TERMCOURSE_PERIOD LTP,");
		sql.append("  LCMS_TERM_COURSEINFO   LTC,");
		sql.append("  LCMS_PERIOD_AREA       LPA,");
		sql.append("  LCMS_TERMCOURSE_AREA   LTR,");
		sql.append("  LCMS_TERMCOURSE_TASK   LTT,");
		sql.append("  LCMS_TASK_AREA         LTK,");
		sql.append("  LCMS_TERMCOURSE_CLASS  LTS");
		sql.append("  WHERE LTP.ISDELETED = 'N'");
		sql.append("  AND LTR.ISDELETED = 'N'");
		sql.append("  AND LPA.ISDELETED = 'N'");
		sql.append("  AND LTT.ISDELETED = 'N'");
		sql.append("  AND LTK.ISDELETED = 'N'");
		sql.append("  AND LTS.ISDELETED = 'N'");
		sql.append("  AND LTC.COURSE_ID = :courseId");
		sql.append("  AND LTP.TERMCOURSE_ID = LTC.TERMCOURSE_ID");
		sql.append("  AND LPA.PERIOD_ID = LTP.PERIOD_ID");
		sql.append("  AND LTR.AREA_ID = LPA.AREA_ID");
		sql.append("  AND LTK.AREA_ID = LTR.AREA_ID");
		sql.append("  AND LTT.TASK_ID = LTK.TASK_ID");
		sql.append("  AND LTT.CLASS_ID = LTS.TERMCOURSE_CLASS_ID");
		sql.append("  AND LTC.TERMCOURSE_ID = LTS.TERMCOURSE_ID");
		sql.append("  AND LTC.TERM_ID = '614b0592ac1082a750505050c86c07e1'");
		sql.append("  AND LTS.CLASS_NAME = '课程制作班级'");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", courseId);

		if (StringUtils.isNotBlank(doFish)) {
			sql.append("  AND ltt.do_finish=:doFish ");
			params.put("doFish", doFish);
		}

		return commonDao.queryForCountNative(sql.toString(), params);
	}

	@Override
	public List<Map<String, Object>> queryCourseIsEnabled() {
		StringBuilder sql = new StringBuilder();
		sql.append("  select gc.course_id from gjt_course gc where gc.is_enabled='5' ");
		return commonDao.queryForMapList(sql.toString(), null);
	}

	@Override
	public void updateIsEnabled(String courseId, String status) {
		gjtCourseDao.updateIsEnabled(courseId, status);
	}

	@Override
	public String queryExamFeeByKch(String kch) {
		return gjtCourseDao.findExamFeeByKch(kch);
	}

	@Override
	public void updateIsEnabled(String courseId, String status, String userName) {
		gjtCourseDao.updateIsEnabled(courseId, status, userName);
	}

	@Override
	public List<GjtTextbook> queryCourseTextbook(String courseId) {
		StringBuilder sql = new StringBuilder();
		sql.append("  select gt.*");
		sql.append("  from gjt_course_textbook gct, gjt_textbook gt");
		sql.append("  where gct.textbook_id = gt.textbook_id");
		sql.append("  and gt.is_deleted = 'N'");
		sql.append("  and gct.course_id = :courseId");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", courseId);
		return commonDao.querySqlForList(sql.toString(), params, GjtTextbook.class);
	}

	@Override
	public List<GjtCourse> findByXxIdAndCourseNatureAndCourseCategoryAndIsDeleted(String xxId, String courseNature,
			int courseCategory, String booleanNo) {		
		return gjtCourseDao.findByXxIdAndCourseNatureAndCourseCategoryAndIsDeleted(xxId,courseNature,courseCategory,booleanNo);
	}
}
