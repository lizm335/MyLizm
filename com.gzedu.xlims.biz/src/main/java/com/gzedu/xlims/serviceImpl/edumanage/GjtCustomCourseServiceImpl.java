package com.gzedu.xlims.serviceImpl.edumanage;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.edumanage.GjtCustomCourseDao;
import com.gzedu.xlims.pojo.GjtCustomCourse;
import com.gzedu.xlims.service.edumanage.GjtCustomCourseService;

@Service
public class GjtCustomCourseServiceImpl implements GjtCustomCourseService {

	@Autowired
	private GjtCustomCourseDao gjtCustomCourseDao;
	@Autowired
	private CommonDao commonDao;
	
	@Override
	public void save(GjtCustomCourse customCourse) {
		gjtCustomCourseDao.save(customCourse);
	}
	
	@Override
	public GjtCustomCourse findOne(String id) {
		return gjtCustomCourseDao.findOne(id);
	}

	public Page<Map<String, Object>> queryAll(Map<String, Object> params,PageRequest pageRequst) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ORG.ID ORG_ID,  ORG.CODE,  ORG.ORG_NAME,");
		sql.append(" 		GTP.TEACH_PLAN_ID ,  GG.GRADE_NAME GRADE_NAME,  GG.UPCOURSE_END_DATE, ");
		sql.append("        GC.KCH REPLACE_COURSE_CODE,  GC.KCMC REPLACE_COURSE_NAME, ");
		sql.append("        GC1.KCH COURSE_CODE,  GC1.KCMC COURSE_NAME, ");
		sql.append("        GS.ZYMC SPECIALTY_NAME,  GS.RULE_CODE RULE_CODE, ");
		sql.append("        (SELECT COUNT(1) ");
		sql.append("           FROM GJT_REC_RESULT GRR ");
		sql.append("          WHERE GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID ");
		sql.append("            AND GRR.IS_DELETED = 'N') CHOOSE_COUNT, ");
		sql.append(" 		GCC.CUSTOM_COURSE_ID, ");
		sql.append("        NVL(GCC.PLAN_STATUS,0) PLAN_STATUS , ");
		sql.append("        NVL(GCC.CERTIFICATE_STATUS,0) CERTIFICATE_STATUS, ");
		sql.append("        NVL(GCC.SCORE_STATUS,0) SCORE_STATUS, ");
		sql.append("        (SELECT COUNT(CASE CERTIFICATE_STATUS  WHEN 2 THEN 1 END) || '/' || COUNT(1) UPLOAD_COUNT ");
		sql.append("           FROM GJT_GRANT_COURSE_PLAN ");
		sql.append("          WHERE CUSTOM_COURSE_ID = GCC.CUSTOM_COURSE_ID ");
		sql.append("            AND IS_DELETED = 'N') UPLOAD_COUNT ");
		sql.append("   from (SELECT ORG.ID, ORG.CODE, ORG.ORG_NAME, ");
		sql.append("                 GGS.ID GRADE_SPECIALTY_ID ");
		sql.append("            FROM GJT_ORG             ORG, ");
		sql.append("                 GJT_GS_STUDY_CENTER GGSC, ");
		sql.append("                 GJT_GRADE_SPECIALTY GGS ");
		sql.append("           WHERE ORG.ID = GGSC.STUDY_CENTER_ID ");
		sql.append("        AND GGSC.GRADE_SPECIALTY_ID = GGS.ID ");
		if(params.get("orgIds") != null) {
			sql.append("        AND ORG.ID IN (:orgIds) ");
		}
		sql.append("        AND ORG.IS_DELETED = 'N' AND GGS.IS_DELETED = 'N') ORG ");
		sql.append("   LEFT JOIN GJT_TEACH_PLAN GTP "); // 教学计划
		sql.append("     ON ORG.GRADE_SPECIALTY_ID = GTP.GRADE_SPECIALTY_ID AND GTP.REPLACECOURSE IS NOT NULL AND GTP.IS_DELETED = 'N'");
		sql.append("   LEFT JOIN GJT_CUSTOM_COURSE GCC ");// 定制课制
		sql.append("     ON GTP.TEACH_PLAN_ID = GCC.TEACH_PLAN_ID AND ORG.ID=GCC.ORG_ID AND GCC.IS_DELETED = 'N' ");
		sql.append("   LEFT JOIN GJT_GRADE GG ");// 年级表
		sql.append("     ON GTP.ACTUAL_GRADE_ID = GG.GRADE_ID AND GG.IS_DELETED = 'N' ");
		sql.append("   LEFT JOIN GJT_COURSE GC ");// 替换课程
		sql.append("     ON GTP.REPLACECOURSE = GC.COURSE_ID AND GC.IS_DELETED = 'N' ");
		sql.append("   LEFT JOIN GJT_COURSE GC1 ");// 学院课程
		sql.append("     ON GTP.COURSE_ID = GC1.COURSE_ID AND GC1.IS_DELETED = 'N' ");
		sql.append("   LEFT JOIN GJT_SPECIALTY GS ");// 专业
		sql.append("     ON GTP.KKZY=GS.SPECIALTY_ID AND GS.IS_DELETED='N'");
		sql.append("  WHERE 1=1 ");
		sql.append("		AND GC.WSJXZK = '0' "); // 非网上教学
		if(params.get("replaceCourseName") != null ) {
			params.put("replaceCourseCode", params.get("replaceCourseName"));
			params.put("replaceCourseName", "%"+params.get("replaceCourseName")+"%");
			sql.append("     AND (GC.KCMC LIKE :replaceCourseName OR GC.KCH=:replaceCourseCode)");
		}
		if(params.get("courseName") != null ) {
			params.put("courseCode", params.get("courseName"));
			params.put("courseName", "%"+params.get("courseName")+"%");
			sql.append("     AND (GC1.KCMC LIKE :courseName OR GC1.KCH=:courseCode)");
		}
		if(params.get("gradeId") != null ) {
			sql.append("     AND GTP.ACTUAL_GRADE_ID=:gradeId "); // 教学计划学期
		}
		if(params.get("planStatus") != null ) {
			sql.append("     AND NVL(GCC.PLAN_STATUS,0) = :planStatus "); // 授课计划状态
		}
		if(params.get("scoreStatus") != null ) {
			sql.append("     AND NVL(GCC.SCORE_STATUS,0) = :scoreStatus ");// 授课成绩状态
		}
		if(params.get("certificateStatus") != null ) {
			sql.append("     AND NVL(GCC.CERTIFICATE_STATUS,0) = :certificateStatus ");// 授课凭证状态
		}
		/*1.审核不通过;2.待审核/上传中;3.已上传/未上传;4.审核通过*/
		sql.append("  ORDER BY CASE ");
		sql.append("             WHEN GCC.PLAN_STATUS = 3 THEN 1 ");
		sql.append("             WHEN GCC.SCORE_STATUS = 3 THEN 2 ");
		sql.append("             WHEN GCC.PLAN_STATUS = 1 THEN 3 ");
		sql.append("             WHEN GCC.SCORE_STATUS = 1 THEN 4 ");
		sql.append("             WHEN GCC.CERTIFICATE_STATUS = 1 THEN 5 ");
		sql.append("             WHEN NVL(GCC.CERTIFICATE_STATUS,0) = 0 AND GCC.PLAN_STATUS = 2 THEN 6 ");
		sql.append("             WHEN NVL(GCC.SCORE_STATUS,0) = 0 AND GCC.CERTIFICATE_STATUS = 2 THEN 7 ");
		sql.append("             WHEN NVL(GCC.SCORE_STATUS,0) = 0 THEN 8 ");
		sql.append("             WHEN GCC.SCORE_STATUS = 2 THEN 99 ");
		sql.append("           ELSE 100 END ");
		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}
	
	public Page<Map<String, Object>> queryAuditAll(Map<String, Object> params,PageRequest pageRequst) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ORG.ID ORG_ID,  ORG.CODE,  ORG.ORG_NAME,");
		sql.append(" 		GTP.TEACH_PLAN_ID ,  GG.GRADE_NAME GRADE_NAME,  GG.UPCOURSE_END_DATE, ");
		sql.append("        GC.KCH REPLACE_COURSE_CODE,  GC.KCMC REPLACE_COURSE_NAME, ");
		sql.append("        GC1.KCH COURSE_CODE,  GC1.KCMC COURSE_NAME, ");
		sql.append("        GS.ZYMC SPECIALTY_NAME,  GS.RULE_CODE RULE_CODE, ");
		sql.append("        (CASE GC.WSJXZK WHEN '1' THEN ( ");
		sql.append("        	SELECT COUNT(1) ");
		sql.append("            FROM GJT_REC_RESULT GRR ");
		sql.append("            WHERE GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID ");
		sql.append("            AND GRR.IS_DELETED = 'N') ");
		sql.append("        ELSE ( ");
		sql.append("            SELECT COUNT(DISTINCT GSI.STUDENT_ID) CHOOSE_COUNT ");
		sql.append("            FROM GJT_STUDENT_INFO GSI, GJT_GRADE_SPECIALTY GGS, VIEW_TEACH_PLAN VTP ");
		sql.append("            WHERE GSI.IS_DELETED = 'N' ");
		sql.append("            AND GGS.IS_DELETED = 'N' ");
		sql.append("            AND VTP.IS_DELETED = 'N' ");
		sql.append("            AND GSI.GRADE_SPECIALTY_ID = GGS.ID ");
		sql.append("            AND GGS.ID = VTP.GRADE_SPECIALTY_ID ");
		sql.append("            AND VTP.TEACH_PLAN_ID=GTP.TEACH_PLAN_ID ");
		sql.append("		) END) CHOOSE_COUNT, ");
		sql.append(" 		GCC.CUSTOM_COURSE_ID, ");
		sql.append("        NVL(GCC.PLAN_STATUS,0) PLAN_STATUS , ");
		sql.append("        NVL(GCC.CERTIFICATE_STATUS,0) CERTIFICATE_STATUS, ");
		sql.append("        NVL(GCC.SCORE_STATUS,0) SCORE_STATUS, ");
		sql.append("        (SELECT COUNT(CASE CERTIFICATE_STATUS  WHEN 2 THEN 1 END) || '/' || COUNT(1) UPLOAD_COUNT ");
		sql.append("           FROM GJT_GRANT_COURSE_PLAN ");
		sql.append("          WHERE CUSTOM_COURSE_ID = GCC.CUSTOM_COURSE_ID ");
		sql.append("            AND IS_DELETED = 'N') UPLOAD_COUNT ");
		sql.append("   from (SELECT ORG.ID, ORG.CODE, ORG.ORG_NAME, ");
		sql.append("                 GGS.ID GRADE_SPECIALTY_ID ");
		sql.append("            FROM GJT_ORG             ORG, ");
		sql.append("                 GJT_GS_STUDY_CENTER GGSC, ");
		sql.append("                 GJT_GRADE_SPECIALTY GGS ");
		sql.append("           WHERE ORG.ID = GGSC.STUDY_CENTER_ID ");
		sql.append("        AND GGSC.GRADE_SPECIALTY_ID = GGS.ID ");
		if(params.get("orgIds") != null) {
			sql.append("        AND ORG.ID IN (:orgIds) ");
		}
		sql.append("        AND ORG.IS_DELETED = 'N' AND GGS.IS_DELETED = 'N') ORG ");
		sql.append("   LEFT JOIN GJT_TEACH_PLAN GTP "); // 教学计划
		sql.append("     ON ORG.GRADE_SPECIALTY_ID = GTP.GRADE_SPECIALTY_ID AND GTP.REPLACECOURSE IS NOT NULL AND GTP.IS_DELETED = 'N'");
		sql.append("   LEFT JOIN GJT_CUSTOM_COURSE GCC ");// 定制课制
		sql.append("     ON GTP.TEACH_PLAN_ID = GCC.TEACH_PLAN_ID AND ORG.ID=GCC.ORG_ID AND GCC.IS_DELETED = 'N' ");
		sql.append("   LEFT JOIN GJT_GRADE GG ");// 年级表
		sql.append("     ON GTP.ACTUAL_GRADE_ID = GG.GRADE_ID AND GG.IS_DELETED = 'N' ");
		sql.append("   LEFT JOIN GJT_COURSE GC ");// 替换课程
		sql.append("     ON GTP.REPLACECOURSE = GC.COURSE_ID AND GC.IS_DELETED = 'N' ");
		sql.append("   LEFT JOIN GJT_COURSE GC1 ");// 学院课程
		sql.append("     ON GTP.COURSE_ID = GC1.COURSE_ID AND GC1.IS_DELETED = 'N' ");
		sql.append("   LEFT JOIN GJT_SPECIALTY GS ");// 专业
		sql.append("     ON GTP.KKZY=GS.SPECIALTY_ID AND GS.IS_DELETED='N'");
		sql.append("  WHERE 1=1 ");
		sql.append("		AND GC.WSJXZK = '0' "); // 非网上教学

		if(params.get("replaceCourseName") != null ) {
			params.put("replaceCourseCode", params.get("replaceCourseName"));
			params.put("replaceCourseName", "%"+params.get("replaceCourseName")+"%");
			sql.append("     AND (GC.KCMC LIKE :replaceCourseName OR GC.KCH=:replaceCourseCode)");
		}
		if(params.get("courseName") != null ) {
			params.put("courseCode", params.get("courseName"));
			params.put("courseName", "%"+params.get("courseName")+"%");
			sql.append("     AND (GC1.KCMC LIKE :courseName OR GC1.KCH=:courseCode)");
		}
		if(params.get("gradeId") != null ) {
			sql.append("     AND GTP.ACTUAL_GRADE_ID=:gradeId "); // 教学计划学期
		}
		if(params.get("planStatus") != null ) {
			sql.append("     AND NVL(GCC.PLAN_STATUS,0) = :planStatus "); // 授课计划状态
		}
		if(params.get("scoreStatus") != null ) {
			sql.append("     AND NVL(GCC.SCORE_STATUS,0) = :scoreStatus ");// 授课成绩状态
		}
		if(params.get("certificateStatus") != null ) {
			sql.append("     AND NVL(GCC.CERTIFICATE_STATUS,0) = :certificateStatus ");// 授课凭证状态
		}
		/*1.待审核/上传中;2.审核不通过;3.审核通过;4.未上传*/
		sql.append("  ORDER BY CASE ");
		sql.append("             WHEN GCC.PLAN_STATUS = 1 THEN 1 ");
		sql.append("             WHEN GCC.SCORE_STATUS = 1 THEN 2 ");
		sql.append("             WHEN GCC.CERTIFICATE_STATUS = 1 THEN 3 ");
		sql.append("             WHEN GCC.PLAN_STATUS = 3 THEN 4 ");
		sql.append("             WHEN GCC.SCORE_STATUS = 3 THEN 5 ");
		sql.append("             WHEN GCC.PLAN_STATUS = 2 THEN 6 ");
		sql.append("             WHEN GCC.CERTIFICATE_STATUS = 2 THEN 7 ");
		sql.append("             WHEN GCC.SCORE_STATUS = 2 THEN 8 ");
		sql.append("             WHEN NVL(GCC.SCORE_STATUS,0) = 0 OR NVL(GCC.PLAN_STATUS,0) = 0 THEN 99 ");
		sql.append("             ELSE 100 ");
		sql.append("           END ");
		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	@Override
	public GjtCustomCourse findByTeachPlanIdAndOrgId(String teachPlanId, String orgId) {
		return gjtCustomCourseDao.findByTeachPlanIdAndOrgId(teachPlanId, orgId);
	}
	
	
}
