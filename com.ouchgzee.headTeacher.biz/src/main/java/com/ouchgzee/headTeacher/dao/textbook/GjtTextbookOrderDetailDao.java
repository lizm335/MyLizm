package com.ouchgzee.headTeacher.dao.textbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import com.ouchgzee.headTeacher.dao.comm.CommonDao;
import com.ouchgzee.headTeacher.dao.textbook.repository.GjtTextbookOrderDetailRepository;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookOrderDetail;

@Deprecated @Repository("bzrGjtTextbookOrderDetailDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookOrderDetailDao {

	@Autowired
	private GjtTextbookOrderDetailRepository gjtTextbookOrderDetailRepository;

	@Autowired
	private CommonDao commonDao;

	public Page<BzrGjtTextbookOrderDetail> findAll(Specification<BzrGjtTextbookOrderDetail> spec, PageRequest pageRequst) {
		return gjtTextbookOrderDetailRepository.findAll(spec, pageRequst);
	}

	public List<BzrGjtTextbookOrderDetail> findAll(List<String> ids) {
		return (List<BzrGjtTextbookOrderDetail>) gjtTextbookOrderDetailRepository.findAll(ids);
	}

	public BzrGjtTextbookOrderDetail save(BzrGjtTextbookOrderDetail entity) {
		return gjtTextbookOrderDetailRepository.save(entity);
	}

	public void save(List<BzrGjtTextbookOrderDetail> entities) {
		gjtTextbookOrderDetailRepository.save(entities);
	}

	public BzrGjtTextbookOrderDetail findOne(String id) {
		return gjtTextbookOrderDetailRepository.findOne(id);
	}

	public void delete(BzrGjtTextbookOrderDetail entity) {
		gjtTextbookOrderDetailRepository.delete(entity);
	}

	public void delete(List<BzrGjtTextbookOrderDetail> entities) {
		gjtTextbookOrderDetailRepository.delete(entities);
	}

	public void deleteByArrangeId(String arrangeId) {
		gjtTextbookOrderDetailRepository.deleteByArrangeId(arrangeId);
	}

	public List<BzrGjtTextbookOrderDetail> findByStudentIdAndTextbookIdAndStatus(String studentId, String textbookId,
																				 int status) {
		return gjtTextbookOrderDetailRepository.findByStudentIdAndTextbookIdAndStatus(studentId, textbookId, status);
	}

	public BzrGjtTextbookOrderDetail findByStudentIdAndTextbookIdAndCourseIdAndPlanId(String studentId, String textbookId,
																					  String courseId, String planId) {
		return gjtTextbookOrderDetailRepository.findByStudentIdAndTextbookIdAndCourseIdAndPlanId(studentId, textbookId,
				courseId, planId);
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
				.append("		count(tod.TEXTBOOK_ID) as \"distributeNum\" ");

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
					" and exists (select 1 from GJT_COURSE_TEXTBOOK ct where ct.TEXTBOOK_ID = tod.TEXTBOOK_ID and ct.COURSE_ID = :courseId) ");
			map.put("courseId", courseId);
		}

		Object status = searchParams.get("EQ_status");
		if (status != null && StringUtils.isNotBlank(status.toString())) {
			sb.append(" and tod.STATUS = :status ");
			map.put("status", status);
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

}
