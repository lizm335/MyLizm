package com.ouchgzee.headTeacher.dao.exam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.ouchgzee.headTeacher.dao.comm.CommonDao;
import com.ouchgzee.headTeacher.dao.exam.repository.GjtExamBatchNewRepository;
import com.ouchgzee.headTeacher.pojo.exam.BzrGjtExamBatchNew;

@Deprecated @Repository("bzrGjtExamBatchNewDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtExamBatchNewDao {

	@PersistenceContext(unitName = "entityManagerFactoryBzr")
	public EntityManager em;

	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private GjtExamBatchNewRepository gjtExamBatchNewRepository;

	public List<BzrGjtExamBatchNew> findAll(Specification<BzrGjtExamBatchNew> spec) {
		return gjtExamBatchNewRepository.findAll(spec);
	}

	public Page<BzrGjtExamBatchNew> findAll(Specification<BzrGjtExamBatchNew> spec, PageRequest pageRequst) {
		return gjtExamBatchNewRepository.findAll(spec, pageRequst);
	}

	public BzrGjtExamBatchNew save(BzrGjtExamBatchNew entity) {
		return gjtExamBatchNewRepository.save(entity);
	}

	public BzrGjtExamBatchNew findOne(String id) {
		return gjtExamBatchNewRepository.findOne(id);
	}

	public List<BzrGjtExamBatchNew> save(List<BzrGjtExamBatchNew> list) {
		return gjtExamBatchNewRepository.save(list);
	}

	public BzrGjtExamBatchNew queryByExamBatchCodeAndXxId(String examBatchCode, String xxid) {
		return gjtExamBatchNewRepository.findByExamBatchCodeAndXxId(examBatchCode, xxid);
	}

	public BzrGjtExamBatchNew queryByExamBatchCode(String examBatchCode) {
		return gjtExamBatchNewRepository.findByExamBatchCodeAndIsDeleted(examBatchCode, 0);
//		return gjtExamBatchNewRepository.findByExamBatchCodeAndIsDeleted(examBatchCode, "N");
	}

	public BzrGjtExamBatchNew findByStudyYearIdAndXxIdAndIsDeleted(String studyYearId, String xxId) {
		return gjtExamBatchNewRepository.findByStudyYearIdAndXxIdAndIsDeleted(studyYearId, xxId, 0);
//		return gjtExamBatchNewRepository.findByStudyYearIdAndXxIdAndIsDeleted(studyYearId, xxId, "N");
	}
	
	public BzrGjtExamBatchNew findByGradeIdAndXxId(String gradeId, String xxId) {
		return gjtExamBatchNewRepository.findByGradeIdAndXxIdAndIsDeleted(gradeId, xxId, 0);
	}
	
	public List<BzrGjtExamBatchNew> findByPlanStatusAndXxIdAndIsDeletedAndRecordEndAfter(String planStatus, String xxId, int isDeleted, Date date) {
		return gjtExamBatchNewRepository.findByPlanStatusAndXxIdAndIsDeletedAndRecordEndAfter(planStatus, xxId, isDeleted, date);
	}
	
	public List<BzrGjtExamBatchNew> findCurrentExamBatchList(String xxId) {
		return gjtExamBatchNewRepository.findCurrentExamBatchList(xxId);
	}
	
	public List<BzrGjtExamBatchNew> findExamBatchList(String xxId) {
		return gjtExamBatchNewRepository.findExamBatchList(xxId);
	}
	
	public BzrGjtExamBatchNew findByExamBatchCodeAndIsDeleted(String examBatchCode, int isDeleted) {
		return gjtExamBatchNewRepository.findByExamBatchCodeAndIsDeleted(examBatchCode, isDeleted);
	}

	@Transactional(value="transactionManagerBzr")
	public int deleteGjtExamBatchNew(List<String> ids, String xxid) {
		int rs = 0;
		if (ids.size() > 0) {
			StringBuilder sbuilder = new StringBuilder();
			sbuilder.append("update " + "				GJT_EXAM_BATCH_NEW " + "			set "
					+ "				IS_DELETED=1" + "			where " + "				BOOK_ST>sysdate and XX_ID='");
//					+ "				IS_DELETED='N'" + "			where " + "				BOOK_ST>sysdate and XX_ID='");
			sbuilder.append(xxid);
			sbuilder.append("' ");
			if (null != ids && ids.size() > 0) {
				sbuilder.append(" and EXAM_BATCH_ID in ('");
				sbuilder.append(ids.get(0));
				sbuilder.append("'");
				for (int i = 1; i < ids.size(); i++) {
					sbuilder.append(", '");
					sbuilder.append(ids.get(i));
					sbuilder.append("'");
				}
				sbuilder.append(")");

				Query query = em.createNativeQuery(sbuilder.toString());
				rs = query.executeUpdate();
			}
		}
		return rs;
	}
	
	/**
	 * 查询批次列表
	 * @return
	 */
	public Page getExamBatchList(Map searchParams, PageRequest pageRequst){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT *");
		sql.append("  FROM (SELECT GEB.EXAM_BATCH_ID,");
		sql.append("  GEB.EXAM_BATCH_CODE,");
		sql.append("  GEB.NAME,");
		sql.append("  GEB.STUDY_YEAR_ID,");
		sql.append("  (SELECT GSI.STUDY_YEAR_NAME");
		sql.append("  FROM GJT_STUDYYEAR_INFO GSI");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GSI.ID = GEB.STUDY_YEAR_ID) STUDY_YEAR_NAME,");
		sql.append("  TO_CHAR(GEB.BOOK_ST, 'yyyy-mm-dd hh24:mi:ss') BOOK_ST,");
		sql.append("  TO_CHAR(GEB.BOOK_END, 'yyyy-mm-dd hh24:mi:ss') BOOK_END,");
		sql.append("  TO_CHAR(GEB.OFFLINE_ST, 'yyyy-mm-dd hh24:mi:ss') OFFLINE_ST,");
		sql.append("  TO_CHAR(GEB.OFFLINE_END, 'yyyy-mm-dd hh24:mi:ss') OFFLINE_END,");
		sql.append("  TO_CHAR(GEB.ONLINE_ST, 'yyyy-mm-dd hh24:mi:ss') ONLINE_ST,");
		sql.append("  TO_CHAR(GEB.ONLINE_END, 'yyyy-mm-dd hh24:mi:ss') ONLINE_END,");
		sql.append("  CASE");
		sql.append("  WHEN TO_CHAR(SYSDATE, 'yyyy-mm-dd') <");
		sql.append("  TO_CHAR(GEB.BOOK_ST, 'yyyy-mm-dd') THEN");
		sql.append("  '1'");
		
		sql.append("  WHEN (TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEB.BOOK_ST, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(GEB.BOOK_END, 'yyyy-mm-dd')) THEN");
		sql.append("  '2'");
		
		sql.append("  WHEN (TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEB.BOOK_END, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(GEB.OFFLINE_ST, 'yyyy-mm-dd')) THEN");
		sql.append("  '3'");
		
		sql.append("  WHEN (TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEB.BOOK_END, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(GEB.ONLINE_ST, 'yyyy-mm-dd')) THEN");
		sql.append("  '3'");
		
		sql.append("  WHEN (TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEB.OFFLINE_ST, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(GEB.OFFLINE_END, 'yyyy-mm-dd')) THEN");
		sql.append("  '4'");
		
		sql.append("  WHEN (TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEB.ONLINE_ST, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(GEB.ONLINE_END, 'yyyy-mm-dd')) THEN");
		sql.append("  '4'");
		
		sql.append("  WHEN TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEB.OFFLINE_END, 'yyyy-mm-dd') THEN");
		sql.append("  '5'");
		
		sql.append("  WHEN TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEB.ONLINE_END, 'yyyy-mm-dd') THEN");
		sql.append("  '5'");
		
		sql.append("  END BSTATUS");
		sql.append("  FROM GJT_EXAM_BATCH_NEW GEB");
		sql.append("  WHERE GEB.IS_DELETED = 0");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))) {
			sql.append("  AND GEB.EXAM_BATCH_CODE LIKE :EXAM_BATCH_CODE");
			param.put("EXAM_BATCH_CODE", "%"+ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("NAME")))) {
			sql.append("  AND GEB.NAME LIKE :NAME");
			param.put("NAME", "%"+ObjectUtils.toString(searchParams.get("NAME"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_YEAR_ID")))) {
			sql.append("  AND GEB.STUDY_YEAR_ID = :STUDY_YEAR_ID");
			param.put("STUDY_YEAR_ID", ObjectUtils.toString(searchParams.get("STUDY_YEAR_ID")));
		}
		sql.append("  AND GEB.XX_ID = :XX_ID");
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BOOK_ST")))) {
			sql.append("  AND TO_CHAR(GEB.BOOK_ST, 'yyyy-mm-dd') <= :BOOK_ST");
			param.put("BOOK_ST", ObjectUtils.toString(searchParams.get("BOOK_ST")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BOOK_END")))) {
			sql.append("  AND TO_CHAR(GEB.BOOK_END, 'yyyy-mm-dd') >= :BOOK_END");
			param.put("BOOK_END", ObjectUtils.toString(searchParams.get("BOOK_END")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("OFFLINE_ST")))) {
			sql.append("  AND TO_CHAR(GEB.OFFLINE_ST, 'yyyy-mm-dd') <= :OFFLINE_ST");
			param.put("OFFLINE_ST", ObjectUtils.toString(searchParams.get("OFFLINE_ST")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("OFFLINE_END")))) {
			sql.append("  AND TO_CHAR(GEB.OFFLINE_END, 'yyyy-mm-dd') >= :OFFLINE_END");
			param.put("OFFLINE_END", ObjectUtils.toString(searchParams.get("OFFLINE_END")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ONLINE_ST")))) {
			sql.append("  AND TO_CHAR(GEB.ONLINE_ST, 'yyyy-mm-dd') <= :ONLINE_ST");
			param.put("ONLINE_ST", ObjectUtils.toString(searchParams.get("ONLINE_ST")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ONLINE_END")))) {
			sql.append("  AND TO_CHAR(GEB.ONLINE_END, 'yyyy-mm-dd') >= :ONLINE_END");
			param.put("ONLINE_END", ObjectUtils.toString(searchParams.get("ONLINE_END")));
		}
		
		sql.append(" ) TAB");
		sql.append("  WHERE 1 = 1");
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BSTATUS")))) {
			sql.append("  AND BSTATUS = :BSTATUS");
			param.put("BSTATUS", ObjectUtils.toString(searchParams.get("BSTATUS")));
		}
		
		return commonDao.queryForPageNative(sql.toString(), param, pageRequst);
	}
	
	/**
	 * 查询批次列表统计项
	 * @return
	 */
	public int getExamBatchCount(Map searchParams){
		Map param = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT COUNT(EXAM_BATCH_ID) BATCH_COUNT");
		sql.append("  FROM (SELECT GEB.EXAM_BATCH_ID,");
		sql.append("  GEB.EXAM_BATCH_CODE,");
		sql.append("  GEB.NAME,");
		sql.append("  GEB.STUDY_YEAR_ID,");
		sql.append("  (SELECT GSI.STUDY_YEAR_NAME");
		sql.append("  FROM GJT_STUDYYEAR_INFO GSI");
		sql.append("  WHERE GSI.IS_DELETED = 'N'");
		sql.append("  AND GSI.ID = GEB.STUDY_YEAR_ID) STUDY_YEAR_NAME,");
		sql.append("  TO_CHAR(GEB.BOOK_ST, 'yyyy-mm-dd hh24:mi:ss') BOOK_ST,");
		sql.append("  TO_CHAR(GEB.BOOK_END, 'yyyy-mm-dd hh24:mi:ss') BOOK_END,");
		sql.append("  TO_CHAR(GEB.OFFLINE_ST, 'yyyy-mm-dd hh24:mi:ss') OFFLINE_ST,");
		sql.append("  TO_CHAR(GEB.OFFLINE_END, 'yyyy-mm-dd hh24:mi:ss') OFFLINE_END,");
		sql.append("  TO_CHAR(GEB.ONLINE_ST, 'yyyy-mm-dd hh24:mi:ss') ONLINE_ST,");
		sql.append("  TO_CHAR(GEB.ONLINE_END, 'yyyy-mm-dd hh24:mi:ss') ONLINE_END,");
		sql.append("  CASE");
		sql.append("  WHEN TO_CHAR(SYSDATE, 'yyyy-mm-dd') <");
		sql.append("  TO_CHAR(GEB.BOOK_ST, 'yyyy-mm-dd') THEN");
		sql.append("  '1'");
		
		sql.append("  WHEN (TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEB.BOOK_ST, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(GEB.BOOK_END, 'yyyy-mm-dd')) THEN");
		sql.append("  '2'");
		
		sql.append("  WHEN (TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEB.BOOK_END, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(GEB.OFFLINE_ST, 'yyyy-mm-dd')) THEN");
		sql.append("  '3'");
		
		sql.append("  WHEN (TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEB.BOOK_END, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(GEB.ONLINE_ST, 'yyyy-mm-dd')) THEN");
		sql.append("  '3'");
		
		sql.append("  WHEN (TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEB.OFFLINE_ST, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(GEB.OFFLINE_END, 'yyyy-mm-dd')) THEN");
		sql.append("  '4'");
		
		sql.append("  WHEN (TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEB.ONLINE_ST, 'yyyy-mm-dd') AND");
		sql.append("  TO_CHAR(SYSDATE, 'yyyy-mm-dd') <=");
		sql.append("  TO_CHAR(GEB.ONLINE_END, 'yyyy-mm-dd')) THEN");
		sql.append("  '4'");
		
		sql.append("  WHEN TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEB.OFFLINE_END, 'yyyy-mm-dd') THEN");
		sql.append("  '5'");
		
		sql.append("  WHEN TO_CHAR(SYSDATE, 'yyyy-mm-dd') >=");
		sql.append("  TO_CHAR(GEB.ONLINE_END, 'yyyy-mm-dd') THEN");
		sql.append("  '5'");
		
		sql.append("  END BSTATUS");
		sql.append("  FROM GJT_EXAM_BATCH_NEW GEB");
		sql.append("  WHERE GEB.IS_DELETED = 0");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))) {
			sql.append("  AND GEB.EXAM_BATCH_CODE LIKE :EXAM_BATCH_CODE");
			param.put("EXAM_BATCH_CODE", "%"+ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("NAME")))) {
			sql.append("  AND GEB.NAME LIKE :NAME");
			param.put("NAME", "%"+ObjectUtils.toString(searchParams.get("NAME"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("STUDY_YEAR_ID")))) {
			sql.append("  AND GEB.STUDY_YEAR_ID = :STUDY_YEAR_ID");
			param.put("STUDY_YEAR_ID", ObjectUtils.toString(searchParams.get("STUDY_YEAR_ID")));
		}
		sql.append("  AND GEB.XX_ID = :XX_ID");
		param.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BOOK_ST")))) {
			sql.append("  AND TO_CHAR(GEB.BOOK_ST, 'yyyy-mm-dd') <= :BOOK_ST");
			param.put("BOOK_ST", ObjectUtils.toString(searchParams.get("BOOK_ST")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BOOK_END")))) {
			sql.append("  AND TO_CHAR(GEB.BOOK_END, 'yyyy-mm-dd') >= :BOOK_END");
			param.put("BOOK_END", ObjectUtils.toString(searchParams.get("BOOK_END")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("OFFLINE_ST")))) {
			sql.append("  AND TO_CHAR(GEB.OFFLINE_ST, 'yyyy-mm-dd') <= :OFFLINE_ST");
			param.put("OFFLINE_ST", ObjectUtils.toString(searchParams.get("OFFLINE_ST")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("OFFLINE_END")))) {
			sql.append("  AND TO_CHAR(GEB.OFFLINE_END, 'yyyy-mm-dd') >= :OFFLINE_END");
			param.put("OFFLINE_END", ObjectUtils.toString(searchParams.get("OFFLINE_END")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ONLINE_ST")))) {
			sql.append("  AND TO_CHAR(GEB.ONLINE_ST, 'yyyy-mm-dd') <= :ONLINE_ST");
			param.put("ONLINE_ST", ObjectUtils.toString(searchParams.get("ONLINE_ST")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ONLINE_END")))) {
			sql.append("  AND TO_CHAR(GEB.ONLINE_END, 'yyyy-mm-dd') >= :ONLINE_END");
			param.put("ONLINE_END", ObjectUtils.toString(searchParams.get("ONLINE_END")));
		}
		
		sql.append(" ) TAB");
		sql.append("  WHERE 1 = 1");
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BSTATUS_TYPE")))) {
			sql.append("  AND BSTATUS = :BSTATUS");
			param.put("BSTATUS", ObjectUtils.toString(searchParams.get("BSTATUS_TYPE")));
		}
		
		int num = 0;
		List list = commonDao.queryForMapListNative(sql.toString(), param);
		if (EmptyUtils.isNotEmpty(list)) {
			Map tempMap = (Map)list.get(0);
			num = Integer.parseInt(ObjectUtils.toString(tempMap.get("BATCH_COUNT")));
		}
		return num;
	}
	
	/**
	 * 查询考试计划列表信息
	 * @param searchParams
	 * @param pageRequest
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page getExamBatchNewList(Map searchParams,PageRequest pageRequest){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	TAB.EXAM_BATCH_ID,");
		sql.append("  	TAB.EXAM_BATCH_CODE,");
		sql.append("  	TAB.EXAM_BATCH_NAME,");
		sql.append("  	TAB.BOOK_ST,");
		sql.append("  	TAB.BOOK_END,");
		sql.append("  	TAB.ONLINE_ST,");
		sql.append("  	TAB.ONLINE_END,");
		sql.append("  	TAB.OFFLINE_ST,");
		sql.append("  	TAB.OFFLINE_END,");
		sql.append("  	TAB.GRADE_ID,");
		sql.append("  	TAB.GRADE_NAME,");
		sql.append("  	TAB.GRADE_CODE,");
		sql.append("  	TAB.CREATED_DT,");
		sql.append("  	TAB.PLAN_STATUS");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GEB.EXAM_BATCH_ID,");
		sql.append("  			GEB.EXAM_BATCH_CODE,");
		sql.append("  			GEB.NAME AS EXAM_BATCH_NAME,");
		sql.append("  			TO_CHAR( GEB.BOOK_ST, 'yyyy-MM-dd' ) BOOK_ST,");
		sql.append("  			TO_CHAR( GEB.BOOK_END, 'yyyy-MM-dd' ) BOOK_END,");
		sql.append("  			TO_CHAR( GEB.ONLINE_ST, 'yyyy-MM-dd' ) ONLINE_ST,");
		sql.append("  			TO_CHAR( GEB.ONLINE_END, 'yyyy-MM-dd' ) ONLINE_END,");
		sql.append("  			TO_CHAR( GEB.OFFLINE_ST, 'yyyy-MM-dd' ) OFFLINE_ST,");
		sql.append("  			TO_CHAR( GEB.OFFLINE_END, 'yyyy-MM-dd' ) OFFLINE_END,");
		sql.append("  			GG.GRADE_ID,");
		sql.append("  			GG.GRADE_NAME,");
		sql.append("  			GG.GRADE_CODE,");
		sql.append("  			GEB.CREATED_DT,");
		sql.append("  			(");
		sql.append("  				CASE");
		sql.append("  					WHEN TO_CHAR( SYSDATE, 'yyyy-MM-dd hh24:mi:ss' )> TO_CHAR( GEB.OFFLINE_END, 'yyyy-MM-dd hh24:mi:ss' ) THEN '4'");
		sql.append("  					ELSE GEB.PLAN_STATUS");
		sql.append("  				END");
		sql.append("  			) PLAN_STATUS ");
		sql.append("  		FROM");
		sql.append("  			GJT_EXAM_BATCH_NEW GEB,");
		sql.append("  			GJT_GRADE GG");
		sql.append("  		WHERE");
		sql.append("  			GEB.IS_DELETED = 0");
		sql.append("  			AND GG.IS_DELETED = 'N'");
		sql.append("  			AND GEB.GRADE_ID = GG.GRADE_ID");
		sql.append("  			AND GG.XX_ID = :XX_ID ");
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		sql.append("  	) TAB");
		sql.append("  WHERE");
		sql.append("  	1 = 1");
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_CODE"))){
			sql.append("	AND TAB.EXAM_BATCH_CODE LIKE :EXAM_BATCH_CODE ");
			params.put("EXAM_BATCH_CODE", "%"+ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE"))+"%");
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("EXAM_BATCH_NAME"))){
			sql.append("	AND TAB.EXAM_BATCH_NAME LIKE :EXAM_BATCH_NAME ");
			params.put("EXAM_BATCH_NAME", "%"+ObjectUtils.toString(searchParams.get("EXAM_BATCH_NAME"))+"%");
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("GRADE_ID"))){
			sql.append("	AND TAB.GRADE_ID = :GRADE_ID ");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		if(EmptyUtils.isNotEmpty(searchParams.get("BSTATUS"))){
			sql.append("	AND TAB.PLAN_STATUS = :PLAN_STATUS ");
			params.put("PLAN_STATUS", ObjectUtils.toString(searchParams.get("BSTATUS")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BOOK_ST")))) {
			sql.append("  AND TAB.BOOK_ST  <= :BOOK_ST");
			params.put("BOOK_ST", ObjectUtils.toString(searchParams.get("BOOK_ST")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BOOK_END")))) {
			sql.append("  AND TAB.BOOK_END  >= :BOOK_END");
			params.put("BOOK_END", ObjectUtils.toString(searchParams.get("BOOK_END")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("OFFLINE_ST")))) {
			sql.append("  AND TAB.OFFLINE_ST <= :OFFLINE_ST");
			params.put("OFFLINE_ST", ObjectUtils.toString(searchParams.get("OFFLINE_ST")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("OFFLINE_END")))) {
			sql.append("  AND TAB.OFFLINE_END >= :OFFLINE_END");
			params.put("OFFLINE_END", ObjectUtils.toString(searchParams.get("OFFLINE_END")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ONLINE_ST")))) {
			sql.append("  AND TAB.ONLINE_ST <= :ONLINE_ST");
			params.put("ONLINE_ST", ObjectUtils.toString(searchParams.get("ONLINE_ST")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ONLINE_END")))) {
			sql.append("  AND TAB.ONLINE_END >= :ONLINE_END");
			params.put("ONLINE_END", ObjectUtils.toString(searchParams.get("ONLINE_END")));
		}
		
		sql.append("ORDER BY ");
		sql.append("	TAB.CREATED_DT DESC ");
		
		return (Page)commonDao.queryForPageNative(sql.toString(), params, pageRequest);
		
	}
	
	/**
	 * 查询批次列表统计项
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getExamBatchNewCount(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	COUNT( TAB.EXAM_BATCH_ID ) BATCH_COUNT");
		sql.append("  FROM");
		sql.append("  	(");
		sql.append("  		SELECT");
		sql.append("  			GEB.EXAM_BATCH_ID,");
		sql.append("  			GEB.EXAM_BATCH_CODE,");
		sql.append("  			GEB.NAME AS EXAM_BATCH_NAME,");
		sql.append("  			TO_CHAR( GEB.BOOK_ST, 'yyyy-MM-dd' ) BOOK_ST,");
		sql.append("  			TO_CHAR( GEB.BOOK_END, 'yyyy-MM-dd' ) BOOK_END,");
		sql.append("  			TO_CHAR( GEB.ONLINE_ST, 'yyyy-MM-dd' ) ONLINE_ST,");
		sql.append("  			TO_CHAR( GEB.ONLINE_END, 'yyyy-MM-dd' ) ONLINE_END,");
		sql.append("  			TO_CHAR( GEB.OFFLINE_ST, 'yyyy-MM-dd' ) OFFLINE_ST,");
		sql.append("  			TO_CHAR( GEB.OFFLINE_END, 'yyyy-MM-dd' ) OFFLINE_END,");
		sql.append("  			GG.GRADE_ID,");
		sql.append("  			GG.GRADE_NAME,");
		sql.append("  			GG.GRADE_CODE,");
		sql.append("  			GEB.CREATED_DT,");
		sql.append("  			(");
		sql.append("  				CASE");
		sql.append("  					WHEN TO_CHAR( SYSDATE, 'yyyy-MM-dd hh24:mi:ss' )> TO_CHAR( GEB.OFFLINE_END, 'yyyy-MM-dd hh24:mi:ss' ) THEN '4'");
		sql.append("  					ELSE GEB.PLAN_STATUS");
		sql.append("  				END");
		sql.append("  			) PLAN_STATUS");
		sql.append("  		FROM");
		sql.append("  			GJT_EXAM_BATCH_NEW GEB,");
		sql.append("  			GJT_GRADE GG");
		sql.append("  		WHERE");
		sql.append("  			GEB.IS_DELETED = 0");
		sql.append("  			AND GG.IS_DELETED = 'N'");
		sql.append("  			AND GEB.GRADE_ID = GG.GRADE_ID");
		sql.append("  			AND GG.XX_ID = :XX_ID ");
		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE")))) {
			sql.append("  AND GEB.EXAM_BATCH_CODE LIKE :EXAM_BATCH_CODE");
			params.put("EXAM_BATCH_CODE", "%"+ObjectUtils.toString(searchParams.get("EXAM_BATCH_CODE"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("EXAM_BATCH_NAME")))) {
			sql.append("  AND GEB.NAME LIKE :EXAM_BATCH_NAME");
			params.put("EXAM_BATCH_NAME", "%"+ObjectUtils.toString(searchParams.get("NAME"))+"%");
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("GRADE_ID")))) {
			sql.append("  AND GG.GRADE_ID = :STUDY_YEAR_ID");
			params.put("GRADE_ID", ObjectUtils.toString(searchParams.get("GRADE_ID")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BOOK_ST")))) {
			sql.append("  AND TO_CHAR(GEB.BOOK_ST, 'yyyy-mm-dd') <= :BOOK_ST");
			params.put("BOOK_ST", ObjectUtils.toString(searchParams.get("BOOK_ST")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BOOK_END")))) {
			sql.append("  AND TO_CHAR(GEB.BOOK_END, 'yyyy-mm-dd') >= :BOOK_END");
			params.put("BOOK_END", ObjectUtils.toString(searchParams.get("BOOK_END")));
		}
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("OFFLINE_ST")))) {
			sql.append("  AND TO_CHAR(GEB.OFFLINE_ST, 'yyyy-mm-dd') <= :OFFLINE_ST");
			params.put("OFFLINE_ST", ObjectUtils.toString(searchParams.get("OFFLINE_ST")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("OFFLINE_END")))) {
			sql.append("  AND TO_CHAR(GEB.OFFLINE_END, 'yyyy-mm-dd') >= :OFFLINE_END");
			params.put("OFFLINE_END", ObjectUtils.toString(searchParams.get("OFFLINE_END")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ONLINE_ST")))) {
			sql.append("  AND TO_CHAR(GEB.ONLINE_ST, 'yyyy-mm-dd') <= :ONLINE_ST");
			params.put("ONLINE_ST", ObjectUtils.toString(searchParams.get("ONLINE_ST")));
		}
		
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("ONLINE_END")))) {
			sql.append("  AND TO_CHAR(GEB.ONLINE_END, 'yyyy-mm-dd') >= :ONLINE_END");
			params.put("ONLINE_END", ObjectUtils.toString(searchParams.get("ONLINE_END")));
		}
		sql.append("  	) TAB");
		sql.append("  WHERE");
		sql.append("  	1 = 1");
		if (EmptyUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("BSTATUS_TYPE")))) {
			sql.append("  AND TAB.PLAN_STATUS = :PLAN_STATUS");
			params.put("PLAN_STATUS", ObjectUtils.toString(searchParams.get("BSTATUS_TYPE")));
		}

		int num = 0;
		List list = commonDao.queryForMapListNative(sql.toString(), params);
		if (EmptyUtils.isNotEmpty(list)) {
			Map tempMap = (Map)list.get(0);
			num = Integer.parseInt(ObjectUtils.toString(tempMap.get("BATCH_COUNT")));
		}
		return num;
		
	}
	
	/**
	 * 查看考试计划详情
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List queryExamBatchDetail(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GEB.EXAM_BATCH_ID,");
		sql.append("  	GEB.EXAM_BATCH_CODE,");
		sql.append("  	GEB.NAME AS EXAM_BATCH_NAME,");
		sql.append("  	TO_CHAR( GEB.BOOK_ST, 'yyyy-MM-dd' ) BOOK_ST,");
		sql.append("  	TO_CHAR( GEB.BOOK_END, 'yyyy-MM-dd' ) BOOK_END,");
		sql.append("  	TO_CHAR( GEB.ONLINE_ST, 'yyyy-MM-dd' ) ONLINE_ST,");
		sql.append("  	TO_CHAR( GEB.ONLINE_END, 'yyyy-MM-dd' ) ONLINE_END,");
		sql.append("  	TO_CHAR( GEB.OFFLINE_ST, 'yyyy-MM-dd' ) OFFLINE_ST,");
		sql.append("  	TO_CHAR( GEB.OFFLINE_END, 'yyyy-MM-dd' ) OFFLINE_END,");
		sql.append("  	GG.GRADE_ID,");
		sql.append("  	GG.GRADE_NAME,");
		sql.append("  	GG.GRADE_CODE,");
		sql.append("  	GEB.CREATED_DT,");
		sql.append("  	(");
		sql.append("  		CASE");
		sql.append("  			WHEN TO_CHAR( SYSDATE, 'yyyy-MM-dd hh24:mi:ss' )> TO_CHAR( GEB.OFFLINE_END, 'yyyy-MM-dd hh24:mi:ss' ) THEN '4'");
		sql.append("  			ELSE GEB.PLAN_STATUS");
		sql.append("  		END");
		sql.append("  	) PLAN_STATUS,");
		sql.append("  	GEB.XX_ID");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_BATCH_NEW GEB,");
		sql.append("  	GJT_GRADE GG");
		sql.append("  WHERE");
		sql.append("  	GEB.IS_DELETED = 0");
		sql.append("  	AND GG.IS_DELETED = 'N'");
		sql.append("  	AND GEB.GRADE_ID = GG.GRADE_ID");
		sql.append("  	AND GG.XX_ID = :XX_ID ");
		sql.append("  	AND GEB.EXAM_BATCH_ID = :EXAM_BATCH_ID ");

		params.put("XX_ID", ObjectUtils.toString(searchParams.get("XX_ID")));
		params.put("EXAM_BATCH_ID", ObjectUtils.toString(searchParams.get("EXAM_BATCH_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), params);
		
	}
	
	/**
	 * 查询考试计划审核记录
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getApprovalExamBatchList(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  SELECT");
		sql.append("  	GEBA.APPROVAL_ID,");
		sql.append("  	GEBA.USER_ID,");
		sql.append("  	GEBA.EXAM_BATCH_ID,");
		sql.append("  	GEBA.AUDIT_STATE,");
		sql.append("  	GEBA.AUDIT_OPERATOR,");
		sql.append("  	GEBA.AUDIT_OPERATOR_ROLE,");
		sql.append("  	TO_CHAR(GEBA.AUDIT_DT,'yyyy-MM-dd hh24:mi') AUDIT_DT,");
		sql.append("  	GEBA.AUDIT_CONTENT");
		sql.append("  FROM");
		sql.append("  	GJT_EXAM_BATCH_APPROVAL GEBA");
		sql.append("  WHERE");
		sql.append("  	GEBA.IS_DELETED = 'N'");
		sql.append("  	AND GEBA.EXAM_BATCH_ID = :EXAM_BATCH_ID ");
		sql.append(" ORDER BY GEBA.CREATED_DT ASC ");

		params.put("EXAM_BATCH_ID", ObjectUtils.toString(searchParams.get("EXAM_BATCH_ID")));
		
		return commonDao.queryForMapListNative(sql.toString(), params);
	}
	
	/**
	 * 删除考试计划
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int delExamBatch(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE");
		sql.append("  	GJT_EXAM_BATCH_NEW GEB");
		sql.append("  SET");
		sql.append("  	GEB.IS_DELETED = 1");
		sql.append("  WHERE");
		sql.append("  	GEB.EXAM_BATCH_ID = :EXAM_BATCH_ID");
		
		params.put("EXAM_BATCH_ID", ObjectUtils.toString(searchParams.get("EXAM_BATCH_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), params);

	}
	
	/**
	 * 修改考试计划状态
	 * @param searchParams
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int updateExamBatchStatus(Map searchParams){
		Map params = new HashMap();
		StringBuffer sql = new StringBuffer();
		
		sql.append("  UPDATE");
		sql.append("  	GJT_EXAM_BATCH_NEW GEB");
		sql.append("  SET");
		sql.append("  	GEB.PLAN_STATUS = :PLAN_STATUS");
		sql.append("  WHERE");
		sql.append("  	GEB.EXAM_BATCH_ID = :EXAM_BATCH_ID ");
		
		params.put("PLAN_STATUS", ObjectUtils.toString(searchParams.get("PLAN_STATUS")));
		params.put("EXAM_BATCH_ID", ObjectUtils.toString(searchParams.get("EXAM_BATCH_ID")));
		
		return commonDao.updateForMapNative(sql.toString(), params);

	}
	
}
