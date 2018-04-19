package com.gzedu.xlims.dao.exam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.exam.repository.GjtExamPlanNewRepository;
import com.gzedu.xlims.daoImpl.base.BaseDaoImpl;
import com.gzedu.xlims.pojo.exam.GjtExamPlanNew;

@Repository
public class GjtExamPlanNewDao extends BaseDaoImpl {
	@Autowired
	private CommonDao commonDao;

	@PersistenceContext(unitName = "entityManagerFactory")
	public EntityManager em;

	@Autowired
	private GjtExamPlanNewRepository gjtExamPlanNewRepository;

	public GjtExamPlanNew findByExamBatchCodeAndTypeAndExamSubjectNewKch(String examBatchCode, String type,
			String kch) {
		return gjtExamPlanNewRepository.findByExamBatchCodeAndTypeAndExamSubjectNewKch(examBatchCode, type, kch);
	}

	public GjtExamPlanNew findByExamBatchNewStudyYearIdAndTypeAndSubjectCodeAndIsDeleted(String studyYearId, int type,
			String subjectCode) {
		return gjtExamPlanNewRepository.findByExamBatchNewStudyYearIdAndTypeAndSubjectCodeAndIsDeleted(studyYearId,
				type, subjectCode, 0);
	}

	public GjtExamPlanNew findByExamBatchNewStudyYearIdAndTypeAndExamSubjectNewSubjectIdAndIsDeleted(String studyYearId,
			int type, String subjectId) {
		return gjtExamPlanNewRepository.findByExamBatchNewStudyYearIdAndTypeAndExamSubjectNewSubjectIdAndIsDeleted(
				studyYearId, type, subjectId, 0);
	}

	public List<GjtExamPlanNew> findAll(Specification<GjtExamPlanNew> spec) {
		return gjtExamPlanNewRepository.findAll(spec);
	}

	public Page<GjtExamPlanNew> findAll(Specification<GjtExamPlanNew> spec, PageRequest pageRequst) {
		return gjtExamPlanNewRepository.findAll(spec, pageRequst);
	}

	public GjtExamPlanNew save(GjtExamPlanNew entity) {
		return gjtExamPlanNewRepository.save(entity);
	}

	public void deleteGjtExamPlanNew(String id) {
		gjtExamPlanNewRepository.deleteGjtExamPlanNew(id);
	}

	public GjtExamPlanNew queryBy(String id) {
		return gjtExamPlanNewRepository.queryByExamPlanId(id);
	}

	public List<GjtExamPlanNew> save(List<GjtExamPlanNew> list) {
		return gjtExamPlanNewRepository.save(list);
	}

	public List<GjtExamPlanNew> findByExamBatchCode(String batchCode) {
		return gjtExamPlanNewRepository.findByExamBatchCodeAndIsDeleted(batchCode, 0);
	}

	/**
	 * 根据考试计划id 获取可更新的的考试计划映射
	 * 
	 * @param ids
	 * @return
	 */
	public Map<String, GjtExamPlanNew> updatePlanMapByIds(List<String> ids) {
		Map<String, GjtExamPlanNew> map = new HashMap<String, GjtExamPlanNew>();
		if (ids.size() > 0) {
			StringBuilder sbuilder = new StringBuilder();
			sbuilder.append("select " + "				* " + "			from " + "				GJT_EXAM_PLAN_NEW ");
			if (null != ids && ids.size() > 0) {
				sbuilder.append(" where (BOOK_ST>sysdate or (EXAM_ST is null or EXAM_END is null)) and is_deleted=0");
				sbuilder.append(" and EXAM_PLAN_ID in  ('");
				sbuilder.append(ids.get(0));
				sbuilder.append("'");
				for (int i = 1; i < ids.size(); i++) {
					sbuilder.append(", '");
					sbuilder.append(ids.get(i));
					sbuilder.append("'");
				}
				sbuilder.append(")");
			}
			Query query = em.createNativeQuery(sbuilder.toString(), GjtExamPlanNew.class);
			List<GjtExamPlanNew> resultList = query.getResultList();

			for (GjtExamPlanNew plan : resultList) {
				map.put(plan.getExamPlanId(), plan);
			}
		}
		return map;
	}

	@Transactional
	public int deletePlansByBatchCode(String batchCode) {
		String sql = "delete from GJT_EXAM_PLAN_NEW where EXAM_BATCH_CODE='" + batchCode + "'";
		Query query = em.createNativeQuery(sql);
		return query.executeUpdate();
	}

	/**
	 * 根据考试批次获取考试计划进行排考. PS: 考试开始结束时间必须设置, 否则不会进行排考
	 * 
	 * @param batchCode
	 * @return
	 */
	public List<GjtExamPlanNew> plansForArrangeByBatchCode(String batchCode) {
		String sql = "select * from gjt_exam_plan_new where is_deleted=0 and EXAM_ST is not null and EXAM_END is not null "
				+ "and exam_batch_code='" + batchCode + "' ordey by EXAM_ST";
		Query query = em.createNativeQuery(sql, GjtExamPlanNew.class);
		List<GjtExamPlanNew> resultList = query.getResultList();
		return resultList;
	}

	public GjtExamPlanNew isPlanExist(String examPlanid) {
		String sql = "	select " + "			* " + "		from " + "			gjt_exam_plan_new " + "		where "
				+ "			exam_plan_id=:examPlanid";
		Query query = em.createNativeQuery(sql, GjtExamPlanNew.class);
		query.setParameter("examPlanid", examPlanid);
		List<GjtExamPlanNew> list = query.getResultList();
		GjtExamPlanNew plan = null;
		if (null != list && list.size() > 0) {
			plan = list.get(0);
		}
		return plan;
	}

	public GjtExamPlanNew queryPlan(int ksfs, String kch, String studyYearId) {
		String sql = "SELECT gepn.*  FROM GJT_EXAM_PLAN_NEW gepn, GJT_EXAM_SUBJECT_NEW gesn WHERE gepn.subject_id = gesn.subject_id AND STUDY_YEAR_ID = :studyYearId  AND gesn.KCH = :kch AND GESN.TYPE = :ksfs ";
		Query query = em.createNativeQuery(sql, GjtExamPlanNew.class);
		query.setParameter("studyYearId", studyYearId);
		query.setParameter("kch", kch);
		query.setParameter("ksfs", ksfs);
		List<GjtExamPlanNew> list = query.getResultList();
		GjtExamPlanNew plan = null;
		if (null != list && list.size() > 0) {
			plan = list.get(0);
		}
		return plan;
	}

	/**
	 * 查询考试计划列表页
	 */
	public Page getExamPlanList(Map searchParams, PageRequest pageRequst) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT STUDY_YEAR_ID,");
		sql.append("  STUDY_YEAR_NAME,");
		sql.append("  EXAM_PLAN_ID,");
		sql.append("  TYPE,");
		sql.append("  EXAM_BATCH_CODE,");
		sql.append("  EXAM_BATCH_NAME,");
		sql.append("  SUBJECT_CODE,");
		sql.append("  SUBJECT_NAME,");
		sql.append("  KCH,");
		sql.append("  EXAM_NO,");
		sql.append("  BOOK_ST,");
		sql.append("  BOOK_END,");
		sql.append("  EXAM_ST,");
		sql.append("  EXAM_END,");
		sql.append("  PSTATUS,");
		sql.append("  (CASE");
		sql.append("  WHEN TYPE = '1' THEN");
		sql.append("  '网考'");
		sql.append("  WHEN TYPE = '2' THEN");
		sql.append("  '笔考'");
		sql.append("  WHEN TYPE = '3' THEN");
		sql.append("  '机考'");
		sql.append("  WHEN TYPE = '4' THEN");
		sql.append("  '形考'");
		sql.append("  WHEN TYPE = '5' THEN");
		sql.append("  '网考（省）      '");
		sql.append("  ELSE");
		sql.append("  '机考'");
		sql.append("  END) TYPE_NAME,");
		sql.append("  (CASE");
		sql.append("  WHEN PSTATUS = '1' THEN");
		sql.append("  '未设置    '");
		sql.append("  WHEN PSTATUS = '2' THEN");
		sql.append("  '未开始    '");
		sql.append("  WHEN PSTATUS = '3' THEN");
		sql.append("  '预约中    '");
		sql.append("  WHEN PSTATUS = '4' THEN");
		sql.append("  '待考试    '");
		sql.append("  WHEN PSTATUS = '5' THEN");
		sql.append("  '考试中    '");
		sql.append("  ELSE");
		sql.append("  '已结束    '");
		sql.append("  END) PSTATUS_NAME");

		sql.append("  FROM (SELECT GEP.STUDY_YEAR_ID,");
		sql.append("  (SELECT GSI.STUDY_YEAR_NAME");
		sql.append("  FROM GJT_STUDYYEAR_INFO GSI");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GSI.ID = GEP.STUDY_YEAR_ID) STUDY_YEAR_NAME,");
		sql.append("  GEP.EXAM_PLAN_ID,");
		sql.append("  GEP.TYPE,");
		sql.append("  GEB.EXAM_BATCH_CODE,");
		sql.append("  GEB.NAME EXAM_BATCH_NAME,");
		sql.append("  GES.SUBJECT_CODE,");
		sql.append("  GES.NAME SUBJECT_NAME,");
		sql.append("  GES.KCH,");
		sql.append("  GES.EXAM_NO,");
		sql.append("  TO_CHAR(GEP.BOOK_ST, 'yyyy-mm-dd hh24:mi:ss') BOOK_ST,");
		sql.append("  TO_CHAR(GEP.BOOK_END, 'yyyy-mm-dd hh24:mi:ss') BOOK_END,");
		sql.append("  TO_CHAR(GEP.EXAM_ST, 'yyyy-mm-dd hh24:mi:ss') EXAM_ST,");
		sql.append("  TO_CHAR(GEP.EXAM_END, 'yyyy-mm-dd hh24:mi:ss') EXAM_END,");

		sql.append("  CASE");
		sql.append("  WHEN GEP.EXAM_ST IS NULL THEN");
		sql.append("  '1'");

		sql.append("  WHEN TO_CHAR(SYSDATE, 'yyyy-mm-dd') <");
		sql.append("  TO_CHAR(GEP.BOOK_ST, 'yyyy-mm-dd') THEN");
		sql.append("  '2'");

		sql.append("  WHEN (TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEP.BOOK_ST, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(GEP.BOOK_END, 'yyyy-mm-dd')) THEN");
		sql.append("  '3'");

		sql.append("  WHEN (TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEP.BOOK_END, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(GEP.EXAM_ST, 'yyyy-mm-dd')) THEN");
		sql.append("  '4'");

		sql.append("  WHEN (TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEP.EXAM_ST, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(GEP.EXAM_END, 'yyyy-mm-dd')) THEN");
		sql.append("  '5'");

		sql.append("  WHEN TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEP.EXAM_END, 'yyyy-mm-dd') THEN");
		sql.append("  '6'");
		sql.append("  ELSE");
		sql.append("  '6'");
		sql.append("  END PSTATUS");

		sql.append("  FROM GJT_EXAM_PLAN_NEW    GEP,");
		sql.append("  GJT_EXAM_BATCH_NEW   GEB,");
		sql.append("  GJT_EXAM_SUBJECT_NEW GES");
		sql.append("  WHERE GEP.IS_DELETED = 0");
		sql.append("  AND GEB.IS_DELETED = 0");
		sql.append("  AND GES.IS_DELETED = 0");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_TYPE")))) {
			sql.append("  AND GEP.TYPE = :EXAM_TYPE");
			param.put("EXAM_TYPE", ObjectUtils.toString(searchParams.get("EXAM_TYPE")));
		}

		sql.append("  AND GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  AND GEP.SUBJECT_CODE = GES.SUBJECT_CODE");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XX_ID")))) {
			sql.append("  AND GEP.XX_ID = :XX_ID");
			param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_NO")))) {
			sql.append("  AND GES.EXAM_NO = :EXAM_NO");
			param.put("EXAM_NO", ObjectUtils.toString(searchParams.get("EXAM_NO")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_YEAR_ID")))) {
			sql.append("  AND GEP.STUDY_YEAR_ID = :STUDY_YEAR_ID");
			param.put("STUDY_YEAR_ID", ObjectUtils.toString(searchParams.get("STUDY_YEAR_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))) {
			sql.append("  AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
			param.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SUBJECT_CODE")))) {
			sql.append("  AND GEP.SUBJECT_CODE = :SUBJECT_CODE");
			param.put("SUBJECT_CODE", ObjectUtils.toString(searchParams.get("SUBJECT_CODE")).trim());
		}
		sql.append("  ) TAB");

		sql.append("  WHERE 1 = 1");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PSTATUS")))) {
			sql.append("  AND PSTATUS = :PSTATUS");
			param.put("PSTATUS", ObjectUtils.toString(searchParams.get("PSTATUS")).trim());
		}

		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}

	/**
	 * 查询考试计划列表页(统计项)
	 */
	public int getExamPlanCount(Map searchParams) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT COUNT(EXAM_PLAN_ID) EXAM_PLAN_COUNT");
		sql.append("  FROM (SELECT GEP.STUDY_YEAR_ID,");
		sql.append("  GEP.EXAM_PLAN_ID,");
		sql.append("  GEP.TYPE,");
		sql.append("  GES.KCH,");
		sql.append("  GES.EXAM_NO,");
		sql.append("  TO_CHAR(GEP.BOOK_ST, 'yyyy-mm-dd hh24:mi:ss') BOOK_ST,");
		sql.append("  TO_CHAR(GEP.BOOK_END, 'yyyy-mm-dd hh24:mi:ss') BOOK_END,");
		sql.append("  TO_CHAR(GEP.EXAM_ST, 'yyyy-mm-dd hh24:mi:ss') EXAM_ST,");
		sql.append("  TO_CHAR(GEP.EXAM_END, 'yyyy-mm-dd hh24:mi:ss') EXAM_END,");
		sql.append("  (CASE");
		sql.append("  WHEN GEP.EXAM_ST IS NULL THEN");
		sql.append("  '1'");

		sql.append("  WHEN TO_CHAR(SYSDATE, 'yyyy-mm-dd') <");
		sql.append("  TO_CHAR(GEP.BOOK_ST, 'yyyy-mm-dd') THEN");
		sql.append("  '2'");

		sql.append("  WHEN (TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEP.BOOK_ST, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(GEP.BOOK_END, 'yyyy-mm-dd')) THEN");
		sql.append("  '3'");

		sql.append("  WHEN (TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEP.BOOK_END, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(GEP.EXAM_ST, 'yyyy-mm-dd')) THEN");
		sql.append("  '4'");

		sql.append("  WHEN (TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEP.EXAM_ST, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(GEP.EXAM_END, 'yyyy-mm-dd')) THEN");
		sql.append("  '5'");

		sql.append("  WHEN TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEP.EXAM_END, 'yyyy-mm-dd') THEN");
		sql.append("  '6'");
		sql.append("  ELSE");
		sql.append("  '6'");
		sql.append("  END) PSTATUS");

		sql.append("  FROM GJT_EXAM_PLAN_NEW    GEP,");
		sql.append("  GJT_EXAM_BATCH_NEW   GEB,");
		sql.append("  GJT_EXAM_SUBJECT_NEW GES");
		sql.append("  WHERE GEP.IS_DELETED = 0");
		sql.append("  AND GEB.IS_DELETED = 0");
		sql.append("  AND GES.IS_DELETED = 0");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_TYPE")))) {
			sql.append("  AND GEP.TYPE = :EXAM_TYPE");
			param.put("EXAM_TYPE", ObjectUtils.toString(searchParams.get("EXAM_TYPE")));
		}

		sql.append("  AND GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  AND GEP.SUBJECT_CODE = GES.SUBJECT_CODE");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XX_ID")))) {
			sql.append("  AND GEP.XX_ID = :XX_ID");
			param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_NO")))) {
			sql.append("  AND GES.EXAM_NO = :EXAM_NO");
			param.put("EXAM_NO", ObjectUtils.toString(searchParams.get("EXAM_NO")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_YEAR_ID")))) {
			sql.append("  AND GEP.STUDY_YEAR_ID = :STUDY_YEAR_ID");
			param.put("STUDY_YEAR_ID", ObjectUtils.toString(searchParams.get("STUDY_YEAR_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))) {
			sql.append("  AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
			param.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")).trim());
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SUBJECT_CODE")))) {
			sql.append("  AND GEP.SUBJECT_CODE = :SUBJECT_CODE");
			param.put("SUBJECT_CODE", ObjectUtils.toString(searchParams.get("SUBJECT_CODE")).trim());
		}
		sql.append("  ) TAB");

		sql.append("  WHERE 1 = 1");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("PSTATUS_TYPE")))) {
			sql.append("  AND PSTATUS = :PSTATUS_TYPE");
			param.put("PSTATUS_TYPE", ObjectUtils.toString(searchParams.get("PSTATUS_TYPE")).trim());
		}
		int num = 0;
		List list = commonDao.queryForMapListNative(sql.toString(), param);
		if (EmptyUtils.isNotEmpty(list)) {
			Map tempMap = (Map) list.get(0);
			num = Integer.parseInt(ObjectUtils.toString(tempMap.get("EXAM_PLAN_COUNT")));
		}
		return num;
	}

	/**
	 * 查询考试计划是否存在
	 */
	public List getPlanListByCode(Map formMap) {
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT GEP.EXAM_PLAN_ID");
		sql.append("  FROM GJT_EXAM_PLAN_NEW GEP, GJT_STUDYYEAR_INFO GSI");
		sql.append("  WHERE GEP.IS_DELETED = '0'");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  AND GEP.STUDY_YEAR_ID = GSI.ID");
		sql.append("  AND GEP.TYPE = :TYPE");
		sql.append("  AND GSI.STUDY_YEAR_NAME = :STUDY_YEAR_NAME");
		sql.append("  AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
		sql.append("  AND GEP.SUBJECT_CODE = :SUBJECT_CODE");

		param.put("TYPE", ObjectUtils.toString(formMap.get("TYPE")));
		param.put("STUDY_YEAR_NAME", ObjectUtils.toString(formMap.get("STUDY_YEAR_NAME")));
		param.put("EXAM_BATCH_CODE", ObjectUtils.toString(formMap.get("EXAM_BATCH_CODE")));
		param.put("SUBJECT_CODE", ObjectUtils.toString(formMap.get("SUBJECT_CODE")));

		return commonDao.queryForMapListNative(sql.toString(), param);
	}

	/**
	 * 设置计划的考试时间
	 * 
	 * @param ids
	 * @param xxid
	 * @return
	 */
	@Transactional
	public int updPlanExamTime(Map formMap) {
		int rs = 0;
		StringBuilder sql = new StringBuilder();
		sql.append("  UPDATE GJT_EXAM_PLAN_NEW GEP");
		sql.append("  SET GEP.EXAM_ST  = TO_DATE('" + ObjectUtils.toString(formMap.get("EXAM_ST"))
				+ "', 'yyyy-mm-dd hh24:mi'),");
		sql.append("  GEP.EXAM_END = TO_DATE('" + ObjectUtils.toString(formMap.get("EXAM_END"))
				+ "', 'yyyy-mm-dd hh24:mi')");
		sql.append("  WHERE GEP.IS_DELETED = 0");
		sql.append("  AND GEP.EXAM_PLAN_ID = '" + ObjectUtils.toString(formMap.get("EXAM_PLAN_ID")) + "'");

		Query query = em.createNativeQuery(sql.toString());
		rs = query.executeUpdate();
		return rs;
	}

	/**
	 * 查询考试计划数据
	 * 
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getExamBatchData(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GEB.EXAM_BATCH_ID,");
		sql.append("  	GEB.EXAM_BATCH_CODE,");
		sql.append("  	GEB.GRADE_ID,");
		sql.append("  	GEB.XX_ID");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_BATCH_NEW GEB");
		sql.append("  WHERE");
		sql.append("  	GEB.IS_DELETED = 0");
		sql.append("  	AND GEB.EXAM_BATCH_ID = :EXAM_BATCH_ID ");

		params.put("EXAM_BATCH_ID", ObjectUtils.toString(searchParams.get("EXAM_BATCH_ID")));

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 查询试卷号是否存在
	 * 
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getIsExistNo(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	COUNT( 1 ) TEMP");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_PLAN_NEW GEP");
		sql.append("  WHERE");
		sql.append("  	GEP.IS_DELETED = 0");
		sql.append("  	AND GEP.TYPE = :TYPE ");
		sql.append("  	AND GEP.EXAM_NO = :EXAM_NO ");
		sql.append("  	AND GEP.XX_ID = :XX_ID ");

		params.put("TYPE", Integer.parseInt(ObjectUtils.toString(searchParams.get("TYPE"))));
		params.put("EXAM_NO", ObjectUtils.toString(searchParams.get("EXAM_NO")));
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 保存开考科目信息
	 * 
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int saveExamPlanData(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_EXAM_PLAN_NEW(");
		sql.append("  			EXAM_PLAN_ID,");
		sql.append("  			TYPE,");
		sql.append("  			BOOK_ST,");
		sql.append("  			BOOK_END,");
		sql.append("  			EXAM_ST,");
		sql.append("  			EXAM_END,");
		sql.append("  			XX_ID,");
		sql.append("  			CREATED_BY,");
		sql.append("  			CREATED_DT,");
		sql.append("  			EXAM_BATCH_CODE,");
		sql.append("  			EXAM_BATCH_ID,");
		sql.append("  			GRADE_ID,");
		sql.append("  			XK_PERCENT,");
		sql.append("  			EXAM_NO,");
		sql.append("  			EXAM_PLAN_NAME,");
		sql.append("  			EXAM_BATCH_CODE,");
		sql.append("  			EXAM_PLAN_ORDER");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:EXAM_PLAN_ID,");
		sql.append("  		:TYPE,");
		sql.append("  		:BOOK_ST,");
		sql.append("  		:BOOK_END,");
		sql.append("  		:EXAM_ST,");
		sql.append("  		:EXAM_END,");
		sql.append("  		:XX_ID,");
		sql.append("  		:CREATED_BY,");
		sql.append("  		SYSDATE,");
		sql.append("  		:EXAM_BATCH_CODE,");
		sql.append("  		:EXAM_BATCH_ID,");
		sql.append("  		:GRADE_ID,");
		sql.append("  		:XK_PERCENT,");
		sql.append("  		:EXAM_NO,");
		sql.append("  		:EXAM_PLAN_NAME,");
		sql.append("  		:EXAM_BATCH_CODE,");
		sql.append("  		:EXAM_PLAN_ORDER");
		sql.append("  	)");

		params.put("EXAM_PLAN_ID", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));
		params.put("TYPE", Integer.parseInt(ObjectUtils.toString(searchParams.get("TYPE"))));
		params.put("BOOK_ST", searchParams.get("BOOK_ST"));
		params.put("BOOK_END", searchParams.get("BOOK_END"));
		params.put("EXAM_ST", searchParams.get("EXAM_ST"));
		params.put("EXAM_END", searchParams.get("EXAM_END"));
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("CREATED_BY", ObjectUtils.toString(searchParams.get("CREATED_BY")));
		params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		params.put("EXAM_BATCH_ID", ObjectUtils.toString(searchParams.get("EXAM_BATCH_ID")));
		params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		params.put("XK_PERCENT", Integer.parseInt(ObjectUtils.toString(searchParams.get("XK_PERCENT"))));
		params.put("EXAM_NO", ObjectUtils.toString(searchParams.get("EXAM_NO")));
		params.put("EXAM_PLAN_NAME", ObjectUtils.toString(searchParams.get("EXAM_PLAN_NAME")));
		params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		params.put("EXAM_PLAN_ORDER", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ORDER")));

		return commonDao.insertForMapNative(sql.toString(), params);

	}

	/**
	 * 保存开考科目与课程之间的关联关系
	 * 
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int saveExamPlanCourseData(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_EXAM_PLAN_COURSE(");
		sql.append("  			PLAN_COURSE_ID,");
		sql.append("  			COURSE_ID,");
		sql.append("  			EXAM_PLAN_ID,");
		sql.append("  			EXAM_PLAN_CODE,");
		sql.append("  			EXAM_PLAN_ORDER,");
		sql.append("  			EXAM_PLAN_TYPE,");
		sql.append("  			KCH,");
		sql.append("  			EXAM_NO,");
		sql.append("  			CREATED_DT,");
		sql.append("  			CREATED_BY,");
		sql.append("  			XX_ID,");
		sql.append("  			GRADE_ID");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:PLAN_COURSE_ID,");
		sql.append("  		:COURSE_ID,");
		sql.append("  		:EXAM_PLAN_ID,");
		sql.append("  		:EXAM_PLAN_CODE,");
		sql.append("  		:EXAM_PLAN_ORDER,");
		sql.append("  		:EXAM_PLAN_TYPE,");
		sql.append("  		:KCH,");
		sql.append("  		:EXAM_NO,");
		sql.append("  		SYSDATE,");
		sql.append("  		:CREATED_BY,");
		sql.append("  		:XX_ID,");
		sql.append("  		:GRADE_ID");
		sql.append("  	)");

		params.put("PLAN_COURSE_ID", ObjectUtils.toString(searchParams.get("PLAN_COURSE_ID")));
		params.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		params.put("EXAM_PLAN_ID", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));
		params.put("EXAM_PLAN_CODE", ObjectUtils.toString(searchParams.get("EXAM_PLAN_CODE")));
		params.put("EXAM_PLAN_ORDER", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ORDER")));
		params.put("EXAM_PLAN_TYPE", ObjectUtils.toString(searchParams.get("EXAM_PLAN_TYPE")));
		params.put("KCH", ObjectUtils.toString(searchParams.get("KCH")));
		params.put("EXAM_NO", ObjectUtils.toString(searchParams.get("EXAM_NO")));
		params.put("CREATED_BY", ObjectUtils.toString(searchParams.get("CREATED_BY")));
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));

		return commonDao.insertForMapNative(sql.toString(), params);

	}

	/** 分页查询 */
	public Page<Map> queryExamPlan(Map formMap, PageRequest pageRequst) {
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT");
		sql.append("  	GEP.EXAM_PLAN_ID,");
		sql.append("  	GSI.STUDY_YEAR_NAME,");
		sql.append("  	GEB.NAME EXAM_BATCH_NAME,");
		sql.append("  	GC.KCMC,");
		sql.append("  	GC.KCH,");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			TSD. NAME");
		sql.append("  		FROM");
		sql.append("  			TBL_SYS_DATA TSD");
		sql.append("  		WHERE");
		sql.append("  			TSD.IS_DELETED = 'N'");
		sql.append("  		AND TSD.TYPE_CODE = 'ExaminationMode'");
		sql.append("  		AND TSD.CODE = GEP.TYPE");
		sql.append("  	) EXAM_TYPE,");
		sql.append("  	NVL(GEP.EXAM_NO, '--')  EXAM_NO,");
		sql.append("  	NVL(TO_CHAR(GEP.XK_PERCENT), '--') XK_PERCENT,");
		sql.append("  	GEP.BOOK_ST,");
		sql.append("  	GEP.BOOK_END,");
		sql.append("  	GEP.EXAM_ST,");
		sql.append("  	GEP.EXAM_END");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_PLAN_NEW GEP,");
		sql.append("  	GJT_COURSE GC,");
		sql.append("  	GJT_STUDYYEAR_INFO GSI,");
		sql.append("  	GJT_EXAM_BATCH_NEW GEB");
		sql.append("  WHERE");
		sql.append("  	GEP.IS_DELETED = 0 ");
		sql.append("  AND GC.IS_DELETED = 'N' ");
		sql.append("  AND GSI.IS_DELETED = 'N' ");
		sql.append("  AND GEB.IS_DELETED = 0 ");
		sql.append("  AND GEP.COURSE_ID = GC.COURSE_ID");
		sql.append("  AND GEP.STUDY_YEAR_ID = GSI. ID");
		sql.append("  AND GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");

		String examType = ObjectUtils.toString(formMap.get("EQ_type"));// 考试方式
		String examBatchCode = ObjectUtils.toString(formMap.get("EQ_examBatchCode"));// 期/批次
		String studyYearId = ObjectUtils.toString(formMap.get("EQ_studyYearId"));// 学年度id
		String courseId = ObjectUtils.toString(formMap.get("EQ_courseId"));// 课程id
		String examNo = ObjectUtils.toString(formMap.get("LIKE_examNo"));// 试卷号
		String status = ObjectUtils.toString(formMap.get("EQ_status"));// 状态
		String schoolId = ObjectUtils.toString(formMap.get("schoolId"));// 学校id
		Map<String, Object> param = new HashedMap();

		if ("1".equals(status)) {// 未设置: 考试时间为空
			sql.append("  AND GEP.EXAM_ST IS NULL  AND GEP.EXAM_END IS NULL");
		} else if ("2".equals(status)) {// 未开始: 当前时间小于预约开始时间
			sql.append("  AND GEP.BOOK_ST > SYSDATE");
		} else if ("3".equals(status)) {// 预约中: 当前时间在预约时间内
			sql.append("  AND (GEP.BOOK_ST < SYSDATE  AND GEP.BOOK_END > SYSDATE) ");
		} else if ("4".equals(status)) {// 待考试: 当前时间超过预约结束时间,且小于考试时间
			sql.append("  AND (GEP.EXAM_ST > SYSDATE  AND GEP.BOOK_END < SYSDATE) ");
		} else if ("5".equals(status)) {// 考试中: 当前时间在考试时间内
			sql.append("  AND (GEP.EXAM_ST < SYSDATE  AND GEP.EXAM_END > SYSDATE) ");
		} else if ("6".equals(status)) {// 已结束: 当前时间超过考试时间
			sql.append("  AND   GEP.EXAM_END < SYSDATE  ");
		}

		if (EmptyUtils.isNotEmpty(courseId)) {
			sql.append("  AND  GEP.COURSE_ID=:courseId ");
			param.put("courseId", courseId);
		}
		if (EmptyUtils.isNotEmpty(examNo)) {
			sql.append("  AND  GEP.EXAM_NO LIKE '%:examNo%' ");
			param.put("examNo", examNo);
		}
		if (EmptyUtils.isNotEmpty(examType)) {
			sql.append("  AND  GEP.TYPE=:examType ");
			param.put("examType", Integer.parseInt(examType));
		}
		if (EmptyUtils.isNotEmpty(schoolId)) {
			sql.append("  AND  GEP.XX_ID=:schoolId ");
			param.put("schoolId", schoolId);
		}
		if (EmptyUtils.isNotEmpty(examBatchCode)) {
			sql.append("  AND  GEP.EXAM_BATCH_CODE=:examBatchCode ");
			param.put("examBatchCode", examBatchCode);
		}
		if (EmptyUtils.isNotEmpty(studyYearId)) {
			sql.append("  AND  GEP.STUDY_YEAR_ID=:studyYearId ");
			param.put("studyYearId", studyYearId);
		}

		return super.findByPageSql(sql, param, pageRequst, Map.class);
	}

	public List<GjtExamPlanNew> findByExamBatchCodeAndCourseIdAndExamType(String batchCode, String courseId,
			String examType) {
		String sql = "SELECT gepn.*  FROM GJT_EXAM_PLAN_NEW gepn  WHERE IS_DELETED='0' AND gepn.EXAM_BATCH_CODE = :batchCode AND gepn.COURSE_ID = :courseId AND gepn.TYPE = :examType ";
		Query query = em.createNativeQuery(sql, GjtExamPlanNew.class);
		query.setParameter("batchCode", batchCode);
		query.setParameter("courseId", courseId);
		query.setParameter("examType", examType);
		List<GjtExamPlanNew> resultList = query.getResultList();
		return resultList;
	}

	public List<GjtExamPlanNew> findByIsDeletedAndExamNoIsNotNull(int isDeleted) {
		return gjtExamPlanNewRepository.findByIsDeletedAndExamNoIsNotNull(isDeleted);
	}

	public GjtExamPlanNew findByExamBatchCodeAndExamNoAndTypeAndIsDeleted(String examBatchCode, String examNo, int type,
			int isDeleted) {
		return gjtExamPlanNewRepository.findByExamBatchCodeAndExamNoAndTypeAndIsDeleted(examBatchCode, examNo, type,
				isDeleted);
	}

	@Deprecated
	public GjtExamPlanNew findByExamBatchCodeAndExamNoAndTypeAndGjtCourseListCourseIdAndIsDeleted(String examBatchCode,
			String examNo, int type, String courseId, int isDeleted) {
		return gjtExamPlanNewRepository.findByExamBatchCodeAndExamNoAndTypeAndGjtCourseListCourseIdAndIsDeleted(
				examBatchCode, examNo, type, courseId, isDeleted);
	}

	public List<GjtExamPlanNew> findCurrentExamPlanList(String xxId) {
		return gjtExamPlanNewRepository.findCurrentExamPlanList(xxId);
	}

	/**
	 * 保存开考科目与专业对应信息
	 * 
	 * @param examPlanId
	 * @return
	 */
	public boolean existsExamPlanSpecialty(String examPlanId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM GJT_EXAM_PLAN_NEW_SPECIALTY WHERE EXAM_PLAN_ID=:EXAM_PLAN_ID");

		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("EXAM_PLAN_ID", examPlanId);
			return commonDao.queryForCountNative(sql.toString(), params) > 0;
		} catch (TransactionSystemException e) {

		}
		return true;
	}

	/**
	 * 保存开考科目与专业对应信息
	 * 
	 * @param params
	 * @return
	 */
	@Transactional
	public int insertExamPlanSpecialty(Map<String, Object> params) {
		StringBuffer sql = new StringBuffer();

		sql.append("  INSERT");
		sql.append("  	INTO");
		sql.append("  		GJT_EXAM_PLAN_NEW_SPECIALTY(");
		sql.append("  			EXAM_PLAN_ID,");
		sql.append("  			SPECIALTY_ID");
		sql.append("  		)");
		sql.append("  	VALUES(");
		sql.append("  		:EXAM_PLAN_ID,");
		sql.append("  		:SPECIALTY_ID");
		sql.append("  	)");

		int re = 0;
		try {
			re = commonDao.insertForMapNative(sql.toString(), params);
		} catch (TransactionSystemException e) {
			System.out.println("\t" + params);
		}

		return re;
	}

	/**
	 * 企业大学接口--查询考试管理信息
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page getExamManagmentList(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT GEP.EXAM_BATCH_CODE,");
		sql.append("  GEP.EXAM_PLAN_ID,");
		sql.append("  GEB.NAME,");
		sql.append("  GEP.EXAM_PLAN_NAME,");
		sql.append("  TO_CHAR(GEP.EXAM_ST,'yyyy-MM-dd hh24:mi:ss')EXAM_ST,");
		sql.append("  TO_CHAR(GEP.EXAM_END,'yyyy-MM-dd hh24:mi:ss')EXAM_END,");
		sql.append("  WM_CONCAT(GS.ZYMC) ZYMC,");
		sql.append("  GCC.COURSE_ID,");
		sql.append("  GC.KCMC,");
		sql.append(
				"  (CASE WHEN TO_CHAR(GEB.BOOK_ST,'yyyy-MM-dd hh24:mi:ss') >TO_CHAR(SYSDATE,'yyyy-MM-dd hh24:mi:ss') THEN '未开始'");
		sql.append("  WHEN TO_CHAR(GEB.BOOK_END,'yyyy-MM-dd hh24:mi:ss') >=TO_CHAR(SYSDATE,'yyyy-MM-dd hh24:mi:ss')");
		sql.append(
				"  AND TO_CHAR(GEB.BOOK_ST,'yyyy-MM-dd hh24:mi:ss') <=TO_CHAR(SYSDATE,'yyyy-MM-dd hh24:mi:ss') THEN '预约中'");
		sql.append("  WHEN  TO_CHAR(GEB.BOOK_END,'yyyy-MM-dd hh24:mi:ss') <TO_CHAR(SYSDATE,'yyyy-MM-dd hh24:mi:ss')");
		sql.append(
				"  AND TO_CHAR(GEP.EXAM_ST,'yyyy-MM-dd hh24:mi:ss') >TO_CHAR(SYSDATE,'yyyy-MM-dd hh24:mi:ss') THEN '待考试'");
		sql.append("  WHEN TO_CHAR(GEP.EXAM_ST,'yyyy-MM-dd hh24:mi:ss') <=TO_CHAR(SYSDATE,'yyyy-MM-dd hh24:mi:ss') ");
		sql.append(
				"  AND TO_CHAR(GEP.EXAM_END,'yyyy-MM-dd hh24:mi:ss') >TO_CHAR(SYSDATE,'yyyy-MM-dd hh24:mi:ss') THEN '考试中'");
		sql.append("  ELSE '已结束' END) EXAM_STATUS,");
		sql.append("  GEP.TYPE,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.TYPE_CODE = 'ExaminationMode'");
		sql.append("  AND TSD.CODE = GEP.TYPE AND TSD.IS_DELETED='N') EXAM_TYPE,");
		sql.append("  (SELECT COUNT(*)");
		sql.append("  FROM GJT_EXAM_APPOINTMENT_NEW GEA");
		sql.append("  WHERE GEA.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  AND GEA.IS_DELETED = 0) APPOINTMENT_NUM,");
		sql.append("  (SELECT COUNT(*)");
		sql.append("  FROM GJT_EXAM_APPOINTMENT_NEW GG");
		sql.append("  INNER JOIN GJT_REC_RESULT GRR");
		sql.append("  ON GRR.STUDENT_ID = GG.STUDENT_ID");
		sql.append("  WHERE GG.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  AND GRR.COURSE_ID = GCC.COURSE_ID");
		sql.append("  AND GRR.IS_DELETED = 'N' AND GRR.EXAM_SCORE1 IS NOT NULL) EXAM_NUM");
		sql.append("  FROM GJT_EXAM_PLAN_NEW GEP");
		sql.append("  LEFT JOIN GJT_EXAM_BATCH_NEW GEB");
		sql.append("  ON GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  AND GEB.IS_DELETED = 0");
		sql.append("  LEFT JOIN GJT_EXAM_PLAN_NEW_SPECIALTY GEPN");
		sql.append("  ON GEPN.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  LEFT JOIN GJT_SPECIALTY GS");
		sql.append("  ON GS.SPECIALTY_ID = GEPN.SPECIALTY_ID");
		sql.append("  AND GS.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_EXAM_PLAN_NEW_COURSE GCC");
		sql.append("  ON GCC.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  LEFT JOIN GJT_COURSE GC");
		sql.append("  ON GC.COURSE_ID = GCC.COURSE_ID");
		sql.append("  AND GC.IS_DELETED = 'N'");
		sql.append("  WHERE GEP.IS_DELETED = 0");
		// 院校ID
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XX_ID")))) {
			sql.append("  AND GEP.XX_ID= :XX_ID");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		// 考试计划
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))) {
			sql.append("  AND GEP.EXAM_BATCH_CODE = :EXAM_BATCH_CODE");
			params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}
		// 开考项目名称
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_PLAN_NAME")))) {
			sql.append("  AND GEP.EXAM_PLAN_NAME LIKE :EXAM_PLAN_NAME ");
			params.put("EXAM_PLAN_NAME", "%" + ObjectUtils.toString(searchParams.get("EXAM_PLAN_NAME")) + "%");
		}
		// 课程
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("COURSE_ID")))) {
			sql.append("  AND GC.COURSE_ID = :COURSE_ID");
			params.put("COURSE_ID", ObjectUtils.toString(searchParams.get("COURSE_ID")));
		}
		// 考试形式
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("TYPE")))) {
			sql.append("  AND GEP.TYPE = :TYPE");
			params.put("TYPE", ObjectUtils.toString(searchParams.get("TYPE")));
		}
		//// 状态：1-未开始，2-预约中，3-待考试，4-考试中，5-已结束
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_STATUS")))) {
			if (("1").equals(ObjectUtils.toString(searchParams.get("EXAM_STATUS")))) {
				sql.append(
						"  AND TO_CHAR(GEB.BOOK_ST,'yyyy-MM-dd hh24:mi:ss') >TO_CHAR(SYSDATE,'yyyy-MM-dd hh24:mi:ss')");
			} else if (("2").equals(ObjectUtils.toString(searchParams.get("EXAM_STATUS")))) {
				sql.append(
						"  AND TO_CHAR(GEB.BOOK_END,'yyyy-MM-dd hh24:mi:ss') >=TO_CHAR(SYSDATE,'yyyy-MM-dd hh24:mi:ss')");
				sql.append(
						"  AND TO_CHAR(GEB.BOOK_ST,'yyyy-MM-dd hh24:mi:ss') <=TO_CHAR(SYSDATE,'yyyy-MM-dd hh24:mi:ss')");
			} else if (("3").equals(ObjectUtils.toString(searchParams.get("EXAM_STATUS")))) {
				sql.append(
						"  AND TO_CHAR(GEB.BOOK_END,'yyyy-MM-dd hh24:mi:ss') <TO_CHAR(SYSDATE,'yyyy-MM-dd hh24:mi:ss')");
				sql.append(
						"  AND TO_CHAR(GEP.EXAM_ST,'yyyy-MM-dd hh24:mi:ss') >TO_CHAR(SYSDATE,'yyyy-MM-dd hh24:mi:ss')");
			} else if (("4").equals(ObjectUtils.toString(searchParams.get("EXAM_STATUS")))) {
				sql.append(
						"  AND TO_CHAR(GEP.EXAM_ST,'yyyy-MM-dd hh24:mi:ss') <=TO_CHAR(SYSDATE,'yyyy-MM-dd hh24:mi:ss')");
				sql.append(
						"  AND TO_CHAR(GEP.EXAM_END,'yyyy-MM-dd hh24:mi:ss') >TO_CHAR(SYSDATE,'yyyy-MM-dd hh24:mi:ss')");
			} else {
				sql.append(
						"  AND TO_CHAR(GEP.EXAM_END,'yyyy-MM-dd hh24:mi:ss') <TO_CHAR(SYSDATE,'yyyy-MM-dd hh24:mi:ss')");
			}
		}
		sql.append("  GROUP BY GEP.EXAM_BATCH_CODE,");
		sql.append("  GEP.EXAM_PLAN_ID,");
		sql.append("  GEB.NAME,");
		sql.append("  GEP.EXAM_PLAN_NAME,");
		sql.append("  GEP.EXAM_END,");
		sql.append("  GC.KCMC,");
		sql.append("  GEP.EXAM_ST,");
		sql.append("  GEP.TYPE,");
		sql.append("  GCC.COURSE_ID,");
		sql.append("  GEB.BOOK_ST,");
		sql.append("  GEB.BOOK_END");
		return (Page) commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	/**
	 * 企业大学接口--查询排考记录
	 * 
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> getArrangeExamRecordList(Map<String, Object> searchParams,
			PageRequest pageRequst) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT GEP.EXAM_BATCH_CODE,");
		sql.append("  GEP.EXAM_PLAN_ID,");
		sql.append("  GEB.NAME,");
		sql.append("  GEP.EXAM_PLAN_NAME,");
		sql.append("  GSI.XM,");
		sql.append("  GSI.XH,");
		sql.append("  GSI.SJH,");
		sql.append("  GSI.STUDENT_ID,");
		sql.append("  (SELECT TBD.NAME");
		sql.append("  FROM TBL_SYS_DATA TBD");
		sql.append("  WHERE TBD.TYPE_CODE = 'TrainingLevel'");
		sql.append("  AND TBD.IS_DELETED = 'N'");
		sql.append("  AND TBD.CODE = GSI.PYCC) PYCC_NAME,");
		sql.append("  GY.NAME YEAR_NAME,");
		sql.append("  GG.GRADE_ID,");
		sql.append("  GG.GRADE_NAME,");
		sql.append("  GS.SPECIALTY_ID,");
		sql.append("  GS.ZYMC,");
		sql.append("  GEPP.NAME EXAM_CENTRE_NAME,");
		sql.append("  GERN.NAME EXAM_ROMM_NAME,");
		sql.append("  DECODE(GEAN.STATUS, '0', '未排考','1','已排考','已过期') RANK_EXAM_STATUS");
		sql.append("  FROM GJT_EXAM_PLAN_NEW GEP");
		sql.append("  LEFT JOIN GJT_EXAM_BATCH_NEW GEB");
		sql.append("  ON GEP.EXAM_BATCH_CODE = GEB.EXAM_BATCH_CODE");
		sql.append("  AND GEB.IS_DELETED = 0");
		sql.append("  LEFT JOIN GJT_EXAM_APPOINTMENT_NEW GEAN");
		sql.append("  ON GEAN.EXAM_PLAN_ID = GEP.EXAM_PLAN_ID");
		sql.append("  AND GEAN.IS_DELETED = 0");
		sql.append("  LEFT JOIN GJT_EXAM_STUDENT_ROOM_NEW GESR");
		sql.append("  ON GESR.APPOINTMENT_ID = GEAN.APPOINTMENT_ID");
		sql.append("  LEFT JOIN GJT_STUDENT_INFO GSI");
		sql.append("  ON GSI.STUDENT_ID = GEAN.STUDENT_ID");
		sql.append("  AND GSI.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_SPECIALTY GS");
		sql.append("  ON GS.SPECIALTY_ID = GSI.MAJOR");
		sql.append("  AND GS.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_GRADE GG");
		sql.append("  ON GG.GRADE_ID = GSI.NJ");
		sql.append("  AND GG.IS_DELETED = 'N'");
		sql.append("  LEFT JOIN GJT_YEAR GY");
		sql.append("  ON GY.GRADE_ID = GG.YEAR_ID");
		sql.append("  LEFT JOIN GJT_EXAM_ROOM_NEW GERN");
		sql.append("  ON GERN.EXAM_ROOM_ID = GESR.EXAM_ROOM_ID");
		sql.append("  AND GERN.IS_DELETED = 0");
		sql.append("  LEFT JOIN GJT_EXAM_POINT_NEW GEPP");
		sql.append("  ON GEPP.EXAM_POINT_ID= GESR.EXAM_POINT_ID");
		sql.append("  AND GEPP.IS_DELETED = 'N'");
		sql.append("  WHERE GEP.IS_DELETED = 0");

		/**
		 * 必填参数查询
		 */
		// 院校ID
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XX_ID")))) {
			sql.append("  AND GEP.XX_ID= :XX_ID");
			params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		}
		// 考试科目ID
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")))) {
			sql.append("  AND GEP.EXAM_PLAN_ID =:EXAM_PLAN_ID");
			params.put("EXAM_PLAN_ID", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));
		}
		// 考试批次
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))) {
			sql.append("  AND GEP.EXAM_BATCH_CODE =:EXAM_BATCH_CODE");
			params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		/**
		 * 搜索条件查询
		 */
		// 姓名
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("XM")))) {
			sql.append("  AND GSI.XM LIKE :XM ");
			params.put("XM", "%" + ObjectUtils.toString(searchParams.get("XM")) + "%");
		}
		// 学期
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GG.GRADE_ID =:GRADE_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		// 专业
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("SPECIALTY_ID")))) {
			sql.append("  AND GS.SPECIALTY_ID =:SPECIALTY_ID");
			params.put("SPECIALTY_ID", ObjectUtils.toString(searchParams.get("SPECIALTY_ID")));
		}
		return (Page) commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	/**
	 * 企业大学接口--查看学员的准考证信息
	 * 
	 * @param searchParams
	 * @return
	 */
	public List<Map<String, String>> getStudentAdmissionTicket(Map<String, Object> searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT GSI.XM,");
		sql.append("  GSI.XH,");
		sql.append("  GEBN.NAME,");
		sql.append("  GEPN.EXAM_BATCH_CODE,");
		sql.append("  GEPN.EXAM_PLAN_NAME,");
		sql.append("  (SELECT TSD.NAME");
		sql.append("  FROM TBL_SYS_DATA TSD");
		sql.append("  WHERE TSD.TYPE_CODE = 'ExaminationMode'");
		sql.append("  AND TSD.CODE = GEPN.TYPE AND TSD.IS_DELETED='N') EXAM_TYPE,");
		sql.append("  GEPN.EXAM_NO,");
		sql.append("  TO_CHAR(GEPN.EXAM_ST,'YYYY-MM-DD')EXAM_ST,");
		sql.append("  (TO_CHAR(GEPN.EXAM_ST,'HH24:MI')||'-'||TO_CHAR(GEPN.EXAM_END,'HH24:MI'))EXAM_TIME,");
		sql.append("  GEPP.NAME EXAM_CENTRE_NAME,");
		sql.append("  GERN.NAME EXAM_ROMM_NAME,");
		sql.append("  GESRN.SEAT_NO,");
		sql.append("  GEPP.ADDRESS");
		sql.append("  FROM GJT_STUDENT_INFO GSI");
		sql.append("  LEFT JOIN GJT_EXAM_APPOINTMENT_NEW GEAN");
		sql.append("  ON GSI.STUDENT_ID = GEAN.STUDENT_ID");
		sql.append("  AND GEAN.IS_DELETED = 0");
		sql.append("  LEFT JOIN GJT_EXAM_PLAN_NEW GEPN");
		sql.append("  ON GEPN.EXAM_PLAN_ID = GEAN.EXAM_PLAN_ID");
		sql.append("  AND GEPN.IS_DELETED = 0");
		sql.append("  LEFT JOIN GJT_EXAM_BATCH_NEW GEBN");
		sql.append("  ON GEBN.EXAM_BATCH_CODE = GEPN.EXAM_BATCH_CODE");
		sql.append("  AND GEBN.IS_DELETED = 0");
		sql.append("  LEFT JOIN GJT_EXAM_STUDENT_ROOM_NEW GESRN");
		sql.append("  ON GESRN.APPOINTMENT_ID = GEAN.APPOINTMENT_ID");
		sql.append("  LEFT JOIN GJT_EXAM_ROOM_NEW GERN");
		sql.append("  ON GERN.EXAM_ROOM_ID = GESRN.EXAM_ROOM_ID");
		sql.append("  AND GERN.IS_DELETED = 0");
		sql.append("  LEFT JOIN GJT_EXAM_POINT_NEW GEPP");
		sql.append("  ON GEPP.EXAM_POINT_ID = GESRN.EXAM_POINT_ID");
		sql.append("  AND GEPP.IS_DELETED = 'N'");
		sql.append("  WHERE GSI.IS_DELETED='N'");
		// 考试科目ID
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")))) {
			sql.append("  AND GEAN.EXAM_PLAN_ID =:EXAM_PLAN_ID");
			params.put("EXAM_PLAN_ID", ObjectUtils.toString(searchParams.get("EXAM_PLAN_ID")));
		}
		// 学生ID
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDENT_ID")))) {
			sql.append(" AND GSI.STUDENT_ID =:STUDENT_ID");
			params.put("STUDENT_ID", ObjectUtils.toString(searchParams.get("STUDENT_ID")));
		}
		return commonDao.queryForMapListNative(sql.toString(), params);
	}

	/**
	 * 查询考试计划
	 * 
	 * @param searchParams
	 * @return
	 */
	public List getExamBatchList(Map searchParams) {
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT");
		sql.append("  	GEB.EXAM_BATCH_ID,");
		sql.append("  	GEB.EXAM_BATCH_CODE,");
		sql.append("  	GEB.NAME EXAM_BATCH_NAME,");
		sql.append("  	GG.GRADE_ID,");
		sql.append("  	GG.GRADE_NAME,");
		sql.append("  	TO_CHAR(GEB.BOOK_ST,'yyyy-MM-dd') BOOK_ST,");
		sql.append("  	TO_CHAR(GEB.BOOK_END,'yyyy-MM-dd') BOOK_END,");
		sql.append("  	TO_CHAR(GEB.ARRANGE_ST,'yyyy-MM-dd') ARRANGE_ST,");
		sql.append("  	TO_CHAR(GEB.ARRANGE_END,'yyyy-MM-dd') ARRANGE_END,");
		sql.append("  	TO_CHAR(GEB.ONLINE_ST,'yyyy-MM-dd') ONLINE_ST,");
		sql.append("  	TO_CHAR(GEB.ONLINE_END,'yyyy-MM-dd') ONLINE_END,");
		sql.append("  	TO_CHAR(GEB.PROVINCE_ONLINE_ST,'yyyy-MM-dd') PROVINCE_ONLINE_ST,");
		sql.append("  	TO_CHAR(GEB.PROVINCE_ONLINE_END,'yyyy-MM-dd') PROVINCE_ONLINE_END,");
		sql.append("  	TO_CHAR(GEB.PAPER_ST,'yyyy-MM-dd') PAPER_ST,");
		sql.append("  	TO_CHAR(GEB.PAPER_END,'yyyy-MM-dd') PAPER_END,");
		sql.append("  	TO_CHAR(GEB.MACHINE_ST,'yyyy-MM-dd') MACHINE_ST,");
		sql.append("  	TO_CHAR(GEB.MACHINE_END,'yyyy-MM-dd') MACHINE_END,");
		sql.append("  	TO_CHAR(GEB.SHAPE_END,'yyyy-MM-dd') SHAPE_END,");
		sql.append("  	TO_CHAR(GEB.THESIS_END,'yyyy-MM-dd') THESIS_END,");
		sql.append("  	TO_CHAR(GEB.REPORT_END,'yyyy-MM-dd') REPORT_END,");
		sql.append("  	TO_CHAR(GEB.RECORD_ST,'yyyy-MM-dd') RECORD_ST,");
		sql.append("  	TO_CHAR(GEB.RECORD_END,'yyyy-MM-dd') RECORD_END,");
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN GEB.RECORD_END IS NOT NULL");
		sql.append("  			AND SYSDATE > GEB.RECORD_END THEN '4'");
		sql.append("  			ELSE TO_CHAR( GEB.PLAN_STATUS )");
		sql.append("  		END");
		sql.append("  	) PLAN_STATUS");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_BATCH_NEW GEB");
		sql.append("  LEFT JOIN GJT_GRADE GG ON");
		sql.append("  	GEB.GRADE_ID = GG.GRADE_ID");
		sql.append("  	AND GG.IS_DELETED = 'N'");
		sql.append("  WHERE");
		sql.append("  	GEB.IS_DELETED = 0");
		sql.append("  	AND GEB.XX_ID IN(");
		sql.append("  	SELECT");
		sql.append("  		org.ID");
		sql.append("  	FROM");
		sql.append("  		GJT_ORG org");
		sql.append("  	WHERE");
		// sql.append(" org.IS_DELETED = 'N' START WITH org.ID =:xxId CONNECT BY
		// PRIOR ORG.ID = ORG.PERENT_ID");

		sql.append("  			org.IS_DELETED = 'N' ");
		if (EmptyUtils.isNotEmpty(searchParams.get("XXZX_CODE"))) {
			sql.append("  START WITH org.CODE = :XXZX_CODE");
			params.put("XXZX_CODE", ObjectUtils.toString(searchParams.get("XXZX_CODE")));
		} else {
			sql.append("  START WITH org.ID = :XXZX_ID");
			params.put("XXZX_ID", ObjectUtils.toString(searchParams.get("XXZX_ID")));
		}
		sql.append("  	CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID ");
		sql.append("  	)");

		if (EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))) {
			sql.append("  	AND GEB.EXAM_BATCH_CODE = :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE", ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")));
		}

		if (EmptyUtils.isNotEmpty(searchParams.get("GRADE_ID"))) {
			sql.append("  	AND GG.GRADE_ID = :GRADE_ID ");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}

		return commonDao.queryForMapListNative(sql.toString(), params);

	}

	/**
	 * 自动生成开考科目
	 * 
	 * @param xxId
	 * @return
	 */
	public List<Map<String, Object>> getExamPlanByTeachPlan(String xxId) {
		Map<String, Object> param = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select t.EXAM_NO,");
		sql.append("  wm_concat(t.SOURCE_COURSE_ID) course_id,");
		sql.append(" wm_concat(t.KKZY) specialty_id");
		sql.append("  from view_teach_plan t");
		sql.append("  where t.EXAM_NO is not null");
		sql.append("  and t.XX_ID=:xxId");

		param.put("xxId", xxId);

		sql.append("  group by t.EXAM_NO");
		return commonDao.queryForStringObjectMapListNative(sql.toString(), param);
	}

	public List<Map<String, Object>> getExamPlanByTeachPlanReplace(String xxId) {
		Map<String, Object> param = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select t.REPLACE_EXAM_NO,");
		sql.append("  wm_concat(t.COURSE_ID) course_id,");
		sql.append(" wm_concat(t.KKZY) specialty_id");
		sql.append("  from view_teach_plan t");
		sql.append("  where t.REPLACE_EXAM_NO is not null and t.SOURCE_COURSE_ID!=t.COURSE_ID");
		sql.append("  and t.XX_ID=:xxId");
		param.put("xxId", xxId);
		sql.append("  group by t.REPLACE_EXAM_NO");
		return commonDao.queryForStringObjectMapListNative(sql.toString(), param);
	}

}
