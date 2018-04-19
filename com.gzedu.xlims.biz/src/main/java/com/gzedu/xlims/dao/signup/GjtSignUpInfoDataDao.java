package com.gzedu.xlims.dao.signup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;

@Repository
public class GjtSignUpInfoDataDao extends BaseDaoImpl {

	@Autowired
	private GjtOrgDao gjtOrgDao;
	
	@Autowired
	private CommonDao commonDao;
	
	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;
	
	/**
	 * 查询报读信息
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page getSignUpList(Map searchParams,PageRequest pageRequst){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	DISTINCT GSI.STUDENT_ID,");
		sql.append("  	GSI.XM,");
		sql.append("  	GSI.XH,");
		sql.append("  	GSI.XBM,");
		sql.append("  	GSI.SJH,");
		sql.append("  	GSI.SFZH,");
		sql.append("  	GSI.AVATAR,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.CODE = GSI.PYCC");
		sql.append("  			AND TSD.TYPE_CODE = 'TrainingLevel'");
		sql.append("  	) PYCC_NAME,");
		sql.append("  	GG.GRADE_NAME,");
		sql.append("  	C.ZYMC,");
		sql.append("  	TO_CHAR( GSI.CREATED_DT, 'yyyy-MM-dd hh24:mi' ) CREATED_DT,");
		sql.append("  	NVL(GS.AUDIT_SOURCE,'--') AUDIT_SOURCE,");
		sql.append("  	E.ORG_NAME SC_NAME,");
		sql.append("  	TRIM(NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GS.AUDIT_STATE AND TSD.TYPE_CODE = 'AUDIT_STATE' ), '--' )) AUDIT_STATE,");

		sql.append("  	TRIM(NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.XJZT AND TSD.TYPE_CODE = 'StudentNumberStatus' ), '--' )) XJZT,");
		sql.append("  	GS.CHARGE_DESCRIB,");
		sql.append("  	GS.ORDER_SN,");
		sql.append("  	GS.CHARGE CHARGE_FLG,");
        sql.append("  	TRIM(NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GS.CHARGE AND TSD.TYPE_CODE = 'CHARGE_STATE' ), '--' )) CHARGE ");

		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN VIEW_STUDENT_INFO B ON");
		sql.append("  	B.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_GRADE GG ON");
		sql.append("  	GG.IS_DELETED = 'N'");
		sql.append("  	AND GG.GRADE_ID = B.GRADE_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY C ON");
		sql.append("  	C.SPECIALTY_ID = GSI.MAJOR");
		sql.append("  LEFT JOIN GJT_SCHOOL_INFO D ON");
		sql.append("  	D.ID = GSI.XX_ID");
		sql.append("  	AND D.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG E ON");
		sql.append("  	E.ID = GSI.XXZX_ID");
		sql.append("  	AND E.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN(");
		sql.append("  		SELECT");
		sql.append("  			STUDENT_ID S_ID,");
		sql.append("  			AUDIT_STATE FLOW_AUDIT_STATE,");
		sql.append("  			AUDIT_OPERATOR_ROLE FLOW_AUDIT_OPERATOR_ROLE");
		sql.append("  		FROM");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					F.STUDENT_ID,");
		sql.append("  					F.AUDIT_STATE,");
		sql.append("  					F.AUDIT_OPERATOR_ROLE,");
		sql.append("  					ROW_NUMBER() OVER(");
		sql.append("  						PARTITION BY F.STUDENT_ID");
		sql.append("  					ORDER BY");
		sql.append("  						F.CREATED_DT DESC,");
		sql.append("  						F.AUDIT_OPERATOR_ROLE DESC");
		sql.append("  					) ID");
		sql.append("  				FROM");
		sql.append("  					GJT_FLOW_RECORD F");
		sql.append("  				WHERE");
		sql.append("  					F.IS_DELETED = 'N'");
		sql.append("  			) TEMP");
		sql.append("  		WHERE");
		sql.append("  			TEMP.ID = 1");
		sql.append("  	) TEMP2 ON");
		sql.append("  	TEMP2.S_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_SIGNUP GS ON");
		sql.append("  	GS.IS_DELETED = 'N'");
		sql.append("  	AND GS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");

		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("STUDY_ID"));
		if(EmptyUtils.isNotEmpty(studyId)){
			sql.append("  	AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxzxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			params.put("xxzxId", studyId);
		} else {
			// 院校ID
			String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
			if(EmptyUtils.isNotEmpty(xxId)){
				sql.append("  	AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
				params.put("xxId", xxId);
			}
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))){
			sql.append("	AND GSI.XH = :XH ");
			params.put("XH", ObjectUtils.toString(searchParams.get("XH")).trim());
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))){
			sql.append("	AND GSI.XM LIKE :XM ");
			params.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM")).trim()+"%");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))){
			sql.append("	AND C.SPECIALTY_ID = :SPECIALTY_ID ");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))){
			sql.append("	AND B.GRADE_ID = :GRADE_ID ");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC_ID")))){
			sql.append("	AND GSI.PYCC = :PYCC_ID ");
			params.put("PYCC_ID", ObjectUtils.toString(searchParams.get("PYCC_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("AUDIT_STATE"))){
			if(searchParams.get("AUDIT_STATE") instanceof String[]){
				String[] audit_state = (String[]) searchParams.get("AUDIT_STATE");
				StringBuffer buffer = new StringBuffer();
				for(int i=0;i<audit_state.length-1;i++){
					buffer.append(audit_state[i]+"','");
				}
				sql.append("	AND GS.AUDIT_STATE IN ('"+buffer.toString()+"')");
			}else{
				sql.append("	AND GS.AUDIT_STATE = :AUDIT_STATE ");
				params.put("AUDIT_STATE", ObjectUtils.toString(searchParams.get("AUDIT_STATE")));
			}
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("XJZT_STATE"))){
			if(searchParams.get("XJZT_STATE") instanceof String[]){
				String[] xjzt_state = (String[]) searchParams.get("XJZT_STATE");
				StringBuffer buffer = new StringBuffer();
				for(int i=0;i<=xjzt_state.length-1;i++){
					buffer.append(xjzt_state[i]+"','");
				}
				sql.append("	AND GSI.XJZT IN ('"+buffer.toString()+"')");
			}else{
				sql.append("	AND GSI.XJZT = :XJZT_STATE ");
				params.put("XJZT_STATE", ObjectUtils.toString(searchParams.get("XJZT_STATE")));
			}
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("CHARGE"))){
			if(searchParams.get("CHARGE") instanceof String[]){
				String[] charge = (String[]) searchParams.get("CHARGE");
				StringBuffer buffer = new StringBuffer();
				for(int i=0;i<=charge.length-1;i++){
					buffer.append(charge[i]+"','");
				}
				sql.append("	AND GS.CHARGE IN ('"+buffer.toString()+"')");
			}else{
				sql.append("	AND GS.CHARGE = :CHARGE ");
				params.put("CHARGE", ObjectUtils.toString(searchParams.get("CHARGE")));
			}
		}
		
	
		return (Page)commonDao.queryForPageNative(sql.toString(), params, pageRequst);
		
	}

	public List getXjzt(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	TSD.CODE,");
		sql.append("  	TSD.NAME");
		sql.append("  FROM");
		sql.append("  	TBL_SYS_DATA TSD");
		sql.append("  WHERE");
		sql.append("  	TSD.IS_DELETED = 'N'");
		sql.append("  	AND TSD.TYPE_CODE = 'StudentNumberStatus'");

		return commonDao.queryForMapListNative(sql.toString(),params);

	}

	
	/**
	 * 报读信息详情
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List querySignUpDetail(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GSI.STUDENT_ID,");
		sql.append("  	GSI.XM,");
		sql.append("  	GSI.XH,");
		sql.append("  	GSI.SJH,");
		sql.append("  	GSI.SFZH,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.CODE = GSI.PYCC");
		sql.append("  			AND TSD.TYPE_CODE = 'TrainingLevel'");
		sql.append("  	) PYCC_NAME,");
		sql.append("  	GG.GRADE_NAME,");
		sql.append("  	C.ZYMC,");
		sql.append("  	TO_CHAR( GSI.CREATED_DT, 'YYYY' )|| '年' || TO_CHAR( GSI.CREATED_DT, 'MM' )|| '月' || TO_CHAR( GSI.CREATED_DT, 'dd' )|| '日' CREATED_DT,");
		sql.append("  	GS.AUDIT_SOURCE,");
		sql.append("  	E.ORG_NAME SC_NAME,");
		sql.append("  	TRIM(NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GS.AUDIT_STATE AND TSD.TYPE_CODE = 'AUDIT_STATE' ), '--' )) AUDIT_STATE,");
		sql.append("  	TRIM(NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.XJZT AND TSD.TYPE_CODE = 'StudentNumberStatus' ), '--' )) XJZT,");
		sql.append("  	GS.CHARGE_DESCRIB,");
		sql.append("  	GS.ORDER_SN,");
		sql.append("  	GS.CHARGE CHARGE_FLG,");
		sql.append("  	TRIM( NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GS.CHARGE AND TSD.TYPE_CODE = 'CHARGE_STATE' ), '--' )) CHARGE,");
		sql.append("  	GSI.DZXX,");
		sql.append("  	GSI.SC_CO_ADDR,");
		sql.append("  	GSI.XZZ,");
		sql.append("  	D.XXMC,");
		sql.append("  	GSI.SC_CO");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN VIEW_STUDENT_INFO B ON");
		sql.append("  	B.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_GRADE GG ON");
		sql.append("  	GG.IS_DELETED = 'N'");
		sql.append("  	AND GG.GRADE_ID = B.GRADE_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY C ON");
		sql.append("  	C.SPECIALTY_ID = GSI.MAJOR");
		sql.append("  LEFT JOIN GJT_SCHOOL_INFO D ON");
		sql.append("  	D.ID = GSI.XX_ID");
		sql.append("  	AND D.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG E ON");
		sql.append("  	E.ID = GSI.XXZX_ID");
		sql.append("  	AND E.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN(");
		sql.append("  		SELECT");
		sql.append("  			STUDENT_ID S_ID,");
		sql.append("  			AUDIT_STATE FLOW_AUDIT_STATE,");
		sql.append("  			AUDIT_OPERATOR_ROLE FLOW_AUDIT_OPERATOR_ROLE");
		sql.append("  		FROM");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					F.STUDENT_ID,");
		sql.append("  					F.AUDIT_STATE,");
		sql.append("  					F.AUDIT_OPERATOR_ROLE,");
		sql.append("  					ROW_NUMBER() OVER(");
		sql.append("  						PARTITION BY F.STUDENT_ID");
		sql.append("  					ORDER BY");
		sql.append("  						F.CREATED_DT DESC,");
		sql.append("  						F.AUDIT_OPERATOR_ROLE DESC");
		sql.append("  					) ID");
		sql.append("  				FROM");
		sql.append("  					GJT_FLOW_RECORD F");
		sql.append("  				WHERE");
		sql.append("  					F.IS_DELETED = 'N'");
		sql.append("  			) TEMP");
		sql.append("  		WHERE");
		sql.append("  			TEMP.ID = 1");
		sql.append("  	) TEMP2 ON");
		sql.append("  	TEMP2.S_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_SIGNUP GS ON");
		sql.append("  	GS.IS_DELETED = 'N'");
		sql.append("  	AND GS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");
		sql.append("  	AND GSI.STUDENT_ID = :STUDENT_ID ");
		
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}

	/**
	 * 导出报读信息统计表
	 * @param searchParams
	 * @return
	 */
//	public List<Map<String, String>> exportSignUpList(String xxId,Map<String, Object> searchParams){
	@SuppressWarnings("rawtypes")
	public List exportSignUpList(Map searchParams){
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	DISTINCT GSI.XM,");
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN GSI.XBM = '1' THEN '男'");
		sql.append("  			WHEN GSI.XBM = '2' THEN '女'");
		sql.append("  			ELSE '--'");
		sql.append("  		END");
		sql.append("  	) XBM,");
		sql.append("  	GSI.XH,");
		sql.append("  	GSI.SFZH,");
		sql.append("  	GSI.SJH,");
		sql.append("  	GG.GRADE_NAME,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.CODE = GSI.PYCC");
		sql.append("  			AND TSD.TYPE_CODE = 'TrainingLevel'");
		sql.append("  	) PYCC_NAME,");
		sql.append("  	C.ZYMC,");
		sql.append("  	TO_CHAR( GSI.CREATED_DT, 'yyyy-MM-dd hh24:mi' ) CREATED_DT,");
		sql.append("  	NVL( GS.AUDIT_SOURCE, '--' ) AUDIT_SOURCE,");
		sql.append("  	NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GS.AUDIT_STATE AND TSD.TYPE_CODE = 'AUDIT_STATE' ), '--' ) AUDIT_STATE,");
		/*
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN GS.AUDIT_STATE = '0' THEN '审核不通过'");
		sql.append("  			WHEN GS.AUDIT_STATE = '1' THEN '审核通过'");
		sql.append("  			WHEN GS.AUDIT_STATE = '2' THEN '审核中'");
		sql.append("  			WHEN GS.AUDIT_STATE = '3' THEN '待审核'");
		sql.append("  			WHEN GS.AUDIT_STATE = '4' THEN '未审核'");
		sql.append("  			ELSE '--'");
		sql.append("  		END");
		sql.append("  	) AUDIT_STATE,");
		*/
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.CODE = GSI.XJZT");
		sql.append("  			AND TSD.TYPE_CODE = 'StudentNumberStatus'");
		sql.append("  	) XJZT,");
		sql.append("  	NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GS.CHARGE AND TSD.TYPE_CODE = 'CHARGE_STATE' ), '--' ) CHARGE,");
		/*
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN GS.CHARGE = '0' THEN '未缴费'");
		sql.append("  			WHEN GS.CHARGE = '1' THEN '已缴费'");
		sql.append("  			ELSE '--'");
		sql.append("  		END");
		sql.append("  	) CHARGE,");
		*/
		sql.append("  	E.ORG_NAME SC_NAME");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN VIEW_STUDENT_INFO B ON");
		sql.append("  	B.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_GRADE GG ON");
		sql.append("  	GG.IS_DELETED = 'N'");
		sql.append("  	AND GG.GRADE_ID = B.GRADE_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY C ON");
		sql.append("  	C.SPECIALTY_ID = GSI.MAJOR");
		sql.append("  LEFT JOIN GJT_SCHOOL_INFO D ON");
		sql.append("  	D.ID = GSI.XX_ID");
		sql.append("  	AND D.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG E ON");
		sql.append("  	E.ID = GSI.XXZX_ID");
		sql.append("  	AND E.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN(");
		sql.append("  		SELECT");
		sql.append("  			STUDENT_ID S_ID,");
		sql.append("  			AUDIT_STATE FLOW_AUDIT_STATE,");
		sql.append("  			AUDIT_OPERATOR_ROLE FLOW_AUDIT_OPERATOR_ROLE");
		sql.append("  		FROM");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					F.STUDENT_ID,");
		sql.append("  					F.AUDIT_STATE,");
		sql.append("  					F.AUDIT_OPERATOR_ROLE,");
		sql.append("  					ROW_NUMBER() OVER(");
		sql.append("  						PARTITION BY F.STUDENT_ID");
		sql.append("  					ORDER BY");
		sql.append("  						F.CREATED_DT DESC,");
		sql.append("  						F.AUDIT_OPERATOR_ROLE DESC");
		sql.append("  					) ID");
		sql.append("  				FROM");
		sql.append("  					GJT_FLOW_RECORD F");
		sql.append("  				WHERE");
		sql.append("  					F.IS_DELETED = 'N'");
		sql.append("  			) TEMP");
		sql.append("  		WHERE");
		sql.append("  			TEMP.ID = 1");
		sql.append("  	) TEMP2 ON");
		sql.append("  	TEMP2.S_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_SIGNUP GS ON");
		sql.append("  	GS.IS_DELETED = 'N'");
		sql.append("  	AND GS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");

		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("STUDY_ID"));
		if(EmptyUtils.isNotEmpty(studyId)){
			sql.append("  	AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxzxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			params.put("xxzxId", studyId);
		} else {
			// 院校ID
			String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
			if(EmptyUtils.isNotEmpty(xxId)){
				sql.append("  	AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
				params.put("xxId", xxId);
			}
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("GRADE_ID"))){
			sql.append("  	AND B.GRADE_ID = :GRADE_ID ");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("PYCC_ID"))){
			sql.append("  	AND GSI.PYCC = :PYCC_ID");
			params.put("PYCC_ID", ObjectUtils.toString(searchParams.get("PYCC_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("SPECIALTY_ID"))){
			sql.append("  	AND GS.SPECIALTY_ID = :SPECIALTY_ID");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("AUDIT_STATE"))){
			sql.append("  	AND GS.AUDIT_STATE = :AUDIT_STATE");
			params.put("AUDIT_STATE", ObjectUtils.toString(searchParams.get("AUDIT_STATE")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("XJZT"))){
			sql.append("  	AND GSI.XJZT = :XJZT");
			params.put("XJZT", ObjectUtils.toString(searchParams.get("XJZT")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("CHARGE"))){
			sql.append("  	AND GS.CHARGE = :CHARGE");
			params.put("CHARGE", ObjectUtils.toString(searchParams.get("CHARGE")));
		}
		
//		return (List<Map<String, String>>)commonDao.queryForMapListNative(sql.toString(), params);
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 用户属性统计 (年龄)
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List userAttributeCount_AGE(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT(1) STUDENT_COUNT");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN VIEW_STUDENT_INFO B ON");
		sql.append("  	B.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_GRADE GG ON");
		sql.append("  	GG.IS_DELETED = 'N'");
		sql.append("  	AND GG.GRADE_ID = B.GRADE_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY C ON");
		sql.append("  	C.SPECIALTY_ID = GSI.MAJOR");
		sql.append("  LEFT JOIN GJT_SCHOOL_INFO D ON");
		sql.append("  	D.ID = GSI.XX_ID");
		sql.append("  	AND D.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG E ON");
		sql.append("  	E.ID = GSI.XXZX_ID");
		sql.append("  	AND E.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN(");
		sql.append("  		SELECT");
		sql.append("  			STUDENT_ID S_ID,");
		sql.append("  			AUDIT_STATE FLOW_AUDIT_STATE,");
		sql.append("  			AUDIT_OPERATOR_ROLE FLOW_AUDIT_OPERATOR_ROLE");
		sql.append("  		FROM");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					F.STUDENT_ID,");
		sql.append("  					F.AUDIT_STATE,");
		sql.append("  					F.AUDIT_OPERATOR_ROLE,");
		sql.append("  					ROW_NUMBER() OVER(");
		sql.append("  						PARTITION BY F.STUDENT_ID");
		sql.append("  					ORDER BY");
		sql.append("  						F.CREATED_DT DESC,");
		sql.append("  						F.AUDIT_OPERATOR_ROLE DESC");
		sql.append("  					) ID");
		sql.append("  				FROM");
		sql.append("  					GJT_FLOW_RECORD F");
		sql.append("  				WHERE");
		sql.append("  					F.IS_DELETED = 'N'");
		sql.append("  			) TEMP");
		sql.append("  		WHERE");
		sql.append("  			TEMP.ID = 1");
		sql.append("  	) TEMP2 ON");
		sql.append("  	TEMP2.S_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_SIGNUP GS ON");
		sql.append("  	GS.IS_DELETED = 'N'");
		sql.append("  	AND GS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");
		
		// 院校ID
		String xxId = (String) searchParams.get("XX_ID");
		if (StringUtils.isNotBlank(xxId)) {
			sql.append(" AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			params.put("xxId", xxId);
		}
		
		if("0".equals(ObjectUtils.toString(searchParams.get("AGE_FLAG")))){  //00后
			sql.append("	AND SUBSTR(GSI.SFZH,7,4) BETWEEN '2000' AND '2009' ");
		}
		
		if("1".equals(ObjectUtils.toString(searchParams.get("AGE_FLAG")))){ //90后
			sql.append("	AND SUBSTR(GSI.SFZH,7,4) BETWEEN '1990' AND '1999' ");
		}
		
		if("2".equals(ObjectUtils.toString(searchParams.get("AGE_FLAG")))){ //80后
			sql.append("	AND SUBSTR(GSI.SFZH,7,4) BETWEEN '1980' AND '1989' ");
		}
		
		if("3".equals(ObjectUtils.toString(searchParams.get("AGE_FLAG")))){ //70后
			sql.append("	AND SUBSTR(GSI.SFZH,7,4) BETWEEN '1970' AND '1979' ");
		}
		
		if("4".equals(ObjectUtils.toString(searchParams.get("AGE_FLAG")))){ //60后
			sql.append("	AND SUBSTR(GSI.SFZH,7,4) BETWEEN '1960' AND '1969' ");
		}
		
		if("5".equals(ObjectUtils.toString(searchParams.get("AGE_FLAG")))){ //60前
			sql.append("	AND SUBSTR(GSI.SFZH,7,4) < '1960' ");
		}
		
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))){
			sql.append("	AND B.GRADE_ID = :GRADE_ID ");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC_ID")))){
			sql.append("	AND GSI.PYCC = :PYCC_ID ");
			params.put("PYCC_ID", ObjectUtils.toString(searchParams.get("PYCC_ID")));
		}
		
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))){
			sql.append("	AND C.SPECIALTY_ID = :SPECIALTY_ID ");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 用户属性统计(性别)
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List userAttributeCount_SEX(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT(1) SEX_COUNT");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN VIEW_STUDENT_INFO B ON");
		sql.append("  	B.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_GRADE GG ON");
		sql.append("  	GG.IS_DELETED = 'N'");
		sql.append("  	AND GG.GRADE_ID = B.GRADE_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY C ON");
		sql.append("  	C.SPECIALTY_ID = GSI.MAJOR");
		sql.append("  LEFT JOIN GJT_SCHOOL_INFO D ON");
		sql.append("  	D.ID = GSI.XX_ID");
		sql.append("  	AND D.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG E ON");
		sql.append("  	E.ID = GSI.XXZX_ID");
		sql.append("  	AND E.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN(");
		sql.append("  		SELECT");
		sql.append("  			STUDENT_ID S_ID,");
		sql.append("  			AUDIT_STATE FLOW_AUDIT_STATE,");
		sql.append("  			AUDIT_OPERATOR_ROLE FLOW_AUDIT_OPERATOR_ROLE");
		sql.append("  		FROM");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					F.STUDENT_ID,");
		sql.append("  					F.AUDIT_STATE,");
		sql.append("  					F.AUDIT_OPERATOR_ROLE,");
		sql.append("  					ROW_NUMBER() OVER(");
		sql.append("  						PARTITION BY F.STUDENT_ID");
		sql.append("  					ORDER BY");
		sql.append("  						F.CREATED_DT DESC,");
		sql.append("  						F.AUDIT_OPERATOR_ROLE DESC");
		sql.append("  					) ID");
		sql.append("  				FROM");
		sql.append("  					GJT_FLOW_RECORD F");
		sql.append("  				WHERE");
		sql.append("  					F.IS_DELETED = 'N'");
		sql.append("  			) TEMP");
		sql.append("  		WHERE");
		sql.append("  			TEMP.ID = 1");
		sql.append("  	) TEMP2 ON");
		sql.append("  	TEMP2.S_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_SIGNUP GS ON");
		sql.append("  	GS.IS_DELETED = 'N'");
		sql.append("  	AND GS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");

		// 院校ID
		String xxId = (String) searchParams.get("XX_ID");
		if (StringUtils.isNotBlank(xxId)) {
			sql.append(" AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			params.put("xxId", xxId);
		}
		
		if("1".equals(searchParams.get("SEX_FLAG"))){ //男
			sql.append("	AND GSI.XBM = :SEX_FLAG ");
			params.put("SEX_FLAG", ObjectUtils.toString(searchParams.get("SEX_FLAG")));
		}
		
		if("2".equals(searchParams.get("SEX_FLAG"))){ //女
			sql.append("	AND GSI.XBM = :SEX_FLAG ");
			params.put("SEX_FLAG", ObjectUtils.toString(searchParams.get("SEX_FLAG")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("GRADE_ID"))){
			sql.append("	AND B.GRADE_ID = :GRADE_ID ");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("PYCC_ID"))){
			sql.append("	AND GSI.PYCC = :PYCC_ID ");
			params.put("PYCC_ID", ObjectUtils.toString(searchParams.get("PYCC_ID")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("SPECIALTY_ID"))){
			sql.append("	AND C.SPECIALTY_ID = :SPECIALTY_ID ");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}
		
		return commonDao.queryForMapListNative(sql.toString(), params);

		
	}
	
	/**
	 * 学历层次统计
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List userPyccCount(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT(1) PYCC_COUNT");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN VIEW_STUDENT_INFO B ON");
		sql.append("  	B.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_GRADE GG ON");
		sql.append("  	GG.IS_DELETED = 'N'");
		sql.append("  	AND GG.GRADE_ID = B.GRADE_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY C ON");
		sql.append("  	C.SPECIALTY_ID = GSI.MAJOR");
		sql.append("  LEFT JOIN GJT_SCHOOL_INFO D ON");
		sql.append("  	D.ID = GSI.XX_ID");
		sql.append("  	AND D.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG E ON");
		sql.append("  	E.ID = GSI.XXZX_ID");
		sql.append("  	AND E.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN(");
		sql.append("  		SELECT");
		sql.append("  			STUDENT_ID S_ID,");
		sql.append("  			AUDIT_STATE FLOW_AUDIT_STATE,");
		sql.append("  			AUDIT_OPERATOR_ROLE FLOW_AUDIT_OPERATOR_ROLE");
		sql.append("  		FROM");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					F.STUDENT_ID,");
		sql.append("  					F.AUDIT_STATE,");
		sql.append("  					F.AUDIT_OPERATOR_ROLE,");
		sql.append("  					ROW_NUMBER() OVER(");
		sql.append("  						PARTITION BY F.STUDENT_ID");
		sql.append("  					ORDER BY");
		sql.append("  						F.CREATED_DT DESC,");
		sql.append("  						F.AUDIT_OPERATOR_ROLE DESC");
		sql.append("  					) ID");
		sql.append("  				FROM");
		sql.append("  					GJT_FLOW_RECORD F");
		sql.append("  				WHERE");
		sql.append("  					F.IS_DELETED = 'N'");
		sql.append("  			) TEMP");
		sql.append("  		WHERE");
		sql.append("  			TEMP.ID = 1");
		sql.append("  	) TEMP2 ON");
		sql.append("  	TEMP2.S_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_SIGNUP GS ON");
		sql.append("  	GS.IS_DELETED = 'N'");
		sql.append("  	AND GS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");

		//企业大学接口调用
		if ("1".equals(ObjectUtils.toString(searchParams.get("TYPE_FLG")))){
			sql.append("  AND GSI.XXZX_ID IN(");
			sql.append("  	SELECT");
			sql.append("  		org.ID");
			sql.append("  	FROM");
			sql.append("  		GJT_ORG org");
			sql.append("  	WHERE");
			//sql.append("  		org.IS_DELETED = 'N' START WITH org.ID =:xxId CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID");

			sql.append("  			org.IS_DELETED = 'N' ");
			if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_CODE"))) {
				sql.append("  START WITH org.CODE = :XXZX_CODE");
				params.put("XXZX_CODE",ObjectUtils.toString(searchParams.get("XXZX_CODE")));
			} else {
				sql.append("  START WITH org.ID = :XXZX_ID");
				params.put("XXZX_ID",ObjectUtils.toString(searchParams.get("XXZX_ID")));
			}
			sql.append("  	CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID ");
			sql.append("  )");
		}else {
			// 院校ID
			String xxId = (String) searchParams.get("XX_ID");
			if (StringUtils.isNotBlank(xxId)) {
				sql.append(" AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
				params.put("xxId", xxId);
			}
		}


		if(EmptyUtils.isNotEmpty(searchParams.get("PYCC_GRADE_ID"))){
			sql.append("	AND B.GRADE_ID = :PYCC_GRADE_ID ");
			params.put("PYCC_GRADE_ID", ObjectUtils.toString(searchParams.get("PYCC_GRADE_ID")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("PYCC_SPECIALTY_ID"))){
			sql.append("	AND GS.SPECIALTY_ID = :PYCC_SPECIALTY_ID ");
			params.put("PYCC_SPECIALTY_ID", ObjectUtils.toString(searchParams.get("PYCC_SPECIALTY_ID")));
		}
		
		if("0".equals(searchParams.get("PYCC_FLAG"))){
			sql.append("	AND GSI.PYCC = :PYCC_FLAG ");
			params.put("PYCC_FLAG", ObjectUtils.toString(searchParams.get("PYCC_FLAG")));
		}
		
		if("2".equals(searchParams.get("PYCC_FLAG"))){
			sql.append("	AND GSI.PYCC = :PYCC_FLAG ");
			params.put("PYCC_FLAG", ObjectUtils.toString(searchParams.get("PYCC_FLAG")));
		}
		
//		if("4".equals(searchParams.get("PYCC_FLAG"))){
//			sql.append("	AND GSI.PYCC = :PYCC_FLAG ");
//			params.put("PYCC_FLAG", ObjectUtils.toString(searchParams.get("PYCC_FLAG")));
//		}
//		
//		if("6".equals(searchParams.get("PYCC_FLAG"))){
//			sql.append("	AND GSI.PYCC = :PYCC_FLAG ");
//			params.put("PYCC_FLAG", ObjectUtils.toString(searchParams.get("PYCC_FLAG")));
//		}
//		
//		if("8".equals(searchParams.get("PYCC_FLAG"))){
//			sql.append("	AND GSI.PYCC = :PYCC_FLAG ");
//			params.put("PYCC_FLAG", ObjectUtils.toString(searchParams.get("PYCC_FLAG")));
//		}
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 查询报读专业
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List specialCount(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	DISTINCT GS.SPECIALTY_ID,");
		sql.append("  	GS.ZYMC");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN GJT_GRADE GG ON");
		sql.append("  	GG.IS_DELETED = 'N'");
		sql.append("  	AND GSI.NJ = GG.GRADE_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY GS ON");
		sql.append("  	GS.IS_DELETED = 'N'");
		sql.append("  	AND GSI.MAJOR = GS.SPECIALTY_ID");
		sql.append("  LEFT JOIN GJT_SIGNUP GSP ON");
		sql.append("  	GSP.IS_DELETED = 'N'");
		sql.append("  	AND GSP.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_STUDY_CENTER GSC ON");
		sql.append("  	GSC.IS_DELETED = 'N'");
		sql.append("  	AND GSC.ID = GSP.XXZX_ID");
		sql.append("  	AND GSC.ID = GSI.XXZX_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");

		// 院校ID
		String xxId = (String) searchParams.get("XX_ID");
		if (StringUtils.isNotBlank(xxId)) {
			sql.append(" AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			params.put("xxId", xxId);
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("SPECIAL_GRADE_ID"))){
			sql.append("	AND GG.GRADE_ID = :SPECIAL_GRADE_ID ");
			params.put("SPECIAL_GRADE_ID", ObjectUtils.toString(searchParams.get("SPECIAL_GRADE_ID")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("SPECIAL_PYCC_ID"))){
			sql.append("	AND GSI.PYCC = :SPECIAL_PYCC_ID ");
			params.put("SPECIAL_PYCC_ID", ObjectUtils.toString(searchParams.get("SPECIAL_PYCC_ID")));
		}
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 统计报读专业人数
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List userCountBySpecial(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT(DISTINCT GSI.STUDENT_ID )");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN GJT_GRADE GG ON");
		sql.append("  	GG.IS_DELETED = 'N'");
		sql.append("  	AND GSI.NJ = GG.GRADE_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY GS ON");
		sql.append("  	GS.IS_DELETED = 'N'");
		sql.append("  	AND GSI.MAJOR = GS.SPECIALTY_ID");
		sql.append("  LEFT JOIN GJT_SIGNUP GSP ON");
		sql.append("  	GSP.IS_DELETED = 'N'");
		sql.append("  	AND GSP.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_STUDY_CENTER GSC ON");
		sql.append("  	GSC.IS_DELETED = 'N'");
		sql.append("  	AND GSC.ID = GSP.XXZX_ID");
		sql.append("  	AND GSC.ID = GSI.XXZX_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");

		// 院校ID
		String xxId = (String) searchParams.get("XX_ID");
		if (StringUtils.isNotBlank(xxId)) {
			sql.append(" AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			params.put("xxId", xxId);
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("SPECIAL_GRADE_ID"))){
			sql.append("	AND GG.GRADE_ID = :SPECIAL_GRADE_ID ");
			params.put("SPECIAL_GRADE_ID", ObjectUtils.toString(searchParams.get("SPECIAL_GRADE_ID")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("SPECIAL_PYCC_ID"))){
			sql.append("	AND GSI.PYCC = :SPECIAL_PYCC_ID ");
			params.put("SPECIAL_PYCC_ID", ObjectUtils.toString(searchParams.get("SPECIAL_PYCC_ID")));
		}
		
		
		if(EmptyUtils.isNotEmpty(searchParams.get("SPECIALTY_ID"))){
			sql.append("	AND GS.SPECIALTY_ID = :SPECIALTY_ID ");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}
		
		return commonDao.queryForMapListNative(sql.toString(), params);

	}
	
	/**
	 * 查询学生人数
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryStudentCount(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT( DISTINCT GSI.STUDENT_ID ) STUDENT_COUNT");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN GJT_GRADE GG ON");
		sql.append("  	GG.IS_DELETED = 'N'");
		sql.append("  	AND GSI.NJ = GG.GRADE_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY GS ON");
		sql.append("  	GS.IS_DELETED = 'N'");
		sql.append("  	AND GSI.MAJOR = GS.SPECIALTY_ID");
		sql.append("  LEFT JOIN GJT_SIGNUP GSP ON");
		sql.append("  	GSP.IS_DELETED = 'N'");
		sql.append("  	AND GSP.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_STUDY_CENTER GSC ON");
		sql.append("  	GSC.IS_DELETED = 'N'");
		sql.append("  	AND GSC.ID = GSP.XXZX_ID");
		sql.append("  	AND GSC.ID = GSI.XXZX_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");

		// 院校ID
		String xxId = (String) searchParams.get("XX_ID");
		if (StringUtils.isNotBlank(xxId)) {
			sql.append(" AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			params.put("xxId", xxId);
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("SPECIAL_GRADE_ID"))){
			sql.append("	AND GG.GRADE_ID = :SPECIAL_GRADE_ID ");
			params.put("SPECIAL_GRADE_ID", ObjectUtils.toString(searchParams.get("SPECIAL_GRADE_ID")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("SPECIAL_PYCC_ID"))){
			sql.append("	AND GSI.PYCC = :SPECIAL_PYCC_ID ");
			params.put("SPECIAL_PYCC_ID", ObjectUtils.toString(searchParams.get("SPECIAL_PYCC_ID")));
		}
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 报读专业统计
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> querySpecialCount(Map searchParams){
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getSpecialSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		return commonDao.queryForStringObjectMapListNative(sql, params);
	}

	/**
	 * 报读专业统计TOP5
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> querySpecialCountTopFive(Map searchParams){
		Map params = null;
		String sql = null;
		Map<String, Object> handlerMap = this.getSpecialSqlHandler(searchParams);
		if (EmptyUtils.isNotEmpty(handlerMap)) {
			params = (Map) handlerMap.get("params");
			sql = ObjectUtils.toString(handlerMap.get("sql"));
		}
		PageRequest pageRequst = new PageRequest(0, 5);
		return commonDao.queryForPageNative(sql, params, pageRequst).getContent();
	}

	private Map<String, Object> getSpecialSqlHandler(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	C.SPECIALTY_BASE_ID,C.SPECIALTY_CODE,C.SPECIALTY_NAME ZYMC,");
		sql.append("  	COUNT(T.STUDENT_ID) ZYMC_COUNT,SUM(COUNT(T.STUDENT_ID)) OVER() STUDENT_COUNT,ROUND( COUNT(T.STUDENT_ID)*100 / SUM(COUNT(T.STUDENT_ID)) OVER(), 2) ZYMC_COUNT_PERCENT");
		sql.append("  FROM GJT_STUDENT_INFO T");
		sql.append("  INNER JOIN GJT_SPECIALTY B ON B.SPECIALTY_ID=T.MAJOR AND B.IS_DELETED='N'");
		sql.append("  INNER JOIN GJT_SPECIALTY_BASE C ON C.SPECIALTY_BASE_ID=B.SPECIALTY_BASE_ID");
		sql.append("  WHERE T.IS_DELETED='N'");
		sql.append("  	AND B.TYPE = '1'");

		//企业大学接口调用
		if ("1".equals(ObjectUtils.toString(searchParams.get("TYPE_FLG")))){
			sql.append("  AND T.XXZX_ID IN(");
			sql.append("  	SELECT");
			sql.append("  		org.ID");
			sql.append("  	FROM");
			sql.append("  		GJT_ORG org");
			sql.append("  	WHERE");
			//sql.append("  		org.IS_DELETED = 'N' START WITH org.ID =:xxId CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID");

			sql.append("  			org.IS_DELETED = 'N' ");
			if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_CODE"))) {
				sql.append("  START WITH org.CODE = :XXZX_CODE");
				params.put("XXZX_CODE",ObjectUtils.toString(searchParams.get("XXZX_CODE")));
			} else {
				sql.append("  START WITH org.ID = :XXZX_ID");
				params.put("XXZX_ID",ObjectUtils.toString(searchParams.get("XXZX_ID")));
			}
			sql.append("  	CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID ");
			sql.append("  )");

		}else {
			// 院校ID
			String xxId = (String) searchParams.get("XX_ID");
			if (StringUtils.isNotBlank(xxId)) {
				sql.append(" AND T.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
				params.put("xxId", xxId);
			}
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("SPECIAL_GRADE_ID"))){
			sql.append("	AND T.NJ = :SPECIAL_GRADE_ID ");
			params.put("SPECIAL_GRADE_ID", ObjectUtils.toString(searchParams.get("SPECIAL_GRADE_ID")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("GRADE_ID"))){
			sql.append("	AND T.NJ = :SPECIAL_GRADE_ID ");
			params.put("SPECIAL_GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("SPECIAL_PYCC_ID"))){
			sql.append("	AND T.PYCC = :SPECIAL_PYCC_ID ");
			params.put("SPECIAL_PYCC_ID", ObjectUtils.toString(searchParams.get("SPECIAL_PYCC_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("PYCC"))){
			sql.append("	AND T.PYCC = :SPECIAL_PYCC_ID ");
			params.put("SPECIAL_PYCC_ID", ObjectUtils.toString(searchParams.get("PYCC")));
		}

		sql.append("  GROUP BY C.SPECIALTY_BASE_ID,C.SPECIALTY_CODE,C.SPECIALTY_NAME");
		sql.append("  ORDER BY COUNT(*) DESC");

		Map<String, Object> handlerMap = new HashMap<String, Object>();
		handlerMap.put("sql", sql.toString());
		handlerMap.put("params", params);
		return handlerMap;
	}
	
	/**
	 * 报读资料统计
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryEnrolmentCount(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT(1) STUDENT_COUNT");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN VIEW_STUDENT_INFO B ON");
		sql.append("  	B.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY C ON");
		sql.append("  	C.SPECIALTY_ID = GSI.MAJOR");
		sql.append("  LEFT JOIN GJT_SCHOOL_INFO D ON");
		sql.append("  	D.ID = GSI.XX_ID");
		sql.append("  	AND D.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG E ON");
		sql.append("  	E.ID = GSI.XXZX_ID");
		sql.append("  	AND E.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN(");
		sql.append("  		SELECT");
		sql.append("  			STUDENT_ID S_ID,");
		sql.append("  			AUDIT_STATE FLOW_AUDIT_STATE,");
		sql.append("  			AUDIT_OPERATOR_ROLE FLOW_AUDIT_OPERATOR_ROLE");
		sql.append("  		FROM");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					F.STUDENT_ID,");
		sql.append("  					F.AUDIT_STATE,");
		sql.append("  					F.AUDIT_OPERATOR_ROLE,");
		sql.append("  					ROW_NUMBER() OVER(");
		sql.append("  						PARTITION BY F.STUDENT_ID");
		sql.append("  					ORDER BY");
		sql.append("  						F.CREATED_DT DESC,");
		sql.append("  						F.AUDIT_OPERATOR_ROLE DESC");
		sql.append("  					) ID");
		sql.append("  				FROM");
		sql.append("  					GJT_FLOW_RECORD F");
		sql.append("  				WHERE");
		sql.append("  					F.IS_DELETED = 'N'");
		sql.append("  			) TEMP");
		sql.append("  		WHERE");
		sql.append("  			TEMP.ID = 1");
		sql.append("  	) TEMP2 ON");
		sql.append("  	TEMP2.S_ID = GSI.STUDENT_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");

        if ("1".equals(ObjectUtils.toString(searchParams.get("TYPE_FLG")))){

            sql.append("  AND GSI.XXZX_ID IN(");
            sql.append("  	SELECT");
            sql.append("  		org.ID");
            sql.append("  	FROM");
            sql.append("  		GJT_ORG org");
            sql.append("  	WHERE");
            //sql.append("  		org.IS_DELETED = 'N' START WITH org.ID =:xxId CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID");

            sql.append("  			org.IS_DELETED = 'N' ");
            if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_CODE"))) {
                sql.append("  START WITH org.CODE = :XXZX_CODE");
                params.put("XXZX_CODE",ObjectUtils.toString(searchParams.get("XXZX_CODE")));
            } else {
                sql.append("  START WITH org.ID = :XXZX_ID");
                params.put("XXZX_ID",ObjectUtils.toString(searchParams.get("XXZX_ID")));
            }
            sql.append("  	CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID ");
            sql.append("  )");

        }else {
            // 院校ID
            String xxId = (String) searchParams.get("XX_ID");
            if (StringUtils.isNotBlank(xxId)) {
                sql.append(" AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
                params.put("xxId", xxId);
            }
        }


		
		if(EmptyUtils.isNotEmpty(searchParams.get("gradeId"))){
			sql.append("	AND B.GRADE_ID = :gradeId ");
			params.put("gradeId", ObjectUtils.toString(searchParams.get("gradeId")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("pycc"))){
			sql.append("	AND GSI.PYCC = :pycc ");
			params.put("pycc", ObjectUtils.toString(searchParams.get("pycc")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("signupSpecialtyId"))){
			sql.append("	AND C.SPECIALTY_ID = :signupSpecialtyId ");
			params.put("signupSpecialtyId", ObjectUtils.toString(searchParams.get("signupSpecialtyId")));
		}
		
		if("4".equals(searchParams.get("AUDIT_STATE"))){ //未审核
			sql.append("  	AND B.AUDIT_STATE <> '1'");
			sql.append("  	AND B.AUDIT_STATE <> '0'");
			sql.append("  	AND TEMP2.FLOW_AUDIT_OPERATOR_ROLE IS NULL");
		}
		
		if("3".equals(searchParams.get("AUDIT_STATE"))){ //待审核
			sql.append("  	AND B.AUDIT_STATE <> '1'");
			sql.append("  	AND B.AUDIT_STATE <> '0'");
			sql.append("  	AND TEMP2.FLOW_AUDIT_OPERATOR_ROLE = 4 ");
		}
		
		if("1".equals(searchParams.get("AUDIT_STATE"))){ //审核通过
			sql.append("  	AND B.AUDIT_STATE = '1' ");
		}
		
		if("2".equals(searchParams.get("AUDIT_STATE"))){ //审核中
			sql.append("  	AND B.AUDIT_STATE <> '1'");
			sql.append("  	AND B.AUDIT_STATE <> '0'");
			sql.append("  	AND TEMP2.FLOW_AUDIT_OPERATOR_ROLE IS NOT NULL ");
			sql.append("  	AND TEMP2.FLOW_AUDIT_OPERATOR_ROLE <> 4 ");
		}
		
		if("0".equals(searchParams.get("AUDIT_STATE"))){ //审核不通过
			sql.append("  	AND B.AUDIT_STATE = '0' ");
		}

		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 报读缴费统计
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryPaymentCount(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT(1) STUDENT_COUNT ");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN GJT_SIGNUP GS ON");
		sql.append("  	GS.IS_DELETED = 'N'");
		sql.append("  	AND GS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");
		
		// 学籍状态
		String xjzt = ObjectUtils.toString(searchParams.get("EQ_xjzt"));
		if (EmptyUtils.isNotEmpty(xjzt)) {
			sql.append(" AND GSI.XJZT=:xjzt");
			params.put("xjzt", xjzt);
		} else {
			sql.append(" AND GSI.XJZT!=:xjzt");
			params.put("xjzt", "5"); // 除去退学
		}
		if ("1".equals(ObjectUtils.toString(searchParams.get("TYPE_FLG")))){

			sql.append("  AND GSI.XXZX_ID IN(");
			sql.append("  	SELECT");
			sql.append("  		org.ID");
			sql.append("  	FROM");
			sql.append("  		GJT_ORG org");
			sql.append("  	WHERE");
			//sql.append("  		org.IS_DELETED = 'N' START WITH org.ID =:xxId CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID");

			sql.append("  			org.IS_DELETED = 'N' ");
			if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_CODE"))) {
				sql.append("  START WITH org.CODE = :XXZX_CODE");
				params.put("XXZX_CODE",ObjectUtils.toString(searchParams.get("XXZX_CODE")));
			} else {
				sql.append("  START WITH org.ID = :XXZX_ID");
				params.put("XXZX_ID",ObjectUtils.toString(searchParams.get("XXZX_ID")));
			}
			sql.append("  	CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID ");
			sql.append("  )");

		}else {
			// 院校ID
			String xxId = (String) searchParams.get("XX_ID");
			if (StringUtils.isNotBlank(xxId)) {
				sql.append(" AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
				params.put("xxId", xxId);
			}
		}


		
		if(EmptyUtils.isNotEmpty(searchParams.get("gradeId"))){
			sql.append("	AND GSI.NJ = :gradeId ");
			params.put("gradeId", ObjectUtils.toString(searchParams.get("gradeId")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("pycc"))){
			sql.append("	AND GSI.PYCC = :pycc ");
			params.put("pycc", ObjectUtils.toString(searchParams.get("pycc")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("signupSpecialtyId"))){
			sql.append("	AND GSI.MAJOR = :signupSpecialtyId ");
			params.put("signupSpecialtyId", ObjectUtils.toString(searchParams.get("signupSpecialtyId")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("CHARGE"))){
            sql.append("	AND GS.CHARGE = :CHARGE");
            params.put("CHARGE", ObjectUtils.toString(searchParams.get("CHARGE")));
        }
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 学习中心统计
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryStudyCenter(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	DISTINCT GSC.ID,");
		sql.append("  	GSC.SC_NAME,");
		sql.append("  	GSC.OFFICE_ADDR,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT( DISTINCT GSI1.STUDENT_ID )");
		sql.append("  		FROM");
		sql.append("  			GJT_STUDENT_INFO GSI1");
		sql.append("  		LEFT JOIN GJT_STUDY_CENTER GSC1 ON");
		sql.append("  			GSC1.IS_DELETED = 'N'");
		sql.append("  			AND GSI1.XXZX_ID = GSC1.ID");
		sql.append("  		WHERE");
		sql.append("  			GSI1.IS_DELETED = 'N'");
		if(EmptyUtils.isNotEmpty(searchParams.get("GSI1_XX_ID"))){
			sql.append("  			AND GSI1.XX_ID = :GSI1_XX_ID ");
			params.put("GSI1_XX_ID", ObjectUtils.toString(searchParams.get("GSI1_XX_ID")));
		}
		sql.append("  			AND GSC1.ID = GSC.ID");
		sql.append("  	) STUDY_COUNT");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN GJT_GRADE GG ON");
		sql.append("  	GG.IS_DELETED = 'N'");
		sql.append("  	AND GSI.NJ = GG.GRADE_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY GS ON");
		sql.append("  	GS.IS_DELETED = 'N'");
		sql.append("  	AND GSI.MAJOR = GS.SPECIALTY_ID");
		sql.append("  LEFT JOIN GJT_SIGNUP GSP ON");
		sql.append("  	GSP.IS_DELETED = 'N'");
		sql.append("  	AND GSP.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_STUDY_CENTER GSC ON");
		sql.append("  	GSC.IS_DELETED = 'N'");
		sql.append("  	AND GSC.ID = GSP.XXZX_ID");
		sql.append("  	AND GSC.ID = GSI.XXZX_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");

		// 院校ID
		String xxId = (String) searchParams.get("XX_ID");
		if (StringUtils.isNotBlank(xxId)) {
			sql.append(" AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			params.put("xxId", xxId);
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("CENTER_GRADE_ID"))){
			sql.append("	AND GG.GRADE_ID = :CENTER_GRADE_ID ");
			params.put("CENTER_GRADE_ID", ObjectUtils.toString(searchParams.get("CENTER_GRADE_ID")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("CENTER_PYCC_ID"))){
			sql.append("	AND GSI.PYCC = :CENTER_PYCC_ID ");
			params.put("CENTER_PYCC_ID", ObjectUtils.toString(searchParams.get("CENTER_PYCC_ID")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("CENTER_SPECIALTY_ID"))){
			sql.append("	AND GS.SPECIALTY_ID = :CENTER_SPECIALTY_ID ");
			params.put("CENTER_SPECIALTY_ID", ObjectUtils.toString(searchParams.get("CENTER_SPECIALTY_ID")));
		}
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 统计学生报读信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List countSignupInfo(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT( 1 ) SIGNUP_COUNT ");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN VIEW_STUDENT_INFO B ON");
		sql.append("  	B.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY C ON");
		sql.append("  	C.SPECIALTY_ID = GSI.MAJOR");
		sql.append("  LEFT JOIN GJT_SCHOOL_INFO D ON");
		sql.append("  	D.ID = GSI.XX_ID");
		sql.append("  	AND D.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_ORG E ON");
		sql.append("  	E.ID = GSI.XXZX_ID");
		sql.append("  	AND E.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN(");
		sql.append("  		SELECT");
		sql.append("  			STUDENT_ID S_ID,");
		sql.append("  			AUDIT_STATE FLOW_AUDIT_STATE,");
		sql.append("  			AUDIT_OPERATOR_ROLE FLOW_AUDIT_OPERATOR_ROLE");
		sql.append("  		FROM");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append("  					F.STUDENT_ID,");
		sql.append("  					F.AUDIT_STATE,");
		sql.append("  					F.AUDIT_OPERATOR_ROLE,");
		sql.append("  					ROW_NUMBER() OVER(");
		sql.append("  						PARTITION BY F.STUDENT_ID");
		sql.append("  					ORDER BY");
		sql.append("  						F.CREATED_DT DESC,");
		sql.append("  						F.AUDIT_OPERATOR_ROLE DESC");
		sql.append("  					) ID");
		sql.append("  				FROM");
		sql.append("  					GJT_FLOW_RECORD F");
		sql.append("  				WHERE");
		sql.append("  					F.IS_DELETED = 'N'");
		sql.append("  			) TEMP");
		sql.append("  		WHERE");
		sql.append("  			TEMP.ID = 1");
		sql.append("  	) TEMP2 ON");
		sql.append("  	TEMP2.S_ID = GSI.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_SIGNUP GS ON");
		sql.append("  	GS.IS_DELETED = 'N'");
		sql.append("  	AND GS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");

		// 学习中心
		String studyId = ObjectUtils.toString(searchParams.get("STUDY_ID"));
		if(EmptyUtils.isNotEmpty(studyId)){
			sql.append("  	AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxzxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			params.put("xxzxId", studyId);
		} else {
			// 院校ID
			String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
			if(EmptyUtils.isNotEmpty(xxId)){
				sql.append("  	AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
				params.put("xxId", xxId);
			}
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XH")))){
			sql.append("	AND GSI.XH = :XH ");
			params.put("XH", ObjectUtils.toString(searchParams.get("XH")).trim());
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))){
			sql.append("	AND GSI.XM LIKE :XM ");
			params.put("XM", "%"+ObjectUtils.toString(searchParams.get("XM")).trim()+"%");
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))){
			sql.append("	AND C.SPECIALTY_ID = :SPECIALTY_ID ");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))){
			sql.append("	AND B.GRADE_ID = :GRADE_ID ");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		if(EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PYCC_ID")))){
			sql.append("	AND GSI.PYCC = :PYCC_ID ");
			params.put("PYCC_ID", ObjectUtils.toString(searchParams.get("PYCC_ID")));
		}
		
		if("4".equals(searchParams.get("AUDIT_STATE"))){ //未审核
			sql.append("  	AND B.AUDIT_STATE <> '1'");
			sql.append("  	AND B.AUDIT_STATE <> '0'");
			sql.append("  	AND TEMP2.FLOW_AUDIT_OPERATOR_ROLE IS NULL");
		}
		
		if("3".equals(searchParams.get("AUDIT_STATE"))){ //待审核
			sql.append("  	AND B.AUDIT_STATE <> '1'");
			sql.append("  	AND B.AUDIT_STATE <> '0'");
			sql.append("  	AND TEMP2.FLOW_AUDIT_OPERATOR_ROLE = 4 ");
		}
		
		if("1".equals(searchParams.get("AUDIT_STATE"))){ //审核通过
			sql.append("  	AND B.AUDIT_STATE = '1' ");
		}
		
		if("2".equals(searchParams.get("AUDIT_STATE"))){ //审核中
			sql.append("  	AND B.AUDIT_STATE <> '1'");
			sql.append("  	AND B.AUDIT_STATE <> '0'");
			sql.append("  	AND TEMP2.FLOW_AUDIT_OPERATOR_ROLE IS NOT NULL ");
			sql.append("  	AND TEMP2.FLOW_AUDIT_OPERATOR_ROLE <> 4 ");
		}
		
		if("0".equals(searchParams.get("AUDIT_STATE"))){ //审核不通过
			sql.append("  	AND B.AUDIT_STATE = '0' ");
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("XJZT_STATE"))){
			sql.append("	AND GSI.XJZT = :XJZT_STATE ");
			params.put("XJZT_STATE", ObjectUtils.toString(searchParams.get("XJZT_STATE")));
		}

		/*
		if("3".equals(searchParams.get("XJZT_STATE"))){ //待注册
			sql.append("	AND GSI.XJZT = :XJZT_STATE ");
			params.put("XJZT_STATE", ObjectUtils.toString(searchParams.get("XJZT_STATE")));
		}
		if("0".equals(searchParams.get("XJZT_STATE"))){ //注册中
			sql.append("	AND GSI.XJZT = :XJZT_STATE ");
			params.put("XJZT_STATE", ObjectUtils.toString(searchParams.get("XJZT_STATE")));
		}
		if("2".equals(searchParams.get("XJZT_STATE"))){ //在籍
			sql.append("	AND GSI.XJZT = :XJZT_STATE ");
			params.put("XJZT_STATE", ObjectUtils.toString(searchParams.get("XJZT_STATE")));
		}
		if("5".equals(searchParams.get("XJZT_STATE"))){ //退学
			sql.append("	AND GSI.XJZT = :XJZT_STATE ");
			params.put("XJZT_STATE", ObjectUtils.toString(searchParams.get("XJZT_STATE")));
		}
		if("4".equals(searchParams.get("XJZT_STATE"))){ //休学
			sql.append("	AND GSI.XJZT = :XJZT_STATE ");
			params.put("XJZT_STATE", ObjectUtils.toString(searchParams.get("XJZT_STATE")));
		}
		if("10".equals(searchParams.get("XJZT_STATE"))){ //转学
			sql.append("	AND GSI.XJZT = :XJZT_STATE ");
			params.put("XJZT_STATE", ObjectUtils.toString(searchParams.get("XJZT_STATE")));
		}
		if("8".equals(searchParams.get("XJZT_STATE"))){ //毕业
			sql.append("	AND GSI.XJZT = :XJZT_STATE ");
			params.put("XJZT_STATE", ObjectUtils.toString(searchParams.get("XJZT_STATE")));
		} */

		if (EmptyUtils.isNotEmpty(searchParams.get("CHARGE"))){
			sql.append("	AND GS.CHARGE = :CHARGE");
			params.put("CHARGE", ObjectUtils.toString(searchParams.get("CHARGE")));
		}

		/*
		if("0".equals(searchParams.get("CHARGE"))){ //未缴费
			sql.append("	AND GS.CHARGE = :CHARGE");
			params.put("CHARGE", ObjectUtils.toString(searchParams.get("CHARGE")));
		}
		if("1".equals(searchParams.get("CHARGE"))){ //已缴费
			sql.append("	AND GS.CHARGE = :CHARGE");
			params.put("CHARGE", ObjectUtils.toString(searchParams.get("CHARGE")));
		}
		if("2".equals(searchParams.get("CHARGE"))){ //已缴费
			sql.append("	AND GS.CHARGE = :CHARGE");
			params.put("CHARGE", ObjectUtils.toString(searchParams.get("CHARGE")));
		}*/

		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 统计前5个学习中心学员人数
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List statisticsByStudyCenter(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	TAB.SC_NAME,");
		sql.append("  	TAB.STUDY_COUNT");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			DISTINCT E.ORG_NAME SC_NAME,");
		sql.append("  			COUNT( GSI.STUDENT_ID ) STUDY_COUNT");
		sql.append("  		FROM");
		sql.append("  			GJT_STUDENT_INFO GSI");
		sql.append("  		LEFT JOIN VIEW_STUDENT_INFO B ON");
		sql.append("  			B.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  		LEFT JOIN GJT_GRADE GG ON");
		sql.append("  			GG.IS_DELETED = 'N'");
		sql.append("  			AND GG.GRADE_ID = B.GRADE_ID");
		sql.append("  		LEFT JOIN GJT_SPECIALTY C ON");
		sql.append("  			C.SPECIALTY_ID = GSI.MAJOR");
		sql.append("  		LEFT JOIN GJT_SCHOOL_INFO D ON");
		sql.append("  			D.ID = GSI.XX_ID");
		sql.append("  			AND D.IS_DELETED = 'N'");
		sql.append("  		LEFT JOIN GJT_ORG E ON");
		sql.append("  			E.ID = GSI.XXZX_ID");
		sql.append("  			AND E.IS_DELETED = 'N'");
		sql.append("  		LEFT JOIN(");
		sql.append("  				SELECT");
		sql.append("  					STUDENT_ID S_ID,");
		sql.append("  					AUDIT_STATE FLOW_AUDIT_STATE,");
		sql.append("  					AUDIT_OPERATOR_ROLE FLOW_AUDIT_OPERATOR_ROLE");
		sql.append("  				FROM");
		sql.append("  					(");
		sql.append("  						SELECT");
		sql.append("  							F.STUDENT_ID,");
		sql.append("  							F.AUDIT_STATE,");
		sql.append("  							F.AUDIT_OPERATOR_ROLE,");
		sql.append("  							ROW_NUMBER() OVER(");
		sql.append("  								PARTITION BY F.STUDENT_ID");
		sql.append("  							ORDER BY");
		sql.append("  								F.CREATED_DT DESC,");
		sql.append("  								F.AUDIT_OPERATOR_ROLE DESC");
		sql.append("  							) ID");
		sql.append("  						FROM");
		sql.append("  							GJT_FLOW_RECORD F");
		sql.append("  						WHERE");
		sql.append("  							F.IS_DELETED = 'N'");
		sql.append("  					) TEMP");
		sql.append("  				WHERE");
		sql.append("  					TEMP.ID = 1");
		sql.append("  			) TEMP2 ON");
		sql.append("  			TEMP2.S_ID = GSI.STUDENT_ID");
		sql.append("  		LEFT JOIN GJT_SIGNUP GS ON");
		sql.append("  			GS.IS_DELETED = 'N'");
		sql.append("  			AND GS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  		WHERE");
		sql.append("  			GSI.IS_DELETED = 'N'");

		if ("1".equals(ObjectUtils.toString(searchParams.get("TYPE_FLG")))){
			sql.append("  AND GSI.XXZX_ID IN(");
			sql.append("  	SELECT");
			sql.append("  		org.ID");
			sql.append("  	FROM");
			sql.append("  		GJT_ORG org");
			sql.append("  	WHERE");
			//sql.append("  		org.IS_DELETED = 'N' START WITH org.ID =:xxId CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID");

			sql.append("  			org.IS_DELETED = 'N' ");
			if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_CODE"))) {
				sql.append("  START WITH org.CODE = :XXZX_CODE");
				params.put("XXZX_CODE",ObjectUtils.toString(searchParams.get("XXZX_CODE")));
			} else {
				sql.append("  START WITH org.ID = :XXZX_ID");
				params.put("XXZX_ID",ObjectUtils.toString(searchParams.get("XXZX_ID")));
			}
			sql.append("  	CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID ");
			sql.append("  )");

		}else {
			// 院校ID
			String xxId = (String) searchParams.get("XX_ID");
			if (StringUtils.isNotBlank(xxId)) {
				sql.append(" AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
				params.put("xxId", xxId);
			}
		}


		
		if(EmptyUtils.isNotEmpty(searchParams.get("CENTER_GRADE_ID"))){
			sql.append("  			AND B.GRADE_ID = :CENTER_GRADE_ID ");
			params.put("CENTER_GRADE_ID", ObjectUtils.toString(searchParams.get("CENTER_GRADE_ID")));
		}
		
		if(EmptyUtils.isNotEmpty(searchParams.get("CENTER_PYCC_ID"))){
			sql.append("	AND GSI.PYCC = :CENTER_PYCC_ID ");
			params.put("CENTER_PYCC_ID", ObjectUtils.toString(searchParams.get("CENTER_PYCC_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("CENTER_SPECIALTY_ID"))){
			sql.append("	AND C.SPECIALTY_ID = :CENTER_SPECIALTY_ID ");
			params.put("CENTER_SPECIALTY_ID", ObjectUtils.toString(searchParams.get("CENTER_SPECIALTY_ID")));
		}
		
		sql.append("  		GROUP BY");
		sql.append("  			E.ORG_NAME");
		sql.append("  	) TAB");
		sql.append("  WHERE");
		sql.append("  	ROWNUM <= 5");
		sql.append("  ORDER BY");
		sql.append("  	STUDY_COUNT DESC");


		return commonDao.queryForMapListNative(sql.toString(), params);
	}


    /**
     * 区域统计
     * @param searchParams
     * @return
     */
	public List queryAreaCount(Map searchParams){
	    Map params = new HashMap();
        StringBuffer sql = new StringBuffer();

        sql.append("  SELECT");
        sql.append("  	TAB.NAME,");
        sql.append("  	COUNT( 1 ) STU_COUNT");
        sql.append("  FROM");
        sql.append("  	(");
        sql.append("  		SELECT");
        sql.append("  			DISTINCT GSI.STUDENT_ID,");
        sql.append("  			GD.NAME,");
        sql.append("  			GSI.XM,");
        sql.append("  			GSI.XH,");
        sql.append("  			GSI.XBM,");
        sql.append("  			GSI.SJH,");
        sql.append("  			GSI.SFZH,");
        sql.append("  			GSI.AVATAR,");
        sql.append("  			(");
        sql.append("  				SELECT");
        sql.append("  					TSD.NAME");
        sql.append("  				FROM");
        sql.append("  					TBL_SYS_DATA TSD");
        sql.append("  				WHERE");
        sql.append("  					TSD.IS_DELETED = 'N'");
        sql.append("  					AND TSD.CODE = GSI.PYCC");
        sql.append("  					AND TSD.TYPE_CODE = 'TrainingLevel'");
        sql.append("  			) PYCC_NAME,");
        sql.append("  			GG.GRADE_NAME,");
        sql.append("  			C.ZYMC,");
        sql.append("  			TO_CHAR( GSI.CREATED_DT, 'yyyy-MM-dd hh24:mi' ) CREATED_DT,");
        sql.append("  			NVL( GS.AUDIT_SOURCE, '--' ) AUDIT_SOURCE,");
        sql.append("  			E.ORG_NAME SC_NAME,");
        sql.append("  			NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GS.AUDIT_STATE AND TSD.TYPE_CODE = 'AUDIT_STATE' ), '--' ) AUDIT_STATE,");
        sql.append("  			(");
        sql.append("  				SELECT");
        sql.append("  					TSD.NAME");
        sql.append("  				FROM");
        sql.append("  					TBL_SYS_DATA TSD");
        sql.append("  				WHERE");
        sql.append("  					TSD.IS_DELETED = 'N'");
        sql.append("  					AND TSD.CODE = GSI.XJZT");
        sql.append("  					AND TSD.TYPE_CODE = 'StudentNumberStatus'");
        sql.append("  			) XJZT,");
        sql.append("  			NVL(( SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GS.CHARGE AND TSD.TYPE_CODE = 'CHARGE' ), '--' ) CHARGE");
        sql.append("  		FROM");
        sql.append("  			GJT_STUDENT_INFO GSI");
        sql.append("  		LEFT JOIN VIEW_STUDENT_INFO B ON");
        sql.append("  			B.STUDENT_ID = GSI.STUDENT_ID");
        sql.append("  		LEFT JOIN GJT_GRADE GG ON");
        sql.append("  			GG.IS_DELETED = 'N'");
        sql.append("  			AND GG.GRADE_ID = B.GRADE_ID");
        sql.append("  		LEFT JOIN GJT_SPECIALTY C ON");
        sql.append("  			C.SPECIALTY_ID = GSI.MAJOR");
        sql.append("  		LEFT JOIN GJT_SCHOOL_INFO D ON");
        sql.append("  			D.ID = GSI.XX_ID");
        sql.append("  			AND D.IS_DELETED = 'N'");
        sql.append("  		LEFT JOIN GJT_ORG E ON");
        sql.append("  			E.ID = GSI.XXZX_ID");
        sql.append("  			AND E.IS_DELETED = 'N'");
        sql.append("  		LEFT JOIN(");
        sql.append("  				SELECT");
        sql.append("  					STUDENT_ID S_ID,");
        sql.append("  					AUDIT_STATE FLOW_AUDIT_STATE,");
        sql.append("  					AUDIT_OPERATOR_ROLE FLOW_AUDIT_OPERATOR_ROLE");
        sql.append("  				FROM");
        sql.append("  					(");
        sql.append("  						SELECT");
        sql.append("  							F.STUDENT_ID,");
        sql.append("  							F.AUDIT_STATE,");
        sql.append("  							F.AUDIT_OPERATOR_ROLE,");
        sql.append("  							ROW_NUMBER() OVER(");
        sql.append("  								PARTITION BY F.STUDENT_ID");
        sql.append("  							ORDER BY");
        sql.append("  								F.CREATED_DT DESC,");
        sql.append("  								F.AUDIT_OPERATOR_ROLE DESC");
        sql.append("  							) ID");
        sql.append("  						FROM");
        sql.append("  							GJT_FLOW_RECORD F");
        sql.append("  						WHERE");
        sql.append("  							F.IS_DELETED = 'N'");
        sql.append("  					) TEMP");
        sql.append("  				WHERE");
        sql.append("  					TEMP.ID = 1");
        sql.append("  			) TEMP2 ON");
        sql.append("  			TEMP2.S_ID = GSI.STUDENT_ID");
        sql.append("  		LEFT JOIN GJT_SIGNUP GS ON");
        sql.append("  			GS.IS_DELETED = 'N'");
        sql.append("  			AND GS.STUDENT_ID = GSI.STUDENT_ID");
        sql.append("  		LEFT JOIN GJT_DISTRICT GD ON");
        sql.append("  			GSI.PROVINCE = GD.ID");
        sql.append("  		WHERE");
        sql.append("  			GSI.IS_DELETED = 'N'");

        // 院校ID
        String xxId = (String) searchParams.get("XX_ID");
        if (StringUtils.isNotBlank(xxId)) {
            sql.append(" AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
            params.put("xxId", xxId);
        }

        if(EmptyUtils.isNotEmpty(searchParams.get("AREA_GRADE_ID"))){
            sql.append("  			AND B.GRADE_ID = :AREA_GRADE_ID ");
            params.put("AREA_GRADE_ID", ObjectUtils.toString(searchParams.get("AREA_GRADE_ID")));
        }

        if(EmptyUtils.isNotEmpty(searchParams.get("AREA_PYCC_ID"))){
            sql.append("	AND GSI.PYCC = :AREA_PYCC_ID ");
            params.put("AREA_PYCC_ID", ObjectUtils.toString(searchParams.get("AREA_PYCC_ID")));
        }
        if(EmptyUtils.isNotEmpty(searchParams.get("AREA_SPECIALTY_ID"))){
            sql.append("	AND C.SPECIALTY_ID = :AREA_SPECIALTY_ID ");
            params.put("AREA_SPECIALTY_ID", ObjectUtils.toString(searchParams.get("AREA_SPECIALTY_ID")));
        }

        sql.append("  	) TAB");
        sql.append("  WHERE");
        sql.append("  	1 = 1");
        sql.append("  	AND TAB.NAME IS NOT NULL");
        sql.append("  GROUP BY");
        sql.append("  	TAB.NAME");

        return commonDao.queryForMapListNative(sql.toString(),params);

    }



	/**
	 * 学籍状态统计
	 * @param searchParams
	 * @return
     */
	public Map countStudentRollSituationBy(Map<String, Object> searchParams) {
		Map params = new HashMap();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT")
				.append(" count(case when b.CHARGE<>'2' then 1 else null end) signupNum,")
				.append(" count(case when t.xjzt='8' then 1 else null end) graduationNum,")
				.append(" count(case when t.xjzt='5' then 1 else null end) leaveSchoolNum,")
				.append(" count(case when t.xjzt='4' then 1 else null end) temporarySchoolNum,")
				.append(" count(case when t.xjzt='2' then 1 else null end) alreadyRegNum,")
				.append(" count(case when b.CHARGE<>'2' and (t.xjzt='0' or t.xjzt='3') then 1 else null end) notRegNum,")
				.append(" count(case when t.perfect_status=1 then 1 else null end) perfectSignupNum,")
				.append(" count(case when t.perfect_status<>1 then 1 else null end) notPerfectSignupNum,")
				.append(" count(DISTINCT t.major) SPECIALTY_COUNT")
				.append(" from gjt_student_info t ")
				.append(" inner join gjt_signup b on t.student_id=b.student_id")
				.append(" where t.is_deleted='N' ");

		// 院校ID
		String xxId = ObjectUtils.toString(searchParams.get("XX_ID"));
		// 学习中心
		String studyId = searchParams.get("EQ_studyId") != null ? ObjectUtils.toString(searchParams.get("EQ_studyId")) : ObjectUtils.toString(searchParams.get("XXZX_ID"));
		String studyCode = ObjectUtils.toString(searchParams.get("XXZX_CODE"));
		// 禁止使用子查询拿到院校ID或者学习中心List，因为会导致整个查询非常慢，致命的，所以要先提前查出来
		String xxIdParam = xxId;
		if (EmptyUtils.isNotEmpty(studyId)) {
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND T.XXZX_ID IN :orgChilds");
			params.put("orgChilds", orgChilds);
		} if (EmptyUtils.isNotEmpty(studyCode)) {
			studyId = gjtOrgDao.queryByCode(studyCode).getId();
			xxIdParam = gjtOrgDao.findSystemAdministrativeOrganizationByXxId(studyId);
			List<String> orgChilds = gjtOrgDao.queryChildsByParentId(studyId);
			sql.append(" AND T.XXZX_ID IN :orgChilds");
			params.put("orgChilds", orgChilds);
		} else {
			sql.append(" AND T.XX_ID=:xxId ");
			params.put("xxId", xxIdParam);
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("gradeId"))){
			sql.append(" and t.nj=:gradeId");
			params.put("gradeId", searchParams.get("gradeId"));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("GRADE_ID"))){
			sql.append(" and t.nj=:gradeId");
			params.put("gradeId", searchParams.get("GRADE_ID"));
		}

		if(EmptyUtils.isNotEmpty(searchParams.get("specialtyId"))){
			sql.append(" and t.major=:specialtyId");
			params.put("specialtyId", searchParams.get("specialtyId"));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("pycc"))){
			sql.append(" and t.pycc=:pycc");
			params.put("pycc", searchParams.get("pycc"));
		}
		List<Map> list = super.findAllByToMap(sql, params, null);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}


	/**
	 * 根据身份证来修改收费状态(0：已全额缴费，1：已部分缴费，2：待缴费，3：已欠费)
	 * @param searchParams
	 * @return
	 */
	public int chargeSignupNew(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE");
		sql.append("  	GJT_SIGNUP GS");
		sql.append("  SET");
		sql.append("  	GS.UPDATED_DT = SYSDATE ");

		if (EmptyUtils.isNotEmpty(searchParams.get("charge"))){
			sql.append("  	,GS.CHARGE = :charge ");
			params.put("charge",ObjectUtils.toString(searchParams.get("charge")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("describ"))){
			sql.append("  	,GS.CHARGE_DESCRIB = :describ ");
			params.put("describ",ObjectUtils.toString(searchParams.get("describ")));
		}
		// 只有待缴费才能修改订单号
		if (EmptyUtils.isNotEmpty(searchParams.get("orderSn"))) {
			sql.append("  	,GS.ORDER_SN = DECODE(GS.CHARGE,'2',:orderSn,GS.ORDER_SN) ");
			params.put("orderSn",ObjectUtils.toString(searchParams.get("orderSn")));
		}

		sql.append("  WHERE");
		sql.append("  	GS.IS_DELETED = 'N'");
		sql.append("  	AND GS.PORTTY = 'Y'");
		sql.append("  	AND GS.STUDENT_ID = :studentId ");
		params.put("studentId",ObjectUtils.toString(searchParams.get("studentId")));

		return commonDao.updateForMapNative(sql.toString(),params);

	}

	/**
	 * 根据学员ID获取报读信息
	 * @param studentId
	 * @return
     */
	public List getSignUpByStudentId(String studentId) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GS.STUDENT_ID");
		sql.append("  FROM");
		sql.append("  	GJT_SIGNUP GS");
		sql.append("  WHERE");
		sql.append("  	GS.IS_DELETED = 'N'");
		sql.append("  	AND GS.PORTTY = 'Y'");
		sql.append("  	AND GS.CHARGE IN(");
		sql.append("  		'0',");
		sql.append("  		'1'");
		sql.append("  	)");
		sql.append("  	AND GS.STUDENT_ID = :studentId ");

		params.put("studentId", ObjectUtils.toString(studentId));

		return commonDao.queryForMapListNative(sql.toString(),params);

	}

}
