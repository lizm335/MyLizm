package com.ouchgzee.study.dao.index;

import com.gzedu.xlims.dao.comm.CommonDao;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class IndexDao {

	@Autowired
	private CommonDao commonDao;
		
	/**
	 * 获得班主任信息
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getHeadTeacher(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GCI.CLASS_ID,");
		sql.append("  GCI.BJMC,");
		sql.append("  '1' BZR_TEACHER_COUNT,");
		sql.append("  GEI.EMPLOYEE_ID,");
		sql.append("  GEI.XM,");
		sql.append("  GEI.ZP,");
		sql.append("  GEI.LXDH,");
		sql.append("  GEI.QQ,");
		sql.append("  GEI.SJH,");
		sql.append("  GEI.DZXX,");
		sql.append("  GEI.EENO,");
		sql.append("  GEI.WORK_TIME,");
		sql.append("  GEI.WORK_ADDR,");
		sql.append("  GEI.ORG_ID,");
		sql.append("  NVL(GSC.SC_NAME, OG.ORG_NAME) SC_NAME,");
		sql.append("  (SELECT GUA.IS_ONLINE");
		sql.append("  FROM GJT_USER_ACCOUNT GUA");
		sql.append("  WHERE GUA.ID = GEI.ACCOUNT_ID) IS_ONLINE");
		sql.append("  FROM GJT_CLASS_INFO GCI, GJT_EMPLOYEE_INFO GEI");
		sql.append("  LEFT JOIN GJT_STUDY_CENTER GSC ON GEI.XXZX_ID = GSC.ID");
		sql.append("  LEFT JOIN GJT_ORG OG ON GEI.XXZX_ID = OG.ID");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GEI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'teach'");
		sql.append("  AND GCI.BZR_ID = GEI.EMPLOYEE_ID");
		sql.append("  AND GCI.CLASS_ID = :CLASS_ID");
		params.put("CLASS_ID", ObjectUtils.toString(searchParams.get("CLASS_ID")));
		return commonDao.queryForMapListNative(sql.toString(), params);
	}
	
	/**
	 * 获取首页统计项个数
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getMsgCount(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT NVL(GSY.ZYMC,c.NAME) ZYMC,");
		sql.append("  GCI.BJMC,");
		sql.append("  GSY.ZYFM,");
		sql.append("  GCI.BZR_ID,");
		
		sql.append("  (SELECT COUNT(GMI.MESSAGE_ID)");
		sql.append("  FROM GJT_MESSAGE_INFO GMI, GJT_MESSAGE_USER GMU");
		sql.append("  WHERE GMI.IS_DELETED = 'N'");
		sql.append("  AND GMU.IS_DELETED = 'N'");
		sql.append("  AND GMU.IS_ENABLED = '0'");
		sql.append("  AND GMU.USER_ID = :USER_ID");
		sql.append("  AND GMI.MESSAGE_ID = GMU.MESSAGE_ID) READ_MESSAGE_COUNT,");

		sql.append("  (SELECT COUNT(GA.ID)");
		sql.append("  FROM GJT_ACTIVITY GA");
		sql.append("  WHERE GA.IS_DELETED = 'N'");
		sql.append("  AND GA.IS_ENABLED = '1'");
		// sql.append("  AND TO_CHAR(GA.BEGIN_TIME, 'yyyy-mm-dd') <=");
		// sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd')");
		sql.append("  AND TO_CHAR(GA.END_TIME, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd')");
		sql.append("  AND GA.RECEIVE_ID = GCI.CLASS_ID) ACTIVITY_COUNT,");

		sql.append("  (SELECT COUNT(LMS.SUBJECT_ID)");
		sql.append("  FROM LCMS_MUTUAL_SUBJECT LMS");
		sql.append("  WHERE LMS.ISDELETED = 'N'");
		sql.append("  AND LMS.SUBJECT_STATUS = 'N'");
		sql.append("  AND LMS.CREATE_ACCOUNT_ID = GSI.ACCOUNT_ID ) FEEDBACK_COUNT,");

		sql.append("  (SELECT COUNT(DISTINCT C.BZR_ID)");
		sql.append("  FROM GJT_CLASS_INFO C");
		sql.append("  INNER JOIN GJT_CLASS_STUDENT S ON S.CLASS_ID = C.CLASS_ID");
		sql.append("  INNER JOIN GJT_EMPLOYEE_INFO GEI ON C.BZR_ID = GEI.EMPLOYEE_ID ");
		sql.append("  WHERE C.CLASS_TYPE = 'course'");
		sql.append("  AND C.IS_DELETED = 'N' AND S.IS_DELETED = 'N' AND GEI.IS_DELETED = 'N' ");
		sql.append("  AND S.STUDENT_ID = GSI.STUDENT_ID) TEACHER_COUNT,");

		sql.append("  (SELECT COUNT(DISTINCT GCS.STUDENT_ID)");
		sql.append("  FROM GJT_CLASS_STUDENT GCS, GJT_STUDENT_INFO GSIO");
		sql.append("  WHERE GCS.IS_DELETED = 'N'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GSIO.STUDENT_ID != GSI.STUDENT_ID");
		sql.append("  AND GCS.STUDENT_ID = GSIO.STUDENT_ID");
		sql.append("  AND GCS.CLASS_ID = GCI.CLASS_ID) STUDENT_COUNT");

		sql.append("  FROM GJT_CLASS_INFO GCI, GJT_CLASS_STUDENT GCS, GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN GJT_SPECIALTY GSY ON GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  LEFT JOIN gjt_specialty_college c ON GSI.MAJOR = c.SPECIALTY_ID");
		sql.append("  WHERE GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GCI.CLASS_TYPE = 'teach'");
		sql.append("  AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GCS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GSI.STUDENT_ID = :STUDENT_ID");

		params.put("USER_ID", ObjectUtils.toString(searchParams.get("USER_ID")));
		params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	/**
	 * 查询班级在线学员
	 * @param param
	 * @return
	 */
	public List<Map<String,String>> getOnlineStudent(Map<String, Object> param) {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT");
		sql.append("  	gsi.STUDENT_ID,gsi.XM,gsi.XH,gsi.AVATAR ZP,gua.EENO,TO_CHAR(gua.CURRENT_LOGIN_TIME,'yyyy-mm-dd hh24:mi:ss') CURRENT_LOGIN_TIME,gua.IS_ONLINE,");
		sql.append("    TO_CHAR(gua.LAST_LOGIN_TIME,'yyyy-mm-dd hh24:mi:ss') LAST_LOGIN_TIME FROM");
		sql.append("  	GJT_STUDENT_INFO gsi,");
		sql.append("  	GJT_CLASS_STUDENT gcs,");
		sql.append("  	gjt_user_account gua");
		sql.append("  WHERE");
		sql.append("  	gsi.IS_DELETED = 'N'");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  	AND gua.IS_DELETED = 'N'");
		sql.append("  	AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  	AND gsi.ACCOUNT_ID = gua.ID");
		sql.append("  	AND gcs.CLASS_ID = :classId AND gua.IS_ONLINE='Y'  ORDER BY gua.CURRENT_LOGIN_TIME DESC");

		List<Map<String,String>> result = commonDao.queryForMapListNative(sql.toString(),param);

		return result;
	}

	/**
	 * 查询查学员的全部选课
	 * @param studentId
	 * @return
	 */
	public List<Map<String,String>> getStudentChoose(String studentId) {

		Map<String,Object> params = new HashMap<String, Object>();

		params.put("STUDENT_ID",studentId);
		StringBuffer sql = new StringBuffer("SELECT grr.REC_ID,grr.COURSE_ID,gg.KCMC,grr.STUDENT_ID FROM GJT_REC_RESULT grr LEFT JOIN GJT_COURSE gg ON grr.COURSE_ID=gg.COURSE_ID AND grr.XX_ID=gg.XX_ID WHERE grr.IS_DELETED='N' AND gg.IS_DELETED='N' AND grr.STUDENT_ID=:STUDENT_ID");

		return commonDao.queryForMapListNative(sql.toString(),params);
	}
}
