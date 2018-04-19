
/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.usermanage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.gzedu.xlims.common.ClobConvertUtil;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.ExcelService;
import com.gzedu.xlims.common.FileKit;
import com.gzedu.xlims.common.GsonUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.ImageCompressUtil;
import com.gzedu.xlims.common.ImgBase64Convert;
import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.Objects;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.ValidateUtil;
import com.gzedu.xlims.common.ZipFileUtil;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.eefile.EEFileUploadUtil;
import com.gzedu.xlims.common.exception.ServiceException;
import com.gzedu.xlims.common.gzdec.framework.util.DateUtil;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzdec.framework.util.WordTemplateUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.constants.OrgUtil;
import com.gzedu.xlims.dao.GjtCourseDao;
import com.gzedu.xlims.dao.ViewStudentInfoDao;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.edumanage.GjtRecResultDao;
import com.gzedu.xlims.dao.edumanage.GjtTeachPlanDao;
import com.gzedu.xlims.dao.edumanage.GjtTermCourseinfoDao;
import com.gzedu.xlims.dao.organization.GjtClassInfoDao;
import com.gzedu.xlims.dao.organization.GjtGradeDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.organization.GjtSchoolInfoDao;
import com.gzedu.xlims.dao.organization.GjtSpecialtyCollegeDao;
import com.gzedu.xlims.dao.organization.GjtSpecialtyDao;
import com.gzedu.xlims.dao.organization.GjtSpecialtyPlanDao;
import com.gzedu.xlims.dao.organization.GradeDaoImpl;
import com.gzedu.xlims.dao.signup.GjtSignupDataDao;
import com.gzedu.xlims.dao.usermanage.GjtClassStudentDao;
import com.gzedu.xlims.dao.usermanage.GjtStudentInfoDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.daoImpl.StudentInfoDaoImpl;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtClassStudent;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtRecResult;
import com.gzedu.xlims.pojo.GjtSchoolInfo;
import com.gzedu.xlims.pojo.GjtSignup;
import com.gzedu.xlims.pojo.GjtSignupData;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtSpecialtyPlan;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtSyncLog;
import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.pojo.GjtTermCourseinfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.ViewStudentInfo;
import com.gzedu.xlims.pojo.dto.StatisticsDto;
import com.gzedu.xlims.pojo.dto.StudentCourseDto;
import com.gzedu.xlims.pojo.dto.StudentSignupInfoDto;
import com.gzedu.xlims.pojo.dto.StudentSynthesizeInfoDto;
import com.gzedu.xlims.pojo.export.StudentExport;
import com.gzedu.xlims.pojo.flow.GjtFlowRecord;
import com.gzedu.xlims.pojo.opi.OpiStudchoose;
import com.gzedu.xlims.pojo.opi.OpiStudent;
import com.gzedu.xlims.pojo.opi.OpiStudentData;
import com.gzedu.xlims.pojo.opi.OpiTermChooseData;
import com.gzedu.xlims.pojo.opi.OpiTermClass;
import com.gzedu.xlims.pojo.opi.OpiTermClassData;
import com.gzedu.xlims.pojo.opi.RSimpleData;
import com.gzedu.xlims.pojo.status.SignupAuditStateEnum;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtSyncLogService;
import com.gzedu.xlims.service.flow.GjtFlowService;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.pcourse.PCourseServer;
import com.gzedu.xlims.service.remote.EnrollOrderInfo;
import com.gzedu.xlims.service.remote.EnrollRemoteService;
import com.gzedu.xlims.service.signup.GjtSignupService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;

import sun.misc.BASE64Decoder;

/**
 * 功能说明：学员管理实现接口
 *
 * @author 张伟文 zhangweiwen@eenet.com
 * @version 2.5
 * @Date 2016年4月26日
 * @since JDK1.7
 */

@Service
public class GjtStudentInfoServiceImpl implements GjtStudentInfoService {

	private static final Logger log = LoggerFactory.getLogger(GjtStudentInfoServiceImpl.class);

	@Autowired
	private ViewStudentInfoDao viewStudentInfoDao;
	@Autowired
	private GjtStudentInfoDao gjtStudentInfoDao;
	@Autowired
	private GjtSignupDataDao gjtSignupDataDao;
	@Autowired
	private GjtGradeDao gjtGradeDao;
	@Autowired
	private GjtClassInfoDao gjtClassInfoDao;
	@Autowired
	private GjtClassStudentDao gjtClassStudentDao;
	@Autowired
	private GjtCourseDao gjtCourseDao;
	@Autowired
	private GjtTeachPlanDao gjtTeachPlanDao;
	@Autowired
	private GjtRecResultDao gjtRecResultDao;

	@Autowired
	private GjtSpecialtyPlanDao gjtSpecialtyPlanDao;

	@Autowired
	private StudentInfoDaoImpl studentInfoDao;

	@Autowired
	private GradeDaoImpl gradeDao;

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	@Autowired
	private GjtSyncLogService gjtSyncLogService;

	@Autowired
	private GjtSignupService gjtSignupService;

	@Autowired
	private GjtFlowService gjtFlowService;

	@Autowired
	private GjtClassInfoService gjtClassInfoService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private CacheService cacheService;

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;

	@Autowired
	public GjtOrgDao gjtOrgDao;

	@Autowired
	public GjtSpecialtyDao gjtSpecialtyDao;

	@Autowired
	public GjtUserAccountDao gjtUserAccountDao;

	@Autowired
	public GjtSpecialtyCollegeDao gjtSpecialtyCollegeDao;

	@Autowired
	public GjtSchoolInfoDao gjtSchoolInfoDao;

	@Autowired
	private GjtTermCourseinfoDao gjtTermCourseinfoDao;

	@Autowired
	public CommonDao commonDao;

	@Autowired
	private EnrollRemoteService enrollRemoteService;

	@Autowired
	private PCourseServer pCourseServer;

	@Value("#{configProperties['yunyingCenterServer']}")
	private String YUNYING_CENTER_SERVER;

	@Override
	public List<GjtStudentInfo> queryBy(Map<String, Object> searchParams, Sort sort) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "createdDt");
		}
		Criteria<GjtStudentInfo> spec = new Criteria();
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.addAll(Restrictions.parse(searchParams));
		return gjtStudentInfoDao.findAll(spec, sort);
	}

	@Override
	public Page<StudentSignupInfoDto> queryStudentSignupInfoByPage(Map<String, Object> searchParams,
			PageRequest pageRequest) {
		Page<Map> result = studentInfoDao.findStudentSignupInfoByPage(searchParams, pageRequest);

		List<StudentSignupInfoDto> infoList = new ArrayList();
		for (Iterator<Map> iter = result.iterator(); iter.hasNext();) {
			Map info = iter.next();

			StudentSignupInfoDto infoDto = new StudentSignupInfoDto();
			infoDto.setStudentId((String) info.get("STUDENT_ID"));
			infoDto.setAccountId((String) info.get("ACCOUNT_ID"));
			infoDto.setXm((String) info.get("XM"));
			infoDto.setXh((String) info.get("XH"));
			infoDto.setXbm((String) info.get("XBM"));
			infoDto.setSfzh((String) info.get("SFZH"));
			infoDto.setSjh((String) info.get("SJH"));
			infoDto.setPycc((String) info.get("PYCC"));
			infoDto.setGradeName((String) info.get("GRADE_NAME"));
			infoDto.setSpecialtyName((String) info.get("ZYMC"));
			BigDecimal perfectStatus = (BigDecimal) info.get("PERFECT_STATUS");
			infoDto.setPerfectStatus(perfectStatus != null ? perfectStatus.intValue() : null);
			infoDto.setSignupAuditState(((Character) info.get("AUDIT_STATE")) + "");
			BigDecimal flowAuditOperatorRole = (BigDecimal) info.get("FLOW_AUDIT_OPERATOR_ROLE");
			infoDto.setFlowAuditOperatorRole(flowAuditOperatorRole != null ? flowAuditOperatorRole.intValue() : null);
			BigDecimal flowAuditState = (BigDecimal) info.get("FLOW_AUDIT_STATE");
			infoDto.setFlowAuditState(flowAuditState != null ? flowAuditState.intValue() : null);

			infoDto.setAvatar((String) info.get("AVATAR"));
			infoDto.setXjzt((String) info.get("XJZT"));
			infoDto.setXxzxName((String) info.get("ORG_NAME"));
			infoDto.setUserType((String) info.get("USER_TYPE"));
			infoDto.setYunyingSync((String) info.get("YUNYING_SYNC"));
			infoDto.setEesync((String) info.get("EESYNC"));
			infoList.add(infoDto);
		}
		return new PageImpl(infoList, pageRequest, result.getTotalElements());
	}

	@Override
	public Map<String, BigDecimal> countGroupbyAuditState(Map<String, Object> searchParams) {
		List<Map> countList = studentInfoDao.countGroupbyAuditState(searchParams);
		Map<String, BigDecimal> countMap = new HashMap<String, BigDecimal>();
		BigDecimal sum = new BigDecimal(0);
		for (Map arr : countList) {
			BigDecimal num = (BigDecimal) arr.get("STUDENT_NUM");
			countMap.put(arr.get("AUDIT_STATE").toString(), num);
			sum = sum.add(num);
		}
		countMap.put("", sum);
		return countMap;
	}

	@Override
	public Map<String, BigDecimal> countGroupbyPerfectStatus(Map<String, Object> searchParams) {
		List<Map> countList = studentInfoDao.countGroupbyPerfectStatus(searchParams);
		Map<String, BigDecimal> countMap = new HashMap<String, BigDecimal>();
		BigDecimal sum = new BigDecimal(0);
		for (Map arr : countList) {
			BigDecimal num = (BigDecimal) arr.get("STUDENT_NUM");
			countMap.put(arr.get("PERFECT_STATUS").toString(), num);
			sum = sum.add(num);
		}
		countMap.put("", sum);
		return countMap;
	}

	@Override
	public Map<String, BigDecimal> countGroupbyXjzt(Map<String, Object> searchParams) {
		List<Map> countList = studentInfoDao.countGroupbyXjzt(searchParams);
		Map<String, BigDecimal> countMap = new HashMap<String, BigDecimal>();
		BigDecimal sum = new BigDecimal(0);
		for (Map arr : countList) {
			BigDecimal num = (BigDecimal) arr.get("STUDENT_NUM");
			countMap.put(arr.get("XJZT").toString(), num);
			sum = sum.add(num);
		}
		countMap.put("", sum);
		return countMap;
	}

	/**
	 * 添加学员
	 *
	 * @param gjtStudentInfo
	 */
	@Override
	public Boolean saveEntity(GjtStudentInfo gjtStudentInfo) {
		GjtStudentInfo save = gjtStudentInfoDao.save(gjtStudentInfo);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 修改学员信息
	 */
	@Override
	public boolean updateEntity(GjtStudentInfo entity) {
		entity.setUpdatedDt(new Date());
		GjtStudentInfo save = gjtStudentInfoDao.save(entity);
		if (save != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 修改学员信息，并且刷新学籍资料缓存
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public boolean updateEntityAndFlushCache(final GjtStudentInfo entity) {
		Map<String, Object> info = studentInfoDao.findById(entity.getStudentId());
		entity.setRollCacheStatus(0);
		entity.setUpdatedDt(new Date());
		GjtStudentInfo save = gjtStudentInfoDao.save(entity);
		if (save != null) {
			// 通知招生平台更新学员信息，{手机号}
			if (StringUtils.isNotEmpty(entity.getSjh()) && !entity.getSjh().equals(info.get("SJH"))) {
				// 10s开始执行调度，通知招生
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						enrollRemoteService.syncUpdateUserInfo(entity.getStudentId());
					}
				}, 10000);
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public GjtStudentInfo queryById(String id) {
		return gjtStudentInfoDao.findOne(id);
	}

	@Override
	public GjtStudentInfo queryByAccountId(String accountId) {
		return gjtStudentInfoDao.queryByAccountId(accountId);
	}

	/**
	 * 假删除，修改isDelete
	 */
	@Override
	public Boolean deleteById(String[] ids) {
		if (ids != null) {
			for (String id : ids) {
				int i = gjtStudentInfoDao.deleteById(id, "Y");
			}
		}
		return true;
	}

	/**
	 * 真删除
	 *
	 * @param id
	 */
	@Override
	public boolean delete(String id) {
		gjtStudentInfoDao.delete(id);
		return true;
	}

	/**
	 * 是否启用
	 */
	@Override
	public Boolean updateIsEnabled(String id, String str) {
		int i = gjtStudentInfoDao.updateIsEnabled(id, str);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public HSSFWorkbook outInfoByExcel(List<StudentExport> list) {
		InputStream in = this.getClass().getResourceAsStream("/StudentInfo.xls");
		HSSFWorkbook book = null;
		try {
			book = new HSSFWorkbook(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		HSSFSheet sheet = book.getSheetAt(0);
		HSSFRow row = null;

		int rowIdx = 1, idx = 1;
		int colIdx = 0;

		for (StudentExport student : list) {

			row = sheet.createRow(rowIdx++);
			colIdx = 0;

			row.createCell(colIdx++).setCellValue(idx++);
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getGRADE_NAME()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getXH()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getXM()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getSEX()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getZYH()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getPYCC_NAME()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getRXNY()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getSFZH()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getXXMC()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getZYMC()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getGRADE_NAME()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getBJMC()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getLXDH()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getSJH()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getDZXX()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getTXDZ()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getXXZXMC()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getSYNC_STATUS()));
			row.createCell(colIdx++).setCellValue(new HSSFRichTextString(student.getEESYNC()));
		}

		return book;
	}

	@Override
	public Page<GjtStudentInfo> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);

		Map<String, String> orgIds = commonMapService.getOrgMapByOrgId(orgId);

		filters.put("gjtSchoolInfo.id", new SearchFilter("gjtSchoolInfo.id", Operator.IN, orgIds.keySet()));

		Specification<GjtStudentInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtStudentInfo.class);

		return gjtStudentInfoDao.findAll(spec, pageRequst);
	}

	/**
	 * 查询没有分班的学员
	 */
	@Override
	public Page<GjtStudentInfo> queryNoBrvbar(String schoolId, Map<String, Object> item, PageRequest pageRequst) {
		Map<String, String> orgMap = commonMapService.getOrgMapByOrgId(schoolId);
		StringBuffer str = new StringBuffer();
		int i = 0;
		for (Map.Entry<String, String> map : orgMap.entrySet()) {
			i++;
			str.append("'" + map.getKey() + "'");
			if (i < orgMap.entrySet().size()) {
				str.append(",");
			}
		}
		String hqlCount = "select count(g) ";
		String hqlhead = "select g ";
		String hql = "  from GjtStudentInfo g  left join g.gjtSignup sign left join  sign.gjtEnrollBatch batch "
				+ " left join g.gjtSchoolInfo org  left join g.gjtStudyCenter gsc  left join g.gjtSpecialty gsy where g.studentId not in "
				+ " (select c.gjtStudentInfo.studentId from GjtClassStudent c  where c.isDeleted='N') ";
		Map<String, String> map = new HashMap<String, String>();

		if (StringUtils.isNotBlank(str.toString())) {
			hql += "and g.gjtSchoolInfo.id in(" + str.toString() + ")";
		}
		// 年级
		String gradeId = (String) item.get("EQ_gradeId");
		if (StringUtils.isNotBlank(gradeId)) {
			hql += " and batch.gjtGrade.gradeId=:gradeId";
			map.put("gradeId", gradeId);
		}

		// 专业
		String gjtSpecialtyId = (String) item.get("EQ_gjtSpecialtyId");
		if (StringUtils.isNotBlank(gjtSpecialtyId)) {
			hql += " and gsy.specialtyId=:specialtyId";
			map.put("specialtyId", gjtSpecialtyId);
		}

		// 层次
		String pycc = (String) item.get("EQ_pycc");
		if (StringUtils.isNotBlank(pycc)) {
			hql += " and g.pycc=:pycc";
			map.put("pycc", pycc);
		}
		// 学号
		String xh = (String) item.get("EQ_xh");
		if (StringUtils.isNotBlank(xh)) {
			hql += " and g.xh=:xh";
			map.put("xh", xh);
		}
		// 姓名
		String xm = (String) item.get("LIKE_xm");
		if (StringUtils.isNotBlank(xm)) {
			hql += " and g.xm=:xm";
			map.put("xm", xm);
		}
		// 所在单位
		String scCo = (String) item.get("LIKE_scCo");
		if (StringUtils.isNotBlank(scCo)) {
			hql += " and g.scCo like :scCo";
			map.put("scCo", "%" + scCo + "%");
		}
		// 招生机构
		String orgId = (String) item.get("EQ_gjtOrgId");
		if (StringUtils.isNotBlank(orgId)) {
			hql += " and org.id=:orgId";
			map.put("orgId", orgId);
		}
		// 学习中心
		String gscId = (String) item.get("EQ_gjtStudyCenterId");
		if (StringUtils.isNotBlank(gscId)) {
			hql += " and gsc.id=:gscId";
			map.put("gscId", gscId);
		}

		Query queryTotal = em.createQuery(hqlCount + hql);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			queryTotal.setParameter(entry.getKey(), entry.getValue());
		}

		Query query = em.createQuery(hqlhead + hql);
		for (Map.Entry<String, String> entry : map.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
		query.setFirstResult(pageRequst.getOffset());
		query.setMaxResults(pageRequst.getPageSize());
		List<GjtStudentInfo> resultList = query.getResultList();
		long num = NumberUtils.toLong(queryTotal.getSingleResult().toString());
		Page<GjtStudentInfo> page = new PageImpl<GjtStudentInfo>(resultList, pageRequst, num);
		return page;
	}

	@Override
	public GjtStudentInfo queryByXh(String xh) {
		return gjtStudentInfoDao.findByXhAndIsDeleted(xh);
	}

	@Override
	public String queryStudentIdByXh(String xh) {
		return gjtStudentInfoDao.findStudentIdByXhAndIsDeleted(xh);
	}

	@Override
	public int updateStudentGrade(String id, String ebid) {
		return gjtStudentInfoDao.updateStudentGrade(id, ebid);
	}

	@Override
	public Page<GjtStudentInfo> queryByCourseIdAndStudyYearId(String courseId, int studyYearCode, boolean exists,
			Map<String, Object> searchParams, Pageable pageRequst) {

		String sql = "select s.* from GJT_STUDENT_INFO s "
				+ " inner join (select distinct specialty_id,grade_id,course_id from gjt_grade_specialty_plan"
				+ " where study_year_code = " + studyYearCode + " and course_id = '" + courseId + "') t "
				+ " on s.MAJOR = t.specialty_id  and s.nj = t.grade_id  where 1=1 ";

		if (exists) {
			sql += " and exists (select 1 from gjt_class_info a,gjt_class_student b "
					+ " where a.class_id = b.class_id and b.student_id = s.student_id  and a.study_year_code = "
					+ studyYearCode + " and a.course_id = '" + courseId + "' )";
		} else {
			sql += " and not exists (select 1 from gjt_class_info a,gjt_class_student b "
					+ " where a.class_id = b.class_id and b.student_id = s.student_id  and a.study_year_code = "
					+ studyYearCode + " and a.course_id = '" + courseId + "' )";
		}

		// 年级
		String gradeId = (String) searchParams.get("EQ_gradeId");
		if (StringUtils.isNotBlank(gradeId)) {
			sql += " and s.nj = '" + gradeId + "'";
		}

		// 专业
		String gjtSpecialtyId = (String) searchParams.get("EQ_gjtSpecialtyId");
		if (StringUtils.isNotBlank(gjtSpecialtyId)) {
			sql += " and s.major ='" + gjtSpecialtyId + "'";
		}

		// 学号
		String xh = (String) searchParams.get("EQ_xh");
		if (StringUtils.isNotBlank(xh)) {
			sql += " and xh='" + xh + "'";
		}
		// 姓名
		String xm = (String) searchParams.get("LIKE_xm");
		if (StringUtils.isNotBlank(xm)) {
			sql += " and xm like '%" + xm + "%'";
		}

		Query query = em.createNativeQuery(sql, GjtStudentInfo.class);

		Query queryTotal = em.createNativeQuery("select count(1) from (" + sql + ")");

		query.setFirstResult(pageRequst.getOffset());
		query.setMaxResults(pageRequst.getPageSize());

		List<GjtStudentInfo> resultList = query.getResultList();
		long num = NumberUtils.toLong(queryTotal.getSingleResult().toString());
		Page<GjtStudentInfo> page = new PageImpl<GjtStudentInfo>(resultList, pageRequst, num);
		return page;
	}

	@Override
	public List<StatisticsDto<Integer>> querySpecialtyRecruitstatisticsBy(Map<String, Object> searchParams) {
		List<Map> result = studentInfoDao.querySpecialtyRecruitstatisticsBy(searchParams, null);

		List<StatisticsDto<Integer>> statisticsDtoList = new ArrayList<StatisticsDto<Integer>>();
		for (Map info : result) {
			StatisticsDto<Integer> dto = new StatisticsDto<Integer>();
			dto.setName(info.get("NAME") + "");
			dto.setValue(NumberUtils.toInt(info.get("VALUE") + ""));
			statisticsDtoList.add(dto);
		}
		return statisticsDtoList;
	}

	@Override
	public List<StatisticsDto<Integer>> querySpecialtyRecruitstatisticsBy(String xxId,
			Map<String, Object> searchParams) {
		searchParams.put("xxId", xxId);
		return querySpecialtyRecruitstatisticsBy(searchParams);
	}

	@Override
	public Map<String, ViewStudentInfo> getViewStudentInfos(Set<String> xhs) {
		Map<String, ViewStudentInfo> m = new HashMap<String, ViewStudentInfo>();
		// １、验证学号和姓名
		int count = xhs.size() / 1000 + 1;
		Object[] list = (Object[]) xhs.toArray();
		for (int i = 0; i < count; i++) {
			Set<String> querys = new HashSet<String>();
			for (int j = 1000 * i; j < 1000 + 1000 * i && j < list.length; j++) {
				querys.add(String.valueOf(list[j]));
			}
			Map<String, SearchFilter> filters = new LinkedHashMap<String, SearchFilter>();
			filters.put("xh", new SearchFilter("xh", Operator.IN, querys));
			Specification<ViewStudentInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(),
					ViewStudentInfo.class);
			List<ViewStudentInfo> viewStudentInfos = viewStudentInfoDao.findAll(spec);
			for (ViewStudentInfo viewStudentInfo : viewStudentInfos) {
				m.put(viewStudentInfo.getXh(), viewStudentInfo);
			}
		}
		return m;
	}

	@Override
	public String exportNotRegInfoToPath(Map<String, Object> searchParams, Sort sort, String outputTmpPath) {
		if (sort == null) {
			sort = new Sort(Sort.Direction.DESC, "createdDt");
		}
		Criteria<GjtStudentInfo> spec = new Criteria();
		// 设置连接方式，如果是内连接可省略掉
		// spec.setJoinType("gjtStudyCenter", JoinType.LEFT);
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtSignup.isDeleted", Constants.BOOLEAN_NO, true));
		// EQ viewStudentInfo.gradeId 5a7eb15a82614101b55de86dad498041
		// EQ gjtSchoolInfo.id 2f5bfcce71fa462b8e1f65bcd0f4c632

		// 471
		// spec.addAll(Restrictions.parse(searchParams));
		spec.add(Restrictions.eq("xbm", searchParams.get("EQ_xbm"), true));// 专业
		String studyId = (String) searchParams.get("EQ_studyId");
		if (StringUtils.isNotBlank(studyId)) {
			// 学习中心
			List<String> orgList = gjtOrgDao.queryChildsByParentId(studyId);
			spec.add(Restrictions.in("gjtStudyCenter.id", orgList, true));
		} else {
			// 院校ID
			String xxId = (String) searchParams.get("schoolId");
			if (StringUtils.isNotBlank(xxId)) {
				List<String> orgList = gjtOrgDao.queryChildsByParentId(xxId);
				spec.add(Restrictions.in("gjtStudyCenter.id", orgList, true));
			}
		}
		spec.add(Restrictions.eq("viewStudentInfo.gradeId", searchParams.get("EQ_viewStudentInfo.gradeId"), true));// 年级
		spec.add(Restrictions.eq("gjtSpecialty.specialtyId", searchParams.get("EQ_specialtyId"), true));// 专业
		spec.add(Restrictions.eq("gjtSignup.auditState", SignupAuditStateEnum.通过.getValue() + "", true)); // 学籍资料审核通过的
		spec.add(Restrictions.eq("xjzt", "3", true));// 学员类型
		spec.add(Restrictions.ne("gjtSignup.charge", "2", true)); // 过滤掉未缴费的

		// 这两 值固定
		spec.add(Restrictions.eq("pycc", searchParams.get("EQ_pycc"), true));// 层次
		spec.add(Restrictions.like("sfzh", ObjectUtils.toString(searchParams.get("EQ_sfzh")), true));// 身份证
		spec.add(Restrictions.eq("userType", searchParams.get("EQ_userType"), true));// 学员类型
		spec.add(Restrictions.like("xm", ObjectUtils.toString(searchParams.get("LIKE_xm")), true));// 姓名
		spec.add(Restrictions.like("xh", ObjectUtils.toString(searchParams.get("EQ_xh")), true));// 学号

		List<GjtStudentInfo> result = gjtStudentInfoDao.findAll(spec, sort);

		try {
			Calendar cal = Calendar.getInstance();
			String folderName = "未注册学员学籍资料批量导出表_" + cal.getTimeInMillis();
			String outputPath = outputTmpPath + WebConstants.EXCEL_DOWNLOAD_URL + "roll" + File.separator
					+ DateFormatUtils.ISO_DATE_FORMAT.format(cal) + File.separator;
			String outputFolderPath = outputPath + File.separator + folderName;
			File folder = new File(outputFolderPath);
			if (!folder.exists()) {
				folder.mkdirs();
				log.info("创建文件 " + outputFolderPath);
			}
			String zps = outputFolderPath + File.separator + "所有照片";
			File zpFils = new File(zps);
			if (!zpFils.exists()) {
				zpFils.mkdirs();
			}

			HSSFWorkbook book = null;
			InputStream in = this.getClass().getResourceAsStream("/excel/signup/Template_SignupInfo.xlt");
			book = new HSSFWorkbook(in);
			HSSFSheet sheet = book.getSheetAt(0);
			HSSFRow row = null;

			int rowIdx = 1, idx = 1;
			int colIdx = 0;

			sheet.createFreezePane(0, 1); // 冻结列：冻结0列1行

			for (Iterator<GjtStudentInfo> iter = result.iterator(); iter.hasNext();) {
				GjtStudentInfo info = iter.next();

				row = sheet.createRow(rowIdx++);
				colIdx = 0;

				row.createCell(colIdx++).setCellValue(idx++);
				row.createCell(colIdx++).setCellValue(getCellVal(info.getXm()));
				row.createCell(colIdx++).setCellValue(getCellVal(
						cacheService.getCachedDictionaryName(CacheService.DictionaryKey.SEX, info.getXbm())));
				row.createCell(colIdx++).setCellValue(getCellVal(info.getPoliticsstatus()));
				row.createCell(colIdx++).setCellValue("身份证");// getCellVal(info.getCertificatetype())
				row.createCell(colIdx++).setCellValue(getCellVal(info.getSfzh()));
				Date csrq = DateUtil.parseDate(info.getCsrq());
				row.createCell(colIdx++)
						.setCellValue(getCellVal(csrq != null ? DateUtil.toString(csrq, "yyyy-MM-dd") : ""));
				row.createCell(colIdx++).setCellValue(getCellVal(cacheService
						.getCachedDictionaryName(CacheService.DictionaryKey.NATIONALITYCODE, info.getMzm())));
				String isonJob = cacheService.getCachedDictionaryName(CacheService.DictionaryKey.OCCUPATIONSTATUS,
						info.getIsonjob());
				isonJob = "工人";
				info.setIsonjob(isonJob);
				row.createCell(colIdx++).setCellValue(getCellVal(isonJob));
				String distributestatus = info.getDistributestatus();
				if (EmptyUtils.isEmpty(distributestatus) || distributestatus.length() < 3) {
					distributestatus = "省(市)";
				}
				info.setDistributestatus(distributestatus);
				row.createCell(colIdx++).setCellValue(getCellVal(distributestatus));
				row.createCell(colIdx++).setCellValue(getCellVal(cacheService
						.getCachedDictionaryName(CacheService.DictionaryKey.MARITALSTATUSCODE, info.getHyzkm())));
				row.createCell(colIdx++).setCellValue(getCellVal("自费"));
				String hkxz = cacheService.getCachedDictionaryName(CacheService.DictionaryKey.ACCOUNTSNATURECODE,
						info.getHkxzm());
				row.createCell(colIdx++).setCellValue(getCellVal(hkxz));
				// row.createCell(colIdx++).setCellValue(getCellVal())));

				row.createCell(colIdx++).setCellValue(getCellVal(
						StringUtils.isNotBlank(info.getNativeplace()) ? info.getNativeplace().split(" ")[0] : ""));
				row.createCell(colIdx++).setCellValue(getCellVal(StringUtils.isNotBlank(info.getNativeplace())
						? info.getNativeplace().split(" ").length >= 2 ? info.getNativeplace().split(" ")[1] : ""
						: ""));
				String hkszd = info.getHkszd();// 户口所在地
				if (EmptyUtils.isNotEmpty(hkszd)) {
					hkszd = hkszd.replaceAll("\\s", "");
				}
				row.createCell(colIdx++).setCellValue(getCellVal(hkszd));
				row.createCell(colIdx++).setCellValue(getCellVal(info.getSjh()));
				row.createCell(colIdx++).setCellValue(getCellVal(info.getLxdh()));
				row.createCell(colIdx++).setCellValue(getCellVal(info.getDzxx()));
				String address = "";
				try {
					/*
					 * if(info.getProvince() != null) { address +=
					 * Objects.toString(cacheService.getCachedDictionaryName(
					 * CacheService.DictionaryKey.PROVINCE, info.getProvince()),
					 * ""); if(info.getCity() != null) { address +=
					 * Objects.toString(cacheService.getCachedDictionaryName(
					 * CacheService.DictionaryKey.CITY.replace("${Province}",
					 * info.getProvince()), info.getCity()), ""); address +=
					 * Objects.toString(cacheService.getCachedDictionaryName(
					 * CacheService.DictionaryKey.AREA.replace("${Province}",
					 * info.getProvince()).replace("${City}", info.getCity()),
					 * info.getArea()), ""); } } address +=
					 * Objects.toString(info.getTxdz(), "");
					 */
					address = Objects.toString(info.getTxdz(), "");
				} catch (Exception e) {
					// System.out.println("改学生信息不完善 ");
				}
				row.createCell(colIdx++).setCellValue(getCellVal(address));
				row.createCell(colIdx++).setCellValue(getCellVal(info.getYzbm()));
				row.createCell(colIdx++).setCellValue(getCellVal(cacheService
						.getCachedDictionaryName(CacheService.DictionaryKey.TRAININGLEVEL, info.getPycc())));
				row.createCell(colIdx++).setCellValue(getCellVal(info.getGjtSpecialty().getZymc()));
				row.createCell(colIdx++).setCellValue(getCellVal(info.getIsgraduatebytv()));
				row.createCell(colIdx++).setCellValue(getCellVal(info.getExedulevel()));
				row.createCell(colIdx++).setCellValue(getCellVal(info.getExschool()));
				if (info.getExgraduatedtime() != null) {
					Date exgraduatedtime = DateUtil.parseDate(info.getExgraduatedtime());
					row.createCell(colIdx++).setCellValue(getCellVal(
							exgraduatedtime != null ? DateUtil.toString(exgraduatedtime, "yyyy-MM-dd") : ""));
				} else {
					row.createCell(colIdx++).setCellValue(getCellVal(""));
				}
				boolean isUndergraduateCourse = isUndergraduateCourse(info.getPycc());
				if (isUndergraduateCourse) {
					row.createCell(colIdx++).setCellValue(getCellVal(info.getExsubject()));
					row.createCell(colIdx++).setCellValue(getCellVal(info.getExsubjectkind()));
					row.createCell(colIdx++).setCellValue(getCellVal(info.getExedubaktype()));
					row.createCell(colIdx++).setCellValue(getCellVal(info.getExedumajor()));
					row.createCell(colIdx++).setCellValue(getCellVal(info.getExcertificatenum()));
					row.createCell(colIdx++).setCellValue(getCellVal(info.getExcertificateprove()));
					row.createCell(colIdx++).setCellValue(getCellVal(info.getExcertificateprovenum()));
					row.createCell(colIdx++).setCellValue(getCellVal(info.getExeduname()));
					row.createCell(colIdx++).setCellValue(getCellVal("身份证"));
					row.createCell(colIdx++).setCellValue(getCellVal(info.getExedunum()));
				} else {
					colIdx += 10;
				}

				GjtClassInfo classInfo = gjtClassInfoDao.findTeachClassByStudentId(info.getStudentId());
				row.createCell(colIdx++).setCellValue(getCellVal(classInfo != null ? classInfo.getBh() : ""));

				String xm = info.getXm().replace("*", "");
				String sfz = info.getSfzh().replace("*", "");
				String certificateFolderPath = outputFolderPath + File.separator + sfz + "（" + xm + "）";
				// 从缓存中获取学籍资料，再生成学籍资料文件夹
				String cacheRollCertificateFolderPath = getCachedSignupInfoPath(info, outputTmpPath);
				FileUtils.copyDirectory(new File(cacheRollCertificateFolderPath), new File(certificateFolderPath));
				File zpFile = new File(certificateFolderPath + File.separator + sfz + ".jpg");
				if (zpFile.exists()) {
					FileUtils.copyFile(zpFile, new File(zps + File.separator + sfz + ".jpg"));// 复制头像到单独文件夹
				}
			}

			// excel
			String excelFilePath = outputFolderPath + File.separator + folderName + ".xls";
			OutputStream out = new FileOutputStream(excelFilePath);
			book.write(out);
			out.flush();
			out.close();

			// zip
			String outputFilePath = outputPath + File.separator + folderName + ".zip";
			ZipFileUtil.zipDir(outputFolderPath, outputFilePath);
			FileKit.delFile(outputFolderPath);
			return outputFilePath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从缓存中获取用户的学籍资料信息路径
	 * 
	 * @param info
	 * @param outputTmpPath
	 * @return
	 */
	private String getCachedSignupInfoPath(GjtStudentInfo info, String outputTmpPath) {
		String cacheRollCertificateFolderPath = null;
		try {
			String rollCachePath = outputTmpPath + File.separator + "tmp" + File.separator + "roll-new-cache";
			File rollCacheFolder = new File(rollCachePath);
			if (!rollCacheFolder.exists()) {
				rollCacheFolder.mkdirs();
			}

			String studentId = info.getStudentId();
			String xm = info.getXm().replace("*", "");
			String sfz = info.getSfzh().replace("*", "");
			// 生成证件照文件夹
			cacheRollCertificateFolderPath = rollCachePath + File.separator + sfz + "（" + xm + "）_" + studentId;
			File cacheRollCertificateFolder = new File(cacheRollCertificateFolderPath);

			// 如果存在缓存中就从缓存中获取，反正生成了放到缓存在再获取就好
			if (!cacheRollCertificateFolder.exists() || info.getRollCacheStatus() == 0) {
				if (cacheRollCertificateFolder.exists())
					FileKit.delFile(cacheRollCertificateFolderPath); // 如果存在删除该学员学籍资料的文件夹
				cacheRollCertificateFolder.mkdirs();

				// boolean isUndergraduateCourse = "2".equals(info.getPycc()) ||
				// "8".equals(info.getPycc());
				Map<String, String> signupCopyData = gjtSignupService.getSignupCopyData(info.getStudentId());
				String zpPic = ""; // 蓝底照片 这里不初始化null 因为freemark对null处理不友好
				String signPic = ""; // 手写签名照 这里不初始化null 因为freemark对null处理不友好
				String sfz_z = ""; // 身份证正面图
				String sfz_f = ""; // 身份证反面图
				String byz_z = ""; // 毕业证
				String xlz = ""; // 学历证
				String jzz = ""; // 区域证明正面
				String jzzf = ""; // 区域证明反面
				String ygzm = ""; // 在读年级证明
				String jbrSign = ""; // 经办人签名
				String shrSign = ""; // 审核人签名
				Map<String, String> urls = new HashMap(); // 图片名称
				urls.put("zp", sfz);
				urls.put("sign", "签名");
				urls.put("sfz-z", "身份证正面");
				urls.put("sfz-f", "身份证反面");
				urls.put("byz-z", "毕业证正面");
				urls.put("jbrSign", "经办人签名");
				urls.put("xlz", "学历证");
				urls.put("xlzm", "学历证明");
				urls.put("jzz", "区域证明正面");
				urls.put("jzzf", "区域证明反面");
				urls.put("ygzm", "在读年级证明");
				urls.put("cns", "承诺书籍贯");
				for (Map.Entry<String, String> item : signupCopyData.entrySet()) {
					String key = item.getKey();
					if (item.getValue().indexOf("base64") == -1) {// 网络资源
						String targetUrl = item.getValue();
						String leftUrl = targetUrl.split("[?]")[0];// 去参数 ?
						String suffix = leftUrl.substring(leftUrl.lastIndexOf("."));// 取后缀
						if ("zp".equals(key)) {
							suffix = ".jpg";
						}
						String newPic = cacheRollCertificateFolderPath + File.separator + urls.get(key) + suffix;
						FileUtils.copyURLToFile(new URL(targetUrl), new File(newPic));
						// 图片压缩
						ImageCompressUtil.saveMinPhoto(newPic, newPic, 1000, 0.9d);

						if ("zp".equals(key)) {
							zpPic = ImgBase64Convert.getImageStr(
									cacheRollCertificateFolderPath + File.separator + urls.get(key) + suffix);
						} else if ("sign".equals(key)) {
							signPic = ImgBase64Convert.getImageStr(
									cacheRollCertificateFolderPath + File.separator + urls.get(key) + suffix);
							FileKit.delFile(newPic);
						} else if ("sfz-f".equals(key)) {
							sfz_f = ImgBase64Convert.getImageStr(
									cacheRollCertificateFolderPath + File.separator + urls.get(key) + suffix);
							FileKit.delFile(newPic);
						} else if ("sfz-z".equals(key)) {
							sfz_z = ImgBase64Convert.getImageStr(
									cacheRollCertificateFolderPath + File.separator + urls.get(key) + suffix);
							FileKit.delFile(newPic);
						} else if ("jzz".equals(key)) {
							jzz = ImgBase64Convert.getImageStr(
									cacheRollCertificateFolderPath + File.separator + urls.get(key) + suffix);
							FileKit.delFile(newPic);
						} else if ("jzzf".equals(key)) {
							jzzf = ImgBase64Convert.getImageStr(
									cacheRollCertificateFolderPath + File.separator + urls.get(key) + suffix);
							FileKit.delFile(newPic);
						} else if ("ygzm".equals(key)) {
							ygzm = ImgBase64Convert.getImageStr(
									cacheRollCertificateFolderPath + File.separator + urls.get(key) + suffix);
							FileKit.delFile(newPic);
						} else if ("jbrSign".equals(key)) {
							jbrSign = ImgBase64Convert.getImageStr(
									cacheRollCertificateFolderPath + File.separator + urls.get(key) + suffix);
							FileKit.delFile(newPic);
						} else if ("byz-z".equals(key)) {
							byz_z = ImgBase64Convert.getImageStr(
									cacheRollCertificateFolderPath + File.separator + urls.get(key) + suffix);
							FileKit.delFile(newPic);
						} else if ("xlz".equals(key)) {
							xlz = ImgBase64Convert.getImageStr(
									cacheRollCertificateFolderPath + File.separator + urls.get(key) + suffix);
							FileKit.delFile(newPic);
						}
					} else {// 图片资源流
						String suffix = ".jpg";
						String[] urlArr = item.getValue().split("base64,");
						String data = urlArr[0];
						String base64Url = urlArr[1];
						if (data.contains("png")) {
							suffix = ".png";
						}
						String newPic = cacheRollCertificateFolderPath + File.separator + urls.get(key) + suffix;
						ImgBase64Convert.generateImage(base64Url, newPic);// 保存base64为图片
						// 图片压缩
						ImageCompressUtil.saveMinPhoto(newPic, newPic, 1000, 0.9d);

						if ("zp".equals(key)) {
							zpPic = base64Url;
						} else if ("sign".equals(key)) {
							signPic = base64Url;
							FileKit.delFile(newPic);
						} else if ("sfz-f".equals(key)) {
							sfz_f = base64Url;
							FileKit.delFile(newPic);
						} else if ("sfz-z".equals(key)) {
							sfz_z = base64Url;
							FileKit.delFile(newPic);
						} else if ("jzz".equals(key)) {
							jzz = base64Url;
							FileKit.delFile(newPic);
						} else if ("jzzf".equals(key)) {
							jzzf = base64Url;
							FileKit.delFile(newPic);
						} else if ("ygzm".equals(key)) {
							ygzm = base64Url;
							FileKit.delFile(newPic);
						} else if ("jbrSign".equals(key)) {
							jbrSign = base64Url;
							FileKit.delFile(newPic);
						} else if ("byz-z".equals(key)) {
							byz_z = base64Url;
							FileKit.delFile(newPic);
						} else if ("xlz".equals(key)) {
							xlz = base64Url;
							FileKit.delFile(newPic);
						}
					}
				}
				// word
				// 根据院校获取审核人签名，按道理是谁审核拿谁的签名，可是产品需求不是这样的
				String userLoginAccount = OrgUtil
						.getSignupPersonApproving(info.getGjtSchoolInfo().getGjtOrg().getCode());
				GjtUserAccount userAccount = gjtUserAccountDao.findByLoginAccount(userLoginAccount);
				if (userAccount != null && StringUtils.isNotBlank(userAccount.getSignPhoto())) {
					String shrStr = cacheRollCertificateFolderPath + File.separator + "审核人.jpg";
					File shr = new File(shrStr);
					FileUtils.copyURLToFile(new URL(userAccount.getSignPhoto()), shr);
					shrSign = ImgBase64Convert.getImageStr(shrStr);
					shr.delete();
				}

				WordTemplateUtil.createWord(renderData(info, zpPic, signPic, jbrSign, shrSign), "word_registration.ftl",
						new FileOutputStream(
								cacheRollCertificateFolderPath + File.separator + "国家开放大学报名登记表_" + xm + ".doc"));
				Map data = new HashMap();
				data.put("sfz_z", sfz_z);
				data.put("sfz_f", sfz_f);
				data.put("byz_z", byz_z);
				data.put("xlz", xlz);
				boolean isOffsite = OrgUtil.isOffsite(info.getGjtSchoolInfo().getGjtOrg().getCode(), info.getSfzh());
				data.put("isOffsite", isOffsite);
				data.put("signupJzzType", info.getGjtSignup().getSignupJzzType());
				data.put("jzz", jzz);
				data.put("jzzf", jzzf);
				data.put("ygzm", ygzm);
				WordTemplateUtil.createWord(data, "word_identification.ftl",
						new FileOutputStream(cacheRollCertificateFolderPath + File.separator + "证件资料_" + xm + ".doc"));

				// 设置为已缓存
				gjtStudentInfoDao.updateRollSyncStatus(info.getStudentId(), 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cacheRollCertificateFolderPath;
	}

	public String outStuWord(String outputTmpPath, String id) {
		Calendar cal = Calendar.getInstance();
		String outputFolderPath = outputTmpPath + WebConstants.EXCEL_DOWNLOAD_URL + "roll" + File.separator
				+ DateFormatUtils.ISO_DATE_FORMAT.format(cal) + File.separator;
		File folder = new File(outputFolderPath);
		if (!folder.exists()) {
			folder.mkdirs();
			log.error("创建文件 " + outputFolderPath);
		}

		GjtStudentInfo info = gjtStudentInfoDao.findOne(id);
		String isonJob = cacheService.getCachedDictionaryName(CacheService.DictionaryKey.OCCUPATIONSTATUS,
				info.getIsonjob());
		isonJob = "工人";
		info.setIsonjob(isonJob);

		String xm = info.getXm().replace("*", "");
		String sfz = info.getSfzh().replace("*", "");
		String certificateFolderPath = outputFolderPath + File.separator + sfz + "（" + xm + "）";
		try {
			// 从缓存中获取学籍资料，再生成学籍资料文件夹
			String cacheRollCertificateFolderPath = getCachedSignupInfoPath(info, outputTmpPath);
			FileUtils.copyDirectory(new File(cacheRollCertificateFolderPath), new File(certificateFolderPath));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			String outputFilePath = outputFolderPath + File.separator + xm + "_" + cal.getTimeInMillis() + ".zip";
			ZipFileUtil.zipDir(certificateFolderPath, outputFilePath);
			FileKit.delFile(certificateFolderPath);
			return outputFilePath;
		}

	}

	/**
	 * 安全输出表格单元格的值
	 * 
	 * @param val
	 * @return
	 */
	private HSSFRichTextString getCellVal(String val) {
		HSSFRichTextString result = new HSSFRichTextString("");
		try {
			val = ObjectUtils.toString(val, "");
			result = new HSSFRichTextString(val);
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * TODO
	 * 
	 * @param info
	 *            学生对象
	 * @param zpPic
	 *            头像(base64)
	 * @param autograph
	 *            手写签名照(base64)
	 * @param jbrSign
	 *            经办人(base64)
	 * @param shrSign
	 *            审核人(base64)
	 * @return 返回一个可Word渲染的Map
	 */
	private Map renderData(GjtStudentInfo info, String zpPic, String autograph, String jbrSign, String shrSign) {
		String defaultValue = " ";
		String checkOk = "</w:t></w:r><w:r wsp:rsidR=\"00816BE9\"><w:sym w:font=\"Wingdings 2\" w:char=\"F052\"/></w:r><w:r><w:rPr><w:rFonts w:hint=\"fareast\"/></w:rPr><w:t>";
		String checkNo = "□";

		Map data = new HashMap();
		Map param = new HashMap();

		String gender = info.getXbm();// 性别

		data.put("shrSign", shrSign);

		data.put("gen2", checkNo);
		data.put("gen1", checkNo);
		if (EmptyUtils.isEmpty(gender)) {
			data.put("gen2", checkOk);
		} else if ("1".equals(gender)) {
			data.put("gen1", checkOk);
		} else {
			data.put("gen2", checkOk);
		}

		String birth = info.getCsrq();
		if (EmptyUtils.isNotEmpty(birth)) {
			try {
				birth = birth.substring(0, 4) + "-" + birth.substring(4, 6) + "-" + birth.substring(6, 8);
			} catch (Exception e) {
				// 存储格式不对
				birth = defaultValue;
			}
		} else {
			birth = defaultValue;
		}

		String marriage = ObjectUtils.toString(info.getHyzkm()); // 婚姻状态
		data.put("marr_unmarriage", checkNo);
		data.put("marr_dissociaton", checkNo);
		data.put("marr_marriaged", checkNo);
		data.put("marr_widowed", checkNo);
		data.put("marr_other", checkNo);
		if (EmptyUtils.isEmpty(marriage)) {// 其他
			data.put("marr_other", checkOk);
		} else if (marriage.equals("0")) {// 未婚
			data.put("marr_unmarriage", checkOk);
		} else if (marriage.equals("1")) {// 已婚
			data.put("marr_marriaged", checkOk);
		} else if (marriage.equals("2")) {// 离异
			data.put("marr_dissociaton", checkOk);
		} else if (marriage.equals("3")) {// 丧偶
			data.put("marr_widowed", checkOk);
		}
		data.put("household_city", checkNo);
		data.put("household_village", checkNo);
		String household = ObjectUtils.toString(info.getHkxzm(), defaultValue);// 户口性质码
																				// 字典
																				// 1-农村户口
																				// 2-城镇户口
		if ("1".equals(household)) { // 农村
			data.put("household_village", checkOk);
		} else { // 城镇
			data.put("household_city", checkOk);
		}
		data.put("graduatebytv_yes", checkNo);
		data.put("graduatebytv_not", checkNo);
		String isThat = info.getIsgraduatebytv();// 是否电大毕业
		if (EmptyUtils.isEmpty(isThat)) {

		} else if ("是".equals(isThat)) {
			data.put("graduatebytv_yes", checkOk);
		} else if ("否".equals(isThat)) {
			data.put("graduatebytv_not", checkOk);
		}

		data.put("studyTyp_2", checkOk); // 学习形式 2.5年
		data.put("studyTyp_3", checkNo);

		data.put("studyLevel_bachelor", checkNo); // 报读层次 2 8 本科
		data.put("studyLevel_college", checkNo);
		String studyLevel = info.getPycc();
		if (EmptyUtils.isNotEmpty(studyLevel)) {
			if (isUndergraduateCourse(studyLevel)) {
				data.put("studyLevel_bachelor", checkOk);
			} else {
				data.put("studyLevel_college", checkOk);
			}
		}

		data.put("baktype_self", checkNo); // 原学历学习类型
		data.put("baktype_unself", checkNo);
		String baktype = info.getExedubaktype();
		if (EmptyUtils.isNotEmpty(baktype)) {
			if ("自考".equals(baktype)) {
				data.put("baktype_self", checkOk);
			} else {
				data.put("baktype_unself", checkOk);
			}
		}

		GjtSpecialty specialty = info.getGjtSpecialty();
		if (EmptyUtils.isNotEmpty(specialty))// 报读专业 志愿
			data.put("major1", ObjectUtils.toString(specialty.getZymc(), defaultValue));
		else
			data.put("major1", defaultValue);
		String major2 = ObjectUtils.toString(info.getMajor2(), defaultValue);
		if (EmptyUtils.isNotEmpty(major2)) {
			try {
				major2 = gjtSpecialtyDao.findOne(major2).getZymc();
			} catch (Exception e) {
				major2 = defaultValue;
			}
		}
		data.put("major2", major2);
		data.put("center", "实验学院");
		data.put("household",
				EmptyUtils.isNotEmpty(info.getHkszd()) ? info.getHkszd().replaceAll("\\s", "") : defaultValue);// 户口所在地
		String nativePlace = info.getNativeplace();
		if (EmptyUtils.isNotEmpty(nativePlace)) {
			if (nativePlace.indexOf("黑龙江") > -1) {
				nativePlace = "黑龙江";
			} else if (nativePlace.indexOf("内蒙古") > -1) {
				nativePlace = "内蒙古";
			} else {
				if (nativePlace.length() >= 2)
					nativePlace = nativePlace.substring(0, 2);
			}
		}
		data.put("nativePlace", ObjectUtils.toString(nativePlace, defaultValue));// 籍贯
		data.put("xm", info.getXm()); // 姓名
		data.put("pic", zpPic);// 蓝底照片
		data.put("politicsstatus", ObjectUtils.toString(info.getPoliticsstatus(), defaultValue));// 政治面貌
		data.put("TypeId", checkOk);// 证件类型 身份证 军人证 其他
		data.put("TypeSoldier", checkNo);
		data.put("TypeOther", checkNo);
		data.put("idNumber", ObjectUtils.toString(info.getSfzh(), defaultValue));// 证件号
		data.put("birth", birth);// 出生日期
		data.put("nation", ObjectUtils.toString(info.getNation(), defaultValue));// 民族
		data.put("isonjob", ObjectUtils.toString(info.getIsonjob(), defaultValue));// 在职情况
		data.put("mobile", ObjectUtils.toString(info.getSjh(), defaultValue));// 手机号
		data.put("phone", ObjectUtils.toString(info.getLxdh(), defaultValue));// 固定电话
		data.put("email", ObjectUtils.toString(info.getDzxx(), defaultValue));// 电子邮箱
		data.put("address", ObjectUtils.toString(info.getTxdz(), defaultValue));// 通讯地址
		data.put("postCode", ObjectUtils.toString(info.getYzbm(), defaultValue));// 邮编
		data.put("exedu_name", ObjectUtils.toString(info.getExeduname(), defaultValue));// 原学历姓名
		String exedu_level = ObjectUtils.toString(info.getExedulevel(), defaultValue);
		data.put("exedu_level", exedu_level.length() > 11 ? exedu_level.substring(0, 11) : exedu_level);// 原学历层次
		data.put("exedu_subject", ObjectUtils.toString(info.getExsubject(), defaultValue));// 原学科
		data.put("exedu_school", ObjectUtils.toString(info.getExschool(), defaultValue));// 原毕业院校
		data.put("exedu_graduatedtime", ObjectUtils.toString(info.getExgraduatedtime(), defaultValue));// 原毕业时间
		data.put("exedu_subjectkind", ObjectUtils.toString(info.getExsubjectkind(), defaultValue));// 原学科门类
		data.put("exedu_prove", ObjectUtils.toString(info.getExcertificateprove(), defaultValue));// 原学历证明材料
		data.put("exedu_provenum", ObjectUtils.toString(info.getExcertificateprovenum(), defaultValue));// 证明材料编号
		data.put("exedu_major", ObjectUtils.toString(info.getExedumajor(), defaultValue));// 原学历所学专业
		data.put("exedu_certificate", ObjectUtils.toString(info.getExcertificatenum(), defaultValue));// 原学历毕业证书编号
		data.put("exedu_num", ObjectUtils.toString(info.getExedunum(), defaultValue));// 原学历证件号码
		data.put("exedu_testcode", ObjectUtils.toString(info.getTestscore(), defaultValue));// 入学测试成绩
		data.put("autograph", autograph);// 手写签名照片
		data.put("jbrSign", jbrSign);// 经办人签名
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = 1 + c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		data.put("year", year + "");// 单独整型显示的是 2,107 所以转字符
		data.put("month", month);
		data.put("day", day);
		List list = new ArrayList();
		list.add(data);
		param.put("list", list);
		return param;
	}

	@Override
	public boolean enteringSignup(String studentId, String updatedBy) {
		gjtStudentInfoDao.enteringSignup(studentId, updatedBy, new Date());
		return true;
	}

	@Override
	public GjtGrade getGjtGradeByStudentId(String studentId) {
		ViewStudentInfo info = viewStudentInfoDao.findByStudentId(studentId);
		return gjtGradeDao.findOne(info.getGradeId());
	}

	@Override
	public StudentSignupInfoDto getStudentSignupInfoById(String studentId) {
		Map<String, Object> info = studentInfoDao.getStudentSignupInfoById(studentId);

		StudentSignupInfoDto infoDto = new StudentSignupInfoDto();
		infoDto.setStudentId((String) info.get("STUDENT_ID"));
		infoDto.setAccountId((String) info.get("ACCOUNT_ID"));
		infoDto.setXm((String) info.get("XM"));
		infoDto.setXh((String) info.get("XH"));
		infoDto.setXbm((String) info.get("XBM"));
		infoDto.setCertificatetype(info.get("CERTIFICATETYPE") != null ? (String) info.get("CERTIFICATETYPE") : "身份证");
		infoDto.setSfzh((String) info.get("SFZH"));
		infoDto.setNativeplace((String) info.get("NATIVEPLACE"));
		infoDto.setMzm((String) info.get("MZM"));
		infoDto.setPoliticsstatus((String) info.get("POLITICSSTATUS"));
		infoDto.setHyzkm((String) info.get("HYZKM"));
		infoDto.setHkxzm((String) info.get("HKXZM"));
		infoDto.setCsrq((String) info.get("CSRQ"));
		infoDto.setIsonjob((String) info.get("ISONJOB"));
		infoDto.setHkszd((String) info.get("HKSZD"));
		infoDto.setSjh((String) info.get("SJH"));
		infoDto.setLxdh((String) info.get("LXDH"));
		infoDto.setDzxx((String) info.get("DZXX"));
		infoDto.setProvince((String) info.get("PROVINCE"));
		infoDto.setCity((String) info.get("CITY"));
		infoDto.setArea((String) info.get("AREA"));
		infoDto.setTxdz((String) info.get("TXDZ"));
		infoDto.setYzbm((String) info.get("YZBM"));
		infoDto.setScCo((String) info.get("SC_CO"));
		infoDto.setScCoAddr((String) info.get("SC_CO_ADDR"));
		infoDto.setJobPost((String) info.get("JOB_POST"));
		infoDto.setGradeName((String) info.get("GRADE_NAME"));
		infoDto.setGradeId((String) info.get("GRADE_ID"));
		infoDto.setYearName((String) info.get("YEAR_NAME"));
		infoDto.setSpecialtyName((String) info.get("ZYMC"));
		infoDto.setSpecialtyId((String) info.get("SPECIALTY_ID"));
		infoDto.setGradeSpecialtyId((String) info.get("GRADE_SPECIALTY_ID"));
		infoDto.setRuleCode((String) info.get("RULE_CODE"));
		infoDto.setPycc((String) info.get("PYCC"));
		infoDto.setAcademic("2.5年");
		infoDto.setXxmc((String) info.get("XXMC"));
		infoDto.setTeachClassName((String) info.get("USERCLASS"));
		infoDto.setXxzxName((String) info.get("ORG_NAME"));
		infoDto.setExedulevel((String) info.get("EXEDULEVEL"));
		infoDto.setXxzxCode((String) info.get("CODE"));
		infoDto.setExschool((String) info.get("EXSCHOOL"));
		infoDto.setExgraduatedtime((String) info.get("EXGRADUATEDTIME"));
		infoDto.setExsubject((String) info.get("EXSUBJECT"));
		infoDto.setExsubjectkind((String) info.get("EXSUBJECTKIND"));
		infoDto.setExedubaktype((String) info.get("EXEDUBAKTYPE"));
		infoDto.setExedumajor((String) info.get("EXEDUMAJOR"));
		infoDto.setExcertificatenum((String) info.get("EXCERTIFICATENUM"));
		infoDto.setExcertificateprove((String) info.get("EXCERTIFICATEPROVE"));
		infoDto.setExcertificateprovenum((String) info.get("EXCERTIFICATEPROVENUM"));
		infoDto.setExeduname((String) info.get("EXEDUNAME"));
		infoDto.setExeducertificate(
				info.get("EXEDUCERTIFICATE") != null ? (String) info.get("EXEDUCERTIFICATE") : "身份证");
		infoDto.setExedunum((String) info.get("EXEDUNUM"));
		infoDto.setIsgraduatebytv((String) info.get("ISGRADUATEBYTV"));
		BigDecimal perfectStatus = (BigDecimal) info.get("PERFECT_STATUS");
		infoDto.setPerfectStatus(perfectStatus != null ? perfectStatus.intValue() : null);
		infoDto.setIsUndergraduateCourse(isUndergraduateCourse((String) info.get("PYCC")) ? 1 : 0);

		infoDto.setAvatar((String) info.get("AVATAR"));
		infoDto.setXjzt((String) info.get("XJZT"));
		infoDto.setUserType((String) info.get("USER_TYPE"));
		return infoDto;
	}

	@Override
	public Map importStuInfo(File targetFile, String path) {
		Map result = new HashMap();
		String[] heads = new String[] { "学号", "姓名", "身份证号", "层次", "学员类型", "学籍状态", "新学号", "学籍注册时间" };
		String[] dbNames = new String[] { "xh", "xm", "sfzh", "pycc", "userType", "studentNumberStatus",
				"newstudentNumber", "rollRegisterDt" };
		String[] etitle = new String[] { "学号", "姓名", "身份证号", "层次", "学员类型", "学籍状态", "新学号", "学籍注册时间", "失败原因" };
		String[] edbNames = new String[] { "xh", "xm", "sfzh", "pycc", "userType", "studentNumberStatus",
				"newstudentNumber", "rollRegisterDt", "msg" };

		List<CacheService.Value> trainingLevels = cacheService
				.getCachedDictionary(CacheService.DictionaryKey.TRAININGLEVEL);//// 层次
		List<CacheService.Value> stuTypes = cacheService
				.getCachedDictionary(CacheService.DictionaryKey.USER_STUDENT_TYPE);// 学员类型
		List<CacheService.Value> stuNumStates = cacheService
				.getCachedDictionary(CacheService.DictionaryKey.STUDENTNUMBERSTATUS);// 学籍状态

		List<Map> successList = new ArrayList<Map>();
		List<Map> errorList = new ArrayList<Map>();
		List<String[]> datas = null;
		try {
			datas = ExcelUtil.readAsStringList(targetFile, 2, heads.length);
			String[] dataTitles = datas.remove(0);// 标题校验
			for (int i = 0; i < heads.length; i++) {
				if (!dataTitles[i].trim().equals(heads[i])) {
					result.put("exception", "请下载正确表格模版填写");
					return result;
				}
			}
		} catch (Exception e) {
			result.put("exception", "请下载正确表格模版填写");
			return result;
		}
		try {
			// 数据校验
			for (String[] data : datas) {
				String pycc = "", userType = "", stuNumState = "";
				for (CacheService.Value trainingLevel : trainingLevels) {
					if (ObjectUtils.toString(trainingLevel.getName()).equals(data[3])) {
						pycc = ObjectUtils.toString(trainingLevel.getCode());
						break;
					}
				}

				for (CacheService.Value stuType : stuTypes) {
					if (ObjectUtils.toString(stuType.getName()).equals(data[4])) {
						userType = ObjectUtils.toString(stuType.getCode());
					}
				}

				for (CacheService.Value state : stuNumStates) {
					if (ObjectUtils.toString(state.getName()).equals(data[5])) {
						stuNumState = ObjectUtils.toString(state.getCode());
					}
				}

				if (EmptyUtils.isEmpty(data[0])) {
					errorList.add(addMap(data, "学号不能为空"));
					continue;
				}
				if (EmptyUtils.isEmpty(data[1])) {
					errorList.add(addMap(data, "姓名不能为空"));
					continue;
				}
				if (EmptyUtils.isEmpty(data[2])) {
					errorList.add(addMap(data, "身份证不能为空"));
					continue;
				}
				if (EmptyUtils.isEmpty(data[3]) || EmptyUtils.isEmpty(pycc)) {
					errorList.add(addMap(data, "层次不正确"));
					continue;
				}
				if (EmptyUtils.isNotEmpty(data[4])) {
					if (EmptyUtils.isEmpty(userType)) {
						errorList.add(addMap(data, "学员类型不正确"));
						continue;
					}
				}
				if (EmptyUtils.isNotEmpty(data[5])) {
					if (EmptyUtils.isEmpty(stuNumState)) {
						errorList.add(addMap(data, "学籍状态不正确"));
						continue;
					}
				}
				Map<String, String> param = addMap(data, "");
				int i = updateStuInfo(param, pycc, userType, stuNumState, data[6], data[7]);
				if (i == 1) {
					successList.add(param);
				} else {
					param.put("msg", "学员不存在 请核对必填项");
					errorList.add(param);
				}

			}

			// 保存失败/成功列表
			String successFile = ExcelService.renderExcelHadTitle(successList, Arrays.asList(heads),
					Arrays.asList(dbNames), "导入成功列表", path);
			String errorFile = ExcelService.renderExcelHadTitle(errorList, Arrays.asList(etitle),
					Arrays.asList(edbNames), "导入失败列表", path);
			result.put("all_num", successList.size() + errorList.size());
			result.put("success_num", successList.size());
			result.put("error_num", errorList.size());
			result.put("success_file", successFile);
			result.put("error_file", errorFile);
			result.put("msg", "success");
		} catch (Exception e) {
			result.put("msg", "error");
			result.put("exception", e.getMessage());
		}
		return result;
	}

	@Override
	public boolean perfectSignupAndCertificateInfo(String studentId) {
		GjtStudentInfo info = this.queryById(studentId);
		int perfectStatus = info.getPerfectStatus(); // 学籍资料完善状态
		boolean isUndergraduateCourse = isUndergraduateCourse(info.getPycc()); // isUndergraduateCourse
																				// 培养层次是否为本科
		// 0-未完善，进入第一步标识-确认个人信息
		boolean pass0 = true;
		pass0 = pass0 && StringUtils.isNotBlank(info.getNativeplace()); // 籍贯
		pass0 = pass0 && StringUtils.isNotBlank(info.getMzm()); // 民族码
		pass0 = pass0 && StringUtils.isNotBlank(info.getPoliticsstatus()); // 政治面貌
		pass0 = pass0 && StringUtils.isNotBlank(info.getHyzkm()); // 婚姻状态码
		pass0 = pass0 && StringUtils.isNotBlank(info.getHkxzm()); // 户口性质码
		// 则通过身份证获取出生日期
		if (info.getSfzh().length() == 18) {
			if (StringUtils.isBlank(info.getCsrq())) {
				info.setCsrq(info.getSfzh().substring(6, 14));
			}
		}
		pass0 = pass0 && StringUtils.isNotBlank(info.getCsrq()); // 出生日期
		pass0 = pass0 && StringUtils.isNotBlank(info.getIsonjob()); // 在职状况
		pass0 = pass0 && StringUtils.isNotBlank(info.getHkszd()); // 户籍所在地
		if (pass0) {
			// gjtStudentInfoDao.perfectSignup(studentId);
			// 由于直接执行sql、hql语句不刷新hibernate的findOne缓存所以必须使用hibernate的save方法更新
			perfectStatus = perfectStatus == 0 ? 2 : perfectStatus;
		}
		// 2-进入第二步标识-确认通讯信息
		boolean pass2 = true;
		pass2 = pass2 && StringUtils.isNotBlank(info.getSjh()); // 手机号
		// pass2 = pass2 && StringUtils.isNotBlank(info.getLxdh()); // 固定电话
		pass2 = pass2 && StringUtils.isNotBlank(info.getDzxx()); // 邮箱
		pass2 = pass2 && StringUtils.isNotBlank(info.getTxdz()); // 通信地址
		pass2 = pass2 && StringUtils.isNotBlank(info.getYzbm()); // 邮编
		pass2 = pass2 && StringUtils.isNotBlank(info.getScCo()); // 所在单位
		pass2 = pass2 && StringUtils.isNotBlank(info.getScCoAddr()); // 单位地址
		pass2 = pass2 && StringUtils.isNotBlank(info.getGjtSignup().getJobPost()); // 岗位职务
		if (pass2) {
			// gjtStudentInfoDao.perfectCommunication(studentId);
			// 由于直接执行sql、hql语句不刷新hibernate的findOne缓存所以必须使用hibernate的save方法更新
			perfectStatus = perfectStatus == 2 ? 3 : perfectStatus;
		}
		// 3-进入第三步标识-确认报读信息
		boolean pass3 = true;
		// ...
		if (pass3) {
			// gjtStudentInfoDao.perfectRead(studentId);
			// 由于直接执行sql、hql语句不刷新hibernate的findOne缓存所以必须使用hibernate的save方法更新
			perfectStatus = perfectStatus == 3 ? 4 : perfectStatus;
		}
		// 4-进入第四步标识-确认原最高学历
		boolean pass4 = true;
		pass4 = pass4 && StringUtils.isNotBlank(info.getExedulevel()); // 原学历层次
		pass4 = pass4 && StringUtils.isNotBlank(info.getExschool()); // 原毕业学校
		if (!"42".equals(info.getUserType())) { // 电大续读生，以下选项不需要填写
			pass4 = pass4 && StringUtils.isNotBlank(info.getExgraduatedtime()); // 毕业时间
			pass4 = pass4 && StringUtils.isNotBlank(info.getIsgraduatebytv()); // 是否电大毕业
			if (isUndergraduateCourse) {
				pass4 = pass4 && StringUtils.isNotBlank(info.getExsubject()); // 原学科
				pass4 = pass4 && StringUtils.isNotBlank(info.getExsubjectkind()); // 原学科门类
				pass4 = pass4 && StringUtils.isNotBlank(info.getExedubaktype()); // 原学历学习类型
				pass4 = pass4 && StringUtils.isNotBlank(info.getExedumajor()); // 原学历所学专业
				pass4 = pass4 && StringUtils.isNotBlank(info.getExcertificatenum()); // 原学历毕业证书编号
				pass4 = pass4 && StringUtils.isNotBlank(info.getExcertificateprove()); // 原学历证明材料
				pass4 = pass4 && StringUtils.isNotBlank(info.getExcertificateprovenum()); // 原学历证明材料编号
				pass4 = pass4 && StringUtils.isNotBlank(info.getExeduname()); // 原学历姓名
				pass4 = pass4 && StringUtils.isNotBlank(info.getExedunum()); // 原学历证件号码
			}
		}
		if (pass4) {
			// gjtStudentInfoDao.perfectEducation(studentId);
			// 由于直接执行sql、hql语句不刷新hibernate的findOne缓存所以必须使用hibernate的save方法更新
			perfectStatus = perfectStatus == 4 ? 5 : perfectStatus;
		}

		// ------------------------------- 华丽丽的分割线 下面是校验证件资料
		// ------------------------------- //

		List<GjtSignupData> dataList = gjtSignupDataDao.findByStudentId(studentId);
		Map<String, String> dataMap = new HashMap<String, String>(dataList.size());
		for (GjtSignupData data : dataList) {
			dataMap.put(data.getFileType(),
					StringUtils.isNotBlank(data.getUrlNew()) ? data.getUrlNew() : data.getUrl());
		}

		// 5-进入第五步标识-确认证件信息
		boolean pass5 = dataList.size() > 0;
		String certs = "";
		if ("11".equals(info.getUserType()) || "12".equals(info.getUserType())) { // 正式生/正式跟读生
			certs = "zp,sfz-z,sfz-f";

			String byzz = dataMap.get("byz-z");
			String xlz = dataMap.get("xlz");
			String dzzch = dataMap.get("dzzch");
			String xsz = dataMap.get("xsz");
			String xlzm = dataMap.get("xlzm");
			String cns = dataMap.get("cns");
			if (isUndergraduateCourse) {
				// 本科-毕业证件类型，默认0 0-毕业证，1-毕业电子注册号
				if (info.getGjtSignup().getSignupByzType() == null || info.getGjtSignup().getSignupByzType() == 0) {
					if (StringUtils.isBlank(byzz) || StringUtils.isBlank(xlz)) {
						pass5 = false;
					}
				} else {
					if (StringUtils.isBlank(dzzch) || StringUtils.isBlank(xsz)) {
						pass5 = false;
					}
				}
			} else {
				// 专科-最高学历证明类型，默认0 0-毕业证复印件，1-学历证明+专科承诺书
				if (info.getGjtSignup().getZgxlRadioType() == null || info.getGjtSignup().getZgxlRadioType() == 0) {
					if (StringUtils.isBlank(byzz)) {
						pass5 = false;
					}
				} else {
					if (StringUtils.isBlank(xlzm) || StringUtils.isBlank(cns)) {
						pass5 = false;
					}
				}
			}
		} else if ("42".equals(info.getUserType())) { // 电大续读生
			certs = "zp,sfz-z,sfz-f,xsz,cjd";
		} else if ("51".equals(info.getUserType())) { // 外校预科生
			certs = "zp,sfz-z,sfz-f,cjd,lqmc,yjbyszm,ykcns";
		}

		if (pass5) {
			for (String key : certs.split(",")) {
				String url = dataMap.get(key);
				if (StringUtils.isBlank(url)) {
					pass5 = false;
					break;
				}
			}
		}
		boolean isOffsite = OrgUtil.isOffsite(info.getGjtSchoolInfo().getGjtOrg().getCode(), info.getSfzh());
		if (pass5 && isOffsite) {
			String jzz = dataMap.get("jzz");
			String jzzf = dataMap.get("jzzf");
			String ygzm = dataMap.get("ygzm");
			// 非广东户口证件类型，默认0 0-居住证 1-在读年级证明
			if (info.getGjtSignup().getSignupJzzType() == null || info.getGjtSignup().getSignupJzzType() == 0) {
				if (StringUtils.isBlank(jzz) || StringUtils.isBlank(jzzf)) {
					pass5 = false;
				}
			} else {
				if (StringUtils.isBlank(ygzm)) {
					pass5 = false;
				}
			}
		}
		if (pass5) {
			// gjtStudentInfoDao.perfectCertificate(studentId);
			// 由于直接执行sql、hql语句不刷新hibernate的findOne缓存所以必须使用hibernate的save方法更新
			perfectStatus = perfectStatus == 5 ? 6 : perfectStatus;
		}
		// 6-进入第六步标识-确认签名
		boolean pass6 = true;
		String sign = dataMap.get("sign");
		if (StringUtils.isBlank(sign)) {
			pass6 = false;
		}
		if (pass6) {
			// int resultNum = gjtStudentInfoDao.perfectSign(studentId);
			// 由于直接执行sql、hql语句不刷新hibernate的findOne缓存所以必须使用hibernate的save方法更新
			perfectStatus = perfectStatus == 6 ? 1 : perfectStatus;
		}
		info.setPerfectStatus(perfectStatus);
		gjtStudentInfoDao.save(info);
		return true;
	}

	/** 自定义数组转MAP */
	private Map<String, String> addMap(String[] data, String msg) {
		Map<String, String> re = new HashMap();
		if (EmptyUtils.isNotEmpty(msg)) {
			re.put("msg", msg);
		}
		re.put("xh", data[0]);
		re.put("xm", data[1]);
		re.put("sfzh", data[2]);
		re.put("pycc", data[3]);
		re.put("userType", data[4]);
		re.put("studentNumberStatus", data[5]);
		re.put("newStudentNumber", data[6]);
		re.put("rollRegisterDt", data[7]);
		return re;
	}

	/** TODO 列表导出 */
	public String outStudentSignupInfo(Map<String, Object> searchParams, String path) {
		List<Map> list = studentInfoDao.findStudentSignupInfo(searchParams);
		String[] titles = new String[] { "姓名", "性别", "学号", "手机号", "身份证号", "层次", "年级", "专业", "学习中心", "学籍状态", "资料审批",
				"学员类型", "工作单位", "邮箱", "教材邮寄地址", "班级名称（教学班）", "班主任" };// "头像",
		String[] dbNames = new String[] { "XM", "XBM", "XH", "SJH", "SFZH", "PYCC", "GRADE_NAME", "ZYMC", "ORG_NAME",
				"XJZT", "AUDIT_STATE", "USER_TYPE", "SC_CO", "DZXX", "ADDRESS", "TEACH_CLASS_NAME", "HEADTEACHER" };// "AVATAR",
		// 字典类型转化
		// 0：不通过；1：通过；3.待审核；
		List datas = new ArrayList();
		for (Map<String, String> m : list) {
			m.put("XBM", cacheService.getCachedDictionaryName("Sex", m.get("XBM")));
			m.put("PYCC", cacheService.getCachedDictionaryName("TrainingLevel", m.get("PYCC")));
			m.put("XJZT", cacheService.getCachedDictionaryName("StudentNumberStatus", m.get("XJZT")));
			m.put("USER_TYPE", cacheService.getCachedDictionaryName("USER_TYPE", m.get("USER_TYPE")));
			String oldAuditState = ObjectUtils.toString(m.get("AUDIT_STATE"));
			if ("0".equals(oldAuditState)) {
				m.put("AUDIT_STATE", "不通过");
			} else if ("1".equals(oldAuditState)) {
				m.put("AUDIT_STATE", "通过");
			} else {
				m.put("AUDIT_STATE", "未审核");
			}
			String address = "";

			// 教材邮寄地址
			String provinceCode = m.get("PROVINCE_CODE");
			if (provinceCode != null) {
				address += Objects.toString(
						cacheService.getCachedDictionaryName(CacheService.DictionaryKey.PROVINCE, provinceCode), "");
				String cityCode = m.get("CITY_CODE");
				if (cityCode != null) {
					address += Objects.toString(
							cacheService.getCachedDictionaryName(
									CacheService.DictionaryKey.CITY.replace("${Province}", provinceCode), cityCode),
							"");
					address += Objects.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.AREA
							.replace("${Province}", provinceCode).replace("${City}", cityCode), m.get("AREA_CODE")),
							"");
				}
			}
			address += Objects.toString(m.get("ADDRESS"), "");
			m.put("ADDRESS", address);
			datas.add(m);
		}

		String fileName = ExcelService.renderExcel(datas, titles, dbNames, "学籍信息导出表", path);
		return fileName;
	}

	/** 修改学员信息 */
	public int updateStuInfo(Map<String, String> data, String pycc, String userType, String stuNumState,
			String newStudentNumber, String rollRegisterDt) {
		int re = 0;
		try {
			GjtStudentInfo info = gjtStudentInfoDao.findByXhAndXmAndSfzhAndPycc(data.get("xh"), data.get("xm"),
					data.get("sfzh"), pycc);
			GjtUserAccount account = info.getGjtUserAccount();
			if (info != null) {
				/*
				 * if (EmptyUtils.isNotEmpty(userType) &&
				 * EmptyUtils.isNotEmpty(stuNumState)) { re =
				 * gjtStudentInfoDao.updateStuInfo(userType, stuNumState,
				 * data.get("xh"), data.get("xm"), data.get("sfzh"), pycc); }
				 * else if (EmptyUtils.isNotEmpty(stuNumState)) { re =
				 * gjtStudentInfoDao.updateStuInfoStudentNumberStatus(
				 * stuNumState, data.get("xh"), data.get("xm"),
				 * data.get("sfzh"), pycc); } else if
				 * (EmptyUtils.isNotEmpty(userType)) { re =
				 * gjtStudentInfoDao.updateStuInfoUserType(userType,
				 * data.get("xh"), data.get("xm"), data.get("sfzh"), pycc); }
				 */
				/**
				 * 如果是非正式跟读生/课程预读生/本科预读生 转为 正式生/正式跟读生/广州电大续读生/外校预科生 <<<<<<< HEAD
				 * 则需要初始化完善状态、审核状态，带审核 反之修改为通过 ======= 则需要初始化完善状态、审核状态，待审核
				 * 反之修改为通过 >>>>>>> branch 'dev_xlims_V2_1_0_0_20160919' of
				 * http://172.16.165.122:3000/tal/com.gzedu.xlims.git
				 */
				if (EmptyUtils.isNotEmpty(stuNumState)) {
					info.setXjzt(stuNumState);
				}
				if (EmptyUtils.isNotEmpty(userType)) {
					if (("13".equals(info.getUserType()) || "41".equals(info.getUserType())
							|| "61".equals(info.getUserType()))
							&& ("11".equals(userType) || "12".equals(userType) || "42".equals(userType)
									|| "51".equals(userType))) {
						info.setStatus("0");
						info.setPerfectStatus(0);
						info.getGjtSignup().setAuditState(SignupAuditStateEnum.待审核.getValue() + "");
					} else if (("13".equals(userType) || "41".equals(userType) || "61".equals(userType))
							&& ("11".equals(info.getUserType()) || "12".equals(info.getUserType())
									|| "42".equals(info.getUserType()) || "51".equals(info.getUserType()))) {
						info.setStatus("1");
						info.setPerfectStatus(1);
						info.getGjtSignup().setAuditState(SignupAuditStateEnum.通过.getValue() + "");
					}
					info.setUserType(userType);
				}
				if (EmptyUtils.isNotEmpty(newStudentNumber)) {
					info.setXh(newStudentNumber);
					account.setLoginAccount(newStudentNumber);
					// 同步至门户中心
					Map<String, Object> params = new HashMap<String, Object>();
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					String date = format.format(new Date());
					params.put("id_card", data.get("sfzh"));
					params.put("client_id", "teaching");
					params.put("client_secret", Md5Util.encodeLower("teaching" + date));
					params.put("account", newStudentNumber);
					try {
						String result = HttpClientUtils.doHttpPost(YUNYING_CENTER_SERVER + "/change/student", params,
								6000, Constants.CHARSET);
						Map<String, Object> resultMap = GsonUtils.toBean(result, Map.class);
						if (resultMap != null) {
							int code = (int) NumberUtils.toDouble(Objects.toString(resultMap.get("code"), ""));
							if (code != 200) {
								// 记录同步失败日志
								gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(),
										Constants.RSBIZ_CODE_B0002, GsonUtils.toJson(params), result));
							}
						} else {
							// 记录同步失败日志
							gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(),
									Constants.RSBIZ_CODE_B0002, GsonUtils.toJson(params), result));
						}
					} catch (Exception e) {
						String objJson = GsonUtils.toJson(params);
						log.error("同步账号至门户中心 fail ======== params:" + objJson);
						// 记录同步失败日志
						gjtSyncLogService.insert(new GjtSyncLog(info.getXm(), info.getXh(), Constants.RSBIZ_CODE_B0002,
								objJson, e.toString().length() > 500 ? e.toString().substring(0, 500) : e.toString()));
						e.printStackTrace();
					}
				}
				if (EmptyUtils.isNotEmpty(rollRegisterDt)) {
					info.setRollRegisterDt(rollRegisterDt);
				}
				info.setUpdatedDt(new Date());
				info.setUpdatedBy("批量修改学籍信息");
				gjtStudentInfoDao.save(info);
				gjtUserAccountDao.save(account);
				re = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re;
	}

	/**
	 * 判断是否为本科
	 * 
	 * @param pycc
	 * @return
	 */
	@Override
	public boolean isUndergraduateCourse(String pycc) {
		return "2".equals(pycc) || "8".equals(pycc);
	}

	@Override
	public boolean isUndergraduateCourseById(String studentId) {
		GjtStudentInfo info = this.queryById(studentId);
		return isUndergraduateCourse(info.getPycc());
	}

	@Override
	public GjtStudentInfo queryByAtid(String atid) {
		return gjtStudentInfoDao.queryByAtid(atid);
	}

	@Override
	public List<Map> queryStudentSignupInfoEveryAuditState(Map<String, Object> searchParams, Sort sort) {
		List<Map> resultMap = studentInfoDao.findStudentSignupInfoEveryAuditState(searchParams, sort);
		return resultMap;
	}

	@Override
	public String exportAuditState(Map<String, Object> searchParams, Sort sort, String path) {
		List<Map> resultMap = studentInfoDao.findStudentSignupInfoEveryAuditState(searchParams, sort);

		try {
			String[] titles = new String[] { "姓名", "性别", "学号", "手机号", "身份证号", "层次", "年级", "专业", "学习中心", "学籍状态", "订单号",
					"学员类型", "工作单位", "邮箱", "通信地址", "教材邮寄地址", "班级名称（教学班）", "班主任", "资料状态", "审核状态", "1.班主任审核状态",
					"2.招生办审核状态", "3.学籍科审核状态" };
			String[] dbNames = new String[] { "XM", "SEX_NAME", "XH", "SJH", "SFZH", "PYCCNAME", "GRADE_NAME", "ZYMC",
					"ORG_NAME", "XJZTNAME", "ORDER_SN", "USER_TYPE_NAME", "SC_CO", "DZXX", "TXDZ", "ADDRESS",
					"TEACH_CLASS_NAME", "HEADTEACHER", "PERFECT_STATUS", "LAST_AUDIT_STATE", "ONE_AUDIT_STATE",
					"TWO_AUDIT_STATE", "THREE_AUDIT_STATE" };

			for (Map<String, Object> m : resultMap) {
				String address = "";

				// 教材邮寄地址
				String provinceCode = (String) m.get("PROVINCE_CODE");
				if (provinceCode != null) {
					address += Objects.toString(
							cacheService.getCachedDictionaryName(CacheService.DictionaryKey.PROVINCE, provinceCode),
							"");
					String cityCode = (String) m.get("CITY_CODE");
					if (cityCode != null) {
						address += Objects.toString(
								cacheService.getCachedDictionaryName(
										CacheService.DictionaryKey.CITY.replace("${Province}", provinceCode), cityCode),
								"");
						address += Objects.toString(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.AREA
								.replace("${Province}", provinceCode).replace("${City}", cityCode),
								(String) m.get("AREA_CODE")), "");
					}
				}
				address += Objects.toString(m.get("ADDRESS"), "");
				m.put("ADDRESS", address);

				BigDecimal one = (BigDecimal) m.get("ONE_AUDIT_STATE");
				m.put("ONE_AUDIT_STATE",
						one == null ? "" : one.intValue() == 1 ? "审核通过" : one.intValue() == 2 ? "审核不通过" : "待审核");
				BigDecimal two = (BigDecimal) m.get("TWO_AUDIT_STATE");
				m.put("TWO_AUDIT_STATE",
						two == null ? "" : two.intValue() == 1 ? "审核通过" : two.intValue() == 2 ? "审核不通过" : "待审核");
				BigDecimal three = (BigDecimal) m.get("THREE_AUDIT_STATE");
				m.put("THREE_AUDIT_STATE",
						three == null ? "" : three.intValue() == 1 ? "审核通过" : three.intValue() == 2 ? "审核不通过" : "待审核");
			}
			String fileName = ExcelService.renderExcel(resultMap, titles, dbNames, "学籍审核环节数据", path, "学籍审核环节数据");
			return fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 修复图片数据
	public Map upPic(Map formMap) {
		// 获取有问题的图片列表
		String path = ObjectUtils.toString(formMap.get("path"));
		// SELECT id,url_new FROM GJT_SIGNUP_DATA WHERE URL_NEW LIKE
		// '%CERTIFICATE%'
		List<Map> errList = studentInfoDao.queryErrorPic();
		int re = 0;
		for (Map e : errList) {
			Object data = e.get("URL_NEW");
			String url = null, imgFilePath = null;
			if (data instanceof Clob) {
				Clob urlNew = (Clob) data;
				try {
					url = urlNew != null ? urlNew.getSubString(1, (int) urlNew.length()) : null;
					String base64 = ImgBase64Convert.encodeImgageToBase64(url);
					BASE64Decoder decoder = new BASE64Decoder();
					// Base64解码
					byte[] b = decoder.decodeBuffer(base64);
					for (int i = 0; i < b.length; ++i) {
						if (b[i] < 0) {
							b[i] += 256;
						}
					}
					// 生成jpeg图片
					imgFilePath = path + File.separator + UUIDUtils.getLongId() + ".jpg";// 新生成的图片
					OutputStream out = new FileOutputStream(imgFilePath);
					out.write(b);
					out.flush();
					out.close();
					File file = new File(imgFilePath);
					String suc = EEFileUploadUtil.sendAliyu("/ossupload/uploadInterface.do", "/files2/xlims/image",
							file)[0];// uploadImageBase64ToUrl(base64,path);
					String id = ObjectUtils.toString(e.get("ID"));
					int i = 0;
					if (EmptyUtils.isNotEmpty(suc))
						i = gjtStudentInfoDao.upPic(suc, id);
					if (i > 1) {
						re++;
						file.delete();
					}
				} catch (Exception ex) {
					System.out.println(ex);
					continue;
				}
			}

		}
		Map result = new HashMap();
		result.put("success_num", re);
		result.put("all_num", errList.size());
		return result;
	}

	@Override
	public Map<String, Object> getAuditInfo(String studentId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GjtStudentInfo info = gjtStudentInfoDao.findOne(studentId);
		resultMap.put("perfectStatus", info.getPerfectStatus());
		resultMap.put("auditState", info.getGjtSignup() != null ? info.getGjtSignup().getAuditState() : "1");
		String isConfirm = info.getStatus();
		String userType = info.getUserType();
		// “体验学员”，“测试学员”，非正式跟读生，课程预读生，本科预读生 不需要审核通过，可进入个人中心
		if ("21".equals(userType) || "31".equals(userType) || "13".equals(userType) || "41".equals(userType)
				|| "61".equals(userType)) {
			isConfirm = "1";
		}
		resultMap.put("isConfirm", isConfirm); // 是否查看入学通知书

		List<GjtFlowRecord> flowRecordList = gjtFlowService.queryFlowRecordByStudentId(studentId);
		if (flowRecordList.size() > 0) {
			GjtFlowRecord gjtFlowRecord = flowRecordList.get(flowRecordList.size() - 1);
			resultMap.put("auditDt", gjtFlowRecord.getAuditDt() != null
					? DateFormatUtils.ISO_DATE_FORMAT.format(gjtFlowRecord.getAuditDt()) : null);
			resultMap.put("auditOperator", gjtFlowRecord.getAuditOperator());
			resultMap.put("auditContent", gjtFlowRecord.getAuditContent());
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getPayOverdueStatus(String studentId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GjtStudentInfo info = gjtStudentInfoDao.findOne(studentId);
		resultMap.put("orderSn", info.getGjtSignup() != null ? info.getGjtSignup().getOrderSn() : null);
		boolean flag = info.getLearningState() != null && info.getLearningState() == 2;
		resultMap.put("isOverdue", flag ? 1 : 0);
		if (flag) {
			resultMap.put("paymentWayCode", "OTHER");
			resultMap.put("overdueReason", "您未按时在缴费期内缴费");

			if (info.getGjtSignup() != null) {
				EnrollOrderInfo orderInfo = enrollRemoteService.getSimpleOrderInfo(info.getGjtSignup().getOrderSn());
				if (orderInfo != null) {
					resultMap.put("paymentWayCode", orderInfo.getPayment_way_code());
					if ("XFY".equals(orderInfo.getPayment_way_code())) {
						resultMap.put("overdueReason", "您的学付易连续3期或以上逾期未还款");
					}
				}
			}

			resultMap.put("customerServicesPhone", "18022302379");

			Map<String, Object> headTeacherInfo = studentInfoDao.getStudentHeadTeacherInfo(studentId); // 获取班主任信息
			resultMap.put("headTeacherRealName", headTeacherInfo.get("XM")); // 班主任姓名
			resultMap.put("headTeacherMobliePhone", headTeacherInfo.get("SJH")); // 班主任手机号
			resultMap.put("headTeacherEmail", headTeacherInfo.get("DZXX")); // 班主任电子邮箱
			resultMap.put("headTeacherPhoto", headTeacherInfo.get("ZP")); // 班主任头像
		}
		return resultMap;
	}

	@Override
	public boolean againSignupInfo(String studentId) {
		GjtStudentInfo info = gjtStudentInfoDao.findOne(studentId);
		info.setStatus("0");
		info.setPerfectStatus(0);
		info.getGjtSignup().setAuditState("3");
		gjtStudentInfoDao.save(info);
		return true;
	}

	@Override
	public boolean confirm(String studentId) {
		GjtStudentInfo info = gjtStudentInfoDao.findOne(studentId);
		if (!"1".equals(info.getStatus()) && "1".equals(info.getGjtSignup().getAuditState())) {
			// 确认报读信息
			gjtStudentInfoDao.confirm(studentId);
		}
		return true;
	}

	@Override
	public boolean updateWxInfo(String sfzh, String wxOpenId, Map<String, Object> wxInfos, String updatedBy) {
		List<GjtStudentInfo> oldList = gjtStudentInfoDao.queryByWxOpenId(wxOpenId);
		// 1.清空旧的账号的微信数据
		for (GjtStudentInfo info : oldList) {
			if (info.getGjtUserAccount() != null) {
				info.getGjtUserAccount().setWxOpenId(null);
				info.getGjtUserAccount().setWxUnionId(null);
				info.getGjtUserAccount().setWxHeadPortrait(null);
				info.getGjtUserAccount().setWxNickName(null);
				gjtUserAccountService.update(info.getGjtUserAccount());
			}
		}

		// 2.赋值新的账号的微信数据
		List<GjtStudentInfo> list = gjtStudentInfoDao.findByXhOrSfzhAndIsDeletedOrderByCreatedDtDesc(sfzh);
		for (GjtStudentInfo info : list) {
			if (info.getGjtUserAccount() != null) {
				info.getGjtUserAccount().setWxOpenId(wxOpenId);
				info.getGjtUserAccount().setWxUnionId((String) wxInfos.get("wxUnionId"));
				info.getGjtUserAccount().setWxHeadPortrait((String) wxInfos.get("wxHeadPortrait"));
				info.getGjtUserAccount().setWxNickName((String) wxInfos.get("wxNickName"));
				info.getGjtUserAccount().setUpdatedBy(updatedBy);
				gjtUserAccountService.update(info.getGjtUserAccount());
			}
		}
		return true;
	}

	@Override
	public GjtStudentInfo querySSOByXhOrSfzh(String sfzh) {
		List<GjtStudentInfo> list = gjtStudentInfoDao.findByXhOrSfzhAndIsDeletedOrderByCreatedDtDesc(sfzh);
		GjtStudentInfo student = null;
		if (list != null && list.size() > 0) {
			student = list.get(0);
			for (GjtStudentInfo info : list) {
				// 获取学员的教学班班
				GjtClassInfo teachClassInfo = gjtClassInfoDao.findTeachClassByStudentId(info.getStudentId());
				if (teachClassInfo != null) { // 这里做判断，选取有教学班的对象
					student = info;
					break;
				}
			}
		}
		return student;
	}

	@Override
	public GjtStudentInfo queryBySfzh(String sfzh) {
		List<GjtStudentInfo> list = gjtStudentInfoDao.queryBySfzh(sfzh);
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public GjtStudentInfo querySSOByXhOrSfzhOrSjh(String sfzh) {
		List<GjtStudentInfo> list = gjtStudentInfoDao.findByXhOrSfzhOrSjhAndIsDeletedOrderByCreatedDtDesc(sfzh);
		GjtStudentInfo student = null;
		if (list != null && list.size() > 0) {
			student = list.get(0);
			for (GjtStudentInfo info : list) {
				// 获取学员的教学班班
				GjtClassInfo teachClassInfo = gjtClassInfoDao.findTeachClassByStudentId(info.getStudentId());
				if (teachClassInfo != null) { // 这里做判断，选取有教学班的对象
					student = info;
					break;
				}
			}
		}
		return student;
	}

	@Override
	public GjtStudentInfo queryByWxOpenId(String wxOpenId) {
		List<GjtStudentInfo> list = gjtStudentInfoDao.queryByWxOpenId(wxOpenId);
		GjtStudentInfo student = null;
		if (list != null && list.size() > 0) {
			student = list.get(0);
			for (GjtStudentInfo info : list) {
				// 获取学员的教学班班
				GjtClassInfo teachClassInfo = gjtClassInfoDao.findTeachClassByStudentId(info.getStudentId());
				if (teachClassInfo != null) { // 这里做判断，选取有教学班的对象
					student = info;
					break;
				}
			}
		}
		return student;
	}

	@Override
	public Page<StudentSignupInfoDto> queryStudentInfoByPage(Map<String, Object> searchParams,
			PageRequest pageRequest) {
		Page<Map> result = studentInfoDao.findStudentInfoByPage(searchParams, pageRequest);

		List<StudentSignupInfoDto> infoList = new ArrayList();
		for (Iterator<Map> iter = result.iterator(); iter.hasNext();) {
			Map info = iter.next();

			StudentSignupInfoDto infoDto = new StudentSignupInfoDto();
			infoDto.setStudentId((String) info.get("STUDENT_ID"));
			infoDto.setAccountId((String) info.get("ACCOUNT_ID"));
			infoDto.setXm((String) info.get("XM"));
			infoDto.setXh((String) info.get("XH"));
			infoDto.setXbm((String) info.get("XBM"));
			infoDto.setSfzh((String) info.get("SFZH"));
			infoDto.setSjh((String) info.get("SJH"));
			infoDto.setPycc((String) info.get("PYCC"));
			infoDto.setYearName((String) info.get("YEAR_NAME"));
			infoDto.setGradeName((String) info.get("GRADE_NAME"));
			infoDto.setSpecialtyName((String) info.get("ZYMC"));
			BigDecimal perfectStatus = (BigDecimal) info.get("PERFECT_STATUS");
			infoDto.setPerfectStatus(perfectStatus != null ? perfectStatus.intValue() : null);

			infoDto.setAvatar((String) info.get("AVATAR"));
			infoDto.setXjzt((String) info.get("XJZT"));
			infoDto.setUserType((String) info.get("USER_TYPE"));
			infoDto.setXxmc((String) info.get("XXMC"));
			infoDto.setDzxx((String) info.get("DZXX"));
			infoDto.setTeachClassName((String) info.get("USERCLASS"));

			infoList.add(infoDto);
		}
		return new PageImpl(infoList, pageRequest, result.getTotalElements());
	}

	@Override
	public Map importCollegeStudentInfo(File targetFile, String path, String orgId, String operatorId) {
		// 院校获取总校ID
		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);

		Map result = new HashMap();
		String[] heads = new String[] { "姓名", "身份证号", "学号", "手机号", "邮箱", "专业代码", "报读专业", "专业层次", "入学年级", "入学学期", "教务班级",
				"头像" };
		String[] dbNames = new String[] { "xm", "sfzh", "xh", "sjh", "dzxx", "zyh", "zymc", "pyccName", "yearName",
				"gradeName", "bjmc", "avatar" };
		String[] etitle = new String[] { "姓名", "身份证号", "学号", "手机号", "邮箱", "专业代码", "报读专业", "专业层次", "入学年级", "入学学期",
				"教务班级", "头像", "失败原因" };
		String[] edbNames = new String[] { "xm", "sfzh", "xh", "sjh", "dzxx", "zyh", "zymc", "pyccName", "yearName",
				"gradeName", "bjmc", "avatar", "msg" };

		List<CacheService.Value> trainingLevels = cacheService
				.getCachedDictionary(CacheService.DictionaryKey.TRAININGLEVEL); // 层次

		List<Map> successList = new ArrayList<Map>();
		List<Map> errorList = new ArrayList<Map>();
		List<String[]> datas = null;
		try {
			datas = ExcelUtil.readAsStringList(targetFile, 2, heads.length);
			String[] dataTitles = datas.remove(0);// 标题校验
			for (int i = 0; i < heads.length; i++) {
				if (!dataTitles[i].trim().equals(heads[i])) {
					result.put("exception", "请下载正确表格模版填写");
					return result;
				}
			}
		} catch (Exception e) {
			result.put("exception", "请下载正确表格模版填写");
			return result;
		}
		try {
			// 数据校验
			for (String[] data : datas) {
				String pycc = "";
				String xm = data[0];
				String sfzh = data[1];
				String xh = data[2];
				String sjh = data[3];
				String zymc = data[6];
				String pyccName = data[7];
				String yearName = data[8];
				String gradeName = data[9];
				String bjmc = data[10];
				for (CacheService.Value trainingLevel : trainingLevels) {
					if (ObjectUtils.toString(trainingLevel.getName()).equals(pyccName)) {
						pycc = ObjectUtils.toString(trainingLevel.getCode());
						break;
					}
				}

				if (EmptyUtils.isEmpty(xm)) {
					errorList.add(addCollegeStudentMap(data, "姓名不能为空"));
					continue;
				}
				if (EmptyUtils.isEmpty(sfzh)) {
					errorList.add(addCollegeStudentMap(data, "身份证号不能为空"));
					continue;
				}
				if (EmptyUtils.isEmpty(xh)) {
					errorList.add(addCollegeStudentMap(data, "学号不能为空"));
					continue;
				}
				if (EmptyUtils.isEmpty(sjh) || !ValidateUtil.isMobile(sjh)) {
					errorList.add(addCollegeStudentMap(data, "手机号格式不正确"));
					continue;
				}
				if (EmptyUtils.isEmpty(pyccName) || EmptyUtils.isEmpty(pycc)) {
					errorList.add(addCollegeStudentMap(data, "层次不正确"));
					continue;
				}
				if (EmptyUtils.isEmpty(yearName)) {
					errorList.add(addCollegeStudentMap(data, "入学年级不能为空"));
					continue;
				}
				if (EmptyUtils.isEmpty(gradeName)) {
					errorList.add(addCollegeStudentMap(data, "入学学期不能为空"));
					continue;
				}
				if (gjtStudentInfoDao.findByXhAndIsDeleted(xh) != null) {
					errorList.add(addCollegeStudentMap(data, "学号已存在"));
					continue;
				}
				List<String> spcialtyList = gjtSpecialtyCollegeDao.findSchoolSpcialty(zymc, pycc, xxId);
				if (spcialtyList == null || spcialtyList.size() == 0) {
					errorList.add(addCollegeStudentMap(data, "报读专业不存在"));
					continue;
				}
				if (spcialtyList.size() > 1) {
					errorList.add(addCollegeStudentMap(data, "院校有重复的专业，无法操作"));
					continue;
				}
				GjtGrade grade = gjtGradeDao.findByGradeNameAndGjtSchoolInfoIdAndIsDeleted(gradeName, xxId,
						Constants.BOOLEAN_NO);
				if (grade == null) {
					errorList.add(addCollegeStudentMap(data, "学期不正确"));
					continue;
				}
				if (!grade.getGjtYear().getName().equals(yearName)) {
					errorList.add(addCollegeStudentMap(data, "年级不正确"));
					continue;
				}
				String teachClassId = null;
				GjtClassInfo gjtClassInfo = null;
				if (EmptyUtils.isNotEmpty(bjmc)) {
					gjtClassInfo = gjtClassInfoDao
							.findByClassTypeAndGradeIdAndSpecialtyIdAndBjmcAndGjtSchoolInfoIdAndIsDeleted("teach",
									grade.getGradeId(), spcialtyList.get(0), bjmc, xxId, Constants.BOOLEAN_NO);
				} else {
					List<GjtClassInfo> lists = gjtClassInfoDao
							.findByClassTypeAndGradeIdAndSpecialtyIdAndGjtSchoolInfoIdAndIsDeleted("teach",
									grade.getGradeId(), spcialtyList.get(0), xxId, Constants.BOOLEAN_NO);
					gjtClassInfo = lists.get(0);
				}
				if (gjtClassInfo == null) {// 没有就新增
					gjtClassInfo = gjtClassInfoService.createNewTeachingTeachClassInfo(grade, spcialtyList.get(0), pycc,
							xxId, zymc, operatorId, 1);
					if (gjtClassInfo == null) {
						errorList.add(addCollegeStudentMap(data, "自动创建教务班级失败！"));
						continue;
					}
				}

				teachClassId = gjtClassInfo.getClassId();

				Map<String, String> param = addCollegeStudentMap(data, "");
				param.put("xxId", xxId);
				param.put("orgId", orgId);
				param.put("spcialtyId", spcialtyList.get(0));
				param.put("pycc", pycc);
				param.put("yearId", grade.getGjtYear().getGradeId()); // 年级
				param.put("gradeId", grade.getGradeId()); // 学期
				param.put("teachClassId", teachClassId); // 教学班 可以为null
				param.put("operatorId", operatorId); // 操作人Id
				param.put("bjmc", gjtClassInfo.getBjmc());
				int i = addCollegeStudentInfo(param);
				if (i == 1) {
					successList.add(param);
				} else if (i == -1) {
					param.put("msg", "服务器异常！");
					errorList.add(param);
				} else {
					param.put("msg", "学员已存在 请核对必填项");
					errorList.add(param);
				}

			}

			// 保存失败/成功列表
			String successFile = ExcelService.renderExcelHadTitle(successList, Arrays.asList(heads),
					Arrays.asList(dbNames), "导入成功列表", path);
			String errorFile = ExcelService.renderExcelHadTitle(errorList, Arrays.asList(etitle),
					Arrays.asList(edbNames), "导入失败列表", path);
			result.put("all_num", successList.size() + errorList.size());
			result.put("success_num", successList.size());
			result.put("error_num", errorList.size());
			result.put("success_file", successFile);
			result.put("error_file", errorFile);
			result.put("msg", "success");
		} catch (Exception e) {
			result.put("msg", "error");
			result.put("exception", e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/** 添加学员信息 */
	public int addCollegeStudentInfo(Map<String, String> data) {
		int re = 0;
		try {
			GjtStudentInfo info = gjtStudentInfoDao.findBySfzhAndPyccAndIsDeleted(data.get("sfzh"), data.get("pycc"),
					Constants.BOOLEAN_NO);
			if (info == null) {
				info = new GjtStudentInfo();
				info.setStudentId(UUIDUtils.random());
				String avatar = data.get("avatar");
				if (StringUtils.isNotBlank(avatar) && !"无".equals(avatar)) {
					info.setAvatar(avatar);
				}
				info.setXm(data.get("xm"));
				String sfzh = data.get("sfzh");
				info.setSfzh(sfzh);
				info.setXh(data.get("xh"));
				info.setSjh(data.get("sjh"));
				info.setDzxx(data.get("dzxx"));
				// 如果性别未知，则通过身份证获取性别 18位身份证号码：第17位代表性别，奇数为男，偶数为女。
				if (sfzh.length() == 18) {
					String sexNum = sfzh.substring(16, 17);
					int mod = NumberUtils.toInt(sexNum) % 2;
					String sex = "0";
					if (mod == 1) {
						sex = "1";
					} else {
						sex = "2";
					}
					info.setXbm(sex);
					info.setCsrq(sfzh.substring(6, 14));
				}
				info.setMajor(data.get("spcialtyId"));
				info.setPycc(data.get("pycc"));
				info.setGjtGrade(gjtGradeDao.findOne(data.get("gradeId")));
				info.setGjtSchoolInfo(gjtSchoolInfoDao.findOne(data.get("orgId")));

				String teachClassId = data.get("teachClassId");
				String operatorId = data.get("operatorId");
				String xxId = data.get("xxId");
				info.setUserclass(data.get("bjmc"));
				// 设置默认属性
				info.setXjzt("3");
				info.setUserType("11");
				info.setClassType("C");
				info.setStatus("1");
				info.setCreatedDt(new Date());
				info.setIsDeleted(Constants.BOOLEAN_NO);
				info.setVersion(new BigDecimal(1));
				info.setCreatedBy(operatorId);
				// 添加账号信息
				GjtUserAccount userAccount = gjtUserAccountService.saveEntity(info.getXm(), info.getXh(),
						info.getGjtSchoolInfo().getId(), 1);
				userAccount.setEmail(info.getDzxx());
				userAccount.setSjh(info.getSjh());
				gjtUserAccountService.update(userAccount);
				// 添加学员信息
				info.setGjtUserAccount(userAccount);
				gjtStudentInfoDao.save(info); // 添加
				info.setPerfectStatus(1);
				gjtStudentInfoDao.save(info); // 修改

				// 学员加入该班级
				GjtClassStudent item = new GjtClassStudent();
				item.setClassStudentId(UUIDUtils.random());
				item.setGjtClassInfo(gjtClassInfoDao.findOne(teachClassId));
				item.setGjtStudentInfo(info);
				item.setGjtSchoolInfo(info.getGjtSchoolInfo());
				item.setGjtGrade(info.getGjtGrade());
				item.setCreatedBy(operatorId);
				gjtClassStudentDao.save(item);
				re = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			re = -1;
		}
		return re;
	}

	/** 自定义数组转MAP */
	private Map<String, String> addCollegeStudentMap(String[] data, String msg) {
		Map<String, String> re = new HashMap();
		if (EmptyUtils.isNotEmpty(msg)) {
			re.put("msg", msg);
		}
		re.put("xm", data[0]);
		re.put("sfzh", data[1]);
		re.put("xh", data[2]);
		re.put("sjh", data[3]);
		re.put("dzxx", data[4]);
		re.put("zyh", data[5]);
		re.put("zymc", data[6]);
		re.put("pyccName", data[7]);
		re.put("yearName", data[8]);
		re.put("gradeName", data[9]);
		re.put("bjmc", data[10]);
		re.put("avatar", data[11]);
		return re;
	}

	@Override
	public Page<StudentCourseDto> queryStudentCourseByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		Page<Map> result = studentInfoDao.findStudentCourseByPage(searchParams, pageRequest);

		List<StudentCourseDto> infoList = new ArrayList();
		for (Iterator<Map> iter = result.iterator(); iter.hasNext();) {
			Map info = iter.next();

			StudentCourseDto infoDto = new StudentCourseDto();
			infoDto.setStudentId((String) info.get("STUDENT_ID"));
			infoDto.setAccountId((String) info.get("ACCOUNT_ID"));
			infoDto.setXm((String) info.get("XM"));
			infoDto.setXh((String) info.get("XH"));
			infoDto.setXbm((String) info.get("XBM"));
			infoDto.setSfzh((String) info.get("SFZH"));
			infoDto.setSjh((String) info.get("SJH"));
			infoDto.setPycc((String) info.get("PYCC"));
			infoDto.setYearName((String) info.get("YEAR_NAME"));
			infoDto.setGradeName((String) info.get("GRADE_NAME"));
			infoDto.setSpecialtyName((String) info.get("ZYMC"));

			infoDto.setAvatar((String) info.get("AVATAR"));
			infoDto.setTakeCourseCreatedDt((Date) info.get("TAKECOURSECREATEDDT"));
			infoDto.setKch((String) info.get("KCH"));
			infoDto.setKcmc((String) info.get("KCMC"));

			infoList.add(infoDto);
		}
		return new PageImpl(infoList, pageRequest, result.getTotalElements());
	}

	@Override
	public Map importCollegeStudentTakeCourse(File targetFile, String path, String orgId, String operatorId) {
		// 院校获取总校ID
		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);

		Map result = new HashMap();
		String[] heads = new String[] { "学期", "姓名", "学号", "课程名称", "课程代码" };
		String[] dbNames = new String[] { "termName", "xm", "xh", "kcmc", "kch" };
		String[] etitle = new String[] { "学期", "姓名", "学号", "课程名称", "课程代码", "失败原因" };
		String[] edbNames = new String[] { "termName", "xm", "xh", "kcmc", "kch", "msg" };
		List<Map> successList = new ArrayList<Map>();
		List<Map> errorList = new ArrayList<Map>();
		List<String[]> datas = null;
		try {
			datas = ExcelUtil.readAsStringList(targetFile, 2, heads.length);
			String[] dataTitles = datas.remove(0);// 标题校验
			for (int i = 0; i < heads.length; i++) {
				if (!dataTitles[i].trim().equals(heads[i])) {
					result.put("exception", "请下载正确表格模版填写");
					return result;
				}
			}
		} catch (Exception e) {
			result.put("exception", "请下载正确表格模版填写");
			return result;
		}
		try {
			// 数据校验
			for (String[] data : datas) {
				String termName = data[0];
				String xm = data[1];
				String xh = data[2];
				String kcmc = data[3];
				String kch = data[4];

				if (EmptyUtils.isEmpty(termName)) {
					errorList.add(addCollegeStudentTakeCourseMap(data, "学期不能为空"));
					continue;
				}
				if (EmptyUtils.isEmpty(xm)) {
					errorList.add(addCollegeStudentTakeCourseMap(data, "姓名不能为空"));
					continue;
				}
				if (EmptyUtils.isEmpty(xh)) {
					errorList.add(addCollegeStudentTakeCourseMap(data, "学号不能为空"));
					continue;
				}
				if (EmptyUtils.isEmpty(kcmc)) {
					errorList.add(addCollegeStudentTakeCourseMap(data, "课程名称不能为空"));
					continue;
				}
				if (EmptyUtils.isEmpty(kch)) {
					errorList.add(addCollegeStudentTakeCourseMap(data, "课程代码不能为空"));
					continue;
				}
				GjtStudentInfo studentInfo = gjtStudentInfoDao.findByXhAndIsDeleted(xh);
				if (studentInfo == null) {
					errorList.add(addCollegeStudentTakeCourseMap(data, "学号不存在"));
					continue;
				}
				if (!xm.equals(studentInfo.getXm())) {
					errorList.add(addCollegeStudentTakeCourseMap(data, "姓名不匹配"));
					continue;
				}
				GjtGrade grade = gjtGradeDao.findByGradeNameAndGjtSchoolInfoIdAndIsDeleted(termName, xxId,
						Constants.BOOLEAN_NO);
				if (grade == null || !grade.getGradeId().equals(studentInfo.getNj())) {
					errorList.add(addCollegeStudentTakeCourseMap(data, "学期不正确"));
					continue;
				}
				List<GjtCourse> courseList = gjtCourseDao.findByKchAndXxIdAndIsDeleted(kch, xxId, Constants.BOOLEAN_NO);
				if (courseList == null || courseList.size() == 0) {
					// 如果当前院校找不到课程就到广州国开下面找，暂时先写死广州国开ID
					courseList = gjtCourseDao.findByKchAndXxIdAndIsDeleted(kch, WebConstants.GK_ORG_ID,
							Constants.BOOLEAN_NO);
					if (courseList == null || courseList.size() == 0) {
						errorList.add(addCollegeStudentTakeCourseMap(data, "课程代码不存在"));
						continue;
					}
				}
				if (courseList.size() > 1) {
					errorList.add(addCollegeStudentTakeCourseMap(data, "课程代码有重复的课程，无法操作"));
					continue;
				}
				if (!kcmc.equals(courseList.get(0).getKcmc())) {
					errorList.add(addCollegeStudentTakeCourseMap(data, "课程名称不匹配"));
					continue;
				}
				String courseId = courseList.get(0).getCourseId();

				// 查询该课程对应的学期，课程在哪个学期上(根据教学计划中的第几学期)
				GjtSpecialtyPlan specialtyPlan = gjtSpecialtyPlanDao.findByXxIdAndSpecialtyIdAndCourseId(xxId,
						studentInfo.getMajor(), courseId);
				Integer kkxq = specialtyPlan.getTermTypeCode(); // 开课学期
				if (kkxq == null) {
					errorList.add(addCollegeStudentTakeCourseMap(data, "该专业计划的课程学期信息有误"));
					continue;
				}
				// 获取该课程在哪个学期学习
				Page<Map> gradeInfo = gradeDao.findGradeByPage(xxId, studentInfo.getNj(), new PageRequest(kkxq - 1, 1));
				if (gradeInfo.getNumberOfElements() == 0) {
					errorList.add(addCollegeStudentTakeCourseMap(data, "院校下缺少第" + kkxq + "学期的学期信息"));
					continue;
				}
				String courseTermId = (String) gradeInfo.getContent().get(0).get("GRADE_ID");

				// 获取教学计划
				List<GjtTeachPlan> teachPlenList = gjtTeachPlanDao
						.findByKkzyAndCourseIdAndActualGradeIdAndXxIdAndIsDeletedOrderByCreatedDtDesc(
								studentInfo.getMajor(), courseId, courseTermId, xxId, Constants.BOOLEAN_NO);
				if (teachPlenList == null || teachPlenList.size() == 0) {
					errorList.add(addCollegeStudentTakeCourseMap(data, "该课程暂无教学计划"));
					continue;
				}
				GjtTeachPlan gjtTeachPlan = teachPlenList.get(0);

				// 根据学期和课程获取期课程信息
				GjtTermCourseinfo termCourseinfo = gjtTermCourseinfoDao.findByXxIdAndTermIdAndCourseId(xxId,
						courseTermId, courseId);
				if (termCourseinfo == null) {
					errorList.add(addCollegeStudentTakeCourseMap(data, "没有找到该课程对应的期课程信息"));
					continue;
				}

				Map<String, String> param = addCollegeStudentTakeCourseMap(data, "");
				param.put("xxId", xxId);
				param.put("orgId", orgId);
				param.put("termId", courseTermId); // 课程对应的学期，课程在哪个学期上
				param.put("courseId", courseId); // 课程ID
				param.put("teachPlanId", gjtTeachPlan.getTeachPlanId()); // 教学计划ID
				param.put("termcourseId", termCourseinfo.getTermcourseId()); // 期课程ID
				param.put("operatorId", operatorId); // 操作人Id
				int i = addCollegeStudentTakeCourse(param, studentInfo);
				if (i == 1) {
					successList.add(param);
				} else if (i == -1) {
					param.put("msg", "服务器异常！");
					errorList.add(param);
				} else {
					param.put("msg", "操作失败，请核对信息是否有误！");
					errorList.add(param);
				}
			}

			// 保存失败/成功列表
			String successFile = ExcelService.renderExcelHadTitle(successList, Arrays.asList(heads),
					Arrays.asList(dbNames), "导入成功列表", path);
			String errorFile = ExcelService.renderExcelHadTitle(errorList, Arrays.asList(etitle),
					Arrays.asList(edbNames), "导入失败列表", path);
			result.put("all_num", successList.size() + errorList.size());
			result.put("success_num", successList.size());
			result.put("error_num", errorList.size());
			result.put("success_file", successFile);
			result.put("error_file", errorFile);
			result.put("msg", "success");
		} catch (Exception e) {
			result.put("msg", "error");
			result.put("exception", e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/** 添加学员选课信息 */
	public int addCollegeStudentTakeCourse(Map<String, String> data, GjtStudentInfo studentInfo) {
		int re = 0;
		try {
			String teachPlanId = data.get("teachPlanId");
			String termcourseId = data.get("termcourseId");
			String courseId = data.get("courseId");
			GjtRecResult info = gjtRecResultDao.findByStudentIdAndTeachPlanIdAndCourseIdAndIsDeleted(
					studentInfo.getStudentId(), teachPlanId, courseId, Constants.BOOLEAN_NO);
			if (info == null) {
				String xxId = data.get("xxId");
				String orgId = data.get("orgId");
				String termId = data.get("termId");
				String operatorId = data.get("operatorId");
				GjtSchoolInfo schoolInfo = gjtSchoolInfoDao.findOne(xxId);
				GjtGrade termInfo = gjtGradeDao.findOne(termId);

				// 获取教学班
				GjtClassInfo teachClassInfo = gjtClassInfoDao.findTeachClassByStudentId(studentInfo.getStudentId());
				// 创建或获取课程班
				int bh = 1; // 班号 暂时为1
				GjtClassInfo courseClassInfo = gjtClassInfoService.createCourseClassInfo(termId, courseId, termcourseId,
						bh, xxId, operatorId, "院校模式-自动生成课程班");

				// 添加选课信息
				info = new GjtRecResult();
				info.setRecId(UUIDUtils.random());
				info.setStudentId(studentInfo.getStudentId());
				info.setCourseId(courseId);
				info.setTeachPlanId(teachPlanId);
				info.setTermcourseId(termcourseId);
				info.setTermId(termId);
				info.setGjtSchoolInfo(schoolInfo);
				info.setOrgId(orgId);
				info.setOrgCode(gjtOrgDao.findOne(orgId).getTreeCode());

				// 设置默认属性
				info.setIsOver("0");
				info.setPayState("2");
				info.setMemo("院校模式-学员选课");
				info.setCreatedBy(operatorId);
				gjtRecResultDao.save(info);

				// 学员加入该班级
				GjtClassStudent item = new GjtClassStudent();
				item.setClassStudentId(UUIDUtils.random());
				item.setGjtClassInfo(courseClassInfo);
				item.setGjtStudentInfo(studentInfo);
				item.setGjtSchoolInfo(schoolInfo);
				item.setGjtGrade(gjtGradeDao.findOne(termId));
				item.setCreatedBy(operatorId);
				gjtClassStudentDao.save(item);

				// 同步数据
				String appId = schoolInfo.getAppid();
				// step 1 同步课程班
				{
					if (courseClassInfo != null && !Constants.BOOLEAN_YES.equals(courseClassInfo.getSyncStatus())) {
						OpiTermClass termClass = new OpiTermClass();
						termClass.setCLASS_ID(courseClassInfo.getClassId());
						termClass.setTERMCOURSE_ID(termcourseId);
						termClass.setCLASS_NAME(courseClassInfo.getBjmc());
						termClass.setYEAR(termInfo.getGradeName());
						termClass.setCREATED_BY(courseClassInfo.getCreatedBy());
						termClass.setIS_EXPERIENCE("N");

						OpiTermClassData otcd = new OpiTermClassData(termClass);
						RSimpleData rSimpleData = pCourseServer.synchroTermClass(otcd);
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
				// step 2 同步学员信息
				{
					OpiStudent op = new OpiStudent();
					op.setSTUD_ID(studentInfo.getStudentId());
					op.setSTUD_USER_ID(studentInfo.getAtid());
					op.setREALNAME(studentInfo.getXm());
					op.setCREATED_BY(studentInfo.getCreatedBy());
					op.setLMS_NO(studentInfo.getXh());
					op.setSTUD_AREA(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.PROVINCE,
							studentInfo.getProvince()));
					op.setMOBILE_NO(studentInfo.getSjh());
					op.setEMAIL(studentInfo.getDzxx());
					op.setEE_NO(studentInfo.getEeno());
					op.setIMG_PATH(studentInfo.getAvatar());
					op.setSEX(
							cacheService.getCachedDictionaryName(CacheService.DictionaryKey.SEX, studentInfo.getXbm()));
					op.setXLCLASS_ID(teachClassInfo.getClassId());
					op.setXLCLASS_NAME(teachClassInfo.getBjmc());
					op.setMANAGER_ID(
							teachClassInfo.getGjtBzr() != null ? teachClassInfo.getGjtBzr().getEmployeeId() : null);

					OpiStudentData od = new OpiStudentData(appId, op);
					RSimpleData synchroCourse = pCourseServer.synchroStudent(od);
					if (synchroCourse == null) {
						throw new ServiceException("学员同步失败");
					} else {
						if (synchroCourse.getStatus() == 1) {

						} else {
							log.error("function synchroStudent error ========= " + synchroCourse.getMsg());
							throw new ServiceException(synchroCourse.getMsg());
						}
					}
				}
				// step 3 同步学员选课
				{
					OpiStudchoose studchoose = new OpiStudchoose();
					studchoose.setCHOOSE_ID(info.getRecId());
					studchoose.setTERMCOURSE_ID(termcourseId);
					studchoose.setSTUD_ID(studentInfo.getStudentId());
					studchoose.setCLASS_ID(courseClassInfo.getClassId());
					studchoose.setCREATED_BY(operatorId);

					OpiTermChooseData termChooseData = new OpiTermChooseData(studchoose);
					RSimpleData rSimpleData = pCourseServer.synchroTermChoose(termChooseData);
					if (rSimpleData == null) {
						throw new ServiceException("选课同步失败");
					} else {
						if (rSimpleData.getStatus() == 1) {
							gjtRecResultDao.updateSyncStatus(Constants.BOOLEAN_YES, info.getRecId());
						} else {
							log.error("function synchroTermChoose error ========= " + rSimpleData.getMsg());
							throw new ServiceException(rSimpleData.getMsg());
						}
					}
				}
			}
			re = 1;
		} catch (Exception e) {
			e.printStackTrace();
			re = -1;
		}
		return re;
	}

	/** 自定义数组转MAP */
	private Map<String, String> addCollegeStudentTakeCourseMap(String[] data, String msg) {
		Map<String, String> re = new HashMap();
		if (EmptyUtils.isNotEmpty(msg)) {
			re.put("msg", msg);
		}
		re.put("termName", data[0]);
		re.put("xm", data[1]);
		re.put("xh", data[2]);
		re.put("kcmc", data[3]);
		re.put("kch", data[4]);
		return re;
	}

	@Override
	public List<Map<String, String>> countStudentInfoByOrgId(Collection<String> orgIds) {
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT COUNT(1) TOTAL,");
		sql.append("  COUNT(CASE");
		sql.append("  WHEN GSI.IS_ENTERING_SCHOOL = 0 THEN 1");
		sql.append("  ELSE NULL");
		sql.append("  END) NO_ENTERING_COUNT,");
		sql.append("  COUNT(CASE");
		sql.append("  WHEN GSI.XJZT = '2' THEN 1");
		sql.append("  ELSE NULL");
		sql.append("  END) IN_SCHOOL_COUNT,");
		sql.append("  COUNT(CASE");
		sql.append("  WHEN GSI.XJZT = '3' OR GSI.XJZT = '0' THEN 1");
		sql.append("  ELSE NULL ");
		sql.append("  END) NO_SCHOOL_COUNT,");
		sql.append("  COUNT(CASE");
		sql.append("  WHEN GSI.XJZT = '8' THEN 1");
		sql.append("  ELSE NULL");
		sql.append("  END) GRADUATION_COUNT,");
		sql.append("  COUNT(CASE");
		sql.append("  WHEN GSI.XJZT = '4' THEN 1");
		sql.append("  ELSE NULL");
		sql.append("  END) STOP_SCHOOL_COUNT,");
		sql.append("  COUNT(CASE");
		sql.append("  WHEN GSI.XJZT = '5' THEN 1");
		sql.append("  ELSE NULL");
		sql.append("  END) LEAVE_SCHOOL_COUNT");
		sql.append("  FROM GJT_STUDENT_INFO GSI ");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GSI.XXZX_ID in :ORG_ID");
		Map<String, Object> params = Maps.newHashMap();
		params.put("ORG_ID", orgIds);

		return this.commonDao.queryForMapListNative(sql.toString(), params);
	}

	@Override
	public List<String> findSidByCreatedDtGreaterThanEqual(Date data) {
		return gjtStudentInfoDao.findSidByCreatedDtGreaterThanEqual(data);
	}

	/**
	 * 查询学员所在的教务班级
	 */
	@Override
	public Page<Map<String, Object>> queryStudentTeachClassInfo(String orgId, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		log.info("参数名称：【orgId:" + orgId + ";searchParams:】" + searchParams.toString());
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		sql.append("  SELECT GSI.XM,");
		sql.append("  GSI.STUDENT_ID,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.SJH,");
		sql.append("  GSI.SFZH,");
		sql.append("  GSI.PYCC,");
		sql.append("  TSD.NAME,");
		sql.append("  GG.GRADE_NAME,");
		sql.append("  GSI.MAJOR,");
		sql.append("  GS.ZYMC,");
		sql.append("  GCI.BJMC,");
		sql.append("  GEI.XM as \"TEACH_NAME\",");
		sql.append("  GSI.SC_CO,");
		sql.append("  (CASE GO.ORG_TYPE");
		sql.append("  WHEN '3' THEN");
		sql.append("  (GO.ORG_NAME)");
		sql.append("  ELSE");
		sql.append("  (SELECT GGG.ORG_NAME FROM GJT_ORG GGG WHERE GGG.ID = GO.PERENT_ID) ");
		sql.append("  END) ORG_NAME,");
		sql.append("  GO.ID");
		sql.append("  FROM GJT_STUDENT_INFO GSI");
		sql.append("  INNER JOIN VIEW_STUDENT_INFO VSI");
		sql.append("  ON VSI.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  INNER JOIN GJT_GRADE GG");
		sql.append("  ON GG.GRADE_ID = VSI.GRADE_ID");
		sql.append("  AND GG.IS_DELETED = 'N'");
		sql.append("  INNER JOIN GJT_SPECIALTY GS");
		sql.append("  ON GS.SPECIALTY_ID = VSI.MAJOR");
		sql.append("  AND GS.IS_DELETED = 'N'");
		sql.append("  INNER JOIN GJT_CLASS_STUDENT GCS");
		sql.append("  ON GCS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  INNER JOIN GJT_CLASS_INFO GCI");
		sql.append("  ON GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'teach'");
		sql.append("  LEFT JOIN GJT_EMPLOYEE_INFO GEI");
		sql.append("  ON GEI.EMPLOYEE_ID = GCI.BZR_ID");
		sql.append("  AND GEI.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG GO");
		sql.append("  ON GO.ID = GSI.XXZX_ID");
		sql.append("  AND GO.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN TBL_SYS_DATA TSD");
		sql.append("  ON TSD.CODE = GSI.PYCC");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel'");
		sql.append("  AND TSD.IS_DELETED = 'N'");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GSI.XXZX_ID IN");
		sql.append("  (SELECT ORG.ID");
		sql.append("  FROM GJT_ORG ORG");
		sql.append("  WHERE ORG.IS_DELETED = 'N'");
		sql.append("  START WITH ORG.ID =:ORG_ID");
		sql.append("  CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID)");
		params.put("ORG_ID", orgId);
		// 学号
		String xh = (String) searchParams.get("XH");
		if (StringUtils.isNotBlank(xh)) {
			sql.append("  AND GSI.XH = :XH");
			params.put("XH", xh);
		}
		// 姓名
		String xm = (String) searchParams.get("XM");
		if (StringUtils.isNotBlank(xm)) {
			sql.append("  AND GSI.XM= :XM");
			params.put("XM", xm);
		}
		// 所在单位
		String scCo = (String) searchParams.get("scCo");
		if (StringUtils.isNotBlank(scCo)) {
			sql.append("  AND GSI.SC_CO LIKE :SC_CO");
			params.put("SC_CO", "%" + scCo + "%");
		}
		// 层次
		String pycc = (String) searchParams.get("PYCC");
		if (StringUtils.isNotBlank(pycc)) {
			sql.append("  AND GSI.PYCC= :PYCC");
			params.put("PYCC", pycc);
		}
		// 专业
		String major = (String) searchParams.get("MAJOR");
		if (StringUtils.isNotBlank(major)) {
			sql.append("  AND GSI.MAJOR= :MAJOR");
			params.put("MAJOR", major);
		}
		// 学习中心
		String studyCenterId = (String) searchParams.get("ID");
		if (StringUtils.isNotBlank(studyCenterId)) {
			sql.append("  AND GO.ID= :ID");
			params.put("ID", studyCenterId);
		}
		return (Page) commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	@Override
	public List<Map> queryStudentIdsBy(Map<String, Object> searchParams) {
		return studentInfoDao.findStudentIdsBy(searchParams);
	}

	@Override
	public Page<Map> findStudent(String xhOrxm, String xxId, PageRequest pageRequest) {
		return studentInfoDao.findStudent(xhOrxm, xxId, pageRequest);
	}

	@Override
	public List<Map<String, Object>> exportStudentTeachClassInfo(String orgId, Map<String, Object> searchParams) {
		log.info("参数名称：searchParams:{}", searchParams);
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		sql.append("  SELECT GSI.XM,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.SJH,");
		sql.append("  GSI.SFZH,");
		sql.append("  GSI.PYCC,");
		sql.append("  GSI.txdz,");
		sql.append("  GSI.xjzt,");
		sql.append("  GSI.xbm,");
		sql.append("  GSI.dzxx,");
		sql.append("  GSI.USER_TYPE,");
		sql.append("  gsg.audit_state,");
		sql.append("  TSD.NAME,");
		sql.append("  GG.GRADE_NAME,");
		sql.append("  GS.ZYMC,");
		sql.append("  GCI.BJMC,");
		sql.append("  GEI.XM as \"TEACH_NAME\",");
		sql.append("  GSI.SC_CO,");
		sql.append("  (CASE GO.ORG_TYPE");
		sql.append("  WHEN '3' THEN");
		sql.append("  (GO.ORG_NAME)");
		sql.append("  ELSE");
		sql.append("  (SELECT GGG.ORG_NAME FROM GJT_ORG GGG WHERE GGG.ID = GO.PERENT_ID) ");
		sql.append("  END) ORG_NAME,");
		sql.append("  GO.ID");
		sql.append("  FROM GJT_STUDENT_INFO GSI");
		sql.append("  INNER JOIN gjt_signup gsg ");
		sql.append("  ON GSI.STUDENT_ID = gsg.STUDENT_ID");
		sql.append("  INNER JOIN VIEW_STUDENT_INFO VSI");
		sql.append("  ON VSI.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  INNER JOIN GJT_GRADE GG");
		sql.append("  ON GG.GRADE_ID = VSI.GRADE_ID");
		sql.append("  AND GG.IS_DELETED = 'N'");
		sql.append("  INNER JOIN GJT_SPECIALTY GS");
		sql.append("  ON GS.SPECIALTY_ID = VSI.MAJOR");
		sql.append("  AND GS.IS_DELETED = 'N'");
		sql.append("  INNER JOIN GJT_CLASS_STUDENT GCS");
		sql.append("  ON GCS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  INNER JOIN GJT_CLASS_INFO GCI");
		sql.append("  ON GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'teach'");
		sql.append("  LEFT JOIN GJT_EMPLOYEE_INFO GEI");
		sql.append("  ON GEI.EMPLOYEE_ID = GCI.BZR_ID");
		sql.append("  AND GEI.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG GO");
		sql.append("  ON GO.ID = GSI.XXZX_ID");
		sql.append("  AND GO.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN TBL_SYS_DATA TSD");
		sql.append("  ON TSD.CODE = GSI.PYCC");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel'");
		sql.append("  AND TSD.IS_DELETED = 'N'");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GSI.XXZX_ID IN");
		sql.append("  (SELECT ORG.ID");
		sql.append("  FROM GJT_ORG ORG");
		sql.append("  WHERE ORG.IS_DELETED = 'N'");
		sql.append("  START WITH ORG.ID =:ORG_ID");
		sql.append("  CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID)");
		params.put("ORG_ID", orgId);
		// 学号
		// String xh = (String) searchParams.get("XH");
		// if (StringUtils.isNotBlank(xh)) {
		// sql.append(" AND GSI.XH = :XH");
		// params.put("XH", xh);
		// }
		// 姓名
		// String xm = (String) searchParams.get("XM");
		// if (StringUtils.isNotBlank(xm)) {
		// sql.append(" AND GSI.XM= :XM");
		// params.put("XM", xm);
		// }
		// 所在单位
		// String scCo = (String) searchParams.get("scCo");
		// if (StringUtils.isNotBlank(scCo)) {
		// sql.append(" AND GSI.SC_CO LIKE :SC_CO");
		// params.put("SC_CO", "%" + scCo + "%");
		// }
		// 层次
		// String pycc = (String) searchParams.get("PYCC");
		// if (StringUtils.isNotBlank(pycc)) {
		// sql.append(" AND GSI.PYCC= :PYCC");
		// params.put("PYCC", pycc);
		// }
		// 专业
		String specialtyId = (String) searchParams.get("specialtyId");
		if (StringUtils.isNotBlank(specialtyId)) {
			sql.append(" AND GSI.MAJOR= :specialtyId");
			params.put("specialtyId", specialtyId);
		}
		// 学习中心
		String studyCenterId = (String) searchParams.get("orgId");
		if (StringUtils.isNotBlank(studyCenterId)) {
			sql.append("  AND GO.ID= :ID");
			params.put("ID", studyCenterId);
		}
		return commonDao.queryForMapList(sql.toString(), params);
	}

	/**
	 * 统计身份证识别有误数据
	 * 
	 * @param searchParams
	 * @param path
	 * @return
	 */
	public String exportNotValidStudentName(Map<String, Object> searchParams, String path) {
		String zipFileName = "statistics.zip";
		List<Map> studentInfos = studentInfoDao.findStudentSignupInfoBy(searchParams);

		try {
			String[] titles = new String[] { "学员ID", "入学学期", "学号", "姓名", "身份证号", "身份证URL", "识别出来的姓名", "识别出来的身份证号",
					"结果" };
			String[] dbNames = new String[] { "ID", "TERM_NAME", "XH", "XM", "SFZH", "SFZ_URL", "SB_XM", "SB_SFZH",
					"RESULT" };

			final int nThreads = Runtime.getRuntime().availableProcessors() * 8;
			// ExecutorService executorService =
			// Executors.newFixedThreadPool(nThreads);
			ExecutorService executorService = Executors.newSingleThreadExecutor(); // 由于身份证识别有并发限制，所以使用单线程池
			final CountDownLatch complete = new CountDownLatch(studentInfos.size());
			long startTime = System.currentTimeMillis();

			final Set<String> yiNum = new ConcurrentSkipListSet<String>();
			final List<Map> pointList = new CopyOnWriteArrayList<Map>();
			for (int i = 0; i < studentInfos.size(); i++) {
				final Map info = studentInfos.get(i);
				executorService.execute(new Runnable() {
					@Override
					public void run() {
						String studentId = (String) info.get("STUDENT_ID");
						Map<String, String> signupCopyDataTemp = gjtSignupService.getSignupCopyData(studentId);
						String sfzz = signupCopyDataTemp.get("sfz-z");
						if (StringUtils.isNotBlank(sfzz)) {
							Map<String, Object> idCardInfo = EEFileUploadUtil.getIDCardInfo(sfzz);
							Double type = (Double) idCardInfo.get("type");
							String name = (String) idCardInfo.get("name");
							String idCardNumber = (String) idCardInfo.get("id_card_number");

							Map m = new HashMap();
							m.put("ID", Objects.toString(studentId, ""));
							m.put("TERM_NAME", Objects.toString(info.get("TERM_NAME"), ""));
							m.put("XH", Objects.toString(info.get("XH"), ""));
							m.put("XM", Objects.toString(info.get("XM"), ""));
							m.put("SFZH", Objects.toString(info.get("SFZH"), ""));

							m.put("SFZ_URL", Objects.toString(sfzz, ""));
							m.put("SB_XM", Objects.toString(name, ""));
							m.put("SB_SFZH", Objects.toString(idCardNumber, ""));
							if (type != null) {
								if (type.intValue() == 1) {
									if (StringUtils.equals(name, (String) info.get("XM"))
											&& StringUtils.equals(idCardNumber, (String) info.get("SFZH"))) {
										// 正确的
										GjtSignup signup = gjtSignupService.querySignupByStudentId(studentId);
										signup.setMail("1");
										gjtSignupService.save(signup);
									} else if (!StringUtils.equals(name, (String) info.get("XM"))
											&& !StringUtils.equals(idCardNumber, (String) info.get("SFZH"))) {
										m.put("RESULT", "姓名、身份证号都不一致");
										pointList.add(m);
										yiNum.add(studentId);
									} else if (!StringUtils.equals(name, (String) info.get("XM"))) {
										m.put("RESULT", "姓名不一致");
										pointList.add(m);
										yiNum.add(studentId);
									} else if (!StringUtils.equals(idCardNumber, (String) info.get("SFZH"))) {
										m.put("RESULT", "身份证号不一致");
										pointList.add(m);
										yiNum.add(studentId);
									}
								} else {
									m.put("RESULT", "非中国居民身份证");
									pointList.add(m);
									yiNum.add(studentId);
								}
							} else {
								m.put("RESULT", "识别异常");
								pointList.add(m);
								yiNum.add(studentId);
							}
						}
						complete.countDown();
						log.error("exportNotValidStudentName <br/>thread size: " + nThreads + ", not execute size: "
								+ complete.getCount() + ".");
					}
				});
			}

			try {
				// 等待完成，设置超时时间25分钟
				complete.await(25, TimeUnit.MINUTES);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				executorService.shutdown();
			}
			long endTime = System.currentTimeMillis();
			log.error("exportNotValidStudentName <br/>thread size: " + nThreads + ", time consuming: "
					+ (endTime - startTime) + "ms, student size: " + studentInfos.size() + ", not execute size: "
					+ complete.getCount() + ".");

			// excel
			String folderName = "身份证识别情况表-是否完整导完_" + (complete.getCount() == 0 ? "是" : "否") + "-----识别有误人数"
					+ yiNum.size() + "_" + System.currentTimeMillis();
			String fileName = ExcelService.renderExcel(pointList, titles, dbNames, "身份证识别情况",
					path + folderName + File.separator, "身份证识别情况");

			// zip
			ZipFileUtil.zipDir(path + folderName, path + zipFileName);
			FileKit.delFile(path + folderName);
			return zipFileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<GjtStudentInfo> findStudent(String xxId, String gradeId, String specialtyId) {
		return gjtStudentInfoDao.findStudentInfo(xxId, gradeId, specialtyId);
	}

	@Override
	public Page<Map<String, Object>> queryPutStudent(Map<String, Object> searchMap, PageRequest pageRequst) {
		Map<String, Object> resultMap = searchSql(searchMap);
		String searchSql = (String) resultMap.get("sql");
		Map<String, Object> params = (Map<String, Object>) resultMap.get("params");
		Page<Map<String, Object>> page = commonDao.queryForPageNative(searchSql, params, pageRequst);
		return page;
	}

	@Override
	public List<Map<String, Object>> queryPutStudent(Map<String, Object> searchMap) {
		Map<String, Object> resultMap = searchSql(searchMap);
		String sql = (String) resultMap.get("sql");
		Map<String, Object> params = (Map<String, Object>) resultMap.get("params");
		List<Map<String, Object>> list = commonDao.queryForMapList(sql, params);
		return list;
	}

	@Override
	public long queryPutStudentCount(Map<String, Object> searchMap) {
		Map<String, Object> resultMap = searchSql(searchMap);
		String searchSql = (String) resultMap.get("sql");
		Map<String, Object> params = (Map<String, Object>) resultMap.get("params");
		long countNative = commonDao.queryForCountNative(searchSql, params);
		return countNative;
	}

	public Map<String, Object> searchSql(Map<String, Object> searchMap) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append("  select distinct a.account_id id , a.xxzx_id,a.sjh,a.sfzh,");
		sql.append("  a.atid,a.xm,a.xh,a.xjzt,a.user_type,a.pycc,b.grade_name,");
		sql.append(" c.zymc,g.org_name");

		sql.append("  from gjt_student_info a");
		sql.append("  inner join gjt_grade b");
		sql.append("  on a.nj = b.grade_id");
		sql.append("  inner join gjt_specialty c");
		sql.append("  on a.major = c.specialty_id");
		sql.append("  inner join gjt_org g");
		sql.append("  on g.id = a.xxzx_id");
		sql.append(" inner join gjt_user_account h");
		sql.append(" on a.account_id=h.id ");
		String courseIdAll = (String) searchMap.get("courseIdAll");// 1全部
		String courseIds = (String) searchMap.get("courseIds");

		if (StringUtils.isBlank(courseIdAll)) {
			if (StringUtils.isNotBlank(courseIds)) {
				sql.append("  left join gjt_class_student d ");
				sql.append("  on d.student_id = a.student_id and d.is_deleted = 'N'");
				sql.append("  left join gjt_class_info e ");
				sql.append("  on d.class_id = e.class_id and e.is_deleted = 'N'");
				sql.append("  left join gjt_course f on  e.course_id = f.course_id and f.is_deleted = 'N'");
			}
		}

		sql.append("  where a.is_deleted='N' and b.is_deleted='N' and c.is_deleted='N' and g.is_deleted='N' ");

		String EQ_orgId = (String) searchMap.get("EQ_orgId");
		if (StringUtils.isNotBlank(EQ_orgId)) {
			sql.append("  and a.xxzx_id =:xxzxId");
			params.put("xxzxId", EQ_orgId);
		}
		String xh = (String) searchMap.get("EQ_xh");
		if (StringUtils.isNotBlank(xh)) {
			sql.append("  and a.xh =:xh");
			params.put("xh", xh);
		}

		String xm = (String) searchMap.get("LIKE_xm");
		if (StringUtils.isNotBlank(xm)) {
			sql.append("  and a.xm like :xm");
			params.put("xm", "%" + xm + "%");
		}

		String gradeIdAll = (String) searchMap.get("gradeIdAll");// 1全部
		if (StringUtils.isBlank(gradeIdAll)) {// 不是全部
			String gradeIds = (String) searchMap.get("gradeIds");
			if (StringUtils.isNotBlank(gradeIds)) {// 有选中其中的某个
				String[] split = gradeIds.split(",");
				sql.append("  and a.nj in (:gradeIds)");
				params.put("gradeIds", Arrays.asList(split));
			}
		}

		String pyccIdAll = (String) searchMap.get("pyccIdAll");// 1全部
		if (StringUtils.isBlank(pyccIdAll)) {// 不是全部
			String pyccIds = (String) searchMap.get("pyccIds");
			if (StringUtils.isNotBlank(pyccIds)) {// 有选中其中的某个
				String[] split = pyccIds.split(",");
				sql.append("  and a.pycc in (:pyccIds)");
				params.put("pyccIds", Arrays.asList(split));
			}
		}

		String specialtyIdAll = (String) searchMap.get("specialtyIdAll");// 1全部
		if (StringUtils.isBlank(specialtyIdAll)) {// 不是全部
			String specialtyIds = (String) searchMap.get("specialtyIds");
			if (StringUtils.isNotBlank(specialtyIds)) {// 有选中其中的某个
				String[] split = specialtyIds.split(",");
				sql.append("  and a.major in (:specialtyIds)");
				params.put("specialtyIds", Arrays.asList(split));
			}
		}

		if (StringUtils.isBlank(courseIdAll)) {// 不是全部

			if (StringUtils.isNotBlank(courseIds)) {// 有选中其中的某个
				String[] split = courseIds.split(",");
				sql.append("  and f.course_id in (:courseIds)");
				params.put("courseIds", Arrays.asList(split));
			}
		}

		String userTypeAll = (String) searchMap.get("userTypeAll");// 1全部
		if (StringUtils.isBlank(userTypeAll)) {// 不是全部
			String userTypes = (String) searchMap.get("userTypes");
			if (StringUtils.isNotBlank(userTypes)) {// 有选中其中的某个
				String[] split = userTypes.split(",");
				sql.append("  and a.user_type in (:userTypes)");
				params.put("userTypes", Arrays.asList(split));
			}
		}

		String xjztTypeAll = (String) searchMap.get("xjztTypeAll");// 1全部
		if (StringUtils.isBlank(xjztTypeAll)) {// 不是全部
			String xjztTypes = (String) searchMap.get("xjztTypes");
			if (StringUtils.isNotBlank(xjztTypes)) {// 有选中其中的某个
				String[] split = xjztTypes.split(",");
				sql.append("  and a.xjzt in (:xjztTypes)");
				params.put("xjztTypes", Arrays.asList(split));
			}
		}

		sql.append("  and a.org_id in (select org.id");
		sql.append("  from gjt_org org");
		sql.append("  where org.IS_DELETED = 'N'");
		sql.append("  start with org.ID = :orgId");
		sql.append("  connect by prior ORG.ID =ORG.PERENT_ID )");

		params.put("orgId", searchMap.get("orgId"));
		resultMap.put("sql", sql.toString());
		resultMap.put("params", params);

		return resultMap;
	}

	@Override
	public String checkImportStudent(String xm, String xh, String specialtyName) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append("  select a.account_id");
		sql.append("  from gjt_student_info a");
		sql.append("  inner join gjt_specialty b");
		sql.append("  on a.major = b.specialty_id");
		sql.append("  where a.is_deleted = 'N'");
		sql.append("  and b.is_deleted = 'N'");

		sql.append("  and a.xh =:xh");
		params.put("xh", xh);

		sql.append("  and a.xm = :xm");
		params.put("xm", xm);

		if (StringUtils.isNotBlank(specialtyName)) {
			sql.append("  and b.zymc = :zymc");
			params.put("zymc", specialtyName);
		}

		List<Map<String, Object>> list = commonDao.queryForMapList(sql.toString(), params);
		String userId = "";
		if (EmptyUtils.isNotEmpty(list)) {
			Map<String, Object> map = list.get(0);
			userId = (String) map.get("ACCOUNT_ID");
		}
		return userId;
	}

	@Override
	public List<Map<String, Object>> findOline(String orgId, Map<String, Object> searchMap) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append("  select a.avatar,");
		sql.append("  a.account_id,");
		sql.append("  a.xm,");
		sql.append("  a.xh,");
		sql.append("  a.sjh,");
		sql.append("  a.xjzt,");
		sql.append("  a.pycc,");
		sql.append("  b.grade_name,");
		sql.append("  c.zymc,");
		sql.append("  g.org_name");
		sql.append("  from gjt_student_info a");
		sql.append("  inner join gjt_grade b");
		sql.append("  on a.nj = b.grade_id");
		sql.append("  inner join gjt_specialty c");
		sql.append("  on a.major = c.specialty_id");
		sql.append("  inner join gjt_user_account d");
		sql.append("  on a.account_id = d.id ");
		sql.append("  inner join gjt_org g");
		sql.append("  on g.id = a.xxzx_id");
		sql.append("  where a.is_deleted = 'N'");
		sql.append("  and b.is_deleted = 'N'");
		sql.append("  and c.is_deleted = 'N'");
		sql.append("  and g.is_deleted = 'N'");
		sql.append("  and d.is_online = 'Y'");

		String EQ_orgId = (String) searchMap.get("EQ_orgId");
		if (StringUtils.isNotBlank(EQ_orgId)) {
			sql.append("  and a.xxzx_id =:xxzxId");
			params.put("xxzxId", EQ_orgId);
		}
		String xh = (String) searchMap.get("EQ_xh");
		if (StringUtils.isNotBlank(xh)) {
			sql.append("  and a.xh =:xh");
			params.put("xh", xh);
		}

		String xm = (String) searchMap.get("LIKE_xm");
		if (StringUtils.isNotBlank(xm)) {
			sql.append("  and a.xm like :xm");
			params.put("xm", "%" + xm + "%");
		}

		String gradeIds = (String) searchMap.get("EQ_gjtGrade.gradeId");
		if (StringUtils.isNotBlank(gradeIds)) {
			sql.append("  and a.nj =:gradeIds");
			params.put("gradeIds", gradeIds);
		}

		String pyccIds = (String) searchMap.get("EQ_pycc");
		if (StringUtils.isNotBlank(pyccIds)) {
			sql.append("  and a.pycc =:pyccIds");
			params.put("pyccIds", pyccIds);
		}

		String specialtyIds = (String) searchMap.get("EQ_gjtSpecialty.specialtyId");
		if (StringUtils.isNotBlank(specialtyIds)) {// 有选中其中的某个
			sql.append("  and a.major= :specialtyIds");
			params.put("specialtyIds", specialtyIds);
		}
		String xjztTypes = (String) searchMap.get("EQ_xjzt");
		if (StringUtils.isNotBlank(xjztTypes)) {// 有选中其中的某个
			sql.append("  and a.xjzt =xjztTypes");
			params.put("xjztTypes", xjztTypes);
		}

		sql.append("  and a.org_id in (select org.id");
		sql.append("  from gjt_org org");
		sql.append("  where org.IS_DELETED = 'N'");
		sql.append("  start with org.ID = :orgId");
		sql.append("  connect by prior ORG.ID = ORG.PERENT_ID)");

		params.put("orgId", orgId);
		List<Map<String, Object>> mapList = commonDao.queryForMapList(sql.toString(), params);
		return mapList;
	}

	@Override
	public Page<Map<String, Object>> findImportStudentPage(String orgId, Map<String, Object> searchParams,
			PageRequest pageRequest) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();

		sql.append("  select  distinct a.account_id,");
		sql.append("  d.id,a.avatar,");
		sql.append("  a.xm,");
		sql.append("  a.xh,");
		sql.append("  a.sjh,");
		sql.append("  a.xjzt,");
		sql.append("  a.pycc,");
		sql.append("  b.grade_name,");
		sql.append("  c.zymc,");
		sql.append("  g.org_name");
		sql.append("  from gjt_student_info a");
		sql.append("  inner join gjt_grade b");
		sql.append("  on a.nj = b.grade_id");
		sql.append("  inner join gjt_specialty c");
		sql.append("  on a.major = c.specialty_id");
		sql.append("  inner join gjt_message_inport_user d");
		sql.append("  on a.account_id = d.user_id ");
		sql.append("  inner join gjt_org g");
		sql.append("  on g.id = a.xxzx_id");
		sql.append("  where a.is_deleted = 'N'");
		sql.append("  and b.is_deleted = 'N'");
		sql.append("  and c.is_deleted = 'N'");
		sql.append("  and g.is_deleted = 'N'");

		String EQ_orgId = (String) searchParams.get("EQ_orgId");
		if (StringUtils.isNotBlank(EQ_orgId)) {
			sql.append("  and a.xxzx_id =:xxzxId");
			params.put("xxzxId", EQ_orgId);
		}
		String xh = (String) searchParams.get("EQ_xh");
		if (StringUtils.isNotBlank(xh)) {
			sql.append("  and a.xh =:xh");
			params.put("xh", xh);
		}

		String xm = (String) searchParams.get("LIKE_xm");
		if (StringUtils.isNotBlank(xm)) {
			sql.append("  and a.xm like :xm");
			params.put("xm", "%" + xm + "%");
		}

		String gradeIds = (String) searchParams.get("EQ_gjtGrade.gradeId");
		if (StringUtils.isNotBlank(gradeIds)) {
			sql.append("  and a.nj =:gradeIds");
			params.put("gradeIds", gradeIds);
		}

		String pyccIds = (String) searchParams.get("EQ_pycc");
		if (StringUtils.isNotBlank(pyccIds)) {
			sql.append("  and a.pycc =:pyccIds");
			params.put("pyccIds", pyccIds);
		}

		String specialtyIds = (String) searchParams.get("EQ_gjtSpecialty.specialtyId");
		if (StringUtils.isNotBlank(specialtyIds)) {
			sql.append("  and a.major= :specialtyIds");
			params.put("specialtyIds", specialtyIds);
		}
		String xjztTypes = (String) searchParams.get("EQ_xjzt");
		if (StringUtils.isNotBlank(xjztTypes)) {
			sql.append("  and a.xjzt =xjztTypes");
			params.put("xjztTypes", xjztTypes);
		}

		String newMessageId = (String) searchParams.get("newMessageId");
		sql.append("  and d.message_Id =:newMessageId ");
		params.put("newMessageId", newMessageId);

		sql.append("  and a.org_id in (select org.id");
		sql.append("  from gjt_org org");
		sql.append("  where org.IS_DELETED = 'N'");
		sql.append("  start with org.ID = :orgId");
		sql.append("  connect by prior ORG.ID = ORG.PERENT_ID)");

		params.put("orgId", orgId);
		Page<Map<String, Object>> page = commonDao.queryForPageNative(sql.toString(), params, pageRequest);
		return page;
	}

	/**
	 * 学员综合信息
	 *
	 * @return
	 */
	public Page<StudentSynthesizeInfoDto> queryStudentSynthesizeListByPage(Map searchParams, PageRequest pageRequst) {
		Page<Map<String, Object>> list = studentInfoDao.findStudentSynthesizeListByPage(searchParams, pageRequst);
		List<StudentSynthesizeInfoDto> dtoList = new ArrayList<StudentSynthesizeInfoDto>();
		for (Iterator<Map<String, Object>> iter = list.iterator(); iter.hasNext();) {
			Map<String, Object> info = iter.next();
			StudentSynthesizeInfoDto dto = GsonUtils.toBean(
					info.get("SYNTHESIZE_INFO") != null
							? ClobConvertUtil.clobToString((Clob) info.get("SYNTHESIZE_INFO")) : "{}",
					StudentSynthesizeInfoDto.class);
			dto.setStudentId((String) info.get("STUDENT_ID"));
			dto.setZp((String) info.get("ZP"));
			dto.setXm((String) info.get("XM"));
			dto.setXh((String) info.get("XH"));
			dto.setSjh((String) info.get("SJH"));
			dto.setUserType((String) info.get("USER_TYPE"));
			dto.setXjzt((String) info.get("XJZT"));
			dto.setYearName((String) info.get("YEAR_NAME"));
			dto.setGradeName((String) info.get("GRADE_NAME"));
			dto.setPycc((String) info.get("PYCC"));
			dto.setZymc((String) info.get("ZYMC"));
			dto.setScName((String) info.get("SC_NAME"));
			dto.setStatus(info.get("STATUS") != null ? ((BigDecimal) info.get("STATUS")).intValue() : null);
			dtoList.add(dto);
		}
		return new PageImpl<StudentSynthesizeInfoDto>(dtoList, pageRequst, list.getTotalElements());
	}

	/**
	 * 学员综合信息
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, String>> downLoadStudentSynthesizeListExportXls(Map searchParams) {
		return null;
	}

	@Override
	public List<GjtStudentInfo> findByXxIdAndIsDeleted(String xxId, String booleanNo) {
		return gjtStudentInfoDao.findByXxIdAndIsDeleted(xxId, booleanNo);
	}

	@Override
	public List<Map<String, Object>> queryStudentInfo() {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select gsi.xm,gsi.student_id,gsi.xh,");
		sql.append("  gsi.sfzh,");
		sql.append("  gsi.sjh,");
		sql.append("  gua.login_account,");
		sql.append("  gsi.atid,");
		sql.append("  gsi.dzxx,");
		sql.append("  gsi.csrq,");
		sql.append("  gsi.xbm,");
		sql.append("  gsi.txdz,");
		sql.append("  gsi.grade_specialty_id,");
		sql.append("  gs.order_sn");
		sql.append("  from gjt_student_info gsi");
		sql.append("  inner join gjt_user_account gua");
		sql.append("  on gua.id = gsi.account_id");
		sql.append("  left join gjt_signup gs");
		sql.append("  on gs.student_id = gsi.student_id");
		sql.append("  where gsi.is_deleted = 'N'");
		sql.append("  and gsi.xx_id = '2f5bfcce71fa462b8e1f65bcd0f4c632'");
		List<Map<String, Object>> mapList = commonDao.queryForMapList(sql.toString(), params);
		return mapList;
	}

	@Override
	public List<Map<String, String>> queryBySfzh(List<String> cardNos) {
		StringBuffer sql = new StringBuffer();
		sql.append("select student_Id \"studentId\",sfzh \"cardNo\" from gjt_student_info where sfzh in(:cardNos)");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cardNos", cardNos);
		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	@Override
	public void updatePhoto(String studentId, String photoUrl) {
		gjtStudentInfoDao.updatePhoto(studentId, photoUrl);
	}

}
