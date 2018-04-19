package com.gzedu.xlims.dao.exam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.exam.repository.GjtExamStudentRoomNewRepository;
import com.gzedu.xlims.pojo.exam.GjtExamStudentRoomNew;

@Repository
public class GjtExamStudentRoomNewDao {

	@Autowired
	GjtExamStudentRoomNewRepository gjtExamStudentRoomNewRepository;

	@Autowired
	private CommonDao commonDao;

	public Page<GjtExamStudentRoomNew> findAll(Specification<GjtExamStudentRoomNew> spec, PageRequest pageRequst) {
		return gjtExamStudentRoomNewRepository.findAll(spec, pageRequst);
	}

	public List<GjtExamStudentRoomNew> findAll(Specification<GjtExamStudentRoomNew> spec) {
		return gjtExamStudentRoomNewRepository.findAll(spec);
	}

	// 获得当前考试批次，考点的考试计划科目ID列表
	public List<String> groupExamPlanIdByExamBatchCodeAndExamPointId(String examBatchCode, String examPointId,
			int examType) {
		return gjtExamStudentRoomNewRepository.groupExamPlanIdByExamBatchCodeAndExamPointId(examBatchCode, examPointId,
				examType);
	}

	public List<GjtExamStudentRoomNew> findByExamBatchCodeAndExamPointIdAndExamTypeOrderByExamPlanIdExamRoomIdSeatNo(
			String examBatchCode, String examPointId, int examType) {
		return gjtExamStudentRoomNewRepository
				.findByExamBatchCodeAndExamPointIdAndExamTypeOrderByExamPlanIdAndExamRoomIdAndSeatNo(examBatchCode,
						examPointId, examType);
	}

	public List<GjtExamStudentRoomNew> findAll(Specification<GjtExamStudentRoomNew> spec, Sort sort) {
		return gjtExamStudentRoomNewRepository.findAll(spec, sort);
	}

	public GjtExamStudentRoomNew save(GjtExamStudentRoomNew entity) {
		return gjtExamStudentRoomNewRepository.save(entity);
	}

	public void save(List<GjtExamStudentRoomNew> entitys) {
		gjtExamStudentRoomNewRepository.save(entitys);
	}

	public void delete(String id) {
		gjtExamStudentRoomNewRepository.delete(id);
	}

	public void delete(List<String> ids) {
		for (String id : ids) {
			gjtExamStudentRoomNewRepository.delete(id);
		}
	}
	
	public GjtExamStudentRoomNew findByStudentIdAndExamPlanId(String studentId, String examPlanId) {
		return gjtExamStudentRoomNewRepository.findByStudentIdAndExamPlanId(studentId, examPlanId);
	}
	
	public void delete(GjtExamStudentRoomNew entity) {
		gjtExamStudentRoomNewRepository.delete(entity);
	}
	
	public GjtExamStudentRoomNew findByExamPlanIdAndExamRoomIdAndSeatNo(String examPlanId, String examRoomId, int seatNo) {
		return gjtExamStudentRoomNewRepository.findByExamPlanIdAndExamRoomIdAndSeatNo(examPlanId, examRoomId, seatNo);
	}
	
	/**
	 * 查询准考证信息（临时）
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> findAdmissionTickets(String orgId, Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> param = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select ")
			.append("     gat.ID, ")
			.append("     gsi.XH, ")
			.append("     gsi.XM, ")
			.append("     gat.EXAM_NO, ")
			.append("     gat.COURSE_NAME, ")
			.append("     gat.EXAM_TYPE, ")
			.append("     gat.EXAM_DATE, ")
			.append("     gat.EXAM_TIME, ")
			.append("     gat.EXAM_SEAT_NUMBER, ")
			.append("     gat.EXAM_PONIT, ")
			.append("     gat.EXAM_POINT_ADDRESS ")
			.append("from ")
			.append("     GJT_STUDENT_INFO gsi, ")
			.append("     GJT_ADMISSION_TICKET gat ")
			.append("where ")
			.append("     gsi.STUDENT_ID = gat.STUDENT_ID ")
			.append("     and gat.IS_DELETED = 'N' ")
			.append("     and gsi.XXZX_ID in ( ")
			.append("                          select ID from GJT_ORG where IS_DELETED = 'N' start with ID = :orgId connect by prior ID = PERENT_ID ")
			.append("                         ) ");
		
		param.put("orgId", orgId);
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("LIKE_xh")))) {
			sql.append("  and gsi.XH = :xh ");
			param.put("xh", ObjectUtils.toString(searchParams.get("LIKE_xh")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("LIKE_xm")))) {
			sql.append("  and gsi.XM like :xm ");
			param.put("xm", "%" + ObjectUtils.toString(searchParams.get("LIKE_xm")) + "%");
		}
		
		sql.append("  order by gat.EXAM_DATE desc, gat.EXAM_TIME desc ");
		
		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}
	
	/**
	 * 查询排考信息
	 */
	public Page getExamStudentRoomList(Map formMap, PageRequest pageRequst) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT GSI.XM,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.SJH,");
		sql.append("  GSI.PYCC,");
		sql.append("  GSY.ZYMC,");
		sql.append("  GYR.NAME YEAR_NAME,");
		sql.append("  GRE.GRADE_NAME,");
		sql.append("  GEB.NAME EXAM_BATCH_NAME,");
		sql.append("  GEP.EXAM_PLAN_NAME,");
		sql.append("  GEP.TYPE EXAM_TYPE,");
		sql.append("  GEP.EXAM_NO,");
		sql.append("  GEB.EXAM_BATCH_CODE,");
		sql.append("  (SELECT GEPN.NAME");
		sql.append("  FROM GJT_EXAM_POINT_APPOINTMENT_NEW GEPA, GJT_EXAM_POINT_NEW GEPN");
		sql.append("  WHERE GEPA.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GEPA.EXAM_POINT_ID = GEPN.EXAM_POINT_ID");
		sql.append("  AND GEPA.GRADE_ID = GEB.GRADE_ID");
		sql.append("  AND ROWNUM = 1) APP_EXAM_POINT_NAME,");
		sql.append("  GEN.NAME EXAM_POINT_NAME,");
		sql.append("  GER.NAME EXAM_ROOM_NAME,");
		sql.append("  GES.SEAT_NO,");
		sql.append("  GEA.STATUS,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.TYPE_CODE = 'TrainingLevel'");
		sql.append("  AND TSD.CODE = GSI.PYCC) PYCC_NAME,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.IS_DELETED = 'N'");
		sql.append("  AND TSD.TYPE_CODE = 'ExaminationMode'");
		sql.append("  AND TSD.CODE = GEP.TYPE) EXAM_TYPE_NAME");
		sql.append("  FROM GJT_STUDENT_INFO         GSI,");
		sql.append("  GJT_CLASS_INFO           GCI,");
		sql.append("  GJT_CLASS_STUDENT        GCS,");
		sql.append("  GJT_GRADE                GRE,");
		sql.append("  GJT_YEAR                 GYR,");
		sql.append("  GJT_SPECIALTY            GSY,");
		sql.append("  GJT_EXAM_PLAN_NEW        GEP,");
		sql.append("  GJT_EXAM_BATCH_NEW       GEB,");
		sql.append("  GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  LEFT JOIN GJT_EXAM_STUDENT_ROOM_NEW GES");
		sql.append("  ON GEA.APPOINTMENT_ID = GES.APPOINTMENT_ID");
		sql.append("  LEFT JOIN GJT_EXAM_POINT_NEW GEN");
		sql.append("  ON GES.EXAM_POINT_ID = GEN.EXAM_POINT_ID");
		sql.append("  LEFT JOIN GJT_EXAM_ROOM_NEW GER");
		sql.append("  ON GES.EXAM_ROOM_ID = GER.EXAM_ROOM_ID");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GRE.IS_DELETED = 'N'");
		sql.append("  AND GSY.IS_DELETED = 'N'");
		sql.append("  AND GCI.IS_DELETED = 'N'");
		sql.append("  AND GCS.IS_DELETED = 'N'");
		sql.append("  AND GEP.IS_DELETED = 0");
		sql.append("  AND GEB.IS_DELETED = 0");
		sql.append("  AND GEA.IS_DELETED = 0");
		sql.append("  AND GEP.TYPE in ('8','11')");
		sql.append("  AND GCI.CLASS_TYPE = 'teach'");
		sql.append("  AND GCI.CLASS_ID = GCS.CLASS_ID");
		sql.append("  AND GCS.STUDENT_ID = GSI.STUDENT_ID");
		sql.append("  AND GSI.NJ = GRE.GRADE_ID");
		sql.append("  AND GRE.YEAR_ID = GYR.GRADE_ID");
		sql.append("  AND GSI.MAJOR = GSY.SPECIALTY_ID");
		sql.append("  AND GSI.STUDENT_ID = GEA.STUDENT_ID");
		sql.append("  AND GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  AND GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(formMap.get("XM")))) {
			sql.append("  AND GSI.XM LIKE '%"+ObjectUtils.toString(formMap.get("XM")).trim()+"%'");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(formMap.get("XH")))) {
			sql.append("  AND GSI.XH LIKE '%"+ObjectUtils.toString(formMap.get("XH")).trim()+"%'");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(formMap.get("EXAM_NO")))) {
			sql.append("  AND GEP.EXAM_NO LIKE '%"+ObjectUtils.toString(formMap.get("EXAM_NO")).trim()+"%'");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(formMap.get("EXAM_BATCH_CODE")))) {
			sql.append("  AND GEB.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
			param.put("EXAM_BATCH_CODE", ObjectUtils.toString(formMap.get("EXAM_BATCH_CODE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(formMap.get("EXAM_PLAN_NAME")))) {
			sql.append("  AND GEP.EXAM_PLAN_NAME LIKE '%"+ObjectUtils.toString(formMap.get("EXAM_PLAN_NAME")).trim()+"%'");
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(formMap.get("TYPE")))) {
			sql.append("  AND GEP.TYPE = :TYPE");
			param.put("TYPE", ObjectUtils.toString(formMap.get("TYPE")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(formMap.get("STATUS")))) {
			sql.append("  AND GEA.STATUS = :STATUS");
			param.put("STATUS", ObjectUtils.toString(formMap.get("STATUS")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(formMap.get("CLASS_ID")))) {
			sql.append("  AND GCI.CLASS_ID = :CLASS_ID");
			param.put("CLASS_ID", ObjectUtils.toString(formMap.get("CLASS_ID")));
		}
		
		sql.append("  AND GSI.XX_ID = :XX_ID");
		param.put("XX_ID", ObjectUtils.toString(formMap.get("XX_ID")));
		
		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

}
