/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.signup;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.signup.GjtRollPlanDao;
import com.gzedu.xlims.daoImpl.RollPlanDaoImpl;
import com.gzedu.xlims.pojo.GjtRollPlan;
import com.gzedu.xlims.service.signup.GjtRollPlanService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

/**
 * 学籍计划业务逻辑<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年05月05日
 * @version 2.5
 * @since JDK 1.7
 */
@Service
public class GjtRollPlanServiceImpl extends BaseServiceImpl<GjtRollPlan> implements GjtRollPlanService {

	private static final Log log = LogFactory.getLog(GjtRollPlanServiceImpl.class);

	@Autowired
	private GjtRollPlanDao gjtRollPlanDao;

	@Autowired
	private RollPlanDaoImpl rollPlanDao;

	@Override
	protected BaseDao<GjtRollPlan, String> getBaseDao() {
		return this.gjtRollPlanDao;
	}

	@Override
	public Map<String, BigDecimal> countGroupbyAuditState(Map<String, Object> searchParams) {
		List<Map> countList = rollPlanDao.countGroupbyAuditState(searchParams);
		Map<String, BigDecimal> countMap = new HashMap<String, BigDecimal>();
		BigDecimal sum = new BigDecimal(0);
		for (Map arr : countList) {
			BigDecimal num = (BigDecimal) arr.get("PLAN_NUM");
			countMap.put(arr.get("AUDIT_STATE").toString(), num);
			sum = sum.add(num);
		}
		countMap.put("", sum);
		return countMap;
	}

	@Override
	public boolean updateRollPlan(GjtRollPlan entity) {
		entity.setUpdatedDt(new Date());
		gjtRollPlanDao.save(entity);
		return true;
	}

	@Override
	public boolean auditRollPlan(String id, BigDecimal auditState, String auditContent, String auditOperator, String updatedBy) {
		Date now = new Date();
		gjtRollPlanDao.auditRollPlan(id, auditState, auditContent, auditOperator, updatedBy, now);
		return true;
	}

	@Override
	public List<GjtRollPlan> findTermRollPlanList(String xxId, String gradeId) {
		// TODO Auto-generated method stub
		return gjtRollPlanDao.findTermRollPlanList(xxId,gradeId);
	}

}
