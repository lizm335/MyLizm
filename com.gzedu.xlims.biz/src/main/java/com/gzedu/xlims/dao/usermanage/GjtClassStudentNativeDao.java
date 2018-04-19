package com.gzedu.xlims.dao.usermanage;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.comm.CommonDao;

@Repository
public class GjtClassStudentNativeDao {
	
	@Autowired
	private CommonDao commonDao;
	
	/**
	 * 查询班级学员
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryClassStudentInfo(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ")
			.append("     t.STUDENT_ID as \"studentId\",")
			.append("     t.ZP as \"zp\",")
			.append("     t.AVATAR as \"avatar\",")
			.append("     t.XH as \"studentCode\",")
			.append("     t.XM as \"studnetName\",")
			.append("     t.ORG_NAME as \"studyCenter\",")
			.append("     t.SC_CO as \"co\",")
			.append("     t.KCH as \"courseCode\",")
			.append("     t.KCMC as \"courseName\",")
			.append("     t.BJMC as \"className\",")
			.append("     t.EXAM_SCORE as \"examScore\",")
			.append("     t.SCORE as \"score\",")
			.append("     t.PROGRESS as \"progress\",")
			.append("     t.LOGIN_TIMES  as \"loginTimes\",")
			.append("     t.ONLINE_TIME as \"loginLength\",")
			.append("     t.EXAM_STATE as \"states\"")
			.append(" from ")
			.append("      ( ")
			.append("        select")
			.append("              gsi.STUDENT_ID, ")
			.append("              gsi.ZP, ")
			.append("              gsi.AVATAR, ")
			.append("              gsi.XH, ")
			.append("              gsi.XM, ")
			.append("              gsi.SC_CO, ")
			.append("              gc.KCH, ")
			.append("              gc.KCMC, ")
			.append("              gci.BJMC, ")
			.append("              gci.COURSE_ID, ")
			.append("              gci.TEACH_PLAN_ID, ")
			.append("     		   vsss.EXAM_SCORE,")
			.append("			   vsss.SCORE,")
			.append("    		   vsss.PROGRESS,")
			.append("     		   vsss.LOGIN_TIMES,")
			.append("     		   vsss.ONLINE_TIME,")
			.append("              vsss.EXAM_STATE,")
			.append("              o.ORG_NAME ")
			.append("        from")
			.append("              GJT_CLASS_STUDENT gcs")
			.append("        inner join GJT_STUDENT_INFO gsi on gsi.STUDENT_ID = gcs.STUDENT_ID and gsi.is_deleted='N'")
			.append("        inner join GJT_CLASS_INFO gci on gcs.CLASS_ID = gci.CLASS_ID and gci.is_deleted='N'")
			.append("        inner join GJT_COURSE gc on gc.COURSE_ID = gci.COURSE_ID and gc.is_deleted='N'")
			.append("        inner join VIEW_STUDENT_STUDY_SITUATION vsss on vsss.STUDENT_ID=gsi.student_id  and vsss.TERMCOURSE_ID=gci.termcourse_id")
			.append("        left join GJT_ORG o on o.ID = gsi.XXZX_ID")
			.append("        where gcs.IS_DELETED = 'N' ");
		
		Object classId = searchParams.get("EQ_classId");
		if (classId != null && StringUtils.isNotBlank((String)classId)) {
			sb.append(" and gci.CLASS_ID = :classId ");
			map.put("classId", classId);
		}
		
		Object xh = searchParams.get("LIKE_gjtStudentInfo.xh");
		if (xh != null && StringUtils.isNotBlank((String)xh)) {
			sb.append(" and gsi.XH = :xh ");
			map.put("xh", xh);
		}
		
		Object xm = searchParams.get("LIKE_gjtStudentInfo.xm");
		if (xm != null && StringUtils.isNotBlank((String)xm)) {
			sb.append(" and gsi.XM like :xm ");
			map.put("xm", "%" + xm + "%");
		}
		
		Object xxzx = searchParams.get("LIKE_xxzx");
		if (xxzx != null && StringUtils.isNotBlank((String)xxzx)) {
			sb.append(" and o.ORG_NAME like :xxzx ");
			map.put("xxzx", "%" + xxzx + "%");
		}
		
		Object scco = searchParams.get("LIKE_scco");
		if (scco != null && StringUtils.isNotBlank((String)scco)) {
			sb.append(" and gsi.SC_CO like :scco ");
			map.put("scco", "%" + scco + "%");
		}
		
		Object status = searchParams.get("EQ_status");
		if (status != null && StringUtils.isNotBlank((String)status)) {
			if ("1".equals(status.toString())) {  //已通过
				sb.append(" and vsss.EXAM_STATE = 1 ");
			} else if ("2".equals(status.toString())) {  //未通过
				sb.append(" and vsss.EXAM_STATE != 1 ");
			}
		}
		
		sb.append("          ) t ");
		sb.append(" order by t.XM");

		return commonDao.queryForPageNative(sb.toString(), map, pageRequst);
	}

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月18日 下午3:38:28
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryTermCourseStudentList(Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select gsi.student_id \"studentId\",");
		sql.append("  gsi.zp \"zp\",");
		sql.append("  gsi.xm \"xm\",");
		sql.append("  gsi.xh \"xh\",");
		sql.append("  gsi.sc_co \"scco\",");
		sql.append("  gs.zymc \"zymc\",");
		sql.append("  gs.pycc \"pycc\",");
		sql.append("  gy.name \"yearName\",");
		sql.append("  gg.grade_name \"gradeName\",");
		sql.append("  gsc.sc_name \"scName\",");
		sql.append("  gc.kcmc \"courseName\",");
		sql.append("  gc.kch \"courseCode\",");
		sql.append("  grr.exam_score \"examScore\",");
		sql.append("  grr.EXAM_STATE \"status\",");
		sql.append("  lud.my_point \"score\",");
		sql.append("  lud.my_progress \"progress\",");
		sql.append("  (nvl(luo.online_count, 0) + nvl(luo.online_count_mobile, 0)) \"loginTimes\",");
		sql.append("  (nvl(luo.online_time, 0) + nvl(luo.online_time_mobile, 0)) \"loginLength\"");
		sql.append("  from gjt_term_courseinfo gtc,");
		sql.append("  gjt_course          gc,");
		sql.append("  gjt_specialty       gs,");
		sql.append("  gjt_grade           gg,");
		sql.append("  gjt_year            gy,");
		sql.append("  gjt_student_info    gsi,");
		sql.append("  gjt_study_center    gsc,");
		sql.append("  gjt_rec_result      grr");
		sql.append("  left join lcms_user_dyna lud");
		sql.append("  on grr.rec_id = lud.choose_id");
		sql.append("  left join lcms_user_onlinetime luo");
		sql.append("  on grr.rec_id = luo.choose_id");
		sql.append("  where gsi.is_deleted = 'N'");
		sql.append("  and grr.is_deleted = 'N'");
		sql.append("  and gtc.is_deleted = 'N'");
		sql.append("  and gc.is_deleted = 'N'");
		sql.append("  and gs.is_deleted = 'N'");
		sql.append("  and gg.is_deleted = 'N'");
		sql.append("  and gtc.termcourse_id = grr.termcourse_id");
		sql.append("  and grr.student_id = gsi.student_id");
		sql.append("  and gtc.course_id = gc.course_id");
		sql.append("  and gsi.major = gs.specialty_id");
		sql.append("  and grr.term_id = gg.grade_id");
		sql.append("  and gsi.xxzx_id = gsc.id");
		sql.append("  and gg.year_id = gy.grade_id");

		Object termCourseId = searchParams.get("EQ_termCourseId");
		if (StringUtils.isNotBlank((String) termCourseId)) {
			sql.append("  and gtc.termcourse_id = :termCourseId");
			map.put("termCourseId", termCourseId);
		}

		Object xh = searchParams.get("LIKE_xh");
		if (xh != null && StringUtils.isNotBlank((String) xh)) {
			sql.append(" and gsi.xh = :xh ");
			map.put("xh", xh);
		}

		Object xm = searchParams.get("LIKE_xm");
		if (xm != null && StringUtils.isNotBlank((String) xm)) {
			sql.append(" and gsi.xm like :xm ");
			map.put("xm", "%" + xm + "%");
		}

		Object pycc = searchParams.get("EQ_pycc");
		if (StringUtils.isNotBlank((String) pycc)) {
			sql.append("  and gs.pycc = :pycc");
			map.put("pycc", pycc);
		}

		Object zymc = searchParams.get("LIKE_zymc");
		if (StringUtils.isNotBlank((String) zymc)) {
			sql.append("  and gs.zymc like :zymc");
			map.put("zymc", "%" + zymc + "%");
		}

		Object gradeId = searchParams.get("EQ_gradeId");
		if (StringUtils.isNotBlank((String) gradeId)) {
			sql.append("  and gg.grade_id = :gradeId");
			map.put("gradeId", gradeId);
		}

		Object xxzx = searchParams.get("LIKE_xxzx");
		if (xxzx != null && StringUtils.isNotBlank((String) xxzx)) {
			sql.append(" and gsc.sc_name like :xxzx ");
			map.put("xxzx", "%" + xxzx + "%");
		}

		Object scco = searchParams.get("LIKE_scco");
		if (scco != null && StringUtils.isNotBlank((String) scco)) {
			sql.append(" and gsi.sc_co like :scco ");
			map.put("scco", "%" + scco + "%");
		}

		Object status = searchParams.get("EQ_status");
		if (status != null && StringUtils.isNotBlank((String) status)) {
			if ("1".equals(status.toString())) { // 已通过
				sql.append(" and grr.EXAM_STATE = 1 ");
			} else if ("2".equals(status.toString())) { // 未通过
				sql.append(" and grr.EXAM_STATE != 1 ");
			}
		}



		return commonDao.queryForPageNative(sql.toString(), map, pageRequst);
	}

}
