package com.gzedu.xlims.dao.studentClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;

@Repository
public class GjtStudentClassInfoDao extends BaseDaoImpl {

	@Autowired
	private CommonDao commonDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;

	/**
	 * 教学班查询
	 * 
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page getTeachClassInfo(Map<String, Object> searchParams, PageRequest pageRequest) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GCI.CLASS_ID,");
		sql.append("  	GCI.BJMC");
		sql.append("  FROM");
		sql.append("  	GJT_CLASS_INFO GCI");
		sql.append("  WHERE");
		sql.append("  	GCI.IS_DELETED = 'N'");
		sql.append("  	AND GCI.CLASS_TYPE = 'teach'");

		if (EmptyUtils.isNotEmpty(searchParams.get("name"))) {
			sql.append("  	AND GCI.BJMC LIKE :BJMC ");
			params.put("BJMC",
					"%" + org.apache.commons.lang.ObjectUtils.toString(searchParams.get("name")).trim() + "%");
		}

		sql.append("  	AND GCI.XXZX_ID IN (");
		sql.append("  		SELECT");
		sql.append("  			org.ID");
		sql.append("  		FROM");
		sql.append("  			GJT_ORG org");
		sql.append("  		WHERE");
		sql.append(
				"  			org.IS_DELETED = 'N' START WITH org.ID = :XX_ID CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID");
		sql.append("  	)");

		params.put("XX_ID", ObjectUtils.toString(searchParams.get("xxId")));

		return (Page) commonDao.queryForPageNative(sql.toString(), params, pageRequest);
	}

	/**
	 * 获取教学班学员信息
	 * 
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page listTeachClassStudentInfo(Map searchParams, PageRequest pageRequest) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GCI.CLASS_ID,");
		sql.append("  	GCI.BJMC,");
		sql.append("  	GSI.STUDENT_ID,");
		sql.append("  	GSI.XM,");
		sql.append("  	GSI.XH");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  INNER JOIN GJT_CLASS_STUDENT GCS ON");
		sql.append("  	GSI.STUDENT_ID = GCS.STUDENT_ID");
		sql.append("  INNER JOIN GJT_CLASS_INFO GCI ON");
		sql.append("  	GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");
		sql.append("  	AND GCS.IS_DELETED = 'N'");
		sql.append("  	AND GCI.IS_DELETED = 'N'");
		sql.append("  	AND GCI.CLASS_TYPE = 'teach'");
		sql.append("  	AND GCI.CLASS_ID = :CLASS_ID");

		params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("classId")));

		return (Page) commonDao.queryForPageNative(sql.toString(), params, pageRequest);

	}

	/**
	 * 获取教学班信息
	 * 
	 * @param searchParams
	 * @return
	 */
	public List getTeachClassInfo(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GCI.CLASS_ID,");
		sql.append("  	GCI.BJMC,");
		sql.append("  	GG.GRADE_ID,");
		sql.append("  	GG.GRADE_NAME,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT(*)");
		sql.append("  		FROM");
		sql.append("  			GJT_CLASS_STUDENT GCS");
		sql.append("  		INNER JOIN GJT_STUDENT_INFO GSI ON");
		sql.append("  			GCS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  		WHERE");
		sql.append("  			GCS.IS_DELETED = 'N'");
		sql.append("  			AND GSI.IS_DELETED = 'N'");
		sql.append("  			AND GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  	) STUDENT_NUM,");
		sql.append("  	GCI.BZR_ID,");
		sql.append("  	GEI.XM BZR_XM,");
		sql.append("  	GEI.ACCOUNT_ID BZR_ACCOUNT_ID");
		sql.append("  FROM");
		sql.append("  	GJT_CLASS_INFO GCI");
		sql.append("  LEFT JOIN GJT_GRADE GG ON");
		sql.append("  	GCI.GRADE_ID = GG.GRADE_ID");
		sql.append("  INNER JOIN GJT_EMPLOYEE_INFO GEI ON");
		sql.append("  	GCI.BZR_ID = GEI.EMPLOYEE_ID");
		sql.append("  	AND GEI.IS_DELETED = 'N'");
		sql.append("  WHERE");
		sql.append("  	GCI.IS_DELETED = 'N'");
		sql.append("  	AND GCI.CLASS_TYPE = 'teach'");
		sql.append("  	AND GCI.CLASS_ID = :CLASS_ID ");

		params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("classId")));

		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	/**
	 * 获取学籍资料审核记录
	 * 
	 * @param searchParams
	 * @return
	 */
	public List queryAuditSingupInfoList(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GFR.AUDIT_OPERATOR,");
		sql.append("  	GFR.AUDIT_OPERATOR_ROLE,");
		sql.append("  	TO_CHAR(GFR.AUDIT_DT,'yyyy-MM-dd HH24:mm:ss') AUDIT_DT,");
		sql.append("  	GFR.AUDIT_STATE,");
		sql.append("  	GFR.AUDIT_CONTENT");
		sql.append("  FROM");
		sql.append("  	GJT_FLOW_RECORD GFR,");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  WHERE");
		sql.append("  	GFR.IS_DELETED = 'N'");
		sql.append("  	AND GSI.IS_DELETED = 'N'");
		sql.append("  	AND GFR.STUDENT_ID = GSI.STUDENT_ID");

		if (EmptyUtils.isNotEmpty(searchParams.get("studentId"))) {
			sql.append("  	AND GFR.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("studentId")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("atId"))) {
			sql.append("  	AND GSI.ATID = :ATID ");
			params.put("ATID", ObjectUtils.toString(searchParams.get("atId")));
		}

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	public Map<String, Object> isPerfect(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GSI.PERFECT_STATUS");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");

		if (EmptyUtils.isNotEmpty(searchParams.get("studentId"))) {
			sql.append("  	AND GSI.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("studentId")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("atId"))) {
			sql.append("  	AND GSI.ATID = :ATID ");
			params.put("ATID", ObjectUtils.toString(searchParams.get("atId")));
		}

		sql.append("  ORDER　BY GSI.CREATED_DT DESC");

		List<Map<String, Object>> list = commonDao.queryForStringObjectMapListNative(sql.toString(), params);
		return list != null && list.size() > 0 ? list.get(0) : null;

	}

	/**
	 * 批量获取学员的报读信息及相关信息
	 * 
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, Object>> queryStudentSignupInfoByAtIds(Map<String, Object> searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		conditionJoin(sql, searchParams, params);
		return commonDao.queryForStringObjectMapListNative(sql.toString(), params);
	}

	/**
	 * 分页获取学员的报读信息及相关信息
	 * 
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	public Page<Map<String, Object>> queryStudentSignupInfoByPage(Map<String, Object> searchParams,
			PageRequest pageRequest) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		conditionJoin(sql, searchParams, params);
		return commonDao.queryForPageNative(sql.toString(), params, pageRequest);
	}

	/**
	 * 条件拼接
	 * 
	 * @param sql
	 * @param searchParams
	 * @param params
	 */
	private void conditionJoin(StringBuffer sql, Map<String, Object> searchParams, Map params) {
		sql.append("  SELECT");
		sql.append("  	T.*,B.GRADE_ID,B.GRADE_NAME,C.ZYMC,C.SPECIALTY_CATEGORY,D.XXMC,E.ORG_NAME,F.JOB_POST,F.AUDIT_STATE,F.ORDER_SN \"orderSn\",");
		sql.append("  	F.ZGXL_RADIO_TYPE,F.SIGNUP_SFZ_TYPE,F.SIGNUP_BYZ_TYPE,F.SIGNUP_JZZ_TYPE,DECODE(F.CHARGE,'2','1','2') \"advanceState\",");
		sql.append("  	temp2.FLOW_AUDIT_OPERATOR_ROLE,temp2.FLOW_AUDIT_STATE");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO T");
		sql.append("  LEFT JOIN VIEW_STUDENT_INFO B ON");
		sql.append("  	B.STUDENT_ID = T.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY C ON");
		sql.append("  	C.specialty_id = T.major");
		sql.append("  	AND C.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_SCHOOL_INFO D ON");
		sql.append("  	D.ID = T.XX_ID");
		sql.append("  	AND D.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG XX ON");
		sql.append("  	XX.ID = T.XX_ID");
		sql.append("  	AND XX.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG E ON");
		sql.append("  	E.ID = T.XXZX_ID");
		sql.append("  	AND E.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG EP ON EP.ID=E.PERENT_ID AND EP.ORG_TYPE='3' AND EP.IS_DELETED = 'N'");
		sql.append("  left join gjt_specialty s on s.specialty_id=t.major and s.is_deleted='N'");
		sql.append(
				"  left join gjt_specialty_base sb on sb.specialty_base_id=s.specialty_base_id and sb.is_deleted='N'");
		sql.append("  INNER JOIN GJT_SIGNUP F ON F.STUDENT_ID = T.STUDENT_ID AND F.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN (");
		sql.append("  		SELECT");
		sql.append("  			student_id S_ID,");
		sql.append("  			audit_state FLOW_AUDIT_STATE,");
		sql.append("  			audit_operator_role FLOW_AUDIT_OPERATOR_ROLE");
		sql.append("  		FROM");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					f.student_id,");
		sql.append("  					f.audit_state,");
		sql.append("  					f.audit_operator_role,");
		sql.append("  					ROW_NUMBER() OVER(");
		sql.append("  						PARTITION BY f.student_id");
		sql.append("  					ORDER BY");
		sql.append("  						f.created_dt DESC,");
		sql.append("  						f.audit_operator_role DESC");
		sql.append("  					) id");
		sql.append("  				FROM");
		sql.append("  					gjt_flow_record f");
		sql.append("  				WHERE");
		sql.append("  					f.is_deleted = 'N'");
		sql.append("  			) temp");
		sql.append("  		WHERE");
		sql.append("  			temp.id = 1");
		sql.append("  	) temp2 ON");
		sql.append("  	temp2.s_id = t.student_id");
		sql.append("  WHERE");
		sql.append("  	T.IS_DELETED = 'N'");

		if (EmptyUtils.isNotEmpty(searchParams.get("studentIds"))) {
			if (searchParams.get("studentIds") instanceof String[]) {
				String[] studentIds = (String[]) searchParams.get("studentIds");
				List<String> studentIdsList = new ArrayList<String>(studentIds.length);
				for (int i = 0; i < studentIds.length; i++) {
					studentIdsList.add(studentIds[i]);
				}
				sql.append("  	AND T.STUDENT_ID IN :studentIds");
				params.put("studentIds", studentIdsList);
			} else {
				sql.append("  	AND T.STUDENT_ID = :studentIds ");
				params.put("studentIds", ObjectUtils.toString(searchParams.get("studentIds")));
			}
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("XM"))) {
			sql.append("  	AND T.XM = :XM ");
			params.put("XM", ObjectUtils.toString(searchParams.get("XM")));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("SFZH"))) {
			sql.append("  	AND T.SFZH = :SFZH ");
			params.put("SFZH", ObjectUtils.toString(searchParams.get("SFZH")));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("USER_TYPE"))) {
			sql.append("  	AND T.USER_TYPE = :USER_TYPE ");
			params.put("USER_TYPE", ObjectUtils.toString(searchParams.get("USER_TYPE")));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("XJZT"))) {
			sql.append("  	AND T.XJZT = :XJZT ");
			params.put("XJZT", ObjectUtils.toString(searchParams.get("XJZT")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("collegeCodes"))) {
			if (searchParams.get("collegeCodes") instanceof String[]) {
				String[] collegeCodes = (String[]) searchParams.get("collegeCodes");
				List<String> collegeCodesList = new ArrayList<String>(collegeCodes.length);
				for (int i = 0; i < collegeCodes.length; i++) {
					collegeCodesList.add(collegeCodes[i]);
				}
				sql.append("  	AND XX.CODE IN :collegeCodes");
				params.put("collegeCodes", collegeCodesList);
			} else {
				sql.append("  	AND XX.CODE= :collegeCodes ");
				params.put("collegeCodes", ObjectUtils.toString(searchParams.get("collegeCodes")));
			}
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("learncenterCodes"))) {
			sql.append("  	AND (");
			if (searchParams.get("learncenterCodes") instanceof String[]) {
				String[] learncenterCodes = (String[]) searchParams.get("learncenterCodes");
				List<String> learncenterCodesList = new ArrayList<String>(learncenterCodes.length);
				for (int i = 0; i < learncenterCodes.length; i++) {
					learncenterCodesList.add(learncenterCodes[i]);
				}
				sql.append("  	E.CODE IN :learncenterCodes OR EP.CODE IN (:learncenterCodes)"); // JPA的bug 第二个集合中sql没有生成括号的，所以要自己加上去
				params.put("learncenterCodes", learncenterCodesList);
			} else {
				sql.append("  	E.CODE = :learncenterCodes OR EP.CODE = :learncenterCodes");
				params.put("learncenterCodes", ObjectUtils.toString(searchParams.get("learncenterCodes")));
			}
			sql.append("  	)");
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("sjh"))) {
			sql.append("  	AND T.SJH = :sjh ");
			params.put("sjh", ObjectUtils.toString(searchParams.get("sjh")));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("ZSD_BM"))) {
			sql.append("  	AND T.ZSD_BM = :ZSD_BM ");
			params.put("ZSD_BM", ObjectUtils.toString(searchParams.get("ZSD_BM")));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("ZSR_ID"))) {
			sql.append("  	AND T.ZSR_ID = :ZSR_ID ");
			params.put("ZSR_ID", ObjectUtils.toString(searchParams.get("ZSR_ID")));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("orderSn"))) {
			sql.append("  	AND (");
			if (searchParams.get("orderSn") instanceof String[]) {
				String[] orderSn = (String[]) searchParams.get("orderSn");
				List<String> orderSnList = new ArrayList<String>(orderSn.length);
				for (int i = 0; i < orderSn.length; i++) {
					orderSnList.add(orderSn[i]);
				}
				sql.append("  	F.ORDER_SN IN (:orderSn)"); // JPA的bug 第二个集合中sql没有生成括号的，所以要自己加上去
				params.put("orderSn", orderSnList);
			} else {
				sql.append("  	F.ORDER_SN = :orderSn");
				params.put("orderSn", ObjectUtils.toString(searchParams.get("orderSn")));
			}
			sql.append("  	)");
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("termId"))) {
			sql.append("  	AND T.NJ = :termId ");
			params.put("termId", ObjectUtils.toString(searchParams.get("termId")));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("specialtyCode"))) {
			sql.append("  	AND sb.SPECIALTY_CODE = :specialtyCode ");
			params.put("specialtyCode", ObjectUtils.toString(searchParams.get("specialtyCode")));
		}

		if("1".equals(searchParams.get("advanceState"))) {
			sql.append("  	AND  F.CHARGE = '2' ");
		} else {
			if ("0".equals(ObjectUtils.toString(searchParams.get("AUDIT_STATE")))) {
				// 配合招生平台的查询
				sql.append("  	AND  F.CHARGE <> '2' ");
				sql.append("  	AND  F.AUDIT_STATE = :AUDIT_STATE ");
				params.put("AUDIT_STATE", ObjectUtils.toString(searchParams.get("AUDIT_STATE")));
			}

			if ("1".equals(ObjectUtils.toString(searchParams.get("AUDIT_STATE")))) {
				// 配合招生平台的查询
				sql.append("  	AND  F.CHARGE <> '2' ");
				sql.append("  	AND  F.AUDIT_STATE = :AUDIT_STATE ");
				params.put("AUDIT_STATE", ObjectUtils.toString(searchParams.get("AUDIT_STATE")));
			}

			if ("3".equals(ObjectUtils.toString(searchParams.get("AUDIT_STATE")))) {
				// 配合招生平台的查询
				sql.append("  	AND  F.CHARGE <> '2' ");
				sql.append("  	AND(");
				sql.append("  		F.AUDIT_STATE <> '0'");
				sql.append("  		AND F.AUDIT_STATE <> '1'");
				sql.append("  	)");
			}

			if ("1".equals(ObjectUtils.toString(searchParams.get("condFlow")))) {
				// 带角色查询只能查非预存的学籍
				sql.append("  	AND  F.CHARGE <> '2' ");
				sql.append("  	AND(");
				sql.append("  		temp2.FLOW_AUDIT_OPERATOR_ROLE = :FLOW_AUDIT_OPERATOR_ROLE ");
				sql.append("  		AND temp2.FLOW_AUDIT_STATE = :FLOW_AUDIT_STATE ");
				sql.append("  	)");
				params.put("FLOW_AUDIT_OPERATOR_ROLE", ObjectUtils.toString(searchParams.get("FLOW_AUDIT_OPERATOR_ROLE")));
				params.put("FLOW_AUDIT_STATE", ObjectUtils.toString(searchParams.get("FLOW_AUDIT_STATE")));
			} else if ("2".equals(ObjectUtils.toString(searchParams.get("condFlow")))) {
				// 带角色查询只能查非预存的学籍
				sql.append("  	AND  F.CHARGE <> '2' ");
				// 班主任和招生办都需要查看待提交资料的学员学籍资料信息
				sql.append("  	AND(");
				sql.append("  		(");
				sql.append("  			temp2.FLOW_AUDIT_OPERATOR_ROLE IS NULL");
				sql.append("  			AND F.AUDIT_STATE <> '0'");
				sql.append("  			AND F.AUDIT_STATE <> '1'");
				sql.append("  		)");
				sql.append("  		OR temp2.FLOW_AUDIT_OPERATOR_ROLE = :FLOW_AUDIT_OPERATOR_ROLE ");
				sql.append("  		OR F.AUDIT_STATE = '0'");
				sql.append("  		OR F.AUDIT_STATE = '1'");
				sql.append("  	)");
				params.put("FLOW_AUDIT_OPERATOR_ROLE", ObjectUtils.toString(searchParams.get("FLOW_AUDIT_OPERATOR_ROLE")));
			} else if ("0".equals(ObjectUtils.toString(searchParams.get("condFlow")))) {
				if ("1".equals(ObjectUtils.toString(searchParams.get("PERFECT_STATUS")))) {
					// 配合招生平台的查询
					sql.append("  	AND  F.CHARGE <> '2' ");
					sql.append("  	AND T.PERFECT_STATUS = :PERFECT_STATUS");
					params.put("PERFECT_STATUS", ObjectUtils.toString(searchParams.get("PERFECT_STATUS")));
				}
				if ("0".equals(ObjectUtils.toString(searchParams.get("PERFECT_STATUS")))) {
					// 配合招生平台的查询
					sql.append("  	AND  F.CHARGE <> '2' ");
					sql.append("  	AND T.PERFECT_STATUS <> 1");
				}
			}
		}
		sql.append("  ORDER BY");
		sql.append("  	T.CREATED_DT DESC");
	}

	/**
	 * 获取学员学籍数量
	 *
	 * @param searchParams
	 * @return
	 */
	public Map<String, Object> countStudentSignupNum(Map<String, Object> searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		Integer flowAuditOperatorRole = (Integer) searchParams.get("FLOW_AUDIT_OPERATOR_ROLE");
		sql.append("  SELECT");
		sql.append("  	COUNT(T.student_id) \"totalElements\",");
		sql.append("  	COUNT(CASE WHEN F.CHARGE = '2' THEN 1 ELSE NULL END) \"advanceCount\",");
		sql.append("  	COUNT(CASE WHEN F.CHARGE <> '2' AND F.AUDIT_STATE <> '0' AND F.AUDIT_STATE <> '1' THEN 1 ELSE NULL END) \"waitCount\",");
		sql.append("  	COUNT(CASE WHEN F.CHARGE <> '2' AND F.AUDIT_STATE = '1' THEN 1 ELSE NULL END) \"passCount\",");
		sql.append("  	COUNT(CASE WHEN F.CHARGE <> '2' AND F.AUDIT_STATE = '0' THEN 1 ELSE NULL END) \"noPassCount\",");
		if (EmptyUtils.isNotEmpty(flowAuditOperatorRole)) {
			sql.append("  	:FLOW_AUDIT_OPERATOR_ROLE \"currentRole\",");
			sql.append(
					"  	COUNT(CASE WHEN F.CHARGE <> '2' AND temp2.FLOW_AUDIT_OPERATOR_ROLE=:FLOW_AUDIT_OPERATOR_ROLE AND temp2.FLOW_AUDIT_STATE = 0 THEN 1 ELSE NULL END) \"currentRoleWaitCount\",");
			sql.append(
					"  	COUNT(CASE WHEN F.CHARGE <> '2' AND temp2.FLOW_AUDIT_OPERATOR_ROLE=:FLOW_AUDIT_OPERATOR_ROLE AND temp2.FLOW_AUDIT_STATE = 1 THEN 1 ELSE NULL END) \"currentRolePassCount\",");
			sql.append(
					"  	COUNT(CASE WHEN F.CHARGE <> '2' AND temp2.FLOW_AUDIT_OPERATOR_ROLE=:FLOW_AUDIT_OPERATOR_ROLE AND temp2.FLOW_AUDIT_STATE = 2 THEN 1 ELSE NULL END) \"currentRoleNoPassCount\",");
			params.put("FLOW_AUDIT_OPERATOR_ROLE", flowAuditOperatorRole);
		}
		sql.append("  	COUNT(CASE WHEN F.CHARGE <> '2' AND T.PERFECT_STATUS <> 1 THEN 1 ELSE NULL END) \"noPerfectCount\"");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO T");
		sql.append("  LEFT JOIN GJT_ORG XX ON");
		sql.append("  	XX.ID = T.XX_ID");
		sql.append("  	AND XX.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG E ON");
		sql.append("  	E.ID = T.XXZX_ID");
		sql.append("  	AND E.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG EP ON EP.ID=E.PERENT_ID AND EP.ORG_TYPE='3' AND EP.IS_DELETED = 'N'");
		sql.append("  left join gjt_specialty s on s.specialty_id=t.major and s.is_deleted='N'");
		sql.append(
				"  left join gjt_specialty_base sb on sb.specialty_base_id=s.specialty_base_id and sb.is_deleted='N'");
		sql.append("  INNER JOIN GJT_SIGNUP F ON F.STUDENT_ID = T.STUDENT_ID AND F.IS_DELETED = 'N'");
		if (EmptyUtils.isNotEmpty(flowAuditOperatorRole)) {
			sql.append("  LEFT JOIN (");
			sql.append("  		SELECT");
			sql.append("  			student_id S_ID,");
			sql.append("  			audit_state FLOW_AUDIT_STATE,");
			sql.append("  			audit_operator_role FLOW_AUDIT_OPERATOR_ROLE");
			sql.append("  		FROM");
			sql.append("  			(");
			sql.append("  				SELECT");
			sql.append("  					f.student_id,");
			sql.append("  					f.audit_state,");
			sql.append("  					f.audit_operator_role,");
			sql.append("  					ROW_NUMBER() OVER(");
			sql.append("  						PARTITION BY f.student_id");
			sql.append("  					ORDER BY");
			sql.append("  						f.created_dt DESC,");
			sql.append("  						f.audit_operator_role DESC");
			sql.append("  					) id");
			sql.append("  				FROM");
			sql.append("  					gjt_flow_record f");
			sql.append("  				WHERE");
			sql.append("  					f.is_deleted = 'N'");
			sql.append("  			) temp");
			sql.append("  		WHERE");
			sql.append("  			temp.id = 1");
			sql.append("  	) temp2 ON");
			sql.append("  	temp2.s_id = t.student_id");
		}
		sql.append("  WHERE");
		sql.append("  	T.IS_DELETED = 'N'");

		if (EmptyUtils.isNotEmpty(searchParams.get("XM"))) {
			sql.append("  	AND T.XM = :XM ");
			params.put("XM", ObjectUtils.toString(searchParams.get("XM")));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("SFZH"))) {
			sql.append("  	AND T.SFZH = :SFZH ");
			params.put("SFZH", ObjectUtils.toString(searchParams.get("SFZH")));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("USER_TYPE"))) {
			sql.append("  	AND T.USER_TYPE = :USER_TYPE ");
			params.put("USER_TYPE", ObjectUtils.toString(searchParams.get("USER_TYPE")));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("XJZT"))) {
			sql.append("  	AND T.XJZT = :XJZT ");
			params.put("XJZT", ObjectUtils.toString(searchParams.get("XJZT")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("collegeCodes"))) {
			if (searchParams.get("collegeCodes") instanceof String[]) {
				String[] collegeCodes = (String[]) searchParams.get("collegeCodes");
				List<String> collegeCodesList = new ArrayList<String>(collegeCodes.length);
				for (int i = 0; i < collegeCodes.length; i++) {
					collegeCodesList.add(collegeCodes[i]);
				}
				sql.append("  	AND XX.CODE IN :collegeCodes");
				params.put("collegeCodes", collegeCodesList);
			} else {
				sql.append("  	AND XX.CODE= :collegeCodes ");
				params.put("collegeCodes", ObjectUtils.toString(searchParams.get("collegeCodes")));
			}
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("learncenterCodes"))) {
			sql.append("  	AND (");
			if (searchParams.get("learncenterCodes") instanceof String[]) {
				String[] learncenterCodes = (String[]) searchParams.get("learncenterCodes");
				List<String> learncenterCodesList = new ArrayList<String>(learncenterCodes.length);
				for (int i = 0; i < learncenterCodes.length; i++) {
					learncenterCodesList.add(learncenterCodes[i]);
				}
				sql.append("  	E.CODE IN :learncenterCodes OR EP.CODE IN (:learncenterCodes)"); // JPA的bug 第二个集合中sql没有生成括号的，所以要自己加上去
				params.put("learncenterCodes", learncenterCodesList);
			} else {
				sql.append("  	E.CODE = :learncenterCodes OR EP.CODE = :learncenterCodes");
				params.put("learncenterCodes", ObjectUtils.toString(searchParams.get("learncenterCodes")));
			}
			sql.append("  	)");
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("sjh"))) {
			sql.append("  	AND T.SJH = :sjh ");
			params.put("sjh", ObjectUtils.toString(searchParams.get("sjh")));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("ZSD_BM"))) {
			sql.append("  	AND T.ZSD_BM = :ZSD_BM ");
			params.put("ZSD_BM", ObjectUtils.toString(searchParams.get("ZSD_BM")));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("ZSR_ID"))) {
			sql.append("  	AND T.ZSR_ID = :ZSR_ID ");
			params.put("ZSR_ID", ObjectUtils.toString(searchParams.get("ZSR_ID")));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("orderSn"))) {
			sql.append("  	AND (");
			if (searchParams.get("orderSn") instanceof String[]) {
				String[] orderSn = (String[]) searchParams.get("orderSn");
				List<String> orderSnList = new ArrayList<String>(orderSn.length);
				for (int i = 0; i < orderSn.length; i++) {
					orderSnList.add(orderSn[i]);
				}
				sql.append("  	F.ORDER_SN IN (:orderSn)"); // JPA的bug 第二个集合中sql没有生成括号的，所以要自己加上去
				params.put("orderSn", orderSnList);
			} else {
				sql.append("  	F.ORDER_SN = :orderSn");
				params.put("orderSn", ObjectUtils.toString(searchParams.get("orderSn")));
			}
			sql.append("  	)");
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("termId"))) {
			sql.append("  	AND T.NJ = :termId ");
			params.put("termId", ObjectUtils.toString(searchParams.get("termId")));
		}
		if (EmptyUtils.isNotEmpty(searchParams.get("specialtyCode"))) {
			sql.append("  	AND sb.SPECIALTY_CODE = :specialtyCode ");
			params.put("specialtyCode", ObjectUtils.toString(searchParams.get("specialtyCode")));
		}

		List<Map<String, Object>> list = commonDao.queryForStringObjectMapListNative(sql.toString(), params);
		return list != null && list.size() > 0 ? list.get(0) : Collections.EMPTY_MAP;
	}

	public List queryStudentInfo(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TEMP.*");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GSI.STUDENT_ID,");
		sql.append("  			GSI.XM ");
		sql.append("  		FROM");
		sql.append("  			GJT_STUDENT_INFO GSI");
		sql.append("  		WHERE");
		sql.append("  			GSI.IS_DELETED = 'N'");

		if (EmptyUtils.isNotEmpty(searchParams.get("studentId"))) {
			sql.append("  			AND GSI.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("studentId")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("atId"))) {
			sql.append("  			AND GSI.ATID = :ATID ");
			params.put("ATID", ObjectUtils.toString(searchParams.get("atId")));
		}

		sql.append("  		ORDER BY");
		sql.append("  			GSI.CREATED_DT DESC");
		sql.append("  	) TEMP");
		sql.append("  WHERE");
		sql.append("  	ROWNUM = 1");

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询学员的学籍资料审核记录
	 * 
	 * @param studentId
	 * @return
	 */
	public List queryFlowRecordByStudentId(String studentId) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT");
		sql.append("  	T.*");
		sql.append("  FROM");
		sql.append("  	GJT_FLOW_RECORD T");
		sql.append("  WHERE");
		sql.append("  	T.IS_DELETED = 'N'");
		sql.append("  	AND T.STUDENT_ID = :studentId ");
		sql.append("  ORDER BY");
		sql.append("  	T.CREATED_DT,");
		sql.append("  	T.AUDIT_OPERATOR_ROLE");

		params.put("studentId", studentId);

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 更新审核记录
	 * 
	 * @param searchParams
	 * @return
	 */
	public int updateFlowRecord(Map searchParams) throws Exception {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE");
		sql.append("  	GJT_FLOW_RECORD T");
		sql.append("  SET");
		sql.append("  	T.VERSION = T.VERSION + 1,");
		sql.append("  	T.UPDATED_DT = SYSDATE ");

		if (EmptyUtils.isNotEmpty(searchParams.get("updatedBy"))) {
			sql.append("  	,T.UPDATED_BY = :updatedBy ");
			params.put("updatedBy", ObjectUtils.toString(searchParams.get("updatedBy")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("auditContent"))) {
			sql.append("  	,T.AUDIT_CONTENT = :auditContent ");
			params.put("auditContent", ObjectUtils.toString(searchParams.get("auditContent")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("auditDt"))) {
			sql.append("  	,T.AUDIT_DT = :auditDt ");
			params.put("auditDt", DateUtils.getDateToString(ObjectUtils.toString(searchParams.get("auditDt"))));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("auditOperator"))) {
			sql.append("  	,T.AUDIT_OPERATOR = :auditOperator ");
			params.put("auditOperator", ObjectUtils.toString(searchParams.get("auditOperator")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("auditOperatorRole"))) {
			sql.append("  	,T.AUDIT_OPERATOR_ROLE= :auditOperatorRole ");
			params.put("auditOperatorRole",
					Integer.parseInt(ObjectUtils.toString(searchParams.get("auditOperatorRole"))));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("auditState"))) {
			sql.append("  	,T.AUDIT_STATE = :auditState ");
			params.put("auditState", Integer.parseInt(ObjectUtils.toString(searchParams.get("auditState"))));
		}

		sql.append("  WHERE");
		sql.append("  	T.FLOW_RECORD_ID = :flowRecordId ");

		params.put("flowRecordId", ObjectUtils.toString(searchParams.get("flowRecordId")));

		return commonDao.updateForMapNative(sql.toString(), params);
	}

	/**
	 * 添加审核记录
	 * 
	 * @param searchParams
	 * @return
	 */
	public int addFlowRecord(Map searchParams) throws Exception {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_FLOW_RECORD(");
		sql.append("  			FLOW_RECORD_ID,");
		sql.append("  			AUDIT_CONTENT,");
		sql.append("  			AUDIT_DT,");
		sql.append("  			AUDIT_OPERATOR,");
		sql.append("  			AUDIT_OPERATOR_ROLE,");
		sql.append("  			AUDIT_STATE,");
		sql.append("  			CREATED_BY,");
		sql.append("  			CREATED_DT,");
		sql.append("  			STUDENT_ID");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:flowRecordId,");
		sql.append("  		:auditContent,");
		sql.append("  		:auditDt,");
		sql.append("  		:auditOperator,");
		sql.append("  		:auditOperatorRole,");
		sql.append("  		:auditState,");
		sql.append("  		:createdBy,");
		sql.append("  		SYSDATE,");
		sql.append("  		:studentId ");
		sql.append("  	)");

		params.put("flowRecordId", ObjectUtils.toString(searchParams.get("flowRecordId")));
		params.put("auditContent", ObjectUtils.toString(searchParams.get("auditContent")));
		params.put("auditDt", DateUtils.getDateToString(ObjectUtils.toString(searchParams.get("auditDt"))));
		params.put("auditOperator", ObjectUtils.toString(searchParams.get("auditOperator")));
		params.put("auditOperatorRole", Integer.parseInt(ObjectUtils.toString(searchParams.get("auditOperatorRole"))));
		params.put("auditState", Integer.parseInt(ObjectUtils.toString(searchParams.get("auditState"))));
		params.put("createdBy", ObjectUtils.toString(searchParams.get("createdBy")));
		params.put("studentId", ObjectUtils.toString(searchParams.get("studentId")));

		return commonDao.insertForMapNative(sql.toString(), params);
	}

	public int auditSignupInfoNotPass(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE");
		sql.append("  	GJT_SIGNUP GS");
		sql.append("  SET");
		sql.append("  	GS.VERSION = GS.VERSION + 1,");
		sql.append("  	GS.UPDATED_DT = SYSDATE ");

		if (EmptyUtils.isNotEmpty(searchParams.get("updatedBy"))) {
			sql.append("  	,GS.UPDATED_BY = :updatedBy ");
			params.put("updatedBy", ObjectUtils.toString(searchParams.get("updatedBy")));
		}

		sql.append("  	GS.AUDIT_STATE = '0'");
		sql.append("  WHERE");
		sql.append("  	GS.IS_DELETED = 'N'");
		sql.append("  	AND GS.STUDENT_ID = :studentId ");

		params.put("studentId", ObjectUtils.toString(searchParams.get("studentId")));

		return commonDao.updateForMapNative(sql.toString(), params);
	}

	public List<Map<String, Object>> getOrgAll() {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT *");
		sql.append("  FROM GJT_ORG ORG");
		sql.append("  WHERE ORG.IS_DELETED = 'N'");
		sql.append("  AND ORG.CODE IS NOT NULL");

		return commonDao.queryForStringObjectMapListNative(sql.toString(), params);
	}

	public List<Map<String, Object>> getOrgByCodes(String[] codes) {
		Map params = new HashMap();
		// params.put("codes",codes);
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GO.ID,");
		sql.append("  	GO.CODE");
		sql.append("  FROM");
		sql.append("  	GJT_ORG GO");
		sql.append("  WHERE");
		sql.append("  	GO.IS_DELETED = 'N'");

		if (EmptyUtils.isNotEmpty(codes)) {
			if (codes instanceof String[]) {
				List<String> codesList = new ArrayList<String>(codes.length);
				for (int i = 0; i < codes.length; i++) {
					codesList.add(codes[i]);
				}
				sql.append("  	AND GO.CODE IN :codes");
				params.put("codes", codesList);
			} else {
				sql.append("	AND GO.CODE = :CODE ");
				params.put("CODE", org.apache.commons.lang.ObjectUtils.toString(codes));
			}
		}

		return commonDao.queryForStringObjectMapListNative(sql.toString(), params);

	}

	public List<Map<String, Object>> queryGradeSpecialt(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GG.GRADE_ID,GG.GRADE_NAME,GG.START_DATE,GG.END_DATE,");
		sql.append("  	GGS.ID AS GRADE_SPECIALTY_ID,");
		sql.append("  	GS.SPECIALTY_ID,");
		sql.append("  	GS.ZYMC,");
		sql.append("  	GS.RULE_CODE,");
		sql.append("  	GS.XZ,");
		sql.append("  	GS.ZDBYXF,");
		sql.append("  	GS.PYCC,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.TYPE_CODE = 'TrainingLevel'");
		sql.append("  			AND TSD.CODE = GS.PYCC");
		sql.append("  	) SPECIALTY_LEVEL,");
		sql.append("  	GS.SUBJECT,");
		sql.append("  	GSB.SPECIALTY_CODE,");
		sql.append(
				"  	NVL(( SELECT TO_CHAR( WMSYS.WM_CONCAT( GSC.SC_NAME )) FROM GJT_STUDY_CENTER GSC, GJT_GS_STUDY_CENTER GGSC WHERE GSC.ID = GGSC.STUDY_CENTER_ID AND GGSC.GRADE_SPECIALTY_ID = GGS.ID ), '通用' ) AS APPLY_RANGE,");
		sql.append(
				"  	NVL(( SELECT TO_CHAR( WMSYS.WM_CONCAT( GSC.ID )) FROM GJT_STUDY_CENTER GSC, GJT_GS_STUDY_CENTER GGSC WHERE GSC.ID = GGSC.STUDY_CENTER_ID AND GGSC.GRADE_SPECIALTY_ID = GGS.ID ), '' ) AS APPLY_RANGE_IDS");
		sql.append("  FROM");
		sql.append("  	GJT_GRADE GG,");
		sql.append("  	GJT_GRADE_SPECIALTY GGS,");
		sql.append("  	GJT_SPECIALTY GS,");
		sql.append("  	GJT_SPECIALTY_BASE GSB");
		sql.append("  WHERE");
		sql.append("  	GG.IS_DELETED = 'N'");
		sql.append("  	AND GGS.IS_DELETED = 'N'");
		sql.append("  	AND GS.IS_DELETED = 'N'");
		sql.append("  	AND GSB.IS_DELETED = 'N'");
		sql.append("  	AND GG.GRADE_ID = GGS.GRADE_ID");
		sql.append("  	AND GGS.SPECIALTY_ID = GS.SPECIALTY_ID");
		sql.append("  	AND GS.SPECIALTY_BASE_ID = GSB.SPECIALTY_BASE_ID");
		sql.append("  	AND GS.XX_ID = :xxId ");

		params.put("xxId", ObjectUtils.toString(searchParams.get("xxId")));

		if (EmptyUtils.isNotEmpty(searchParams.get("gradeId"))) {
			sql.append("  	AND GG.GRADE_ID = :gradeId ");
			params.put("gradeId", ObjectUtils.toString(searchParams.get("gradeId")));
		}

		return commonDao.queryForStringObjectMapListNative(sql.toString(), params);

	}

	public Map getOrgByCollegeCode(String codes) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	*");
		sql.append("  FROM");
		sql.append("  	GJT_ORG ORG");
		sql.append("  WHERE");
		sql.append("  	ORG.IS_DELETED = 'N'");
		sql.append("  	AND ORG.CODE = :codes ");

		params.put("codes", codes);

		return commonDao.queryObjectToMapNative(sql.toString(), params);
	}

	public List<Map<String, Object>> queryGradeList(String xxId) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GG.GRADE_ID,");
		sql.append("  	GG.GRADE_NAME,");
		sql.append("  	TO_CHAR( GG.START_DATE, 'yyyy-MM-dd' ) START_DATE,");
		sql.append("  	TO_CHAR( NVL( GG.END_DATE, ADD_MONTHS( GG.START_DATE, 4 )), 'yyyy-MM-dd' ) END_DATE");
		sql.append("  FROM");
		sql.append("  	GJT_GRADE GG");
		sql.append("  WHERE");
		sql.append("  	GG.IS_DELETED = 'N'");
		sql.append("  	AND GG.IS_ENABLED = '1' ");
		sql.append("  	AND GG.XX_ID = :xxId");
		sql.append("  ORDER BY");
		sql.append("  	GG.START_DATE");

		params.put("xxId", xxId);

		return commonDao.queryForStringObjectMapListNative(sql.toString(), params);

	}

	/**
	 * 查询学期专业计划
	 * 
	 * @param searchParams
	 * @return
	 */
	public List queryGradeSpecialtyPlan(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GTP.TEACH_PLAN_ID,");
		sql.append("  	GTP.XF,");
		sql.append("  	GG.GRADE_ID,");
		sql.append("  	GG.GRADE_NAME,");
		sql.append("  	TO_CHAR(GG.START_DATE,'yyyy-MM-dd') START_DATE,");
		sql.append("  	TO_CHAR(NVL(GG.END_DATE,ADD_MONTHS(GG.START_DATE,4)),'yyyy-MM-dd') END_DATE,");
		sql.append("  	GS.SPECIALTY_ID,");
		sql.append("  	GS.ZYMC,");
		sql.append("  	GS.ZXF,");
		sql.append("  	GC.COURSE_ID,");
		sql.append("  	GC.KCMC,");
		sql.append("  	TO_CHAR(GC.KCJJ) KCJJ,");
		sql.append("  	GC.KCFM");
		sql.append("  FROM");
		sql.append("  	VIEW_TEACH_PLAN GTP");
		sql.append("  INNER JOIN GJT_GRADE GG ON");
		sql.append("  	GTP.ACTUAL_GRADE_ID = GG.GRADE_ID");
		sql.append("  	AND GG.IS_DELETED = 'N'");
		sql.append("  INNER JOIN GJT_COURSE GC ON");
		sql.append("  	GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("  	AND GC.IS_DELETED = 'N'");
		sql.append("  INNER JOIN GJT_SPECIALTY GS ON");
		sql.append("  	GTP.KKZY = GS.SPECIALTY_ID");
		sql.append("  	AND GS.IS_DELETED = 'N'");
		sql.append("  WHERE");
		sql.append("  	GG.GRADE_ID = :gradeId ");
		sql.append("  ORDER BY");
		sql.append("  	GTP.KKZY,");
		sql.append("  	GG.GRADE_CODE");

		params.put("gradeId", ObjectUtils.toString(searchParams.get("gradeId")));

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

}
