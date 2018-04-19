/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.organization;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.HttpUtil;
import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.exception.ServiceException;
import com.gzedu.xlims.common.gzedu.AnalyXmlUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.constants.OrgUtil;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.edumanage.OpenClassDao;
import com.gzedu.xlims.dao.organization.GjtClassInfoDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.organization.GjtSpecialtyCollegeDao;
import com.gzedu.xlims.dao.organization.GjtStudyCenterDao;
import com.gzedu.xlims.dao.usermanage.GjtEmployeeInfoDao;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtClassStudent;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtGradeSpecialty;
import com.gzedu.xlims.pojo.GjtGradeSpecialtyPlan;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtStudyYearCourse;
import com.gzedu.xlims.pojo.GjtStudyYearInfo;
import com.gzedu.xlims.pojo.college.GjtSpecialtyCollege;
import com.gzedu.xlims.pojo.graduation.GjtRulesClass;
import com.gzedu.xlims.pojo.opi.OpiTerm;
import com.gzedu.xlims.pojo.opi.OpiTermClass;
import com.gzedu.xlims.pojo.opi.OpiTermClassData;
import com.gzedu.xlims.pojo.opi.OpiTermCourse;
import com.gzedu.xlims.pojo.opi.OpiTermCourseData;
import com.gzedu.xlims.pojo.opi.RSimpleData;
import com.gzedu.xlims.pojo.system.StudyYear;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.api.ApiOpenClassService;
import com.gzedu.xlims.service.edumanage.GjtGradeSpecialtyPlanService;
import com.gzedu.xlims.service.edumanage.GjtRulesClassService;
import com.gzedu.xlims.service.edumanage.GjtStudyYearService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.service.pcourse.PCourseServer;
import com.gzedu.xlims.service.signup.SignupDataAddService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.third.eechat.util.EEIMService;

import net.sf.json.JSONObject;

/**
 *
 * 功能说明： 班级管理 实现接口
 *
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年5月5日
 * @version 2.5
 * @since JDK1.7
 *
 */

@Service
public class GjtClassInfoServiceImpl implements GjtClassInfoService {
	public static final Logger logger = LoggerFactory.getLogger(GjtClassInfoServiceImpl.class);

	@Value("#{configProperties['courseSyncServer']}")
	private String courseSyncServer;

	@Autowired
	private GjtClassInfoDao gjtClassInfoDao;

	@Autowired
	GjtClassStudentService gjtClassStudentService;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	ApiOpenClassService apiOpenClassService;

	@Autowired
	private SignupDataAddService signupDataAddService;

	@Autowired
	private GjtEmployeeInfoDao gjtEmployeeInfoDao;

	@Autowired
	private GjtStudyCenterDao gjtStudyCenterDao;

	@Autowired
	GjtRulesClassService gjtRulesClassService;

	@Autowired
	private EEIMService eEIMService;

	@Autowired
	private GjtSpecialtyCollegeDao gjtSpecialtyCollegeDao;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtSpecialtyService gjtSpecialtyService;

	@Autowired
	private GjtSchoolInfoService schoolInfoService;

	@Autowired
	private GjtCourseService gjtCourseService;

	@Autowired
	private GjtGradeSpecialtyPlanService gjtGradeSpecialtyPlanService;

	@Autowired
	private PCourseServer pCourseServer;

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private GjtStudyYearService gjtStudyYearService;

	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Autowired
	private OpenClassDao openClassDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;

	/**
	 * 按条件查询，分页
	 * 
	 * @param pageRequest
	 * @return
	 */
	@Override
	public Page<GjtClassInfo> querySource(final GjtClassInfo seachEntity, PageRequest pageRequest) {
		Specification<GjtClassInfo> spec = new Specification<GjtClassInfo>() {

			@Override
			public Predicate toPredicate(Root<GjtClassInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Path<String> jgbm = root.get("bjmc"); // 条件

				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();

				// 条件
				// if (StringUtils.isNotBlank(seachEntity.getZgh())) {
				// expressions.add(cb.equal(zgh, seachEntity.getZgh()));
				// }

				return predicate;

			}
		};

		return gjtClassInfoDao.findAll(spec, pageRequest);
	}

	@Override
	public Page<GjtClassInfo> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequest) {
		Criteria<GjtClassInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
		spec.add(Restrictions.or(Restrictions.eq("gjtSchoolInfo.id", orgId, true),
				Restrictions.in("orgId", orgList, true)));
		spec.addAll(Restrictions.parse(searchParams));

		PageRequest PageRequestOrder = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
				new Sort(Direction.DESC, "createdDt"));
		return gjtClassInfoDao.findAll(spec, PageRequestOrder);
	}

	@Override
	public List<GjtClassInfo> queryAllExport(String orgId, Map<String, Object> searchParams) {
		Criteria<GjtClassInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
		spec.add(Restrictions.or(Restrictions.eq("gjtSchoolInfo.id", orgId, true),
				Restrictions.in("orgId", orgList, true)));
		spec.addAll(Restrictions.parse(searchParams));

		return gjtClassInfoDao.findAll(spec);
	}

	@Override
	public long countClassInfo(String orgId, Map<String, Object> searchParams) {
		Criteria<GjtClassInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
		spec.add(Restrictions.or(Restrictions.eq("gjtSchoolInfo.id", orgId, true),
				Restrictions.in("xxzxId", orgList, true)));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtClassInfoDao.count(spec);
	}

	@Override
	public Map<String, Integer> queryClassPeople(List<String> classIds) {
		String sql = "select count(0) NUM,class_id from gjt_class_student cst  "
				+ "	where cst.class_id in(:classIds) and cst.is_deleted='N' and "
				+ "	exists (select 1 from gjt_student_info gsi where gsi.student_id = cst.student_id)  "
				+ "	group by cst.class_id";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("classIds", classIds);
		List<Map<String, Object>> resultList = commonDao.queryForMapList(sql, params);

		Map<String, Integer> newMap = new HashMap<String, Integer>();
		for (Map<String, Object> map : resultList) {
			newMap.put(String.valueOf(map.get("CLASS_ID")), Integer.valueOf(map.get("NUM").toString()));
		}
		return newMap;
	}

	@Override
	public List<Map<String, String>> queryClassInfo(List<String> str) {
		String sql = "select new map(g.classId as id,g.bjmc as name) from GjtClassInfo g   where g.classId in(:str)  and g.isDeleted='N'";
		Query query = em.createQuery(sql);
		query.setParameter("str", str);
		List<Map<String, String>> resultList = query.getResultList();
		return resultList;
	}

	/**
	 * 添加班级
	 *
	 * @param employeeInfo
	 */
	@Override
	public Boolean saveEntity(GjtClassInfo entity) {
		GjtClassInfo save = gjtClassInfoDao.save(entity);
		return save == null ? false : true;
	}

	@Override
	public void insertCreateClass(String xxId, String gradeId, String specialtyId) throws RuntimeException {
		List<GjtGradeSpecialtyPlan> specialtyPlans = gjtGradeSpecialtyPlanService.queryGradeSpecialtyPlan(gradeId,
				specialtyId);

		String appId = schoolInfoService.queryById(xxId).getAppid();

		for (GjtGradeSpecialtyPlan plan : specialtyPlans) {

			String courseId = plan.getCourseId();// 课程ID
			GjtCourse gjtCourse = gjtCourseService.queryBy(courseId);
			if (gjtCourse == null) {
				throw new ServiceException("课程不存在：" + courseId);
			}
			int studyYearCode = plan.getStudyYearCode();// 学年度CODE

			// 再次同步课程(防漏)
			gjtCourseService.syncCourse(appId, gjtCourse);

			// 自动生成 学年度
			GjtStudyYearInfo studyYearInfo = gjtStudyYearService.queryByStudyYearCodeAndXxId(studyYearCode,
					plan.getXxId());

			// 自动生成 学年度课程
			GjtStudyYearCourse studyyearCourse = gjtStudyYearService.queryByCourseAndStudyYearInfo(gjtCourse,
					studyYearInfo);

			// 添加 学年度课程 班级
			GjtClassInfo classInfo = queryByStudyyearCourseId(studyyearCourse.getId());

			// 如果不存在学年度课程班，就创建
			if (classInfo == null) {
				// GjtEmployeeInfo gjtEmployeeInfo =
				// gjtEmployeeInfoService.queryById(plan.getCounselorId());
				classInfo = new GjtClassInfo(plan, gjtCourse, 1);
				classInfo.setStudyyearCourse(studyyearCourse);
				// classInfo.setGjtBzr(gjtEmployeeInfo);
				classInfo.setVersion(BigDecimal.valueOf(2.5));
				classInfo.setIsDeleted("N");
				classInfo.setIsEnabled("1");
				classInfo.setClassType("course");
				classInfo.setBjlx("N");
				this.saveEntity(classInfo);
			}

			// 创建完 学年度，学年度课程，学年度课程班级 然后 同步 去教学平台

			// 同步学年度和学年度课程
			{
				OpiTerm opiTerm = new OpiTerm();
				opiTerm.setCREATED_BY("");
				opiTerm.setTERM_CODE(String.valueOf(studyYearCode));
				opiTerm.setTERM_END_DT(DateUtils.getTimeYMD(studyYearInfo.getStudyyearEndDate()));
				opiTerm.setTERM_START_DT(DateUtils.getTimeYMD(studyYearInfo.getStudyyearStartDate()));
				opiTerm.setTERM_NAME(StudyYear.getName(studyYearCode));
				opiTerm.setTERM_ID(studyYearInfo.getId());

				OpiTermCourse opiTermCourse = new OpiTermCourse();
				opiTermCourse.setTERMCOURSE_ID(studyyearCourse.getId());
				opiTermCourse.setTERM_ID(studyyearCourse.getStudyYearInfo().getId());
				opiTermCourse.setCOURSE_ID(gjtCourse.getCourseId());
				opiTermCourse.setCREATED_BY(gjtCourse.getCreatedBy());
				opiTermCourse.setTERMCOURSE_START_DT(DateUtils.getTimeYMD(studyYearInfo.getStudyyearStartDate()));
				opiTermCourse.setTERMCOURSE_END_DT(DateUtils.getTimeYMD(studyYearInfo.getStudyyearEndDate()));

				OpiTermCourseData data = new OpiTermCourseData(appId, opiTerm, opiTermCourse);
				RSimpleData rSimpleData = pCourseServer.synchroTermCourse(data);
				if (rSimpleData == null) {
					throw new ServiceException("学年度和学年度课程同步失败");
				} else {
					if (rSimpleData.getStatus() == 1) {

					} else {
						// throw new ServiceException(rSimpleData.getMsg());
						System.out.println(rSimpleData.getMsg());
					}
				}
			}

			{
				// 同步 学年度课程班级
				OpiTermClass termClass = new OpiTermClass();
				termClass.setCLASS_ID(classInfo.getClassId());
				termClass.setTERMCOURSE_ID(plan.getStudyYearCourseId());
				termClass.setCLASS_NAME(classInfo.getBjmc());
				termClass.setCREATED_BY(classInfo.getCreatedBy());
				termClass.setIS_EXPERIENCE("Y");

				OpiTermClassData data = new OpiTermClassData(termClass);
				RSimpleData rSimpleData = pCourseServer.synchroTermClass(data);
				if (rSimpleData == null) {
					throw new ServiceException("学年度课程班级同步失败");
				} else {
					if (rSimpleData.getStatus() == 1) {

					} else {
						System.out.println(rSimpleData.getMsg());
					}
				}
			}

		}
	}

	/**
	 * 修改班级信息
	 */
	@Override
	public Boolean updateEntity(GjtClassInfo employeeInfo) {
		GjtClassInfo save = gjtClassInfoDao.save(employeeInfo);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查询单个班级信息
	 */
	@Override
	public GjtClassInfo queryById(String id) {
		return gjtClassInfoDao.findOne(id);
	}

	@Override
	public Boolean queryByBjmc(String bjmc, String xxId) {
		GjtClassInfo gjtClassInfo = gjtClassInfoDao.findByBjmcAndGjtSchoolInfoIdAndIsDeleted(bjmc, xxId,
				Constants.BOOLEAN_NO);
		if (gjtClassInfo != null) {
			return true;
		}
		return false;
	}

	/**
	 * 查询所有班级
	 */
	@Override
	public List<GjtClassInfo> queryAll() {
		return gjtClassInfoDao.findAll();
	}

	@Override
	public Boolean deleteById(String id) {
		int i = gjtClassInfoDao.deleteById(id, "Y");
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Boolean deleteById(String[] ids) {
		for (String id : ids) {
			gjtClassInfoDao.deleteById(id, "Y");
		}
		return true;
	}

	@Override
	public void delete(String id) {
		gjtClassInfoDao.delete(id);
	}

	@Override
	public Boolean updateIsEnabled(String id, String str) {
		int i = gjtClassInfoDao.updateIsEnabled(id, str);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public long count(int studyYearCode, String courseId, String xxId) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("gjtOrg.id", new SearchFilter("gjtOrg.id", Operator.EQ, xxId));
		filters.put("gjtCourse.courseId", new SearchFilter("gjtCourse.courseId", Operator.EQ, courseId));
		filters.put("studyYearCode", new SearchFilter("studyYearCode", Operator.EQ, studyYearCode));
		Specification<GjtClassInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtClassInfo.class);

		return gjtClassInfoDao.count(spec);
	}

	public GjtClassInfo queryByStudyyearCourseId(String studyyearCourseId) {
		GjtClassInfo gjtClassInfo = gjtClassInfoDao.findByStudyYearCourseId(studyyearCourseId);
		return gjtClassInfo;
	}

	@Override
	public GjtClassInfo createTeachClassInfo(String termId, String specialtyId, int bh, String xxzxId, String xxId,
			String operatorId) throws Exception {
		GjtGrade gjtGrade = gjtGradeService.queryById(termId);
		if (gjtGrade == null) {
			throw new ServiceException("学期不存在");
		}
		String pycc, zymc;
		GjtSpecialty gjtSpecialty = gjtSpecialtyService.queryById(specialtyId);
		if (gjtSpecialty == null) {
			GjtSpecialtyCollege specialtyCollege = gjtSpecialtyCollegeDao.findOne(specialtyId);
			if (specialtyCollege == null) {
				throw new ServiceException("专业不存在");
			} else {
				pycc = specialtyCollege.getSpecialtyLevel();
				zymc = specialtyCollege.getName();
			}
		} else {
			pycc = gjtSpecialty.getPycc();
			zymc = gjtSpecialty.getZymc();
		}
		GjtClassInfo teachClass = null;
		GjtClassInfo info = null;
		// 根据年级、专业、院校ID、学习中心ID和培养层次查询班级
		List<GjtClassInfo> classInfo = gjtClassInfoDao
				.findByClassTypeAndGradeIdAndSpecialtyIdAndXxzxIdAndGjtSchoolInfoIdAndPyccAndIsDeletedOrderByCreatedDtDesc(
						"teach", termId, specialtyId, xxzxId, xxId, pycc, Constants.BOOLEAN_NO);
		// 查询按学习中心分班规则信息
		GjtRulesClass gjtRulesClass = gjtRulesClassService.findByOrgIdAndGradeIdAndXxzxId(xxId, gjtGrade.getGradeId(),
				xxzxId);
		if (classInfo != null && classInfo.size() > 0) {
			// 获取最后一条记录
			info = classInfo.get(classInfo.size() - 1);
			// 查询该教学班的人数是否大于分班规则的人数,小于则插入该班级，大于则新建班级
			int size = info.getGjtStudentInfos().size();
			int classNum = 500;
			if (gjtRulesClass != null) {
				classNum = gjtRulesClass.getPointClassNum();
			}
			if (size >= classNum) {
				// 重新生成一个教学班
				teachClass = createNewTeachClassInfo(gjtGrade, termId, specialtyId, pycc, xxId, zymc, operatorId,
						classInfo.size() + 1, xxzxId);
				return teachClass;
			}
			return info;
		} else {
			// 开发区和旗下的 生成相应的班级
			List<GjtOrg> org = gjtOrgDao.findByIdOrParentGjtOrg("a75ac723e412465a9d73799a10e1b255");
			for (GjtOrg gjtOrg : org) {
				if (gjtOrg.getId().equals(xxzxId)) {
					teachClass = createNewTeachClassInfo(gjtGrade, termId, specialtyId, pycc, xxId, zymc, operatorId,
							classInfo.size() + 1, xxzxId);
					return teachClass;
				}
			}

			// 根据年级、专业、院校ID和培养层次查询班级
			List<GjtClassInfo> classInfo2 = gjtClassInfoDao
					.findByClassTypeAndGradeIdAndSpecialtyIdAndGjtSchoolInfoIdAndPyccAndIsDeletedOrderByCreatedDtDesc(
							"teach", termId, specialtyId, xxId, pycc, Constants.BOOLEAN_NO);
			// 查询按年级专业层次总体分班规则信息
			GjtRulesClass ruleClassInfo = gjtRulesClassService.queryByOrgIdAndGradeId(xxId, gjtGrade.getGradeId(), "1");
			if (classInfo2 != null && classInfo2.size() > 0) {
				// 获取最后一条记录
				info = classInfo2.get(classInfo2.size() - 1);
				// 查询该教学班的人数是否大于500人,小于则插入该班级，大于则新建班级
				int size = info.getGjtStudentInfos().size();
				int classNum = 500;
				if (ruleClassInfo != null) {
					classNum = ruleClassInfo.getPointClassNum();
				}
				if (size >= classNum) {
					// 重新生成一个教学班
					teachClass = createNewTeachClassInfo(gjtGrade, termId, specialtyId, pycc, xxId, zymc, operatorId,
							classInfo.size() + 1, xxzxId);
					return teachClass;
				}
				return info;
			}
		}

		teachClass = createNewTeachClassInfo(gjtGrade, termId, specialtyId, pycc, xxId, zymc, operatorId, 1, xxzxId);
		return teachClass;
	}

	private GjtClassInfo createNewTeachClassInfo(GjtGrade gjtGrade, String termId, String specialtyId, String pycc,
			String xxId, String zymc, String operatorId, int index, String xxzxId) {
		// 根据教学班规则生成教学班名称：1/年级+专业+学习中心（简称）+层次+序号;2/年级+专业+层次+序号
		// 查询按学习中心分班规则信息
		GjtRulesClass gjtRulesClass = gjtRulesClassService.findByOrgIdAndGradeIdAndXxzxId(xxId, gjtGrade.getGradeId(),
				xxzxId);
		// 查询按年级专业层次总体分班规则信息
		GjtRulesClass ruleClassInfo = gjtRulesClassService.queryByOrgIdAndGradeId(xxId, gjtGrade.getGradeId(), "1");
		// 创建教学班
		GjtClassInfo classInfos = new GjtClassInfo();
		classInfos.setClassId(UUIDUtils.random());
		classInfos.setGjtGrade(gjtGrade);
		classInfos.setActualGradeId(termId);
		classInfos.setSpecialtyId(specialtyId);
		classInfos.setPycc(pycc);
		classInfos.setClassType("teach");
		// 0 专科 2 本科
		String pyccName = "Z";
		if ("0".equals(classInfos.getPycc())) {
			pyccName = "Z";
		} else if ("2".equals(classInfos.getPycc())) {
			pyccName = "B";
		}
		/*
		 * // 设置班主任，默认设置为第一个班主任 Object[] empInfo =
		 * gjtEmployeeInfoDao.findFirstTeacher(xxId); if (empInfo != null &&
		 * empInfo.length > 0) {
		 * classInfos.setGjtBzr(gjtEmployeeInfoDao.findOne((String)
		 * empInfo[0])); }
		 */
		// 国家开放大学实验学院默认班主任：bjbzr01 吴老师（芒果）【暂时固定死】
		if ("9b2f42ececf64f38af621554d1ea5b79".equals(xxId)) {
			classInfos.setGjtBzr(gjtEmployeeInfoDao.findOne("09ff9be24f6d4e34815bcb835c97e9c7"));
		} else if ("2f5bfcce71fa462b8e1f65bcd0f4c632".equals(xxId)
				&& !"a75ac723e412465a9d73799a10e1b255".equals(xxzxId)) {
			// 国家开放大学(广州)实验学院默认班主任：syxyxz01 陈老师【暂时固定死】
			classInfos.setGjtBzr(gjtEmployeeInfoDao.findOne("757356e506f54d38b18ecfc409d5f983"));
		} else {
			// 如果学员是广州市开发区学习中心的学员，则默认班主任的账号为 gkdongxiaojuan【董晓娟】
			classInfos.setGjtBzr(gjtEmployeeInfoDao.findOne("e51f49c7d4ff4e8296ff2a13535b7263"));
		}

		if (gjtRulesClass != null) {
			classInfos.setGjtStudyCenter(new GjtStudyCenter(xxzxId));
			classInfos.setGjtOrg(new GjtOrg(xxzxId));
			String bjmc = gjtGrade.getGradeName() + zymc + "X" + pyccName + (index < 10 ? "0" : "") + index + "班";
			classInfos.setBjmc(bjmc);
		} else {
			if (ruleClassInfo != null) {
				classInfos.setGjtOrg(new GjtOrg(xxId));
				String bjmc = gjtGrade.getGradeName() + zymc + pyccName + (index < 10 ? "0" : "") + index + "班";
				classInfos.setBjmc(bjmc);
			}
		}
		classInfos.setGjtSchoolInfo(gjtGrade.getGjtSchoolInfo());
		classInfos.setCreatedBy(operatorId);
		return gjtClassInfoDao.save(classInfos);
	}

	@Override
	public GjtClassInfo createCourseClassInfo(String termId, String courseId, String termcourseId, int bh, String xxId,
			String operatorId, String memo) throws Exception {
		GjtGrade gjtGrade = gjtGradeService.queryById(termId);
		if (gjtGrade == null) {
			throw new ServiceException("学期不存在");
		}
		GjtCourse gjtCourse = gjtCourseService.queryBy(courseId);
		if (gjtCourse == null) {
			throw new ServiceException("课程不存在");
		}

		GjtClassInfo classInfo = gjtClassInfoDao
				.findByClassTypeAndActualGradeIdAndCourseIdAndBhAndGjtSchoolInfoIdAndIsDeleted("course", termId,
						courseId, String.valueOf(bh), xxId, Constants.BOOLEAN_NO);
		if (classInfo != null) {
			return classInfo;
		}
		// 创建课程班
		classInfo = new GjtClassInfo();
		classInfo.setClassId(UUIDUtils.random());
		classInfo.setCourseId(courseId);
		classInfo.setActualGradeId(termId);
		classInfo.setBh(String.valueOf(bh));
		classInfo.setClassType("course");
		// 根据课程班规则生成课程班名称：程名+学期+序号 例如： 职业英语（专）2017春季01班
		String bjmc = gjtCourse.getKcmc() + gjtGrade.getGradeName() + (bh < 10 ? "0" : "") + bh + "班";
		classInfo.setBjmc(bjmc);
		classInfo.setGjtSchoolInfo(gjtGrade.getGjtSchoolInfo());

		classInfo.setBjlx("N");
		classInfo.setTermcourseId(termcourseId);
		classInfo.setLimitNum(2000); // 人数限制2000
		classInfo.setMemo(memo);
		classInfo.setCreatedBy(operatorId);
		return gjtClassInfoDao.save(classInfo);
	}

	// -----------------------------------------------------------------------------------------------------------------------------------------------------
	// //

	@Override
	public GjtClassInfo queryTeachClassInfo(String studentId) {
		return gjtClassInfoDao.findTeachClassByStudentId(studentId);
	}

	@Override
	public GjtClassInfo queryClassInfosByCourseIdAndStudyYearCode(String courseId, int studyYearCode) {
		return gjtClassInfoDao.findByCourseIdAndStudyYearCode(courseId, studyYearCode);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void setProgressAvg(List<GjtClassInfo> classInfoList) {
		if (classInfoList != null && !classInfoList.isEmpty()) {
			StringBuffer sb = new StringBuffer();
			sb.append("formMap.TERMCOURSECLASSIDS=");
			for (int i = 0; i < classInfoList.size(); i++) {
				GjtClassInfo classInfo = (GjtClassInfo) classInfoList.get(i);
				// 期课程ID
				String teachPlanId = classInfo.getTeachPlanId();
				// 期班级ID
				String classId = classInfo.getClassId();
				sb.append(teachPlanId + "||" + classId);
				if (i < classInfoList.size() - 1) {
					sb.append(",");
				}
			}
			String params = sb.toString();
			String url = courseSyncServer + "/opi/termcourse/getClassProover.do";
			String result = HttpUtil.postData(url, params, "GBK");

			if (result != null && !"".equals(result)) {
				Map resultMap = AnalyXmlUtil.parserXml(result);
				List list1 = AnalyXmlUtil.getList(resultMap.get("PROGRESS"));

				for (int i = 0; i < classInfoList.size(); i++) {
					for (int j = 0; j < list1.size(); j++) {
						GjtClassInfo classInfo = (GjtClassInfo) classInfoList.get(i);
						Map map1 = (Map) list1.get(j);

						String classId = classInfo.getClassId();
						String termclassId = (String) map1.get("TERMCLASS_ID");
						// 班级的CLASS_ID与接口数据中的TERMCLASS_ID对比
						if (classId.equals(termclassId)) {
							if (map1.get("CLASS_PROGRESS") != null) {
								classInfo.setProgressAvg(map1.get("CLASS_PROGRESS").toString());
							}
						}
					}
				}
			}
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void setScoreAvg(List<GjtClassInfo> classInfoList) {
		for (int i = 0; i < classInfoList.size(); i++) {
			GjtClassInfo classInfo = (GjtClassInfo) classInfoList.get(i);
			StringBuffer sb = new StringBuffer();
			sb.append("formMap.TERMCOURSE_ID=");
			// 期课程ID
			String teachPlanId = classInfo.getTeachPlanId();
			// 期班级ID
			String classId = classInfo.getClassId();
			sb.append(teachPlanId);
			sb.append("&formMap.CLASS_ID=" + classId);
			String params = sb.toString();
			String url = courseSyncServer + "/opi/termcourse/getClassAct.do";
			String result = HttpUtil.postData(url, params, "GBK");

			if (result != null && !"".equals(result)) {
				Map resultMap = AnalyXmlUtil.parserXml(result);
				if (resultMap.get("AVERAGESCORE") != null) {
					classInfo.setScoreAvg(resultMap.get("AVERAGESCORE").toString());
				}
			}
		}
	}

	@Override
	public List<Object[]> findClassIdANDTermcourseId(String supervisorId, String classType) {
		List<Object[]> list = gjtClassInfoDao.findClassIdANDTermcourseId(supervisorId, classType, "N");
		return list;
	}

	/**
	 * 查询课程班级
	 */
	@Override
	public Page getClassList(Map searchParams, PageRequest pageRequst) {
		return openClassDao.getClassList(searchParams, pageRequst);
	}

	/**
	 * 查询课程班级统计
	 */
	public int getClassCount(Map searchParams) {
		return openClassDao.getClassCount(searchParams);
	}

	/**
	 * 新建教学班
	 */
	@Override
	@Transactional
	public void createGjtClassInfo(GjtStudentInfo gjtStudentInfo, GjtOrg gjtOrg, String newSpecialtyId, int bh,
			String operatorId, GjtClassInfo oldGjtClassInfo) {
		logger.info("学习中心ID：" + gjtOrg.getId() + ";新专业ID：" + newSpecialtyId + ";院校ID："
				+ gjtStudentInfo.getGjtSchoolInfo().getId());
		String gz_teacher_eeno = "6957253";// 广州班主任老师EE号
		String bj_teacher_eeno = "3521112";// 北京班主任老师EE号
		GjtGrade gjtGrade = gjtGradeService.queryById(gjtStudentInfo.getViewStudentInfo().getGradeId());
		GjtStudyCenter gjtStudyCenter = gjtStudyCenterDao.findOne(gjtOrg.getId());
		GjtSpecialty gjtSpecialty = gjtSpecialtyService.findBySpecialtyById(newSpecialtyId);
		// 查询学员的默认班主任:广州的班主任为：学支老师(账号：syxyxz02、ee号：6957253/账号：syxyxz01、ee号：4795693);
		// 北京的班主任为：傅老师(账号：bjbzr01、ee号：3521112)
		GjtEmployeeInfo gjtEmployeeInfo = null;
		if ("2f5bfcce71fa462b8e1f65bcd0f4c632".equals(gjtStudentInfo.getGjtSchoolInfo().getId())) {// 国家开放大学（广州）实验学院ID
			gjtEmployeeInfo = gjtEmployeeInfoDao.findByEeno(gz_teacher_eeno);
		}
		if ("9b2f42ececf64f38af621554d1ea5b79".equals(gjtStudentInfo.getGjtSchoolInfo().getId())) {// 国家开放大学实验学院ID
			gjtEmployeeInfo = gjtEmployeeInfoDao.findByEeno(bj_teacher_eeno);
		}
		// 生成教学班级名称；命名规则：年级+专业+专业层次+序号
		// 0 专科 2 本科
		String pyccName = "";
		if ("0".equals(gjtStudentInfo.getPycc())) {
			pyccName = "Z";
		} else if ("2".equals(gjtStudentInfo.getPycc())) {
			pyccName = "B";
		}
		String bjmc = gjtGrade.getGradeName() + gjtSpecialty.getZymc() + pyccName + (bh < 10 ? "0" : "") + bh + "班";
		// 新建教学班
		GjtClassInfo classInfo = new GjtClassInfo();
		String classId = UUIDUtils.random();
		classInfo.setClassId(classId);
		classInfo.setGjtGrade(gjtGrade);
		classInfo.setActualGradeId(gjtStudentInfo.getNj());
		classInfo.setSpecialtyId(newSpecialtyId);
		classInfo.setPycc(gjtStudentInfo.getPycc());
		classInfo.setClassType("teach");
		classInfo.setGjtOrg(gjtOrg);
		classInfo.setGjtStudyCenter(gjtStudyCenter);
		classInfo.setGjtSchoolInfo(gjtStudentInfo.getGjtSchoolInfo());
		classInfo.setIsDeleted("N");
		classInfo.setOrgCode(gjtOrg.getCode());
		classInfo.setGjtBzr(gjtEmployeeInfo);
		classInfo.setBjmc(bjmc);
		classInfo.setVersion(BigDecimal.valueOf(2.5));
		classInfo.setCreatedBy(operatorId);
		gjtClassInfoDao.save(classInfo);
		// 把学员添加到该教学班中
		// 查询刚刚新生成的教学班，把学员添加到该教学班中
		GjtClassInfo gjtClassInfo = gjtClassInfoDao.findOne(classId);
		if (gjtClassInfo != null) {
			gjtClassStudentService.addStudentToClassStudent(gjtClassInfo, gjtStudentInfo, oldGjtClassInfo);
		}
	}

	@Override
	public GjtClassInfo findBySpecialtyIdAndGradeIdAndPyccAndClassTypeAndxxzxIdAndIsDeleted(String newSpecialty,
			String gradeId, String pycc, String classType, String id, String isDeleted) {
		return gjtClassInfoDao.findBySpecialtyIdAndGradeIdAndPyccAndClassTypeAndXxzxIdAndIsDeleted(newSpecialty,
				gradeId, pycc, classType, id, isDeleted);
	}

	@Override
	public Page<GjtClassInfo> queryGjtClassInfo(String id, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, "N"));
		filters.put("classType", new SearchFilter("classType", Operator.EQ, "teach"));
		List<String> orgList = gjtOrgDao.queryChildsByParentId(id);
		filters.put("orgId", new SearchFilter("orgId", SearchFilter.Operator.IN, orgList));
		// filters.put("gjtCourse.courseId", new
		// SearchFilter("gjtCourse.courseId", Operator.EQ, courseId));
		// filters.put("studyYearCode", new SearchFilter("studyYearCode",
		// Operator.EQ, studyYearCode));
		Specification<GjtClassInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtClassInfo.class);
		return gjtClassInfoDao.findAll(spec, pageRequst);

	}

	@Override
	public GjtClassInfo queryByBjmcAndXxzxId(String bjmc, String xxzxId) {
		return gjtClassInfoDao.findByBjmcAndXxzxIdAndIsDeletedAndClassType(bjmc.trim(), xxzxId, Constants.BOOLEAN_NO,
				"teach");
	}

	/**
	 * 获取教学辅导机构的课程列表
	 * 
	 * @param formMap
	 * @return
	 */
	@Override
	public List getXxzxClassList(Map formMap) {
		try {
			return openClassDao.getXxzxClassList(formMap);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList();
		}
	}

	/**
	 * 获取教学辅导机构的课程列表
	 * 
	 * @param formMap
	 * @return
	 */
	@Override
	public List getXxzxCourseList(Map formMap) {
		try {
			return openClassDao.getXxzxCourseList(formMap);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList();
		}
	}

	/**
	 * 新增学习中心和期课程关系
	 * 
	 * @param formMap
	 * @return
	 */
	@Override
	public int addTermcourseOrg(Map formMap) {
		int num = 0;
		try {
			String termcourseId = ObjectUtils.toString(formMap.get("TERMCOURSE_ID"));
			if (EmptyUtils.isNotEmpty(termcourseId)) {
				String termcourseIds[] = termcourseId.split(",");
				for (int i = 0; i < termcourseIds.length; i++) {
					if (EmptyUtils.isNotEmpty(ObjectUtils.toString(termcourseIds[i]).trim())) {
						formMap.put("TERM_ORG_ID", SequenceUUID.getSequence());
						formMap.put("TERMCOURSE_ID", ObjectUtils.toString(termcourseIds[i]).trim());
						num = openClassDao.addTermcourseOrg(formMap);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}

	/**
	 * 删除学习中心和期课程关系
	 * 
	 * @param formMap
	 * @return
	 */
	@Override
	public int delTermcourseOrg(Map formMap) {
		int num = 0;
		try {
			num = openClassDao.delTermcourseOrg(formMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}

	@Override
	public boolean syncClassTeacherToEeChat(GjtClassInfo classInfo) {
		final String appId = OrgUtil.getEEChatAppId(classInfo.getGjtSchoolInfo().getGjtOrg().getCode());
		// 先解散群
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("GROUP_ID", classInfo.getClassId());// 班级ID
		searchParams.put("GROUP_EEIM_NO", classInfo.getEegroup());// 班级EE号
		searchParams.put("APP_ID", appId);// APPID
		String status = eEIMService.destroyGroup(searchParams);
		if ("0".equals(status)) {
			// 组群
			Boolean flag = signupDataAddService.createGroupNoAndAddAllStudent(classInfo.getClassId());
			if (!flag) {
				logger.info("组建新群失败:" + classInfo.getClassId());
				return false;
			}
		} else {
			logger.info("解散群组失败:" + classInfo.getClassId());
			return false;
		}
		return true;
	}

	@Override
	public boolean createTeachClassAndRec(GjtStudentInfo gjtStudentInfo, GjtOrg gjtStudyCenter,
			GjtGradeSpecialty gjtGradeSpecialty, String newSpecialty, String newGradeId, int bh) {
		GjtClassInfo teachClass = null;
		try {
			// 生成教学班
			teachClass = this.createTeachClassInfo(newGradeId, newSpecialty, bh, gjtStudyCenter.getId(),
					gjtStudentInfo.getGjtSchoolInfo().getId(), "学员转学期生成");
			gjtStudentInfo.setUserclass(teachClass.getBjmc());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		// 学员加入该班级
		GjtClassStudent item = new GjtClassStudent();
		item.setClassStudentId(UUIDUtils.random());
		item.setGjtClassInfo(teachClass);
		item.setGjtStudentInfo(gjtStudentInfo);
		item.setGjtSchoolInfo(gjtStudentInfo.getGjtSchoolInfo());
		item.setGjtGrade(gjtStudentInfo.getGjtGrade());
		item.setCreatedBy("学员转学期生成");
		gjtClassStudentService.save(item);
		// 更新学员的专业ID、学期ID和产品ID
		gjtStudentInfo.setGjtGrade(new GjtGrade(newGradeId));
		gjtStudentInfo.setMajor(newSpecialty);
		gjtStudentInfo.setGradeSpecialtyId(gjtGradeSpecialty.getId());
		gjtStudentInfoService.updateEntity(gjtStudentInfo);
		// 生成课程班及选课
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("STUDENT_ID", gjtStudentInfo.getStudentId());
		Map result = apiOpenClassService.initStudentChoose(params);
		if (result != null) {
			JSONObject jsonResult = JSONObject.fromObject(result);
			logger.info("生成课程班及选课记录：" + jsonResult);
		}
		return true;
	}

	@Override
	public GjtClassInfo findCourseClassByStudentIdAndCourseId(String studentId, String courseId) {
		return gjtClassInfoDao.findCourseClassByStudentIdAndCourseId(studentId, courseId);
	}

	@Override
	public GjtClassInfo createNewTeachingTeachClassInfo(GjtGrade gjtGrade, String specialtyId, String pycc, String xxId,
			String zymc, String operatorId, int index) {
		// 查询按年级专业层次总体分班规则信息
		// 创建教学班
		GjtClassInfo classInfos = new GjtClassInfo();
		classInfos.setClassId(UUIDUtils.random());
		classInfos.setGjtGrade(gjtGrade);
		classInfos.setActualGradeId(gjtGrade.getGradeId());
		classInfos.setSpecialtyId(specialtyId);
		classInfos.setPycc(pycc);
		classInfos.setClassType("teach");
		// 0 专科 2 本科
		String pyccName = "Z";
		if ("0".equals(classInfos.getPycc())) {
			pyccName = "Z";
		} else if ("2".equals(classInfos.getPycc())) {
			pyccName = "B";
		}

		classInfos.setGjtOrg(new GjtOrg(xxId));
		String bjmc = gjtGrade.getGradeName() + zymc + pyccName + (index < 10 ? "0" : "") + index + "班";
		classInfos.setBjmc(bjmc);
		classInfos.setGjtSchoolInfo(gjtGrade.getGjtSchoolInfo());
		classInfos.setCreatedBy(operatorId);
		return gjtClassInfoDao.save(classInfos);
	}

}
