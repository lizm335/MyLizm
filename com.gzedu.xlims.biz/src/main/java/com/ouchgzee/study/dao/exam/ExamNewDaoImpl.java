/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.dao.exam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;

/**
 * 新的考试操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年11月01日
 * @version 3.0
 * @since JDK 1.7
 */
@Repository
@Transactional(readOnly = true)
public class ExamNewDaoImpl extends BaseDaoImpl {

	private static Logger log = LoggerFactory.getLogger(ExamNewDaoImpl.class);

	/**
	 * 我的预约考试
	 * @param searchParams
	 * @return
	 */
	public List<Map> myExamList(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder sql = new StringBuilder("");
		sql.append("  select (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'KKXQ' AND TSD.CODE = e.KKXQ) TERM_NAME,");
		sql.append("  f.grade_id TERM_ID,f.grade_name GRADE_NAME,f.grade_code TERM_CODE,");
		sql.append("  t.exam_plan_id,c.rec_id,c.rec_id CHOOSE_ID,e.teach_plan_id,c.course_id,d.KCH,d.kcmc COURSE_NAME,");
		sql.append("  e.SOURCE_KCH \"sourceCourseKch\",e.SOURCE_KCMC \"sourceCourseKcmc\",");
		sql.append("  (case when e.SOURCE_COURSE_ID!=e.COURSE_ID then e.KCH else null end) \"courseKch\",(case when e.SOURCE_COURSE_ID!=e.COURSE_ID then e.kcmc else null end) \"courseKcmc\",");
		sql.append("  c.EXAM_SCORE NEW_EXAM_SCORE,c.EXAM_SCORE1,c.EXAM_STATE RESULT_STATE,");
		sql.append("  NVL(T.EXAM_SCORE,'') EXAM_SCORE,");
		sql.append("  b.type KSFS_FLAG,b.exam_no,");
		sql.append("  (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE TSD.IS_DELETED = 'N' AND TSD.TYPE_CODE = 'ExaminationMode' AND TSD.CODE = b.TYPE) EXAM_STYLE,");
		sql.append("  TO_CHAR(b.EXAM_ST, 'yyyy-MM-dd hh24:mi') EXAM_STIME,TO_CHAR(b.EXAM_END, 'yyyy-MM-dd hh24:mi') EXAM_ETIME,");
		sql.append("  NVL(t.exam_status,0) EXAM_STATE,");
		sql.append("  NVL(t.EXAM_COUNT,0) EXAM_COUNT,");
		sql.append("  (case when SYSDATE > b.EXAM_END  then '2' else '1' end) EXAM_TIME_STATE,");
		sql.append("  (select decode(count(*),0,0,1) from gjt_exam_correct_papers x where x.is_deleted='N' and x.student_id=t.student_id and x.exam_plan_id=t.exam_plan_id) IS_UPLOAD");
		sql.append("  from gjt_exam_appointment_new t ");
		sql.append("  inner join GJT_EXAM_BATCH_NEW a on a.exam_batch_code=t.exam_batch_code");
		sql.append("  inner join gjt_exam_plan_new b on b.exam_plan_id=t.exam_plan_id");
		sql.append("  inner join gjt_rec_result c on c.rec_id = t.rec_id");
		sql.append("  inner join gjt_course d on d.course_id = c.course_id");
		sql.append("  inner join VIEW_TEACH_PLAN e on e.teach_plan_id=c.teach_plan_id");
		sql.append("  inner join gjt_grade f on f.grade_id=e.actual_grade_id");
		sql.append("  where t.is_deleted=0");
		sql.append("  and t.student_id=:studentId");

		// 学员ID
		parameters.put("studentId", searchParams.get("STUDENT_ID"));
		// 考试计划编号
		if (StringUtils.isNotBlank(searchParams.get("EXAM_BATCH_CODE"))) {
			sql.append("  and t.exam_batch_code=:examBatchCode");
			parameters.put("examBatchCode", searchParams.get("EXAM_BATCH_CODE"));
		}

		sql.append("  order by f.START_DATE,e.COURSE_ID");
		long beginTime = System.currentTimeMillis();
		try {
			return super.findAllByToMap(sql, parameters, null);
		} finally {
			// 计算执行当前sql耗时时长
			log.info(String.format("function myExamList select use time:%1$sms, sql:%2$s, parameters:%3$s",
					System.currentTimeMillis() - beginTime,
					sql,
					parameters));
		}
	}

	/**
	 * 获取应该统计考试情况的学员
	 * @param examBatchCode
	 * @param searchParams xxId-院校ID
	 * @return
	 */
	public List<Map> getExamStudentList(String examBatchCode, Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder sql = new StringBuilder("");
		sql.append("  SELECT ");
		sql.append("  distinct b.STUDENT_ID,b.USER_TYPE,b.sfzh,b.XJZT,b.major,b.xx_id,b.xh,b.xm,b.PYCC,b.SJH,c.grade_name,c.ZYMC");
		sql.append("  FROM gjt_student_info b");
		sql.append("  LEFT JOIN view_student_info c ON c.STUDENT_ID = b.student_id");
		sql.append("  left join gjt_exam_appointment_new d on d.student_id=b.student_id and d.is_deleted=0 and d.exam_batch_code=:examBatchCode");
		sql.append("  WHERE b.is_deleted='N'");
		sql.append("  AND B.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
		sql.append("  and (b.xjzt in ('0','2','3') or d.appointment_id is not null)");

		// 考试计划编号
		parameters.put("examBatchCode", examBatchCode);
		// 院校ID
		parameters.put("xxId", searchParams.get("xxId"));
		return super.findAllByToMap(sql, parameters, null);
	}
	
	/**
	 * 获取该考生未获取成绩的科目
	 * @param formMap
	 * @param searchParams STUDENT_ID
	 * @return
	 */
	public List<Map> getNoExamScoreList(Map formMap){
		Map<String, Object> parameters = new HashMap();
		StringBuilder sql = new StringBuilder("");
		sql.append("  SELECT GEP.EXAM_NO, B.EXAM_BATCH_CODE, GEA.STUDENT_ID, GSI.XH, GSI.SFZH, GEA.APPOINTMENT_ID");
		sql.append("  FROM GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  LEFT JOIN GJT_EXAM_PLAN_NEW GEP");
		sql.append("  ON GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  LEFT JOIN GJT_STUDENT_INFO GSI");
		sql.append("  ON GSI.STUDENT_ID = GEA.STUDENT_ID");
		sql.append("  LEFT JOIN (SELECT MAX(GEAN.EXAM_BATCH_CODE) EXAM_BATCH_CODE, GEAN.TYPE");
		sql.append("  FROM GJT_EXAM_APPOINTMENT_NEW GEAN");
		sql.append("  GROUP BY GEAN.TYPE) B");
		sql.append("  ON B.TYPE = GEP.TYPE");
		sql.append("  WHERE GEA.IS_DELETED = '0'");
		sql.append("  AND GEP.IS_DELETED = '0'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GEP.TYPE IN ('7', '19')");
		sql.append("  AND GEA.STUDENT_ID = :STUDENT_ID");
		
		parameters.put("STUDENT_ID", ObjectUtils.toString(formMap.get("STUDENT_ID")));
		return super.findAllByToMap(sql, parameters, null);
		
	}
	
}
