package com.gzedu.xlims.dao.textbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.textbook.repository.GjtTextbookRepository;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;

@Repository
public class GjtTextbookDao {

	@Autowired
	private GjtTextbookRepository gjtTextbookRepository;

	@Autowired
	private CommonDao commonDao;

	public Page<GjtTextbook> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ").append("		t.* ")
		.append("from ")
		.append("		GJT_TEXTBOOK t, ")
		.append("		GJT_TEXTBOOK_STOCK s ")
		.append("where ")
		.append("		t.IS_DELETED = 'N' ")
		.append("		and s.TEXTBOOK_ID = t.TEXTBOOK_ID ");

		Object orgId = searchParams.get("EQ_orgId");
		if (orgId != null) {
			sb.append(" and t.ORG_ID = :orgId ");
			map.put("orgId", orgId);
		}

		Object textbookCode = searchParams.get("LIKE_textbookCode");
		if (textbookCode != null && StringUtils.isNotBlank(textbookCode.toString())) {
			sb.append(" and t.TEXTBOOK_CODE like :textbookCode ");
			map.put("textbookCode", "%" + textbookCode + "%");
		}

		Object textbookName = searchParams.get("LIKE_textbookName");
		if (textbookName != null && StringUtils.isNotBlank(textbookName.toString())) {
			sb.append(" and t.TEXTBOOK_NAME like :textbookName ");
			map.put("textbookName", "%" + textbookName + "%");
		}

		Object courseName = searchParams.get("LIKE_courseName");
		if (courseName != null && StringUtils.isNotBlank(courseName.toString())) {
			sb.append(" and exists ( ")
			.append("			select ")
			.append("					1 ")
			.append("			from ")
			.append("					GJT_COURSE_TEXTBOOK ct, ")
			.append("					GJT_COURSE c ")
			.append("			where ")
			.append("					ct.TEXTBOOK_ID = t.TEXTBOOK_ID ")
			.append("					and ct.COURSE_ID = c.COURSE_ID ")
			.append("					and c.IS_DELETED = 'N' ")
			.append("					and c.KCMC like :courseName ")
			.append("			) ");
			map.put("courseName", "%" + courseName + "%");
		}

		Object courseId = searchParams.get("EQ_courseId");
		if (courseId != null && StringUtils.isNotBlank(courseId.toString())) {
			sb.append(" and exists ( ")
			.append("			select ")
			.append("					1 ")
			.append("			from ")
			.append("					GJT_COURSE_TEXTBOOK ct ")
			.append("			where ")
			.append("					ct.TEXTBOOK_ID = t.TEXTBOOK_ID ")
			.append("					and ct.COURSE_ID = :courseId ")
			.append("			) ");
			map.put("courseId", courseId);
		}
		
		courseId = searchParams.get("NEQ_courseId");
		if (courseId != null && StringUtils.isNotBlank(courseId.toString())) {
			sb.append(" and not exists ( ")
			.append("			select ")
			.append("					1 ")
			.append("			from ")
			.append("					GJT_COURSE_TEXTBOOK ct ")
			.append("			where ")
			.append("					ct.TEXTBOOK_ID = t.TEXTBOOK_ID ")
			.append("					and ct.COURSE_ID = :courseId ")
			.append("			) ");
			map.put("courseId", courseId);
		}

		Object textbookType = searchParams.get("EQ_textbookType");
		if (textbookType != null && StringUtils.isNotBlank(textbookType.toString())) {
			sb.append(" and t.TEXTBOOK_TYPE = :textbookType ");
			map.put("textbookType", textbookType);
		}

		Object author = searchParams.get("LIKE_author");
		if (author != null && StringUtils.isNotBlank(author.toString())) {
			sb.append(" and t.AUTHOR like :author ");
			map.put("author", "%" + author + "%");
		}

		Object revision = searchParams.get("LIKE_revision");
		if (revision != null && StringUtils.isNotBlank(revision.toString())) {
			sb.append(" and t.REVISION like :revision ");
			map.put("revision", "%" + revision + "%");
		}

		Object stockStatus = searchParams.get("EQ_stockStatus");
		if (stockStatus != null && StringUtils.isNotBlank(stockStatus.toString())) {
			if ("1".equals(stockStatus.toString())) {
				sb.append(" and s.STOCK_NUM >= s.PLAN_OUT_STOCK_NUM ").append(" and s.STOCK_NUM != 0 ");
			} else if ("2".equals(stockStatus.toString())) {
				sb.append(" and s.STOCK_NUM < s.PLAN_OUT_STOCK_NUM ");
			}
		}

		Object status = searchParams.get("EQ_status");
		if (status != null && StringUtils.isNotBlank(status.toString())) {
			sb.append(" and t.STATUS = :status ");
			map.put("status", status);
		}

		Object isbn = searchParams.get("LIKE_isbn");
		if (isbn != null && StringUtils.isNotBlank(isbn.toString())) {
			sb.append(" and t.isbn like :isbn ");
			map.put("isbn", "%" + isbn + "%");
		}

		sb.append(" order by t.CREATED_DT desc ");

		return (Page<GjtTextbook>) commonDao.queryForPageNative(sb.toString(), map, pageRequst, GjtTextbook.class);
	}

	public List<GjtTextbook> findAll(Specification<GjtTextbook> spec) {
		return gjtTextbookRepository.findAll(spec);
	}

	public GjtTextbook findOne(Specification<GjtTextbook> spec) {
		return gjtTextbookRepository.findOne(spec);
	}

	public GjtTextbook findOne(String id) {
		return gjtTextbookRepository.findOne(id);
	}

	public List<GjtTextbook> findAll(Iterable<String> ids) {
		return (List<GjtTextbook>) gjtTextbookRepository.findAll(ids);
	}

	public GjtTextbook findByCode(String textbookCode, String orgId) {
		return gjtTextbookRepository.findByCode(textbookCode, orgId);
	}

	public GjtTextbook save(GjtTextbook entity) {
		return gjtTextbookRepository.save(entity);
	}
	
	public List<GjtTextbook> findCurrentArrangeTextbook(String planId, String orgId) {
		return gjtTextbookRepository.findCurrentArrangeTextbook(planId, orgId);
	}

	public List<GjtTextbook> findUnsetTextBookByGradeId(String gradeId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select *");
		sql.append("  from gjt_textbook gt");
		sql.append("  where gt.is_deleted = 'N'");
		sql.append("  and exists (select 1");
		sql.append("  from gjt_grade           gg,");
		sql.append("  gjt_grade_specialty ggs,");
		sql.append("  gjt_teach_plan      gtp,");
		sql.append("  gjt_course_textbook gct");
		sql.append("  where gg.grade_id = ggs.grade_id");
		sql.append("  and ggs.id = gtp.grade_specialty_id");
		sql.append("  and gtp.course_id = gct.course_id");
		sql.append("  and gct.textbook_id = gt.textbook_id");
		sql.append("  and gg.is_deleted = 'N'");
		sql.append("  and ggs.is_deleted = 'N'");
		sql.append("  and gtp.is_deleted = 'N'");
		sql.append("  and gg.grade_id = :gradeId)");
		sql.append("  and not exists");
		sql.append("  (select 1");
		sql.append("  from gjt_grade_textbook ggt");
		sql.append("  where ggt.textbook_id=gt.textbook_id and ggt.grade_id = :gradeId)");
		params.put("gradeId", gradeId);
		return commonDao.querySqlForList(sql.toString(), params, GjtTextbook.class);
	}

	public Page<GjtTextbook> findArrangeTextBook(Map<String, Object> searchParams, PageRequest pageRequest) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select gt.*");
		sql.append("  from gjt_textbook gt, gjt_grade_textbook ggt");
		sql.append("  where gt.textbook_id = ggt.textbook_id");
		sql.append("  and gt.is_deleted = 'N'");
		if (StringUtils.isNotBlank((String) searchParams.get("gradeId"))) {
			sql.append("  and ggt.grade_id = :gradeId");
			params.put("gradeId", searchParams.get("gradeId"));
		}
		if (StringUtils.isNotBlank((String) searchParams.get("textbookCode"))) {
			sql.append("  and ggt.textbook_code = :textbookCode");
			params.put("textbookCode", searchParams.get("textbookCode"));
		}
		if (StringUtils.isNotBlank((String) searchParams.get("textbookName"))) {
			sql.append("  and ggt.textbook_name = :textbookName");
			params.put("textbookName", searchParams.get("textbookName"));
		}
		if (StringUtils.isNotBlank((String) searchParams.get("courseId"))) {
			sql.append("  and exists(select 1 from gjt_course_textbook gct where gct.textbook_id=gt.textbook_id and gct.course_id=:courseId)");
			params.put("courseId", searchParams.get("courseId"));
		}
		return commonDao.queryForPageNative(sql.toString(), params, pageRequest, GjtTextbook.class);
	}

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月29日 下午4:13:01
	 * @param gradeSpecialtyId
	 * @return
	 */
	public List findByGradeSpecialtyId(String gradeSpecialtyId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select gg.grade_id      \"termId\",");
		sql.append("  gg.grade_name    \"termName\",");
		sql.append("  gtp.kkxq         \"termIndex\",");
		sql.append("  gt.cover \"cover\",");
		sql.append("  gt.textbook_id \"textbookId\",");
		sql.append("  gt.textbook_name \"textbookName\",");
		sql.append("  gc.kcmc  \"courseName\",");
		sql.append("  gt.price         \"price\"");
		sql.append("  from gjt_grade           gg,");
		sql.append("  gjt_teach_plan      gtp,");
		sql.append("  gjt_course          gc,");
		sql.append("  gjt_course_textbook gct,");
		sql.append("  gjt_textbook        gt,");
		sql.append("  gjt_grade_textbook  ggt");
		sql.append("  where gg.grade_id = gtp.actual_grade_id");
		sql.append("  and gtp.course_id = gc.course_id");
		sql.append("  and gc.course_id = gct.course_id");
		sql.append("  and gct.textbook_id = gt.textbook_id");
		sql.append("  and gtp.actual_grade_id = ggt.grade_id");
		sql.append("  and gct.textbook_id = ggt.textbook_id");
		sql.append("  and gtp.is_deleted = 'N'");
		sql.append("  and gt.is_deleted = 'N'");
		sql.append("  and gc.is_deleted = 'N'");
		sql.append("  and gtp.grade_specialty_id = :gradeSpecialtyId");
		sql.append("  order by gtp.kkxq");
		params.put("gradeSpecialtyId", gradeSpecialtyId);
		return commonDao.queryForMapList(sql.toString(), params);
	}

	public List<Map<String, Object>> findByGradeId(String gradeId, String gradeSpecialtyId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select gt.textbook_name \"textbookName\",");
		sql.append("  gt.textbook_id         \"textbookId\",");
		sql.append("  gt.price         \"price\",");
		sql.append("  gt.cover         \"cover\",");
		sql.append("  gc.kcmc          \"courseName\"");
		sql.append("  from gjt_teach_plan      gtp,");
		sql.append("  gjt_course          gc,");
		sql.append("  gjt_course_textbook gct,");
		sql.append("  gjt_textbook        gt,");
		sql.append("  gjt_grade_textbook  ggt");
		sql.append("  where gtp.course_id = gc.course_id");
		sql.append("  and gc.course_id = gct.course_id");
		sql.append("  and gct.textbook_id = gt.textbook_id");
		sql.append("  and gtp.actual_grade_id = ggt.grade_id");
		sql.append("  and gct.textbook_id = ggt.textbook_id");
		sql.append("  and gtp.is_deleted = 'N'");
		sql.append("  and gt.is_deleted = 'N'");
		sql.append("  and gc.is_deleted = 'N'");
		sql.append("  and gtp.grade_specialty_id = :gradeSpecialtyId");
		sql.append("  and ggt.grade_id = :gradeId");
		sql.append("  order by gtp.kkxq");
		params.put("gradeSpecialtyId", gradeSpecialtyId);
		params.put("gradeId", gradeId);
		return commonDao.queryForMapList(sql.toString(), params);
	}

	public List<Map<String, Object>> findByDistributeId(String distributeId, String studentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select gt.cover         \"cover\",");
		sql.append("         gt.textbook_name \"textbookName\",");
		sql.append("         gtdd.price       \"price\",");
		sql.append("         (");
		sql.append("           select gc.kcmc from gjt_course_textbook gct");
		sql.append("           inner join gjt_course gc on gct.course_id = gc.course_id");
		sql.append("           inner join gjt_rec_result b on b.student_id=:studentId and b.course_id=gc.course_id and b.is_deleted='N'");
		sql.append("           where gct.textbook_id = gt.textbook_id");
		sql.append("         )         \"courseName\"");
		sql.append("    from gjt_textbook_distribute_detail gtdd,");
		sql.append("         gjt_textbook                   gt");
		sql.append("   where gtdd.textbook_id = gt.textbook_id");
		sql.append("  and gtdd.distribute_id=:distributeId");
		params.put("distributeId", distributeId);
		params.put("studentId", studentId);
		return commonDao.queryForMapList(sql.toString(), params);
	}

}
