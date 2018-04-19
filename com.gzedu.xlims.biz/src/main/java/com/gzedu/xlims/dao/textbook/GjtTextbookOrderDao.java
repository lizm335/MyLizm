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
import com.gzedu.xlims.dao.textbook.repository.GjtTextbookOrderRepository;
import com.gzedu.xlims.pojo.textbook.GjtTextbookOrder;

@Repository
public class GjtTextbookOrderDao {

	@Autowired
	private GjtTextbookOrderRepository gjtTextbookOrderRepository;

	@Autowired
	private CommonDao commonDao;
	
	public Page<GjtTextbookOrder> findAll(Specification<GjtTextbookOrder> spec, PageRequest pageRequst) {
		return gjtTextbookOrderRepository.findAll(spec, pageRequst);
	}
	
	public GjtTextbookOrder save(GjtTextbookOrder entity) {
		return gjtTextbookOrderRepository.save(entity);
	}
	
	public GjtTextbookOrder findOne(String id) {
		return gjtTextbookOrderRepository.findOne(id);
	}
	
	public GjtTextbookOrder findByPlanIdAndStatus(String planId, int status) {
		return gjtTextbookOrderRepository.findByPlanIdAndStatus(planId, status);
	}
	
	public void delete(GjtTextbookOrder entity) {
		gjtTextbookOrderRepository.delete(entity);
	}
	
	public Page<Map<String, Object>> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ")
			.append("		gg.GRADE_NAME as \"gradeName\", ")
			.append("		tp.PLAN_CODE as \"planCode\", ")
			.append("		tp.PLAN_NAME as \"planName\", ")
			.append("		o.ORDER_ID as \"orderId\", ")
			.append("		o.DISTRIBUTE_NUM as \"distributeNum1\", ")
			.append("		o.ORDER_NUM as \"orderNum1\", ")
			.append("		o.ORDER_PRICE as \"orderPrice1\", ")
			.append("		o.STATUS as \"status\", ")
			.append("		o.REASON as \"reason\", ")
			.append("		o.UPDATED_DT as \"updatedDt\", ")
			.append("		count(distinct tod.COURSE_ID) as \"courseNum\", ")
			.append("		count(distinct tod.STUDENT_ID) as \"studentNum\", ")
			.append("		count(tod.TEXTBOOK_ID) as \"distributeNum2\" ")
			.append("from ")
			.append("		GJT_TEXTBOOK_ORDER_DETAIL tod, ")
			.append("		GJT_GRADE gg, ")
			.append("		GJT_TEXTBOOK_PLAN tp, ")
			.append("		GJT_TEXTBOOK_ORDER o ")
			.append("where ")
			.append("		tod.NEED_DISTRIBUTE = 1 ")
			.append("		and tod.GRADE_ID = gg.GRADE_ID ")
			.append("		and tod.PLAN_ID = tp.PLAN_ID ")
			.append("		and tod.ORDER_ID = o.ORDER_ID ");
		
		Object orgId = searchParams.get("EQ_orgId");
		if (orgId != null) {
			sb.append(" and tp.ORG_ID = :orgId ");
			map.put("orgId", orgId);
		}
		
		Object orderId = searchParams.get("EQ_orderId");
		if (orderId != null) {
			sb.append(" and tod.ORDER_ID = :orderId ");
			map.put("orderId", orderId);
		}

		Object gradeId = searchParams.get("EQ_gradeId");
		if (gradeId != null && StringUtils.isNotBlank(gradeId.toString())) {
			sb.append(" and tod.GRADE_ID = :gradeId ");
			map.put("gradeId", gradeId);
		}

		Object planId = searchParams.get("EQ_planId");
		if (planId != null && StringUtils.isNotBlank(planId.toString())) {
			sb.append(" and tod.PLAN_ID = :planId ");
			map.put("planId", planId);
		}

		Object status = searchParams.get("EQ_status");
		if (status != null && StringUtils.isNotBlank(status.toString())) {
			sb.append(" and tod.status = :status ");
			map.put("status", status);
		}
		
		sb.append(" group by gg.GRADE_NAME, tp.PLAN_CODE, tp.PLAN_NAME, o.ORDER_ID, o.DISTRIBUTE_NUM, o.ORDER_NUM, o.ORDER_PRICE, o.STATUS, o.REASON, o.UPDATED_DT, o.CREATED_DT ")
			.append(" order by o.CREATED_DT desc");
		
		return commonDao.queryForPageNative(sb.toString(), map, pageRequst);
	}
	
	public Map<String, Object> findNeedOrderNumAndPrice(String orderId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select ")
			.append("		sum(t.NUM - s.STOCK_NUM) as \"num\", ")
			.append("		sum((t.NUM - s.STOCK_NUM) * gt.DISCOUNT_PRICE) as \"price\" ")
			.append("from ")
			.append("		GJT_TEXTBOOK_STOCK s ")
			.append("inner join ( ")
			.append("			select ")
			.append("				tod.TEXTBOOK_ID, ")
			.append("				count(1) as NUM ")
			.append("			from ")
			.append("				GJT_TEXTBOOK_ORDER_DETAIL tod ")
			.append("			where ")
			.append("				tod.NEED_DISTRIBUTE = 1 ")
			.append("				and tod.ORDER_ID = :orderId ")
			.append("			group by tod.TEXTBOOK_ID ")
			.append("			) t ")
			.append("on s.TEXTBOOK_ID = t.TEXTBOOK_ID and t.NUM > s.STOCK_NUM ")
			.append("inner join ")
			.append("		 GJT_TEXTBOOK gt ")
			.append("on s.TEXTBOOK_ID = gt.TEXTBOOK_ID");
		
		map.put("orderId", orderId);
		
		return commonDao.queryObjectToMapNative(sb.toString(), map);
	}
	
	public List<GjtTextbookOrder> findByStatusAndOrgId(int status, String orgId) {
		return gjtTextbookOrderRepository.findByStatusAndOrgId(status, orgId);
	}

}
