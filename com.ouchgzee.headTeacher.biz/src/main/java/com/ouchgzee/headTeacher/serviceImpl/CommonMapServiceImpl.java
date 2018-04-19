package com.ouchgzee.headTeacher.serviceImpl;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.ouchgzee.headTeacher.pojo.status.EmployeeType;
import com.ouchgzee.headTeacher.pojo.status.RoleType;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * 功能说明：公共MAP
 *
 * @author 李明 liming@eenet.com
 * @Date 2016年5月26日
 * @version 2.5
 *
 */
@Deprecated @Service("bzrCommonMapServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class CommonMapServiceImpl implements BzrCommonMapService {

	@PersistenceContext(unitName = "entityManagerFactoryBzr")
	protected EntityManager em;

	// 常用的map 查询，注意：需要返回 id,name
	public static final String EXAM_TYPE = " SELECT CODE ID,NAME   FROM tbl_sys_data tsd where tsd.IS_DELETED='N' AND tsd.type_code = 'ExaminationMode'  ";

	// 常用的map 查询，注意：需要返回 id,name
	public static final String PYCC_MAP = "SELECT CODE ID,NAME FROM TBL_SYS_DATA WHERE  TBL_SYS_DATA.TYPE_CODE ='TrainingLevel' AND is_deleted = 'N' ";
	public static final String ROLL_TYPE_MAP = "SELECT CODE ID,NAME FROM TBL_SYS_DATA WHERE  TBL_SYS_DATA.TYPE_CODE ='StudentNumberStatus' AND is_deleted = 'N' ";

	@Override
	public Map<String, String> getOrgMap() {
		String sql = "select a.id ,a.org_name as name from gjt_org a where a.is_deleted = 'N'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getOrgMap(String userId) {
		String sql = "select a.id ,a.org_name as name,a.perent_id from gjt_org a,gjt_user_account b where a.is_deleted = 'N' and (a.id=b.org_id or a.perent_id = b.org_id) "
				+ "and b.id = '" + userId + "'";
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
	public String getParentWithType(String orgId, String orgType) {
		String sql = "select id ID, org_type NAME from gjt_org where id = (select perent_id from gjt_org where id = '" + orgId + "')";
		Map<String, String> map = getMap(sql);
		if (map == null || map.isEmpty()) {
			return null;
		} else {
			Entry<String, String> entry = map.entrySet().iterator().next();
			String id = entry.getKey();
			String type = entry.getValue();
			if (orgType.equals(type)) {
				return id;
			} else {
				while(true) {
					sql = "select id ID, org_type NAME from gjt_org where id = (select perent_id from gjt_org where id = '" + map.get("ID") + "')";
					map = getMap(sql);
					if (map == null || map.isEmpty()) {
						return null;
					} else {
						entry = map.entrySet().iterator().next();
						id = entry.getKey();
						type = entry.getValue();
						if (orgType.equals(type)) {
							return id;
						}
					}
				}
			}
		}
		
	}

	@Override
	public Map<String, String> getOrgMagagerRoleMap() {
		List<String> roleCodes = new ArrayList();
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

	@Override
	public Map<String, String> getStudyCenterMap(String orgId) {// 不改变其他取值的情况下获取该admin所在的院校下面的学习中心
		String sql = "select B.ID,DECODE(b.org_type,'3',B.ORG_NAME,'6','——'||B.ORG_NAME) NAME from gjt_org b"
				+ " where b.is_deleted = 'N' AND (b.org_type='3' or b.org_type='6')"
				+ " start with B.ID = '" + orgId + "' connect by prior B.ID = B.PERENT_ID"
				+ " ORDER BY (select REVERSE(to_char(wm_concat(x.id))) from GJT_ORG x START WITH x.id=b.id CONNECT BY PRIOR x.PERENT_ID=x.ID)"; // 层级排序
		return getMap(sql);
	}

	@Override
	public Map<String, String> getSpecialtyMap(String orgId) {
		String sql = "select SPECIALTY_ID  ID,ZYMC  NAME from GJT_SPECIALTY where is_deleted = 'N' and is_enabled = '1' and xx_id='"
				+ orgId + "' ORDER BY created_dt DESC";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getGradeMap(String orgId) {
		String sql = "select g.Grade_Id ID,g.Grade_Name NAME from gjt_grade g where g.is_deleted = 'N' and g.is_enabled = '1' and "
				+ " g.xx_id=(SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' AND org.ORG_TYPE='1' START WITH org.ID='"
				+ orgId + "' CONNECT BY PRIOR ORG.PERENT_ID=ORG.ID)" + " order by g.start_date desc";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getPyccMap() {
		return getMap(PYCC_MAP);
	}

	@Override
	public Map<String, String> getPyccMap(String orgId) {
		return getMap(PYCC_MAP + " AND (ORG_ID IS NULL OR ORG_ID='" + orgId + "')");
	}

	@Override
	public Map<String, String> getRollTypeMap() {
		return getMap(ROLL_TYPE_MAP);
	}

	@Override
	public Map<String, String> getEmployeeMap(String orgId, EmployeeType employeeType) {
		String sql = "SELECT EMPLOYEE_ID ID,XM NAME FROM GJT_EMPLOYEE_INFO  WHERE xx_id = '" + orgId
				+ "' and EMPLOYEE_TYPE=" + employeeType.getValue() + " AND is_deleted = 'N'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getClassInfoMap(String orgId, String classType) {
		String sql = "select class_id ID,bjmc NAME from gjt_class_info  WHERE is_deleted = 'N' and is_enabled = '1' and xx_id='"
				+ orgId + "' and class_type='" + classType + "'";
		return getMap(sql);
	}

	/**
	 * 获取班主任所教的班级
	 *
	 * @param bzrId
	 * @return
	 */
	@Override
	public Map<String, String> getClassInfoMapByBzrId(String bzrId) {
		String sql = "select class_id ID,bjmc NAME from gjt_class_info  WHERE is_deleted = 'N' and is_enabled = '1' and class_type='teach'" +"and bzr_id='" +bzrId+"'";
		return getMap(sql);
	}

	@Override
	public Map<String, String> getStudentMap(String classId) {
		String sql = "select t.student_id ID, t.xm NAME from gjt_student_info t inner join gjt_class_student b on t.student_id=b.student_id inner join gjt_class_info c on b.class_id=c.class_id where t.is_deleted='N' and b.is_deleted='N' and c.is_deleted='N' and c.class_id='"
				+ classId + "' order by t.created_dt desc";
		return getMap(sql);
	}

	/**
	 * 根据教学班级Id查询班级userId
	 */
	@Override
	public List<Object> queryStudentUserId(String classId) {
		String sql = "select gua.id  from gjt_student_info t "
				+ " inner join gjt_user_account gua on gua.id=t.account_id"
				+ " inner join gjt_class_student b on t.student_id=b.student_id "
				+ " inner join gjt_class_info c on b.class_id=c.class_id where t.is_deleted='N'"
				+ " and b.is_deleted='N' and c.is_deleted='N' and c.class_id='" + classId
				+ "' order by t.created_dt desc";
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Object> rows = query.getResultList();
		return rows;
	}

	@Override
	public Map<String, String> getDates(String typeCode) {
		String sql = "SELECT CODE ID,NAME FROM TBL_SYS_DATA WHERE  TBL_SYS_DATA.TYPE_CODE ='" + typeCode
				+ "' AND is_deleted = 'N' order by ORD_NO ";
		return getMap(sql);
	}

	private Map<String, String> getMap(String sql) {
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Object> rows = query.getResultList();
		Map<String, String> resultMap = new LinkedHashMap();
		for (Object obj : rows) {
			Map<String, String> m = (Map<String, String>) obj;
			resultMap.put(m.get("ID"), m.get("NAME"));
		}
		return resultMap;
	}

	/**
	 * 获取考试方式
	 *
	 * @return
	 */
	@Override
	public Map<BigDecimal, String> getExamTypeMap() {
		Query query = em.createNativeQuery(EXAM_TYPE);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Object> rows = query.getResultList();
		Map<BigDecimal, String> resultMap = new LinkedHashMap<BigDecimal, String>();
		for (Object obj : rows) {
			Map<String, String> m = (Map<String, String>) obj;
			resultMap.put(new BigDecimal(m.get("ID")), m.get("NAME"));
		}
		return resultMap;
	}

	@Override
	public Map<String, String> getTextbookPlanMap(String orgId) {
		String sql = "select PLAN_ID id,PLAN_NAME name from GJT_TEXTBOOK_PLAN where ORG_ID ='" + orgId
				+ "' and IS_DELETED = 'N' and STATUS = 3 order by CREATED_DT";
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

	@Override
	public String getCurrentGjtExamBatchNew(String xxid) {
		String sql = "SELECT EXAM_BATCH_CODE  FROM GJT_EXAM_BATCH_NEW WHERE IS_DELETED=0 AND PLAN_STATUS=3 AND XX_ID = '"
			+ xxid + "' order by CREATED_DT desc";
		String code = null;
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Object> rows = query.getResultList();
		if(EmptyUtils.isNotEmpty(rows)&& EmptyUtils.isNotEmpty(rows.get(0))){
			Map re= (Map)rows.get(0);
			code = ObjectUtils.toString(re.get("EXAM_BATCH_CODE"));
		}
		return code;
	}

	@Override
	public Map<String, String> getGjtExamBatchNewIdNameMap(String xxid) {
		String sql = "SELECT EXAM_BATCH_CODE AS ID, NAME FROM GJT_EXAM_BATCH_NEW WHERE IS_DELETED=0 AND PLAN_STATUS=3 AND XX_ID = '"
				+ xxid + "' order by CREATED_DT desc";
		return getMap(sql);
	}

	/**
	 * 获取班主任所教的教学班所有学员拥有的专业
	 *
	 * @param bzrId
	 * @return
	 */
	@Override
	public Map<String, String> getBzrTeachMajor(String bzrId) {

		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT");
		sql.append("  	gsi.MAJOR ID,(SELECT gs.ZYMC FROM GJT_SPECIALTY gs WHERE gs.IS_DELETED='N' AND gs.SPECIALTY_ID=gsi.MAJOR) NAME ");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO gsi,");
		sql.append("  	GJT_CLASS_INFO gci,");
		sql.append("  	GJT_CLASS_STUDENT gcs");
		sql.append("  WHERE");
		sql.append("  	gsi.IS_DELETED = 'N'");
		sql.append("  	AND gci.IS_DELETED = 'N'");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  	AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  	AND gcs.CLASS_ID = gci.CLASS_ID");
		sql.append("  	AND gci.CLASS_TYPE = 'teach'");
		sql.append("  	AND gci.BZR_ID = '"+bzrId+"' GROUP BY gsi.MAJOR");

		return getMap(sql.toString());
	}

	/**
	 * 获取当前学期
	 *
	 * @param orgId
	 * @return
	 */
	@Override
	public Map<String, String> getCurrentGradeMap(String orgId) {
		String sql = "SELECT gg.GRADE_ID ID,gg.GRADE_NAME NAME FROM GJT_GRADE gg WHERE gg.IS_DELETED='N' AND  SYSDATE BETWEEN gg.START_DATE AND NVL( gg.END_DATE, ADD_MONTHS(gg.START_DATE, 4 )) AND"
				+ " gg.XX_ID=(SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' AND org.ORG_TYPE='1' START WITH org.ID='"
				+ orgId + "' CONNECT BY PRIOR ORG.PERENT_ID=ORG.ID)";
		return getMap(sql);
	}

	/**
	 * 获取院校所属的课程班
	 *
	 * @param orgId
	 * @param courseId
	 * @return
	 */
	@Override
	public Map getCourseClassInfoMap(String orgId, String courseId) {
		String sql = " SELECT gci.CLASS_ID ID,gci.BJMC NAME FROM GJT_CLASS_INFO gci WHERE gci.CLASS_TYPE = 'course' AND gci.IS_DELETED = 'N' AND gci.COURSE_ID = '"+courseId+"'"
				+" AND(gci.XX_ID = '"+orgId+"' OR gci.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID = '"+orgId+"' CONNECT BY PRIOR ORG.PERENT_ID = ORG.ID))";
		return getMap(sql);
	}

	/**
	 * 获取年级
	 *
	 * @param xxId
	 * @return
	 */
	@Override
	public Map getYearMap(String xxId) {
		String sql = "select t.grade_id ID,t.name from gjt_year t"
				+ " where t.org_id=(SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' AND org.ORG_TYPE='1' START WITH org.ID='"
				+ xxId + "' CONNECT BY PRIOR ORG.PERENT_ID=ORG.ID)" + " order by t.created_dt desc";
		return getMap(sql);
	}
}
