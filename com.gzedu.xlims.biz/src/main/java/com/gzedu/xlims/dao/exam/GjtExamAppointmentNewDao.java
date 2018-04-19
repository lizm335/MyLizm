package com.gzedu.xlims.dao.exam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.MatchMode;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.exam.repository.GjtExamAppointmentNewRepository;
import com.gzedu.xlims.dao.exam.repository.GjtExamPointAppointmentNewRepository;
import com.gzedu.xlims.dao.exam.repository.GjtExamPointNewRepository;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import com.gzedu.xlims.pojo.GjtStudyYearInfo;
import com.gzedu.xlims.pojo.exam.GjtExamAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;
import com.gzedu.xlims.pojo.exam.GjtExamPointAppointmentNew;
import com.gzedu.xlims.pojo.exam.GjtExamPointNew;

@Repository
public class GjtExamAppointmentNewDao extends BaseDaoImpl {

	private static Logger log = LoggerFactory.getLogger(GjtExamAppointmentNewDao.class);

	@Autowired
	private GjtExamAppointmentNewRepository gjtExamAppointmentNewRepository;

	@Autowired
	private GjtExamPointNewRepository gjtExamPointNewRepository;

	@Autowired
	private GjtExamPointAppointmentNewRepository gjtExamPointAppointmentNewRepository;

	@Autowired
	private CommonDao commonDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;

	public List<GjtExamAppointmentNew> findAll(Specification<GjtExamAppointmentNew> spec) {
		return gjtExamAppointmentNewRepository.findAll(spec);
	}

	public GjtExamAppointmentNew findOne(Specification<GjtExamAppointmentNew> spec) {
		return gjtExamAppointmentNewRepository.findOne(spec);
	}

	public long count(Specification<GjtExamAppointmentNew> spec) {
		return gjtExamAppointmentNewRepository.count(spec);
	}

	public GjtExamAppointmentNew savePlan(GjtExamAppointmentNew entity) {
		return gjtExamAppointmentNewRepository.save(entity);
	}

	public GjtExamAppointmentNew save(GjtExamAppointmentNew entity) {
		return gjtExamAppointmentNewRepository.save(entity);
	}

	public GjtExamAppointmentNew findByStudentIdAndExamPlanId(String studentId, String examPlanId) {
		return gjtExamAppointmentNewRepository.findByStudentIdAndExamPlanIdAndIsDeleted(studentId, examPlanId, 0);
	}

	public GjtExamPointAppointmentNew savePoint(GjtExamPointAppointmentNew entity) {
		return gjtExamPointAppointmentNewRepository.save(entity);
	}

	// 这方法是坑
	public int countAppointMents(String studentid) {
		String sql = "select count(1) from GJT_EXAM_APPOINTMENT_NEW where is_deleted=0 and STATUS=0 and STUDENT_ID='"
				+ studentid + "'";
		Query query = em.createNativeQuery(sql);
		BigDecimal count = (BigDecimal) query.getSingleResult();
		return count.intValue();
	}

	public GjtExamAppointmentNew getAppointmentByStuidAndPlanid(String studentid, String planid) {
		String sql = "select * from GJT_EXAM_APPOINTMENT_NEW where student_id='" + studentid + "' and exam_plan_id='"
				+ planid + "'";
		Query query = em.createNativeQuery(sql, GjtExamAppointmentNew.class);
		List<GjtExamAppointmentNew> list = query.getResultList();
		GjtExamAppointmentNew entity = null;
		if (null != list && list.size() > 0) {
			entity = list.get(0);
		}
		return entity;
	}

	// 该学年度考点的预约记录人数
	public List<GjtExamAppointmentNew> getAppointmentsBy(String studyYearId, String examPointId, int examType) {
		String sql = "select a.* from gjt_exam_appointment_new a  left join gjt_exam_point_appointment_new b "
				+ "on a.student_id = b.student_id  where b.study_year_id = '" + studyYearId + "' "
				+ "and b.exam_point_id = '" + examPointId + "'  and a.type =" + examType
				+ " and b.is_deleted = 0 and a.is_deleted = 0";
		Query query = em.createNativeQuery(sql, GjtExamAppointmentNew.class);
		List<GjtExamAppointmentNew> list = query.getResultList();
		return list;
	}

	// 该学期考点的预约记录
	public List<GjtExamAppointmentNew> getAppointmentsByGrade(String gradeId, String examPointId, int examType) {
		String sql = "select a.* from gjt_exam_appointment_new a, gjt_exam_point_appointment_new b, gjt_exam_plan_new c "
				+ "where a.student_id = b.student_id  and b.grade_id = '" + gradeId + "' "
				+ "and b.exam_point_id = '" + examPointId + "'  and a.type =" + examType
				+ " and b.is_deleted = 0 and a.is_deleted = 0"
				+ " and a.exam_plan_id = c.exam_plan_id and c.grade_id = '" + gradeId + "' ";
		Query query = em.createNativeQuery(sql, GjtExamAppointmentNew.class);
		List<GjtExamAppointmentNew> list = query.getResultList();
		return list;
	}

	public int getAppointmentsCount(String studyYearId, String examPointId, int examType) {
		String sql = "select count(distinct a.student_id) from gjt_exam_appointment_new a  left join gjt_exam_point_appointment_new b "
				+ "on a.student_id = b.student_id  where b.study_year_id = '" + studyYearId + "' "
				+ "and b.exam_point_id = '" + examPointId + "'  and a.type =" + examType
				+ " and b.is_deleted = 0 and a.is_deleted = 0";
		Query query = em.createNativeQuery(sql);
		List list = query.getResultList();
		return Integer.valueOf(list.get(0).toString());
	}

	public GjtExamPlanNew getExamPlanByCourseid(String courseid) {
		GjtExamPlanNew plan = null;
		String sql = "select * from gjt_exam_plan_new where subject_code = "
				+ "(select subject_code from gjt_exam_subject_new where course_id='" + courseid
				+ "' and rownum=1) and rownum=1";
		Query query = em.createNativeQuery(sql, GjtExamPlanNew.class);
		List<GjtExamPlanNew> list = query.getResultList();
		if (null != list && list.size() > 0) {
			plan = list.get(0);
		}
		return plan;
	}

	public GjtExamPlanNew getExamPlanBySubjectCode(String subjectCode) {
		// GjtExamPlanNew plan = null;
		// String sql = "select * from gjt_exam_plan_new where is_deleted=0 and
		// subject_code = '"+subjectCode+"' and rownum=1";
		// Query query = em.createNativeQuery(sql);
		// List<GjtExamPlanNew> list = query.getResultList();
		// if(null!=list && list.size()>0) {
		// plan = list.get(0);
		// }
		// return plan;
		// 上面代码有个莫名奇妙的bug...查了好久没找到原因, 于是就换成下面的方式了.
		GjtExamPlanNew plan = null;
		String sql = "select exam_plan_id, xx_id, type from gjt_exam_plan_new where is_deleted=0 and subject_code = '"
				+ subjectCode + "' and rownum=1";
		Query query = em.createNativeQuery(sql);
		List<Object[]> list = query.getResultList();
		if (null != list && list.size() > 0) {
			plan = new GjtExamPlanNew();
			Object[] arr = list.get(0);
			plan.setExamPlanId(arr[0].toString());
			plan.setXxId(arr[1].toString());
			plan.setType(((BigDecimal) arr[2]).intValue());
		}
		return plan;

	}

	public GjtExamPointNew getExamPointByNameAndXxid(String xxid, String name) {
		GjtExamPointNew entity = null;
		String sql = "select * from gjt_exam_point_new where name = '" + name + "' and xx_id = '" + xxid
				+ "' and rownum=1";
		Query query = em.createNativeQuery(sql, GjtExamPointNew.class);
		List<GjtExamPointNew> list = query.getResultList();
		if (null != list && list.size() > 0) {
			entity = list.get(0);
		}
		return entity;
	}

	public GjtStudyYearInfo getCurrentStudyYear(String xxid) {
		GjtStudyYearInfo entity = null;
		String sql = "select * from GJT_STUDYYEAR_INFO where studyyear_start_date<sysdate and studyyear_end_date>sysdate and xx_id='"
				+ xxid + "'";
		Query query = em.createNativeQuery(sql, GjtStudyYearInfo.class);
		List<GjtStudyYearInfo> list = query.getResultList();
		if (null != list && list.size() > 0) {
			entity = list.get(0);
		}
		return entity;
	}

	public GjtExamPointAppointmentNew getPointAppointment(String studentid, String pointid, String studyyearid) {
		GjtExamPointAppointmentNew entity = null;
		String sql = "select * from GJT_EXAM_POINT_APPOINTMENT_NEW where is_deleted = 0 and student_id= '" + studentid
				+ "' and study_year_id='" + studyyearid + "'";
		if (null != pointid) {
			sql = sql + " and exam_point_id='" + pointid + "'";
		}
		Query query = em.createNativeQuery(sql, GjtExamPointAppointmentNew.class);
		List<GjtExamPointAppointmentNew> list = query.getResultList();
		if (null != list && list.size() > 0) {
			entity = list.get(0);
		}
		return entity;
	}

	public List<GjtExamPointAppointmentNew> getPointAppointmentList(String studentid, String studyyearid) {
		GjtExamPointAppointmentNew entity = null;
		String sql = "select * from GJT_EXAM_POINT_APPOINTMENT_NEW where is_deleted = 0 and student_id= '" + studentid
				+ "' and study_year_id='" + studyyearid + "'";

		Query query = em.createNativeQuery(sql, GjtExamPointAppointmentNew.class);
		List<GjtExamPointAppointmentNew> list = query.getResultList();

		return list;
	}

	public Map<String, Map<String, String>> studentViewList(List<String> ids) {
		Map<String, Map<String, String>> resultMap = new HashMap<String, Map<String, String>>();
		if (ids.size() > 0) {
			StringBuilder sbuilder = new StringBuilder();
			sbuilder.append("select student_id, pycc, grade_name, zymc from VIEW_STUDENT_INFO ");
			if (null != ids && ids.size() > 0) {
				sbuilder.append(" where student_id in  ('");
				sbuilder.append(ids.get(0));
				sbuilder.append("'");
				for (int i = 1; i < ids.size(); i++) {
					sbuilder.append(", '");
					sbuilder.append(ids.get(i));
					sbuilder.append("'");
				}
				sbuilder.append(")");
			}
			Query query = em.createNativeQuery(sbuilder.toString());
			List<Object[]> resultList = query.getResultList();
			for (Object[] obj : resultList) {
				if (!resultMap.containsKey(obj[0].toString())) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("studentid", obj[0].toString());
					map.put("pycc", obj[1].toString());
					map.put("gradeName", obj[2].toString());
					map.put("zymc", obj[3].toString());
					resultMap.put(obj[0].toString(), map);
				}
			}
		}
		return resultMap;
	}

	public Map<String, String> appointPointMap(List<String> ids) {
		Map<String, String> map = new HashMap<String, String>();
		if (ids.size() > 0) {
			StringBuilder sbuilder = new StringBuilder();
			sbuilder.append("select appmnt.student_id, pnt.name " + "from gjt_exam_point_appointment_new appmnt "
					+ "inner join gjt_exam_point_new pnt on appmnt.exam_point_id=pnt.exam_point_id where appmnt.is_deleted = 0 ");
			if (null != ids && ids.size() > 0) {
				sbuilder.append(" and appmnt.student_id in  ('");
				sbuilder.append(ids.get(0));
				sbuilder.append("'");
				for (int i = 1; i < ids.size(); i++) {
					sbuilder.append(", '");
					sbuilder.append(ids.get(i));
					sbuilder.append("'");
				}
				sbuilder.append(")");
			}
			Query query = em.createNativeQuery(sbuilder.toString());
			List<Object[]> resultList = query.getResultList();
			for (Object[] obj : resultList) {
				if (map.containsKey(obj[0].toString())) {
					map.put(obj[0].toString(), map.get(obj[0].toString()) + ", " + obj[1].toString());
				} else {
					map.put(obj[0].toString(), obj[1].toString());
				}
			}
		}
		return map;
	}



	/**
	 * 根据考试计划导出学生预约考点数据
	 * @param examBatchCode
	 * @param orgId
	 * @return
	 */
	public List<Map> appointPointMapBySearch(String examBatchCode, String orgId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select "
				+ "vsi.xm as student_name, vsi.grade_name as grade_name, vsi.zymc as zymc, vsi.xh as xh, ge.name as point_name, ge.exam_type, ge.code as point_code "
				+ "from VIEW_STUDENT_INFO vsi, gjt_exam_point_appointment_new ep "
				+ "inner join gjt_exam_point_new ge on ep.exam_point_id=ge.exam_point_id "
				+ "where ep.is_deleted = 0 and ep.student_id=vsi.student_id ");// 请勿修改select
		// 字段顺序

		sql.append(" and ep.exam_Batch_Code=:examBatchCode");
		params.put("examBatchCode", examBatchCode);

		if(EmptyUtils.isNotEmpty(orgId)) {
			sql.append("  AND vsi.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:xxzxId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID) ");
			params.put("xxzxId", orgId);
		}
		return super.findAllByToMap(sql, params, null);
	}

	public List<Map<String, Object>> appointmentListBySearch(Map<String, String> params) {
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("select "
				+ "			stu.sfzh, stuview.student_id as student_id, stuview.xm as student_name, stuview.grade_name, stuview.zymc, stuview.pycc, stuview.xh, "
				+ "			stu.sjh, "
				+ "			gappoint.appointment_id, gappoint.seat_no, gappoint.type, gappoint.status, "
				+ "			gplan.exam_st, gplan.exam_end, "
				+ "			gbatch.name as batch_name, gbatch.exam_batch_code, "
				+ "			gsubject.name as subject_name, gsubject.subject_code, gsubject.exam_no as examNo, "
				+ "			gpoint.name as point_name,"
				+ " (SELECT gtp.course_code FROM VIEW_TEACH_PLAN GTP WHERE  GTP.GRADE_ID = stuview.GRADE_ID AND GTP.PYCC = stuview.PYCC AND GTP.KKZY = stuview.MAJOR AND GTP.IS_DELETED='N' and	 gtp.subject_id = gsubject.subject_id ) as COURSECODE"
				+ "		from gjt_exam_appointment_new gappoint "
				+ "		left join VIEW_STUDENT_INFO stuview on gappoint.student_id=stuview.student_id "
				+ "		left join gjt_student_info stu on gappoint.student_id=stu.student_id "
				+ "		left join gjt_exam_plan_new gplan on gappoint.exam_plan_id=gplan.exam_plan_id "
				+ "		left join gjt_exam_batch_new gbatch on gplan.exam_batch_code=gbatch.exam_batch_code "
				+ "		left join gjt_exam_subject_new gsubject on gplan.subject_code=gsubject.subject_code "
				+ "		left join gjt_exam_point_appointment_new pointappoint on pointappoint.student_id=stu.student_id and pointappoint.study_year_id = gbatch.study_year_id and pointappoint.IS_DELETED=0"
				+ "		left join gjt_exam_point_new gpoint on pointappoint.exam_point_id=gpoint.exam_point_id and gpoint.IS_DELETED='N' where "
				+ "			gappoint.is_deleted=0 ");
		if (params.containsKey("status")) {
			sbuilder.append(" and gappoint.status=");
			sbuilder.append(Integer.parseInt(params.get("status")));
		}
		if (params.containsKey("examBatchCode")) {
			sbuilder.append(" and gbatch.exam_batch_code='");
			sbuilder.append(params.get("examBatchCode"));
			sbuilder.append("'");
		}
		Query query = em.createNativeQuery(sbuilder.toString());
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = query.getResultList();
		return list;
	}

	public List<GjtExamAppointmentNew> appointmentListForArrange(List<String> studentidList, List<String> planidList) {
		List<GjtExamAppointmentNew> list = new ArrayList<GjtExamAppointmentNew>();
		if (studentidList.size() > 0 && planidList.size() > 0) {
			StringBuilder sbuilder = new StringBuilder();
			sbuilder.append("select appoint.* " + "			from " + "		 		gjt_exam_appointment_new appoint "
					+ "			left join gjt_exam_plan_new plan on appoint.exam_plan_id=plan.exam_plan_id "
					+ "			where " + "				appoint.is_deleted=0 and appoint.student_id in ('");
			sbuilder.append(studentidList.get(0));
			sbuilder.append("'");
			for (int i = 1; i < studentidList.size(); i++) {
				sbuilder.append(", '");
				sbuilder.append(studentidList.get(i));
				sbuilder.append("'");
			}
			sbuilder.append(") and appoint.exam_plan_id in ('");
			sbuilder.append(planidList.get(0));
			sbuilder.append("'");
			for (int i = 1; i < planidList.size(); i++) {
				sbuilder.append(", '");
				sbuilder.append(planidList.get(i));
				sbuilder.append("'");
			}
			sbuilder.append(")");
			sbuilder.append(" order by plan.exam_st, created_dt");
			Query query = em.createNativeQuery(sbuilder.toString(), GjtExamAppointmentNew.class);
			list = query.getResultList();
		}
		return list;
	}

	public Map<String, Object> getByTeachPlanid(String teachPlanid) {
		String sql = "select TEACH_PLAN_ID,COURSE_ID,COURSE_CODE from VIEW_TEACH_PLAN where teach_plan_id='"
				+ teachPlanid + "'";
		Query query = em.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = query.getResultList();
		return list.size() > 0 ? list.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	public Page<GjtExamAppointmentNew> queryPage(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ").append("		gea.* ").append("from ").append("		GJT_EXAM_APPOINTMENT_NEW gea ")
				.append("left join ").append("		GJT_EXAM_STUDENT_ROOM_NEW gesr ").append("on ")
				.append("		gesr.APPOINTMENT_ID = gea.APPOINTMENT_ID ").append("left join ")
				.append("		GJT_EXAM_ROOM_NEW ger ").append("on ")
				.append("		ger.EXAM_ROOM_ID = gesr.EXAM_ROOM_ID ").append("where ")
				.append("		gea.IS_DELETED = 0 ").append("		and gea.STATUS < 2 ");

		Object xxId = searchParams.get("EQ_xxId");
		if (xxId != null) {
			sb.append(" and gea.XX_ID = :xxId ");
			map.put("xxId", xxId);
		}

		Object type = searchParams.get("EQ_type");
		if (type != null) {
			sb.append(" and gea.type = :type ");
			map.put("type", type);
		}

		Object studentName = searchParams.get("LIKE_student.xm");
		if (studentName != null && StringUtils.isNotBlank((String) studentName)) {
			sb.append(" and exists ( ").append("			select ").append("					1 ")
					.append("			from ").append("					GJT_STUDENT_INFO ")
					.append("			where ").append("					STUDENT_ID = gea.STUDENT_ID ")
					.append("					and XM like :studentName ").append("			) ");
			map.put("studentName", "%" + studentName + "%");
		}

		Object studentCode = searchParams.get("LIKE_student.xh");
		if (studentCode != null && StringUtils.isNotBlank((String) studentCode)) {
			sb.append(" and exists ( ").append("			select ").append("					1 ")
					.append("			from ").append("					GJT_STUDENT_INFO ")
					.append("			where ").append("					STUDENT_ID = gea.STUDENT_ID ")
					.append("					and XH like :studentCode ").append("			) ");
			map.put("studentCode", "%" + studentCode + "%");
		}

		Object status = searchParams.get("EQ_status");
		if (status != null && StringUtils.isNotBlank((String) status)) {
			int n = Integer.parseInt(status.toString());
			if (n == 0) {
				sb.append(" and gesr.APPOINTMENT_ID is null ");
			} else if (n == 1) {
				sb.append(" and gesr.APPOINTMENT_ID is not null ");
			}

			/*
			 * sb.append(" and gea.STATUS = :status "); map.put("status",
			 * Integer.parseInt(status.toString()));
			 */
		}

		Object subjectCode = searchParams.get("EQ_examPlanNew.subjectCode");
		if (subjectCode != null && StringUtils.isNotBlank((String) subjectCode)) {
			sb.append(" and exists ( ").append("			select ").append("					1 ")
					.append("			from ").append("					GJT_EXAM_PLAN_NEW ")
					.append("			where ").append("					EXAM_PLAN_ID = gea.EXAM_PLAN_ID ")
					.append("					and IS_DELETED = 0 ")
					.append("					and SUBJECT_CODE = :subjectCode ").append("			) ");
			map.put("subjectCode", subjectCode);
		}

		Object examBatchCode = searchParams.get("EQ_examPlanNew.examBatchCode");
		if (examBatchCode != null && StringUtils.isNotBlank((String) examBatchCode)) {
			sb.append(" and exists ( ").append("			select ").append("					1 ")
					.append("			from ").append("					GJT_EXAM_PLAN_NEW ")
					.append("			where ").append("					EXAM_PLAN_ID = gea.EXAM_PLAN_ID ")
					.append("					and IS_DELETED = 0 ")
					.append("					and EXAM_BATCH_CODE = :examBatchCode ").append("			) ");
			map.put("examBatchCode", examBatchCode);
		}

		Object examPoint = searchParams.get("EQ_examPoint");
		if (examPoint != null && StringUtils.isNotBlank((String) examPoint)) {
			sb.append(" and exists (").append("			select ").append("					1 ")
					.append("			from ").append("					GJT_EXAM_POINT_APPOINTMENT_NEW ")
					.append("			where ").append("					EXAM_POINT_ID = :examPoint ")
					.append("					and IS_DELETED = 0 ")
					.append("					and STUDENT_ID = gea.STUDENT_ID ").append("			) ");
			map.put("examPoint", examPoint);
		}

		if (pageRequst.getSort() != null) {
			sb.append("order by gesr.exam_plan_id,ger.order_no, gesr.seat_no ");
		}

		return (Page<GjtExamAppointmentNew>) commonDao.queryForPageNative(sb.toString(), map, pageRequst,
				GjtExamAppointmentNew.class);
	}
	
	
	// TODO  不使用科目表

    /** 列表查询  考试预约 考试排考*/
    public List<Map> queryList(Map formMap){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashedMap();
        sql.append(" select distinct t.appointment_id,b.xh,b.xm,b.sfzh,e.exam_plan_name,e.exam_no,");
        sql.append("        (SELECT TSD.NAME FROM TBL_SYS_DATA TSD WHERE IS_DELETED = 'N' AND TYPE_CODE = 'ExaminationMode' AND CODE = e.TYPE) EXAM_TYPE_NAME,");
        sql.append("        d.name POINT_NAME,d.code POINT_CODE");
        sql.append(" from gjt_exam_appointment_new t");
        sql.append(" inner join gjt_student_info b on b.student_id=t.student_id");
        sql.append(" inner join gjt_exam_point_appointment_new c on c.is_deleted=0 and c.student_id=t.student_id");
        sql.append(" inner join gjt_exam_point_new d on d.exam_point_id=c.exam_point_id");
        sql.append(" inner join gjt_exam_plan_new e on e.exam_plan_id=t.exam_plan_id");
        sql.append(" where t.IS_DELETED = 0 ");

        String status = ObjectUtils.toString(formMap.get("status"));
        String examBatchCode = (String) formMap.get("examBatchCode");
        String examTypes = ObjectUtils.toString(formMap.get("examTypes"));
        String orgId = (String) formMap.get("orgId");
        
        if(EmptyUtils.isNotEmpty(orgId)) {
            sql.append(" AND b.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:orgId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
            params.put("orgId",  orgId);
        }

        if(EmptyUtils.isNotEmpty(examBatchCode)){
            sql.append(" AND t.EXAM_BATCH_CODE = :examBatchCode and c.EXAM_BATCH_CODE = :examBatchCode");
            params.put("examBatchCode",  examBatchCode);
        }

        if(EmptyUtils.isNotEmpty(examTypes)){
            sql.append(" and e.type=:type and d.exam_type=:type");
            params.put("type", examTypes);
        }

        if(EmptyUtils.isNotEmpty(status)){
            sql.append(" AND t.STATUS = :status");
            params.put("status", status);
        }
        return super.findAllByToMap(sql, params, null);
    }
    
    public GjtExamAppointmentNew queryOne(String id) {
    	return gjtExamAppointmentNewRepository.findOne(id);
    }
    
    public List<GjtExamAppointmentNew> queryList(Iterable<String> ids) {
    	return (List<GjtExamAppointmentNew>)gjtExamAppointmentNewRepository.findAll(ids);
    }
    
    public List<GjtExamAppointmentNew> findByExamBatchCodeAndStatus(String examBatchCode, int status) {
    	return gjtExamAppointmentNewRepository.findByExamBatchCodeAndStatus(examBatchCode, status);
    }
    
    /**
     * 查询学员预约的考点
     * @return
     */
    public List queryStudentPoint(Map formMap){
        StringBuilder sql = new StringBuilder();
        Map params = new HashedMap();
        sql.append("  SELECT GEP.EXAM_POINT_ID, GEP.NAME EXAM_POINT_NAME,GEP.EXAM_TYPE");
        sql.append("  FROM GJT_EXAM_POINT_APPOINTMENT_NEW GEPA,");
        sql.append("  GJT_EXAM_POINT_NEW             GEP");
        sql.append("  WHERE GEPA.IS_DELETED = 0");
        sql.append("  AND GEPA.EXAM_POINT_ID = GEP.EXAM_POINT_ID");
        sql.append("  AND GEPA.STUDENT_ID = :STUDENT_ID");
        sql.append("  AND GEPA.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
        
        params.put("STUDENT_ID", ObjectUtils.toString(formMap.get("STUDENT_ID")));
        params.put("EXAM_BATCH_CODE", ObjectUtils.toString(formMap.get("EXAM_BATCH_CODE")));

        return commonDao.queryForMapListNative(sql.toString(), params);
    }

	/**
	 * 取消考试预约
	 * @param examBatchCode
	 * @param studentId
	 * @param recId
     * @return
     */
	public boolean deleteExamAppointment(String examBatchCode, String studentId, String recId) {
		return gjtExamAppointmentNewRepository.deleteExamAppointment(examBatchCode, studentId, recId) > 0;
	}

	/**
	 * 取消考点预约
	 * @param examBatchCode
	 * @param studentId
	 * @param examPointId
     * @return
     */
	public boolean deletePointExamAppointment(String examBatchCode, String studentId, String examPointId) {
		GjtExamPointNew point = gjtExamPointNewRepository.findOne(examPointId);
		int resultNum = gjtExamPointAppointmentNewRepository.deletePointExamAppointment(examBatchCode, studentId, point.getExamType());
		return resultNum > 0;
	}

	/**
	 * 查询预约记录数据
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, Object>> queryExamAppointment(Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder sql = new StringBuilder("");
		sql.append("select t.exam_batch_code \"examBatchCode\",t.exam_plan_id \"examPlanId\",c.teach_plan_id \"teachPlanId\",");
		sql.append("	c.rec_id \"recId\",c.TERMCOURSE_ID \"termCourseId\",v.COURSE_ID \"courseId\",v.KCMC \"courseName\",v.LEARNING_STYLE \"learningStyle\",");
		sql.append("	b.type \"type\",b.exam_st \"examSt\",b.exam_end \"examEnd\",");
		sql.append("	b.XK_PERCENT \"xkPercent\",(100-b.XK_PERCENT) \"ksPercent\",t.EXAM_SCORE \"score\",t.EXAM_STATUS \"examStatus\",");
		sql.append("	c.exam_state \"examState\",c.exam_score \"examScore\",c.exam_score1 \"examScore1\",c.exam_score2 \"examScore2\"");
		sql.append(" from gjt_exam_appointment_new t");
		sql.append(" inner join gjt_exam_plan_new b on b.exam_plan_id=t.exam_plan_id");
		sql.append(" inner join gjt_rec_result c on c.rec_id=t.rec_id");
		sql.append(" inner join view_teach_plan v on v.TEACH_PLAN_ID=c.teach_plan_id");
		sql.append(" where t.is_deleted=0");

		// 考试计划编号
		String examBatchCode = (String) searchParams.get("EQ_examBatchCode");
		if (StringUtils.isNotBlank(examBatchCode)) {
			sql.append(" and t.exam_batch_code=:examBatchCode");
			parameters.put("examBatchCode", examBatchCode);
		}
		// 学员ID
		String studentId = (String) searchParams.get("EQ_studentId");
		if (StringUtils.isNotBlank(studentId)) {
			sql.append(" and t.student_id=:studentId");
			parameters.put("studentId", studentId);
		}

		// 离当前时间最近的排前，过了当前时间的排后面，但是还是以离当前时间最近的排前面
		sql.append(" order by (case when b.exam_st-sysdate>0 then b.exam_st-sysdate else abs(b.exam_st-sysdate)*1000 end)");
		return commonDao.queryForStringObjectMapListNative(sql.toString(), parameters);
	}

	/**
	 * 导出预约记录数据
	 * @param orgId
	 * @param searchParams
	 * @return
	 */
	public List<Map> findExamAppointmentList(String orgId, Map<String, Object> searchParams) {
		Map<String, Object> parameters = new HashMap();
		StringBuilder sql = new StringBuilder("");
		sql.append("  select f.xm \"xm\",f.xh \"xh\",g.grade_name \"termName\",f.major \"major\",h.zymc \"zymc\",f.pycc \"pycc\",");
		sql.append("  a.name \"examBatchName\",a.exam_batch_code \"examBatchCode\",b.exam_plan_name \"examPlanName\",b.exam_no \"examNo\",");
		sql.append("  e.SOURCE_COURSE_ID \"sourceCourseId\",e.SOURCE_KCH \"sourceCourseKch\",e.SOURCE_KCMC \"sourceCourseKcmc\",e.KCH \"courseKch\",e.kcmc \"courseKcmc\",");
		sql.append("  b.TYPE \"type\",i.name \"pointName\",TO_CHAR(t.created_dt, 'yyyy-MM-dd hh24:mi:ss') \"appointmentDt\",t.status \"status\",");
		sql.append("  (case when j.cost_id is not null then '是' else '否' end) \"isResit\",j.course_cost \"resitCost\"");
		sql.append("  from gjt_exam_appointment_new t ");
		sql.append("  inner join GJT_EXAM_BATCH_NEW a on a.exam_batch_code=t.exam_batch_code");
		sql.append("  inner join gjt_exam_plan_new b on b.exam_plan_id=t.exam_plan_id");
		sql.append("  inner join gjt_rec_result c on c.rec_id = t.rec_id");
		sql.append("  inner join VIEW_TEACH_PLAN e on e.teach_plan_id=c.teach_plan_id");
		sql.append("  inner join gjt_student_info f on f.student_id=t.student_id");
		sql.append("  left join gjt_grade g on g.grade_id=f.nj");
		sql.append("  left join gjt_specialty h on h.specialty_id=f.major");
		sql.append("  left join (select x.exam_batch_code,x.student_id,y.exam_type,y.name from gjt_exam_point_appointment_new x inner join gjt_exam_point_new y on y.exam_point_id=x.exam_point_id where x.is_deleted=0) i on i.exam_batch_code=t.exam_batch_code and i.student_id=t.student_id and i.exam_type=b.type");
		sql.append("  left join gjt_exam_cost j on j.student_id=t.student_id and j.exam_plan_id=t.exam_plan_id and j.pay_status='0' and j.is_deleted='N'");
		sql.append("  where t.is_deleted=0");

		sql.append(" AND F.XXZX_ID IN (SELECT org.ID FROM GJT_ORG org WHERE org.IS_DELETED='N' START WITH org.ID=:orgId CONNECT BY PRIOR ORG.ID=ORG.PERENT_ID)");
		parameters.put("orgId", orgId);
		
		// 考试计划编号
		String examBatchCode = (String) searchParams.get("EQ_examPlanNew.examBatchCode");
		if (StringUtils.isNotBlank(examBatchCode)) {
			sql.append("  and t.exam_batch_code=:examBatchCode");
			parameters.put("examBatchCode", examBatchCode);
		}
		// 考试科目
		String examPlanId = (String) searchParams.get("EQ_examPlanNew.examPlanId");
		if (StringUtils.isNotBlank(examPlanId)) {
			sql.append("  and t.exam_plan_id=:examPlanId");
			parameters.put("examPlanId", examPlanId);
		}
		// 考试形式
		String type = (String) searchParams.get("EQ_examPlanNew.type");
		if (StringUtils.isNotBlank(type)) {
			sql.append("  and b.type=:type");
			parameters.put("type", type);
		}
		// 姓名
		String xm = (String) searchParams.get("LIKE_student.xm");
		if (StringUtils.isNotBlank(xm)) {
			sql.append("  and f.xm like :xm");
			parameters.put("xm", MatchMode.ANYWHERE.toMatchString(xm));
		}
		// 学号
		String xh = (String) searchParams.get("EQ_student.xh");
		if (StringUtils.isNotBlank(xh)) {
			sql.append("  and f.xh=:xh");
			parameters.put("xh", xh);
		}
		// 学期
		String gradeId = (String) searchParams.get("EQ_student.gjtGrade.gradeId");
		if (StringUtils.isNotBlank(gradeId)) {
			sql.append("  and g.grade_id=:gradeId");
			parameters.put("gradeId", gradeId);
		}
		// 学习中心
		String xxzxId = (String) searchParams.get("EQ_student.gjtStudyCenter.id");
		if (StringUtils.isNotBlank(xxzxId)) {
			sql.append("  and f.xxzx_Id=:xxzxId");
			parameters.put("xxzxId", xxzxId);
		}

		sql.append("  order by t.created_dt desc");
		long beginTime = System.currentTimeMillis();
		try {
			return super.findAllByToMap(sql, parameters, null);
		} finally {
			// 计算执行当前sql耗时时长
			log.info(String.format("function findExamAppointmentList select use time:%1$sms, sql:%2$s, parameters:%3$s",
					System.currentTimeMillis() - beginTime,
					sql,
					parameters));
		}
	}
}
