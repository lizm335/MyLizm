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
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.comm.CommonDao;
import com.gzedu.xlims.dao.textbook.repository.GjtTextbookOrderDetailRepository;
import com.gzedu.xlims.pojo.textbook.GjtTextbookOrderDetail;

@Repository
public class GjtTextbookOrderDetailDao {

	@Autowired
	private GjtTextbookOrderDetailRepository gjtTextbookOrderDetailRepository;

	@Autowired
	private CommonDao commonDao;

	public Page<GjtTextbookOrderDetail> findAll(Specification<GjtTextbookOrderDetail> spec, PageRequest pageRequst) {
		return gjtTextbookOrderDetailRepository.findAll(spec, pageRequst);
	}

	public List<GjtTextbookOrderDetail> findAll(List<String> ids) {
		return (List<GjtTextbookOrderDetail>) gjtTextbookOrderDetailRepository.findAll(ids);
	}

	public GjtTextbookOrderDetail save(GjtTextbookOrderDetail entity) {
		return gjtTextbookOrderDetailRepository.save(entity);
	}

	public void save(List<GjtTextbookOrderDetail> entities) {
		gjtTextbookOrderDetailRepository.save(entities);
	}

	public GjtTextbookOrderDetail findOne(String id) {
		return gjtTextbookOrderDetailRepository.findOne(id);
	}

	public void delete(GjtTextbookOrderDetail entity) {
		gjtTextbookOrderDetailRepository.delete(entity);
	}

	public void delete(List<GjtTextbookOrderDetail> entities) {
		gjtTextbookOrderDetailRepository.delete(entities);
	}

	public void deleteByArrangeId(String arrangeId) {
		gjtTextbookOrderDetailRepository.deleteByArrangeId(arrangeId);
	}

	public List<GjtTextbookOrderDetail> findByStudentIdAndTextbookIdAndStatus(String studentId, String textbookId,
			int status) {
		return gjtTextbookOrderDetailRepository.findByStudentIdAndTextbookIdAndStatus(studentId, textbookId, status);
	}

	public GjtTextbookOrderDetail findByStudentIdAndTextbookIdAndCourseIdAndPlanId(String studentId, String textbookId,
			String courseId, String planId) {
		return gjtTextbookOrderDetailRepository.findByStudentIdAndTextbookIdAndCourseIdAndPlanId(studentId, textbookId,
				courseId, planId);
	}

	public long findByStudentIdAndCourseIdAndGradeIdAndTextbookId(String studentId, String courseId, String gradeId,
			String textbookId) {
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_studentId", studentId);
		searchParams.put("EQ_courseId", courseId);
		searchParams.put("EQ_gradeId", gradeId);
		searchParams.put("EQ_textbookId", textbookId);
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<GjtTextbookOrderDetail> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtTextbookOrderDetail.class);
		return gjtTextbookOrderDetailRepository.count(spec);

		// return
		// gjtTextbookOrderDetailRepository.findByStudentIdAndCourseIdAndGradeIdAndTextbookId(studentId,
		// courseId, gradeId, textbookId);
	}

	public Page<Map<String, Object>> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();

		Object textbookId = searchParams.get("EQ_textbookId");

		sb.append(" select ").append("		gt.TEXTBOOK_ID as \"textbookId\", ")
				.append("		gt.TEXTBOOK_CODE as \"textbookCode\", ")
				.append("		gt.TEXTBOOK_NAME as \"textbookName\", ")
				.append("		gt.TEXTBOOK_TYPE as \"textbookType\", ")
				.append("		gt.DISCOUNT_PRICE as \"price\", ").append("		s.STOCK_NUM as \"stockNum\", ")
				.append("		count(distinct tod.STUDENT_ID) as \"studentNum\", ")
				.append("		count(tod.TEXTBOOK_ID) as \"distributeNum\", ")
				.append("		to_char(wmsys.wm_concat(distinct tod.COURSE_ID)) as \"courseIds\" ");

		if (textbookId != null && StringUtils.isNotBlank(textbookId.toString())) {
			sb.append("	,	gc.KCMC as \"courseName\", ").append("   gc.KCH as \"courseCode\" ");
		}

		sb.append("from ").append("		GJT_TEXTBOOK_ORDER_DETAIL tod, ").append("		GJT_TEXTBOOK gt, ")
				.append("		GJT_TEXTBOOK_STOCK s ");

		if (textbookId != null && StringUtils.isNotBlank(textbookId.toString())) {
			sb.append("	,	GJT_COURSE gc ");
		}

		sb.append("where ").append("		tod.NEED_DISTRIBUTE = 1 ")
				.append("		and tod.TEXTBOOK_ID = gt.TEXTBOOK_ID ")
				.append("		and tod.TEXTBOOK_ID = s.TEXTBOOK_ID ");

		if (textbookId != null && StringUtils.isNotBlank(textbookId.toString())) {
			sb.append("		and tod.COURSE_ID = gc.COURSE_ID ").append("   and tod.TEXTBOOK_ID = :textbookId ");
			map.put("textbookId", textbookId);
		}

		Object orgId = searchParams.get("EQ_orgId");
		if (orgId != null) {
			sb.append(" and gt.ORG_ID = :orgId ");
			map.put("orgId", orgId);
		}

		Object orderId = searchParams.get("EQ_orderId");
		if (orderId != null) {
			sb.append(" and tod.ORDER_ID = :orderId ");
			map.put("orderId", orderId);
		}

		Object textbookCode = searchParams.get("LIKE_textbookCode");
		if (textbookCode != null && StringUtils.isNotBlank(textbookCode.toString())) {
			sb.append(" and gt.TEXTBOOK_CODE like :textbookCode ");
			map.put("textbookCode", "%" + textbookCode + "%");
		}

		Object textbookName = searchParams.get("LIKE_textbookName");
		if (textbookName != null && StringUtils.isNotBlank(textbookName.toString())) {
			sb.append(" and gt.TEXTBOOK_NAME like :textbookName ");
			map.put("textbookName", "%" + textbookName + "%");
		}

		Object textbookType = searchParams.get("EQ_textbookType");
		if (textbookType != null && StringUtils.isNotBlank(textbookType.toString())) {
			sb.append(" and gt.TEXTBOOK_TYPE = :textbookType ");
			map.put("textbookType", textbookType);
		}

		Object courseId = searchParams.get("EQ_courseId");
		if (courseId != null && StringUtils.isNotBlank(courseId.toString())) {
			sb.append(
					" and exists (select 1 from GJT_TEXTBOOK_ORDER_DETAIL ct where ct.ORDER_ID = tod.ORDER_ID and ct.TEXTBOOK_ID = tod.TEXTBOOK_ID and ct.COURSE_ID = :courseId) ");
			map.put("courseId", courseId);
		}

		Object status = searchParams.get("EQ_status");
		if (status != null && StringUtils.isNotBlank(status.toString())) {
			sb.append(" and tod.STATUS = :status ");
			map.put("status", status);
		}

		Object needDistribute = searchParams.get("EQ_needDistribute");
		if (needDistribute != null && StringUtils.isNotBlank(needDistribute.toString())) {
			sb.append(" and tod.NEED_DISTRIBUTE = :needDistribute ");
			map.put("needDistribute", needDistribute);
		}

		Object planId = searchParams.get("EQ_planId");
		if (planId != null && StringUtils.isNotBlank(planId.toString())) {
			sb.append(" and tod.PLAN_ID = :planId ");
			map.put("planId", planId);
		}

		Object gradeId = searchParams.get("EQ_gradeId");
		if (gradeId != null && StringUtils.isNotBlank(gradeId.toString())) {
			sb.append(
					" and exists (select 1 from GJT_STUDENT_INFO gsi where gsi.STUDENT_ID = tod.STUDENT_ID and gsi.NJ = :gradeId) ");
			map.put("gradeId", gradeId);
		}

		Object gradeId2 = searchParams.get("NE_gradeId");
		if (gradeId2 != null && StringUtils.isNotBlank(gradeId2.toString())) {
			sb.append(
					" and exists (select 1 from GJT_STUDENT_INFO gsi where gsi.STUDENT_ID = tod.STUDENT_ID and gsi.NJ != :gradeId2) ");
			map.put("gradeId2", gradeId2);
		}

		sb.append(
				" group by gt.TEXTBOOK_ID, gt.TEXTBOOK_CODE, gt.TEXTBOOK_NAME, gt.TEXTBOOK_TYPE, gt.DISCOUNT_PRICE, s.STOCK_NUM, gt.CREATED_DT ");

		if (textbookId != null && StringUtils.isNotBlank(textbookId.toString())) {
			sb.append("	, gc.KCMC, gc.KCH ");
		}

		Object stockStatus = searchParams.get("EQ_stockStatus");
		if (stockStatus != null && StringUtils.isNotBlank(stockStatus.toString())) {
			if ("0".equals(stockStatus.toString())) { // 需订购
				sb.append("having s.STOCK_NUM < count(tod.TEXTBOOK_ID)");
			} else if ("1".equals(stockStatus.toString())) { // 库存充足
				sb.append("having s.STOCK_NUM >= count(tod.TEXTBOOK_ID)");
			}
		}

		sb.append(" order by gt.CREATED_DT desc");

		return commonDao.queryForPageNative(sb.toString(), map, pageRequst);
	}

	/**
	 * 查询当前待发订单
	 * 
	 * @param planId
	 * @param textbookType
	 * @return
	 */
	public List<Map<String, String>> queryCurrentDistributeList(String planId, int textbookType) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();

		sb.append(" select ").append("		gg.grade_name as \"gradeName\", ")
				.append("		gsi.xh as \"studentCode\", ").append("		gsi.xm as \"studentName\", ")
				.append("		gsc.sc_name as \"scName\", ")
				.append("		(select tsd.name from tbl_sys_data tsd where tsd.is_deleted = 'N' and tsd.type_code = 'TrainingLevel' and tsd.code = gsi.pycc) as \"trainingLevel\", ")
				.append("		gs.zymc as \"specialtyName\", ").append("		gsi.sfzh as \"id\", ")
				.append("		gsi.sjh as \"mobile\", ")
				.append("		to_char(wmsys.wm_concat(gt.textbook_name)) as \"textbookName\", ")
				.append("		to_char(count(gt.textbook_name)) as \"textbookCount\" ").append("from ")
				.append("		gjt_textbook_order_detail tod, ").append("		gjt_textbook gt, ")
				.append("		gjt_student_info gsi ").append("left join ").append("		gjt_grade gg ")
				.append("on ").append("		gg.grade_id = gsi.nj ").append("left join ")
				.append("		gjt_specialty gs ").append("on ").append("		gs.specialty_id = gsi.major ")
				.append("left join ").append("		gjt_study_center gsc ").append("on ")
				.append("		gsc.id = gsi.xxzx_id ").append("where ").append("		tod.status = 3 ")
				.append("		and tod.need_distribute = 1 ").append("		and tod.plan_id = :planId ")
				.append("		and gt.textbook_id = tod.textbook_id ")
				.append("		and gt.textbook_type = :textbookType ")
				.append("		and gsi.student_id = tod.student_id ")
				.append("group by gg.grade_name, gsi.xh, gsi.xm, gsc.sc_name, gsi.pycc, gs.zymc, gsi.sfzh, gsi.sjh ");

		map.put("planId", planId);
		map.put("textbookType", textbookType);

		return commonDao.queryForMapListNative(sb.toString(), map);
	}

	/**
	 * 查询当前待配送订单
	 * 
	 * @param orgId
	 * @return
	 */
	public List<Map<String, String>> queryCurrentDistributeList2(String orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("  select");
		sql.append("  gg.grade_name as \"gradeName\",");
		sql.append("  gsi.xh as \"studentCode\",");
		sql.append("  gsi.xm as \"studentName\",");
		sql.append("  (select tsd.name");
		sql.append("  from tbl_sys_data tsd");
		sql.append("  where tsd.is_deleted = 'N'");
		sql.append("  and tsd.type_code = 'TrainingLevel'");
		sql.append("  and tsd.code = gsi.pycc) as \"trainingLevel\",");
		sql.append("  gs.zymc as \"specialtyName\",");
		sql.append("  gsi.sfzh as \"id\",");
		sql.append("  gsi.sjh as \"mobile\",");
		sql.append("  (select d1.name from gjt_district d1 where d1.id = gsa.province_code) ||");
		sql.append("  (select d2.name from gjt_district d2 where d2.id = gsa.city_code) ||");
		sql.append("  (select d3.name from gjt_district d3 where d3.id = gsa.area_code) ||");
		sql.append("  gsa.address as \"address\",");
		sql.append("  to_char(wmsys.wm_concat(gt.textbook_name)) as \"textbookName\",");
		sql.append("  to_char(count(gt.textbook_name)) as \"textbookCount\",");
		sql.append("  gtd.order_code as \"orderCode\"");
		sql.append("  from gjt_textbook_distribute        gtd,");
		sql.append("  gjt_textbook_distribute_detail gtdd,");
		sql.append("  gjt_textbook                   gt,");
		sql.append("  gjt_student_info gsi");
		sql.append("  left join gjt_grade gg");
		sql.append("  on gg.grade_id = gsi.nj");
		sql.append("  left join gjt_specialty gs");
		sql.append("  on gs.specialty_id = gsi.major");
		sql.append("  left join gjt_student_address gsa");
		sql.append("  on gsa.student_id = gsi.student_id");
		sql.append("  and gsa.is_deleted = 'N'");
		sql.append("  and gsa.is_default = 1");
		sql.append("  where gtd.is_deleted = 'N'");
		sql.append("  and gtd.status = 1");
		sql.append("  and gtdd.distribute_id = gtd.distribute_id");
		sql.append("  and gt.textbook_id = gtdd.textbook_id");
		sql.append("  and gsi.student_id = gtd.student_id");
		sql.append("  and gsi.is_deleted='N'");
		sql.append("  and gsi.org_id in");
		sql.append("  (SELECT org.ID");
		sql.append("  FROM GJT_ORG org");
		sql.append("  WHERE org.IS_DELETED = 'N'");
		sql.append("  START WITH org.ID = :orgId");
		sql.append("  CONNECT BY PRIOR ORG.ID = ORG.PERENT_ID)");
		sql.append("  group by gtd.grade_id,");
		sql.append("  gg.grade_name,");
		sql.append("  gsi.xh,");
		sql.append("  gsi.xm,");
		sql.append("  gsi.pycc,");
		sql.append("  gs.zymc,");
		sql.append("  gsi.sfzh,");
		sql.append("  gsi.sjh,");
		sql.append("  gsa.province_code,");
		sql.append("  gsa.city_code,");
		sql.append("  gsa.area_code,");
		sql.append("  gsa.address,");
		sql.append("  gtd.order_code");

		map.put("orgId", orgId);

		return commonDao.queryForMapListNative(sql.toString(), map);
	}

	@Transactional
	public void updateStatus(String date) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();

		sql.append("  update  gjt_textbook_order_detail b");
		sql.append("  set b.status='7',b.updated_dt=sysdate");
		sql.append("  where b.status = '6'");
		sql.append("  and b.textbook_id in (select d.textbook_id");
		sql.append("  from gjt_textbook_distribute_detail d");
		sql.append("  where d.distribute_id in");
		sql.append("  (select distribute_id ");
		sql.append("  from gjt_textbook_distribute a");
		sql.append("  where a.status = '2'");
		sql.append("  and a.distribution_dt <");
		sql.append("  to_date(:date, 'yyyy-mm-dd')) ");
		sql.append("  )");
		map.put("date", date);

		commonDao.updateForMapNative(sql.toString(), map);
	}

}
