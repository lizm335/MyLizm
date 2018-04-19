package com.gzedu.xlims.dao.textbook;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.textbook.repository.GjtTextbookDistributeRepository;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistribute;

@Repository
public class GjtTextbookDistributeDao {

	@Autowired
	private GjtTextbookDistributeRepository gjtTextbookDistributeRepository;

	@Autowired
	private CommonDao commonDao;

	public Page<GjtTextbookDistribute> findAll(Specification<GjtTextbookDistribute> spec, PageRequest pageRequst) {
		return gjtTextbookDistributeRepository.findAll(spec, pageRequst);
	}

	public GjtTextbookDistribute save(GjtTextbookDistribute entity) {
		return gjtTextbookDistributeRepository.save(entity);
	}

	public List<GjtTextbookDistribute> save(List<GjtTextbookDistribute> entities) {
		return gjtTextbookDistributeRepository.save(entities);
	}

	public GjtTextbookDistribute findByOrderCodeAndStatusAndIsDeleted(String orderCode, int status, String isDeleted) {
		return gjtTextbookDistributeRepository.findByOrderCodeAndStatusAndIsDeleted(orderCode, status, isDeleted);
	}

	public List<GjtTextbookDistribute> findByStudentIdAndIsDeletedAndStatusIn(String studentId, String isDeleted,
			Collection<Integer> statuses) {
		return gjtTextbookDistributeRepository.findByStudentIdAndIsDeletedAndStatusIn(studentId, isDeleted, statuses);
	}

	public List<GjtTextbookDistribute> findByStudentIdAndStatus(String studentId, String isDeleted, Collection<Integer> statuses) {
		return gjtTextbookDistributeRepository.findByStudentIdAndStatus(studentId, statuses);
	}

	public Long countByStudentIdAndIsDeletedAndStatusIn(String studentId, String isDeleted, Collection<Integer> statuses) {
		return gjtTextbookDistributeRepository.countByStudentIdAndIsDeletedAndStatusIn(studentId, isDeleted, statuses);
	}

	public List<GjtTextbookDistribute> findByIsDeletedAndStatusIn(String orgId, String isDeleted,
			Collection<Integer> statuses) {
		return gjtTextbookDistributeRepository.findByIsDeletedAndStatusIn(orgId, isDeleted, statuses);
	}

	public Page<Map<String, Object>> findDistributeSummary(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ").append("		t1.studentId as \"studentId\", ")
				.append("		t1.studentName as \"studentName\", ")
				.append("		t1.studentCode as \"studentCode\", ").append("		t1.grade as \"grade\", ")
				.append("		t1.trainingLevel as \"trainingLevel\", ")
				.append("		t1.specialtyName as \"specialtyName\", ")
				.append("		Decode(t2.hadDistributeCount, null, 0, t2.hadDistributeCount) as \"hadDistributeCount\", ")
				.append("		Decode(t3.currentDistributeCount, null, 0, t3.currentDistributeCount) ")
				.append("		+ Decode(t4.currentDistributeCount, null, 0, t4.currentDistributeCount) as \"currentDistributeCount\", ")
				.append("		Decode(t5.willDistributeCount, null, 0, t5.willDistributeCount) as \"willDistributeCount\", ")
				.append("		Decode(t6.status, null, 0, t6.status) as \"status\" ").append("from ").append("		( ")
				.append("		 select ").append("        	s.STUDENT_ID as studentId, ")
				.append("        	s.XM as studentName, ").append("        	s.XH as studentCode, ")
				.append("        	gg.GRADE_NAME as grade, ").append("        	d.NAME as trainingLevel, ")
				.append("        	gs.ZYMC as specialtyName ").append("        from ")
				.append("        	GJT_STUDENT_INFO s, ").append("        	GJT_GRADE gg, ")
				.append("        	GJT_SPECIALTY gs, ").append("        	TBL_SYS_DATA d ").append("        where ")
				.append("        	gg.grade_id = s.nj ").append("        	and gs.specialty_id = s.major ")
				.append("        	and s.PYCC = d.CODE and d.TYPE_CODE = 'TrainingLevel' ");

		Object orgId = searchParams.get("EQ_orgId");
		if (orgId != null) {
			sb.append(" and s.XX_ID = :orgId ");
			map.put("orgId", orgId);
		}

		Object studentName = searchParams.get("LIKE_xm");
		if (studentName != null && StringUtils.isNotBlank(studentName.toString())) {
			sb.append(" and s.XM like :studentName ");
			map.put("studentName", "%" + studentName + "%");
		}

		Object studentCode = searchParams.get("LIKE_xh");
		if (studentCode != null && StringUtils.isNotBlank(studentCode.toString())) {
			sb.append(" and s.XH like :studentCode ");
			map.put("studentCode", "%" + studentCode + "%");
		}

		Object gradeId = searchParams.get("EQ_nj");
		if (gradeId != null && StringUtils.isNotBlank(gradeId.toString())) {
			sb.append(" and s.NJ = :gradeId ");
			map.put("gradeId", gradeId);
		}

		Object trainingLevel = searchParams.get("EQ_pycc");
		if (trainingLevel != null && StringUtils.isNotBlank(trainingLevel.toString())) {
			sb.append(" and s.PYCC = :trainingLevel ");
			map.put("trainingLevel", trainingLevel);
		}

		Object specialtyId = searchParams.get("EQ_major");
		if (specialtyId != null && StringUtils.isNotBlank(specialtyId.toString())) {
			sb.append(" and s.MAJOR = :specialtyId ");
			map.put("specialtyId", specialtyId);
		}

		sb.append("        order by s.XM  ").append("       ) t1 ").append("left join ").append("		( ")
				.append("		 select ").append("        	d2.STUDENT_ID as studentId, ")
				.append("        	sum(d1.QUANTITY) as hadDistributeCount ").append("        from ")
				.append("        	GJT_TEXTBOOK_DISTRIBUTE_DETAIL d1, ")
				.append("        	GJT_TEXTBOOK_DISTRIBUTE d2 ").append("        where ")
				.append("        	d1.DISTRIBUTE_ID = d2.DISTRIBUTE_ID ").append("        	and d2.STATUS in (2, 3) ")
				.append("        	and d2.IS_DELETED = 'N' ").append("        group by d2.STUDENT_ID  ")
				.append("       ) t2 ").append("on ").append("		t1.studentId = t2.studentId ").append("left join ")
				.append("		( ").append("		 select ").append("        	d2.STUDENT_ID as studentId, ")
				.append("        	sum(d1.QUANTITY) as currentDistributeCount ").append("        from ")
				.append("        	GJT_TEXTBOOK_DISTRIBUTE_DETAIL d1, ")
				.append("        	GJT_TEXTBOOK_DISTRIBUTE d2 ").append("        where ")
				.append("        	d1.DISTRIBUTE_ID = d2.DISTRIBUTE_ID ").append("        	and d2.STATUS in (0, 1) ")
				.append("        	and d2.IS_DELETED = 'N' ").append("        group by d2.STUDENT_ID  ")
				.append("       ) t3 ").append("on ").append("		t1.studentId = t3.studentId ").append("left join ")
				.append("		( ").append("		 select ").append("        	s.STUDENT_ID as studentId, ")
				.append("        	count(*) as currentDistributeCount ").append("        from ")
				.append("        	gjt_student_info s, ").append("        	gjt_grade_specialty_plan gsp, ")
				.append("        	gjt_grade_specialty_plan_book gspb, ").append("        	GJT_TEXTBOOK t ")
				.append("        where ").append("        	s.nj = gsp.grade_id ")
				.append("        	and s.major = gsp.specialty_id ")
				.append("        	and gsp.study_year_code = :studyYearCode ")
				.append("        	and gspb.grade_specialty_plan_id = gsp.id ")
				.append("        	and gspb.textbook_id = t.textbook_id ")
				.append("        	and t.IS_DELETED = 'N' ").append("        	and t.TEXTBOOK_ID not in ")
				.append("        		( ").append("        		 select ")
				.append("        		 		d1.TEXTBOOK_ID ").append("        		 from ")
				.append("        		 		GJT_TEXTBOOK_DISTRIBUTE_DETAIL d1, ")
				.append("        		 		GJT_TEXTBOOK_DISTRIBUTE d2 ").append("        		 where ")
				.append("        		 		d1.DISTRIBUTE_ID = d2.DISTRIBUTE_ID ")
				.append("        		 		and d2.STUDENT_ID = s.STUDENT_ID ")
				.append("        		 		and d2.IS_DELETED = 'N' ").append("        		) ")
				.append("        group by s.STUDENT_ID  ").append("       ) t4 ").append("on ")
				.append("		t1.studentId = t4.studentId ").append("left join ").append("		( ")
				.append("		 select ").append("        	s.STUDENT_ID as studentId, ")
				.append("        	count(*) as willDistributeCount ").append("        from ")
				.append("        	gjt_student_info s, ").append("        	gjt_grade_specialty_plan gsp, ")
				.append("        	gjt_grade_specialty_plan_book gspb, ").append("        	GJT_TEXTBOOK t ")
				.append("        where ").append("        	s.nj = gsp.grade_id ")
				.append("        	and s.major = gsp.specialty_id ")
				.append("        	and gsp.study_year_code > :studyYearCode ")
				.append("        	and gspb.grade_specialty_plan_id = gsp.id ")
				.append("        	and gspb.textbook_id = t.textbook_id ")
				.append("        	and t.IS_DELETED = 'N' ").append("        group by s.STUDENT_ID  ")
				.append("       ) t5 ").append("on ").append("		t1.studentId = t5.studentId ").append("left join ")
				.append("		( ").append("		 select ").append("        	d.STUDENT_ID as studentId, ")
				.append("        	d.STATUS as status ").append("        from ").append("        	  ( ")
				.append("        	  	select ").append("        	  		t.STUDENT_ID, ")
				.append("        	  		max(t.CREATED_DT) as CREATED_DT ").append("        	  	from ")
				.append("        	  		GJT_TEXTBOOK_DISTRIBUTE t ").append("        	  	group by t.STUDENT_ID ")
				.append("        	  ) t, ").append("        	GJT_TEXTBOOK_DISTRIBUTE d ").append("        where ")
				.append("        	d.IS_DELETED = 'N' ").append("        	and d.STUDENT_ID = t.STUDENT_ID ")
				.append("        	and d.CREATED_DT = t.CREATED_DT ").append("       ) t6 ").append("on ")
				.append("		t1.studentId = t6.studentId ").append("where 1 = 1 ");

		Object status = searchParams.get("EQ_status");
		if (status != null && StringUtils.isNotBlank(status.toString())) {
			int s = Integer.parseInt(status.toString());
			if (s > 0) {
				sb.append(" and t6.status = :status ");
				map.put("status", status);
			} else {
				sb.append(" and (t6.status = 0 or t6.status is null) ");
			}
		}

		map.put("studyYearCode", searchParams.get("studyYearCode"));

		return commonDao.queryForPageNative(sb.toString(), map, pageRequst);
	}

	/**
	 * 查询当前待发教材总数
	 * 
	 * @param orgId
	 * @param studyYearCode
	 * @return
	 */
	public int queryCurrentDistributeCount(String orgId, int studyYearCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("		 select ").append("        	count(*) as \"currentDistributeCount\" ").append("    from ")
				.append("        	gjt_student_info s, ").append("        	gjt_grade_specialty_plan gsp, ")
				.append("        	gjt_grade_specialty_plan_book gspb, ").append("        	GJT_TEXTBOOK t ")
				.append("    where ").append("        	s.XX_ID = :orgId ")
				.append("        	and s.nj = gsp.grade_id ").append("        	and s.major = gsp.specialty_id ")
				.append("        	and gsp.study_year_code = :studyYearCode ")
				.append("        	and gspb.grade_specialty_plan_id = gsp.id ")
				.append("        	and gspb.textbook_id = t.textbook_id ")
				.append("        	and t.IS_DELETED = 'N' ").append("        	and t.TEXTBOOK_ID not in ")
				.append("        		( ").append("        		 select ")
				.append("        		 		d1.TEXTBOOK_ID ").append("        		 from ")
				.append("        		 		GJT_TEXTBOOK_DISTRIBUTE_DETAIL d1, ")
				.append("        		 		GJT_TEXTBOOK_DISTRIBUTE d2 ").append("        		 where ")
				.append("        		 		d1.DISTRIBUTE_ID = d2.DISTRIBUTE_ID ")
				.append("        		 		and d2.STUDENT_ID = s.STUDENT_ID ")
				.append("        		 		and d2.IS_DELETED = 'N' ").append("        		) ");

		map.put("orgId", orgId);
		map.put("studyYearCode", studyYearCode);

		int count = 0;
		Object object = commonDao.queryObjectNative(sb.toString(), map);
		if (object != null) {
			count = ((BigDecimal) object).intValue();
		}

		return count;
	}

	/**
	 * 查询当前待发教材列表
	 * 
	 * @param orgId
	 * @param studyYearCode
	 * @param isEnough：
	 *            true-只查询足够库存的，false或null-查询全部
	 * @return
	 */
	public List<Object[]> queryCurrentDistributeList(String orgId, int studyYearCode, Boolean isEnough) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("		 select ").append("        	t.TEXTBOOK_ID as \"textbookId\", ")
				.append("        	t.TEXTBOOK_CODE as \"textbookCode\", ")
				.append("        	t.TEXTBOOK_NAME as \"textbookName\", ")
				.append("        	ts.STOCK_NUM as \"stockNum\", ")
				.append("        	ts.PLAN_OUT_STOCK_NUM as \"palnOutStockNum\", ")
				.append("        	count(*) as \"currentDistributeCount\" ").append("    from ")
				.append("        	gjt_student_info s, ").append("        	gjt_grade_specialty_plan gsp, ")
				.append("        	gjt_grade_specialty_plan_book gspb, ").append("        	GJT_TEXTBOOK t, ")
				.append("        	GJT_TEXTBOOK_STOCK ts ").append("    where ").append("        	s.XX_ID = :orgId ")
				.append("        	and s.nj = gsp.grade_id ").append("        	and s.major = gsp.specialty_id ")
				.append("        	and gsp.study_year_code = :studyYearCode ")
				.append("        	and gspb.grade_specialty_plan_id = gsp.id ")
				.append("        	and gspb.textbook_id = t.textbook_id ")
				.append("        	and t.IS_DELETED = 'N' ").append("        	and t.TEXTBOOK_ID not in ")
				.append("        		( ").append("        		 select ")
				.append("        		 		d1.TEXTBOOK_ID ").append("        		 from ")
				.append("        		 		GJT_TEXTBOOK_DISTRIBUTE_DETAIL d1, ")
				.append("        		 		GJT_TEXTBOOK_DISTRIBUTE d2 ").append("        		 where ")
				.append("        		 		d1.DISTRIBUTE_ID = d2.DISTRIBUTE_ID ")
				.append("        		 		and d2.STUDENT_ID = s.STUDENT_ID ")
				.append("        		 		and d2.IS_DELETED = 'N' ").append("        		) ")
				.append("        	and ts.TEXTBOOK_ID = t.TEXTBOOK_ID ").append("    group by ")
				.append("        	t.TEXTBOOK_ID, t.TEXTBOOK_CODE, t.TEXTBOOK_NAME, ts.STOCK_NUM, ts.PLAN_OUT_STOCK_NUM ");

		if (isEnough != null && isEnough) { // 只查询足够库存的
			sb.append("    having ").append("        	ts.STOCK_NUM > ts.PLAN_OUT_STOCK_NUM + count(*) ");
		}

		map.put("orgId", orgId);
		map.put("studyYearCode", studyYearCode);

		return commonDao.queryForObjectListNative(sb.toString(), map);
	}

	/**
	 * 查询学生的当前待发教材列表
	 * 
	 * @param orgId
	 * @param studyYearCode
	 * @return
	 */
	public List<Object[]> queryStudentCurrentDistributeList(String orgId, int studyYearCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append("		 select ").append("        	t.TEXTBOOK_ID as \"textbookId\", ")
				.append("        	t.TEXTBOOK_CODE as \"textbookCode\", ")
				.append("        	t.TEXTBOOK_NAME as \"textbookName\", ").append("        	t.PRICE as \"price\", ")
				.append("        	ts.STOCK_NUM as \"stockNum\", ")
				.append("        	ts.PLAN_OUT_STOCK_NUM as \"palnOutStockNum\", ")
				.append("        	s.STUDENT_ID as \"studentId\" ").append("    from ")
				.append("        	gjt_student_info s, ").append("        	gjt_grade_specialty_plan gsp, ")
				.append("        	gjt_grade_specialty_plan_book gspb, ").append("        	GJT_TEXTBOOK t, ")
				.append("        	GJT_TEXTBOOK_STOCK ts ").append("    where ").append("        	s.XX_ID = :orgId ")
				.append("        	and s.nj = gsp.grade_id ").append("        	and s.major = gsp.specialty_id ")
				.append("        	and gsp.study_year_code = :studyYearCode ")
				.append("        	and gspb.grade_specialty_plan_id = gsp.id ")
				.append("        	and gspb.textbook_id = t.textbook_id ")
				.append("        	and t.IS_DELETED = 'N' ").append("        	and t.TEXTBOOK_ID not in ")
				.append("        		( ").append("        		 select ")
				.append("        		 		d1.TEXTBOOK_ID ").append("        		 from ")
				.append("        		 		GJT_TEXTBOOK_DISTRIBUTE_DETAIL d1, ")
				.append("        		 		GJT_TEXTBOOK_DISTRIBUTE d2 ").append("        		 where ")
				.append("        		 		d1.DISTRIBUTE_ID = d2.DISTRIBUTE_ID ")
				.append("        		 		and d2.STUDENT_ID = s.STUDENT_ID ")
				.append("        		 		and d2.IS_DELETED = 'N' ").append("        		) ")
				.append("        	and ts.TEXTBOOK_ID = t.TEXTBOOK_ID ");

		map.put("orgId", orgId);
		map.put("studyYearCode", studyYearCode);

		return commonDao.queryForObjectListNative(sb.toString(), map);
	}

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年3月30日 下午7:32:45
	 * @param studentId
	 * @param isDeleted
	 * @param status
	 * @return
	 */
	public List<GjtTextbookDistribute> findByStudentIdAndIsDeletedAndStatus(String studentId, String isDeleted,
			int status) {
		return gjtTextbookDistributeRepository.findByStudentIdAndIsDeletedAndStatus(studentId, isDeleted, status);
	}

	public List<GjtTextbookDistribute> findByStudentIdAndIsDeleted(String studentId, String isDeleted) {
		return gjtTextbookDistributeRepository.findByStudentIdAndIsDeleted(studentId, isDeleted);
	}

	@Transactional
	public void updateStatus(String date) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("  update  gjt_textbook_distribute a");
		sql.append("  set  a.status = '3', a.updated_dt=sysdate");
		sql.append("  where a.status = '2'");
		sql.append("  and a.distribution_dt < to_date(:date, 'yyyy-mm-dd')");

		map.put("date", date);
		commonDao.updateForMapNative(sql.toString(), map);
	}

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2018年1月23日 下午5:58:29
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	public Page<Map<String, Object>> queryDistributeList(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select gtd.distribute_id,");
		sql.append("  gtd.student_id,");
		sql.append("  gtd.order_code,");
		sql.append("  gtd.RECEIVER,gtd.MOBILE,gtd.ADDRESS,");
		sql.append("  gsi.xm,");
		sql.append("  gsi.xh,");
		sql.append("  gtd.created_dt,");
		sql.append("  gtd.waybill_code,");
		sql.append("  gtd.status,");
		sql.append("  (gtd.price+nvl(gtd.freight,0)) total_price,");
		sql.append("  gtd.freight,");
		sql.append("  gtd.belong order_type");
		sql.append("  from gjt_student_info gsi, gjt_textbook_distribute gtd");
		sql.append("  left join gjt_grade gg");
		sql.append("  on gg.grade_id = gtd.grade_id");
		sql.append("  and gg.is_deleted = 'N'");
		sql.append("  where gtd.student_id = gsi.student_id");
		sql.append("  and gtd.is_deleted = 'N'");
		sql.append("  and gsi.is_deleted = 'N'");
		sql.append("  and gsi.xx_id = :orgId");
		sql.append("  and gtd.status > 0");
		sql.append("  and exists (select 1 from gjt_textbook_distribute_detail gtdd where gtdd.distribute_id=gtd.distribute_id )");
		params.put("orgId", searchParams.get("orgId"));
		if (StringUtils.isNotBlank((String)searchParams.get("orderCode"))){
			sql.append(" and gtd.order_code=:orderCode");
			params.put("orderCode", searchParams.get("orderCode"));
		}
		if (StringUtils.isNotBlank((String) searchParams.get("studentCode"))) {
			sql.append(" and gsi.xh=:studentCode");
			params.put("studentCode", searchParams.get("studentCode"));
		}
		if (StringUtils.isNotBlank((String) searchParams.get("studentName"))) {
			sql.append(" and gsi.xm like :studentName");
			params.put("studentName", "%" + searchParams.get("studentName") + "%");
		}
		if (StringUtils.isNotBlank((String) searchParams.get("textbookName"))) {
			sql.append(
					" and exists (select 1 from gjt_textbook gt,gjt_textbook_distribute_detail gtdd where gt.textbook_id=gtdd.textbook_id and gtdd.distribute_id=gtd.distribute_id and gt.textbook_name like :textbookName)");
			params.put("textbookName", "%" + searchParams.get("textbookName") + "%");
		}
		if (searchParams.get("status") != null && !"".equals(searchParams.get("status"))) {
			sql.append(" and gtd.status=:status");
			params.put("status", searchParams.get("status"));
		}
		if (StringUtils.isNoneBlank((String) searchParams.get("isBookseller"))) {
			sql.append(" and gtd.belong = 1");
		}
		sql.append(" order by gtd.created_dt desc");

		return commonDao.queryForPageNative(sql.toString(), params, pageRequst);

	}

	public List<Map<String, Object>> queryDistributeDetailList(Map<String, Object> searchParams) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("  select gtdd.quantity    \"quantity\",");
		sql.append("  gtdd.distribute_id \"distributeId\",");
		sql.append("  gtdd.price       \"price\",");
		sql.append("  gt.textbook_name \"textbookName\"");
		sql.append("  from gjt_student_info               gsi,");

		sql.append("  gjt_textbook_distribute_detail gtdd,");
		sql.append("  gjt_textbook                   gt,");
		sql.append("  gjt_textbook_distribute        gtd");
		sql.append("  left join gjt_grade        gg on gg.grade_id=gtd.grade_id");
		sql.append("  where gtd.student_id = gsi.student_id");
		sql.append("  and gtd.distribute_id = gtdd.distribute_id");
		sql.append("  and gtdd.textbook_id = gt.textbook_id");
		sql.append("  and gtd.is_deleted = 'N'");
		sql.append("  and gsi.is_deleted = 'N'");
		sql.append("  and gt.is_deleted = 'N'");
		sql.append("  and gsi.xx_id = :orgId");

		params.put("orgId", searchParams.get("orgId"));
		if (StringUtils.isNotBlank((String) searchParams.get("orderCode"))) {
			sql.append(" and gtd.order_code=:orderCode");
			params.put("orderCode", searchParams.get("orderCode"));
		}
		if (StringUtils.isNotBlank((String) searchParams.get("studentCode"))) {
			sql.append(" and gsi.xh=:studentCode");
			params.put("studentCode", searchParams.get("studentCode"));
		}
		if (StringUtils.isNotBlank((String) searchParams.get("studentName"))) {
			sql.append(" and gsi.xm like :studentName");
			params.put("studentName", "%" + searchParams.get("studentName") + "%");
		}
		if (StringUtils.isNotBlank((String) searchParams.get("textbookName"))) {
			sql.append(
					" and gt.textbook_name like :textbookName)");
			params.put("textbookName", "%" + searchParams.get("textbookName") + "%");
		}
		if (searchParams.get("status") != null && !"".equals(searchParams.get("status"))) {
			sql.append(" and gtd.status=:status");
			params.put("status", searchParams.get("status"));
		}

		if (StringUtils.isNoneBlank((String) searchParams.get("isBookseller"))) {
			sql.append(" and gtd.belong=1");
		}

		return commonDao.queryForMapList(sql.toString(), params);
	}

}
