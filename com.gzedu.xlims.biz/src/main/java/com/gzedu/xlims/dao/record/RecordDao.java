package com.gzedu.xlims.dao.record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;

@Repository
public class RecordDao {
	
	@Autowired
	private CommonDao commonDao;

	/**
	 * 查询每个院校的最后的考试计划
	 * @return
	 */
	public List getLastExamBatchList(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GEBN.EXAM_BATCH_ID, GEBN.EXAM_BATCH_CODE, GEBN.NAME EXAM_BATCH_NAME, GEBN.XX_ID");
		sql.append("  FROM GJT_EXAM_BATCH_NEW GEBN");
		sql.append("  WHERE GEBN.IS_DELETED = 0");
		sql.append("  AND (GEBN.XX_ID, GEBN.CREATED_DT) IN");
		sql.append("  (SELECT GEB.XX_ID, MAX(GEB.CREATED_DT) CREATED_DT");
		sql.append("  FROM GJT_EXAM_BATCH_NEW GEB");
		sql.append("  WHERE GEB.IS_DELETED = 0");
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))) {
			sql.append(" AND GEB.EXAM_BATCH_CODE=:EXAM_BATCH_CODE");
			param.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}
		
		sql.append("  GROUP BY GEB.XX_ID)");
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 查询预约统计记录
	 * @return
	 */
	public List getRecordAppointment(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT *");
		sql.append("  FROM GJT_RECORD_APPOINTMENT GRA");
		sql.append("  WHERE GRA.IS_DELETED = 'N'");
		sql.append("  AND GRA.STUDENT_ID = :STUDENT_ID");
		sql.append("  AND GRA.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
		sql.append("  AND GRA.EXAM_PLAN_ID = :EXAM_PLAN_ID");
		
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		param.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		param.put("EXAM_PLAN_ID", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), param);
	}
	
	/**
	 * 插入预约统计记录
	 * @return
	 */
	@Transactional
	public int addRecordAppointment(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT INTO GJT_RECORD_APPOINTMENT");
		sql.append("  (RECORD_APPOINTMENT_ID,");
		sql.append("  STUDENT_ID,");
		sql.append("  EXAM_BATCH_CODE,");
		sql.append("  EXAM_PLAN_ID,");
		sql.append("  REC_ID,");
		sql.append("  BESPEAK_STATE,");
		sql.append("  EXAM_PLAN_LIMIT)");
		sql.append("  VALUES");
		sql.append("  (");
		sql.append("  :RECORD_APPOINTMENT_ID,");
		sql.append("  :STUDENT_ID,");
		sql.append("  :EXAM_BATCH_CODE,");
		sql.append("  :EXAM_PLAN_ID,");
		sql.append("  :REC_ID,");
		sql.append("  :BESPEAK_STATE,");
		sql.append("  :EXAM_PLAN_LIMIT   ");
		sql.append("  )");

		param.put("RECORD_APPOINTMENT_ID", ObjectUtils.toString(searchParams.get("RECORD_APPOINTMENT_ID")));
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		param.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		param.put("EXAM_PLAN_ID", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));
		param.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		param.put("BESPEAK_STATE", ObjectUtils.toString(searchParams.get("BESPEAK_STATE")));
		param.put("EXAM_PLAN_LIMIT", ObjectUtils.toString(searchParams.get("EXAM_PLAN_LIMIT")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 修改预约统计记录
	 * @return
	 */
	@Transactional
	public int updRecordAppointment(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_RECORD_APPOINTMENT GRA");
		sql.append("  SET GRA.BESPEAK_STATE   = :BESPEAK_STATE,");
		sql.append("  GRA.EXAM_PLAN_LIMIT = :EXAM_PLAN_LIMIT,");
		sql.append("  GRA.REC_ID = :REC_ID,");
		sql.append("  GRA.UPDATED_DT      = SYSDATE");
		sql.append("  WHERE GRA.STUDENT_ID = :STUDENT_ID");
		sql.append("  AND GRA.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
		sql.append("  AND GRA.EXAM_PLAN_ID = :EXAM_PLAN_ID");

		param.put("BESPEAK_STATE", ObjectUtils.toString(searchParams.get("BESPEAK_STATE")));
		param.put("EXAM_PLAN_LIMIT", ObjectUtils.toString(searchParams.get("EXAM_PLAN_LIMIT")));
		param.put("REC_ID", ObjectUtils.toString(searchParams.get("REC_ID")));
		param.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		param.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		param.put("EXAM_PLAN_ID", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));

		return commonDao.updateForMapNative(sql.toString(), param);
	}
	
	/**
	 * 删除预约统计记录
	 * @return
	 */
	@Transactional
	public int delRecordAppointment(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  UPDATE GJT_RECORD_APPOINTMENT GRA");
		sql.append("  SET GRA.IS_DELETED = 'Y'");
		sql.append("  WHERE GRA.IS_DELETED = 'N'");
		sql.append("  AND GRA.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");

		param.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		
		return commonDao.updateForMapNative(sql.toString(), param);
	}

}
