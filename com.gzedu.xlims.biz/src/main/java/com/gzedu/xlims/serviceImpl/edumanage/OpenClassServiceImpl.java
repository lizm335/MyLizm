package com.gzedu.xlims.serviceImpl.edumanage;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.SequenceUUID;
import com.gzedu.xlims.common.ToolUtil;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.exception.ServiceException;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.json.JsonUtils;
import com.gzedu.xlims.dao.GjtCourseDao;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.edumanage.GjtTeachPlanDao;
import com.gzedu.xlims.dao.edumanage.GjtTermCourseinfoDao;
import com.gzedu.xlims.dao.edumanage.OpenClassDao;
import com.gzedu.xlims.dao.openclass.GjtOnlineLessonCourseDao;
import com.gzedu.xlims.dao.openclass.GjtOnlineLessonDao;
import com.gzedu.xlims.dao.openclass.LcmsOnlineLessonDao;
import com.gzedu.xlims.dao.openclass.LcmsOnlineObjectDao;
import com.gzedu.xlims.dao.openclass.LcmsOnlinetutorInfoDao;
import com.gzedu.xlims.dao.organization.GjtClassInfoDao;
import com.gzedu.xlims.dao.organization.GjtGradeDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.organization.GjtSchoolInfoDao;
import com.gzedu.xlims.dao.organization.GjtSpecialtyDao;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.pojo.GjtTermCourseinfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.dto.CanCourseDto;
import com.gzedu.xlims.pojo.openClass.LcmsOnlineLesson;
import com.gzedu.xlims.pojo.openClass.LcmsOnlineObject;
import com.gzedu.xlims.pojo.openClass.LcmsOnlinetutorInfo;
import com.gzedu.xlims.pojo.openClass.OnlineLessonVo;
import com.gzedu.xlims.pojo.opi.OpiTerm;
import com.gzedu.xlims.pojo.opi.OpiTermClass;
import com.gzedu.xlims.pojo.opi.OpiTermClassData;
import com.gzedu.xlims.pojo.opi.OpiTermCourse;
import com.gzedu.xlims.pojo.opi.OpiTermCourseData;
import com.gzedu.xlims.pojo.opi.RSimpleData;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.api.ApiOucSyncService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.edumanage.LcmsOnlineLessonService;
import com.gzedu.xlims.service.edumanage.OpenClassService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.pcourse.PCourseServer;
import com.gzedu.xlims.serviceImpl.GjtCourseServiceImpl;

import net.spy.memcached.MemcachedClient;

@Service
public class OpenClassServiceImpl implements OpenClassService {

	private static Logger log = LoggerFactory.getLogger(GjtCourseServiceImpl.class);
	private static SimpleDateFormat ps = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Autowired
	private GjtCourseDao gjtCourseDao;

	@Autowired
	private OpenClassDao openClassDao;

	@Autowired
	private GjtTeachPlanDao gjtTeachPlanDao;

	@Autowired
	private GjtTermCourseinfoDao gjtTermCourseinfoDao;

	@Autowired
	private GjtSpecialtyDao gjtSpecialtyDao;

	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Autowired
	private GjtGradeDao gjtGradeDao;

	@Autowired
	private GjtClassInfoDao gjtClassInfoDao;

	@Autowired
	private GjtSchoolInfoDao gjtSchoolInfoDao;

	@Autowired
	private GjtCourseService gjtCourseService;

	@Autowired
	private GjtClassInfoService gjtClassInfoService;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private PCourseServer pCourseServer;

	@Autowired
	private MemcachedClient memcachedClient;

	@Autowired
	private GjtOnlineLessonDao gjtOnlineLessonDao;

	@Autowired
	private GjtOnlineLessonCourseDao gjtOnlineLessonCourseDao;

	@Autowired
	private LcmsOnlineLessonDao lcmsOnlineLessonDao;

	@Autowired
	private LcmsOnlinetutorInfoDao lcmsOnlinetutorInfoDao;

	@Autowired
	private GjtRecResultService gjtRecResultService;

	@Autowired
	private CommonDao commonDao;

	@Autowired
	private LcmsOnlineObjectDao lcmsOnlineObjectDao;

	@Autowired
	private LcmsOnlineLessonService lcmsOnlineLessonService;

	@Autowired
	ApiOucSyncService apiOucSyncService;

	@Override
	public Page<Map<String, Object>> queryGraduationSpecialtyList(Map<String, Object> searchParams,
			PageRequest pageRequst) {
		return openClassDao.queryGraduationSpecialtyList(searchParams, pageRequst);
	}

	@Override
	public List<CanCourseDto> queryCanCourseBy(String termId, String orgId) {
		// 院校获取总校ID
		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);

		Map<String, Object> searchParams = new HashMap<String, Object>(2);
		searchParams.put("termId", termId);
		searchParams.put("xxId", xxId);
		List<Map> result = openClassDao.queryCanCourseBy(searchParams);

		List<CanCourseDto> infoList = new ArrayList<CanCourseDto>(result.size());
		for (Iterator<Map> iter = result.iterator(); iter.hasNext();) {
			Map info = iter.next();

			CanCourseDto infoDto = new CanCourseDto();
			infoDto.setCourseId(ObjectUtils.toString(info.get("COURSE_ID")));
			infoDto.setKch(ObjectUtils.toString(info.get("KCH")));
			infoDto.setKcmc(ObjectUtils.toString(info.get("KCMC")));
			infoDto.setWsjxzk(ObjectUtils.toString(info.get("WSJXZK")));
			if ("0".equals(infoDto.getWsjxzk())) {
				infoDto.setWsjxzkName("非网上教学");
			} else if ("1".equals(infoDto.getWsjxzk())) {
				infoDto.setWsjxzkName("网上教学");
			}
			infoDto.setCourseNature(ObjectUtils.toString(info.get("COURSE_NATURE")));
			infoDto.setPycc(ObjectUtils.toString(info.get("PYCC")));
			if (StringUtils.isNotEmpty(infoDto.getPycc())) {
				infoDto.setPyccName(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.TRAININGLEVEL,
						infoDto.getPycc()));
			}
			infoDto.setCategory(ObjectUtils.toString(info.get("CATEGORY")));
			infoDto.setSubject(ObjectUtils.toString(info.get("SUBJECT")));
			infoDto.setSyhy(ObjectUtils.toString(info.get("SYHY")));
			infoDto.setSyzy(ObjectUtils.toString(info.get("SYZY")));
			BigDecimal hour = (BigDecimal) info.get("HOUR");
			infoDto.setHour(hour != null ? hour.intValue() : null);
			infoDto.setIsEnabled(ObjectUtils.toString(info.get("IS_ENABLED")));

			infoList.add(infoDto);
		}
		return infoList;
	}

	@Override
	public boolean updateOpenCourse(String termId, String courseIds, String orgId, String operatorId) {
		// 院校获取总校ID
		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);
		GjtSchoolInfo schoolInfo = gjtSchoolInfoDao.findOne(xxId);
		GjtGrade termInfo = gjtGradeDao.findOne(termId);

		String[] courseIdArrs = courseIds.split(",");
		for (int i = 0; i < courseIdArrs.length; i++) {
			String courseId = courseIdArrs[i];
			GjtCourse course = gjtCourseService.queryBy(courseId);

			Map<String, Object> searchParams = new HashMap<String, Object>(2);
			searchParams.put("courseId", courseId);
			searchParams.put("xxId", xxId);
			List<Map> result = openClassDao.querySpecialtyByCourse(searchParams);

			for (Iterator<Map> iter = result.iterator(); iter.hasNext();) {
				Map infoMap = iter.next();

				GjtTeachPlan info = new GjtTeachPlan();
				info.setTeachPlanId(UUIDUtils.random());
				info.setCourseId(courseId);
				info.setKclbm((String) infoMap.get("COURSE_TYPE_ID"));
				BigDecimal termTypeCode = (BigDecimal) infoMap.get("TERM_TYPE_CODE");
				info.setKkxq(termTypeCode != null ? termTypeCode.intValue() : null);
				info.setKkzy((String) infoMap.get("SPECIALTY_ID"));
				info.setPycc((String) infoMap.get("PYCC"));
				BigDecimal courseCategory = (BigDecimal) infoMap.get("COURSE_CATEGORY");
				info.setKcsx(courseCategory != null ? courseCategory.intValue() + "" : "");
				BigDecimal credit = (BigDecimal) infoMap.get("CREDIT");
				info.setXf(credit != null ? credit.doubleValue() : null);
				BigDecimal hour = (BigDecimal) infoMap.get("HOUR");
				info.setXs(hour != null ? hour.intValue() : null);
				info.setXxId(xxId);
				info.setOrgId(orgId);
				info.setOrgCode(gjtOrgDao.findOne(orgId).getTreeCode());
				info.setActualGradeId(termId);

				// 设置默认属性
				info.setExamStyle("0");
				info.setIsDoubleCertificate("0");
				info.setCourseType("0");
				info.setKsdw("1");
				info.setKcxxbz(0);
				info.setKcksbz(0);
				info.setKsfs("0");
				info.setCreatedBy(operatorId);
				gjtTeachPlanDao.save(info);
			}

			// 添加到学期课程信息
			GjtTermCourseinfo termCourseinfo = new GjtTermCourseinfo();
			termCourseinfo.setTermcourseId(UUIDUtils.random());
			termCourseinfo.setCourseId(courseId);
			termCourseinfo.setTermId(termId);
			termCourseinfo.setXxId(xxId);
			termCourseinfo.setCreatedBy(operatorId);
			termCourseinfo.setRemark("院校模式-学期课程");
			gjtTermCourseinfoDao.save(termCourseinfo);

			// 同步数据
			String appId = schoolInfo.getAppid();
			// step 1 再次同步课程(防漏)
			{
				gjtCourseService.syncCourse(appId, course);
			}
			// step 2 同步期
			{
				OpiTerm opiTerm = new OpiTerm();
				opiTerm.setCREATED_BY("");
				opiTerm.setTERM_CODE(String.valueOf(termInfo.getGradeCode()));
				opiTerm.setTERM_END_DT(DateUtils.getTimeYMD(termInfo.getStartDate()));
				opiTerm.setTERM_START_DT(DateUtils.getTimeYMD(termInfo.getEndDate()));
				opiTerm.setTERM_NAME(termInfo.getGradeName());
				opiTerm.setTERM_ID(termInfo.getGradeId());

				OpiTermCourse opiTermCourse = new OpiTermCourse();
				opiTermCourse.setTERMCOURSE_ID(termCourseinfo.getTermcourseId());
				opiTermCourse.setTERM_ID(termInfo.getGradeId());
				opiTermCourse.setCOURSE_ID(course.getCourseId());
				opiTermCourse.setCREATED_BY(course.getCreatedBy());
				opiTermCourse.setTERMCOURSE_START_DT(DateUtils.getTimeYMD(termInfo.getStartDate()));
				opiTermCourse.setTERMCOURSE_END_DT(DateUtils.getTimeYMD(termInfo.getEndDate()));

				OpiTermCourseData data = new OpiTermCourseData(appId, opiTerm, opiTermCourse);
				RSimpleData rSimpleData = pCourseServer.synchroTermCourse(data);
				if (rSimpleData == null) {
					throw new ServiceException("期同步失败");
				} else {
					if (rSimpleData.getStatus() == 1) {

					} else {
						log.error("function synchroTermCourse error ========= " + rSimpleData.getMsg());
						throw new ServiceException(rSimpleData.getMsg());
					}
				}
			}
			// step 3 创建课程班且同步课程班
			{
				// 创建课程班
				GjtClassInfo courseClassInfo = null;
				int bh = 1; // 班号 暂时为1
				try {
					courseClassInfo = gjtClassInfoService.createCourseClassInfo(termId, courseId,
							termCourseinfo.getTermcourseId(), bh, xxId, operatorId, "院校模式-自动生成课程班");
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (courseClassInfo != null && !Constants.BOOLEAN_YES.equals(courseClassInfo.getSyncStatus())) {
					OpiTermClass termClass = new OpiTermClass();
					termClass.setCLASS_ID(courseClassInfo.getClassId());
					termClass.setTERMCOURSE_ID(termCourseinfo.getTermcourseId());
					termClass.setCLASS_NAME(courseClassInfo.getBjmc());
					termClass.setYEAR(termInfo.getGradeName());
					termClass.setCREATED_BY(courseClassInfo.getCreatedBy());
					termClass.setIS_EXPERIENCE("N");

					OpiTermClassData data = new OpiTermClassData(termClass);
					RSimpleData rSimpleData = pCourseServer.synchroTermClass(data);
					if (rSimpleData == null) {
						throw new ServiceException("学年度课程班级同步失败");
					} else {
						if (rSimpleData.getStatus() == 1) {
							gjtClassInfoDao.updateSyncStatus(Constants.BOOLEAN_YES, courseClassInfo.getClassId());
						} else {
							log.error("function synchroTermClass error ========= " + rSimpleData.getMsg());
							throw new ServiceException(rSimpleData.getMsg());
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public Page<Map<String, Object>> queryTermCoureList(Map<String, Object> searchParams, PageRequest pageRequst) {
		return openClassDao.queryTermCoureList(searchParams, pageRequst);
	}

	@Override
	public Page<Map<String, Object>> queryTermCoureListByTeacher(Map<String, Object> searchParams,
			PageRequest pageRequst) {
		return openClassDao.queryTermCoureListByTeacher(searchParams, pageRequst);
	}

	/**
	 * 查询开课课程
	 *
	 * @param termId
	 * @param orgId
	 * @return
	 */
	@Override
	public List<CanCourseDto> getCourseList(String termId, String orgId) {

		// 院校获取总校ID
		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);

		Map<String, Object> searchParams = new HashMap<String, Object>(2);
		searchParams.put("termId", termId);
		searchParams.put("xxId", xxId);
		List<Map> result = openClassDao.getCourseList(searchParams);
		List<CanCourseDto> infoList = new ArrayList<CanCourseDto>(result.size());
		for (Iterator<Map> iter = result.iterator(); iter.hasNext();) {
			Map info = iter.next();
			CanCourseDto infoDto = new CanCourseDto();
			infoDto.setCourseId(ObjectUtils.toString(info.get("COURSE_ID")));
			infoDto.setKch(ObjectUtils.toString(info.get("KCH")));
			infoDto.setKcmc(ObjectUtils.toString(info.get("KCMC")));
			infoDto.setWsjxzk(ObjectUtils.toString(info.get("WSJXZK")));
			if ("0".equals(infoDto.getWsjxzk())) {
				infoDto.setWsjxzkName("非网上教学");
			} else if ("1".equals(infoDto.getWsjxzk())) {
				infoDto.setWsjxzkName("网上教学");
			}
			infoDto.setCourseNature(ObjectUtils.toString(info.get("COURSE_NATURE")));
			if (StringUtils.isNotEmpty(ObjectUtils.toString(info.get("COURSE_NATURE")))) {
				infoDto.setCourseNature(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.COURSENATURE,
						infoDto.getCourseNature()));
			}
			BigDecimal hour = (BigDecimal) info.get("HOUR");
			infoDto.setHour(hour != null ? hour.intValue() : null);
			infoDto.setIsEnabled(ObjectUtils.toString(info.get("IS_ENABLED")));
			infoDto.setIsOpen(ObjectUtils.toString(info.get("IS_OPEN")));
			infoList.add(infoDto);
		}
		return infoList;
	}

	@Override
	public boolean saveOpenCourse(String termId, String courseIds, GjtUserAccount user) {
		try {
			// 院校获取总校ID
			String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(user.getGjtOrg().getId());
			GjtSchoolInfo schoolInfo = gjtSchoolInfoDao.findOne(xxId);
			GjtGrade termInfo = gjtGradeDao.findOne(termId);

			if (EmptyUtils.isNotEmpty(schoolInfo) && EmptyUtils.isNotEmpty(termInfo)
					&& EmptyUtils.isNotEmpty(courseIds)) {
				String[] courseIdArrs = courseIds.split(",");
				String appId = schoolInfo.getAppid();
				for (int i = 0; i < courseIdArrs.length; i++) {
					String courseId = ObjectUtils.toString(courseIdArrs[i]).trim();
					GjtCourse courseInfo = gjtCourseDao.findOne(courseId);

					// 同步课程到学习平台
					gjtCourseService.syncCourse(appId, courseInfo);

					Map termMap = new HashMap();
					termMap.put("XX_ID", xxId);
					termMap.put("COURSE_ID", courseId);
					termMap.put("APP_ID", schoolInfo.getAppid());
					termMap.put("TERM_ID", termId);

					// 根据开课学期和课程查询需要开课的期课程属性
					List termList = openClassDao.getTermCourseInfo(termMap);
					if (EmptyUtils.isNotEmpty(termList)) {
						Map tcMap = (Map) termList.get(0);
						String termcourse_id = ObjectUtils.toString(tcMap.get("TERMCOURSE_ID"));
						String pcourse_termcourse_id = ObjectUtils.toString(tcMap.get("PCOURSE_TERMCOURSE_ID"));

						// 期课程不存在，新增期课程
						if (EmptyUtils.isEmpty(termcourse_id)) {
							termcourse_id = SequenceUUID.getSequence();
							tcMap.put("TERMCOURSE_ID", termcourse_id);
							tcMap.put("TERM_ID", termId);
							openClassDao.addTermCourse(tcMap);

							// 同步期课程到学习平台
							if (EmptyUtils.isEmpty(pcourse_termcourse_id)) {
								OpiTerm opiTerm = new OpiTerm();
								opiTerm.setCREATED_BY("admin");
								opiTerm.setTERM_CODE(String.valueOf(termInfo.getGradeCode()));
								opiTerm.setTERM_END_DT(DateUtils.getTimeYMD(termInfo.getCourseEndDate()));
								opiTerm.setTERM_START_DT(DateUtils.getTimeYMD(termInfo.getCourseStartDate()));
								opiTerm.setTERM_NAME(termInfo.getGradeName());
								opiTerm.setTERM_ID(termInfo.getGradeId());

								OpiTermCourse opiTermCourse = new OpiTermCourse();
								opiTermCourse.setTERMCOURSE_ID(ObjectUtils.toString(termcourse_id));
								opiTermCourse.setTERM_ID(ObjectUtils.toString(termId));
								opiTermCourse.setCOURSE_ID(courseId);
								opiTermCourse.setCREATED_BY("admin");
								opiTermCourse
										.setTERMCOURSE_START_DT(DateUtils.getTimeYMD(termInfo.getCourseStartDate()));
								opiTermCourse.setTERMCOURSE_END_DT(DateUtils.getTimeYMD(termInfo.getCourseEndDate()));

								OpiTermCourseData data = new OpiTermCourseData(appId, opiTerm, opiTermCourse);
								RSimpleData rSimpleData = pCourseServer.synchroTermCourse(data);
								if (EmptyUtils.isNotEmpty(rSimpleData) && rSimpleData.getStatus() == 1) {
									// 标识已同步
									openClassDao.updTermCourseSync(tcMap);
								}
							}
						}

						// 查询期课程下面的班级是否创建
						List classList = openClassDao.getTermClassInfo(tcMap);
						if (EmptyUtils.isNotEmpty(classList)) {
							Map cMap = (Map) classList.get(0);
							String term_class_id = ObjectUtils.toString(cMap.get("TERM_CLASS_ID"));
							String class_id = "";
							String className = ObjectUtils.toString(courseInfo.getKcmc())
									+ ObjectUtils.toString(termInfo.getGradeName()) + "01班";
							String bh = ToolUtil.getRandomNum(8);
							Map classMap = new HashMap();
							classMap.put("BH", bh);
							classMap.put("BJMC", className);
							classMap.put("TERM_ID", termId);
							classMap.put("CLASS_TYPE", "course");
							classMap.put("COURSE_ID", courseId);
							classMap.put("XX_ID", xxId);
							classMap.put("TERMCOURSE_ID", ObjectUtils.toString(tcMap.get("TERMCOURSE_ID")));
							classMap.put("AOTU_FLG", "Y");
							classMap.put("LIMIT_NUM", "500");

							if (EmptyUtils.isEmpty(term_class_id)) {
								class_id = SequenceUUID.getSequence();
								classMap.put("CLASS_ID", class_id);
								openClassDao.addCourseClass(classMap);

								OpiTermClass termClass = new OpiTermClass();
								termClass.setCLASS_ID(class_id);
								termClass.setTERMCOURSE_ID(ObjectUtils.toString(tcMap.get("TERMCOURSE_ID")));
								termClass.setCLASS_NAME(className);
								termClass.setYEAR(termInfo.getGradeName());
								termClass.setCREATED_BY("admin");
								termClass.setIS_EXPERIENCE("N");

								OpiTermClassData data = new OpiTermClassData(termClass);
								RSimpleData rSimpleData = pCourseServer.synchroTermClass(data);
								if (EmptyUtils.isNotEmpty(rSimpleData) && rSimpleData.getStatus() == 1) {
									// 标识已同步
									openClassDao.updClassSync(classMap);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 是否复制个数
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public int getCopyCount(Map searchParams) {
		return openClassDao.getCopyCount(searchParams);
	}

	@Override
	public long getStatusCount(Map searchParams) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  SELECT COUNT(GTC.TERMCOURSE_ID) COPY_COUNT");
		sql.append("  FROM GJT_COURSE GCE,GJT_GRADE GRE, GJT_TERM_COURSEINFO GTC");
		sql.append("  LEFT JOIN GJT_USER_ACCOUNT GUA ON GTC.CLASS_TEACHER=GUA.ID");
		sql.append("  WHERE GTC.IS_DELETED = 'N'");
		sql.append("  AND GCE.IS_DELETED = 'N'");
		sql.append("  AND GRE.IS_DELETED = 'N'");
		sql.append("  AND GTC.COURSE_ID = GCE.COURSE_ID");
		sql.append("  AND GTC.TERM_ID = GRE.GRADE_ID");
		String gradeId = (String) searchParams.get("EQ_gradeId");
		if (EmptyUtils.isNotEmpty(gradeId)) {
			sql.append("  AND GTC.TERM_ID = :gradeId");
			params.put("gradeId", gradeId);
		}
		String orgId = (String) searchParams.get("EQ_orgId");
		if (EmptyUtils.isNotEmpty(orgId)) {
			sql.append("  AND GTC.XX_ID = :orgId");
			params.put("orgId", orgId);
		}

		Object course = searchParams.get("LIKE_course");
		if (course != null && StringUtils.isNotBlank((String) course)) {
			sql.append(" and (GCE.KCH = :kch OR GCE.KCMC like :course) ");
			params.put("kch", course);
			params.put("course", "%" + course + "%");
		}

		String status = (String) searchParams.get("EQ_status");
		if (StringUtils.isNotEmpty(status)) {
			if ("1".equals(status)) {
				sql.append(" and SYSDATE >= GRE.COURSE_START_DATE and  SYSDATE <=GRE.COURSE_END_DATE ");
			} else if ("2".equals(status)) {
				sql.append(" and SYSDATE >GRE.COURSE_END_DATE ");
			}
		}
		Object teacherName = searchParams.get("LIKE_teacherName");
		if (StringUtils.isNotBlank((String) teacherName)) {
			sql.append(" and GUA.REAL_NAME like :teacherName ");
			params.put("teacherName", "%" + teacherName + "%");
		}
		return openClassDao.countBySql(sql, params);
	}

	/**
	 * 获取开设课程的选课人数
	 */
	@Override
	public List getCourseChooseCount(Map searchParams) {
		return openClassDao.getCourseChooseCount(searchParams);
	}

	/**
	 * 初始化期课程的复制状态
	 *
	 * @param searchParams
	 * @return
	 */
	@Override
	public Map updTermCopyFlg(Map searchParams) {
		Map resultMap = new HashMap();
		int num = 0;
		String termId = ObjectUtils.toString(searchParams.get("TERM_ID"));
		try {
			// 放入缓存，在两分钟内不重复调用
			if (EmptyUtils.isNotEmpty(ObjectUtils.toString(memcachedClient.get("TERM_" + termId)))) {
				return resultMap;
			} else {
				memcachedClient.add("TERM_" + termId, 120, termId);
			}

			num = openClassDao.updTermCopyFlg2(searchParams);
			if (num > 0) {
				List termList = openClassDao.getTermCourseTaskCount(searchParams);
				if (EmptyUtils.isNotEmpty(termList)) {
					for (int i = 0; i < termList.size(); i++) {
						Map tempMap = (Map) termList.get(i);
						openClassDao.updTermCopyFlg1(tempMap);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		memcachedClient.delete("TERM_" + termId);
		resultMap.put("num", num);
		return resultMap;
	}

	/**
	 * 导出数据
	 */
	public Workbook expTermCourse(Map formMap, HttpServletRequest request, HttpServletResponse response) {
		HSSFWorkbook wb = new HSSFWorkbook();
		try {
			HSSFSheet sheet = wb.createSheet();
			HSSFRow row;
			HSSFCell cell;
			int rowIndex = 0;
			int cellIndex = 0;
			row = sheet.createRow(rowIndex++);
			// 标题栏
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程代码");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程名称");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("选课人数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("课程班数");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("资源状态");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("复制状态");

			List recordlist = openClassDao.queryTermCoureList(formMap);
			if (EmptyUtils.isNotEmpty(recordlist)) {
				for (int i = 0; i < recordlist.size(); i++) {
					Map e = (Map) recordlist.get(i);

					cellIndex = 0;
					row = sheet.createRow(rowIndex++);

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("GRADE_NAME")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("KCH")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("KCMC")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("CHOOSE_COUNT")));

					cell = row.createCell(cellIndex++);
					cell.setCellValue(ObjectUtils.toString(e.get("CLASS_COUNT")));

					String is_enabled = ObjectUtils.toString(e.get("IS_ENABLED"));
					cell = row.createCell(cellIndex++);
					if ("0".equals(is_enabled)) {
						cell.setCellValue("暂无资源");
					} else if ("1".equals(is_enabled)) {
						cell.setCellValue("已启用");
					} else if ("2".equals(is_enabled)) {
						cell.setCellValue("建设中");
					} else {
						cell.setCellValue("暂无资源");
					}

					String copy_flg = ObjectUtils.toString(e.get("COPY_FLG"));
					cell = row.createCell(cellIndex++);
					if ("2".equals(copy_flg)) {
						cell.setCellValue("已复制");
					} else {
						cell.setCellValue("待复制");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}

	@Override
	public int updateOnlineLesson(Map formMap, int type) throws Exception {
		int re = 0;
		if (1 == type) {// add
			// 新增直播
			re = saveLesson(formMap);
		} else if (2 == type) {// update
			re = updateLesson(formMap);

		} else if (3 == type) {// delete
			LcmsOnlineLesson lesson = lcmsOnlineLessonDao.findOne((String) formMap.get("onlineId"));
			lesson.setIsDeleted(Constants.ISDELETED);
			lcmsOnlineLessonDao.save(lesson);
		}
		return re;
	}

	@Override
	public Page<Map> getLessonStu(Map param, PageRequest pageRequest) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append(
				"  SELECT PIC,XM, XH,SJH, GRADE_NAME, TERM_NAME, ZYMC, PYCC, KCMC, KCH, min(LOGIN_DT) LOGIN_DT,IS_JOIN");
		sql.append("  FROM (SELECT GSI.AVATAR PIC,");
		sql.append("  VSI.XM,");
		sql.append("  VSI.XH,");
		sql.append("  GSI.SJH,");
		sql.append("  GY.NAME GRADE_NAME,");
		sql.append("  GG.GRADE_NAME TERM_NAME,");
		sql.append("  VSI.ZYMC,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.CODE = VSI.PYCC");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel') PYCC,");
		sql.append("  GC.KCMC,");
		sql.append("  GC.KCH,");
		sql.append("  (SELECT LOU.CREATED_DT LOGIN_DT");
		sql.append("  FROM LCMS_ONLINETUTOR_USER LOU");
		sql.append("  WHERE LOU.ONLINETUTOR_ID = LOI.ONLINETUTOR_ID");
		sql.append("  AND LOU.ISDELETED = 'N'");
		sql.append("  AND LOU.USER_ID = GSI.STUDENT_ID) LOGIN_DT,");
		sql.append("  (CASE");
		sql.append("  WHEN EXISTS (SELECT 1");
		sql.append("  FROM LCMS_ONLINETUTOR_USER LOU");
		sql.append("  WHERE LOU.ONLINETUTOR_ID = LOI.ONLINETUTOR_ID");
		sql.append("  AND LOU.ISDELETED = 'N'");
		sql.append("  AND LOU.USER_ID = GSI.STUDENT_ID");
		sql.append("  AND LOU.IS_INTO = 'Y') THEN");
		sql.append("  '已参与'");
		sql.append("  ELSE");
		sql.append("  '未参与'");
		sql.append("  END) IS_JOIN");
		sql.append("  FROM VIEW_STUDENT_INFO  VSI,");
		sql.append("  GJT_COURSE         GC,");
		sql.append("  GJT_YEAR           GY,");
		sql.append("  GJT_GRADE          GG,");
		sql.append("  GJT_REC_RESULT     GRR,");
		sql.append("  LCMS_ONLINE_LESSON LOL");
		sql.append("  INNER JOIN GJT_STUDENT_INFO GSI");
		sql.append("  ON GSI.IS_DELETED = 'N'");
		sql.append("  INNER JOIN LCMS_ONLINETUTOR_INFO LOI");
		sql.append("  ON LOI.ISDELETED = 'N'");
		sql.append("  WHERE GRR.IS_DELETED = 'N'");
		sql.append("  AND GC.IS_DELETED = 'N'");
		sql.append("  AND GG.IS_DELETED = 'N'");
		sql.append("  AND GY.GRADE_ID = GG.YEAR_ID");
		sql.append("  AND GC.COURSE_ID = GRR.COURSE_ID");
		sql.append("  AND VSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND GRR.TERM_ID = GG.GRADE_ID");
		sql.append("  AND GSI.STUDENT_ID = GRR.STUDENT_ID");
		sql.append("  AND LOI.ONLINETUTOR_ID = LOL.ONLINETUTOR_ID");
		sql.append("  AND GRR.TERMCOURSE_ID = LOI.TERMCOURSE_ID");
		sql.append("  AND LOL.NUMBER_ = :number) TAB");
		sql.append("  WHERE 1 = 1");

		String joinType = ObjectUtils.toString(param.get("joinType"));
		String stuName = ObjectUtils.toString(param.get("stuName"));
		if (EmptyUtils.isNotEmpty(joinType) && joinType.length() == 1) {
			if (Constants.BOOLEAN_NO.equals(joinType))
				sql.append(" AND TAB.IS_JOIN = '未参与' ");
			else
				sql.append(" AND TAB.IS_JOIN = '已参与' ");
		}
		if (EmptyUtils.isNotEmpty(stuName)) {
			sql.append(" AND TAB.XM LIKE '%" + stuName + "%' ");
		}
		sql.append("  GROUP BY PIC,XM, XH,SJH,GRADE_NAME, TERM_NAME,ZYMC,PYCC,KCMC,KCH,IS_JOIN");
		params.put("number", ObjectUtils.toString(param.get("number")));
		return openClassDao.findByPageSql(sql, params, pageRequest, Map.class);
	}

	public Long[] getLessonStuCount(String number) {

		StringBuilder sql = new StringBuilder();
		sql.append("  select 1");
		sql.append("  from gjt_term_courseinfo gtc, gjt_rec_result grr");
		sql.append("  where gtc.termcourse_id = grr.termcourse_id");
		sql.append("  and gtc.is_deleted = 'N'");
		sql.append("  and grr.is_deleted = 'N'");
		sql.append("  and exists");
		sql.append("  (select 1");
		sql.append("  from LCMS_ONLINETUTOR_INFO loi, LCMS_ONLINE_LESSON lol");
		sql.append("  where loi.onlinetutor_id = lol.onlinetutor_id");
		sql.append("  and loi.isdeleted = 'N'");
		sql.append("  and lol.number_ = :number");
		sql.append("  and loi.termcourse_id = gtc.termcourse_id)");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("number", number);
		long allNum = commonDao.queryForCountNative(sql.toString(), params);
		sql = new StringBuilder();
		sql.append("  select distinct (lou.user_id)");
		sql.append("  from LCMS_ONLINETUTOR_USER lou");
		sql.append("  where lou.isdeleted = 'N'");
		sql.append("  and lou.is_into = 'Y'");
		sql.append("  and exists");
		sql.append("  (select 1");
		sql.append("  from LCMS_ONLINETUTOR_INFO loi, LCMS_ONLINE_LESSON lol");
		sql.append("  where loi.onlinetutor_id = lol.onlinetutor_id");
		sql.append("  and loi.isdeleted = 'N'");
		sql.append("  and lol.number_ = :number");
		sql.append("  and loi.onlinetutor_id = lou.onlinetutor_id)");

		long joinNum = commonDao.queryForCountNative(sql.toString(), params);
		Long[] arr = new Long[] { allNum, joinNum };
		return arr;
	}

	@Override
	public long getOnlineLessonCount(Map param) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  SELECT 1");
		sql.append("  FROM lcms_online_lesson    LOL,");
		sql.append("  LCMS_ONLINE_OBJECT LOO,");
		sql.append("  GJT_COURSE            GC,");
		sql.append("  GJT_TERM_COURSEINFO   GTC");
		sql.append("  WHERE LOL.IS_DELETED= 'N'");
		sql.append("  AND GC.IS_DELETED = 'N'");
		sql.append("  AND GTC.IS_DELETED = 'N'");
		sql.append("  AND LOL.ONLINETUTOR_ID = LOO.ONLINETUTOR_ID");
		sql.append("  AND GTC.TERMCOURSE_ID = LOO.OBJECT_ID");
		sql.append("  AND GC.COURSE_ID = GTC.COURSE_ID");
		sql.append("  AND LOO.OBJECT_TYPE='3'");

		if (EmptyUtils.isNotEmpty(param.get("onlinetutorId"))) {
			params.put("onlinetutorId", ObjectUtils.toString(param.get("onlinetutorId")));
			sql.append("  AND LOL.ONLINETUTOR_ID = :onlinetutorId");
		}

		if (EmptyUtils.isNotEmpty(param.get("termCourseId"))) {
			params.put("TERMCOURSE_ID", ObjectUtils.toString(param.get("termCourseId")));
			sql.append("  AND GTC.TERMCOURSE_ID = :TERMCOURSE_ID");
		}
		if (EmptyUtils.isNotEmpty(param.get("termId")) && ObjectUtils.toString(param.get("termId")).length() > 10) {
			params.put("TERM_ID", ObjectUtils.toString(param.get("termId")));
			sql.append("  AND GTC.TERM_ID = :TERM_ID");
		}

		if (EmptyUtils.isNotEmpty(param.get("onlineName"))) {
			params.put("ONLINE_NAME", "%" + ObjectUtils.toString(param.get("onlineName")) + "%");
			sql.append("  AND LOL.ONLINETUTOR_NAME LIKE :ONLINE_NAME");
		}

		if (EmptyUtils.isNotEmpty(param.get("courseName"))) {
			params.put("COURSE_NAME", "%" + ObjectUtils.toString(param.get("courseName")) + "%");
			sql.append("  AND ( GC.KCMC LIKE :COURSE_NAME OR  GC.KCH LIKE :COURSE_NAME ) ");
		}

		String lessonType = ObjectUtils.toString(param.get("lessonType"));
		if (EmptyUtils.isNotEmpty(lessonType)) {
			if ("1".equals(lessonType)) {// 直播中
				sql.append("  AND (LOL.ONLINETUTOR_START < SYSDATE AND LOL.ONLINETUTOR_FINISH > SYSDATE )");
			} else if ("0".equals(lessonType)) {// 未开始
				sql.append("  AND LOL.ONLINETUTOR_START > SYSDATE");
			} else if ("2".equals(lessonType)) {// 已结束
				sql.append("  AND LOL.ONLINETUTOR_FINISH < SYSDATE");
			}
		}

		String classTeacher = ObjectUtils.toString(param.get("classTeacher"));
		if (EmptyUtils.isNotEmpty(classTeacher)) {
			params.put("CLASS_TEACHER", classTeacher);
			sql.append("  AND GTC.CLASS_TEACHER = :CLASS_TEACHER");
		}
		sql.append(" GROUP BY LOL.ONLINETUTOR_ID");
		return commonDao.queryForCountNative(sql.toString(), params);
	}

	public LcmsOnlinetutorInfo getOnlineLesson(String number) {
		return lcmsOnlinetutorInfoDao.findOneByNumber(number);
	}

	public List<Map> getTeachPlan(List<String> termCourseIds, PageRequest pageRequest) {

		StringBuilder sql = new StringBuilder();

		sql.append("  SELECT");
		sql.append("  	GTC.TERMCOURSE_ID,");
		sql.append("  	GG.GRADE_NAME,");
		sql.append("  	GC.KCMC,");
		sql.append("  	GC.KCH,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT (1)");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		WHERE");
		sql.append("  			GRR.TERMCOURSE_ID = GTC.TERMCOURSE_ID");
		sql.append("  		AND GRR.IS_DELETED = 'N'");
		sql.append("  	) CHOOSE_STU,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT (1)");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		WHERE");
		sql.append("  			GRR.TERMCOURSE_ID = GTC.TERMCOURSE_ID");
		sql.append("  		AND GRR.IS_DELETED = 'N'");
		sql.append("  		AND GRR.EXAM_STATE = '0'");
		sql.append("  	) NEEDLESSON_STU,");
		sql.append("  	NVL (");
		sql.append("  		(");
		sql.append("  			SELECT");
		sql.append("  				GUA.REAL_NAME");
		sql.append("  			FROM");
		sql.append("  				GJT_USER_ACCOUNT GUA");
		sql.append("  			WHERE");
		sql.append("  				GUA.IS_DELETED = 'N'");
		sql.append("  			AND GUA. ID = GTC.CLASS_TEACHER");
		sql.append("  		),");
		sql.append("  		'--'");
		sql.append("  	) CLASS_TEACHER");
		sql.append("  FROM");
		sql.append("  	GJT_TERM_COURSEINFO GTC,");
		sql.append("  	GJT_COURSE GC,");
		sql.append("  	GJT_GRADE GG");
		sql.append("  WHERE");
		sql.append("    GTC.IS_DELETED ='N' AND GC.IS_DELETED ='N' AND GG.IS_DELETED ='N'");
		sql.append("  AND GTC.COURSE_ID = GC.COURSE_ID");
		sql.append("  AND GTC.TERM_ID = GG.GRADE_ID");
		if (EmptyUtils.isNotEmpty(termCourseIds))
			sql.append("  AND GTC.TERMCOURSE_ID IN (:termCourseIds) ");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("termCourseIds", termCourseIds);
		return openClassDao.findAllByToMap(sql, params, null);
	}

	public int saveLesson(Map formMap) {
		int result = 1;
		String onlineTutorName = ObjectUtils.toString(formMap.get("onlineName"));
		String onlinetutorDesc = ObjectUtils.toString(formMap.get("onlinetutorDesc"));
		String imgUrl = ObjectUtils.toString(formMap.get("imgUrl"));

		String url = AppConfig.getProperty("roomCreate_url");
		PostMethod postMethod = new PostMethod(url);
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setContentCharset("UTF-8");

		try {
			NameValuePair nvp = new NameValuePair("loginName", AppConfig.getProperty("gensee_loginName"));
			NameValuePair nvp1 = new NameValuePair("password", AppConfig.getProperty("gensee_password"));
			String subject = "" + new Date().getTime();
			NameValuePair nvp2 = new NameValuePair("subject", onlineTutorName + subject);
			NameValuePair nvp3 = new NameValuePair("startDate", ObjectUtils.toString(formMap.get("startDt")) + ":00");
			// 支持Web端学生加入
			NameValuePair nvp4 = new NameValuePair("webJoin", "true");
			// 暂不支持客户端端学生加入
			NameValuePair nvp5 = new NameValuePair("clientJoin", "true");
			// 默认用户口令
			NameValuePair nvp6 = new NameValuePair("teacherToken", "654321");
			NameValuePair nvp7 = new NameValuePair("studentToken", "123456");
			NameValuePair nvp8 = new NameValuePair("assistantToken", "111111");
			NameValuePair nvp9 = new NameValuePair("description", onlinetutorDesc);
			NameValuePair nvp10 = new NameValuePair("uiMode", "4");

			String invalidDate = (String) formMap.get("endDt") + ":00";
			NameValuePair nvp11 = new NameValuePair("invalidDate", invalidDate);

			postMethod.addParameter(nvp);
			postMethod.addParameter(nvp1);
			postMethod.addParameter(nvp2);
			postMethod.addParameter(nvp3);
			postMethod.addParameter(nvp4);
			postMethod.addParameter(nvp5);
			postMethod.addParameter(nvp6);
			postMethod.addParameter(nvp7);
			postMethod.addParameter(nvp8);
			postMethod.addParameter(nvp9);
			postMethod.addParameter(nvp10);
			postMethod.addParameter(nvp11);

			httpClient.executeMethod(postMethod);
			if (postMethod.getStatusCode() == HttpStatus.SC_OK) {
				String message = postMethod.getResponseBodyAsString();
				OnlineLessonVo onlineLessonVo = JsonUtils.toObject(message, OnlineLessonVo.class);
				if ("0".equals(onlineLessonVo.getCode())) {
					onlineLessonVo.setSubject(onlineTutorName + subject);
					onlineLessonVo.setSdkId(onlineLessonVo.getId());

					String[] termCourseIds = (ObjectUtils.toString(formMap.get("termCourseIds"))).split(",");
					String createdBy = (String) formMap.get("createdBy");
					Date now = new Date();
					LcmsOnlineLesson lcmsLesson = new LcmsOnlineLesson();
					lcmsLesson.setId(UUIDUtils.random());
					lcmsLesson.setOnlinetutorId(UUIDUtils.random());
					lcmsLesson.setSubject(onlineLessonVo.getSubject());
					lcmsLesson.setDescription(onlinetutorDesc);
					lcmsLesson.setAssistanttoken(onlineLessonVo.getAssistantToken());
					lcmsLesson.setClientjoin(onlineLessonVo.getClientJoin());
					lcmsLesson.setCode(onlineLessonVo.getCode());
					lcmsLesson.setSdkId(onlineLessonVo.getSdkId());
					lcmsLesson.setNumber(onlineLessonVo.getNumber());
					lcmsLesson.setScene(onlineLessonVo.getScene());
					lcmsLesson.setStartdate(new Date(onlineLessonVo.getStartDate()));
					lcmsLesson.setInvaliddate(new Date(onlineLessonVo.getInvalidDate()));
					lcmsLesson.setStudentclienttoken(onlineLessonVo.getStudentClientToken());
					lcmsLesson.setStudentjoinurl(onlineLessonVo.getStudentJoinUrl());
					lcmsLesson.setTeacherjoinurl(onlineLessonVo.getTeacherJoinUrl());
					lcmsLesson.setTeachertoken(onlineLessonVo.getTeacherToken());
					lcmsLesson.setWebjoin(onlineLessonVo.getWebJoin());
					lcmsLesson.setStudenttoken(onlineLessonVo.getStudentToken());
					lcmsLesson.setVideojoinurl(onlineLessonVo.getVideoJoinUrl());
					lcmsLesson.setVideotoken(onlineLessonVo.getVideoToken());
					lcmsLesson.setOnlinetutorName(onlineTutorName);
					lcmsLesson.setOnlinetutorType("2");
					lcmsLesson.setOnlinetutorDesc(onlinetutorDesc);
					lcmsLesson.setOnlinetutorStart(new Date(onlineLessonVo.getStartDate()));
					lcmsLesson.setOnlinetutorFinish(new Date(onlineLessonVo.getInvalidDate()));
					lcmsLesson.setIsDeleted(Constants.NODELETED);
					lcmsLesson.setCreatedBy(createdBy);
					lcmsLesson.setCreatedDt(now);
					lcmsLesson.setImgUrl(imgUrl);
					lcmsOnlineLessonDao.save(lcmsLesson);
					for (String tcid : termCourseIds) {
						LcmsOnlineObject object = new LcmsOnlineObject();
						object.setCreatedBy(createdBy);
						object.setCreatedDt(now);
						object.setIsDeleted(Constants.NODELETED);
						object.setObjectId(tcid);
						object.setObjectType("3");
						object.setOnlineObjectId(UUIDUtils.random());
						object.setOnlinetutorId(lcmsLesson.getOnlinetutorId());
						lcmsOnlineObjectDao.save(object);
						lcmsOnlineLessonService.insertJoinStudent(lcmsLesson.getOnlinetutorId(), tcid, createdBy);
					}
				} else {
					System.out.println("调用" + url + "接口失败!");
					System.out.println(message);
					result = 0;
				}
			} else {
				System.out.println("调用" + url + "接口失败!");
				result = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = 0;
		} finally {
			postMethod.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
			return result;
		}

	}

	public int updateLesson(Map formMap) throws ParseException {

		List<String> newTcids = Arrays.asList((ObjectUtils.toString(formMap.get("termCourseIds"))).split(","));
		String id = ObjectUtils.toString(formMap.get("id"));
		LcmsOnlineLesson lesson = lcmsOnlineLessonDao.findOne(id);
		if (EmptyUtils.isEmpty(lesson)) {
			return 0;
		}
		String uid = ObjectUtils.toString(formMap.get("createdBy"));
		String lessonName = ObjectUtils.toString(formMap.get("onlineName"));
		String onlinetutorDesc = ObjectUtils.toString(formMap.get("onlinetutorDesc"));
		String startDateStr = ObjectUtils.toString(formMap.get("startDt")) + ":00";
		String endDateStr = ObjectUtils.toString(formMap.get("endDt")) + ":00";
		Date startDate = DateUtils.getDateToString(startDateStr);
		Date endDate = DateUtils.getDateToString(endDateStr);
		String imgUrl = ObjectUtils.toString(formMap.get("imgUrl"));
		Date now = new Date();
		lesson.setSubject(lessonName + now.getTime());
		lesson.setDescription(onlinetutorDesc);
		lesson.setStartdate(startDate);
		lesson.setInvaliddate(endDate);
		lesson.setOnlinetutorStart(startDate);
		lesson.setOnlinetutorFinish(endDate);
		lesson.setUpdatedBy(uid);
		lesson.setUpdatedDt(now);
		lesson.setImgUrl(imgUrl);
		lesson.setOnlinetutorName(lessonName);
		lesson.setOnlinetutorDesc(onlinetutorDesc);
		// 修改直播
		lesson = updateGensee(startDateStr, endDateStr, lesson);
		lcmsOnlineLessonDao.save(lesson);
		// 修改直播期课程
		List<LcmsOnlineObject> objs = lcmsOnlineObjectDao.findByOnlinetutorId(lesson.getOnlinetutorId());
		for (LcmsOnlineObject o : objs) {
			o.setIsDeleted(Constants.ISDELETED);
			lcmsOnlineObjectDao.save(o);
		}
		lcmsOnlineLessonDao.deleteStudentByOnlinetutorId(lesson.getOnlinetutorId());
		if (newTcids.size() > 0) {
			for (String tcid : newTcids) {
				LcmsOnlineObject object = new LcmsOnlineObject();
				object.setCreatedBy(uid);
				object.setCreatedDt(now);
				object.setIsDeleted(Constants.NODELETED);
				object.setObjectId(tcid);
				object.setObjectType("3");
				object.setOnlineObjectId(UUIDUtils.random());
				object.setOnlinetutorId(lesson.getOnlinetutorId());
				lcmsOnlineObjectDao.save(object);
				lcmsOnlineLessonService.insertJoinStudent(lesson.getOnlinetutorId(), tcid, uid);
			}
		}

		return 1;
	}

	/**
	 * @param lesson
	 *            新直播对象
	 * @return 修改 成功的直播对象
	 * @throws ParseException
	 */
	private LcmsOnlineLesson updateGensee(String startDate, String invalidDate, LcmsOnlineLesson lesson) {

		// 添加北京展示互动科技接口/training/room/created调用
		String url = AppConfig.getProperty("roomModify_url");
		PostMethod postMethod = new PostMethod(url);
		HttpClient httpClient = new HttpClient();
		httpClient.getParams().setContentCharset("UTF-8");
		try {
			NameValuePair nvp = new NameValuePair("loginName", AppConfig.getProperty("gensee_loginName"));
			NameValuePair nvp1 = new NameValuePair("password", AppConfig.getProperty("gensee_password"));
			NameValuePair nvp2 = new NameValuePair("description", StringUtils.defaultString(lesson.getDescription()));
			NameValuePair nvp3 = new NameValuePair("startDate", startDate);
			NameValuePair nvp4 = new NameValuePair("invalidDate", invalidDate);
			NameValuePair nvp5 = new NameValuePair("Id", lesson.getSdkId());
			// 支持Web端学生加入
			NameValuePair nvp6 = new NameValuePair("webJoin", "true");
			// 暂不支持客户端端学生加入
			NameValuePair nvp7 = new NameValuePair("clientJoin", "true");
			// 默认用户口令
			NameValuePair nvp8 = new NameValuePair("teacherToken", lesson.getTeachertoken());
			NameValuePair nvp9 = new NameValuePair("studentToken", lesson.getStudenttoken());
			NameValuePair nvp10 = new NameValuePair("assistantToken", "111111");
			NameValuePair nvp11 = new NameValuePair("uiMode", "");
			NameValuePair nvp12 = new NameValuePair("subject", lesson.getSubject());

			postMethod.addParameter(nvp);
			postMethod.addParameter(nvp1);
			postMethod.addParameter(nvp2);
			postMethod.addParameter(nvp3);
			postMethod.addParameter(nvp4);
			postMethod.addParameter(nvp5);
			postMethod.addParameter(nvp6);
			postMethod.addParameter(nvp7);
			postMethod.addParameter(nvp8);
			postMethod.addParameter(nvp9);
			postMethod.addParameter(nvp10);
			postMethod.addParameter(nvp11);
			postMethod.addParameter(nvp12);

			httpClient.executeMethod(postMethod);
			if (postMethod.getStatusCode() == HttpStatus.SC_OK) {
				String message = postMethod.getResponseBodyAsString();
				OnlineLessonVo onlineLessonVo = JsonUtils.toObject(message, OnlineLessonVo.class);
				if (!"0".equals(onlineLessonVo.getCode())) {
					System.out.println("调用" + url + "接口失败!");
					lesson = null;
				}
			} else {
				System.out.println("调用" + url + "接口失败!");
				lesson = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			lesson = null;

		} finally {
			postMethod.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}
		return lesson;
	}

	@Override
	public List<GjtTermCourseinfo> getByCourseCodeAndOrg(String courseCode, String orgId) throws Exception {
		String hql = "select t from GjtTermCourseinfo t,GjtCourse gc,GjtGrade gg where t.courseId=gc.courseId and t.termId=gg.gradeId and t.xxId=:xxId and t.isDeleted='N' and gc.kch=:kch and gc.isDeleted='N' AND gg.startDate<=:startDate and gg.isDeleted='N'";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("kch", courseCode);
		params.put("xxId", orgId);
		params.put("startDate", DateUtils.getDateToString("2017-04-01 00:00:00"));
		List<GjtTermCourseinfo> list = commonDao.queryForList(hql, params, GjtTermCourseinfo.class);
		return list;
	}

	@Override
	public List<String> findTermCourseIdByNumber(String number) {
		return lcmsOnlinetutorInfoDao.findTermCourseIdByNumber(number);
	}

	@Override
	public LcmsOnlineLesson findOnlineLessonById(String id) {
		return lcmsOnlineLessonDao.findOne(id);
	}

	@Override
	public LcmsOnlineLesson findOnlineLessonByTutorId(String onlinetutorId) {
		return lcmsOnlineLessonDao.findOneByOnlinetutorId(onlinetutorId);
	}

	@Override
	public void saveLcmsOnlineLesson(LcmsOnlineLesson onlineLesson) {
		lcmsOnlineLessonDao.save(onlineLesson);
	}

	@Override
	public LcmsOnlinetutorInfo findOnlinetutorInfoById(String id) {
		return lcmsOnlinetutorInfoDao.findOne(id);
	}

}
