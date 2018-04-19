/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.daoImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import com.gzedu.xlims.pojo.status.SignupAuditStateEnum;

/**
 * 学员信息操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年09月23日
 * @version 2.5
 * @since JDK 1.7
 */
@Repository
@Transactional(readOnly = true)
public class StudentInfoDaoImpl extends BaseDaoImpl {

	private static Logger log = LoggerFactory.getLogger(StudentInfoDaoImpl.class);

	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Autowired
	private CommonDao commonDao;

	public List<Map> queryErrorPic() {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder(
				"SELECT ID,URL_NEW  FROM GJT_SIGNUP_DATA WHERE URL_NEW LIKE '%CERTIFICATE%' ");
		return super.findAllByToMap(querySql, parameters, null);
	}

	public List<Map> findStudentSignupInfo(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder(
				"SELECT T.AVATAR,T.XM,T.XBM,T.XH,T.SFZH,T.SJH,T.PYCC,T.XJZT,T.USER_TYPE,B.GRADE_NAME,C.ZYMC,E.ORG_NAME,B.AUDIT_STATE");
		querySql.append(
				" ,T.SC_CO,T.DZXX,F.PROVINCE_CODE,F.CITY_CODE,F.AREA_CODE,F.ADDRESS,G.BJMC TEACH_CLASS_NAME,J.XM HEADTEACHER");
		querySql.append(" FROM GJT_STUDENT_INFO T");
		querySql.append(" LEFT JOIN VIEW_STUDENT_INFO B ON B.STUDENT_ID = T.STUDENT_ID");
		querySql.append(" LEFT JOIN GJT_SPECIALTY C ON C.specialty_id = T.major AND C.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_SCHOOL_INFO D ON D.ID=T.XX_ID AND D.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_ORG E ON E.ID=T.XXZX_ID AND E.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_SIGNUP H ON H.STUDENT_ID = T.STUDENT_ID");
		querySql.append(
				" LEFT JOIN GJT_STUDENT_ADDRESS F ON F.STUDENT_ID=T.STUDENT_ID AND F.IS_DEFAULT=1 AND F.IS_DELETED='N'");
		querySql.append(" LEFT JOIN (");
		querySql.append("      select X.Student_Id,I.Class_Id,I.Bzr_Id,I.BJMC from GJT_CLASS_INFO I");
		querySql.append("      INNER JOIN GJT_CLASS_STUDENT X ON I.CLASS_ID = X.CLASS_ID AND X.IS_DELETED = 'N'");
		querySql.append("      WHERE I.IS_DELETED = 'N' AND I.CLASS_TYPE = 'teach'");
		querySql.append(" ) G ON G.STUDENT_ID = T.STUDENT_ID");
		querySql.append(" LEFT JOIN GJT_EMPLOYEE_INFO J ON J.EMPLOYEE_ID = G.BZR_ID");
		querySql.append(" WHERE T.IS_DELETED='N'");
		querySql.append(" AND H.CHARGE<>'2'"); // 过滤掉未缴费的
		conditionJoin(searchParams, querySql, parameters);
		return super.findAllByToMap(querySql, parameters, null);
	}

	/**
	 * 分页条件查询学员的学籍资料，SQL语句
	 * 
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page<Map> findStudentSignupInfoByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder(
				"SELECT T.*,B.GRADE_ID,B.GRADE_NAME,C.ZYMC,C.SPECIALTY_CATEGORY,D.XXMC,E.ORG_NAME,B.AUDIT_STATE,temp2.FLOW_AUDIT_OPERATOR_ROLE,temp2.FLOW_AUDIT_STATE ");
		querySql.append(" FROM GJT_STUDENT_INFO T");
		querySql.append(" LEFT JOIN VIEW_STUDENT_INFO B ON B.STUDENT_ID = T.STUDENT_ID");
		querySql.append(" LEFT JOIN GJT_SPECIALTY C ON C.specialty_id = T.major AND C.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_SCHOOL_INFO D ON D.ID=T.XX_ID AND D.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_ORG E ON E.ID=T.XXZX_ID AND E.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_SIGNUP H ON H.STUDENT_ID = T.STUDENT_ID");
		// <!-- 和审核记录关联，获取最新审核状态 -->
		querySql.append(" LEFT JOIN (");
		querySql.append(
				"   SELECT student_id S_ID,audit_state FLOW_AUDIT_STATE,audit_operator_role FLOW_AUDIT_OPERATOR_ROLE FROM (");
		querySql.append(
				"       select f.student_id,f.audit_state,f.audit_operator_role,row_number() over (partition by f.student_id order by f.created_dt desc,f.audit_operator_role desc) id FROM gjt_flow_record f WHERE f.is_deleted='N'");
		querySql.append("   ) temp WHERE temp.id=1");
		querySql.append(" ) temp2 ON temp2.s_id=t.student_id");
		querySql.append(" WHERE T.IS_DELETED='N'");
		querySql.append(" AND H.CHARGE<>'2'"); // 过滤掉未缴费的

		conditionJoin(searchParams, querySql, parameters);
		long beginTime = System.currentTimeMillis();
		try {
			return super.findByPageToMap(querySql, parameters, pageRequest);
		} finally {
			// 计算执行当前sql耗时时长
			log.info(String.format(
					"function findStudentSignupInfoByPage select use time:%1$sms, sql:%2$s, parameters:%3$s",
					System.currentTimeMillis() - beginTime, querySql, parameters));
		}
	}

	/**
	 * 分页条件查询学员的学籍资料，SQL语句
	 *
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	public List<Map> findStudentSignupInfoEveryAuditState(Map<String, Object> searchParams, Sort sort) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuilder querySql = new StringBuilder();
		querySql.append("SELECT").append("   t.XH,t.TXDZ,").append("   T.XM,")
				.append("   (select t1.name from tbl_sys_data t1 where t1.is_deleted='N' and t1.type_code='Sex' and t1.code=t.XBM) SEX_NAME,")
				.append("   E.ORG_NAME,").append("   T.SFZH,").append("   T.SJH,")
				.append("   (select t1.name from tbl_sys_data t1 where t1.is_deleted='N' and t1.type_code='USER_TYPE' and t1.code=t.user_type) USER_TYPE_NAME,")
				.append("   (select t1.name from tbl_sys_data t1 where t1.is_deleted='N' and t1.type_code='TrainingLevel' and t1.code=t.pycc) pyccName,")
				.append("	(select t1.name from tbl_sys_data t1 where t1.is_deleted='N' and t1.type_code='StudentNumberStatus' and t1.code=t.xjzt) XJZTNAME,")
				.append("   b.GRADE_NAME,B.ZYMC,").append("   N.ORDER_SN,")
				.append(" 	T.SC_CO,T.DZXX,F.PROVINCE_CODE,F.CITY_CODE,F.AREA_CODE,F.ADDRESS,G.BJMC TEACH_CLASS_NAME,J.XM HEADTEACHER,")
				.append("   DECODE( T.PERFECT_STATUS, 1, '已完善', '未完善' ) PERFECT_STATUS,")
				.append("   DECODE( B.AUDIT_STATE, '1', '通过', '0', '不通过', '待审核' ) LAST_AUDIT_STATE,")
				.append("   decode(temp01.audit_state,1,'已提交',2,'不通过','待提交') INFO_STATE,")
				.append("   (case when temp02.created_dt>=temp01.created_dt then temp02.audit_state else null end) ONE_AUDIT_STATE,")
				.append("   (case when temp03.created_dt>=temp02.created_dt then temp03.audit_state else null end) TWO_AUDIT_STATE,")
				.append("   (case when temp04.created_dt>=temp03.created_dt then temp04.audit_state else null end) THREE_AUDIT_STATE,")
				.append("   row_number() over (partition by t.student_id order by temp01.created_dt desc,temp02.created_dt desc,temp03.created_dt desc,temp04.created_dt desc) no")
				.append(" FROM").append("   GJT_STUDENT_INFO T").append(" LEFT JOIN VIEW_STUDENT_INFO B ON")
				.append("   B.STUDENT_ID = T.STUDENT_ID").append(" LEFT JOIN GJT_SIGNUP N ON")
				.append("   N.STUDENT_ID = T.STUDENT_ID")
				.append(" LEFT JOIN GJT_SPECIALTY C ON C.specialty_id = T.major AND C.IS_DELETED = 'N'")
				.append(" LEFT JOIN (")
				.append("     SELECT student_id S_ID,audit_state FLOW_AUDIT_STATE,audit_operator_role FLOW_AUDIT_OPERATOR_ROLE FROM (")
				.append("            select f.student_id,f.audit_state,f.audit_operator_role,row_number() over (partition by f.student_id order by f.created_dt desc,f.audit_operator_role desc) id FROM gjt_flow_record f WHERE f.is_deleted='N'")
				.append("     ) temp WHERE temp.id=1").append(" ) temp2 ON temp2.s_id=t.student_id")
				.append(" left join (select x.student_id,x.audit_state,x.created_dt from gjt_flow_record x where x.audit_operator_role=1) temp01 on temp01.student_id=t.student_id")
				.append(" left join (select x.student_id,x.audit_state,x.created_dt from gjt_flow_record x where x.audit_operator_role=2) temp02 on temp02.student_id=t.student_id")
				.append(" left join (select x.student_id,x.audit_state,x.created_dt from gjt_flow_record x where x.audit_operator_role=3) temp03 on temp03.student_id=t.student_id")
				.append(" left join (select x.student_id,x.audit_state,x.created_dt from gjt_flow_record x where x.audit_operator_role=4) temp04 on temp04.student_id=t.student_id");

		querySql.append(" LEFT JOIN GJT_ORG E ON E.ID=T.XXZX_ID AND E.IS_DELETED = 'N'");
		querySql.append(
				" LEFT JOIN GJT_STUDENT_ADDRESS F ON F.STUDENT_ID=T.STUDENT_ID AND F.IS_DEFAULT=1 AND F.IS_DELETED='N'");
		querySql.append(" LEFT JOIN (");
		querySql.append("      select H.Student_Id,I.Class_Id,I.Bzr_Id,I.BJMC from GJT_CLASS_INFO I");
		querySql.append("      INNER JOIN GJT_CLASS_STUDENT H ON I.CLASS_ID = H.CLASS_ID AND H.IS_DELETED = 'N'");
		querySql.append("      WHERE I.IS_DELETED = 'N' AND I.CLASS_TYPE = 'teach'");
		querySql.append(" ) G ON G.STUDENT_ID = T.STUDENT_ID");
		querySql.append(" LEFT JOIN GJT_EMPLOYEE_INFO J ON J.EMPLOYEE_ID = G.BZR_ID");
		querySql.append(" WHERE T.IS_DELETED = 'N'");
		querySql.append(" AND N.CHARGE<>'2'"); // 过滤掉未缴费的

		conditionJoin(searchParams, querySql, parameters);
		querySql.insert(0, "select zzz.* from (");
		querySql.append(") zzz where no=1");
		return super.findAllByToMap(querySql, parameters, sort);
	}

	/**
	 * 获取学员的信息
	 * @param studentId
	 * @return
	 */
	public Map<String, Object> findById(String studentId) {
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("studentId", studentId);

		StringBuilder querySql = new StringBuilder();
		querySql.append("SELECT T.*");
		querySql.append(" FROM GJT_STUDENT_INFO T");
		querySql.append(" WHERE T.IS_DELETED='N' AND T.STUDENT_ID=:studentId");
		return super.getMap(querySql.toString(), params);
	}

	/**
	 * 获取学员的报读信息及相关信息
	 * 
	 * @param studentId
	 * @return
	 */
	public Map<String, Object> getStudentSignupInfoById(String studentId) {
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("studentId", studentId);

		StringBuilder querySql = new StringBuilder();
		querySql.append(
				"SELECT T.*,B.GRADE_ID,B.GRADE_NAME,BB.NAME YEAR_NAME,C.ZYMC,C.SPECIALTY_ID,C.SPECIALTY_CATEGORY,C.RULE_CODE,D.XXMC,E.ORG_NAME,E.CODE,F.JOB_POST,F.AUDIT_STATE,F.ORDER_SN,F.ZGXL_RADIO_TYPE");
		querySql.append(" FROM GJT_STUDENT_INFO T");
		querySql.append(" LEFT JOIN GJT_GRADE B ON B.GRADE_ID = T.NJ ");
		querySql.append(" LEFT JOIN GJT_YEAR BB ON BB.GRADE_ID=B.YEAR_ID ");
		querySql.append(" LEFT JOIN  GJT_SPECIALTY C ON C.specialty_id = T.major AND C.IS_DELETED = 'N' ");
		querySql.append(" LEFT JOIN GJT_SCHOOL_INFO D ON D.ID=T.XX_ID AND D.IS_DELETED = 'N'  ");
		querySql.append(" LEFT JOIN GJT_ORG E ON E.ID=T.XXZX_ID AND E.IS_DELETED = 'N' ");
		querySql.append(" INNER JOIN GJT_SIGNUP F ON F.STUDENT_ID=T.STUDENT_ID AND F.IS_DELETED = 'N' ");
		querySql.append(" WHERE T.IS_DELETED='N' AND T.STUDENT_ID=:studentId");
		return super.getMap(querySql.toString(), params);
	}

	/**
	 * 统计审核状态
	 * 
	 * @param searchParams
	 * @return
	 */
	public List<Map> countGroupbyAuditState(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder(
				"SELECT (case when t.xjzt = '5' then '5' when B.AUDIT_STATE = '1' then '1' when B.AUDIT_STATE = '0' then '0' when temp2.FLOW_AUDIT_OPERATOR_ROLE is null then '4' when temp2.FLOW_AUDIT_OPERATOR_ROLE = 4 then '3' when temp2.FLOW_AUDIT_OPERATOR_ROLE is not null then '2' else ' ' end) audit_state,count(*) student_num");
		querySql.append(" FROM GJT_STUDENT_INFO T");
		querySql.append(" LEFT JOIN VIEW_STUDENT_INFO B ON B.STUDENT_ID = T.STUDENT_ID");
		querySql.append(" LEFT JOIN GJT_SPECIALTY C ON C.specialty_id = T.major AND C.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_SCHOOL_INFO D ON D.ID=T.XX_ID AND D.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_ORG E ON E.ID=T.XXZX_ID AND E.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_SIGNUP H ON H.STUDENT_ID = T.STUDENT_ID");
		// <!-- 和审核记录关联，获取最新审核状态 -->
		querySql.append(" LEFT JOIN (");
		querySql.append(
				"   SELECT student_id S_ID,audit_state FLOW_AUDIT_STATE,audit_operator_role FLOW_AUDIT_OPERATOR_ROLE FROM (");
		querySql.append(
				"       select f.student_id,f.audit_state,f.audit_operator_role,row_number() over (partition by f.student_id order by f.created_dt desc,f.audit_operator_role desc) id FROM gjt_flow_record f WHERE f.is_deleted='N'");
		querySql.append("   ) temp WHERE temp.id=1");
		querySql.append(" ) temp2 ON temp2.s_id=t.student_id");
		querySql.append(" WHERE T.IS_DELETED='N'");
		querySql.append(" AND H.CHARGE<>'2'"); // 过滤掉未缴费的

		conditionJoin(searchParams, querySql, parameters);
		querySql.append(
				" group by (case when t.xjzt = '5' then '5' when B.AUDIT_STATE = '1' then '1' when B.AUDIT_STATE = '0' then '0' when temp2.FLOW_AUDIT_OPERATOR_ROLE is null then '4' when temp2.FLOW_AUDIT_OPERATOR_ROLE = 4 then '3' when temp2.FLOW_AUDIT_OPERATOR_ROLE is not null then '2' else ' ' end)");
		long beginTime = System.currentTimeMillis();
		try {
			return super.findAllByToMap(querySql, parameters, null);
		} finally {
			// 计算执行当前sql耗时时长
			log.info(String.format("function countGroupbyAuditState select use time:%1$sms, sql:%2$s, parameters:%3$s",
					System.currentTimeMillis() - beginTime, querySql, parameters));
		}
	}

	/**
	 * 统计完善情况
	 * 
	 * @param searchParams
	 * @return
	 */
	public List<Map> countGroupbyPerfectStatus(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder(
				"SELECT decode(T.perfect_status,1,1,0) perfect_status,count(*) student_num");
		querySql.append(" FROM GJT_STUDENT_INFO T");
		querySql.append(" LEFT JOIN VIEW_STUDENT_INFO B ON B.STUDENT_ID = T.STUDENT_ID");
		querySql.append(" LEFT JOIN GJT_SPECIALTY C ON C.specialty_id = T.major AND C.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_SCHOOL_INFO D ON D.ID=T.XX_ID AND D.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_ORG E ON E.ID=T.XXZX_ID AND E.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_SIGNUP H ON H.STUDENT_ID = T.STUDENT_ID");
		querySql.append(" WHERE T.IS_DELETED='N'");
		querySql.append(" AND H.CHARGE<>'2'"); // 过滤掉未缴费的

		conditionJoin(searchParams, querySql, parameters);
		querySql.append(" group by decode(T.perfect_status,1,1,0)");
		return super.findAllByToMap(querySql, parameters, null);
	}

	/**
	 * 统计学籍状态
	 * 
	 * @param searchParams
	 * @return
	 */
	public List<Map> countGroupbyXjzt(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("SELECT decode(T.xjzt,null,' ',T.xjzt) xjzt,count(*) student_num");
		querySql.append(" FROM GJT_STUDENT_INFO T");
		querySql.append(" LEFT JOIN VIEW_STUDENT_INFO B ON B.STUDENT_ID = T.STUDENT_ID");
		querySql.append(" LEFT JOIN GJT_SPECIALTY C ON C.specialty_id = T.major AND C.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_SCHOOL_INFO D ON D.ID=T.XX_ID AND D.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_ORG E ON E.ID=T.XXZX_ID AND E.IS_DELETED = 'N'");
		querySql.append(" LEFT JOIN GJT_SIGNUP H ON H.STUDENT_ID = T.STUDENT_ID");
		querySql.append(" WHERE T.IS_DELETED='N'");
		querySql.append(" AND H.CHARGE<>'2'"); // 过滤掉未缴费的

		conditionJoin(searchParams, querySql, parameters);
		querySql.append(" group by decode(T.xjzt,null,' ',T.xjzt)");
		return super.findAllByToMap(querySql, parameters, null);
	}

	/**
	 * 查询条件拼接
	 * 
	 * @param searchParams
	 * @param querySql
	 * @param parameters
	 */
	private void conditionJoin(Map<String, Object> searchParams, StringBuilder querySql,
			Map<String, Object> parameters) {
		// 学员ID
		if (StringUtils.isNotBlank(searchParams.get("EQ_studentId"))) {
			querySql.append(" AND T.STUDENT_ID = :studentId");
			parameters.put("studentId", searchParams.get("EQ_studentId"));
		}
		// 学习中心
		if (StringUtils.isNotBlank(searchParams.get("EQ_studyId"))) {
			querySql.append(
					" AND T.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxzxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			parameters.put("xxzxId", searchParams.get("EQ_studyId"));
		} else {
			// 院校ID
			if (StringUtils.isNotBlank(searchParams.get("EQ_gjtSchoolInfo.id"))) {
				querySql.append(
						" AND T.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
				parameters.put("xxId", searchParams.get("EQ_gjtSchoolInfo.id"));
			}
		}
		// 学号
		if (StringUtils.isNotBlank(searchParams.get("EQ_xh"))) {
			querySql.append(" AND T.XH = :xh");
			parameters.put("xh", searchParams.get("EQ_xh"));
		}
		// 姓名
		if (StringUtils.isNotBlank(searchParams.get("LIKE_xm"))) {
			querySql.append(" AND t.xm LIKE :xm");
			parameters.put("xm", "%" + searchParams.get("LIKE_xm") + "%");
		}
		// 身份证号
		if (StringUtils.isNotBlank(searchParams.get("EQ_sfzh"))) {
			querySql.append(" AND T.sfzh = :sfzh");
			parameters.put("sfzh", searchParams.get("EQ_sfzh"));
		}
		// 性别
		if (StringUtils.isNotBlank(searchParams.get("EQ_xbm"))) {
			querySql.append(" AND T.XBM = :xbm");
			parameters.put("xbm", searchParams.get("EQ_xbm"));
		}
		// 年级
		if (StringUtils.isNotBlank(searchParams.get("EQ_viewStudentInfo.gradeId"))) {
			querySql.append(" AND B.GRADE_ID = :gradeId");
			parameters.put("gradeId", searchParams.get("EQ_viewStudentInfo.gradeId"));
		}
		// 培养层次
		if (StringUtils.isNotBlank(searchParams.get("EQ_pycc"))) {
			querySql.append(" AND T.pycc = :pycc");
			parameters.put("pycc", searchParams.get("EQ_pycc"));
		}
		// 专业
		if (StringUtils.isNotBlank(searchParams.get("EQ_specialtyId"))) {
			querySql.append(" AND T.major = :specialtyId");
			parameters.put("specialtyId", searchParams.get("EQ_specialtyId"));
		}
		// 学员类型
		if (StringUtils.isNotBlank(searchParams.get("EQ_userType"))) {
			querySql.append(" AND T.USER_TYPE = :userType");
			parameters.put("userType", searchParams.get("EQ_userType"));
		}
		// 单位名称
		if (StringUtils.isNotBlank(searchParams.get("LIKE_scCo"))) {
			querySql.append(" AND T.SC_CO LIKE :scCo");
			parameters.put("scCo", "%" + searchParams.get("LIKE_scCo") + "%");
		}
		// 学籍状态
		if (StringUtils.isNotBlank(searchParams.get("EQ_xjzt"))) {
			querySql.append(" AND T.XJZT = :xjzt");
			parameters.put("xjzt", searchParams.get("EQ_xjzt"));
		}

		// 完善状态
		String perfectStatus = (String) searchParams.get("EQ_perfectStatus");
		if (StringUtils.isNotBlank(perfectStatus)) {
			if ("1".equals(perfectStatus)) {
				querySql.append(" AND T.perfect_status = :perfectStatus");
				parameters.put("perfectStatus", perfectStatus);
			} else {
				querySql.append(" AND T.perfect_status <> :perfectStatus");
				parameters.put("perfectStatus", "1");
			}
		}

		// 资料审核状态
		String auditState = (String) searchParams.get("EQ_signupAuditState");
		if (StringUtils.isNotBlank(auditState)) {
			if ("5".equals(auditState)) { // 已退费，无需审核
				querySql.append(" AND t.xjzt='5'");
			} else if ((SignupAuditStateEnum.待审核.getValue() + "").equals(auditState)) {
				// querySql.append(" AND B.AUDIT_STATE IN
				// (:auditState1,:auditState2,:auditState3)");
				// parameters.put("auditState1",
				// SignupAuditStateEnum.重提交.getValue()+"");
				// parameters.put("auditState2",
				// SignupAuditStateEnum.待审核.getValue()+"");
				// parameters.put("auditState3",
				// SignupAuditStateEnum.未提交.getValue()+"");
				querySql.append(" AND t.xjzt <>'5' and B.AUDIT_STATE<>'1' and B.AUDIT_STATE<>'0' and temp2.FLOW_AUDIT_OPERATOR_ROLE=4");
			} else if ((SignupAuditStateEnum.未提交.getValue() + "").equals(auditState)) { // 未审核
				querySql.append(" AND t.xjzt <>'5' and B.AUDIT_STATE<>'1' and B.AUDIT_STATE<>'0' and temp2.FLOW_AUDIT_OPERATOR_ROLE is null");
			} else if ((SignupAuditStateEnum.重提交.getValue() + "").equals(auditState)) { // 审核中
				querySql.append(" AND t.xjzt <>'5' and B.AUDIT_STATE<>'1' and B.AUDIT_STATE<>'0' and temp2.FLOW_AUDIT_OPERATOR_ROLE is not null and temp2.FLOW_AUDIT_OPERATOR_ROLE<>4");
			} else {
				querySql.append(" AND B.AUDIT_STATE = :auditState");
				parameters.put("auditState", auditState);
			}
		}
	}

	/**
	 * 统计专业学员数，SQL语句
	 * 
	 * @param searchParams
	 * @param sort
	 * @return
	 */
	public List<Map> querySpecialtyRecruitstatisticsBy(Map<String, Object> searchParams, Sort sort) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("SELECT gs.zymc NAME,count(*) VALUE");
		querySql.append(" FROM view_student_info vsi, gjt_specialty gs");
		querySql.append(" WHERE vsi.major=gs.specialty_id AND gs.is_deleted='N' AND gs.type=1");

		// 查询条件拼接
		if (StringUtils.isNotBlank(searchParams.get("xxId"))) {
			querySql.append(" AND vsi.XX_ID = :xxId");
			parameters.put("xxId", searchParams.get("xxId"));
		}
		if (StringUtils.isNotBlank(searchParams.get("xxzxId"))) {
			querySql.append(" AND vsi.XXZX_ID = :xxzxId");
			parameters.put("xxzxId", searchParams.get("xxzxId"));
		}
		if (StringUtils.isNotBlank(searchParams.get("gradeId"))) {
			querySql.append(" AND vsi.GRADE_ID = :gradeId");
			parameters.put("gradeId", searchParams.get("gradeId"));
		}
		if (StringUtils.isNotBlank(searchParams.get("specialtyId"))) {
			querySql.append(" AND gs.specialty_id = :specialtyId");
			parameters.put("specialtyId", searchParams.get("specialtyId"));
		}
		if (StringUtils.isNotBlank(searchParams.get("pycc"))) {
			querySql.append(" AND gs.PYCC = :pycc");
			parameters.put("pycc", searchParams.get("pycc"));
		}
		querySql.append(" GROUP BY gs.zymc");
		querySql.append(" ORDER BY value");
		return super.findAllByToMap(querySql, parameters, sort);
	}

	/**
	 * 获取视图view_student_info信息
	 * 
	 * @param studentId
	 * @return
	 */
	public Map getViewStudentInfo(String studentId) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("select * from view_student_info v where v.STUDENT_ID=:studentId");
		parameters.put("studentId", studentId);
		List<Map> list = super.findAllByToMap(querySql, parameters, null);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * 获取学员的班主任信息
	 * 
	 * @param studentId
	 * @return
	 */
	public Map<String, Object> getStudentHeadTeacherInfo(String studentId) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder("select * from gjt_employee_info t where t.employee_id=(")
				.append(" 	select x.bzr_id from gjt_class_info x inner join gjt_class_student y on y.class_id=x.class_id")
				.append(" 	where x.is_deleted='N' and y.is_deleted='N' and x.class_type='teach'")
				.append(" 	and y.student_id=:studentId").append(" )");
		parameters.put("studentId", studentId);
		List<Map> list = super.findAllByToMap(querySql, parameters, null);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * 分页条件查询学员的学籍资料，SQL语句
	 *
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page<Map> findStudentInfoByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder(
				"select t.*,C.NAME ZYMC,D.XXMC,e.GRADE_ID,e.GRADE_NAME,f.name year_name");
		querySql.append(" FROM GJT_STUDENT_INFO T");
		querySql.append(" LEFT JOIN GJT_SPECIALTY_COLLEGE C ON C.specialty_id = T.major");
		querySql.append(" LEFT JOIN GJT_SCHOOL_INFO D ON D.ID=T.XX_ID AND D.IS_DELETED = 'N'");
		querySql.append(" left join gjt_grade e on e.grade_id=t.nj and e.is_deleted='N'");
		querySql.append(" left join gjt_year f on f.grade_id=e.year_id");
		querySql.append(" where t.is_deleted='N'");
		// parameters.put("isDeleted", Constants.BOOLEAN_NO);

		conditionJoinStudent(searchParams, querySql, parameters);
		querySql.append(" ORDER BY T.CREATED_DT DESC");
		long beginTime = System.currentTimeMillis();
		try {
			return super.findByPageToMap(querySql, parameters, pageRequest);
		} finally {
			// 计算执行当前sql耗时时长
			log.info(String.format("function findStudentInfoByPage select use time:%1$sms, sql:%2$s, parameters:%3$s",
					System.currentTimeMillis() - beginTime, querySql, parameters));
		}
	}

	/**
	 * 查询条件拼接
	 *
	 * @param searchParams
	 * @param querySql
	 * @param parameters
	 */
	private void conditionJoinStudent(Map<String, Object> searchParams, StringBuilder querySql,
			Map<String, Object> parameters) {
		// 学习中心
		String studyId = (String) searchParams.get("EQ_studyId");
		if (StringUtils.isNotBlank(studyId)) {
			querySql.append(
					" AND T.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxzxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			parameters.put("xxzxId", studyId);
		} else {
			// 院校ID
			String xxId = (String) searchParams.get("EQ_gjtSchoolInfo.id");
			if (StringUtils.isNotBlank(xxId)) {
				querySql.append(" AND T.XX_ID=:xxId");
				parameters.put("xxId", xxId);
			}
		}
		// 学号
		if (StringUtils.isNotBlank(searchParams.get("EQ_xh"))) {
			querySql.append(" AND T.XH = :xh");
			parameters.put("xh", searchParams.get("EQ_xh"));
		}
		// 姓名
		if (StringUtils.isNotBlank(searchParams.get("LIKE_xm"))) {
			querySql.append(" AND t.xm LIKE :xm");
			parameters.put("xm", "%" + searchParams.get("LIKE_xm") + "%");
		}
		// 身份证号
		if (StringUtils.isNotBlank(searchParams.get("EQ_sfzh"))) {
			querySql.append(" AND T.sfzh = :sfzh");
			parameters.put("sfzh", searchParams.get("EQ_sfzh"));
		}
		// 培养层次
		if (StringUtils.isNotBlank(searchParams.get("EQ_pycc"))) {
			querySql.append(" AND T.pycc = :pycc");
			parameters.put("pycc", searchParams.get("EQ_pycc"));
		}
		// 专业
		if (StringUtils.isNotBlank(searchParams.get("EQ_specialtyId"))) {
			querySql.append(" AND T.major = :specialtyId");
			parameters.put("specialtyId", searchParams.get("EQ_specialtyId"));
		}
		// 学员类型
		if (StringUtils.isNotBlank(searchParams.get("EQ_userType"))) {
			querySql.append(" AND T.USER_TYPE = :userType");
			parameters.put("userType", searchParams.get("EQ_userType"));
		}
		// 单位名称
		if (StringUtils.isNotBlank(searchParams.get("LIKE_scCo"))) {
			querySql.append(" AND T.SC_CO LIKE :scCo");
			parameters.put("scCo", "%" + searchParams.get("LIKE_scCo") + "%");
		}
		// 学籍状态
		if (StringUtils.isNotBlank(searchParams.get("EQ_xjzt"))) {
			querySql.append(" AND T.XJZT = :xjzt");
			parameters.put("xjzt", searchParams.get("EQ_xjzt"));
		}

		// 完善状态
		String perfectStatus = (String) searchParams.get("EQ_perfectStatus");
		if (StringUtils.isNotBlank(perfectStatus)) {
			if ("1".equals(perfectStatus)) {
				querySql.append(" AND T.perfect_status = :perfectStatus");
				parameters.put("perfectStatus", perfectStatus);
			} else {
				querySql.append(" AND T.perfect_status <> :perfectStatus");
				parameters.put("perfectStatus", "1");
			}
		}

		// 班级名称
		if (StringUtils.isNotBlank(searchParams.get("EQ_bjmc"))) {
			querySql.append(" AND T.USERCLASS LIKE :bjmc");
			parameters.put("bjmc", "%" + searchParams.get("EQ_bjmc") + "%");
		}
		// 年级
		if (StringUtils.isNotBlank(searchParams.get("EQ_yearId"))) {
			querySql.append(" AND f.GRADE_ID = :yearId");
			parameters.put("yearId", searchParams.get("EQ_yearId"));
		}
		// 学期
		if (StringUtils.isNotBlank(searchParams.get("EQ_viewStudentInfo.gradeId"))) {
			querySql.append(" AND e.GRADE_ID = :gradeId");
			parameters.put("gradeId", searchParams.get("EQ_viewStudentInfo.gradeId"));
		}
	}

	/**
	 * 分页条件查询学员的学籍资料，SQL语句
	 *
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page<Map> findStudentCourseByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder(
				"select t.*,C.NAME ZYMC,D.XXMC,e.GRADE_ID,e.GRADE_NAME,f.name year_name,g.created_dt takeCourseCreatedDt,h.KCH,h.KCMC");
		querySql.append(" FROM GJT_STUDENT_INFO T");
		querySql.append(" LEFT JOIN GJT_SPECIALTY_COLLEGE C ON C.specialty_id = T.major");
		querySql.append(" LEFT JOIN GJT_SCHOOL_INFO D ON D.ID=T.XX_ID AND D.IS_DELETED = 'N'");
		querySql.append(" left join gjt_grade e on e.grade_id=t.nj and e.is_deleted='N'");
		querySql.append(" left join gjt_year f on f.grade_id=e.year_id");
		querySql.append(" inner join gjt_rec_result g on g.student_id=t.student_id and g.is_deleted='N'");
		querySql.append(" inner join gjt_course h on h.course_id=g.course_id and h.is_deleted='N'");
		querySql.append(" where t.is_deleted='N'");
		// parameters.put("isDeleted", Constants.BOOLEAN_NO);

		conditionJoinStudentCourse(searchParams, querySql, parameters);
		querySql.append(" ORDER BY T.CREATED_DT DESC");
		long beginTime = System.currentTimeMillis();
		try {
			return super.findByPageToMap(querySql, parameters, pageRequest);
		} finally {
			// 计算执行当前sql耗时时长
			log.info(String.format("function findStudentCourseByPage select use time:%1$sms, sql:%2$s, parameters:%3$s",
					System.currentTimeMillis() - beginTime, querySql, parameters));
		}
	}

	/**
	 * 查询条件拼接
	 *
	 * @param searchParams
	 * @param querySql
	 * @param parameters
	 */
	private void conditionJoinStudentCourse(Map<String, Object> searchParams, StringBuilder querySql,
			Map<String, Object> parameters) {
		// 学习中心
		String studyId = (String) searchParams.get("EQ_studyId");
		if (StringUtils.isNotBlank(studyId)) {
			querySql.append(
					" AND T.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxzxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			parameters.put("xxzxId", studyId);
		} else {
			// 院校ID
			String xxId = (String) searchParams.get("EQ_gjtSchoolInfo.id");
			if (StringUtils.isNotBlank(xxId)) {
				querySql.append(" AND T.XX_ID=:xxId");
				parameters.put("xxId", xxId);
			}
		}
		// 学号
		if (StringUtils.isNotBlank(searchParams.get("EQ_xh"))) {
			querySql.append(" AND T.XH = :xh");
			parameters.put("xh", searchParams.get("EQ_xh"));
		}
		// 姓名
		if (StringUtils.isNotBlank(searchParams.get("LIKE_xm"))) {
			querySql.append(" AND t.xm LIKE :xm");
			parameters.put("xm", "%" + searchParams.get("LIKE_xm") + "%");
		}
		// 身份证号
		if (StringUtils.isNotBlank(searchParams.get("EQ_sfzh"))) {
			querySql.append(" AND T.sfzh = :sfzh");
			parameters.put("sfzh", searchParams.get("EQ_sfzh"));
		}
		// 培养层次
		if (StringUtils.isNotBlank(searchParams.get("EQ_pycc"))) {
			querySql.append(" AND T.pycc = :pycc");
			parameters.put("pycc", searchParams.get("EQ_pycc"));
		}
		// 专业
		if (StringUtils.isNotBlank(searchParams.get("EQ_specialtyId"))) {
			querySql.append(" AND T.major = :specialtyId");
			parameters.put("specialtyId", searchParams.get("EQ_specialtyId"));
		}
		// 学员类型
		if (StringUtils.isNotBlank(searchParams.get("EQ_userType"))) {
			querySql.append(" AND T.USER_TYPE = :userType");
			parameters.put("userType", searchParams.get("EQ_userType"));
		}
		// 单位名称
		if (StringUtils.isNotBlank(searchParams.get("LIKE_scCo"))) {
			querySql.append(" AND T.SC_CO LIKE :scCo");
			parameters.put("scCo", "%" + searchParams.get("LIKE_scCo") + "%");
		}
		// 学籍状态
		if (StringUtils.isNotBlank(searchParams.get("EQ_xjzt"))) {
			querySql.append(" AND T.XJZT = :xjzt");
			parameters.put("xjzt", searchParams.get("EQ_xjzt"));
		}

		// 完善状态
		String perfectStatus = (String) searchParams.get("EQ_perfectStatus");
		if (StringUtils.isNotBlank(perfectStatus)) {
			if ("1".equals(perfectStatus)) {
				querySql.append(" AND T.perfect_status = :perfectStatus");
				parameters.put("perfectStatus", perfectStatus);
			} else {
				querySql.append(" AND T.perfect_status <> :perfectStatus");
				parameters.put("perfectStatus", "1");
			}
		}

		// 课程号或课程名称
		if (StringUtils.isNotBlank(searchParams.get("EQ_kchkcmc"))) {
			querySql.append(" AND (H.KCH=:kch OR H.KCMC LIKE :kcmc)");
			parameters.put("kch", searchParams.get("EQ_kchkcmc"));
			parameters.put("kcmc", "%" + searchParams.get("EQ_kchkcmc") + "%");
		}
		// 年级
		if (StringUtils.isNotBlank(searchParams.get("EQ_yearId"))) {
			querySql.append(" AND f.GRADE_ID = :yearId");
			parameters.put("yearId", searchParams.get("EQ_yearId"));
		}
		// 学期
		if (StringUtils.isNotBlank(searchParams.get("EQ_viewStudentInfo.gradeId"))) {
			querySql.append(" AND e.GRADE_ID = :gradeId");
			parameters.put("gradeId", searchParams.get("EQ_viewStudentInfo.gradeId"));
		}
	}

	/**
	 * 获取教学班所有学员的EE号
	 * 
	 * @param teachClassId
	 * @return
	 */
	public List<String> findStudentEENoListByTeachClassId(String teachClassId) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder();
		querySql.append(
				" select b.EENO from Gjt_Class_Student t inner join Gjt_Student_Info b on t.Student_Id=b.Student_Id");
		querySql.append(" where t.is_Deleted = 'N' and b.is_Deleted = 'N' and t.Class_Id=:teachClassId and b.eeno is not null");
		parameters.put("teachClassId", teachClassId);
		List<Map> list = super.findAllBySql(querySql, parameters, null, Map.class);
		List<String> result = new ArrayList<String>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map eeMap = list.get(i);
				result.add(eeMap.get("EENO").toString());
			}
		}
		return result;
	}

	/**
	 * 根据atid或userId查询studentId
	 * 
	 * @param searchParams
	 * @return
	 */
	public List<Map> findStudentIdsBy(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder querySql = new StringBuilder();
		querySql.append(
				" select t.student_id \"studentId\",t.pycc \"pycc\",t.xjzt \"xjzt\",b.audit_state \"auditState\" from gjt_student_info t ");
		querySql.append(" inner join gjt_signup b on b.student_id=t.student_id ");
		querySql.append(
				" where t.is_deleted='N' and t.xm=:xm and (t.atid=:atid or t.user_id=:userId or t.sfzh=:sfzh or b.order_sn=:orderSn)");

		parameters.put("xm", searchParams.get("xm"));
		parameters.put("atid", searchParams.get("atid"));
		parameters.put("userId", searchParams.get("userId"));
		parameters.put("sfzh", searchParams.get("sfzh"));
		parameters.put("orderSn", searchParams.get("orderSn"));
		List<Map> list = super.findAllBySql(querySql, parameters, null, Map.class);
		return list;
	}

	/**
	 * 学员信息
	 * 
	 * @param searchParams
	 * @return
	 */
	public List<Map> findStudentSignupInfoBy(Map<String, Object> searchParams) {
		Map<String, Object> params = new HashMap();
		StringBuilder sql = new StringBuilder("");
		sql.append("  SELECT ");
		sql.append("  	T.STUDENT_ID,T.XH,T.XM,T.SFZH,N.AUDIT_STATE,N.CHARGE,T.XJZT,V.GRADE_NAME TERM_NAME,T.GRADE_SPECIALTY_ID,T.XX_ID");
		sql.append("  FROM GJT_STUDENT_INFO T");
		sql.append("  INNER JOIN GJT_SIGNUP N ON N.STUDENT_ID = T.STUDENT_ID");
		sql.append("  INNER JOIN VIEW_STUDENT_INFO V ON V.STUDENT_ID = T.STUDENT_ID");
		sql.append("  WHERE T.IS_DELETED = 'N'");

		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND T.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND T.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}

		if (searchParams.get("EQ_perfectStatus") != null) {
			sql.append("  and T.PERFECT_STATUS=:perfectStatus");
			params.put("perfectStatus", searchParams.get("EQ_perfectStatus"));
		}
		if (searchParams.get("IN_nj") != null) {
			sql.append("  AND T.NJ in :njList");
			params.put("njList", searchParams.get("IN_nj"));
		}
		if (searchParams.get("IN_sfzh") != null) {
			sql.append("  AND T.SFZH in :sfzhList");
			params.put("sfzhList", searchParams.get("IN_sfzh"));
		}

		if (searchParams.get("EQ_gjtSignup.signupSfzType") != null) {
			sql.append("  AND N.SIGNUP_SFZ_TYPE=:signupSfzType");
			params.put("signupSfzType", searchParams.get("EQ_gjtSignup.signupSfzType"));
		}
		if (searchParams.get("ISNULL_gjtSignup.mail") != null) {
			sql.append("  AND N.MAIL IS NULL");
		}
//		sql.append("  ORDER BY T.CREATED_DT DESC NULLS LAST"); // 排序太慢了
		return super.findAllByToMap(sql, params, null);
	}

	/**
	 * 备份账号信息
	 * 
	 * @param accountId
	 * @return
	 */
	public int copyAccountHistory(String accountId) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder sql = new StringBuilder("");
		sql.append("  insert into HI_USER_ACCOUNT(");
		sql.append(
				"  	ID,LOGIN_ACCOUNT,REAL_NAME,NICK_NAME,PASSWORD,PASSWORD2,ORG_ID,EMAIL,ALLOW_BACK_LOGIN,USER_TYPE,CREATE_TIME,LAST_LOGIN_TIME,LAST_LOGIN_IP,CURRENT_LOGIN_TIME,CURRENT_LOGIN_IP,LOGIN_COUNT,IS_SUPER_MGR,IS_DELETED,START_DATE,END_DATE,CREATED_BY,CREATED_DT,UPDATED_BY,UPDATED_DT,VERSION,");
		sql.append(
				"  	IS_SYNC,REMARK,STATUS,ORG_CODE,GRANT_TYPE,IS_ENABLED,EENO,DATA_SCOPE,SJH,IS_ACCOUNT_CREATED,ACCOUNT_CREATED_DT,ROLE_ID,SIGN_PHOTO,IS_ONLINE,TELEPHONE");
		sql.append("  )");
		sql.append("  (");
		sql.append(
				"  	select gua.ID,gua.LOGIN_ACCOUNT,gua.REAL_NAME,gua.NICK_NAME,gua.PASSWORD,gua.PASSWORD2,gua.ORG_ID,gua.EMAIL,gua.ALLOW_BACK_LOGIN,gua.USER_TYPE,gua.CREATE_TIME,gua.LAST_LOGIN_TIME,gua.LAST_LOGIN_IP,gua.CURRENT_LOGIN_TIME,gua.CURRENT_LOGIN_IP,gua.LOGIN_COUNT,gua.IS_SUPER_MGR,gua.IS_DELETED,gua.START_DATE,gua.END_DATE,gua.CREATED_BY,gua.CREATED_DT,gua.UPDATED_BY,(select sysdate from dual) UPDATED_DT,gua.VERSION,");
		sql.append(
				"  		gua.IS_SYNC,gua.REMARK,gua.STATUS,gua.ORG_CODE,gua.GRANT_TYPE,gua.IS_ENABLED,gua.EENO,gua.DATA_SCOPE,gua.SJH,gua.IS_ACCOUNT_CREATED,gua.ACCOUNT_CREATED_DT,gua.ROLE_ID,gua.SIGN_PHOTO,gua.IS_ONLINE,gua.TELEPHONE");
		sql.append("  	from GJT_USER_ACCOUNT gua ");
		sql.append("  	where gua.id=:accountId");
		sql.append("  )");
		parameters.put("accountId", accountId);
		return commonDao.updateForMapNative(sql.toString(), parameters);
	}

	/**
	 * 解除教学班课程班关系
	 * 
	 * @param studentId
	 * @return
	 */
	public int removeClassStudent(String studentId) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder sql = new StringBuilder("");
		sql.append(
				"  update gjt_class_student t set t.memo=:memo,t.is_deleted='Y',t.updated_dt=sysdate where t.is_deleted='N' and t.student_id=:studentId ");
		parameters.put("studentId", studentId);
		parameters.put("memo", DateFormatUtils.ISO_DATE_FORMAT.format(new Date()) + " 解除教学班课程班关系");
		return commonDao.updateForMapNative(sql.toString(), parameters);
	}

	public Page<Map> findStudent(String xhOrxm, String xxId, PageRequest pageRequst) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();

		sql.append("  select a.xh \"studentNo\", a.xm \"userName\", b.zymc \"specialtyName\",");
		sql.append("  a.student_Id \"studentId\",a.avatar \"avatar\" ");
		sql.append("  from gjt_student_info a");
		sql.append("  inner join gjt_specialty b");
		sql.append("  on a.major = b.specialty_id");
		sql.append("  where a.is_deleted = 'N' and a.xjzt!='5'");
		sql.append("  and b.is_deleted = 'N'");
		sql.append("  AND A.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
		sql.append("  and (a.xh = :xh or a.xm like :xm)");
		parameters.put("xm", xhOrxm + "%");
		parameters.put("xh", xhOrxm);
		parameters.put("xxId", xxId);
		Page<Map> findByPageSql = super.findByPageSql(sql, parameters, pageRequst, Map.class);
		return findByPageSql;
	}

	/**
	 * 获取已缴费没有生成账号的学员ID
	 * @return
	 */
	public List<Map> findStudentByNoAccount() {
		Map<String, Object> params = new HashMap();
		StringBuilder sql = new StringBuilder("");
		sql.append("  SELECT ");
		sql.append("  	T.STUDENT_ID");
		sql.append("  FROM GJT_STUDENT_INFO T");
		sql.append("  INNER JOIN GJT_SIGNUP N ON N.STUDENT_ID = T.STUDENT_ID");
		sql.append("  WHERE T.IS_DELETED = 'N' AND T.XJZT!='5' and (n.charge='1' or n.charge='0') and t.account_id is null");
		return super.findAllByToMap(sql, params, null);
	}

	// ======================================================== 学员综合信息 ======================================================== //

	/**
	 * 临时保存学生综合信息
	 * @param params
	 * @return
	 */
	public boolean saveTempStudentSynthesize(Map<String, Object> params) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" INSERT INTO GJT_TEMP_STUDENT_SYNTHESIZE(STUDENT_ID,STATUS,SYNTHESIZE_INFO) ");
		sql.append("  VALUES ");
		sql.append(" (:STUDENT_ID,:STATUS,:SYNTHESIZE_INFO)");
		return commonDao.insertForMapNative(sql.toString(), params) != 0;
	}

	/**
	 * 删除临时学生综合信息
	 * @param studentId
	 * @return
	 */
	public boolean deleteTempStudentSynthesize(String studentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder("");
		sql.append(" DELETE FROM GJT_TEMP_STUDENT_SYNTHESIZE ");
		sql.append(" WHERE STUDENT_ID=:STUDENT_ID");
		params.put("STUDENT_ID", studentId);
		return commonDao.updateForMapNative(sql.toString(), params) != 0;
	}

	/**
	 * 学员综合信息
	 *
	 * @return
	 */
	public Page<Map<String, Object>> findStudentSynthesizeListByPage(Map searchParams, PageRequest pageRequst) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getStudentSynthesizeListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	/**
	 * 学员综合信息
	 *
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, String>> findStudentSynthesizeList(Map searchParams) {
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getStudentSynthesizeListSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}

		return commonDao.queryForMapListNative(sql, params);
	}

	/**
	 * 学员综合信息sql拼接处理
	 * @return
	 */
	public Map<String, Object> getStudentSynthesizeListSqlHandler(Map searchParams) {
		StringBuffer sql = new StringBuffer();
		Map params = new HashMap();

		sql.append(" SELECT GSI.STUDENT_ID,GR.GRADE_ID,GR.GRADE_NAME,gsi.XXZX_ID,gsc.SC_NAME,");
		sql.append("        GSI.XM,GSI.XH,GSI.SJH,GSI.PYCC,GY.NAME YEAR_NAME,gsi.AVATAR ZP,gsi.EENO,GSI.XJZT,GSI.USER_TYPE,");
		sql.append("        GSY.SPECIALTY_ID,GSY.ZYMC,b.STATUS,b.SYNTHESIZE_INFO");
		sql.append(" FROM GJT_STUDENT_INFO  GSI");
		sql.append(" INNER JOIN GJT_GRADE GR ON GR.IS_DELETED = 'N' AND GR.GRADE_ID = GSI.NJ");
		sql.append(" INNER JOIN GJT_YEAR GY ON GY.GRADE_ID = GR.YEAR_ID");
		sql.append(" INNER JOIN GJT_SPECIALTY GSY ON GSY.IS_DELETED = 'N' AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append(" LEFT JOIN GJT_STUDY_CENTER gsc ON gsc.IS_DELETED = 'N' AND gsc.ID = gsi.XXZX_ID");
		sql.append(" LEFT JOIN GJT_TEMP_STUDENT_SYNTHESIZE B ON B.STUDENT_ID=GSI.STUDENT_ID");
		sql.append("     WHERE GSI.IS_DELETED = 'N'");

		// 学员类型
		String userType = ObjectUtils.toString(searchParams.get("EQ_userType"));
		if (EmptyUtils.isNotEmpty(userType)) {
			sql.append(" AND GSI.USER_TYPE=:userType");
			params.put("userType", userType);
		}
		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND GSI.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND GSI.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}

		// 院校ID
		String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("EQ_studyId"));
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND GSI.XXZX_ID IN :orgChilds");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND GSI.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDENT_ID")))) {
			sql.append("  AND gsi.STUDENT_ID =:STUDENT_ID");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC")))) {
			sql.append("  AND GSI.PYCC = :PYCC");
			params.put("PYCC", ObjectUtils.toString(searchParams.get("PYCC")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GSI.MAJOR = :SPECIALTY_ID");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))) {
			sql.append("  AND GSI.XH = :XH");
			params.put("XH", ObjectUtils.toString(searchParams.get("XH")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM");
			params.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")) + "%");
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append(" AND gsi.NJ = :GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XXZX_ID")))) {
			sql.append(" AND gsi.XXZX_ID= :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {
			if ("1".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 正常
				sql.append("  			AND b.STATUS=1");
			} else if ("2".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 异常
				sql.append("  			AND b.STATUS=2");
			} else if ("3".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 在线
				sql.append("  			AND instr(b.SYNTHESIZE_INFO,'\"isOnline\":\"Y\"')>0");
			} else if ("4".equals(ObjectUtils.toString(searchParams.get("STUDY_STATUS")))) {// 离线
				sql.append("  			AND instr(b.SYNTHESIZE_INFO,'\"isOnline\":\"N\"')>0");
			}
		}

		sql.append(" ORDER BY GSI.CREATED_DT DESC");

		Map<String, Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql", sql.toString());
		handlerMap.put("params", params);
		return handlerMap;
	}

}
