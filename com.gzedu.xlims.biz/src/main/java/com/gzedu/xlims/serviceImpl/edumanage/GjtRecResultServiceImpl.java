/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.edumanage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.edumanage.GjtRecResultDao;
import com.gzedu.xlims.pojo.GjtRecResult;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月16日
 * @version 2.5
 *
 */
@Service
public class GjtRecResultServiceImpl extends BaseServiceImpl<GjtRecResult> implements GjtRecResultService {

	@Autowired
	GjtRecResultDao gjtRecResultDao;

	@Autowired
	private CommonDao commonDao;

	@Override
	protected BaseDao<GjtRecResult, String> getBaseDao() {
		return this.gjtRecResultDao;
	}

	@Override
	public Page<GjtRecResult> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("gjtOrg.id", new SearchFilter("gjtOrg.id", Operator.EQ, orgId));
		Specification<GjtRecResult> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtRecResult.class);
		return gjtRecResultDao.findAll(spec, pageRequst);
	}

	@Override
	public GjtRecResult queryBy(String id) {
		return gjtRecResultDao.findOne(id);
	}

	@Override
	public List<GjtRecResult> queryAll(Iterable<String> ids) {
		return (List<GjtRecResult>) gjtRecResultDao.findAll(ids);
	}

	@Override
	public GjtRecResult queryByStudentIdAndCourseName(String studentId, String courseName) {
		return gjtRecResultDao.queryByStudentIdAndCourseName(studentId, courseName);
	}

	@Override
	public GjtRecResult queryByStudentId(String studentId, String teachPlanId) {
		return gjtRecResultDao.findByStudentIdAndTeachPlanIdAndIsDeleted(studentId, teachPlanId, "N");
	}

	// 成绩管理-考试成绩 不能在serviceImp写SQL语句，以后在优化吧
	@Override
	public Page<Map<String, Object>> queryAllBySql(String orgId, Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		// sb.append(
		// " select * from (select gsi.xh, gsi.student_id,gg.grade_name, gsi.xm,
		// gsi.sjh, grr.rec_id, gsi.pycc, gc.kcmc,gti.term_name,
		// grr.score_state, gs.zymc, NVL(gtp.kcxxbz, 70) kcxxbz, NVL(gtp.kcksbz,
		// 30) kcksbz,"
		// + " NVL(grr.exam_score1, gsss.exam_score) AS exam_score, GRR.IS_OVER,
		// GRR.IS_RESERVE, NVL(grr.exam_score, GSSS.SCORE) COURSE_SCHEDULE,"
		// + " NVL(grr.exam_score2, ROUND(NVL(GSSS.SCORE * NVL(GTP.KCXXBZ, 70) /
		// 100, 0) + NVL(GSSS.EXAM_SCORE * NVL(GTP.KCKSBZ, 30) / 100, 0),2)) AS
		// TOTAL_POINTS,"
		// + " GRR.IS_RESERVE_BOOK, gtp.ksfs FROM GJT_REC_RESULT GRR inner JOIN
		// GJT_STUDENT_INFO GSI ON GRR.STUDENT_ID = GSI.STUDENT_ID"
		// + " AND GSI.IS_DELETED = 'N' inner JOIN VIEW_TEACH_PLAN gtp ON
		// GTP.TEACH_PLAN_ID = GRR.TEACH_PLAN_ID "
		// + " and gtp.is_deleted = 'N' inner JOIN GJT_TERM_INFO GTI ON
		// GTI.TERM_ID = gtp.TERM_ID AND GTP.IS_DELETED = 'N'"
		// + " inner JOIN GJT_COURSE GC ON GC.COURSE_ID = Gtp.COURSE_ID and
		// gc.is_deleted = 'N' inner JOIN GJT_SPECIALTY GS "
		// + " ON GSI.MAJOR = GS.SPECIALTY_ID AND GS.IS_DELETED = 'N' inner join
		// gjt_grade gg on gg.grade_id = gtp.grade_id "
		// + " and gg.is_deleted = 'N' LEFT JOIN VIEW_STUDENT_STUDY_SITUATION
		// GSSS ON GSSS.CHOOSE_ID = grr.REC_ID WHERE GRR.IS_DELETED = 'N' and
		// grr.xx_id='" + orgId
		// + "' ) a where 1=1");

		sb.append(
				" select *   from (select gsi.xh,  gsi.student_id,  gg.grade_name,   gsi.xm,   gsi.sjh,gsi.sfzh,gsi.nj,  grr.rec_id,"
						+ "  gsi.pycc,  gc.kcmc,  GTP.TERM_TYPE_CODE,  grr.score_state,  gs.zymc,gs.specialty_id,NVL(GTP.STUDY_RATIO, 70) STUDY_RATIO,"
						+ " NVL(GTP.EXAM_RATIO, 30) EXAM_RATIO,NVL(grr.exam_score1, gsss.exam_score) AS exam_score,  NVL(grr.exam_score, GSSS.SCORE) COURSE_SCHEDULE, NVL(grr.exam_score2,"
						+ " ROUND(NVL(GSSS.SCORE * NVL(GTP.STUDY_RATIO, 70) / 100, 0)+NVL(GSSS.EXAM_SCORE * NVL(GTP.EXAM_RATIO, 30) / 100, 0),2)) AS TOTAL_POINTS,"
						+ " gtp.EXAM_TYPE   FROM GJT_REC_RESULT GRR  inner JOIN GJT_STUDENT_INFO GSI    ON GRR.STUDENT_ID = GSI.STUDENT_ID "
						+ " inner JOIN gjt_grade_specialty_plan gtp ON GTP.Id = GRR.TEACH_PLAN_ID  inner JOIN GJT_COURSE GC "
						+ " ON GC.COURSE_ID = Gtp.COURSE_ID  inner JOIN GJT_SPECIALTY GS   ON GSI.MAJOR = GS.SPECIALTY_ID"
						+ " AND GS.IS_DELETED = 'N'  inner join gjt_grade gg   on gg.grade_id = gtp.grade_id   LEFT JOIN VIEW_STUDENT_STUDY_SITUATION GSSS"
						+ " ON GSSS.CHOOSE_ID = grr.REC_ID   WHERE GRR.IS_DELETED = 'N'  AND GSI.IS_DELETED = 'N'  and gg.is_deleted = 'N'  and gc.is_deleted = 'N'   ) a where 1 = 1");

		String name = (String) searchParams.get("LIKE_xm");
		if (StringUtils.isNotEmpty(name)) {
			sb.append(" and a.xm like :xm ");
			map.put("xm", "%" + name + "%");
		}

		String xh = (String) searchParams.get("EQ_xh");
		if (StringUtils.isNotEmpty(xh)) {
			sb.append(" and a.xh = :xh ");
			map.put("xh", xh);
		}

		String score_state = (String) searchParams.get("EQ_scoreState");
		if (StringUtils.isNotEmpty(score_state)) {
			if (score_state.equals("0")) {
				sb.append(" and a.score_state = 0 or  a.TOTAL_POINTS>=60"); // 不知道为什么这么写，可能那个状态不是实时更新的
			} else if (score_state.equals("1")) {
				sb.append(" and a.TOTAL_POINTS<60 or (a.score_state!=0 and a.score_state is not null)");
			}
		}

		String specialtyId = (String) searchParams.get("EQ_specialtyId");
		if (StringUtils.isNotEmpty(specialtyId)) {
			sb.append(" and a.specialty_id = :specialtyId ");
			map.put("specialtyId", specialtyId);
		}

		String courseName = (String) searchParams.get("LIKE_courseName");
		if (StringUtils.isNotEmpty(courseName)) {
			sb.append(" and a.kcmc like  :courseName ");
			map.put("courseName", "%" + courseName + "%");
		}

		String examSate = (String) searchParams.get("EQ_examSate");
		if (StringUtils.isNotEmpty(examSate)) {
			// sb.append(" and a.ksfs = :examSate ");
			sb.append(" and a.EXAM_TYPE =  :examSate ");
			map.put("examSate", examSate);
		}

		String identityCard = (String) searchParams.get("LIKE_identityCard");
		if (StringUtils.isNotEmpty(identityCard)) {
			sb.append(" and a.sfzh like :identityCard ");
			map.put("identityCard", "%" + identityCard + "%");
		}

		String grade = (String) searchParams.get("EQ_examGrade");
		if (StringUtils.isNotEmpty(grade)) {
			sb.append(" and a.nj = :grade ");
			map.put("grade", grade);
		}

		Page<Map<String, Object>> page = commonDao.queryForPageNative(sb.toString(), map, pageRequst);
		return page;
	}

	// 查询学员信息 不能在serviceImp写SQL语句，以后在优化吧
	@Override
	public Map<String, Object> queryStudent(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(
				"SELECT  GSI.AVATAR,GSI.XM, GSI.XH,GSI.SFZH CARDNO,GSI.SJH MOBILEPHONE,GSI.DZXX EMAIL,GSI.SC_CO SCCO,GSI.TXDZ ADDRESS, ");
		sb.append(" GSS.XXMC,GRADE_NAME GRADENAME,GSI.PYCC, ZYMC,GOO.ORG_NAME ORGNAME, GS.SPECIALTY_ID,"
				+ " gsi.rxny, gsi.xbm,gsi.STUDENT_ID   FROM   GJT_ENROLL_BATCH geb,GJT_SIGNUP gsp,gjt_student_info gsi,GJT_GRADE GG,GJT_SPECIALTY GS,"
				+ "  gjt_school_info gss,gjt_org goo  WHERE  gsp.student_id = gsi.student_id  AND geb.ENROLL_BATCH_ID = gsp.ENROLL_BATCH_ID "
				+ "   AND GSI.MAJOR = GS.SPECIALTY_ID   AND GEB.GRADE_ID = GG.GRADE_ID    and gsi.xx_id=gss.id and goo.id=gsi.xxzx_id "
				+ "  AND gsi.IS_DELETED = 'N' AND gg.IS_DELETED = 'N' and gss.is_deleted='N' and goo.is_deleted='N' and gs.is_deleted='N' and gsi.student_id=:studentId ");
		map.put("studentId", id);
		Map<String, Object> mapResult = commonDao.queryObjectToMapNative(sb.toString(), map);
		return mapResult;
	}

	// 获取个人成绩明细
	@Override
	public List<Map<String, String>> queryStudentSourceDetail(String id, String term) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(
				"select  gtp.score,GTP.TERM_TYPE_CODE, gc.kcmc,grr.score_state, gtp.course_type_id,  NVL(GTP.STUDY_RATIO, 70) STUDY_RATIO,NVL(GTP.EXAM_RATIO, 30) EXAM_RATIO,"
						+ "   NVL(grr.exam_score1, gsss.exam_score) AS exam_score, NVL(grr.exam_score, GSSS.SCORE) COURSE_SCHEDULE,NVL(grr.exam_score2,"
						+ " ROUND(NVL(GSSS.SCORE * NVL(GTP.STUDY_RATIO, 70) / 100, 0)+NVL(GSSS.EXAM_SCORE * NVL(GTP.EXAM_RATIO, 30) / 100, 0),2)) AS TOTAL_POINTS,"
						+ " gtp.EXAM_TYPE  FROM GJT_REC_RESULT GRR  inner JOIN gjt_grade_specialty_plan gtp  ON GTP.Id = GRR.TEACH_PLAN_ID   inner JOIN GJT_COURSE GC"
						+ " ON GC.COURSE_ID = Gtp.COURSE_ID  LEFT JOIN VIEW_STUDENT_STUDY_SITUATION GSSS "
						+ "  ON GSSS.CHOOSE_ID = grr.REC_ID  WHERE GRR.IS_DELETED = 'N'  and gc.is_deleted = 'N'  and grr.student_id=:studentId  and GTP.TERM_TYPE_CODE=:term order by gtp.term_type_code");
		map.put("studentId", id);
		map.put("term", term);
		List<Map<String, String>> list = commonDao.queryForMapListNative(sb.toString(), map);
		return list;
	}

	// 获取一共多少个学期
	@Override
	public List<Map<String, String>> queryTerm(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		sb.append("select  distinct GTP.TERM_TYPE_CODE FROM GJT_REC_RESULT GRR  "
				+ "  inner JOIN gjt_grade_specialty_plan gtp  ON GTP.Id = GRR.TEACH_PLAN_ID   inner JOIN GJT_COURSE GC"
				+ " 	ON GC.COURSE_ID = Gtp.COURSE_ID   LEFT JOIN VIEW_STUDENT_STUDY_SITUATION GSSS "
				+ "  ON GSSS.CHOOSE_ID = grr.REC_ID  WHERE GRR.IS_DELETED = 'N'  and gc.is_deleted = 'N' "
				+ "  and grr.student_id=:studentId  order by gtp.term_type_code");
		map.put("studentId", id);
		List<Map<String, String>> list = commonDao.queryForMapListNative(sb.toString(), map);
		return list;
	}

	// 先查询学分
	@Override
	public List<Map<String, String>> getCreditInfoAnd(String id, String specialtyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(
				"select t.kclbm ,sum(t.score) xf,count(*) KCSL,t.kclbm_code, sum(EXAM_SCORE2) YXXF,  (select sum(a.score)  from gjt_specialty_module_limit a, "
						+ "  tbl_sys_data b   WHERE a.MODULE_ID = b.id  AND b.name = t.kclbm   and a.is_deleted = 'N' and b.is_deleted = 'N'  AND a.specialty_id = :specialtyId) as ZDF  "
						+ "   from (SELECT (SELECT tsd.name  FROM tbl_sys_data tsd   WHERE IS_DELETED = 'N'  AND tsd.type_code = 'CourseType'  AND tsd.code = gtp.course_type_id) AS KCLBM,"
						+ "  (SELECT GTP.Score  FROM GJT_REC_RESULT grr  WHERE grr.TEACH_PLAN_ID = gtp.id "
						+ "  and vs.STUDENT_ID = grr.student_id  and grr.is_deleted = 'N'  and grr.exam_score2 >= 60) AS EXAM_SCORE2,"
						+ "  GTP.Score,  gtp.course_type_id as kclbm_code  FROM gjt_grade_specialty_plan    gtp,"
						+ "  GJT_COURSE   gc,  GJT_SPECIALTY     gs, view_student_info vs   WHERE gtp.course_id = gc.course_id "
						+ "  AND gs.specialty_id = gtp.specialty_id  AND gc.IS_DELETED = 'N'  AND vs.MAJOR = gtp.specialty_id "
						+ "  AND vs.STUDENT_ID =:studentId  and vs.GRADE_ID = gtp.grade_id) t  group by t.kclbm, t.kclbm_code  order by kclbm_code");

		map.put("studentId", id);
		map.put("specialtyId", specialtyId);
		List<Map<String, String>> list = commonDao.queryForMapListNative(sb.toString(), map);
		return list;

	}

	@Override
	public List<Map<String, String>> getPassCreditInfoAnd(String id, String specialtyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(
				"select dic.name kclbm, gtp.kclbm kclbm_code, sum(gtp.xf) xf, count(1) pass  from VIEW_TEACH_PLAN   gtp,"
						+ "  gjt_rec_result   grr,   gjt_student_info gsi,   tbl_sys_data     dic  where gtp.teach_plan_id = grr.teach_plan_id"
						+ "   and gsi.student_id = grr.student_id  and gtp.kclbm = dic.code  and dic.is_deleted = 'N'    and gtp.is_deleted = 'N'"
						+ "   and grr.is_deleted = 'N'  and gsi.is_deleted = 'N'  and dic.type_code = 'CourseType'  and grr.exam_score2 >= 60 "
						+ "  and gsi.STUDENT_ID = :studentId  group by gtp.kclbm, dic.name  order by gtp.kclbm");

		map.put("studentId", id);
		List<Map<String, String>> list = commonDao.queryForMapListNative(sb.toString(), map);
		return list;

	}

	@Override
	public List<Map<String, String>> getMinAndSum(String id, String specialtyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(
				"select to_char(sum(d.xf)) XF,  to_char(sum(d.zdf)) ZDXF,to_char(sum(d.YXXF)) YXXF from (select t.kclbm KCLBM,"
						+ " sum(t.Score) XF, count(*) KCSL, t.kclbm_code,sum(EXAM_SCORE2) YXXF,(select sum(a.score) "
						+ " from gjt_specialty_module_limit a, tbl_sys_data b  WHERE a.MODULE_ID = b.id AND b.name = t.kclbm "
						+ " and a.is_deleted='N' and b.is_deleted='N' AND a.specialty_id = :specialtyId ) as ZDF"
						+ " from (SELECT (SELECT tsd.name  FROM tbl_sys_data tsd WHERE IS_DELETED='N' AND tsd.type_code = 'CourseType'"
						+ " AND tsd.code = gtp.course_type_id) AS KCLBM, (SELECT GTP.Score FROM GJT_REC_RESULT grr"
						+ " WHERE grr.TEACH_PLAN_ID = gtp.id and vs.STUDENT_ID = grr.student_id and grr.is_deleted = 'N' "
						+ " and grr.exam_score2 >= 60) AS EXAM_SCORE2, GTP.Score,gtp.course_type_id as kclbm_code FROM"
						+ " gjt_grade_specialty_plan gtp,GJT_COURSE   gc,  GJT_TERM_INFO gti, GJT_SPECIALTY  gs,"
						+ " view_student_info vs WHERE gtp.course_id = gc.course_id  AND gs.specialty_id = gtp.specialty_id"
						+ " AND gc.IS_DELETED = 'N'  AND vs.MAJOR = gtp.specialty_id AND vs.STUDENk_ID=:studentId  and "
						+ " vs.GRADE_ID = gti.grade_id) t group by t.kclbm, t.kclbm_code order by kclbm_code) d   ");
		map.put("studentId", id);
		map.put("specialtyId", specialtyId);
		List<Map<String, String>> list = commonDao.queryForMapListNative(sb.toString(), map);
		return list;
	}

	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		sb.append(
				"select to_char(sum(d.xf)) XF,  to_char(sum(d.zdf)) ZDXF,to_char(sum(d.YXXF)) YXXF from (select t.kclbm KCLBM,"
						+ " sum(t.Score) XF, count(*) KCSL, t.kclbm_code,sum(EXAM_SCORE2) YXXF,(select sum(a.score) "
						+ " from gjt_specialty_module_limit a, tbl_sys_data b  WHERE a.MODULE_ID = b.id AND b.name = t.kclbm "
						+ " and a.is_deleted='N' and b.is_deleted='N' AND a.specialty_id = :specialtyId ) as ZDF"
						+ " from (SELECT (SELECT tsd.name  FROM tbl_sys_data tsd WHERE IS_DELETED='N' AND tsd.type_code = 'CourseType'"
						+ " AND tsd.code = gtp.course_type_id) AS KCLBM, (SELECT GTP.Score FROM GJT_REC_RESULT grr"
						+ " WHERE grr.TEACH_PLAN_ID = gtp.id and vs.STUDENT_ID = grr.student_id and grr.is_deleted = 'N' "
						+ " and grr.exam_score2 >= 60) AS EXAM_SCORE2, GTP.Score,gtp.course_type_id as kclbm_code FROM"
						+ " gjt_grade_specialty_plan gtp,GJT_COURSE   gc,  GJT_TERM_INFO gti, GJT_SPECIALTY  gs,"
						+ " view_student_info vs WHERE gtp.course_id = gc.course_id  AND gs.specialty_id = gtp.specialty_id"
						+ " AND gc.IS_DELETED = 'N'  AND vs.MAJOR = gtp.specialty_id AND vs.STUDENk_ID=:studentId  and "
						+ " vs.GRADE_ID = gti.grade_id) t group by t.kclbm, t.kclbm_code order by kclbm_code) d   ");
		System.out.println(sb.toString());
	}

	/**
	 * 根据学生id查询教学班学生期信息
	 *
	 * @param student_id
	 * @return
	 */
	@Override
	public List<Map<String, String>> queryTeachTerm(String student_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		// sb.append(" SELECT DISTINCT gti.TERM_CODE,GTI.TERM_ID FROM
		// GJT_TERM_INFO gti,GJT_STUDENT_TERM gst,GJT_STUDENT_INFO
		// gsi,VIEW_TEACH_PLAN gtp,GJT_REC_RESULT grr");
		// sb.append(" WHERE gti.IS_DELETED='N' AND gsi.IS_DELETED='N' AND
		// gtp.IS_DELETED='N' AND grr.IS_DELETED='N'");
		// sb.append(" AND gti.TERM_ID=gst.TERM_ID AND gst.TERM_ID=gtp.TERM_ID
		// AND gtp.TERM_ID=grr.TERM_ID");
		// sb.append(" AND gtp.TEACH_PLAN_ID=grr.TEACH_PLAN_ID AND
		// grr.STUDENT_ID=gst.STUDENT_ID");
		// sb.append(" AND gst.STUDENT_ID=gsi.STUDENT_ID AND
		// gsi.STUDENT_ID=:student_id ORDER BY gti.TERM_CODE");

		sb.append("  SELECT");
		sb.append("  	DISTINCT gg.GRADE_CODE TERM_CODE,gg.GRADE_NAME TERM_NAME,");
		sb.append(
				"		(CASE WHEN SYSDATE BETWEEN gg.START_DATE AND NVL( gg.END_DATE, ADD_MONTHS(gg.START_DATE, 4 )) THEN 'Y' WHEN gg.END_DATE<SYSDATE THEN 'Y' ELSE 'N' END) IS_CURRENT,");
		sb.append("  	gg.GRADE_ID TERM_ID,gtp.KKXQ");
		sb.append("  FROM");
		sb.append("  	GJT_GRADE gg");
		sb.append("  INNER JOIN VIEW_TEACH_PLAN gtp ON");
		sb.append("  	gg.GRADE_ID = gtp.ACTUAL_GRADE_ID");
		sb.append("  	AND gg.IS_DELETED = 'N'");
		sb.append("  	AND gtp.IS_DELETED = 'N'");
		sb.append("  LEFT JOIN GJT_REC_RESULT grr ON");
		sb.append("  	gtp.TEACH_PLAN_ID = grr.TEACH_PLAN_ID AND grr.IS_DELETED='N'");
		sb.append("  INNER JOIN GJT_STUDENT_INFO gsi ON ");
		sb.append(
				"  	grr.STUDENT_ID=gsi.STUDENT_ID AND gsi.IS_DELETED='N' AND gsi.STUDENT_ID=:student_id ORDER BY gtp.KKXQ, gg.GRADE_CODE ASC");

		map.put("student_id", student_id);
		return commonDao.queryForMapListNative(sb.toString(), map);
	}

	/**
	 * 根据学生id，期id信息获取教学班学生的课程学习信息详情
	 *
	 * @param student_id
	 * @param term_id
	 * @return
	 */
	@Override
	public List<Map<String, String>> queryTeachStudentSourceDetail(String student_id, String term_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	gtp.TEACH_PLAN_ID,");
		sql.append("  	grr.STUDENT_ID,");
		sql.append("  	gc.COURSE_ID,");
		sql.append(
				"  	(SELECT gg.GRADE_NAME FROM GJT_GRADE gg WHERE gg.GRADE_ID=gtp.ACTUAL_GRADE_ID AND gg.IS_DELETED='N') TERM_NAME,");
		sql.append(
				"  	(SELECT tsd.name FROM tbl_sys_data tsd WHERE IS_DELETED = 'N' AND tsd.type_code = 'CourseType' AND tsd.code = gtp.KCLBM ) KCLBM_NAME,");
		sql.append(
				"  	(SELECT tsd.name FROM tbl_sys_data tsd WHERE IS_DELETED = 'N' AND tsd.type_code = 'ExaminationMode' AND tsd.code = gtp.KSFS ) KSFS_NAME,");
		sql.append("  	gc.KCMC,gc.KCH,");
		sql.append("  	gtp.XF,");
		sql.append("  	nvl(grr.GET_CREDITS,0) GET_POINT,");
		sql.append("  	grr.EXAM_SCORE STUDY_SCORE,");
		sql.append("  	grr.EXAM_SCORE1 EXAM_SCORE,");
		sql.append("  	grr.EXAM_SCORE2 SUM_SCORE,");
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN GRR.EXAM_STATE = '2' AND GRR.EXAM_SCORE IS NULL THEN '4'");
		sql.append("  			ELSE TO_CHAR( GRR.EXAM_STATE )");
		sql.append("  		END");
		sql.append("  	) SCORE_STATE,");
		sql.append(
				"  	(SELECT COUNT(*) FROM GJT_LEARN_REPAIR glr WHERE glr.IS_DELETED='N' AND glr.TEACH_PLAN_ID=gtp.TEACH_PLAN_ID AND glr.STUDENT_ID=grr.STUDENT_ID) EXAM_NUM, ");
		sql.append("  	TO_CHAR(GG.START_DATE,'yyyy-MM-dd') START_DATE, ");
		sql.append("  	TO_CHAR(END_DATE,'yyyy-MM-dd') END_DATE, ");
		sql.append(
				"  	GSS.PROGRESS,NVL( GSS.LOGIN_TIMES, 0 ) LOGIN_COUNT,ROUND( NVL( GSS.ONLINE_TIME / 60, 0 ), 1 ) LOGIN_TIME,GSS.IS_ONLINE,FLOOR(SYSDATE-GSS.LAST_LOGIN_DATE) LEFT_DAY,GSS.BYOD_TYPE,");
		sql.append("  		(");
		sql.append("            CASE");
		sql.append("              WHEN grr.EXAM_STATE = '3' THEN(");
		sql.append("                (");
		sql.append("                  select NVL(TO_CHAR(XK_PERCENT), '') from (");
		sql.append("                        select gea.appointment_id,gea.student_id,gea.rec_id,GEP.XK_PERCENT");
		sql.append("                        from GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("                        inner join Gjt_Exam_Plan_New GEP on GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("                        where GEA.IS_DELETED = 0");
		sql.append("                        order by gea.created_dt desc");
		sql.append("                  ) xxx");
		sql.append(
				"                  where xxx.student_id = GRR.STUDENT_ID and xxx.rec_id = GRR.Rec_Id AND ROWNUM = 1");
		sql.append("                )");
		sql.append("              )");
		sql.append("              ELSE NVL( TO_CHAR( grr.COURSE_SCHEDULE ), '' )");
		sql.append("            END");
		sql.append("          ) XK_PERCENT,");
		sql.append("          (");
		sql.append(
				"            select (select tsd.name from tbl_sys_data tsd where tsd.is_deleted='N' and tsd.type_code='ExaminationMode' and tsd.code=type) from (");
		sql.append("                  select gea.appointment_id,gea.student_id,gea.rec_id,GEP.type");
		sql.append("                  from GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("                  inner join Gjt_Exam_Plan_New GEP on GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("                  where GEA.IS_DELETED = 0");
		sql.append("                  order by gea.created_dt desc");
		sql.append("            ) xxx");
		sql.append("            where xxx.student_id = GRR.STUDENT_ID and xxx.rec_id = GRR.Rec_Id AND ROWNUM = 1");
		sql.append("          ) EXAM_PLAN_KSFS_NAME");
		sql.append("  FROM");
		sql.append("  	GJT_REC_RESULT grr");
		sql.append("  INNER JOIN VIEW_TEACH_PLAN gtp ON");
		sql.append("  	grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID ");
		sql.append("  	AND grr.STUDENT_ID = :student_id ");
		sql.append("  INNER JOIN GJT_COURSE gc ON");
		sql.append("  	gtp.COURSE_ID = gc.COURSE_ID ");
		sql.append("  INNER JOIN GJT_GRADE GG ON");
		sql.append("  	GTP.ACTUAL_GRADE_ID = GG.GRADE_ID");
		sql.append("  LEFT JOIN VIEW_STUDENT_STUDY_SITUATION GSS ON");
		sql.append("  	grr.REC_ID = GSS.CHOOSE_ID");
		sql.append("  	WHERE grr.IS_DELETED='N' AND gtp.IS_DELETED='N' AND gc.IS_DELETED='N' ");
		sql.append("  	AND GG.IS_DELETED = 'N' ");
		sql.append("    AND GTP.ACTUAL_GRADE_ID = :term_id");
		sql.append("  ORDER BY gtp.CREATED_DT");

		/*
		 * sql.append("  SELECT"); sql.append("    gtp.TEACH_PLAN_ID,");
		 * sql.append("  	gsi.STUDENT_ID,"); sql.append("    gep.XK_PERCENT,"
		 * ); sql.append("  	gti.TERM_ID,"); sql.append("  	gti.TERM_NAME,"
		 * ); sql.append("  	gti.TERM_CODE,"); sql.append(
		 * "  	gsr.COURSE_ID,"); sql.append(
		 * "    (SELECT gc.KCMC FROM GJT_COURSE gc WHERE gc.IS_DELETED='N' AND gc.COURSE_ID=gsr.COURSE_ID) KCMC,"
		 * ); sql.append("  	gtp.KCLBM,"); sql.append(
		 * "  	(SELECT tsd.name FROM tbl_sys_data tsd WHERE IS_DELETED = 'N' AND tsd.type_code = 'CourseType' AND tsd.code = gtp.KCLBM ) KCLBM_NAME,"
		 * ); sql.append("  	gtp.XF,"); sql.append("  	gtp.KSFS,");
		 * sql.append(
		 * "    (SELECT tsd.name FROM tbl_sys_data tsd WHERE IS_DELETED = 'N' AND tsd.type_code = 'ExaminationMode' AND tsd.code = gtp.KSFS ) KSFS_NAME,"
		 * ); sql.append("  	NVL( gsr.GET_POINT, 0 ) AS GET_POINT,");
		 * sql.append("  	gsr.STUDY_SCORE,"); sql.append("  	gsr.EXAM_SCORE,"
		 * ); sql.append("  	gsr.SUM_SCORE,"); sql.append(
		 * "  	gsr.SCORE_STATE,"); sql.append(
		 * "    (SELECT COUNT(*) FROM GJT_LEARN_REPAIR glr WHERE glr.IS_DELETED='N' AND glr.STUDENT_ID=grr.STUDENT_ID AND glr.TEACH_PLAN_ID=gtp.TEACH_PLAN_ID) EXAM_NUM"
		 * ); sql.append("  FROM"); sql.append("  	GJT_TERM_INFO gti,");
		 * sql.append("  	GJT_STUDENT_TERM gst,"); sql.append(
		 * "  	GJT_STUDENT_INFO gsi,"); //sql.append(
		 * "  	VIEW_TEACH_PLAN gtp,"); sql.append(
		 * "    VIEW_TEACH_PLAN gtp LEFT JOIN");// sql.append(
		 * "    GJT_EXAM_SUBJECT_NEW ges ON");// sql.append(
		 * "    gtp.COURSE_CODE=ges.COURSE_ID AND ges.IS_DELETED=0 LEFT JOIN"
		 * );// sql.append("    GJT_EXAM_PLAN_NEW gep ON");// sql.append(
		 * "    ges.SUBJECT_CODE=gep.SUBJECT_CODE AND gep.IS_DELETED=0,");//
		 * sql.append("  	GJT_REC_RESULT grr"); sql.append(
		 * "  LEFT JOIN GJT_STUDY_RECORD gsr ON"); sql.append(
		 * "  	gsr.COURSE_ID = grr.COURSE_ID"); sql.append("  WHERE");
		 * sql.append("  	gti.IS_DELETED = 'N'"); sql.append(
		 * "  	AND gsi.IS_DELETED = 'N'"); sql.append(
		 * "  	AND gtp.IS_DELETED = 'N'"); sql.append(
		 * "  	AND grr.IS_DELETED = 'N'"); sql.append(
		 * "  	AND gti.TERM_ID = gst.TERM_ID"); sql.append(
		 * "  	AND gst.TERM_ID = gtp.TERM_ID"); sql.append(
		 * "  	AND gtp.TERM_ID = grr.TERM_ID"); sql.append(
		 * "  	AND grr.TERM_ID = :term_id"); sql.append(
		 * "  	AND gtp.TEACH_PLAN_ID = grr.TEACH_PLAN_ID"); sql.append(
		 * "  	AND grr.STUDENT_ID = gst.STUDENT_ID"); sql.append(
		 * "  	AND gst.STUDENT_ID = gsi.STUDENT_ID"); sql.append(
		 * "  	AND gsi.STUDENT_ID = gsr.STUDENT_ID"); sql.append(
		 * "  	AND gsi.STUDENT_ID = :student_id"); sql.append("  ORDER BY");
		 * sql.append("  	gti.TERM_CODE,gsr.UPDATED_DT,gsr.CREATED_DT");
		 */

		map.put("student_id", student_id);
		map.put("term_id", term_id);
		List<Map<String, String>> list = commonDao.queryForMapListNative(sql.toString(), map);
		return list;
	}

	/**
	 * 首页 成绩与学分--学分详情
	 *
	 * @param param
	 * @return
	 */
	@Override
	public List<Map<String, String>> getCreditDetail(Map<String, Object> param) {

		StringBuffer sql = new StringBuffer();

		Map params = new HashMap();

		sql.append("  SELECT");
		sql.append("  	tsd.NAME,");
		sql.append("  	gsi.MAJOR SPECIALTY_ID,");
		sql.append("  	gtp.kclbm kclbm_code,");
		sql.append("  	COUNT( gtp.TEACH_PLAN_ID ) AS COUNT_COURSE,");
		sql.append("  	SUM((CASE WHEN grr.EXAM_STATE='0' THEN 1 ELSE 0 END)) UN_PASS_COURSE,");// 未通过课程
		sql.append("  	SUM((CASE WHEN grr.EXAM_STATE='1' THEN 1 ELSE 0 END)) PASS_COURSE,");// 已通过课程
		sql.append("  	gsml.TOTALSCORE AS XF_COUNT,");
		sql.append("  	SUM( NVL( grr.GET_CREDITS, 0 )) AS GET_POINT,");
		sql.append("  	SUM( NVL( grr.EXAM_SCORE2, 0 )) AS SUM_SCORE,");
		sql.append("  	gsml.CRTVU_SCORE,");
		sql.append("  	gsml.module_id,");
		sql.append("  	gsml.SCORE AS LIMIT_SCORE");
		sql.append("  FROM");
		sql.append("  	GJT_GRADE GG,");
		sql.append("  	GJT_STUDENT_INFO gsi");
		sql.append("  LEFT JOIN GJT_SPECIALTY_MODULE_LIMIT gsml ON");
		sql.append("  	gsi.MAJOR = gsml.SPECIALTY_ID,");
		sql.append("  	VIEW_TEACH_PLAN gtp");
		sql.append("  LEFT JOIN TBL_SYS_DATA tsd ON");
		sql.append("  	tsd.TYPE_CODE = 'CourseType'");
		sql.append("  	AND tsd.CODE = gtp.KCLBM,");
		sql.append("  	GJT_REC_RESULT grr");
		sql.append("  WHERE");
		sql.append("  	gg.IS_DELETED = 'N'");
		sql.append("  	AND gsi.IS_DELETED = 'N'");
		sql.append("  	AND gtp.IS_DELETED = 'N'");
		sql.append("  	AND grr.IS_DELETED = 'N'");
		sql.append("  	AND gg.GRADE_ID = gtp.ACTUAL_GRADE_ID");
		sql.append("  	AND grr.COURSE_ID = gtp.COURSE_ID");
		sql.append("  	AND gtp.TEACH_PLAN_ID = grr.TEACH_PLAN_ID");
		sql.append("  	AND gsi.STUDENT_ID = grr.STUDENT_ID");
		sql.append("  	AND gsml.SPECIALTY_ID = gsi.MAJOR");
		sql.append("  	AND gsml.MODULE_ID = tsd.ID");
		sql.append("  	AND gsi.STUDENT_ID = :student_id");
		// sql.append(" AND GSI.MAJOR = :specialty_id");//不需要知道专业
		sql.append("  GROUP BY");
		sql.append("  	GTP.KCLBM,");
		sql.append("  	tsd.NAME,");
		sql.append("  	gsi.MAJOR,");
		sql.append("  	gsml.CRTVU_SCORE,");
		sql.append("  	gsml.SCORE,");
		sql.append("  	gsml.module_id,");
		sql.append("  	gsml.TOTALSCORE");
		sql.append("  ORDER BY");
		sql.append("  	GTP.KCLBM");

		params.put("student_id", ObjectUtils.toString(param.get("student_id"), ""));
		// params.put("specialty_id",ObjectUtils.toString(param.get("specialty_id"),""));

		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	public List<Map<String, String>> queryStudentCredit(String StudentId) {
		StringBuilder sql = new StringBuilder(" select t.xf credit ,r.get_credits getCredits, s.name courseName");
		sql.append(" from VIEW_TEACH_PLAN t ,gjt_rec_result r ,tbl_sys_data s");
		sql.append(" where t.teach_plan_id = r.teach_plan_id ");
		sql.append(" 	and s.type_code = 'CourseType' ");
		sql.append(" 	and s.code = t.KCLBM ");
		sql.append(" 	and r.student_id = :student_id ");
		sql.append(" 	and t.is_deleted = 'N'");
		sql.append(" 	and r.is_deleted = 'N' ");
		Map<String, Object> params = Maps.newHashMap();
		params.put("student_id", StudentId);
		List<Map<String, String>> list = commonDao.queryForMapListNative(sql.toString(), params);
		return list;
	}

	/**
	 * 首页 成绩与学分-成绩详情--查看历史成绩
	 *
	 * @param formMap
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getHistoryScore(Map<String, Object> formMap) {

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		StringBuilder sql = new StringBuilder();

		sql.append("  SELECT");
		sql.append("  	gti.GRADE_ID TERM_ID,");
		sql.append("  	gti.GRADE_NAME TERM_NAME,");
		sql.append("  	glr.TEACH_PLAN_ID,");
		sql.append("  	NVL(glr.XCX_SCORE,0) XCX_SCORE,");
		sql.append("  	NVL(glr.ZJX_SCORE,0) ZJX_SCORE,");
		sql.append("  	NVL(glr.ZCJ_SCORE,0) ZCJ_SCORE,");
		sql.append("  	NVL(TO_CHAR(GRR.COURSE_SCHEDULE),'') XCX_PERCENT,");
		// sql.append(" NVL((SELECT DISTINCT gep.XK_PERCENT FROM
		// GJT_EXAM_PLAN_NEW gep WHERE gep.IS_DELETED=0 AND
		// gep.\"TYPE\"=gtp.KSFS AND gep.COURSE_ID=gtp.COURSE_ID AND ROWNUM<2
		// GROUP BY gep.XK_PERCENT),0) XCX_PERCENT,");//用上面替代
		sql.append("  	glr.STATUS");
		sql.append("  FROM");
		sql.append("  	GJT_LEARN_REPAIR glr");
		sql.append("  	LEFT JOIN VIEW_TEACH_PLAN gtp ON");
		sql.append("  	gtp.TEACH_PLAN_ID=glr.TEACH_PLAN_ID");
		sql.append("  	AND gtp.IS_DELETED='N' LEFT JOIN GJT_GRADE gti ON");
		sql.append("  	gtp.ACTUAL_GRADE_ID = gti.GRADE_ID AND gti.IS_DELETED='N'");
		sql.append(
				"    LEFT JOIN GJT_REC_RESULT grr ON grr.STUDENT_ID=glr.STUDENT_ID AND grr.TEACH_PLAN_ID=glr.TEACH_PLAN_ID");
		sql.append("  WHERE");
		sql.append("  	glr.IS_DELETED = 'N'");
		sql.append("  	AND grr.IS_DELETED='N'");
		sql.append("  	AND glr.STUDENT_ID = :studentId");
		sql.append("  	AND GTP.TEACH_PLAN_ID = :teachPlanId");
		sql.append("  ORDER BY");
		sql.append("  	glr.CREATED_DT DESC");

		Map<String, String> tm = new HashMap<String, String>();

		List<Map<String, String>> list = commonDao.queryForMapListNative(sql.toString(), formMap);
		if (list != null && list.size() > 0) {
			for (Map temp : list) {
				tm.put(ObjectUtils.toString(temp.get("TERM_ID")), ObjectUtils.toString(temp.get("TERM_ID")));
			}
			for (String string : tm.keySet()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("HISTORY_MSG", new ArrayList());
				for (Map temp : list) {
					if (string.equals(ObjectUtils.toString(temp.get("TERM_ID")))) {
						map.put("TERM_ID", ObjectUtils.toString(temp.get("TERM_ID")));
						map.put("TERM_NAME", ObjectUtils.toString(temp.get("TERM_NAME")));
						if (EmptyUtils.isNotEmpty(ObjectUtils.toString(temp.get("XCX_PERCENT"), ""))) {
							temp.put("ZJX_PERCENT",
									100 - Integer.parseInt(ObjectUtils.toString(temp.get("XCX_PERCENT"), "0")));
						} else {
							temp.put("ZJX_PERCENT", "0");
						}
						((List) map.get("HISTORY_MSG")).add(temp);
					}
				}
				resultList.add(map);
			}
		}
		return resultList;

	}

	@Override
	public GjtRecResult findByStudentIdAndTeachPlanIdAndCourseId(String studentId, String teachPlanId,
			String courseId) {
		return gjtRecResultDao.findByStudentIdAndTeachPlanIdAndCourseIdAndIsDeleted(studentId, teachPlanId, courseId,
				"N");
	}

	@Override
	public GjtRecResult findByStudentIdAndTeachPlanId(String studentId, String teachPlanId) {
		return gjtRecResultDao.findByStudentIdAndTeachPlanIdAndIsDeleted(studentId, teachPlanId, "N");
	}

	@Override
	public GjtRecResult update(GjtRecResult entity) {
		return gjtRecResultDao.save(entity);
	}

	/**
	 * 查询学员学情详情
	 *
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> queryStuStudyCondition(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> args = new HashMap<String, Object>();

		sql.append("  SELECT ");
		sql.append(
				"  	gsi.STUDENT_ID,gsi.XM,gsi.XH,gsi.SJH,GSI.AVATAR ZP,gsi.MAJOR,GY.NAME,GG.GRADE_NAME,GS.ZDBYXF CREDITS_MIN,gsi.NJ,gci.BJMC,");
		sql.append(
				"  	NVL((SELECT vss.IS_ONLINE  FROM VIEW_STUDENT_STUDY_SITUATION vss  WHERE vss.STUDENT_ID=gsi.STUDENT_ID AND VSS.IS_ONLINE='Y' AND ROWNUM=1),'N') IS_ONLINE,");
		sql.append(
				"  	(SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = GSI.XBM AND TSD.TYPE_CODE = 'Sex') SEX,");
		sql.append(
				"  	(SELECT gs.zymc FROM GJT_SPECIALTY gs WHERE gs.IS_DELETED = 'N' AND gs.SPECIALTY_ID = gsi.MAJOR) ZYMC,gsi.PYCC,");
		sql.append(
				"  	(SELECT NVL( SUM( GTP.XF ), 0 ) FROM GJT_REC_RESULT GRR,VIEW_TEACH_PLAN GTP WHERE GRR.IS_DELETED = 'N' AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID AND GRR.STUDENT_ID = GSI.STUDENT_ID) ZXF,");
		sql.append(
				"  	(SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.CODE = gsi.PYCC AND TSD.TYPE_CODE = 'TrainingLevel') PYCC_NAME,");
		sql.append(
				"  	(SELECT ROUND( NVL( DECODE( SUM( NVL( gsr.MY_ACTCOUNT, 0 )), 0, 0, SUM( NVL( gsr.MY_ACTCOUNT, 0 ))/ SUM( NVL( gsr.ACT_COUNT, 0 ))* 100 ), 0 ), 1 ) ACT_PROGRESS FROM VIEW_STUDENT_STUDY_SITUATION gsr");
		sql.append("  		WHERE gsr.STUDENT_ID = gsi.STUDENT_ID GROUP BY gsr.STUDENT_ID) ACT_PROGRESS,");
		sql.append(
				"  	(SELECT ggg.grade_name FROM GJT_GRADE ggg WHERE ggg.IS_DELETED = 'N' AND ggg.START_DATE < SYSDATE AND ggg.END_DATE > SYSDATE AND ggg.GRADE_ID = gg.GRADE_ID");
		sql.append(
				"  			AND ggg.XX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED = 'N' START WITH org.ID =:xxId CONNECT BY PRIOR ORG.PERENT_ID = ORG.ID) AND rownum = 1) CURRENT_TERM,");
		sql.append(
				"  	SUM( NVL( grr.GET_CREDITS, 0 )) credits_count,COUNT( grr.COURSE_ID ) course_count,COUNT( grr.TEACH_PLAN_ID ) exam_count,");
		sql.append(
				"  	(SELECT SUM( NVL( gsr.LOGIN_TIMES, 0 )) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON grr.REC_ID = gsr.CHOOSE_ID WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID) LOGIN_TIMES,");
		sql.append(
				"  	(SELECT ROUND( SUM( NVL( gsr.ONLINE_TIME, 0 ))/ 60, 1 ) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON grr.REC_ID = gsr.CHOOSE_ID WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID) LOGIN_TIME,");
		sql.append(
				"  	(SELECT COUNT(*) FROM GJT_REC_RESULT grr WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID AND grr.EXAM_STATE = '0') UNPASS_COURSE,");
		sql.append(
				"  	(SELECT COUNT(*) FROM GJT_REC_RESULT grr WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID AND grr.EXAM_STATE = '1') PASS_COURSE,");
		sql.append(
				"  	(SELECT COUNT(*) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON grr.REC_ID = gsr.CHOOSE_ID WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID AND grr.EXAM_STATE = '2' AND gsr.LOGIN_TIMES > 0) LEARNING_COURSE,");
		sql.append(
				"  	(SELECT COUNT(*) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON grr.REC_ID = gsr.CHOOSE_ID WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID AND grr.EXAM_STATE = '2' AND(gsr.LOGIN_TIMES = 0 OR GSR.LOGIN_TIMES IS NULL)) UNLEARN_COURSE,");
		sql.append(
				"  	(SELECT COUNT(*) FROM GJT_REC_RESULT grr WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID AND grr.EXAM_STATE = '3') REGISTER_COURSE,");
		sql.append(
				"  	(SELECT COUNT(*) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_TEACH_PLAN gtp ON grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID AND grr.EXAM_STATE IN ('0','1','3')) EXAM_FINISH,");
		sql.append(
				"  	(SELECT COUNT(*) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_TEACH_PLAN gtp ON grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID WHERE grr.IS_DELETED = 'N' AND grr.STUDENT_ID = gsi.STUDENT_ID AND grr.EXAM_STATE ='1') PASS_EXAM");
		sql.append("  FROM");
		sql.append("  	GJT_CLASS_INFO gci");
		sql.append("  LEFT JOIN GJT_GRADE gg ON");
		sql.append("  	gg.GRADE_ID = gci.GRADE_ID");
		sql.append("  LEFT JOIN GJT_YEAR gy ON");
		sql.append("  	gg.YEAR_ID = gy.GRADE_ID,");
		sql.append("  	GJT_CLASS_STUDENT gcs,");
		sql.append("  	GJT_STUDENT_INFO gsi");
		sql.append("  LEFT JOIN GJT_REC_RESULT grr ON");
		sql.append("  	gsi.STUDENT_ID = grr.STUDENT_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY gs ON");
		sql.append("  	gsi.MAJOR = gs.SPECIALTY_ID");
		sql.append("  WHERE");
		sql.append("  	gci.IS_DELETED = 'N'");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  	AND gsi.IS_DELETED = 'N'");
		sql.append("  	AND gg.IS_DELETED = 'N'");
		sql.append("  	AND grr.IS_DELETED = 'N'");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  	AND gsi.IS_DELETED = 'N'");
		sql.append("  	AND gci.CLASS_ID = gcs.CLASS_ID");
		sql.append("  	AND gcs.STUDENT_ID = gsi.STUDENT_ID");
		sql.append("  	AND gci.CLASS_ID =:classId");
		sql.append("  	AND gsi.STUDENT_ID =:studentId");
		sql.append("  GROUP BY");
		sql.append(
				"  	gsi.STUDENT_ID,gsi.XM,gsi.XH,gsi.SJH,gsi.MAJOR,gsi.PYCC,gsi.NJ,gg.GRADE_ID,GY.NAME,GG.GRADE_NAME,GSI.XBM,GSI.AVATAR,GS.ZDBYXF,gci.BJMC");

		args.put("xxId", params.get("xxId"));
		args.put("classId", params.get("classId"));
		args.put("studentId", params.get("studentId"));
		return commonDao.queryObjectToMapNative(sql.toString(), args);
	}

	/**
	 * 学员课程学情详情
	 * 
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, String>> queryStudentRecResultLearningDetail(Map<String, Object> params) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder sql = new StringBuilder();

		sql.append("  SELECT");
		sql.append(
				"  			grr.TEACH_PLAN_ID,gtc.TERMCOURSE_ID,gc.COURSE_ID,gsr.PROGRESS SCHEDULE,gtp.XF,NVL(gsr.IS_ONLINE,'N') IS_ONLINE,FLOOR(SYSDATE-gsr.LAST_LOGIN_DATE) LEFT_DATE,");
		sql.append(
				"			(SELECT FLOOR(SUM(NVL(vss.LOGIN_TIMES,0))/COUNT(vss.CHOOSE_ID)) FROM VIEW_STUDENT_STUDY_SITUATION VSS WHERE vss.CLASS_ID=gsr.CLASS_ID) CLASS_AVG_LOGINS,");
		sql.append(
				"  			grr.STUDENT_ID,grr.REC_ID CHOOSE_ID,gc.KCMC,gc.KCH,ROUND(NVL(gsr.ONLINE_TIME,0)/60,1) LOGIN_TIME,gsr.LOGIN_TIMES LOGIN_COUNT,");
		sql.append(
				"  			(SELECT gg.GRADE_ID FROM GJT_GRADE gg WHERE gg.GRADE_ID = gtp.ACTUAL_GRADE_ID AND gg.IS_DELETED = 'N') TERM_ID,");
		sql.append(
				"  			(SELECT gg.GRADE_NAME FROM GJT_GRADE gg WHERE gg.GRADE_ID = gtp.ACTUAL_GRADE_ID AND gg.IS_DELETED = 'N') TERM_NAME,");
		sql.append(
				"  			(SELECT tsd.name FROM tbl_sys_data tsd WHERE IS_DELETED = 'N' AND tsd.type_code = 'CourseType' AND tsd.code = gtp.KCLBM) KCLBM_NAME,");
		sql.append(
				"  			(SELECT tsd.name FROM tbl_sys_data tsd WHERE IS_DELETED = 'N' AND tsd.type_code = 'ExaminationMode' AND tsd.code = gtp.KSFS) KSFS_NAME,");
		sql.append(
				"  			NVL( TO_CHAR(grr.GET_CREDITS), '--' ) GET_CREDITS,NVL( TO_CHAR( grr.COURSE_SCHEDULE ), '--' ) XK_PERCENT,");
		sql.append("  			gsr.EXAM_SCORE,gsr.EXAM_SCORE1,gsr.EXAM_SCORE2,gsr.EXAM_STATE,gsr.STATE");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT grr");
		sql.append("  		LEFT JOIN VIEW_TEACH_PLAN gtp ON");
		sql.append("  			grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID");
		sql.append("  		LEFT JOIN GJT_TERM_COURSEINFO gtc ON grr.TERMCOURSE_ID=gtc.TERMCOURSE_ID");
		sql.append("  		LEFT JOIN VIEW_STUDENT_STUDY_SITUATION gsr ON");
		sql.append("  			grr.REC_ID = gsr.CHOOSE_ID");
		sql.append("  		LEFT JOIN GJT_COURSE gc ON");
		sql.append("  			gtp.COURSE_ID = gc.COURSE_ID");
		sql.append("  		WHERE");
		sql.append("  			grr.IS_DELETED = 'N'");
		sql.append("  			AND gc.IS_DELETED = 'N'");
		sql.append("  			AND grr.STUDENT_ID =:studentId");

		if (EmptyUtils.isNotEmpty(params.get("GRADE_ID"))) {
			sql.append("	AND gtp.ACTUAL_GRADE_ID = :GRADE_ID");
			parameters.put("GRADE_ID", ObjectUtils.toString(params.get("GRADE_ID")));
		}
		sql.append("  		ORDER BY");
		sql.append("  			gtp.KKXQ");

		parameters.put("studentId", ObjectUtils.toString(params.get("studentId")));
		return commonDao.queryForMapListNative(sql.toString(), parameters);
	}

	/**
	 * 学员课程学情明细
	 *
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> courseConditionDetials(Map<String, Object> params) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();

		Map args = new HashMap();

		sql.append("  SELECT");
		sql.append(
				"  	gsi.STUDENT_ID,gsi.XM,gsi.XH,gsi.SJH,gsi.PYCC,gy.NAME,vss.EXAM_SCORE2,grr.REC_ID,grr.TERMCOURSE_ID,gcs.CLASS_ID,gci.COURSE_ID");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO gsi ");
		sql.append("  	LEFT JOIN GJT_SPECIALTY gs ON gsi.MAJOR=gs.SPECIALTY_ID ");
		sql.append("  	LEFT JOIN GJT_REC_RESULT grr ON gsi.STUDENT_ID=grr.STUDENT_ID ");
		sql.append("  	LEFT JOIN VIEW_STUDENT_STUDY_SITUATION vss ON grr.REC_ID=vss.CHOOSE_ID,");
		sql.append("  	GJT_CLASS_INFO gci,");
		sql.append("  	GJT_CLASS_STUDENT gcs");
		sql.append("  LEFT JOIN GJT_GRADE gg ON gg.GRADE_ID=gcs.GRADE_ID");
		sql.append("  LEFT JOIN GJT_YEAR gy ON gg.YEAR_ID=gy.GRADE_ID");
		sql.append("  WHERE");
		sql.append("  	gsi.IS_DELETED = 'N'");
		sql.append("  	AND gci.IS_DELETED='N'");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  	AND gs.IS_DELETED='N'");
		sql.append("  	AND gcs.IS_DELETED='N'");
		sql.append("  	AND grr.IS_DELETED='N'");
		sql.append("  	AND gci.CLASS_TYPE='course'");
		sql.append("  	AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		sql.append("  	AND gcs.CLASS_ID = gci.CLASS_ID");
		sql.append("  	AND grr.TERMCOURSE_ID=vss.TERMCOURSE_ID");
		sql.append("  	AND vss.TERMCOURSE_ID=gci.TERMCOURSE_ID");
		sql.append("  	AND gci.COURSE_ID=:courseId");
		sql.append("  	AND grr.TERMCOURSE_ID= :termcourseId");
		sql.append("  	AND gsi.STUDENT_ID = :studentId");

		args.put("courseId", ObjectUtils.toString(params.get("courseId")));
		args.put("termcourseId", ObjectUtils.toString(params.get("termcourseId")));
		args.put("studentId", ObjectUtils.toString(params.get("studentId")));

		Map<String, Object> result = commonDao.queryObjectToMapNative(sql.toString(), args);
		if (EmptyUtils.isNotEmpty(result)) {
			resultMap.put("CLASS_ID", ObjectUtils.toString(result.get("CLASS_ID"), ""));
			resultMap.put("TERMCOURSE_ID", ObjectUtils.toString(result.get("TERMCOURSE_ID"), ""));
			resultMap.put("STUDENT_ID", ObjectUtils.toString(result.get("STUDENT_ID"), ""));
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("formMap.TERMCOURSE_ID", ObjectUtils.toString(params.get("termcourseId"), ""));
			param.put("formMap.CLASS_ID", ObjectUtils.toString(result.get("CLASS_ID"), ""));
			param.put("formMap.STUD_ID", ObjectUtils.toString(params.get("studentId"), ""));

			String oclassUrl = AppConfig.getProperty("oclassUrl", "http://oclass.oucnet.cn");
			String jsonObject = HttpClientUtils.doHttpPost(oclassUrl + "/app/teacher/studDetailIndex.do", param, 10000,
					"UTF-8");

			String jsonObject1 = HttpClientUtils.doHttpPost(oclassUrl + "/app/teacher/getStudAnalysisDetail.do", param,
					10000, "UTF-8");

			Map map = (Map) JSON.parse(jsonObject);
			if (EmptyUtils.isNotEmpty(map)) {
				resultMap.putAll(map);
			}

			Map map1 = (Map) JSON.parse(jsonObject1);
			if (EmptyUtils.isNotEmpty(map1)) {
				resultMap.put("MY_POINT", ObjectUtils.toString(map1.get("MY_POINT"), ""));
				resultMap.put("MY_PROGRESS", ObjectUtils.toString(map1.get("MY_PROGRESS"), ""));
			}
		}
		return resultMap;
	}

	/**
	 * 查询学情
	 *
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	@Override
	public Page<Map<String, Object>> queryLearningSituations(Map<String, Object> searchParams, PageRequest pageRequst) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("xxId", ObjectUtils.toString(searchParams.get("xxId")));
		param.put("classId", ObjectUtils.toString(searchParams.get("classId")));

		sql.append("  SELECT * FROM (");
		sql.append("  	SELECT gsi.STUDENT_ID,gsi.XM,gsi.XH,gsi.SJH,gsi.MAJOR,GY.NAME,GG.GRADE_NAME,");
		sql.append(
				"  	(SELECT SUM(NVL(gsml.SCORE,0)) FROM GJT_SPECIALTY_MODULE_LIMIT gsml WHERE gsml.SPECIALTY_ID=gsi.MAJOR AND gsml.IS_DELETED='N') credits_min, ");
		sql.append("  	(	SELECT");
		sql.append("  			gs.zymc");
		sql.append("  		FROM");
		sql.append("  			GJT_SPECIALTY gs");
		sql.append("  		WHERE");
		sql.append("  			gs.IS_DELETED = 'N'");
		sql.append("  			AND gs.SPECIALTY_ID = gsi.MAJOR");
		sql.append("  	) ZYMC,");
		sql.append("  	(	SELECT");
		sql.append("  			TSD.NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  			AND TSD.CODE = gsi.PYCC");
		sql.append("  			AND TSD.TYPE_CODE = 'TrainingLevel'");
		sql.append("  	) PYCC_NAME,");
		sql.append("  	gsi.PYCC,");
		sql.append(
				"  	(SELECT ggg.grade_name FROM GJT_GRADE ggg WHERE ggg.IS_DELETED='N' AND  ggg.START_DATE<SYSDATE AND ggg.END_DATE>SYSDATE AND ggg.XX_ID=:xxId AND ggg.GRADE_ID=gg.GRADE_ID) current_term,");
		sql.append("  	gsi.NJ,");
		sql.append("  	gs.ZXF,");
		sql.append("  	SUM(NVL(grr.GET_CREDITS,0)) credits_count,");
		sql.append(
				"  	to_char(decode(SUM(NVL(grr.GET_CREDITS,0)),0,0,SUM(NVL(grr.GET_CREDITS,0))/NVL(gs.ZXF,0)*100),'FM990.90') credits_percent,");
		sql.append("  	COUNT(grr.COURSE_ID) course_count,");
		sql.append(
				"  	(SELECT COUNT(*) FROM GJT_REC_RESULT grr  LEFT JOIN VIEW_TEACH_PLAN gtp ON grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=gsi.STUDENT_ID AND grr.EXAM_STATE='1') course_finish,");
		sql.append(
				"  	to_char(((SELECT COUNT(*) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_TEACH_PLAN gtp ON grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=gsi.STUDENT_ID AND grr.EXAM_STATE='1')/COUNT(grr.COURSE_ID)*100),'FM990.90') course_percent,");
		sql.append("  	COUNT(grr.TEACH_PLAN_ID) exam_count,");
		sql.append(
				"  	(SELECT COUNT(*) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_TEACH_PLAN gtp ON grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=gsi.STUDENT_ID AND grr.EXAM_STATE IN ('0','1')) exam_finish,");
		sql.append(
				"  	to_char(((SELECT COUNT(*) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_TEACH_PLAN gtp ON grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID WHERE grr.IS_DELETED='N' AND grr.STUDENT_ID=gsi.STUDENT_ID AND grr.EXAM_STATE IN ('0','1'))/COUNT(grr.TEACH_PLAN_ID)*100),'FM990.90') exam_percent");
		sql.append("  	FROM GJT_CLASS_INFO gci");
		sql.append("  	LEFT JOIN GJT_CLASS_STUDENT gcs ON gci.CLASS_ID=gcs.CLASS_ID AND gcs.IS_DELETED='N'");
		sql.append("  	LEFT JOIN GJT_STUDENT_INFO gsi ON gcs.STUDENT_ID=gsi.STUDENT_ID AND gsi.IS_DELETED='N'");
		sql.append("  	LEFT JOIN GJT_GRADE gg ON gg.GRADE_ID=gcs.GRADE_ID AND gg.IS_DELETED='N' ");
		sql.append("  	LEFT JOIN GJT_YEAR gy ON gg.YEAR_ID=gy.GRADE_ID");
		sql.append("  	LEFT JOIN GJT_REC_RESULT grr ON gsi.STUDENT_ID=grr.STUDENT_ID");
		sql.append("  	LEFT JOIN GJT_SPECIALTY gs ON gsi.MAJOR=gs.SPECIALTY_ID");
		sql.append("  	WHERE gci.IS_DELETED = 'N'");
		sql.append("    AND grr.IS_DELETED = 'N'");
		sql.append("    AND gcs.IS_DELETED='N'");
		sql.append("    AND gsi.IS_DELETED='N'");
		sql.append("    AND gci.CLASS_ID= :classId ");
		sql.append(
				"  	GROUP BY gsi.STUDENT_ID,gsi.XM,gsi.XH,gsi.SJH,gsi.MAJOR,gsi.PYCC,gsi.NJ,gs.ZXF,gg.GRADE_ID,GY.NAME,GG.GRADE_NAME) temp WHERE 1=1 ");

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("xm")))) {
			sql.append("  	AND temp.XM LIKE :xm");
			param.put("xm", "%" + ObjectUtils.toString(searchParams.get("xm")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("xh")))) {
			sql.append("  	AND temp.XH LIKE :xh");
			param.put("xh", "%" + ObjectUtils.toString(searchParams.get("xh")) + "%");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("pycc")))) {
			sql.append("  	AND temp.PYCC = :pycc");
			param.put("pycc", ObjectUtils.toString(searchParams.get("pycc")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("specialtyId")))) {
			sql.append("  	AND temp.MAJOR = :specialtyId ");
			param.put("specialtyId", ObjectUtils.toString(searchParams.get("specialtyId")));
		}
		// 完成率
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("course_percent")))) {

			sql.append("  	AND temp.course_finish " + ObjectUtils.toString(searchParams.get("courseSign"), "=")
					+ " :course_percent");
			param.put("course_percent", ObjectUtils.toString(searchParams.get("course_percent")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("exam_percent")))) {
			sql.append("  	AND  temp.exam_percent " + ObjectUtils.toString(searchParams.get("examSign"), "=")
					+ " :exam_percent");
			param.put("exam_percent", ObjectUtils.toString(searchParams.get("exam_percent")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("credits_percent")))) {
			sql.append("  	AND temp.credits_percent " + ObjectUtils.toString(searchParams.get("creditsSign"), "=")
					+ " :credits_percent");
			param.put("credits_percent", ObjectUtils.toString(searchParams.get("credits_percent")));
		}

		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("passState")))) {
			if ("0".equals(ObjectUtils.toString(searchParams.get("passState"), ""))) {// 不满足
				sql.append(" AND  temp.credits_count < temp.credits_min");
			}
			if ("1".equals(ObjectUtils.toString(searchParams.get("passState"), ""))) {// 满足
				sql.append(" AND temp.credits_count >= temp.credits_min");
			}
		}

		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	/**
	 * 查询成绩与学分成绩总览
	 *
	 * @param seachParams
	 * @return
	 */
	@Override
	public Map<String, Object> queryResultsOverview(Map<String, Object> seachParams) {

		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		/**
		 * sql.append("  SELECT"); sql.append("  	STD_NAME,"); sql.append(
		 * "  	STD_ID,"); sql.append("  	STD_RANK,"); sql.append(
		 * "  	ORDER_CHANGE,"); sql.append("  	COUNT_COURSE,"); sql.append(
		 * "  	UNPASS_COURSE,"); sql.append("  	PASS_COURSE,"); sql.append(
		 * "  	LEARNING_COURSE,"); sql.append("  	REGING_COURSE, ");
		 * sql.append("  	WXI_COURSE, "); sql.append("  	SUM_XF, ");
		 * sql.append("  	GETED_XF, "); sql.append("  	ZDBYXF ");
		 * sql.append("  FROM"); sql.append("  	("); sql.append("  		SELECT"
		 * ); sql.append(
		 * "  			ROUND( DECODE( COUNT( gsr.SCHEDULE ), 0, 0, SUM( NVL( gsr.SCHEDULE, 0 ))/ COUNT( gsr.SCHEDULE )))|| '%' avg_progress,"
		 * ); sql.append("  			gsi.XM std_name,"); sql.append(
		 * "  			gsi.STUDENT_ID std_id,"); sql.append(
		 * "  			gpo.ORDER_CHANGE,"); sql.append(
		 * "  			(SELECT COUNT(*) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_TEACH_PLAN gtp ON grr.TEACH_PLAN_ID=gtp.TEACH_PLAN_ID WHERE grr.STUDENT_ID=gsi.STUDENT_ID AND grr.IS_DELETED='N'  AND gtp.IS_DELETED='N') COUNT_COURSE,"
		 * ); sql.append(
		 * "  			(SELECT COUNT(*) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_TEACH_PLAN gtp ON grr.TEACH_PLAN_ID=gtp.TEACH_PLAN_ID WHERE grr.STUDENT_ID=gsi.STUDENT_ID AND grr.IS_DELETED='N'  AND gtp.IS_DELETED='N' AND grr.EXAM_STATE='0') UNPASS_COURSE,"
		 * ); sql.append(
		 * "  			(SELECT COUNT(*) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_TEACH_PLAN gtp ON grr.TEACH_PLAN_ID=gtp.TEACH_PLAN_ID WHERE grr.STUDENT_ID=gsi.STUDENT_ID AND grr.IS_DELETED='N'  AND gtp.IS_DELETED='N' AND grr.EXAM_STATE='1') PASS_COURSE,"
		 * ); sql.append("  			("); sql.append("  				SELECT"
		 * ); sql.append("  					COUNT(*)"); sql.append(
		 * "  				FROM"); sql.append(
		 * "  					GJT_REC_RESULT grr"); sql.append(
		 * "  				LEFT JOIN VIEW_TEACH_PLAN gtp ON"); sql.append(
		 * "  					grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID");
		 * sql.append("  				LEFT JOIN GJT_GRADE GG1 ON");
		 * sql.append("  					gtp.ACTUAL_GRADE_ID = GG1.GRADE_ID"
		 * ); sql.append("  				WHERE"); sql.append(
		 * "  					grr.STUDENT_ID = gsi.STUDENT_ID"); sql.append(
		 * "  					AND grr.IS_DELETED = 'N'"); sql.append(
		 * "  					AND gtp.IS_DELETED = 'N'"); sql.append(
		 * "  					AND GG1.IS_DELETED = 'N'"); sql.append(
		 * "  					AND grr.EXAM_STATE = '2'"); sql.append(
		 * "  					AND SYSDATE > GG1.START_DATE"); sql.append(
		 * "  			) LEARNING_COURSE,"); //sql.append(
		 * "  			(SELECT COUNT(*) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_TEACH_PLAN gtp ON grr.TEACH_PLAN_ID=gtp.TEACH_PLAN_ID WHERE grr.STUDENT_ID=gsi.STUDENT_ID AND grr.IS_DELETED='N'  AND gtp.IS_DELETED='N' AND grr.EXAM_STATE='2') LEARNING_COURSE,"
		 * ); sql.append(
		 * "  			(SELECT COUNT(*) FROM GJT_REC_RESULT grr LEFT JOIN VIEW_TEACH_PLAN gtp ON grr.TEACH_PLAN_ID=gtp.TEACH_PLAN_ID WHERE grr.STUDENT_ID=gsi.STUDENT_ID AND grr.IS_DELETED='N'  AND gtp.IS_DELETED='N' AND grr.EXAM_STATE='3') REGING_COURSE,"
		 * ); sql.append("  			("); sql.append("  				SELECT"
		 * ); sql.append("  					COUNT(*)"); sql.append(
		 * "  				FROM"); sql.append(
		 * "  					GJT_REC_RESULT GRR"); sql.append(
		 * "  				LEFT JOIN VIEW_TEACH_PLAN GTP ON"); sql.append(
		 * "  					GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		 * sql.append("  				LEFT JOIN GJT_GRADE GG1 ON");
		 * sql.append("  					GTP.ACTUAL_GRADE_ID = GG1.GRADE_ID"
		 * ); sql.append("  				WHERE"); sql.append(
		 * "  					GRR.STUDENT_ID = GSI.STUDENT_ID"); sql.append(
		 * "  					AND GRR.IS_DELETED = 'N'"); sql.append(
		 * "  					AND GTP.IS_DELETED = 'N'"); sql.append(
		 * "  					AND GG1.IS_DELETED = 'N'"); sql.append(
		 * "  					AND SYSDATE < GG1.START_DATE"); sql.append(
		 * "  			) WXI_COURSE,"); sql.append("  			("); sql.append(
		 * "  				SELECT"); sql.append(
		 * "  					SUM( GTP.XF )"); sql.append(
		 * "  				FROM"); sql.append(
		 * "  					GJT_REC_RESULT GRR"); sql.append(
		 * "  				LEFT JOIN VIEW_TEACH_PLAN GTP ON"); sql.append(
		 * "  					GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		 * sql.append("  				LEFT JOIN GJT_GRADE GG1 ON");
		 * sql.append("  					GTP.ACTUAL_GRADE_ID = GG1.GRADE_ID"
		 * ); sql.append("  				WHERE"); sql.append(
		 * "  					GRR.STUDENT_ID = GSI.STUDENT_ID"); sql.append(
		 * "  					AND GRR.IS_DELETED = 'N'"); sql.append(
		 * "  					AND GTP.IS_DELETED = 'N'"); sql.append(
		 * "  					AND GG1.IS_DELETED = 'N'"); sql.append(
		 * "  			) SUM_XF,"); sql.append("  			("); sql.append(
		 * "  				SELECT"); sql.append(
		 * "  					NVL( SUM( GTP.XF ), 0 )"); sql.append(
		 * "  				FROM"); sql.append(
		 * "  					GJT_REC_RESULT GRR"); sql.append(
		 * "  				LEFT JOIN VIEW_TEACH_PLAN GTP ON"); sql.append(
		 * "  					GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		 * sql.append("  				LEFT JOIN GJT_GRADE GG1 ON");
		 * sql.append("  					GTP.ACTUAL_GRADE_ID = GG1.GRADE_ID"
		 * ); sql.append("  				WHERE"); sql.append(
		 * "  					GRR.STUDENT_ID = GSI.STUDENT_ID"); sql.append(
		 * "  					AND GRR.IS_DELETED = 'N'"); sql.append(
		 * "  					AND GTP.IS_DELETED = 'N'"); sql.append(
		 * "  					AND GG1.IS_DELETED = 'N'"); sql.append(
		 * "  					AND GRR.EXAM_STATE = '1'"); sql.append(
		 * "  			) GETED_XF,"); sql.append("  			("); sql.append(
		 * "  				SELECT"); sql.append("  					ZDBYXF"
		 * ); sql.append("  				FROM"); sql.append(
		 * "  					GJT_SPECIALTY GS,"); sql.append(
		 * "  					GJT_STUDENT_INFO GSI1"); sql.append(
		 * "  				WHERE"); sql.append(
		 * "  					GS.IS_DELETED = 'N'"); sql.append(
		 * "  					AND GSI1.IS_DELETED = 'N'"); sql.append(
		 * "  					AND GS.SPECIALTY_ID = GSI1.MAJOR"); sql.append(
		 * "  					AND GSI1.STUDENT_ID = GSI.STUDENT_ID");
		 * sql.append("  			) ZDBYXF,"); sql.append(
		 * "  			ROW_NUMBER() OVER("); sql.append(
		 * "  			ORDER BY"); sql.append(
		 * "  				ROUND( DECODE( COUNT( gsr.SCHEDULE ), 0, 0, SUM( NVL( gsr.SCHEDULE, 0 ))/ COUNT( gsr.SCHEDULE ))) DESC"
		 * ); sql.append("  			) std_rank"); sql.append(
		 * "  		FROM"); sql.append("  			GJT_STUDY_RECORD gsr");
		 * sql.append("  		LEFT JOIN VIEW_TEACH_PLAN gtp ON"); sql.append(
		 * "  			gsr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID"); sql.append(
		 * "  		LEFT JOIN GJT_REC_RESULT grr ON"); sql.append(
		 * "  			grr.TEACH_PLAN_ID = gtp.TEACH_PLAN_ID"); sql.append(
		 * "  		LEFT JOIN GJT_GRADE gg ON"); sql.append(
		 * "  			gtp.ACTUAL_GRADE_ID = gg.GRADE_ID,"); sql.append(
		 * "  			GJT_CLASS_STUDENT gcs,"); sql.append(
		 * "  			GJT_CLASS_INFO gci,"); sql.append(
		 * "  			GJT_STUDENT_INFO gsi "); sql.append(
		 * "  		LEFT JOIN GJT_PROGRESS_ORDER gpo ON"); sql.append(
		 * "  			gsi.STUDENT_ID=gpo.STUDENT_ID"); sql.append(
		 * "  		WHERE"); sql.append("  			gcs.CLASS_ID = gci.CLASS_ID"
		 * ); sql.append("  			AND gci.CLASS_TYPE = 'teach'");
		 * sql.append("  			AND gsi.STUDENT_ID = gcs.STUDENT_ID");
		 * sql.append("  			AND gsr.STUDENT_ID = gsi.STUDENT_ID");
		 * sql.append(
		 * "  			AND SYSDATE BETWEEN gg.START_DATE AND NVL( gg.END_DATE, ADD_MONTHS( gg.START_DATE, 4 ))"
		 * ); sql.append("  			AND gci.CLASS_ID= :CLASS_ID");
		 * sql.append("  		GROUP BY"); sql.append("  			gsi.XM,");
		 * sql.append("  			gsi.STUDENT_ID,"); sql.append(
		 * "  			gpo.ORDER_CHANGE"); sql.append("  		ORDER BY");
		 * sql.append(
		 * "  			ROUND( DECODE( COUNT( gsr.SCHEDULE ), 0, 0, SUM( NVL( gsr.SCHEDULE, 0 ))/ COUNT( gsr.SCHEDULE ))) DESC,"
		 * ); sql.append("  			gsi.XM"); sql.append("  	)");
		 * sql.append("  WHERE"); sql.append(
		 * "  	1=1 AND std_id = :STUDENT_ID");
		 * 
		 * params.put("CLASS_ID",ObjectUtils.toString(seachParams.get("CLASS_ID"
		 * ))); params.put("STUDENT_ID",ObjectUtils.toString(seachParams.get(
		 * "STUDENT_ID")));
		 */

		sql.append("  SELECT");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			NVL( SUM( GTP.XF ), 0 )");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("        LEFT JOIN GJT_GRADE GG ON ");
		sql.append("  	        GTP.ACTUAL_GRADE_ID = GG.GRADE_ID ");
		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");
		sql.append("  			AND GG.IS_DELETED = 'N' ");
		sql.append("  			AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  			AND GTP.XX_ID = GSI.XX_ID");
		sql.append("  	) SUM_XF,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			NVL( TO_CHAR( GS.ZDBYXF ), '--' )");
		sql.append("  		FROM");
		sql.append("  			GJT_SPECIALTY GS");
		sql.append("  		WHERE");
		sql.append("  			GS.IS_DELETED = 'N'");
		sql.append("  			AND GS.SPECIALTY_ID = GSI.MAJOR");
		sql.append("  	) ZDBYXF,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			NVL( SUM( GTP.XF ), 0 )");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("        LEFT JOIN GJT_GRADE GG ON ");
		sql.append("  	        GTP.ACTUAL_GRADE_ID = GG.GRADE_ID ");
		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");
		sql.append("  			AND GG.IS_DELETED = 'N' ");
		sql.append("  			AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  			AND GTP.XX_ID = GSI.XX_ID");
		sql.append("  			AND GRR.EXAM_STATE = '1'");
		sql.append("  	) GETED_XF,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT(*)");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("        LEFT JOIN GJT_GRADE GG ON ");
		sql.append("  	        GTP.ACTUAL_GRADE_ID = GG.GRADE_ID ");
		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");
		sql.append("  			AND GG.IS_DELETED = 'N' ");
		sql.append("  			AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  			AND GTP.XX_ID = GSI.XX_ID");
		sql.append("  	) COUNT_COURSE,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT(*)");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("        LEFT JOIN GJT_GRADE GG ON ");
		sql.append("  	        GTP.ACTUAL_GRADE_ID = GG.GRADE_ID ");
		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");
		sql.append("  			AND GG.IS_DELETED = 'N' ");
		sql.append("  			AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  			AND GTP.XX_ID = GSI.XX_ID");
		sql.append("  			AND GRR.EXAM_STATE = '1'");
		sql.append("  	) PASS_COURSE,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT(*)");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("        LEFT JOIN GJT_GRADE GG ON ");
		sql.append("  	        GTP.ACTUAL_GRADE_ID = GG.GRADE_ID ");
		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");
		sql.append("  			AND GG.IS_DELETED = 'N' ");
		sql.append("  			AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  			AND GTP.XX_ID = GSI.XX_ID");
		sql.append("  			AND GRR.EXAM_STATE = '0'");
		sql.append("  	) UNPASS_COURSE,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT(*)");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("  		LEFT JOIN GJT_GRADE GG ON");
		sql.append("  			GTP.ACTUAL_GRADE_ID = GG.GRADE_ID");
		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");
		sql.append("  			AND GG.IS_DELETED = 'N'");
		sql.append("  			AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  			AND GTP.XX_ID = GSI.XX_ID");
		sql.append("  			AND GRR.EXAM_STATE = '2'");
		sql.append("  			AND SYSDATE > GG.START_DATE");
		sql.append("  	) LEARNING_COURSE,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT(*)");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("  		LEFT JOIN GJT_GRADE GG ON");
		sql.append("  			GTP.ACTUAL_GRADE_ID = GG.GRADE_ID");
		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N'");
		sql.append("  			AND GC.IS_DELETED = 'N'");
		sql.append("  			AND GG.IS_DELETED = 'N'");
		sql.append("  			AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  			AND GTP.XX_ID = GSI.XX_ID");
		sql.append("  			AND SYSDATE < GG.START_DATE");
		sql.append("  	) WXI_COURSE,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			COUNT(*)");
		sql.append("  		FROM");
		sql.append("  			GJT_REC_RESULT GRR");
		sql.append("  		INNER JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  			GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  		INNER JOIN GJT_COURSE GC ON");
		sql.append("  			GTP.COURSE_ID = GC.COURSE_ID");
		sql.append("        LEFT JOIN GJT_GRADE GG ON ");
		sql.append("  	        GTP.ACTUAL_GRADE_ID = GG.GRADE_ID ");
		sql.append("  		WHERE");
		sql.append("  			GRR.IS_DELETED = 'N'");
		sql.append("  			AND GTP.IS_DELETED = 'N' ");
		sql.append("  			AND GC.IS_DELETED = 'N' ");
		sql.append("  			AND GG.IS_DELETED = 'N' ");
		sql.append("  			AND GRR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  			AND GTP.XX_ID = GSI.XX_ID");
		sql.append("  			AND GRR.EXAM_STATE = '3'");
		sql.append("  	) REGING_COURSE,");
		sql.append("  	A.STD_NAME,");
		sql.append("  	A.STD_ID,");
		sql.append("  	A.STD_RANK,");
		sql.append("  	A.ORDER_CHANGE");
		sql.append("  FROM");
		sql.append("  	GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN(");
		sql.append("  		SELECT");
		sql.append("  			STD_NAME,");
		sql.append("  			STD_ID,");
		sql.append("  			STD_RANK,");
		sql.append("  			ORDER_CHANGE");
		sql.append("  		FROM");
		sql.append("  			(");
		sql.append("  				SELECT");
		sql.append(
				"  					ROUND( DECODE( COUNT( GSR.PROGRESS ), 0, 0, SUM( NVL( GSR.PROGRESS, 0 ))/ COUNT( GSR.PROGRESS )))|| '%' AVG_PROGRESS,");
		sql.append("  					GSI.XM STD_NAME,");
		sql.append("  					GSI.STUDENT_ID STD_ID,");
		sql.append("  					GPO.ORDER_CHANGE,");
		sql.append("  					ROW_NUMBER() OVER(");
		sql.append("  					ORDER BY");
		sql.append(
				"  						ROUND( DECODE( COUNT( GSR.PROGRESS ), 0, 0, SUM( NVL( GSR.PROGRESS, 0 ))/ COUNT( GSR.PROGRESS ))) DESC");
		sql.append("  					) STD_RANK");
		sql.append("  				FROM");
		/*
		 * sql.append("  					GJT_STUDY_RECORD GSR");
		 */
		sql.append("  					VIEW_STUDENT_STUDY_SITUATION GSR"); // 从视图中查询学习进度排名
		sql.append("  				LEFT JOIN VIEW_TEACH_PLAN GTP ON");
		sql.append("  					GSR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  				LEFT JOIN GJT_REC_RESULT GRR ON");
		sql.append("  					GRR.TEACH_PLAN_ID = GTP.TEACH_PLAN_ID");
		sql.append("  				LEFT JOIN GJT_GRADE GG ON");
		sql.append("  					GTP.ACTUAL_GRADE_ID = GG.GRADE_ID,");
		sql.append("  					GJT_CLASS_STUDENT GCS,");
		sql.append("  					GJT_CLASS_INFO GCI,");
		sql.append("  					GJT_STUDENT_INFO GSI");
		sql.append("  				LEFT JOIN GJT_PROGRESS_ORDER GPO ON");
		sql.append("  					GSI.STUDENT_ID = GPO.STUDENT_ID");
		sql.append("  				WHERE");
		sql.append("  					GCS.CLASS_ID = GCI.CLASS_ID");
		sql.append("  					AND GCI.CLASS_TYPE = 'teach'");
		sql.append("  					AND GSI.STUDENT_ID = GCS.STUDENT_ID");
		sql.append("  					AND GSR.STUDENT_ID = GSI.STUDENT_ID");
		sql.append(
				"  					AND SYSDATE BETWEEN GG.START_DATE AND NVL( GG.END_DATE, ADD_MONTHS( GG.START_DATE, 4 ))");

		if (EmptyUtils.isNotEmpty(seachParams.get("CLASS_ID"))) {
			sql.append("  					AND GCI.CLASS_ID = :CLASS_ID ");
			params.put("CLASS_ID", ObjectUtils.toString(seachParams.get("CLASS_ID")));
		}

		sql.append("  				GROUP BY");
		sql.append("  					GSI.XM,");
		sql.append("  					GSI.STUDENT_ID,");
		sql.append("  					GPO.ORDER_CHANGE");
		sql.append("  				ORDER BY");
		sql.append(
				"  					ROUND( DECODE( COUNT( GSR.PROGRESS ), 0, 0, SUM( NVL( GSR.PROGRESS, 0 ))/ COUNT( GSR.PROGRESS ))) DESC,");
		sql.append("  					GSI.XM");
		sql.append("  			)");
		sql.append("  		WHERE");
		sql.append("  			1 = 1");

		if (EmptyUtils.isNotEmpty(seachParams.get("STUDENT_ID"))) {
			sql.append("  			AND STD_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(seachParams.get("STUDENT_ID")));
		}

		sql.append("  	) A ON");
		sql.append("  	A.STD_ID = GSI.STUDENT_ID");
		sql.append("  WHERE");
		sql.append("  	GSI.IS_DELETED = 'N'");

		if (EmptyUtils.isNotEmpty(seachParams.get("STUDENT_ID"))) {
			sql.append("  	AND GSI.STUDENT_ID = :STUDENT_ID ");
			params.put("STUDENT_ID", ObjectUtils.toString(seachParams.get("STUDENT_ID")));
		}

		Map<String, Object> result = commonDao.queryObjectToMapNative(sql.toString(), params);

		if (EmptyUtils.isNotEmpty(result)) {
			int rank = Integer.valueOf(ObjectUtils.toString(result.get("ORDER_CHANGE"), "0"));
			String CHANGE_RANK = rank > 0 ? "↑" + rank : "↓" + Math.abs(rank);
			if (rank == 0) {
				result.put("CHANGE_RANK", "");
			} else {
				result.put("CHANGE_RANK", CHANGE_RANK);
			}
		}

		return result;
	}

	/**
	 * 学员学情详情页-- 学分详情
	 *
	 * @param params
	 * @return
	 */
	@Override
	public List getCreditInfoAnd(Map<String, Object> params) {

		return this.getCreditDetail(params);
	}

	@Override
	public Boolean deleteByStudentId(String studentId) {
		if (studentId != null) {
			String memo = "学员转专业";
			gjtRecResultDao.deleteByStudentId(studentId, memo, "Y");
		}
		return true;
	}

	@Override
	public boolean updatePayState(String recId, String paySn) {
		return gjtRecResultDao.updatePayState(recId, paySn) == 1;
	}

	@Override
	public boolean updateRebuildState(String recId) {
		int i = gjtRecResultDao.updateRebuildState(recId);
		return i > 0;
	}

	@Override
	public long countByTermCourseId(String termcourseId) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
		filters.put("termcourseId", new SearchFilter("termcourseId", Operator.EQ, termcourseId));
		filters.put("isDeleted", new SearchFilter("isDeleted", Operator.EQ, Constants.BOOLEAN_NO));
		Specification<GjtRecResult> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtRecResult.class);
		return gjtRecResultDao.count(spec);
	}

	public List<Map<String, Object>> queryGradeCourseStudentNo(Map<String, Object> parm) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  select count(0) num, a.term_id,a.course_id,c.grade_name, B.kcmc");
		sql.append("  from gjt_rec_result a");
		sql.append("  inner join gjt_course b");
		sql.append("  on a.course_id = b.course_id");
		sql.append("  inner join gjt_grade c");
		sql.append("  on a.term_id = c.grade_id");
		sql.append("  where a.term_id in (:termIds)");
		sql.append("  and a.course_id in (:courseIds)");
		sql.append("  and a.is_deleted='N'");
		sql.append("  and b.is_deleted='N'");
		sql.append("  and c.is_deleted='N'");

		String courseName = (String) parm.get("courseName");
		if (StringUtils.isNotBlank(courseName)) {
			sql.append("  and b.kcmc like  :courseName ");
			params.put("courseName", "%" + courseName + "%");
		}

		params.put("termIds", parm.get("gradeId"));
		params.put("courseIds", parm.get("courseIds"));

		sql.append("  group by a.term_id, a.course_id, b.kcmc, c.grade_name");

		return commonDao.queryForMapList(sql.toString(), params);
	}
}
