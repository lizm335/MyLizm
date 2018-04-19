/**
 * @Package com.gzedu.xlims.serviceImpl 
 * @Project com.gzedu.xlims.biz
 * @File TestServiceImpl.java
 * @Date:2016年4月18日下午5:11:41
 * @Copyright (c) 2016, eenet.com All Rights Reserved.
 *
*/

package com.gzedu.xlims.serviceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.graduation.GjtApplyDegreeCertifDao;
import com.gzedu.xlims.pojo.graduation.GjtApplyDegreeCertif;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyCertif;
import com.gzedu.xlims.serviceImpl.exam.GjtExamStudentArrangesServiceImpl;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.pojo.status.RoleType;
import com.gzedu.xlims.pojo.system.StudyYear;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;

/**
 * 
 * 功能说明：公共MAP
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月26日
 * @version 2.5
 *
 */
@Service
public class CommonMapServiceImpl implements CommonMapService {

	@PersistenceContext(unitName = "entityManagerFactory")
	protected EntityManager em;

	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Autowired
	CacheService cacheService;

	@Autowired
	private GjtApplyDegreeCertifDao gjtApplyDegreeCertifDao;

	// 常用的map 查询，注意：需要返回 id,name
	public static final String PYCC_MAP = "SELECT CODE ID,NAME FROM TBL_SYS_DATA WHERE  TBL_SYS_DATA.TYPE_CODE ='TrainingLevel' AND is_deleted = 'N' ";
	public static final String COURSE_TYPE = "SELECT  ID,NAME FROM TBL_SYS_DATA WHERE  TBL_SYS_DATA.TYPE_CODE ='CourseType' AND is_deleted = 'N' ";
	public static final String ROLL_TYPE_MAP = "SELECT CODE ID,NAME FROM TBL_SYS_DATA WHERE  TBL_SYS_DATA.TYPE_CODE ='StudentNumberStatus' AND is_deleted = 'N' ";

	/**
	 * 返回获取所有上级机构的ID的sql语句
	 * 
	 * @param childrenId
	 * @return
	 */
	private String getParentOrgIds(String childrenId) {
		return " select org.ID from GJT_ORG org where org.IS_DELETED='N' start with org.ID = '" + childrenId
				+ "' connect by prior ORG.PERENT_ID = ORG.ID ";
	}

	/**
	 * 返回获取所有下级机构的ID的sql语句
	 * 
	 * @param parentId
	 * @return
	 */
	private String getChildrenOrgIds(String parentId) {
		return " select org.ID from GJT_ORG org where org.IS_DELETED='N' start with org.ID = '" + parentId
				+ "' connect by prior ORG.ID = ORG.PERENT_ID ";
	}

	@Override
	public Map<String, String> getOrgMap() {
		String sql = "select a.id ,a.org_name as name from gjt_org a where a.is_deleted = 'N'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getOrgMapByType(String orgType) {
		String sql = "select a.id ,a.org_name as name from gjt_org a where a.is_deleted = 'N' and a.org_type='"
				+ orgType + "'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getChildOrgMap(String orgId, String orgTypes) {
		String sql = "select a.id ,a.org_name as name from gjt_org a where a.is_deleted = 'N' and org_type in ("
				+ orgTypes + ") START WITH id = '" + orgId + "' CONNECT BY PRIOR id = perent_id";
		return getMap(sql);
	}

	/**
	 * 查询管理员所在的院校信息 org_type=1
	 */
	@Override
	public Map<String, String> getOrgMap(String userId) {
		String sql = "select a.id ,a.org_name as name,a.perent_id from gjt_org a,gjt_user_account b where a.is_deleted = 'N' and "
				+ "(a.id=b.org_id or a.perent_id = b.org_id) and (a.org_type='1' or a.org_type='2') and b.id = '"
				+ userId + "' order by a.perent_id desc";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getOrgMapByOrgId(String orgId) {
		String sql = "select a.id ,a.org_name as name,a.perent_id from gjt_org a where a.is_deleted = 'N' and (a.perent_id ='"
				+ orgId + "' or a.id='" + orgId + "') order by a.perent_id desc";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getOrgMapBy(String userId, boolean isChild) {
		String sql;
		if (!isChild) {
			sql = "select a.id ,a.org_name as name,a.perent_id from gjt_org a,gjt_user_account b where a.id=b.org_id and a.is_deleted = 'N' and b.id = '"
					+ userId + "'";
		} else {
			sql = "select a.id ,a.org_name as name,a.perent_id,b.login_account from gjt_org a,gjt_user_account b where a.is_deleted = 'N' and b.id = '"
					+ userId + "' and (a.id = b.org_id or a.perent_id = b.org_id) order by a.perent_id desc";
		}
		return getMap(sql);
	}

	@Override
	public Map<String, String> getOrgMagagerRoleMap() {
		List<String> roleCodes = new ArrayList<String>();
		roleCodes.add(RoleType.院校管理员.getCode());
		roleCodes.add(RoleType.分校管理员.getCode());
		roleCodes.add(RoleType.教务管理员.getCode());
		roleCodes.add(RoleType.考务管理员.getCode());
		roleCodes.add(RoleType.教学点管理员.getCode());
		String codes = "";
		for (String code : roleCodes) {
			codes += "'" + code + "',";
		}
		codes = codes.substring(0, codes.length() - 1);
		String sql = "select role_id as id,role_name as name from pri_role_info where role_code in (" + codes + ")";
		return getMap(sql);
	}

	public Map<String, String> getOrgTree(String parentId, boolean onlySchool) {
		String sql = "SELECT ID, CASE WHEN ORG_TYPE = '2' THEN '——'||ORG_NAME "
				+ " WHEN ORG_TYPE = '3' THEN '————'||ORG_NAME "
				+ " WHEN ORG_TYPE = '6' THEN '——————'||ORG_NAME ELSE ORG_NAME END NAME "
				+ " FROM GJT_ORG a WHERE IS_DELETED = 'N' AND PERENT_ID != ID START WITH ID = '" + parentId
				+ "' CONNECT BY PRIOR ID = PERENT_ID AND PERENT_ID != ID "
				+ " ORDER BY (select REVERSE(to_char(wm_concat(x.id))) from GJT_ORG x START WITH x.id=a.id CONNECT BY PRIOR x.PERENT_ID=x.ID)"; // 层级排序
		if (onlySchool) {
			sql += " AND ORG_TYPE = 1";
		}
		return getMap(sql);
	}

	@Override
	public Map<String, String> getStudyCenterMap(String orgId) {// 获取改院校下面的已经审核并且启用的学习中心
		String sql = "select a.id, case  when a.org_type = '3' then  a.org_name  when a.org_type = '6' then '——' || a.org_name"
				+ " else a.org_name end name, level from gjt_org a left join gjt_study_center b"
				+ " on a.id = b.id where a.is_deleted = 'N' and b.is_deleted = 'N' and b.audit_status = '1'"
				+ "  and b.Is_Enabled = '1' and (a.org_type = '3' or a.org_type = '6') "
				+ " start with a.id = '" + orgId + "' connect by prior a.id = a.perent_id"
				+ " ORDER BY (select REVERSE(to_char(wm_concat(x.id))) from GJT_ORG x START WITH x.id=a.id CONNECT BY PRIOR x.PERENT_ID=x.ID)"; // 层级排序
		return getMap(sql);
	}

	@Override
	public Map<String, String> getStudyCenterTypeThreeMap(String orgId) {// 获取改院校下面的已经审核并且启用的学习中心
		String sql = "select a.id, a.org_name name, level from gjt_org a left join gjt_study_center b"
				+ " on a.id = b.id where a.is_deleted = 'N' and b.is_deleted = 'N' and b.audit_status = '1'"
				+ "  and b.Is_Enabled = '1' and a.org_type = '3' "
				+ " start with a.id = '" + orgId + "' connect by prior a.id = a.perent_id";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getxxIdByxxzxId(String xxzxId) {
		String sql = "SELECT gog.ID id,gog.ORG_NAME name FROM GJT_ORG gog WHERE gog.ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH "
				+ "org.ID ='" + xxzxId
				+ "' CONNECT BY PRIOR ORG.PERENT_ID =ORG.ID) AND gog.ORG_TYPE='1' AND ROWNUM = 1";
		return getMap(sql);
	}

	public Map<String, String> getStudyCenter() {
		String sql = "select a.ID, a.SC_NAME NAME  from GJT_STUDY_CENTER a, gjt_org b  where a.id = b.id"
				+ "  and a.is_deleted = 'N'  and b.org_type = 3  and b.is_deleted = 'N' ";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getSpecialtyMap(String orgId) {
		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);
		String sql = "select SPECIALTY_ID  ID, ZYMC || '(' || RULE_CODE || ')'  NAME from GJT_SPECIALTY where is_deleted = 'N' and is_enabled = '1' "
				+ " and xx_id='" + xxId + "'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getSpecialtyMapBySpecialtyBaseId(String specialtyBaseId) {
		String sql = "select SPECIALTY_ID  ID, ZYMC || '(' || RULE_CODE || ')'  NAME from GJT_SPECIALTY where is_deleted = 'N' and is_enabled = '1' "
				+ " and specialty_base_id = '" + specialtyBaseId + "'";
		return getMap(sql);
	}

	public Map<String, String> getSpecialtyCodeMap(String orgId) {
		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);
		String sql = "select GS.SPECIALTY_ID ID, GS.ZYMC || '(' || GSB.SPECIALTY_CODE || ')' NAME from GJT_SPECIALTY GS LEFT JOIN GJT_SPECIALTY_BASE GSB ON GS.SPECIALTY_BASE_ID = GSB.SPECIALTY_BASE_ID AND GSB.IS_DELETED = 'N' where GS.is_deleted = 'N' and GS.is_enabled = '1' "
				+ " and GS.xx_id='" + xxId + "'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getSpecialtyBaseMap(String orgId) {
		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);
		String sql = "SELECT SPECIALTY_BASE_ID  ID, SPECIALTY_NAME || '[' || (select t.name from tbl_sys_data t where t.is_deleted='N' and t.type_code='TrainingLevel' and t.code=SPECIALTY_LAYER) || ']' || '(' || SPECIALTY_CODE || ')'  NAME from GJT_SPECIALTY_BASE where is_deleted = 'N' "
				+ " and XX_ID='" + xxId + "'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getSchoolModelSpecialtyMap(String orgId) {
		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);
		String sql = "select SPECIALTY_ID  ID,NAME from GJT_SPECIALTY_COLLEGE "
				+ " where ORG_ID='" + xxId + "'";
		return getMap(sql);
	}

	// 最新年级按照学年度时间来排序
	@Override
	public Map<String, String> getGradeMap(String orgId) {
		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);
		String sql = "select g.Grade_Id ID,g.Grade_Name NAME from gjt_grade g where g.is_deleted = 'N' and g.is_enabled = '1' and "
				+ " g.xx_id='" + xxId + "' order by g.start_date desc";
		return getMap(sql);
	}

	public Map<String, String> getXjzt() {
		String sql = "SELECT TSD.CODE ID, TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'StudentNumberStatus'";
		return getMap(sql);
	}

	public Map<String, String> getChargeMap() {
		String sql = "SELECT TSD.CODE ID, TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'CHARGE_STATE'";
		return getMap(sql);
	}

	public Map<String, String> getAuditMap() {
		String sql = "SELECT TSD.CODE ID, TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'AUDIT_STATE'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getPyccMap() {
		Map<String, String> pyccMap = new LinkedHashMap<String, String>();
		Map<String, String> map = cacheService.getMapCachedDictionary(CacheService.DictionaryKey.TRAININGLEVEL);
		// 只取本科和专科
		Set<String> keys = map.keySet();
		for (String key : keys) {
			if ("0".equals(key) || "2".equals(key)) {
				pyccMap.put(key, map.get(key));
			}
		}

		return pyccMap;
	}

	@Override
	public Map<String, String> getPyccMap(String orgId) {
		return getMap(PYCC_MAP + " AND (ORG_ID IS NULL OR ORG_ID='" + orgId + "')");
	}

	@Override
	public Map<BigDecimal, String> getExamTypeMap() {
		// 因为其他表的这个类型是BigDecimal，只有转换成BigDecimal，页面才能显示值
		Map<String, String> map = cacheService.getMapCachedDictionary(CacheService.DictionaryKey.EXAMINATIONMODE);
		Map<BigDecimal, String> resultMap = new LinkedHashMap<BigDecimal, String>();
		for (Map.Entry<String, String> item : map.entrySet()) {
			resultMap.put(new BigDecimal(item.getKey()), item.getValue());
		}
		return resultMap;
	}

	@Override
	public Map getExamTypeMapNew() {
		// 因为其他表的这个类型是BigDecimal，只有转换成BigDecimal，页面才能显示值
		Map<String, String> map = cacheService.getMapCachedDictionary(CacheService.DictionaryKey.EXAMINATIONMODE);
		Map<BigDecimal, String> resultMap = new LinkedHashMap<BigDecimal, String>();
		for (Map.Entry<String, String> item : map.entrySet()) {
			resultMap.put(new BigDecimal(item.getKey()), item.getValue());
		}
		return resultMap;
	}

	@Override
	public Map<Integer, String> getExamTypeIntMap() {
		Map<String, String> map = cacheService.getMapCachedDictionary(CacheService.DictionaryKey.EXAMINATIONMODE);
		Map<Integer, String> resultMap = new LinkedHashMap<Integer, String>();
		for (Map.Entry<String, String> item : map.entrySet()) {
			resultMap.put(new Integer(item.getKey()), item.getValue());
		}
		return resultMap;
	}

	@Override
	public Map<String, String> getCourseTypeMap() {
		return cacheService.getMapCachedDictionary(CacheService.DictionaryKey.COURSETYPE);
	}

	@Override
	public Map<String, String> getRollTypeMap() {
		return cacheService.getMapCachedDictionary(CacheService.DictionaryKey.STUDENTNUMBERSTATUS);
	}

	@Override
	public Map<String, String> getEmployeeMap(String orgId, EmployeeTypeEnum employeeType) {
		String sql = "select employee_id id,xm name from gjt_employee_info  where  employee_type="
				+ employeeType.getNum() + " and is_deleted = 'N' and xxzx_id  in "
				+ "   (select org.id from gjt_org org where org.is_deleted='N' start with org.id= '" + orgId
				+ "' connect by prior org.id=org.perent_id) ";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getClassInfoMap(String orgId, String classType) {
//		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);
		String sql = "select gci.class_id id,gci.bjmc name from gjt_class_info gci  where gci.is_deleted = 'N' and gci.is_enabled = '1'"
				+ " and gci.class_type='" + classType + "'" + " and (gci.xx_id='" + orgId
				+ "' or gci.xxzx_id in (select org.id from gjt_org org where org.is_deleted='N' start with org.id= '"
				+ orgId + "' connect by prior org.id=org.perent_id))";
		return getMap(sql);
	}

	/**
	 * 获取院校课程班
	 *
	 * @param orgId
	 * @param courseId
	 * @return
	 */
	@Override
	public Map<String, String> getCourseClassInfoMap(String orgId, String courseId) {
		String sql = " SELECT gci.CLASS_ID ID,gci.BJMC NAME FROM GJT_CLASS_INFO gci WHERE gci.CLASS_TYPE = 'course' AND gci.IS_DELETED = 'N' AND gci.COURSE_ID = '"
				+ courseId + "'" + " AND(gci.XX_ID = '" + orgId
				+ "' OR gci.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID = '"
				+ orgId + "' CONNECT BY PRIOR ORG.PERENT_ID = ORG.ID))";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getCourseMap(String orgId) {
	    String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);
		String sql = "select course_id id,kcmc||'（课程号：'||t.kch||'）' name from gjt_course t" +
				" where is_deleted = 'N' " +
				" and (XX_ID='" + xxId + "'" +
                "     or exists (select x.course_id from Gjt_Course_Ownership x where x.is_deleted='N' and x.org_id='" + xxId + "' and x.course_id=t.course_id))" +
				"order by kcmc";
		return getMap(sql);
	}

	@Override
	public Map<Integer, String> getStudyYearMap() {
		Map<Integer, String> m = StudyYear.getList();
		return m;
	}

	@Override
	public Map<String, String> getMap(String sql) {
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Object> rows = query.getResultList();
		Map<String, String> resultMap = new LinkedHashMap<String, String>();
		for (Object obj : rows) {
			Map<String, String> m = (Map<String, String>) obj;
			resultMap.put(m.get("ID"), m.get("NAME"));
		}
		return resultMap;
	}

	@Override
	public Map<String, String> getDates(String typeCode) {
		return cacheService.getMapCachedDictionary(typeCode);
	}

	@Override
	public Map<Integer, String> getDatesBy(String typeCode) {
		Map<String, String> mapCachedDictionary = cacheService.getMapCachedDictionary(typeCode);
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (Map.Entry<String, String> item : mapCachedDictionary.entrySet()) {
			map.put(Integer.valueOf(item.getKey()), item.getValue());
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> getDateList(String typeCode) {
		return cacheService.getListCachedDictionary(typeCode);
	}

	@Override
	public Map<String, String> getIdNameMap(String typeCode) {
		String sql = "SELECT ID,NAME FROM TBL_SYS_DATA WHERE is_deleted = 'N' and type_code = '" + typeCode + "' ";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getCategoryDates() {
		String sql = "SELECT ID,lpad('　',level*4,'　')||NAME NAME FROM GJT_ARTICLE_CATEGORY WHERE is_deleted = 'N' connect by PARENT_ID =prior ID start with PARENT_ID is null";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getMenuDates(String xx_id) {
		String sql = "SELECT ID,lpad('　',level*4,'　')||NAME NAME FROM GJT_MENU WHERE xx_id = '" + xx_id
				+ "' connect by PID =prior ID start with PID is null";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getMenuByName(String name) {
		String sql = "SELECT ID,NAME FROM GJT_MENU WHERE NAME LIKE '%" + name + "%' ";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getSpecialtyDates(String specialtyId) {
		String sql = "SELECT SPECIALTY_ID ID,ZYMC NAME from GJT_SPECIALTY WHERE SPECIALTY_ID = '" + specialtyId + "'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getGradeDates(String GRADE_ID) {
		String sql = "SELECT GRADE_ID ID,GRADE_NAME NAME from GJT_GRADE WHERE GRADE_ID = '" + GRADE_ID + "'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getXxmcDates(String xxId) {
		String sql = "SELECT ID,XXMC NAME from gjt_school_info WHERE id = '" + xxId + "'";
		return getMap(sql);
	}

	/*
	 * 查询除本身之外的院校
	 */
	@Override
	public Map<String, String> getXxmcDatesExceptBS(String xxId) {
		String sql = "SELECT ID,XXMC NAME from gjt_school_info WHERE id != '" + xxId + "' and is_deleted = 'N'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getXxmcAllDates() {
		String sql = "SELECT ID,XXMC NAME from gjt_school_info WHERE IS_ENABLED = '1'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getXxzxmcDates(String id) {
		String sql = "SELECT ID,SC_NAME NAME from GJT_STUDY_CENTER WHERE is_deleted = 'N' and id = '" + id + "'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getStudyYearMap(String xxid) {
		String sql = "SELECT ID, STUDY_YEAR_NAME AS NAME FROM GJT_STUDYYEAR_INFO WHERE XX_ID = '" + xxid
				+ "' order by STUDY_YEAR_CODE";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getGradeMapData(String xxid) {
		String sql = "SELECT GRADE_ID AS ID,GRADE_NAME AS NAME FROM GJT_GRADE WHERE IS_DELETED = 'N' AND XX_ID ='"
				+ xxid + "' ORDER BY CREATED_DT DESC";
		return getMap(sql);
	}

	@Override
	public Map<Integer, String> getStudyYearCodeNameMap(String xxid) {
		String sql = "SELECT STUDY_YEAR_CODE AS ID, STUDY_YEAR_NAME AS NAME FROM GJT_STUDYYEAR_INFO WHERE XX_ID = '"
				+ xxid + "' order by STUDY_YEAR_CODE";

		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Object> rows = query.getResultList();
		Map<Integer, String> resultMap = new LinkedHashMap<Integer, String>();
		for (Object obj : rows) {
			Map<String, Object> m = (Map<String, Object>) obj;
			resultMap.put(((BigDecimal) m.get("ID")).intValue(), m.get("NAME").toString());
		}
		return resultMap;
	}

	// 获取当前批次
	@Override
	public String getCurrentGjtExamBatchNew(String xxid) {
		String sql = "SELECT EXAM_BATCH_CODE  FROM GJT_EXAM_BATCH_NEW WHERE IS_DELETED=0 AND PLAN_STATUS=3 AND XX_ID in ("
				+ getParentOrgIds(xxid) + ") order by CREATED_DT desc";
		String code = null;
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Object> rows = query.getResultList();
		if (EmptyUtils.isNotEmpty(rows) && EmptyUtils.isNotEmpty(rows.get(0))) {
			Map re = (Map) rows.get(0);
			code = ObjectUtils.toString(re.get("EXAM_BATCH_CODE"));
		}
		return code;
	}

	public String getCurrentGjtExamBatchNewId(String xxid) {
		String sql = "SELECT EXAM_BATCH_ID  FROM GJT_EXAM_BATCH_NEW WHERE IS_DELETED=0 AND PLAN_STATUS=3 AND XX_ID in ("
				+ getParentOrgIds(xxid) + ") order by CREATED_DT desc";
		String id = null;
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Object> rows = query.getResultList();
		if (EmptyUtils.isNotEmpty(rows) && EmptyUtils.isNotEmpty(rows.get(0))) {
			Map re = (Map) rows.get(0);
			id = ObjectUtils.toString(re.get("EXAM_BATCH_ID"));
		}
		return id;
	}

	@Override
	public Map<String, String> getGjtExamBatchNewIdNameMap(String xxid) {
		String sql = "SELECT EXAM_BATCH_CODE AS ID, NAME FROM GJT_EXAM_BATCH_NEW WHERE IS_DELETED=0 AND PLAN_STATUS=3 AND XX_ID in ("
				+ getParentOrgIds(xxid) + ") order by CREATED_DT desc";
		return getMap(sql);
	}

	public Map<String, String> getAllExamBatchs(String xxid) {
		String sql = "SELECT GEB.EXAM_BATCH_ID AS ID,GEB.NAME FROM GJT_EXAM_BATCH_NEW GEB WHERE GEB.IS_DELETED = 0 AND GEB.XX_ID in ("
				+ getParentOrgIds(xxid) + ")";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getGjtExamBatchNewMap(String xxid) {
		// String sql = "select exam_batch_id id,name || '(' ||
		// exam_batch_code|| ')' as name from gjt_exam_batch_new WHERE
		// IS_DELETED=0 AND XX_ID = '" + xxid + "'";
		String sql = "select exam_batch_id id,name || '(' || exam_batch_code|| ')' as name from gjt_exam_batch_new  WHERE IS_DELETED=0 AND XX_ID in ("
				+ getParentOrgIds(xxid) + ")";
		return getMap(sql);
	}

	// 地区 列表
	@Override
	public Map<String, String> getAreaMap() {
		String sql = "SELECT t.ID,t.NAME FROM GJT_DISTRICT t WHERE ID != '1' ORDER BY t.ID";
		return getMap(sql);
	}

	// 省 列表
	@Override
	public Map<String, String> getProvinceMap() {
		String sql = "SELECT t.ID,t.NAME FROM GJT_DISTRICT t WHERE ID like '%0000' ORDER BY ID";
		return getMap(sql);
	}

	// 县级列表 by 省CODE
	@Override
	public Map<String, String> getCityMap(String provinceCode) {
		String code = provinceCode.substring(0, 2);
		String sql = "SELECT t.ID,t.NAME FROM GJT_DISTRICT t WHERE ID like '" + code + "%00' and ID != '" + provinceCode
				+ "' ORDER BY ID";
		return getMap(sql);
	}

	// 县级列表 by 省CODE
	@Override
	public Map<String, String> getDistrictMap(String cityCode) {
		String code = cityCode.substring(0, 4);
		String sql = "SELECT t.ID,t.NAME FROM GJT_DISTRICT t where ID like '" + code + "%' and ID != '" + cityCode
				+ "' ORDER BY ID";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getExamPointMap(String xxid) {
		String sql = "select exam_point_id as id, name from gjt_exam_point_new where xx_id='" + xxid + "'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getGjtDistrictMap() {
		return getAreaMap();
	}

	@Override
	public Map<String, String> getGjtExamSubjectNewIdNameMap(String xxid, int examType) {
		// String sql = "SELECT SUBJECT_CODE AS ID, NAME FROM
		// GJT_EXAM_SUBJECT_NEW WHERE IS_DELETED=0 AND XX_ID = '" + xxid + "'
		// AND TYPE = '" + examType + "'";
		String sql = "SELECT SUBJECT_CODE AS ID, NAME FROM GJT_EXAM_SUBJECT_NEW WHERE IS_DELETED=0 AND XX_ID = '" + xxid
				+ "' AND TYPE = '" + examType + "'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getExamPointMapByBatchCode(String examBatchCode) {
		String sql = "select exam_point_id as id, name from gjt_exam_point_new where is_deleted='N' and exam_batch_code='"
				+ examBatchCode + "'";
		return getMap(sql);
	}

	/*
	 * 查询除本身之外的班级
	 */
	@Override
	public Map<String, String> getBjmcDatesExceptBS(String classId, String orgId) {
		String sql = "SELECT t1.CLASS_ID ID, t1.BJMC NAME from gjt_class_info t1, gjt_class_info t2 "
				+ " WHERE t2.CLASS_ID = '" + classId + "' and t1.course_id = t2.course_id and "
				+ " t1.actual_grade_id = t2.actual_grade_id and t1.class_id != '" + classId + "'" + " and t1.ORG_ID = '"
				+ orgId + "' and t1.class_type = 'course' and t1.is_deleted = 'N'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getBjmcDatesExceptBS(GjtClassInfo gjtClassInfo, String orgId) {
		String sql = "SELECT CLASS_ID ID, BJMC NAME from gjt_class_info " + " where class_id != '"
				+ gjtClassInfo.getClassId() + "' and grade_id= '" + gjtClassInfo.getGradeId()
				+ "' and class_type = 'teach' and is_deleted = 'N'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getResponsibleMap() {
		String sql = "select CODE ID,NAME NAME from TBL_SYS_DATA where TYPE_CODE = 'Responsible' and IS_DELETED = 'N' order by ORD_NO";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getTermMap(String xxid) {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT GTI.TERM_ID ID, GTI.TERM_NAME NAME");
		sql.append("  FROM GJT_TERM_INFO GTI");
		sql.append("  WHERE GTI.IS_DELETED = 'N'");
		sql.append("  AND GTI.IS_ENABLED = '1'");
		sql.append("  AND GTI.XX_ID = '" + xxid + "'");
		sql.append("  ORDER BY GTI.TERM_NAME DESC");

		return getMap(sql.toString());
	}

	@Override
	public Map<String, String> getTextbookPlanMap(String orgId) {
		String sql = "select PLAN_ID id,PLAN_NAME name from GJT_TEXTBOOK_PLAN where ORG_ID in ("
				+ getParentOrgIds(orgId) + ") and IS_DELETED = 'N' and STATUS = 3 order by CREATED_DT desc";
		return getMap(sql);
	}

	@Override
	public Map<String, Map<String, String>> getExsubjectAndkindMap() {
		Map<String, Map<String, String>> map = new LinkedHashMap<String, Map<String, String>>();
		for (CacheService.ExsubjectValue exsubjectValue : CacheService.Dictionary.dicExsubjectList) {
			if (exsubjectValue.getDicExsubjectkindList() != null) {
				Map<String, String> map2 = new LinkedHashMap<String, String>();
				for (CacheService.Value value : exsubjectValue.getDicExsubjectkindList()) {
					map2.put(value.getCode(), value.getName());
				}
				map.put(exsubjectValue.getCode(), map2);
			} else {
				map.put(exsubjectValue.getCode(), new LinkedHashMap<String, String>());
			}
		}

		return map;
	}

	@Override
	public Map<String, String> getExsubjectMap() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (CacheService.ExsubjectValue exsubjectValue : CacheService.Dictionary.dicExsubjectList) {
			map.put(exsubjectValue.getCode(), exsubjectValue.getName());
		}

		return map;
	}

	@Override
	public Map<String, String> getExsubjectkindMap(String exsubjectKey) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		if (exsubjectKey == null) {
			return map;
		}
		for (CacheService.ExsubjectValue exsubjectValue : CacheService.Dictionary.dicExsubjectList) {
			if (exsubjectKey.equals(exsubjectValue.getCode())) {
				if (exsubjectValue.getDicExsubjectkindList() != null) {
					for (CacheService.Value value : exsubjectValue.getDicExsubjectkindList()) {
						map.put(value.getCode(), value.getName());
					}
				}
				break;
			}
		}

		return map;
	}

	@Override
	public List<CacheService.Value> getExsubjectkindList(String exsubjectKey) {
		List<CacheService.Value> list = new ArrayList<CacheService.Value>();
		if (exsubjectKey == null) {
			return list;
		}
		for (CacheService.ExsubjectValue exsubjectValue : CacheService.Dictionary.dicExsubjectList) {
			if (exsubjectKey.equals(exsubjectValue.getCode())) {
				if (exsubjectValue.getDicExsubjectkindList() != null) {
					list = exsubjectValue.getDicExsubjectkindList();
				}
				break;
			}
		}

		return list;
	}

	@Override
	public Map<String, String> getYearMap(String orgId) {
		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);
		String sql = "select t.grade_id ID,t.name from gjt_year t"
				+ " where t.org_id='" + xxId + "' order by t.created_dt desc";
		return getMap(sql);
	}

	/**
	 * 获取当前学期
	 *
	 * @param orgId
	 * @return
	 */
	@Override
	public Map<String, String> getCurrentGradeMap(String orgId) {
		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);
		String sql = "SELECT gg.GRADE_ID ID,gg.GRADE_NAME NAME, TO_CHAR(gg.END_DATE, 'yyyy-mm-dd') END_DATE FROM GJT_GRADE gg WHERE gg.IS_DELETED='N' AND  SYSDATE BETWEEN gg.START_DATE AND NVL( gg.END_DATE, ADD_MONTHS(gg.START_DATE, 4 )) AND"
				+ " gg.XX_ID='"	+ xxId + "'";
		return getMap(sql);
	}

	/**
	 * 企业大学接口--根据学习中心CODE查询院校ID
	 */
	public Map<String, String> getxxIdByCode(String prams) {
		String sql = "SELECT gog.ID id,gog.ORG_NAME name FROM GJT_ORG gog WHERE gog.ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH "
				+ "org.CODE ='" + prams
				+ "' CONNECT BY PRIOR ORG.PERENT_ID =ORG.ID) AND gog.ORG_TYPE='1' AND ROWNUM = 1";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getXxzxIdByXxzxCode(String orgCode) {
		String sql = "SELECT gog.ID ID,gog.ORG_NAME name FROM GJT_ORG gog WHERE gog.IS_DELETED='N' AND  gog.CODE='"
				+ orgCode + "'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getGraduationPlanMap(String orgId) {
		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);
		String sql = "select t.id,t.gra_plan_title name from gjt_graduation_plan t where t.is_deleted='N' and t.xx_id='" + xxId + "' order by t.created_dt desc";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getGraduationGradeMap(String orgId) {
		String xxId = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(orgId);
		String sql = "select g.Grade_Id ID,g.Grade_Name NAME from gjt_grade g inner join gjt_graduation_plan ggr on g.grade_id=ggr.term_id"
				+ " where g.is_deleted = 'N' and g.is_enabled = '1' and ggr.is_deleted='N' and"
				+ " g.xx_id='" + xxId + "' order by g.start_date desc";
		return getMap(sql);
	}
	
	/**
	 * 获取此班级下学生所有的选课列表
	 * @param classId
	 * @return
	 */
	@Override
	public Map<String, String> getTeachClassCourse(String classId) {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT");
		sql.append("  		grr.COURSE_ID id,gc.KCMC name");
		sql.append("  	FROM");
		sql.append("  		GJT_STUDENT_INFO gsi LEFT JOIN GJT_REC_RESULT grr ON gsi.STUDENT_ID = grr.STUDENT_ID");
		sql.append("  		LEFT JOIN VIEW_TEACH_PLAN gtp ON grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID");
		sql.append("  		LEFT JOIN GJT_COURSE gc ON gc.COURSE_ID=grr.COURSE_ID,");
		sql.append("  		GJT_CLASS_INFO gci,");
		sql.append("  		GJT_CLASS_STUDENT gcs ");
		sql.append("  	WHERE");
		sql.append("  		gsi.IS_DELETED = 'N'");
		sql.append("  		AND grr.IS_DELETED = 'N'");
		sql.append("  		AND gci.IS_DELETED = 'N'");
		sql.append("  		AND gcs.IS_DELETED = 'N'");
		sql.append("  		AND gtp.IS_DELETED = 'N'");
		sql.append("  		AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  		AND gcs.CLASS_ID = gci.CLASS_ID");
		sql.append("  		AND gtp.XX_ID=gc.XX_ID");
		sql.append("  		AND gci.CLASS_TYPE = 'teach'");
		sql.append("  		AND gci.CLASS_ID='"+classId+"' GROUP BY grr.COURSE_ID,gc.KCMC");

		return getMap(sql.toString());
	}
	
    /* (non-Javadoc)
     * 获取此班级下学生的所有专业
     * @see com.gzedu.xlims.service.CommonMapService#getTeachClassMajor(java.lang.String)
     */
    @Override
    public Map<String, String> getTeachClassMajor(String classId) {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT DISTINCT");
		sql.append("  	gs.SPECIALTY_ID id,gs.ZYMC name");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO gsi,");
		sql.append("  	GJT_CLASS_STUDENT gcs,");
		sql.append("  	GJT_SPECIALTY gs");
		sql.append("  WHERE");
		sql.append("  	gsi.IS_DELETED = 'N'");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  	AND gs.IS_DELETED = 'N'");
		sql.append("  	AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  	AND gsi.MAJOR=gs.SPECIALTY_ID");
		sql.append("  	AND gcs.CLASS_ID = '"+classId+"' GROUP BY gs.SPECIALTY_ID,gs.ZYMC");
		return getMap(sql.toString());
    }

	/**
	 * 学位申请记录
	 * @param formMap
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public Workbook exportDegreeApply(Map formMap, HttpServletRequest request, HttpServletResponse response) {
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
			cell.setCellValue("姓名");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("层次");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学期");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("专业");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学习中心");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("是否满足学位申请条件");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("电子注册号");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("申请院校");
			cell = row.createCell(cellIndex++);
			cell.setCellValue("学位状态");

			List<GjtApplyDegreeCertif> recordlist = queryDegreeApplyList(formMap);
			if (EmptyUtils.isNotEmpty(recordlist)) {
				for (int i = 0; i < recordlist.size(); i++) {
					GjtApplyDegreeCertif gjtApplyDegreeCertif = recordlist.get(i);

					cellIndex = 0;
					row = sheet.createRow(rowIndex++);

					cell = row.createCell(cellIndex++);
					cell.setCellValue(gjtApplyDegreeCertif.getGjtStudentInfo().getXm());
					cell = row.createCell(cellIndex++);
					cell.setCellValue(gjtApplyDegreeCertif.getGjtStudentInfo().getXh());
					cell = row.createCell(cellIndex++);

					cell.setCellValue(cacheService.getCachedDictionaryName(CacheService.DictionaryKey.TRAININGLEVEL,gjtApplyDegreeCertif.getGjtStudentInfo().getPycc()));
					cell = row.createCell(cellIndex++);
					cell.setCellValue(gjtApplyDegreeCertif.getGjtStudentInfo().getGjtGrade().getGradeName());
					cell = row.createCell(cellIndex++);
					cell.setCellValue(gjtApplyDegreeCertif.getGjtStudentInfo().getGjtSpecialty().getZymc());
					cell = row.createCell(cellIndex++);
					cell.setCellValue(gjtApplyDegreeCertif.getGjtStudentInfo().getGjtStudyCenter().getScName());
					cell = row.createCell(cellIndex++);
					cell.setCellValue(gjtApplyDegreeCertif.getDegreeCondition() == 1 ? "已达标" : "未达标");
					cell = row.createCell(cellIndex++);
					cell.setCellValue(gjtApplyDegreeCertif.getEleRegistrationNumber() == null ? "暂无电子注册号(未完成毕业申请)" : gjtApplyDegreeCertif.getEleRegistrationNumber());

					cell = row.createCell(cellIndex++);
					cell.setCellValue(gjtApplyDegreeCertif.getGjtDegreeCollege().getCollegeName());


					cell = row.createCell(cellIndex++);
					if (gjtApplyDegreeCertif.getAuditState() == 0) {
						cell.setCellValue("未审核");
					} else if (gjtApplyDegreeCertif.getAuditState() == 1) {
						cell.setCellValue("省校审核通过");
					} else if (gjtApplyDegreeCertif.getAuditState() == 2) {
						cell.setCellValue("省校审核不通过");
					}else if (gjtApplyDegreeCertif.getAuditState() == 11) {
						cell.setCellValue("中央审核通过");
					}else{
						cell.setCellValue("中央审核不通过");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wb;
	}

	private List<GjtApplyDegreeCertif> queryDegreeApplyList(Map searchParams) {

		Map<String, Object> searchParamsNew = new HashMap<String, Object>();
		searchParamsNew.putAll(searchParams);
		Criteria<GjtApplyDegreeCertif> spec = new Criteria();
		// 设置连接方式，如果是内连接可省略掉
		// spec.setJoinType("gjtStudyCenter", JoinType.LEFT);
		spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
		spec.add(Restrictions.eq("gjtStudentInfo.isDeleted", Constants.BOOLEAN_NO, true));
		// 机构ID
		String orgId = (String) searchParamsNew.remove("EQ_orgId");
		if (StringUtils.isNotBlank(orgId)) {
			List<String> orgList = gjtOrgDao.queryChildsByParentId(orgId);
			spec.add(Restrictions.in("gjtStudentInfo.gjtStudyCenter.id", orgList, true));
		}
		spec.addAll(Restrictions.parse(searchParamsNew));

		List<GjtApplyDegreeCertif> gjtApplyDegreeCertifs = gjtApplyDegreeCertifDao.findAll(spec);

		return gjtApplyDegreeCertifs;
	}
}
