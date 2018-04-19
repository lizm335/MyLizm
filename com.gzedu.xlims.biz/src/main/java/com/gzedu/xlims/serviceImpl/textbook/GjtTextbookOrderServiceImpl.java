package com.gzedu.xlims.serviceImpl.textbook;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.textbook.GjtTextbookOrderDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookOrderDetailDao;
import com.gzedu.xlims.pojo.textbook.GjtTextbookOrder;
import com.gzedu.xlims.pojo.textbook.GjtTextbookOrderDetail;
import com.gzedu.xlims.service.textbook.GjtTextbookOrderService;

@Service
public class GjtTextbookOrderServiceImpl implements GjtTextbookOrderService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookOrderServiceImpl.class);
	
	@Autowired
	private GjtTextbookOrderDao gjtTextbookOrderDao;
	
	@Autowired
	private GjtTextbookOrderDetailDao gjtTextbookOrderDetailDao;

	@Override
	public Page<Map<String, Object>> findAll(Map<String, Object> searchParams, PageRequest pageRequst) {
		log.info("searchParams:[" + searchParams + "], pageRequst:[" + pageRequst + "]");
		//Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		//Specification<GjtTextbookOrder> spec = DynamicSpecifications.bySearchFilter(filters.values(), GjtTextbookOrder.class);
		return gjtTextbookOrderDao.findAll(searchParams, pageRequst);
	}

	@Override
	public Map<String, Object> findNeedOrderNumAndPrice(String orderId) {
		return gjtTextbookOrderDao.findNeedOrderNumAndPrice(orderId);
	}

	@Override
	@Transactional
	public GjtTextbookOrder insert(GjtTextbookOrder entity) {
		log.info("entity:[" + entity + "]");
		entity.setOrderId(UUIDUtils.random());
		entity.setStatus(0);
		entity.setCreatedDt(new Date());
		
		GjtTextbookOrder textbookOrder = gjtTextbookOrderDao.save(entity);
		List<GjtTextbookOrderDetail> textbookOrderDetails = entity.getGjtTextbookOrderDetails();
		if (textbookOrderDetails != null && textbookOrderDetails.size() > 0) {
			for (GjtTextbookOrderDetail textbookOrderDetail : textbookOrderDetails) {
				textbookOrderDetail.setDetailId(UUIDUtils.random());
				textbookOrderDetail.setGjtTextbookOrder(entity);
				textbookOrderDetail.setPlanId(entity.getPlanId());
				textbookOrderDetail.setNeedDistribute(1);
				textbookOrderDetail.setStatus(0);
				textbookOrderDetail.setCreatedDt(new Date());
			}
			
			gjtTextbookOrderDetailDao.save(textbookOrderDetails);
		}
		
		return textbookOrder;
	}

	@Override
	@Transactional
	public void update(GjtTextbookOrder entity) {
		log.info("entity:[" + entity + "]");
		entity.setUpdatedDt(new Date());
		gjtTextbookOrderDao.save(entity);
		
		List<GjtTextbookOrderDetail> textbookOrderDetails = entity.getGjtTextbookOrderDetails();
		if (textbookOrderDetails != null && textbookOrderDetails.size() > 0) {
			gjtTextbookOrderDetailDao.save(textbookOrderDetails);
		}
	}

	@Override
	public GjtTextbookOrder findOne(String id) {
		return gjtTextbookOrderDao.findOne(id);
	}

	@Override
	public GjtTextbookOrder findByPlanId(String planId) {
		return gjtTextbookOrderDao.findByPlanIdAndStatus(planId, 0);
	}

	@Override
	public void delete(GjtTextbookOrder entity) {
		gjtTextbookOrderDao.delete(entity);
	}

}
