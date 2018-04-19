package com.gzedu.xlims.dao.textbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.textbook.repository.GjtTextbookArrangeRepository;
import com.gzedu.xlims.pojo.textbook.GjtTextbookArrange;

@Repository
public class GjtTextbookArrangeDao {

	@Autowired
	private GjtTextbookArrangeRepository gjtTextbookArrangeRepository;

	@Autowired
	private CommonDao commonDao;

	public Page<GjtTextbookArrange> findAll(Specification<GjtTextbookArrange> spec, PageRequest pageRequst) {
		return gjtTextbookArrangeRepository.findAll(spec, pageRequst);
	}

	public GjtTextbookArrange save(GjtTextbookArrange entity) {
		return gjtTextbookArrangeRepository.save(entity);
	}

	public void save(List<GjtTextbookArrange> entities) {
		gjtTextbookArrangeRepository.save(entities);
	}

	public GjtTextbookArrange findOne(String id) {
		return gjtTextbookArrangeRepository.findOne(id);
	}

	public void delete(GjtTextbookArrange entity) {
		gjtTextbookArrangeRepository.delete(entity);
	}

	public GjtTextbookArrange findByPlanIdAndTextbookId(String planId, String textbookId) {
		return gjtTextbookArrangeRepository.findByPlanIdAndTextbookId(planId, textbookId);
	}

	public List<String> findTeachPlanIdsByGradeIdAndTextbookId(String gradeId, String textbookId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ").append("		gtp.TEACH_PLAN_ID ").append("from ").append("		GJT_TEACH_PLAN gtp, ")
				.append("		GJT_TEACH_PLAN_TEXTBOOK  gtpt ").append("where ").append("		gtp.IS_DELETED = 'N' ")
				.append("		and gtp.ACTUAL_GRADE_ID = :gradeId ")
				.append("		and gtp.TEACH_PLAN_ID = gtpt.TEACH_PLAN_ID ")
				.append("		and gtpt.TEXTBOOK_ID = :textbookId ");

		map.put("gradeId", gradeId);
		map.put("textbookId", textbookId);

		return commonDao.queryForStringListNative(sb.toString(), map);
	}

	public List<Map<String, Object>> findRecResultsToReview(Map<String, Object> params) {
		StringBuilder sql = new StringBuilder();
		sql.append("  select r.student_id, p.course_Id, p.grade_id");
		sql.append("  from gjt_rec_result r");
		sql.append("  inner join gjt_teach_plan p");
		sql.append("  on r.teach_plan_id = p.teach_plan_id");
		sql.append("  inner join gjt_student_info s");
		sql.append("  on s.student_id = r.student_id");
		sql.append("  where r.is_Deleted = 'N'");
		sql.append("  and s.is_deleted = 'N'");
		sql.append("  and p.is_Deleted = 'N'");
		sql.append("  and p.grant_Data = 0");
		sql.append("  and p.actual_Grade_Id = :gradeId");
		sql.append("  and p.course_Id in (:courseIds)");
		sql.append("  and s.nj in (:textbookGradeIds)");
		sql.append("  and s.xjzt not in('5','8','10','1','9')");

		return commonDao.queryForMapList(sql.toString(), params);
	}

	public List<Map<String, Object>> findRecResults(Map<String, Object> params) {
		StringBuilder sql = new StringBuilder();
		sql.append("  select r.student_id, p.course_Id, p.grade_id");
		sql.append("  from gjt_rec_result r");
		sql.append("  inner join gjt_student_info s");
		sql.append("  on s.student_id = r.student_id");
		sql.append("  inner join gjt_teach_plan p");
		sql.append("  on r.teach_plan_id = p.teach_plan_id");
		sql.append("  where r.is_Deleted = 'N'");
		sql.append("  and p.is_Deleted = 'N'");
		sql.append("  and s.is_deleted = 'N'");
		sql.append("  and r.teach_plan_id in (:teachPlanIds)");
		sql.append("  and s.nj in (:textbookGradeIds)");
		sql.append("  and s.xjzt not in('5','8','10','1','9')");

		return commonDao.queryForMapList(sql.toString(), params);
	}

	public Page<Map<String, Object>> findTextbookArrangeList(Map<String, Object> searchParams, PageRequest pageRequst) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		sql.append("  select gg.grade_id,");
		sql.append("  gg.textbook_status status,");
		sql.append("  gg.grade_name,");
		sql.append("  (select count(distinct(gtp.course_id))");
		sql.append("  from gjt_grade_specialty ggs, gjt_teach_plan gtp");
		sql.append("  where ggs.is_deleted = 'N'");
		sql.append("  and gtp.is_deleted = 'N'");
		sql.append("  and ggs.grade_id = gg.grade_id");
		sql.append("  and ggs.id = gtp.grade_specialty_id) course_count,");
		sql.append("  (select count(1)");
		sql.append("  from gjt_grade_textbook ggt");
		sql.append("  where ggt.grade_id=gg.grade_id) textbook_count");
		sql.append("  From gjt_grade gg");
		sql.append("  where gg.xx_id = :orgId");
		sql.append("  and gg.is_deleted = 'N'");
		sql.append("  and gg.is_enabled = '1'");
		params.put("orgId", searchParams.get("orgId"));
		if (StringUtils.isNotBlank(searchParams.get("gradeId"))) {
			sql.append(" and gg.grade_id=:gradeId");
			params.put("gradeId", searchParams.get("gradeId"));
		}
		sql.append("  order by gg.start_date desc");
		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);
	}

	@Transactional
	public void saveArrangeTextbook(String gradeId, List<String> textbookIds) {
		for (String textbookId : textbookIds) {
			gjtTextbookArrangeRepository.insertGradeTextbook(gradeId, textbookId);
		}
	}

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月22日 下午3:12:36
	 * @param gradeId
	 * @param textbookId
	 */
	public void removeTextbook(java.lang.String gradeId, java.lang.String textbookId) {
		gjtTextbookArrangeRepository.removeTextbook(gradeId, textbookId);

	}

}
