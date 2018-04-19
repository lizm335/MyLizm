package com.gzedu.xlims.dao.signup;

import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：
 * @author 卢林林
 * @Date 2016年11月29日
 * @version 1.0
 *
 */
@Repository
public class GjtSignupNativeDao extends BaseDaoImpl{
	
	 private static Logger log = LoggerFactory.getLogger(GjtSignupNativeDao.class);
	@Autowired
	private CommonDao commonDao;
	
	public List<Map<String, String>> queryGjtSignupList(String xxId,Map<String, Object> searchParams){
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT ");
		sb.append("GSI.XM,GSI.XBM,GSI.XH,GSI.SFZH,GG.GRADE_NAME,(SELECT NAME FROM  TBL_SYS_DATA WHERE CODE=GSI.PYCC AND TYPE_CODE = 'TrainingLevel')PYCC, ");
		sb.append("GSP.ZYMC,GS.CREATED_DT,GS.AUDIT_SOURCE, CAST(GS.AUDIT_STATE AS varchar2(1))AS AUDIT_STATE,GS.CHARGE,GS.XXZX_ID ");
		sb.append("FROM GJT_SIGNUP GS INNER JOIN GJT_STUDENT_INFO GSI ON GS.STUDENT_ID=GSI.STUDENT_ID ");
		sb.append("INNER JOIN GJT_SPECIALTY GSP ON GS.SIGNUP_SPECIALTY_ID=GSP.SPECIALTY_ID ");
		sb.append("INNER JOIN GJT_GRADE GG ON GSI.NJ=GG.GRADE_ID ");
		sb.append("WHERE GS.IS_DELETED='N' AND GSI.IS_DELETED='N' ");
		sb.append("AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
		map.put("xxId", xxId);

		Object auditState = searchParams.get("EQ_auditState");
		if (auditState != null && StringUtils.isNotBlank((String)auditState)) {
			sb.append(" AND GS.AUDIT_STATE=:auditState");//:batchId
			map.put("auditState", auditState);
		}		
		return commonDao.queryForMapListNative(sb.toString(), map);
	}
	public List<Map> querySignupNums(Map<String, Object> searchParams, Sort sort) {
		Map<String, Object> parameters = new HashMap();
		 StringBuilder querySql = new StringBuilder();
		 querySql.append("SELECT ");
		 querySql.append("GSP.AUDIT_STATE CODE,COUNT(*) VALUE ");
		 querySql.append("FROM ");
		 querySql.append("GJT_ENROLL_BATCH GEB,GJT_SIGNUP GSP,GJT_STUDENT_INFO GSI,GJT_GRADE GG,GJT_SPECIALTY GS ");
		 querySql.append("WHERE ");
		 querySql.append("GSP.STUDENT_ID = GSI.STUDENT_ID ");		 
		 querySql.append("AND GEB.ENROLL_BATCH_ID = GSP.ENROLL_BATCH_ID ");
		 querySql.append("AND GSI.MAJOR = GS.SPECIALTY_ID ");
		 querySql.append("AND GEB.GRADE_ID = GG.GRADE_ID ");
		 querySql.append("AND GSI.IS_DELETED = 'N' AND GG.IS_DELETED = 'N' ");
		// 查询条件拼接
		String xxId = (String) searchParams.get("xxId");
        if (StringUtils.isNotBlank(xxId)) {
            querySql.append(" AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
            parameters.put("xxId", xxId);
        }
        if (StringUtils.isNotBlank(searchParams.get("gradeId").toString())) {
            querySql.append(" AND GG.GRADE_ID =:gradeId ");
            parameters.put("gradeId", searchParams.get("gradeId"));
        }
        if (StringUtils.isNotBlank(searchParams.get("pycc").toString())) {
            querySql.append(" AND GSI.PYCC =:pycc ");
            parameters.put("pycc", searchParams.get("pycc"));
        }
        if (StringUtils.isNotBlank(searchParams.get("signupSpecialtyId").toString())) {
            querySql.append(" AND GS.SPECIALTY_ID =:signupSpecialtyId ");
            parameters.put("signupSpecialtyId", searchParams.get("signupSpecialtyId"));
        }
        querySql.append("GROUP BY GSP.AUDIT_STATE ");
        return super.findAllByToMap(querySql, parameters, sort);
	}
	
	public List<Map> querySignupPayCostNum(Map<String, Object> searchParams, Sort sort) {
		Map<String, Object> parameters = new HashMap();
		 StringBuilder querySql = new StringBuilder();
		 querySql.append("SELECT ");
		 querySql.append("GSP.CHARGE CODE,COUNT(*) VALUE ");
		 querySql.append("FROM ");
		 querySql.append("GJT_ENROLL_BATCH GEB,GJT_SIGNUP GSP,GJT_STUDENT_INFO GSI,GJT_GRADE GG,GJT_SPECIALTY GS ");
		 querySql.append("WHERE ");
		 querySql.append("GSP.STUDENT_ID = GSI.STUDENT_ID ");		 
		 querySql.append("AND GEB.ENROLL_BATCH_ID = GSP.ENROLL_BATCH_ID ");
		 querySql.append("AND GSI.MAJOR = GS.SPECIALTY_ID ");
		 querySql.append("AND GEB.GRADE_ID = GG.GRADE_ID ");
		 querySql.append("AND GSI.IS_DELETED = 'N' AND GG.IS_DELETED = 'N' ");
		// 查询条件拼接
		String xxId = (String) searchParams.get("xxId");
		if (StringUtils.isNotBlank(xxId)) {
			querySql.append(" AND GSI.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
			parameters.put("xxId", xxId);
		}
        if (StringUtils.isNotBlank(searchParams.get("gradeId").toString())) {
            querySql.append(" AND GG.GRADE_ID =:gradeId ");
            parameters.put("gradeId", searchParams.get("gradeId"));
        }
        if (StringUtils.isNotBlank(searchParams.get("pycc").toString())) {
            querySql.append(" AND GSI.PYCC =:pycc ");
            parameters.put("pycc", searchParams.get("pycc"));
        }
        if (StringUtils.isNotBlank(searchParams.get("signupSpecialtyId").toString())) {
            querySql.append(" AND GS.SPECIALTY_ID =:signupSpecialtyId ");
            parameters.put("signupSpecialtyId", searchParams.get("signupSpecialtyId"));
        }
       querySql.append("GROUP BY GSP.CHARGE ");
       return super.findAllByToMap(querySql, parameters, sort);
	}	
}
